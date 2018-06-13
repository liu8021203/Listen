package com.ting.play.subview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.ting.R;
import com.ting.base.BaseObserver;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.play.BookDetailsActivity;
import com.ting.play.adapter.CommentAdapter;
import com.ting.bean.play.MessageResult;
import com.ting.bean.play.CommentResult;
import com.ting.util.UtilFileManage;
import com.ting.util.UtilGson;
import com.ting.util.UtilMD5Encryption;
import com.ting.util.UtilNetStatus;
import com.ting.util.UtilRetrofit;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/9/6.
 * 播放之详情界面
 */
public class PlayIntroduceSubView extends LinearLayout implements OnLoadMoreListener {
    private BookDetailsActivity activity;
    private View playIntroduceSubView;
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private RecyclerView mCommentRecycle;
    private CommentAdapter adapter;
    private int page = 1;//评论制定的查询页数;

    public PlayIntroduceSubView(BookDetailsActivity activity) {
        super(activity);
        this.activity = activity;
        playIntroduceSubView = LayoutInflater.from(activity).inflate(R.layout.subview_play_introduce, this);
        initView();
    }
    private void initView() {
        mCommentRecycle =  playIntroduceSubView.findViewById(R.id.swipe_target);
        mSwipeToLoadLayout =  playIntroduceSubView.findViewById(R.id.swipeToLoadLayout);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        mCommentRecycle.setLayoutManager(manager);
        mCommentRecycle.setAdapter(adapter);
    }

    public void initData(){
        if(UtilNetStatus.isHasConnection(activity)){
            requestNetData(0);
        }else{
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            String jsonStr = UtilFileManage.readSDData(AppData.CACHE_PATH, UtilMD5Encryption.getMd5Value(String.valueOf(activity.getBookId())));
            CommentResult data = (CommentResult) UtilGson.fromJson(jsonStr, CommentResult.class);
            if(data != null){
                adapter = new CommentAdapter(activity, PlayIntroduceSubView.this);
                activity.initInfo(data);
                adapter.setBookId(data.getId());
                adapter.setResult(data.getComment().getData());
                adapter.setResult(data);
                adapter.setCommentoff(data.getCommentoff());
                mCommentRecycle.setAdapter(adapter);
            }
        }
    }

    /**
     * 请求网络数据
     * @param type
     */
    public void requestNetData(final int type) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("type", "comment");
        map.put("count", "20");
        map.put("bookID", activity.getBookId() + "");
        map.put("sort", "asc");
        if (TokenManager.isLogin(activity)) {
            map.put("uid", TokenManager.getUid(activity));
        }
        BaseObserver baseObserver = new BaseObserver<CommentResult>(activity, false) {
            @Override
            public void success(CommentResult data) {
                super.success(data);
                mSwipeToLoadLayout.setLoadingMore(false);
                activity.initInfo(data);
                if (adapter == null) {
                    //缓存内容
                    UtilFileManage.writeSDData(AppData.CACHE_PATH, UtilMD5Encryption.getMd5Value(String.valueOf(activity.getBookId())), UtilGson.toJson(data));
                    adapter = new CommentAdapter(activity, PlayIntroduceSubView.this);
                    adapter.setBookId(data.getId());
                    adapter.setResult(data.getComment().getData());
                    adapter.setResult(data);
                    adapter.setCommentoff(data.getCommentoff());
                    mCommentRecycle.setAdapter(adapter);
                } else {
                    if(type == 0) {
                        adapter.setResult(data.getComment().getData());
                    }else{
                        adapter.addData(data.getComment().getData());
                    }
                    adapter.notifyDataSetChanged();
                }
                isPaging(data.getComment().getLength(), adapter.getItemCount());
            }

            @Override
            public void error() {
                mSwipeToLoadLayout.setLoadingMore(false);
                if(type == 1){
                    page--;
                }
            }
        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getPlayer(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
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

    /**
     * 发送评论成功
     * @param result
     */
    public void sendMessageSuccess(MessageResult result) {
        if (adapter != null && result.getCommentData() != null) {
            adapter.addVO(result.getCommentData());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadMore() {
        page++;
        requestNetData(1);
    }
}
