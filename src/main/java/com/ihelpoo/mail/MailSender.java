package com.ihelpoo.mail;


import com.ihelpoo.mail.MailService.Message;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import redis.clients.jedis.Jedis;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

final class MailSender {

    public static final String REDIS_HOST = "localhost";

    public static final String MAILGUN_COUNTER = "Mail:Mailgun:counter";
    public static final int MAILGUN_QUOTA = 200;// per day (86400000 seconds)

    public static final String MAIL_FROM = "info@ihelpoo.com";


    public static final Pattern pattern = Pattern.compile("qq.com|foxmail.com|sina.com");

    public void send(final Message message) throws MessagingException {
//        if (isQqOrSina(message) || overQuotaOfMailgun()) {
            try {
                sendByQqmail(message);
            } catch (EmailException e) {
                e.printStackTrace(); // here we might hit the limit of qqmail
                sendFromLocal(message);
            }
//        } else {
//            sendByMailgun(message);
//        }

    }

    private void sendFromLocal(Message message) {
        Properties props = new Properties();
        props.put("mail.smtp.allow8bitmime", "true");
        props.put("mail.smtp.host", "127.0.0.1");
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.from", "");
        props.put("mail.from", "");
        Session mailSession = Session.getInstance(props);

        SimpleEmail email = new SimpleEmail();
        email.setMailSession(mailSession);
        String result = null;
        try {
            email.setFrom(MAIL_FROM, "我帮圈圈");
            email.addTo(message.getTo());
            email.setSubject(message.getSubject());
            email.setCharset("UTF-8");
            email.setContent(wrapHtml(message.getBody()), "text/html");
            result = email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
        System.out.println("send mail from local machine, result is:" + result);
    }

    public void sendByMailgun(final Message message) throws MessagingException {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", "key-448of04auigg4w5ibi5tsgqsm6048nt8"));
        WebResource webResource = client.resource("https://api.mailgun.net/v2/oo.mailgun.org/messages");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("from", message.getFrom() == null ? "我帮圈圈 <ihelp@oo.mailgun.org>" : message.getFrom());
        formData.add("to", message.getTo());
        formData.add("subject", message.getSubject());
        formData.add("text", message.getBody());
        formData.add("html", wrapHtml(message.getBody()));
        ClientResponse result = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);

        computeQuotaOfMailgun();

        System.out.println(result);
    }

    public void sendByQqmail(final Message message) throws EmailException {
        SimpleEmail email = new SimpleEmail();
        email.setHostName("smtp.exmail.qq.com");
        email.setAuthentication("info@ihelpoo.com", "help2012");
        email.setCharset("UTF-8");
        email.setSSLOnConnect(true);
        email.setFrom(MAIL_FROM, "我帮圈圈");
        email.addTo(message.getTo());
        email.setSubject(message.getSubject());
        email.setContent(wrapHtml(message.getBody()), "text/html");
        String result = email.send();
        System.out.println("send mail by qqmail, result is:" + result);
    }

    private void computeQuotaOfMailgun() {
        incrDuring24Hours();
    }

    private void incrDuring24Hours() {
        Jedis jedis = new Jedis(REDIS_HOST);
        Map<String, String> all = jedis.hgetAll(MAILGUN_COUNTER);
        if (notEverBeenSet(all)) {
            jedis.hincrBy(MAILGUN_COUNTER, String.valueOf(System.currentTimeMillis()), 1L);
        } else {
            Set<String> keySet = all.keySet();
            String key = null;
            for (String keyStr : keySet) {
                key = keyStr;
            }
            Long keyLong = Long.parseLong(key);
            if (System.currentTimeMillis() - keyLong < 86400000) {
                jedis.hincrBy(MAILGUN_COUNTER, key, 1L);
            } else {
                jedis.hdel(MAILGUN_COUNTER, key);
                jedis.hincrBy(MAILGUN_COUNTER, String.valueOf(System.currentTimeMillis()), 1L);
            }
        }
        jedis.disconnect();
    }

    private boolean notEverBeenSet(Map<String, String> all) {
        return all == null || all.size() < 1;
    }

    private boolean overQuotaOfMailgun() {
        Jedis jedis = new Jedis(REDIS_HOST);
        boolean result = false;
        Map<String, String> all = jedis.hgetAll(MAILGUN_COUNTER);
        if (notEverBeenSet(all)) {
            result = false;
        } else {
            Set<String> keySet = all.keySet();
            String key = null;
            for (String keyStr : keySet) {
                key = keyStr;
            }
            String value = jedis.hget(MAILGUN_COUNTER, key);
            if (Integer.parseInt(value) < MAILGUN_QUOTA) {
                result = false;
            } else {
                System.err.println("WARN: exceed the quota of mailgun");
                result = true;
            }
        }
        jedis.disconnect();
        return result;
    }

    private boolean isQqOrSina(Message message) {
        return pattern.matcher(message.getTo()).find();
    }

    private String wrapHtml(String body) {
        StringBuilder template = new StringBuilder();
        template.append("<div style=\"width:100%!important;margin:0;padding:0\" marginheight=\"0\" marginwidth=\"0\"><center><table cellpadding=\"8\" cellspacing=\"0\" style=\"width:100%!important;background:#ffffff;margin:0;padding:0\" border=\"0\"><tbody><tr><td valign=\"top\">")
                .append("<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" border=\"0\"><tbody><tr><td bgcolor=\"#b9e1fb\" align=\"center\" style=\"font-size:0px\" height=\"1\"><img src=\"http://ihelpoo.cn/Public/image/common/email_header.png\" alt=\"\" style=\"width:100%;max-height:129px\"></td></tr><tr><td><table cellpadding=\"0\" cellspacing=\"0\" style=\"border-left:1px #b9e1fb solid;border-right:1px #b9e1fb solid;border-bottom:1px #b9e1fb solid;border-radius:0px 0px 4px 4px\" border=\"0\" align=\"center\"><tbody><tr><td colspan=\"3\" height=\"36\"></td></tr><tr><td width=\"36\"></td>")
                .append("<td width=\"454\" style=\"font-size:14px;color:#444444;font-family:'Open Sans','Lucida Grande','Segoe UI',Arial,Verdana,'Lucida Sans Unicode',Tahoma,'Sans Serif','Luxi Sans', 'DejaVu Sans', 'Hiragino Sans GB', STHeiti !important;border-collapse:collapse\" align=\"left\" valign=\"top\">")
                .append(body)
                .append("</td>")
                .append("<td width=\"36\"></td>")
                .append("</tr><tr><td colspan=\"3\" height=\"36\"></td></tr></tbody></table></td></tr></tbody></table><table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" border=\"0\"><tbody><tr><td height=\"10\"></td></tr><tr><td style=\"padding:0;border-collapse:collapse\"><table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" border=\"0\"><tbody><tr style=\"color:#a8b9c6;font-size:11px;font-family:'Open Sans','Lucida Grande','Segoe UI',Arial,Verdana,'Lucida Sans Unicode',Tahoma,'Sans Serif'\" valign=\"top\"><td width=\"400\" align=\"left\">© 2013 <a href=\"http://www.ihelpoo.cn\" target=\"_blank\">我帮圈圈</a> | <span class=\"il\">保留所有权利</span></td>")
                .append("<td width=\"128\" align=\"right\"></td>")
                .append("</tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table></center></div>");
        return template.toString();
//        return "<div style=\"font-family:verdana;font-size:12px;color:#555555;line-height:14pt\"> <div style=\"width:590px\"> <div style=\"background:url('http://ihelpoo.cn/Public/image/common/email_top.png') no-repeat;width:100%;min-height:75px;display:block\"> <div style=\"padding-top:30px;padding-left:50px;padding-right:50px\"><a target=\"_blank\" href=\"http://www.ihelpoo.com\"><img style=\"border:none\" src=\"http://ihelpoo.cn/Public/image/common/ihelpoo.png\" alt=\"我帮圈圈\" /></a> </div> </div> <div style=\"background:url('http://ihelpoo.cn/Public/image/common/email_mid.png') repeat-y;width:100%;display:block\"> <div style=\"padding-left:50px;padding-right:50px;padding-bottom:1px\"> <div style=\"border-bottom:1px solid #ededed\"></div> <div style=\"border-bottom:1px solid #ededed\"></div> <div style=\"margin:30px 0\">" + body + "</div> <div style=\"border-bottom:1px solid #ededed\"></div> <div style=\"border-bottom:1px solid #ededed\"></div> <div style=\"font-size:9px;color:#B0B0B0\">&copy; 2013 我帮圈圈 | 保留所有权利。</div> </div> </div> <div style=\"background:url('http://ihelpoo.cn/Public/image/common/email_bottom.png') no-repeat;width:100%;min-height:50px;display:block\" class=\"adL\"> <div style=\"padding-left:50px;padding-right:50px\"></div> </div> </div></div>";
    }


    public static void main(String[] args) {
        System.out.println(pattern.matcher("1321321@gmail.com").find());
    }

}