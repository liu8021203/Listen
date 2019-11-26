package com.ting.play.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ting.R;
import com.ting.bean.BaseResult;
import com.ting.base.BaseObserver;
import com.ting.bean.vo.CommentListVO;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.play.BookDetailsActivity;
import com.ting.util.UtilGlide;
import com.ting.util.UtilRetrofit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/10/17.
 * 评论适配器
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private BookDetailsActivity activity;
    private LayoutInflater inflater;
    private List<CommentListVO> data;
    private ItemOnClickListener mListener;
    private String bookId = null;

    public CommentAdapter(BookDetailsActivity activity) {
        this.activity = activity;
        inflater = inflater.from(activity);
        mListener = new ItemOnClickListener();
    }


    public void addData(List<CommentListVO> data) {
        if (this.data != null) {
            this.data.addAll(data);
        }
    }

    public void addVO(CommentListVO vo) {
        if (this.data != null) {
            this.data.add(0, vo);
        }
    }

    public void setData(List<CommentListVO> data) {
        this.data = data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = inflater.inflate(R.layout.item_comment_list, parent, false);
        holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        CommentListVO vo = data.get(position);
        UtilGlide.loadHeadImg(activity, vo.getUserImage(), itemViewHolder.comment_touxiang);
        if (!TextUtils.isEmpty(vo.getCommentsName())) {
            itemViewHolder.comment_user_name.setText(vo.getCommentsName());
        } else {
            itemViewHolder.comment_user_name.setText("佚名");
        }
        itemViewHolder.comment_context.setText(vo.getCommentsContent());
        itemViewHolder.mTvTime.setText(vo.getTime());

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView comment_touxiang;//头像
        private TextView comment_user_name;//评论名
        private TextView comment_context;//评论内容
        private TextView mTvTime;

        public ItemViewHolder(View itemView) {
            super(itemView);
            comment_touxiang = itemView.findViewById(R.id.comment_touxiang);
            comment_user_name = itemView.findViewById(R.id.comment_user_name);
            comment_context = itemView.findViewById(R.id.comment_context);
            mTvTime = itemView.findViewById(R.id.tv_time);
        }
    }


    private class ItemOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
//            CommentDate date = (CommentDate) v.getTag();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("vo", date);
//            bundle.putInt("bookId", bookId);
//            UtilIntent.intentDIY(activity, ReplyMessageActivity.class, bundle);
        }

    }


    /**
     * 收藏
     */
    private void collectBook(final TextView textView) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(activity));
        map.put("bookId", bookId);
        BaseObserver baseObserver = new BaseObserver<BaseResult>(activity, BaseObserver.MODEL_ONLY_SHOW_TOAST) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
//                mBookDataVO.setCollect(true);
                activity.showToast("收藏成功");
                textView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bookdetails_collect, 0, 0, 0);
            }

        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).collect(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    /**
     * 取消收藏
     */
    private void unCollectBook(final TextView textView) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(activity));
        map.put("bookId", bookId);
        BaseObserver baseObserver = new BaseObserver<BaseResult>(activity, BaseObserver.MODEL_ONLY_SHOW_TOAST) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
//                mBookDataVO.setCollect(false);
                activity.showToast("取消收藏");
                textView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bookdetails_uncollect, 0, 0, 0);
            }
        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).unCollect(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }
}
