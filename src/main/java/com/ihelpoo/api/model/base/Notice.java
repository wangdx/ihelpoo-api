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
