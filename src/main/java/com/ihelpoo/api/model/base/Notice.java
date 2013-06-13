package com.ihelpoo.api.model.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author: dongxu.wang@acm.org
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Notice {
    @XmlElement
    private int atmeCount;
    @XmlElement
    private int msgCount;
    @XmlElement
    private int reviewCount;
    @XmlElement
    private int newFansCount;

    /**
     * @deprecated
     */
    public Notice(){};

    public Notice(Builder builder) {
        this.atmeCount = builder.atmeCount;
        this.msgCount = builder.msgCount;
        this.reviewCount = builder.reviewCount;
        this.newFansCount = builder.newFansCount;
    }

    public static class Builder{
        /* at messages count */
        private int atmeCount;
        /* Talk contents count */
        private int msgCount;
        /* comment messages count */
        private int reviewCount;
        /* System messages count */
        private int newFansCount;

        public Builder(){}
        public Builder at(int atCount) {
            this.atmeCount = atCount;
            return this;
        }

        public Builder talk(int talkCount) {
            this.msgCount = talkCount;
            return this;
        }

        public Builder comment(int commentCount) {
            this.reviewCount = commentCount;
            return this;
        }

        public Builder system(int systemCount) {
            this.newFansCount = systemCount;
            return this;
        }
        public Notice build(){
            return new Notice(this);
        }
    }
    public void setAtmeCount(int atmeCount) {
        this.atmeCount = atmeCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public void setNewFansCount(int newFansCount) {
        this.newFansCount = newFansCount;
    }

    public int getAtmeCount() {
        return atmeCount;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public int getNewFansCount() {
        return newFansCount;
    }
}
