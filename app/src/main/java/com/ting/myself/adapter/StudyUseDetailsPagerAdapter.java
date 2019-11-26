package com.ting.myself.adapter;

import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ting.myself.StudyUseDetailsActivity;

import java.util.List;

/**
 * Created by liu on 2017/8/9.
 */

public class StudyUseDetailsPagerAdapter extends PagerAdapter{
    private StudyUseDetailsActivity mActivity;
    private List<ImageView> data;

    public StudyUseDetailsPagerAdapter(StudyUseDetailsActivity activity) {
        mActivity = activity;
    }

    public void setData(List<ImageView> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(data.get(position));
        return data.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView(data.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
