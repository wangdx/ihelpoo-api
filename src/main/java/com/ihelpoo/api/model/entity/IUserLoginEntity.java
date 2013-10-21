package com.ihelpoo.api.model.entity;

/**
 * @author: echowdx@gmail.com
 */
public class IUserLoginEntity {
    private int uid;
    private Integer status;
    private String email;
    private String password;
    private String nickname;
    private Integer sex;
    private String birthday;
    private String enteryear;
    private Integer type;
    private Integer priority;
    private String ip;
    private Integer logintime;
    private Integer lastlogintime;
    private Integer creatTi;
    private Integer loginDaysCo;
    private String online;
    private String coins;
    private Integer active;
    private String iconFl;
    private String iconUrl;
    private String skin;
    private Integer school;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getLogintime() {
        return logintime;
    }

    public void setLogintime(Integer logintime) {
        this.logintime = logintime;
    }

    public Integer getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(Integer lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public Integer getCreatTi() {
        return creatTi;
    }

    public void setCreatTi(Integer creatTi) {
        this.creatTi = creatTi;
    }

    public Integer getLoginDaysCo() {
        return loginDaysCo;
    }

    public void setLoginDaysCo(Integer loginDaysCo) {
        this.loginDaysCo = loginDaysCo;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getIconFl() {
        return iconFl;
    }

    public void setIconFl(String iconFl) {
        this.iconFl = iconFl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public Integer getSchool() {
        return school;
    }

    public void setSchool(Integer school) {
        this.school = school;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IUserLoginEntity that = (IUserLoginEntity) o;

        if (uid != that.uid) return false;
        if (active != null ? !active.equals(that.active) : that.active != null) return false;
        if (birthday != null ? !birthday.equals(that.birthday) : that.birthday != null) return false;
        if (coins != null ? !coins.equals(that.coins) : that.coins != null) return false;
        if (creatTi != null ? !creatTi.equals(that.creatTi) : that.creatTi != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (enteryear != null ? !enteryear.equals(that.enteryear) : that.enteryear != null) return false;
        if (iconFl != null ? !iconFl.equals(that.iconFl) : that.iconFl != null) return false;
        if (iconUrl != null ? !iconUrl.equals(that.iconUrl) : that.iconUrl != null) return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (lastlogintime != null ? !lastlogintime.equals(that.lastlogintime) : that.lastlogintime != null)
            return false;
        if (loginDaysCo != null ? !loginDaysCo.equals(that.loginDaysCo) : that.loginDaysCo != null) return false;
        if (logintime != null ? !logintime.equals(that.logintime) : that.logintime != null) return false;
        if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null) return false;
        if (online != null ? !online.equals(that.online) : that.online != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (priority != null ? !priority.equals(that.priority) : that.priority != null) return false;
        if (school != null ? !school.equals(that.school) : that.school != null) return false;
        if (sex != null ? !sex.equals(that.sex) : that.sex != null) return false;
        if (skin != null ? !skin.equals(that.skin) : that.skin != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uid;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (enteryear != null ? enteryear.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (logintime != null ? logintime.hashCode() : 0);
        result = 31 * result + (lastlogintime != null ? lastlogintime.hashCode() : 0);
        result = 31 * result + (creatTi != null ? creatTi.hashCode() : 0);
        result = 31 * result + (loginDaysCo != null ? loginDaysCo.hashCode() : 0);
        result = 31 * result + (online != null ? online.hashCode() : 0);
        result = 31 * result + (coins != null ? coins.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (iconFl != null ? iconFl.hashCode() : 0);
        result = 31 * result + (iconUrl != null ? iconUrl.hashCode() : 0);
        result = 31 * result + (skin != null ? skin.hashCode() : 0);
        result = 31 * result + (school != null ? school.hashCode() : 0);
        return result;
    }
}
