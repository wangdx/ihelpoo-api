package com.ihelpoo.api;

import com.ihelpoo.api.common.OoConstant;
import com.ihelpoo.api.model.*;
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

    @RequestMapping(value = "/tweets.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public TweetResult stream(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                               @RequestParam(value = "pageSize", required = false) int pageSize,
                               @RequestParam(value = "catalog", required = false) int catalog,
                               @RequestParam(value = "uid", required = false) int uid,
                               @CookieValue(value = OoConstant.OO_USER_COOKIE, required = false) String userCookie){
        //TODO credential verification by cookie
        return tweetService.pullBy(uid, catalog, pageIndex, pageSize);
    }

    @RequestMapping(value = "/tweets/{id}.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public TweetDetailResult say(@RequestParam(value = "sid", required = false) int sid, //TODO sid == id
                                 @PathVariable int id,
                                        @CookieValue(value = OoConstant.OO_USER_COOKIE, required = false) String userCookie){
        return tweetService.pullTweetBy(id);
    }

    @RequestMapping(value = "/comments.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public TweetCommentResult comments(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                                       @RequestParam(value = "pageSize", required = false) int pageSize,
                                       @RequestParam(value = "catalog", required = false) int catalog,
                                       @RequestParam(value = "id", required = false) int id,
                                       @CookieValue(value = OoConstant.OO_USER_COOKIE, required = false) String userCookie){
        //TODO credential verification by cookie
        return tweetService.pullCommentsBy(id, catalog, pageIndex, pageSize);
    }



    @RequestMapping(value = "/commentPush.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public TweetCommentPushResult commentPush(@RequestParam(value = "content", required = false) String content,
                                       @RequestParam(value = "uid", required = false) int uid,
                                       @RequestParam(value = "catalog", required = false) int catalog,
                                       @RequestParam(value = "id", required = false) int id,
                                       @RequestParam(value = "isPostToMyZone", required = false) int isPostToMyZone,
                                       @CookieValue(value = OoConstant.OO_USER_COOKIE, required = false) String userCookie){
        //TODO credential verification by cookie
        return tweetService.pushComment(id, uid, null, content, 0, isPostToMyZone);
    }


    @RequestMapping(value = "/commentReply.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public TweetCommentPushResult commentReply(@RequestParam(value = "content", required = false) String content,
                                              @RequestParam(value = "uid", required = false) int uid,
                                              @RequestParam(value = "authorid", required = false) int authorid,
                                              @RequestParam(value = "replyid", required = false) int replyid,
                                              @RequestParam(value = "catalog", required = false) int catalog,
                                              @RequestParam(value = "id", required = false) int id,
                                              @RequestParam(value = "isPostToMyZone", required = false) Integer isPostToMyZone,
                                              @CookieValue(value = OoConstant.OO_USER_COOKIE, required = false) String userCookie){
        //TODO credential verification by cookie
        return tweetService.pushComment(id, uid, null, content, 0, 0);
    }



}
