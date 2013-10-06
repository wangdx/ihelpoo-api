package com.ihelpoo.api;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.*;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.base.Result;
import com.ihelpoo.common.Constant;
import com.ihelpoo.common.util.UpYun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
        String filePath = uploadFile(uid, (MultipartHttpServletRequest) request, t);

        GenericResult result = new GenericResult();
        Result r = new Result();
        r.setErrorCode("1");
        r.setErrorMessage("操作成功");


        result.setResult(r);
        result.setNotice(new Notice());
        return result;
    }


    private String uploadFile(int uid, MultipartHttpServletRequest request, long t) {
        MultipartFile multipartFile = request.getFile("portrait");
        if (multipartFile == null) {
            return "";
        }
        String imageOldName = multipartFile.getOriginalFilename();
        String filePath = "";
        UpYun upyun = new UpYun("ihelpoo", "api", "Ihelpoo.com");
        try {
            filePath = "/useralbum/" + uid + "/icon" + uid +t + imageOldName.substring(imageOldName.lastIndexOf('.'));
            boolean result = upyun.writeFile(filePath, multipartFile.getBytes());
        } catch (IOException e) {

        }
        return filePath;
    }



    @RequestMapping(value = "/active.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public MessageResult stream(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                                @RequestParam(value = "pageSize", required = false) int pageSize,
                                @RequestParam(value = "catalog", required = false) int catalog,
                                @RequestParam(value = "uid", required = false) int uid,
                                @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie){

//        return wordService.fetchAndDeliverActive(uid, pageIndex, pageSize);

        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();
        List<MessageResult.Message> list = new ArrayList<MessageResult.Message>();

//        wordService.fetchAndDeliverActive(uid, pageIndex, pageSize);

        MessageResult.Message m1 = new MessageResult.Message();
        m1.author = "蔡耀华";
        m1.commentCount = 2;
        m1.id = 1;
        m1.pubDate = "2013-09-06 08:02:44";
        m1.title =  "赞了你发布的";
        m1.url = "http://www.google.com";
        m1.inout = "";

        MessageResult.Message m2 = new MessageResult.Message();
        m2.author = "蔡耀华";
        m2.commentCount = 2;
        m2.id = 1;
        m2.pubDate = "2013-09-06 08:02:44";
        m2.title =  "赞了你发布的";
        m2.url = "http://www.google.com";
        m2.inout = "";

        MessageResult.Message m3 = new MessageResult.Message();
        m3.author = "蔡耀华";
        m3.commentCount = 2;
        m3.id = 1;
        m3.pubDate = "2013-09-06 08:02:44";
        m3.title =  "赞了你发布的";
        m3.url = "http://www.google.com";
        m3.inout = "";
        list.add(m1);
        list.add(m2);
        list.add(m3);
        MessageResult.Messages newslist = new MessageResult.Messages(list);
        MessageResult mr = new MessageResult();
        mr.pagesize = 20;
        mr.notice = notice;
        mr.newslist = newslist;
        return mr;
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
