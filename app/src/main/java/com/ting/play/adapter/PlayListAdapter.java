package com.ting.play.adapter;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
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
import com.ting.base.PlayerBaseActivity;
import com.ting.bean.anchor.ListenBookVO;
import com.ting.bean.play.PlayListVO;
import com.ting.bean.play.PlayingVO;
import com.ting.bean.vo.CardVO;
import com.ting.bean.vo.ChapterListVO;
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
import com.ting.play.PlayActivity;
import com.ting.play.controller.MusicController;
import com.ting.play.controller.MusicDBController;
import com.ting.play.dialog.PlayListDialog;
import com.ting.play.dialog.PlayListPayDialog;
import com.ting.play.service.MusicService;
import com.ting.play.subview.PlayListSubView;
import com.ting.base.ListenDialog;
import com.ting.util.UtilFileManage;
import com.ting.util.UtilIntent;
import com.ting.util.UtilListener;
import com.ting.util.UtilMD5Encryption;
import com.ting.util.UtilNetStatus;
import com.ting.util.UtilPermission;
import com.ting.util.UtilStr;
import com.ting.view.AnimView;
import com.ting.view.CircleProgressBar;
import com.ting.view.ColorfulRingProgressView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liu on 15/9/6.
 * 播放列表适配器
 */
public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ItemViewHolder> {

    private PlayerBaseActivity activity;
    private LayoutInflater inflater;
    private List<DBChapter> data;//分集详情;
    private List<CardVO> cardData;
    private MusicController mMusicController;
    private DownloadController controller;

    private DownloadReceiver downloadReceiver;
    private Map<String, DBChapter> downloadMap = new HashMap<String, DBChapter>();
    private Map<String, DBListenHistory> historyMap = new HashMap<>();
    private String bookId;
    private PlayListSubView view;
    private PlayOnClickListener mPlayOnClickListener;
    private DownloadOnClickListener downloadOnClickListener;
    private DownloadStopOnClickListener downloadStopOnClickListener;
    private DeleteOnClickListener deleteOnClickListener;
    private PayOnClickListener payOnClickListener;
    private FisrtDownloadOnClickListener fisrtDownloadOnClickListener;
    private MusicDBController musicDBController;

    private int price;


    public PlayListAdapter(PlayerBaseActivity activity, String bookId, PlayListSubView view) {
        this.activity = activity;
        this.bookId = bookId;
        this.view = view;
        init();
    }


    public void init() {
        inflater = inflater.from(activity);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownLoadService.BROADCAST_ACTION_START);
        intentFilter.addAction(DownLoadService.BROADCAST_ACTION_PROGRESS);
        intentFilter.addAction(DownLoadService.BROADCAST_ACTION_COMPLETE);
        intentFilter.addAction(DownLoadService.BROADCAST_ACTION_CANCLE);
        intentFilter.addAction(DownLoadService.BROADCAST_ACTION_ERROR);

        downloadReceiver = new DownloadReceiver();
        activity.registerReceiver(downloadReceiver, intentFilter);
        controller = new DownloadController();
        mMusicController = new MusicController(activity);
        musicDBController = new MusicDBController();
        initData();
        initHistory();
        this.mPlayOnClickListener = new PlayOnClickListener();
        this.downloadOnClickListener = new DownloadOnClickListener();
        this.downloadStopOnClickListener = new DownloadStopOnClickListener();
        this.deleteOnClickListener = new DeleteOnClickListener();
        this.payOnClickListener = new PayOnClickListener();
        this.fisrtDownloadOnClickListener = new FisrtDownloadOnClickListener();
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setData(List<DBChapter> result) {
        this.data = result;
    }

    public void setCardData(List<CardVO> cardData) {
        this.cardData = cardData;
    }

    public void addFooterData(List<DBChapter> result) {
        if (this.data != null && result != null) {
            this.data.addAll(result);
        }
    }

    public void addHeaderData(List<DBChapter> result) {
        if (this.data != null && result != null) {
            this.data.addAll(0, result);
        }
    }

    public List<DBChapter> getResult() {
        return data;
    }

    private void initData() {
        List<DBChapter> data = controller.getData();
        if (data != null && !data.isEmpty()) {
            for (int i = 0; i < data.size(); i++) {
                downloadMap.put(data.get(i).getChapterId(), data.get(i));
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

    public void initHistory() {
        List<DBListenHistory> mHistorys = musicDBController.getBookIdHistory(String.valueOf(bookId));
        if (mHistorys != null && !mHistorys.isEmpty()) {
            for (int i = 0; i < mHistorys.size(); i++) {
                historyMap.put(mHistorys.get(i).getChapterId(), mHistorys.get(i));
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
        DBChapter vo = data.get(position);
        String key = vo.getChapterId();
        holder.mAnimView.setVisibility(View.GONE);
        if (!UtilStr.isEmpty(vo.getUrl())) {


            if (activity.getPlaybackStateCompat() != null && (activity.getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_PLAYING || activity.getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_PAUSED)) {
                Bundle bundle = activity.getPlaybackStateCompat().getExtras();
                if (bundle != null && !TextUtils.isEmpty(bundle.getString("chapterId")) && TextUtils.equals(vo.getChapterId(), bundle.getString("chapterId"))) {
                    holder.mAnimView.setVisibility(View.VISIBLE);
                    holder.mAnimView.start();
                } else {
                    holder.mAnimView.stop();
                }
            } else {
                holder.mAnimView.stop();
            }
            holder.ivMark.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.tvPrice.setVisibility(View.GONE);
        } else {
            holder.mAnimView.stop();
            holder.ivMark.setVisibility(View.VISIBLE);
            holder.ivMark.setImageResource(R.mipmap.chapter_lock);
            holder.progressBar.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.VISIBLE);
            holder.tvPrice.setText(vo.getPrice() + "听豆");
        }
        holder.play_diversity_name.setText(vo.getTitle());
        holder.item_play.setTag(vo);
        DBListenHistory mHistory = historyMap.get(vo.getChapterId());
        if (mHistory != null) {
            if (mHistory.getDuration() != 0L && mHistory.getTotal() != 0L) {
                holder.tvRecord.setVisibility(View.VISIBLE);
                int percent = (int) (mHistory.getDuration() * 100 / mHistory.getTotal());
                if (percent >= 95) {
                    holder.tvRecord.setText("播放完成");
                    holder.itemView.setTag(R.id.history, null);
                } else if (percent > 1) {
                    holder.tvRecord.setText("播放至" + percent + "%");
                    holder.itemView.setTag(R.id.history, mHistory);
                } else {
                    holder.tvRecord.setVisibility(View.GONE);
                }
            } else {
                holder.tvRecord.setVisibility(View.GONE);
            }
        } else {
            holder.tvRecord.setVisibility(View.GONE);
        }
        if (!UtilStr.isEmpty(vo.getUrl())) {
            holder.item_play.setOnClickListener(mPlayOnClickListener);
        } else {
            holder.item_play.setOnClickListener(payOnClickListener);
        }
        DBChapter tempVO = downloadMap.get(vo.getChapterId());
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
                    if (tempVO.getSize() != null && tempVO.getCompleteSize() != null && tempVO.getSize() != 0) {
                        holder.progressBar.downloadResume(tempVO.getCompleteSize() * 100 / tempVO.getSize());
                    } else {
                        holder.progressBar.downloadResume(0f);
                    }
                    holder.progressBar.setTag(tempVO);
                    holder.progressBar.setOnClickListener(downloadStopOnClickListener);
                }

                break;
                case 3: {
                    if (tempVO.getSize() != null && tempVO.getCompleteSize() != null && tempVO.getSize() != 0) {
                        holder.progressBar.downloadPause(tempVO.getCompleteSize() * 100 / tempVO.getSize());
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
            holder.progressBar.downloadInit();
            holder.progressBar.setTag(vo);
            holder.progressBar.setOnClickListener(fisrtDownloadOnClickListener);
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
        private ProgressBar mProgressBar;
        private TextView tvPrice;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item_play = itemView.findViewById(R.id.rl_layout);
            play_diversity_name = itemView.findViewById(R.id.tv_title);
            progressBar = itemView.findViewById(R.id.progress);
            mAnimView = itemView.findViewById(R.id.anim_view);
            ivMark = itemView.findViewById(R.id.iv_mark);
            tvRecord = itemView.findViewById(R.id.tv_record);
            mProgressBar = itemView.findViewById(R.id.progressBar);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }


    private void updateDownloadMap(DBChapter vo) {
        String key = vo.getChapterId();
        downloadMap.put(key, vo);
        notifyDataSetChanged();
    }

    private void removeDownloadMap(DBChapter vo) {
        String key = vo.getChapterId();
        downloadMap.remove(key);
        notifyDataSetChanged();
    }

    public class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle bundle = intent.getBundleExtra("data");
            DBChapter vo = bundle.getParcelable("vo");
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
                DBChapter vo = (DBChapter) v.getTag();
                //通过判断url是否为空来判断章节是否需要购买
                if (!UtilStr.isEmpty(vo.getUrl())) {
                    if (!TokenManager.isLogin(activity)) {
                        activity.intent(LoginMainActivity.class);
                        activity.showToast("请先登录");
                        return;
                    }

                    Intent intent = new Intent(activity, DownLoadService.class);
                    intent.putExtra("vo", vo);
                    intent.putExtra("MSG", 1);
                    activity.startService(intent);
                } else {
                    if (TokenManager.isLogin(activity)) {
                        PlayListPayDialog dialog = new PlayListPayDialog(activity);
//                        dialog.setVo(vo, price);
                        dialog.setListener(new PlayListPayDialog.CallBackListener() {
                            @Override
                            public void singleCallBack() {
                                notifyDataSetChanged();
                            }

                            @Override
                            public void allCallBack() {
//                                playListSubView.getData(1, playListSubView.getPages());
                            }

                            @Override
                            public void listBookCallBack() {
//                                playListSubView.getData(1, playListSubView.getPages());
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
            ListenDialog.makeListenDialog(activity, "提示", "是否要删除" + vo.getTitle(), true, "否", true, "是", new ListenDialog.CallBackListener() {
                @Override
                public void callback(ListenDialog dialog, int mark) {
                    dialog.dismiss();
                    if (mark == ListenDialog.RIGHT) {
                        controller.delete(vo);
                        UtilFileManage.delete(AppData.FILE_PATH + vo.getBookId() + "/" + UtilMD5Encryption.getMd5Value(vo.getChapterId()) + ".tsj");
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
                DBChapter vo = (DBChapter) v.getTag();
                PlayListPayDialog dialog = new PlayListPayDialog(activity);
                dialog.setVo(vo, price);
                dialog.setCardData(cardData);
                dialog.setListener(new PlayListPayDialog.CallBackListener() {
                    @Override
                    public void singleCallBack() {
                        notifyDataSetChanged();
                    }

                    @Override
                    public void allCallBack() {
                        int page = (data.get(data.size() - 1).getPosition() - 1) / 50 + 1;
                        if (view != null) {
                            view.getData(0, page);
                        }
//                        if(mDialog != null){
//                            mDialog.getData(1, page);
//                        }
                    }

                    @Override
                    public void listBookCallBack() {
                        int page = (data.get(data.size() - 1).getPosition() - 1) / 50 + 1;
                        if (view != null) {
                            view.getData(0, page);
                        }
//                        if(mDialog != null){
//                            mDialog.getData(1, page);
//                        }
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
            DBChapter vo = (DBChapter) v.getTag();
            if (activity.getPlaybackStateCompat() != null) {
                Bundle bundle = activity.getPlaybackStateCompat().getExtras();
                bundle.setClassLoader(getClass().getClassLoader());
                if (activity.getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_PAUSED) {
                    String chapterId = bundle.getString("chapterId");
                    if (TextUtils.equals(chapterId, vo.getChapterId())) {
                        MediaControllerCompat.getMediaController(activity).getTransportControls().play();
                    } else {
                        Bundle bundle1 = new Bundle();
                        bundle1.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) data);
                        bundle1.putParcelable("vo", vo);
                        MediaControllerCompat.getMediaController(activity).getTransportControls().playFromSearch("music", bundle1);
                    }
                } else if (activity.getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_PLAYING) {
                    String chapterId = bundle.getString("chapterId");
                    if (TextUtils.equals(chapterId, vo.getChapterId())) {
                        MediaControllerCompat.getMediaController(activity).getTransportControls().pause();
                    } else {
                        Bundle bundle1 = new Bundle();
                        bundle1.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) data);
                        bundle1.putParcelable("vo", vo);
                        MediaControllerCompat.getMediaController(activity).getTransportControls().playFromSearch("music", bundle1);
                    }
                } else {
                    Bundle bundle1 = new Bundle();
                    bundle1.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) data);
                    bundle1.putParcelable("vo", vo);
                    MediaControllerCompat.getMediaController(activity).getTransportControls().playFromSearch("music", bundle1);
                }
            } else {
                Bundle bundle1 = new Bundle();
                bundle1.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) data);
                bundle1.putParcelable("vo", vo);
                MediaControllerCompat.getMediaController(activity).getTransportControls().playFromSearch("music", bundle1);
            }

        }
    }

}
