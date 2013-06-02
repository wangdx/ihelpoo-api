package com.ihelpoo.api.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RecordSay {
    private Integer sid;
    private Integer uid;
    private String content;

    public RecordSay() {
    }

    public RecordSay(Integer sid, Integer uid, String content) {
        this.sid = sid;
        this.uid = uid;
        this.content = content;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
