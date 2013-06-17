package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.StreamDao;
import com.ihelpoo.api.model.entity.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public class StreamDaoImpl extends JdbcDaoSupport implements StreamDao {
    @Override
    public List<VTweetStreamEntity> findAllTweetsBy(int catalog, StringBuilder pids, StringBuilder sids, int pageIndex, int pageSize) {
        int offset = pageIndex * pageSize;
        StringBuilder sql = new StringBuilder(" select sid,i_user_login.uid,say_type,content,image,url,comment_co,diffusion_co,hit_co,time,'from',last_comment_ti,nickname,sex,birthday,enteryear,type,online,active,icon_url,i_user_info.specialty_op,i_op_specialty.name,i_op_specialty.number,i_op_specialty.academy\n" +
                "from i_record_say \n" +
                "join i_user_login on i_record_say.uid = i_user_login.uid\n" +
                "join i_user_info on i_record_say.uid=i_user_info.uid\n" +
                "join i_op_specialty on i_user_info.specialty_op=i_op_specialty.id where say_type != '9' ");
        if (sids.length() > 0) {
            sql.append(" and i_record_say.uid IN (").append(sids).append(") ");
        }
        if (catalog == Integer.MIN_VALUE && pids.length() > 0) {//只看我圈的,TODO 没圈需要提醒，目前只是显是所有
            sql.append(" and i_record_say.uid IN (").append(pids).append(") ");
        } else if (-1 == catalog) {
            sql.append(" and say_type = '1' ");
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
        if(tweetEntity.getImage() == null) return Collections.emptyList();
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
        return imgs;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateTweet(IRecordSayEntity tweetEntity) {
        String sql = " update i_record_say set hit_co=? where sid=? ";
        getJdbcTemplate().update(sql, new Object[]{tweetEntity.getHitCo(), tweetEntity.getSid()});
    }
}
