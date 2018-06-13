package com.ting.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ting.R;
import com.ting.bookcity.HotHostActivity;

/**
 * Glide工具类
 * Created by liu on 15/6/29.
 */
public class UtilGlide {
    private static RequestOptions options = new RequestOptions().placeholder(R.drawable.book_def).error(R.drawable.book_def).override(500, 500);

    private static RequestOptions anchorOptions = new RequestOptions().placeholder(R.drawable.default_tou_image).error(R.drawable.default_tou_image);

    private static RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.vector_head).error(R.drawable.vector_head).override(200,200);
    /**
     * 使用Glide加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImg(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).apply(options).into(imageView);
    }



    public static void loadAnchorImg(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).apply(anchorOptions).into(imageView);
    }

    public static void loadHeadImg(Context context, String thumb, ImageView hot_anchor_image) {
        Glide.with(context).load(thumb).apply(headOptions).into(hot_anchor_image);
    }
}
