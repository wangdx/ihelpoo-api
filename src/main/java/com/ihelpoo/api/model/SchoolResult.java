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
public class SchoolResult {
    @XmlElement
    public Result result;
    @XmlElement
    public Schools schools;


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Schools{
        public List<School> school;

        public Schools(List<School> school) {
            this.school = school;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class School{
        @XmlElement
        public Integer id;
        @XmlElement
        public String schoolName;
        @XmlElement
        public String initial;
        @XmlElement
        public Integer city_op;
        @XmlElement
        public String domain;
        @XmlElement
        public String domain_main;
        @XmlElement
        public Integer time;
        @XmlElement
        public Integer status;
    }
}
