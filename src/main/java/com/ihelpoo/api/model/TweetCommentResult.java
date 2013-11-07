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
public class TweetCommentResult {
    @XmlElement
    public int all_count;
    @XmlElement
    public int page_size;
    @XmlElement
    public Comments comments;
    @XmlElement
    public Notice notice;


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Comments {
        @XmlElement
        public List<Comment> comment;

        public Comments(List<Comment> comment) {
            this.comment = comment;
        }
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Comment {
        @XmlElement
        public int id;
        @XmlElement
        public String portrait;
        @XmlElement
        public String author;
        @XmlElement
        public int authorid;
        @XmlElement
        public String content;
        @XmlElement
        public String pubDate;
        @XmlElement
        public int appclient;
    }
}
