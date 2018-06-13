package com.ting.play.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ting.base.BaseSubView;

import java.util.ArrayList;

/**
 * Created by gengjiajia on 15/9/6.
 */
public class PlayViewPagerAdapter extends PagerAdapter {
    private ArrayList<View> palySubViews;

    public PlayViewPagerAdapter(ArrayList<View> palySubViews) {
        this.palySubViews = palySubViews;

    }

    @Override
    public int getCount() {
        return palySubViews.size();
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        container.removeView(palySubViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        container.addView(palySubViews.get(position), 0);
        return palySubViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "书籍·详情";
        }else{
            return  "目录·下载";
        }
    }
}
