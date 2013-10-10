package com.ihelpoo.api.model;

import com.ihelpoo.common.Constant;
import com.ihelpoo.api.model.base.Actives;
import com.ihelpoo.api.model.base.Notice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author: dongxu.wang@acm.org
 */

@XmlRootElement(name = Constant.IHELPOO_XML_ROOT)
@XmlAccessorType(XmlAccessType.FIELD)
public class UserWordResult {
    @XmlElement
    public int pagesize;
    @XmlElement
    public User user;
    @XmlElement
    public Actives actives;
    @XmlElement
    public Notice notice;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class User {
        @XmlElement
        public String name;
        @XmlElement
        public int uid;
        @XmlElement
        public String portrait;
        @XmlElement
        public String jointime;
        @XmlElement
        public String gender;
        @XmlElement
        public String from;
        @XmlElement
        public String devplatform;
        @XmlElement
        public String expertise;
        @XmlElement
        public String relation;
        @XmlElement
        public String latestonline;
    }

}
