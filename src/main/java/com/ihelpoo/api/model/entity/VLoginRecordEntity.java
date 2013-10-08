package com.ihelpoo.api.model.entity;

/**
 * @author: echowdx@gmail.com
 */
public class VLoginRecordEntity {
    private int sid;
    private Integer uid;
    private String sayType;
    private String content;
    private String image;
    private String url;
    private Integer commentCo;
    private Integer diffusionCo;
    private Integer hitCo;
    private Integer time;
    private String from;
    private Integer lastCommentTi;
    private String nickname;
    private Integer sex;
    private String birthday;
    private String enteryear;
    private Integer type;
    private String online;
    private Integer active;
    private String iconUrl;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getSayType() {
        return sayType;
    }

    public void setSayType(String sayType) {
        this.sayType = sayType;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Integer getHitCo() {
        return hitCo;
    }

    public void setHitCo(Integer hitCo) {
        this.hitCo = hitCo;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Integer getLastCommentTi() {
        return lastCommentTi;
    }

    public void setLastCommentTi(Integer lastCommentTi) {
        this.lastCommentTi = lastCommentTi;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getEnteryear() {
        return enteryear;
    }

    public void setEnteryear(String enteryear) {
        this.enteryear = enteryear;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VLoginRecordEntity that = (VLoginRecordEntity) o;

        if (sid != that.sid) return false;
        if (active != null ? !active.equals(that.active) : that.active != null) return false;
        if (birthday != null ? !birthday.equals(that.birthday) : that.birthday != null) return false;
        if (commentCo != null ? !commentCo.equals(that.commentCo) : that.commentCo != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (diffusionCo != null ? !diffusionCo.equals(that.diffusionCo) : that.diffusionCo != null) return false;
        if (enteryear != null ? !enteryear.equals(that.enteryear) : that.enteryear != null) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (hitCo != null ? !hitCo.equals(that.hitCo) : that.hitCo != null) return false;
        if (iconUrl != null ? !iconUrl.equals(that.iconUrl) : that.iconUrl != null) return false;
        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        if (lastCommentTi != null ? !lastCommentTi.equals(that.lastCommentTi) : that.lastCommentTi != null)
            return false;
        if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null) return false;
        if (online != null ? !online.equals(that.online) : that.online != null) return false;
        if (sayType != null ? !sayType.equals(that.sayType) : that.sayType != null) return false;
        if (sex != null ? !sex.equals(that.sex) : that.sex != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sid;
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (sayType != null ? sayType.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (commentCo != null ? commentCo.hashCode() : 0);
        result = 31 * result + (diffusionCo != null ? diffusionCo.hashCode() : 0);
        result = 31 * result + (hitCo != null ? hitCo.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (lastCommentTi != null ? lastCommentTi.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (enteryear != null ? enteryear.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (online != null ? online.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (iconUrl != null ? iconUrl.hashCode() : 0);
        return result;
    }
}
