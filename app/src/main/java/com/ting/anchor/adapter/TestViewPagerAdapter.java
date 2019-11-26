package com.ting.anchor.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ting.base.BaseFragment;

import java.util.List;

public class TestViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> mData;
    public TestViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.mData=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
