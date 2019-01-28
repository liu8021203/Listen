package com.ting.bean.vo;

import java.util.List;

public class CommentDataVO {
    private int count;
    private List<CommentListVO> commentList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<CommentListVO> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentListVO> commentList) {
        this.commentList = commentList;
    }
}
