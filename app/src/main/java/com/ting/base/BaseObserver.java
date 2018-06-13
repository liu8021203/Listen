package com.ting.base;

import android.text.TextUtils;
import android.util.Log;

import com.ting.bean.BaseResult;

import java.lang.ref.WeakReference;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by liu on 2017/7/6.
 */

public class BaseObserver<T extends BaseResult> extends DisposableObserver<T> {
    private WeakReference<BaseActivity> mActivityWeakReference;
    private WeakReference<BaseFragment> mFragmentWeakReference;
    //默认显示加载loading
    private boolean isShow = true;

    public BaseObserver(BaseActivity activity) {
        mActivityWeakReference = new WeakReference<BaseActivity>(activity);
    }

    public BaseObserver(BaseActivity activity, boolean b) {
        mActivityWeakReference = new WeakReference<BaseActivity>(activity);
        isShow = b;
    }

    public BaseObserver(BaseFragment fragment) {
        mFragmentWeakReference = new WeakReference<BaseFragment>(fragment);
    }

    public BaseObserver(BaseFragment fragment, boolean b) {
        mFragmentWeakReference = new WeakReference<BaseFragment>(fragment);
        isShow = b;
    }

    public BaseObserver() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mActivityWeakReference != null && mActivityWeakReference.get() != null) {
            if (isShow) {
                mActivityWeakReference.get().showProgressDialog();
            }
        }
        if (mFragmentWeakReference != null && mFragmentWeakReference.get() != null) {
            if (isShow) {
                mFragmentWeakReference.get().showProgressDialog();
            }
        }
    }

    @Override
    public void onNext(T value) {
        if (value.isStatus()) {
            if (value != null) {
                if (mActivityWeakReference != null && mActivityWeakReference.get() != null) {
                    mActivityWeakReference.get().showDataLayout();
                }
                if (mFragmentWeakReference != null && mFragmentWeakReference.get() != null) {
                    mFragmentWeakReference.get().showDataLayout();
                }
                success(value);
            } else {
                error();
            }
        } else {
            if (!TextUtils.isEmpty(value.getMessage())) {
                if (mActivityWeakReference != null && mActivityWeakReference.get() != null) {
                    mActivityWeakReference.get().showToast(value.getMessage());
                }
                if (mFragmentWeakReference != null && mFragmentWeakReference.get() != null) {
                    mFragmentWeakReference.get().showToast(value.getMessage());
                }
            }
            error();
        }
    }

    public void success(T data) {
    }

    public void error() {
        if (mActivityWeakReference != null && mActivityWeakReference.get() != null) {
            mActivityWeakReference.get().errorService();
        }

        if (mFragmentWeakReference != null && mFragmentWeakReference.get() != null) {
            mFragmentWeakReference.get().errorService();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mActivityWeakReference != null && mActivityWeakReference.get() != null) {
            mActivityWeakReference.get().removeProgressDialog();
        }
        if (mFragmentWeakReference != null && mFragmentWeakReference.get() != null) {
            mFragmentWeakReference.get().removeProgressDialog();
        }
        error();
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
