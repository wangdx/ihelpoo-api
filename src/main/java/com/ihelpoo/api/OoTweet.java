package com.ihelpoo.api;

import com.ihelpoo.api.common.OoConstant;
import com.ihelpoo.api.model.StreamResult;
import com.ihelpoo.api.model.TweetResult;
import com.ihelpoo.api.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author: dongxu.wang@acm.org
 */
@Controller
public class OoTweet {
    @Autowired
    TweetService tweetService;

    @RequestMapping(value = "/tweet.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public TweetResult stream(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                               @RequestParam(value = "pageSize", required = false) int pageSize,
                               @RequestParam(value = "catalog", required = false) int catalog,
                               @RequestParam(value = "uid", required = false) int uid,
                               @CookieValue(value = OoConstant.OO_USER_COOKIE, required = false) String userCookie){
        //TODO credential verification by cookie
        return tweetService.pullBy(uid, catalog, pageIndex, pageSize);
    }

}
