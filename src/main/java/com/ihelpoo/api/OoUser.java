package com.ihelpoo.api;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.*;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.base.Result;
import com.ihelpoo.api.model.entity.VLoginRecordEntity;
import com.ihelpoo.api.service.UserService;
import com.ihelpoo.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * ihelpoo users
 *
 * @author: dongxu.wang@acm.org
 */

@Controller
public class OoUser {


    @Autowired
    private
    UserDao userDao;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/avatarUpload.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult avatarUploadXML(
            @RequestParam(value = "uid") Integer uid,
            HttpServletRequest request,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return avatarUpload(uid, request, userCookie);

    }
    @RequestMapping(value = "/avatarUpload.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult avatarUploadJSON(
            @RequestParam(value = "uid") Integer uid,
            HttpServletRequest request,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return avatarUpload(uid, request, userCookie);
    }
    @RequestMapping(value = "/avatarUpload", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult avatarUpload(
            @RequestParam(value = "uid") Integer uid,
            HttpServletRequest request,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {


        long t = System.currentTimeMillis() / 1000L;
        String newImageName = userService.uploadFile(uid, (MultipartHttpServletRequest) request, t);
        userService.updateRecord(uid, newImageName, 1);// TODO get schoolId from userCookie

        GenericResult result = new GenericResult();
        Result r = new Result();
        r.setErrorCode("1");
        r.setErrorMessage("操作成功");


        result.setResult(r);
        result.setNotice(new Notice());
        return result;
    }


    @RequestMapping(value = "/active.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public MessageResult stream(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                                @RequestParam(value = "pageSize", required = false) int pageSize,
                                @RequestParam(value = "catalog", required = false) int catalog,
                                @RequestParam(value = "uid", required = false) int uid,
                                @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie){

//        return wordService.fetchAndDeliverActive(uid, pageIndex, pageSize);

        return userService.fetchActivesBy(uid, pageIndex, pageSize);
    }

    @RequestMapping(value = "/fos.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public FoResult getFosJSON(
            @RequestParam(value = "uid") Integer uid,
            @RequestParam(value = "relation") Integer relation,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "pageIndex") Integer pageIndex,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return getFos(uid, relation, pageSize, pageIndex, userCookie);
    }

    @RequestMapping(value = "/fos.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public FoResult getFos(
            @RequestParam(value = "uid") Integer uid,
            @RequestParam(value = "relation") Integer relation,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "pageIndex") Integer pageIndex,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {

        FoResult foResult = new FoResult();
        foResult.notice = new Notice();
        FoResult.Fo friend1 = new FoResult.Fo();
        friend1.name = "friend1";
        friend1.expertise = "expertise";
        friend1.gender = "2";
        friend1.portrait = "http://static.oschina.net/uploads/user/583/1166611_100.jpg?t=1370763973000";
        friend1.userid = 12419;

        FoResult.Fo friend2 = new FoResult.Fo();
        friend2.name = "friend2";
        friend2.expertise = "expertise2";
        friend2.gender = "1";
        friend2.portrait = "http://static.oschina.net/uploads/user/583/1166611_100.jpg?t=1370763973000";
        friend2.userid = 12419;

        FoResult.Fos friends = new FoResult.Fos();
        friends.friend = new ArrayList<FoResult.Fo>();
        friends.friend.add(friend1);
        friends.friend.add(friend2);


        foResult.friends = friends;
        return foResult;

    }


    @RequestMapping(value = "/user/{uid}.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public UserResult getUserById(
            @PathVariable(value = "uid") int uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        UserResult userResult = new UserResult();
        UserResult.User user = new UserResult.User();
        user.name = "echow";
        user.portrait = "http://static.oschina.net/uploads/user/457/915579_100.jpeg";
        user.jointime = "2012-12-09 13:48:05";
        user.gender = "1";
        user.from = "5";
        user.devplatform = "793（1）";
        user.expertise = "2天";
        user.favoritecount = 1;
        user.fanscount = 2;
        user.followerscount = 3;
        userResult.user = user;
        userResult.notice = new Notice();
        return userResult;
    }

    @RequestMapping(value = "/user/{uid}.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public UserResult getUserByIdJSON(
            @PathVariable(value = "uid") int uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return getUserById(uid, userCookie);

    }

    @RequestMapping(value = "/users/{dimension}/{level}.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public UserList getUsers(@PathVariable String dimension,
                             @PathVariable(value = "level") int level,
                             @RequestParam(value = "limit") int limit,
                             @RequestParam(value = "offset") int offset) {
        UserDimension userDimension = new UserDimension();
        userDimension.addAttribute(dimension, level);
        userDimension.addAttribute("limit", limit);
        userDimension.addAttribute("offset", offset);
        return findAllUsers(userDimension);
//        return userDao.getUserList(grade);
    }

    @RequestMapping(value = "/users/{dimension}/{level}.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public UserList getUsersJson(@PathVariable String dimension,
                                 @PathVariable(value = "level") int level,
                                 @RequestParam(value = "limit") int limit,
                                 @RequestParam(value = "offset") int offset) {
        UserDimension userDimension = new UserDimension();
        userDimension.addAttribute(dimension, level);
        userDimension.addAttribute("limit", limit);
        userDimension.addAttribute("offset", offset);
        return findAllUsers(userDimension);
    }

    private UserList findAllUsers(UserDimension userDimension) {
        return userDao.getUserList(userDimension);
    }

    public static class UserDimension {
        private Map<String, Object> attributes;

        public UserDimension() {
            attributes = new LinkedHashMap();
        }

//        private UserDimension(Map<String, Object> attributes) {
//            this.attributes = attributes;
//        }

        private Map<String, Object> getAttributes() {
            return Collections.unmodifiableMap(attributes);
        }

        public Object getAttribute(String attrKey) {
            return attributes.get(attrKey);
        }

        private void setAttributes(Map<String, Object> attributes) {
            this.attributes = attributes;
        }

        public void addAttribute(String attribute, Object value) {
            attributes.put(attribute, value);
        }

        public Object removeAttribute(Object attrValue) {
            return attributes.remove(attrValue);
        }


    }

}
