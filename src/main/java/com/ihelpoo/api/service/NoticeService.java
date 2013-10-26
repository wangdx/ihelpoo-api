package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.NotificationDao;
import com.ihelpoo.api.model.GenericResult;
import com.ihelpoo.api.model.obj.Result;
import com.ihelpoo.api.service.base.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class NoticeService extends RecordService{
    @Autowired
    NotificationDao notificationDao;

    public GenericResult clearNotice(Integer uid, Integer type, Integer fromUid) {
        String uidStr = String.valueOf(uid);
        GenericResult genericResult = new GenericResult();
        Result result = new Result();
        result.setErrorCode("0");
        if (uid == null || type == null) {
            result.setErrorMessage("参数错误");
            genericResult.setResult(result);
            return genericResult;
        }

        switch (type.intValue()) {
            case 0:
                clearSystem(uidStr);
            case 1:
                clearAt(uid);
            case 2:
                clearComment(uid);
            case 3:
                clearActive(uid);
            case 4:
                clearMessage(uid, fromUid);
            default:
        }

        try {

        } catch (Exception e) {
            result.setErrorMessage("清除失败，系统错误：" + e.getMessage());
            genericResult.setResult(result);
            return genericResult;
        }
        result.setErrorCode("1");
        result.setErrorMessage("清除成功");
        genericResult.setResult(result);
        return genericResult;  //To change body of created methods use File | Settings | File Templates.
    }

    private void clearMessage(Integer uid, Integer fromUid) {
        try {
            int i = notificationDao.deliverAllMessage(uid, fromUid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearActive(Integer uid) {
        try {
            int i = notificationDao.deliverAllActive(uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearComment(Integer uid) {
        try {
            notificationDao.deliverAllComment(uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearAt(Integer uid) {
        try {
            notificationDao.deliverAllAtme(uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearSystem(String uidStr) {
        Jedis jedis = new Jedis("localhost");
        Set<String> notices = jedis.hkeys(WordService.R_ACCOUNT + WordService.R_MESSAGE + uidStr);
        for (String notice : notices) {
            jedis.hset(WordService.R_ACCOUNT + WordService.R_MESSAGE + uidStr, notice, "1");
        }
        resestNoticeCount(uidStr, jedis);
        jedis.disconnect();
    }

    private void resestNoticeCount(String uidStr, Jedis jedis) {
        jedis.hdel(WordService.R_NOTICE + WordService.R_SYSTEM + uidStr.substring(0, uidStr.length() - 3), uidStr.substring(uidStr.length() - 3));
    }
}
