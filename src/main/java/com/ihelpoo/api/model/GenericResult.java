package com.ihelpoo.api.model;

import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.base.Result;
import com.ihelpoo.api.model.base.User;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author: dongxu.wang@acm.org
 */
@XmlRootElement(name = "ihelpoo")
@XmlAccessorType(XmlAccessType.FIELD)
public class GenericResult {
    @XmlElement
    private Result result;
    @XmlElement
    private User user;
    @XmlElement
    private Notice notice;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
