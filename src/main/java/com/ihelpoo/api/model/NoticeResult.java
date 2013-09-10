package com.ihelpoo.api.model;

import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.base.Result;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author: dongxu.wang@acm.org
 */

@XmlRootElement(name = "ihelpoo")
@XmlAccessorType(XmlAccessType.FIELD)
public class NoticeResult {

    //TODO add amount of the chats count

    @XmlElement
    private Result result;
    @XmlElement
    private Notice notice;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
