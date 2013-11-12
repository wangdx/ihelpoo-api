package com.ihelpoo.api.dao;

import com.ihelpoo.api.model.entity.*;

import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public interface MessageDao {

    List<VMsgLoginEntity> findNoticesByIds(String ids, int pageIndex, int pageSize);

    IRecordDiffusionEntity findDiffusionBy(int id);

    List<IMsgActiveEntity> findActivesByUid(int uid, int pageIndex, int pageSize);

    int updateActiveDeliver(int uid);

//    List<VTweetCommentEntity> findAllChatsBy(int id, int pageIndex, int pageSize);

    List<ITalkContentEntity> findRecentChatsBy(int uid, int pageIndex, int pageSize);

    List<VTweetCommentEntity> findAllChatsBy(Integer uid, Integer id, Integer pageIndex, Integer pageSize);

    void updateChats(Integer uid, Integer friendId);

    void deleteChats(Integer uid, Integer friendId);

    void updateOneChat(Integer id, Integer uid, Integer friendId);

    void deleteOneChat(Integer id, Integer uid, Integer friendId);
}
