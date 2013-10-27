package com.ihelpoo.api.service.base;

import com.ihelpoo.api.dao.NotificationDao;
import com.ihelpoo.api.model.obj.Notice;
import com.ihelpoo.api.service.WordService;
import com.ihelpoo.common.Constant;
import com.ihelpoo.api.dao.StreamDao;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public class RecordService {
    @Autowired
    protected StreamDao streamDao;
    @Autowired
    protected NotificationDao notificationDao;

    public Notice getNotice(int uid){
        Notice notice = new Notice();
        if(uid < 10000){
            return notice;
        }
        notice.activeCount = countActive(uid);
        notice.atmeCount = countAtme(uid);
        notice.chatCount = countChat(uid);
        notice.commentCount = countComment(uid);
        notice.systemCount = countSystem(uid);
        return notice;
    }

    public int countSystem(int uid) {
        if(uid < 10000){
            return 0;
        }
        String uidStr = String.valueOf(uid);
        Jedis jedis = new Jedis("localhost");
        String countStr = jedis.hget(WordService.R_NOTICE + WordService.R_SYSTEM + uidStr.substring(0, uidStr.length() - 3), uidStr.substring(uidStr.length() - 3));
        jedis.disconnect();
        return countStr == null ? 0 : Integer.parseInt(countStr);
    }

    public int countAtme(int uid) {
        return notificationDao.findNewAtmeCount(uid);
    }

    public int countComment(int uid) {
        return notificationDao.findNewCommentCount(uid);
    }

    public int countActive(int uid) {
        return notificationDao.findNewActiveCount(uid);
    }

    public int countChat(int uid) {
        return notificationDao.fineNewChatCount(uid);
    }

    protected String convertToOnlineState(String onlineCode) {
        return "1".equals(onlineCode) ? "在线" : "";
    }

    /**
     * TODO get real images urls
     *
     * @param sid
     * @return
     */
    protected String convertToImageUrl(int sid) {
        List<String> imgs = streamDao.findUserImgLinkEntitiesBy(sid);
        String imgUrl = "";
        if (imgs != null && imgs.size() > 0)
            imgUrl = imgs.get(0) + "?t=" + System.currentTimeMillis();
        return imgUrl;
    }

    protected String convertToAvatarUrl(String iconUrl, int uid) {
        if (!empty(iconUrl)) {
            return Constant.IMG_STORAGE_ROOT + "/useralbum/" + uid + "/" + iconUrl + "_s.jpg!app?t=" + System.currentTimeMillis();
        } else {
            return Constant.IMG_STORAGE_ROOT + "/useralbum/default_avatar.jpg!app";
        }
    }

    protected boolean empty(String iconUrl) {
        return iconUrl == null || iconUrl.length() <= 0;
    }

    protected String convertToType(Integer type, String enteryear) {
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

    protected String getGrade(String $enteryear) {
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

    protected String convertToGossip(Integer sex, String birthday) {
        if(birthday == null){
            return "未知";
        }
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

    protected int convertToBy(String from) {
        return 0; //TODO get user's header of client
    }

    protected int convertToRank(Integer active) {
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

    protected String convertToDate(Integer time) {
        return (new java.text.SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss")).format(new Date((long) (time.floatValue() * 1000)));
    }

    protected int convertToLevel(Integer activeCredits) {
        return convertToRank(activeCredits);
    }
}
