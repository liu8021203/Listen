package com.ting.record.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.common.AppData;
import com.ting.db.DBChapter;
import com.ting.db.DBListenHistory;
import com.ting.download.DownloadController;
import com.ting.play.BookDetailsActivity;
import com.ting.play.PlayActivity;
import com.ting.play.controller.MusicDBController;
import com.ting.play.dialog.DeleteDialog;
import com.ting.record.DownChapterActivity;
import com.ting.util.UtilFileManage;
import com.ting.util.UtilGlide;
import com.ting.util.UtilMD5Encryption;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by gengjiajia on 15/9/9.
 * 已经下载适配器
 */
public class HaveDownAdapter extends RecyclerView.Adapter<HaveDownAdapter.ItemViewHolder> {
    private DownChapterActivity activity;
    private LayoutInflater inflater;
    private List<DBChapter> data;
    private ItemOnClickListener listener;
    private DeleteOnClickListener mDeleteOnClickListener;
    private DownloadController controller;
    private MusicDBController musicDBController;
    private Map<String, DBListenHistory> historyMap = new HashMap<>();
    private String bookId;

    public HaveDownAdapter(DownChapterActivity activity, List<DBChapter> data) {
        this.activity = activity;
        inflater = inflater.from(activity);
        this.data = data;
        this.listener = new ItemOnClickListener();
        this.controller = new DownloadController();
        this.mDeleteOnClickListener = new DeleteOnClickListener();
        musicDBController = new MusicDBController();
        if(data != null && !data.isEmpty()){
            bookId = data.get(0).getBookId();
        }
        initHistory();
    }


    private void initHistory(){
        List<DBListenHistory> mHistorys = musicDBController.getBookIdHistory(String.valueOf(bookId));
        if (mHistorys != null && !mHistorys.isEmpty()) {
            for (int i = 0; i < mHistorys.size(); i++) {
                historyMap.put(mHistorys.get(i).getChapterId(), mHistorys.get(i));
            }
        }
    }

    public void setData(List<DBChapter> data) {
        this.data = data;
    }

    public List<DBChapter> getData() {
        return data;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_have_down, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        DBChapter vo = data.get(position);
        UtilGlide.loadImg(activity, vo.getBookImage(), holder.ivImg);
        holder.tvName.setText(vo.getTitle());
        holder.itemView.setTag(vo);
        holder.itemView.setOnClickListener(listener);
        holder.ivDelete.setTag(vo);
        holder.ivDelete.setOnClickListener(mDeleteOnClickListener);
        DBListenHistory mHistory = historyMap.get(vo.getChapterId());
        if (mHistory != null) {
            if (mHistory.getDuration() != 0L && mHistory.getTotal() != 0L) {
                holder.tvRecord.setVisibility(View.VISIBLE);
                int percent = (int) (mHistory.getDuration() * 100 / mHistory.getTotal());
                if (percent >= 95) {
                    holder.tvRecord.setText("播放完成");
                    holder.itemView.setTag(R.id.history, null);
                } else if(percent > 1){
                    holder.tvRecord.setText("播放至" + percent + "%");
                    holder.itemView.setTag(R.id.history, mHistory);
                }else{
                    holder.tvRecord.setVisibility(View.GONE);
                }
            }else{
                holder.tvRecord.setVisibility(View.GONE);
            }
        } else {
            holder.tvRecord.setVisibility(View.GONE);
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

    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private ImageView ivDelete;
        private CircleImageView ivImg;
        private TextView tvRecord;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            ivDelete = itemView.findViewById(R.id.down_delete);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvRecord = itemView.findViewById(R.id.tv_record);
        }
    }

    private class ItemOnClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            DBChapter vo = (DBChapter) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putString("bookId", vo.getBookId());
            bundle.putBoolean("play", true);
            bundle.putInt("position", vo.getPosition());
            activity.intent(PlayActivity.class, bundle);

        }
    }

    private class DeleteOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            DBChapter vo = (DBChapter) v.getTag();
            DeleteDialog dialog = new DeleteDialog(activity);
            dialog.setListener(new DeleteDialog.CallBackListener() {
                @Override
                public void callback(DBChapter vo) {
                    controller.delete(vo);
                    data.remove(vo);
                    UtilFileManage.delete(AppData.FILE_PATH + vo.getBookId() + "/" + UtilMD5Encryption.getMd5Value(vo.getChapterId()) + ".tsj");
                    notifyDataSetChanged();
                    if(data.size() == 0){
                        activity.showEmpty();
                    }
                }
            });
            dialog.setVo(vo, 0);
            dialog.show();
        }
    }
}
