package com.ihelpoo.api;

import com.ihelpoo.api.common.OoConstant;
import com.ihelpoo.api.model.MessageResult;
import com.ihelpoo.api.model.NoticeResult;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.base.Result;
import com.ihelpoo.api.service.NotificationService;
import com.ihelpoo.api.service.UserService;
import com.ihelpoo.api.service.WordService;
import com.ihelpoo.api.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    WordService wordService;

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



    @RequestMapping(value = "/notices.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public MessageResult stream(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                              @RequestParam(value = "pageSize", required = false) int pageSize,
                              @RequestParam(value = "catalog", required = false) int catalog,
                              @RequestParam(value = "uid", required = false) int uid,
//                              @RequestParam(value = "schoolId", required = false) int schoolId,
                              @CookieValue(value = OoConstant.OO_USER_COOKIE, required = false) String userCookie){

        return wordService.fetchActive(uid, pageIndex, pageSize);

//        Notice notice = new Notice.Builder()
//                .talk(0)
//                .system(0)
//                .comment(0)
//                .at(0)
//                .build();
//        List<MessageResult.Message> list = new ArrayList<MessageResult.Message>();
//
//        wordService.fetchActive(uid, pageIndex, pageSize);
//
//        MessageResult.Message m1 = new MessageResult.Message.Builder()
//                .author("蔡耀华")
//                .commentCount(2)
//                .id(1)
//                .pubDate("2013-09-06 08:02:44")
//                .title("赞了你发布的")
//                .url("http://www.google.com")
//                .build();
//        MessageResult.Message m2 = new MessageResult.Message.Builder()
//                .author("孙权")
//                .commentCount(3)
//                .id(2)
//                .pubDate("2013-09-06 08:02:44")
//                .title("您组织的活动审核未通过，请重新填写，务必合符组织活动规范!")
//                .url("http://www.google.com")
//                .build();
//        MessageResult.Message m3 = new MessageResult.Message.Builder()
//                .author("我帮圈圈 提示：")
//                .commentCount(3)
//                .id(3)
//                .pubDate("2013-09-06 08:02:44")
//                .title("有人屏蔽了你，请注意言行，被扣除5活跃 (每天最多扣5活跃)")
//                .url("http://www.google.com")
//                .build();
//        list.add(m1);
//        list.add(m2);
//        list.add(m3);
//        MessageResult.Messages newslist = new MessageResult.Messages(list);
//        MessageResult mr = new MessageResult();
//        mr.setPagesize(20);
//        mr.setNotice(notice);
//        mr.setNewslist(newslist);
//        return mr;
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
