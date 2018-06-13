package com.ting.base;

/**
 * Created by liu on 2017/7/26.
 */

public class MessageEventBus {
    //登录
    public static final int LOGIN = 1;
    public static final int LOGIN_OUT = 2;
    public static final int BUY_VIP = 3;
    private int type;

    public MessageEventBus(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
