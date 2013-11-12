package com.ihelpoo.api;

import com.ihelpoo.api.model.DoChatResult;
import com.ihelpoo.api.model.GenericResult;
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
    public UserWordResult stream(
            @RequestParam(value = "pageIndex", required = false) int pageIndex,
            @RequestParam(value = "pageSize", required = false) int pageSize,
            @RequestParam(value = "catalog", required = false) int catalog,
            @RequestParam(value = "uid", required = false) int uid,
            @RequestParam(value = "schoolId", required = false) int schoolId,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {

        return wordService.fetchNotice(uid, catalog, schoolId, pageIndex, pageSize);
    }

    @RequestMapping(value = "/words.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public UserWordResult streamJson(
            @RequestParam(value = "pageIndex", required = false) int pageIndex,
            @RequestParam(value = "pageSize", required = false) int pageSize,
            @RequestParam(value = "catalog", required = false) int catalog,
            @RequestParam(value = "uid", required = false) int uid,
            @RequestParam(value = "schoolId", required = false) int schoolId,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {

        return stream(pageIndex, pageSize, catalog, uid, schoolId, userCookie);
    }


    @RequestMapping(value = "/chats.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ChatResult chatsJSON(
            @RequestParam(value = "pageIndex", required = false) int pageIndex,
            @RequestParam(value = "pageSize", required = false) int pageSize,
            @RequestParam(value = "uid", required = false) int uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return chats(pageIndex, pageSize, uid, userCookie);
    }


    @RequestMapping(value = "/chats.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public ChatResult chats(
            @RequestParam(value = "pageIndex", required = false) int pageIndex,
            @RequestParam(value = "pageSize", required = false) int pageSize,
            @RequestParam(value = "uid", required = false) int uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {

        return wordService.fetchChats(uid, pageIndex, pageSize);
    }


    @RequestMapping(value = "/doChat.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public DoChatResult doChat(
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "receiver", required = false) Integer receiver,
            @RequestParam(value = "receiver_nickname", required = false) String receiverNickname,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {

        try {
            return wordService.doChat(uid, receiver, content, receiverNickname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value = "/doChat.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DoChatResult doChatJSON(
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "receiver", required = false) Integer receiver,
            @RequestParam(value = "receiver_nickname", required = false) String receiverNickname,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return doChat(content, receiver, receiverNickname, uid, userCookie);
    }


    @RequestMapping(value = "/delChat.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult delChat(
            @RequestParam(value = "friend_id", required = false) Integer friendId,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {

        try {
            return wordService.delChat(uid, friendId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/delChat.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult delChatJSON(
            @RequestParam(value = "friend_id", required = false) Integer friendId,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return delChat(uid, friendId, userCookie);
    }
}
