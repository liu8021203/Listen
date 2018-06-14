package com.ting.play.adapter;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseApplication;
import com.ting.bean.anchor.ListenBookVO;
import com.ting.bean.play.PlayListVO;
import com.ting.bean.play.PlayingVO;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.db.DBChapter;
import com.ting.db.DBListenHistory;
import com.ting.db.DBListenHistoryDao;
import com.ting.download.DownLoadService;
import com.ting.download.DownloadController;
import com.ting.login.LoginMainActivity;
import com.ting.myself.MyDouActivity;
import com.ting.play.BookDetailsActivity;
import com.ting.play.controller.MusicController;
import com.ting.play.controller.MusicDBController;
import com.ting.play.dialog.PlayListDialog;
import com.ting.play.dialog.PlayListPayDialog;
import com.ting.play.service.MusicService;
import com.ting.play.subview.PlayListSubView;
import com.ting.base.ListenDialog;
import com.ting.util.UtilIntent;
import com.ting.util.UtilListener;
import com.ting.util.UtilNetStatus;
import com.ting.util.UtilPermission;
import com.ting.util.UtilStr;
import com.ting.view.AnimView;
import com.ting.view.CircleProgressBar;
import com.ting.view.ColorfulRingProgressView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gengjiajia on 15/9/6.
 * 播放列表适配器
 */
public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ItemViewHolder> {

    private BaseActivity activity;
    private PlayListSubView playListSubView;
    private PlayListDialog mDialog;
    private LayoutInflater inflater;
    private List<PlayingVO> data;//分集详情;
    private List<ListenBookVO> tingshuka;
    private MusicController mMusicController;
    private DownloadController controller;

    private DownloadReceiver downloadReceiver;
    private PlayListReceiver mPlayListReceiver;
    private Map<String, DBChapter> downloadMap = new HashMap<String, DBChapter>();
    private int bookId;
    private PlayOnClickListener mPlayOnClickListener;
    private DownloadOnClickListener downloadOnClickListener;
    private DownloadStopOnClickListener downloadStopOnClickListener;
    private DeleteOnClickListener deleteOnClickListener;
    private PayOnClickListener payOnClickListener;
    private FisrtDownloadOnClickListener fisrtDownloadOnClickListener;
    private MusicDBController musicDBController;

    private String bookName;
    private String bookAnchor;
    private String bookPic;
    private int price;
    private DBListenHistory mHistory;

    public PlayListAdapter(BaseActivity activity, PlayListDialog dialog, int bookId){
        this.activity = activity;
        this.mDialog = dialog;
        this.bookId = bookId;
        init(activity, bookId);
    }

    public PlayListAdapter(BaseActivity activity, PlayListSubView baseSubView, int bookId) {
        this.activity = activity;
        this.playListSubView = baseSubView;
        this.bookId = bookId;
        init(activity, bookId);
    }

    public void init(BaseActivity activity, int bookId) {
        inflater = inflater.from(activity);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownLoadService.BROADCAST_ACTION_START);
        intentFilter.addAction(DownLoadService.BROADCAST_ACTION_PROGRESS);
        intentFilter.addAction(DownLoadService.BROADCAST_ACTION_COMPLETE);
        intentFilter.addAction(DownLoadService.BROADCAST_ACTION_CANCLE);
        intentFilter.addAction(DownLoadService.BROADCAST_ACTION_ERROR);

        downloadReceiver = new DownloadReceiver();
        activity.registerReceiver(downloadReceiver, intentFilter);
        IntentFilter intentFilterPlayState = new IntentFilter();
        intentFilterPlayState.addAction(MusicService.MUSIC_LOADING);
        intentFilterPlayState.addAction(MusicService.MUSIC_PLAY);
        intentFilterPlayState.addAction(MusicService.MUSIC_PAUSE);
        intentFilterPlayState.addAction(MusicService.MUSIC_COMPLETE);
        intentFilterPlayState.addAction(MusicService.MUSIC_ERROR);
        intentFilterPlayState.addAction(MusicService.MUSIC_DATA_UPDATE);
        mPlayListReceiver = new PlayListReceiver();
        activity.registerReceiver(mPlayListReceiver, intentFilterPlayState);
        controller = new DownloadController();
        mMusicController = new MusicController(activity);
        initData();
        this.mPlayOnClickListener = new PlayOnClickListener();
        this.downloadOnClickListener = new DownloadOnClickListener();
        this.downloadStopOnClickListener = new DownloadStopOnClickListener();
        this.deleteOnClickListener = new DeleteOnClickListener();
        this.payOnClickListener = new PayOnClickListener();
        this.fisrtDownloadOnClickListener = new FisrtDownloadOnClickListener();
        musicDBController = new MusicDBController();
        mHistory = musicDBController.getBookIdData(String.valueOf(bookId));
    }

    public void setBookInfo(String bookName, String bookAnchor, String bookPic, int price){
        this.bookName = bookName;
        this.bookAnchor = bookAnchor;
        this.bookPic = bookPic;
        this.price = price;
    }

    public void setData(List<PlayingVO> result) {
        this.data = result;
    }

    public void setTingshuka(List<ListenBookVO> tingshuka) {
        this.tingshuka = tingshuka;
    }

    public void addFooterData(List<PlayingVO> result) {
        if (this.data != null && result != null) {
            this.data.addAll(result);
        }
    }

    public void addHeaderData(List<PlayingVO> result) {
        if (this.data != null && result != null) {
            this.data.addAll(0, result);
        }
    }

    public List<PlayingVO> getResult() {
        return data;
    }

    private void initData() {
        List<DBChapter> data = controller.getData();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                downloadMap.put(data.get(i).getBookId() + "" + data.get(i).getChapterId(), data.get(i));
                if (data.get(i).getState() == 2 || data.get(i).getState() == 1 || data.get(i).getState() == 3) {
                    if (UtilNetStatus.isWifiConnection()) {
                        Intent intent = new Intent(activity, DownLoadService.class);
                        intent.putExtra("MSG", 1);
                        intent.putExtra("vo", data.get(i));
                        activity.startService(intent);
                    }
                }
            }
        }
    }

    public void unregister() {
        activity.unregisterReceiver(downloadReceiver);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_play_list, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        PlayingVO vo = data.get(position);
        String key = bookId + "-" + vo.getId();
        holder.mAnimView.setVisibility(View.GONE);
        if (!UtilStr.isEmpty(vo.getUrl())) {
            if (TextUtils.equals(key, AppData.playKey)) {
                holder.mAnimView.setVisibility(View.VISIBLE);
                if (AppData.isPlaying) {
                    holder.mAnimView.start();
                } else {
                    holder.mAnimView.stop();
                }
            } else {
                holder.mAnimView.stop();
            }
            holder.ivMark.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.VISIBLE);
        } else {
            holder.mAnimView.stop();
            holder.ivMark.setVisibility(View.VISIBLE);
            holder.ivMark.setImageResource(R.mipmap.chapter_lock);
            holder.progressBar.setVisibility(View.GONE);
        }
        holder.play_diversity_name.setText(vo.getTitle());
        holder.item_play.setTag(vo);
        if (mHistory != null) {
            if (mHistory.getCid() == vo.getId() && !TextUtils.equals(AppData.playKey, key) && !AppData.isPlaying) {
                holder.tvRecord.setVisibility(View.VISIBLE);
                if(mHistory.getTotal() == 0L && mHistory.getDuration() == 0L) {
                    holder.tvRecord.setText("上次听到100%");
                }else{
                    int percent = (int) (mHistory.getDuration() * 100 / mHistory.getTotal());
                    holder.tvRecord.setText("上次听到" + percent + "%");
                }
            }else{
                holder.tvRecord.setVisibility(View.GONE);
            }
        }else{
            holder.tvRecord.setVisibility(View.GONE);
        }
        if (!UtilStr.isEmpty(vo.getUrl())) {
            holder.item_play.setOnClickListener(mPlayOnClickListener);
        } else {
            holder.item_play.setOnClickListener(payOnClickListener);
        }
        DBChapter tempVO = downloadMap.get(bookId + "" + vo.getId());
        if (tempVO != null) {
            switch (tempVO.getState()) {
                case 0: {
                    holder.progressBar.downloadInit();
                    holder.progressBar.setTag(tempVO);
                    holder.progressBar.setOnClickListener(downloadOnClickListener);
                    holder.ivMark.setVisibility(View.GONE);

                }
                break;

                case 1: {
                    holder.progressBar.downloadWait();
                    holder.progressBar.setOnClickListener(null);
                }
                break;

                case 2: {
                    if (tempVO.getSize() != null && tempVO.getCompletesize() != null && tempVO.getSize() != 0) {
                        holder.progressBar.downloadResume(tempVO.getCompletesize() * 100 / tempVO.getSize());
                    } else {
                        holder.progressBar.downloadResume(0f);
                    }
                    holder.progressBar.setTag(tempVO);
                    holder.progressBar.setOnClickListener(downloadStopOnClickListener);
                }

                break;
                case 3: {
                    if (tempVO.getSize() != null && tempVO.getCompletesize() != null && tempVO.getSize() != 0) {
                        holder.progressBar.downloadPause(tempVO.getCompletesize() * 100 / tempVO.getSize());
                    } else {
                        holder.progressBar.downloadPause(0f);
                    }
                    holder.progressBar.setTag(tempVO);
                    holder.progressBar.setOnClickListener(downloadOnClickListener);
                }
                break;

                case 4:
                    holder.progressBar.downloadComplete();
                    holder.progressBar.setTag(tempVO);
                    holder.progressBar.setOnClickListener(deleteOnClickListener);
                    break;
            }


        } else {
            if (vo.getDownload() == 1) {
                holder.progressBar.downloadInit();
                holder.progressBar.setTag(vo);
                holder.progressBar.setOnClickListener(fisrtDownloadOnClickListener);
            } else {
                holder.progressBar.downloadNo();
                holder.progressBar.setOnClickListener(null);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    protected class ItemViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout item_play;//播放单个条目
        private ColorfulRingProgressView progressBar;
        private TextView play_diversity_name;//每集条目名称
        private ImageView ivMark;
        private AnimView mAnimView;
        private TextView tvRecord;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item_play = itemView.findViewById(R.id.rl_layout);
            play_diversity_name = itemView.findViewById(R.id.tv_title);
            progressBar = itemView.findViewById(R.id.progress);
            mAnimView = itemView.findViewById(R.id.anim_view);
            ivMark = itemView.findViewById(R.id.iv_mark);
            tvRecord = itemView.findViewById(R.id.tv_record);
        }
    }


    private void updateDownloadMap(DBChapter vo) {
        String key = vo.getBookId() + "" + vo.getChapterId();
        downloadMap.put(key, vo);
        notifyDataSetChanged();
    }

    private void removeDownloadMap(DBChapter vo) {
        String key = vo.getBookId() + "" + vo.getChapterId();
        downloadMap.remove(key);
        notifyDataSetChanged();
    }

    public class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle bundle = intent.getBundleExtra("data");
            DBChapter vo = (DBChapter) bundle.getParcelable("vo");
            if (vo != null) {
                switch (action) {
                    case DownLoadService.BROADCAST_ACTION_START:
                        updateDownloadMap(vo);
                        break;

                    case DownLoadService.BROADCAST_ACTION_PROGRESS:
                        updateDownloadMap(vo);
                        break;


                    case DownLoadService.BROADCAST_ACTION_COMPLETE:

                        updateDownloadMap(vo);
                        break;

                    case DownLoadService.BROADCAST_ACTION_CANCLE:
                        updateDownloadMap(vo);
                        break;
                    case DownLoadService.BROADCAST_ACTION_ERROR:
                        updateDownloadMap(vo);
                        break;
                }
            }
        }
    }

    /**
     * 开始下载监听
     */
    private class DownloadOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            DBChapter vo = (DBChapter) v.getTag();
            Intent intent = new Intent(activity, DownLoadService.class);
            intent.putExtra("MSG", 1);
            intent.putExtra("vo", vo);
            activity.startService(intent);
        }
    }

    private class FisrtDownloadOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (UtilPermission.hasPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                PlayingVO vo = (PlayingVO) v.getTag();
                //通过判断url是否为空来判断章节是否需要购买
                if (!UtilStr.isEmpty(vo.getUrl())) {
                    if (!TokenManager.isLogin(activity)) {
                        activity.showToast("请登录");
                        return;
                    }
                    if (TokenManager.getInfo(activity).getIsvip() == 0) {
                        ListenDialog.makeListenDialog(activity, "提示", "亲，章节下载属于会员功能", true, "取消", true, "成为VIP", new ListenDialog.CallBackListener() {
                            @Override
                            public void callback(ListenDialog dialog, int mark) {
                                dialog.dismiss();
                                if(mark == ListenDialog.RIGHT){
                                    UtilIntent.intentDIY(activity, MyDouActivity.class);
                                }
                            }
                        }).show();
                        return;
                    }
                    Intent intent = new Intent(activity, DownLoadService.class);
                    intent.putExtra("MSG", 1);
                    intent.putExtra("vo", UtilListener.PlayingVOToBook(vo, bookId, bookName, bookAnchor, bookPic));
                    activity.startService(intent);
                } else {
                    if (TokenManager.isLogin(activity)) {
                        PlayListPayDialog dialog = new PlayListPayDialog(activity);
                        dialog.setVo(vo, price);
                        dialog.setListener(new PlayListPayDialog.CallBackListener() {
                            @Override
                            public void singleCallBack() {
                                notifyDataSetChanged();
                            }

                            @Override
                            public void allCallBack() {
                                playListSubView.getData(1, playListSubView.getPages());
                            }

                            @Override
                            public void listBookCallBack() {
                                playListSubView.getData(1, playListSubView.getPages());
                            }
                        });
                        dialog.show();
                    } else {
                        activity.intent(LoginMainActivity.class);
                        activity.showToast("请先登录");
                    }
                }
            } else {
                UtilPermission.requestPermissions(activity, AppData.PERMISSION_CODE, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
            }
        }
    }

    /**
     * 暂停下载监听
     */
    private class DownloadStopOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            DBChapter vo = (DBChapter) v.getTag();
            Intent intent = new Intent(activity, DownLoadService.class);
            intent.putExtra("MSG", 2);
            intent.putExtra("vo", vo);
            activity.startService(intent);
        }
    }

    /**
     * 删除监听
     */
    private class DeleteOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final DBChapter vo = (DBChapter) v.getTag();
            ListenDialog.makeListenDialog(activity, "提示", "是否要删除" + vo.getChapterTitle(), true, "否", true, "是", new ListenDialog.CallBackListener() {
                @Override
                public void callback(ListenDialog dialog, int mark) {
                    dialog.dismiss();
                    if (mark == ListenDialog.RIGHT) {
                        controller.delete(vo);
                        removeDownloadMap(vo);
                    }
                }
            }).show();
        }
    }


    private class PayOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (TokenManager.isLogin(activity)) {
                final PlayingVO vo = (PlayingVO) v.getTag();
                PlayListPayDialog dialog = new PlayListPayDialog(activity);
                dialog.setVo(vo, price);
                dialog.setTingshuka(tingshuka);
                dialog.setBookId(bookId);
                dialog.setListener(new PlayListPayDialog.CallBackListener() {
                    @Override
                    public void singleCallBack() {
                        notifyDataSetChanged();
                    }

                    @Override
                    public void allCallBack() {
                        int page = vo.getPosition() / 50 + 1;
                        if(playListSubView != null) {
                            playListSubView.getData(1, page);
                        }
                        if(mDialog != null){
                            mDialog.getData(1, page);
                        }
                    }

                    @Override
                    public void listBookCallBack() {
                        int page = vo.getPosition() / 50 + 1;
                        if(playListSubView != null) {
                            playListSubView.getData(1, page);
                        }
                        if(mDialog != null){
                            mDialog.getData(1, page);
                        }
                    }
                });
                dialog.show();
            } else {
                activity.intent(LoginMainActivity.class);
                activity.showToast("请先登录");
            }

        }
    }


    private class PlayOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            PlayingVO vo = (PlayingVO) v.getTag();
            String key = bookId + "-" + vo.getId();
            if (TextUtils.equals(key, AppData.playKey)) {
                if (AppData.isPlaying) {
                    mMusicController.pause();
                } else {
                    mHistory = musicDBController.getBookIdData(String.valueOf(bookId));
                    PlayListVO listVO = new PlayListVO();
                    listVO.setData(data);
                    if(mHistory == null || mHistory.getCid() != vo.getId()) {
                        mMusicController.play(bookId, vo.getId(), vo.getUrl(), vo.getTitle(), bookName, bookAnchor, bookPic, listVO, "asc", 0);
                    }else{
                        mMusicController.play(mHistory.getDuration(), bookId, mHistory.getCid(), mHistory.getUrl(), mHistory.getChapter_name(), mHistory.getBookname(), mHistory.getHost(), mHistory.getPic(), listVO, "asc", 0, 0);
                    }
                }
            } else {
                mHistory = musicDBController.getBookIdData(String.valueOf(bookId));
                PlayListVO listVO = new PlayListVO();
                listVO.setData(data);
                if(mHistory == null || mHistory.getCid() != vo.getId()) {
                    mMusicController.play(bookId, vo.getId(), vo.getUrl(), vo.getTitle(), bookName, bookAnchor, bookPic, listVO, "asc", 0);
                }else {
                    mMusicController.play(mHistory.getDuration(), bookId, mHistory.getCid(), mHistory.getUrl(), mHistory.getChapter_name(), mHistory.getBookname(), mHistory.getHost(), mHistory.getPic(), listVO, "asc", 0, 0);
                }
            }
        }
    }


    private class PlayListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("aaa", "action=====" + action);
            switch (action) {
                case MusicService.MUSIC_LOADING:
                    notifyDataSetChanged();
                    break;
                case MusicService.MUSIC_PLAY:
                    mHistory = musicDBController.getBookIdData(String.valueOf(bookId));
                    notifyDataSetChanged();
                    if(activity instanceof BookDetailsActivity){
                        BookDetailsActivity detailsActivity = (BookDetailsActivity) activity;
                        detailsActivity.startAnim();
                    }
                    break;
                case MusicService.MUSIC_COMPLETE:
                    mHistory = musicDBController.getBookIdData(String.valueOf(bookId));
                    notifyDataSetChanged();
                    if(activity instanceof BookDetailsActivity){
                        BookDetailsActivity detailsActivity = (BookDetailsActivity) activity;
                        detailsActivity.stopAnim();
                    }
                    break;
                case MusicService.MUSIC_PAUSE:
                    mHistory = musicDBController.getBookIdData(String.valueOf(bookId));
                    notifyDataSetChanged();
                    if(activity instanceof BookDetailsActivity){
                        BookDetailsActivity detailsActivity = (BookDetailsActivity) activity;
                        detailsActivity.stopAnim();
                    }
                    break;
                case MusicService.MUSIC_ERROR:
                    mHistory = musicDBController.getBookIdData(String.valueOf(bookId));
                    notifyDataSetChanged();
                    if(activity instanceof BookDetailsActivity){
                        BookDetailsActivity detailsActivity = (BookDetailsActivity) activity;
                        detailsActivity.stopAnim();
                    }
                    break;
                case MusicService.MUSIC_DATA_UPDATE:
                    PlayListVO vo = intent.getParcelableExtra("data");
                    data = vo.getData();
                    notifyDataSetChanged();
                    break;
            }
        }
    }
}
