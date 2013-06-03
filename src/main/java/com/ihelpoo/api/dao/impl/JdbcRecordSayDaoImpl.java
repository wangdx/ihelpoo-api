package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.JdbcRecordSayDao;
import com.ihelpoo.api.model.RecordSay;
import com.ihelpoo.api.model.RecordSayList;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;

/**
 *  JdbcDao.
 *  @author dongxu.wang@acm.org
 *  @version 1
 */
public class JdbcRecordSayDaoImpl extends JdbcDaoSupport implements JdbcRecordSayDao {

    @Override
    public RecordSay getRecordSay(final Integer uid) {
        final String sql = "SELECT sid, uid, content, url FROM i_record_say LIMIT 1";
        final RecordSay recordSay = (RecordSay) getJdbcTemplate().queryForObject(sql, new BeanPropertyRowMapper<RecordSay>(RecordSay.class));
        return recordSay;
    }

    @Override
    public RecordSayList getRecordSays(final Integer uid) {
        final RecordSayList recordSays = new RecordSayList();
        final String sql = "SELECT sid, uid, content,url FROM i_record_say LIMIT 10";
        final List<RecordSay> recordSays1 = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<RecordSay>(RecordSay.class));
        recordSays.setList(recordSays1);
        return recordSays;
    }
}
