package com.ihelpoo.api.model;

import com.ihelpoo.api.common.OoConstant;
import com.ihelpoo.api.model.base.Notice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
@XmlRootElement(name = OoConstant.IHELPOO_XML_ROOT)
@XmlAccessorType(XmlAccessType.FIELD)
public class ChatResult {
    @XmlElement
    int pagesize;
    @XmlElement
    int messageCount;

    @XmlElement
    Chats messages;

    @XmlElement
    Notice notice;


    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public Chats getMessages() {
        return messages;
    }

    public void setMessages(Chats messages) {
        this.messages = messages;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Chats {
        @XmlElement
        List<Chat> message;

        public Chats(List<Chat> message) {
            this.message = message;
        }

        public List<Chat> getMessage() {
            return message;
        }

        public void setMessage(List<Chat> message) {
            this.message = message;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Chat {
        @XmlElement
        int id;
        @XmlElement
        String portrait;
        @XmlElement
        int friendid;
        @XmlElement
        String friendname;
        @XmlElement
        String sender;
        @XmlElement
        int senderid;
        @XmlElement
        String content;
        @XmlElement
        int messageCount;
        @XmlElement
        String pubDate;

        private Chat() {
        }

        public Chat(Builder builder) {
            this.content = builder.content;
            this.friendid = builder.friendid;
            this.friendname = builder.friendname;
            this.id = builder.id;
            this.messageCount = builder.messageCount;
            this.portrait = builder.portrait;
            this.pubDate = builder.pubDate;
            this.sender = builder.sender;
            this.senderid=builder.senderid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public int getFriendid() {
            return friendid;
        }

        public void setFriendid(int friendid) {
            this.friendid = friendid;
        }

        public String getFriendname() {
            return friendname;
        }

        public void setFriendname(String friendname) {
            this.friendname = friendname;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public int getSenderid() {
            return senderid;
        }

        public void setSenderid(int senderid) {
            this.senderid = senderid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getMessageCount() {
            return messageCount;
        }

        public void setMessageCount(int messageCount) {
            this.messageCount = messageCount;
        }

        public String getPubDate() {
            return pubDate;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public static class Builder {
            public Chat build() {
                return new Chat(this);
            }

            int id;
            String portrait;
            int friendid;
            String friendname;
            String sender;
            int senderid;
            String content;
            int messageCount;
            String pubDate;

            public Builder portrait(String portrait) {
                this.portrait = portrait;
                return this;
            }

            public Builder friendname(String friendname) {
                this.friendname = friendname;
                return this;
            }

            public Builder sender(String sender) {
                this.sender = sender;
                return this;
            }

            public Builder content(String content) {
                this.content = content;
                return this;
            }

            public Builder pubDate(String pubDate) {
                this.pubDate = pubDate;
                return this;
            }

            public Builder senderid(int senderid) {
                this.senderid = senderid;
                return this;
            }

            public Builder id(int id) {
                this.id = id;
                return this;
            }

            public Builder friendid(int friendid) {
                this.friendid = friendid;
                return this;
            }

            public Builder messageCount(int messageCount) {
                this.messageCount = messageCount;
                return this;
            }

        }


    }
}
