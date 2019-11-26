package com.ting.anchor.adapter;

import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gengjiajia on 15/9/1.
 * 主播viewPage适配器
 */
public class AnchorMainadapter extends PagerAdapter {
    private List<View> anchorSunList;
    private String[] titles = {"主播作品","打赏榜"};

    public AnchorMainadapter(ArrayList<View> list) {
        this.anchorSunList = list;


    }

    @Override
    public int getCount() {
        return anchorSunList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        container.removeView(anchorSunList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        container.addView(anchorSunList.get(position), 0);
        return anchorSunList.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
