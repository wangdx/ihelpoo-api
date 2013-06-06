package com.ihelpoo.api.dao;

import com.ihelpoo.api.model.PostList;

import java.util.Calendar;

/**
 * @author: dongxu.wang@acm.org
 */
public interface PostDao {
    /**
     *
     * @param timeLevel
     * @param order   CATALOG_ASK = 1; CATALOG_SHARE = 2; OTHER = 3; JOB = 4; SITE = 5;
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PostList getPostListByTimeLevel(String timeLevel, int order, int pageIndex, int pageSize);
}
