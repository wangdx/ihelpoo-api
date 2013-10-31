package com.ihelpoo.api.service;

import com.google.code.hs4j.HSClient;
import com.google.code.hs4j.IndexSession;
import com.google.code.hs4j.ModifyStatement;
import com.google.code.hs4j.exception.HandlerSocketException;
import com.google.code.hs4j.impl.HSClientImpl;
import com.ihelpoo.api.model.*;
import com.ihelpoo.api.model.obj.*;
import com.ihelpoo.common.Constant;
import com.ihelpoo.api.dao.CommentDao;
import com.ihelpoo.api.dao.MessageDao;
import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.entity.*;
import com.ihelpoo.api.service.base.RecordService;
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

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;
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

        long t = System.currentTimeMillis();

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


            Active active = new Active();
            active.sid = msg.getDetailId();
            active.iconUrl = convertToAvatarUrl(msg.getIconUrl(), msg.getUid(), false);
            active.nickname = msg.getNickname();
            active.uid = msg.getUid();
            active.catalog = 3;
            active.objecttype = 3;
            active.objectcatalog = 0;
            active.objecttitle = "";
            active.from = 3;
            active.url = "";
            active.objectID = sid;
            active.content = content;
            active.commentCo = 0;
            active.time = convertToDate(msg.getCreatTi());
            active.online = Integer.parseInt(msg.getOnline());
            active.objectreply = or;

            activeList.add(active);
        }
        actives.setActive(activeList);
        UserWordResult uar = new UserWordResult();

        uar.notice = getNotice(uid);
        uar.pagesize = 20;
        uar.actives = actives;
        jedis.disconnect();

        return uar;
    }

    private UserWordResult fetchAt(int uid, int pageIndex, int pageSize) {
        List<VAtUserEntity> atUserEntities = commentDao.fetchAllAtBy(uid, pageIndex, pageSize);
        List<Active> activeList = new ArrayList<Active>();
        for (VAtUserEntity atUserEntity : atUserEntities) {
//            String content = "";
            String info = "";
            String contentDetail = "";
            if (atUserEntity.getCid() != null && atUserEntity.getCid() > 0) {
                info = "这条评论@了你";
//                IRecordCommentEntity commentEntity = commentDao.fetchCommentBy(atUserEntity.getCid());
//                content = commentEntity.getContent() == null ? "这条评论被你删除了的" : commentEntity.getContent();
            } else if (atUserEntity.getHid() != null && atUserEntity.getHid() > 0) {
                info = "这条帮助回复@了你";
//                IRecordHelpreplyEntity helpreplyEntity = streamDao.findHelpBy(atUserEntity.getHid());
//                content = helpreplyEntity.getContent() == null ? "这条评论被你删除了的" : helpreplyEntity.getContent();
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
            active.from = 3;
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
        uar.pagesize = 20;
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
                IRecordCommentEntity commentEntity = commentDao.fetchCommentBy(msgCommentEntity.getCid());
                content = commentEntity.getContent() == null ? "这条评论被你删除了的" : commentEntity.getContent();
                info = "回复了你的评论: ";
            } else {
                IRecordSayEntity sayEntity = streamDao.findTweetBy(msgCommentEntity.getSid());
                content = sayEntity.getContent() == null ? "内容被你删除了的" : sayEntity.getContent();
                info = "评论了你：";
            }
            if (msgCommentEntity.getNcid() != null) {
                IRecordCommentEntity commentEntityDetail = commentDao.fetchCommentBy(msgCommentEntity.getNcid());
                contentDetail = commentEntityDetail.getContent() == null ? "评论又被" + nickname + "删除了" : commentEntityDetail.getContent();
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
            active.from = 3;
            active.url = "";
            active.objectID = msgCommentEntity.getCid() == null ? msgCommentEntity.getSid() : msgCommentEntity.getCid();
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
        uar.pagesize = 20;
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
            msgs.author = "(" + active.getTotal() + ")";
            msgs.commentCount = Integer.valueOf(active.getDeliver());
            msgs.pubDate = convertToDate(active.getTime());
            msgs.title = " " + active.getReason();
            msgs.inout = "min".equals(active.getWay()) ? "-" + active.getChange() : "+" + active.getChange();
            list.add(msgs);
        }
        MessageResult.Messages newslist = new MessageResult.Messages(list);
        MessageResult mr = new MessageResult();
        mr.pagesize = 20;
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
            chats.add(c1);

        }


        ChatResult.Chats chatList = new ChatResult.Chats(chats);

        chatResult.messageCount = oneWayTalks.size();
        chatResult.messages = chatList;
        chatResult.notice = getNotice(uid);
        chatResult.pagesize = pageSize;
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
        commentResult.allCount = allCount;
        commentResult.pagesize = pageSize;
        commentResult.comments = commentWrapper;
        commentResult.notice = getNotice(uid);
        return commentResult;
    }

    private static final int TIMEOUT = 120 * 1000;


    //TODO we need to refactor here, since it would be very slow to establish cometd connection every time.
    public DoChatResult doChat(Integer uid, Integer receiver, String content) throws Exception {

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
//
//        store(String.valueOf(receiver), String.valueOf(uid), content, "", time);
        IUserLoginEntity user = userDao.findUserById(uid);

        DoChatResult doChatResult = new DoChatResult();
        Result result = new Result();
        result.setErrorCode("1");
        result.setErrorMessage("留言发表成功");
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


    private void store(String to, String from, String chat, String image, int time) {
        HSClient wr = null;
        try {

            String db = "ihelpoo";
            String table = "i_talk_content";

            wr = new HSClientImpl(HS_WR.getLeft(), HS_WR.getRight(), 2);
            IndexSession sessionChat = wr.openIndexSession(db, table,
                    "PRIMARY", new String[]{"uid", "touid", "content", "image", "time", "deliver", "del"});
            ModifyStatement stmt = sessionChat.createStatement();

            stmt.setInt(1, Integer.parseInt(from));
            stmt.setInt(2, Integer.parseInt(to));
            stmt.setString(3, chat);
            stmt.setString(4, image);
            stmt.setInt(5, (int) time);
//            stmt.setString(6, value != null && value.equals(from) ? "1" : "0");//TODO should be delivered if they are talking, namely, touid is online
            stmt.setString(6, "0");
            stmt.setInt(7, 0);
            boolean result = stmt.insert();
            System.err.println("store ++++--insert into talk table===" + result);
        } catch (InterruptedException e) {
        } catch (TimeoutException e) {
        } catch (HandlerSocketException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wr.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
