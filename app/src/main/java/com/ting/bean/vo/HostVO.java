package com.ting.bean.vo;

public class HostVO {

    /**
     * id : 121589
     * userImage : http://www.tingshijie.com/Uploads/avatar/45023c5c6aa651ae1f69287890cb0d79.jpg
     * nickname : 有声亦笑天
     */

    private String id;
    private String userImage;
    private String nickname;
    private int focus;
    private String firstStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }


    public String getFirstStr() {
        return firstStr;
    }

    public void setFirstStr(String firstStr) {
        this.firstStr = firstStr;
    }
}
