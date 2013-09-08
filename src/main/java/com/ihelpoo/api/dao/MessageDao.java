package com.ihelpoo.api.dao;

import com.ihelpoo.api.model.entity.IMsgActiveEntity;
import com.ihelpoo.api.model.entity.IMsgNoticeEntity;
import com.ihelpoo.api.model.entity.IRecordDiffusionEntity;
import com.ihelpoo.api.model.entity.VMsgLoginEntity;

import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public interface MessageDao {

    List<VMsgLoginEntity> findNoticesByIds(String ids, int pageIndex, int pageSize);

    IRecordDiffusionEntity findDiffusionBy(int id);

    List<IMsgActiveEntity> findActivesByUid(int uid, int pageIndex, int pageSize);
}
