package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.NotificationDao;
import com.ihelpoo.api.model.entity.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public class NotificationDaoImpl extends JdbcDaoSupport implements NotificationDao {
    @Override
    public List<IMsgAtEntity> findAllAtMsgsByUid(int uid) {
        String sql = " SELECT * FROM i_msg_at WHERE touid=? and deliver=0 ";
        Object[] params = new Object[]{uid};
        return getJdbcTemplate().query(sql, params, new BeanPropertyRowMapper<IMsgAtEntity>(IMsgAtEntity.class));
    }

    @Override
    public List<IMsgSystemEntity> findAllSystemMsgsByUid(int uid) {
        String sql = " SELECT * FROM i_msg_system WHERE uid=? and deliver=0  ";
        Object[] params = new Object[]{uid};
        return getJdbcTemplate().query(sql, params, new BeanPropertyRowMapper<IMsgSystemEntity>(IMsgSystemEntity.class));
    }

    @Override
    public List<IMsgCommentEntity> findAllCommentMsgsByUid(int uid) {
        String sql = " SELECT * FROM i_msg_comment WHERE uid=? and deliver=0  ";
        Object[] params = new Object[]{uid};
        return getJdbcTemplate().query(sql, params, new BeanPropertyRowMapper<IMsgCommentEntity>(IMsgCommentEntity.class));
    }

    @Override
    public List<ITalkContentEntity> findAllTalkContentsByUid(int uid) {
        String sql = " SELECT * FROM i_talk_content WHERE touid=? and deliver=0  ";
        Object[] params = new Object[]{uid};
        return getJdbcTemplate().query(sql, params, new BeanPropertyRowMapper<ITalkContentEntity>(ITalkContentEntity.class));
    }

    @Override
    public List<IUserCoinsEntity> findAllUserCoinsByUid(int uid) {
        String sql = " SELECT * FROM i_user_coins WHERE uid=? and deliver=0  ";
        Object[] params = new Object[]{uid};
        return getJdbcTemplate().query(sql, params, new BeanPropertyRowMapper<IUserCoinsEntity>(IUserCoinsEntity.class));
    }
}
