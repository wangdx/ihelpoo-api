package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.UserPriorityDao;
import com.ihelpoo.api.model.entity.IUserPriorityEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public class UserPriorityDaoImpl extends JdbcDaoSupport implements UserPriorityDao{
    @Override
    public List<IUserPriorityEntity> findAllPrioritiesByUid(int uid) {
        String sql = " SELECT * FROM i_user_priority WHERE uid=? ";
        return getJdbcTemplate().query(sql, new Object[]{uid}, new BeanPropertyRowMapper<IUserPriorityEntity>(IUserPriorityEntity.class));
    }
}
