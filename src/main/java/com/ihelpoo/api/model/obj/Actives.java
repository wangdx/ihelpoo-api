package com.ihelpoo.api.model.obj;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
* @author: dongxu.wang@acm.org
*/
@XmlAccessorType(XmlAccessType.FIELD)
public class Actives {
    @XmlElement(name="active")
    List<Active> active;

    public List<Active> getActive() {
        return active;
    }

    public void setActive(List<Active> active) {
        this.active = active;
    }
}
