package com.ting.constant;


import android.content.Context;

import com.ting.util.UtilSharedPreferences;

// TODO: Auto-generated Javadoc

/**
 * 描述：app设置常量.
 */
public class SettingInfo {
    private static SettingInfo instance;
    private Context context;
    /**
     * 是否第一次登录
     */
    private boolean first = true;
    public SettingInfo(Context context) {
        this.context = context;
    }

    public static SettingInfo getInstance(Context context) {
        if (instance == null) {

//            if (instance == null) {
                instance = new SettingInfo(context);
//                instance.first = UtilSharedPreferences.getInstance(context)
//                        .getBoolean("First");

//            }



        }
        return instance;
    }




    /***
     * 取得头像地址
     */
    public String getTouXiang() {
        return UtilSharedPreferences.getInstance(context).getString(
                "touxiangAdd");

    }

    /**
     * 保存个人头像网络地址
     **/
    public void setTouXiang(String touxiang) {
        UtilSharedPreferences.getInstance(context).setTouXiangAdd(
                "touxiangAdd", touxiang);
    }

    /***
     * 判断是否第一次打开应用
     */
    public boolean isFist() {
        return UtilSharedPreferences.getInstance(context).getBoolean(
                "First", true);

    }

    /**
     * 第一次打开应用对引导页进行设置
     **/
    public void setFirst(Boolean isFirst) {
        UtilSharedPreferences.getInstance(context).setBoolean(
                "First", isFirst);
    }

    /***
     * 取得用户名
     */
    public String getUsername() {
        return UtilSharedPreferences.getInstance(context).getString(
                "username");

    }

    /**
     * 设置用户名
     **/
    public void setUsername(String username) {
        UtilSharedPreferences.getInstance(context).setTouXiangAdd(
                "username", username);
    }


}
