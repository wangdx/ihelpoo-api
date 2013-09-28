package com.ihelpoo.api.model.entity;

/**
 * @author: echowdx@gmail.com
 */
public class VUserDetailEntity {
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
    private Integer creatTi;
    private Integer loginDaysCo;
    private String online;
    private String coins;
    private Integer active;
    private String iconFl;
    private String iconUrl;
    private String skin;
    private String introduction;
    private Integer introductionRe;
    private String realname;
    private String mobile;
    private String qq;
    private String weibo;
    private Integer fans;
    private Integer follow;
    private String school;
    private String domain;
    private String academyName;
    private String majorName;

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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getIntroductionRe() {
        return introductionRe;
    }

    public void setIntroductionRe(Integer introductionRe) {
        this.introductionRe = introductionRe;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

    public Integer getFollow() {
        return follow;
    }

    public void setFollow(Integer follow) {
        this.follow = follow;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAcademyName() {
        return academyName;
    }

    public void setAcademyName(String academyName) {
        this.academyName = academyName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VUserDetailEntity that = (VUserDetailEntity) o;

        if (uid != that.uid) return false;
        if (academyName != null ? !academyName.equals(that.academyName) : that.academyName != null) return false;
        if (active != null ? !active.equals(that.active) : that.active != null) return false;
        if (birthday != null ? !birthday.equals(that.birthday) : that.birthday != null) return false;
        if (coins != null ? !coins.equals(that.coins) : that.coins != null) return false;
        if (creatTi != null ? !creatTi.equals(that.creatTi) : that.creatTi != null) return false;
        if (domain != null ? !domain.equals(that.domain) : that.domain != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (enteryear != null ? !enteryear.equals(that.enteryear) : that.enteryear != null) return false;
        if (fans != null ? !fans.equals(that.fans) : that.fans != null) return false;
        if (follow != null ? !follow.equals(that.follow) : that.follow != null) return false;
        if (iconFl != null ? !iconFl.equals(that.iconFl) : that.iconFl != null) return false;
        if (iconUrl != null ? !iconUrl.equals(that.iconUrl) : that.iconUrl != null) return false;
        if (introduction != null ? !introduction.equals(that.introduction) : that.introduction != null) return false;
        if (introductionRe != null ? !introductionRe.equals(that.introductionRe) : that.introductionRe != null)
            return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (loginDaysCo != null ? !loginDaysCo.equals(that.loginDaysCo) : that.loginDaysCo != null) return false;
        if (logintime != null ? !logintime.equals(that.logintime) : that.logintime != null) return false;
        if (majorName != null ? !majorName.equals(that.majorName) : that.majorName != null) return false;
        if (mobile != null ? !mobile.equals(that.mobile) : that.mobile != null) return false;
        if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null) return false;
        if (online != null ? !online.equals(that.online) : that.online != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (priority != null ? !priority.equals(that.priority) : that.priority != null) return false;
        if (qq != null ? !qq.equals(that.qq) : that.qq != null) return false;
        if (realname != null ? !realname.equals(that.realname) : that.realname != null) return false;
        if (school != null ? !school.equals(that.school) : that.school != null) return false;
        if (sex != null ? !sex.equals(that.sex) : that.sex != null) return false;
        if (skin != null ? !skin.equals(that.skin) : that.skin != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (weibo != null ? !weibo.equals(that.weibo) : that.weibo != null) return false;

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
        result = 31 * result + (creatTi != null ? creatTi.hashCode() : 0);
        result = 31 * result + (loginDaysCo != null ? loginDaysCo.hashCode() : 0);
        result = 31 * result + (online != null ? online.hashCode() : 0);
        result = 31 * result + (coins != null ? coins.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (iconFl != null ? iconFl.hashCode() : 0);
        result = 31 * result + (iconUrl != null ? iconUrl.hashCode() : 0);
        result = 31 * result + (skin != null ? skin.hashCode() : 0);
        result = 31 * result + (introduction != null ? introduction.hashCode() : 0);
        result = 31 * result + (introductionRe != null ? introductionRe.hashCode() : 0);
        result = 31 * result + (realname != null ? realname.hashCode() : 0);
        result = 31 * result + (mobile != null ? mobile.hashCode() : 0);
        result = 31 * result + (qq != null ? qq.hashCode() : 0);
        result = 31 * result + (weibo != null ? weibo.hashCode() : 0);
        result = 31 * result + (fans != null ? fans.hashCode() : 0);
        result = 31 * result + (follow != null ? follow.hashCode() : 0);
        result = 31 * result + (school != null ? school.hashCode() : 0);
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        result = 31 * result + (academyName != null ? academyName.hashCode() : 0);
        result = 31 * result + (majorName != null ? majorName.hashCode() : 0);
        return result;
    }
}
