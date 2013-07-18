create view user_post as
(SELECT user.*, sid, say_type, content, image, url, authority, comment_co, diffusion_co, hit_co, post.time, post.from, last_comment_ti FROM ihelpooapi.i_record_say post
INNER JOIN i_user_login user
ON post.uid = user.uid) ;

rename table user_post to v_user_post


create view v_tweet_stream as
(
select sid,i_user_login.uid,say_type,content,image,url,comment_co,diffusion_co,hit_co,time,'from',last_comment_ti,nickname,sex,birthday,enteryear,type,online,active,icon_url,i_user_info.specialty_op,i_op_specialty.name,i_op_specialty.number,i_op_specialty.academy
from i_record_say
join i_user_login on i_record_say.uid = i_user_login.uid
join i_user_info on i_record_say.uid=i_user_info.uid
join i_op_specialty on i_user_info.specialty_op=i_op_specialty.id
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

