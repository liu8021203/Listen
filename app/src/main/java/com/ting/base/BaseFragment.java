package com.ting.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ting.R;
import com.ting.util.UtilIntent;
import com.ting.view.LoadingDialog;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by liu on 15/5/29.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener{
    private View baseView;
    protected FrameLayout flActionBar;
    protected FrameLayout flError;
    protected FrameLayout flContent;
    protected BaseActivity mActivity;
    /**
     * 全局的加载框对象，已经完成初始化.
     */
    public LoadingDialog mProgressDialog;
    protected abstract void initView();
    protected abstract void initData();
    protected abstract int getLayoutId();
    protected abstract boolean showActionBar();
    protected abstract void getIntentData();
    protected abstract int setActionBar();
    protected abstract void reload();

    protected CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if(baseView == null){
            baseView = inflater.inflate(R.layout.base_fragment_layout, container,false);
            flContent = baseView.findViewById(R.id.fl_content);
            flError = baseView.findViewById(R.id.fl_error);
            flActionBar = baseView.findViewById(R.id.fl_actionbar);
            inflater.inflate(getLayoutId(), flContent);
            if(showActionBar()){
                flActionBar.setVisibility(View.VISIBLE);
                inflater.inflate(setActionBar(), flActionBar);
            }else{
                flActionBar.setVisibility(View.GONE);
            }
            getIntentData();
            initView();
        }
        return baseView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_refresh:
                reload();
                break;
        }
    }

    /**
     * 显示有数据界面
     */
    protected void showDataLayout(){
        flError.setVisibility(View.GONE);
        flContent.setVisibility(View.VISIBLE);
    }

    /**
     * 数据为空
     */
    protected void errorEmpty(){
        flContent.setVisibility(View.GONE);
        flError.setVisibility(View.VISIBLE);
        flError.removeAllViews();

    }

    /**
     * 服务器错误
     */
    protected void errorService(){
        flContent.setVisibility(View.GONE);
        flError.setVisibility(View.VISIBLE);
        flError.removeAllViews();
    }

    /**
     * 描述：Toast提示文本.
     *
     * @param text 文本
     */
    public void showToast(String text) {
        mActivity.showToast(text);
    }



    /**
     * 描述：Toast提示文本.
     *
     * @param resId 文本的资源ID
     */
    public void showToast(int resId) {
        String message = this.getResources().getText(resId).toString();
        showToast(message);
    }




    /**
     * 页面跳转
     *
     * @param classes c
     */
    public void intent(final Class<?> classes) {
        UtilIntent.intentDIY(this.getActivity(), classes);
    }



    /**
     * 页面跳转
     *
     * @param classes 目标
     * @param
     */
    public void intent(final Class<?> classes, final Bundle bundle) {
        UtilIntent.intentDIY(this.getActivity(), classes, bundle);
    }


    /**
     * 描述：移除进度框.
     */
    public void removeProgressDialog() {
        mActivity.removeProgressDialog();
    }





    /**
     * 显示加载框
     */
    public void showProgressDialog() {
        mActivity.showProgressDialog();
    }


    public void finishAnim(BaseFragment fragment) {
        UtilIntent.finishDIY(getActivity());
    }


}
