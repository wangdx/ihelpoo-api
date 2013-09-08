package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.MessageDao;
import com.ihelpoo.api.model.MessageResult;
import com.ihelpoo.api.model.UserWordResult;
import com.ihelpoo.api.model.base.Active;
import com.ihelpoo.api.model.base.Actives;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.base.ObjectReply;
import com.ihelpoo.api.model.entity.IMsgActiveEntity;
import com.ihelpoo.api.model.entity.IRecordDiffusionEntity;
import com.ihelpoo.api.model.entity.VMsgLoginEntity;
import com.ihelpoo.api.model.entity.VTweetDetailEntity;
import com.ihelpoo.api.service.base.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class WordService extends RecordService {

    public static final String R_ACCOUNT = "A:";
    public static final String R_MESSAGE = "M:";
    public static final String R_NOTICE = "N:";
    public static final String R_SYSTEM = "SY:";
    public static final String R_Notice_Message_Template = "Notice:Message:Template";
    public static final String R_Notice_Message_Link = "Notice:Message:Link";
    public static final String R_HOST = "42.62.50.238";

    @Autowired
    MessageDao messageDao;

    public UserWordResult fetchNotice(int uid, int catalog, int schoolId, int pageIndex, int pageSize) {

        switch (catalog) {
            case 2:
                return fetchComment(uid, schoolId, pageIndex, pageSize);
            default:
                break;
        }

        long t = System.currentTimeMillis();
        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();

        Jedis jedis = new Jedis(R_HOST);
        Set<String> notices = jedis.hkeys(R_ACCOUNT + R_MESSAGE + uid);


        String msgIdsStr = "";
        for (String msgId : notices) {
            msgIdsStr += msgId + ",";
        }
        if (msgIdsStr.length() > 0) {
            msgIdsStr = msgIdsStr.substring(0, msgIdsStr.length() - 1);
        }

        String uidStr = String.valueOf(uid);
        jedis.hdel(R_NOTICE + R_SYSTEM + uidStr.substring(0, uidStr.length() - 3), uidStr.substring(uidStr.length() - 3));
        List<VMsgLoginEntity> msgLoginEntities = messageDao.findNoticesByIds(msgIdsStr, pageIndex, pageSize);

        Actives actives = new Actives();
        UserWordResult.User user = new UserWordResult.User.Builder()
                .avatar("http://static.oschina.net/uploads/user/0/12_100.jpg?t=" + System.currentTimeMillis())
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
        List<Active> activeList = new ArrayList<Active>();
        for (VMsgLoginEntity msg : msgLoginEntities) {
            String view = "";
            if ("diffusiontoowner".equals(msg.getFormatId()) || "diffusion".equals(msg.getFormatId())) {
                IRecordDiffusionEntity diffusionEntity = null;
                try {
                    diffusionEntity = messageDao.findDiffusionBy(msg.getDetailId());
                } catch (EmptyResultDataAccessException e) {

                }

                if (diffusionEntity != null && !StringUtils.isEmpty(diffusionEntity.getView())) {
                    view = " 并表示：" + diffusionEntity.getView();
                }

            }


            String tpl = jedis.hget(R_Notice_Message_Template, msg.getFormatId());
            String content = String.format(tpl.replace("详情", "").replace("去看看", "").replace("去帮忙", ""), "", "", "", "", "");
            content += view;

            VTweetDetailEntity tweetDetailEntity = null;
            ObjectReply or = null;
            int sid = 0;
            if (msg.getDetailId() > 0) {
                try {
                    tweetDetailEntity = streamDao.findTweetDetailBy(msg.getDetailId());
                } catch (EmptyResultDataAccessException e) {
                    //eat it
                }
                if (tweetDetailEntity != null) {
                    or = new ObjectReply(tweetDetailEntity.getAuthor(), tweetDetailEntity.getContent());
                    sid = tweetDetailEntity.getSid();
                }
            }


            Active active = new Active.Builder()
                    .sid(msg.getDetailId())
                    .avatar(convertToAvatarUrl(msg.getIconUrl(), msg.getUid()))
                    .name(msg.getNickname())
                    .uid(msg.getUid())
                    .catalog(4)
                    .setObjecttype(3)
                    .setObjectcatalog(0)
                    .setObjecttitle("孤独")
                    .by(3)
                    .setUrl("")
                    .setObjectID(sid)
                    .content(content)
                    .commentCount(0)
                    .date(convertToDate(msg.getCreatTi()))
                    .online(Integer.parseInt(msg.getOnline()))
                    .setObjectreply(or)
                    .build();
            activeList.add(active);
        }
        actives.setActive(activeList);
        UserWordResult uar = new UserWordResult();
        uar.setNotice(notice);
        uar.setPagesize(20);
        uar.setActives(actives);
//        uar.setUser(user);

        jedis.disconnect();

        return uar;
    }

    private UserWordResult fetchComment(int uid, int schoolId, int pageIndex, int pageSize) {
        return null;
    }


    public MessageResult fetchActive(int uid, int pageIndex, int pageSize) {
        List<IMsgActiveEntity> actives = messageDao.findActivesByUid(uid, pageIndex, pageSize);
        List<MessageResult.Message> list = new ArrayList<MessageResult.Message>();
        for (IMsgActiveEntity active : actives) {
            MessageResult.Message msgs = new MessageResult.Message.Builder()
                    .id(active.getId())
                    .author("(" + active.getTotal() + ")")
                    .commentCount(2)
                    .pubDate(convertToDate(active.getTime()))
                    .title(" " + active.getReason())
                    .inout("min".equals(active.getWay()) ? "-" + active.getChange() : "+" + active.getChange())
                    .build();
            list.add(msgs);
        }
        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();
        MessageResult.Messages newslist = new MessageResult.Messages(list);
        MessageResult mr = new MessageResult();
        mr.setPagesize(20);
        mr.setNotice(notice);
        mr.setNewslist(newslist);
        return mr;
    }
}
