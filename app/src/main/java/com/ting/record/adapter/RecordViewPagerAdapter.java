package com.ting.record.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ting.base.BaseFragment;
import com.ting.record.fragment.LastListenFragment;

/**
 * Created by gengjiajia on 15/9/8.
 */
public class RecordViewPagerAdapter extends FragmentPagerAdapter {

    public RecordViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = null;
        switch (position){
            case 0:
                fragment = new LastListenFragment();
                break;

            case 1:
//                fragment = new DownFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
