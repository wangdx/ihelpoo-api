package com.ihelpoo.api;

import com.ihelpoo.api.common.OoConstant;
import com.ihelpoo.api.dao.LoginDao;
import com.ihelpoo.api.model.LoginResult;
import com.ihelpoo.api.service.LoginService;
import com.ihelpoo.api.util.MD5;
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
    public LoginResult validate(@RequestParam(value = "username", required = false) String username,
                                @RequestParam(value = "pwd", required = false) String pwd,
                                @RequestParam(value = "keep_login", required = false) String keep_login,
                                HttpServletResponse response) throws NoSuchAlgorithmException, IOException {
        Cookie ooidCookie = new Cookie(OoConstant.OO_USER_COOKIE, md5.encrypt(username) + md5.encrypt(pwd) + md5.encrypt(String.valueOf(System.currentTimeMillis())));
        response.addCookie(ooidCookie);
        response.setContentType("text/xml; charset=utf-8");
        return loginService.validate(username, pwd, 1);
    }

}
