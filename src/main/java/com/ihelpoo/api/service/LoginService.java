package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.LoginResult;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.base.Result;
import com.ihelpoo.api.model.base.User;
import com.ihelpoo.api.model.entity.IUserLoginEntity;
import com.ihelpoo.api.model.entity.IUserStatusEntity;
import com.ihelpoo.api.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class LoginService {

    public static final String FAILURE = "0";
    public static final String SUCCESS = "1";
    public static final String MSG_ERR_USERNAME_OR_PWD = "用户名或密码错误";
    public static final String MSG_ERR_SYNC_STATUS = "同步出错，请重试";
    public static final String MSG_SUC_LOGIN = "登录成功";

    @Autowired
    UserDao userDao;
    @Autowired
    MD5 md5;

    public LoginResult login(String username, String pwd, String status, String ip) {
        LoginResult loginResult = new LoginResult();
        IUserLoginEntity userLoginEntity = null;
        try {
            userLoginEntity = userDao.findByUserName(username);
        } catch (Exception e) {
            //TODO gentle message to user
            loginResult.setResult(new Result(FAILURE, e.getMessage()));
            return loginResult;
        }


        if (!userLoginEntity.getPassword().equals(md5.encrypt(pwd))) {
            loginResult.setResult(new Result(FAILURE, MSG_ERR_USERNAME_OR_PWD));
            return loginResult;
        }

        if (syncUserStatus(userLoginEntity, status, ip) < 1) {
            loginResult.setResult(new Result(FAILURE, MSG_ERR_SYNC_STATUS));
            return loginResult;
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

            int newUserActive = userLoginEntity.getActive();
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
                userDao.saveMsgActive(userLoginEntity.getUid(), total, change, reason, userLoginEntity.getSchool());
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
                userDao.updateLogin(uid, newUserActive, userLoginEntity.getLoginDaysCo());
                userDao.updateStatus(uid, activeFlag, 1);
            }

            if (dayRules > 0) {
                userDao.saveMsgActive(uid, total, change, reason, userLoginEntity.getSchool());
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
        calendar.setTimeInMillis((long) userLoginEntity.getLastlogintime() * 1000);
        int latestLoginDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        if (latestLoginDayOfYear != todayOfYear) {
            userLoginEntity.setLastlogintime(userLoginEntity.getLogintime());
        }
        return userLoginEntity.getLastlogintime();
    }

    private LoginResult succeedToLogin(IUserLoginEntity userLoginEntity) {
        LoginResult loginResult = new LoginResult();
        User user = new User();
        user.setUid(userLoginEntity.getUid());

        user.setLocation(userLoginEntity.getSchool());
        user.setName(userLoginEntity.getNickname());
        user.setScore((userLoginEntity.getCoins() == null || "".equals(userLoginEntity.getCoins())) ? 0 : Integer.parseInt(userLoginEntity.getCoins()));
        Notice notice = new Notice.Builder().build();
        loginResult.setUser(user);
        loginResult.setResult(new Result(SUCCESS, MSG_SUC_LOGIN));
        loginResult.setNotice(notice);
        return loginResult;
    }
}
