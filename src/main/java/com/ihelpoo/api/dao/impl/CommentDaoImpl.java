package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.CommentDao;
import com.ihelpoo.api.model.entity.IMsgCommentEntity;
import com.ihelpoo.api.model.entity.IRecordCommentEntity;
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
    public IRecordCommentEntity fetchCommentBy(Integer cid) {
        String sql = " SELECT * FROM i_record_comment WHERE cid=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{cid}, new BeanPropertyRowMapper<IRecordCommentEntity>(IRecordCommentEntity.class));
    }
}
