package com.ting.play;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.ChapterResult;
import com.ting.bean.play.CommentResult;
import com.ting.bean.play.PlayListVO;
import com.ting.bean.play.PlayResult;
import com.ting.bean.play.PlayingVO;
import com.ting.bean.vo.ChapterListVO;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.db.DBChapter;
import com.ting.db.DBListenHistory;
import com.ting.download.DownloadController;
import com.ting.play.controller.MusicController;
import com.ting.play.controller.MusicDBController;
import com.ting.play.dialog.OffLinePlayListDialog;
import com.ting.play.dialog.PlayListDialog;
import com.ting.play.dialog.ShareDialog;
import com.ting.play.dialog.SpeedDialog;
import com.ting.play.dialog.TimeSettingDialog;
import com.ting.play.receiver.PlayerReceiver;
import com.ting.play.service.MusicService;
import com.ting.util.UtilDate;
import com.ting.util.UtilGlide;
import com.ting.util.UtilIntent;
import com.ting.util.UtilListener;
import com.ting.util.UtilNetStatus;
import com.ting.util.UtilRetrofit;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 2017/12/9.
 */

public class PlayActivity extends BaseActivity {
    private ImageView ivClose;
    private ImageView ivPrevious;
    private ImageView ivNext;
    private ImageView ivPlay;
    private ImageView ivList;
    private TextView tvTiming;
    private CircleImageView playImage;
    private TextView play_name;
    private TextView program_number;
    private MusicController musicController;
    private MusicDBController mDBController;
    private TextView tvShare;
    private PlayerReceiver mPlayerReceiver;
    private TextView tvSpeed;
    private SeekBar music_seekbar;
    private TextView tv_current_time;
    private TextView tv_total_time;
    private ProgressBar music_progress;
    //在线数据
    private List<DBChapter> data;
    private ObjectAnimator animator;
    //章节总数
    private int total;
    //书籍ID
    private String bookId;
    //是否播放
    private boolean isPlay = false;
    //书籍名称
    private String bookTitle;
    //书籍封面
    private String bookImage;
    //书籍主播
    private String bookHost;
    //书籍价格
    private int price;

    private int position = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
    }

    @Override
    protected String setTitle() {
        return null;
    }

    @Override
    protected void initView() {
        musicController = new MusicController(this);
        mDBController = new MusicDBController();
        //注册一个广播
        mPlayerReceiver = new PlayerReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicService.MUSIC_LOADING);
        filter.addAction(MusicService.MUSIC_PLAY);
        filter.addAction(MusicService.MUSIC_PAUSE);
        filter.addAction(MusicService.MUSIC_PROGRESS);
        filter.addAction(MusicService.MUSIC_COMPLETE);
        filter.addAction(MusicService.MUSIC_ERROR);
        filter.addAction(MusicService.MUSIC_BUFFER_START);
        filter.addAction(MusicService.MUSIC_BUFFER_END);
        filter.addAction(MusicService.MUSIC_DESTORY);
        filter.addAction(MusicService.TIMER_PROGRESS);
        filter.addAction(MusicService.TIMER_COMPLETE);
        registerReceiver(mPlayerReceiver, filter);
        ivClose = flContent.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(this);
        ivPrevious = flContent.findViewById(R.id.iv_previous);
        ivPrevious.setOnClickListener(this);
        ivNext = flContent.findViewById(R.id.iv_next);
        ivNext.setOnClickListener(this);
        ivPlay = flContent.findViewById(R.id.iv_play);
        ivPlay.setOnClickListener(this);
        program_number = flContent.findViewById(R.id.program_number);//播放集数
        play_name = flContent.findViewById(R.id.play_name);//播放小说名
        play_name.setOnClickListener(this);
        tvTiming = flContent.findViewById(R.id.tv_timing);//时间定时器
        tvTiming.setOnClickListener(this);
        tv_current_time = flContent.findViewById(R.id.tv_current_time);
        tv_total_time = flContent.findViewById(R.id.tv_total_time);
        music_seekbar = flContent.findViewById(R.id.music_seekbar);
        music_seekbar.setOnSeekBarChangeListener(new SeekBarChangeListener());
        music_progress = flContent.findViewById(R.id.music_progress);
        tvShare = flContent.findViewById(R.id.tv_share);
        tvShare.setOnClickListener(this);
        if (AppData.ifTimeSetting) {
            tvTiming.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.vector_time_ding, 0, 0);
        }

        playImage = flContent.findViewById(R.id.play_image);
        animator = ObjectAnimator.ofFloat(playImage, "rotation", 0.0f, 360.0f);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(5000);
        ivList = flContent.findViewById(R.id.iv_list);
        ivList.setOnClickListener(this);
        tvSpeed = findViewById(R.id.tv_speed);
        tvSpeed.setOnClickListener(this);

        switch (AppData.speedType) {
            case 1:
                tvSpeed.setText("1X倍速");
                break;

            case 2:
                tvSpeed.setText("1.25X倍速");
                break;

            case 3:
                tvSpeed.setText("1.5X倍速");
                break;
            case 4:
                tvSpeed.setText("1.75X倍速");
                break;

            case 5:
                tvSpeed.setText("2X倍速");
                break;
        }
    }

    @Override
    protected void initData() {
        /****************统计*******************/
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(bookId));
        MobclickAgent.onEvent(this, "BOOK_ID", map);
        /****************统计******************/
        if (UtilNetStatus.isHasConnection(this)) {
            MusicDBController mMusicController = new MusicDBController();
            DBListenHistory mHistory = mMusicController.getBookIdData(String.valueOf(bookId));
            if (mHistory != null) {
                int position = mHistory.getPosition();
                int page = (position - 1) / 50 + 1;
                getChapterData(page);
            } else {
                getChapterData(1);
            }
        } else {
            playImage.setImageResource(R.drawable.book_def);
            DownloadController controller = new DownloadController();
            data = controller.queryData(bookId + "", 4 + "");
            if (data != null && !data.isEmpty()) {
                bookTitle = data.get(0).getBookTitle();
                bookImage = data.get(0).getBookImage();
                bookHost = data.get(0).getBookHost();
                play_name.setText(data.get(0).getBookTitle() + "(" + data.get(0).getBookHost() + ")");
                program_number.setText(data.get(0).getTitle());
                total = data.size();
                if (isPlay) {
                    play(true);
                }else{
                    if (!AppData.isPlaying) {
                        play(true);
                    }
                }
            } else {
                program_number.setText("暂无播放章节");
                return;
            }
        }

    }

    @Override
    protected void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        bookId = bundle.getString("bookId");
        position = bundle.getInt("position", -1);
        isPlay = bundle.getBoolean("play", false);
    }

    @Override
    protected boolean showActionBar() {
        return false;
    }


    /**
     * 获取章节数据
     */
    private void getChapterData(int page) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("size", "50");
        map.put("bookId", bookId);
        if (TokenManager.isLogin(this)) {
            map.put("uid", TokenManager.getUid(this));
        }
        BaseObserver baseObserver = new BaseObserver<BaseResult<ChapterResult>>(mActivity, BaseObserver.MODEL_NO) {
            @Override
            public void success(BaseResult<ChapterResult> data) {
                super.success(data);
                ChapterResult result = data.getData();
                total = result.getCount();
                PlayActivity.this.data = result.getList();
                bookTitle = result.getList().get(0).getBookTitle();
                bookImage = result.getList().get(0).getBookImage();
                bookHost = result.getList().get(0).getBookHost();
                UtilGlide.loadImg(mActivity, result.getList().get(0).getBookImage(), playImage);
                play_name.setText(result.getList().get(0).getBookTitle() + "(" + result.getList().get(0).getBookHost() + ")");
                if (isPlay) {
                    play(true);
                } else {
                    if (!AppData.isPlaying) {
                        play(true);
                    }
                }
            }


            @Override
            public void error(BaseResult<ChapterResult> value, Throwable e) {
                super.error(value, e);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).chapter(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //上一章
            case R.id.iv_previous: {
                musicController.previous();
            }
            break;
            //下一章
            case R.id.iv_next: {
                musicController.next();
            }
            break;
            case R.id.iv_play:
                play(!AppData.isPlaying);
                break;
            case R.id.tv_timing:
                if (AppData.isPlaying) {
                    TimeSettingDialog dialog = new TimeSettingDialog(this);
                    dialog.setListener(new TimeSettingDialog.SettingTimingCallBackListener() {
                        @Override
                        public void callback(int timing) {
                            if (timing == 0) {
                                AppData.shutDownTimer = 0;
                                AppData.ifTimeSetting = false;
                                showToast("取消定时设置");
                                tvTiming.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.vector_time, 0, 0);
                                tvTiming.setText("--:--");
                                musicController.timeStop();
                            } else {
                                AppData.ifTimeSetting = true;
                                tvTiming.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.vector_time_ding, 0, 0);
                                musicController.timeStart(AppData.shutDownTimer);
                            }
                        }
                    });
                    dialog.show();
                } else {
                    showToast("请先收听您喜爱的书籍");
                }
                break;

            case R.id.tv_share:
                if (data == null || data.isEmpty()) {
                    showToast("数据加载中，请稍后");
                    return;
                }
                ShareDialog shareDialog = new ShareDialog(mActivity);
                shareDialog.setImageUrl(bookImage);
                shareDialog.setBookname(bookTitle);
                shareDialog.setUrl(data.get(0).getUrl());
                shareDialog.show();
                break;

            case R.id.iv_close:
                UtilIntent.finishDIYBottomToTop(mActivity);
                break;

            case R.id.iv_list: {
                PlayListDialog dialog = new PlayListDialog(mActivity);
                dialog.setData(total, data);
                dialog.show();
            }
            break;

            case R.id.tv_speed:
                SpeedDialog dialog = new SpeedDialog(mActivity);
                dialog.setListener(new SpeedDialog.SpeedCallBackListener() {
                    @Override
                    public void speed(float speed) {
                        switch (AppData.speedType) {
                            case 1:
                                tvSpeed.setText("1X倍速");
                                break;

                            case 2:
                                tvSpeed.setText("1.25X倍速");
                                break;

                            case 3:
                                tvSpeed.setText("1.5X倍速");
                                break;
                            case 4:
                                tvSpeed.setText("1.75X倍速");
                                break;

                            case 5:
                                tvSpeed.setText("2X倍速");
                                break;
                        }
                        musicController.speed(speed);
                    }
                });
                dialog.show();
                break;

            case R.id.play_name:
                Bundle bundle = new Bundle();
                bundle.putString("bookId", bookId);
                mActivity.intent(BookDetailsActivity.class, bundle);
                break;
            default:
                break;
        }
    }

    public void play(boolean b) {
        if (!UtilNetStatus.isWifiConnection() && UtilNetStatus.isHasConnection(this)) {
            showToast("非wifi下请注意流量");
        }
        if (data == null || data.isEmpty()) {
            showToast("请稍等，数据在加载中");
            return;
        }
        if (b) {
            if (position == -1) {
                DBListenHistory mHistory = mDBController.getBookIdData(bookId);
                if (mHistory != null) {
                    if (isRecordExist(mHistory)) {
                        musicController.seekToPlay(mHistory.getDuration(), UtilListener.dBListenHistoryToDBChapter(mHistory), data);
                    } else {
                        musicController.play(data.get(0), data);
                    }
                } else {
                    musicController.play(data.get(0), data);
                }
            } else {
                DBListenHistory history = mDBController.getBookIdAndPositionData(bookId, String.valueOf(position));
                if (history != null) {
                    if (history.getDuration() != 0L && history.getTotal() != 0L) {
                        int percent = (int) (history.getDuration() * 100 / history.getTotal());
                        if (percent >= 95) {
                            int index = getIndex(position);
                            musicController.play(data.get(index), data);
                        } else if (percent > 1) {
                            musicController.play(UtilListener.dBListenHistoryToDBChapter(history), data);
                        } else {
                            int index = getIndex(position);
                            musicController.play(data.get(index), data);
                        }
                    } else {
                        int index = getIndex(position);
                        musicController.play(data.get(index), data);
                    }
                } else {
                    int index = getIndex(position);
                    if (index != -1) {
                        musicController.play(data.get(index), data);
                    } else {
                        musicController.play(data.get(0), data);
                    }
                }
            }
        } else {
            musicController.pause();
        }
    }


    /**
     * 判断记录数据是否在数据集中
     *
     * @param history
     * @return
     */
    private boolean isRecordExist(DBListenHistory history) {
        boolean b = false;
        for (int i = 0; i < data.size(); i++) {
            if (TextUtils.equals(data.get(i).getChapterId(), history.getChapterId())) {
                b = true;
                break;
            }
        }
        return b;
    }


    private int getIndex(int position) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getPosition() == position) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 播放初始化
     *
     * @param duration
     */
    public void initPlay(long duration, String chapterTitle) {
        music_seekbar.setMax((int) duration);
        tv_total_time.setText(UtilDate.transformToTimeStr((int) duration));
        program_number.setText(chapterTitle);
        music_progress.setVisibility(View.GONE);
        ivPlay.setVisibility(View.VISIBLE);
        ivPlay.setImageResource(R.drawable.vector_play);
        animator.cancel();
        animator.start();
    }


    /**
     * 时间更新
     *
     * @param time
     */
    public void updateTime(long time, long totalTime, String chapterTitle, String bookTitle) {
        if (time != -1) {
            if (ivPlay != null) {
                ivPlay.setImageResource(R.drawable.vector_play);
            }
            if (tv_current_time != null) {
                tv_current_time.setText(UtilDate.transformToTimeStr((int) time));
            }
            if (music_seekbar != null) {
                music_seekbar.setMax((int) totalTime);
                music_seekbar.setProgress((int) time);
            }
            if (tv_total_time != null) {
                tv_total_time.setText(UtilDate.transformToTimeStr((int) totalTime));
            }
            if (program_number != null) {
                program_number.setText(chapterTitle);
            }
            if (play_name != null) {
                play_name.setText(bookTitle);
            }
            if (!animator.isStarted()) {
                animator.start();
            }
        }
    }

    /**
     * 通知暂停
     */
    public void pauseNotify() {
        ivPlay.setImageResource(R.drawable.vector_pause);
        animator.cancel();
    }

    /**
     * 加载MP3
     */
    public void loadingMusic() {
        music_progress.setVisibility(View.VISIBLE);
        ivPlay.setVisibility(View.GONE);
    }

    /**
     * 错误通知
     */
    public void error() {
        music_progress.setVisibility(View.GONE);
        ivPlay.setVisibility(View.VISIBLE);
        ivPlay.setImageResource(R.drawable.vector_pause);
        music_seekbar.setProgress(0);
        tv_current_time.setText("00:00");
        tv_total_time.setText("00:00");
        animator.cancel();
    }


    /**
     * 播放完成
     */
    public void playComplete() {
        music_seekbar.setProgress(0);
        music_progress.setVisibility(View.GONE);
        ivPlay.setVisibility(View.VISIBLE);
        ivPlay.setImageResource(R.drawable.vector_pause);
        tv_current_time.setText("00:00");
        tv_total_time.setText("00:00");
        animator.cancel();
    }

    /**
     * 开始缓存
     */
    public void buffStart() {
        music_progress.setVisibility(View.VISIBLE);
        ivPlay.setVisibility(View.GONE);
    }

    /**
     * 完成缓存
     */
    public void buffEnd() {
        music_progress.setVisibility(View.GONE);
        ivPlay.setVisibility(View.VISIBLE);
    }

    public void destory() {
        onBackPressed();
    }

    //定时完成
    public void timerComplete() {
        music_seekbar.setProgress(0);
        music_progress.setVisibility(View.GONE);
        ivPlay.setVisibility(View.VISIBLE);
        ivPlay.setImageResource(R.drawable.vector_pause);
        tv_current_time.setText("00:00");
        tv_total_time.setText("00:00");
        animator.cancel();
        tvTiming.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.vector_time, 0, 0);
        tvTiming.setText("--:--");
    }

    public void timerProgress(int time) {
        Log.d("aaa", "timerProgress========" + time);
        if (time > 60 * 60) {
            int hours = time / 60 / 60;
            int minute = (time - hours * 60 * 60) / 60;
            int second = time - hours * 60 * 60 - minute * 60;
            tvTiming.setText(String.format("%02d:%02d:%02d", hours, minute, second));
        } else if (time > 60 && time <= 60 * 60) {
            int minute = time / 60;
            int second = time % 60;
            tvTiming.setText(String.format("%02d:%02d", minute, second));
        } else {
            tvTiming.setText(String.format("%02d:%02d", 00, time));
        }
    }


    /**
     * TODO 实现监听Seekbar的类 ---------------------------------------------------
     */
    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            tv_current_time.setText(UtilDate
                    .transformToTimeStr(seekBar.getProgress()));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int currentTime = music_seekbar.getProgress();
            musicController.seekTo((long) currentTime);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mPlayerReceiver);
    }

    @Override
    public void onBackPressed() {
        UtilIntent.finishDIYBottomToTop(mActivity);
    }
}
