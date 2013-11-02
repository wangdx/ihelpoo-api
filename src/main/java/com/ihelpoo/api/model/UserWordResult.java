package com.ihelpoo.api.model;

import com.ihelpoo.api.model.common.User;
import com.ihelpoo.api.model.obj.Result;
import com.ihelpoo.common.Constant;
import com.ihelpoo.api.model.obj.Actives;
import com.ihelpoo.api.model.obj.Notice;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author: dongxu.wang@acm.org
 */

@XmlRootElement(name = Constant.IHELPOO_XML_ROOT)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UserWordResult {
    @XmlElement
    public Result result;
    @XmlElement
    public int page_size;
    @XmlElement
    public User user;
    @XmlElement
    public Actives actives;
    @XmlElement
    public Notice notice;

}
