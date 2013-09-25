package com.ihelpoo.api;

import com.ihelpoo.api.model.DoChatResult;
import com.ihelpoo.api.model.TweetCommentResult;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.base.Result;
import com.ihelpoo.common.Constant;
import com.ihelpoo.api.model.ChatResult;
import com.ihelpoo.api.model.UserWordResult;
import com.ihelpoo.api.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author: dongxu.wang@acm.org
 */
@Controller
public class OoWord {
    @Autowired
    WordService wordService;

    @RequestMapping(value = "/words.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public UserWordResult stream(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                                   @RequestParam(value = "pageSize", required = false) int pageSize,
                                   @RequestParam(value = "catalog", required = false) int catalog,
                                   @RequestParam(value = "uid", required = false) int uid,
                                   @RequestParam(value = "schoolId", required = false) int schoolId,
                                   @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {

        return wordService.fetchNotice(uid, catalog, schoolId, pageIndex, pageSize);
    }

    @RequestMapping(value = "/words.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public UserWordResult streamJson(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                                 @RequestParam(value = "pageSize", required = false) int pageSize,
                                 @RequestParam(value = "catalog", required = false) int catalog,
                                 @RequestParam(value = "uid", required = false) int uid,
                                 @RequestParam(value = "schoolId", required = false) int schoolId,
                                 @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {

        return stream(pageIndex, pageSize, catalog, uid, schoolId, userCookie);
    }




    @RequestMapping(value = "/chats.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public ChatResult chats(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                            @RequestParam(value = "pageSize", required = false) int pageSize,
                            @RequestParam(value = "uid", required = false) int uid,
                            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {

        return wordService.fetchChats(uid, pageIndex, pageSize);
    }



    @RequestMapping(value = "/doChat.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public DoChatResult doChat(@RequestParam(value = "content", required = false) String content,
                            @RequestParam(value = "receiver", required = false) Integer receiver,
                            @RequestParam(value = "uid", required = false) Integer uid,
                            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        DoChatResult doChatResult = new DoChatResult();
        Result result = new Result();
        result.setErrorCode("1");
        result.setErrorMessage("留言发表成功");

        TweetCommentResult.Comment comment = new TweetCommentResult.Comment();
        comment.setId(997502);
        comment.setAuthorid(12419);
        comment.setAuthor("echow");
        comment.setContent("测试");
        comment.setPortrait("http://static.oschina.net/uploads/user/457/915579_50.jpeg");
        comment.setPubDate("2013-09-25 11:47:54");



        doChatResult.result = result;
        doChatResult.comment = comment;
        doChatResult.notice = new Notice();

        return doChatResult;
    }
}
