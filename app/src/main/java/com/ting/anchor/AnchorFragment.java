package com.ting.anchor;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bigkoo.quicksidebar.QuickSideBarTipsView;
import com.bigkoo.quicksidebar.QuickSideBarView;
import com.bigkoo.quicksidebar.listener.OnQuickSideBarTouchListener;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.ting.R;
import com.ting.anchor.adapter.AnchorAdapter;
import com.ting.base.BaseFragment;
import com.ting.base.BaseObserver;
import com.ting.bean.anchor.AnchorResult;
import com.ting.bean.anchor.AnchorVO;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.db.DBListenHistory;
import com.ting.play.PlayActivity;
import com.ting.play.controller.MusicDBController;
import com.ting.util.UtilChina;
import com.ting.util.UtilIntent;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;
import com.ting.view.MusicAnimView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 2017/11/6.
 */

public class AnchorFragment extends BaseFragment implements OnQuickSideBarTouchListener {
    private RecyclerView mRecyclerView;
    private QuickSideBarView mQuickSideBarView;
    private QuickSideBarTipsView mQuickSideBarTipsView;
    private MusicAnimView mAnimView;
    private AnchorAdapter mAnchorAdapter;
    private Map<String, Integer> letterMap = new HashMap<>();
    private boolean move = false;
    private int mIndex;

    @Override
    protected void initView() {
        mRecyclerView = flContent.findViewById(R.id.recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1);
        mRecyclerView.addItemDecoration(decoration);
        mQuickSideBarView = flContent.findViewById(R.id.quickSideBarView);
        mQuickSideBarTipsView = flContent.findViewById(R.id.quickSideBarTipsView);
        mQuickSideBarView.setOnQuickSideBarTouchListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (move ){
                    move = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int n = mIndex - manager.findFirstVisibleItemPosition();
                    if ( 0 <= n && n < recyclerView.getChildCount()){
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = recyclerView.getChildAt(n).getTop() - 150;
                        //最后的移动
                        recyclerView.scrollBy(0, top);
                    }
                }
            }
        });
        mAnimView = flContent.findViewById(R.id.iv_play);
        mAnimView.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Map<String, String> map = new HashMap<>();
        if(TokenManager.isLogin(mActivity)){
            map.put("uid", TokenManager.getUid(mActivity));
        }
        BaseObserver observer = new BaseObserver<AnchorResult>(this) {
            @Override
            public void success(AnchorResult data) {
                super.success(data);
                List<String> letters = new ArrayList<>();
                int position = 0;

                for (AnchorVO vo : data.getData()) {
                    String letter = UtilChina.getFirstStr(vo.getName(), true);

                    vo.setFirstStr(letter);
                }
                Collections.sort(data.getData(), new Comparents());
                for (AnchorVO vo: data.getData()){
                    if (!letterMap.containsKey(vo.getFirstStr())) {
                        letterMap.put(vo.getFirstStr(), position);
                        letters.add(vo.getFirstStr());
                    }
                    position++;
                }
                Collections.sort(letters, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        Collator collator = Collator.getInstance();
                        return collator.getCollationKey(o1).compareTo(
                                collator.getCollationKey(o2));
                    }
                });
                mQuickSideBarView.setLetters(letters);
                mAnchorAdapter = new AnchorAdapter(mActivity);
                mAnchorAdapter.setData(data.getData());
                mRecyclerView.setAdapter(mAnchorAdapter);
                StickyRecyclerHeadersDecoration decoration = new StickyRecyclerHeadersDecoration(mAnchorAdapter);
                mRecyclerView.addItemDecoration(decoration);
            }

            @Override
            public void error() {
                super.error();
            }
        };
        mDisposable.add(observer);
        UtilRetrofit.getInstance().create(HttpService.class).getAllBroadcaster(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_anchor;
    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected int setActionBar() {
        return 0;
    }

    @Override
    protected void reload() {

    }

    @Override
    public void onLetterChanged(String letter, int position, float y) {
        mQuickSideBarTipsView.setText(letter, position, y);
        //有此key则获取位置并滚动到该位置
        if(letterMap.containsKey(letter)) {
            int  pos = letterMap.get(letter);
            mIndex = pos;
            LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            int firstItem = manager.findFirstVisibleItemPosition();
            int lastItem = manager.findLastVisibleItemPosition();
            //然后区分情况
            if (pos <= firstItem ){
                //当要置顶的项在当前显示的第一个项的前面时
                mRecyclerView.scrollToPosition(pos);
            }else if ( pos <= lastItem ){
                //当要置顶的项已经在屏幕上显示时
                int top1 = mRecyclerView.getChildAt(pos - firstItem).getTop() - 150;
                mRecyclerView.scrollBy(0, top1);
            }else{
                //当要置顶的项在当前显示的最后一项的后面时
                mRecyclerView.scrollToPosition(pos);
                //这里这个变量是用在RecyclerView滚动监听里面的
                move = true;
            }
        }
    }

    @Override
    public void onLetterTouching(boolean touching) {
        mQuickSideBarTipsView.setVisibility(touching ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_play:
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
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AppData.isPlaying){
            mAnimView.start();
        }else{
            mAnimView.stop();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if(AppData.isPlaying){
                mAnimView.start();
            }else{
                mAnimView.stop();
            }
        }else{
            mAnimView.stop();
        }
    }

    public void startAnim(){
        mAnimView.start();
    }

    public void stopAnim(){
        mAnimView.stop();
    }
}
