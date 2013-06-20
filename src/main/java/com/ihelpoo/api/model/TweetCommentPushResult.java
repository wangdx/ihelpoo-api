package com.ihelpoo.api.model;

import com.ihelpoo.api.common.OoConstant;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.base.Result;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author: dongxu.wang@acm.org
 */

@XmlRootElement(name = OoConstant.IHELPOO_XML_ROOT)
@XmlAccessorType(XmlAccessType.FIELD)
public class TweetCommentPushResult {
    @XmlElement
    Result result;
    @XmlElement
    TweetCommentResult.Comment comment;
    @XmlElement
    Notice notice;

    public TweetCommentPushResult(){}

    public TweetCommentPushResult(Result result, TweetCommentResult.Comment comment, Notice notice) {
        this.result = result;
        this.comment = comment;
        this.notice = notice;
    }

    public TweetCommentResult.Comment getComment() {
        return comment;
    }

    public void setComment(TweetCommentResult.Comment comment) {
        this.comment = comment;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
