package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.GenericResult;
import com.ihelpoo.api.model.MessageResult;
import com.ihelpoo.api.model.obj.Notice;
import com.ihelpoo.api.model.obj.Result;
import com.ihelpoo.api.model.entity.IUserLoginEntity;
import com.ihelpoo.api.model.entity.VLoginRecordEntity;
import com.ihelpoo.api.service.base.RecordService;
import com.ihelpoo.common.Constant;
import com.ihelpoo.common.util.UpYun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public static final int TO_FOLLOW = 0;
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

    @Transactional
    public GenericResult updateRelation(int hisuid, int uid, int relation) {
        GenericResult genericResult = new GenericResult();
        genericResult.setNotice(new Notice());
        Result result = new Result();
        genericResult.setResult(result);
        result.setErrorCode("0");

        switch (relation) {
            case 0:
                return unfollow(hisuid, uid, genericResult, result);
            case 1:
                return follow(hisuid, uid, genericResult, result);
            default:
                result.setErrorMessage("updateRelation参数relation错误");
                return genericResult;
        }
    }

    private GenericResult follow(int hisuid, int uid, GenericResult genericResult, Result result) {
        if (hisuid == uid) {
            result.setErrorMessage("自己不能圈自己");
            return genericResult;
        }

        try {
            userDao.findPrioritiesBy(uid, hisuid);
            result.setErrorMessage("你已经圈了Ta");
            return genericResult;
        } catch (EmptyResultDataAccessException e) {
        }

        try {
            userDao.findShieldBy(uid, hisuid);
            result.setErrorMessage("你已经屏蔽了Ta，取消屏蔽后才能圈");
            return genericResult;
        } catch (EmptyResultDataAccessException e) {
        }

        IUserLoginEntity loginEntity = null;
        try {
            loginEntity = userDao.findUserById(hisuid);
        } catch (EmptyResultDataAccessException e) {
            result.setErrorMessage("你要圈的用户不存在");
            return genericResult;
        }

        try {
            userDao.savePriority(uid, hisuid, loginEntity.getType());
            userDao.updateRelation(uid, hisuid, true);
            int active = loginEntity.getActive() == null ? 0 : loginEntity.getActive();
            userDao.updateFollowActive(hisuid, uid, active, "有人圈了你 (每人最多加1次 user:" + uid + ")");
            userDao.updateActive(hisuid, active + 5);
        } catch (Exception e) {
            result.setErrorMessage("系统错误：" + e.getMessage());
            return genericResult;
        }

        //TODO notice

        result.setErrorCode("1");
        result.setErrorMessage("成功圈了Ta");
        genericResult.setResult(result);
        return genericResult;
    }

    private GenericResult unfollow(int hisuid, int uid, GenericResult genericResult, Result result) {
        try {
            userDao.findPrioritiesBy(uid, hisuid);
        } catch (EmptyResultDataAccessException e) {
            result.setErrorMessage("取消圈失败，您还未圈Ta");
            return genericResult;
        }

        try {
            userDao.deletePriority(uid, hisuid);
            userDao.updateRelation(uid, hisuid, false);
        } catch (Exception e) {
            result.setErrorMessage("系统错误：" + e.getMessage());
            return genericResult;
        }

        result.setErrorCode("1");
        result.setErrorMessage("成功取消圈");
        return genericResult;
    }
}
