package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.PostDao;
import com.ihelpoo.api.model.Post;
import com.ihelpoo.api.model.PostList;
import com.ihelpoo.api.model.Posts;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.entity.VUserPostEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public class PostDaoImpl extends JdbcDaoSupport implements PostDao {
    @Override
    public PostList getPostListByTimeLevel(String timeLevel, int order, int pageIndex, int pageSize) {
        PostList postListRoot = new PostList();

        String sql = "SELECT user.*, sid, say_type, content, image, url, authority, comment_co, diffusion_co, hit_co, post.time, post.from, last_comment_ti " +
                "FROM i_record_say post INNER JOIN i_user_login user ON post.uid = user.uid " +
                "WHERE post.say_type = ? and post.time > ? " +
                "ORDER BY post." + orderBys[order] + " DESC limit ? offset ?";
        List<VUserPostEntity> postEntityList = getJdbcTemplate().query(
                sql, new Object[]{order < 4 ? 0 : 1, /*System.currentTimeMillis() / 1000 - 3600 * 24 * 7*/0, pageSize, pageIndex * pageSize}, new BeanPropertyRowMapper<VUserPostEntity>(VUserPostEntity.class));

        Posts posts = new Posts();
        List<Post> postList = new ArrayList<Post>();
        for (VUserPostEntity vUser : postEntityList) {
            Post post = new Post();
            post.setId(vUser.getSid());
            post.setAnswerCount(vUser.getCommentCo());
            post.setAuthor(vUser.getNickname());
            post.setAuthorid(vUser.getActive());
            post.setPortrait("http://ihelpoo-public.stor.sinaapp.com/useralbum/" + vUser.getUid() + "/" + vUser.getIconUrl() + "_s.jpg?t=" + System.currentTimeMillis());
            post.setPubDate(friendly_time(new Date((long) vUser.getTime() * 1000)));
            post.setTitle(vUser.getContent());
            post.setViewCount(vUser.getHitCo());
            post.setAnswer(vUser.getContent());
            postList.add(post);
        }
        posts.setPost(postList);

        postListRoot.setPosts(posts);
        postListRoot.setNotice(new Notice());
        return postListRoot;
    }

    private static final String[] orderBys = {"hit_co", "hit_co", "comment_co", "diffusion_co", "hit_co", "comment_co"};

    private static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public String friendly_time(Date time) {
        {
            if (time == null) {
                return "Unknown";
            }
            String ftime = "";
            Calendar cal = Calendar.getInstance();

            //判断是否是同一天
            String curDate = dateFormater2.get().format(cal.getTime());
            String paramDate = dateFormater2.get().format(time);
            if (curDate.equals(paramDate)) {
                int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
                if (hour == 0)
                    ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
                else
                    ftime = hour + "小时前";
                return ftime;
            }

            long lt = time.getTime() / 86400000;
            long ct = cal.getTimeInMillis() / 86400000;
            int days = (int) (ct - lt);
            if (days == 0) {
                int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
                if (hour == 0)
                    ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
                else
                    ftime = hour + "小时前";
            } else if (days == 1) {
                ftime = "昨天";
            } else if (days == 2) {
                ftime = "前天";
            } else if (days > 2 && days <= 10) {
                ftime = days + "天前";
            } else if (days > 10) {
                ftime = dateFormater2.get().format(time);
            }
            return ftime;
        }

    }
}
