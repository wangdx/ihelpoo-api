package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.NotificationDao;
import com.ihelpoo.api.model.entity.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author: dongxu.wang@acm.org
 */
public class NotificationDaoImpl extends JdbcDaoSupport implements NotificationDao {



    @Override
    public int findNewAtmeCount(int uid) {
        String sql = " SELECT COUNT(*) FROM i_msg_at WHERE touid=? and deliver=0 ";
        return getJdbcTemplate().queryForObject(sql, Integer.class, uid);
    }

    @Override
    public int findNewCommentCount(int uid) {
        String sql = " SELECT COUNT(*) FROM i_msg_comment WHERE uid=? and deliver=0  ";
        return getJdbcTemplate().queryForObject(sql, Integer.class, uid);
    }
    @Override
    public int findNewActiveCount(int uid) {
        String sql = " SELECT COUNT(*) FROM i_msg_active WHERE uid=? and deliver=0  ";
        return getJdbcTemplate().queryForObject(sql, Integer.class, uid);
    }

    @Override
    public int deliverAllAtme(Integer uid) {
        final String sql = " UPDATE i_msg_at SET deliver='1' WHERE touid=? AND deliver='0' ";
        return getJdbcTemplate().update(sql, new Object[]{uid});
    }

    @Override
    public int fineNewChatCount(int uid) {
        String sql = " SELECT COUNT(*) FROM i_talk_content WHERE touid=? and deliver=0  ";
        return getJdbcTemplate().queryForObject(sql, Integer.class, uid);
    }


    @Override
    public int deliverAllComment(Integer uid) {
        final String sql = " UPDATE i_msg_comment SET deliver='1' WHERE uid=? AND deliver='0' ";
        return getJdbcTemplate().update(sql, new Object[]{uid});
    }

    @Override
    public int deliverAllActive(Integer uid) {
        final String sql = " UPDATE i_msg_active SET deliver='1' WHERE uid=? AND deliver='0' ";
        return getJdbcTemplate().update(sql, new Object[]{uid});
    }

    @Override
    public int deliverAllMessage(Integer uid, Integer fromUid) {//TODO only fromuid
        final String sql = " update i_talk_content set deliver='1' where touid=? and deliver='0' ";
        return getJdbcTemplate().update(sql, new Object[]{uid});
    }
}
