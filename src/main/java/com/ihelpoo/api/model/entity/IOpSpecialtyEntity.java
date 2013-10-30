package com.ihelpoo.api.model.entity;

/**
 * @author: echowdx@gmail.com
 */
public class IOpSpecialtyEntity {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Integer academy;

    public Integer getAcademy() {
        return academy;
    }

    public void setAcademy(Integer academy) {
        this.academy = academy;
    }

    private Integer school;

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

        IOpSpecialtyEntity that = (IOpSpecialtyEntity) o;

        if (id != that.id) return false;
        if (academy != null ? !academy.equals(that.academy) : that.academy != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (school != null ? !school.equals(that.school) : that.school != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (academy != null ? academy.hashCode() : 0);
        result = 31 * result + (school != null ? school.hashCode() : 0);
        return result;
    }
}
