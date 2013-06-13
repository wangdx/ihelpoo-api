package com.ihelpoo.api.dao;

import com.ihelpoo.api.OoUser;
import com.ihelpoo.api.model.UserList;
import com.ihelpoo.api.model.entity.IUserLoginEntity;

/**
 * @author dongxu.wang@acm.org
 */
public interface UserDao {
    UserList getUserList(int grade);

    UserList getUserList(OoUser.UserDimension userDimension);

    IUserLoginEntity findByUserName(String username);

    IUserLoginEntity findUserById(int uid);
}
