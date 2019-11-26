package com.ting.category.adapter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.ting.base.BaseActivity;
import com.ting.bean.vo.CategoryVO;
import com.ting.category.CategoryListFragment;

import java.util.List;

/**
 * Created by liu on 2017/11/6.
 */

public class CategoryAdapter extends FragmentStatePagerAdapter{
    private List<CategoryVO> data;
    private BaseActivity mActivity;


    public CategoryAdapter(FragmentManager fm, BaseActivity activity) {
        super(fm);
        mActivity = activity;
    }

    public void setData(List<CategoryVO> data) {
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        CategoryVO vo = data.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("id", vo.getId());
        CategoryListFragment fragment = (CategoryListFragment) CategoryListFragment.instantiate(mActivity, CategoryListFragment.class.getName(), bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).getName();
    }
}
