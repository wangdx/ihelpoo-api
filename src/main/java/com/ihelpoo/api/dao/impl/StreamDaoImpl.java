package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.StreamDao;
import com.ihelpoo.api.model.TweetCommentPushResult;
import com.ihelpoo.api.model.TweetCommentResult;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.base.Result;
import com.ihelpoo.api.model.entity.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: dongxu.wang@acm.org
 */
public class StreamDaoImpl extends JdbcDaoSupport implements StreamDao {

    public static final int CATALOG_STREAM = 0;
    public static final int CATALOG_HELP = -1;
    public static final int CATALOG_MINE = -2;

    @Override
    public List<VTweetStreamEntity> findAllTweetsBy(int catalog, StringBuilder pids, StringBuilder sids, int schoolId, int pageIndex, int pageSize) {
        int recentThreeMonth = (int) (System.currentTimeMillis() / 1000L) - 24 * 3600 * 90;
        StringBuilder sql = new StringBuilder(" select i_record_say.sid,i_user_login.uid,say_type,content,image,url,i_user_login.school,comment_co,diffusion_co,hit_co,plus_co,i_record_say.time,`from`,last_comment_ti,school_id,nickname,sex,birthday,enteryear,type,online,active,icon_url,i_user_info.specialty_op,i_op_specialty.name,i_op_specialty.academy,i_school_info.id,i_school_info.school as schoolname,i_school_info.domain,i_school_info.domain_main\n" +
                " from i_record_say \n" +
                " join i_user_login ON i_record_say.uid = i_user_login.uid\n" +
                " join i_user_info on i_record_say.uid=i_user_info.uid\n" +
                " join i_op_specialty on i_user_info.specialty_op=i_op_specialty.id " +
                " join i_school_info ON i_user_login.school = i_school_info.id " +
                " where say_type != '9' ");
        if (sids.length() > 0) {
            sql.append(" and i_record_say.uid IN (").append(sids).append(") ");
        }
        if (catalog == CATALOG_MINE && pids.length() > 0) {
            sql.append(" and i_record_say.uid IN (").append(pids).append(") ");
        } else if (CATALOG_HELP == catalog) {
            sql.append(" and i_record_say.school_id = ").append(schoolId).append(" and say_type = '1' ");
        } else {
            sql.append(" and i_record_say.school_id = ").append(schoolId).append(" and i_record_say.time > ").append(recentThreeMonth).append(" ");
        }

        sql.append(" order by i_record_say.last_comment_ti DESC limit ? offset ? ");
        return getJdbcTemplate().query(sql.toString(), new Object[]{pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<VTweetStreamEntity>(VTweetStreamEntity.class));
    }

    @Override
    public IRecordSayEntity findTweetBy(int sid) {//FIXME if help, will throw exception
        String sql = " select * from i_record_say where sid=? and say_type in (0,2,9) ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{sid}, new BeanPropertyRowMapper<IRecordSayEntity>(IRecordSayEntity.class));
    }

    @Override
    public List<IRecordDiffusionEntity> findSpreadsBySid(int sid) {
        String sql = "select * from i_record_diffusion where sid=? ";
        return getJdbcTemplate().query(sql, new Object[]{sid}, new BeanPropertyRowMapper<IRecordDiffusionEntity>(IRecordDiffusionEntity.class));
    }

    @Override
    public List<VTweetSpreadEntity> findUserSpreadsBy(int sid) {
        String sql = " select id,i_user_login.uid,i_record_diffusion.sid,i_record_diffusion.time,nickname,sex,birthday,enteryear,type,online,active,icon_url from i_record_diffusion\n" +
                "join i_user_login on i_record_diffusion.uid=i_user_login.uid\n" +
                "where sid = ?\n" +
                "order by i_record_diffusion.time DESC ";
        return getJdbcTemplate().query(sql, new Object[]{sid}, new BeanPropertyRowMapper<VTweetSpreadEntity>(VTweetSpreadEntity.class));
    }

    @Override
    public List<String> findUserImgLinkEntitiesBy(int sid) {
        IRecordSayEntity tweetEntity = getJdbcTemplate().queryForObject("select * from i_record_say where sid=? ", new Object[]{sid}, new BeanPropertyRowMapper<IRecordSayEntity>(IRecordSayEntity.class));
        String sotrageUrl = "http://ihelpoo-public.stor.sinaapp.com/";
        List<String> imgs = new ArrayList<String>();
        if (tweetEntity.getImage() == null) return Collections.emptyList();
        try {
            if (tweetEntity.getImage().contains(";")) {
                String[] imgIds = tweetEntity.getImage().split(";");
                for (String id : imgIds) {
                    if (id.contains("ihelpooupload")) {
                        imgs.add(sotrageUrl + "upload" + id.substring(13));
                    } else {
                        IRecordOutimgEntity imgEntity = getJdbcTemplate().queryForObject("select * from i_record_outimg where id=? ", new Object[]{id}, new BeanPropertyRowMapper<IRecordOutimgEntity>(IRecordOutimgEntity.class));
                        imgs.add(imgEntity.getRpath());
                    }
                }

            } else {
                if (!"yes".equals(tweetEntity.getImage())) {
                    if (tweetEntity.getImage().contains("ihelpooupload")) {
                        imgs.add(sotrageUrl + "upload" + tweetEntity.getImage().substring(13));
                    } else {
                        IRecordOutimgEntity imgEntity = getJdbcTemplate().queryForObject("select * from i_record_outimg where id=? ", new Object[]{tweetEntity.getImage()}, new BeanPropertyRowMapper<IRecordOutimgEntity>(IRecordOutimgEntity.class));
                        imgs.add(imgEntity.getRpath());
                    }
                }
            }

        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
        return imgs;
    }

    @Override
    public void updateTweet(IRecordSayEntity tweetEntity) {
        String sql = " update i_record_say set hit_co=? where sid=? ";
        getJdbcTemplate().update(sql, new Object[]{tweetEntity.getHitCo(), tweetEntity.getSid()});
    }

    @Override
    public List<VTweetCommentEntity> findAllCommentssBy(int sid, int catalog, int pageIndex, int pageSize) {
        String sql = "select cid,i_user_login.uid,sid,toid,content,image,diffusion_co,time,nickname,sex,birthday,enteryear,type,online,active,icon_url from i_record_comment\n" +
                "join i_user_login on i_record_comment.uid=i_user_login.uid\n" +
                "where sid=?\n" +
                "order by cid ASC\n" +
                "limit ? offset ? ";
        return getJdbcTemplate().query(sql, new Object[]{sid, pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<VTweetCommentEntity>(VTweetCommentEntity.class));
    }

    @Override
    public TweetCommentPushResult pushComment(final int id, final int uid, String[] atUsers, final String content, int catalog, int postToMyZone) {
        final String sql = "insert into i_record_comment (uid, sid, toid, content, image, time) values(?,?,0, ?, '', unix_timestamp());";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(sql, new String[]{"cid"});
                ps.setInt(1, uid);
                ps.setInt(2, id);
                ps.setString(3, content);
                return ps;
            }
        },
                keyHolder);
        IRecordSayEntity recordSayEntity = null;
        IUserLoginEntity userLoginEntity = null;
        if (keyHolder.getKey().intValue() > 0) {
            userLoginEntity = getJdbcTemplate().queryForObject("select  * from i_user_login where uid=? ", new Object[]{uid}, new BeanPropertyRowMapper<IUserLoginEntity>(IUserLoginEntity.class));
            int rank = convertToRank(userLoginEntity.getActive());
            String sql2 = " update i_record_say set comment_co=? ";
            if (rank >= 2) {
                sql2 += " ,last_comment_ti=unix_timestamp() ";
            }
            sql2 += " where sid=? ";
            recordSayEntity = getJdbcTemplate().queryForObject("select * from i_record_say where sid=? ", new Object[]{id}, new BeanPropertyRowMapper<IRecordSayEntity>(IRecordSayEntity.class));
            getJdbcTemplate().update(sql2, new Object[]{recordSayEntity.getCommentCo() + 1, recordSayEntity.getSid()});
        }

        IUserStatusEntity userStatusEntity = getJdbcTemplate().queryForObject(" select * from i_user_status where uid=? ", new Object[]{uid}, new BeanPropertyRowMapper<IUserStatusEntity>(IUserStatusEntity.class));
        if (userStatusEntity.getActiveCLimit() < 15) {
            String sqlUpdateStatus = "update i_user_status set active_c_limit=? where uid=? ";
            getJdbcTemplate().update(sqlUpdateStatus, new Object[]{userStatusEntity.getActiveCLimit() + 1, uid});
            String sqlUpdateUser = "update i_user_login set active=? where uid=? ";
            getJdbcTemplate().update(sqlUpdateUser, new Object[]{fetchUserActive(userLoginEntity), uid});
            String sqlUpdateMsg = "insert into i_msg_active (uid, total, `change`, way, reason, `time`, deliver, school) values (?, ?, 1, 'add', '评论或回复他人的记录 (每天最多加15次，包含回复帮助次数)', unix_timestamp(), 0, 0)";//TODO school
            getJdbcTemplate().update(sqlUpdateMsg, new Object[]{uid, fetchUserActive(userLoginEntity)});
        }
        if (uid != recordSayEntity.getUid()) {
            String sqlAddComment = "insert into i_msg_comment (uid, sid, ncid, rid, time, deliver) values(?,?,?,?,unix_timestamp(), 0) ";
            getJdbcTemplate().update(sqlAddComment, new Object[]{recordSayEntity.getUid(), id, keyHolder.getKey().intValue(), uid});
        }

        final Pattern AT_PATTERN = Pattern.compile("@[\\u4e00-\\u9fa5\\w\\-]+");
        Matcher matcher = AT_PATTERN.matcher(content);
        while (matcher.find()) {
            String atUserName = matcher.group().substring(1);
            IUserLoginEntity userLoginEntity1;
            try {
                userLoginEntity1 = getJdbcTemplate().queryForObject("select * from i_user_login where nickname=?", new Object[]{atUserName}, new BeanPropertyRowMapper<IUserLoginEntity>(IUserLoginEntity.class));
                String sqlAddAtMsg = "insert into i_msg_at (touid, fromuid, sid, cid, time, deliver) values(?,?,?,?,unix_timestamp(), 0)";
                getJdbcTemplate().update(sqlAddAtMsg, new Object[]{userLoginEntity1.getUid(), uid, id, keyHolder.getKey().intValue()});
            } catch (EmptyResultDataAccessException e) {
                // can not find such an @ user.
                e.printStackTrace();
            }

        }


        Result result = new Result("1", "操作成功");
        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();
        TweetCommentResult.Comment comment = new TweetCommentResult.Comment.Builder()
                .id(id)
                .authorid(uid)
                .avatar(convertToAvatarUrl(userLoginEntity.getIconUrl(), uid))
                .author(userLoginEntity.getNickname())
                .content(content)
                .date((new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date(System.currentTimeMillis())))
                .build();

        TweetCommentPushResult commentPushResult = new TweetCommentPushResult(result, comment, notice);

        return commentPushResult;
    }

    @Override
    public VTweetDetailEntity findTweetDetailBy(int sid) {
        String sql = "select sid,i_record_say.uid,i_user_login.icon_url,online,comment_co,diffusion_co,`from` `by`,content,`time`,active,sex,birthday,i_op_specialty.`name` academy, `type` author_type,enteryear enter_year,nickname author\n" +
                " from i_record_say " +
                " join i_user_login on i_record_say.uid=i_user_login.uid " +
                " join i_user_info on i_record_say.uid=i_user_info.uid " +
                " join i_op_specialty ON i_user_info.specialty_op = i_op_specialty.id " +
                " where sid=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{sid}, new BeanPropertyRowMapper<VTweetDetailEntity>(VTweetDetailEntity.class));
    }

    private int fetchUserActive(IUserLoginEntity userLoginEntity) {
        return userLoginEntity.getActive() == null ? 1 : userLoginEntity.getActive() + 1;
    }

    public static void main(String[] args) {
        final Pattern AT_PATTERN = Pattern.compile("@[\\u4e00-\\u9fa5\\w\\-]+");
        Matcher matcher = AT_PATTERN.matcher("hello@123 @hei asdf");
        while (matcher.find()) {
            System.out.println(matcher.group().substring(1));
        }
    }

    private String convertToDate(Integer time) {
        return (new java.text.SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss")).format(new Date((long) (time.floatValue() * 1000)));
    }

    private String convertToAvatarUrl(String iconUrl, int uid) {

        String baseUrl = "http://ihelpoo-public.stor.sinaapp.com/";
        if (!empty(iconUrl)) {
            return baseUrl + "useralbum/" + uid + "/" + iconUrl + "_m.jpg?t=" + System.currentTimeMillis();
        } else {
            return "http://zzuli.sinaapp.com/Public/image/common/0.jpg?t=" + System.currentTimeMillis();
        }
    }

    private boolean empty(String iconUrl) {
        return iconUrl == null || iconUrl.length() <= 0;
    }

    private int convertToRank(Integer active) {
        if (active == null)
            return 1;
        if (active < 30) {
            return 1;
        } else if (active < 120) {
            return 2;
        } else if (active < 330) {
            return 3;
        } else if (active < 720) {
            return 4;
        } else if (active < 1350) {
            return 5;
        } else if (active < 3990) {
            return 6;
        } else if (active < 10200) {
            return 7;
        } else if (active < 22230) {
            return 8;
        } else if (active < 41280) {
            return 9;
        } else {
            return 10;
        }
    }
}
