package com.ting.record;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseFragment;
import com.ting.play.controller.MusicDBController;
import com.ting.record.adapter.RecordViewPagerAdapter;
import com.ting.base.ListenDialog;

/**
 * Created by gengjiajia on 15/8/31.
 * 记录主frame
 */
public class RecordMainFrame extends BaseFragment implements View.OnClickListener {
    private ImageView iv_activity_title_left;
    private TextView tv_activity_title;
    private TextView tv_activity_title_right;
    private MusicDBController musicDBController;
    private RelativeLayout last_listen_layout;
    private TextView last_listen_text;
    private RelativeLayout have_down_layout;
    private TextView have_down_text;
    private ViewPager record_main_viewpager;
    private RecordViewPagerAdapter recordViewPagerAdapter;
    private View rec_one_line;
    private View rec_three_line;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        musicDBController = new MusicDBController();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isHidden()) {
            Log.d("aaa", "onResume");
            recordViewPagerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 通知更新数据
     */
    public void update(){
        recordViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("abc", hidden + "");
    }

    public void tvRightGone() {
        tv_activity_title_right.setVisibility(View.GONE);
    }

    public void tvRightVisible() {
        tv_activity_title_right.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initView() {
        iv_activity_title_left = (ImageView) flContent.findViewById(R.id.iv_activity_title_left);
        tv_activity_title = (TextView) flContent.findViewById(R.id.tv_activity_title);
        iv_activity_title_left.setVisibility(View.GONE);
        tv_activity_title.setText("记录");
        tv_activity_title_right = (TextView) flContent.findViewById(R.id.tv_activity_title_right);
        tv_activity_title_right.setText("清空");
        tv_activity_title_right.setOnClickListener(this);


        last_listen_layout = (RelativeLayout) flContent.findViewById(R.id.last_listen_layout);//最近收听
        last_listen_text = (TextView) flContent.findViewById(R.id.last_listen_text);



        have_down_layout = (RelativeLayout) flContent.findViewById(R.id.have_down_layout);//已下载
        have_down_text = (TextView) flContent.findViewById(R.id.have_down_text);


        rec_one_line = flContent.findViewById(R.id.rec_one_line);
        rec_three_line = flContent.findViewById(R.id.rec_three_line);


        record_main_viewpager = (ViewPager) flContent.findViewById(R.id.record_main_viewpager);

        recordViewPagerAdapter = new RecordViewPagerAdapter(getChildFragmentManager());
        record_main_viewpager.setAdapter(recordViewPagerAdapter);

        last_listen_layout.setOnClickListener(this);
        have_down_layout.setOnClickListener(this);
        record_main_viewpager.setCurrentItem(0);
        record_main_viewpager
                .setOnPageChangeListener(new RecordMainbViewPagechangeListener());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.frame_record_main;
    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected int setActionBar() {
        return 0;
    }

    @Override
    protected void reload() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.last_listen_layout:
                cleanState();
                last_listen_text.setTextColor(getActivity().getResources().getColor(R.color.c14acf0));
                record_main_viewpager.setCurrentItem(0);
                rec_one_line.setVisibility(View.VISIBLE);
                break;
            case R.id.have_down_layout:
                cleanState();
                have_down_text.setTextColor(getActivity().getResources().getColor(R.color.c14acf0));
                record_main_viewpager.setCurrentItem(2);
                rec_three_line.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_activity_title_right:
                ListenDialog.makeListenDialog(mActivity, "提示", "您确定要清空收听记录吗？", true, "否", true, "是", new ListenDialog.CallBackListener() {
                    @Override
                    public void callback(ListenDialog dialog, int mark) {
                        dialog.dismiss();
                        if(mark == ListenDialog.RIGHT){
                            musicDBController.delete();
                            update();
                        }
                    }
                }).show();
                break;

            default:
                break;
        }
    }

    private void cleanState() {
        last_listen_text.setTextColor(getActivity().getResources().getColor(R.color.cbbbbbb));
        have_down_text.setTextColor(getActivity().getResources().getColor(R.color.cbbbbbb));
        rec_one_line.setVisibility(View.GONE);
        rec_three_line.setVisibility(View.GONE);
    }


    private class RecordMainbViewPagechangeListener implements ViewPager.OnPageChangeListener {


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
            cleanState();
            if (arg0 == 0) {
                last_listen_text.setTextColor(getActivity().getResources().getColor(R.color.c14acf0));
                rec_one_line.setVisibility(View.VISIBLE);
                tvRightVisible();
            } else if (arg0 == 1) {
                have_down_text.setTextColor(getActivity().getResources().getColor(R.color.c14acf0));
                rec_three_line.setVisibility(View.VISIBLE);
                tvRightGone();
            }


        }
    }
}
