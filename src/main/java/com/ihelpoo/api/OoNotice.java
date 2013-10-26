package com.ihelpoo.api;

import com.ihelpoo.api.model.GenericResult;
import com.ihelpoo.common.Constant;
import com.ihelpoo.api.model.MessageResult;
import com.ihelpoo.api.model.NoticeResult;
import com.ihelpoo.api.model.obj.Notice;
import com.ihelpoo.api.model.obj.Result;
import com.ihelpoo.api.service.NoticeService;
import com.ihelpoo.api.service.UserService;
import com.ihelpoo.api.service.WordService;
import com.ihelpoo.common.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author: dongxu.wang@acm.org
 */

@Controller
public class OoNotice {

    @Autowired
    NoticeService noticeService;

    @Autowired
    UserService userService;

    @Autowired
    MD5 md5;

    @Autowired
    WordService wordService;


    @RequestMapping(value = "/noticeClear.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult noticeClearXML(
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "from_uid", required = false) Integer fromUid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        return noticeService.clearNotice(uid, type, 0);
    }

    @RequestMapping(value = "/noticeClear.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult noticeClearJSON(
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "from_uid", required = false) Integer fromUid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        return noticeClearXML(uid, type, fromUid, userCookie);
    }

    @RequestMapping(value = "/noticeClear", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult noticeClear(
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "from_uid", required = false) Integer fromUid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        return noticeClearXML(uid, type, fromUid, userCookie);
    }

    @RequestMapping(value = "/noticeBound.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public NoticeResult validate(
            @RequestParam(value = "uid", required = false) int uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        NoticeResult noticeResult = new NoticeResult();

        Result result = checkUserCookie(uid, userCookie);
        noticeResult.setResult(result);
        noticeResult.setNotice(noticeService.getNotice(uid));
        return noticeResult;
    }


    @RequestMapping(value = "/notices.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public MessageResult streamJSON(
            @RequestParam(value = "pageIndex", required = false) int pageIndex,
            @RequestParam(value = "pageSize", required = false) int pageSize,
            @RequestParam(value = "catalog", required = false) int catalog,
            @RequestParam(value = "uid", required = false) int uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        return stream(pageIndex, pageSize, catalog, uid, userCookie);
    }

    @RequestMapping(value = "/notices.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public MessageResult stream(
            @RequestParam(value = "pageIndex", required = false) int pageIndex,
            @RequestParam(value = "pageSize", required = false) int pageSize,
            @RequestParam(value = "catalog", required = false) int catalog,
            @RequestParam(value = "uid", required = false) int uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {

        return wordService.fetchAndDeliverActive(uid, pageIndex, pageSize);
    }

    private Result checkUserCookie(int uid, String userCookie) {
        String encryptedPwd = userService.fetchPwdByUid(uid);
        String userName = userService.fetchUserNameByUid(uid);
        String encryptedUserName = md5.encrypt(userName);
        Result result = null;
        if (userCookie == null || !userCookie.contains(encryptedUserName + encryptedPwd)) {
            result = new Result("0", "用户未登录");
        }
        return result;
    }
}
