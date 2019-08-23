package com.ting.bean.vo;

public class HostInfoVO {

    /**
     * id : 5463
     * userImage : ./Uploads/201605/5742761ccf2ac.jpg
     * nickname : null
     */

    private String id;
    private String userImage;
    private String nickname;
    private int userCard;
    private String contact;

    public int getUserCard() {
        return userCard;
    }

    public void setUserCard(int userCard) {
        this.userCard = userCard;
    }

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
