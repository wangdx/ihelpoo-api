package com.ihelpoo.api.model.entity;

/**
 * @author: dongxu.wang@acm.org
 */
public class IMsgAtEntity {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private Integer touid;

    public Integer getTouid() {
        return touid;
    }

    public void setTouid(Integer touid) {
        this.touid = touid;
    }

    private int fromuid;

    public int getFromuid() {
        return fromuid;
    }

    public void setFromuid(int fromuid) {
        this.fromuid = fromuid;
    }

    private Integer sid;

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    private Integer cid;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    private Integer hid;

    public Integer getHid() {
        return hid;
    }

    public void setHid(Integer hid) {
        this.hid = hid;
    }

    private Integer aid;

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    private Integer time;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    private String deliver;

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IMsgAtEntity that = (IMsgAtEntity) o;

        if (fromuid != that.fromuid) return false;
        if (id != that.id) return false;
        if (aid != null ? !aid.equals(that.aid) : that.aid != null) return false;
        if (cid != null ? !cid.equals(that.cid) : that.cid != null) return false;
        if (deliver != null ? !deliver.equals(that.deliver) : that.deliver != null) return false;
        if (hid != null ? !hid.equals(that.hid) : that.hid != null) return false;
        if (sid != null ? !sid.equals(that.sid) : that.sid != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (touid != null ? !touid.equals(that.touid) : that.touid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (touid != null ? touid.hashCode() : 0);
        result = 31 * result + fromuid;
        result = 31 * result + (sid != null ? sid.hashCode() : 0);
        result = 31 * result + (cid != null ? cid.hashCode() : 0);
        result = 31 * result + (hid != null ? hid.hashCode() : 0);
        result = 31 * result + (aid != null ? aid.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (deliver != null ? deliver.hashCode() : 0);
        return result;
    }
}
