package com.ihelpoo.api.model;

import com.ihelpoo.common.Constant;
import com.ihelpoo.api.model.obj.Notice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
@XmlRootElement(name = Constant.IHELPOO_XML_ROOT)
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageResult {
    @XmlElement
    public int page_size;

    @XmlElement
    public Messages newslist;

    @XmlElement
    public Notice notice;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Messages {
        @XmlElement
        public List<Message> news;

        public Messages(List<Message> news) {
            this.news = news;
        }
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Message {
        @XmlElement
        public int id;
        @XmlElement
        public String title;
        @XmlElement
        public int commentCount;
        @XmlElement
        public String author;
        @XmlElement
        public String inout;
        @XmlElement
        public String pubDate;
        @XmlElement
        public String url;
    }

}
