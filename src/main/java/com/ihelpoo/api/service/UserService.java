package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.entity.IUserLoginEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public String fetchPwdByUid(int uid) {
        IUserLoginEntity user = userDao.findUserById(uid);
        return user.getPassword();
    }

    public String fetchUserNameByUid(int uid) {
        IUserLoginEntity user = userDao.findUserById(uid);
        return user.getEmail();
    }


}
