package com.ihelpoo.api.model.entity;

/**
 * @author: echowdx@gmail.com
 */
public class IRecordHelpEntity {
    private int hid;
    private Integer sid;
    private Integer rewardCoins;
    private String status;
    private Integer winUid;
    private String thanks;
    private Integer thanksTi;

    public int getHid() {
        return hid;
    }

    public void setHid(int hid) {
        this.hid = hid;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getRewardCoins() {
        return rewardCoins;
    }

    public void setRewardCoins(Integer rewardCoins) {
        this.rewardCoins = rewardCoins;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getWinUid() {
        return winUid;
    }

    public void setWinUid(Integer winUid) {
        this.winUid = winUid;
    }

    public String getThanks() {
        return thanks;
    }

    public void setThanks(String thanks) {
        this.thanks = thanks;
    }

    public Integer getThanksTi() {
        return thanksTi;
    }

    public void setThanksTi(Integer thanksTi) {
        this.thanksTi = thanksTi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IRecordHelpEntity that = (IRecordHelpEntity) o;

        if (hid != that.hid) return false;
        if (rewardCoins != null ? !rewardCoins.equals(that.rewardCoins) : that.rewardCoins != null) return false;
        if (sid != null ? !sid.equals(that.sid) : that.sid != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (thanks != null ? !thanks.equals(that.thanks) : that.thanks != null) return false;
        if (thanksTi != null ? !thanksTi.equals(that.thanksTi) : that.thanksTi != null) return false;
        if (winUid != null ? !winUid.equals(that.winUid) : that.winUid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hid;
        result = 31 * result + (sid != null ? sid.hashCode() : 0);
        result = 31 * result + (rewardCoins != null ? rewardCoins.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (winUid != null ? winUid.hashCode() : 0);
        result = 31 * result + (thanks != null ? thanks.hashCode() : 0);
        result = 31 * result + (thanksTi != null ? thanksTi.hashCode() : 0);
        return result;
    }
}
