package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.OoUser;
import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.UserList;
import com.ihelpoo.api.model.entity.IUserLoginEntity;
import com.ihelpoo.api.model.entity.IUserStatusEntity;
import com.ihelpoo.api.model.entity.VUserDetailEntity;
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
    public int saveSay(final int uid, final long t, final String msg, final String imageIds, final String reward, final String by, final int schoolId) {
        final String sql = "insert into i_record_say (uid, say_type, content, image, url, authority, `time`, last_comment_ti, `from`, school_id) values(?,'0',?,?,'','0',?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(sql, new String[]{"sid"});
                ps.setInt(1, uid);
                ps.setString(2, msg);
                ps.setString(3, imageIds);
                ps.setInt(4, (int)t);
                ps.setInt(5, (int)t);
                ps.setString(6, by);
                ps.setInt(7, schoolId);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public VUserDetailEntity findUserDetailById(int uid) {
        String sql = " select a.uid, a. status, a. email, a.password, a.nickname, a.sex, a.birthday, a.enteryear, a.type, a.priority, a.ip, a.logintime,\n" +
                "a.creat_ti, a.login_days_co, a.online, a.coins, a.active, a.icon_fl, a.icon_url, a.skin, \n" +
                "b.introduction, b.introduction_re, b.realname, b.mobile, b.qq, b.weibo, b.fans, b.follow,  \n" +
                "c.school, c.domain, d.name as academy_name, e.name as major_name\n" +
                "from i_user_login a\n" +
                "join i_user_info b on a.uid=b.uid\n" +
                "join i_school_info c on a.school=c.id\n" +
                "join i_op_academy d on d.id=b.academy_op\n" +
                "join i_op_specialty e on e.id=b.specialty_op where a.uid=? ";
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
    public int addDynamic(final int sayId, final String changeicon) {
        final String sql = "insert into i_record_dynamic (sid, `type`) values(?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, sayId);
                ps.setString(2, changeicon);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
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
    public IUserLoginEntity findByUserName(String username) {
        String sql = " SELECT * FROM i_user_login WHERE email=? ";
        String[] params = new String[]{username};
        IUserLoginEntity userLoginEntity = null;
        userLoginEntity = getJdbcTemplate().queryForObject(sql, params, new BeanPropertyRowMapper<IUserLoginEntity>(IUserLoginEntity.class));
        return userLoginEntity;
    }

    @Override
    public IUserLoginEntity findUserById(int uid) {
        String sql = " SELECT * FROM i_user_login WHERE uid=? ";
        Object[] params = new Object[]{uid};
        return getJdbcTemplate().queryForObject(sql, params, new BeanPropertyRowMapper<IUserLoginEntity>(IUserLoginEntity.class));
    }

    @Override
    public IUserStatusEntity findUserStatusById(int uid) {
        String sql = " SELECT * FROM i_user_status WHERE uid=? ";
        Object[] params = new Object[]{uid};
        return getJdbcTemplate().queryForObject(sql, params, new BeanPropertyRowMapper<IUserStatusEntity>(IUserStatusEntity.class));
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
    public int saveMsgActive(int uid, int total, int change, String reason) {
        String sql = " INSERT INTO i_msg_active(way, `time`, deliver, uid, total, `change`, reason) VALUES('add', unix_timestamp(), '0', ?,?,?,?) ";
        return getJdbcTemplate().update(sql, new Object[]{uid, total, change, reason});
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


    public static void main(String[] args) {

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        System.out.println(thisYear);
        Calendar calendar = Calendar.getInstance();
        int todayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTimeInMillis(((long) 1377850396 * 1000));
        int latestLoginDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        System.out.println(todayOfYear + " " + latestLoginDayOfYear);

    }
}
