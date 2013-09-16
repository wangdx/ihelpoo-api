package com.ihelpoo.api.dao;

import com.ihelpoo.api.OoUser;
import com.ihelpoo.api.model.UserList;
import com.ihelpoo.api.model.entity.IUserLoginEntity;
import com.ihelpoo.api.model.entity.IUserStatusEntity;

import java.util.List;
import java.util.Set;

/**
 * @author dongxu.wang@acm.org
 */
public interface UserDao {
    UserList getUserList(int grade);

    UserList getUserList(OoUser.UserDimension userDimension);

    IUserLoginEntity findByUserName(String username);

    IUserLoginEntity findUserById(int uid);

    IUserStatusEntity findUserStatusById(int uid);

    int updateLogin(String ip, long loginTime, String status, Integer lastLoginTime, int uid);

    int updateLogin(int uid, int newUserActive, int count);

    int updateActive(int uid, int newUserActive);

    int saveMsgActive(int uid, int total, int change, String reason);

    int updateStatus(int uid, int activeFlag, int clear);

    List<IUserLoginEntity> findUsersBy(Set<Integer> uids);

    int saveOutimg(int uid, long t, String filePath);

    int saveSay(int uid, long t, String msg, String imageIds, String reward, String by, int schoolId);
}
