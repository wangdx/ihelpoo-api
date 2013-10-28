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

//    TweetCommentPushResult pushComment(int id, int uid, String[] atUsers, String content, int catalog);

    VTweetDetailEntity findTweetDetailBy(int sid);

    IRecordHelpreplyEntity findHelpBy(Integer hid);

    List<IRecordSayEntity> findTweetsWithin(int uid, long twelveHours);

    int findCountOfSaysWithin(int uid, long twelveHours);

    IRecordSayEntity findLastTweetBy(int uid);

    int saveHelpData(int sayId, int reward);

    int deleteTweet(Integer uid, Integer sid);

    int isRecordPlusByMe(int sid, Integer uid);

    int isRecordDiffuseByMe(int sid, Integer uid);

    IRecordPlusEntity findPlusBy(Integer sid, Integer uid);

    int deletePlus(int id);

    IRecordSayEntity findOneTweetBy(Integer sid);

    IMsgNoticeEntity findMsgNotice(String noticeType, Integer uid, Integer sid, String type);

    int deleteNoticeMessage(long noticeId);

    int addPlus(Integer sid, Integer uid);

    int saveNoticeMessage(String noticeType, Integer uid, Integer detailId, String sayType);

    IRecordDiffusionEntity findDiffusion(Integer sid, Integer uid);

    List<IRecordDiffusionEntity> findDiffusions(Integer uid, long time12Hour);

    int saveDiffusion(Integer uid, Integer sid, String content);

    int incSayCount(Integer sid, boolean canAffect, String countColumn);

    int incOrDecSayCount(Integer sid, int offset);

    int saveComment(int sid, int uid, String content);

    int saveMsgComment(Integer ownerUid, int sid, int ncid, int uid);

    int saveMsgAt(int uid, int uid1, int id, int cid);
}
