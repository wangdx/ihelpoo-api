package com.ihelpoo.api.model.entity;

/**
 * @author: echowdx@gmail.com
 */
public class ISchoolInfoEntity {
    private int id;
    private String school;
    private String initial;
    private int cityOp;
    private String domain;
    private String domainMain;
    private String remark;
    private int time;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public int getCityOp() {
        return cityOp;
    }

    public void setCityOp(int cityOp) {
        this.cityOp = cityOp;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomainMain() {
        return domainMain;
    }

    public void setDomainMain(String domainMain) {
        this.domainMain = domainMain;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ISchoolInfoEntity that = (ISchoolInfoEntity) o;

        if (cityOp != that.cityOp) return false;
        if (id != that.id) return false;
        if (status != that.status) return false;
        if (time != that.time) return false;
        if (domain != null ? !domain.equals(that.domain) : that.domain != null) return false;
        if (domainMain != null ? !domainMain.equals(that.domainMain) : that.domainMain != null) return false;
        if (initial != null ? !initial.equals(that.initial) : that.initial != null) return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;
        if (school != null ? !school.equals(that.school) : that.school != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (school != null ? school.hashCode() : 0);
        result = 31 * result + (initial != null ? initial.hashCode() : 0);
        result = 31 * result + cityOp;
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        result = 31 * result + (domainMain != null ? domainMain.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + time;
        result = 31 * result + status;
        return result;
    }
}
