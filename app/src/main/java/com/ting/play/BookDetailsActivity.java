package com.ting.play;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.google.android.material.tabs.TabLayout;

import android.support.v4.media.session.PlaybackStateCompat;

import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lechuan.midunovel.base.util.FoxBaseCommonUtils;
import com.lechuan.midunovel.base.util.FoxBaseGsonUtil;
import com.lechuan.midunovel.view.FoxCustomerTm;
import com.lechuan.midunovel.view.FoxNsTmListener;
import com.lechuan.midunovel.view.video.bean.FoxResponseBean;
import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.base.MessageEventBus;
import com.ting.base.PlayerBaseActivity;
import com.ting.bean.BookResult;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.vo.BookDataVO;
import com.ting.bean.vo.CardVO;
import com.ting.bean.vo.HostInfoVO;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.db.DBListenHistory;
import com.ting.login.LoginMainActivity;
import com.ting.play.adapter.PlayViewPagerAdapter;
import com.ting.play.controller.MusicDBController;
import com.ting.play.dialog.ContactHostDialog;
import com.ting.play.dialog.PurchaseNotesDialog;
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

    private RelativeLayout rlContactHost;
    private RelativeLayout rlBuyBook;

    private ImageView ivAdImg;
    private TextView tvAdDesc;
    private RelativeLayout rlAdLayout;
    private TTAdNative mTTAdNative;

    private TextView tvComment;
    private LinearLayout llRewardCollect;
    private View hView;
    //订阅
    private RelativeLayout rlCollect;
    //打赏
    private RelativeLayout rlReward;

    private TextView tvCollect;

    private TextView tvUpdateStatus;

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

    private FoxCustomerTm mOxCustomerTm;
    private FoxResponseBean.DataBean mDataBean;

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
        tvIntroduce = findViewById(R.id.tv_introduce);
        ivOpenClose = findViewById(R.id.iv_open_close);
        ivOpenClose.setOnClickListener(this);
        llRewardCollect = findViewById(R.id.ll_reward_collect);
        rlCollect = findViewById(R.id.rl_collect);
        rlCollect.setOnClickListener(this);
        rlReward = findViewById(R.id.rl_reward);
        rlReward.setOnClickListener(this);
        hView = findViewById(R.id.h_view);
        tvCollect = findViewById(R.id.tv_collect);
        tvCollect.setOnClickListener(this);

        ivAdImg = findViewById(R.id.iv_ad_img);
        tvAdDesc = findViewById(R.id.tv_ad_desc);
        rlAdLayout = findViewById(R.id.rl_ad_layout);
        rlAdLayout.setOnClickListener(this);

//        AdSlot adSlot = new AdSlot.Builder()
//                .setCodeId("938728263") //广告位id
//                .setSupportDeepLink(true)
//                .setAdCount(1) //请求广告数量为1到3条
//                .setImageAcceptedSize(100, 100)//这个参数设置即可，不影响模板广告的size
//                .build();
//        mTTAdNative = TTAdSdk.getAdManager().createAdNative(this);
//        mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
//            @Override
//            public void onError(int i, String s) {
//
//            }
//
//            @Override
//            public void onFeedAdLoad(List<TTFeedAd> list) {
//                if (list != null && !list.isEmpty()) {
//                    rlAdLayout.setVisibility(View.VISIBLE);
//                    TTFeedAd ad = list.get(0);
//                    UtilGlide.loadImg(mActivity, ad.getIcon().getImageUrl(), ivAdImg, 40);
//                    Log.d("ad", "url=====" + ad.getIcon().getImageUrl());
//                    tvAdDesc.setText(ad.getDescription());
//                    //可以被点击的view, 也可以把convertView放进来意味item可被点击
//                    List<View> clickViewList = new ArrayList<>();
//                    clickViewList.add(rlAdLayout);
//                    List<View> creativeViewList = new ArrayList<>();
//                    creativeViewList.add(rlAdLayout);
//                    ad.registerViewForInteraction(rlAdLayout, clickViewList, creativeViewList, new TTNativeAd.AdInteractionListener() {
//                        @Override
//                        public void onAdClicked(View view, TTNativeAd ad) {
//
//                        }
//
//                        @Override
//                        public void onAdCreativeClick(View view, TTNativeAd ad) {
//                        }
//
//                        @Override
//                        public void onAdShow(TTNativeAd ad) {
//
//                        }
//
//
//                    });
//
//
//                    switch (ad.getInteractionType()) {
//                        case TTAdConstant.INTERACTION_TYPE_DOWNLOAD:
//                            //如果初始化ttAdManager.createAdNative(getApplicationContext())没有传入activity 则需要在此传activity，否则影响使用Dislike逻辑
//                            ad.setActivityForDownloadApp(mActivity);
//                            break;
//
//                    }
//                }
//            }
//        });




        mOxCustomerTm = new FoxCustomerTm(mActivity);
        mOxCustomerTm.setAdListener(new FoxNsTmListener() {
            @Override
            public void onReceiveAd(String result) {
                Log.d("========", "onReceiveAd:" + result);
                if (!FoxBaseCommonUtils.isEmpty(result)) {
                    FoxResponseBean.DataBean dataBean = FoxBaseGsonUtil.GsonToBean(result, FoxResponseBean.DataBean.class);
                    if (dataBean != null) {
                        mDataBean = dataBean;
                        rlAdLayout.setVisibility(View.VISIBLE);
                        UtilGlide.loadImg(mActivity, dataBean.getImageUrl(), ivAdImg);
                        tvAdDesc.setText(dataBean.getExtDesc());
                    }
                    //素材加载成功时候调用素材加载曝光方法
                    mOxCustomerTm.adExposed();
                }
            }

            @Override
            public void onFailedToReceiveAd() {
                Log.d("========", "onFailedToReceiveAd");
            }

            @Override
            public void onAdActivityClose(String s) {
                Log.d("========", "onAdActivityClose" + s);
                if (!FoxBaseCommonUtils.isEmpty(s)) {
//                    ToastUtils.showShort(s);
                }
            }

        });
        mOxCustomerTm.loadAd(360702, TokenManager.getUid(mActivity));



        tvUpdateStatus = findViewById(R.id.tv_update_status);
        rlContactHost = findViewById(R.id.rl_contact_host);
        rlContactHost.setOnClickListener(this);
        rlBuyBook = findViewById(R.id.rl_buy_book);
        rlBuyBook.setOnClickListener(this);
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

                if (mBookDataVO.getBookUpdateStatus() == 1) {
                    tvUpdateStatus.setText("更新状态：已完结");
                } else {
                    tvUpdateStatus.setText("更新状态：更新至" + mBookDataVO.getCount() + "集");
                }

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

                if (mBookDataVO.isCollect()) {
                    tvCollect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bookdetails_collect, 0, 0, 0);
                } else {
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

                PurchaseNotesDialog dialog = new PurchaseNotesDialog(mActivity);
                dialog.setData(mInfoVO.getNickname(), mInfoVO.getContact());
                dialog.show();
            }
            break;

            case R.id.rl_contact_host: {
                if (mInfoVO == null) {
                    showToast("主播信息加载失败，请重新加载书籍");
                    return;
                }

                ContactHostDialog dialog = new ContactHostDialog(mActivity);
                dialog.setData(mInfoVO.getNickname(), mInfoVO.getContact());
                dialog.show();
            }
            break;
            case R.id.rl_collect:
                if (cardData != null && !cardData.isEmpty()) {
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

            case R.id.rl_ad_layout:
                if (mOxCustomerTm != null && mDataBean != null && !FoxBaseCommonUtils.isEmpty(mDataBean.getActivityUrl())) {
                    //素材点击时候调用素材点击曝光方法
                    mOxCustomerTm.adClicked();
                    mOxCustomerTm.openFoxActivity(mDataBean.getActivityUrl());
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

            case R.id.rl_buy_book: {
                if (mBookDataVO == null) {
                    showToast("书籍信息加载中");
                    return;
                }
                if (!TokenManager.isLogin(mActivity)) {
                    mActivity.intent(LoginMainActivity.class);
                    return;
                }
                String str = "";
                if (mBookDataVO.getBookUpdateStatus() == 1) {
                    str = "该作品收费属于主播个人行为，本次交易属于主播跟您的自愿行为，发生任何纠纷跟本平台无关";
                } else {
                    str = "现在购买全集，并不代表买完以后可以立刻听完全本，只是以后更新的章节不需要再付费。该作品收费属于主播个人行为，本次交易属于主播跟您的自愿行为，发生任何纠纷跟本平台无关";
                }
                new AlertDialog.Builder(mActivity).setTitle("提醒").setMessage("确定要花费" + mBookDataVO.getBookPrice() + "听豆购买此书籍\n\n" + str).setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buyAll();
                    }
                }).show();
            }
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
            if (playListSubView != null) {
                playListSubView.notifyPlayStateChange();
            }
            startAnim();
        } else if (getPlaybackStateCompat() != null && getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_PAUSED) {
            if (playListSubView != null) {
                playListSubView.notifyPlayStateChange();
            }
            stopAnim();
        } else if (getPlaybackStateCompat() != null && getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_STOPPED) {
            if (playListSubView != null) {
                playListSubView.notifyPlayStateChange();
            }
            stopAnim();
        } else {
            if (playListSubView != null) {
                playListSubView.notifyPlayStateChange();
            }
            stopAnim();
        }
    }

    @Override
    protected void notifyPlay(String bookId, String bookTitle, String chapterTitle, String bookImage, long duration) {
        super.notifyPlay(bookId, bookTitle, chapterTitle, bookImage, duration);
        if (playListSubView != null) {
            playListSubView.notifyPlayStateChange();
        }
        startAnim();
    }


    @Override
    protected void notifyPause() {
        super.notifyPause();
        if (playListSubView != null) {
            playListSubView.notifyPlayStateChange();
        }
        stopAnim();
    }

    @Override
    protected void notifyStop() {
        super.notifyStop();
        if (playListSubView != null) {
            playListSubView.notifyPlayStateChange();
        }
        stopAnim();
    }


    /**
     * 购买全集
     */
    private void buyAll() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(mActivity));
        map.put("bookId", bookId);
        BaseObserver baseObserver = new BaseObserver<BaseResult>(mActivity, BaseObserver.MODEL_SHOW_DIALOG_TOAST) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                if (playListSubView != null) {
                    playListSubView.getData(0, playListSubView.getNextPage());
                }
            }


        };
        mActivity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).buyBook(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }
}
