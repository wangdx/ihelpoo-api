package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.CommentDao;
import com.ihelpoo.api.model.entity.IMsgCommentEntity;
import com.ihelpoo.api.model.entity.IRecordCommentEntity;
import com.ihelpoo.api.model.entity.VAtUserEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public class CommentDaoImpl extends NamedParameterJdbcDaoSupport implements CommentDao {


    @Override
    public List<IMsgCommentEntity> fetchAllCommentsBy(int uid, int pageIndex, int pageSize) {
        String sql = " SELECT * FROM i_msg_comment WHERE uid=? ORDER BY id DESC LIMIT ? OFFSET ? ";
        return getJdbcTemplate().query(sql, new Object[]{uid, pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<IMsgCommentEntity>(IMsgCommentEntity.class));
    }

    @Override
    public int fetchAllCommentsCountBy(int uid) {
        String sql = " SELECT count(*) FROM i_msg_comment WHERE uid=? ";
        return getJdbcTemplate().queryForObject(sql, Integer.class, uid);
    }

    @Override
    public IRecordCommentEntity fetchCommentBy(Integer cid) {
        String sql = " SELECT * FROM i_record_comment WHERE cid=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{cid}, new BeanPropertyRowMapper<IRecordCommentEntity>(IRecordCommentEntity.class));
    }

    @Override
    public List<VAtUserEntity> fetchAllAtBy(int uid, int pageIndex, int pageSize) {
        String sql = "SELECT id,touid,fromuid,sid,cid,hid,aid,`time`,deliver,uid,nickname,icon_url FROM i_msg_at LEFT JOIN i_user_login ON i_msg_at.fromuid = i_user_login.uid where touid=? ORDER BY `time` DESC LIMIT ? OFFSET ? ";
        return getJdbcTemplate().query(sql, new Object[]{uid,  pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<VAtUserEntity>(VAtUserEntity.class));
    }
}
