package com.ihelpoo.api.model;

import com.ihelpoo.api.model.entity.IUserLoginEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserList {

    @XmlElement(name = "user")
    private List<IUserLoginEntity> users;

    public UserList() {
        users = new ArrayList<IUserLoginEntity>();
    }

    public List<IUserLoginEntity> getUsers() {
        return users;
    }

    public void setUsers(List<IUserLoginEntity> users) {
        this.users = users;
    }
}
