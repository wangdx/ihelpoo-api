package com.ihelpoo.api.model;

import com.ihelpoo.api.model.obj.Result;
import com.ihelpoo.common.Constant;
import org.codehaus.jackson.map.annotate.JsonSerialize;

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
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class MajorResult {
    @XmlElement
    public Result result;
    @XmlElement
    public Majors majors;


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Majors{
        public List<Major> major;

        public Majors(List<Major> major) {
            this.major = major;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Major{
        @XmlElement
        public Integer id;
        @XmlElement
        public String majorName;
        @XmlElement
        public Integer academyId;
        @XmlElement
        public Integer schoolId;
    }
}
