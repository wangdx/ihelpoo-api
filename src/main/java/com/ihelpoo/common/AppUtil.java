package com.ihelpoo.common;

import com.google.code.hs4j.HSClient;
import com.google.code.hs4j.IndexSession;
import com.google.code.hs4j.ModifyStatement;
import com.google.code.hs4j.exception.HandlerSocketException;
import com.google.code.hs4j.impl.HSClientImpl;
import com.ihelpoo.api.service.WordService;
import com.ihelpoo.common.util.ID;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: echowdx@gmail.com
 */
public class AppUtil {

    public static String MAIL_COUNTER = "Mail:counter";
    public static String MAIL_QUEUE_TO = "Mail:Queue:to";
    public static String MAIL_QUEUE_SUBJECT = "Mail:Queue:subject";
    public static String MAIL_QUEUE_BODY = "Mail:Queue:body";

    public static void sendMail(String to, String subject, String body) {
        Jedis jedis = new Jedis(Constant.R_HOST);
        jedis.incr(MAIL_COUNTER);
        jedis.lpush(MAIL_QUEUE_TO, to);
        jedis.lpush(MAIL_QUEUE_SUBJECT, subject);
        jedis.lpush(MAIL_QUEUE_BODY, body);
    }

    public static long saveNotice(Integer from, Integer to, String noticeType, Integer detailId) {
        long noticeId = ID.INSTANCE.next();
        long now = System.currentTimeMillis() / 1000L;
        addNoticeToDB(from, noticeType, detailId, noticeId, (int) now);
        String toStr = String.valueOf(to);
        Jedis jedis = new Jedis(Constant.R_HOST);
        jedis.hset(WordService.R_ACCOUNT + WordService.R_MESSAGE + toStr, String.valueOf(noticeId), String.valueOf(0));
        jedis.hincrBy(WordService.R_NOTICE + WordService.R_SYSTEM + toStr.substring(0, toStr.length() - 3), toStr.substring(toStr.length() - 3), 1);
        return noticeId;
    }

    private static void addNoticeToDB(Integer from, String noticeType, Integer detailId, long noticeId, int now) {
        HSClient wr = null;
        try {

            String db = "ihelpoo";
            String table = "i_msg_notice";
            wr = new HSClientImpl(Constant.HS_WR.getLeft(), Constant.HS_WR.getRight(), 2);
            IndexSession sessionChat = wr.openIndexSession(db, table,
                    "PRIMARY", new String[]{"notice_id", "notice_type", "source_id", "detail_id", "format_id", "create_time"});
            ModifyStatement stmt = sessionChat.createStatement();

            stmt.setLong(1, noticeId);
            stmt.setString(2, noticeType);
            stmt.setInt(3, from);
            stmt.setInt(4, detailId);
            stmt.setString(5, noticeType);
            stmt.setInt(6, (int) now);
            boolean result = stmt.insert();
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

    public static String getDeviceType(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String deviceType = "我帮圈圈";
        if (userAgent != null && userAgent.length() > 0 && userAgent.contains("/")) {
            String[] agentArr = userAgent.split("/");
            if (agentArr.length >= 2) {
                deviceType = agentArr[agentArr.length - 2];
            }
        }
        return deviceType;
    }
}
