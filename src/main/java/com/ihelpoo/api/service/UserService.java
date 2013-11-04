package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.FriendsResult;
import com.ihelpoo.api.model.GenericResult;
import com.ihelpoo.api.model.MessageResult;
import com.ihelpoo.api.model.UserResult;
import com.ihelpoo.api.model.common.User;
import com.ihelpoo.api.model.entity.IUserPriorityEntity;
import com.ihelpoo.api.model.entity.VUserDetailEntity;
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
import java.util.*;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class UserService extends RecordService {
    public static final int TO_UNFOLLOW = 0x00;
    public static final int TO_FOLLOW = 0x01;
    public static final int TO_UNSHIELD = 0x02;
    public static final int TO_SHIELD = 0x03;

    public final static int TYPE_FOLLOWER = 0x00;
    public final static int TYPE_FRIEND = 0x01;

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

    public static void main(String[] args) {
        Integer time =  1383496809;
        System.out.println((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date((long) time * 1000)));
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

        MessageResult.Messages newslist = new MessageResult.Messages(list);
        MessageResult mr = new MessageResult();
        mr.page_size = entities.size();
        mr.notice = getNotice(uid);
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
            case TO_UNFOLLOW:
                return unfollow(hisuid, uid, genericResult, result);
            case TO_FOLLOW:
                return follow(hisuid, uid, genericResult, result);
            case TO_UNSHIELD:
                return unshield(hisuid, uid, genericResult, result);
            case TO_SHIELD:
                return shield(hisuid, uid, genericResult, result);
            default:
                result.setErrorMessage("updateRelation参数relation错误");
                return genericResult;
        }
    }

    private GenericResult unshield(int hisuid, int uid, GenericResult genericResult, Result result) { //TODO
        return null;
    }

    private GenericResult shield(int hisuid, int uid, GenericResult genericResult, Result result) {
        return null;
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

    public UserResult getUserDetail(int uid) {

        UserResult userResult = new UserResult();
        Result result = new Result();
        result.setErrorCode("0");
        if(uid < 10000){
            result.setErrorMessage("用户未登录");
            userResult.result = result;
            return userResult;
        }

        VUserDetailEntity entity = userDao.findUserDetailById(uid);
        User user = new User();
        user.avatar_url = convertToAvatarUrl(entity.getAvatarUrl(), entity.getUid(), false);
        user.email = entity.getEmail();
        user.nickname = entity.getNickname();
        user.gender = entity.getGender();
        user.enrol_time = entity.getEnrolTime();
        user.school_name = entity.getSchoolName();
        user.academy_name = entity.getAcademyName();
        user.major_name = entity.getMajorName();
        user.dorm_name = entity.getDormName();
        user.self_intro = entity.getSelfIntro();
        user.birthday = entity.getBirthday();
        user.followers_count = entity.getFollowersCount();
        user.friends_count = entity.getFriendsCount();

        user.uid = entity.getUid();
        user.level = convertToLevel(entity.getActiveCredits());
        user.email_verified = entity.getEmailVerified();
        user.user_type = entity.getUserType();
        user.login_days = entity.getLoginDays();
        user.active_credits = entity.getActiveCredits();
        user.web_theme = entity.getWebTheme();
        user.create_time = String.valueOf(entity.getCreateTime());
        user.login_time = String.valueOf(entity.getLoginTime());
        user.last_login = entity.getLastLogin();

        result.setErrorCode("1");
        result.setErrorMessage("");
        userResult.result = result;
        userResult.user = user;
        userResult.notice = new Notice();
        return userResult;
    }

    public FriendsResult getFriends(Integer uid, Integer relation, Integer pageSize, Integer pageIndex) {

        FriendsResult foResult = new FriendsResult();
        Result result = new Result();
        result.setErrorCode("0");
        if (relation == null || (relation > 1 || relation < 0)) {
            result.setErrorMessage("缺少参数");
            foResult.result = result;
            return foResult;
        }

        return getMyFriends(foResult, getFriendIds(uid, relation, pageIndex, pageSize));

    }

    private Set<Integer> getFriendIds(Integer uid, int relation, Integer pageIndex, Integer pageSize) {
        Set<Integer> ids = new HashSet<Integer>();
        switch (relation) {
            case TYPE_FRIEND:
                List<IUserPriorityEntity> entities = userDao.findAllPrioritiesByUid(uid, pageIndex, pageSize);
                for (IUserPriorityEntity priorityEntity : entities) {
                    ids.add(priorityEntity.getPid());
                }
                break;
            case TYPE_FOLLOWER:
                entities = userDao.findFollowersBy(uid, pageIndex, pageSize);
                for (IUserPriorityEntity priorityEntity : entities) {
                    ids.add(priorityEntity.getUid());
                }
                break;
        }
        return ids;
    }

    private FriendsResult getMyFriends(FriendsResult foResult, Set<Integer> ids) {

        FriendsResult.Friends friends = new FriendsResult.Friends();
        List<User> users = new ArrayList<User>();

        List<IUserLoginEntity> entities = userDao.findUsersBy(ids);
        for (IUserLoginEntity entity : entities) {
            User user = new User();
            user.nickname = entity.getNickname();
            user.enrol_time = entity.getEnteryear();
            user.active_credits = entity.getActive() == null ? 0 : entity.getActive();
            user.level = convertToLevel(entity.getActive());
            user.gender = entity.getSex();
            user.avatar_url = convertToAvatarUrl(entity.getIconUrl(), entity.getUid(), false);
            user.uid = entity.getUid();
            user.online_status = entity.getOnline();
            user.school_id = String.valueOf(entity.getSchool());
            user.uid = entity.getUid();
            user.user_type = entity.getType();
            users.add(user);
        }

        Result result = new Result();
        result.setErrorCode("1");
        result.setErrorMessage("成功");
        friends.friend = users;
        foResult.friends = friends;
        foResult.result = result;
        foResult.notice = new Notice();
        return foResult;
    }

    public GenericResult updateNickname(Integer uid, String newNickname) {
        GenericResult genericResult = new GenericResult();
        genericResult.setNotice(new Notice());
        Result result = new Result();
        genericResult.setResult(result);
        result.setErrorCode("0");
        try {
            userDao.updateUserLogin(uid, "nickname", newNickname);
        } catch (Exception e) {
            result.setErrorMessage("修改昵称失败");
            logger.error("修改昵称失败: ", e);
            genericResult.setResult(result);
            return genericResult;
        }
        result.setErrorCode("1");
        result.setErrorMessage("昵称修改成功");
        genericResult.setResult(result);
        return genericResult;
    }

    public GenericResult updateGender(Integer uid, String newGender) {
        GenericResult genericResult = new GenericResult();
        genericResult.setNotice(new Notice());
        Result result = new Result();
        genericResult.setResult(result);
        result.setErrorCode("0");
        try {
            userDao.updateUserLogin(uid, "sex", newGender);
        } catch (Exception e) {
            result.setErrorMessage("修改性别失败");
            logger.error("修改性别失败: ", e);
            genericResult.setResult(result);
            return genericResult;
        }
        result.setErrorCode("1");
        result.setErrorMessage("性别修改成功");
        genericResult.setResult(result);
        return genericResult;
    }

    public GenericResult updateEnrol(Integer uid, String newEnrol) {
        GenericResult genericResult = new GenericResult();
        genericResult.setNotice(new Notice());
        Result result = new Result();
        genericResult.setResult(result);
        result.setErrorCode("0");
        try {
            userDao.updateUserLogin(uid, "enteryear", newEnrol);
        } catch (Exception e) {
            result.setErrorMessage("修改失败");
            logger.error("修改入学年份失败: ", e);
            genericResult.setResult(result);
            return genericResult;
        }
        result.setErrorCode("1");
        result.setErrorMessage("修改成功");
        genericResult.setResult(result);
        return genericResult;
    }

    public GenericResult updateIntro(Integer uid, String newIntro) {
        GenericResult genericResult = new GenericResult();
        genericResult.setNotice(new Notice());
        Result result = new Result();
        genericResult.setResult(result);
        result.setErrorCode("0");
        try {
            userDao.updateUserInfo(uid, "introduction", newIntro);
        } catch (Exception e) {
            result.setErrorMessage("修改失败");
            logger.error("修改失败: ", e);
            genericResult.setResult(result);
            return genericResult;
        }
        result.setErrorCode("1");
        result.setErrorMessage("修改成功");
        genericResult.setResult(result);
        return genericResult;
    }

    @Transactional
    public GenericResult updateMajor(Integer uid, Integer schoolId, Integer academyId, Integer majorId, Integer dormId) {
        GenericResult genericResult = new GenericResult();
        genericResult.setNotice(new Notice());
        Result result = new Result();
        genericResult.setResult(result);
        result.setErrorCode("0");
        try {
            userDao.updateUserLogin(uid, "school", schoolId);
            userDao.updateUserInfo(uid, new String[]{"academy_op", "specialty_op", "dormitory_op" }, new Object[]{academyId, majorId, dormId});
        } catch (Exception e) {
            result.setErrorMessage("修改失败");
            logger.error("修改失败: ", e);
            genericResult.setResult(result);
            return genericResult;
        }
        result.setErrorCode("1");
        result.setErrorMessage("修改成功");
        genericResult.setResult(result);
        return genericResult;
    }
}
