package com.ting.play.subview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ting.R;
import com.ting.base.BaseObserver;
import com.ting.bean.play.PlayingVO;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.db.DBChapter;
import com.ting.play.BookDetailsActivity;
import com.ting.play.adapter.ChapterScopeAdapter;
import com.ting.play.adapter.OfflinePlayListAdapter;
import com.ting.play.adapter.PlayListAdapter;
import com.ting.bean.play.chapterScopeVO;
import com.ting.play.dialog.PaiXuDialog;
import com.ting.bean.play.PlayResult;
import com.ting.util.UtilPixelTransfrom;
import com.ting.util.UtilRetrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 播放之列表界面
 */
public class PlayListSubView extends LinearLayout implements View.OnClickListener{
    static {
        ClassicsFooter.REFRESH_FOOTER_PULLUP = "上拉加载更多";
        ClassicsFooter.REFRESH_FOOTER_RELEASE = "释放立即加载";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "正在加载...";
        ClassicsFooter.REFRESH_FOOTER_LOADING = "正在加载...";
        ClassicsFooter.REFRESH_FOOTER_FINISH = "加载完成";
        ClassicsFooter.REFRESH_FOOTER_FAILED = "加载失败";
        ClassicsFooter.REFRESH_FOOTER_ALLLOADED = "全部加载完成";
    }

    static {
        ClassicsHeader.REFRESH_HEADER_PULLDOWN = "下拉加载章节";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "正在加载...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "正在加载...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "释放立即加载";
        ClassicsHeader.REFRESH_HEADER_FINISH = "加载完成";
        ClassicsHeader.REFRESH_HEADER_FAILED = "加载失败";
    }
    private SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerView mRecyclerView;
    private BookDetailsActivity activity;
    private LayoutInflater inflater;
    private View playIntroduceSubView;
    private PlayListAdapter playListAdapter;
    private OfflinePlayListAdapter adapter;
    private ChapterScopeAdapter scopeAdapter;
    private GridView griedView;
    private View view;
    private TextView all_list_play;
    private TextView paixun;
    private TextView tiaozhuan;
    private RecyclerView mOfflineRecyclerView;
    private Map<String, String> map = new HashMap<String, String>();
    private List<chapterScopeVO> scopeVOs = new ArrayList<>();
    private int nextPage = 1;
    private int previousPage = 1;

    public PlayListSubView(BookDetailsActivity activity) {
        super(activity);
        this.activity = activity;
        inflater = inflater.from(activity);
        playIntroduceSubView = inflater.inflate(R.layout.subview_play_list, this);
        initView();
    }

    private void initView() {
        mSmartRefreshLayout =  playIntroduceSubView.findViewById(R.id.refreshLayout);
        ClassicsFooter footer = playIntroduceSubView.findViewById(R.id.footer);
        footer.setTextSizeTitle(16);//设置标题文字大小（sp单位）
        footer.setFinishDuration(200);//设置刷新完成显示的停留时间
        footer.setDrawableSize(20);//同时设置箭头和图片的大小（dp单位）
        footer.setDrawableArrowSize(20);//设置箭头的大小（dp单位）
        footer.setDrawableProgressSize(20);//设置图片的大小（dp单位）
        footer.setDrawableMarginRight(20);//设置图片和箭头和文字的间距（dp单位）
        footer.setProgressResource(R.mipmap.header_loadding);//设置图片资源
        footer.setSpinnerStyle(SpinnerStyle.Translate);//设置状态（不支持：MatchLayout）

        ClassicsHeader header = (ClassicsHeader)findViewById(R.id.header);
        header.setTextSizeTitle(16);//设置标题文字大小（sp单位）
        header.setTextTimeMarginTop(10);//设置时间文字的上边距（dp单位）
        header.setEnableLastTime(false);//是否显示时间
        header.setFinishDuration(200);//设置刷新完成显示的停留时间
        header.setDrawableSize(20);//同时设置箭头和图片的大小（dp单位）
        header.setDrawableArrowSize(20);//设置箭头的大小（dp单位）
        header.setDrawableProgressSize(20);//设置图片的大小（dp单位）
        header.setDrawableMarginRight(20);//设置图片和箭头和文字的间距（dp单位）
        header.setProgressResource(R.mipmap.header_loadding);//设置图片资源
        header.setSpinnerStyle(SpinnerStyle.Translate);//设置状态（不支持：MatchLayout

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                previousPage--;
                getData(1, previousPage);
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                nextPage++;
                getData(2, nextPage);
            }
        });
        mRecyclerView =  playIntroduceSubView.findViewById(R.id.swipe_target);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(manager);
        all_list_play =  playIntroduceSubView.findViewById(R.id.all_list_play);//总共的集数
        paixun =  playIntroduceSubView.findViewById(R.id.paixun);//排序
        tiaozhuan =  playIntroduceSubView.findViewById(R.id.tiaozhuan);//跳转
        griedView =  playIntroduceSubView.findViewById(R.id.gridview);
        view = playIntroduceSubView.findViewById(R.id.view);
        mOfflineRecyclerView =  playIntroduceSubView.findViewById(R.id.offline_recycle_view);
        LinearLayoutManager offlineManager = new LinearLayoutManager(activity);
        mOfflineRecyclerView.setLayoutManager(offlineManager);
        view.setOnClickListener(this);
        paixun.setOnClickListener(this);
        tiaozhuan.setOnClickListener(this);
        if (activity.getType() == 1) {
            paixun.setVisibility(View.GONE);
            tiaozhuan.setVisibility(View.GONE);
        }
    }




    /**m
     * 设置离线数据
     * @param data
     */
    public void setOfflineData(List<DBChapter> data) {
        adapter = new OfflinePlayListAdapter(activity, activity.getBookId());
        adapter.setData(data);
        mSmartRefreshLayout.setVisibility(View.GONE);
        mOfflineRecyclerView.setVisibility(View.VISIBLE);
        mOfflineRecyclerView.setAdapter(adapter);
        tiaozhuan.setVisibility(View.GONE);
        paixun.setVisibility(View.GONE);
        all_list_play.setText("共" + data.size() + "集");
    }

    public int getPages() {
        return this.previousPage;
    }

    public void getData(final int type, final int page) {
        map.put("page", page + "");
        map.put("count", "50");
        map.put("bookID", activity.getBookId() + "");
        map.put("sort", activity.getSort());
        map.put("type", "works");
        if (TokenManager.isLogin(activity)) {
            map.put("uid", TokenManager.getUid(activity));
        }
        BaseObserver baseObserver = new BaseObserver<PlayResult>() {
            @Override
            public void success(PlayResult data) {
                super.success(data);
                if (data != null && data.getData() != null && data.getData().getData() != null && data.getData().getData().size() > 0) {
                    all_list_play.setText("共" + data.getData().getLenght() + "集");
                    int page = data.getData().getPage();
                    int count = data.getData().getCount();
                    int length = data.getData().getLenght();
                    switch (type){
                        case 0:
                            PlayListSubView.this.nextPage = data.getData().getPage();
                            PlayListSubView.this.previousPage = data.getData().getPage();

                            for (int i = 0; i < data.getData().getData().size(); i++) {
                                int position = -1;
                                if (activity.getSort().equals("asc")) {
                                    position = (page - 1) * count + i;
                                } else {
                                    position = length - ((page - 1) * count + i);
                                }
                                data.getData().getData().get(i).setPosition(position);
                            }
                            if (playListAdapter == null) {
                                playListAdapter = new PlayListAdapter(activity, PlayListSubView.this, activity.getBookId());
                                playListAdapter.setData(data.getData().getData());
                                playListAdapter.setTingshuka(data.getTingshuka());
                                playListAdapter.setBookInfo(data.getTitle(), data.getBroadercaster(), data.getThumb(), data.getPrice());
                                mRecyclerView.setAdapter(playListAdapter);
                            } else {
                                playListAdapter.setData(data.getData().getData());
                                playListAdapter.notifyDataSetChanged();
                            }
                            isLoadHeader();
                            isLoadFooter(data.getData().getLenght(), page * count);
                            activity.setChapterList(playListAdapter.getResult());
                            setScopeData(data.getData().getLenght());
                            if (scopeAdapter == null) {
                                scopeAdapter = new ChapterScopeAdapter(activity, PlayListSubView.this);
                                scopeAdapter.setData(scopeVOs);
                                griedView.setAdapter(scopeAdapter);
                            } else {
                                scopeAdapter.setData(scopeVOs);
                                scopeAdapter.notifyDataSetChanged();
                            }
                            activity.initInfo(data);

                            break;
                        case 1:
                            mSmartRefreshLayout.finishRefresh(0);
                            for (int i = 0; i < data.getData().getData().size(); i++) {
                                int position = -1;
                                if (activity.getSort().equals("asc")) {
                                    position = (page - 1) * count + i;
                                } else {
                                    position = length - ((page - 1) * count + i);
                                }
                                data.getData().getData().get(i).setPosition(position);
                            }
                            playListAdapter.addHeaderData(data.getData().getData());
                            playListAdapter.notifyDataSetChanged();
                            mRecyclerView.scrollBy(0, UtilPixelTransfrom.dip2px(activity, 48) * 49 + UtilPixelTransfrom.dip2px(activity, 48) / 2);
                            activity.setChapterList(playListAdapter.getResult());
                            isLoadHeader();
                            break;
                        case 2:
                            mSmartRefreshLayout.finishLoadmore(0);
                            for (int i = 0; i < data.getData().getData().size(); i++) {
                                int position = -1;
                                if (activity.getSort().equals("asc")) {
                                    position = (page - 1) * count + i;
                                } else {
                                    position = length - ((page - 1) * count + i);
                                }
                                data.getData().getData().get(i).setPosition(position);
                            }
                            playListAdapter.addFooterData(data.getData().getData());
                            playListAdapter.notifyDataSetChanged();
                            activity.setChapterList(playListAdapter.getResult());
                            isLoadFooter(data.getData().getLenght(), page * count);
                            break;
                    }
                }
            }

            @Override
            public void error() {
                super.error();
                if (type == 1) {
                    PlayListSubView.this.previousPage--;
                }
                if(type == 2){
                    PlayListSubView.this.nextPage--;
                }
            }
        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getPlayerList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    public void unregister() {
        if (playListAdapter != null) {
            playListAdapter.unregister();
        }
    }

    public void updateList() {
        if (activity.getType() == 0) {
            if (playListAdapter != null) {
                playListAdapter.notifyDataSetChanged();
            }
        } else {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }


    private void isLoadFooter(int total, int currTotal){
        if (total > currTotal) {
            mSmartRefreshLayout.setEnableLoadmore(true);
        } else {
            mSmartRefreshLayout.setEnableLoadmore(false);
        }
    }


    private void isLoadHeader(){
        if(previousPage > 1){
            mSmartRefreshLayout.setEnableRefresh(true);
        }else{
            mSmartRefreshLayout.setEnableRefresh(false);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paixun:
                PaiXuDialog paiXuDialog = new PaiXuDialog(activity);
                paiXuDialog.setListener(new PaiXuDialog.SortCallBackListener() {
                    @Override
                    public void callback() {
                        if(activity.getSort().equals("desc")){
                            paixun.setText("排序(反序)");
                        }else{
                            paixun.setText("排序(正序)");
                        }
                        getData(0,1);
                    }
                });
                paiXuDialog.show();
                break;
            case R.id.tiaozhuan: {
                if (griedView.getVisibility() == View.GONE) {
                    final int temp = scopeVOs.size() % 3;
                    int lines = scopeVOs.size() / 3;
                    int height = 0;
                    if (temp == 0) {
                        height = lines * UtilPixelTransfrom.dip2px(activity, 24);
                    } else {
                        height = (lines + 1) * UtilPixelTransfrom.dip2px(activity, 24);
                    }
                    griedView.setVisibility(View.VISIBLE);
                    griedView.getLayoutParams().height = 0;
                    griedView.requestLayout();
                    ViewWrapper wrapper = new ViewWrapper(griedView);
                    ObjectAnimator animator = ObjectAnimator.ofInt(wrapper, "height", height);
                    animator.setDuration(300);
                    animator.start();
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            tiaozhuan.setEnabled(false);
                            view.setEnabled(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            tiaozhuan.setEnabled(true);
                            view.setEnabled(true);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    view.setVisibility(View.VISIBLE);
                } else {
                    closeTiaozhuan();
                }
            }
            break;

            case R.id.view: {
                closeTiaozhuan();
            }
            break;
            default:
                break;
        }
    }

    /**
     * 关闭跳转
     */
    public void closeTiaozhuan() {
        ViewWrapper wrapper = new ViewWrapper(griedView);
        ObjectAnimator animator = ObjectAnimator.ofInt(wrapper, "height", 0);
        animator.setDuration(300);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setEnabled(false);
                tiaozhuan.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setEnabled(true);
                tiaozhuan.setEnabled(true);
                view.setVisibility(View.GONE);
                griedView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }




    private void setScopeData(int count) {
        scopeVOs.clear();
        if (count == 0) {
            return;
        } else {
            int num = count / 50;
            for (int i = 1; i <= num + 1; i++) {
                chapterScopeVO vo = new chapterScopeVO();
                vo.setPage(i);
                vo.setCount(count);
                vo.setName(((i - 1) * 50 + 1) + "~" + (i * 50));
                scopeVOs.add(vo);
            }
        }
    }




    private static class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View target) {
            mTarget = target;
        }

        public int getHeight() {
            return mTarget.getLayoutParams().height;
        }

        public void setHeight(int height) {
            mTarget.getLayoutParams().height = height;
            mTarget.requestLayout();
        }
    }
}
