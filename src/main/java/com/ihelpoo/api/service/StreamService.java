package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.StreamDao;
import com.ihelpoo.api.dao.UserPriorityDao;
import com.ihelpoo.api.model.StreamResult;
import com.ihelpoo.api.model.UserActiveResult;
import com.ihelpoo.api.model.base.Active;
import com.ihelpoo.api.model.base.Actives;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.entity.IUserLoginEntity;
import com.ihelpoo.api.model.entity.IUserPriorityEntity;
import com.ihelpoo.api.model.entity.VTweetStreamEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class StreamService {
    @Autowired
    StreamDao streamDao;

    @Autowired
    UserPriorityDao userPriorityDao;

    public UserActiveResult pullUserActiveBy(int uid, String hisname, int hisuid, int pageIndex, int pageSize){
        long t = System.currentTimeMillis();
        UserActiveResult.User user = new UserActiveResult.User.Builder()
                .avatar("http://static.oschina.net/uploads/user/0/12_100.jpg?t="+System.currentTimeMillis())
                .academy("文学与传媒学院")
                .foer("12")
                .foing("21")
                .gossip("狮子女")
                .major("汉语言文学")
                .nickname("孤独不苦")
                .rank("6")
                .type("大四")
                .uid(123456)
                .build();
        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();
        int pagesize = 20;
        Actives actives = new Actives();
        Active active0 = new Active.Builder()
                .sid(24968)
                .avatar("http://ihelpoo-public.stor.sinaapp.com/useralbum/10116/101161364749925_m.jpg?t=" + t)
                .name("陈源杉")
                .uid(10116)
                .catalog(4)
                .setObjecttype(3)
                .setObjectcatalog(0)
                .setObjecttitle("孤独")
                .by(3)
                .setUrl("")
                .setObjectID(136727)
                .content("还没断网哦！得瑟一个……千万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊")
                .commentCount(0)
                .date("2012-06-14 22:00:22")
                .image("http://ihelpoo-public.stor.sinaapp.com/useralbum/12781/thumb_recordsay1368613690.jpg?t=" + t)
                .academy("[汉语言文学1]")
                .type("大三")
                .rank("2")
                .gossip("处女女")
                .diffusionCount(2)
                .online(0)
                .build();
                Active active1 = new Active.Builder()
                .sid(24968)
                .avatar("http://ihelpoo-public.stor.sinaapp.com/useralbum/10116/101161364749925_m.jpg?t=" + t)
                .name("陈源杉")
                .uid(10116)
                .catalog(4)
                .setObjecttype(3)
                .setObjectcatalog(0)
                .setObjecttitle("孤独")
                .by(3)
                .rank("2")
                .setUrl("")
                .setObjectID(136727)
                .content("还没断网哦！得瑟一个……千万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊")
                .commentCount(0)
                .date("2012-06-14 22:00:22")
                .image("http://ihelpoo-public.stor.sinaapp.com/useralbum/12781/thumb_recordsay1368613690.jpg?t=" + t)
                .academy("[汉语言文学1]")
                .type("大三")
                .gossip("处女女")
                .diffusionCount(2)
                .online(0)
                .build();
        Active active2 = new Active.Builder()
                .sid(24968)
                .rank("2")
                .avatar("http://ihelpoo-public.stor.sinaapp.com/useralbum/10116/101161364749925_m.jpg?t=" + t)
                .name("李云龙")
                .uid(10116)
                .catalog(4)
                .setObjecttype(3)
                .setObjectcatalog(0)
                .setObjecttitle("TEST")
                .by(2)
                .setUrl("")
                .setObjectID(136727)
                .content("还没断网哦！得瑟一个……千万别刚得瑟完就断了啊…… ")
                .commentCount(3)
                .date("2013-06-14 22:00:22")
                .academy("[汉语言文学]")
                .type("大四")
                .gossip("狮子女")
                .diffusionCount(4)
                .online(1)
                .build();
        Active active3 = new Active.Builder()
                .sid(24968)
                .avatar("http://ihelpoo-public.stor.sinaapp.com/useralbum/10116/101161364749925_m.jpg?t=" + t)
                .name("李云龙")
                .rank("2")
                .uid(10116)
                .catalog(4)
                .setObjecttype(3)
                .setObjectcatalog(0)
                .setObjecttitle("TEST")
                .by(3)
                .setUrl("")
                .setObjectID(136727)
                .content("还没断网哦！得瑟一个……千万别刚得瑟完就断了啊…… ")
                .commentCount(3)
                .date("2013-06-14 22:00:22")
                .academy("[汉语言文学]")
                .type("大四")
                .gossip("狮子男")
                .diffusionCount(4)
                .online(1)
                .build();
        Active active4 = new Active.Builder()
                .sid(24968)
                .avatar("http://ihelpoo-public.stor.sinaapp.com/useralbum/10116/101161364749925_m.jpg?t=" + t)
                .name("李云龙")
                .uid(10116)
                .rank("4")
                .catalog(4)
                .setObjecttype(3)
                .setObjectcatalog(0)
                .setObjecttitle("TEST")
                .by(3)
                .setUrl("")
                .setObjectID(136727)
                .content("还没断网哦！得瑟一个……千万别刚得瑟完就断了啊…… ")
                .commentCount(3)
                .date("2013-06-14 22:00:22")
                .academy("[汉语言文学]")
                .type("大四")
                .gossip("狮子男")
                .diffusionCount(4)
                .online(1)
                .build();
        List<Active> activeList = new ArrayList<Active>();
        activeList.add(active0);
        activeList.add(active1);
        activeList.add(active2);
        activeList.add(active3);
        activeList.add(active4);
        actives.setActive(activeList);
        UserActiveResult uar = new UserActiveResult();
        uar.setNotice(notice);
        uar.setPagesize(20);
        uar.setActives(actives);
        uar.setUser(user);

        return uar;
    }

    public StreamResult pullBy(int uid, int catalog, int pageIndex, int pageSize) {
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
        StreamResult streamResult = new StreamResult();
        long t = System.currentTimeMillis();
        List<VTweetStreamEntity> tweets = streamDao.findAllTweetsBy(catalog, pids, sids, pageIndex, pageSize);
        List<Active> actives = new ArrayList<Active>();
        for (VTweetStreamEntity tweet : tweets) {
            Active active = new Active.Builder()
                    .academy("[" + tweet.getName() + "]")
                    .rank(convertToRank(tweet.getActive()))
                    .online(tweet.getOnline() == null ? 0 : Integer.parseInt(tweet.getOnline().trim()))
                    .avatar(convertToAvatarUrl(tweet.getIconUrl(), tweet.getUid()))
                    .by(convertToBy(tweet.getFrom()))
                    .catalog(4)//my space in android
                    .commentCount(tweet.getCommentCo() == null ? 0 : tweet.getCommentCo())
                    .content(tweet.getContent())
                    .date((new java.text.SimpleDateFormat(
                            "yyyy-MM-dd hh:mm:ss")).format(new Date((long) (tweet.getTime().floatValue() * 1000))))
                    .diffusionCount(tweet.getDiffusionCo() == null ? 0 : tweet.getDiffusionCo())
                    .gossip(convertToGossip(tweet.getSex(), tweet.getBirthday()))
                    .image(convertToImageUrl(tweet.getImage()))
                    .name(tweet.getNickname())
                    .sid(tweet.getSid())
                    .type(convertToType(tweet.getType(), tweet.getEnteryear()))
                    .uid(tweet.getUid())
                    .build();
            actives.add(active);
        }
        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();

        StreamResult sr = new StreamResult();
        Actives activesWrapper = new Actives();
        activesWrapper.setActive(actives);
        sr.setActiveCount(0);
        sr.setPagesize(pageSize);
        sr.setNotice(notice);
        sr.setActivies(activesWrapper);
        return sr;
//
//
//        long t = System.currentTimeMillis();
//
//        List<StreamResult.Active> actives00 = new ArrayList<StreamResult.Active>();
//        StreamResult.Active active1 = new StreamResult.Active.Builder()
//                .sid(24968)
//                .avatar("http://ihelpoo-public.stor.sinaapp.com/useralbum/10116/101161364749925_m.jpg?t=" + t)
//                .name("陈源杉")
//                .uid(10116)
//                .catalog(4)
//                .setObjecttype(3)
//                .setObjectcatalog(0)
//                .setObjecttitle("孤独")
//                .by(3)
//                .setUrl("")
//                .setObjectID(136727)
//                .content("还没断网哦！得瑟一个……千万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊")
//                .commentCount(0)
//                .date("2012-06-14 22:00:22")
//                .image("http://ihelpoo-public.stor.sinaapp.com/useralbum/12781/thumb_recordsay1368613690.jpg?t=" + t)
//                .academy("[汉语言文学1]")
//                .type("大三")
//                .gossip("处女女")
//                .diffusionCount(2)
//                .online(0)
//                .rank(5)
//                .build();
//        StreamResult.Active active2 = new StreamResult.Active.Builder()
//                .sid(24968)
//                .avatar("http://ihelpoo-public.stor.sinaapp.com/useralbum/10116/101161364749925_m.jpg?t=" + t)
//                .name("李云龙")
//                .uid(10116)
//                .catalog(4)
//                .setObjecttype(3)
//                .setObjectcatalog(0)
//                .setObjecttitle("TEST")
//                .by(2)
//                .setUrl("")
//                .setObjectID(136727)
//                .content("还没断网哦！得瑟一个……千万别刚得瑟完就断了啊…… ")
//                .commentCount(3)
//                .date("2013-06-14 22:00:22")
//                .academy("[汉语言文学]")
//                .type("大四")
//                .gossip("狮子女")
//                .diffusionCount(4)
//                .online(1)
//                .rank(4)
//                .build();
//        StreamResult.Active active3 = new StreamResult.Active.Builder()
//                .sid(24968)
//                .avatar("http://ihelpoo-public.stor.sinaapp.com/useralbum/10116/101161364749925_m.jpg?t=" + t)
//                .name("李云龙")
//                .uid(10116)
//                .catalog(4)
//                .setObjecttype(3)
//                .setObjectcatalog(0)
//                .setObjecttitle("TEST")
//                .by(3)
//                .setUrl("")
//                .setObjectID(136727)
//                .content("还没断网哦！得瑟一个……千万别刚得瑟完就断了啊…… ")
//                .commentCount(3)
//                .date("2013-06-14 22:00:22")
//                .academy("[汉语言文学]")
//                .type("大四")
//                .gossip("狮子男")
//                .diffusionCount(4)
//                .online(1)
//                .rank(1)
//                .build();
//        StreamResult.Active active4 = new StreamResult.Active.Builder()
//                .sid(24968)
//                .avatar("http://ihelpoo-public.stor.sinaapp.com/useralbum/10116/101161364749925_m.jpg?t=" + t)
//                .name("李云龙")
//                .uid(10116)
//                .catalog(4)
//                .setObjecttype(3)
//                .setObjectcatalog(0)
//                .setObjecttitle("TEST")
//                .by(3)
//                .setUrl("")
//                .setObjectID(136727)
//                .content("还没断网哦！得瑟一个……千万别刚得瑟完就断了啊…… ")
//                .commentCount(3)
//                .date("2013-06-14 22:00:22")
//                .academy("[汉语言文学]")
//                .type("大四")
//                .gossip("狮子男")
//                .diffusionCount(4)
//                .online(1)
//                .rank(1)
//                .build();
//        actives00.add(active1);
//        actives00.add(active2);
//        actives00.add(active3);
//        actives00.add(active4);
//        Notice notice = new Notice.Builder()
//                .talk(0)
//                .system(0)
//                .comment(0)
//                .at(0)
//                .build();
//        StreamResult.Actives actives1 = new StreamResult.Actives();
//        actives1.setActive(actives00);
//
//        StreamResult sr = new StreamResult();
//        sr.setActiveCount(0);
//        sr.setPagesize(3);
//        sr.setNotice(notice);
//        sr.setActivies(actives1);
//        return sr;
    }

    /**
     * TODO get real images urls
     *
     * @param image
     * @return
     */
    private String convertToImageUrl(String image) {
        if (!empty(image)) {
            return "http://zzuli.sinaapp.com/Public/image/common/0.jpg?t=" + System.currentTimeMillis();
        } else {
            return "";
        }
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

    private String convertToRank(Integer active) {
        if (active == null)
            return "1";
        if (active < 30) {
            return "1";
        } else if (active < 120) {
            return "2";
        } else if (active < 330) {
            return "3";
        } else if (active < 720) {
            return "4";
        } else if (active < 1350) {
            return "5";
        } else if (active < 3990) {
            return "6";
        } else if (active < 10200) {
            return "7";
        } else if (active < 22230) {
            return "8";
        } else if (active < 41280) {
            return "9";
        } else {
            return "10";
        }
    }

}
