package com.ting.play.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.play.BookDetailsActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by gengjiajia on 15/11/4.
 * 分享dialog
 */
public class ShareDialog extends Dialog implements View.OnClickListener{
    private LinearLayout share_weibo;
    private LinearLayout share_qq;
    private LinearLayout share_weixin;
    private LinearLayout share_circle_friends;
    private TextView tvCancle;


    private String shareContext = "我爱分享";
    private String shareUrl = "http://www.tingshijie.com/share.php?shareid=";
    //    final UMSocialService mController = UMServiceFactory
//            .getUMSocialService("com.umeng.share");
    private static final String desc = "智者善听，善听者智，来听世界听书聆听--";
    private BaseActivity activity;
    private String imageUrl = null;
    private String id;
    private String url = "http://www.tingshijie.com/Index/share/id/";
    private String bookname;

    public ShareDialog(BaseActivity activity) {
        super(activity, R.style.SendMessageDialog);
        this.activity = activity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
//        if(!TextUtils.isEmpty(imageUrl)) {
//            mImage = new UMImage(activity, imageUrl);
//        }else{
//            mImage = new UMImage(activity, R.mipmap.ic_launcher);
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        initView();

    }
    private void initView() {
        share_weibo = (LinearLayout) findViewById(R.id.share_weibo);//微博
        share_qq = (LinearLayout) findViewById(R.id.share_qq);//qq
        share_weixin = (LinearLayout) findViewById(R.id.share_weixin);//微信
        share_circle_friends = (LinearLayout) findViewById(R.id.share_circle);//朋友圈
        tvCancle = (TextView) findViewById(R.id.tv_cancle);
        tvCancle.setOnClickListener(this);
        share_weibo.setOnClickListener(this);
        share_qq.setOnClickListener(this);
        share_weixin.setOnClickListener(this);
        share_circle_friends.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.share_qq: {
                UMWeb umWeb = new UMWeb(url + id);
                umWeb.setThumb(new UMImage(activity, imageUrl));
                umWeb.setTitle(bookname);
                umWeb.setDescription(desc + bookname);
                new ShareAction(activity).setPlatform(SHARE_MEDIA.QQ).withMedia(umWeb).setCallback(mListener).share();
            }
                break;
            case R.id.share_weibo: {
                UMWeb umWeb = new UMWeb(url + id);
                umWeb.setThumb(new UMImage(activity, imageUrl));
                umWeb.setTitle(bookname);
                umWeb.setDescription(desc + bookname);
                new ShareAction(activity).setPlatform(SHARE_MEDIA.SINA).withMedia(umWeb).setCallback(mListener).share();
            }
                break;
            case R.id.share_weixin: {
                UMWeb umWeb = new UMWeb(url + id);
                umWeb.setThumb(new UMImage(activity, imageUrl));
                umWeb.setTitle(bookname);
                umWeb.setDescription(desc + bookname);
                new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN).withMedia(umWeb).setCallback(mListener).share();
            }
                break;
            case R.id.share_circle: {
                UMWeb umWeb = new UMWeb(url + id);
                umWeb.setThumb(new UMImage(activity,imageUrl));
                umWeb.setTitle(bookname);
                umWeb.setDescription(desc + bookname);
                new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).withMedia(umWeb).setCallback(mListener).share();
            }
                break;
            case R.id.tv_cancle:
                dismiss();
                break;
            default:
                break;
        }
    }

    private UMShareListener mListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA media) {

        }

        @Override
        public void onResult(SHARE_MEDIA media) {
            activity.showToast("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA media, Throwable throwable) {
            activity.showToast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA media) {
            activity.showToast("分享取消");
        }
    };

}
