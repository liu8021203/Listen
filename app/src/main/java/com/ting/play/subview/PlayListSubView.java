package com.ting.play.subview;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ting.R;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.ChapterResult;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.db.DBListenHistory;
import com.ting.play.BookDetailsActivity;
import com.ting.play.ChapterBuyActivity;
import com.ting.play.adapter.PlayListAdapter;
import com.ting.play.controller.MusicDBController;
import com.ting.play.dialog.ChapterSelectDialog;
import com.ting.util.UtilPixelTransfrom;
import com.ting.util.UtilRetrofit;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 播放之列表界面
 */
public class PlayListSubView extends LinearLayout implements View.OnClickListener {
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
    private ConstraintLayout networkErrorLayout;
    private Button btnReload;
    private BookDetailsActivity activity;
    private LayoutInflater inflater;
    private View playIntroduceSubView;
    private PlayListAdapter playListAdapter;
    private TextView all_list_play;
    private TextView tvBatchBuyChapter;
    private TextView tiaozhuan;
    private ProgressBar mProgressBar;
    private Map<String, String> map = new HashMap<String, String>();
    private int nextPage = 1;
    private int previousPage = 1;
    private int count = 0;
    private String bookId;
    private MusicDBController mMusicController = new MusicDBController();

    public PlayListSubView(BookDetailsActivity activity, String bookId) {
        super(activity);
        this.activity = activity;
        this.bookId = bookId;
        inflater = inflater.from(activity);
        playIntroduceSubView = inflater.inflate(R.layout.subview_play_list, this);
        initView();
        initData();
    }

    private void initData() {
        DBListenHistory mHistory = mMusicController.getBookIdData(String.valueOf(bookId));
        int page;
        if (mHistory != null) {
            int position = mHistory.getPosition();
            if (position == -1) {
                page = 1;
            } else {
                page = position / 50 + 1;
            }
        } else {
            page = 1;
        }
        getData(0, page);
    }

    private void initView() {
        mSmartRefreshLayout = playIntroduceSubView.findViewById(R.id.refreshLayout);
        ClassicsFooter footer = playIntroduceSubView.findViewById(R.id.footer);
        footer.setTextSizeTitle(16);//设置标题文字大小（sp单位）
        footer.setFinishDuration(200);//设置刷新完成显示的停留时间
        footer.setDrawableSize(20);//同时设置箭头和图片的大小（dp单位）
        footer.setDrawableArrowSize(20);//设置箭头的大小（dp单位）
        footer.setDrawableProgressSize(20);//设置图片的大小（dp单位）
        footer.setDrawableMarginRight(20);//设置图片和箭头和文字的间距（dp单位）
        footer.setProgressResource(R.mipmap.header_loadding);//设置图片资源
        footer.setSpinnerStyle(SpinnerStyle.Translate);//设置状态（不支持：MatchLayout）

        ClassicsHeader header = (ClassicsHeader) findViewById(R.id.header);
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
        mRecyclerView = playIntroduceSubView.findViewById(R.id.swipe_target);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(manager);
        all_list_play = playIntroduceSubView.findViewById(R.id.all_list_play);//总共的集数
        tiaozhuan = playIntroduceSubView.findViewById(R.id.tiaozhuan);//跳转
        tiaozhuan.setOnClickListener(this);
        networkErrorLayout = findViewById(R.id.network_error_layout);
        btnReload = findViewById(R.id.btn_reload);
        btnReload.setOnClickListener(this);
        mProgressBar = findViewById(R.id.progressBar);
        tvBatchBuyChapter = findViewById(R.id.tv_batch_chapter_buy);
        tvBatchBuyChapter.setOnClickListener(this);
    }


    public int getPages() {
        return this.previousPage;
    }

    public void getData(final int type, final int page) {
        map.put("page", String.valueOf(page));
        map.put("size", "50");
        map.put("bookId", bookId);
        map.put("sort", "asc");
        if (TokenManager.isLogin(activity)) {
            map.put("uid", TokenManager.getUid(activity));
        }
        if(type == 0){
            mProgressBar.setVisibility(View.VISIBLE);
            mSmartRefreshLayout.setVisibility(View.GONE);
            networkErrorLayout.setVisibility(View.GONE);
        }
        BaseObserver baseObserver = new BaseObserver<BaseResult<ChapterResult>>(activity, BaseObserver.MODEL_ONLY_SHOW_TOAST) {
            @Override
            public void success(BaseResult<ChapterResult> data) {
                super.success(data);
                ChapterResult result = data.getData();
                if (result.getList() != null && !result.getList().isEmpty()) {
                    all_list_play.setText("共" + result.getCount() + "集");
                    int page = result.getPageNo();
                    int size = result.getPageSize();
                    count = result.getCount();
                    switch (type) {
                        case 0:
                            if(mProgressBar.getVisibility() == View.VISIBLE){
                                mProgressBar.setVisibility(View.GONE);
                            }
                            if(networkErrorLayout.getVisibility() == View.VISIBLE){
                                networkErrorLayout.setVisibility(View.GONE);
                            }
                            if(mSmartRefreshLayout.getVisibility() == View.GONE){
                                mSmartRefreshLayout.setVisibility(View.VISIBLE);
                            }
                            PlayListSubView.this.nextPage = page;
                            PlayListSubView.this.previousPage = page;
                            if (playListAdapter == null) {
                                playListAdapter = new PlayListAdapter(activity, bookId, PlayListSubView.this);
                                playListAdapter.setData(result.getList());
                                playListAdapter.setCardData(result.getCardData());
                                mRecyclerView.setAdapter(playListAdapter);
                            } else {
                                playListAdapter.setData(result.getList());
                                playListAdapter.notifyDataSetChanged();
                            }
                            isLoadHeader();
                            isLoadFooter(result.getCount(), page * size);
                            break;
                        case 1:
                            mSmartRefreshLayout.finishRefresh(0);
                            playListAdapter.addHeaderData(result.getList());
                            playListAdapter.notifyDataSetChanged();
                            mRecyclerView.scrollBy(0, UtilPixelTransfrom.dip2px(activity, 48) * 49 + UtilPixelTransfrom.dip2px(activity, 48) / 2);
//                            activity.setChapterList(playListAdapter.getResult());
                            isLoadHeader();
                            break;
                        case 2:
                            mSmartRefreshLayout.finishLoadmore(0);
                            playListAdapter.addFooterData(result.getList());
                            playListAdapter.notifyDataSetChanged();
//                            activity.setChapterList(playListAdapter.getResult());
                            isLoadFooter(count, page * size);
                            break;
                    }
                }
            }

            @Override
            public void error(BaseResult<ChapterResult> value, Throwable e) {
                super.error(value, e);
                if (type == 0) {
                    networkErrorLayout.setVisibility(View.VISIBLE);
                    mSmartRefreshLayout.setVisibility(View.GONE);
                } else if (type == 1) {
                    PlayListSubView.this.previousPage--;
                } else if (type == 2) {
                    PlayListSubView.this.nextPage--;
                }
            }
        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).chapter(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    public void unregister() {
        if (playListAdapter != null) {
            playListAdapter.unregister();
        }
    }

    public void updateList() {
//        if (activity.getType() == 0) {
//            if (playListAdapter != null) {
//                playListAdapter.notifyDataSetChanged();
//            }
//        } else {
//
//        }
    }


    private void isLoadFooter(int total, int currTotal) {
        if (total > currTotal) {
            mSmartRefreshLayout.setEnableLoadmore(true);
        } else {
            mSmartRefreshLayout.setEnableLoadmore(false);
        }
    }


    private void isLoadHeader() {
        if (previousPage > 1) {
            mSmartRefreshLayout.setEnableRefresh(true);
        } else {
            mSmartRefreshLayout.setEnableRefresh(false);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tiaozhuan: {
                ChapterSelectDialog dialog = new ChapterSelectDialog(activity);
                dialog.setData(count);
                dialog.setListener(new ChapterSelectDialog.ChapterSelectCallBackListener() {
                    @Override
                    public void callback(int p) {
                        getData(0, p);
                    }
                });
                dialog.show();
            }
            break;


            case R.id.btn_reload:
                getData(0, nextPage);
                break;

            case R.id.tv_batch_chapter_buy:
                Bundle bundle = new Bundle();
                bundle.putString("bookId", bookId);
                activity.intent(ChapterBuyActivity.class, bundle);
                break;

            default:
                break;
        }
    }

    public int getNextPage() {
        return nextPage;
    }


    public void notifyPlayStateChange(){
        if(playListAdapter != null){
            playListAdapter.initHistory();
            playListAdapter.notifyDataSetChanged();
        }
    }
}
