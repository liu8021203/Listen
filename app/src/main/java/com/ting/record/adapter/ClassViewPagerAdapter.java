package com.ting.record.adapter;

import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gengjiajia on 15/9/8.
 */
public class ClassViewPagerAdapter extends PagerAdapter {
    private List<View> recordsubViews;
    public ClassViewPagerAdapter(ArrayList<View>
                                         list)
    {
       this.recordsubViews=list;
    }


    @Override
    public int getCount() {

        return recordsubViews.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        container.removeView(recordsubViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        container.addView(recordsubViews.get(position), 0);
        return recordsubViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }


}
