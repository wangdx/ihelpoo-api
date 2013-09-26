package com.ihelpoo.api.model;

import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.common.Constant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author: echowdx@gmail.com
 */

@XmlRootElement(name = Constant.IHELPOO_XML_ROOT)
@XmlAccessorType(XmlAccessType.FIELD)
public class UserResult {

    @XmlElement
    public  User user;
    @XmlElement
    public Notice notice;

    public static class User{
        @XmlElement
        public  String name;
        @XmlElement
        public  String portrait;
        @XmlElement
        public  String jointime;
        @XmlElement
        public  String gender;
        @XmlElement
        public  String from;
        @XmlElement
        public  String devplatform;
        @XmlElement
        public  String expertise;
        @XmlElement
        public  Integer favoritecount;
        @XmlElement
        public  Integer fanscount;
        @XmlElement
        public  Integer followerscount;
    }
}
