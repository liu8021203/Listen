package com.ting.bookcity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.liu.learning.library.LoopViewPager;
import com.ting.R;
import com.ting.anchor.HostListActivity;
import com.ting.base.BaseFragment;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.HomeResult;
import com.ting.bean.vo.LoopVO;
import com.ting.bookcity.adapter.HomeTeamAdapter;
import com.ting.bookcity.adapter.HostAdapter;
import com.ting.bookcity.adapter.HotRankAdapter;
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
import com.ting.view.MusicAnimView;
import com.ting.view.NoScrollGridLayoutManager;

import java.util.List;

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
    private RelativeLayout rlHot;
    private RelativeLayout rlTeam;
    private HotRankAdapter mHotRankAdapter;
    private HotRankAdapter mSpecialAdapter;
    private HomeTeamAdapter mTeamAdapter;
    private RecyclerView hostRecyclerView;
    private RelativeLayout rlHost;
    private HostAdapter mHostAdapter;
    private RelativeLayout rlSpecial;
    private RelativeLayout rlSearch;
    private RecyclerView specialRecycleView;
    private RecyclerView teamRecyclerView;
    private TextView tvHostMore;

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
        tvHotMore.setOnClickListener(this);
        hotRecyclerView = flContent.findViewById(R.id.hot_recycle_view);
        NoScrollGridLayoutManager hotManager = new NoScrollGridLayoutManager(mActivity, 4);
        hotManager.setScrollEnabled(false);
        hotRecyclerView.setLayoutManager(hotManager);
        NoScrollGridLayoutManager anchorManager = new NoScrollGridLayoutManager(mActivity, 3);
        anchorManager.setScrollEnabled(false);
        rlSearch = flActionBar.findViewById(R.id.rl_search);
        rlSearch.setOnClickListener(this);
        specialRecycleView = flContent.findViewById(R.id.special_recycler_view);
        NoScrollGridLayoutManager manager = new NoScrollGridLayoutManager(getActivity(), 4);
        manager.setScrollEnabled(false);
        specialRecycleView.setLayoutManager(manager);
        hostRecyclerView = flContent.findViewById(R.id.host_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hostRecyclerView.setLayoutManager(linearLayoutManager);
        teamRecyclerView = flContent.findViewById(R.id.team_recycler_view);
        NoScrollGridLayoutManager managerTeam = new NoScrollGridLayoutManager(getActivity(), 4);
        teamRecyclerView.setLayoutManager(managerTeam);
        rlHost = flContent.findViewById(R.id.rl_host);
        rlHot = flContent.findViewById(R.id.rl_hot);
        rlSpecial = flContent.findViewById(R.id.rl_special);
        rlTeam = flContent.findViewById(R.id.rl_team);
        tvHostMore = flContent.findViewById(R.id.tv_host_more);
        tvHostMore.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        BaseObserver baseObserver = new BaseObserver<BaseResult<HomeResult>>(this, BaseObserver.MODEL_ALL) {
            @Override
            public void success(BaseResult<HomeResult> data) {
                super.success(data);
                mSwipeToLoadLayout.setRefreshing(false);
                HomeResult result = data.getData();
                if (result != null) {
                    mLoopViewPager.setData(result.getLoop(), new LoopViewPager.ImageListener<LoopVO>() {

                        @Override
                        public void onImageClick(LoopVO data) {
                            try {
                                if (Integer.parseInt(data.getType()) == 1) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("bookId", data.getUrl());
                                    mActivity.intent(BookDetailsActivity.class, bundle);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void initData(ImageView view, LoopVO slider) {
                            UtilGlide.loadImg(mActivity, slider.getImage(), view);
                        }
                    });
                    if (result.getHostData() != null && !result.getHostData().isEmpty()) {
                        if (mHostAdapter == null) {
                            mHostAdapter = new HostAdapter(mActivity);
                            mHostAdapter.setData(result.getHostData());
                            hostRecyclerView.setAdapter(mHostAdapter);
                        } else {
                            mHostAdapter.setData(result.getHostData());
                            mHostAdapter.notifyDataSetChanged();
                        }
                    } else {
                        rlHost.setVisibility(View.GONE);
                        hostRecyclerView.setVisibility(View.GONE);
                    }
                    if (result.getBest() != null && result.getBest().getList() != null && !result.getBest().getList().isEmpty()) {
                        tvHotMore.setTag(result.getBest().getId());
                        if (mHotRankAdapter == null) {
                            mHotRankAdapter = new HotRankAdapter(mActivity);
                            mHotRankAdapter.setData(result.getBest().getList());
                            hotRecyclerView.setAdapter(mHotRankAdapter);
                        } else {
                            mHotRankAdapter.setData(result.getBest().getList());
                            mHotRankAdapter.notifyDataSetChanged();
                        }
                    } else {
                        rlHot.setVisibility(View.GONE);
                        hotRecyclerView.setVisibility(View.GONE);
                    }


                    if (result.getFree() != null && result.getFree().getList() != null && !result.getFree().getList().isEmpty()) {
                        tvHotMore.setTag(result.getBest().getId());
                        if (mSpecialAdapter == null) {
                            mSpecialAdapter = new HotRankAdapter(mActivity);
                            mSpecialAdapter.setData(result.getFree().getList());
                            specialRecycleView.setAdapter(mSpecialAdapter);
                        } else {
                            mSpecialAdapter.setData(result.getFree().getList());
                            mSpecialAdapter.notifyDataSetChanged();
                        }
                    } else {
                        rlSpecial.setVisibility(View.GONE);
                        specialRecycleView.setVisibility(View.GONE);
                    }
                    if (result.getTeam() != null && !result.getTeam().isEmpty()) {
                        if (mTeamAdapter == null) {
                            mTeamAdapter = new HomeTeamAdapter(mActivity);
                            mTeamAdapter.setData(result.getTeam());
                            teamRecyclerView.setAdapter(mTeamAdapter);
                        } else {
                            mTeamAdapter.setData(result.getTeam());
                            mTeamAdapter.notifyDataSetChanged();
                        }
                    } else {
                        rlTeam.setVisibility(View.GONE);
                        teamRecyclerView.setVisibility(View.GONE);
                    }
                }
            }


            @Override
            public void error(BaseResult<HomeResult> value, Throwable e) {
                super.error(value, e);
                mSwipeToLoadLayout.setRefreshing(false);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).appHome().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
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
        initData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {


            case R.id.rl_search:
                mActivity.intent(SearchActivity.class);
                break;
            case R.id.iv_play: {
                String bookId = null;
                MusicDBController controller = new MusicDBController();
                List<DBListenHistory> historys = controller.getListenHistory();
                if (historys != null && !historys.isEmpty()) {
                    DBListenHistory history = historys.get(0);
                    bookId = history.getBookId();
                }
                if (bookId != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("bookId", bookId);
                    bundle.putBoolean("play", !AppData.isPlaying);
                    UtilIntent.intentDIYBottomToTop(mActivity, PlayActivity.class, bundle);
                } else {
                    mActivity.showToast("快去书城收听喜欢的书籍吧！！！");
                }
            }
            break;

            case R.id.tv_host_more:
                mActivity.intent(HostListActivity.class);
                break;

            case R.id.tv_hot_more:
                int activityId = (int) tvHotMore.getTag();
                Bundle bundle = new Bundle();
                bundle.putString("activityId", String.valueOf(activityId));
                mActivity.intent(HotRecommenActivity.class, bundle);
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
        if (AppData.isPlaying) {
            mMusicAnimView.start();
        } else {
            mMusicAnimView.stop();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (AppData.isPlaying) {
                mMusicAnimView.start();
            } else {
                mMusicAnimView.stop();
            }
        } else {
            mMusicAnimView.stop();
        }
    }

    public void statAnim() {
        mMusicAnimView.start();
    }

    public void stopAnim() {
        mMusicAnimView.stop();
    }
}
