package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.MessageResult;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.entity.IUserLoginEntity;
import com.ihelpoo.api.model.entity.VLoginRecordEntity;
import com.ihelpoo.api.service.base.RecordService;
import com.ihelpoo.common.Constant;
import com.ihelpoo.common.util.UpYun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class UserService extends RecordService {
    @Autowired
    UserDao userDao;
    private int imgId;

    public String fetchPwdByUid(int uid) {
        IUserLoginEntity user = userDao.findUserById(uid);
        return user.getPassword();
    }

    public String fetchUserNameByUid(int uid) {
        IUserLoginEntity user = userDao.findUserById(uid);
        return user.getEmail();
    }


    @Transactional
    public void updateRecord(int uid, String newImageName, int schoolId) {
        long t = System.currentTimeMillis() / 1000;
        userDao.updateAvatar(uid, 1, newImageName);
        int imgId = userDao.saveOutimg(uid, t, Constant.IMG_STORAGE_ROOT + "/useralbum/" + uid + "/" + newImageName + ".jpg");
        int sayId = userDao.addSay(uid, 2, "我刚刚换了新头像噢 :)", imgId, t, "动态", schoolId);
        int dynId = userDao.addDynamic(sayId, "changeicon");
    }

    public String uploadFile(int uid, MultipartHttpServletRequest request, long t) {
        MultipartFile multipartFile = request.getFile("portrait");
        if (multipartFile == null) {
            return "";
        }
        String imageOldName = multipartFile.getOriginalFilename();
        String newImageName = "icon" + uid + t;
        String imagePath = "/useralbum/" + uid + "/" + newImageName;
        String suffix = imageOldName.substring(imageOldName.lastIndexOf('.'));
        String mFilePath = imagePath + "_m" + suffix;
        String sFilePath = imagePath + "_s" + suffix;
        String lFilePath = imagePath + suffix;

        Map<String, String> lParams = new HashMap<String, String>();
        Map<String, String> mParams = new HashMap<String, String>();
        Map<String, String> sParams = new HashMap<String, String>();

        // 设置缩略图类型，必须搭配缩略图参数值（KEY_VALUE）使用，否则无效
        lParams.put(UpYun.PARAMS.KEY_X_GMKERL_TYPE.getValue(), UpYun.PARAMS.VALUE_FIX_BOTH.getValue());
        // 设置缩略图参数值，必须搭配缩略图类型（KEY_TYPE）使用，否则无效
        lParams.put(UpYun.PARAMS.KEY_X_GMKERL_VALUE.getValue(), "500x375");
        // 设置缩略图的质量，默认 95
        lParams.put(UpYun.PARAMS.KEY_X_GMKERL_QUALITY.getValue(), "95");

        mParams.put(UpYun.PARAMS.KEY_X_GMKERL_TYPE.getValue(), UpYun.PARAMS.VALUE_FIX_BOTH.getValue());
        mParams.put(UpYun.PARAMS.KEY_X_GMKERL_VALUE.getValue(), "180x135");


        sParams.put(UpYun.PARAMS.KEY_X_GMKERL_TYPE.getValue(), UpYun.PARAMS.VALUE_FIX_BOTH.getValue());
        sParams.put(UpYun.PARAMS.KEY_X_GMKERL_VALUE.getValue(), "68x51");

        UpYun upyun = new UpYun("ihelpoo", "api", "Ihelpoo.com");
        try {
            upyun.writeFile(lFilePath, multipartFile.getBytes(), true, lParams);
            upyun.writeFile(mFilePath, multipartFile.getBytes(), true, mParams);
            upyun.writeFile(sFilePath, multipartFile.getBytes(), true, sParams);
        } catch (IOException e) {

        }
        return newImageName;
    }

    public MessageResult fetchActivesBy(int uid, int pageIndex, int pageSize) {
        List<VLoginRecordEntity> entities = userDao.findAllActivesBy(uid, pageIndex, pageSize);
        List<MessageResult.Message> list = new ArrayList<MessageResult.Message>();
        for (VLoginRecordEntity entity : entities) {
            MessageResult.Message message = new MessageResult.Message();
            message.author = entity.getNickname();
            message.commentCount = entity.getCommentCo() == null ? 0 : entity.getCommentCo();
            message.id = entity.getSid();
            message.pubDate = convertToDate(entity.getTime());
            message.title = entity.getContent();
            message.url = entity.getUrl();
            message.inout = "";
            list.add(message);
        }

        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();
        MessageResult.Messages newslist = new MessageResult.Messages(list);
        MessageResult mr = new MessageResult();
        mr.pagesize = 20;
        mr.notice = notice;
        mr.newslist = newslist;
        return mr;
    }
}
