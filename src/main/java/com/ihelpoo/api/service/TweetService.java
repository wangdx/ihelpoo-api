package com.ihelpoo.api.service;

import com.ihelpoo.common.Constant;
import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.dao.UserPriorityDao;
import com.ihelpoo.api.model.TweetCommentPushResult;
import com.ihelpoo.api.model.TweetCommentResult;
import com.ihelpoo.api.model.TweetDetailResult;
import com.ihelpoo.api.model.TweetResult;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.entity.*;
import com.ihelpoo.api.service.base.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class TweetService extends RecordService{

    @Autowired
    UserPriorityDao userPriorityDao;

    @Autowired
    UserDao userDao;

    public TweetResult pullBy(int uid, int catalog, int schoolId, int pageIndex, int pageSize) {
        List<IUserPriorityEntity> priorityEntities = userPriorityDao.findAllPrioritiesByUid(uid);
        StringBuilder pids = new StringBuilder();
        StringBuilder sids = new StringBuilder();
        if (!priorityEntities.isEmpty()) {
            for (IUserPriorityEntity userPriorityEntity : priorityEntities) {
                if (null != userPriorityEntity.getPid()) {
                    pids.append(userPriorityEntity.getPid()).append(",");
                } else if (null != userPriorityEntity.getSid()) {
                    sids.append(userPriorityEntity.getSid()).append(",");
                }
            }
            if (pids.length() - 1 >= 0)
                pids.deleteCharAt(pids.length() - 1);
            if (sids.length() - 1 >= 0)
                sids.deleteCharAt(sids.length() - 1);
        }

        List<VTweetStreamEntity> tweetEntities = streamDao.findAllTweetsBy(catalog, pids, sids, schoolId, pageIndex, pageSize);
        List<TweetResult.Tweet> tweets = new ArrayList<TweetResult.Tweet>();
        for (VTweetStreamEntity tweetEntity : tweetEntities) {
            String firstImgUrl = convertToImageUrl(tweetEntity.getSid());
            TweetResult.Tweet tweet = new TweetResult.Tweet.Builder()
                    .academy("[" + tweetEntity.getName() + "]")
                    .authorRank(convertToRank(tweetEntity.getActive()))
                    .onlineState(convertToOnlineState(tweetEntity.getOnline()))
                    .avatar(convertToAvatarUrl(tweetEntity.getIconUrl(), tweetEntity.getUid()))
                    .from(convertToBy(tweetEntity.getFrom()))
                    .comments(tweetEntity.getCommentCo() == null ? 0 : tweetEntity.getCommentCo())
                    .content(tweetEntity.getContent())
                    .date(convertToDate(tweetEntity.getTime()))
                    .spreads(tweetEntity.getDiffusionCo() == null ? 0 : tweetEntity.getDiffusionCo())
                    .authorGossip(convertToGossip(tweetEntity.getSex(), tweetEntity.getBirthday()))
                    .small(firstImgUrl)
                    .big(firstImgUrl)
                    .author(tweetEntity.getNickname())
                    .id(tweetEntity.getSid())
                    .authorType(convertToType(tweetEntity.getType(), tweetEntity.getEnteryear()))
                    .authorid(tweetEntity.getUid())
                    .build();
            tweets.add(tweet);
        }
        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();

        TweetResult tweetResult = new TweetResult();
        TweetResult.Tweets tweetsWrapper = new TweetResult.Tweets(tweets);
        tweetResult.setTweetCount(pageSize + 1);
        tweetResult.setPagesize(pageSize);
        tweetResult.setNotice(notice);
        tweetResult.setTweets(tweetsWrapper);
        return tweetResult;
    }


    public TweetDetailResult pullTweetBy(int sid) {
        VTweetDetailEntity tweetDetailEntity = streamDao.findTweetDetailBy(sid);
        long t = System.currentTimeMillis();
        String imgUrl = convertToImageUrl(tweetDetailEntity.getSid());
        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();
        TweetResult.Tweet tweet = new TweetResult.Tweet.Builder()
                .spreads(tweetDetailEntity.getDiffusionCo() == null ? 0 : tweetDetailEntity.getDiffusionCo())
                .onlineState(convertToOnlineState(tweetDetailEntity.getOnline()))
                .from(convertToBy(tweetDetailEntity.getBy()))
                .content(tweetDetailEntity.getContent())
                .date(convertToDate(tweetDetailEntity.getTime()))
                .comments(tweetDetailEntity.getCommentCo() == null ? 0 : tweetDetailEntity.getCommentCo())
                .small(imgUrl)//TODO cope with
                .big(imgUrl)
                .avatar(convertToAvatarUrl(tweetDetailEntity.getIconUrl(), tweetDetailEntity.getUid()))
                .authorRank(convertToRank(tweetDetailEntity.getActive()))
                .authorGossip(convertToGossip(tweetDetailEntity.getSex(), tweetDetailEntity.getBirthday()))
                .academy(tweetDetailEntity.getAcademy())
                .id(tweetDetailEntity.getSid())
                .authorType(convertToType(tweetDetailEntity.getAuthorType(), tweetDetailEntity.getEnterYear()))
                .author(tweetDetailEntity.getAuthor())
                .authorid(tweetDetailEntity.getUid())
                .build();
        TweetDetailResult tdr = new TweetDetailResult(tweet, notice);
        return tdr;
    }

    public TweetCommentResult pullCommentsBy(int sid, int pageIndex, int pageSize) {
        List<VTweetCommentEntity> commentEntities = streamDao.findAllCommentsBy(sid, pageIndex, pageSize);
        int allCount = commentEntities.size();
        List<TweetCommentResult.Comment> comments = new ArrayList<TweetCommentResult.Comment>();
        for (VTweetCommentEntity commentEntity : commentEntities) {
            TweetCommentResult.Comment comment = new TweetCommentResult.Comment.Builder()
                    .content(commentEntity.getContent())
                    .date(convertToDate(commentEntity.getTime()))
                    .author(commentEntity.getNickname())
                    .authorid(commentEntity.getUid())
                    .avatar(convertToAvatarUrl(commentEntity.getIconUrl(), commentEntity.getUid()))
                    .by(0)
                    .id(commentEntity.getSid())
                    .build();
            comments.add(comment);
        }
        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();

        TweetCommentResult commentResult = new TweetCommentResult();
        TweetCommentResult.Comments commentWrapper = new TweetCommentResult.Comments(comments);
        commentResult.setAllCount(allCount);
        commentResult.setPagesize(pageSize);
        commentResult.setComments(commentWrapper);
        commentResult.setNotice(notice);
        return commentResult;
    }


    public TweetCommentPushResult pushComment(int id, int uid, String[] atUsers, String content, int catalog, int isPostToMyZone) {
        return streamDao.pushComment(id, uid, atUsers, content, catalog, isPostToMyZone);
    }


    private String[] fetchLinks(List<IRecordOutimgEntity> imgLinkEntities) {
        return new String[0];  //To change body of created methods use File | Settings | File Templates.
    }

    public int pubTweet(int uid, long t, String msg, String reward, String filePath, String by, int schoolId) {
        int sayId = -1, imgId = -1;
        if(!StringUtils.isEmpty(filePath)) {
            imgId = userDao.saveOutimg(uid, t, Constant.IMG_STORAGE_ROOT+filePath);
        }
        String imageIds = imgId == -1? "":String.valueOf(imgId);
        sayId = userDao.saveSay(uid, t, msg, imageIds, reward, by, schoolId);
        return sayId;
    }
}
