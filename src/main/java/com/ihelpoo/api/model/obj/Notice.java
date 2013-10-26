package com.ihelpoo.api.model.obj;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author: dongxu.wang@acm.org
 */

@XmlAccessorType(XmlAccessType.FIELD)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Notice {
    @XmlElement
    public int systemCount;
    @XmlElement
    public int atmeCount;
    @XmlElement
    public int commentCount;
    @XmlElement
    public int activeCount;
    @XmlElement
    public int chatCount;
}
