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
import com.ting.bean.anchor.ListenBookVO;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.play.PlayingVO;
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
import com.ting.view.MusicAnimView;
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
    private ImageView ivDownload;


    private int bookId;//书页的ID;
    //章节id
    private int position = -1;

    //0：为正常播放， 1：离线播放
    private int type = 0;
    private List<PlayingVO> chapterList;
    //排序方式
    public String sort = "asc";
    //章节页
    public int page = 1;
    //当前显示那个view
    private int currView = 1;
    //是否播放
    private boolean isPlay = true;
    //标题
    private String bookname = null;
    //主播
    private String broadercaster = null;
    //封面图
    private String thumb = null;

    private MusicDBController mMusicController = new MusicDBController();

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
        mAnimView = flActionbar.findViewById(R.id.music_view);
        mAnimView.setOnClickListener(this);
        if (AppData.isPlaying) {
            mAnimView.start();
        } else {
            mAnimView.stop();
        }
        pager = flContent.findViewById(R.id.pager);
        playIntroduceSubView = new PlayIntroduceSubView(this);
        playListSubView = new PlayListSubView(this);
        playMainSubViews.add(playIntroduceSubView);
        playMainSubViews.add(playListSubView);
        playViewPagerAdapter = new PlayViewPagerAdapter(playMainSubViews);
        pager.setAdapter(playViewPagerAdapter);
        mTabLayout = flContent.findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(pager);
        ivDownload = (ImageView) findViewById(R.id.iv_download);
        ivDownload.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        /****************统计*******************/
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(bookId));
        MobclickAgent.onEvent(this, "BOOK_ID", map);
        /****************统计******************/

        if (UtilNetStatus.isHasConnection(this)) {
            DBListenHistory mHistory = mMusicController.getBookIdData(String.valueOf(bookId));
            if (mHistory != null) {
                position = mHistory.getPosition();
            }
            playIntroduceSubView.initData();
            if (position == -1) {
                playListSubView.getData(0, 1);
            } else {
                int page = position / 50 + 1;
                playListSubView.getData(0, page);
            }
        } else {
            DownloadController controller = new DownloadController();
            List<DBChapter> data = controller.queryData(bookId + "", 4 + "");
            playIntroduceSubView.initData();
            playListSubView.setOfflineData(data);
        }
    }

    @Override
    protected void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        bookId = bundle.getInt("bookID", -1);
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }


    public boolean isPlay() {
        return isPlay;
    }

    public int getBookId() {
        return bookId;
    }

    public int getPosition() {
        return position;
    }


    public int getType() {
        return type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }


    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }


    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }


    @Subscribe
    public void onEvent(LoginEventBus event) {
        playIntroduceSubView.initData();
        if (position == -1) {
            playListSubView.getData(0, 1);
        } else {
            int page = position / 50 + 1;
            playListSubView.getData(0, page);
        }
    }


    /**
     * 设置章节列表
     *
     * @param chapterList
     */
    public void setChapterList(List<PlayingVO> chapterList) {
        this.chapterList = chapterList;
    }

    /**
     * 获取章节列表
     */
    public List<PlayingVO> getChapterList() {
        return chapterList;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {



            case R.id.iv_left:
                onBackPressed();
                break;

            case R.id.music_view: {
                int bookId = -1;
                if (AppData.currPlayBookId != -1) {
                    bookId = AppData.currPlayBookId;
                } else {
                    MusicDBController controller = new MusicDBController();
                    List<DBListenHistory> historys = controller.getListenHistory();
                    if (historys != null && !historys.isEmpty()) {
                        DBListenHistory history = historys.get(0);
                        bookId = history.getBookid();
                    }
                }
                if (bookId != -1) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("bookID", bookId);
                    bundle.putBoolean("play", !AppData.isPlaying);
                    UtilIntent.intentDIYBottomToTop(mActivity, PlayActivity.class, bundle);
                } else {
                    mActivity.showToast("快去书城收听喜欢的书籍吧！！！");
                }
            }
            break;

            case R.id.iv_download:
                if (chapterList != null && !chapterList.isEmpty()) {
                    if (!TokenManager.isLogin(mActivity)) {
                        showToast("请登录");
                        UtilIntent.intentDIY(mActivity, LoginMainActivity.class);
                        return;
                    }
                    if (TokenManager.getInfo(mActivity).getIsvip() == 0) {
                        ListenDialog.makeListenDialog(mActivity, "提示", "亲，批量下载属于会员功能", true, "取消", true, "成为VIP", new ListenDialog.CallBackListener() {
                            @Override
                            public void callback(ListenDialog dialog, int mark) {
                                dialog.dismiss();
                                if(mark == ListenDialog.RIGHT){
                                    UtilIntent.intentDIY(mActivity, MyDouActivity.class);
                                }
                            }
                        }).show();
                        return;
                    }
                    DownloadMoreDialog dialog = new DownloadMoreDialog(this);
                    dialog.setResult(chapterList, bookId, bookname, broadercaster, thumb);
                    dialog.show();
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

    public void initInfo(CommentResult result) {
        tvTitle.setText(result.getTitle());
        bookname = result.getTitle();
        broadercaster = result.getBroadercaster();
        thumb = result.getThumb();

    }


    public void initInfo(PlayResult result) {
        tvTitle.setText(result.getTitle());
        bookname = result.getTitle();
        broadercaster = result.getBroadercaster();
        thumb = result.getThumb();
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


}
