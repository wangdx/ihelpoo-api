package com.ihelpoo.api.dao;

import com.ihelpoo.api.model.RecordSay;
import com.ihelpoo.api.model.RecordSayList;

/**
 * Dao.
 * @author dongxu.wang@acm.org
 * @version 1
 */
public interface JdbcRecordSayDao {
    /**
     * get.
     * @param uid user id
     * @return list list
     */
    RecordSayList getRecordSays(Integer uid);

    /**
     * get.
     * @param uid user id
     * @return single list
     */
    RecordSay getRecordSay(Integer uid);
}
