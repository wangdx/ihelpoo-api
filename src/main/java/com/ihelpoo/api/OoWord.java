package com.ihelpoo.api;

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
}
