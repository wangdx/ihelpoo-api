package com.ihelpoo.api;

import com.ihelpoo.common.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author: echowdx@gmail.com
 */
@Controller
public class Test {
    @RequestMapping(value = "/books/{id}", method = RequestMethod.DELETE, produces = "application/xml")
    @ResponseBody
    public String delete(@PathVariable int id, @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        System.out.println("---DELETE---");
        System.out.println(id);
        System.out.println(userCookie);
        return String.valueOf(id);
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.PUT, produces = "application/xml")
    @ResponseBody
    public String put(@PathVariable int id, @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        System.out.println("---PUT---");
        System.out.println(id);
        System.out.println(userCookie);
        return String.valueOf(id);
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public String get(@PathVariable int id, @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        System.out.println("---GET---");
        System.out.println(id);
        System.out.println(userCookie);
        return String.valueOf(id);
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public String post(@PathVariable int id, @CookieValue(value = Constant.OO_USER_COOKIE, required = false) String userCookie) {
        System.out.println("---POST---");
        System.out.println(id);
        System.out.println(userCookie);
        return String.valueOf(id);
    }
}
