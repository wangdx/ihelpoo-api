package com.ihelpoo.api.dao;

import com.ihelpoo.api.model.PostList;

import java.util.Calendar;

public interface PostDao {
    public PostList getPostListByTimeLevel(String timeLevel, Integer schoolId, int order, int pageIndex, int pageSize);
}
