package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.GenericResult;
import com.ihelpoo.api.model.common.User;
import com.ihelpoo.api.model.entity.IUserLoginEntity;
import com.ihelpoo.api.model.entity.IUserStatusEntity;
import com.ihelpoo.api.model.obj.Result;
import com.ihelpoo.api.service.base.RecordService;
import com.ihelpoo.common.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class LoginService extends RecordService {

    public static final String FAILURE = "0";
    public static final String SUCCESS = "1";
    public static final String MSG_ERR_USERNAME_OR_PWD = "用户名或密码错误";
    public static final String MSG_ERR_SYNC_STATUS = "同步出错，请重试";
    public static final String MSG_SUC_LOGIN = "登录成功";

    @Autowired
    UserDao userDao;
    @Autowired
    MD5 md5;

    public GenericResult login(String username, String pwd, String status, String ip, boolean isThirdLogin) {
        GenericResult genericResult = new GenericResult();
        IUserLoginEntity userLoginEntity = null;
        try {
            userLoginEntity = userDao.findByAccount(username);
        } catch (EmptyResultDataAccessException e) {
            genericResult.setResult(new Result(FAILURE, MSG_ERR_USERNAME_OR_PWD));
            return genericResult;
        } catch (Exception e) {
            genericResult.setResult(new Result(FAILURE, e.getMessage()));
            return genericResult;
        }

        if(!isThirdLogin){
            if (!userLoginEntity.getPassword().equals(md5.encrypt(pwd))) {
                genericResult.setResult(new Result(FAILURE, MSG_ERR_USERNAME_OR_PWD));
                return genericResult;
            }
        }


        if (syncUserStatus(userLoginEntity, status, ip) < 1) {
            genericResult.setResult(new Result(FAILURE, MSG_ERR_SYNC_STATUS));
            return genericResult;
        }


        return succeedToLogin(userLoginEntity);
    }


    public GenericResult thirdLogin(int uid, String status, String ip) {
        GenericResult genericResult = new GenericResult();
        IUserLoginEntity userLoginEntity = null;
        try {
            userLoginEntity = userDao.findUserById(uid);
        } catch (IncorrectResultSizeDataAccessException e) {
            genericResult.setResult(new Result(FAILURE, MSG_ERR_USERNAME_OR_PWD));
            return genericResult;
        } catch (Exception e) {
            genericResult.setResult(new Result(FAILURE, e.getMessage()));
            return genericResult;
        }

        if (syncUserStatus(userLoginEntity, status, ip) < 1) {
            genericResult.setResult(new Result(FAILURE, MSG_ERR_SYNC_STATUS));
            return genericResult;
        }

        return succeedToLogin(userLoginEntity);
    }


    /**
     * @param userLoginEntity
     * @param status
     * @param ip
     * @return 1 success, 0 fail, -1 system error
     */
    @Transactional
    public int syncUserStatus(IUserLoginEntity userLoginEntity, String status, String ip) {
        long nowMillis = System.currentTimeMillis();

        final int uid = userLoginEntity.getUid();
        final long loginTime = nowMillis / 1000L;
        final Integer lastLoginTime = mayUpdateLastLoginTime(userLoginEntity);

        IUserStatusEntity iUserStatusEntity = userDao.findUserStatusById(uid);

        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long) lastLoginTime * 1000);
        int lastDay = calendar.get(Calendar.DAY_OF_YEAR);

        int timeIntervalType = getTimeInterval(today, lastDay);

        if (!("1".equals(iUserStatusEntity.getActiveFlag()) && 0 == timeIntervalType)) {

            int newUserActive = userLoginEntity.getActive() == null ? 0 : userLoginEntity.getActive();
            int activeFlag = 0;
            int hourRules = 0;
            int dayRules = 0;

            int total = 0;
            int change = 0;
            iUserStatusEntity.getTotalActiveTi();
            String reason = "";


            if (iUserStatusEntity.getTotalActiveTi() != null) {
                if (iUserStatusEntity.getTotalActiveTi() > 7200) {
                    total = newUserActive;
                    change = 10;
                    reason = "上次在线时长 " + Math.floor(iUserStatusEntity.getTotalActiveTi() / 60) + "min 大于两小时";
                    newUserActive = newUserActive + 10;
                    activeFlag = 1;
                    hourRules = 1;
                } else if (iUserStatusEntity.getTotalActiveTi() > 1800) {
                    total = newUserActive;
                    change = 5;
                    reason = "上次在线时长 " + Math.floor(iUserStatusEntity.getTotalActiveTi() / 60) + "min 大于半小时";
                    newUserActive = newUserActive + 5;
                    activeFlag = 1;
                    hourRules = 1;

                }

            }
            if (hourRules == 1) {
                userDao.updateActive(uid, newUserActive);
                userDao.saveMsgActive("add", userLoginEntity.getUid(), total, change, reason);
            }
            if (1 == timeIntervalType && userLoginEntity.getLoginDaysCo() != null) {

                switch (userLoginEntity.getLoginDaysCo()) {
                    case 3:
                        change = 5;
                        break;
                    case 5:
                        change = 10;
                        break;
                    case 10:
                        change = 25;
                        break;
                    case 20:
                        change = 50;
                        break;
                    default:
                        change = 2;
                }
                if (userLoginEntity.getLoginDaysCo() > 20 && userLoginEntity.getLoginDaysCo() % 10 == 0) {
                    change = 75;
                }
                reason = "连续登录" + userLoginEntity.getLoginDaysCo() + "天";
                activeFlag = 1;
                total = newUserActive;
                newUserActive = newUserActive + change;
                dayRules = 1;

                userDao.updateLogin(uid, newUserActive, userLoginEntity.getLoginDaysCo() + 1);
                userDao.updateStatus(uid, activeFlag, 0);

            } else if (-1 == timeIntervalType) {
                userDao.updateLogin(uid, newUserActive, 1);
                userDao.updateStatus(uid, activeFlag, 0);
            } else {
                userDao.updateLogin(uid, newUserActive, userLoginEntity.getLoginDaysCo() == null ? 1 : userLoginEntity.getLoginDaysCo());
                userDao.updateStatus(uid, activeFlag, 1);
            }

            if (dayRules > 0) {
                userDao.saveMsgActive("add", uid, total, change, reason);
            }
        }

        return userDao.updateLogin(ip, loginTime, status, lastLoginTime, uid);
    }

    private int getTimeInterval(int today, int lastDay) {
        int timeIntervalType = -1;//more than one day
        if ((lastDay + 1 == today) || (lastDay - getDaysOfLastYear() == today)) {
            timeIntervalType = 1;// one day
        } else if (lastDay == today) {
            timeIntervalType = 0;// same day
        }
        return timeIntervalType;
    }

    private int getDaysOfLastYear() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        boolean isLastYearLeap = gregorianCalendar.isLeapYear(gregorianCalendar.get(Calendar.DAY_OF_YEAR) - 1);
        return isLastYearLeap ? 366 : 365;
    }

    private Integer mayUpdateLastLoginTime(IUserLoginEntity userLoginEntity) {
        Calendar calendar = Calendar.getInstance();
        int todayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        if (!isLoginFirstTime(userLoginEntity)) {
            calendar.setTimeInMillis((long) userLoginEntity.getLastlogintime() * 1000);
        }
        int latestLoginDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        if (latestLoginDayOfYear != todayOfYear || isLoginFirstTime(userLoginEntity)) {
            userLoginEntity.setLastlogintime(userLoginEntity.getLogintime());
        }
        return userLoginEntity.getLastlogintime();
    }

    private boolean isLoginFirstTime(IUserLoginEntity userLoginEntity) {
        return userLoginEntity.getLastlogintime() == null;
    }

    public GenericResult succeedToLogin(IUserLoginEntity userLoginEntity) {
        GenericResult genericResult = new GenericResult();
        User user = new User();
        user.uid = userLoginEntity.getUid();
        user.school_id = String.valueOf(userLoginEntity.getSchool());
        user.nickname = userLoginEntity.getNickname();
        user.email = userLoginEntity.getEmail();
        user.avatar_url = convertToAvatarUrl(userLoginEntity.getIconUrl(), userLoginEntity.getUid(), false);
        genericResult.setUser(user);
        genericResult.setResult(new Result(SUCCESS, MSG_SUC_LOGIN));
        genericResult.setNotice(getNotice(userLoginEntity.getUid()));
        return genericResult;
    }

    protected boolean empty(String iconUrl) {
        return iconUrl == null || iconUrl.length() <= 0;
    }

    public static void main(String[] args) {
        System.out.println("你好");
    }
}
