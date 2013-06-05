package com.ihelpoo.api.model.entity;


public class IUserInfoEntity {
    private int uid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    private String introduction;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    private Integer introductionRe;

    public Integer getIntroductionRe() {
        return introductionRe;
    }

    public void setIntroductionRe(Integer introductionRe) {
        this.introductionRe = introductionRe;
    }

    private Integer academyOp;

    public Integer getAcademyOp() {
        return academyOp;
    }

    public void setAcademyOp(Integer academyOp) {
        this.academyOp = academyOp;
    }

    private Integer specialtyOp;

    public Integer getSpecialtyOp() {
        return specialtyOp;
    }

    public void setSpecialtyOp(Integer specialtyOp) {
        this.specialtyOp = specialtyOp;
    }

    private Integer dormitoryOp;

    public Integer getDormitoryOp() {
        return dormitoryOp;
    }

    public void setDormitoryOp(Integer dormitoryOp) {
        this.dormitoryOp = dormitoryOp;
    }

    private Integer provinceOp;

    public Integer getProvinceOp() {
        return provinceOp;
    }

    public void setProvinceOp(Integer provinceOp) {
        this.provinceOp = provinceOp;
    }

    private Integer cityOp;

    public Integer getCityOp() {
        return cityOp;
    }

    public void setCityOp(Integer cityOp) {
        this.cityOp = cityOp;
    }

    private String realname;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    private Integer realnameRe;

    public Integer getRealnameRe() {
        return realnameRe;
    }

    public void setRealnameRe(Integer realnameRe) {
        this.realnameRe = realnameRe;
    }

    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String qq;

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    private String weibo;

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    private String room;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    private Integer dynamic;

    public Integer getDynamic() {
        return dynamic;
    }

    public void setDynamic(Integer dynamic) {
        this.dynamic = dynamic;
    }

    private Integer fans;

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

    private Integer follow;

    public Integer getFollow() {
        return follow;
    }

    public void setFollow(Integer follow) {
        this.follow = follow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IUserInfoEntity that = (IUserInfoEntity) o;

        if (uid != that.uid) return false;
        if (academyOp != null ? !academyOp.equals(that.academyOp) : that.academyOp != null) return false;
        if (cityOp != null ? !cityOp.equals(that.cityOp) : that.cityOp != null) return false;
        if (dormitoryOp != null ? !dormitoryOp.equals(that.dormitoryOp) : that.dormitoryOp != null) return false;
        if (dynamic != null ? !dynamic.equals(that.dynamic) : that.dynamic != null) return false;
        if (fans != null ? !fans.equals(that.fans) : that.fans != null) return false;
        if (follow != null ? !follow.equals(that.follow) : that.follow != null) return false;
        if (introduction != null ? !introduction.equals(that.introduction) : that.introduction != null) return false;
        if (introductionRe != null ? !introductionRe.equals(that.introductionRe) : that.introductionRe != null)
            return false;
        if (mobile != null ? !mobile.equals(that.mobile) : that.mobile != null) return false;
        if (provinceOp != null ? !provinceOp.equals(that.provinceOp) : that.provinceOp != null) return false;
        if (qq != null ? !qq.equals(that.qq) : that.qq != null) return false;
        if (realname != null ? !realname.equals(that.realname) : that.realname != null) return false;
        if (realnameRe != null ? !realnameRe.equals(that.realnameRe) : that.realnameRe != null) return false;
        if (room != null ? !room.equals(that.room) : that.room != null) return false;
        if (specialtyOp != null ? !specialtyOp.equals(that.specialtyOp) : that.specialtyOp != null) return false;
        if (weibo != null ? !weibo.equals(that.weibo) : that.weibo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uid;
        result = 31 * result + (introduction != null ? introduction.hashCode() : 0);
        result = 31 * result + (introductionRe != null ? introductionRe.hashCode() : 0);
        result = 31 * result + (academyOp != null ? academyOp.hashCode() : 0);
        result = 31 * result + (specialtyOp != null ? specialtyOp.hashCode() : 0);
        result = 31 * result + (dormitoryOp != null ? dormitoryOp.hashCode() : 0);
        result = 31 * result + (provinceOp != null ? provinceOp.hashCode() : 0);
        result = 31 * result + (cityOp != null ? cityOp.hashCode() : 0);
        result = 31 * result + (realname != null ? realname.hashCode() : 0);
        result = 31 * result + (realnameRe != null ? realnameRe.hashCode() : 0);
        result = 31 * result + (mobile != null ? mobile.hashCode() : 0);
        result = 31 * result + (qq != null ? qq.hashCode() : 0);
        result = 31 * result + (weibo != null ? weibo.hashCode() : 0);
        result = 31 * result + (room != null ? room.hashCode() : 0);
        result = 31 * result + (dynamic != null ? dynamic.hashCode() : 0);
        result = 31 * result + (fans != null ? fans.hashCode() : 0);
        result = 31 * result + (follow != null ? follow.hashCode() : 0);
        return result;
    }
}
