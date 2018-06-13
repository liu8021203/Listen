package com.ting.myself;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.base.BaseActivity;
import com.ting.myself.adapter.MySeeAdapter;

/**
 * Created by gengjiajia on 15/9/12.
 */
public class ShareFriendsActivity extends BaseActivity {
    private ImageView iv_activity_title_left;
    private TextView tv_activity_title;
    private FrameLayout fl_activity_title_left;
    private MySeeAdapter mySeeAdapter;
    private LinearLayout to_pay_dou_layout;
    private LinearLayout to_pay_member_layout;
    private RelativeLayout share_sina_layout;
    private RelativeLayout share_qq_layout;
    private RelativeLayout share_weixin_layout;
    private RelativeLayout share_circle_friends;
    String WXappID = "wx65ef305f9d0ebdec";
    String WXappSecret = "b26f6c4da76885d534673cbc04a08a48";
    String QQAPPID = "1104890302";
    String QQAPPKEY = "wHPiSxuQV3J1E1VC";
    private String shareContext = "我爱分享";
    private String shareUrl = "http://www.baidu.com";
    private static final String NAME = "听世界";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.frame_share_friends);
//        initView();


    }

    @Override
    protected String setTitle() {
        return null;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

//
//    private void initView() {
//        iv_activity_title_left = (ImageView) findViewById(R.id.iv_activity_title_left);
//        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
//        tv_activity_title.setText("分享给好友");
//        iv_activity_title_left.setImageResource(R.mipmap.white_left_arrow);
//        share_sina_layout = (RelativeLayout) findViewById(R.id.share_sina_layout);//新浪微博分享
//        share_qq_layout = (RelativeLayout) findViewById(R.id.share_qq_layout);//QQ分享
//        share_weixin_layout = (RelativeLayout) findViewById(R.id.share_weixin_layout);//微信分享
//        share_circle_friends = (RelativeLayout) findViewById(R.id.share_circle_friends);//分享到朋友圈
//
//    }


}
