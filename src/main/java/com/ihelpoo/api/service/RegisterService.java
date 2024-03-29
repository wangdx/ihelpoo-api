package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.GenericResult;
import com.ihelpoo.api.model.SMSCodeResult;
import com.ihelpoo.api.model.common.User;
import com.ihelpoo.api.model.entity.IUserLoginWbEntity;
import com.ihelpoo.api.model.obj.Result;
import com.ihelpoo.api.model.entity.IUserLoginEntity;
import com.ihelpoo.api.service.base.RecordService;
import com.ihelpoo.common.Constant;
import com.ihelpoo.common.util.ID;
import com.ihelpoo.common.util.MD5;
import com.ihelpoo.common.util.Rand;
import com.ihelpoo.sms.SMSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * @author: echowdx@gmail.com
 */
@Service
public class RegisterService extends RecordService {

    private static final String MOB_REGISTER = "Register:";
    private static final String REGISTER_COUNT = ":CountDuringDay";
    private static final String REGISTER_CODE = ":Code";
    public static final int ONE_DAY = 3600 * 24;
    public static final int QUOTA_SMS = 202;
    public static final String LOGINWEIBO_COM = "@loginweibo.com";
    public static final String LOGINQQ_COM = "@loginqq.com";
    public static final int RANDOM_MIN = 10000000;
    public static final int RANDOM_MAX = 99999999;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserDao userDao;

    @Autowired
    LoginService loginService;

    @Autowired
    TweetService tweetService;

    @Autowired
    WordService wordService;

    /**
     * 获取短信验证码，24小时内有效
     *
     * @param mobile
     * @return
     */

    public SMSCodeResult fetchSMSCode(String mobile) {
        logger.info("Fetching sms code for registering");
        SMSCodeResult codeResult = new SMSCodeResult();
        Result result = new Result();
        try {
            userDao.findByAccount(mobile);
            result.setErrorCode("-" + mobile);
            result.setErrorMessage("该手机号已被注册，请直接登录或找回密码");
            codeResult.result = result;
            return codeResult;
        } catch (EmptyResultDataAccessException e) {
        } catch (Exception e) {
            result.setErrorCode("0");
            result.setErrorMessage("系统错误，注册失败：" + e.getMessage());
            codeResult.result = result;
            return codeResult;
        }


        String code = Rand.INSTANCE.nextString();

        Jedis jedis = new Jedis(Constant.REDIS_HOST);
        String countKey = MOB_REGISTER + mobile + REGISTER_COUNT;
        String codeKey = MOB_REGISTER + mobile + REGISTER_CODE;
        jedis.set(codeKey, code);
        jedis.expire(codeKey, ONE_DAY);
        Long count = jedis.incr(countKey);
        if (count > 5L) {
            result.setErrorCode("-5");
            result.setErrorMessage("该号码最近24小时内验证次数已超出系统限额，请明天再试或联系站长注册帐号，对此给您带来的不便，敬请谅解！");
            codeResult.result = result;
            return codeResult;
        } else if (count == 1) {
            jedis.expire(countKey, ONE_DAY);
        }
        jedis.disconnect();


        result.setErrorCode("1");
        result.setErrorMessage("Succeed");
        codeResult.result = result;
        SMSClient smsClient = new SMSClient();
        String codeContent = code + "（我帮圈圈手机注册验证码，请24小时内完成注册），如非本人操作请忽略；本条免费。【我帮圈圈】";
        String balance = smsClient.getBalance();
        if (balance != null && (balance.contains("-") || balance.matches("\\d+") && Integer.parseInt(balance) < QUOTA_SMS)) {
            logger.warn("短信余额不多了（" + balance + "条），请相关人员对短信平台进行充值。");
            smsClient.send("18603041303,15007133699", "短信配额已经低于100，请相关人员对短信平台进行充值。");
        }
        logger.info(mobile + "->这次的短信验证码为:" + code);
        smsClient.send(mobile, codeContent);
        codeResult.code = code;
        return codeResult;
    }


    //TODO other things besides register
    @Transactional
    public GenericResult register(String code, String mobile, String pwd, Integer school, String ip, String deviceType) {
        logger.info("sms register...");

        ip = ip == null ? "0.0.0.0" : ip;
        deviceType = deviceType == null ? "圈圈App" : deviceType;

        GenericResult genericResult = new GenericResult();
        Result result = new Result();
        try {
            userDao.findByAccount(mobile);
            result.setErrorCode("-" + mobile);
            result.setErrorMessage("该手机号已被注册，请直接登录或找回密码");
            genericResult.setResult(result);
            return genericResult;
        } catch (EmptyResultDataAccessException e) {
            // empty, continue to register
        } catch (Exception e) {
            result.setErrorCode("0");
            result.setErrorMessage("系统错误，注册失败：" + e.getMessage());
            genericResult.setResult(result);
            return genericResult;
        }

        Jedis jedis = new Jedis(Constant.REDIS_HOST);
        String codeInRedis = jedis.get(MOB_REGISTER + mobile + REGISTER_CODE);
        if (codeInRedis != null && codeInRedis.equals(code)) {
            // correct, continue
        } else {
            result.setErrorCode("-4");
            result.setErrorMessage("验证码不正确或已失效");
            genericResult.setResult(result);
            return genericResult;
        }
        jedis.disconnect();

        int t = (int) (System.currentTimeMillis() / 1000L);
        final String nickname = "oih_" + ID.INSTANCE.next();
        IUserLoginEntity userLoginEntity = userDao.saveUser(mobile, new MD5().encrypt(pwd), nickname, school, t);
        DefaultMajor defaultMajor = userDao.fetchDefaultMajor(school);
        userDao.saveUserInfo(userLoginEntity.getUid(), defaultMajor.getAcademyId(), defaultMajor.getMajorId(), defaultMajor.getDormId(), "");

        userDao.saveStatus(userLoginEntity.getUid(), 6);

        final long noticeId = ID.INSTANCE.next();
        streamDao.saveNotice(10000, "system/welcome", 0, noticeId);
        deliverTo(userLoginEntity.getUid(), noticeId);
        bounceNoticeMessageCount(userLoginEntity.getUid(), 1);

        tweetService.pubTweet(userLoginEntity.getUid(), 0L, "我刚刚加入了我帮圈圈:)", null, null, deviceType, school);

        loginService.syncUserStatus(userLoginEntity, "1", ip);
        result.setErrorCode("1");
        result.setErrorMessage("注册成功");
        User user = new User();
        user.uid = userLoginEntity.getUid();
        user.nickname = nickname;
        user.school_id = String.valueOf(school);
        user.avatar_url = convertToAvatarUrl("", userLoginEntity.getUid(), false);
        genericResult.setUser(user);
        genericResult.setResult(result);
        return genericResult;
    }

    public GenericResult thirdLogin(String thirdUid, Integer schoolId, String ip, String deviceType, String thirdNickname, String thirdType, String status) {
        logger.info("sms register...");

        ip = ip == null ? "0.0.0.0" : ip;
        deviceType = deviceType == null ? "圈圈App" : deviceType;

        GenericResult genericResult = new GenericResult();
        Result result = new Result();
        try {
            return thirdDirectLogin(thirdUid, ip, thirdType, status);
        } catch (IncorrectResultSizeDataAccessException e) {// FIXME if the amount of tuple with empty email is 1, there should be a bug....
            try {
                return toRegisterAndLogin(thirdUid, schoolId, ip, deviceType, thirdNickname, thirdType);
            } catch (Exception e1) {
                result.setErrorCode("0");
                result.setErrorMessage("系统错误，登录失败：" + e.getMessage());
                genericResult.setResult(result);
                return genericResult;
            }
        } catch (Exception e) {
            result.setErrorCode("0");
            result.setErrorMessage("系统错误，登录失败：" + e.getMessage());
            genericResult.setResult(result);
            return genericResult;
        }
    }

    private GenericResult thirdDirectLogin(String thirdUid, String ip, String thirdType, String status) {
        IUserLoginWbEntity entity = userDao.findByThirdAccount(thirdUid, thirdType);
        return loginService.thirdLogin(entity.getUid(), status, ip);
    }

    private GenericResult toRegisterAndLogin(String thirdUid, Integer schoolId, String ip, String deviceType, String thirdNickname, String thirdType) throws Exception {
        String email = thirdUid + getSuffixOfAccount(thirdType);
        String password = String.valueOf(new Random().nextInt(RANDOM_MAX - RANDOM_MIN) + RANDOM_MIN);
        String encryptedPwd = new MD5().encrypt(password);
        String nickname = thirdNickname;
        int t = (int) (System.currentTimeMillis() / 1000L);
        IUserLoginEntity userLoginEntity = userDao.saveUser(email, encryptedPwd, nickname, schoolId, t);
        logger.info("->>> Generate random password for user [" + userLoginEntity.getUid() + "] " + email + " is:" + password);
        userDao.saveUserThird(thirdUid, userLoginEntity.getUid(), thirdType);
        DefaultMajor defaultMajor = userDao.fetchDefaultMajor(schoolId);
        userDao.saveUserInfo(userLoginEntity.getUid(), defaultMajor.getAcademyId(), defaultMajor.getMajorId(), defaultMajor.getDormId(), "http://weibo.com/" + thirdUid);
        userDao.saveStatus(userLoginEntity.getUid(), 6);
        wordService.doChat(10000, userLoginEntity.getUid(), "系统为您自动分配了我帮圈圈登录账号:" + email + " 密码为:" + password + "。为方便保证独立账号登录，希望您能及时重新设置账号和密码:)", null);
        int sid = tweetService.pubTweet(userLoginEntity.getUid(), 0L, "我刚刚通过" + getThirdName(thirdType) + "登录加入了我帮圈圈:)", null, null, deviceType, schoolId);
        userDao.addDynamic(sid, "join");
        return loginService.succeedToLogin(userLoginEntity);//First Time is register , not login
    }

    private String getThirdName(String thirdType) {
        return "weibo".equals(thirdType) ? "微博" : "QQ";
    }

    private String getSuffixOfAccount(String thirdType) {
        return "weibo".equals(thirdType) ? LOGINWEIBO_COM : LOGINQQ_COM;
    }

    public static class DefaultMajor {
        private int schoolId;
        private int academyId;
        private int majorId;
        private int dormId;

        public int getAcademyId() {
            return academyId;
        }

        public void setAcademyId(int academyId) {
            this.academyId = academyId;
        }

        public int getMajorId() {
            return majorId;
        }

        public void setMajorId(int majorId) {
            this.majorId = majorId;
        }

        public int getDormId() {
            return dormId;
        }

        public void setDormId(int dormId) {
            this.dormId = dormId;
        }

        public int getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(int schoolId) {
            this.schoolId = schoolId;
        }
    }
}
