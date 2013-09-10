package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.MessageDao;
import com.ihelpoo.api.model.entity.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.*;

/**
 * @author: dongxu.wang@acm.org
 */
public class MessageDaoImpl extends JdbcDaoSupport implements MessageDao {
    @Override
    public List<VMsgLoginEntity> findNoticesByIds(String ids, int pageIndex, int pageSize) {
        String sql = " SELECT * FROM i_msg_notice JOIN i_user_login ON source_id = uid WHERE notice_id IN (" + ids + ") order by create_time DESC LIMIT ? OFFSET ?  ";
        return getJdbcTemplate().query(sql, new Object[]{pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<VMsgLoginEntity>(VMsgLoginEntity.class));
    }

    @Override
    public IRecordDiffusionEntity findDiffusionBy(int id) {
        String sql = " SELECT * FROM i_record_diffusion WHERE id = ? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<IRecordDiffusionEntity>(IRecordDiffusionEntity.class));
    }

    @Override
    public List<IMsgActiveEntity> findActivesByUid(int uid, int pageIndex, int pageSize) {
        String sql = " SELECT * FROM i_msg_active WHERE uid=? ORDER BY id DESC LIMIT ? OFFSET ? ";
        return getJdbcTemplate().query(sql, new Object[]{uid, pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<IMsgActiveEntity>(IMsgActiveEntity.class));
    }

    @Override
    public int updateActiveDeliver(int uid) {
        final String sql = " UPDATE i_msg_active SET deliver=1 WHERE uid=? AND deliver=0 ";
        return getJdbcTemplate().update(sql, uid);
    }

    @Override
    public List<ITalkContentEntity> findRecentChatsBy(int uid, int pageIndex, int pageSize){
        final String sql = " SELECT *, COUNT(*) AS chat_num FROM i_talk_content WHERE uid=? or touid=? GROUP BY uid, touid ORDER BY id DESC LIMIT ? OFFSET ? ";
        return getJdbcTemplate().query(sql, new Object[]{uid, uid, pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<ITalkContentEntity>(ITalkContentEntity.class));
    }

//    @Override
//    public List<VTweetCommentEntity> findAllChatsBy(int uid, int pageIndex, int pageSize) {
//        final String sql = " SELECT * FROM i_talk_content WHERE uid=? or touid=? GROUP BY uid, touid ORDER BY id DESC LIMIT ? OFFSET ? ";
//        List<ITalkContentEntity> talks = getJdbcTemplate().query(sql, new Object[]{uid, uid, pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<ITalkContentEntity>(ITalkContentEntity.class));
//        Set<Integer> uids = new HashSet<Integer>();
//        for (ITalkContentEntity talk : talks) {
//            uids.add(talk.getUid());
//            uids.add(talk.getTouid());
//        }
//        MapSqlParameterSource parameters = new MapSqlParameterSource();
//        parameters.addValue("ids", uids);
//        List<IUserLoginEntity> users = getNamedParameterJdbcTemplate().query(" SELECT * FROM i_user_login WHERE uid IN (:ids) ", parameters, new BeanPropertyRowMapper<IUserLoginEntity>(IUserLoginEntity.class));
//        Map<Integer, IUserLoginEntity> usersMap = new HashMap<Integer, IUserLoginEntity>();
//        for (IUserLoginEntity user : users) {
//            usersMap.put(user.getUid(), user);
//        }
//
//        List<VTweetCommentEntity> comments = new ArrayList<VTweetCommentEntity>();
//        for (ITalkContentEntity talk : talks) {
//            VTweetCommentEntity comment = new VTweetCommentEntity();
//            comment.setCid(talk.getId());
//            comment.setIconUrl(usersMap.get(talk.getUid()).getIconUrl());
//            comment.setNickname(usersMap.get(talk.getUid()).getNickname());
//            comment.setUid(talk.getUid());
//            comment.setContent(talk.getContent());
//            comment.setTime(talk.getTime());
//            comments.add(comment);
//        }
//        return comments;
//    }


}
