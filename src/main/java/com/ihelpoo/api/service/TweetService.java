package com.ihelpoo.api.service;

import com.ihelpoo.api.common.OoConstant;
import com.ihelpoo.api.dao.StreamDao;
import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.dao.UserPriorityDao;
import com.ihelpoo.api.model.TweetCommentPushResult;
import com.ihelpoo.api.model.TweetCommentResult;
import com.ihelpoo.api.model.TweetDetailResult;
import com.ihelpoo.api.model.TweetResult;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class TweetService {

    @Autowired
    StreamDao streamDao;

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

    private String convertToDate(Integer time) {
        return (new java.text.SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss")).format(new Date((long) (time.floatValue() * 1000)));
    }

    public TweetCommentResult pullCommentsBy(int sid, int catalog, int pageIndex, int pageSize) {
        List<VTweetCommentEntity> commentEntities = streamDao.findAllCommentssBy(sid, catalog, pageIndex, pageSize);
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

    private String convertToOnlineState(String onlineCode) {
        return "1".equals(onlineCode) ? "在线" : "";


    }

    /**
     * TODO get real images urls
     *
     * @param sid
     * @return
     */
    private String convertToImageUrl(int sid) {
        List<String> imgs = streamDao.findUserImgLinkEntitiesBy(sid);
        String imgUrl = "";
        if (imgs != null && imgs.size() > 0)
            imgUrl = imgs.get(0) + "?t=" + System.currentTimeMillis();
        return imgUrl;
    }

    private String convertToAvatarUrl(String iconUrl, int uid) {
        if (!empty(iconUrl)) {
            return OoConstant.IMG_STORAGE_ROOT + "useralbum/" + uid + "/" + iconUrl + "_s.jpg!app?t=" + System.currentTimeMillis();
        } else {
            return OoConstant.IMG_STORAGE_ROOT + "useralbum/default_avatar.jpg!app";
        }
    }

    private boolean empty(String iconUrl) {
        return iconUrl == null || iconUrl.length() <= 0;
    }

    private String convertToType(Integer type, String enteryear) {
        String authorType = "";
        if (type == 3) {
            authorType += "商家";
        } else if (type == 2) {
            authorType += "组织";
        } else {
            authorType += getGrade(enteryear);
        }
        return authorType;
    }

    private String getGrade(String $enteryear) {
        if (empty($enteryear)) {
            return "";
        }
        int $num;
        if (Calendar.getInstance().get(Calendar.MONTH) + 1 > 8) {
            $num = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt($enteryear) + 1;
        } else {
            $num = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt($enteryear);
        }
        switch ($num) {
            case 1:
                return "大一";
            case 2:
                return "大二";
            case 3:
                return "大三";
            case 4:
                return "大四";
            case 5:
                return "大五";
            default:
                return "毕业";
        }
    }

    private String convertToGossip(Integer sex, String birthday) {
        String gossip = "";
        if (empty(birthday)) {
            gossip += "";
        }
        String[] $birthstring = birthday.split("-");
        String $birthmonth = $birthstring[1];
        String $birthday = $birthstring[2];
        if (Integer.parseInt($birthday) < 10) {
            $birthday = "0" + $birthday;
        }
        int $birthint = Integer.parseInt($birthmonth + $birthday);
        if (101 <= $birthint && $birthint <= 119) {
            gossip += "摩羯";
        } else if (120 <= $birthint && $birthint <= 218) {
            gossip += "水瓶";
        } else if (219 <= $birthint && $birthint <= 320) {
            gossip += "双鱼";
        } else if (321 <= $birthint && $birthint <= 419) {
            gossip += "白羊";
        } else if (420 <= $birthint && $birthint <= 520) {
            gossip += "金牛";
        } else if (521 <= $birthint && $birthint <= 621) {
            gossip += "双子";
        } else if (622 <= $birthint && $birthint <= 722) {
            gossip += "巨蟹";
        } else if (723 <= $birthint && $birthint <= 822) {
            gossip += "狮子";
        } else if (823 <= $birthint && $birthint <= 922) {
            gossip += "处女";
        } else if (923 <= $birthint && $birthint <= 1023) {
            gossip += "天枰";
        } else if (1024 <= $birthint && $birthint <= 1122) {
            gossip += "天蝎";
        } else if (1123 <= $birthint && $birthint <= 1221) {
            gossip += "射手";
        } else if (1122 <= $birthint && $birthint <= 1231) {
            gossip += "摩羯";
        } else {
            gossip += "";
        }
        if (sex == 1) {
            gossip += "男";
        } else if (sex == 2) {
            gossip += "女";
        }
        return gossip;
    }

    private int convertToBy(String from) {
        return 0; //TODO get user's header of client
    }

    private int convertToRank(Integer active) {
        if (active == null)
            return 1;
        if (active < 30) {
            return 1;
        } else if (active < 120) {
            return 2;
        } else if (active < 330) {
            return 3;
        } else if (active < 720) {
            return 4;
        } else if (active < 1350) {
            return 5;
        } else if (active < 3990) {
            return 6;
        } else if (active < 10200) {
            return 7;
        } else if (active < 22230) {
            return 8;
        } else if (active < 41280) {
            return 9;
        } else {
            return 10;
        }
    }
}
