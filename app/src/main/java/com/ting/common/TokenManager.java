package com.ting.common;

import android.content.Context;
import android.text.TextUtils;

import com.ting.bean.UserInfoResult;
import com.ting.util.UtilSPutil;

/**
 * Created by liu on 2017/7/7.
 */

public class TokenManager {
    public static UserInfoResult infoResult;

    public static String getUid(Context context){
        String uid = UtilSPutil.getInstance(context).getString("uid");
        return uid;
    }

    public static void setUid(Context context, String uid){
        UtilSPutil.getInstance(context).setString("uid", uid);
    }


    public static String getToken(Context context){
        String token = UtilSPutil.getInstance(context).getString("token");
        return token;
    }

    public static void setToken(Context context, String token){
        UtilSPutil.getInstance(context).setString("token", token);
    }

    public static void claerUid(Context context){
        UtilSPutil.getInstance(context).setString("uid", "");
    }

    public static void clearToken(Context context){
        UtilSPutil.getInstance(context).setString("token", "");
    }

    public static void setInfo(UserInfoResult info){
        infoResult = info;
    }

    public static UserInfoResult getInfo(Context context){
        return infoResult;
    }


    public static UserInfoResult getUserInfo(){
        return infoResult;
    }

    /**
     * 判断是否登录
     * @param context
     * @return false是未登录， true是已登录
     */
    public static boolean isLogin(Context context){
        String uid = UtilSPutil.getInstance(context).getString("uid");
        if(!TextUtils.isEmpty(uid)){
            return true;
        }else{
            return false;
        }
    }
}
