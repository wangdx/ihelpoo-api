package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.StreamDao;
import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.dao.UserPriorityDao;
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

    public TweetResult pullBy(int uid, int catalog, int pageIndex, int pageSize) {
        int offset = pageIndex * pageSize;
        List<IUserPriorityEntity> priorityEntities = userPriorityDao.findAllPrioritiesByUid(uid);
        StringBuilder pids = new StringBuilder();
        StringBuilder sids = new StringBuilder();
        List<IUserLoginEntity> pidGroupList = null;
        if (!priorityEntities.isEmpty()) {
            for (IUserPriorityEntity userPriorityEntity : priorityEntities) {
                if (null != userPriorityEntity.getPid()) {
                    pids.append(userPriorityEntity.getPid());
                } else if (null != userPriorityEntity.getSid()) {
                    sids.append(userPriorityEntity.getSid());
                }
            }
            pids.deleteCharAt(pids.length() - 1);
            sids.deleteCharAt(sids.length() - 1);
        }

        List<VTweetStreamEntity> tweetEntities = streamDao.findAllTweetsBy(catalog, pids, sids, pageIndex, pageSize);
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
                    .date((new java.text.SimpleDateFormat(
                            "yyyy-MM-dd hh:mm:ss")).format(new Date((long) (tweetEntity.getTime().floatValue() * 1000))))
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

        IRecordSayEntity tweetEntity = streamDao.findTweetBy(sid);
        IUserLoginEntity userLoginEntity = userDao.findUserById(tweetEntity.getUid());
        List<IRecordDiffusionEntity> spreads = streamDao.findSpreadsBySid(sid);
        int spreadCount = spreads == null ? 0 : spreads.size();
        List<VTweetSpreadEntity> spreadEntities = Collections.emptyList();
        if (spreadCount > 0) {
            spreadEntities = streamDao.findUserSpreadsBy(sid);
        }
        List<String> imgs = streamDao.findUserImgLinkEntitiesBy(sid);
        tweetEntity.setHitCo(tweetEntity.getHitCo() == null ? 1 : 1 + tweetEntity.getHitCo());
        streamDao.updateTweet(tweetEntity);


        long t = System.currentTimeMillis();
        String imgUrl = convertToImageUrl(tweetEntity.getSid());
        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();
        TweetResult.Tweet tweet = new TweetResult.Tweet.Builder()
                .spreads(spreadCount)
                .onlineState(convertToOnlineState(userLoginEntity.getOnline()))
                .from(convertToBy(tweetEntity.getFrom()))
                .content(tweetEntity.getContent())
                .date((new java.text.SimpleDateFormat(
                        "yyyy-MM-dd hh:mm:ss")).format(new Date((long) (tweetEntity.getTime().floatValue() * 1000))))
                .comments(tweetEntity.getCommentCo() == null ? 0 : tweetEntity.getCommentCo())
                .small(imgUrl)//TODO cope with
                .big(imgUrl)
                .avatar(convertToAvatarUrl(userLoginEntity.getIconUrl(), tweetEntity.getUid()))
                .authorRank(convertToRank(userLoginEntity.getActive()))
                .authorGossip(convertToGossip(userLoginEntity.getSex(), userLoginEntity.getBirthday()))
                .academy("[化学院]")
                .id(tweetEntity.getSid())
                .authorType(convertToType(userLoginEntity.getType(), userLoginEntity.getEnteryear()))
                .author(userLoginEntity.getNickname())
                .authorid(userLoginEntity.getUid())
                .build();
        TweetDetailResult tdr = new TweetDetailResult(tweet, notice);
        return tdr;
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

        String baseUrl = "http://ihelpoo-public.stor.sinaapp.com/";
        if (!empty(iconUrl)) {
            return baseUrl + "useralbum/" + uid + "/" + iconUrl + "_m.jpg?t=" + System.currentTimeMillis();
        } else {
            return "http://zzuli.sinaapp.com/Public/image/common/0.jpg?t=" + System.currentTimeMillis();
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
