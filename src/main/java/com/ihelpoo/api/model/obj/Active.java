package com.ihelpoo.api.model.obj;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
* @author: dongxu.wang@acm.org
*/
@XmlRootElement(name = "active")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Active {

    @XmlElement(name = "id")
    public int sid;
    @XmlElement(name = "portrait")
    public String iconUrl;
    @XmlElement(name = "author")
    public String nickname;
    @XmlElement(name = "authorid")
    public int uid;
    @XmlElement(name = "catalog")
    public int catalog;
    @XmlElement(name = "objecttype")
    public int objecttype;
    @XmlElement(name = "objectcatalog")
    public int objectcatalog;
    @XmlElement(name = "objecttitle")
    public String objecttitle;
    @XmlElement(name = "appclient")
    public int from;
    @XmlElement(name = "objectreply")
    public ObjectReply objectreply;
    @XmlElement(name = "url")
    public String url;
    @XmlElement(name = "objectID")
    public int objectID;
    @XmlElement(name = "message")
    public String content;
    @XmlElement(name = "commentCount")
    public int commentCo;
    @XmlElement(name = "pubDate")
    public String time;
    @XmlElement(name = "tweetimage")
    public String image;


    @XmlElement(name = "diffusionCo")
    public int diffusionCo;
    @XmlElement(name = "academy")
    public String academy;
    @XmlElement(name = "authorType")
    public String authorType;
    @XmlElement(name = "authorGossip")
    public String authorGossip;
    @XmlElement(name = "online")
    public int online;
    @XmlElement(name="activeRank")
    public String rank;
}
