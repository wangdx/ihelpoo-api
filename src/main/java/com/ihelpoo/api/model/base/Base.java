package com.ihelpoo.api.model.base;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author: dongxu.wang@acm.org
 */
public abstract class Base implements Serializable {

    public final static String UTF8 = "UTF-8";
    public final static String NODE_ROOT = "oschina";



    @XmlElement
    protected Notice notice;
    public void setNotice(Notice notice) {
        this.notice = notice;
    }

}