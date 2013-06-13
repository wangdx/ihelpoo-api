package com.ihelpoo.api.model.entity;

/**
 * @author: dongxu.wang@acm.org
 */
public class IUserCoinsEntity {
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

    private String total;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    private String use;

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    private String way;

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private Integer checkTi;

    public Integer getCheckTi() {
        return checkTi;
    }

    public void setCheckTi(Integer checkTi) {
        this.checkTi = checkTi;
    }

    private Integer time;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    private int deliver;

    public int getDeliver() {
        return deliver;
    }

    public void setDeliver(int deliver) {
        this.deliver = deliver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IUserCoinsEntity that = (IUserCoinsEntity) o;

        if (deliver != that.deliver) return false;
        if (id != that.id) return false;
        if (checkTi != null ? !checkTi.equals(that.checkTi) : that.checkTi != null) return false;
        if (hash != null ? !hash.equals(that.hash) : that.hash != null) return false;
        if (reason != null ? !reason.equals(that.reason) : that.reason != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (total != null ? !total.equals(that.total) : that.total != null) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;
        if (use != null ? !use.equals(that.use) : that.use != null) return false;
        if (way != null ? !way.equals(that.way) : that.way != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (use != null ? use.hashCode() : 0);
        result = 31 * result + (way != null ? way.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (hash != null ? hash.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (checkTi != null ? checkTi.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + deliver;
        return result;
    }
}
