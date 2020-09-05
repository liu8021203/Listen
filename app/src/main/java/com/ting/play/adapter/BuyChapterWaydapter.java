package com.ting.play.adapter;

import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.vo.BuyChapterWayVO;
import com.ting.common.http.HttpService;
import com.ting.util.UtilRetrofit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liu on 16/8/5.
 */
public class BuyChapterWaydapter extends RecyclerView.Adapter<BuyChapterWaydapter.ItemViewHolder>{
    private List<BuyChapterWayVO> data;
    private BuyChapterWayVO defVO = null;
    private ItemOnClickListener listener;
    private BaseActivity mActivity;

    public BuyChapterWaydapter(BaseActivity activity) {
        this.listener = new ItemOnClickListener();
        this.mActivity = activity;
    }

    public BuyChapterWayVO getDefVO() {
        return defVO;
    }

    public void setData(List<BuyChapterWayVO> data) {
        this.data = data;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_listen_book_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        BuyChapterWayVO vo = data.get(position);
        holder.tvDesc.setText(vo.getDesc());
        holder.itemView.setTag(vo);
        holder.itemView.setOnClickListener(listener);
        if(vo == defVO)
        {
            holder.tvDesc.setTextColor(0xff1296db);
            holder.ivMark.setImageResource(R.mipmap.check_select);
        }
        else {
            holder.tvDesc.setTextColor(0xff000000);
            holder.ivMark.setImageResource(R.mipmap.check_unselect);
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


    protected class ItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvDesc;
        ImageView ivMark;
        public ItemViewHolder(View itemView) {
            super(itemView);
            tvDesc =  itemView.findViewById(R.id.tv_desc);
            ivMark = itemView.findViewById(R.id.iv_mark);
        }
    }

    private class ItemOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            BuyChapterWayVO vo = (BuyChapterWayVO) v.getTag();
            if(defVO == null)
            {
                defVO = vo;
                if(vo.getType() == 1){
                    isBookFinish(vo.getBookId());
                }else{
                    new AlertDialog.Builder(mActivity).setTitle("提醒").setMessage("该作品收费属于主播个人行为，本次交易属于主播跟您的自愿行为，发生任何纠纷跟本平台无关").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }
                notifyDataSetChanged();
            }
            else {
                if(defVO == vo)
                {
                    defVO = null;
                    notifyDataSetChanged();
                }
                else {
                    defVO = vo;
                    if(vo.getType() == 1){
                        isBookFinish(vo.getBookId());
                    }else{
                        new AlertDialog.Builder(mActivity).setTitle("提醒").setMessage("该作品收费属于主播个人行为，本次交易属于主播跟您的自愿行为，发生任何纠纷跟本平台无关").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    }
                    notifyDataSetChanged();
                }
            }
        }
    }


    private void isBookFinish(String bookId){
        Map<String, String> map = new HashMap<>();
        map.put("bookId", bookId);
        BaseObserver baseObserver = new BaseObserver<BaseResult<Integer>>(mActivity, BaseObserver.MODEL_SHOW_DIALOG_TOAST){
            @Override
            public void success(BaseResult<Integer> data) {
                super.success(data);
                if(data.getData() == 0){
                    new AlertDialog.Builder(mActivity).setTitle("提醒").setMessage("现在购买全集，并不代表买完以后可以立刻听完全本，只是以后更新的章节不需要再付费。该作品收费属于主播个人行为，本次交易属于主播跟您的自愿行为，发生任何纠纷跟本平台无关").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }
            }


        };
        mActivity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).isBookFinish(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }
}
