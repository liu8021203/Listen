package com.ting.bookcity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.liu.learning.library.LoopViewPager;
import com.ting.R;
import com.ting.base.BaseFragment;
import com.ting.base.BaseObserver;
import com.ting.bean.HomeResult;
import com.ting.bean.Slider;
import com.ting.bean.home.HomeHotAnchorResult;
import com.ting.bean.home.HotRecommendResult;
import com.ting.bookcity.adapter.HomeAnchorAdapter;
import com.ting.bookcity.adapter.HomeCategoryAdapter;
import com.ting.bookcity.adapter.HomeSpecialAdapter;
import com.ting.bookcity.adapter.HotRankAdapter;
import com.ting.classification.ClassChilActivity;
import com.ting.common.AppData;
import com.ting.common.http.HttpService;
import com.ting.db.DBListenHistory;
import com.ting.play.BookDetailsActivity;
import com.ting.play.PlayActivity;
import com.ting.play.controller.MusicDBController;
import com.ting.search.SearchActivity;
import com.ting.util.UtilGlide;
import com.ting.util.UtilIntent;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;
import com.ting.view.GridItemDecoration;
import com.ting.view.MusicAnimView;
import com.ting.view.NoScrollGridLayoutManager;
import com.ting.welcome.MainActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 2017/8/1.
 */
public class HomeFragment extends BaseFragment implements OnRefreshListener {
    private MusicAnimView mMusicAnimView;
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private LoopViewPager mLoopViewPager;
    private TextView tvHotMore;
    private RecyclerView hotRecyclerView;
    private LinearLayout llDoushi;
    private TextView tvDoushiMore;
    private RecyclerView doushiRecyclerView;
    private LinearLayout llXuanhuan;
    private TextView tvXuanhuanMore;
    private RecyclerView xuanhuanRecyclerView;
    private LinearLayout llLingyi;
    private TextView tvLingyiMore;
    private RecyclerView lingyiRecyclerView;
    private LinearLayout llGuanchang;
    private TextView tvGuanchangMore;
    private RecyclerView guanchangRecyclerView;
    private LinearLayout llWanben;
    private TextView tvWanbenMore;
    private RecyclerView wanbenRecyclerView;
    private RelativeLayout rlHotExchange;
    private HotRankAdapter mHotRankAdapter;
    private HomeCategoryAdapter doushiAdapter;
    private HomeCategoryAdapter xuanhuanAdapter;
    private HomeCategoryAdapter lingyiAdapter;
    private HomeCategoryAdapter guanchangAdapter;
    private HomeCategoryAdapter wanbenAdapter;
    private RelativeLayout rlA1;
    private RelativeLayout rlA2;
    private RelativeLayout rlA3;
    private RelativeLayout rlA4;
    private RelativeLayout rlA5;
    private RelativeLayout rlA6;
    private RelativeLayout rlSearch;
    private RecyclerView specialRecycleView;
    private HomeSpecialAdapter mHomeSpecialAdapter;

    private int hotPage = 1;

    @Override
    protected void initView() {
        mMusicAnimView = flActionBar.findViewById(R.id.iv_play);
        mMusicAnimView.setOnClickListener(this);
        mSwipeToLoadLayout = flContent.findViewById(R.id.swipeToLoadLayout);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mLoopViewPager = flContent.findViewById(R.id.loop_pager);
        mLoopViewPager.setCycle(true);
        mLoopViewPager.setWheel(true);
        mLoopViewPager.setTime(4000);
        mLoopViewPager.setIndicatorRes(R.drawable.vector_banner_unselect, R.drawable.vector_banner_select);
        tvHotMore = flContent.findViewById(R.id.tv_hot_more);
        tvHotMore.setVisibility(View.GONE);
        hotRecyclerView = flContent.findViewById(R.id.hot_recycle_view);
        llDoushi = flContent.findViewById(R.id.ll_doushi);
        tvDoushiMore = flContent.findViewById(R.id.tv_doushi_more);

        doushiRecyclerView = flContent.findViewById(R.id.doushi_recycle_view);
        llXuanhuan = flContent.findViewById(R.id.ll_xuanhuan);
        tvXuanhuanMore = flContent.findViewById(R.id.tv_xuanhuan_more);

        xuanhuanRecyclerView = flContent.findViewById(R.id.xuanhuan_recycle_view);
        llLingyi = flContent.findViewById(R.id.ll_lingyi);
        tvLingyiMore = flContent.findViewById(R.id.tv_lingyi_more);

        lingyiRecyclerView = flContent.findViewById(R.id.lingyi_recycle_view);
        llGuanchang = flContent.findViewById(R.id.ll_guanchang);
        tvGuanchangMore = flContent.findViewById(R.id.tv_guanchang_more);

        guanchangRecyclerView = flContent.findViewById(R.id.guanchang_recycle_view);
        llWanben = flContent.findViewById(R.id.ll_wanben);
        tvWanbenMore = flContent.findViewById(R.id.tv_wanben_more);

        wanbenRecyclerView = flContent.findViewById(R.id.wanben_recycle_view);
        NoScrollGridLayoutManager doushiManager = new NoScrollGridLayoutManager(mActivity, 4);
        doushiManager.setScrollEnabled(false);
        doushiRecyclerView.setLayoutManager(doushiManager);
        NoScrollGridLayoutManager xuanhuanManager = new NoScrollGridLayoutManager(mActivity, 4);
        xuanhuanManager.setScrollEnabled(false);
        xuanhuanRecyclerView.setLayoutManager(xuanhuanManager);
        NoScrollGridLayoutManager lingyiManager = new NoScrollGridLayoutManager(mActivity, 4);
        lingyiManager.setScrollEnabled(false);
        lingyiRecyclerView.setLayoutManager(lingyiManager);
        NoScrollGridLayoutManager guanchangManager = new NoScrollGridLayoutManager(mActivity, 4);
        guanchangManager.setScrollEnabled(false);
        guanchangRecyclerView.setLayoutManager(guanchangManager);
        NoScrollGridLayoutManager wanbenManager = new NoScrollGridLayoutManager(mActivity, 4);
        wanbenManager.setScrollEnabled(false);
        wanbenRecyclerView.setLayoutManager(wanbenManager);
        NoScrollGridLayoutManager hotManager = new NoScrollGridLayoutManager(mActivity, 4);
        hotManager.setScrollEnabled(false);
        hotRecyclerView.setLayoutManager(hotManager);
        NoScrollGridLayoutManager anchorManager = new NoScrollGridLayoutManager(mActivity, 3);
        anchorManager.setScrollEnabled(false);
        rlHotExchange = flContent.findViewById(R.id.rl_hot_exchange);
        rlHotExchange.setOnClickListener(this);
        rlSearch = flActionBar.findViewById(R.id.rl_search);
        rlSearch.setOnClickListener(this);
        specialRecycleView = flContent.findViewById(R.id.special_recycle_view);
        NoScrollGridLayoutManager manager = new NoScrollGridLayoutManager(getActivity(), 3);
        manager.setScrollEnabled(false);
        GridItemDecoration decoration1 = new GridItemDecoration(mActivity, R.drawable.money_grid, 3);
        specialRecycleView.addItemDecoration(decoration1);
        specialRecycleView.setLayoutManager(manager);
    }

    @Override
    protected void initData() {
        BaseObserver baseObserver = new BaseObserver<HomeResult>(this, false) {
            @Override
            public void success(HomeResult data) {
                super.success(data);
                mSwipeToLoadLayout.setRefreshing(false);
                mLoopViewPager.setData(data.getSlider(), new LoopViewPager.ImageListener<Slider>() {

                    @Override
                    public void onImageClick(Slider data) {
                        try {
                            if (Integer.parseInt(data.getType()) == 0) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("bookID", Integer.parseInt(data.getUrl()));
                                mActivity.intent(BookDetailsActivity.class, bundle);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void initData(ImageView view, Slider slider) {
                        UtilGlide.loadImg(mActivity, slider.getThumb(), view);
                    }
                });
                if (data.getSpecial() != null) {
                    specialRecycleView.setVisibility(View.VISIBLE);
                    if (mHomeSpecialAdapter == null) {
                        mHomeSpecialAdapter = new HomeSpecialAdapter((MainActivity) mActivity);
                        mHomeSpecialAdapter.setData(data.getSpecial());
                        specialRecycleView.setAdapter(mHomeSpecialAdapter);
                    } else {
                        mHomeSpecialAdapter.setData(data.getSpecial());
                        mHomeSpecialAdapter.notifyDataSetChanged();
                    }
                } else {
                    specialRecycleView.setVisibility(View.GONE);
                }
                if (mHotRankAdapter == null) {
                    mHotRankAdapter = new HotRankAdapter(mActivity);
                    mHotRankAdapter.setData(data.getSupermarket());
                    hotRecyclerView.setAdapter(mHotRankAdapter);
                } else {
                    mHotRankAdapter.setData(data.getSupermarket());
                    mHotRankAdapter.notifyDataSetChanged();
                }
                if (data.getBooklist() != null) {
                    if (data.getBooklist().getA7() != null && data.getBooklist().getA7().size() > 0) {
                        llDoushi.setVisibility(View.VISIBLE);
                        tvDoushiMore.setTag(data.getBooklist().getA7().get(0).getCatid());
                        tvDoushiMore.setOnClickListener(HomeFragment.this);
                        if (doushiAdapter == null) {
                            doushiAdapter = new HomeCategoryAdapter(mActivity);
                            doushiAdapter.setData(data.getBooklist().getA7());
                            doushiRecyclerView.setAdapter(doushiAdapter);
                        } else {
                            doushiAdapter.setData(data.getBooklist().getA7());
                            doushiAdapter.notifyDataSetChanged();
                        }
                    } else {
                        llDoushi.setVisibility(View.GONE);
                    }
                    if (data.getBooklist().getA6() != null && data.getBooklist().getA6().size() > 0) {
                        llXuanhuan.setVisibility(View.VISIBLE);
                        tvXuanhuanMore.setTag(data.getBooklist().getA6().get(0).getCatid());
                        tvXuanhuanMore.setOnClickListener(HomeFragment.this);
                        if (xuanhuanAdapter == null) {
                            xuanhuanAdapter = new HomeCategoryAdapter(mActivity);
                            xuanhuanAdapter.setData(data.getBooklist().getA6());
                            xuanhuanRecyclerView.setAdapter(xuanhuanAdapter);
                        } else {
                            xuanhuanAdapter.setData(data.getBooklist().getA6());
                            xuanhuanAdapter.notifyDataSetChanged();
                        }
                    } else {
                        llXuanhuan.setVisibility(View.GONE);
                    }
                    if (data.getBooklist().getA14() != null && data.getBooklist().getA14().size() > 0) {
                        llLingyi.setVisibility(View.VISIBLE);
                        tvLingyiMore.setTag(data.getBooklist().getA14().get(0).getCatid());
                        tvLingyiMore.setOnClickListener(HomeFragment.this);
                        if (lingyiAdapter == null) {
                            lingyiAdapter = new HomeCategoryAdapter(mActivity);
                            lingyiAdapter.setData(data.getBooklist().getA14());
                            lingyiRecyclerView.setAdapter(lingyiAdapter);
                        } else {
                            lingyiAdapter.setData(data.getBooklist().getA14());
                            lingyiAdapter.notifyDataSetChanged();
                        }
                    } else {
                        llLingyi.setVisibility(View.GONE);
                    }
                    if (data.getBooklist().getA9() != null && data.getBooklist().getA9().size() > 0) {
                        llGuanchang.setVisibility(View.VISIBLE);
                        tvGuanchangMore.setTag(data.getBooklist().getA9().get(0).getCatid());
                        tvGuanchangMore.setOnClickListener(HomeFragment.this);
                        if (guanchangAdapter == null) {
                            guanchangAdapter = new HomeCategoryAdapter(mActivity);
                            guanchangAdapter.setData(data.getBooklist().getA9());
                            guanchangRecyclerView.setAdapter(guanchangAdapter);
                        } else {
                            guanchangAdapter.setData(data.getBooklist().getA9());
                            guanchangAdapter.notifyDataSetChanged();
                        }
                    } else {
                        llGuanchang.setVisibility(View.GONE);
                    }
                    if (data.getBooklist().getA48() != null && data.getBooklist().getA48().size() > 0) {
                        llWanben.setVisibility(View.VISIBLE);
                        tvWanbenMore.setTag(data.getBooklist().getA48().get(0).getCatid());
                        tvWanbenMore.setOnClickListener(HomeFragment.this);
                        if (wanbenAdapter == null) {
                            wanbenAdapter = new HomeCategoryAdapter(mActivity);
                            wanbenAdapter.setData(data.getBooklist().getA48());
                            wanbenRecyclerView.setAdapter(wanbenAdapter);
                        } else {
                            wanbenAdapter.setData(data.getBooklist().getA48());
                            wanbenAdapter.notifyDataSetChanged();
                        }
                    } else {
                        llWanben.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void error() {
                super.error();
                mSwipeToLoadLayout.setRefreshing(false);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getIndexScencs3().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected int setActionBar() {
        return R.layout.home_actionbar;
    }

    @Override
    protected void reload() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_hot_exchange: {
                hotPage++;
                Map<String, String> map = new HashMap<>();
                map.put("page", String.valueOf(hotPage));
                map.put("count", "8");
                BaseObserver baseObserver = new BaseObserver<HotRecommendResult>() {
                    @Override
                    public void success(HotRecommendResult data) {
                        super.success(data);
                        if (mHotRankAdapter == null) {
                            mHotRankAdapter = new HotRankAdapter(mActivity);
                            mHotRankAdapter.setData(data.getData());
                            hotRecyclerView.setAdapter(mHotRankAdapter);
                        } else {
                            mHotRankAdapter.setData(data.getData());
                            mHotRankAdapter.notifyDataSetChanged();
                        }
                        hotPage = data.getPage();
                    }

                    @Override
                    public void error() {
                        hotPage--;
                        super.error();
                    }
                };
                mActivity.mDisposable.add(baseObserver);
                UtilRetrofit.getInstance().create(HttpService.class).index_book_supermarket(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
            }

            break;

            case R.id.tv_doushi_more: {
                Bundle bundle = new Bundle();
                bundle.putInt("classItemID", (Integer) v.getTag());
                bundle.putString("classItemName", "都市言情");
                mActivity.intent(ClassChilActivity.class, bundle);
            }
            break;

            case R.id.tv_xuanhuan_more: {
                Bundle bundle = new Bundle();
                bundle.putInt("classItemID", (Integer) v.getTag());
                bundle.putString("classItemName", "玄幻穿越");
                mActivity.intent(ClassChilActivity.class, bundle);
            }
            break;

            case R.id.tv_lingyi_more: {
                Bundle bundle = new Bundle();
                bundle.putInt("classItemID", (Integer) v.getTag());
                bundle.putString("classItemName", "灵异悬疑");
                mActivity.intent(ClassChilActivity.class, bundle);
            }
            break;

            case R.id.tv_wanben_more: {
                Bundle bundle = new Bundle();
                bundle.putInt("classItemID", (Integer) v.getTag());
                bundle.putString("classItemName", "最佳完本");
                mActivity.intent(ClassChilActivity.class, bundle);
            }
            break;
            case R.id.tv_guanchang_more: {
                Bundle bundle = new Bundle();
                bundle.putInt("classItemID", (Integer) v.getTag());
                bundle.putString("classItemName", "官场职场");
                mActivity.intent(ClassChilActivity.class, bundle);
            }
            break;

            case R.id.rl_search:
                mActivity.intent(SearchActivity.class);
                break;
            case R.id.iv_play: {
                int bookId = -1;
                boolean isPlay = false;
                if (AppData.currPlayBookId != -1) {
                    bookId = AppData.currPlayBookId;
                } else {
                    MusicDBController controller = new MusicDBController();
                    List<DBListenHistory> historys = controller.getListenHistory();
                    if (historys != null && !historys.isEmpty()) {
                        DBListenHistory history = historys.get(0);
                        bookId = history.getBookid();
                        isPlay = true;
                    }
                }
                if (bookId != -1) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("bookID", bookId);
                    bundle.putBoolean("play", isPlay);
                    UtilIntent.intentDIYBottomToTop(mActivity, PlayActivity.class, bundle);
                } else {
                    mActivity.showToast("请此时");
                }
            }
            break;
        }
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AppData.isPlaying){
            mMusicAnimView.start();
        }else{
            mMusicAnimView.stop();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if(AppData.isPlaying){
                mMusicAnimView.start();
            }else{
                mMusicAnimView.stop();
            }
        }else{
            mMusicAnimView.stop();
        }
    }

    public void statAnim(){
        mMusicAnimView.start();
    }

    public void stopAnim(){
        mMusicAnimView.stop();
    }
}
