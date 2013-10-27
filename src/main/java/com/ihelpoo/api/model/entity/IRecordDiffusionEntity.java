package com.ihelpoo.api.model.entity;

/**
 * @author: dongxu.wang@acm.org
 */
public class IRecordDiffusionEntity {
    private int id;
    private int uid;
    private int sid;
    private Integer commentId;
    private Integer helpreplyId;
    private Integer assessId;
    private int time;
    private String view;

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

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getHelpreplyId() {
        return helpreplyId;
    }

    public void setHelpreplyId(Integer helpreplyId) {
        this.helpreplyId = helpreplyId;
    }

    public Integer getAssessId() {
        return assessId;
    }

    public void setAssessId(Integer assessId) {
        this.assessId = assessId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IRecordDiffusionEntity that = (IRecordDiffusionEntity) o;

        if (id != that.id) return false;
        if (sid != that.sid) return false;
        if (time != that.time) return false;
        if (uid != that.uid) return false;
        if (assessId != null ? !assessId.equals(that.assessId) : that.assessId != null) return false;
        if (commentId != null ? !commentId.equals(that.commentId) : that.commentId != null) return false;
        if (helpreplyId != null ? !helpreplyId.equals(that.helpreplyId) : that.helpreplyId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + uid;
        result = 31 * result + sid;
        result = 31 * result + (commentId != null ? commentId.hashCode() : 0);
        result = 31 * result + (helpreplyId != null ? helpreplyId.hashCode() : 0);
        result = 31 * result + (assessId != null ? assessId.hashCode() : 0);
        result = 31 * result + time;
        return result;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
