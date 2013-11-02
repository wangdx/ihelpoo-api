package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.StreamDao;
import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.StreamResult;
import com.ihelpoo.api.model.UserWordResult;
import com.ihelpoo.api.model.entity.VTweetStreamEntity;
import com.ihelpoo.api.model.entity.VUserDetailEntity;
import com.ihelpoo.api.model.obj.Active;
import com.ihelpoo.api.model.obj.Actives;
import com.ihelpoo.api.model.entity.IRecordSayEntity;
import com.ihelpoo.api.model.entity.IUserPriorityEntity;
import com.ihelpoo.api.model.obj.Result;
import com.ihelpoo.api.service.base.RecordService;
import com.ihelpoo.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class StreamService extends RecordService {
    @Autowired
    StreamDao streamDao;

    @Autowired
    UserDao userDao;

    //TODO use hisName to find
    public UserWordResult pullUserActiveBy(Integer hisUid, String hisName, Integer uid, Integer pageIndex, Integer pageSize) {
        pageIndex = pageIndex == null ? Constant.DEFAULT_PAGE_INDEX : pageIndex;
        pageSize = pageSize == null ? Constant.DEFAULT_PAGE_SIZE : pageSize;
        UserWordResult uar = new UserWordResult();
        Result result = new Result();
        result.setErrorCode("0");


        VUserDetailEntity entity = null;
        try {
            entity = userDao.findUserDetailById(hisUid);
        } catch (EmptyResultDataAccessException e) {
            result.setErrorMessage("没有该用户的详细信息");
            uar.result = result;
            return uar;
        }
        com.ihelpoo.api.model.common.User user = new com.ihelpoo.api.model.common.User();
        user.academy_name = entity.getAcademyName();
        user.active_credits = entity.getActiveCredits();
        user.birthday = entity.getBirthday();
        user.create_time = (new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date((long) (entity.getCreateTime() * 1000)));
        user.school_domain = entity.getSchoolDomain();
        user.enrol_time = entity.getEnrolTime();
        user.followers_count = entity.getFollowersCount();
        user.friends_count = entity.getFriendsCount();
        user.avatar_type = entity.getAvatarType();
        user.avatar_url = convertToAvatarUrl(entity.getAvatarUrl(), entity.getUid(), false);
        user.avatar_preview = convertToAvatarUrl(entity.getAvatarUrl(), entity.getUid(), true);
        user.self_intro = entity.getSelfIntro();
        user.login_time = (new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date((long) (entity.getLoginTime() * 1000)));
        user.login_days = entity.getLoginDays();
        user.major_name = entity.getMajorName();
        user.nickname = entity.getNickname();
        user.online_status = entity.getOnlineStatus();
        user.real_name = entity.getRealName();
        user.school_id = String.valueOf(entity.getSchoolId());
        user.school_name = entity.getSchoolName();
        user.gender = entity.getGender();
        user.web_theme = entity.getWebTheme();
        user.email_verified = entity.getEmailVerified();
        user.user_type = entity.getUserType();
        user.uid = entity.getUid();
        user.level = convertToLevel(entity.getActiveCredits());
        user.relation = convertToRelation(uid, hisUid);


        List<IRecordSayEntity> recordSayEntities = streamDao.findTweetsBy(hisUid, pageIndex, pageSize);
        Actives actives = new Actives();
        List<Active> activeList = new ArrayList<Active>();
        for (IRecordSayEntity recordSayEntity : recordSayEntities) {
            String firstImgUrl = convertToImageUrl(recordSayEntity.getSid());
            Active active = new Active();
            active.sid = recordSayEntity.getSid();
            active.iconUrl = convertToAvatarUrl(entity.getAvatarUrl(), hisUid, false);
            active.nickname = entity.getNickname();
            active.uid = hisUid;
            active.catalog = 4;
            active.objecttype = 3;
            active.objectcatalog = 0;
            active.objecttitle = "孤独";
            active.from = recordSayEntity.getFrom();
            active.objectID = 136727;

            active.content = recordSayEntity.getContent();

            active.commentCo = recordSayEntity.getCommentCo() == null ? 0 : recordSayEntity.getCommentCo();
            active.time = (new java.text.SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss")).format(new Date((long) (recordSayEntity.getTime().floatValue() * 1000)));
            active.image =  firstImgUrl.replace("recordsay", "thumb_recordsay");
            active.imgBig = firstImgUrl;

            active.academy = entity.getAcademyName();
            active.authorType = convertToType(entity.getUserType(), entity.getEnrolTime());
            active.rank = String.valueOf(convertToRank(entity.getActiveCredits()));
            active.authorGossip = convertToGossip(entity.getGender(), entity.getBirthday());
            active.diffusionCo = recordSayEntity.getDiffusionCo() == null ? 0 : recordSayEntity.getDiffusionCo();
            active.online = 0;
            activeList.add(active);
        }

        result.setErrorCode("1");
        result.setErrorMessage("成功");
        uar.result = result;
        actives.setActive(activeList);
        uar.actives = actives;
        uar.notice = getNotice(uid);
        uar.page_size = recordSayEntities.size();
        uar.user = user;
        return uar;
    }

    // 0 没圈没屏蔽，1：圈， 2：屏蔽 3: 自己
    private int convertToRelation(Integer uid, Integer hisUid) {
        if (uid.equals(hisUid)) {
            return 3;
        }
        int relation = 0;
        boolean friend = false, shield = false;
        try {
            userDao.findPrioritiesBy(uid, hisUid);
            friend = true;
        } catch (EmptyResultDataAccessException e) {
        }
        try {
            userDao.findShieldBy(uid, hisUid);
            shield = true;
        } catch (EmptyResultDataAccessException e) {
        }
        if (!friend && !shield) {
            relation = 0;
        } else if (friend && !shield) {
            relation = 1;
        } else if (shield && !friend) {
            relation = 2;
        } else {
            relation = Integer.MAX_VALUE;
        }
        return relation;
    }

    public StreamResult pullBy(int uid, int catalog, int schoolId, int pageIndex, int pageSize) {
        List<IUserPriorityEntity> priorityEntities = userDao.findAllPrioritiesByUid(uid, 0, Integer.MAX_VALUE);
        StringBuilder pids = new StringBuilder();
        StringBuilder sids = new StringBuilder();
        if (!priorityEntities.isEmpty()) {
            for (IUserPriorityEntity userPriorityEntity : priorityEntities) {
                if (null != userPriorityEntity.getPid()) {
                    pids.append(userPriorityEntity.getPid()).append(",");
                } else if (null != userPriorityEntity.getSid()) {
                    sids.append(userPriorityEntity.getSid()).append(",");
                }
            }
            if (pids.length() > 0)
                pids.deleteCharAt(pids.length() - 1);
            if (sids.length() > 0)
                sids.deleteCharAt(sids.length() - 1);
        }
        long t = System.currentTimeMillis();
        List<VTweetStreamEntity> tweets = streamDao.findAllTweetsBy(catalog, pids, sids, schoolId, pageIndex, pageSize);
        List<Active> actives = new ArrayList<Active>();
        for (VTweetStreamEntity tweet : tweets) {
            Active active = new Active();
            active.academy = "[" + tweet.getShowMajorName() + "]";
            active.rank = String.valueOf(convertToRank(tweet.getActive()));
            active.online = tweet.getOnline() == null ? 0 : Integer.parseInt(tweet.getOnline().trim());
            active.iconUrl = convertToAvatarUrl(tweet.getIconUrl(), tweet.getUid(), false);
            active.from = tweet.getFrom();
            active.catalog = 4;                     //my space in android
            active.commentCo = tweet.getCommentCo() == null ? 0 : tweet.getCommentCo();
            active.content = tweet.getContent();
            active.time = (new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date((long) (tweet.getTime().floatValue() * 1000)));
            active.diffusionCo = tweet.getDiffusionCo() == null ? 0 : tweet.getDiffusionCo();
            active.authorGossip = convertToGossip(tweet.getSex(), tweet.getBirthday());
            active.image = convertToImageUrl(tweet.getSid());
            active.nickname = tweet.getNickname();
            active.sid = tweet.getSid();
            active.authorType = convertToType(tweet.getType(), tweet.getEnteryear());
            active.uid = tweet.getUid();

            actives.add(active);
        }

        StreamResult sr = new StreamResult();
        Actives activesWrapper = new Actives();
        activesWrapper.setActive(actives);
        sr.setActiveCount(0);
        sr.setPagesize(pageSize);
        sr.setNotice(getNotice(uid));
        sr.setActivies(activesWrapper);
        return sr;
    }

}
