package com.ihelpoo.api.model;

import com.ihelpoo.api.model.base.Actives;
import com.ihelpoo.api.model.base.Notice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author: dongxu.wang@acm.org
 */

@XmlRootElement(name = "ihelpoo")
@XmlAccessorType(XmlAccessType.FIELD)
public class StreamResult {
    @XmlElement(name = "activeCount")
    int activeCount;
    @XmlElement(name = "pagesize")
    int pagesize;
    @XmlElement(name = "activies")
    Actives activies;
    @XmlElement(name = "notice")
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

    public Actives getActivies() {
        return activies;
    }

    public void setActivies(Actives activies) {
        this.activies = activies;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
