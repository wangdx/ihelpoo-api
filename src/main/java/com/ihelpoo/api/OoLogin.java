package com.ihelpoo.api;

import com.ihelpoo.api.dao.LoginDao;
import com.ihelpoo.api.dao.impl.LoginDaoImpl;
import com.ihelpoo.api.model.LoginResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author: dongxu.wang@acm.org
 */
@Controller
public class OoLogin {
    @Autowired
    LoginDao loginDao;


    @RequestMapping(value = "/login.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public LoginResult validate(HttpServletRequest request,
                                @RequestParam(value = "username", required = false) String username,
                                @RequestParam(value = "pwd", required = false) String pwd,
                                @RequestParam(value = "keep_login", required = false) String keep_login,
                                HttpServletResponse response) throws NoSuchAlgorithmException, IOException {
        Cookie ooidCookie = new Cookie("ooid", LoginDaoImpl.md5(username) + LoginDaoImpl.md5(pwd) + LoginDaoImpl.md5(String.valueOf(System.currentTimeMillis())));
        response.addCookie(ooidCookie);
        response.setContentType("text/xml; charset=utf-8");
        return loginDao.validate(username, pwd, 1);
    }

}
