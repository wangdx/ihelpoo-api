package com.ihelpoo.api.service;

import com.ihelpoo.api.model.TweetCommentResult;
import com.ihelpoo.common.Constant;
import com.ihelpoo.api.dao.CommentDao;
import com.ihelpoo.api.dao.MessageDao;
import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.ChatResult;
import com.ihelpoo.api.model.MessageResult;
import com.ihelpoo.api.model.UserWordResult;
import com.ihelpoo.api.model.base.Active;
import com.ihelpoo.api.model.base.Actives;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.base.ObjectReply;
import com.ihelpoo.api.model.entity.*;
import com.ihelpoo.api.service.base.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class WordService extends RecordService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    public static final String R_ACCOUNT = "A:";
    public static final String R_MESSAGE = "M:";
    public static final String R_NOTICE = "N:";
    public static final String R_SYSTEM = "SY:";
    public static final String R_Notice_Message_Template = "Notice:Message:Template";
    public static final String R_Notice_Message_Link = "Notice:Message:Link";

    @Autowired
    MessageDao messageDao;

    @Autowired
    UserDao userDao;

    @Autowired
    CommentDao commentDao;

    public UserWordResult fetchNotice(int uid, int catalog, int schoolId, int pageIndex, int pageSize) {

        switch (catalog) {
            case 2:
            case 3:
                return fetchComment(uid, schoolId, pageIndex, pageSize, catalog);
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

        Jedis jedis = new Jedis(Constant.R_HOST);
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
            String content = String.format(replace(tpl, "详情|去看看|去帮忙", ""), "", "", "", "", "");
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
                    .catalog(3)
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


    private UserWordResult fetchComment(int uid, int schoolId, int pageIndex, int pageSize, int catalog) {
        if(catalog == 2){//AT_ME
            return fetchAt(uid, schoolId, pageIndex, pageSize);
        } else {
            return fetchComments(uid, pageIndex, pageSize);
        }
    }

    private UserWordResult fetchAt(int uid, int schoolId, int pageIndex, int pageSize) {
        List<VAtUserEntity> atUserEntities = commentDao.fetchAllAtBy(uid, pageIndex, pageSize);
        List<Active> activeList = new ArrayList<Active>();
        for(VAtUserEntity atUserEntity: atUserEntities){
            String content = "";
            String info = "";
            String contentDetail = "";
            if(atUserEntity.getCid() != null && atUserEntity.getCid() > 0){
                info = "这条评论@了你";
                IRecordCommentEntity commentEntity = commentDao.fetchCommentBy(atUserEntity.getCid());
                content = commentEntity.getContent() == null ? "这条评论被你删除了的" : commentEntity.getContent();
            } else if(atUserEntity.getHid() != null && atUserEntity.getHid() > 0){
                info = "这条帮助回复@了你";
                IRecordHelpreplyEntity helpreplyEntity = streamDao.findHelpBy(atUserEntity.getHid());
                content = helpreplyEntity.getContent() == null ? "这条评论被你删除了的" : helpreplyEntity.getContent();
            } else if(atUserEntity.getSid() != null && atUserEntity.getSid() > 0){
                IRecordSayEntity sayEntity = null;
                try{
                    sayEntity = streamDao.findTweetBy(atUserEntity.getSid());
                }catch (EmptyResultDataAccessException e){
                    logger.error("//////"+e.getMessage());
                }
                if(sayEntity != null){
                    if ("0".equals(sayEntity.getSayType())) {
                        info = "这条记录@了你";
                    } else if ("1".equals(sayEntity.getSayType())) {
                        info = "这条帮助@了你";
                    }
                    contentDetail = sayEntity.getContent() == null ? "信息被删除了的" : sayEntity.getContent();
                }

            }


            Active active = new Active.Builder()
                    .sid(atUserEntity.getSid())
                    .avatar(convertToAvatarUrl(atUserEntity.getIconUrl(), atUserEntity.getUid()))
                    .name(atUserEntity.getNickname())
                    .uid(atUserEntity.getUid())
                    .catalog(3)
                    .setObjecttype(3)
                    .setObjectcatalog(0)
                    .setObjecttitle("孤独")
                    .academy(info)
                    .by(3)
                    .setUrl("")
                    .setObjectID(atUserEntity.getCid() == null ? atUserEntity.getSid() : atUserEntity.getCid())
                    .content(contentDetail)
                    .commentCount(0)
                    .date(convertToDate(atUserEntity.getTime()))
                    .online(0)
                    .build();
            activeList.add(active);

        }
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
        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();
        actives.setActive(activeList);
        UserWordResult uar = new UserWordResult();
        uar.setNotice(notice);
        uar.setPagesize(20);
        uar.setActives(actives);
        return uar;
    }

    private UserWordResult fetchComments(int uid, int pageIndex, int pageSize) {
        List<IMsgCommentEntity> msgCommentEntities = commentDao.fetchAllCommentsBy(uid, pageIndex, pageSize);
        List<Active> activeList = new ArrayList<Active>();
        for(IMsgCommentEntity msgCommentEntity:msgCommentEntities){
            IUserLoginEntity user = userDao.findUserById(msgCommentEntity.getRid());
            String nickname = user.getNickname() == null ?  "匿名用户" : user.getNickname();
            String content = null;
            String info = null;
            String contentDetail = null;
            if(msgCommentEntity.getCid() != null){
                IRecordCommentEntity commentEntity = commentDao.fetchCommentBy(msgCommentEntity.getCid());
                content = commentEntity.getContent() == null ? "这条评论被你删除了的" : commentEntity.getContent();
                info = "回复了你的评论: ";
            }else {
                IRecordSayEntity sayEntity = streamDao.findTweetBy(msgCommentEntity.getSid());
                content = sayEntity.getContent() == null ? "内容被你删除了的" : sayEntity.getContent();
                info = "评论了你：";
            }
            if(msgCommentEntity.getNcid() != null){
                IRecordCommentEntity commentEntityDetail = commentDao.fetchCommentBy(msgCommentEntity.getNcid());
                contentDetail = commentEntityDetail.getContent() == null ? "评论又被"+nickname+"删除了":commentEntityDetail.getContent();
            }

            ObjectReply or =  new ObjectReply("我", content);
            Active active = new Active.Builder()
                    .sid(msgCommentEntity.getSid())
                    .avatar(convertToAvatarUrl(user.getIconUrl(), user.getUid()))
                    .name(user.getNickname())
                    .uid(user.getUid())
                    .catalog(3)
                    .setObjecttype(3)
                    .setObjectcatalog(0)
                    .setObjecttitle("孤独")
                    .setObjectreply(or)
                    .academy(info)
                    .by(3)
                    .setUrl("")
                    .setObjectID(msgCommentEntity.getCid() == null ? msgCommentEntity.getSid() : msgCommentEntity.getCid())
                    .content(contentDetail)
                    .commentCount(0)
                    .date(convertToDate(msgCommentEntity.getTime()))
                    .online(Integer.parseInt(user.getOnline()))
                    .setObjectreply(or)
                    .build();
            activeList.add(active);

        }
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
        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();
        actives.setActive(activeList);
        UserWordResult uar = new UserWordResult();
        uar.setNotice(notice);
        uar.setPagesize(20);
        uar.setActives(actives);
        return uar;
    }


    public MessageResult fetchAndDeliverActive(int uid, int pageIndex, int pageSize) {
        List<IMsgActiveEntity> actives = messageDao.findActivesByUid(uid, pageIndex, pageSize);
        messageDao.updateActiveDeliver(uid);
        List<MessageResult.Message> list = new ArrayList<MessageResult.Message>();
        for (IMsgActiveEntity active : actives) {
            MessageResult.Message msgs = new MessageResult.Message();
                    msgs.id = active.getId();
                    msgs.author = "(" + active.getTotal() + ")";
                    msgs.commentCount = Integer.valueOf(active.getDeliver());
                    msgs.pubDate = convertToDate(active.getTime());
                    msgs.title = " " + active.getReason();
                    msgs.inout = "min".equals(active.getWay()) ? "-" + active.getChange() : "+" + active.getChange();
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
        mr.pagesize = 20;
        mr.notice = notice;
        mr.newslist = newslist;
        return mr;
    }

    public static void main(String[] args) {
        System.out.println(Integer.valueOf("w"));
    }

    public ChatResult fetchChats(int uid, int pageIndex, int pageSize) {
        List<ITalkContentEntity> talks = messageDao.findRecentChatsBy(uid, pageIndex, pageSize);
        Collection<ITalkContentEntity> oneWayTalks = normalizeToOneWay(talks);
        Set<Integer> uids = new HashSet<Integer>();
        for (ITalkContentEntity talk : talks) {
            uids.add(talk.getUid());
            uids.add(talk.getTouid());
        }
        List<IUserLoginEntity> users = userDao.findUsersBy(uids);
        Map<Integer, IUserLoginEntity> usersMap = new HashMap<Integer, IUserLoginEntity>();
        for (IUserLoginEntity user : users) {
            usersMap.put(user.getUid(), user);
        }


        ChatResult chatResult = new ChatResult();

        List<ChatResult.Chat> chats = new ArrayList<ChatResult.Chat>();

        for (ITalkContentEntity talk : oneWayTalks) {

            logger.debug("-talk.getId()="+ talk.getId() +" talk.getUid()="+talk.getUid()+" talk.getTouid()="+talk.getTouid()
                    +" talk.getContent()"+talk.getContent()+" talk.getChatNum()"+talk.getChatNum() +" talk.getTime()"+talk.getTime());
            IUserLoginEntity fromUser = usersMap.get(talk.getUid());
            logger.debug(" fromUser: "+fromUser);
            String fromIcon = fromUser == null ? "" : fromUser.getIconUrl();

            IUserLoginEntity friend = usersMap.get(talk.getUid());
            logger.debug(" friend: "+friend);
            String friendName = friend == null ?"":friend.getNickname();

            int friendID = talk.getTouid() == uid ? talk.getUid() : talk.getTouid();



            ChatResult.Chat c1 = new ChatResult.Chat.Builder()
                    .id(talk.getId())
                    .portrait(convertToAvatarUrl(fromIcon, talk.getUid()))
                    .friendid(friendID)
                    .friendname(usersMap.get(friendID).getNickname())
                    .sender(friendName)
                    .senderid(talk.getUid() == null ? 0 : talk.getUid())
                    .content(talk.getContent())
                    .messageCount(talk.getChatNum() == null ? 0 : talk.getChatNum())
                    .pubDate(convertToDate(talk.getTime()))
                    .build();
            chats.add(c1);

        }

        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();


        ChatResult.Chats chatList = new ChatResult.Chats(chats);

        chatResult.setMessageCount(oneWayTalks.size());
        chatResult.setMessages(chatList);
        chatResult.setNotice(notice);
        chatResult.setPagesize(pageSize);
        return chatResult;  //To change body of created methods use File | Settings | File Templates.
    }

    private Collection<ITalkContentEntity> normalizeToOneWay(List<ITalkContentEntity> talks) {
        Map<String, ITalkContentEntity> talkMap = new HashMap<String, ITalkContentEntity>();
        ValueComparator bvc =  new ValueComparator(talkMap);
        TreeMap<String,ITalkContentEntity> sortedMap = new TreeMap<String,ITalkContentEntity>(bvc);
        for (ITalkContentEntity talk : talks) {
            String key = talk.getUid() + "-" + talk.getTouid();
            if (talkMap.keySet().contains(talk.getTouid() + "-" +talk.getUid())) {
                ITalkContentEntity originTalk = talkMap.get(talk.getTouid() + "-" +talk.getUid());
                originTalk.setChatNum(originTalk.getChatNum() + talk.getChatNum());
                continue;
            }
            talkMap.put(key, talk);
        }
        sortedMap.putAll(talkMap);
        return sortedMap.values();  //To change body of created methods use File | Settings | File Templates.
    }

    public TweetCommentResult pullChatsBy(Integer id, Integer pageIndex, Integer pageSize) {
        List<VTweetCommentEntity> commentEntities = messageDao.findAllChatsBy(id, pageIndex, pageSize);
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
                    .id(commentEntity.getSid() == null ? -1 :  commentEntity.getSid())//would be chats if -1
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

    class ValueComparator implements Comparator<String> {

        Map<String, ITalkContentEntity> base;
        public ValueComparator(Map<String, ITalkContentEntity> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.
        public int compare(String a, String b) {
            if (base.get(a).getTime() >= base.get(b).getTime()) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }


    public String replace(String src, String patternStr, String replacement){
        StringBuffer target = new StringBuffer();
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(src);
        while(matcher.find()){
            matcher.appendReplacement(target, replacement);
        }
        matcher.appendTail(target);
        return target.toString();
    }

//    public TweetCommentResult pullChatsBy(int id, int pageIndex, int pageSize) {
//
//        List<VTweetCommentEntity> commentEntities = messageDao.findAllChatsBy(id, pageIndex, pageSize);
//        int allCount = commentEntities.size();
//        List<TweetCommentResult.Comment> comments = new ArrayList<TweetCommentResult.Comment>();
//        for (VTweetCommentEntity commentEntity : commentEntities) {
//            TweetCommentResult.Comment comment = new TweetCommentResult.Comment.Builder()
//                    .content(commentEntity.getContent())
//                    .date(convertToDate(commentEntity.getTime()))
//                    .author(commentEntity.getNickname())
//                    .authorid(commentEntity.getUid())
//                    .avatar(convertToAvatarUrl(commentEntity.getIconUrl(), commentEntity.getUid()))
//                    .by(0)
//                    .id(commentEntity.getSid())
//                    .build();
//            comments.add(comment);
//        }
//        Notice notice = new Notice.Builder()
//                .talk(0)
//                .system(0)
//                .comment(0)
//                .at(0)
//                .build();
//
//        TweetCommentResult commentResult = new TweetCommentResult();
//        TweetCommentResult.Comments commentWrapper = new TweetCommentResult.Comments(comments);
//        commentResult.setAllCount(allCount);
//        commentResult.setPagesize(pageSize);
//        commentResult.setComments(commentWrapper);
//        commentResult.setNotice(notice);
//        return commentResult;
//    }
}
