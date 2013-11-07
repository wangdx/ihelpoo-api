package com.ihelpoo.api.dao;

import com.ihelpoo.api.model.entity.*;

import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public interface NotificationDao {

    int findNewAtmeCount(int uid);

    int findNewCommentCount(int uid);

    int fineNewChatCount(int uid);

    int findNewActiveCount(int uid);


    int deliverAllAtme(Integer uid);

    int deliverAllComment(Integer uid);

    int deliverAllActive(Integer uid);

    int deliverAllMessage(Integer uid, Integer touid);
}
