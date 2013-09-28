package com.ihelpoo.api;

import com.ihelpoo.common.Constant;
import com.ihelpoo.api.model.StreamResult;
import com.ihelpoo.api.model.UserWordResult;
import com.ihelpoo.api.service.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author: dongxu.wang@acm.org
 */
@Controller
public class OoStream {

    @Autowired
    StreamService streamService;

    @RequestMapping(value = "/stream.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public StreamResult stream(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                               @RequestParam(value = "pageSize", required = false) int pageSize,
                               @RequestParam(value = "catalog", required = false) int catalog,
                               @RequestParam(value = "uid", required = false) int uid,
                               @RequestParam(value = "schoolId", required = false) int schoolId,
                               @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie){
        //TODO credential verification by cookie
        return streamService.pullBy(uid, catalog, schoolId, pageIndex, pageSize);
    }


    @RequestMapping(value = "/stream.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public StreamResult streamJson(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                               @RequestParam(value = "pageSize", required = false) int pageSize,
                               @RequestParam(value = "catalog", required = false) int catalog,
                               @RequestParam(value = "uid", required = false) int uid,
                               @RequestParam(value = "schoolId", required = false) int schoolId,
                               @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie){
        //TODO credential verification by cookie
        return streamService.pullBy(uid, catalog, schoolId, pageIndex, pageSize);
    }


    @RequestMapping(value = "/userActive.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public UserWordResult userInfo(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                               @RequestParam(value = "pageSize", required = false) int pageSize,
                               @RequestParam(value = "hisname", required = false) String hisname,
                               @RequestParam(value = "uid", required = false) int uid,
                               @RequestParam(value = "hisuid", required = false) int hisuid,
                               @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie){
        //TODO credential verification by cookie
        return streamService.pullUserActiveBy(uid, hisname,hisuid, pageIndex, pageSize);
    }


    @RequestMapping(value = "/userActive.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public UserWordResult userInfoJSON(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                                   @RequestParam(value = "pageSize", required = false) int pageSize,
                                   @RequestParam(value = "hisname", required = false) String hisname,
                                   @RequestParam(value = "uid", required = false) int uid,
                                   @RequestParam(value = "hisuid", required = false) int hisuid,
                                   @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie){
        //TODO credential verification by cookie
        return userInfo(pageIndex, pageSize, hisname, uid, hisuid, userCookie);
    }

}
