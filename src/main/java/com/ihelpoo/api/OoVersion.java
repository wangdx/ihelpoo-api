package com.ihelpoo.api;

import com.ihelpoo.api.model.VersionResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: dongxu.wang@acm.org
 */
@Controller
public class OoVersion {


    public static final String VERSION_NAME = "1.0.1";
    public static final String VERSION_CODE = "2";
    public static final String DOWNLOADS_URL = "http://download.ihelpoo.cn/apps/ihelpoo.apk";
    public static final String WP7_VERSION_NAME = "1.0";
    public static final String IOS_VERSION_NAME = "1.0.0";

    @RequestMapping(value = "/versions.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public VersionResult checkVersionXML(){
        VersionResult.Android andr = new VersionResult.Android.Builder()
                .versionCode(VERSION_CODE)
                .versionName(VERSION_NAME)
                .downloadUrl(DOWNLOADS_URL)
                .changeLog("\r\n  版本信息：ihelpoo.com for Android v" + VERSION_NAME
                        + "\r\n  更新日志：首次发布"
                        + "\r\n")
                .build();
        //TODO using CDATA and escape

        VersionResult.Update update = new VersionResult.Update.Builder()
                .wp7(WP7_VERSION_NAME)
                .ios(IOS_VERSION_NAME)
                .android(andr)
                .build();
        VersionResult vr = new VersionResult();
        vr.setUpdate(update);
        return vr;
    }


    @RequestMapping(value = "/versions.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public VersionResult checkVersionJson(){
        return checkVersionXML();
    }


    @RequestMapping(value = "/versions", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public VersionResult checkVersion(){
        return checkVersionXML();
    }
}


