package com.ting.anchor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.anchor.dialog.GiftDialog;
import com.ting.bean.anchor.AnchorMessResult;
import com.ting.bean.anchor.LiWuResult;
import com.ting.anchor.adapter.AnchorMainadapter;
import com.ting.anchor.subview.AnchorWorkSubView;
import com.ting.anchor.subview.FollowAnchorSubView;
import com.ting.anchor.subview.RewardRankSubView;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.login.LoginMainActivity;
import com.ting.util.UtilGlide;
import com.ting.util.UtilPixelTransfrom;
import com.ting.util.UtilRetrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by gengjiajia on 15/8/29.
 * 主播
 */
public class AnchorMainActivity extends BaseActivity implements View.OnClickListener {
    private CircleImageView anchor_face_image;
    private TextView anchor_introduce_name;
    private TextView anchor_introduce_text;
    private ImageView ivBack;
    private LinearLayout dashang_layout1;
    private LinearLayout follow_anchor_layout1;
    private TabLayout mTabLayout;
    private Map<String, String> map = new HashMap<String, String>();
    private TextView anchor_fource_text;
    private ArrayList<View> anchorSubViews = new ArrayList<>();
    private ViewPager anchor_main_viewpager;
    private AnchorWorkSubView anchorWorkSubView;
    private FollowAnchorSubView followAnchorSubView;
    private RewardRankSubView rewardRankSubView;
    private String anchorID;
    private TextView tv_anchor_rank;
    private AppBarLayout mAppBarLayout;
    private RelativeLayout rlActiobar;
    private TextView tvTitle;
    private View line;
    private int height = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.anchorID = bundle.getString("anchorId");
        } else {
            showToast("数据加载失败");
            return;
        }
        setContentView(R.layout.activity_anchor_main2);
    }

    @Override
    protected String setTitle() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void getDate() {
        BaseObserver baseObserver = new BaseObserver<AnchorMessResult>(mActivity) {
            @Override
            public void success(AnchorMessResult data) {
                super.success(data);
                tv_anchor_rank.setText(data.getRankname());
                if (data.isFollowed()) {
                    anchor_fource_text.setText("已关注");
                } else {
                    anchor_fource_text.setText("关注");
                }
                UtilGlide.loadAnchorImg(mActivity, data.getThumb(), anchor_face_image);
                anchor_introduce_name.setText(data.getName());
                tvTitle.setText("主播-" + data.getName());
                if (!TextUtils.isEmpty(data.getDeclaration())) {
                    anchor_introduce_text.setText(data.getDeclaration());
                } else {
                    anchor_introduce_text.setText("主人很懒，什么都没留下");
                }

                anchorWorkSubView.setData(data.getWorks(), anchorID);
                rewardRankSubView.setData(data.getReward(), anchorID);
            }

            @Override
            public void error() {
                super.error();
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getBroadcasterInfo(TokenManager.getUid(mActivity), anchorID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    @Override
    protected void initView() {
        height = UtilPixelTransfrom.dip2px(this, 216 - 46);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        line = flContent.findViewById(R.id.actiobar_line);
        tvTitle = flContent.findViewById(R.id.tv_title);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        anchor_face_image = (CircleImageView) findViewById(R.id.anchor_face_image);//主播头像
        anchor_introduce_name = (TextView) findViewById(R.id.anchor_introduce_name);//主播名字
        anchor_introduce_text = (TextView) findViewById(R.id.anchor_introduce_text);//主播介绍
        tv_anchor_rank = (TextView) findViewById(R.id.tv_anchor_rank);
        dashang_layout1 = (LinearLayout) findViewById(R.id.dashang_layout);//打赏催更
        follow_anchor_layout1 = (LinearLayout) findViewById(R.id.follow_anchor_layout);//关注主播
        anchor_fource_text = (TextView) findViewById(R.id.anchor_fource_text);
        dashang_layout1.setOnClickListener(this);
        follow_anchor_layout1.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        anchor_main_viewpager = (ViewPager) findViewById(R.id.anchor_main_viewpager);
        mTabLayout.setupWithViewPager(anchor_main_viewpager);
        setViewPager();
        mAppBarLayout = flContent.findViewById(R.id.appbar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("aaa", "verticalOffset=======" + verticalOffset);
                int offset = Math.abs(verticalOffset);
                if (offset < height) {
                    float scale = (float) offset / height;
                    float alpha = 255 * scale;
                    Log.d("aaa", "scale======" + scale);
                    rlActiobar.setBackgroundColor(Color.argb((int)alpha, 248, 248, 248));
                    int rgb = (int) ((float)(height - offset) / height * (255 - 51));
                    if(offset < 10){
                        ivBack.setColorFilter(Color.argb(255, 255, 255, 255));
                    }else {
                        ivBack.setColorFilter(Color.argb(255, rgb, rgb, rgb));
                    }
                    line.setVisibility(View.GONE);
                    tvTitle.setVisibility(View.GONE);
                } else {
                    rlActiobar.setBackgroundColor(Color.argb(255, 248, 248, 248));
                    ivBack.setColorFilter(Color.argb(255, 51, 51, 51));
                    line.setVisibility(View.VISIBLE);
                    tvTitle.setVisibility(View.VISIBLE);
                }
            }
        });
        rlActiobar = flContent.findViewById(R.id.rl_actionbar);
    }

    @Override
    protected void initData() {
        getDate();
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.follow_anchor_layout:
                if (!TokenManager.isLogin(mActivity)) {
                    intent(LoginMainActivity.class);
                } else {
                    if (anchor_fource_text.getText().toString().equals("关注")) {
                        forceAnchor();
                    } else {
                        cancleAnchor();
                    }

                }
                break;
            case R.id.dashang_layout:
                if (!TokenManager.isLogin(this)) {
                    intent(LoginMainActivity.class);
                } else {
                    if (AppData.liWuResult != null) {
                        GiftDialog dialog = new GiftDialog(this);
                        dialog.setAnchorId(Integer.valueOf(anchorID));
                        dialog.show();
                    } else {
                        BaseObserver baseObserver = new BaseObserver<LiWuResult>(this) {
                            @Override
                            public void success(LiWuResult data) {
                                super.success(data);
                                AppData.liWuResult = data;
                                GiftDialog dialog = new GiftDialog(mActivity);
                                dialog.setAnchorId(Integer.valueOf(anchorID));
                                dialog.show();
                            }

                            @Override
                            public void error() {
                                showToast("礼物数据获取失败，请重试");
                            }
                        };
                        mDisposable.add(baseObserver);
                        UtilRetrofit.getInstance().create(HttpService.class).getRewardSymbol().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
                    }
                }


                break;
            case R.id.iv_back:
                finishAnim(this);
                break;
            default:
                break;

        }
    }

    /**
     * 关 主播
     */
    private void forceAnchor() {
        map.put("uid", TokenManager.getUid(this));
        map.put("bid", anchorID);
        map.put("op", "focus");
        BaseObserver baseObserver = new BaseObserver<BaseResult>(mActivity) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                anchor_fource_text.setText("已关注");
            }

            @Override
            public void error() {
                showToast("出错了，请重新关注");
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).setFocus(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    /**
     * 取 关 主播
     */
    private void cancleAnchor() {
        map.put("uid", TokenManager.getUid(this));
        map.put("bid", anchorID);
        map.put("op", "cancel");
        BaseObserver baseObserver = new BaseObserver<BaseResult>(mActivity) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                anchor_fource_text.setText("关注");
            }

            @Override
            public void error() {
                showToast("出错了，请重新取消");
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).setFocus(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    /**
     * 往里面添加viewPager数据
     */
    private void setViewPager() {
        anchorWorkSubView = new AnchorWorkSubView(this);
//        followAnchorSubView = new FollowAnchorSubView(this);
        rewardRankSubView = new RewardRankSubView(this);
        anchorSubViews.add(anchorWorkSubView);
//        anchorSubViews.add(followAnchorSubView);
        anchorSubViews.add(rewardRankSubView);
        AnchorMainadapter anchorMainadapter = new AnchorMainadapter(anchorSubViews);
        anchor_main_viewpager.setAdapter(anchorMainadapter);
        anchor_main_viewpager.setCurrentItem(0);
    }


}
