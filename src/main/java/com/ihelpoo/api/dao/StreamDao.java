package com.ihelpoo.api.dao;

import com.ihelpoo.api.model.TweetCommentPushResult;
import com.ihelpoo.api.model.entity.*;

import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public interface StreamDao {
    List<VTweetStreamEntity> findAllTweetsBy(int catalog, StringBuilder pids, StringBuilder sids, int schoolId, int pageIndex, int pageSize);

    IRecordSayEntity findTweetBy(int sid);

    List<IRecordDiffusionEntity> findSpreadsBySid(int sid);

    List<VTweetSpreadEntity> findUserSpreadsBy(int sid);

    List<String> findUserImgLinkEntitiesBy(int sid);

    void updateTweet(IRecordSayEntity tweetEntity);

    List<VTweetCommentEntity> findAllCommentssBy(int sid, int catalog, int pageIndex, int pageSize);

    TweetCommentPushResult pushComment(int id, int uid, String[] atUsers, String content, int catalog, int postToMyZone);
}
