package com.ihelpoo.api.dao;

import com.ihelpoo.api.model.entity.IMsgCommentEntity;
import com.ihelpoo.api.model.entity.IRecordCommentEntity;
import com.ihelpoo.api.model.entity.VAtUserEntity;

import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
public interface CommentDao {
    List<IMsgCommentEntity> fetchAllCommentsBy(int uid, int pageIndex, int pageSize);

    IRecordCommentEntity fetchCommentBy(Integer cid);

    List<VAtUserEntity> fetchAllAtBy(int uid, int pageIndex, int pageSize);

    int fetchAllCommentsCountBy(int uid);
}
