package com.ihelpoo.api.model;

import com.ihelpoo.common.Constant;
import com.ihelpoo.api.model.base.Notice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = Constant.IHELPOO_XML_ROOT)
@XmlAccessorType(XmlAccessType.FIELD)
public class WordResult {

    @XmlElement
    private int activeCount;
    @XmlElement
    private int pagesize;
    @XmlElement
    private Words activies;

    @XmlElement
    Notice notice;

    public int getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public Words getActivies() {
        return activies;
    }

    public void setActivies(Words activies) {
        this.activies = activies;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Word {
        public Word(Builder builder) {
            this.appclient = builder.appclient;
            this.author = builder.author;
            this.authorid = builder.authorid;
            this.catalog = builder.catalog;
            this.commentCount = builder.commentCount;
            this.id = builder.id;
            this.message = builder.message;
            this.objectcatalog = builder.objectcatalog;
            this.objectID = builder.objectID;
            this.objectreply = builder.objectreply;
            this.objecttitle = builder.objecttitle;
            this.objecttype = builder.objecttype;
            this.portrait = builder.portrait;
            this.pubDate = builder.pubDate;
            this.tweetimage = builder.tweetimage;
            this.url = builder.url;
        }

        public static class Builder {
            private int id;
            private String portrait;
            private String author;
            private int authorid;
            private int catalog;
            private int objecttype;
            private int objectcatalog;
            private String objecttitle;
            private int appclient;
            private Blob objectreply;
            private String url;
            private int objectID;
            private String message;
            private int commentCount;
            private String pubDate;
            private String tweetimage;

            public Word build() {
                return new Word(this);
            }

            public Builder id(int id) {
                this.id = id;
                return this;
            }

            public Builder portrait(String portrait) {
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

            public Builder catalog(int catalog) {
                this.catalog = catalog;
                return this;
            }

            public Builder objecttype(int objecttype) {
                this.objecttype = objecttype;
                return this;
            }

            public Builder objectcatalog(int objectcatalog) {
                this.objectcatalog = objectcatalog;
                return this;
            }

            public Builder objecttitle(String objecttitle) {
                this.objecttitle = objecttitle;
                return this;
            }

            public Builder appclient(int appclient) {
                this.appclient = appclient;
                return this;
            }

            public Builder objectreply(Blob objectreply) {
                this.objectreply = objectreply;
                return this;
            }

            public Builder url(String url) {
                this.url = url;
                return this;
            }

            public Builder objectID(int objectID) {
                this.objectID = objectID;
                return this;
            }

            public Builder message(String message) {
                this.message = message;
                return this;
            }

            public Builder commentCount(int commentCount) {
                this.commentCount = commentCount;
                return this;
            }

            public Builder pubDate(String pubDate) {
                this.pubDate = pubDate;
                return this;
            }

            public Builder tweetimage(String tweetimage) {
                this.tweetimage = tweetimage;
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
        private int catalog;
        @XmlElement
        private int objecttype;
        @XmlElement
        private int objectcatalog;
        @XmlElement
        private String objecttitle;
        @XmlElement
        private int appclient;
        @XmlElement
        private Blob objectreply;
        @XmlElement
        private String url;
        @XmlElement
        private int objectID;
        @XmlElement
        private String message;
        @XmlElement
        private int commentCount;
        @XmlElement
        private String pubDate;
        @XmlElement
        private String tweetimage;


    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Blob {
        @XmlElement
        String objectname;
        @XmlElement
        String objectbody;

        public Blob(String objectname, String objectbody) {
            this.objectname = objectname;
            this.objectbody = objectbody;
        }

        public String getObjectname() {
            return objectname;
        }

        public void setObjectname(String objectname) {
            this.objectname = objectname;
        }

        public String getObjectbody() {
            return objectbody;
        }

        public void setObjectbody(String objectbody) {
            this.objectbody = objectbody;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Words {
        @XmlElement
        List<Word> active;

        public Words(List<Word> word) {
            this.active = word;
        }

        public List<Word> getWord() {
            return active;
        }

        public void setWord(List<Word> word) {
            this.active = word;
        }
    }
}
