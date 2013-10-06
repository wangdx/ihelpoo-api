package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.entity.IUserLoginEntity;
import com.ihelpoo.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional
    public void updateRecord(int uid, String newImageName, int schoolId) {
        long t = System.currentTimeMillis()/1000;
        userDao.updateAvatar(uid, 1, newImageName);
        int imgId = userDao.saveOutimg(uid, t, Constant.IMG_STORAGE_ROOT  + "useralbum/" + uid + "/" + newImageName + ".jpg");
        int sayId = userDao.addSay(uid, 2, "我刚刚换了新头像噢 :)", imgId, t, "动态", schoolId);
        int dynId = userDao.addDynamic(sayId, "changeicon");
    }
}
