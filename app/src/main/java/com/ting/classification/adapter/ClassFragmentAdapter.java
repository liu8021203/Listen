package com.ting.classification.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.ting.base.BaseFragment;
import com.ting.classification.fragment.ClassifyFragment;

/**
 * Created by liu on 2017/7/7.
 */

public class ClassFragmentAdapter extends FragmentPagerAdapter{
    private String[] requestType = {"0","1","2"};
    private String cateId;

    public ClassFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    @Override
    public Fragment getItem(int position) {
        ClassifyFragment fragment = ClassifyFragment.getInstance();
        Bundle bundle = new Bundle();
        bundle.putString("requestType", requestType[position]);
        bundle.putString("cateId", cateId);
        fragment.setArguments(bundle);
        Log.d("aaa", requestType[position]);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
