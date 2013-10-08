package com.ihelpoo.api.dao;

import com.ihelpoo.api.model.TweetCommentPushResult;
import com.ihelpoo.api.model.entity.*;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public interface StreamDao {

    List<IRecordSayEntity> findTweetsBy(int uid, int pageIndex, int pageSize);

    List<VTweetStreamEntity> findAllTweetsBy(int catalog, StringBuilder pids, StringBuilder sids, int schoolId, int pageIndex, int pageSize);

    IRecordSayEntity findTweetBy(int sid);

    List<IRecordDiffusionEntity> findSpreadsBySid(int sid);

    List<VTweetSpreadEntity> findUserSpreadsBy(int sid);

    List<String> findUserImgLinkEntitiesBy(int sid);

    void updateTweet(IRecordSayEntity tweetEntity);

    List<VTweetCommentEntity> findAllCommentsBy(int sid, int pageIndex, int pageSize);

    TweetCommentPushResult pushComment(int id, int uid, String[] atUsers, String content, int catalog);

    VTweetDetailEntity findTweetDetailBy(int sid);

    IRecordHelpreplyEntity findHelpBy(Integer hid);

    List<IRecordSayEntity> findTweetsWithin(int uid, long twelveHours);

    int findCountOfSaysWithin(int uid, long twelveHours);

    IRecordSayEntity findLastTweetBy(int uid);

    int saveHelpData(int sayId, int reward);
}
