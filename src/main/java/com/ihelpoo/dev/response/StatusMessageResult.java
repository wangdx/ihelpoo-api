package com.ihelpoo.dev.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author: echowdx@gmail.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
public final class StatusMessageResult {

    @XmlElement
    public String status;
    @XmlElement
    public String message;
    @XmlElement
    public String code;
    @XmlElement
    public String more;

}
