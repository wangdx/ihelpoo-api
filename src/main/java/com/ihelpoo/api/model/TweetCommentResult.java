package com.ihelpoo.api.model;

import com.ihelpoo.common.Constant;
import com.ihelpoo.api.model.base.Notice;

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
public class TweetCommentResult {
    @XmlElement
    private int allCount;
    @XmlElement
    private int pagesize;
    @XmlElement
    private Comments comments;
    @XmlElement
    Notice notice;


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Comments {
        @XmlElement
        List<Comment> comment;

        public Comments(List<Comment> comment) {
            this.comment = comment;
        }

        public List<Comment> getComment() {
            return comment;
        }

        public void setComment(List<Comment> comment) {
            this.comment = comment;
        }
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Comment {
        @XmlElement
        private int id;
        @XmlElement
        private String portrait;
        @XmlElement
        private String author;
        @XmlElement
        private int authorid;
        @XmlElement
        private String content;
        @XmlElement
        private String pubDate;
        @XmlElement
        private int appclient;

        public Comment(){}
        public Comment(Builder builder) {
            this.appclient = builder.appclient;
            this.author = builder.author;
            this.authorid = builder.authorid;
            this.id = builder.id;
            this.portrait = builder.portrait;
            this.pubDate = builder.pubDate;
            this.content = builder.content;
        }


        public static class Builder {
            private int id;
            private String portrait;
            private String author;
            private int authorid;
            private String content;
            private String pubDate;
            private int appclient;

            public Comment build() {
                return new Comment(this);
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

            public Builder content(String content) {
                this.content = content;
                return this;
            }

            public Builder date(String pubDate) {
                this.pubDate = pubDate;
                return this;
            }

            public Builder by(int appclient) {
                this.appclient = appclient;
                return this;
            }
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getAuthorid() {
            return authorid;
        }

        public void setAuthorid(int authorid) {
            this.authorid = authorid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPubDate() {
            return pubDate;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public int getAppclient() {
            return appclient;
        }

        public void setAppclient(int appclient) {
            this.appclient = appclient;
        }
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
