package com.ihelpoo.api.model;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
* @author: dongxu.wang@acm.org
*/
public class Posts {
    @XmlElement
    private List<Post> post;

    public void setPost(List<Post> post) {
        this.post = post;
    }
}
