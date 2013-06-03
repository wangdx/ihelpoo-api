package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.JdbcRecordSayDao;
import com.ihelpoo.api.model.RecordSay;
import com.ihelpoo.api.model.RecordSayList;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;

public class JdbcRecordSayDaoImpl extends JdbcDaoSupport implements JdbcRecordSayDao {

    @Override
    public RecordSay getRecordSay(Integer uid) {
        String sql = "SELECT sid, uid, content, url FROM i_record_say LIMIT 1";
        RecordSay recordSay = (RecordSay) getJdbcTemplate().queryForObject(sql, new BeanPropertyRowMapper<RecordSay>(RecordSay.class));
        return recordSay;
    }

    @Override
    public RecordSayList getRecordSays(Integer uid) {
        RecordSayList recordSays = new RecordSayList();
        String sql = "SELECT sid, uid, content,url FROM i_record_say LIMIT 10";
        List<RecordSay> recordSays1 = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<RecordSay>(RecordSay.class));
        recordSays.setList(recordSays1);
        return recordSays;
    }
}
