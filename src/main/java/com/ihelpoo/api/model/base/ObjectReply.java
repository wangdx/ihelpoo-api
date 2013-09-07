package com.ihelpoo.api.model.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
* @author: dongxu.wang@acm.org
*/
@XmlRootElement(name = "objectreply")
@XmlAccessorType(XmlAccessType.FIELD)
public class ObjectReply implements Serializable {
    @XmlElement(name = "objectname")
    public String objectname;
    @XmlElement(name = "objectbody")
    public String objectbody;

    public ObjectReply(String name, String body){
        this.objectname = name;
        this.objectbody = body;
    }

    public ObjectReply(){}
}
