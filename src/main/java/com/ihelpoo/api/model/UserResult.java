package com.ihelpoo.api.model;

import com.ihelpoo.api.model.common.User;
import com.ihelpoo.api.model.obj.Notice;
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
    public User user;
    @XmlElement
    public Notice notice;
}
