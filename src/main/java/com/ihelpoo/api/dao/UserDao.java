package com.ihelpoo.api.dao;

import com.ihelpoo.api.OoUser;
import com.ihelpoo.api.model.UserList;
import com.ihelpoo.api.model.entity.*;

import java.util.List;
import java.util.Set;

/**
 * @author dongxu.wang@acm.org
 */
public interface UserDao {
    UserList getUserList(int grade);

    UserList getUserList(OoUser.UserDimension userDimension);

    IUserLoginEntity findByUserName(String username);

    IUserLoginEntity findUserById(int uid);

    IUserStatusEntity findUserStatusById(int uid);

    int updateLogin(String ip, long loginTime, String status, Integer lastLoginTime, int uid);

    int updateLogin(int uid, int newUserActive, int count);

    int updateActive(int uid, int newUserActive);

    int saveMsgActive(String way, int uid, int total, int change, String reason);

    int updateStatus(int uid, int activeFlag, int clear);

    List<IUserLoginEntity> findUsersBy(Set<Integer> uids);

    int saveOutimg(int uid, long t, String filePath);

    int saveSay(int uid, long t, String msg, String imageIds, Integer reward, String by, int schoolId);

    VUserDetailEntity findUserDetailById(int uid);

    int updateAvatar(int uid, int iconFlag, String iconUrl);

    int addImage(int uid, String s);

    int addSay(int uid, int sayType, String body, int imgId, long t, String from, int schoolId);

    int addDynamic(int dynId, String changeicon);

    int findSizeOfAlbum(int uid);

    int addImageToAlbum(int uid, int schoolId, String s, long size, long t);

    int updateStatus(int uid, int schoolId);

    List<IUserPriorityEntity> findAllPrioritiesByUid(int uid);

    List<IUserPriorityEntity> findFollowersBy(int uid);

    List<VLoginRecordEntity> findAllActivesBy(int uid, int pageIndex, int pageSize);

    int saveUser(String mobile, String pwd, String nickname, Integer school, String skin, Integer lastLoginTime);

    List<ISchoolInfoEntity> fetchAllSchools();

    IUserLoginEntity saveUser(String mobile, String encrypt, String nickname, Integer school, Integer time);

    int saveStatus(int uid, int recordLimit);

    IUserPriorityEntity findPrioritiesBy(int uid, int hisuid);

    IUserPriorityEntity findShieldBy(int uid, int hisuid);

    int savePriority(int uid, int hisuid, Integer type);

    IUserInfoEntity findUserInfoBy(int uid);

    int updateRelation(int uid, int hisuid, boolean isFollow);

    int updateFollowActive(int hisuid, int uid, int active, String reason);

    int deletePriority(int uid, int hisuid);
}
