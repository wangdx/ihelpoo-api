package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.NotificationDao;
import com.ihelpoo.api.model.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class NotificationService {
    @Autowired
    NotificationDao notificationDao;

    public int countAtMsgs(int uid){
        List<IMsgAtEntity> atMsgs = notificationDao.findAllAtMsgsByUid(uid);
        return atMsgs.size();
    }

    public int countCommentMsgs(int uid){
        List<IMsgCommentEntity> commentMsgs = notificationDao.findAllCommentMsgsByUid(uid);
        return commentMsgs.size();
    }

    public int countSystemMsgs(int uid){
        List<IMsgSystemEntity> systemMsgs = notificationDao.findAllSystemMsgsByUid(uid);
        return systemMsgs.size();
    }

    public int countTalkContents(int uid){
        List<ITalkContentEntity> talkContents = notificationDao.findAllTalkContentsByUid(uid);
        return talkContents.size();
    }

    public int countUserCoins(int uid){
        List<IUserCoinsEntity> userCoins = notificationDao.findAllUserCoinsByUid(uid);
        return userCoins.size();
    }
}
