package com.ihelpoo.api.model.base;

import com.ihelpoo.api.model.base.Base;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author: dongxu.wang@acm.org
 */
public class Entity extends Base {

    @XmlElement
    protected int id;

    public int getId() {
        return id;
    }
}
