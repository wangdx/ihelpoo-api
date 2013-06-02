package com.ihelpoo.api.dao;

import com.ihelpoo.api.model.RecordSay;
import com.ihelpoo.api.model.RecordSayList;

import java.util.List;

public interface JdbcRecordSayDao {
    RecordSayList getRecordSays(Integer uid);

    RecordSay getRecordSay(Integer uid);
}
