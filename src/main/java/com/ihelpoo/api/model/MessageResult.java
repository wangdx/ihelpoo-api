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
public class MessageResult {
    @XmlElement
    private int pagesize;

    @XmlElement
    Messages newslist;

    @XmlElement
    Notice notice;

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public Messages getNewslist() {
        return newslist;
    }

    public void setNewslist(Messages newslist) {
        this.newslist = newslist;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Messages {
        @XmlElement
        List<Message> news;

        public Messages(List<Message> news) {
            this.news = news;
        }

        public List<Message> getNews() {
            return news;
        }

        public void setNews(List<Message> news) {
            this.news = news;
        }
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Message {

        private Message(Builder builder) {
            this.author = builder.author;
            this.inout = builder.inout;
            this.commentCount = builder.commentCount;
            this.id = builder.id;
            this.pubDate = builder.pubDate;
            this.title = builder.title;
            this.url = builder.url;
        }

        public static class Builder {
            private int id;
            private String title;
            private int commentCount;
            private String author;
            private String pubDate;
            private String url;
            private String inout;

            public Message build() {
                return new Message(this);
            }

            public Builder id(int id) {
                this.id = id;
                return this;
            }

            public Builder title(String title) {
                this.title = title;
                return this;
            }

            public Builder commentCount(int commentCount) {
                this.commentCount = commentCount;
                return this;
            }

            public Builder author(String author) {
                this.author = author;
                return this;
            }

            public Builder inout(String inout) {
                this.inout = inout;
                return this;
            }

            public Builder pubDate(String pubDate) {
                this.pubDate = pubDate;
                return this;
            }

            public Builder url(String url) {
                this.url = url;
                return this;
            }
        }

        @XmlElement
        private int id;
        @XmlElement
        private String title;
        @XmlElement
        private int commentCount;
        @XmlElement
        private String author;
        @XmlElement
        private String inout;
        @XmlElement
        private String pubDate;
        @XmlElement
        private String url;


    }

}
