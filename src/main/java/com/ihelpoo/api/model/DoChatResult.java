package com.ihelpoo.api.model;

import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.base.Result;
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
public class DoChatResult {
    @XmlElement
    public Result result;
    @XmlElement
    public TweetCommentResult.Comment comment;
    @XmlElement
    public Notice notice;
}
