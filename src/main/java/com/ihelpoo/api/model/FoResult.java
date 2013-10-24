package com.ihelpoo.api.model;

import com.ihelpoo.api.model.obj.Notice;
import com.ihelpoo.common.Constant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author: echowdx@gmail.com
 */
@XmlRootElement(name = Constant.IHELPOO_XML_ROOT)
@XmlAccessorType(XmlAccessType.FIELD)
public class FoResult {

    @XmlElement
    public Fos friends;

    @XmlElement
    public Notice notice;

    public static class Fos{
        @XmlElement
        public List<Fo> friend;
    }

    public static class Fo{
        @XmlElement
        public String name;
        @XmlElement
        public Integer userid;
        @XmlElement
        public String portrait;
        @XmlElement
        public String expertise;
        @XmlElement
        public String gender;
    }
}
