package com.ihelpoo.api.dao;

import com.ihelpoo.api.model.entity.*;

import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public interface StreamDao {
    List<VTweetStreamEntity> findAllTweetsBy(int catalog, StringBuilder pids, StringBuilder sids, int pageIndex, int pageSize);

    IRecordSayEntity findTweetBy(int sid);

    List<IRecordDiffusionEntity> findSpreadsBySid(int sid);

    List<VTweetSpreadEntity> findUserSpreadsBy(int sid);

    List<String> findUserImgLinkEntitiesBy(int sid);

    void updateTweet(IRecordSayEntity tweetEntity);
}
