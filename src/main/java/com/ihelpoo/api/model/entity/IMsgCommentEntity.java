package com.ihelpoo.api.model.entity;

/**
 * @author: dongxu.wang@acm.org
 */
public class IMsgCommentEntity {
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

    private Integer ncid;

    public Integer getNcid() {
        return ncid;
    }

    public void setNcid(Integer ncid) {
        this.ncid = ncid;
    }

    private Integer rid;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
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

        IMsgCommentEntity that = (IMsgCommentEntity) o;

        if (id != that.id) return false;
        if (cid != null ? !cid.equals(that.cid) : that.cid != null) return false;
        if (deliver != null ? !deliver.equals(that.deliver) : that.deliver != null) return false;
        if (ncid != null ? !ncid.equals(that.ncid) : that.ncid != null) return false;
        if (rid != null ? !rid.equals(that.rid) : that.rid != null) return false;
        if (sid != null ? !sid.equals(that.sid) : that.sid != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (sid != null ? sid.hashCode() : 0);
        result = 31 * result + (cid != null ? cid.hashCode() : 0);
        result = 31 * result + (ncid != null ? ncid.hashCode() : 0);
        result = 31 * result + (rid != null ? rid.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (deliver != null ? deliver.hashCode() : 0);
        return result;
    }
}
