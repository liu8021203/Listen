package com.ting.play.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.bean.play.PlayListVO;
import com.ting.bean.play.PlayingVO;
import com.ting.common.AppData;
import com.ting.db.DBChapter;
import com.ting.db.DBListenHistory;
import com.ting.download.DownloadController;
import com.ting.play.BookDetailsActivity;
import com.ting.play.controller.MusicController;
import com.ting.play.controller.MusicDBController;
import com.ting.play.dialog.DeleteDialog;
import com.ting.play.service.MusicService;
import com.ting.view.AnimView;
import com.ting.view.CircleProgressBar;
import com.ting.view.ColorfulRingProgressView;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/6.
 * 播放列表适配器
 */
public class OfflinePlayListAdapter extends RecyclerView.Adapter<OfflinePlayListAdapter.ItemViewHolder>{
    private BaseActivity activity;
    private LayoutInflater inflater;
    private List<DBChapter> data;//分集详情;
    private DownloadController controller;
    private MusicDBController musicDBController;
    private int bookId;
    private DeleteOnClickListener deleteOnClickListener;
    private ItemOnClickListener mItemOnClickListener;
    private DBListenHistory mHistory;
    private MusicController mMusicController;
    private PlayListReceiver mPlayListReceiver;


    public OfflinePlayListAdapter(BaseActivity activity, int bookId) {
        this.activity = activity;
        inflater = inflater.from(activity);
        this.bookId = bookId;
        controller = new DownloadController();
        deleteOnClickListener = new DeleteOnClickListener();
        this.mItemOnClickListener = new ItemOnClickListener();
        mMusicController = new MusicController(activity);
        musicDBController = new MusicDBController();
        mHistory = musicDBController.getBookIdData(String.valueOf(bookId));

        IntentFilter intentFilterPlayState = new IntentFilter();
        intentFilterPlayState.addAction(MusicService.MUSIC_LOADING);
        intentFilterPlayState.addAction(MusicService.MUSIC_PLAY);
        intentFilterPlayState.addAction(MusicService.MUSIC_PAUSE);
        intentFilterPlayState.addAction(MusicService.MUSIC_COMPLETE);
        intentFilterPlayState.addAction(MusicService.MUSIC_ERROR);
        mPlayListReceiver = new PlayListReceiver();
        activity.registerReceiver(mPlayListReceiver, intentFilterPlayState);
    }


    public List<DBChapter> getData() {
        return data;
    }

    public void setData(List<DBChapter> data) {
        this.data = data;
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
        String key = bookId + "-" + vo.getChapterId();
        holder.mAnimView.setVisibility(View.GONE);
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
        if (mHistory != null) {
            if (mHistory.getCid() == vo.getChapterId() && !TextUtils.equals(AppData.playKey, key) && !AppData.isPlaying) {
                holder.tvRecord.setVisibility(View.VISIBLE);
                holder.tvRecord.setText("上次听到这里");
            }else{
                holder.tvRecord.setVisibility(View.GONE);
            }
        }else{
            holder.tvRecord.setVisibility(View.GONE);
        }
        holder.play_diversity_name.setText(vo.getChapterTitle());
        holder.itemView.setTag(vo);
        holder.itemView.setOnClickListener(mItemOnClickListener);
        holder.ivMark.setVisibility(View.GONE);
        holder.progress.setVisibility(View.VISIBLE);
        holder.progress.downloadComplete();
        holder.progress.setTag(vo);
        holder.progress.setOnClickListener(deleteOnClickListener);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivMark;//播放下载图标
        private TextView play_diversity_name;//每集条目名称
        private ColorfulRingProgressView progress;
        private AnimView mAnimView;
        private TextView tvRecord;


        public ItemViewHolder(View itemView) {
            super(itemView);
            tvRecord = itemView.findViewById(R.id.tv_record);
            ivMark =  itemView.findViewById(R.id.iv_mark);
            play_diversity_name =  itemView.findViewById(R.id.tv_title);
            progress =  itemView.findViewById(R.id.progress);
            mAnimView = itemView.findViewById(R.id.anim_view);
        }
    }


    private class ItemOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            DBChapter vo = (DBChapter) v.getTag();
            String key = bookId + "-" + vo.getChapterId();
            if (TextUtils.equals(key, AppData.playKey)) {
                if (AppData.isPlaying) {
                    mMusicController.pause();
                } else {
                    mHistory = musicDBController.getBookIdData(String.valueOf(bookId));
                    PlayListVO listVO = new PlayListVO();
                    listVO.setOfflineData(data);
                    if(mHistory == null || mHistory.getCid() != vo.getChapterId()) {
                        mMusicController.play(bookId, vo.getChapterId(), vo.getChapterUrl(), vo.getChapterTitle(), vo.getBookName(), vo.getHost(), vo.getBookUrl(), listVO, "asc", 1);
                    }else{
                        mMusicController.play(mHistory.getDuration(), bookId, mHistory.getCid(), mHistory.getUrl(), mHistory.getChapter_name(), mHistory.getBookname(), mHistory.getHost(), mHistory.getPic(), listVO, "asc", 1, 0);
                    }
                }
            } else {
                mHistory = musicDBController.getBookIdData(String.valueOf(bookId));
                PlayListVO listVO = new PlayListVO();
                listVO.setOfflineData(data);
                if(mHistory == null || mHistory.getCid() != vo.getChapterId()) {
                    mMusicController.play(bookId, vo.getChapterId(), vo.getChapterUrl(), vo.getChapterTitle(), vo.getBookName(), vo.getHost(), vo.getBookUrl(), listVO, "asc", 1);
                }else {
                    mMusicController.play(mHistory.getDuration(), bookId, mHistory.getCid(), mHistory.getUrl(), mHistory.getChapter_name(), mHistory.getBookname(), mHistory.getHost(), mHistory.getPic(), listVO, "asc", 1, 0);
                }
            }
        }
    }



    /**
     * 删除监听
     */
    private class DeleteOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            DBChapter vo = (DBChapter) v.getTag();
            DeleteDialog dialog = new DeleteDialog(activity);
            dialog.setVo(vo, 0);
            dialog.setListener(new DeleteDialog.CallBackListener() {
                @Override
                public void callback(DBChapter vo) {
                    controller.delete(vo);
                    data.remove(vo);
                    notifyDataSetChanged();
                }
            });
            dialog.show();
        }
    }

    private class PlayListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("aaa", "action=====offlinePlayListAdapter=======" + action);
            switch (action) {
                case MusicService.MUSIC_LOADING:
                    notifyDataSetChanged();
                    break;
                case MusicService.MUSIC_PLAY:
                    notifyDataSetChanged();
                    if(activity instanceof BookDetailsActivity){
                        BookDetailsActivity detailsActivity = (BookDetailsActivity) activity;
                        detailsActivity.startAnim();
                    }
                    break;
                case MusicService.MUSIC_COMPLETE:
                    notifyDataSetChanged();
                    if(activity instanceof BookDetailsActivity){
                        BookDetailsActivity detailsActivity = (BookDetailsActivity) activity;
                        detailsActivity.stopAnim();
                    }
                    break;
                case MusicService.MUSIC_PAUSE:
                    notifyDataSetChanged();
                    if(activity instanceof BookDetailsActivity){
                        BookDetailsActivity detailsActivity = (BookDetailsActivity) activity;
                        detailsActivity.stopAnim();
                    }
                    break;
                case MusicService.MUSIC_ERROR:
                    notifyDataSetChanged();
                    if(activity instanceof BookDetailsActivity){
                        BookDetailsActivity detailsActivity = (BookDetailsActivity) activity;
                        detailsActivity.stopAnim();
                    }
                    break;
            }
        }
    }

    public void unregister(){
        activity.unregisterReceiver(mPlayListReceiver);
    }
}