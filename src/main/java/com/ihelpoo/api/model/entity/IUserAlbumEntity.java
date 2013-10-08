package com.ihelpoo.api.model.entity;

/**
 * @author: echowdx@gmail.com
 */
public class IUserAlbumEntity {
    private int id;
    private Integer uid;
    private String type;
    private int foreignid;
    private String url;
    private int size;
    private Integer hit;
    private Integer time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getForeignid() {
        return foreignid;
    }

    public void setForeignid(int foreignid) {
        this.foreignid = foreignid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Integer getHit() {
        return hit;
    }

    public void setHit(Integer hit) {
        this.hit = hit;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IUserAlbumEntity that = (IUserAlbumEntity) o;

        if (foreignid != that.foreignid) return false;
        if (id != that.id) return false;
        if (size != that.size) return false;
        if (hit != null ? !hit.equals(that.hit) : that.hit != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + foreignid;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + size;
        result = 31 * result + (hit != null ? hit.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
