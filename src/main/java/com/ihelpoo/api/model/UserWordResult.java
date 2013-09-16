package com.ihelpoo.api.model;

import com.ihelpoo.common.Constant;
import com.ihelpoo.api.model.base.Actives;
import com.ihelpoo.api.model.base.Notice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author: dongxu.wang@acm.org
 */

@XmlRootElement(name = Constant.IHELPOO_XML_ROOT)
@XmlAccessorType(XmlAccessType.FIELD)
public class UserWordResult {
    @XmlElement
    int pagesize;
    @XmlElement
    User user;
    @XmlElement
    Actives actives;
    @XmlElement
    Notice notice;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class User {
        @XmlElement
        String name;
        @XmlElement
        int uid;
        @XmlElement
        String portrait;
        @XmlElement
        String jointime;
        @XmlElement
        String gender;
        @XmlElement
        String from;
        @XmlElement
        String devplatform;
        @XmlElement
        String expertise;
        @XmlElement
        String relation;
        @XmlElement
        String latestonline;

        private User(){}
        public User(Builder buider){
            this.devplatform = buider.devplatform;
            this.expertise = buider.expertise;
            this.from = buider.from;
            this.gender = buider.gender;
            this.jointime = buider.jointime;
            this.latestonline = buider.latestonline;
            this.name = buider.name;
            this.portrait = buider.portrait;
            this.relation = buider.relation;

        }
        public static class Builder{
            public User build(){
                return new User(this);
            }
            String name;
            int uid;
            String portrait;
            String jointime;
            String gender;
            String from;
            String devplatform;
            String expertise;
            String relation;
            String latestonline;

            public Builder nickname(String name) {
                this.name = name;
                return this;
            }

            public Builder uid(int uid) {
                this.uid = uid;
                return this;
            }

            public Builder avatar(String portrait) {
                this.portrait = portrait;
                return this;
            }

            public Builder type(String jointime) {
                this.jointime = jointime;
                return this;
            }

            public Builder gossip(String gender) {
                this.gender = gender;
                return this;
            }

            public Builder academy(String from) {
                this.from = from;
                return this;
            }

            public Builder major(String devplatform) {
                this.devplatform = devplatform;
                return this;
            }

            public Builder rank(String expertise) {
                this.expertise = expertise;
                return this;
            }

            public Builder foing(String relation) {
                this.relation = relation;
                return this;
            }

            public Builder foer(String latestonline) {
                this.latestonline = latestonline;
                return this;
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getJointime() {
            return jointime;
        }

        public void setJointime(String jointime) {
            this.jointime = jointime;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getDevplatform() {
            return devplatform;
        }

        public void setDevplatform(String devplatform) {
            this.devplatform = devplatform;
        }

        public String getExpertise() {
            return expertise;
        }

        public void setExpertise(String expertise) {
            this.expertise = expertise;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getLatestonline() {
            return latestonline;
        }

        public void setLatestonline(String latestonline) {
            this.latestonline = latestonline;
        }
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Actives getActives() {
        return actives;
    }

    public void setActives(Actives actives) {
        this.actives = actives;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

}
