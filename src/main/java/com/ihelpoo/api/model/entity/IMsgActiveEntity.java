package com.ihelpoo.api.model.entity;

public class IMsgActiveEntity {
    private int id;
    private int uid;
    private Integer total;
    private int change;
    private String way;
    private String reason;
    private int time;
    private String deliver;
    private int school;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public int getSchool() {
        return school;
    }

    public void setSchool(int school) {
        this.school = school;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IMsgActiveEntity that = (IMsgActiveEntity) o;

        if (change != that.change) return false;
        if (id != that.id) return false;
        if (school != that.school) return false;
        if (time != that.time) return false;
        if (uid != that.uid) return false;
        if (deliver != null ? !deliver.equals(that.deliver) : that.deliver != null) return false;
        if (reason != null ? !reason.equals(that.reason) : that.reason != null) return false;
        if (total != null ? !total.equals(that.total) : that.total != null) return false;
        if (way != null ? !way.equals(that.way) : that.way != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + uid;
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + change;
        result = 31 * result + (way != null ? way.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + time;
        result = 31 * result + (deliver != null ? deliver.hashCode() : 0);
        result = 31 * result + school;
        return result;
    }
}
