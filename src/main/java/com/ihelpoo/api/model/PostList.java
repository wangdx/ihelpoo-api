package com.ihelpoo.api.model;

import com.ihelpoo.api.model.base.Base;
import com.ihelpoo.api.model.base.Notice;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author: dongxu.wang@acm.org
 */
@XmlRootElement(name = "oschina")
public class PostList{
    @XmlElement
    private int postCount;
    @XmlElement
    private int pagesize = 20;
    @XmlElement
    private Posts posts;

    @XmlElement
    private Notice notice;
    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

}
