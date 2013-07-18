package com.ihelpoo.api.model.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
* @author: dongxu.wang@acm.org
*/
@XmlRootElement(name = "active")
@XmlAccessorType(XmlAccessType.FIELD)
public class Active {

    public Active(){}
    public Active(Builder builder) {
        this.academy = builder.academy;
        this.authorGossip = builder.authorGossip;
        this.authorType = builder.authorType;
        this.catalog = builder.catalog;
        this.commentCo = builder.commentCo;
        this.content = builder.content;
        this.diffusionCo = builder.diffusionCo;
        this.from = builder.from;
        this.iconUrl = builder.iconUrl;
        this.image = builder.image;
        this.nickname = builder.nickname;
        this.objectcatalog = builder.objectcatalog;
        this.objectID = builder.objectID;
        this.objectreply = builder.objectreply;
        this.objecttitle = builder.objecttitle;
        this.objecttype = builder.objecttype;
        this.iconUrl = builder.iconUrl;
        this.online = builder.online;
        this.sid = builder.sid;
        this.time = builder.time;
        this.uid = builder.uid;
        this.url = builder.url;
        this.rank = builder.rank;
    }

    public static class Builder {
        int sid;
        String iconUrl;
        String nickname;
        int uid;
        int catalog;
        int objecttype;
        int objectcatalog;
        String objecttitle;
        int from;
        ObjectReply objectreply;
        String url;
        int objectID;
        String content;
        int commentCo;
        String time;
        String image;


        int diffusionCo;
        String academy;
        String authorType;
        String authorGossip;
        int online;
        public String rank;

        public Active build() {
            return new Active(this);
        }

        public Builder sid(int sid) {
            this.sid = sid;
            return this;
        }

        public Builder avatar(String iconUrl) {
            this.iconUrl = iconUrl;
            return this;
        }

        public Builder name(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder uid(int uid) {
            this.uid = uid;
            return this;
        }

        public Builder catalog(int catalog) {
            this.catalog = catalog;
            return this;
        }

        public Builder setObjecttype(int objecttype) {
            this.objecttype = objecttype;
            return this;
        }

        public Builder setObjectcatalog(int objectcatalog) {
            this.objectcatalog = objectcatalog;
            return this;
        }

        public Builder setObjecttitle(String objecttitle) {
            this.objecttitle = objecttitle;
            return this;
        }

        public Builder by(int from) {
            this.from = from;
            return this;
        }

        public Builder setObjectreply(ObjectReply objectreply) {
            this.objectreply = objectreply;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setObjectID(int objectID) {
            this.objectID = objectID;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder commentCount(int commentCo) {
            this.commentCo = commentCo;
            return this;
        }

        public Builder date(String time) {
            this.time = time;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public Builder diffusionCount(int diffusionCo) {
            this.diffusionCo = diffusionCo;
            return this;
        }

        public Builder academy(String academy) {
            this.academy = academy;
            return this;
        }

        public Builder type(String authorType) {
            this.authorType = authorType;
            return this;
        }

        public Builder gossip(String authorGossip) {
            this.authorGossip = authorGossip;
            return this;
        }

        public Builder online(int online) {
            this.online = online;
            return this;
        }

        public Builder rank(String rank){
            this.rank = rank;
            return this;
        }
    }

    @XmlElement(name = "id")
    int sid;
    @XmlElement(name = "portrait")
    String iconUrl;
    @XmlElement(name = "author")
    String nickname;
    @XmlElement(name = "authorid")
    int uid;
    @XmlElement(name = "catalog")
    int catalog;
    @XmlElement(name = "objecttype")
    int objecttype;
    @XmlElement(name = "objectcatalog")
    int objectcatalog;
    @XmlElement(name = "objecttitle")
    String objecttitle;
    @XmlElement(name = "appclient")
    int from;
    @XmlElement(name = "objectreply")
    ObjectReply objectreply;
    @XmlElement(name = "url")
    String url;
    @XmlElement(name = "objectID")
    int objectID;
    @XmlElement(name = "message")
    String content;
    @XmlElement(name = "commentCount")
    int commentCo;
    @XmlElement(name = "pubDate")
    String time;
    @XmlElement(name = "tweetimage")
    String image;


    @XmlElement(name = "diffusionCo")
    int diffusionCo;
    @XmlElement(name = "academy")
    String academy;
    @XmlElement(name = "authorType")
    String authorType;
    @XmlElement(name = "authorGossip")
    String authorGossip;
    @XmlElement(name = "online")
    int online;
    @XmlElement(name="activeRank")
    String rank;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCatalog() {
        return catalog;
    }

    public void setCatalog(int catalog) {
        this.catalog = catalog;
    }

    public int getObjecttype() {
        return objecttype;
    }

    public void setObjecttype(int objecttype) {
        this.objecttype = objecttype;
    }

    public int getObjectcatalog() {
        return objectcatalog;
    }

    public void setObjectcatalog(int objectcatalog) {
        this.objectcatalog = objectcatalog;
    }

    public String getObjecttitle() {
        return objecttitle;
    }

    public void setObjecttitle(String objecttitle) {
        this.objecttitle = objecttitle;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public ObjectReply getObjectreply() {
        return objectreply;
    }

    public void setObjectreply(ObjectReply objectreply) {
        this.objectreply = objectreply;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getObjectID() {
        return objectID;
    }

    public void setObjectID(int objectID) {
        this.objectID = objectID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommentCo() {
        return commentCo;
    }

    public void setCommentCo(int commentCo) {
        this.commentCo = commentCo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getDiffusionCo() {
        return diffusionCo;
    }

    public void setDiffusionCo(int diffusionCo) {
        this.diffusionCo = diffusionCo;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getAuthorType() {
        return authorType;
    }

    public void setAuthorType(String authorType) {
        this.authorType = authorType;
    }

    public String getAuthorGossip() {
        return authorGossip;
    }

    public void setAuthorGossip(String authorGossip) {
        this.authorGossip = authorGossip;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }
}
