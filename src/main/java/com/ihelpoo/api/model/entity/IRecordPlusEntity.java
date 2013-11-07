package com.ihelpoo.api.model.entity;

/**
 * @author: echowdx@gmail.com
 */
public class IRecordPlusEntity {
    private int id;
    private int sid;
    private int uid;
    private String view;
    private int deliver;
    private int createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public int getDeliver() {
        return deliver;
    }

    public void setDeliver(int deliver) {
        this.deliver = deliver;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IRecordPlusEntity that = (IRecordPlusEntity) o;

        if (createTime != that.createTime) return false;
        if (deliver != that.deliver) return false;
        if (id != that.id) return false;
        if (sid != that.sid) return false;
        if (uid != that.uid) return false;
        if (view != null ? !view.equals(that.view) : that.view != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + sid;
        result = 31 * result + uid;
        result = 31 * result + (view != null ? view.hashCode() : 0);
        result = 31 * result + deliver;
        result = 31 * result + createTime;
        return result;
    }
}
