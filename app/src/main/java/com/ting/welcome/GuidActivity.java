package com.ting.welcome;

import android.os.Bundle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ting.R;
import com.ting.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gengjiajia on 15/11/5.
 * 引导页
 */
public class GuidActivity extends BaseActivity {
    private ViewPager vp_guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isHome = true;
        isWelcome = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);
    }

    @Override
    protected String setTitle() {
        return null;
    }

    @Override
    protected void initView() {
        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
        ImageView imageView1 = new ImageView(this);
        imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView1.setImageResource(R.mipmap.one_guid);
        ImageView imageView2 = new ImageView(this);
        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView2.setImageResource(R.mipmap.two_guid);
        ImageView imageView3 = new ImageView(this);
        imageView3.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView3.setImageResource(R.mipmap.three_guid);
        List<View> list = new ArrayList<View>();
        list.add(imageView1);
        list.add(imageView2);
        list.add(imageView3);


        GuiPagerAdapter adapter = new GuiPagerAdapter(list);
        vp_guide.setAdapter(adapter);

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent(MainActivity.class);
                GuidActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return false;
    }


    private class GuiPagerAdapter extends PagerAdapter {
        private List<View> list;

        public GuiPagerAdapter(List<View> list) {
            this.list = list;
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list == null ? 0 : list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView(list.get(position));
        }
    }

    @Override
    public void onBackPressed() {
    }
}