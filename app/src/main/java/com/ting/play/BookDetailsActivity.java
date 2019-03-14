package com.ting.play;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.MessageEventBus;
import com.ting.bean.anchor.ListenBookVO;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.apk.ApkResult;
import com.ting.bean.play.PlayingVO;
import com.ting.bean.vo.CardVO;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.db.DBChapter;
import com.ting.db.DBListenHistory;
import com.ting.download.DownloadController;
import com.ting.login.LoginEventBus;
import com.ting.login.LoginMainActivity;
import com.ting.myself.MyDouActivity;
import com.ting.play.adapter.PlayViewPagerAdapter;
import com.ting.play.controller.MusicDBController;
import com.ting.play.dialog.DownloadMoreDialog;
import com.ting.bean.play.CommentResult;
import com.ting.bean.play.PlayResult;
import com.ting.play.subview.PlayIntroduceSubView;
import com.ting.play.subview.PlayListSubView;
import com.ting.base.ListenDialog;
import com.ting.util.UtilIntent;
import com.ting.util.UtilNetStatus;
import com.ting.util.UtilPermission;
import com.ting.util.UtilRetrofit;
import com.ting.util.UtilSystem;
import com.ting.view.MusicAnimView;
import com.ting.welcome.ApkDownloadActivity;
import com.ting.welcome.MainActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.ting.welcome.MainActivity.MAIN_PAUSE;
import static com.ting.welcome.MainActivity.MAIN_PLAY;


/**
 * Created by gengjiajia on 15/9/6.
 * 播放activity;
 */
public class BookDetailsActivity extends BaseActivity implements View.OnClickListener, UtilPermission.PermissionCallbacks {
    private ArrayList<View> playMainSubViews = new ArrayList<View>();
    private ViewPager pager;
    private TabLayout mTabLayout;
    private ImageView ivBack;
    private TextView tvTitle;
    private MusicAnimView mAnimView;
    //书籍详情
    private PlayIntroduceSubView playIntroduceSubView;
    //章节列表
    private PlayListSubView playListSubView;
    private PlayViewPagerAdapter playViewPagerAdapter;
    //主播听书卡
    private List<CardVO> cardData;


    private String bookId;//书页的ID;
    private String bookTitle;

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
        tvTitle = flActionbar.findViewById(R.id.tv_actionbar_title);
        tvTitle.setText(bookTitle);
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
        mTabLayout = flContent.findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(pager);
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
        BaseObserver baseObserver = new BaseObserver<BaseResult>(this, BaseObserver.MODEL_NO) {
            @Override
            public void success(BaseResult data) {
                super.success(data);

            }
        };
        UtilRetrofit.getInstance().create(HttpService.class).browseBook(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
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
    protected void onResume() {
        super.onResume();
        if (mAnimView != null) {
            if (AppData.isPlaying) {
                mAnimView.start();
            } else {
                mAnimView.stop();
            }
        }
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
}
