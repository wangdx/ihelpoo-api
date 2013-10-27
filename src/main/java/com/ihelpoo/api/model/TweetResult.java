package com.ihelpoo.api.model;

import com.ihelpoo.common.Constant;
import com.ihelpoo.api.model.obj.Notice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
@XmlRootElement(name = Constant.IHELPOO_XML_ROOT)
@XmlAccessorType(XmlAccessType.FIELD)
public class TweetResult {
    @XmlElement
    public int tweetCount;
    @XmlElement
    public int pagesize;
    @XmlElement
    public Tweets tweets;
    @XmlElement
    public Notice notice;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Tweets {
        @XmlElement
        public List<Tweet> tweet;

        public Tweets(List<Tweet> tweet){
            this.tweet = tweet;
        }

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Tweet {

        @XmlElement
        public int id;
        @XmlElement
        public String sayType;
        @XmlElement
        public String portrait;
        @XmlElement
        public String author;
        @XmlElement
        public int authorid;
        @XmlElement
        public String body;
        @XmlElement
        public int appclient;
        @XmlElement
        public String by;
        @XmlElement
        public int commentCount;
        @XmlElement
        public String pubDate;
        @XmlElement
        public String imgSmall;
        @XmlElement
        public String imgBig;

        @XmlElement
        public int spreadCount;
        @XmlElement
        public int plusCount;
        @XmlElement
        public String academy;
        @XmlElement
        public String authorType;
        @XmlElement
        public String authorGossip;
        @XmlElement
        public String onlineState;
        @XmlElement
        public int rank;
        @XmlElement
        public int plusByMe;
        @XmlElement
        public int diffuseByMe;

    }


}
