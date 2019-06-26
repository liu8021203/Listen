package com.ting.play.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ting.bean.play.PlayResult;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.db.DBChapter;
import com.ting.db.DBListenHistory;
import com.ting.download.DownloadController;
import com.ting.play.PlayActivity;
import com.ting.play.adapter.PlayListAdapter;
import com.ting.play.controller.MusicDBController;
import com.ting.util.UtilNetStatus;
import com.ting.util.UtilPixelTransfrom;
import com.ting.util.UtilRetrofit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 2017/12/21.
 */

public class PlayListDialog extends Dialog {
    private RecyclerView mRecyclerView;
    private TextView tvChapterNum;
    private ImageView ivClose;
    private SmartRefreshLayout mSmartRefreshLayout;
    private PlayListAdapter mAdapter;
    private PlayActivity mActivity;
    private String bookId;
    private int page;
    private int nextPage;
    private int previousPage;
    private LinearLayout llLoading;

    public PlayListDialog(@NonNull Context context) {
        super(context, R.style.PlayListDialog);
        this.mActivity = (PlayActivity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_play_list);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initView();
    }


    private void initView() {
        Log.d("aaa", "PlayListDialog初始化");
        mSmartRefreshLayout = findViewById(R.id.refreshLayout);
        tvChapterNum = findViewById(R.id.tv_chapter_num);

        ivClose = findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ClassicsFooter footer = findViewById(R.id.footer);
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
        if (UtilNetStatus.isHasConnection(mActivity)) {
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
        } else {
            mSmartRefreshLayout.setEnableRefresh(false);
            mSmartRefreshLayout.setEnableLoadmore(false);
        }

        mRecyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        llLoading = findViewById(R.id.ll_loading);
    }

    public void setData(String bookId) {
        this.bookId = bookId;

    }

    public void getData(final int type, final int page) {
        if (UtilNetStatus.isHasConnection(mActivity)) {
            Map<String, String> map = new HashMap<>();
            map.put("page", String.valueOf(page));
            map.put("size", "50");
            map.put("bookId", bookId);
            if (TokenManager.isLogin(mActivity)) {
                map.put("uid", TokenManager.getUid(mActivity));
            }
            BaseObserver baseObserver = new BaseObserver<BaseResult<ChapterResult>>() {
                @Override
                public void success(BaseResult<ChapterResult> data) {
                    super.success(data);
                    ChapterResult result = data.getData();
                    if (result != null && result.getList() != null && !result.getList().isEmpty()) {
                        tvChapterNum.setText("共" + result.getCount() + "集");
                        int page = result.getPageNo();
                        int count = result.getPageSize();
                        switch (type) {

                            case 0:
                                llLoading.setVisibility(View.GONE);
                                mSmartRefreshLayout.setVisibility(View.VISIBLE);
                                mAdapter = new PlayListAdapter(mActivity, bookId, null);
                                mAdapter.setData(result.getList());
                                mRecyclerView.setAdapter(mAdapter);
                                nextPage = page;
                                previousPage = page;
                                tvChapterNum.setText("共" + result.getCount() + "集");
                                isLoadHeader();
                                isLoadFooter(result.getCount(), nextPage * 50);

                                break;
                            case 1:
                                mSmartRefreshLayout.finishRefresh(0);
                                mAdapter.addHeaderData(result.getList());
                                mAdapter.notifyDataSetChanged();
                                mRecyclerView.scrollBy(0, UtilPixelTransfrom.dip2px(mActivity, 48) * 49 + UtilPixelTransfrom.dip2px(mActivity, 48) / 2);
                                isLoadHeader();
                                break;
                            case 2:
                                mSmartRefreshLayout.finishLoadmore(0);
                                mAdapter.addFooterData(result.getList());
                                mAdapter.notifyDataSetChanged();
                                isLoadFooter(result.getCount(), page * count);
                                break;
                        }
                    }
                }

                @Override
                public void error(BaseResult<ChapterResult> value, Throwable e) {
                    super.error(value, e);
                    mSmartRefreshLayout.finishLoadmore(0);
                    mSmartRefreshLayout.finishRefresh(0);
                    if (type == 1) {
                        PlayListDialog.this.previousPage--;
                    }
                    if (type == 2) {
                        PlayListDialog.this.nextPage--;
                    }
                }
            };
            mActivity.mDisposable.add(baseObserver);
            UtilRetrofit.getInstance().create(HttpService.class).chapter(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
        } else {
            DownloadController controller = new DownloadController();
            List<DBChapter> data = controller.queryData(bookId, "4");
            llLoading.setVisibility(View.GONE);
            mSmartRefreshLayout.setVisibility(View.VISIBLE);
            mAdapter = new PlayListAdapter(mActivity, bookId, null);
            mAdapter.setData(data);
            mRecyclerView.setAdapter(mAdapter);
            nextPage = page;
            previousPage = page;
            tvChapterNum.setText("共" + data.size() + "集");
        }
    }


    @Override
    public void show() {
        super.show();
        llLoading.setVisibility(View.VISIBLE);
        mSmartRefreshLayout.setVisibility(View.GONE);
        List<DBListenHistory> data = MusicDBController.getListenHistoryByBookId(bookId);
        int page = 1;
        if (data != null && !data.isEmpty()) {
            page = (data.get(0).getPosition() - 1) / 50 + 1;
        }
        getData(0, page);
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


    public void unRegister() {
        if (mAdapter != null) {
            mAdapter.unregister();
        }
    }

    public void notifyPlayStateChange() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
