package com.ihelpoo.api.dao;

import com.ihelpoo.api.model.entity.VTweetStreamEntity;

import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public interface StreamDao {
    List<VTweetStreamEntity> findAllTweetsBy(int catalog, StringBuilder pids, StringBuilder sids, int pageIndex, int pageSize);
}
