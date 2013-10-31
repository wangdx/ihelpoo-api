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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import redis.clients.jedis.Jedis;

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

    private static final int ERR_PUB_TOO_MANY = -1;
    private static final int ERR_ACTIVE_NOT_ENOUGH = -2;
    private static final int ERR_DUPLICATED_CONTENT = -3;

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
            tweet.commentCount = tweetEntity.getCommentCo() == null ? 0 : tweetEntity.getCommentCo();
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
        tweetResult.tweetCount = pageSize + 1;
        tweetResult.pagesize = pageSize;
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
        tweet.commentCount = tweetDetailEntity.getCommentCo() == null ? 0 : tweetDetailEntity.getCommentCo();
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
            comment.portrait = convertToAvatarUrl(commentEntity.getIconUrl(), commentEntity.getUid(), false);
            comment.id = commentEntity.getSid() == null ? -1 : commentEntity.getSid();
            comment.appclient = 0;
            comments.add(comment);
        }

        TweetCommentResult commentResult = new TweetCommentResult();
        TweetCommentResult.Comments commentWrapper = new TweetCommentResult.Comments(comments);
        commentResult.allCount = allCount;
        commentResult.pagesize = pageSize;
        commentResult.comments = commentWrapper;
        commentResult.notice = new Notice();//FIXME
        return commentResult;
    }


    public TweetCommentPushResult pushComment(int id, int uid, String content) {
        TweetCommentPushResult pushResult = new TweetCommentPushResult();
        Result result = new Result();
        result.setErrorCode("0");

        int cid = streamDao.saveComment(id, uid, content);
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
            streamDao.incSayCount(id, rank >= 2, "comment_co");
        }

        int affected = userDao.incIfLessThan("active_c_limit", 15, uid);
        if (affected > 0) {
            userDao.incActive(uid, 1);
            userDao.saveMsgActive("add", uid, fetchUserActive(userLoginEntity), 1, "评论或回复他人的记录 (每天最多加15次，包含回复帮助次数)");
        }
        if (uid != recordSayEntity.getUid()) {
            streamDao.saveMsgComment(recordSayEntity.getUid(), id, cid, uid);
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


        TweetCommentResult.Comment comment = new TweetCommentResult.Comment();
        comment.content = content;
        comment.pubDate = (new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date(System.currentTimeMillis()));
        comment.author = userLoginEntity.getNickname();
        comment.authorid = uid;
        comment.portrait = convertToAvatarUrl(userLoginEntity.getIconUrl(), uid, false);
        comment.id = id;
        comment.appclient = 0;

        result.setErrorCode("1");
        result.setErrorMessage("评论成功");
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

        userDao.incStatus(uid, 1);

        if (isHelp(reward)) {
            List<IUserPriorityEntity> priorityEntities = userDao.findFollowersBy(uid, 0, Integer.MAX_VALUE);
            for (IUserPriorityEntity priorityEntity : priorityEntities) {
                AppUtil.saveNotice(uid, priorityEntity.getUid(), "stream/ih-para:needhelp", sayId);

                IUserLoginEntity loginEntity1 = userDao.findUserById(priorityEntity.getUid());
                if (!StringUtils.isEmpty(loginEntity1.getEmail())) {
                    helpstatusNeed(loginEntity1.getEmail(), loginEntity1.getNickname(), loginEntity.getNickname(), content, "schoolDomainTODO", "schoolNameTODO");
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
            int noticeIdForOwner = streamDao.saveNoticeMessage(noticeType, uid, sid, "plus");
            bounceNoticeMessageCount(sayEntity.getUid(), 1);
            deliverTo(sayEntity.getUid(), noticeIdForOwner);
        }

        result.setErrorCode("1");
        result.setErrorMessage("操作成功");
        genericResult.setResult(result);
        genericResult.setNotice(getNotice(uid));
        return genericResult;
    }


    private void deliverTo(Integer uid, long noticeId) {
        String uidStr = String.valueOf(uid);
        Jedis jedis = new Jedis("localhost");
        jedis.hset(WordService.R_ACCOUNT + WordService.R_MESSAGE + uidStr, String.valueOf(noticeId), "0");
        jedis.disconnect();
    }

    private void deliverBack(Integer uid, long noticeId) {
        String uidStr = String.valueOf(uid);
        Jedis jedis = new Jedis("localhost");
        jedis.hdel(WordService.R_ACCOUNT + WordService.R_MESSAGE + uidStr, String.valueOf(noticeId));
        jedis.disconnect();
    }

    private void bounceNoticeMessageCount(Integer uid, int offset) {
        String uidStr = String.valueOf(uid);
        Jedis jedis = new Jedis("localhost");
        jedis.hincrBy(WordService.R_NOTICE + WordService.R_SYSTEM + uidStr.substring(0, uidStr.length() - 3), uidStr.substring(uidStr.length() - 3), offset);
        jedis.disconnect();
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
        int noticeIdForFollowers = streamDao.saveNoticeMessage(noticeType, uid, diffuseId, "diffusion");
        increaseDiffusionsCountOfRecord(sid, uid);
        int noticeIdForOwner = streamDao.saveNoticeMessage(noticeType, uid, diffuseId, "diffusiontoowner");
        diffseIt(uid, noticeIdForOwner, noticeIdForFollowers, sayEntity.getUid());


        result.setErrorCode("1");
        result.setErrorMessage("操作成功");
        genericResult.setResult(result);
        genericResult.setNotice(getNotice(uid));
        return genericResult;
    }

    private void diffseIt(Integer uid, int noticeIdForOwner, int noticeIdForFollowers, Integer recordOwnerId) {
        List<IUserPriorityEntity> entities = userDao.findFollowersBy(uid, 0, Integer.MAX_VALUE);
        List<Integer> uids = new ArrayList<Integer>();
        for (IUserPriorityEntity entity : entities) {
            uids.add(entity.getUid());
        }
        saveDiffusionRelations(noticeIdForOwner, noticeIdForFollowers, uids, recordOwnerId);
    }

    private void saveDiffusionRelations(int noticeIdForOwner, int noticeIdForFollowers, List<Integer> uids, Integer recordOwnerId) {
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
        streamDao.incSayCount(sid, canAffect, "diffusion_co");
        return sayEntity;
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

    @Transactional
    public GenericResult deleteTweet(Integer uid, Integer sid) {
        GenericResult genericResult = new GenericResult();
        Result result = new Result();
        try {
            streamDao.deleteTweet(uid, sid);
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
        System.out.println(Arrays.asList(new String[]{"a","b","c"}).indexOf("c"));
        System.out.println(Arrays.asList(new String[]{"a","b","cd"}).indexOf("d"));
        System.out.println(Arrays.asList(new String[]{"a","b","c"}).indexOf("d"));
    }

}
