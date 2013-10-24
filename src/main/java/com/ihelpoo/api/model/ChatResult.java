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
public class ChatResult {
    @XmlElement
    public int pagesize;
    @XmlElement
    public int messageCount;

    @XmlElement
    public Chats messages;

    @XmlElement
    public Notice notice;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Chats {
        @XmlElement
        public List<Chat> message;

        public Chats(List<Chat> message) {
            this.message = message;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Chat {
        @XmlElement
        public int id;
        @XmlElement
        public String portrait;
        @XmlElement
        public int friendid;
        @XmlElement
        public String friendname;
        @XmlElement
        public String sender;
        @XmlElement
        public int senderid;
        @XmlElement
        public String content;
        @XmlElement
        public int messageCount;
        @XmlElement
        public String pubDate;
    }
}
