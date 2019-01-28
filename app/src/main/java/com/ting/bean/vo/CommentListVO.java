package com.ting.bean.vo;

public class CommentListVO {


    /**
     * commentId : 104
     * commentsUserId : 0
     * commentsContent : 哦咯
     * commentsInsertDate : 2018-09-23 23:51:24
     * supportNum : 0
     * commentsName : null
     * userImage : null
     * time : 3天前
     * replyList : null
     * isSupport : false
     */

    private String commentId;
    private String commentsUserId;
    private String commentsContent;
    private String commentsInsertDate;
    private String supportNum;
    private String commentsName;
    private String userImage;
    private String time;
    private boolean isSupport;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentsUserId() {
        return commentsUserId;
    }

    public void setCommentsUserId(String commentsUserId) {
        this.commentsUserId = commentsUserId;
    }

    public String getCommentsContent() {
        return commentsContent;
    }

    public void setCommentsContent(String commentsContent) {
        this.commentsContent = commentsContent;
    }

    public String getCommentsInsertDate() {
        return commentsInsertDate;
    }

    public void setCommentsInsertDate(String commentsInsertDate) {
        this.commentsInsertDate = commentsInsertDate;
    }

    public String getSupportNum() {
        return supportNum;
    }

    public void setSupportNum(String supportNum) {
        this.supportNum = supportNum;
    }

    public String getCommentsName() {
        return commentsName;
    }

    public void setCommentsName(String commentsName) {
        this.commentsName = commentsName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSupport() {
        return isSupport;
    }

    public void setSupport(boolean support) {
        isSupport = support;
    }
}
