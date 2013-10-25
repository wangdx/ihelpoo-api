package com.ihelpoo.api.model;

import com.ihelpoo.api.model.common.User;
import com.ihelpoo.api.model.obj.Notice;
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
public class FriendsResult {
    @XmlElement
    public Result result;

    @XmlElement
    public Friends friends;

    @XmlElement
    public Notice notice;

    public static class Friends {
        @XmlElement
        public List<User> friend;
    }
}
