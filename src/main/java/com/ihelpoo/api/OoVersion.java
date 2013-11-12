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


    public static final String VERSION_NAME = "1.0.2";
    public static final String VERSION_CODE = "3";
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
                        + "\r\n  更新日志：\r\n1.增加悄悄话删除功能；\r\n2.调整列表界面；\r\n3.优化安装包体积"
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


