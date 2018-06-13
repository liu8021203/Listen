package com.ting.bookcity.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.bean.Recommend;
import com.ting.play.BookDetailsActivity;
import com.ting.util.UtilGlide;

import java.util.List;

/**
 * Created by gengjiajia on 15/8/28.
 * 精品推荐listview 适配器
 */
public class FineRecomListAdapter extends RecyclerView.Adapter<FineRecomListAdapter.ItemViewHolder> {
    private BaseActivity mActivity;
    private LayoutInflater inflater;
    private List<Recommend> data;

    public FineRecomListAdapter(BaseActivity activity) {
        this.mActivity = activity;
        this.inflater = LayoutInflater.from(activity);
    }

    public void setData(List<Recommend> data) {
        this.data = data;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fin_recommend_list_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Recommend vo = data.get(position);
        UtilGlide.loadImg(mActivity, vo.getThumb(), holder.fin_reconmmend_book_image);

        if (vo.getTitle() != null) {
            holder.fin_reconmmend_book_name.setText(vo.getTitle());
        }
        if (vo.getAuthor() != null) {
            holder.fin_reconmmend_book_worker.setText("主播:" + vo.getAuthor());
        }
        if (vo.isStatus()) {
            holder.fin_reconmmend_book_state.setText("动态:连载中");
            holder.fin_reconmmend_book_zhang.setText("章节:已更新至" + String.valueOf(vo.getLastUpdate()) + "章");
        } else {
            holder.fin_reconmmend_book_state.setText("动态:已完结");
            holder.fin_reconmmend_book_zhang.setText("章节:" + String.valueOf(vo.getLastUpdate()));
        }
        if (vo.getCategory() != null) {
            holder.fin_reconmmend_book_lei.setText("分类:" + vo.getCategory());
        }
        holder.fin_reconmmend_star.setRating(vo.getDiffcult());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("bookID", vo.getId());
                mActivity.intent(BookDetailsActivity.class, bundle);
            }
        });
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
        private ImageView fin_reconmmend_book_image;//书籍封面
        private TextView fin_reconmmend_book_name;//书籍名称
        private TextView fin_reconmmend_book_worker;//书籍作者
        private TextView fin_reconmmend_book_state;//书籍状态
        private TextView fin_reconmmend_book_lei;//书籍类别
        private TextView fin_reconmmend_book_zhang;//书籍最近更新
        private RatingBar fin_reconmmend_star;//书籍难度星星个数


        public ItemViewHolder(View itemView) {
            super(itemView);
            fin_reconmmend_book_image = (ImageView) itemView.findViewById(R.id.fin_reconmmend_book_image);
            fin_reconmmend_book_name = (TextView) itemView.findViewById(R.id.fin_reconmmend_book_name);
            fin_reconmmend_book_worker = (TextView) itemView.findViewById(R.id.fin_reconmmend_book_worker);
            fin_reconmmend_book_state = (TextView) itemView.findViewById(R.id.fin_reconmmend_book_state);
            fin_reconmmend_book_lei = (TextView) itemView.findViewById(R.id.fin_reconmmend_book_lei);
            fin_reconmmend_book_zhang = (TextView) itemView.findViewById(R.id.fin_reconmmend_book_zhang);
            fin_reconmmend_star = (RatingBar) itemView.findViewById(R.id.ratingbar);
        }
    }
}
