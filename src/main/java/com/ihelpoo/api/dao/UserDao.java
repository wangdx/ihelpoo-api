package com.ihelpoo.api.dao;

import com.ihelpoo.api.OoUser;
import com.ihelpoo.api.model.UserList;

/**
 * @author dongxu.wang@acm.org
 */
public interface UserDao {
    UserList getUserList(int grade);

    UserList getUserList(OoUser.UserDimension userDimension);
}
