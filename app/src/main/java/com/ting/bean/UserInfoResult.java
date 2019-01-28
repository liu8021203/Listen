package com.ting.bean;

/**
 * Created by gengjiajia on 15/9/17.
 * 用户结果集
 */
public class UserInfoResult {

    /**
     * id : 15434
     * image : ./Uploads/201708/599ac81c3f6ee.jpg
     * nickname : 听世界好呃
     * phone : null
     * token : 226f442a2a406ca2c25f534733249232
     * money : 479
     */

    private String id;
    private String image;
    private String nickname;
    private String phone;
    private String token;
    private String money;
    private int sex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }


    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
