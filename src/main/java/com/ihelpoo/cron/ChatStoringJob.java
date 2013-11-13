package com.ihelpoo.cron;

import com.ihelpoo.api.model.entity.ITalkContentEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import redis.clients.jedis.Jedis;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: echowdx@gmail.com
 */
public class ChatStoringJob extends JdbcDaoSupport {
    public static final String CHAT_COUNTER = "Chat:counter";
    public static final String CHAT_QUEUE_UID = "Chat:Queue:uid";
    public static final String CHAT_QUEUE_TOUID = "Chat:Queue:touid";
    public static final String CHAT_QUEUE_CONTENT = "Chat:Queue:content";
    public static final String CHAT_QUEUE_IMAGE = "Chat:Queue:image";
    public static final String CHAT_QUEUE_TIME = "Chat:Queue:time";
    public void store() {
        System.out.println(">>>>>");

        final List<ITalkContentEntity> chats = new ArrayList<ITalkContentEntity>();
        Jedis jedis = new Jedis("localhost");

        String counter = jedis.get(CHAT_COUNTER);
        if (counter == null || !counter.matches("\\d+") || Integer.parseInt(counter) < 1) {
            jedis.del(CHAT_COUNTER);
            return;
        }

        do {
            ITalkContentEntity chat = new ITalkContentEntity();
            chat.setUid(Integer.parseInt(jedis.rpop(CHAT_QUEUE_UID)));
            chat.setTouid(Integer.parseInt(jedis.rpop(CHAT_QUEUE_TOUID)));
            chat.setContent(jedis.rpop(CHAT_QUEUE_CONTENT));
            chat.setImage(jedis.rpop(CHAT_QUEUE_IMAGE));
            chat.setTime(Integer.parseInt(jedis.rpop(CHAT_QUEUE_TIME)));
            chats.add(chat);

            logger.info(">>>sending message from:" + chat.getUid() + " to " + chat.getTouid());
        } while (jedis.decr(CHAT_COUNTER) > 0);
        jedis.del(CHAT_COUNTER);
        jedis.disconnect();
        storeToDB(chats);
    }

    private void storeToDB(final List<ITalkContentEntity> chats) {
        String sql = "INSERT INTO i_talk_content (uid,touid,content,image,`time`,deliver,del) VALUES (?,?,?,?,?,'0',0)";

        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ITalkContentEntity customer = chats.get(i);
                ps.setInt(1, customer.getUid());
                ps.setInt(2, customer.getTouid());
                ps.setString(3, customer.getContent());
                ps.setString(4, customer.getImage());
                ps.setInt(5, customer.getTime());
            }

            @Override
            public int getBatchSize() {
                return chats.size();
            }
        });
    }
}
