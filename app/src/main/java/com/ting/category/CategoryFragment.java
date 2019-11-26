package com.ting.category;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;


import com.ting.R;
import com.ting.base.BaseFragment;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.vo.CategoryVO;
import com.ting.category.adapter.CategoryAdapter;
import com.ting.common.http.HttpService;
import com.ting.util.UtilRetrofit;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 2017/9/27.
 */

public class CategoryFragment extends BaseFragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void initView() {
        mTabLayout =  flContent.findViewById(R.id.tab_layout);
        mViewPager =  flContent.findViewById(R.id.pager);
    }

    @Override
    protected void initData() {
        BaseObserver observer = new BaseObserver<BaseResult<List<CategoryVO>>>(this, BaseObserver.MODEL_ALL){
            @Override
            public void success(BaseResult<List<CategoryVO>> data) {
                super.success(data);
                CategoryAdapter adapter = new CategoryAdapter(getChildFragmentManager(), mActivity);
                adapter.setData(data.getData());
                mViewPager.setAdapter(adapter);
                mTabLayout.setupWithViewPager(mViewPager);
            }

            @Override
            public void error(BaseResult<List<CategoryVO>> value, Throwable e) {
                super.error(value, e);
            }
        };
        mDisposable.add(observer);
        UtilRetrofit.getInstance().create(HttpService.class).category().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category;
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
        initData();
    }
}
