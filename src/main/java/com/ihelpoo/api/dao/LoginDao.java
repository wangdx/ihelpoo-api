package com.ihelpoo.api.dao;

import com.ihelpoo.api.model.GenericResult;

/**
 * @author: dongxu.wang@acm.org
 */
public interface LoginDao {
    GenericResult validate(String username, String password, int keepAlive);
}
