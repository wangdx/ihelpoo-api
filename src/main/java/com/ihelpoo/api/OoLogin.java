package com.ihelpoo.api;

import com.ihelpoo.api.dao.LoginDao;
import com.ihelpoo.api.model.GenericResult;
import com.ihelpoo.api.service.LoginService;
import com.ihelpoo.common.Constant;
import com.ihelpoo.common.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author: dongxu.wang@acm.org
 */
@Controller
public class OoLogin {
    @Autowired
    LoginDao loginDao;

    @Autowired
    LoginService loginService;

    @Autowired
    MD5 md5;


    @RequestMapping(value = "/login.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult login(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "pwd", required = false) String pwd,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "ip", required = false) String ip,
            HttpServletResponse response) throws NoSuchAlgorithmException, IOException {
        Cookie ooidCookie = new Cookie(Constant.OO_USER_COOKIE, md5.encrypt(username) + pwd + md5.encrypt(String.valueOf(System.currentTimeMillis())));
        response.addCookie(ooidCookie);
        response.setContentType("text/xml; charset=utf-8");
        return loginService.login(username, pwd, status, ip, false);
    }

    @RequestMapping(value = "/login.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult loginJson(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "pwd", required = false) String pwd,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "ip", required = false) String ip,
            HttpServletResponse response) throws NoSuchAlgorithmException, IOException {
        return login(username, pwd, status, ip, response);
    }
}
