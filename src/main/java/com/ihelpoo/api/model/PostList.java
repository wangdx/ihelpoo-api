package com.ihelpoo.api.model;

import com.ihelpoo.api.model.base.Notice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
@XmlRootElement(name = "ihelpoo")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostList{
    @XmlElement
    private int postCount;
    @XmlElement
    private int pagesize = 20;
    @XmlElement
    private Posts posts;
    @XmlElement
    protected Notice notice;

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    public int getPostCount() {
        return postCount;
    }

    public int getPagesize() {
        return pagesize;
    }

    public Posts getPosts() {
        return posts;
    }

    public Notice getNotice() {
        return notice;
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Posts {
        @XmlElement
        private List<Post> post;

        public void setPost(List<Post> post) {
            this.post = post;
        }

        public List<Post> getPost() {
            return post;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Post {
        @XmlElement
        private String portrait;
        @XmlElement
        private String author;
        @XmlElement
        private Integer authorid;
        @XmlElement
        private String title;
        @XmlElement
        private Integer answerCount;
        @XmlElement
        private Integer viewCount;
        @XmlElement
        private String pubDate;

        @XmlElement
        private String answer;

        @XmlElement
        protected int id;

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setAuthorid(Integer authorid) {
            this.authorid = authorid;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setAnswerCount(Integer answerCount) {
            this.answerCount = answerCount;
        }

        public void setViewCount(Integer viewCount) {
            this.viewCount = viewCount;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPortrait() {
            return portrait;
        }

        public String getAuthor() {
            return author;
        }

        public Integer getAuthorid() {
            return authorid;
        }

        public String getTitle() {
            return title;
        }

        public Integer getAnswerCount() {
            return answerCount;
        }

        public Integer getViewCount() {
            return viewCount;
        }

        public String getPubDate() {
            return pubDate;
        }

        public String getAnswer() {
            return answer;
        }

        public int getId() {
            return id;
        }
    }
}
