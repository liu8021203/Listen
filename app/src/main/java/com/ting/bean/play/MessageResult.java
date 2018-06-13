package com.ting.bean.play;

import com.ting.bean.BaseResult;
import com.ting.bean.play.CommentDate;

/**
 * Created by liu on 16/7/19.
 */
public class MessageResult extends BaseResult{
    private CommentDate commentData;

    public CommentDate getCommentData() {
        return commentData;
    }

    public void setCommentData(CommentDate commentData) {
        this.commentData = commentData;
    }
}
