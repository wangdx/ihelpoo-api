package com.ihelpoo.api.model.entity;

/**
 * @author: dongxu.wang@acm.org
 */
public class IMsgNoticeEntity {
    private long noticeId;
    private String noticeType;
    private int sourceId;
    private int detailId;
    private String formatId;
    private int createTime;

    public long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(long noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public String getFormatId() {
        return formatId;
    }

    public void setFormatId(String formatId) {
        this.formatId = formatId;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IMsgNoticeEntity that = (IMsgNoticeEntity) o;

        if (createTime != that.createTime) return false;
        if (detailId != that.detailId) return false;
        if (noticeId != that.noticeId) return false;
        if (sourceId != that.sourceId) return false;
        if (formatId != null ? !formatId.equals(that.formatId) : that.formatId != null) return false;
        if (noticeType != null ? !noticeType.equals(that.noticeType) : that.noticeType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (noticeId ^ (noticeId >>> 32));
        result = 31 * result + (noticeType != null ? noticeType.hashCode() : 0);
        result = 31 * result + sourceId;
        result = 31 * result + detailId;
        result = 31 * result + (formatId != null ? formatId.hashCode() : 0);
        result = 31 * result + createTime;
        return result;
    }
}
