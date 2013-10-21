package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.GenericResult;
import com.ihelpoo.api.model.SMSCodeResult;
import com.ihelpoo.api.model.base.Result;
import com.ihelpoo.api.model.base.User;
import com.ihelpoo.common.Constant;
import com.ihelpoo.common.util.ID;
import com.ihelpoo.common.util.MD5;
import com.ihelpoo.common.util.Rand;
import com.ihelpoo.sms.SMSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * @author: echowdx@gmail.com
 */
@Service
public class RegisterService {

    private static final String MOB_REGISTER = "Register:";
    private static final String REGISTER_COUNT = ":CountDuringDay";
    private static final String REGISTER_CODE = ":Code";
    public static final int ONE_DAY = 3600*24;
    public static final int QUOTA_SMS = 202;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserDao userDao;

    /**
     * 获取短信验证码，24小时内有效
     * @param mobile
     * @return
     */

    public SMSCodeResult fetchSMSCode(String mobile) {
        logger.info("Fetching sms code for registering");
        SMSCodeResult codeResult = new SMSCodeResult();
        Result result = new Result();
        try {
            userDao.findByUserName(mobile);
            result.setErrorCode("-" + mobile);
            result.setErrorMessage("该手机号已被注册，请直接登录或找回密码");
            codeResult.result = result;
            return codeResult;
        } catch (EmptyResultDataAccessException e) {
        } catch (Exception e) {
            result.setErrorCode("0");
            result.setErrorMessage("系统错误，注册失败："+ e.getMessage());
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
        smsClient.send(mobile, codeContent);
        codeResult.code = code;
        return codeResult;
    }

    public GenericResult register(String code, String mobile, String pwd, Integer school) {
        logger.info("sms register...");

        GenericResult genericResult = new GenericResult();
        Result result = new Result();
        try {
            userDao.findByUserName(mobile);
            result.setErrorCode("-" + mobile);
            result.setErrorMessage("该手机号已被注册，请直接登录或找回密码");
            genericResult.setResult(result);
            return genericResult;
        } catch (EmptyResultDataAccessException e) {
            // empty, continue to register
        } catch (Exception e) {
            result.setErrorCode("0");
            result.setErrorMessage("系统错误，注册失败："+ e.getMessage());
            genericResult.setResult(result);
            return genericResult;
        }

        Jedis jedis = new Jedis(Constant.REDIS_HOST);
        String codeInRedis = jedis.get(MOB_REGISTER + mobile + REGISTER_CODE);
        if(codeInRedis != null && codeInRedis.equals(code)){
            // correct, continue
        } else {
            result.setErrorCode("-4");
            result.setErrorMessage("验证码不正确或已失效");
            genericResult.setResult(result);
            return genericResult;
        }
        jedis.disconnect();

        final String nickname = "oih_" + ID.INSTANCE.next();
        int uid = userDao.saveUser(mobile, new MD5().encrypt(pwd), nickname, school, "");
        result.setErrorCode("1");
        result.setErrorMessage("注册成功");
        User user = new User();
        user.setUid(uid);
        user.setName(nickname);
        user.setSchoolId(String.valueOf(school));
        genericResult.setUser(user);
        genericResult.setResult(result);
        return genericResult;
    }
}