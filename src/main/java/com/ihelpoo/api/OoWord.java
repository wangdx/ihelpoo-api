package com.ihelpoo.api;

import com.ihelpoo.api.common.OoConstant;
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
                                   @CookieValue(value = OoConstant.OO_USER_COOKIE, required = false) String userCookie) {

        return wordService.fetchNotice(uid, catalog, schoolId, pageIndex, pageSize);
    }
}
