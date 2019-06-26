package com.ting.play.subview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.ting.R;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.BookResult;
import com.ting.bean.CommentListResult;
import com.ting.bean.vo.CommentListVO;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.login.LoginMainActivity;
import com.ting.play.BookDetailsActivity;
import com.ting.play.adapter.CommentAdapter;
import com.ting.bean.play.MessageResult;
import com.ting.bean.play.CommentResult;
import com.ting.play.dialog.SendMessDialog;
import com.ting.util.UtilFileManage;
import com.ting.util.UtilGson;
import com.ting.util.UtilMD5Encryption;
import com.ting.util.UtilNetStatus;
import com.ting.util.UtilRetrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/9/6.
 * 播放之详情界面
 */
public class PlayIntroduceSubView extends LinearLayout implements OnLoadMoreListener, View.OnClickListener {
    private BookDetailsActivity activity;
    private View playIntroduceSubView;
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private RecyclerView mCommentRecycle;
    private CommentAdapter adapter;
    private int page = 1;//评论制定的查询页数;
    private String bookId;
    private TextView tvComment;

    public PlayIntroduceSubView(BookDetailsActivity activity, String bookId) {
        super(activity);
        this.activity = activity;
        this.bookId = bookId;
        playIntroduceSubView = LayoutInflater.from(activity).inflate(R.layout.subview_play_introduce, this);
        initView();
        initData();
    }

    private void initView() {
        mCommentRecycle = playIntroduceSubView.findViewById(R.id.swipe_target);
        mSwipeToLoadLayout = playIntroduceSubView.findViewById(R.id.swipeToLoadLayout);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        mCommentRecycle.setLayoutManager(manager);
        mCommentRecycle.setAdapter(adapter);
        tvComment = playIntroduceSubView.findViewById(R.id.tv_comment);
        tvComment.setOnClickListener(this);
    }

    public void initData() {
        if (UtilNetStatus.isHasConnection(activity)) {
            requestNetData(0);
        }
    }

    /**
     * 请求网络数据
     *
     * @param type
     */
    public void requestNetData(final int type) {
        Map<String, String> map = new HashMap<>();
        map.put("bookId", bookId);
        if (TokenManager.isLogin(activity)) {
            map.put("uid", TokenManager.getUid(activity));
        }
        BaseObserver baseObserver = new BaseObserver<BaseResult<BookResult>>(activity, false) {
            @Override
            public void success(BaseResult<BookResult> data) {
                super.success(data);
                mSwipeToLoadLayout.setLoadingMore(false);
                BookResult result = data.getData();
                if (adapter == null) {
                    adapter = new CommentAdapter(activity);
                    adapter.setData(result.getCommentData().getCommentList());
                    mCommentRecycle.setAdapter(adapter);
                } else {
                    if (type == 0) {
                        adapter.setData(result.getCommentData().getCommentList());
                    } else {
                        adapter.addData(result.getCommentData().getCommentList());
                    }
                    adapter.notifyDataSetChanged();
                }
                isPaging(result.getCommentData().getCount(), adapter.getItemCount() - 1);
            }


        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).book(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    /**
     * 加载更多评论数据
     */
    private void loadMoreComments() {
        Map<String, String> map = new HashMap<>();
        map.put("bookId", bookId);
        if (TokenManager.isLogin(activity)) {
            map.put("uid", TokenManager.getUid(activity));
        }
        map.put("page", String.valueOf(page));
        map.put("size", "10");
        BaseObserver baseObserver = new BaseObserver<BaseResult<CommentListResult>>(activity, BaseObserver.MODEL_ONLY_SHOW_TOAST) {
            @Override
            public void success(BaseResult<CommentListResult> data) {
                super.success(data);
                mSwipeToLoadLayout.setLoadingMore(false);
                CommentListResult result = data.getData();
                page = result.getPage();
                if (adapter != null) {
                    adapter.addData(result.getList());
                    adapter.notifyDataSetChanged();
                }
                isPaging(result.getCount(), adapter.getItemCount() - 1);
            }

            @Override
            public void error(BaseResult<CommentListResult> value, Throwable e) {
                super.error(value, e);
                mSwipeToLoadLayout.setLoadingMore(false);
                page--;
            }
        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).queryComments(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    /**
     * 是否分页
     */
    private void isPaging(int total, int currTotal) {
        if (total > currTotal) {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        }
    }

    @Override
    public void onLoadMore() {
        page++;
        loadMoreComments();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_comment:

                if (!TokenManager.isLogin(activity)) {
                    activity.intent(LoginMainActivity.class);
                } else {
                    SendMessDialog dialog = new SendMessDialog(activity);
                    dialog.setListener(new SendMessDialog.SendMessageCallBackListener() {
                        @Override
                        public void callback(CommentListVO vo) {
                            if(adapter != null){
                                adapter.addVO(vo);
                                adapter.notifyDataSetChanged();
                            }else{
                                List<CommentListVO> commentList = new ArrayList<>();
                                commentList.add(vo);
                                adapter = new CommentAdapter(activity);
                                adapter.setData(commentList);
                                mCommentRecycle.setAdapter(adapter);
                            }
                        }
                    });
                    dialog.setBookId(bookId);
                    dialog.show();
                }
                break;
        }
    }
}
