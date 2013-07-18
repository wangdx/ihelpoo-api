package com.ihelpoo.api.model;

import com.ihelpoo.api.common.OoConstant;
import com.ihelpoo.api.model.base.Notice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
@XmlRootElement(name = OoConstant.IHELPOO_XML_ROOT)
@XmlAccessorType(XmlAccessType.FIELD)
public class TweetResult {
    @XmlElement
    private int tweetCount;
    @XmlElement
    private int pagesize;
    @XmlElement
    Tweets tweets;
    @XmlElement
    Notice notice;

    public int getTweetCount() {
        return tweetCount;
    }

    public void setTweetCount(int tweetCount) {
        this.tweetCount = tweetCount;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public Tweets getTweets() {
        return tweets;
    }

    public void setTweets(Tweets tweets) {
        this.tweets = tweets;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Tweets {
        @XmlElement
        List<Tweet> tweet;

        public Tweets(List<Tweet> tweet){
            this.tweet = tweet;
        }

        public List<Tweet> getTweet() {
            return tweet;
        }

        public void setTweet(List<Tweet> tweet) {
            this.tweet = tweet;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Tweet {
        private Tweet(Builder builder) {
            this.academy = builder.academy;
            this.appclient = builder.appclient;
            this.author = builder.author;
            this.authorGossip = builder.authorGossip;
            this.authorid = builder.authorid;
            this.authorType = builder.authorType;
            this.body = builder.body;
            this.commentCount = builder.commentCount;
            this.id = builder.id;
            this.imgBig = builder.imgBig;
            this.imgSmall = builder.imgSmall;
            this.onlineState = builder.onlineState;
            this.portrait = builder.portrait;
            this.pubDate = builder.pubDate;
            this.rank = builder.rank;
            this.spreadCount = builder.spreadCount;
        }

        public static class Builder {
            private int id;
            private String portrait;
            private String author;
            private int authorid;
            private String body;
            private int appclient;
            private int commentCount;
            private String pubDate;
            private String imgSmall;
            private String imgBig;

            private int spreadCount;
            private String academy;
            private String authorType;
            private String authorGossip;
            private String onlineState;
            private int rank;

            public Tweet build() {
                return new Tweet(this);
            }

            public Builder id(int id) {
                this.id = id;
                return this;
            }

            public Builder avatar(String portrait) {
                this.portrait = portrait;
                return this;
            }

            public Builder author(String author) {
                this.author = author;
                return this;
            }

            public Builder authorid(int authorid) {
                this.authorid = authorid;
                return this;
            }

            public Builder content(String body) {
                this.body = body;
                return this;
            }

            public Builder from(int appclient) {
                this.appclient = appclient;
                return this;
            }

            public Builder comments(int commentCount) {
                this.commentCount = commentCount;
                return this;
            }

            public Builder date(String pubDate) {
                this.pubDate = pubDate;
                return this;
            }

            public Builder small(String imgSmall) {
                this.imgSmall = imgSmall;
                return this;
            }

            public Builder big(String imgBig) {
                this.imgBig = imgBig;
                return this;
            }

            public Builder spreads(int spreadCount) {
                this.spreadCount = spreadCount;
                return this;
            }

            public Builder academy(String academy) {
                this.academy = academy;
                return this;
            }

            public Builder authorType(String authorType) {
                this.authorType = authorType;
                return this;
            }

            public Builder authorGossip(String authorGossip) {
                this.authorGossip = authorGossip;
                return this;
            }

            public Builder onlineState(String onlineState) {
                this.onlineState = onlineState;
                return this;
            }

            public Builder authorRank(int rank) {
                this.rank = rank;
                return this;
            }
        }

        @XmlElement
        private int id;
        @XmlElement
        private String portrait;
        @XmlElement
        private String author;
        @XmlElement
        private int authorid;
        @XmlElement
        private String body;
        @XmlElement
        private int appclient;
        @XmlElement
        private int commentCount;
        @XmlElement
        private String pubDate;
        @XmlElement
        private String imgSmall;
        @XmlElement
        private String imgBig;

        @XmlElement
        private int spreadCount;
        @XmlElement
        private String academy;
        @XmlElement
        private String authorType;
        @XmlElement
        private String authorGossip;
        @XmlElement
        private String onlineState;
        @XmlElement
        private int rank;

    }


}
