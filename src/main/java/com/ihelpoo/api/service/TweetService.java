package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.StreamDao;
import com.ihelpoo.api.model.base.Result;
import com.ihelpoo.common.AppUtil;
import com.ihelpoo.common.Constant;
import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.TweetCommentPushResult;
import com.ihelpoo.api.model.TweetCommentResult;
import com.ihelpoo.api.model.TweetDetailResult;
import com.ihelpoo.api.model.TweetResult;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.entity.*;
import com.ihelpoo.api.service.base.RecordService;
import com.ihelpoo.common.util.UpYun;
import com.ihelpoo.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class TweetService extends RecordService {

    private static final int ERR_PUB_TOO_MANY = -1;
    private static final int ERR_ACTIVE_NOT_ENOUGH = -2;
    private static final int ERR_DUPLICATED_CONTENT = -3;

    @Autowired
    UserDao userDao;

    @Autowired
    StreamDao streamDao;

    public TweetResult pullBy(int uid, int catalog, int schoolId, int pageIndex, int pageSize) {
        List<IUserPriorityEntity> priorityEntities = userDao.findAllPrioritiesByUid(uid);
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
            tweet.imgSmall = firstImgUrl.replace("recordsay", "thumb_recordsay");
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
        tweet.imgSmall = imgUrl.replace("recordsay", "thumb_recordsay");
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
            TweetCommentResult.Comment comment = new TweetCommentResult.Comment();
            comment.content = commentEntity.getContent();
            comment.pubDate = convertToDate(commentEntity.getTime());
            comment.author = commentEntity.getNickname();
            comment.authorid = commentEntity.getUid();
            comment.portrait = convertToAvatarUrl(commentEntity.getIconUrl(), commentEntity.getUid());
            comment.id = commentEntity.getSid() == null ? -1 : commentEntity.getSid();
            comment.appclient = 0;
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
        commentResult.allCount = allCount;
        commentResult.pagesize = pageSize;
        commentResult.comments = commentWrapper;
        commentResult.notice = notice;
        return commentResult;
    }


    public TweetCommentPushResult pushComment(int id, int uid, String[] atUsers, String content, int catalog) {
        return streamDao.pushComment(id, uid, atUsers, content, catalog);
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

        IRecordSayEntity sayEntity = streamDao.findLastTweetBy(uid);
        if (sayEntity != null && sayEntity.getContent().equals(content)) {
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

        userDao.updateStatus(uid, statusEntity.getActiveSLimit() + 1);

        if (isHelp(reward)) {
            List<IUserPriorityEntity> priorityEntities = userDao.findFollowersBy(uid);
            for (IUserPriorityEntity priorityEntity : priorityEntities) {
                AppUtil.saveNotice(uid, priorityEntity.getUid(), "stream/ih-para:needhelp", sayId);

                IUserLoginEntity loginEntity1 = userDao.findUserById(priorityEntity.getUid());
                if (!StringUtils.isEmpty(loginEntity1.getEmail())) {
                    helpstatusNeed(loginEntity1.getEmail(), loginEntity1.getNickname(), loginEntity.getNickname(), content, "schoolDomainTODO", "schoolNameTODO");
                }
            }

            streamDao.saveHelpData(sayId, reward);
            userDao.saveMsgActive("min", uid,recordUserLoginActive, reward,"求帮助使用");
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

    @Transactional
    public String uploadImage(int uid, MultipartHttpServletRequest request, long t) throws AppException {
        MultipartFile multipartFile = request.getFile("img");
        if (multipartFile.getSize() > 3670016L) {
            throw new AppException("上传图片太大，最大能上传单张3.5MB");
        }

        int curSize = userDao.findSizeOfAlbum(uid);
        IUserLoginEntity loginEntity = userDao.findUserById(uid);
        int rank = this.convertToRank(loginEntity.getActive());
        BigInteger defaultSize = getAlbumSize(rank);
        if (curSize > defaultSize.intValue()) {
            throw new AppException("相册容量不够了，请联系我帮圈圈扩容");
        }

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
}
