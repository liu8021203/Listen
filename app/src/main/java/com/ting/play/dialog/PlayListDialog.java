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
import com.ting.bean.play.PlayResult;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.db.DBListenHistory;
import com.ting.play.PlayActivity;
import com.ting.play.adapter.ChapterScopeAdapter;
import com.ting.play.adapter.PlayListAdapter;
import com.ting.play.adapter.PlayListDialogAdapter;
import com.ting.play.controller.MusicDBController;
import com.ting.play.service.MusicService;
import com.ting.play.subview.PlayListSubView;
import com.ting.util.UtilPixelTransfrom;
import com.ting.util.UtilRetrofit;

import java.util.HashMap;
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
    private int bookId;
    private String bookName;
    private String bookHost;
    private String bookPic;
    private int price;
    private int nextPage;
    private int previousPage;

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

    public void setData(int bookId, String bookName, String bookHost, String bookPic, int price) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookHost = bookHost;
        this.bookPic = bookPic;
        this.price = price;
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

        mRecyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
    }


    public void getData(final int type, final int page) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("count", "50");
        map.put("bookID", String.valueOf(bookId));
        map.put("sort", "asc");
        map.put("type", "works");
        if (TokenManager.isLogin(mActivity)) {
            map.put("uid", TokenManager.getUid(mActivity));
        }
        BaseObserver baseObserver = new BaseObserver<PlayResult>() {
            @Override
            public void success(PlayResult data) {
                super.success(data);
                if (data != null && data.getData() != null && data.getData().getData() != null && data.getData().getData().size() > 0) {
                    tvChapterNum.setText("共" + data.getData().getLenght() + "集");
                    int page = data.getData().getPage();
                    int count = data.getData().getCount();
                    switch (type) {
                        case 0:
                            PlayListDialog.this.nextPage = data.getData().getPage();
                            PlayListDialog.this.previousPage = data.getData().getPage();

                            for (int i = 0; i < data.getData().getData().size(); i++) {
                                int position = (page - 1) * count + i;
                                data.getData().getData().get(i).setPosition(position);
                            }
                            if (mAdapter == null) {
                                mAdapter = new PlayListAdapter(mActivity, PlayListDialog.this, bookId);
                                mAdapter.setTingshuka(data.getTingshuka());
                                mAdapter.setBookInfo(data.getTitle(), data.getBroadercaster(), data.getThumb(), data.getPrice());
                                mAdapter.setData(data.getData().getData());
                                mRecyclerView.setAdapter(mAdapter);
                            } else {
                                mAdapter.setData(data.getData().getData());
                                mAdapter.notifyDataSetChanged();
                            }
                            isLoadHeader();
                            isLoadFooter(data.getData().getLenght(), page * count);
                            break;
                        case 1:
                            mSmartRefreshLayout.finishRefresh(0);
                            for (int i = 0; i < data.getData().getData().size(); i++) {
                                int position = (page - 1) * count + i;
                                data.getData().getData().get(i).setPosition(position);
                            }
                            mAdapter.addHeaderData(data.getData().getData());
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.scrollBy(0, UtilPixelTransfrom.dip2px(mActivity, 48) * 49 + UtilPixelTransfrom.dip2px(mActivity, 48) / 2);
                            isLoadHeader();
                            break;
                        case 2:
                            mSmartRefreshLayout.finishLoadmore(0);
                            for (int i = 0; i < data.getData().getData().size(); i++) {
                                int position = (page - 1) * count + i;
                                data.getData().getData().get(i).setPosition(position);
                            }
                            mAdapter.addFooterData(data.getData().getData());
                            mAdapter.notifyDataSetChanged();
                            isLoadFooter(data.getData().getLenght(), page * count);
                            break;
                    }
                }
            }

            @Override
            public void error() {
                super.error();
                if (type == 1) {
                    PlayListDialog.this.previousPage--;
                }
                if (type == 2) {
                    PlayListDialog.this.nextPage--;
                }
            }
        };
        mActivity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getPlayerList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    @Override
    public void show() {
        super.show();
        MusicDBController musicDBController = new MusicDBController();
        DBListenHistory mHistory = musicDBController.getBookIdData(String.valueOf(bookId));
        int position = 0;
        if (mHistory != null) {
            position = mHistory.getPosition();
        }
        Log.d("aaa", "PlayListDialog-------" + position);
        if (position == -1) {
            getData(0, 1);
        } else {
            int page = position / 50 + 1;
            getData(0, page);
        }
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
    public void dismiss() {
        super.dismiss();
        if(mAdapter != null){
            mAdapter.unregister();
        }
    }
}
