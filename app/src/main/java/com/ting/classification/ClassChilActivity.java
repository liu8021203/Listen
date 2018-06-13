package com.ting.classification;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.classification.adapter.ClassFragmentAdapter;

/**
 * Created by gengjiajia on 15/9/15.
 */
public class ClassChilActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout class_new_layout;
    private TextView class_new_text;
    private View class_new_line;
    private RelativeLayout class_hot_layout;
    private TextView class_hot_text;
    private View class_hot_line;
    private RelativeLayout class_recommend_layout;
    private TextView class_recommend_text;
    private View class_recommend_line;
    private ViewPager class_more_main_viewpager;
    private Bundle bundle;
    private int classItemID;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_class_more_main);
        bundle = this.getIntent().getExtras();
        if (bundle != null) {
            classItemID = bundle.getInt("classItemID");
            title = bundle.getString("classItemName");
        }
        initView();
        setCenterTitle(title);
    }

    @Override
    protected String setTitle() {
        return null;
    }

    @Override
    protected void initView() {
        class_new_layout = (RelativeLayout) findViewById(R.id.class_new_layout);//最新
        class_hot_layout = (RelativeLayout) findViewById(R.id.class_hot_layout);//热门
        class_recommend_layout = (RelativeLayout) findViewById(R.id.class_recommend_layout);//推荐
        class_new_text = (TextView) findViewById(R.id.class_new_text);//最新
        class_hot_text = (TextView) findViewById(R.id.class_hot_text);//热门
        class_recommend_text = (TextView) findViewById(R.id.class_recommend_text);//推荐
        class_new_line = (View) findViewById(R.id.class_new_line);//最新
        class_hot_line = (View) findViewById(R.id.class_hot_line);//热门
        class_recommend_line = (View) findViewById(R.id.class_recommend_line);//推荐
        class_new_layout.setOnClickListener(this);
        class_hot_layout.setOnClickListener(this);
        class_recommend_layout.setOnClickListener(this);
        class_more_main_viewpager = (ViewPager) findViewById(R.id.class_more_main_viewpager);
        ClassFragmentAdapter adapter = new ClassFragmentAdapter(getSupportFragmentManager());
        adapter.setCateId(String.valueOf(classItemID));
        class_more_main_viewpager.setAdapter(adapter);

        class_more_main_viewpager.setCurrentItem(0);
        class_more_main_viewpager
                .addOnPageChangeListener(new ClassMoreViewPagechangeListener());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }

    private class ClassMoreViewPagechangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            clearState();
            if (arg0 == 0) {
                class_new_text.setTextColor(getResources().getColor(R.color.c14acf0));
                class_new_line.setVisibility(View.VISIBLE);
            } else if (arg0 == 1) {
                class_hot_text.setTextColor(getResources().getColor(R.color.c14acf0));
                class_hot_line.setVisibility(View.VISIBLE);
            } else if (arg0 == 2) {
                class_recommend_text.setTextColor(getResources().getColor(R.color.c14acf0));
                class_recommend_line.setVisibility(View.VISIBLE);
            }


        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.class_new_layout:
                clearState();
                class_more_main_viewpager.setCurrentItem(0);
                class_new_text.setTextColor(this.getResources().getColor(R.color.c14acf0));
                class_new_line.setVisibility(View.VISIBLE);
                break;
            case R.id.class_hot_layout:
                clearState();
                class_more_main_viewpager.setCurrentItem(1);
                class_hot_text.setTextColor(this.getResources().getColor(R.color.c14acf0));
                class_hot_line.setVisibility(View.VISIBLE);
                break;
            case R.id.class_recommend_layout:
                clearState();
                class_more_main_viewpager.setCurrentItem(2);
                class_recommend_text.setTextColor(this.getResources().getColor(R.color.c14acf0));
                class_recommend_line.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void clearState() {

        class_new_text.setTextColor(this.getResources().getColor(R.color.cbbbbbb));
        class_hot_text.setTextColor(this.getResources().getColor(R.color.cbbbbbb));
        class_recommend_text.setTextColor(this.getResources().getColor(R.color.cbbbbbb));
        class_new_line.setVisibility(View.GONE);
        class_hot_line.setVisibility(View.GONE);
        class_recommend_line.setVisibility(View.GONE);
    }
}
