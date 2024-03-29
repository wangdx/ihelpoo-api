package com.ihelpoo.api.model;

import com.ihelpoo.api.model.obj.Notice;

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
    private int post_count;
    @XmlElement
    private int page_size;
    @XmlElement
    private Posts posts;
    @XmlElement
    protected Notice notice;

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public void setPost_count(int post_count) {
        this.post_count = post_count;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    public int getPost_count() {
        return post_count;
    }

    public int getPage_size() {
        return page_size;
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
        private String sayType;


        @XmlElement
        private String answer;

        @XmlElement
        protected int id;

        public String getSayType() {
            return sayType;
        }

        public void setSayType(String sayType) {
            this.sayType = sayType;
        }

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
