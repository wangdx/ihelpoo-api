package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.MessageDao;
import com.ihelpoo.api.model.entity.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import java.util.*;

/**
 * @author: dongxu.wang@acm.org
 */
public class MessageDaoImpl extends NamedParameterJdbcDaoSupport implements MessageDao {
    @Override
    public List<VMsgLoginEntity> findNoticesByIds(String ids, int pageIndex, int pageSize) {
        if(ids == null || ids.trim().length() < 1){
            return Collections.emptyList();
        }
        String sql = " SELECT * FROM i_msg_notice LEFT JOIN i_user_login ON source_id = uid WHERE notice_id IN (" + ids + ") order by create_time DESC LIMIT ? OFFSET ?  ";
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
    public List<ITalkContentEntity> findRecentChatsBy(int uid, int pageIndex, int pageSize) {
        final String sql = " SELECT *, COUNT(*) AS chat_num , max(`time`)  as max_time FROM (" +
                "select * from i_talk_content where del != :me and (uid=:me or touid=:me) order by `time` desc) a " +
                "GROUP BY uid, touid ORDER BY `time` DESC LIMIT :limit OFFSET :offset ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("me", uid);
        parameters.addValue("limit", pageSize);
        parameters.addValue("offset", pageIndex * pageSize);
        return getNamedParameterJdbcTemplate().query(sql, parameters, new BeanPropertyRowMapper<ITalkContentEntity>(ITalkContentEntity.class));
    }

    @Override
    public List<VTweetCommentEntity> findAllChatsBy(Integer uid, Integer id, Integer pageIndex, Integer pageSize) {
        List<VTweetCommentEntity> commentEntities = new ArrayList<VTweetCommentEntity>();
        final String sql = " select t.content, t.`time`,t.deliver, t.del,t.touid,t.id, u.* from i_talk_content t join i_user_login u on t.uid = u.uid WHERE (t.uid = :me AND touid = :friend) OR (t.uid = :friend AND touid = :me)  AND del != :me ORDER BY time DESC LIMIT :limit OFFSET :offset ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("me", uid);
        parameters.addValue("friend", id);
        parameters.addValue("limit", pageSize);
        parameters.addValue("offset", pageIndex * pageSize);
        List<VUserTalkEntity> talks = getNamedParameterJdbcTemplate().query(sql, parameters, new BeanPropertyRowMapper<VUserTalkEntity>(VUserTalkEntity.class));
        for(VUserTalkEntity talk : talks){
            VTweetCommentEntity commentEntity = new VTweetCommentEntity();
            commentEntity.setNickname(talk.getNickname());
            commentEntity.setTime(talk.getTime());
            commentEntity.setContent(talk.getContent());
            commentEntity.setIconUrl(talk.getIconUrl());
            commentEntity.setUid(talk.getUid());
            commentEntity.setSid(talk.getId());
            commentEntities.add(commentEntity);
        }
        return commentEntities;
    }

    @Override
    public void updateChats(Integer uid, Integer friendId) {
        final String sql = " UPDATE i_talk_content SET del = :me WHERE ((uid= :me and touid= :friendId) or (touid= :me and uid= :friendId)) AND del = 0 ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("me", uid);
        parameters.addValue("friendId", friendId);
        getNamedParameterJdbcTemplate().update(sql, parameters);
    }

    @Override
    public void deleteChats(Integer uid, Integer friendId) {
        final String sql = " DELETE FROM i_talk_content WHERE ((uid= :me and touid= :friendId) or (touid= :me and uid= :friendId)) AND del = :friendId ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("me", uid);
        parameters.addValue("friendId", friendId);
        getNamedParameterJdbcTemplate().update(sql, parameters);
    }

    @Override
    public void updateOneChat(Integer id, Integer uid, Integer friendId) {
        final String sql = " UPDATE i_talk_content SET del = :me WHERE id=:id AND del = 0 ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        parameters.addValue("me", uid);
        getNamedParameterJdbcTemplate().update(sql, parameters);
    }

    @Override
    public void deleteOneChat(Integer id, Integer uid, Integer friendId) {
        final String sql = " DELETE FROM i_talk_content WHERE  id=:id AND del = :friendId ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        parameters.addValue("friendId", friendId);
        getNamedParameterJdbcTemplate().update(sql, parameters);
    }
}
