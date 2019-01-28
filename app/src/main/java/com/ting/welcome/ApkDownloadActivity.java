package com.ting.welcome;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.bean.apk.ApkResult;
import com.ting.download.ApkDownload;

/**
 * Created by liu on 16/9/21.
 */
public class ApkDownloadActivity extends BaseActivity implements View.OnClickListener{
    private TextView tvDesc;
    private LinearLayout llNotMustDownload;
    private Button btnCancle;
    private Button btnNotMustDownload;
    private LinearLayout llMustDownload;
    private Button btnMustDonwload;
    private ImageView ivApk;
    private ApkResult result;
    private ApkDownload apkDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_apk);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        apkDownload = new ApkDownload(this);
    }

    @Override
    protected String setTitle() {
        return null;
    }
    @Override
    protected void initView() {
        tvDesc = (TextView)findViewById( R.id.tv_desc );
        llNotMustDownload = (LinearLayout)findViewById( R.id.ll_not_must_download );
        btnCancle = (Button)findViewById( R.id.btn_cancle );
        btnNotMustDownload = (Button)findViewById( R.id.btn_not_must_download );
        llMustDownload = (LinearLayout)findViewById( R.id.ll_must_download );
        btnMustDonwload = (Button)findViewById( R.id.btn_must_donwload );
        ivApk = (ImageView)findViewById( R.id.iv_apk );
        btnCancle.setOnClickListener( this );
        btnNotMustDownload.setOnClickListener( this );
        btnMustDonwload.setOnClickListener( this );
        if(result.getUpdate() == 0)
        {
            llNotMustDownload.setVisibility(View.GONE);
            llMustDownload.setVisibility(View.VISIBLE);
        }
        else
        {
            llNotMustDownload.setVisibility(View.VISIBLE);
            llMustDownload.setVisibility(View.GONE);
        }
        tvDesc.setText(result.getContent());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        result = (ApkResult) bundle.getSerializable("vo");
    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_cancle:
                finish();
                break;

            case R.id.btn_not_must_download:
                showToast("开始下载");
                finish();
                apkDownload.download(result.getDownloadUrl(), "听世界听书","");
                break;

            case R.id.btn_must_donwload:
                showToast("开始下载");
                btnMustDonwload.setEnabled(false);
                apkDownload.download(result.getDownloadUrl(), "听世界听书","");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(result.getUpdate() == 1)
        {
            finish();
        }
    }
}
