package com.ihelpoo.api.dao;

import com.ihelpoo.api.model.entity.*;

import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public interface NotificationDao {
    List<IMsgAtEntity> findAllAtMsgsByUid(int uid);

    List<IMsgSystemEntity> findAllSystemMsgsByUid(int uid);

    List<IMsgCommentEntity> findAllCommentMsgsByUid(int uid);

    List<ITalkContentEntity> findAllTalkContentsByUid(int uid);

    List<IUserCoinsEntity> findAllUserCoinsByUid(int uid);
}
