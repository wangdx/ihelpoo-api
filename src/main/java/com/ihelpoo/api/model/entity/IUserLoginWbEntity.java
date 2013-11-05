package com.ihelpoo.api.model.entity;

/**
 * @author: echowdx@gmail.com
 */
public class IUserLoginWbEntity {
    private int uid;
    private String thirdUid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getThirdUid() {
        return thirdUid;
    }

    public void setThirdUid(String thirdUid) {
        this.thirdUid = thirdUid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IUserLoginWbEntity that = (IUserLoginWbEntity) o;

        if (uid != that.uid) return false;
        if (thirdUid != null ? !thirdUid.equals(that.thirdUid) : that.thirdUid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uid;
        result = 31 * result + (thirdUid != null ? thirdUid.hashCode() : 0);
        return result;
    }
}
