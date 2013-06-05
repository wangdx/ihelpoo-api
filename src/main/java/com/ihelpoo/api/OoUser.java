package com.ihelpoo.api;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ihelpoo users
 * @author: dongxu.wang@acm.org
 */

@Controller
public class OoUser {


    @Autowired
    private
    UserDao userDao;

    @RequestMapping(value = "/users/{dimension}.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public UserList getUsers(@PathVariable String dimension,
                             @RequestParam(value="_p") int param,
                             @RequestParam(value="limit") int limit,
                             @RequestParam(value="offset") int offset) {
        UserDimension userDimension = new UserDimension();
        userDimension.addAttribute(dimension, param);
        userDimension.addAttribute("limit", limit);
        userDimension.addAttribute("offset", offset);
        return findAllUsers(userDimension);
//        return userDao.getUserList(grade);
    }

    @RequestMapping(value = "/users/{dimension}.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public UserList getUsersJson(@PathVariable String dimension,
                                 @RequestParam(value="_p") int param,
                                 @RequestParam(value="limit") int limit,
                                 @RequestParam(value="offset") int offset) {
        UserDimension userDimension = new UserDimension();
        userDimension.addAttribute(dimension, param);
        userDimension.addAttribute("limit", limit);
        userDimension.addAttribute("offset", offset);
        return findAllUsers(userDimension);
    }

    private UserList findAllUsers(UserDimension userDimension){
        return userDao.getUserList(userDimension);
    }

    public static class UserDimension{
        private Map<String, Object> attributes;

        public UserDimension(){
            attributes = new LinkedHashMap();
        }

//        private UserDimension(Map<String, Object> attributes) {
//            this.attributes = attributes;
//        }

        private Map<String, Object> getAttributes() {
            return Collections.unmodifiableMap(attributes);
        }

        public Object getAttribute(String attrKey){
            return attributes.get(attrKey);
        }

        private void setAttributes(Map<String, Object> attributes) {
            this.attributes = attributes;
        }

        public void addAttribute(String attribute, Object value){
            attributes.put(attribute, value);
        }

        public Object removeAttribute(Object attrValue){
            return attributes.remove(attrValue);
        }


    }

}
