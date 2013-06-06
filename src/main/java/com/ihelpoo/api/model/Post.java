package com.ihelpoo.api.model;

import com.ihelpoo.api.model.base.Entity;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author: dongxu.wang@acm.org
 */
public class Post {
    @XmlElement
    private String portrait;
    @XmlElement
    private String author;
    @XmlElement
    private Integer authorid;
    @XmlElement
    private String title;
    @XmlElement
    private Integer answerCount;
    @XmlElement
    private Integer viewCount;
    @XmlElement
    private String pubDate;

    @XmlElement
    private String answer;

    @XmlElement
    protected int id;

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAuthorid(Integer authorid) {
        this.authorid = authorid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setId(int id) {
        this.id = id;
    }
}
