package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.MessageDao;
import com.ihelpoo.api.model.entity.IMsgActiveEntity;
import com.ihelpoo.api.model.entity.IRecordDiffusionEntity;
import com.ihelpoo.api.model.entity.VMsgLoginEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;

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


}
