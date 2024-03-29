package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.LoginDao;
import com.ihelpoo.api.model.GenericResult;
import com.ihelpoo.api.model.common.User;
import com.ihelpoo.api.model.obj.Notice;
import com.ihelpoo.api.model.obj.Result;
import com.ihelpoo.api.model.entity.IUserLoginEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: dongxu.wang@acm.org
 */
public class LoginDaoImpl extends JdbcDaoSupport implements LoginDao {
    @Override
    public GenericResult validate(String username, String password, int keepAlive) {
        GenericResult genericResult = new GenericResult();
        String sql = " SELECT uid, password, school, nickname, coins FROM i_user_login WHERE email=? ";
        String[] params = new String[]{username};

        IUserLoginEntity userLoginEntity = null;
        try {
            userLoginEntity = getJdbcTemplate().queryForObject(sql, params, new BeanPropertyRowMapper<IUserLoginEntity>(IUserLoginEntity.class));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            genericResult.setResult(new Result("0", "用户名或密码错误2"));
            return genericResult;
        }
        String pwd = null;
        pwd = md5(password);
        if (!userLoginEntity.getPassword().equals(pwd)) {
            genericResult.setResult(new Result("0", "用户名或密码错误2"));
            return genericResult;
        }
        User user = new User();
        user.uid = userLoginEntity.getUid();
        user.school_id = String.valueOf(userLoginEntity.getSchool());
        user.nickname = userLoginEntity.getNickname();

        Notice notice = new Notice();
        genericResult.setUser(user);
        genericResult.setResult(new Result("1", "登录成功"));
        genericResult.setNotice(notice);
        return genericResult;
    }

    //    public static String md5(String input) throws NoSuchAlgorithmException {
//        String result = input;
//        if (input != null) {
//            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
//            md.update(input.getBytes());
//            BigInteger hash = new BigInteger(1, md.digest());
//            result = hash.toString(16);
//            while (result.length() < 32) { //40 for SHA-1
//                result = "0" + result;
//            }
//        }
//        return result;
//    }

    public static String md5(String origin) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(origin.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

    public static void main(String[] args) {
        long s = System.currentTimeMillis();
        LoginDaoImpl md = new LoginDaoImpl();
        String s1 = md.md5("hello");
        String s2 = md.md5(md.md5("hello"));
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(System.currentTimeMillis() - s);

        System.out.println(LoginDaoImpl.md5("asdfsdf") + LoginDaoImpl.md5("dsfsdfds") + LoginDaoImpl.md5(String.valueOf(System.currentTimeMillis())));
    }
}
