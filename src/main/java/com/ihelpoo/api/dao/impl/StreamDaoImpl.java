package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.StreamDao;
import com.ihelpoo.api.model.entity.VTweetStreamEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

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
        if(sids.length() > 0){
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
}
