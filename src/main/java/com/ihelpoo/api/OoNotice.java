package com.ihelpoo.api;

import com.ihelpoo.api.common.OoConstant;
import com.ihelpoo.api.model.NoticeResult;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.base.Result;
import com.ihelpoo.api.service.NotificationService;
import com.ihelpoo.api.service.UserService;
import com.ihelpoo.api.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author: dongxu.wang@acm.org
 */

@Controller
public class OoNotice {

    @Autowired
    NotificationService ns;

    @Autowired
    UserService userService;

    @Autowired
    MD5 md5;

    @RequestMapping(value = "/notifications.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public NoticeResult validate(@RequestParam(value = "uid", required = false) int uid, @CookieValue(value = OoConstant.OO_USER_COOKIE, required = false) String userCookie) {
        NoticeResult noticeResult = new NoticeResult();

        Result result = checkUserCookie(uid, userCookie);
        noticeResult.setResult(result);
        Notice notice = new Notice.Builder().
                at(ns.countAtMsgs(uid)).
                comment(ns.countCommentMsgs(uid)).
                system(ns.countSystemMsgs(uid)).
                talk(ns.countTalkContents(uid)).build();
        noticeResult.setNotice(notice);

        //TODO return only error message if user not login
        return noticeResult;
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
