package com.ihelpoo.api;

import com.ihelpoo.api.common.OoConstant;
import com.ihelpoo.api.model.StreamResult;
import com.ihelpoo.api.model.UserActiveResult;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.service.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
                               @CookieValue(value = OoConstant.OO_USER_COOKIE, required = false) String userCookie){
        //TODO credential verification by cookie
        return streamService.pullBy(uid, catalog, pageIndex, pageSize);
    }


    @RequestMapping(value = "/userActive.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public UserActiveResult userInfo(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                               @RequestParam(value = "pageSize", required = false) int pageSize,
                               @RequestParam(value = "hisname", required = false) String hisname,
                               @RequestParam(value = "uid", required = false) int uid,
                               @RequestParam(value = "hisuid", required = false) int hisuid,
                               @CookieValue(value = OoConstant.OO_USER_COOKIE, required = false) String userCookie){
        //TODO credential verification by cookie
        return streamService.pullUserActiveBy(uid, hisname,hisuid, pageIndex, pageSize);
    }


}
