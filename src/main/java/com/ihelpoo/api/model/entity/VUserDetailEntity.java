package com.ihelpoo.api.model.entity;

/**
 * @author: echowdx@gmail.com
 */
public class VUserDetailEntity {
    private int uid;
    private String nickname;
    private Integer gender;
    private String email;
    private Integer emailVerified;
    private String birthday;
    private String enrolTime;
    private Integer userType;
    private String ipAddr;
    private Integer loginTime;
    private Integer lastLogin;
    private Integer createTime;
    private Integer loginDays;
    private String onlineStatus;
    private Integer activeCredits;
    private String avatarType;
    private String avatarUrl;
    private String webTheme;
    private String selfIntro;
    private String realName;
    private Integer followersCount;
    private Integer friendsCount;
    private String schoolName;
    private int schoolId;
    private String schoolDomain;
    private String academyName;
    private String majorName;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Integer emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEnrolTime() {
        return enrolTime;
    }

    public void setEnrolTime(String enrolTime) {
        this.enrolTime = enrolTime;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public Integer getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Integer loginTime) {
        this.loginTime = loginTime;
    }

    public Integer getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Integer lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getLoginDays() {
        return loginDays;
    }

    public void setLoginDays(Integer loginDays) {
        this.loginDays = loginDays;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Integer getActiveCredits() {
        return activeCredits;
    }

    public void setActiveCredits(Integer activeCredits) {
        this.activeCredits = activeCredits;
    }

    public String getAvatarType() {
        return avatarType;
    }

    public void setAvatarType(String avatarType) {
        this.avatarType = avatarType;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getWebTheme() {
        return webTheme;
    }

    public void setWebTheme(String webTheme) {
        this.webTheme = webTheme;
    }

    public String getSelfIntro() {
        return selfIntro;
    }

    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(Integer friendsCount) {
        this.friendsCount = friendsCount;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolDomain() {
        return schoolDomain;
    }

    public void setSchoolDomain(String schoolDomain) {
        this.schoolDomain = schoolDomain;
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

        if (schoolId != that.schoolId) return false;
        if (uid != that.uid) return false;
        if (academyName != null ? !academyName.equals(that.academyName) : that.academyName != null) return false;
        if (activeCredits != null ? !activeCredits.equals(that.activeCredits) : that.activeCredits != null)
            return false;
        if (avatarType != null ? !avatarType.equals(that.avatarType) : that.avatarType != null) return false;
        if (avatarUrl != null ? !avatarUrl.equals(that.avatarUrl) : that.avatarUrl != null) return false;
        if (birthday != null ? !birthday.equals(that.birthday) : that.birthday != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (emailVerified != null ? !emailVerified.equals(that.emailVerified) : that.emailVerified != null)
            return false;
        if (enrolTime != null ? !enrolTime.equals(that.enrolTime) : that.enrolTime != null) return false;
        if (followersCount != null ? !followersCount.equals(that.followersCount) : that.followersCount != null)
            return false;
        if (friendsCount != null ? !friendsCount.equals(that.friendsCount) : that.friendsCount != null) return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (ipAddr != null ? !ipAddr.equals(that.ipAddr) : that.ipAddr != null) return false;
        if (lastLogin != null ? !lastLogin.equals(that.lastLogin) : that.lastLogin != null) return false;
        if (loginDays != null ? !loginDays.equals(that.loginDays) : that.loginDays != null) return false;
        if (loginTime != null ? !loginTime.equals(that.loginTime) : that.loginTime != null) return false;
        if (majorName != null ? !majorName.equals(that.majorName) : that.majorName != null) return false;
        if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null) return false;
        if (onlineStatus != null ? !onlineStatus.equals(that.onlineStatus) : that.onlineStatus != null) return false;
        if (realName != null ? !realName.equals(that.realName) : that.realName != null) return false;
        if (schoolDomain != null ? !schoolDomain.equals(that.schoolDomain) : that.schoolDomain != null) return false;
        if (schoolName != null ? !schoolName.equals(that.schoolName) : that.schoolName != null) return false;
        if (selfIntro != null ? !selfIntro.equals(that.selfIntro) : that.selfIntro != null) return false;
        if (userType != null ? !userType.equals(that.userType) : that.userType != null) return false;
        if (webTheme != null ? !webTheme.equals(that.webTheme) : that.webTheme != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uid;
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (emailVerified != null ? emailVerified.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (enrolTime != null ? enrolTime.hashCode() : 0);
        result = 31 * result + (userType != null ? userType.hashCode() : 0);
        result = 31 * result + (ipAddr != null ? ipAddr.hashCode() : 0);
        result = 31 * result + (loginTime != null ? loginTime.hashCode() : 0);
        result = 31 * result + (lastLogin != null ? lastLogin.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (loginDays != null ? loginDays.hashCode() : 0);
        result = 31 * result + (onlineStatus != null ? onlineStatus.hashCode() : 0);
        result = 31 * result + (activeCredits != null ? activeCredits.hashCode() : 0);
        result = 31 * result + (avatarType != null ? avatarType.hashCode() : 0);
        result = 31 * result + (avatarUrl != null ? avatarUrl.hashCode() : 0);
        result = 31 * result + (webTheme != null ? webTheme.hashCode() : 0);
        result = 31 * result + (selfIntro != null ? selfIntro.hashCode() : 0);
        result = 31 * result + (realName != null ? realName.hashCode() : 0);
        result = 31 * result + (followersCount != null ? followersCount.hashCode() : 0);
        result = 31 * result + (friendsCount != null ? friendsCount.hashCode() : 0);
        result = 31 * result + (schoolName != null ? schoolName.hashCode() : 0);
        result = 31 * result + schoolId;
        result = 31 * result + (schoolDomain != null ? schoolDomain.hashCode() : 0);
        result = 31 * result + (academyName != null ? academyName.hashCode() : 0);
        result = 31 * result + (majorName != null ? majorName.hashCode() : 0);
        return result;
    }
}
