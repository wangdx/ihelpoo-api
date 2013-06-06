create view user_post as
(SELECT user.*, sid, say_type, content, image, url, authority, comment_co, diffusion_co, hit_co, post.time, post.from, last_comment_ti FROM ihelpooapi.i_record_say post
INNER JOIN i_user_login user
ON post.uid = user.uid) ;

rename table user_post to v_user_post

