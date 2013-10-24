package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.StreamDao;
import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.StreamResult;
import com.ihelpoo.api.model.UserWordResult;
import com.ihelpoo.api.model.entity.VUserDetailEntity;
import com.ihelpoo.api.model.obj.Active;
import com.ihelpoo.api.model.obj.Actives;
import com.ihelpoo.api.model.obj.Notice;
import com.ihelpoo.api.model.entity.IRecordSayEntity;
import com.ihelpoo.api.model.entity.IUserPriorityEntity;
import com.ihelpoo.api.model.entity.VTweetStreamEntity;
import com.ihelpoo.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    UserDao userDao;

    //TODO use hisName to find
    public UserWordResult pullUserActiveBy(Integer hisUid, String hisName, Integer uid, Integer pageIndex, Integer pageSize) {
        pageIndex = pageIndex == null ? Constant.DEFAULT_PAGE_INDEX : pageIndex;
        pageSize = pageSize == null ? Constant.DEFAULT_PAGE_SIZE : pageSize;

        VUserDetailEntity userDetailEntity = userDao.findUserDetailById(hisUid);


        List<IRecordSayEntity> recordSayEntities = streamDao.findTweetsBy(hisUid, pageIndex, pageSize);



        VUserDetailEntity entity = userDao.findUserDetailById(hisUid);
        com.ihelpoo.api.model.common.User user = new com.ihelpoo.api.model.common.User();
        user.academy_name =  entity.getAcademyName();
        user.active_credits = entity.getActiveCredits();
        user.birthday = entity.getBirthday();
        user.create_time = (new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date((long) (entity.getCreateTime() * 1000)));
        user.school_domain = entity.getSchoolDomain();
        user.enrol_time = entity.getEnrolTime();
        user.followers_count = entity.getFollowersCount();
        user.friends_count = entity.getFriendsCount();
        user.avatar_type = entity.getAvatarType();
        user.avatar_url = convertToAvatarUrl(entity.getAvatarUrl(), entity.getUid());
        user.self_intro = entity.getSelfIntro();
        user.login_time = (new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date((long) (entity.getLoginTime() * 1000)));
        user.login_days = entity.getLoginDays();
        user.major_name = entity.getMajorName();
        user.nickname = entity.getNickname();
        user.online_status = entity.getOnlineStatus();
        user.real_name = entity.getRealName();
        user.school_id = String.valueOf(entity.getSchoolId());
        user.school_name = entity.getSchoolName();
        user.gender = entity.getGender();
        user.web_theme = entity.getWebTheme();
        user.email_verified = entity.getEmailVerified();
        user.user_type = entity.getUserType();
        user.uid = entity.getUid();
        user.level =  convertToLevel(userDetailEntity.getActiveCredits());
        user.relation = convertToRelation(uid, hisUid);


//        UserWordResult.User user = new UserWordResult.User();
//        user.portrait = convertToAvatarUrl(userDetailEntity.getAvatarUrl(), hisUid);
//        user.from = userDetailEntity.getAcademyName();
//        user.latestonline = String.valueOf(userDetailEntity.getFollowersCount());
//        user.relation = String.valueOf(userDetailEntity.getFriendsCount());
//        user.gender = convertToGossip(userDetailEntity.getGender(), userDetailEntity.getBirthday());
//        user.devplatform = userDetailEntity.getMajorName();
//        user.name = userDetailEntity.getNickname();
//        user.expertise = convertToRank(userDetailEntity.getActiveCredits());
//        user.jointime = convertToType(userDetailEntity.getUserType(), userDetailEntity.getEnrolTime());
//        user.uid = hisUid;

        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();
        Actives actives = new Actives();
        List<Active> activeList = new ArrayList<Active>();
        for (IRecordSayEntity recordSayEntity : recordSayEntities) {
            Active active = new Active();
            active.sid = recordSayEntity.getSid();
            active.iconUrl = convertToAvatarUrl(userDetailEntity.getAvatarUrl(), hisUid);
            active.nickname = userDetailEntity.getNickname();
            active.uid = hisUid;
            active.catalog = 4;
            active.objecttype = 3;
            active.objectcatalog = 0;
            active.objecttitle = "孤独";
            active.from = 3;
            active.url = "";
            active.objectID = 136727;

            active.content = recordSayEntity.getContent();

            active.commentCo = recordSayEntity.getCommentCo() == null ? 0 : recordSayEntity.getCommentCo();
            active.time = (new java.text.SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss")).format(new Date((long) (recordSayEntity.getTime().floatValue() * 1000)));
            active.image = convertToImageUrl(recordSayEntity.getImage());

            active.academy = userDetailEntity.getAcademyName();
            active.authorType = convertToType(userDetailEntity.getUserType(), userDetailEntity.getEnrolTime());
            active.rank = convertToRank(userDetailEntity.getActiveCredits());
            active.authorGossip = convertToGossip(userDetailEntity.getGender(), userDetailEntity.getBirthday());
            active.diffusionCo = recordSayEntity.getDiffusionCo() == null ? 0 : recordSayEntity.getDiffusionCo();
            active.online = 0;
            activeList.add(active);
        }

        actives.setActive(activeList);
        UserWordResult uar = new UserWordResult();
        uar.actives = actives;
        uar.notice = notice;
        uar.pagesize = recordSayEntities.size();
        uar.user = user;
        return uar;
    }

    // 0 没圈没屏蔽，1：圈， 2：屏蔽 3: 自己
    private int convertToRelation(Integer uid, Integer hisUid) {
        if(uid.equals(hisUid)){
            return 3;
        }
        int relation = 0;
        boolean friend = false, shield = false;
        try{
            userDao.findPrioritiesBy(uid, hisUid);
            friend = true;
        }catch (EmptyResultDataAccessException e){
        }
        try{
            userDao.findShieldBy(uid, hisUid);
            shield = true;
        }catch (EmptyResultDataAccessException e){
        }
        if(!friend && !shield){
            relation = 0;
        } else if(friend && !shield){
            relation = 1;
        } else if(shield && !friend){
            relation = 2;
        } else {
            relation = Integer.MAX_VALUE;
        }
        return relation;
    }

    public StreamResult pullBy(int uid, int catalog, int schoolId, int pageIndex, int pageSize) {
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
            if (pids.length() > 0)
                pids.deleteCharAt(pids.length() - 1);
            if (sids.length() > 0)
                sids.deleteCharAt(sids.length() - 1);
        }
        long t = System.currentTimeMillis();
        List<VTweetStreamEntity> tweets = streamDao.findAllTweetsBy(catalog, pids, sids, schoolId, pageIndex, pageSize);
        List<Active> actives = new ArrayList<Active>();
        for (VTweetStreamEntity tweet : tweets) {
            Active active = new Active();
            active.academy = "[" + tweet.getName() + "]";
            active.rank = convertToRank(tweet.getActive());
            active.online = tweet.getOnline() == null ? 0 : Integer.parseInt(tweet.getOnline().trim());
            active.iconUrl = convertToAvatarUrl(tweet.getIconUrl(), tweet.getUid());
            active.from = convertToBy(tweet.getFrom());
            active.catalog = 4;                     //my space in android
            active.commentCo = tweet.getCommentCo() == null ? 0 : tweet.getCommentCo();
            active.content = tweet.getContent();
            active.time = (new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date((long) (tweet.getTime().floatValue() * 1000)));
            active.diffusionCo = tweet.getDiffusionCo() == null ? 0 : tweet.getDiffusionCo();
            active.authorGossip = convertToGossip(tweet.getSex(), tweet.getBirthday());
            active.image = convertToImageUrl(tweet.getImage());
            active.nickname = tweet.getNickname();
            active.sid = tweet.getSid();
            active.authorType = convertToType(tweet.getType(), tweet.getEnteryear());
            active.uid = tweet.getUid();

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
        if (!empty(iconUrl)) {
            return Constant.IMG_STORAGE_ROOT + "/useralbum/" + uid + "/" + iconUrl + "_s.jpg!app?t=" + System.currentTimeMillis();
        } else {
            return Constant.IMG_STORAGE_ROOT + "/useralbum/default_avatar.jpg!app";
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
        if (birthday == null || !birthday.contains("-")) {
            return "未知";
        }
        String[] birthstring = birthday.split("-");
        String $birthmonth = birthstring[1];
        String $birthday = birthstring[2];
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

    private int convertToLevel(Integer activeCredits){
        return Integer.parseInt(convertToRank(activeCredits));
    }

}
