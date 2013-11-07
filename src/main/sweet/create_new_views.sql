create view user_post as
(SELECT user.*, sid, say_type, content, image, url, authority, comment_co, diffusion_co, hit_co, post.time, post.from, last_comment_ti FROM ihelpooapi.i_record_say post
INNER JOIN i_user_login user
ON post.uid = user.uid) ;

rename table user_post to v_user_post


create view v_tweet_stream as
(
select sid,IF(i_user_login.school != i_record_say.school_id, i_school_info.`school`, i_op_specialty.`name`) as show_major_name,i_user_login.uid,say_type,content,image,url,comment_co,diffusion_co,hit_co,time,'from',last_comment_ti,nickname,sex,birthday,enteryear,type,online,active,icon_url,i_user_info.specialty_op,i_op_specialty.name,i_op_specialty.school,i_op_specialty.academy
from i_record_say
join i_user_login on i_record_say.uid = i_user_login.uid
join i_user_info on i_record_say.uid=i_user_info.uid
join i_op_specialty on i_user_info.specialty_op=i_op_specialty.id
);


create view v2_tweet_stream as
(
select i_record_say.sid,i_user_login.uid,say_type,content,image,url,i_user_login.school,comment_co,diffusion_co,hit_co,plus_co,i_record_say.time,`from`,last_comment_ti,school_id,nickname,sex,birthday,enteryear,type,online,active,icon_url,i_user_info.specialty_op,i_op_specialty.name,i_op_specialty.academy,i_school_info.id,i_school_info.school as schoolname,i_school_info.domain,i_school_info.domain_main
from i_record_say
join i_user_login ON i_record_say.uid = i_user_login.uid
join i_user_info ON i_record_say.uid = i_user_info.uid
join i_op_specialty ON i_user_info.specialty_op = i_op_specialty.id
join i_school_info ON i_user_login.school = i_school_info.id
};

create view v_tweet_detail as (
select sid,say_type,i_record_say.uid,online,comment_co,diffusion_co,plus_co,`from` `by`,
	content,`time`,active,sex,birthday,i_op_specialty.`name` academy,
	`type`,enteryear,nickname author
from i_record_say
join i_user_login on i_record_say.uid=i_user_login.uid
join i_user_info on i_record_say.uid=i_user_info.uid
join i_op_specialty ON i_user_info.specialty_op = i_op_specialty.id
);

create view v_tweet_spread as
(
select id,i_user_login.uid,i_record_diffusion.sid,i_record_diffusion.time,nickname,sex,birthday,enteryear,type,online,active,icon_url from i_record_diffusion
join i_user_login on i_record_diffusion.uid=i_user_login.uid
);

create view v_tweet_comment as(
select cid,i_user_login.uid,sid,toid,content,image,diffusion_co,time,nickname,sex,birthday,enteryear,type,online,active,icon_url from i_record_comment
join i_user_login on i_record_comment.uid=i_user_login.uid
);


create view v_msg_login as (

select * from i_msg_notice
join i_user_login on source_id = uid
);


create view v_user_talk as (
select t.content, t.`time`,t.deliver, t.del,t.touid,t.id, u.* from i_talk_content t
join i_user_login u
on t.uid = u.uid);


create view v_at_user as (
select id,touid,fromuid,sid,cid,hid,aid,`time`,deliver,uid,nickname,icon_url from i_msg_at
join i_user_login on i_msg_at.fromuid = i_user_login.uid
);


create view v_user_detail as
    (select
        a.uid as uid,
        a.nickname as nickname,
        a.sex as gender,
        a.email as email,
        a.status as email_verified,
        a.birthday as birthday,
        a.enteryear as enrol_time,
        a.type as user_type,
        a.ip as ip_addr,
        a.logintime as login_time,
        a.lastlogintime as last_login,
        a.creat_ti as create_time,
        a.login_days_co as login_days,
        a.online as online_status,
        a.active as active_credits,
        a.icon_fl as avatar_type,
        a.icon_url as avatar_url,
        a.skin as web_theme,
        b.introduction as self_intro,
        b.realname as real_name,
        b.fans as followers_count,
        b.follow as friends_count,
        c.id as school_id,
        c.domain as school_domain,
        IF(c.school IS NULL, '', c.school) as school_name,
        IF(d.name IS NULL, '', d.name) as academy_name,
        IF(e.name IS NULL, '', e.name) as major_name,
        IF(f.name IS NULL, '', f.name) as dorm_name
    from
        i_user_login a
            left join
        i_user_info b ON a.uid = b.uid
            left join
        i_school_info c ON a.school = c.id
            left join
        i_op_academy d ON d.id = b.academy_op
            left join
        i_op_specialty e ON e.id = b.specialty_op
            left join
        i_op_dormitory f ON f.id = b.dormitory_op)
;

create view v_login_record as(
select
sid,i_user_login.uid,say_type,content,image,url,comment_co,diffusion_co,hit_co,`time`,`from`,last_comment_ti,nickname,sex,birthday,enteryear,`type`,online,active,icon_url
from i_record_say
left join i_user_login on i_record_say.uid = i_user_login.uid
);

