package com.ting.myself;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseApplication;
import com.ting.util.UtilSystem;
import com.ting.welcome.AdActivity;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;


/**
 * Created by gengjiajia on 15/9/14.
 * 关于我们activity
 */
public class AboutMeActivity extends BaseActivity {
    private TextView app_version_name;
    private TextView listener_web;
    private TextView sina_web;
    private TextView tv_kefu_qq;
    private TextView tvA1;
    private TextView tvA2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_about_we_new);
        initView();
    }

    @Override
    protected String setTitle() {
        return "关于我们";
    }
    @Override
    protected void initView() {
        app_version_name = (TextView) findViewById(R.id.app_version_name);//版本名称

        app_version_name.setText(UtilSystem.getVersionName(this));


        listener_web = (TextView) findViewById(R.id.listener_web);//听世界网址
        sina_web = (TextView) findViewById(R.id.sina_web);//新浪微博


        tv_kefu_qq = (TextView) findViewById(R.id.tv_kefu_qq);//客服QQ
        tv_kefu_qq.setOnClickListener(this);
        listener_web.setOnClickListener(this);
        sina_web.setOnClickListener(this);
        tvA1 = (TextView) findViewById(R.id.tv_a1);
        tvA2 = (TextView) findViewById(R.id.tv_a2);
        tvA1.setOnClickListener(this);
        tvA2.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.listener_web:
                Uri uri = Uri.parse("http://" + listener_web.getText().toString());
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;
            case R.id.sina_web:
                Uri uri1 = Uri.parse(sina_web.getText().toString());
                Intent it1 = new Intent(Intent.ACTION_VIEW, uri1);
                startActivity(it1);
                break;
            case R.id.tv_kefu_qq:
                boolean isInstall = UMShareAPI.get(this).isInstall(mActivity, SHARE_MEDIA.QQ);
                Log.d("aaa", "isInstall=====" + isInstall);
                if(isInstall){
                    //指定的QQ号只需要修改uin后的值即可。
                    String url11 = "mqqwpa://im/chat?chat_type=wpa&uin=769991514&version=1";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url11)));
                }else{
                    showToast("未安装QQ");
                }

                break;



            case R.id.tv_a1: {
                Bundle bundle = new Bundle();
                bundle.putString("url", "http://www.tingshijie.com/serviceAgreement.html");
                intent(AdActivity.class, bundle);
            }
                break;

            case R.id.tv_a2: {
                Bundle bundle = new Bundle();
                bundle.putString("url", "http://www.tingshijie.com/copyright.html");
                intent(AdActivity.class, bundle);
            }
                break;
        }
    }
}
