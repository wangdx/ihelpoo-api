package com.ihelpoo.cron;

import com.ihelpoo.mail.LocalMailService;
import com.ihelpoo.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class MailSendingJob {

    public static final String MAIL_COUNTER = "Mail:counter";
    public static final String MAIL_QUEUE_TO = "Mail:Queue:to";
    public static final String MAIL_QUEUE_SUBJECT = "Mail:Queue:subject";
    public static final String MAIL_QUEUE_BODY = "Mail:Queue:body";
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void send() throws IOException {
        MailService mailService = new LocalMailService();
        Jedis jedis = new Jedis("localhost");

        String counter = jedis.get(MAIL_COUNTER);
        if (counter == null || !counter.matches("\\d+") || Integer.parseInt(counter) < 1) {
            jedis.del(MAIL_COUNTER);
            return;
        }

        do {
            MailService.Message message = new MailService.Message();
            String to = jedis.rpop(MAIL_QUEUE_TO);
            String subject = jedis.rpop(MAIL_QUEUE_SUBJECT);
            String body = jedis.rpop(MAIL_QUEUE_BODY);
            if(to == null || subject == null || body == null){
                continue;
            }
            message.setTo(to);
            message.setSubject(subject);
            message.setBody(body);
            logger.info("//////////sending email//////////"+body+"////");
            mailService.send(message);

        } while (jedis.decr(MAIL_COUNTER) > 0);
        jedis.del(MAIL_COUNTER);

        jedis.disconnect();
    }

    public static void main(String[] args) throws IOException {
        new MailSendingJob().send();
    }

}