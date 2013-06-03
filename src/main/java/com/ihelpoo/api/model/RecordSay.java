package com.ihelpoo.api.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@SuppressWarnings("unused")
@XmlRootElement
public class RecordSay implements Serializable {
    private Integer sid;
    private Integer uid;
    private String content;
    private String url;

    public RecordSay() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
