package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.StreamDao;
import com.ihelpoo.api.model.entity.*;
import com.ihelpoo.common.util.ID;
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
    public List<IRecordSayEntity> findTweetsBy(int uid, int pageIndex, int pageSize) {
        String sql = " SELECT * from i_record_say where uid=? order by `time` DESC limit ? offset ?  ";
        return getJdbcTemplate().query(sql, new Object[]{uid, pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<IRecordSayEntity>(IRecordSayEntity.class));
    }

    @Override
    public List<VTweetStreamEntity> findAllTweetsBy(int catalog, StringBuilder pids, StringBuilder sids, int schoolId, int pageIndex, int pageSize) {
        int recentThreeMonth = (int) (System.currentTimeMillis() / 1000L) - 24 * 3600 * 90;
        StringBuilder sql = new StringBuilder(
                "select i_record_say.sid,IF(i_user_login.school != i_record_say.school_id, i_school_info.`school`, i_op_specialty.`name`) as show_major_name,i_user_login.uid,say_type,content,image,url,i_user_login.school,comment_co,diffusion_co,hit_co,plus_co,i_record_say.time,`from`,last_comment_ti,school_id,nickname,sex,birthday,enteryear,type,online,active,icon_url,i_user_info.specialty_op,i_op_specialty.name,i_op_specialty.academy,i_school_info.id,i_school_info.school as schoolname,i_school_info.domain,i_school_info.domain_main\n" +
                        "from i_record_say\n" +
                        "join i_user_login ON i_record_say.uid = i_user_login.uid\n" +
                        "join i_user_info ON i_record_say.uid = i_user_info.uid\n" +
                        "join i_op_specialty ON i_user_info.specialty_op = i_op_specialty.id\n" +
                        "join i_school_info ON i_user_login.school = i_school_info.id" +
                        " where say_type != '9' ");
        if (sids.length() > 0) {
            sql.append(" and i_record_say.uid NOT IN (").append(sids).append(") ");
        }
        if (catalog == CATALOG_MINE) {
            if (pids.length() > 0) {
                sql.append(" and i_record_say.uid IN (").append(pids).append(") ");
            } else { // 如果没有圈人，则返回空
                sql.append(" and false ");
            }
        } else if (CATALOG_HELP == catalog) {
            sql.append(" and i_record_say.school_id = ").append(schoolId).append(" and say_type = '1' ");
        } else {
            sql.append(" and i_record_say.school_id = ").append(schoolId).append(" and i_record_say.time > ").append(recentThreeMonth).append(" ");
        }

        sql.append(" order by i_record_say.last_comment_ti DESC limit ? offset ? ");
        return getJdbcTemplate().query(sql.toString(), new Object[]{pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<VTweetStreamEntity>(VTweetStreamEntity.class));
    }

    @Override
    public int findAllTweetsCountBy(int catalog, StringBuilder pids, StringBuilder sids, int schoolId) {
        int recentThreeMonth = (int) (System.currentTimeMillis() / 1000L) - 24 * 3600 * 90;
        StringBuilder sql = new StringBuilder(
                "select count(*) \n" +
                        "from i_record_say\n" +
                        "join i_user_login ON i_record_say.uid = i_user_login.uid\n" +
                        "join i_user_info ON i_record_say.uid = i_user_info.uid\n" +
                        "join i_op_specialty ON i_user_info.specialty_op = i_op_specialty.id\n" +
                        "join i_school_info ON i_user_login.school = i_school_info.id" +
                        " where say_type != '9' ");
        if (sids.length() > 0) {
            sql.append(" and i_record_say.uid NOT IN (").append(sids).append(") ");
        }
        if (catalog == CATALOG_MINE && pids.length() > 0) {// 如果没有圈人，则会显示所有
            sql.append(" and i_record_say.uid IN (").append(pids).append(") ");
        } else if (CATALOG_HELP == catalog) {
            sql.append(" and i_record_say.school_id = ").append(schoolId).append(" and say_type = '1' ");
        } else {
            sql.append(" and i_record_say.school_id = ").append(schoolId).append(" and i_record_say.time > ").append(recentThreeMonth).append(" ");
        }
        return getJdbcTemplate().queryForObject(sql.toString(), Integer.class);
    }

    @Override
    public IRecordSayEntity findTweetBy(int sid) {//FIXME if help, will throw exception
        String sql = " select * from i_record_say where sid=? and say_type in (0,2,9) ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{sid}, new BeanPropertyRowMapper<IRecordSayEntity>(IRecordSayEntity.class));
    }

    public IRecordSayEntity findOneTweetBy(int sid, int uid) {
        String sql = " select * from i_record_say where sid=? and uid=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{sid, uid}, new BeanPropertyRowMapper<IRecordSayEntity>(IRecordSayEntity.class));
    }

    @Override
    public List<IRecordDiffusionEntity> findSpreadsBySid(int sid) {
        String sql = "select * from i_record_diffusion where sid=? ";
        return getJdbcTemplate().query(sql, new Object[]{sid}, new BeanPropertyRowMapper<IRecordDiffusionEntity>(IRecordDiffusionEntity.class));
    }

    @Override
    public List<VTweetSpreadEntity> findUserSpreadsBy(int sid) {
        String sql = " select id,i_user_login.uid,i_record_diffusion.sid,i_record_diffusion.time,nickname,sex,birthday,enteryear,type,online,active,icon_url from i_record_diffusion\n" +
                "left join i_user_login on i_record_diffusion.uid=i_user_login.uid\n" +
                "where sid = ?\n" +
                "order by i_record_diffusion.time DESC ";
        return getJdbcTemplate().query(sql, new Object[]{sid}, new BeanPropertyRowMapper<VTweetSpreadEntity>(VTweetSpreadEntity.class));
    }

    @Override
    public List<String> findUserImgLinkEntitiesBy(int sid) {
        IRecordSayEntity tweetEntity = getJdbcTemplate().queryForObject("select * from i_record_say where sid=? ", new Object[]{sid}, new BeanPropertyRowMapper<IRecordSayEntity>(IRecordSayEntity.class));
        String sotrageUrl = "http://img.ihelpoo.cn/";
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
    public List<VTweetCommentEntity> findAllCommentsBy(int sid, int pageIndex, int pageSize) {
        String sql = "select cid,i_user_login.uid,sid,toid,content,image,diffusion_co,`time`,nickname,sex,birthday,enteryear,`type`,online,active,icon_url from i_record_comment\n" +
                "left join i_user_login on i_record_comment.uid=i_user_login.uid\n" +
                "where sid=?\n" +
                "order by cid DESC\n" +
                "limit ? offset ? ";
        return getJdbcTemplate().query(sql, new Object[]{sid, pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<VTweetCommentEntity>(VTweetCommentEntity.class));
    }

    @Override
    public int findAllCommentsCountBy(int sid) {
        String sql = "select COUNT(*) from i_record_comment\n" +
                " left join i_user_login on i_record_comment.uid=i_user_login.uid\n" +
                " where sid=? ";
        return getJdbcTemplate().queryForObject(sql, Integer.class, sid);
    }

    @Override
    public List<VTweetCommentEntity> findAllHelpRepliesBy(Integer sid, Integer pageIndex, Integer pageSize) {
        String sql = "select id as cid,i_user_login.uid,sid,toid,content,image,diffusion_co,time,nickname,sex,birthday,enteryear,type,online,active,icon_url from i_record_helpreply\n" +
                "left join i_user_login on i_record_helpreply.uid=i_user_login.uid\n" +
                "where sid=?\n" +
                "order by `time` DESC \n" +
                "limit ? offset ? ";
        return getJdbcTemplate().query(sql, new Object[]{sid, pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<VTweetCommentEntity>(VTweetCommentEntity.class));
    }

    @Override
    public int saveComment(final int sid, final int uid, final String content, final int toUid, Boolean isHelp) {
        final String tableName = isHelpReply(isHelp) ? "i_record_helpreply" : "i_record_comment";
        final String cid = isHelpReply(isHelp) ? "id" : "cid";
        final String sql = "insert into " + tableName + " (uid, sid, toid, content, image, time) values(?,?,?,?, '', unix_timestamp());";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{cid});
                ps.setInt(1, uid);
                ps.setInt(2, sid);
                ps.setInt(3, toUid);
                ps.setString(4, content);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    private boolean isHelpReply(Boolean isHelp) {
        return isHelp != null && isHelp;
    }

    @Override
    public int saveMsgComment(Integer ownerUid, int sid, int ncid, int uid, int isReply) {
        String column = isReply > 0 ? "cid," : "";
        String question = isReply > 0 ? "?," : "";
        final String sql = "insert into i_msg_comment (uid, sid, " + column + "ncid, rid, time, deliver) values(?,?," + question + "?,?,unix_timestamp(), 0) ";
        return getJdbcTemplate().update(sql, isReply > 0 ? new Object[]{ownerUid, sid, isReply, ncid, uid} : new Object[]{ownerUid, sid, ncid, uid});
    }

    @Override
    public int saveMsgAt(int touid, int uid, int sid, int cid) {
        final String sql = "insert into i_msg_at (touid, fromuid, sid, cid, time, deliver) values(?,?,?,?,unix_timestamp(), 0)";
        return getJdbcTemplate().update(sql, new Object[]{touid, uid, sid, cid});
    }

    @Override
    public VTweetDetailEntity findTweetDetailBy(int sid) {
        String sql = "select sid,say_type,i_record_say.uid,i_user_login.icon_url,online,comment_co,diffusion_co,plus_co,`from` `by`,content,`time`,active,sex,birthday,i_op_specialty.`name` academy, `type` author_type,enteryear enter_year,nickname author\n" +
                " from i_record_say " +
                " left join i_user_login on i_record_say.uid=i_user_login.uid " +
                " left join i_user_info on i_record_say.uid=i_user_info.uid " +
                " left join i_op_specialty ON i_user_info.specialty_op = i_op_specialty.id " +
                " where sid=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{sid}, new BeanPropertyRowMapper<VTweetDetailEntity>(VTweetDetailEntity.class));
    }

    @Override
    public IRecordHelpreplyEntity findHelpBy(Integer hid) {
        String sql = " SELECT * from i_record_helpreply where id=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{hid}, new BeanPropertyRowMapper<IRecordHelpreplyEntity>(IRecordHelpreplyEntity.class));
    }

    @Override
    public List<IRecordSayEntity> findTweetsWithin(int uid, long time) {
        final String sql = " SELECT * FROM i_record_say WHERE uid=? AND `time`>? AND (say_type = 0 OR say_type = 1) ORDER BY `time` DESC ";
        return getJdbcTemplate().query(sql, new Object[]{uid, time}, new BeanPropertyRowMapper<IRecordSayEntity>(IRecordSayEntity.class));
    }

    @Override
    public int findCountOfSaysWithin(int uid, long twelveHours) {
        final String sql = " SELECT COUNT(*) FROM i_record_say WHERE uid=? AND `time`>? AND (say_type = 0 OR say_type = 1) ";
        return getJdbcTemplate().queryForObject(sql, Integer.class, uid, twelveHours);
    }

    @Override
    public IRecordSayEntity findLastTweetBy(int uid) {
        final String sql = " SELECT * FROM i_record_say WHERE uid=? ORDER BY `time` DESC LIMIT 1 ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{uid}, new BeanPropertyRowMapper<IRecordSayEntity>(IRecordSayEntity.class));
    }

    @Override
    public int saveHelpData(final int sayId, final int reward) {
        final String sql = " INSERT INTO i_record_help (sid, reward_coins, status) VALUES(?,?,?) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"hid"});
                ps.setInt(1, sayId);
                ps.setInt(2, reward);
                ps.setString(3, "1");
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public int deleteTweet(Integer uid, Integer sid) {
        IRecordSayEntity sayEntity = findOneTweetBy(sid, uid);
        final String sqlSay = " DELETE FROM i_record_say WHERE sid=? AND uid=? ";
        final String sqlHelp = " DELETE FROM i_record_help WHERE sid=? ";
        final String sqlHelpReply = " DELETE FROM i_record_helpreply WHERE sid=? ";
        final String sqlComment = " DELETE FROM i_record_comment WHERE sid=? ";
        final String sqlDiffusion = " DELETE FROM i_record_diffusion WHERE sid=? ";

        int affectedSay = getJdbcTemplate().update(sqlSay, new Object[]{sid, uid});
        Object[] sidParam = {sid};
        if (affectedSay > 0) {
            if ("1".equals(sayEntity.getSayType())) {
                getJdbcTemplate().update(sqlHelp, sidParam);
                getJdbcTemplate().update(sqlHelpReply, sidParam);
            } else {
                getJdbcTemplate().update(sqlComment, sidParam);
            }
        }
        getJdbcTemplate().update(sqlDiffusion, sidParam);
        return affectedSay;
    }

    @Override
    public int isRecordPlusByMe(int sid, Integer uid) {
        String sql = "  SELECT COUNT(*) FROM i_record_plus WHERE sid=? and uid=? ";
        return getJdbcTemplate().queryForObject(sql, Integer.class, sid, uid);
    }

    @Override
    public int isRecordDiffuseByMe(int sid, Integer uid) {
        String sql = "  SELECT COUNT(*) FROM i_record_diffusion WHERE sid=? and uid=? ";
        return getJdbcTemplate().queryForObject(sql, Integer.class, sid, uid);
    }

    @Override
    public IRecordPlusEntity findPlusBy(Integer sid, Integer uid) {
        final String sql = " SELECT * FROM i_record_plus WHERE sid=? AND uid=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{sid, uid}, new BeanPropertyRowMapper<IRecordPlusEntity>(IRecordPlusEntity.class));
    }

    @Override
    public int deletePlus(int id) {
        final String sql = " DELETE FROM i_record_plus WHERE id=? ";
        return getJdbcTemplate().update(sql, new Object[]{id});
    }

    @Override
    public IRecordSayEntity findOneTweetBy(Integer sid) {
        String sql = " select * from i_record_say where sid=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{sid}, new BeanPropertyRowMapper<IRecordSayEntity>(IRecordSayEntity.class));
    }

    @Override
    public IMsgNoticeEntity findMsgNotice(String noticeType, Integer uid, Integer sid, String type) {
        final String sql = " SELECT * FROM i_msg_notice WHERE notice_type=? AND source_id=? AND detail_id=? AND format_id=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{noticeType, uid, sid, type}, new BeanPropertyRowMapper<IMsgNoticeEntity>(IMsgNoticeEntity.class));
    }

    @Override
    public int deleteNoticeMessage(long noticeId) {
        final String sql = " DELETE FROM i_msg_notice WHERE notice_id=? ";
        return getJdbcTemplate().update(sql, new Object[]{noticeId});
    }

    @Override
    public int addPlus(final Integer sid, final Integer uid) {
        final String sql = " INSERT INTO i_record_plus (sid, uid, `view`, deliver, create_time) values(?,?,?,?,unix_timestamp()) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, sid);
                ps.setInt(2, uid);
                ps.setString(3, "");
                ps.setInt(4, 0);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public long saveNoticeMessage(final String noticeType, final Integer uid, final Integer sid, String sayType) {
        final long noticeId = ID.INSTANCE.next();
        final String sql = " INSERT INTO i_msg_notice (notice_id, notice_type, source_id, detail_id, format_id, create_time) values (?,?,?,?,?,unix_timestamp()) ";
        getJdbcTemplate().update(sql, new Object[]{noticeId, "stream/" + noticeType + "-para:" + sayType, uid, sid, sayType});
        return noticeId;
    }

    @Override
    public void saveNotice(int from, String noticeType, int detailId, long noticeId) {
        String sql = " INSERT INTO i_msg_notice (notice_id, notice_type, source_id, detail_id, format_id, create_time) values (?,?,?,?,?,unix_timestamp()) ";
        getJdbcTemplate().update(sql, new Object[]{noticeId, noticeType, from, detailId, noticeType});
    }

    @Override
    public IRecordCommentEntity findLastCommentBy(int uid, Boolean isHelp) {
        String tableName = isHelpReply(isHelp) ? "i_record_helpreply" : "i_record_comment";
        String cid = isHelpReply(isHelp) ? "id as cid" : "cid";
        final String sql = " SELECT " + cid + ", uid, sid, `time`, content FROM " + tableName + " WHERE uid=? ORDER BY `time` DESC limit 1 ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{uid}, new BeanPropertyRowMapper<IRecordCommentEntity>(IRecordCommentEntity.class));
    }

    @Override
    public int deleteCommment(Integer replyid, Integer authorid, Integer sid) {
        if (sid != null && sid > 0) {
            final String sql = " DELETE FROM i_record_comment WHERE sid=? ";
            return getJdbcTemplate().update(sql, new Object[]{sid});
        }
        final String sql = " DELETE FROM i_record_comment WHERE cid=? and uid=? ";
        return getJdbcTemplate().update(sql, new Object[]{replyid, authorid});
    }

    @Override
    public int deleteHelpReply(Integer replyid, Integer authorid, Integer sid) {
        if (sid != null && sid > 0) {
            final String sql = " DELETE FROM i_record_helpreply WHERE sid=? ";
            return getJdbcTemplate().update(sql, new Object[]{sid});
        }
        final String sql = " DELETE FROM i_record_helpreply WHERE id=? and uid=? ";
        return getJdbcTemplate().update(sql, new Object[]{replyid, authorid});
    }

    @Override
    public IAuMailSendEntity findAuMailSend(Integer sayUid, int sid, int uid) {
        final String sql = " SELECT * FROM i_au_mail_send WHERE uid=? AND sid=? AND helperid=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{sayUid, sid, uid}, new BeanPropertyRowMapper<IAuMailSendEntity>(IAuMailSendEntity.class));
    }

    @Override
    public int saveAuMailSend(final Integer sayUid, final int uid, final int sid, final String type) {
        final String sql = " INSERT INTO i_au_mail_send (uid, helperid, sid, `type`, `time`) VALUES (?,?,?,?,unix_timestamp()) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, sayUid);
                ps.setInt(2, uid);
                ps.setInt(3, sid);
                ps.setString(4, type);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public IRecordHelpEntity findRecordHelp(int sid) {
        final String sql = " SELECT * FROM i_record_help WHERE sid=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{sid}, new BeanPropertyRowMapper<IRecordHelpEntity>(IRecordHelpEntity.class));
    }

    @Override
    public int updateRecordHelp(int hid, String newStatus, String statusStub) {
        final String sql = " UPDATE i_record_help SET status=IF(status != ?, ?, status) WHERE hid=? ";
        return getJdbcTemplate().update(sql, new Object[]{statusStub, newStatus, hid});
    }


    @Override
    public IRecordDiffusionEntity findDiffusion(Integer sid, Integer uid) {
        final String sql = " SELECT id,uid,sid,`view`,`time` FROM i_record_diffusion WHERE uid=? AND sid=? ";
        return getJdbcTemplate().queryForObject(sql, new Object[]{uid, sid}, new BeanPropertyRowMapper<IRecordDiffusionEntity>(IRecordDiffusionEntity.class));
    }

    @Override
    public List<IRecordDiffusionEntity> findDiffusions(Integer uid, long time12Hour) {
        final String sql = " SELECT id,uid,sid,`view`,`time` FROM i_record_diffusion WHERE uid=? AND `time` > ? ";
        return getJdbcTemplate().query(sql, new Object[]{uid, time12Hour}, new BeanPropertyRowMapper<IRecordDiffusionEntity>(IRecordDiffusionEntity.class));
    }

    @Override
    public int saveDiffusion(final Integer uid, final Integer sid, final String content) {
        final String sql = " INSERT INTO i_record_diffusion (uid, sid, `view`, `time`) VALUES (?,?,?,unix_timestamp()) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, uid);
                ps.setInt(2, sid);
                ps.setString(3, content);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public int incSayCount(Integer sid, boolean canAffect, String countColumn, boolean shouldInc) {
        String columnInc = shouldInc ? countColumn + " + 1" : countColumn;
        StringBuilder sb = new StringBuilder(" UPDATE i_record_say SET " + countColumn + " = IF(" + countColumn + " IS NULL,1, " + columnInc + " ) ");
        if (canAffect) {
            sb.append(" , last_comment_ti=unix_timestamp() ");
        }
        sb.append(" WHERE sid=? ");
        return getJdbcTemplate().update(sb.toString(), new Object[]{sid});
    }

    @Override
    public int incOrDecSayCount(Integer sid, int offset) {
        if (offset >= 0) {
            final String sqlInc = " UPDATE i_record_say SET plus_co = IF(plus_co IS NULL, ?, plus_co + ?) WHERE sid=? ";
            return getJdbcTemplate().update(sqlInc, new Object[]{offset, offset, sid});
        } else {
            final String sqlDec = " UPDATE i_record_say SET plus_co = IF(plus_co IS NULL OR plus_co - ? < 0, 0, plus_co - ?) WHERE sid=? ";
            return getJdbcTemplate().update(sqlDec, new Object[]{-offset, -offset, sid});
        }
    }

    public static void main(String[] args) {
        final Pattern AT_PATTERN = Pattern.compile("@[\\u4e00-\\u9fa5\\w\\-]+");
        Matcher matcher = AT_PATTERN.matcher("hello@123 @hei asdf");
        while (matcher.find()) {
            System.out.println(matcher.group().substring(1));
        }
    }
}
