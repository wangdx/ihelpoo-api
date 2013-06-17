package com.ihelpoo.api.model;

import com.ihelpoo.api.common.OoConstant;
import com.ihelpoo.api.model.base.Notice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author: dongxu.wang@acm.org
 */

@XmlRootElement(name = OoConstant.IHELPOO_XML_ROOT)
@XmlAccessorType(XmlAccessType.FIELD)
public class TweetDetailResult {
    @XmlElement
    TweetResult.Tweet tweet;
    @XmlElement
    Notice notice;

    public TweetDetailResult(){}

    public TweetDetailResult(TweetResult.Tweet tweet, Notice notice) {
        this.tweet = tweet;
        this.notice = notice;
    }

    public TweetResult.Tweet getTweet() {
        return tweet;
    }

    public void setTweet(TweetResult.Tweet tweet) {
        this.tweet = tweet;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
