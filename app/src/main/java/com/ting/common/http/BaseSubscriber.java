package com.ting.common.http;

import com.ting.bean.BaseResult;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by liu on 2017/6/22.
 */

public class BaseSubscriber <T extends BaseResult> implements Observer<T> {

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }


    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }
}
