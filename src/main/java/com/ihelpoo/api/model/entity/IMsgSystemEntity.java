package com.ihelpoo.api.model.entity;

/**
 * @author: dongxu.wang@acm.org
 */
public class IMsgSystemEntity {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private Integer uid;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private Integer urlId;

    public Integer getUrlId() {
        return urlId;
    }

    public void setUrlId(Integer urlId) {
        this.urlId = urlId;
    }

    private Integer fromUid;

    public Integer getFromUid() {
        return fromUid;
    }

    public void setFromUid(Integer fromUid) {
        this.fromUid = fromUid;
    }

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private Integer time;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    private Integer deliver;

    public Integer getDeliver() {
        return deliver;
    }

    public void setDeliver(Integer deliver) {
        this.deliver = deliver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IMsgSystemEntity that = (IMsgSystemEntity) o;

        if (id != that.id) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (deliver != null ? !deliver.equals(that.deliver) : that.deliver != null) return false;
        if (fromUid != null ? !fromUid.equals(that.fromUid) : that.fromUid != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;
        if (urlId != null ? !urlId.equals(that.urlId) : that.urlId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (urlId != null ? urlId.hashCode() : 0);
        result = 31 * result + (fromUid != null ? fromUid.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (deliver != null ? deliver.hashCode() : 0);
        return result;
    }
}
