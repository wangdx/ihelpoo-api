package com.ihelpoo.api.dao;

import com.ihelpoo.api.model.entity.IUserPriorityEntity;

import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public interface UserPriorityDao {
    List<IUserPriorityEntity> findAllPrioritiesByUid(int uid);
}
