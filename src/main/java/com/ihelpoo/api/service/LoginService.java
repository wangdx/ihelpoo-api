package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.LoginResult;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.base.Result;
import com.ihelpoo.api.model.base.User;
import com.ihelpoo.api.model.entity.IUserLoginEntity;
import com.ihelpoo.api.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class LoginService {
    public static final String FAILURE = "0";
    public static final String SUCCESS = "1";
    public static final String MSG_ERR_USERNAME_OR_PWD = "用户名或密码错误";
    public static final String MSG_SUC_LOGIN = "登录成功";
    @Autowired
    UserDao userDao;
    @Autowired
    MD5 md5;

    public LoginResult validate(String username, String pwd, int keepLogin) {
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
        User user = new User();
        user.setUid(userLoginEntity.getUid());
        user.setLocation(userLoginEntity.getSchool());
        user.setName(userLoginEntity.getNickname());
        user.setScore((userLoginEntity.getCoins() == null || "".equals(userLoginEntity.getCoins())) ? 0 : Integer.parseInt(userLoginEntity.getCoins()));
        Notice notice = new Notice();
        loginResult.setUser(user);
        loginResult.setResult(new Result(SUCCESS, MSG_SUC_LOGIN));
        loginResult.setNotice(notice);
        return loginResult;
    }
}
