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
import com.ting.bean.HostDetailResult;
import com.ting.bean.anchor.AnchorMessResult;
import com.ting.bean.anchor.LiWuResult;
import com.ting.anchor.adapter.AnchorMainadapter;
import com.ting.anchor.subview.AnchorWorkSubView;
import com.ting.anchor.subview.FollowAnchorSubView;
import com.ting.anchor.subview.RewardRankSubView;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.vo.GiftVO;
import com.ting.common.AppData;
import com.ting.common.GiftManager;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.login.LoginMainActivity;
import com.ting.util.UtilGlide;
import com.ting.util.UtilPixelTransfrom;
import com.ting.util.UtilRetrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private ImageView ivBack;
    private LinearLayout dashang_layout1;
    private LinearLayout follow_anchor_layout1;
    private TabLayout mTabLayout;
    private Map<String, String> map = new HashMap<String, String>();
    private TextView anchor_fource_text;
    private ArrayList<View> anchorSubViews = new ArrayList<>();
    private ViewPager anchor_main_viewpager;
    private AnchorWorkSubView anchorWorkSubView;
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
        Map<String, String> map = new HashMap<>();
        map.put("hostId", anchorID);
        if(TokenManager.isLogin(this)){
            map.put("uid", TokenManager.getUid(this));
        }
        BaseObserver baseObserver = new BaseObserver<BaseResult<HostDetailResult>>(mActivity) {
            @Override
            public void success(BaseResult<HostDetailResult> data) {
                super.success(data);
                HostDetailResult result = data.getData();
                if (result.getHostData().getFocus() == 0) {
                    anchor_fource_text.setText("已关注");
                    anchor_fource_text.setTextColor(0xff666666);
                    anchor_fource_text.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.anchor_focus, 0,0,0);
                } else {
                    anchor_fource_text.setText("关注");
                    anchor_fource_text.setTextColor(0xff46bafc);
                    anchor_fource_text.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.anchor_unfocus, 0,0,0);
                }
                anchor_fource_text.setTag(result.getHostData().getFocus());
                UtilGlide.loadAnchorImg(mActivity, result.getHostData().getUserImage(), anchor_face_image);
                anchor_introduce_name.setText(result.getHostData().getNickname());
                tvTitle.setText("主播-" + result.getHostData().getNickname());
                anchorWorkSubView = new AnchorWorkSubView((AnchorMainActivity) mActivity);
                anchorWorkSubView.setData(result.getBookData());
                rewardRankSubView = new RewardRankSubView((AnchorMainActivity) mActivity);
                rewardRankSubView.setData(result.getGiftData());
                anchorSubViews.add(anchorWorkSubView);
                anchorSubViews.add(rewardRankSubView);
                AnchorMainadapter anchorMainadapter = new AnchorMainadapter(anchorSubViews);
                anchor_main_viewpager.setAdapter(anchorMainadapter);
                anchor_main_viewpager.setCurrentItem(0);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).hostDetail(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    @Override
    protected void initView() {
        height = UtilPixelTransfrom.dip2px(this, 216 - 46);
        ivBack =  findViewById(R.id.iv_back);
        line = flContent.findViewById(R.id.actiobar_line);
        tvTitle = flContent.findViewById(R.id.tv_title);
        mTabLayout =  findViewById(R.id.tab_layout);
        anchor_face_image =  findViewById(R.id.anchor_face_image);//主播头像
        anchor_introduce_name =  findViewById(R.id.anchor_introduce_name);//主播名字
        tv_anchor_rank =  findViewById(R.id.tv_anchor_rank);
        dashang_layout1 =  findViewById(R.id.dashang_layout);//打赏催更
        follow_anchor_layout1 =  findViewById(R.id.follow_anchor_layout);//关注主播
        anchor_fource_text =  findViewById(R.id.anchor_fource_text);
        dashang_layout1.setOnClickListener(this);
        follow_anchor_layout1.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        anchor_main_viewpager =  findViewById(R.id.anchor_main_viewpager);
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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.anchorID = bundle.getString("anchorId");
        } else {
            showToast("数据加载失败");
            return;
        }
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
                    int focus = (int) anchor_fource_text.getTag();
                    if (focus == 1) {
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
                    if (GiftManager.getGifts() != null) {
                        GiftDialog dialog = new GiftDialog(this);
                        dialog.setHostId(anchorID);
                        dialog.show();
                    } else {
                        BaseObserver baseObserver = new BaseObserver<BaseResult<List<GiftVO>>>(this) {
                            @Override
                            public void success(BaseResult<List<GiftVO>> data) {
                                super.success(data);
                                GiftManager.setGifts(data.getData());
                                GiftDialog dialog = new GiftDialog(mActivity);
                                dialog.setHostId(anchorID);
                                dialog.show();
                            }
                        };
                        mDisposable.add(baseObserver);
                        UtilRetrofit.getInstance().create(HttpService.class).gift().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
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
        map.put("hostId", anchorID);
        BaseObserver baseObserver = new BaseObserver<BaseResult>(mActivity, BaseObserver.MODEL_SHOW_DIALOG_TOAST) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                anchor_fource_text.setText("已关注");
                anchor_fource_text.setTag(0);
                anchor_fource_text.setTextColor(0xff666666);
                anchor_fource_text.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.anchor_focus, 0,0,0);
                mActivity.showToast("关注成功");
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).focusHost(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    /**
     * 取 关 主播
     */
    private void cancleAnchor() {
        map.put("uid", TokenManager.getUid(this));
        map.put("hostId", anchorID);
        BaseObserver baseObserver = new BaseObserver<BaseResult>(mActivity, BaseObserver.MODEL_SHOW_DIALOG_TOAST) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                anchor_fource_text.setText("关注");
                anchor_fource_text.setTag(1);
                anchor_fource_text.setTextColor(0xff46bafc);
                anchor_fource_text.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.anchor_unfocus, 0,0,0);
                mActivity.showToast("取消关注");
            }

        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).cancleFocusHost(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    /**
     * 往里面添加viewPager数据
     */
    private void setViewPager() {

    }


}
