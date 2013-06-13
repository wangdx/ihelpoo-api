package com.ihelpoo.api.model.entity;

/**
 * @author: dongxu.wang@acm.org
 */
public class IUserStatusEntity {
    private int uid;
    private int acquireSeconds;
    private int acquireTimes;
    private Integer lastActiveTi;
    private Integer totalActiveTi;
    private String activeFlag;
    private Integer activeSLimit;
    private Integer activeCLimit;
    private String dynamicFlag;
    private int recordLimit;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getAcquireSeconds() {
        return acquireSeconds;
    }

    public void setAcquireSeconds(int acquireSeconds) {
        this.acquireSeconds = acquireSeconds;
    }

    public int getAcquireTimes() {
        return acquireTimes;
    }

    public void setAcquireTimes(int acquireTimes) {
        this.acquireTimes = acquireTimes;
    }

    public Integer getLastActiveTi() {
        return lastActiveTi;
    }

    public void setLastActiveTi(Integer lastActiveTi) {
        this.lastActiveTi = lastActiveTi;
    }

    public Integer getTotalActiveTi() {
        return totalActiveTi;
    }

    public void setTotalActiveTi(Integer totalActiveTi) {
        this.totalActiveTi = totalActiveTi;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Integer getActiveSLimit() {
        return activeSLimit;
    }

    public void setActiveSLimit(Integer activeSLimit) {
        this.activeSLimit = activeSLimit;
    }

    public Integer getActiveCLimit() {
        return activeCLimit;
    }

    public void setActiveCLimit(Integer activeCLimit) {
        this.activeCLimit = activeCLimit;
    }

    public String getDynamicFlag() {
        return dynamicFlag;
    }

    public void setDynamicFlag(String dynamicFlag) {
        this.dynamicFlag = dynamicFlag;
    }

    public int getRecordLimit() {
        return recordLimit;
    }

    public void setRecordLimit(int recordLimit) {
        this.recordLimit = recordLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IUserStatusEntity that = (IUserStatusEntity) o;

        if (acquireSeconds != that.acquireSeconds) return false;
        if (acquireTimes != that.acquireTimes) return false;
        if (recordLimit != that.recordLimit) return false;
        if (uid != that.uid) return false;
        if (activeCLimit != null ? !activeCLimit.equals(that.activeCLimit) : that.activeCLimit != null) return false;
        if (activeFlag != null ? !activeFlag.equals(that.activeFlag) : that.activeFlag != null) return false;
        if (activeSLimit != null ? !activeSLimit.equals(that.activeSLimit) : that.activeSLimit != null) return false;
        if (dynamicFlag != null ? !dynamicFlag.equals(that.dynamicFlag) : that.dynamicFlag != null) return false;
        if (lastActiveTi != null ? !lastActiveTi.equals(that.lastActiveTi) : that.lastActiveTi != null) return false;
        if (totalActiveTi != null ? !totalActiveTi.equals(that.totalActiveTi) : that.totalActiveTi != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uid;
        result = 31 * result + acquireSeconds;
        result = 31 * result + acquireTimes;
        result = 31 * result + (lastActiveTi != null ? lastActiveTi.hashCode() : 0);
        result = 31 * result + (totalActiveTi != null ? totalActiveTi.hashCode() : 0);
        result = 31 * result + (activeFlag != null ? activeFlag.hashCode() : 0);
        result = 31 * result + (activeSLimit != null ? activeSLimit.hashCode() : 0);
        result = 31 * result + (activeCLimit != null ? activeCLimit.hashCode() : 0);
        result = 31 * result + (dynamicFlag != null ? dynamicFlag.hashCode() : 0);
        result = 31 * result + recordLimit;
        return result;
    }
}
