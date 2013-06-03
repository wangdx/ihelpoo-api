package com.ihelpoo.api.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * record say.
 * @author dongxu.wang@acm.org
 * @version 1
 */
@SuppressWarnings("unused")
@XmlRootElement
public class RecordSay implements Serializable {
    /**
     *  sid.
     */
    private Integer sid;
    /**
     * uid.
     */
    private Integer uid;
    /**
     * content.
     */
    private String content;
    /**
     * url.
     */
    private String url;

    /**
     * constructor.
     */
    public RecordSay() {
    }

    /**
     * get url.
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * set url.
     * @param url some url
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * get sid.
     * @return sid.
     */
    public Integer getSid() {
        return sid;
    }

    /**
     * set sid.
     * @param sid some sid.
     */
    public void setSid(final Integer sid) {
        this.sid = sid;
    }

    /**
     * get uid.
     * @return get some uid
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * set uid.
     * @param uid some uid.
     */
    public void setUid(final Integer uid) {
        this.uid = uid;
    }

    /**
     * get content.
     * @return some content
     */
    public String getContent() {
        return content;
    }

    /**
     * some content.
     * @param content some content
     */
    public void setContent(final String content) {
        this.content = content;
    }
}
