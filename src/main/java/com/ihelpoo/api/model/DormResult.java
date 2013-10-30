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
public class DormResult {
    @XmlElement
    public Result result;
    @XmlElement
    public Dorms dorms;


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Dorms{
        public List<Dorm> dorm;

        public Dorms(List<Dorm> dorm) {
            this.dorm = dorm;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Dorm{
        @XmlElement
        public Integer id;
        @XmlElement
        public String dormName;
        @XmlElement
        public Integer dormType;
        @XmlElement
        public Integer schoolId;
    }
}
