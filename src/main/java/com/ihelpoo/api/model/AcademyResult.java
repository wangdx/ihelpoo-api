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
public class AcademyResult {
    @XmlElement
    public Result result;
    @XmlElement
    public Academys academys;


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Academys{
        public List<Academy> academy;

        public Academys(List<Academy> academy) {
            this.academy = academy;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Academy{
        @XmlElement
        public Integer id;
        @XmlElement
        public String academyName;
        @XmlElement
        public Integer schoolId;
    }
}
