package com.ihelpoo.api.service;

import com.ihelpoo.api.model.base.Result;
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
public class TweetService extends RecordService {

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
            TweetResult.Tweet tweet = new TweetResult.Tweet();
            tweet.academy = "[" + tweetEntity.getName() + "]";
            tweet.rank = convertToRank(tweetEntity.getActive());
            tweet.onlineState = convertToOnlineState(tweetEntity.getOnline());
            tweet.portrait = convertToAvatarUrl(tweetEntity.getIconUrl(), tweetEntity.getUid());
            tweet.appclient = convertToBy(tweetEntity.getFrom());
            tweet.commentCount = tweetEntity.getCommentCo() == null ? 0 : tweetEntity.getCommentCo();
            tweet.body = tweetEntity.getContent();
            tweet.pubDate = convertToDate(tweetEntity.getTime());
            tweet.spreadCount = tweetEntity.getDiffusionCo() == null ? 0 : tweetEntity.getDiffusionCo();
            tweet.authorGossip = convertToGossip(tweetEntity.getSex(), tweetEntity.getBirthday());
            tweet.imgSmall = firstImgUrl;
            tweet.imgBig = firstImgUrl;
            tweet.author = tweetEntity.getNickname();
            tweet.id = tweetEntity.getSid();
            tweet.authorType = convertToType(tweetEntity.getType(), tweetEntity.getEnteryear());
            tweet.authorid = tweetEntity.getUid();
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
        tweetResult.tweetCount = pageSize + 1;
        tweetResult.pagesize = pageSize;
        tweetResult.notice = notice;
        tweetResult.tweets = tweetsWrapper;
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
        TweetResult.Tweet tweet = new TweetResult.Tweet();
        tweet.spreadCount = tweetDetailEntity.getDiffusionCo() == null ? 0 : tweetDetailEntity.getDiffusionCo();
        tweet.onlineState = convertToOnlineState(tweetDetailEntity.getOnline());
        tweet.appclient = convertToBy(tweetDetailEntity.getBy());
        tweet.body = tweetDetailEntity.getContent();
        tweet.pubDate = convertToDate(tweetDetailEntity.getTime());
        tweet.commentCount = tweetDetailEntity.getCommentCo() == null ? 0 : tweetDetailEntity.getCommentCo();
        tweet.imgSmall = imgUrl;
        tweet.imgBig = imgUrl;
        tweet.portrait = convertToAvatarUrl(tweetDetailEntity.getIconUrl(), tweetDetailEntity.getUid());
        tweet.rank = convertToRank(tweetDetailEntity.getActive());
        tweet.authorGossip = convertToGossip(tweetDetailEntity.getSex(), tweetDetailEntity.getBirthday());
        tweet.academy = tweetDetailEntity.getAcademy();
        tweet.id = tweetDetailEntity.getSid();
        tweet.authorType = convertToType(tweetDetailEntity.getAuthorType(), tweetDetailEntity.getEnterYear());
        tweet.author = tweetDetailEntity.getAuthor();
        tweet.authorid = tweetDetailEntity.getUid();
        tweet.plusByMe = 0;
        tweet.plusCount = tweetDetailEntity.getPlusCo();
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


    public TweetCommentPushResult pushComment(int id, int uid, String[] atUsers, String content, int catalog) {
        return streamDao.pushComment(id, uid, atUsers, content, catalog);
    }


    private String[] fetchLinks(List<IRecordOutimgEntity> imgLinkEntities) {
        return new String[0];  //To change body of created methods use File | Settings | File Templates.
    }

    public int pubTweet(int uid, long t, String msg, String reward, String filePath, String by, int schoolId) {
        int sayId = -1, imgId = -1;
        if (!StringUtils.isEmpty(filePath)) {
            imgId = userDao.saveOutimg(uid, t, Constant.IMG_STORAGE_ROOT + filePath);
        }
        String imageIds = imgId == -1 ? "" : String.valueOf(imgId);
        sayId = userDao.saveSay(uid, t, msg, imageIds, reward, by, schoolId);
        return sayId;
    }

    public TweetCommentPushResult plus(int id, int uid) {

        Result result = new Result("1", "操作成功");
        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();

        TweetCommentPushResult commentPushResult = new TweetCommentPushResult(result, null, notice);

        return commentPushResult;
    }

    public TweetCommentPushResult diffuse(int id, int uid, String content) {

        Result result = new Result("1", "发布成功");
        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();

        TweetCommentPushResult commentPushResult = new TweetCommentPushResult(result, null, notice);

        return commentPushResult;
    }
}
