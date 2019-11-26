package com.ting.myself;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.widget.ImageView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.myself.adapter.StudyUseDetailsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2017/8/9.
 */

public class StudyUseDetailsActivity extends BaseActivity{
    private ViewPager mPager;
    private int code = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_use_details);
    }

    protected String setTitle() {
        return null;
    }

    @Override
    protected void initView() {
        mPager = (ViewPager) findViewById(R.id.pager);
    }

    @Override
    protected void initData() {
        switch (code){
            case 0:{
                setCenterTitle("小米手机使用教程");
                List<ImageView> data = new ArrayList<>();
                ImageView view1 = new ImageView(mActivity);
                view1.setImageResource(R.mipmap.xiaomi_1);
                data.add(view1);
                ImageView view2 = new ImageView(mActivity);
                view2.setImageResource(R.mipmap.xiaomi_2);
                data.add(view2);
                ImageView view3 = new ImageView(mActivity);
                view3.setImageResource(R.mipmap.xiaomi_3);
                data.add(view3);
                ImageView view4 = new ImageView(mActivity);
                view4.setImageResource(R.mipmap.xiaomi_4);
                data.add(view4);
                ImageView view5 = new ImageView(mActivity);
                view5.setImageResource(R.mipmap.xiaomi_5);
                data.add(view5);
                StudyUseDetailsPagerAdapter adapter = new StudyUseDetailsPagerAdapter((StudyUseDetailsActivity) mActivity);
                adapter.setData(data);
                mPager.setAdapter(adapter);
            }
                break;
            case 1:{
                setCenterTitle("华为手机使用教程");
                List<ImageView> data = new ArrayList<>();
                ImageView view1 = new ImageView(mActivity);
                view1.setImageResource(R.mipmap.huawei_1);
                data.add(view1);
                ImageView view2 = new ImageView(mActivity);
                view2.setImageResource(R.mipmap.huawei_2);
                data.add(view2);
                ImageView view3 = new ImageView(mActivity);
                view3.setImageResource(R.mipmap.huawei_3);
                data.add(view3);
                ImageView view4 = new ImageView(mActivity);
                view4.setImageResource(R.mipmap.huawei_4);
                data.add(view4);
                StudyUseDetailsPagerAdapter adapter = new StudyUseDetailsPagerAdapter((StudyUseDetailsActivity) mActivity);
                adapter.setData(data);
                mPager.setAdapter(adapter);
            }
                break;
        }
    }

    @Override
    protected void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        code = bundle.getInt("code");
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }



}
