package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.OoUser;
import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.UserList;
import com.ihelpoo.api.model.entity.*;
import com.ihelpoo.api.service.RegisterService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @author: dongxu.wang@acm.org
 */
public class UserDaoImpl extends NamedParameterJdbcDaoSupport implements UserDao {


    @Override
    public List<IUserLoginEntity> findUsersBy(Set<Integer> uids) {
        if (uids == null || uids.size() < 1) {
            return Collections.emptyList();
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", uids);
        return getNamedParameterJdbcTemplate().query(" SELECT * FROM i_user_login WHERE uid IN (:ids) ", parameters, new BeanPropertyRowMapper<IUserLoginEntity>(IUserLoginEntity.class));
    }

    @Override
    public int saveOutimg(final int uid, final long t, final String filePath) {
        final String sql = "insert into i_record_outimg (uid, rpath, time) values(?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, uid);
                ps.setString(2, filePath);
                ps.setLong(3, t);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public int saveSay(final int uid, final long lastCommentTime, final String msg, final String imageIds, final Integer reward, final String by, final int schoolId) {
        final String[] from = new String[1];
        from[0] = by;
        if (from[0] != null && from[0].contains(" ") && from[0].split(" ").length > 3) {
            from[0] = from[0].substring(0, nthOccurrence(by, ' ', 2));
        }
        final String sql = "insert into i_record_say (uid, say_type, content, image, url, authority, `time`, last_comment_ti, `from`, school_id) values(?,?,?,?,'','0',unix_timestamp(),?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(sql, new String[]{"sid"});
                ps.setInt(1, uid);
                ps.setString(2, getSayType(reward));
                ps.setString(3, msg);
                ps.setString(4, imageIds);
                ps.setInt(5, (int) lastCommentTime);
                ps.setString(6, from[0]);
                ps.setInt(7, schoolId);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    private String getSayType(Integer reward) {
        return isHelp(reward) ? "1" : "0";
    }

    private boolean isHelp(Integer reward) {
        return reward != null && reward >= 0;
    }

    @Override
    public VUserDetailEntity findUserDetailById(int uid) {
        String sql = "select\n" +
                "        a.uid as uid,\n" +
                "        a.nickname as nickname,\n" +
                "        a.sex as gender,\n" +
                "        a.email as email,\n" +
                "        a.status as email_verified,\n" +
                "        a.birthday as birthday,\n" +
                "        a.enteryear as enrol_time,\n" +
                "        a.type as user_type,\n" +
                "        a.ip as ip_addr,\n" +
                "        a.logintime as login_time,\n" +
                "        a.lastlogintime as last_login,\n" +
                "        a.creat_ti as create_time,\n" +
                "        a.login_days_co as login_days,\n" +
                "        a.online as online_status,\n" +
                "        a.active as active_credits,\n" +
                "        a.icon_fl as avatar_type,\n" +
                "        a.icon_url as avatar_url,\n" +
                "        a.skin as web_theme,\n" +
                "        b.introduction as self_intro,\n" +
                "        b.realname as real_name,\n" +
                "        b.fans as followers_count,\n" +
                "        b.follow as friends_count,\n" +
                "        c.id as school_id,\n" +
                "        c.domain as school_domain, \n" +
                "        IF(c.school IS NULL, '', c.school) as school_name,\n" +
                "        IF(d.name IS NULL, '', d.name) as academy_name,\n" +
                "        IF(e.name IS NULL, '', e.name) as major_name,\n" +
                "        IF(f.name IS NULL, '', f.name) as dorm_name\n" +
                "    from\n" +
                "        i_user_login a\n" +
                "            left join\n" +
                "        i_user_info b ON a.uid = b.uid\n" +
                "            left join\n" +
                "        i_school_info c ON a.school = c.id\n" +
                "            left join\n" +
                "        i_op_academy d ON d.id = b.academy_op\n" +
                "            left join\n" +
                "        i_op_specialty e ON e.id = b.specialty_op\n" +
                "            left join\n" +
                "        i_op_dormitory f ON f.id = b.dormitory_op" +
                "\twhere a.uid=?";
        return getJdbcTemplate().queryForObject(sql, new Object[]{uid}, new BeanPropertyRowMapper<VUserDetailEntity>(VUserDetailEntity.class));
    }

    @Override
    public int updateAvatar(int uid, int iconFlag, String iconUrl) {
        final String sql = " UPDATE i_user_login SET icon_fl=?, icon_url=? WHERE uid=? ";
        return getJdbcTemplate().update(sql, iconFlag, iconUrl, uid);
    }

    @Override
    public int addImage(int uid, String avatarPath) {
        String sql = " INSERT INTO i_record_outimg(uid, rpath, `time`) VALUES(?,?, unix_timestamp()) ";
        return getJdbcTemplate().update(sql, new Object[]{uid, avatarPath});
    }

    @Override
    public int addSay(final int uid, final int sayType, final String body, final int imgId, final long t, final String from, final int schoolId) {
        final String sql = "insert into i_record_say (uid, say_type, content, image, `time`, `from`, school_id) values(?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(sql, new String[]{"sid"});
                ps.setInt(1, uid);
                ps.setInt(2, sayType);
                ps.setString(3, body);
                ps.setString(4, imgId + "");
                ps.setLong(5, t);
                ps.setString(6, from);
                ps.setInt(7, schoolId);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public int addDynamic(final int sayId, final String dType) {
        final String sql = "insert into i_record_dynamic (sid, `type`) values(?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, sayId);
                ps.setString(2, dType);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public int findSizeOfAlbum(int uid) {
        final String sql = " SELECT sum(size) FROM i_user_album WHERE uid=? ";
        return getJdbcTemplate().queryForObject(sql, Integer.class, uid);
    }

    @Override
    public int addImageToAlbum(final int uid, final int type, final String imgUrl, final long size, final long t) {
        final String sql = " INSERT INTO i_user_album (uid, `type`, url, `size`, `time`, foreignid) VALUES(?,?,?,?,?, 0) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, uid);
                ps.setString(2, type + "");
                ps.setString(3, imgUrl);
                ps.setLong(4, size);
                ps.setLong(5, t);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public int incStatus(int uid, int offset) {
        final String sql = " UPDATE i_user_status SET active_s_limit=active_s_limit + ?  WHERE uid=? ";
        return getJdbcTemplate().update(sql, offset, uid);
    }


    @Override
    public int saveStatus(int uid, int recordLimit) {
        final String sql = " INSERT INTO i_user_status (uid,record_limit,acquire_seconds,acquire_times) VALUES(?,?,0,0) ";
        return getJdbcTemplate().update(sql, uid, recordLimit);
    }

    @Override
    public IUserPriorityEntity findPrioritiesBy(int uid, int hisuid) {
        final String sql = " SELECT * FROM i_user_priority WHERE uid=? AND pid=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{uid, hisuid}, new BeanPropertyRowMapper<IUserPriorityEntity>(IUserPriorityEntity.class));
    }

    @Override
    public IUserPriorityEntity findShieldBy(int uid, int hisuid) {
        final String sql = " SELECT * FROM i_user_priority WHERE uid=? AND sid=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{uid, hisuid}, new BeanPropertyRowMapper<IUserPriorityEntity>(IUserPriorityEntity.class));
    }

    @Override
    public int savePriority(final int uid, final int hisuid, final Integer type) {
        final String sql = " INSERT INTO i_user_priority (uid, pid, pid_type,group_id,`time`) VALUES(?,?,?,0,unix_timestamp()) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, uid);
                ps.setInt(2, hisuid);
                ps.setInt(3, type);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public IUserInfoEntity findUserInfoBy(int uid) {
        final String sql = " SELECT * FROM i_user_info WHERE uid=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{uid}, new BeanPropertyRowMapper<IUserInfoEntity>(IUserInfoEntity.class));
    }

    @Override
    public int updateRelation(int uid, int hisuid, boolean isFollow) {
        if (isFollow) {
            final String sql1 = " UPDATE i_user_info SET fans = fans + 1 WHERE uid=? ";
            final String sql2 = " UPDATE i_user_info SET follow = follow + 1 WHERE uid=? ";
            getJdbcTemplate().update(sql1, hisuid);
            getJdbcTemplate().update(sql2, uid);
        } else {
            final String sql3 = " UPDATE i_user_info SET fans = IF(fans -1 < 0, 0, fans-1) WHERE uid=? ";
            final String sql4 = " UPDATE i_user_info SET follow = IF(follow -1 < 0, 0, follow-1) WHERE uid=? ";
            getJdbcTemplate().update(sql3, hisuid);
            getJdbcTemplate().update(sql4, uid);
        }
        return 2;
    }


    @Override
    public int updateFollowActive(final int hisuid, final int uid, final int active, final String reason) {

        final String sql = " SELECT * from i_msg_active WHERE uid=? AND `reason` LIKE '%" + uid + "%' ";
        try {
            getJdbcTemplate().queryForObject(sql, new Object[]{hisuid}, new BeanPropertyRowMapper<IMsgActiveEntity>(IMsgActiveEntity.class));
        } catch (EmptyResultDataAccessException e) {
            final String sql2 = " INSERT INTO i_msg_active (uid, total, `change`,way,reason,deliver,`time`) VALUES (?,?,?,?,?,?,unix_timestamp()) ";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(sql2, new String[]{"id"});
                    ps.setInt(1, hisuid);
                    ps.setInt(2, active);
                    ps.setInt(3, 5);
                    ps.setString(4, "add");
                    ps.setString(5, reason);
                    ps.setString(6, "0");
                    return ps;
                }
            }, keyHolder);
            return keyHolder.getKey().intValue();
        }
        return 0;
    }

    @Override
    public int deletePriority(int uid, int hisuid) {
        final String sql = " DELETE FROM i_user_priority WHERE uid=? AND pid=? ";
        return getJdbcTemplate().update(sql, new Object[]{uid, hisuid});
    }

    @Override
    public int incUserStatusIfLessThan(String column, int limit, int uid) {
        final String sql = " update i_user_status set " + column + "=" + column + " + 1 " + " where uid=? and " + column + " < ? ";
        return getJdbcTemplate().update(sql, new Object[]{uid, limit});
    }

    @Override
    public int incUserLoginActive(int uid, int amount) {
        String sqlUpdateUser = " update i_user_login set active=active + ? where uid=? ";
        return getJdbcTemplate().update(sqlUpdateUser, new Object[]{amount, uid});
    }


    @Override
    public UserList getUserList(int grade) {
        UserList userList = new UserList();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int enrolledYear = currentMonth > Calendar.AUGUST ? currentYear - grade + 1 : currentYear - grade;

        String sql = "SELECT * FROM i_user_login WHERE enteryear = ? and type = 1 LIMIT 10";
        List<IUserLoginEntity> users = getJdbcTemplate().query(sql, new Object[]{enrolledYear}, new BeanPropertyRowMapper<IUserLoginEntity>(IUserLoginEntity.class));
        userList.setUsers(users);
        return userList;
    }

    @Override
    public UserList getUserList(OoUser.UserDimension userDimension) {
        UserList userList = new UserList();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int grade = (Integer) userDimension.getAttribute("grade");
        int enrolledYear = currentMonth > Calendar.AUGUST ? currentYear - grade + 1 : currentYear - grade;

        String sql = "SELECT uid, status, email, nickname, sex, birthday, enteryear, type, priority, ip, logintime, lastlogintime, creat_ti, login_days_co, online, coins, active, icon_fl, icon_url, skin, school " +
                " FROM i_user_login user " +
                " WHERE user.enteryear = ? and user.type = 1 " +
                " ORDER BY user.icon_fl DESC , user.online DESC " +
                " LIMIT ? OFFSET ?";
        List<IUserLoginEntity> users = getJdbcTemplate().query(
                sql,
                new Object[]{enrolledYear, userDimension.getAttribute("limit"), userDimension.getAttribute("offset")},
                new BeanPropertyRowMapper<IUserLoginEntity>(IUserLoginEntity.class));
        userList.setUsers(users);
        return userList;
    }

    @Override
    public IUserLoginEntity findByAccount(String username) {
        String sql = " SELECT * FROM i_user_login WHERE email=? ";
        String[] params = new String[]{username};
        IUserLoginEntity userLoginEntity = null;
        userLoginEntity = getJdbcTemplate().queryForObject(sql, params, new BeanPropertyRowMapper<IUserLoginEntity>(IUserLoginEntity.class));
        return userLoginEntity;
    }

    @Override
    public IUserLoginEntity findUserById(int uid) {
        String sql = " SELECT * FROM i_user_login WHERE uid=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{uid}, new BeanPropertyRowMapper<IUserLoginEntity>(IUserLoginEntity.class));
    }

    @Override
    public IUserStatusEntity findUserStatusById(int uid) {
        final String sql = " SELECT * FROM i_user_status WHERE uid=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{uid}, new BeanPropertyRowMapper<IUserStatusEntity>(IUserStatusEntity.class));
    }

    @Override
    public int updateLogin(int uid, int newUserActive, int count) {
        final String sql = " UPDATE i_user_login SET active=?, login_days_co=? WHERE uid=? ";
        return getJdbcTemplate().update(sql, newUserActive, count, uid);
    }

    @Override
    public int updateLogin(String ip, long loginTime, String status, Integer lastLoginTime, int uid) {
        final String sql = " UPDATE i_user_login SET ip=?, logintime=?, online=?, lastlogintime=? WHERE uid=? ";
        return getJdbcTemplate().update(sql, ip, loginTime, status, lastLoginTime, uid);
    }

    @Override
    public int updateActive(int uid, int newUserActive) {
        final String sql = " UPDATE i_user_login SET active=? WHERE uid=? ";
        return getJdbcTemplate().update(sql, newUserActive, uid);
    }

    @Override
    public int saveMsgActive(String way, int uid, int total, int change, String reason) {
        String sql = " INSERT INTO i_msg_active(way, `time`, deliver, uid, total, `change`, reason) VALUES(?, unix_timestamp(), '0', ?,?,?,?) ";
        return getJdbcTemplate().update(sql, new Object[]{way, uid, total, change, reason});
    }

    @Override
    public int updateStatus(int uid, int activeFlag, int clear) {
        String otherFields = " ,total_active_ti=0, active_s_limit=0, active_c_limit=0 ";
        if (clear != 0) {
            otherFields = "";
        }
        final String sql = " UPDATE i_user_status SET active_flag=? " + otherFields + " WHERE uid=? ";
        return getJdbcTemplate().update(sql, activeFlag, uid);
    }


    @Override
    public List<IUserPriorityEntity> findAllPrioritiesByUid(int uid, Integer pageIndex, Integer pageSize) {
        String sql = " SELECT * FROM i_user_priority WHERE uid=?  ORDER BY `time` DESC limit ? offset ?  ";
        return getJdbcTemplate().query(sql, new Object[]{uid, pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<IUserPriorityEntity>(IUserPriorityEntity.class));
    }

    @Override
    public List<IUserPriorityEntity> findFollowersBy(int uid, Integer pageIndex, Integer pageSize) {
        String sql = " SELECT * FROM i_user_priority WHERE pid=?  ORDER BY `time` DESC limit ? offset ?  ";
        return getJdbcTemplate().query(sql, new Object[]{uid, pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<IUserPriorityEntity>(IUserPriorityEntity.class));
    }

    @Override
    public List<VLoginRecordEntity> findAllActivesBy(int uid, int pageIndex, int pageSize) {
        final String sql = " select sid,i_user_login.uid,say_type,content,image,url,comment_co,diffusion_co,hit_co,`time`,`from`,last_comment_ti,nickname,sex,birthday,enteryear,`type`,online,active,icon_url from i_record_say\n" +
                " left join i_user_login on i_record_say.uid = i_user_login.uid\n" +
                " where i_user_login.uid=?\n" +
                " order by i_record_say.time DESC ";
        return getJdbcTemplate().query(sql, new Object[]{uid}, new BeanPropertyRowMapper<VLoginRecordEntity>(VLoginRecordEntity.class));
    }

    @Override
    public int saveUser(final String mobile, final String encryptedPwd, final String nickname, final Integer school, final String skin, final Integer registerTime) {
        final String sql = " INSERT INTO i_user_login (email, password, nickname, school, skin, logintime, lastlogintime, creat_ti, status, `type`) VALUES(?,?,?,?,?,?,?,?,2,1) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, mobile);
                ps.setString(2, encryptedPwd);
                ps.setString(3, nickname);
                ps.setInt(4, school);
                ps.setString(5, skin);
                ps.setInt(6, registerTime);
                ps.setInt(7, registerTime);
                ps.setInt(8, registerTime);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }


    @Override
    public int saveUserInfo(int uid, int academyId, int majorId, int dormId, String weibo) {
        String sql = " INSERT INTO i_user_info (uid, academy_op, specialty_op, dormitory_op, dynamic, fans, follow) VALUES(?,?,?,?,1,0,0) ";
        return getJdbcTemplate().update(sql, new Object[]{uid, academyId, majorId, dormId});
    }

    @Override
    public RegisterService.DefaultMajor fetchDefaultMajor(Integer school) {
        String sql = "select \n" +
                "(select id from i_op_academy where id=?) as school_id ,\n" +
                "(select id from i_op_academy where school=school_id limit 1) as academy_id ,\n" +
                "(select id from i_op_specialty where school=school_id and academy=academy_id limit 1) as major_id,\n" +
                "(select id from i_op_dormitory where school=school_id limit 1) as dorm_id";// exclude test school by id!=35
        return getJdbcTemplate().queryForObject(sql, new BeanPropertyRowMapper<RegisterService.DefaultMajor>(RegisterService.DefaultMajor.class), school);
    }

    @Override
    public IUserLoginWbEntity findByThirdAccount(String thirdUid, String thirdType) {
        String tableName = "weibo".equals(thirdType) ? "i_user_login_wb" : "i_user_login_qq";
        String columnName = "weibo".equals(thirdType) ? "weibo_uid" : "qq_uid";
        final String sql = " SELECT uid, " + columnName + " AS third_uid FROM " + tableName + " WHERE " + columnName + "=?";
        return getJdbcTemplate().queryForObject(sql, new BeanPropertyRowMapper<IUserLoginWbEntity>(IUserLoginWbEntity.class), thirdUid);
    }

    @Override
    public int saveUserThird(String thirdUid, int uid, String thirdType) {
        String tableName = "weibo".equals(thirdType) ? "i_user_login_wb" : "i_user_login_qq";
        String columnName = "weibo".equals(thirdType) ? "weibo_uid" : "qq_uid";
        final String sql = " INSERT INTO " + tableName + "(uid, " + columnName + ") values(?,?)";
        return getJdbcTemplate().update(sql, new Object[]{uid, thirdUid});
    }

    @Override
    public List<VUserBuddiesEntity> findAllBuddies(Integer uid, int relation, Integer pageIndex, Integer pageSize) {
        String primaryUid = relation == 0 ? "pid" : "uid";
        String buddiesUid = relation == 0 ? "uid" : "pid";
        final String sql = "select a.id, a.pid, a.pid_type, a.group_id, a.sid, a.`time` as create_time, b.* " +
                " from i_user_priority a" +
                " inner join i_user_login b on b.uid = a." + buddiesUid +
                " where a." + primaryUid + "=? ORDER BY `time` DESC limit ? offset ? ";
        return getJdbcTemplate().query(sql, new Object[]{uid, pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<VUserBuddiesEntity>(VUserBuddiesEntity.class));
    }

    @Override
    public int updateUserStatus(int uid) {
        final String sql = " update i_user_status \n" +
                "set total_active_ti=IF(unix_timestamp()-last_active_ti > 999, total_active_ti, total_active_ti + unix_timestamp()-last_active_ti),\n" +
                "last_active_ti = unix_timestamp(),\n" +
                "acquire_seconds = 1500,\n" +
                "acquire_times = 5\n" +
                "where uid=? ";
        return getJdbcTemplate().update(sql, uid);
    }

    @Override
    public List<ISchoolInfoEntity> fetchAllSchools() {
        String sql = " SELECT * FROM i_school_info where id!=35 AND status=1 ORDER BY initial ";// exclude test school by id!=35
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<ISchoolInfoEntity>(ISchoolInfoEntity.class));
    }

    @Override
    public IUserLoginEntity saveUser(String mobile, String encrypt, String nickname, Integer school, Integer registerTime) {
        int uid = saveUser(mobile, encrypt, nickname, school, "1", registerTime);
        IUserLoginEntity userLoginEntity = new IUserLoginEntity();
        userLoginEntity.setUid(uid);
        userLoginEntity.setEmail(mobile);
        userLoginEntity.setPassword(encrypt);
        userLoginEntity.setNickname(nickname);
        userLoginEntity.setSkin("1");
        userLoginEntity.setSchool(school);
        userLoginEntity.setLogintime(registerTime);
        userLoginEntity.setLastlogintime(registerTime);
        return userLoginEntity;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IUserLoginEntity findUserByNickname(String nickname) {
        final String sql = " SELECT * from i_user_login where nickname=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{nickname}, new BeanPropertyRowMapper<IUserLoginEntity>(IUserLoginEntity.class));
    }

    @Override
    public int updateUserLogin(Integer uid, String column, Object newValue) {
        final String sql = " UPDATE i_user_login SET " + column + "=? WHERE uid=? ";
        return getJdbcTemplate().update(sql, newValue, uid);
    }

    @Override
    public int updateUserInfo(Integer uid, String column, String newValue) {
        final String sql = " UPDATE i_user_info SET " + column + "=? WHERE uid=? ";
        return getJdbcTemplate().update(sql, newValue, uid);
    }

    @Override
    public List<IOpAcademyEntity> fetchAllAcademys(Integer schoolId) {
        String sql = " SELECT * FROM i_op_academy where school=? ";
        return getJdbcTemplate().query(sql, new Object[]{schoolId}, new BeanPropertyRowMapper<IOpAcademyEntity>(IOpAcademyEntity.class));
    }

    @Override
    public List<IOpSpecialtyEntity> fetchAllSpecialties(Integer schoolId, Integer academyId) {
        String sql = " SELECT * FROM i_op_specialty where school=? AND academy=?";
        return getJdbcTemplate().query(sql, new Object[]{schoolId, academyId}, new BeanPropertyRowMapper<IOpSpecialtyEntity>(IOpSpecialtyEntity.class));
    }

    @Override
    public List<IOpDormitoryEntity> fetchAllDorms(Integer schoolId) {
        String sql = " SELECT * FROM i_op_dormitory where school=? ";
        return getJdbcTemplate().query(sql, new Object[]{schoolId}, new BeanPropertyRowMapper<IOpDormitoryEntity>(IOpDormitoryEntity.class));
    }

    @Override
    public int updateUserInfo(Integer uid, String[] columns, Object[] values) {
        if (columns == null || values == null || columns.length != values.length) {
            return 0;
        }
        final StringBuilder sqlBuilder = new StringBuilder(" UPDATE i_user_info SET ");
        for (int i = 0; i < columns.length; ++i) {
            sqlBuilder.append(columns[i]).append("=?,");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(","));
        sqlBuilder.append(" WHERE uid=?");

        return getJdbcTemplate().update(sqlBuilder.toString(), values[0], values[1], values[2], uid);
    }


    public static int nthOccurrence(String str, char c, int n) {
        int pos = str.indexOf(c, 0);
        while (n-- > 0 && pos != -1)
            pos = str.indexOf(c, pos + 1);
        return pos;
    }

    public static void main(String[] args) {
        String by = "HTC ONE X hello world";
        String a = by.substring(0, nthOccurrence(by, ' ', 2));
        System.out.println(a);

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        System.out.println(thisYear);
        Calendar calendar = Calendar.getInstance();
        int todayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTimeInMillis(((long) 1377850396 * 1000));
        int latestLoginDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        System.out.println(todayOfYear + " " + latestLoginDayOfYear);

    }
}
