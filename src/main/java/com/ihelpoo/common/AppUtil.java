package com.ihelpoo.common;

import com.ihelpoo.api.service.WordService;
import com.ihelpoo.common.util.ID;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;

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
        jedis.disconnect();
    }

    public static long toNotice(Integer to) {
        long noticeId = ID.INSTANCE.next();
        String toStr = String.valueOf(to);
        Jedis jedis = new Jedis(Constant.R_HOST);
        jedis.hset(WordService.R_ACCOUNT + WordService.R_MESSAGE + toStr, String.valueOf(noticeId), String.valueOf(0));
        jedis.hincrBy(WordService.R_NOTICE + WordService.R_SYSTEM + toStr.substring(0, toStr.length() - 3), toStr.substring(toStr.length() - 3), 1);
        return noticeId;
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
        return "手机"+deviceType;
    }
}
