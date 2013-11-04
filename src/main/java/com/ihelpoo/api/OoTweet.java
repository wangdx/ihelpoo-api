package com.ihelpoo.api;

import com.ihelpoo.common.AppUtil;
import com.ihelpoo.common.Constant;
import com.ihelpoo.api.model.*;
import com.ihelpoo.api.model.obj.Result;
import com.ihelpoo.api.service.TweetService;
import com.ihelpoo.api.service.WordService;
import com.ihelpoo.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public GenericResult pubTweetXML(
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "msg", required = false) String msg,
            @RequestParam(value = "reward", required = false) Integer reward,
            @RequestParam(value = "target_school", required = false) Integer targetSchool,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String ooidCookie,
            HttpServletRequest request
    ) {

        GenericResult genericResult = new GenericResult();
        Result result = new Result();
        result.setErrorCode(FAILURE);

        if (uid == null || msg == null || targetSchool == null) {
            result.setErrorMessage("参数错误");
            genericResult.setResult(result);
            logger.error("缺少参数：uid=" + uid + " message=" + msg + " target school = " + targetSchool);
            return genericResult;
        }

        if (targetSchool < 1) {
            result.setErrorMessage("未选择学校");
            genericResult.setResult(result);
            logger.error("未选择学校");
            return genericResult;
        }

        if (targetSchool == 35) {
            logger.warn("选择了默认学校，某些信息在WEB端可能不会显示");
        }

        long t = System.currentTimeMillis() / 1000L;
        String imgPath = null;

        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile multipartFile = multipartRequest.getFile("img");
            if (multipartFile != null) {
                try {
                    imgPath = tweetService.uploadImage(uid, multipartRequest, t);
                } catch (AppException e) {
                    result.setErrorMessage(e.getMessage());
                    genericResult.setResult(result);
                    logger.error("upload image failed, ", e);
                    return genericResult;
                }
            }
        }

        int resultCode = 0;
        try {
            resultCode = tweetService.pubTweet(uid, t, msg, reward, imgPath, AppUtil.getDeviceType(request), targetSchool);
        } catch (Exception e) {
            result.setErrorMessage(e.getMessage());
            genericResult.setResult(result);
            logger.error("upload image failed, ", e);
            return genericResult;
        }

        if (resultCode == TweetService.ERR_DUPLICATED_CONTENT) {
            result.setErrorCode(FAILURE);
            result.setErrorMessage("不要贪心噢，不能重复发布相同的内容");
            genericResult.setResult(result);
            return genericResult;
        }

        result.setErrorCode(SUCCESS);
        result.setErrorMessage(MSG_SUC_LOGIN);
        genericResult.setResult(result);
        genericResult.setNotice(tweetService.getNotice(uid));
        return genericResult;
    }

    @RequestMapping(value = "/pubTweet.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult tweetPubJson(
            @RequestParam(value = "uid", required = false) int uid,
            @RequestParam(value = "msg", required = false) String msg,
            @RequestParam(value = "reward", required = false) Integer reward,
            @RequestParam(value = "target_school", required = false) Integer targetSchool,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String ooidCookie,
            HttpServletRequest request) {
        return pubTweetXML(uid, msg, reward, targetSchool, ooidCookie, request);
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
    public TweetDetailResult say(
            @PathVariable Integer id,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        TweetDetailResult tdr = new TweetDetailResult();
        Result result = new Result();
        result.setErrorCode("0");
        if (id == null || uid == null) {
            result.setErrorMessage("参数错误");
            logger.error("参数错误 id=" + id + " uid=" + uid);
            tdr.result = result;
            return tdr;
        }
        try {
            tdr = tweetService.pullTweetBy(id, uid);
        } catch (EmptyResultDataAccessException e) {
            logger.error(e.getMessage());
            result.setErrorMessage("未找到该记录，可能已被作者删除");
            tdr.result = result;
            return tdr;
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setErrorMessage("系统错误：" + e.getMessage());
            tdr.result = result;
            return tdr;
        }

        result.setErrorCode("1");
        result.setErrorMessage("成功");
        tdr.result = result;
        return tdr;
    }


    @RequestMapping(value = "/tweets/{id}.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public TweetDetailResult sayJson(
            @PathVariable Integer id,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        return say(id, uid, userCookie);
    }

    @RequestMapping(value = "/comments.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public TweetCommentResult comments(
            @RequestParam(value = "is_help", required = false) Boolean isHelp,
            @RequestParam(value = "page_index", required = false) Integer pageIndex,
            @RequestParam(value = "page_size", required = false) Integer pageSize,
            @RequestParam(value = "catalog", required = false) Integer catalog,
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "sid", required = false) Integer id,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        catalog = catalog == null ? 0 : catalog;


        if (catalog == 4) {//chat
            return wordService.pullChatsBy(uid, id, pageIndex, pageSize);
        }

        if (isHelp != null && isHelp) {
            return tweetService.pullHelpRepliesBy(uid, id, pageIndex, pageSize);
        }
        return tweetService.pullCommentsBy(uid, id, pageIndex, pageSize);
    }

    @RequestMapping(value = "/comments.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public TweetCommentResult commentsJSON(
            @RequestParam(value = "is_help", required = false) Boolean isHelp,
            @RequestParam(value = "page_index", required = false) Integer pageIndex,
            @RequestParam(value = "page_size", required = false) Integer pageSize,
            @RequestParam(value = "catalog", required = false) Integer catalog,
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "sid", required = false) Integer id,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        return comments(isHelp, pageIndex, pageSize, catalog, uid, id, userCookie);

    }


    @RequestMapping(value = "/plus.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult plus(
            @RequestParam(value = "sid", required = false) Integer sid,
            @RequestParam(value = "uid", required = false) Integer uid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        return tweetService.plus(sid, uid);
    }

    @RequestMapping(value = "/diffuse.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult diffuse(
            @RequestParam(value = "sid", required = false) Integer sid,
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "content", required = false) String content,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie
    ) {
        return tweetService.diffuse(sid, uid, content);
    }


    @RequestMapping(value = "/commentPush.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public TweetCommentPushResult commentPushXML(@RequestParam(value = "content", required = false) String content,
                                                 @RequestParam(value = "uid", required = false) int uid,
                                                 @RequestParam(value = "catalog", required = false) int catalog,
                                                 @RequestParam(value = "id", required = false) int id,
                                                 @RequestParam(value = "is_help", required = false) Boolean help,
                                                 @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return tweetService.pushComment(id, uid, content, 0, help);
    }


    @RequestMapping(value = "/commentPush.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public TweetCommentPushResult commentPushJSON(@RequestParam(value = "content", required = false) String content,
                                                  @RequestParam(value = "uid", required = false) int uid,
                                                  @RequestParam(value = "catalog", required = false) int catalog,
                                                  @RequestParam(value = "id", required = false) int id,
                                                  @RequestParam(value = "is_help", required = false) Boolean help,
                                                  @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return commentPushXML(content, uid, catalog, id, help, userCookie);
    }


    @RequestMapping(value = "/commentDelete.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult commentDeleteXML(
            @RequestParam(value = "authorid", required = false) Integer authorid,
            @RequestParam(value = "replyid", required = false) Integer replyid,
            @RequestParam(value = "is_help", required = false) Boolean isHelp,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return tweetService.deleteComment(authorid, replyid, isHelp);
    }

    @RequestMapping(value = "/commentReply.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public TweetCommentPushResult commentReply(@RequestParam(value = "content", required = false) String content,
                                               @RequestParam(value = "uid", required = false) int uid,
                                               @RequestParam(value = "authorid", required = false) int authorid,
                                               @RequestParam(value = "replyid", required = false) int replyid,
                                               @RequestParam(value = "catalog", required = false) int catalog,
                                               @RequestParam(value = "id", required = false) int id,
                                               @RequestParam(value = "is_help", required = false) Boolean isHelp,
                                               @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return tweetService.replyComment(id, uid, content, authorid, isHelp);
    }

    @RequestMapping(value = "/commentReply.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public TweetCommentPushResult commentReplyJSON(@RequestParam(value = "content", required = false) String content,
                                                   @RequestParam(value = "uid", required = false) int uid,
                                                   @RequestParam(value = "authorid", required = false) int authorid,
                                                   @RequestParam(value = "replyid", required = false) int replyid,
                                                   @RequestParam(value = "catalog", required = false) int catalog,
                                                   @RequestParam(value = "id", required = false) int id,
                                                   @RequestParam(value = "is_help", required = false) Boolean isHelp,
                                                   @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        return commentReply(content, uid, authorid, replyid, catalog, id, isHelp, userCookie);
    }


    @RequestMapping(value = "/delTweet.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult deleteTweetXML(
            @RequestParam(value = "uid", required = false) Integer uid,
            @RequestParam(value = "sid", required = false) Integer sid,
            @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String ooidCookie) {
        return tweetService.deleteTweet(uid, sid, false);
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
