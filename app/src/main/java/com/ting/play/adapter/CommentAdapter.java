package com.ting.play.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.anchor.dialog.GiftDialog;
import com.ting.bean.BaseResult;
import com.ting.bean.anchor.LiWuResult;
import com.ting.base.BaseObserver;
import com.ting.bean.play.CommentDate;
import com.ting.bean.vo.BookDataVO;
import com.ting.bean.vo.CommentListVO;
import com.ting.bean.vo.GiftVO;
import com.ting.bean.vo.HostInfoVO;
import com.ting.common.AppData;
import com.ting.common.GiftManager;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.login.LoginMainActivity;
import com.ting.play.BookDetailsActivity;
import com.ting.play.ReplyMessageActivity;
import com.ting.play.dialog.ListenBookDialog;
import com.ting.bean.play.CommentResult;
import com.ting.play.dialog.SendMessDialog;
import com.ting.play.dialog.ShareDialog;
import com.ting.play.subview.PlayIntroduceSubView;
import com.ting.util.UtilDate;
import com.ting.util.UtilGlide;
import com.ting.util.UtilIntent;
import com.ting.util.UtilListener;
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
    private static final int FIRST = 1;
    private static final int SECOND = 2;
    private BookDetailsActivity activity;
    private LayoutInflater inflater;
    private List<CommentListVO> data;
    private BookDataVO mBookDataVO;
    private HostInfoVO mHostInfoVO;
    private ItemOnClickListener mListener;
    private int commentoff = 1;
    private boolean isOpen = false;
    private String bookId = null;

    public CommentAdapter(BookDetailsActivity activity) {
        this.activity = activity;
        inflater = inflater.from(activity);
        mListener = new ItemOnClickListener();
    }

    public void setBookDataVO(BookDataVO bookDataVO) {
        mBookDataVO = bookDataVO;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
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
        if (viewType == FIRST) {
            View view = inflater.inflate(R.layout.play_introduce_head_view, parent, false);
            holder = new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_comment_list, parent, false);
            holder = new ItemViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == SECOND) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            CommentListVO vo = data.get(position - 1);
            UtilGlide.loadHeadImg(activity, vo.getUserImage(), itemViewHolder.comment_touxiang);
            if(!TextUtils.isEmpty(vo.getCommentsName())) {
                itemViewHolder.comment_user_name.setText(vo.getCommentsName());
            }else{
                itemViewHolder.comment_user_name.setText("佚名");
            }
            itemViewHolder.comment_context.setText(vo.getCommentsContent());
            itemViewHolder.mTvTime.setText(vo.getTime());
        } else {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            if (mBookDataVO.isCollect()) {
                headerViewHolder.tvCollect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bookdetails_collect, 0, 0, 0);
            } else {
                headerViewHolder.tvCollect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bookdetails_uncollect, 0, 0, 0);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return 1 + (data == null ? 0 : data.size());
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FIRST;
        } else {
            return SECOND;
        }
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

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        //封面
        private ImageView ivCover;
        //名称
        private TextView tvTitle;
        //主播
        private TextView tvAnchor;
        //介绍
        private TextView tvIntroduce;
        //展开
        private ImageView ivOpenClose;

        private TextView tvComment;
        private View vLine;
        private LinearLayout llRewardCollect;
        private View hView;
        //订阅
        private RelativeLayout rlCollect;
        //打赏
        private RelativeLayout rlReward;

        private TextView tvCollect;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAnchor = itemView.findViewById(R.id.tv_anchor);
            tvIntroduce =  itemView.findViewById(R.id.tv_introduce);
            ivOpenClose =  itemView.findViewById(R.id.iv_open_close);
            vLine = itemView.findViewById(R.id.v_line);
            llRewardCollect =  itemView.findViewById(R.id.ll_reward_collect);
            rlCollect =  itemView.findViewById(R.id.rl_collect);
            rlCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListenBookDialog dialog = new ListenBookDialog(activity);
                    dialog.setData(activity.getCardData());
                    dialog.show();
                }
            });
            rlReward = (RelativeLayout) itemView.findViewById(R.id.rl_reward);
            //送礼物
            rlReward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TokenManager.isLogin(activity)) {
                        activity.intent(LoginMainActivity.class);
                    } else {
                        if (GiftManager.getGifts() != null) {
                            GiftDialog dialog = new GiftDialog(activity);
                            dialog.setHostId(mBookDataVO.getHostId());
                            dialog.show();
                        } else {
                            BaseObserver baseObserver = new BaseObserver<BaseResult<List<GiftVO>>>(activity, BaseObserver.MODEL_SHOW_DIALOG_TOAST) {
                                @Override
                                public void success(BaseResult<List<GiftVO>> data) {
                                    super.success(data);
                                    GiftManager.setGifts(data.getData());
                                    GiftDialog dialog = new GiftDialog(activity);
                                    dialog.setHostId(mBookDataVO.getHostId());
                                    dialog.show();
                                }

                            };
                            activity.mDisposable.add(baseObserver);
                            UtilRetrofit.getInstance().create(HttpService.class).gift().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
                        }
                    }
                }
            });
            hView = itemView.findViewById(R.id.h_view);
            ivOpenClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOpen) {
                        tvIntroduce.setMaxLines(3);
                        ivOpenClose.setImageResource(R.drawable.vector_introduce_down);
                        isOpen = false;
                    } else {
                        ivOpenClose.setImageResource(R.drawable.vector_introduce_up);
                        tvIntroduce.setMaxLines(Integer.MAX_VALUE);
                        isOpen = true;
                    }
                }
            });
            UtilGlide.loadImg(activity, mBookDataVO.getBookImage(), ivCover);
            tvTitle.setText(mBookDataVO.getBookTitle());
            tvAnchor.setText("主播：" + mBookDataVO.getBookAnchor());
            tvIntroduce.setText("\t" + mBookDataVO.getBookDesc());

            int line = tvIntroduce.getMaxLines();
            if (line <= 3) {
                ivOpenClose.setVisibility(View.GONE);
            } else {
                tvIntroduce.setMaxLines(3);
            }
            if (activity.getCardData() != null && !activity.getCardData().isEmpty()) {
                rlCollect.setVisibility(View.VISIBLE);
            } else {
                rlCollect.setVisibility(View.GONE);
                hView.setVisibility(View.GONE);
            }
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvComment.setVisibility(View.VISIBLE);
            tvComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TokenManager.isLogin(activity)) {
                        activity.intent(LoginMainActivity.class);
                    } else {
                        SendMessDialog dialog = new SendMessDialog(activity);
                        dialog.setListener(new SendMessDialog.SendMessageCallBackListener() {
                            @Override
                            public void callback(CommentListVO vo) {
                                addVO(vo);
                                notifyDataSetChanged();
                            }
                        });
                        dialog.setBookId(bookId);
                        dialog.show();
                    }
                }
            });

            tvCollect = itemView.findViewById(R.id.tv_collect);
            if (mBookDataVO.isCollect())
            {
                tvCollect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bookdetails_collect, 0, 0, 0);
            } else

            {
                tvCollect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bookdetails_uncollect, 0, 0, 0);
            }
            tvCollect.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    if (!TokenManager.isLogin(activity)) {
                        activity.intent(LoginMainActivity.class);
                        return;
                    }
                    if (mBookDataVO.isCollect()) {
                        unCollectBook((TextView) v);
                    } else {
                        collectBook((TextView) v);
                    }
                }
            });
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
        BaseObserver baseObserver = new BaseObserver<BaseResult>(activity, BaseObserver.MODEL_SHOW_TOAST) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                mBookDataVO.setCollect(true);
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
        BaseObserver baseObserver = new BaseObserver<BaseResult>(activity, BaseObserver.MODEL_SHOW_TOAST) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                mBookDataVO.setCollect(false);
                activity.showToast("取消收藏");
                textView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bookdetails_uncollect, 0, 0, 0);
            }
        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).unCollect(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }
}
