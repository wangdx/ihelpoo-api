package com.ihelpoo.api;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.*;
import com.ihelpoo.api.model.obj.Notice;
import com.ihelpoo.api.model.obj.Result;
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

    @RequestMapping(value = "/updateMajor.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult updateMajorXML(
            @RequestParam(value = "school_id", required = false) Integer schoolId,
            @RequestParam(value = "academy_id", required = false) Integer academyId,
            @RequestParam(value = "major_id", required = false) Integer majorId,
            @RequestParam(value = "dorm_id", required = false) Integer dormId,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        return userService.updateMajor(uid, schoolId, academyId, majorId, dormId);
    }

    @RequestMapping(value = "/updateMajor.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult updateMajorJSON(
            @RequestParam(value = "school_id", required = false) Integer schoolId,
            @RequestParam(value = "academy_id", required = false) Integer academyId,
            @RequestParam(value = "major_id", required = false) Integer majorId,
            @RequestParam(value = "dorm_id", required = false) Integer dormId,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        return userService.updateMajor(uid, schoolId, academyId, majorId, dormId);
    }

    @RequestMapping(value = "/updateMajor", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult updateMajor(
            @RequestParam(value = "school_id", required = false) Integer schoolId,
            @RequestParam(value = "academy_id", required = false) Integer academyId,
            @RequestParam(value = "major_id", required = false) Integer majorId,
            @RequestParam(value = "dorm_id", required = false) Integer dormId,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        return userService.updateMajor(uid, schoolId, academyId, majorId, dormId);
    }

    @RequestMapping(value = "/updateIntro.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult updateIntroXML(
            @RequestParam(value = "intro", required = false) String newIntro,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return userService.updateIntro(uid, newIntro);
    }

    @RequestMapping(value = "/updateIntro.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult updateIntroJSON(
            @RequestParam(value = "intro", required = false) String newIntro,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return userService.updateIntro(uid, newIntro);
    }

    @RequestMapping(value = "/updateIntro", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult updateIntro(
            @RequestParam(value = "intro", required = false) String newIntro,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return userService.updateIntro(uid, newIntro);
    }

    @RequestMapping(value = "/updateEnrol.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult updateEnrolXML(
            @RequestParam(value = "enrol", required = false) String newEnrol,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return userService.updateEnrol(uid, newEnrol);
    }

    @RequestMapping(value = "/updateEnrol.json", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult updateEnrolJSON(
            @RequestParam(value = "enrol", required = false) String newEnrol,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return userService.updateEnrol(uid, newEnrol);
    }

    @RequestMapping(value = "/updateEnrol", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult updateEnrol(
            @RequestParam(value = "enrol", required = false) String newEnrol,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return userService.updateEnrol(uid, newEnrol);
    }

    @RequestMapping(value = "/updateGender.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult updateGenderXML(
            @RequestParam(value = "gender", required = false) String newGender,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return userService.updateGender(uid, newGender);
    }

    @RequestMapping(value = "/updateGender.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult updateGenderJSON(
            @RequestParam(value = "gender", required = false) String newGender,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return userService.updateGender(uid, newGender);
    }

    @RequestMapping(value = "/updateGender", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult updateGender(
            @RequestParam(value = "gender", required = false) String newGender,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return userService.updateGender(uid, newGender);
    }

    @RequestMapping(value = "/updateNickname.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult updateNicknameXML(
            @RequestParam(value = "nickname", required = false) String newNickname,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return userService.updateNickname(uid, newNickname);
    }

    @RequestMapping(value = "/updateNickname.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult updateNicknameJSON(
            @RequestParam(value = "nickname", required = false) String newNickname,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return userService.updateNickname(uid, newNickname);
    }

    @RequestMapping(value = "/updateNickname", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult updateNickname(
            @RequestParam(value = "nickname", required = false) String newNickname,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return userService.updateNickname(uid, newNickname);
    }

    /**
     * @param hisUid
     * @param uid
     * @param relation   0 取消圈 1 圈
     * @param userCookie
     * @return
     */
    @RequestMapping(value = "/updateRelation.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult followXML(
            @RequestParam(value = "his_uid", required = false) Integer hisUid,
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "relation", required = false) Integer relation,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return userService.updateRelation(hisUid, uid, relation);
    }

    @RequestMapping(value = "/updateRelation.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult followJSON(
            @RequestParam(value = "his_uid", required = false) Integer hisUid,
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "relation", required = false) Integer relation,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return followXML(hisUid, uid, relation, userCookie);
    }

    @RequestMapping(value = "/updateRelation", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult follow(
            @RequestParam(value = "his_uid", required = false) Integer hisUid,
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "relation", required = false) Integer relation,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return followXML(hisUid, uid, relation, userCookie);
    }


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
    public MessageResult stream(
            @RequestParam(value = "pageIndex", required = false) int pageIndex,
            @RequestParam(value = "pageSize", required = false) int pageSize,
            @RequestParam(value = "catalog", required = false) int catalog,
            @RequestParam(value = "uid", required = false) int uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {

//        return wordService.fetchAndDeliverActive(uid, pageIndex, pageSize);

        return userService.fetchActivesBy(uid, pageIndex, pageSize);
    }

    @RequestMapping(value = "/buddies", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public FriendsResult getBuddies(
            @RequestParam(value = "uid") Integer uid,
            @RequestParam(value = "relation") Integer relation,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "pageIndex") Integer pageIndex,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        return getBuddiesXML(uid, relation, pageSize, pageIndex, userCookie);
    }

    @RequestMapping(value = "/buddies.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public FriendsResult getBuddiesJSON(
            @RequestParam(value = "uid") Integer uid,
            @RequestParam(value = "relation") Integer relation,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "pageIndex") Integer pageIndex,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        return getBuddiesXML(uid, relation, pageSize, pageIndex, userCookie);
    }

    @RequestMapping(value = "/buddies.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public FriendsResult getBuddiesXML(
            @RequestParam(value = "uid") Integer uid,
            @RequestParam(value = "relation") Integer relation,
            @RequestParam(value = "page_size") Integer pageSize,
            @RequestParam(value = "page_index") Integer pageIndex,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {

        return userService.getFriends(uid, relation, pageSize, pageIndex);

    }


    @RequestMapping(value = "/user/{uid}.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public UserResult getUserById(
            @PathVariable(value = "uid") int uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return userService.getUserDetail(uid);

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
    public UserList getUsers(
            @PathVariable String dimension,
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
    public UserList getUsersJson(
            @PathVariable String dimension,
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
