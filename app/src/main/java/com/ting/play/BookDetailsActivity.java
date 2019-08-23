package com.ting.play;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.anchor.dialog.GiftDialog;
import com.ting.base.MessageEventBus;
import com.ting.base.PlayerBaseActivity;
import com.ting.bean.BookResult;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.vo.BookDataVO;
import com.ting.bean.vo.CardVO;
import com.ting.bean.vo.GiftVO;
import com.ting.bean.vo.HostInfoVO;
import com.ting.common.AppData;
import com.ting.common.GiftManager;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.db.DBListenHistory;
import com.ting.login.LoginMainActivity;
import com.ting.play.adapter.PlayViewPagerAdapter;
import com.ting.play.controller.MusicDBController;
import com.ting.play.dialog.ContactDialog;
import com.ting.play.dialog.ListenBookDialog;
import com.ting.play.subview.PlayIntroduceSubView;
import com.ting.play.subview.PlayListSubView;
import com.ting.base.ListenDialog;
import com.ting.util.UtilGlide;
import com.ting.util.UtilIntent;
import com.ting.util.UtilPermission;
import com.ting.util.UtilRetrofit;
import com.ting.view.MusicAnimView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;



/**
 * Created by gengjiajia on 15/9/6.
 * 播放activity;
 */
public class BookDetailsActivity extends PlayerBaseActivity implements View.OnClickListener, UtilPermission.PermissionCallbacks {
    private ArrayList<View> playMainSubViews = new ArrayList<View>();
    private ViewPager pager;
    private TabLayout mTabLayout;
    private ImageView ivBack;
    private TextView tvActionBarTitle;

    //封面
    private ImageView ivCover;
    //名称
    private TextView tvTitle;
    //主播
    private TextView tvAnchor;
    //介绍
    private TextView tvIntroduce;
    //展开
    private ImageView ivOpenClose;

    private TextView tvComment;
    private LinearLayout llRewardCollect;
    private View hView;
    //订阅
    private RelativeLayout rlCollect;
    //打赏
    private RelativeLayout rlReward;

    private TextView tvCollect;

    private MusicAnimView mAnimView;
    //书籍详情
    private PlayIntroduceSubView playIntroduceSubView;
    //章节列表
    private PlayListSubView playListSubView;
    private PlayViewPagerAdapter playViewPagerAdapter;
    //主播听书卡
    private List<CardVO> cardData;
    //书籍信息
    private BookDataVO mBookDataVO;
    private HostInfoVO mInfoVO;

    private String bookId;//书页的ID;
    private String bookTitle;
    private boolean isOpen = false;
    //是否播放
    private boolean isPlay = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_play_main);
        EventBus.getDefault().register(this);
    }




    @Override
    protected String setTitle() {
        return null;
    }

    @Override
    protected void initView() {
        setCustomActionBar(R.layout.actionbar_book_details);
        ivBack = flActionbar.findViewById(R.id.iv_left);
        ivBack.setOnClickListener(this);
        tvActionBarTitle = flActionbar.findViewById(R.id.tv_actionbar_title);
        tvActionBarTitle.setText(bookTitle);
        mAnimView = flActionbar.findViewById(R.id.music_view);
        mAnimView.setOnClickListener(this);
        if (AppData.isPlaying) {
            mAnimView.start();
        } else {
            mAnimView.stop();
        }
        pager = flContent.findViewById(R.id.pager);
        playIntroduceSubView = new PlayIntroduceSubView(this, bookId);
        playListSubView = new PlayListSubView(this, bookId);
        playMainSubViews.add(playIntroduceSubView);
        playMainSubViews.add(playListSubView);
        playViewPagerAdapter = new PlayViewPagerAdapter(playMainSubViews);
        pager.setAdapter(playViewPagerAdapter);
        pager.setCurrentItem(1);
        mTabLayout = flContent.findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(pager);


        ivCover = findViewById(R.id.iv_cover);
        tvTitle = findViewById(R.id.tv_title);
        tvAnchor = findViewById(R.id.tv_anchor);
        tvAnchor.setOnClickListener(this);
        tvIntroduce =  findViewById(R.id.tv_introduce);
        ivOpenClose =  findViewById(R.id.iv_open_close);
        ivOpenClose.setOnClickListener(this);
        llRewardCollect =  findViewById(R.id.ll_reward_collect);
        rlCollect =  findViewById(R.id.rl_collect);
        rlCollect.setOnClickListener(this);
        rlReward = findViewById(R.id.rl_reward);
        rlReward.setOnClickListener(this);
        hView = findViewById(R.id.h_view);
        tvCollect = findViewById(R.id.tv_collect);
        tvCollect.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        /****************统计*******************/
//        Map<String, String> map = new HashMap<>();
//        map.put("id", String.valueOf(bookId));
//        MobclickAgent.onEvent(this, "BOOK_ID", map);
        /****************统计******************/


        Map<String, String> map = new HashMap<>();
        map.put("bookId", String.valueOf(bookId));
        if (TokenManager.isLogin(this)) {
            map.put("uid", TokenManager.getUid(this));
        }
        BaseObserver baseObserver = new BaseObserver<BaseResult>(this, BaseObserver.MODEL_NO) {
            @Override
            public void success(BaseResult data) {
                super.success(data);

            }
        };
        UtilRetrofit.getInstance().create(HttpService.class).browseBook(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);

        BaseObserver baseObserverBook = new BaseObserver<BaseResult<BookResult>>(this, false) {
            @Override
            public void success(BaseResult<BookResult> data) {
                super.success(data);
                BookResult result = data.getData();
                mBookDataVO = data.getData().getBookData();
                mInfoVO = data.getData().getHostInfo();
                tvActionBarTitle.setText(mBookDataVO.getBookTitle());
                BookDetailsActivity.this.cardData = result.getCardData();



                UtilGlide.loadImg(mActivity, mBookDataVO.getBookImage(), ivCover);
                tvTitle.setText(mBookDataVO.getBookTitle());
                tvAnchor.setText("主播：" + mBookDataVO.getBookAnchor());
                tvIntroduce.setText("\t" + mBookDataVO.getBookDesc());

                int line = tvIntroduce.getMaxLines();
                if (line <= 3) {
                    ivOpenClose.setVisibility(View.GONE);
                } else {
                    tvIntroduce.setMaxLines(3);
                }
                if (cardData != null && !cardData.isEmpty()) {
                    rlCollect.setVisibility(View.VISIBLE);
                } else {
                    rlCollect.setVisibility(View.GONE);
                    hView.setVisibility(View.GONE);
                }

                if (mBookDataVO.isCollect())
                {
                    tvCollect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bookdetails_collect, 0, 0, 0);
                } else

                {
                    tvCollect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bookdetails_uncollect, 0, 0, 0);
                }

            }


        };
        mDisposable.add(baseObserverBook);
        UtilRetrofit.getInstance().create(HttpService.class).book(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserverBook);

    }

    @Override
    protected void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        bookId = bundle.getString("bookId", null);
        bookTitle = bundle.getString("bookTitle", null);
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }


    public boolean isPlay() {
        return isPlay;
    }


    public List<CardVO> getCardData() {
        return cardData;
    }

    public void setCardData(List<CardVO> cardData) {
        this.cardData = cardData;
    }

    @Subscribe
    public void onEvent(MessageEventBus event) {
        if (event.getType() == MessageEventBus.BATCH_BUY_CHAPTER || event.getType() == MessageEventBus.BUY_BOOK) {
            if (playListSubView != null) {
                playListSubView.getData(0, playListSubView.getNextPage());
            }
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_collect:
                if (!TokenManager.isLogin(this)) {
                    this.intent(LoginMainActivity.class);
                    return;
                }
                if (mBookDataVO.isCollect()) {
                    unCollectBook();
                } else {
                    collectBook();
                }

                break;
            case R.id.iv_open_close:
                if (isOpen) {
                    tvIntroduce.setMaxLines(3);
                    ivOpenClose.setImageResource(R.drawable.vector_introduce_down);
                    isOpen = false;
                } else {
                    ivOpenClose.setImageResource(R.drawable.vector_introduce_up);
                    tvIntroduce.setMaxLines(Integer.MAX_VALUE);
                    isOpen = true;
                }
                break;
            case R.id.rl_reward: {
                if (mInfoVO == null) {
                    showToast("主播信息加载失败，请重新加载书籍");
                    return;
                }

                ContactDialog dialog = new ContactDialog(mActivity);
                dialog.setData(mInfoVO.getNickname(), mInfoVO.getContact());
                dialog.show();
            }
                break;
            case R.id.rl_collect:
                if(cardData != null && !cardData.isEmpty()) {
                    ListenBookDialog dialog = new ListenBookDialog(this);
                    dialog.setData(cardData);
                    dialog.show();
                }
                break;

            case R.id.iv_left:
                onBackPressed();
                break;

            case R.id.music_view: {
                String bookId = null;
                if (AppData.currPlayBookId != null) {
                    bookId = AppData.currPlayBookId;
                } else {
                    MusicDBController controller = new MusicDBController();
                    List<DBListenHistory> historys = controller.getListenHistory();
                    if (historys != null && !historys.isEmpty()) {
                        DBListenHistory history = historys.get(0);
                        bookId = history.getBookId();
                    }
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

            case R.id.tv_anchor:
                if (mInfoVO == null) {
                    showToast("主播信息加载失败，请重新加载书籍");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("anchorId", mInfoVO.getId());
                mActivity.intent(AnchorMainActivity.class, bundle);
                break;

            default:
                break;
        }
    }


    public void unregister() {
        if (playListSubView != null) {
            playListSubView.unregister();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAnimView.stop();
        unregister();
        EventBus.getDefault().unregister(this);
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (mAnimView != null) {
            mAnimView.stop();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UtilPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {
        ListenDialog.makeListenDialog(BookDetailsActivity.this, "提示", "下载章节需要SD卡存储权限，请开启", false, null, true, "去允许", new ListenDialog.CallBackListener() {
            @Override
            public void callback(ListenDialog dialog, int mark) {
                dialog.dismiss();
                if (mark == ListenDialog.RIGHT) {
                    Intent intent = new Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
            }
        }).show();
    }


    public void stopAnim() {
        if (mAnimView != null) {
            mAnimView.stop();
        }
    }

    public void startAnim() {
        if (mAnimView != null) {
            mAnimView.start();
        }
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        tvTitle.setText(bookTitle);
    }


    /**
     * 收藏
     */
    private void collectBook() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(this));
        map.put("bookId", bookId);
        BaseObserver baseObserver = new BaseObserver<BaseResult>(this, BaseObserver.MODEL_ONLY_SHOW_TOAST) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                mBookDataVO.setCollect(true);
                showToast("收藏成功");
                tvCollect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bookdetails_collect, 0, 0, 0);
            }

        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).collect(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    /**
     * 取消收藏
     */
    private void unCollectBook() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(this));
        map.put("bookId", bookId);
        BaseObserver baseObserver = new BaseObserver<BaseResult>(this, BaseObserver.MODEL_ONLY_SHOW_TOAST) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                mBookDataVO.setCollect(false);
                showToast("取消收藏");
                tvCollect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bookdetails_uncollect, 0, 0, 0);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).unCollect(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    @Override
    protected void notifyServiceConnected() {
        super.notifyServiceConnected();
        if (getPlaybackStateCompat() != null && (getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_PLAYING)) {
            if(playListSubView != null){
                playListSubView.notifyPlayStateChange();
            }
            startAnim();
        } else if (getPlaybackStateCompat() != null && getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_PAUSED) {
            if(playListSubView != null){
                playListSubView.notifyPlayStateChange();
            }
            stopAnim();
        } else if (getPlaybackStateCompat() != null && getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_STOPPED) {
            if(playListSubView != null){
                playListSubView.notifyPlayStateChange();
            }
            stopAnim();
        } else {
            if(playListSubView != null){
                playListSubView.notifyPlayStateChange();
            }
            stopAnim();
        }
    }

    @Override
    protected void notifyPlay(String bookId, String bookTitle, String chapterTitle, String bookImage, long duration) {
        super.notifyPlay(bookId, bookTitle, chapterTitle, bookImage, duration);
        if(playListSubView != null){
            playListSubView.notifyPlayStateChange();
        }
        startAnim();
    }


    @Override
    protected void notifyPause() {
        super.notifyPause();
        if(playListSubView != null){
            playListSubView.notifyPlayStateChange();
        }
        stopAnim();
    }

    @Override
    protected void notifyStop() {
        super.notifyStop();
        if(playListSubView != null){
            playListSubView.notifyPlayStateChange();
        }
        stopAnim();
    }
}
