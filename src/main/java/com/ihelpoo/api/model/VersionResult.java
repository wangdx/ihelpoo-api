package com.ihelpoo.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author: dongxu.wang@acm.org
 */

@XmlRootElement(name = "ihelpoo")
@XmlAccessorType(XmlAccessType.FIELD)
public class VersionResult {

    @XmlElement
    Update update;

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public static class Update {

        @XmlElement
        public String wp7;
        @XmlElement
        public String ios;
        @XmlElement
        public Android android;

        private Update(Builder builder){
            this.wp7 = builder.wp7;
            this.ios = builder.ios;
            this.android = builder.android;
        }

        public static class Builder{
            String wp7;
            String ios;
            Android android;

            public Builder wp7(String wp7) {
                this.wp7 = wp7;
                return this;
            }

            public Builder ios(String ios) {
                this.ios = ios;
                return this;
            }

            public Builder android(Android android) {
                this.android = android;
                return this;
            }
            public Update build(){
                return new Update(this);
            }
        }
    }

    public static class Android {
        @XmlElement
        public String versionCode;
        @XmlElement
        public String versionName;
        @XmlElement
        public String downloadUrl;
        @XmlElement
        public String updateLog;

        private Android(Builder builder) {
            this.versionCode = builder.versionCode;
            this.versionName = builder.versionName;
            this.downloadUrl = builder.downloadUrl;
            this.updateLog = builder.updateLog;
        }

        public static class Builder{
            String versionCode;
            String versionName;
            String downloadUrl;
            String updateLog;

            public Builder versionCode(String versionCode) {
                this.versionCode = versionCode;
                return this;
            }

            public Builder versionName(String versionName) {
                this.versionName = versionName;
                return this;
            }

            public Builder downloadUrl(String downloadUrl) {
                this.downloadUrl = downloadUrl;
                return this;
            }

            public Builder changeLog(String updateLog) {
                this.updateLog = updateLog;
                return this;
            }

            public Android build(){
                return new Android(this);
            }
        }
    }
}
