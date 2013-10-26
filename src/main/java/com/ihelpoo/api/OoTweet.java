package com.ihelpoo.api;

import com.ihelpoo.common.Constant;
import com.ihelpoo.api.model.*;
import com.ihelpoo.api.model.obj.Notice;
import com.ihelpoo.api.model.obj.Result;
import com.ihelpoo.api.service.TweetService;
import com.ihelpoo.api.service.WordService;
import com.ihelpoo.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: dongxu.wang@acm.org
 */
@Controller
public class OoTweet {
    @Autowired
    TweetService tweetService;

    @Autowired
    WordService wordService;
    public static final String FAILURE = "0";
    public static final String SUCCESS = "1";
    public static final String MSG_SUC_LOGIN = "发布成功";


    final Logger logger = LoggerFactory.getLogger(this.getClass());


    //TODO add school by user cookies
    @RequestMapping(value = "/pubTweet.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult tweetPub(
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "msg", required = false) String msg,
            @RequestParam(value = "reward", required = false) Integer reward,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String ooidCookie,
            @CookieValue(value = Constant.SELECTED_SCHOOL, required = false) Integer selectedSchool,
            HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String deviceType = "圈圈App";
        if (userAgent != null && userAgent.length() > 0 && userAgent.contains("/")) {
            String[] agentArr = userAgent.split("/");
            deviceType = agentArr[agentArr.length - 2];
        }


        long t = System.currentTimeMillis() / 1000L;
        String imgPath = null;

        if(request instanceof MultipartHttpServletRequest){
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile multipartFile = multipartRequest.getFile("img");
            if (multipartFile != null) {
                try {
                    imgPath = tweetService.uploadImage(uid, multipartRequest, t);
                } catch (AppException e) {
                    GenericResult genericResult = new GenericResult();
                    genericResult.setResult(new Result(FAILURE, e.getMessage()));
                    logger.error("upload image failed, ", e);
                    return genericResult;
                }
            }
        }


        try {
            //TODO school
            tweetService.pubTweet(uid, t, msg, reward, imgPath, deviceType, selectedSchool);
        } catch (Exception e) {
            GenericResult genericResult = new GenericResult();
            genericResult.setResult(new Result(FAILURE, e.getMessage()));
            logger.error("upload image failed, ", e);
            return genericResult;
        }


        GenericResult genericResult = new GenericResult();
        genericResult.setResult(new Result(SUCCESS, MSG_SUC_LOGIN));
        genericResult.setNotice(tweetService.getNotice(uid));
        return genericResult;
    }

    @RequestMapping(value = "/pubTweet.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult tweetPubJson(
            @RequestParam(value = "uid", required = false) int uid,
            @RequestParam(value = "msg", required = false) String msg,
            @RequestParam(value = "reward", required = false) Integer reward,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String ooidCookie,
            @CookieValue(value = Constant.SELECTED_SCHOOL, required = false) Integer selectedSchool,
            HttpServletRequest request) {
        return tweetPub(uid, msg, reward, ooidCookie, selectedSchool, request);
    }

    @RequestMapping(value = "/tweets.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public TweetResult stream(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                              @RequestParam(value = "pageSize", required = false) int pageSize,
                              @RequestParam(value = "catalog", required = false) int catalog,
                              @RequestParam(value = "uid", required = false) int uid,
                              @RequestParam(value = "schoolId", required = false) int schoolId,
                              @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie,
                              HttpServletResponse response) {
        Cookie schoolCookie = new Cookie(Constant.SELECTED_SCHOOL, String.valueOf(schoolId));
        response.addCookie(schoolCookie);
        return tweetService.pullBy(uid, catalog, schoolId, pageIndex, pageSize);
    }

    @RequestMapping(value = "/tweets.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public TweetResult streamJson(@RequestParam(value = "pageIndex", required = false) int pageIndex,
                                  @RequestParam(value = "pageSize", required = false) int pageSize,
                                  @RequestParam(value = "catalog", required = false) int catalog,
                                  @RequestParam(value = "uid", required = false) int uid,
                                  @RequestParam(value = "schoolId", required = false) int schoolId,
                                  @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie,
                                  HttpServletResponse response) {
        return stream(pageIndex, pageSize, catalog, uid, schoolId, userCookie, response);
    }

    @RequestMapping(value = "/tweets/{id}.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public TweetDetailResult say(@PathVariable int id,
                                 @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        TweetDetailResult tdr = null;
        try {
            tdr = tweetService.pullTweetBy(id);
        } catch (Exception e) {
            tdr = new TweetDetailResult();
            logger.error(e.getMessage());
        }
        return tdr;
    }


    @RequestMapping(value = "/tweets/{id}.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public TweetDetailResult sayJson(@PathVariable int id,
                                     @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return say(id, userCookie);
    }

    @RequestMapping(value = "/comments.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public TweetCommentResult comments(@RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                       @RequestParam(value = "catalog", required = false) Integer catalog,
                                       @RequestParam(value = "uid", required = false) Integer uid,
                                       @RequestParam(value = "id", required = false) Integer id,
                                       @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        if (id == null) {
            throw new IllegalArgumentException("id is mandatory");
        }
        pageIndex = pageIndex == null ? Constant.DEFAULT_PAGEINDEX : pageIndex;
        pageSize = pageSize == null ? Constant.DEFAULT_PAGESIZE : pageSize;
        catalog = catalog == null ? 0 : catalog;


        if (catalog == 4) {//chat
            return wordService.pullChatsBy(uid, id, pageIndex, pageSize);
        }

        return tweetService.pullCommentsBy(id, pageIndex, pageSize);
    }

    @RequestMapping(value = "/comments.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public TweetCommentResult commentsJSON(@RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                           @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                           @RequestParam(value = "catalog", required = false) Integer catalog,
                                           @RequestParam(value = "uid", required = false) Integer uid,
                                           @RequestParam(value = "id", required = false) Integer id,
                                           @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return comments(pageIndex, pageSize, catalog, uid, id, userCookie);

    }


    @RequestMapping(value = "/plus.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public TweetCommentPushResult plus(
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "id", required = false) Integer id,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        //TODO credential verification by cookie

        if (uid == null || id == null) {
            return tweetService.plus(0, 0);
        }
        return tweetService.plus(id, uid);
    }

    @RequestMapping(value = "/diffuse.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public TweetCommentPushResult diffuse(
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "content", required = false) String content,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        //TODO credential verification by cookie

        if (uid == null || id == null) {
            return tweetService.diffuse(0, 0, content);
        }
        return tweetService.diffuse(id, uid, content);
    }


    @RequestMapping(value = "/commentPush.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public TweetCommentPushResult commentPush(@RequestParam(value = "content", required = false) String content,
                                              @RequestParam(value = "uid", required = false) int uid,
                                              @RequestParam(value = "catalog", required = false) int catalog,
                                              @RequestParam(value = "id", required = false) int id,
                                              @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        //TODO credential verification by cookie
        return tweetService.pushComment(id, uid, null, content, 0);
    }


    @RequestMapping(value = "/commentPush.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public TweetCommentPushResult commentPushJSON(@RequestParam(value = "content", required = false) String content,
                                                  @RequestParam(value = "uid", required = false) int uid,
                                                  @RequestParam(value = "catalog", required = false) int catalog,
                                                  @RequestParam(value = "id", required = false) int id,
                                                  @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return commentPush(content, uid, catalog, id, userCookie);
    }

    @RequestMapping(value = "/commentReply.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public TweetCommentPushResult commentReply(@RequestParam(value = "content", required = false) String content,
                                               @RequestParam(value = "uid", required = false) int uid,
                                               @RequestParam(value = "authorid", required = false) int authorid,
                                               @RequestParam(value = "replyid", required = false) int replyid,
                                               @RequestParam(value = "catalog", required = false) int catalog,
                                               @RequestParam(value = "id", required = false) int id,
                                               @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return tweetService.pushComment(id, uid, null, content, 0);
    }

    @RequestMapping(value = "/commentReply.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public TweetCommentPushResult commentReplyJSON(@RequestParam(value = "content", required = false) String content,
                                                   @RequestParam(value = "uid", required = false) int uid,
                                                   @RequestParam(value = "authorid", required = false) int authorid,
                                                   @RequestParam(value = "replyid", required = false) int replyid,
                                                   @RequestParam(value = "catalog", required = false) int catalog,
                                                   @RequestParam(value = "id", required = false) int id,
                                                   @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return commentReply(content, uid, authorid, replyid, catalog, id, userCookie);
    }


    @RequestMapping(value = "/delTweet.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult deleteTweetXML(
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "sid", required = false) Integer sid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String ooidCookie) {
        return tweetService.deleteTweet(uid, sid);
    }

    @RequestMapping(value = "/delTweet", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult deleteTweet(
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "sid", required = false) Integer sid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String ooidCookie) {
        return deleteTweetXML(uid, sid, ooidCookie);
    }

    @RequestMapping(value = "/delTweet.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult deleteTweetJSON(
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "sid", required = false) Integer sid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String ooidCookie) {
        return deleteTweetXML(uid, sid, ooidCookie);
    }

}
