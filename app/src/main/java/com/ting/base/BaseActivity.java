package com.ting.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ting.R;
import com.ting.play.BookDetailsActivity;
import com.ting.util.UtilIntent;
import com.ting.util.UtilPixelTransfrom;
import com.ting.view.LoadingDialog;
import com.ting.view.MyToast;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by liu on 15/10/30.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    protected BaseActivity mActivity;
    /**
     * 全局的加载框对象，已经完成初始化.
     */
    public LoadingDialog mProgressDialog;

    public boolean isHome = false;
    public boolean isWelcome = false;
    private TextView mTvTitle;
    private ImageView mIvLeft;
    protected TextView mTvRight;
    private ImageView mIvRight;
    protected FrameLayout flContent;
    protected FrameLayout flError;
    protected FrameLayout flActionbar;

    private ViewStub stubError;
    private ViewStub stubActionBar;

    public CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        getIntentData();
    }

    protected abstract String setTitle();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void getIntentData();
    protected abstract boolean showActionBar();


    /**
     * 设置自定义头部
     * @param layoutResID
     */
    protected void setCustomActionBar(@LayoutRes int layoutResID){
        if(flActionbar != null) {
            flActionbar.removeAllViews();
            LayoutInflater.from(this).inflate(layoutResID, flActionbar);
        }
    }



    @SuppressLint("ResourceType")
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_base);
        flContent =  findViewById(R.id.fl_content);
        LayoutInflater.from(this).inflate(layoutResID, flContent);
        if(showActionBar()){
            if(stubActionBar == null){
                stubActionBar =findViewById(R.id.view_stub_actionbar);
                flActionbar = (FrameLayout) stubActionBar.inflate();
                flActionbar.setId(999);
            }
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) flContent.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, flActionbar.getId());
            LayoutInflater.from(this).inflate(R.layout.actionbar_layout, flActionbar);
            mTvTitle = flActionbar.findViewById(R.id.tv_actionbar_title);
            mIvLeft = flActionbar.findViewById(R.id.iv_left);
            mIvLeft.setOnClickListener(this);
            mIvRight = flActionbar.findViewById(R.id.iv_right);
            mIvRight.setOnClickListener(this);
            mTvRight = flActionbar.findViewById(R.id.tv_right);
            mTvRight.setOnClickListener(this);
            if(!TextUtils.isEmpty(setTitle())) {
                mTvTitle.setText(setTitle());
            }
        }
        initView();
        initData();
    }




    /**
     * 设置中间标题
     * @param str
     */
    protected void setCenterTitle(String str){
        mTvTitle.setText(str);
    }

    /**
     * 显示右面的图标
     * @param id
     */
    protected void showRightImage(int id){
        if(mTvRight != null){
            mTvRight.setVisibility(View.GONE);
        }
        if(mIvRight != null){
            mIvRight.setVisibility(View.VISIBLE);
            mIvRight.setImageResource(id);
            mIvRight.setOnClickListener(this);
        }
    }




    /**
     * 显示右面的文字
     * @param str
     */
    protected void showRightText(String str){
        if(mIvRight != null){
            mIvRight.setVisibility(View.GONE);
        }
        if(mTvRight != null){
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setText(str);
            mTvRight.setOnClickListener(this);
        }
    }

    protected void isShowRightImage(boolean b){

        if(mIvRight != null){
            mIvRight.setVisibility(b ? View.VISIBLE : View.GONE);
        }
    }

    protected void isShowRightTextView(boolean b){
        if(mTvRight != null){
            mTvRight.setVisibility(b ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.btn_reload:
                reload();
                break;
        }
    }

    protected void reload() {
        initData();
    }

    /**
     * 显示有数据界面
     */
    protected void showDataLayout(){
        flContent.setVisibility(View.VISIBLE);
        if(flError != null) {
            flError.setVisibility(View.GONE);
        }
    }


    protected void errorEmpty(){
        flContent.setVisibility(View.GONE);
        if(stubError == null){
            stubError = findViewById(R.id.view_stub_error);
            flError = (FrameLayout) stubError.inflate();
        }else{
            flError.setVisibility(View.VISIBLE);
        }
        if(flActionbar != null){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) flError.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, flActionbar.getId());
        }
        flError.removeAllViews();
        LayoutInflater.from(this).inflate(R.layout.base_empty_layout, flError);
    }


    protected void errorEmpty(String str){
        flContent.setVisibility(View.GONE);
        if(stubError == null){
            stubError = findViewById(R.id.view_stub_error);
            flError = (FrameLayout) stubError.inflate();
        }else{
            flError.setVisibility(View.VISIBLE);
        }
        if(flActionbar != null){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) flError.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, flActionbar.getId());
        }
        flError.removeAllViews();
        LayoutInflater.from(this).inflate(R.layout.base_empty_layout, flError);
        TextView textView = flError.findViewById(R.id.tv_desc);
        textView.setText(str);
    }

    protected void showErrorService(@LayoutRes int layout){
        flContent.setVisibility(View.GONE);
        if(stubError == null){
            stubError = findViewById(R.id.view_stub_error);
            flError = (FrameLayout) stubError.inflate();
        }else{
            flError.setVisibility(View.VISIBLE);
        }
        if(flActionbar != null){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) flError.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, flActionbar.getId());
        }
        LayoutInflater.from(this).inflate(layout, flError);
    }


    protected void showErrorService(){
        flContent.setVisibility(View.GONE);
        if(stubError == null){
            stubError = findViewById(R.id.view_stub_error);
            flError = (FrameLayout) stubError.inflate();
        }else{
            flError.setVisibility(View.VISIBLE);
        }
        if(flActionbar != null){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) flError.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, flActionbar.getId());
        }
        LayoutInflater.from(this).inflate(R.layout.base_network_error_layout, flError);
        flError.findViewById(R.id.btn_reload).setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    /**
     * 显示加载框
     */
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new LoadingDialog(this);
        }
        //如果加载框不显示， 那么就显示加载框
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * 移除加载框.
     */
    public void removeProgressDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    /**
     * 页面跳转
     *
     * @param classes c
     */
    public void intent(final Class<?> classes) {
        UtilIntent.intentDIY(this, classes);
    }


    /**
     * 页面跳转
     *
     * @param classes 目标
     * @param
     */
    public void intent(final Class<?> classes, final Bundle bundle) {
        UtilIntent.intentDIY(this, classes, bundle);
    }

    public void finishAnim(Activity activity) {
        UtilIntent.finishDIY(activity);
    }

    public void finishAnim() {
        UtilIntent.finishDIY(this);
    }

    public void onback(View view) {
        finishAnim(this);
    }

    @Override
    public void onBackPressed() {
        finishAnim(this);
    }

    /**
     * 描述：Toast提示文本.
     *
     * @param text 文本
     */
    public void showToast(String text) {
        MyToast toast = new MyToast(this);
        toast.setText(text);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
