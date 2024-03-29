package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.CommentDao;
import com.ihelpoo.api.dao.MessageDao;
import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.*;
import com.ihelpoo.api.model.entity.*;
import com.ihelpoo.api.model.obj.*;
import com.ihelpoo.api.service.base.RecordService;
import com.ihelpoo.common.Constant;
import org.cometd.bayeux.client.ClientSession;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;
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
    public static final Pair<String, Integer> HS_WR = new Pair<String, Integer>("10.6.1.208", 9999);

    @Autowired
    MessageDao messageDao;

    @Autowired
    UserDao userDao;

    @Autowired
    CommentDao commentDao;

    public UserWordResult fetchNotice(int uid, int catalog, int schoolId, int pageIndex, int pageSize) {

        switch (catalog) {
            case 1:
                return fetchAt(uid, pageIndex, pageSize);
            case 2:
                return fetchComments(uid, pageIndex, pageSize);
            default:
                break;
        }

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
        List<Active> activeList = new ArrayList<Active>();
        for (VMsgLoginEntity msg : msgLoginEntities) {
            String view = "";
            IRecordDiffusionEntity diffusionEntity = null;
            if ("diffusiontoowner".equals(msg.getFormatId()) || "diffusion".equals(msg.getFormatId())) {
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
            String sayType = null;
            if (diffusionEntity != null) {
                try {
                    tweetDetailEntity = streamDao.findTweetDetailBy(diffusionEntity.getSid());
                } catch (EmptyResultDataAccessException e) {
                    //eat it
                }
            } else if (msg.getDetailId() > 0) {
                try {
                    tweetDetailEntity = streamDao.findTweetDetailBy(msg.getDetailId());
                } catch (EmptyResultDataAccessException e) {
                    //eat it
                }
            }
            if (tweetDetailEntity != null) {
                or = new ObjectReply(tweetDetailEntity.getAuthor(), tweetDetailEntity.getContent());
                sid = tweetDetailEntity.getSid();
                sayType = tweetDetailEntity.getSayType();
            }


            Active active = new Active();
            if (tweetDetailEntity != null && (
                    "diffusiontoowner".equals(msg.getFormatId())
                            || "diffusion".equals(msg.getFormatId())
                            || "plus".equals(msg.getFormatId())
                            || "stream/ih-para:needhelp".equals(msg.getFormatId())
                            || "stream/ih-para:newHelp".equals(msg.getFormatId())
                            || "stream/ih-para:reply".equals(msg.getFormatId())
                            || "stream/ih-para:success".equals(msg.getFormatId())
            )) {
                active.catalog = 3; //TWEET
            }
            active.sid = msg.getDetailId();
            active.iconUrl = convertToAvatarUrl(msg.getIconUrl(), msg.getUid(), false);
            active.nickname = msg.getNickname();
            active.uid = msg.getUid();
            active.objecttype = 3;
            active.objectcatalog = 0;
            active.objecttitle = "";
            active.from = "";
            active.url = "";
            active.objectID = sid;
            active.content = content;
            active.commentCo = 0;
            active.time = convertToDate(msg.getCreateTime());
            active.online = Integer.parseInt(msg.getOnline());
            active.objectreply = or;
            active.objectSayType = sayType;

            activeList.add(active);
        }
        actives.setActive(activeList);
        UserWordResult uar = new UserWordResult();

        uar.notice = getNotice(uid);
        uar.page_size = msgLoginEntities.size();
        uar.actives = actives;
        jedis.disconnect();

        return uar;
    }

    private UserWordResult fetchAt(int uid, int pageIndex, int pageSize) {
        List<VAtUserEntity> atUserEntities = commentDao.fetchAllAtBy(uid, pageIndex, pageSize);
        List<Active> activeList = new ArrayList<Active>();
        for (VAtUserEntity atUserEntity : atUserEntities) {
            String info = "";
            String contentDetail = "";
            if (atUserEntity.getCid() != null && atUserEntity.getCid() > 0) {
                info = "这条评论@了你";
            } else if (atUserEntity.getHid() != null && atUserEntity.getHid() > 0) {
                info = "这条帮助回复@了你";
            } else if (atUserEntity.getSid() != null && atUserEntity.getSid() > 0) {
                IRecordSayEntity sayEntity = null;
                try {
                    sayEntity = streamDao.findTweetBy(atUserEntity.getSid());
                } catch (EmptyResultDataAccessException e) {
                    logger.error("//////" + e.getMessage());
                }
                if (sayEntity != null) {
                    if ("0".equals(sayEntity.getSayType())) {
                        info = "这条记录@了你";
                    } else if ("1".equals(sayEntity.getSayType())) {
                        info = "这条帮助@了你";
                    }
                    contentDetail = sayEntity.getContent() == null ? "信息被删除了的" : sayEntity.getContent();
                }

            }


            Active active = new Active();
            active.sid = atUserEntity.getSid();
            active.iconUrl = convertToAvatarUrl(atUserEntity.getIconUrl(), atUserEntity.getUid(), false);
            active.nickname = atUserEntity.getNickname();
            active.uid = atUserEntity.getUid();
            active.catalog = 3;
            active.objecttype = 3;
            active.objectcatalog = 0;
            active.objecttitle = "";
            active.academy = info;
            active.from = "";
            active.url = "";
            active.objectID = atUserEntity.getCid() == null ? atUserEntity.getSid() : atUserEntity.getCid();
            active.content = contentDetail;
            active.commentCo = 0;
            active.time = convertToDate(atUserEntity.getTime());
            active.online = 0;

            activeList.add(active);

        }
        Actives actives = new Actives();
        actives.setActive(activeList);
        UserWordResult uar = new UserWordResult();
        uar.notice = getNotice(uid);
        uar.page_size = atUserEntities.size();
        uar.actives = actives;
        return uar;
    }

    private UserWordResult fetchComments(int uid, int pageIndex, int pageSize) {
        List<IMsgCommentEntity> msgCommentEntities = commentDao.fetchAllCommentsBy(uid, pageIndex, pageSize);
        List<Active> activeList = new ArrayList<Active>();
        for (IMsgCommentEntity msgCommentEntity : msgCommentEntities) {
            IUserLoginEntity user = userDao.findUserById(msgCommentEntity.getRid());
            String nickname = user.getNickname() == null ? "匿名用户" : user.getNickname();
            String content = null;
            String info = null;
            String contentDetail = null;
            if (msgCommentEntity.getCid() != null) {
                IRecordCommentEntity commentEntity = null;
                try {
                    commentEntity = commentDao.fetchCommentBy(msgCommentEntity.getCid());
                    content = commentEntity.getContent();
                } catch (EmptyResultDataAccessException e) {
                    content = "这条评论被你删除了的";
                }
                info = "回复了你的评论: ";
            } else {
                IRecordSayEntity sayEntity = null;
                try {
                    sayEntity = streamDao.findTweetBy(msgCommentEntity.getSid());
                    content = sayEntity.getContent();
                } catch (EmptyResultDataAccessException e) {
                    content = "内容被你删除了的";
                }
                info = "评论了你：";
            }
            if (msgCommentEntity.getNcid() != null) {
                IRecordCommentEntity commentEntityDetail = null;
                try {
                    commentEntityDetail = commentDao.fetchCommentBy(msgCommentEntity.getNcid());
                    contentDetail = commentEntityDetail.getContent();
                } catch (EmptyResultDataAccessException e) {
                    contentDetail = "评论又被" + nickname + "删除了";
                }

            }

            ObjectReply or = new ObjectReply("我", content);
            Active active = new Active();

            active.sid = msgCommentEntity.getSid();
            active.iconUrl = convertToAvatarUrl(user.getIconUrl(), user.getUid(), false);
            active.nickname = user.getNickname();

            active.uid = user.getUid();
            active.catalog = 3;
            active.objecttype = 3;
            active.objectcatalog = 0;
            active.objecttitle = "";
            active.objectreply = or;
            active.academy = info;
            active.from = "";
            active.url = "";
            active.objectID = msgCommentEntity.getSid();
            active.content = contentDetail;
            active.commentCo = 0;
            active.time = convertToDate(msgCommentEntity.getTime());
            active.online = Integer.parseInt(user.getOnline());

            activeList.add(active);

        }
        Actives actives = new Actives();
        actives.setActive(activeList);
        UserWordResult uar = new UserWordResult();
        uar.notice = getNotice(uid);
        uar.page_size = msgCommentEntities.size();
        uar.actives = actives;
        return uar;
    }


    public MessageResult fetchAndDeliverActive(int uid, int pageIndex, int pageSize) {
        List<IMsgActiveEntity> actives = messageDao.findActivesByUid(uid, pageIndex, pageSize);
        messageDao.updateActiveDeliver(uid);
        List<MessageResult.Message> list = new ArrayList<MessageResult.Message>();
        for (IMsgActiveEntity active : actives) {
            MessageResult.Message msgs = new MessageResult.Message();
            msgs.id = active.getId();
            msgs.author = "[" + active.getTotal() + "]";
            msgs.commentCount = Integer.valueOf(active.getDeliver());
            msgs.pubDate = convertToDate(active.getTime());
            msgs.title = active.getReason();
            msgs.inout = "min".equals(active.getWay()) ? "-" + active.getChange() : "+" + active.getChange();
            list.add(msgs);
        }
        MessageResult.Messages newslist = new MessageResult.Messages(list);
        MessageResult mr = new MessageResult();
        mr.page_size = actives.size();
        mr.notice = getNotice(uid);
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

            logger.debug("-talk.getId()=" + talk.getId() + " talk.getUid()=" + talk.getUid() + " talk.getTouid()=" + talk.getTouid()
                    + " talk.getContent()" + talk.getContent() + " talk.getChatNum()" + talk.getChatNum() + " talk.getTime()" + talk.getTime());
            IUserLoginEntity fromUser = usersMap.get(talk.getUid());
            logger.debug(" fromUser: " + fromUser);
            String fromIcon = fromUser == null ? "" : fromUser.getIconUrl();

            IUserLoginEntity friend = usersMap.get(talk.getUid());
            logger.debug(" friend: " + friend);
            String friendName = friend == null ? "" : friend.getNickname();

            int friendID = talk.getTouid() == uid ? talk.getUid() : talk.getTouid();


            ChatResult.Chat c1 = new ChatResult.Chat();
            c1.id = talk.getId();
            c1.portrait = convertToAvatarUrl(fromIcon, talk.getUid() == null ? 10000 : talk.getUid(), false);
            c1.friendid = friendID;
            c1.friendname = usersMap.get(friendID) == null ? "" : usersMap.get(friendID).getNickname();
            c1.sender = friendName;
            c1.senderid = talk.getUid() == null ? 0 : talk.getUid();
            c1.content = talk.getContent();
            c1.messageCount = talk.getChatNum() == null ? 0 : talk.getChatNum();
            c1.pubDate = convertToDate(talk.getTime());
            c1.del = talk.getDel() == null ? 0 : talk.getDel();
            chats.add(c1);

        }


        ChatResult.Chats chatList = new ChatResult.Chats(chats);

        chatResult.message_count = talks.size();
        chatResult.messages = chatList;
        chatResult.notice = getNotice(uid);
        chatResult.page_size = talks.size();
        return chatResult;  //To change body of created methods use File | Settings | File Templates.
    }

    private Collection<ITalkContentEntity> normalizeToOneWay(List<ITalkContentEntity> talks) {
        Map<String, ITalkContentEntity> talkMap = new HashMap<String, ITalkContentEntity>();
        ValueComparator bvc = new ValueComparator(talkMap);
        TreeMap<String, ITalkContentEntity> sortedMap = new TreeMap<String, ITalkContentEntity>(bvc);
        for (ITalkContentEntity talk : talks) {
            String key = talk.getUid() + "-" + talk.getTouid();
            if (talkMap.keySet().contains(talk.getTouid() + "-" + talk.getUid())) {
                ITalkContentEntity originTalk = talkMap.get(talk.getTouid() + "-" + talk.getUid());
                originTalk.setChatNum(originTalk.getChatNum() + talk.getChatNum());
                continue;
            }
            talkMap.put(key, talk);
        }
        sortedMap.putAll(talkMap);
        return sortedMap.values();  //To change body of created methods use File | Settings | File Templates.
    }

    public TweetCommentResult pullChatsBy(Integer uid, Integer id, Integer pageIndex, Integer pageSize) {
        List<VTweetCommentEntity> commentEntities = messageDao.findAllChatsBy(uid, id, pageIndex, pageSize);
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
        commentResult.all_count = allCount;
        commentResult.page_size = allCount;
        commentResult.comments = commentWrapper;
        commentResult.notice = getNotice(uid);
        return commentResult;
    }

    private static final int TIMEOUT = 120 * 1000;


    //TODO we need to refactor here, since it would be very slow to establish cometd connection every time.
    public DoChatResult doChat(Integer uid, Integer receiver, String content, String receiverNickname) throws Exception {

        DoChatResult doChatResult = new DoChatResult();
        Result result = new Result();
        result.setErrorCode("0");
        if (receiverNickname != null && receiverNickname.length() > 0) {
            try {
                IUserLoginEntity user = userDao.findUserByNickname(receiverNickname);
                receiver = user.getUid();
            } catch (EmptyResultDataAccessException e) {
                result.setErrorMessage("没有使用该昵称的用户");
                doChatResult.result = result;
                return doChatResult;
            }
        }


        // Set up a Jetty HTTP client to use with CometD
        HttpClient httpClient = new HttpClient();
        httpClient.setConnectTimeout(TIMEOUT);
        httpClient.setTimeout(TIMEOUT);
        httpClient.start();


        Map<String, Object> options = new HashMap<String, Object>();
        options.put(ClientTransport.TIMEOUT_OPTION, TIMEOUT);

        // Adds the OAuth header in LongPollingTransport
        LongPollingTransport transport = new LongPollingTransport(options, httpClient);

        BayeuxClient client = new BayeuxClient("http://comet.ihelpoo.cn/c1/cometd", transport);
        client.handshake();
        waitForHandshake(client, 60 * 1000, 1000);


        Map<String, Object> data = new HashMap<String, Object>();
        data.put("room", "/chat/p2p");
        data.put("from", uid);
        data.put("to", receiver);
        data.put("chat", content);
        data.put("status", "");
        data.put("image", "");
        client.getChannel("/service/p2ps").publish(data);
        client.disconnect();

        final Date now = new Date();
        int time = (int) (now.getTime() / 1000L);
        IUserLoginEntity user = userDao.findUserById(uid);

        result.setErrorCode("1");
        result.setErrorMessage("发送成功");
        TweetCommentResult.Comment comment = new TweetCommentResult.Comment();
        comment.id = 0;
        comment.authorid = uid;
        comment.author = user.getNickname();
        comment.content = content;
        comment.portrait = convertToAvatarUrl(user.getIconUrl(), user.getUid(), false);
        comment.pubDate = convertToDate(time);
        doChatResult.result = result;
        doChatResult.comment = comment;
        doChatResult.notice = new Notice();

        return doChatResult;
    }


    private void waitForHandshake(ClientSession client,
                                  long timeoutInMilliseconds, long intervalInMilliseconds) {
        long start = System.currentTimeMillis();
        long end = start + timeoutInMilliseconds;
        while (System.currentTimeMillis() < end) {
            if (client.isHandshook())
                return;
            try {
                Thread.sleep(intervalInMilliseconds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        throw new IllegalStateException("Client did not handshake with server");
    }

    public GenericResult delChat(Integer uid, Integer friendId) {
        GenericResult genericResult = new GenericResult();
        genericResult.setNotice(new Notice());
        Result result = new Result();
        genericResult.setResult(result);
        result.setErrorCode("0");

        messageDao.updateChats(uid, friendId);
        messageDao.deleteChats(uid, friendId);

        result.setErrorCode("1");
        result.setErrorMessage("删除成功");
        genericResult.setResult(result);
        return genericResult;
    }

    public GenericResult deleteOneChat(Integer id, Integer authorid, Integer replyid) {
        GenericResult genericResult = new GenericResult();
        genericResult.setNotice(new Notice());
        Result result = new Result();
        genericResult.setResult(result);
        result.setErrorCode("0");

        messageDao.updateOneChat(replyid, authorid, id);
        messageDao.deleteOneChat(replyid, authorid, id);

        result.setErrorCode("1");
        result.setErrorMessage("删除成功");
        genericResult.setResult(result);
        return genericResult;
    }

    static class Pair<L, R> {

        private final L left;
        private final R right;

        public Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }

        public L getLeft() {
            return left;
        }

        public R getRight() {
            return right;
        }

        @Override
        public int hashCode() {
            return left.hashCode() ^ right.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof Pair)) return false;
            Pair pairo = (Pair) o;
            return this.left.equals(pairo.getLeft()) &&
                    this.right.equals(pairo.getRight());
        }

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


    public String replace(String src, String patternStr, String replacement) {
        StringBuffer target = new StringBuffer();
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            matcher.appendReplacement(target, replacement);
        }
        matcher.appendTail(target);
        return target.toString();
    }
}
