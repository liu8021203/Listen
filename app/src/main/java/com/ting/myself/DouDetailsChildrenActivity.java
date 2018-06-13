package com.ting.myself;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;

/**
 * Created by gengjiajia on 16/7/27.
 * <p/>
 * 听豆明细activity
 */
public class DouDetailsChildrenActivity extends BaseActivity {
    private ImageView iv_activity_title_left;
    private TextView tv_activity_title;
    private TextView online_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dou_details_children);
        initView();
    }

    @Override
    protected String setTitle() {
        return "支付详情";
    }
    @Override
    protected void initView() {
        iv_activity_title_left = (ImageView) findViewById(R.id.iv_activity_title_left);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        tv_activity_title.setText("支付详情");
        iv_activity_title_left.setImageResource(R.mipmap.white_left_arrow);
        online_help = (TextView) findViewById(R.id.online_help);//在线帮助
        online_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //指定的QQ号只需要修改uin后的值即可。
                String url11 = "mqqwpa://im/chat?chat_type=wpa&uin=769991514&version=1";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url11)));
            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }
}