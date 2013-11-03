package com.ihelpoo.api.model.common;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author: echowdx@gmail.com
 */

@XmlAccessorType(XmlAccessType.FIELD)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class User {
    @XmlElement
    public int uid;
    @XmlElement
    public String nickname;
    @XmlElement
    public String email;
    @XmlElement
    public int email_verified;
    @XmlElement
    public Integer gender;
    @XmlElement
    public String birthday;
    @XmlElement
    public String enrol_time;
    @XmlElement
    public Integer user_type;
    @XmlElement
    public String login_time;
    @XmlElement
    public Integer last_login;
    @XmlElement
    public String create_time;
    @XmlElement
    public Integer login_days;
    @XmlElement
    public String online_status;
    @XmlElement
    public Integer active_credits;
    @XmlElement
    public String avatar_type;
    @XmlElement
    public String avatar_url;
    @XmlElement
    public String avatar_preview;
    @XmlElement
    public String web_theme;
    @XmlElement
    public String self_intro;
    @XmlElement
    public String real_name;
    @XmlElement
    public Integer followers_count;
    @XmlElement
    public Integer friends_count;
    @XmlElement
    public String school_id;
    @XmlElement
    public String school_name;
    @XmlElement
    public String academy_name;
    @XmlElement
    public String major_name;
    @XmlElement
    public String dorm_name;
    @XmlElement
    public String school_domain;
    @XmlElement
    public Integer level;
    @XmlElement
    public Integer relation;





}
