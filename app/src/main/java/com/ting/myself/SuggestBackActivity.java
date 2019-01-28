package com.ting.myself;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.constant.StaticConstant;
import com.ting.util.UtilRetrofit;
import com.ting.util.UtilStr;
import com.umeng.socialize.bean.StatusCode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 16/7/27.
 * 意见反馈activity
 */
public class SuggestBackActivity extends BaseActivity implements View.OnClickListener {
    private EditText etContent;
    private EditText etContack;
    private TextView tvSumbit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_back);
        initView();
    }

    @Override
    protected String setTitle() {
        return "意见反馈";
    }

    @Override
    protected void initView() {
        etContent =  findViewById(R.id.et_content);
        etContack =  findViewById(R.id.et_contack);
        tvSumbit =  findViewById(R.id.tv_sumbit);
        tvSumbit.setOnClickListener(this);
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
            case R.id.iv_activity_title_left:
                onBackPressed();
                break;

            case R.id.tv_sumbit:
                String content = etContent.getText().toString().trim();
                String contack = etContack.getText().toString().trim();
                if (UtilStr.isEmpty(content)) {
                    showToast("请填写您对我们的建议");
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("userId", TokenManager.getUid(mActivity));
                map.put("contack", contack);
                map.put("content", content);
                BaseObserver baseObserver = new BaseObserver<BaseResult>(mActivity) {
                    @Override
                    public void success(BaseResult data) {
                        super.success(data);
                        showToast("感谢您的反馈");
                        finish();
                    }


                    @Override
                    public void error(BaseResult value, Throwable e) {
                        super.error(value, e);
                    }
                };
                mActivity.mDisposable.add(baseObserver);
                UtilRetrofit.getInstance().create(HttpService.class).feedback(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
                break;
        }
    }
}
