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

    public static void setInfo(Context context,UserInfoResult info){
        infoResult = info;
        UtilSPutil.getInstance(context).setString("thumb", info.getThumb());
        UtilSPutil.getInstance(context).setString("uid", String.valueOf(info.getUid()));
        UtilSPutil.getInstance(context).setString("token", info.getToken());
        UtilSPutil.getInstance(context).setString("name", info.getName());
        UtilSPutil.getInstance(context).setInt("vipLevel", info.getVipLevel());
        UtilSPutil.getInstance(context).setInt("level", info.getLevel());
        UtilSPutil.getInstance(context).setBoolean("isSignIn", info.isSignIn());
        UtilSPutil.getInstance(context).setString("signature", info.getSignature());
        UtilSPutil.getInstance(context).setInt("sexual", info.getSexual());
        UtilSPutil.getInstance(context).setInt("type", info.getType());
        UtilSPutil.getInstance(context).setInt("expire", info.getExpire());
        UtilSPutil.getInstance(context).setString("nickname", info.getNickname());
        UtilSPutil.getInstance(context).setInt("isvip", info.getIsvip());
    }

    public static UserInfoResult getInfo(Context context){
        if(infoResult != null){
            return infoResult;
        }else {
            String thumb = UtilSPutil.getInstance(context).getString("thumb");
            int uid = Integer.valueOf(UtilSPutil.getInstance(context).getString("uid"));
            String token = UtilSPutil.getInstance(context).getString("token");
            String name = UtilSPutil.getInstance(context).getString("name");
            int vipLevel = UtilSPutil.getInstance(context).getInt("vipLevel");
            int level = UtilSPutil.getInstance(context).getInt("level");
            boolean isSignIn = UtilSPutil.getInstance(context).getBoolean("isSignIn");
            String signature = UtilSPutil.getInstance(context).getString("signature");
            int sexual = UtilSPutil.getInstance(context).getInt("sexual");
            int type = UtilSPutil.getInstance(context).getInt("type");
            int expire = UtilSPutil.getInstance(context).getInt("expire");
            String nickname = UtilSPutil.getInstance(context).getString("nickname");
            int isvip = UtilSPutil.getInstance(context).getInt("isvip");
            UserInfoResult result = new UserInfoResult();
            result.setUid(uid);
            result.setToken(token);
            result.setThumb(thumb);
            result.setName(name);
            result.setVipLevel(vipLevel);
            result.setLevel(level);
            result.setIsSignIn(isSignIn);
            result.setSignature(signature);
            result.setSexual(sexual);
            result.setType(type);
            result.setExpire(expire);
            result.setIsvip(isvip);
            result.setNickname(nickname);
            infoResult = result;
            return result;
        }
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
