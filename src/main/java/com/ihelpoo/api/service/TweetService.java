package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.StreamDao;
import com.ihelpoo.api.model.*;
import com.ihelpoo.api.model.obj.Result;
import com.ihelpoo.common.AppUtil;
import com.ihelpoo.common.Constant;
import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.obj.Notice;
import com.ihelpoo.api.model.entity.*;
import com.ihelpoo.api.service.base.RecordService;
import com.ihelpoo.common.util.UpYun;
import com.ihelpoo.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class TweetService extends RecordService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final int ERR_PUB_TOO_MANY = -1;
    public static final int ERR_ACTIVE_NOT_ENOUGH = -2;
    public static final int ERR_DUPLICATED_CONTENT = -3;

    @Autowired
    UserDao userDao;

    @Autowired
    StreamDao streamDao;

    public TweetResult pullBy(int uid, int catalog, int schoolId, int pageIndex, int pageSize) {
        List<IUserPriorityEntity> priorityEntities = userDao.findAllPrioritiesByUid(uid, 0, Integer.MAX_VALUE);
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
        int count = streamDao.findAllTweetsCountBy(catalog, pids, sids, schoolId);
        List<TweetResult.Tweet> tweets = new ArrayList<TweetResult.Tweet>();
        for (VTweetStreamEntity tweetEntity : tweetEntities) {
            String firstImgUrl = convertToImageUrl(tweetEntity.getSid());
            TweetResult.Tweet tweet = new TweetResult.Tweet();
            tweet.academy = StringUtils.isEmpty(tweetEntity.getShowMajorName()) ? "" : "[" + tweetEntity.getShowMajorName() + "]";
            tweet.rank = convertToRank(tweetEntity.getActive());
            tweet.onlineState = convertToOnlineState(tweetEntity.getOnline());
            tweet.portrait = convertToAvatarUrl(tweetEntity.getIconUrl(), tweetEntity.getUid(), false);
            tweet.appclient = convertToBy(tweetEntity.getFrom());
            tweet.by = tweetEntity.getFrom();
            tweet.plusCount = tweetEntity.getPlusCo();
            tweet.comment_count = tweetEntity.getCommentCo() == null ? 0 : tweetEntity.getCommentCo();
            tweet.body = tweetEntity.getContent();
            tweet.pubDate = convertToDate(tweetEntity.getLastCommentTi());
            tweet.spreadCount = tweetEntity.getDiffusionCo() == null ? 0 : tweetEntity.getDiffusionCo();
            tweet.authorGossip = convertToGossip(tweetEntity.getSex(), tweetEntity.getBirthday());
            tweet.imgSmall = firstImgUrl.replace("recordsay", "thumb_recordsay");
            tweet.imgBig = firstImgUrl;
            tweet.author = tweetEntity.getNickname();
            tweet.id = tweetEntity.getSid();
            tweet.authorType = convertToType(tweetEntity.getType(), tweetEntity.getEnteryear());
            tweet.authorid = tweetEntity.getUid();
            tweet.sayType = tweetEntity.getSayType();
            tweets.add(tweet);
        }

        TweetResult tweetResult = new TweetResult();
        TweetResult.Tweets tweetsWrapper = new TweetResult.Tweets(tweets);
        tweetResult.tweet_count = count;
        tweetResult.page_size = tweetEntities.size();
        tweetResult.notice = getNotice(uid);
        tweetResult.tweets = tweetsWrapper;
        return tweetResult;
    }


    public TweetDetailResult pullTweetBy(int sid, Integer uid) {
        VTweetDetailEntity tweetDetailEntity = streamDao.findTweetDetailBy(sid);
        TweetResult.Tweet tweet = makeTweet(tweetDetailEntity, sid, uid);
        TweetDetailResult tdr = new TweetDetailResult(tweet, getNotice(uid));
        return tdr;
    }

    private TweetResult.Tweet makeTweet(VTweetDetailEntity tweetDetailEntity, int sid, Integer uid) {
        TweetResult.Tweet tweet = toTweet(tweetDetailEntity);
        tweet.plusByMe = streamDao.isRecordPlusByMe(sid, uid);
        tweet.diffuseByMe = streamDao.isRecordDiffuseByMe(sid, uid);
        return tweet;
    }

    private TweetResult.Tweet toTweet(VTweetDetailEntity tweetDetailEntity) {
        String imgUrl = convertToImageUrl(tweetDetailEntity.getSid());
        TweetResult.Tweet tweet = new TweetResult.Tweet();
        tweet.spreadCount = tweetDetailEntity.getDiffusionCo() == null ? 0 : tweetDetailEntity.getDiffusionCo();
        tweet.onlineState = convertToOnlineState(tweetDetailEntity.getOnline());
        tweet.appclient = convertToBy(tweetDetailEntity.getBy());
        tweet.by = tweetDetailEntity.getBy();
        tweet.body = tweetDetailEntity.getContent();
        tweet.pubDate = convertToDate(tweetDetailEntity.getTime());
        tweet.comment_count = tweetDetailEntity.getCommentCo() == null ? 0 : tweetDetailEntity.getCommentCo();
        tweet.imgSmall = imgUrl.replace("recordsay", "thumb_recordsay");
        tweet.imgBig = imgUrl;
        tweet.portrait = convertToAvatarUrl(tweetDetailEntity.getIconUrl(), tweetDetailEntity.getUid(), false);
        tweet.rank = convertToRank(tweetDetailEntity.getActive());
        tweet.authorGossip = convertToGossip(tweetDetailEntity.getSex(), tweetDetailEntity.getBirthday());
        tweet.academy = tweetDetailEntity.getAcademy();
        tweet.id = tweetDetailEntity.getSid();
        tweet.authorType = convertToType(tweetDetailEntity.getAuthorType(), tweetDetailEntity.getEnterYear());
        tweet.author = tweetDetailEntity.getAuthor();
        tweet.authorid = tweetDetailEntity.getUid();
        tweet.plusByMe = 0;
        tweet.plusCount = tweetDetailEntity.getPlusCo();
        tweet.sayType = tweetDetailEntity.getSayType();
        return tweet;
    }

    public TweetCommentResult pullCommentsBy(Integer uid, Integer sid, Integer pageIndex, Integer pageSize) {
        if (sid == null) {
            throw new IllegalArgumentException("id is mandatory");
        }
        pageIndex = pageIndex == null ? Constant.DEFAULT_PAGEINDEX : pageIndex;
        pageSize = pageSize == null ? Constant.DEFAULT_PAGESIZE : pageSize;
        List<VTweetCommentEntity> commentEntities = streamDao.findAllCommentsBy(sid, pageIndex, pageSize);
//        int allCount = streamDao.findAllCommentsCountBy(sid);
        List<TweetCommentResult.Comment> comments = new ArrayList<TweetCommentResult.Comment>();
        for (VTweetCommentEntity commentEntity : commentEntities) {
            TweetCommentResult.Comment comment = new TweetCommentResult.Comment();
            comment.content = commentEntity.getContent();
            if (commentEntity.getToid() != null && commentEntity.getToid() > 9999) {
                IUserLoginEntity user = userDao.findUserById(commentEntity.getToid());
                comment.content = "[回复:" + user.getNickname() + "] " + comment.content;
            }
            comment.pubDate = convertToDate(commentEntity.getTime());
            comment.author = commentEntity.getNickname();
            comment.authorid = commentEntity.getUid();
            comment.portrait = convertToAvatarUrl(commentEntity.getIconUrl(), commentEntity.getUid(), false);
            comment.id = commentEntity.getCid();
            comment.appclient = 0;
            comments.add(comment);
        }

        TweetCommentResult commentResult = new TweetCommentResult();
        TweetCommentResult.Comments commentWrapper = new TweetCommentResult.Comments(comments);
//        commentResult.all_count = allCount;
        commentResult.page_size = commentEntities.size();
        commentResult.comments = commentWrapper;
        commentResult.notice = getNotice(uid);
        return commentResult;
    }


    public TweetCommentResult pullHelpRepliesBy(Integer uid, Integer sid, Integer pageIndex, Integer pageSize) {
        List<VTweetCommentEntity> commentEntities = streamDao.findAllHelpRepliesBy(sid, pageIndex, pageSize);
        int allCount = commentEntities.size();
        List<TweetCommentResult.Comment> comments = new ArrayList<TweetCommentResult.Comment>();
        for (VTweetCommentEntity commentEntity : commentEntities) {
            TweetCommentResult.Comment comment = new TweetCommentResult.Comment();
            comment.content = commentEntity.getContent();
            if (commentEntity.getToid() != null && commentEntity.getToid() > 9999) {
                IUserLoginEntity user = userDao.findUserById(commentEntity.getToid());
                comment.content = "[回复:" + user.getNickname() + "] " + comment.content;
            }
            comment.pubDate = convertToDate(commentEntity.getTime());
            comment.author = commentEntity.getNickname();
            comment.authorid = commentEntity.getUid();
            comment.portrait = convertToAvatarUrl(commentEntity.getIconUrl(), commentEntity.getUid(), false);
            comment.id = commentEntity.getCid();
            comment.appclient = 0;
            comments.add(comment);
        }

        TweetCommentResult commentResult = new TweetCommentResult();
        TweetCommentResult.Comments commentWrapper = new TweetCommentResult.Comments(comments);
        commentResult.all_count = allCount;
        commentResult.page_size = allCount;
        commentResult.comments = commentWrapper;
        commentResult.notice = getNotice(uid);
        return commentResult;
    }


    public TweetCommentPushResult pushComment(int id, int uid, int replyId, String content, int toUid, String toNickname, Boolean isHelp) {
        isHelp = isHelp == null ? false : isHelp;
        TweetCommentPushResult pushResult = new TweetCommentPushResult();
        Result result = new Result();
        result.setErrorCode("0");

        if (content == null || id < 0 || uid < 10000) {
            result.setErrorMessage("参数错误");
            pushResult.result = result;
            return pushResult;
        }

        String lastContent = null;
        try {
            IRecordCommentEntity commentEntity = streamDao.findLastCommentBy(uid, isHelp);
            lastContent = commentEntity.getContent();
        } catch (EmptyResultDataAccessException e) {
        }
        if (content.equals(lastContent)) {
            result.setErrorMessage("不要贪心噢，不能回复相同的内容");
            pushResult.result = result;
            return pushResult;
        }

        int cid = streamDao.saveComment(id, uid, content, toUid, isHelp);
        if (isHelp) {
            return getTweetHelpPushResult(id, uid, content, toUid, toNickname, pushResult, result, cid);
        }

        return getTweetCommentPushResult(id, uid, content, toUid, toNickname, pushResult, result, cid, replyId);
    }

    private TweetCommentPushResult getTweetHelpPushResult(int id, int uid, String content, int toUid, String toNickname, TweetCommentPushResult pushResult, Result result, int cid) {

        IRecordSayEntity recordSayEntity = null;
        IUserLoginEntity userLoginEntity = null;
        IAuMailSendEntity auMailSendEntity = null;
        if (cid > 0) {
            userLoginEntity = userDao.findUserById(uid);
            int rank = convertToRank(userLoginEntity.getActive());
            try {
                recordSayEntity = streamDao.findOneTweetBy(id);
            } catch (EmptyResultDataAccessException e) {
                result.setErrorMessage("未找到或已被删除");
                pushResult.notice = getNotice(uid);
                pushResult.result = result;
                return pushResult;
            }
            if (recordSayEntity.getUid().intValue() != uid) {
                long noticeId = AppUtil.toNotice(recordSayEntity.getUid());
                streamDao.saveNotice(uid, "stream/ih-para:newHelp", recordSayEntity.getSid(), noticeId);
                try {
                    auMailSendEntity = streamDao.findAuMailSend(recordSayEntity.getUid(), recordSayEntity.getSid(), uid);
                } catch (EmptyResultDataAccessException e) {
                    IUserLoginEntity helpRecordOwner = userDao.findUserById(recordSayEntity.getUid());
                    String body = "<p>" + helpRecordOwner.getNickname() + " 童鞋  <br />您的求助有了新回复! 看能对您有什么促进不? <br />"
                            + recordSayEntity.getContent() + "<br>" + userLoginEntity.getNickname() + ":" + content + " 快去看看吧!</p><br /><p style='color:gray; font-size:12px; font-style:italic;'>"
                            + helpRecordOwner.getNickname() + "校园帮助主题社交网站 - <a href='http://ihelpoo.cn'>我帮圈圈</a>敬上!" +
                            "<a href='http://www.weibo.com/ihelpoogroup' style='font-size:10px; color:gray'>(新浪微博)</a><br />" + helpRecordOwner.getNickname() + "天天开心:D 祝好</p>";
                    AppUtil.sendMail(helpRecordOwner.getEmail(), "有人来帮助您啦 快来看看吧 - 我帮圈圈", body);
                    int i = streamDao.saveAuMailSend(recordSayEntity.getUid(), uid, recordSayEntity.getSid(), "2");
                }
            } else {
                if (toUid > 9999) {
                    long noticeId = AppUtil.toNotice(toUid);
                    streamDao.saveNotice(uid, "stream/ih-para:reply", recordSayEntity.getSid(), noticeId);
                }
            }

            streamDao.incSayCount(id, rank >= 2, "comment_co", auMailSendEntity == null && recordSayEntity.getUid().intValue() != uid);
        }

        if (recordSayEntity.getUid().intValue() != uid) {
            int affected = userDao.incUserStatusIfLessThan("active_c_limit", 15, uid);

            if (affected > 0) {
                userDao.incUserLoginActive(uid, 1);
                userDao.saveMsgActive("add", uid, fetchUserActive(userLoginEntity), 1, "回复帮助 (每天最多加15次，包含评论回复他人的记录次数)");
            }
        }

        IRecordHelpEntity helpEntity = streamDao.findRecordHelp(recordSayEntity.getSid());
        streamDao.updateRecordHelp(helpEntity.getHid(), "2", "3");


        final Pattern AT_PATTERN = Pattern.compile("@[\\u4e00-\\u9fa5\\w\\-]+");
        Matcher matcher = AT_PATTERN.matcher(content);
        while (matcher.find()) {
            String atUserName = matcher.group().substring(1);
            try {
                IUserLoginEntity userLoginEntity1 = userDao.findUserByNickname(atUserName);
                streamDao.saveMsgAt(userLoginEntity1.getUid(), uid, id, cid);
            } catch (Exception e) {
                logger.error("Fail to at user:", e);
            }
        }


        //返回结果供显示
        TweetCommentResult.Comment comment = new TweetCommentResult.Comment();
        comment.content = toUid > 9999 ? "[回复" + toNickname + "] " + content : content;
        comment.pubDate = (new java.text.SimpleDateFormat(DEFAULT_DATE_FORMAT)).format(new Date());
        comment.author = userLoginEntity.getNickname();
        comment.authorid = uid;
        comment.portrait = convertToAvatarUrl(userLoginEntity.getIconUrl(), uid, false);
        comment.id = id;
        comment.appclient = 0;

        result.setErrorCode("1");
        result.setErrorMessage("帮助回复成功");
        pushResult.result = result;
        pushResult.notice = getNotice(uid);
        pushResult.comment = comment;
        return pushResult;
    }

    private TweetCommentPushResult getTweetCommentPushResult(int id, int uid, String content, int toUid, String toNickname, TweetCommentPushResult pushResult, Result result, int cid, int replyId) {
        IRecordSayEntity recordSayEntity = null;
        IUserLoginEntity userLoginEntity = null;
        if (cid > 0) {
            userLoginEntity = userDao.findUserById(uid);
            int rank = convertToRank(userLoginEntity.getActive());
            try {
                recordSayEntity = streamDao.findOneTweetBy(id);
            } catch (EmptyResultDataAccessException e) {
                result.setErrorMessage("未找到或已被删除");
                pushResult.notice = getNotice(uid);
                pushResult.result = result;
                return pushResult;
            }
            streamDao.incSayCount(id, rank >= 2, "comment_co", true);
        }

        int affected = userDao.incUserStatusIfLessThan("active_c_limit", 15, uid);
        if (affected > 0) {
            userDao.incUserLoginActive(uid, 1);
            userDao.saveMsgActive("add", uid, fetchUserActive(userLoginEntity), 1, "评论或回复他人的记录 (每天最多加15次，包含回复帮助次数)");
        }
        if (uid != recordSayEntity.getUid()  && recordSayEntity.getUid() != toUid) {
            streamDao.saveMsgComment(recordSayEntity.getUid(), id, cid, uid, -1);
        }

        if(toUid > 9999){
            streamDao.saveMsgComment(toUid, id, cid, uid, replyId);
        }

        final Pattern AT_PATTERN = Pattern.compile("@[\\u4e00-\\u9fa5\\w\\-]+");
        Matcher matcher = AT_PATTERN.matcher(content);
        while (matcher.find()) {
            String atUserName = matcher.group().substring(1);
            try {
                IUserLoginEntity userLoginEntity1 = userDao.findUserByNickname(atUserName);
                streamDao.saveMsgAt(userLoginEntity1.getUid(), uid, id, cid);
            } catch (Exception e) {
                logger.error("Fail to at user:", e);
            }
        }


        //返回结果供显示
        TweetCommentResult.Comment comment = new TweetCommentResult.Comment();
        comment.content = toUid > 9999 ? "[回复" + toNickname + "] " + content : content;
        comment.pubDate = (new java.text.SimpleDateFormat(DEFAULT_DATE_FORMAT)).format(new Date());
        comment.author = userLoginEntity.getNickname();
        comment.authorid = uid;
        comment.portrait = convertToAvatarUrl(userLoginEntity.getIconUrl(), uid, false);
        comment.id = id;
        comment.appclient = 0;

        result.setErrorCode("1");
        result.setErrorMessage("回复成功");
        pushResult.result = result;
        pushResult.notice = getNotice(uid);
        pushResult.comment = comment;
        return pushResult;
    }


    private int fetchUserActive(IUserLoginEntity userLoginEntity) {
        return userLoginEntity.getActive() == null ? 1 : userLoginEntity.getActive() + 1;
    }

    private String[] fetchLinks(List<IRecordOutimgEntity> imgLinkEntities) {
        return new String[0];
    }

    @Transactional
    public int pubTweet(int uid, long t, String content, Integer reward, String imgPath, String by, int schoolId) {

        IUserLoginEntity loginEntity = userDao.findUserById(uid);
        IUserStatusEntity statusEntity = userDao.findUserStatusById(uid);
        long twelveHours = t - 43200;
        int saysCountWithin12Hours = streamDao.findCountOfSaysWithin(uid, twelveHours);
        if (saysCountWithin12Hours >= statusEntity.getRecordLimit()) {
            return ERR_PUB_TOO_MANY;
        }

        if (isHelp(reward) && loginEntity.getActive() - reward < 0) {
            return ERR_ACTIVE_NOT_ENOUGH;
        }

        String lastSay = null;
        try {
            IRecordSayEntity sayEntity = streamDao.findLastTweetBy(uid);
            lastSay = sayEntity.getContent();
        } catch (EmptyResultDataAccessException e) {
        }
        if (content.equals(lastSay)) {
            return ERR_DUPLICATED_CONTENT;
        }


        int sayId = -1, imgId = -1;
        if (!StringUtils.isEmpty(imgPath)) {
            imgId = userDao.saveOutimg(uid, t, Constant.IMG_STORAGE_ROOT + imgPath);
        }
        String imageIds = imgId == -1 ? "" : String.valueOf(imgId);
        sayId = userDao.saveSay(uid, t, content, imageIds, reward, by, schoolId);


        int recordUserLoginActive = 0;
        Integer active = loginEntity.getActive() == null ? 0 : loginEntity.getActive();
        if (statusEntity.getActiveSLimit() != null && statusEntity.getActiveSLimit() < 3) {
            recordUserLoginActive = active + 3;
            userDao.saveMsgActive("add", uid, active, 3, "写新记录或求助 (每天最多加3次)");
        } else {
            recordUserLoginActive = active;
        }

        userDao.incStatus(uid, 1);

        if (isHelp(reward)) {
            List<IUserPriorityEntity> priorityEntities = userDao.findFollowersBy(uid, 0, Integer.MAX_VALUE);
            for (IUserPriorityEntity priorityEntity : priorityEntities) {
                long noticeId = AppUtil.toNotice(priorityEntity.getUid());
                streamDao.saveNotice(uid, "stream/ih-para:needhelp", sayId, noticeId);

                IUserLoginEntity loginEntity1 = userDao.findUserById(priorityEntity.getUid());
                if (!StringUtils.isEmpty(loginEntity1.getEmail())) {
                    helpstatusNeed(loginEntity1.getEmail(), loginEntity1.getNickname(), loginEntity.getNickname(), content, "ihelpoo.cn", "");
                }
            }

            streamDao.saveHelpData(sayId, reward);
            userDao.saveMsgActive("min", uid, recordUserLoginActive, reward, "求帮助使用");
            recordUserLoginActive -= reward;
        }

        userDao.updateActive(uid, recordUserLoginActive);

        //TODO AT users


        return sayId;
    }


    public void helpstatusNeed(String to, String toWhom, String who, String content, String schoolDomain, String schoolName) {
        String subject = who + "有困难了需要您的帮助，快来看看吧 - 我帮圈圈";
        StringBuilder body = new StringBuilder();
        body.append("<p>").append(toWhom).append("童鞋  <br />您的朋友 ").append(who).append(" 有困难了，需要您的帮助 help~ </p> <p style='font-size:12px'> ")
                .append(content).append("<a href='http://").append(schoolDomain).append("'>快去看看吧!</a></p><br><p style='color:gray; font-size:12px; font-style:italic;'>")
                .append(schoolName).append("校园帮助主题社交网站 - <a href='http://").append(schoolDomain).append("/'>我帮圈圈</a>敬上!<a href='http://www.weibo.com/ihelpoo' style='font-size:10px; color:gray'>(新浪微博)</a><br />")
                .append(toWhom).append("天天开心:D 祝好</p>");
        AppUtil.sendMail(to, subject, body.toString());
    }

    private boolean isHelp(Integer reward) {
        return reward != null && reward >= 0;
    }

    @Transactional
    public GenericResult plus(Integer sid, Integer uid) {
        GenericResult genericResult = new GenericResult();
        Result result = new Result();
        result.setErrorCode("0");

        if (sid == null || uid == null) {
            result.setErrorMessage("参数错误：sid=" + sid + " uid=" + uid);
            genericResult.setResult(result);
            return genericResult;
        }

        IRecordSayEntity sayEntity = null;
        try {
            sayEntity = streamDao.findOneTweetBy(sid);
        } catch (EmptyResultDataAccessException e) {
        }

        if (sayEntity == null) {
            result.setErrorMessage("查看的记录不存在或已被删除");
            genericResult.setResult(result);
            return genericResult;
        }

        String noticeType = "i";
        if ("1".equals(sayEntity.getSayType())) {
            noticeType = "ih";
        }


        IRecordPlusEntity plusEntity = null;
        try {
            plusEntity = streamDao.findPlusBy(sid, uid);
            streamDao.deletePlus(plusEntity.getId());
            incrPlusCountOfRecordBy(sid, -1);
            IMsgNoticeEntity noticeEntity = streamDao.findMsgNotice("stream/" + noticeType + "-para:plus", uid, sid, "plus");
            deleteNoticeMessage(noticeEntity.getNoticeId());
            bounceNoticeMessageCount(sayEntity.getUid(), -1);
            deliverBack(sayEntity.getUid(), noticeEntity.getNoticeId());
        } catch (EmptyResultDataAccessException e) {//to plus
            streamDao.addPlus(sid, uid);
            incrPlusCountOfRecordBy(sid, 1);
            long noticeIdForOwner = streamDao.saveNoticeMessage(noticeType, uid, sid, "plus");
            bounceNoticeMessageCount(sayEntity.getUid(), 1);
            deliverTo(sayEntity.getUid(), noticeIdForOwner);
        } catch (IncorrectResultSizeDataAccessException e) {
            logger.error("系统错误:", e);
            result.setErrorMessage("系统错误:" + e.getMessage());
            genericResult.setResult(result);
            return genericResult;
        }

        result.setErrorCode("1");
        result.setErrorMessage("操作成功");
        genericResult.setResult(result);
        genericResult.setNotice(getNotice(uid));
        return genericResult;
    }


    private void deleteNoticeMessage(long noticeId) {
        streamDao.deleteNoticeMessage(noticeId);
    }

    private int incrPlusCountOfRecordBy(Integer sid, int offset) {
        return streamDao.incOrDecSayCount(sid, offset);
    }

    public GenericResult diffuse(Integer sid, Integer uid, String content) {
        GenericResult genericResult = new GenericResult();
        Result result = new Result();
        result.setErrorCode("0");

        if (sid == null || uid == null) {
            result.setErrorMessage("参数错误：sid=" + sid + " uid=" + uid);
            genericResult.setResult(result);
            return genericResult;
        }


        IRecordSayEntity sayEntity = null;
        try {
            sayEntity = streamDao.findOneTweetBy(sid);
        } catch (EmptyResultDataAccessException e) {
        }

        if (sayEntity == null) {
            result.setErrorMessage("查看的记录不存在或已被删除");
            genericResult.setResult(result);
            return genericResult;
        }

        String noticeType = "i";
        if ("1".equals(sayEntity.getSayType())) {
            noticeType = "ih";
        }

        try {
            streamDao.findDiffusion(sid, uid);
            result.setErrorMessage("你已扩散了这条消息");
            genericResult.setResult(result);
            return genericResult;
        } catch (EmptyResultDataAccessException e) {
        }


        long time12Hour = System.currentTimeMillis() / 1000 - 43200L;
        List<IRecordDiffusionEntity> entities = streamDao.findDiffusions(uid, time12Hour);
        if (entities != null && entities.size() >= 3) {
            result.setErrorMessage("12小时内最多扩散3条");
            genericResult.setResult(result);
            return genericResult;
        }

        int diffuseId = streamDao.saveDiffusion(uid, sid, content);
        long noticeIdForFollowers = streamDao.saveNoticeMessage(noticeType, uid, diffuseId, "diffusion");
        increaseDiffusionsCountOfRecord(sid, uid);
        long noticeIdForOwner = streamDao.saveNoticeMessage(noticeType, uid, diffuseId, "diffusiontoowner");
        diffseIt(uid, noticeIdForOwner, noticeIdForFollowers, sayEntity.getUid());


        result.setErrorCode("1");
        result.setErrorMessage("操作成功");
        genericResult.setResult(result);
        genericResult.setNotice(getNotice(uid));
        return genericResult;
    }

    private void diffseIt(Integer uid, long noticeIdForOwner, long noticeIdForFollowers, Integer recordOwnerId) {
        List<IUserPriorityEntity> entities = userDao.findFollowersBy(uid, 0, Integer.MAX_VALUE);
        List<Integer> uids = new ArrayList<Integer>();
        for (IUserPriorityEntity entity : entities) {
            uids.add(entity.getUid());
        }
        saveDiffusionRelations(noticeIdForOwner, noticeIdForFollowers, uids, recordOwnerId);
    }

    private void saveDiffusionRelations(long noticeIdForOwner, long noticeIdForFollowers, List<Integer> uids, Integer recordOwnerId) {
        bounceNoticeMessageCount(recordOwnerId, 1);
        deliverTo(recordOwnerId, noticeIdForOwner);
        for (Integer uid : uids) {
            bounceNoticeMessageCount(uid, 1);
            deliverTo(uid, noticeIdForFollowers);
        }
    }

    private IRecordSayEntity increaseDiffusionsCountOfRecord(Integer sid, Integer uid) {
        IUserLoginEntity loginEntity = userDao.findUserById(uid);
        boolean canAffect = convertToLevel(loginEntity.getActive()) >= 2;
        IRecordSayEntity sayEntity = streamDao.findOneTweetBy(sid);
        streamDao.incSayCount(sid, canAffect, "diffusion_co", true);
        return sayEntity;
    }

    @Transactional
    public String uploadImage(int uid, MultipartHttpServletRequest request, long t) throws AppException {
        MultipartFile multipartFile = request.getFile("img");
        if (multipartFile.getSize() > 3670016L) {
            throw new AppException("上传图片太大，最大能上传单张3.5MB");
        }

//        int curSize = userDao.findSizeOfAlbum(uid);
        IUserLoginEntity loginEntity = userDao.findUserById(uid);
        int rank = this.convertToRank(loginEntity.getActive());
        BigInteger defaultSize = getAlbumSize(rank);
//        if (curSize > defaultSize.intValue()) {
//            throw new AppException("相册容量不够了，请联系我帮圈圈扩容");
//        }

        Map<String, String> lParams = new HashMap<String, String>();
        // 设置缩略图类型，必须搭配缩略图参数值（KEY_VALUE）使用，否则无效
        lParams.put(UpYun.PARAMS.KEY_X_GMKERL_TYPE.getValue(), UpYun.PARAMS.VALUE_FIX_MAX.getValue());
        // 设置缩略图参数值，必须搭配缩略图类型（KEY_TYPE）使用，否则无效
        lParams.put(UpYun.PARAMS.KEY_X_GMKERL_VALUE.getValue(), "150");
        // 设置缩略图的质量，默认 95
        lParams.put(UpYun.PARAMS.KEY_X_GMKERL_QUALITY.getValue(), "95");

        String imgOldName = multipartFile.getOriginalFilename();
        String imgPath = "";
        String thumbImgPath = "";
        UpYun upyun = new UpYun("ihelpoo", "api", "Ihelpoo.com");
        try {
            String imgName = "recordsay" + t + imgOldName.substring(imgOldName.lastIndexOf('.')).toLowerCase();
            imgPath = "/useralbum/" + uid + "/" + imgName;
            thumbImgPath = "/useralbum/" + uid + "/thumb_" + imgName;
            upyun.writeFile(imgPath, multipartFile.getBytes(), true, null);
            upyun.writeFile(thumbImgPath, multipartFile.getBytes(), true, lParams);
        } catch (IOException e) {
            throw new AppException("上传图片失败");
        }

        userDao.addImageToAlbum(uid, 2, Constant.IMG_STORAGE_ROOT + imgPath, multipartFile.getSize(), t);

        return imgPath;
    }


    /**
     * album degree configure
     * 1~3 0.5GB
     * 4~6 1GB
     * 7~8 4GB
     * 9>  16GB
     */
    public BigInteger getAlbumSize(int rank) {
        BigInteger GB = BigInteger.valueOf(1073741824);
        BigInteger albumSize = GB.divide(BigInteger.valueOf(2));
        if (rank <= 5) {
            albumSize = GB.divide(BigInteger.valueOf(2));
        } else if (rank <= 6) {
            albumSize = GB;
        } else if (rank <= 7) {
            albumSize = GB.multiply(BigInteger.valueOf(4));
        } else if (rank <= 10) {
            albumSize = GB.multiply(BigInteger.valueOf(16));
        }
        return albumSize;
    }

    @Transactional
    public GenericResult deleteTweet(Integer uid, Integer sid, Boolean isHelp) {
        GenericResult genericResult = new GenericResult();
        Result result = new Result();
        try {
            streamDao.deleteTweet(uid, sid);
            if (isHelp != null && isHelp) {
                streamDao.deleteCommment(-1, uid, sid);
            } else {
                streamDao.deleteHelpReply(-1, uid, sid);
            }
        } catch (Exception e) {
            result.setErrorCode("0");
            result.setErrorMessage(e.getMessage());
            genericResult.setResult(result);
            genericResult.setNotice(new Notice());
            return genericResult;
        }
        result.setErrorCode("1");
        result.setErrorMessage("操作成功");
        genericResult.setResult(result);
        genericResult.setNotice(new Notice());
        return genericResult;
    }


    public static void main(String[] args) {
        System.out.println(Arrays.asList(new String[]{"a", "b", "c"}).indexOf("c"));
        System.out.println(Arrays.asList(new String[]{"a", "b", "cd"}).indexOf("d"));
        System.out.println(Arrays.asList(new String[]{"a", "b", "c"}).indexOf("d"));
    }

    public TweetCommentPushResult replyComment(int id, int uid, int replyId, String content, int authorid, String author, Boolean help) {
        return pushComment(id, uid, replyId, content, authorid, author, help);
    }

    public GenericResult deleteComment(Integer authorid, Integer replyid, Boolean help, Integer sid) {
        GenericResult genericResult = new GenericResult();
        Result result = new Result();
        result.setErrorCode("0");
        int affected = 0;
        try {
            if (help != null && help) {
                affected = streamDao.deleteHelpReply(replyid, authorid, -1);
            } else {
                affected = streamDao.deleteCommment(replyid, authorid, -1);
            }
            if (affected == 0) {
                result.setErrorMessage("没有权限,删除评论出错啦");
                genericResult.setResult(result);
                return genericResult;
            }
        } catch (Exception e) {
            result.setErrorMessage("系统出错：" + e.getMessage());
            genericResult.setResult(result);
            return genericResult;
        }
        result.setErrorCode("1");
        result.setErrorMessage("操作成功");
        genericResult.setResult(result);
        genericResult.setNotice(new Notice());
        return genericResult;
    }
}
