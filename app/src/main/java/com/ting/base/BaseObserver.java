package com.ting.base;

import android.text.TextUtils;
import android.util.Log;

import com.ting.R;
import com.ting.bean.BaseResult;
import com.ting.myself.MyDouActivity;

import java.lang.ref.WeakReference;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by liu on 2017/7/6.
 */

public class BaseObserver<T extends BaseResult> extends DisposableObserver<T> {
    private WeakReference<BaseActivity> mActivityWeakReference;
    private WeakReference<BaseFragment> mFragmentWeakReference;
    public static final int MODEL_NO = 1;
    public static final int MODEL_ALL = 2;
    public static final int MODEL_LAYOUT = 3;
    public static final int MODEL_SHOW_DIALOG = 4;
    public static final int MODEL_ONLY_SHOW_TOAST = 5;
    public static final int MODEL_SHOW_DIALOG_TOAST = 6;
    public static final int MODEL_ONLY_SHOW_DIALOG = 7;
    public static final int MODEL_SHOW_PROGRESSBAR_LAYOUT = 8;
    public static final int MODEL_REFRESH = 9;
    public static final int MODEL_LOADMORE = 10;


    private int model = 1;
    //默认显示加载loading

    public BaseObserver(BaseActivity activity, int model) {
        mActivityWeakReference = new WeakReference<BaseActivity>(activity);
        this.model = model;
    }

    public BaseObserver(BaseActivity activity, boolean b) {
        mActivityWeakReference = new WeakReference<BaseActivity>(activity);
    }

    public BaseObserver(BaseFragment fragment, int model) {
        mFragmentWeakReference = new WeakReference<BaseFragment>(fragment);
        this.model = model;
    }

    public BaseObserver(BaseFragment fragment, boolean b) {
        mFragmentWeakReference = new WeakReference<BaseFragment>(fragment);
    }

    public BaseObserver(BaseActivity activity) {
        mActivityWeakReference = new WeakReference<BaseActivity>(activity);
        this.model = 2;
    }


    public BaseObserver(BaseFragment fragment) {
        mFragmentWeakReference = new WeakReference<BaseFragment>(fragment);
        this.model = 2;
    }


    public BaseObserver() {
        this.model = 1;
    }


    @Override
    protected void onStart() {
        super.onStart();
        switch (model){
            case MODEL_SHOW_DIALOG_TOAST:
            case MODEL_ONLY_SHOW_DIALOG:
            case MODEL_ALL:
                if (mActivityWeakReference != null && mActivityWeakReference.get() != null) {
                    mActivityWeakReference.get().showProgressDialog();
                }
                if (mFragmentWeakReference != null && mFragmentWeakReference.get() != null) {
                    mFragmentWeakReference.get().showProgressDialog();
                }
                break;

            case MODEL_SHOW_PROGRESSBAR_LAYOUT:
                if (mActivityWeakReference != null && mActivityWeakReference.get() != null) {
                    mActivityWeakReference.get().showErrorService(R.layout.base_loading);
                }
                if (mFragmentWeakReference != null && mFragmentWeakReference.get() != null) {
                    mFragmentWeakReference.get().showErrorService(R.layout.base_loading);
                }
                break;
        }

    }

    @Override
    public void onNext(T value) {
        if (value != null) {
            if (value.getStatus() == 0) {
                if (mActivityWeakReference != null && mActivityWeakReference.get() != null) {
                    mActivityWeakReference.get().showDataLayout();
                }
                if (mFragmentWeakReference != null && mFragmentWeakReference.get() != null) {
                    mFragmentWeakReference.get().showDataLayout();
                }
                success(value);
            } else {
                if (!TextUtils.isEmpty(value.getMessage())) {
                    if (mActivityWeakReference != null && mActivityWeakReference.get() != null) {
                        if(model != BaseObserver.MODEL_NO) {
                            mActivityWeakReference.get().showToast(value.getMessage());
                        }
                    }
                    if (mFragmentWeakReference != null && mFragmentWeakReference.get() != null) {
                        if(model != BaseObserver.MODEL_NO) {
                            mFragmentWeakReference.get().showToast(value.getMessage());
                        }
                    }
                }

                if(value.getStatus() == 111){
                    if (mActivityWeakReference != null && mActivityWeakReference.get() != null) {
                        mActivityWeakReference.get().intent(MyDouActivity.class);
                    }
                    if (mFragmentWeakReference != null && mFragmentWeakReference.get() != null) {
                        mFragmentWeakReference.get().intent(MyDouActivity.class);
                    }
                }

                error(value, null);
            }
        } else {
            error(null, null);
        }
    }

    public void success(T data) {
    }

    public void error(T value, Throwable e) {
        switch (model){
            case MODEL_ALL:
            case MODEL_LAYOUT:
                if (mActivityWeakReference != null && mActivityWeakReference.get() != null) {
                    mActivityWeakReference.get().showErrorService();
                }

                if (mFragmentWeakReference != null && mFragmentWeakReference.get() != null) {
                    mFragmentWeakReference.get().showErrorService();
                }
                break;
        }

    }

    public void error(){

    }

    @Override
    public void onError(Throwable e) {
        if (mActivityWeakReference != null && mActivityWeakReference.get() != null) {
            if(model != BaseObserver.MODEL_NO){
                mActivityWeakReference.get().showToast("网络异常");
            }
            mActivityWeakReference.get().removeProgressDialog();
        }
        if (mFragmentWeakReference != null && mFragmentWeakReference.get() != null) {
            mFragmentWeakReference.get().removeProgressDialog();
            if(model != BaseObserver.MODEL_NO) {
                mFragmentWeakReference.get().showToast("网络异常");
            }
        }

        error(null, e);
    }

    @Override
    public void onComplete() {
        if (mActivityWeakReference != null && mActivityWeakReference.get() != null) {
            mActivityWeakReference.get().removeProgressDialog();
        }
        if (mFragmentWeakReference != null && mFragmentWeakReference.get() != null) {
            mFragmentWeakReference.get().removeProgressDialog();
        }
    }
}
