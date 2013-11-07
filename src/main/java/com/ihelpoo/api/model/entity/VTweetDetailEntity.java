package com.ihelpoo.api.model.entity;

/**
 * @author: echowdx@gmail.com
 */
public class VTweetDetailEntity {
    private int sid;
    private String sayType;
    private Integer uid;
    private String iconUrl;
    private String online;
    private Integer commentCo;
    private Integer diffusionCo;
    private int plusCo;
    private String by;
    private String content;
    private Integer time;
    private Integer active;
    private Integer sex;
    private String birthday;
    private String academy;
    private Integer authorType;
    private String enterYear;
    private String author;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSayType() {
        return sayType;
    }

    public void setSayType(String sayType) {
        this.sayType = sayType;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public Integer getCommentCo() {
        return commentCo;
    }

    public void setCommentCo(Integer commentCo) {
        this.commentCo = commentCo;
    }

    public Integer getDiffusionCo() {
        return diffusionCo;
    }

    public void setDiffusionCo(Integer diffusionCo) {
        this.diffusionCo = diffusionCo;
    }

    public int getPlusCo() {
        return plusCo;
    }

    public void setPlusCo(int plusCo) {
        this.plusCo = plusCo;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public Integer getAuthorType() {
        return authorType;
    }

    public void setAuthorType(Integer authorType) {
        this.authorType = authorType;
    }

    public String getEnterYear() {
        return enterYear;
    }

    public void setEnterYear(String enterYear) {
        this.enterYear = enterYear;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VTweetDetailEntity that = (VTweetDetailEntity) o;

        if (plusCo != that.plusCo) return false;
        if (sid != that.sid) return false;
        if (academy != null ? !academy.equals(that.academy) : that.academy != null) return false;
        if (active != null ? !active.equals(that.active) : that.active != null) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (authorType != null ? !authorType.equals(that.authorType) : that.authorType != null) return false;
        if (birthday != null ? !birthday.equals(that.birthday) : that.birthday != null) return false;
        if (by != null ? !by.equals(that.by) : that.by != null) return false;
        if (commentCo != null ? !commentCo.equals(that.commentCo) : that.commentCo != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (diffusionCo != null ? !diffusionCo.equals(that.diffusionCo) : that.diffusionCo != null) return false;
        if (enterYear != null ? !enterYear.equals(that.enterYear) : that.enterYear != null) return false;
        if (iconUrl != null ? !iconUrl.equals(that.iconUrl) : that.iconUrl != null) return false;
        if (online != null ? !online.equals(that.online) : that.online != null) return false;
        if (sayType != null ? !sayType.equals(that.sayType) : that.sayType != null) return false;
        if (sex != null ? !sex.equals(that.sex) : that.sex != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sid;
        result = 31 * result + (sayType != null ? sayType.hashCode() : 0);
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (iconUrl != null ? iconUrl.hashCode() : 0);
        result = 31 * result + (online != null ? online.hashCode() : 0);
        result = 31 * result + (commentCo != null ? commentCo.hashCode() : 0);
        result = 31 * result + (diffusionCo != null ? diffusionCo.hashCode() : 0);
        result = 31 * result + plusCo;
        result = 31 * result + (by != null ? by.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (academy != null ? academy.hashCode() : 0);
        result = 31 * result + (authorType != null ? authorType.hashCode() : 0);
        result = 31 * result + (enterYear != null ? enterYear.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }
}
