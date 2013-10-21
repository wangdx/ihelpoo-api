package com.ihelpoo.api;

import com.ihelpoo.api.model.GenericResult;
import com.ihelpoo.api.model.SMSCodeResult;
import com.ihelpoo.api.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: echowdx@gmail.com
 */

@Controller
public class SMSRegister {

    @Autowired
    RegisterService registerService;

    @RequestMapping(value = "/mobileCode.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public SMSCodeResult smsCodeXML(@RequestParam(value = "mobile") String mobile) {
        return registerService.fetchSMSCode(mobile);
    }


    @RequestMapping(value = "/mobileCode.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public SMSCodeResult smsCodeJSON(@RequestParam(value = "mobile") String mobile) {
        return smsCodeXML(mobile);
    }

    @RequestMapping(value = "/mobileCode", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public SMSCodeResult smsCode(@RequestParam(value = "mobile") String mobile) {
        return smsCodeXML(mobile);
    }


    @RequestMapping(value = "/mobileRegister.xml", method = RequestMethod.POST, produces = "application/xml")
    @ResponseBody
    public GenericResult registerXML(
            @RequestParam(value = "code") String code,
            @RequestParam(value = "mobile") String mobile,
            @RequestParam(value = "pwd") String pwd,
            @RequestParam(value = "schoolId") Integer schoolId,
            @RequestParam(value = "ip") String ip,
            @RequestParam(value = "deviceType") String deviceType
    ) {
        return registerService.register(code, mobile, pwd, schoolId, ip, deviceType);
    }

    @RequestMapping(value = "/mobileRegister.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult registerJSON(
            @RequestParam(value = "code") String code,
            @RequestParam(value = "mobile") String mobile,
            @RequestParam(value = "pwd") String pwd,
            @RequestParam(value = "schoolId") Integer schoolId,
            @RequestParam(value = "ip") String ip,
            @RequestParam(value = "deviceType") String deviceType
    ) {
        return registerXML(code, mobile, pwd, schoolId, ip, deviceType);
    }

    @RequestMapping(value = "/mobileRegister", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GenericResult register(
            @RequestParam(value = "code") String code,
            @RequestParam(value = "mobile") String mobile,
            @RequestParam(value = "pwd") String pwd,
            @RequestParam(value = "schoolId") Integer schoolId,
            @RequestParam(value = "ip") String ip,
            @RequestParam(value = "deviceType") String deviceType
    ) {
        return registerXML(code, mobile, pwd, schoolId, ip, deviceType);
    }
}
