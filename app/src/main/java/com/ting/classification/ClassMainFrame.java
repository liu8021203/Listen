package com.ting.classification;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ting.R;
import com.ting.base.BaseFragment;
import com.ting.base.BaseObserver;
import com.ting.classification.adapter.ClassifiAdapter;
import com.ting.bean.classfi.ClassMainResult;
import com.ting.common.http.HttpService;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/8/31.
 * 分类主之frame;
 */
public class ClassMainFrame extends BaseFragment{
    private ClassifiAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) flContent.findViewById(R.id.recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1, 0xffebebeb);
        decoration.setDividerSize(20);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {
        getDate();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frame_class_main;
    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected int setActionBar() {
        return 0;
    }

    @Override
    protected void reload() {

    }

    /**
     * 获取分类数据
     */
    private void getDate() {
        BaseObserver baseObserver = new BaseObserver<ClassMainResult>(this){
            @Override
            public void success(ClassMainResult data) {
                super.success(data);
                if(data.getData() != null && data.getData().size() > 0){
                    if(mAdapter == null){
                        mAdapter = new ClassifiAdapter(mActivity);
                        mAdapter.setResult(data.getData());
                        mRecyclerView.setAdapter(mAdapter);
                    }else{
                        mAdapter.setResult(data.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                }else{
                    errorEmpty();
                }
            }

            @Override
            public void error() {
                super.error();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getCategorys().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

}
