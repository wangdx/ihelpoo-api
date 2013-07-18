package com.ihelpoo.api.model.entity;

/**
 * @author: dongxu.wang@acm.org
 */
public class IRecordCommentEntity {
    private int cid;
    private Integer uid;
    private Integer sid;
    private Integer toid;
    private String content;
    private String image;
    private Integer diffusionCo;
    private Integer time;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getToid() {
        return toid;
    }

    public void setToid(Integer toid) {
        this.toid = toid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getDiffusionCo() {
        return diffusionCo;
    }

    public void setDiffusionCo(Integer diffusionCo) {
        this.diffusionCo = diffusionCo;
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

        IRecordCommentEntity that = (IRecordCommentEntity) o;

        if (cid != that.cid) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (diffusionCo != null ? !diffusionCo.equals(that.diffusionCo) : that.diffusionCo != null) return false;
        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        if (sid != null ? !sid.equals(that.sid) : that.sid != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (toid != null ? !toid.equals(that.toid) : that.toid != null) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cid;
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (sid != null ? sid.hashCode() : 0);
        result = 31 * result + (toid != null ? toid.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (diffusionCo != null ? diffusionCo.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
