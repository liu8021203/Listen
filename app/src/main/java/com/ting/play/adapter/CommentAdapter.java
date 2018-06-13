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
import com.ting.common.AppData;
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

import java.util.List;

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
    private PlayIntroduceSubView subview;
    private List<CommentDate> result;
    private CommentResult mResult;
    private ItemOnClickListener mListener;
    private int bookId;
    private int commentoff = 1;
    private boolean isOpen = false;

    public CommentAdapter(BookDetailsActivity activity, PlayIntroduceSubView playIntroduceSubView) {
        this.activity = activity;
        this.subview = playIntroduceSubView;
        inflater = inflater.from(activity);
        mListener = new ItemOnClickListener();
    }

    public void setResult(CommentResult result) {
        mResult = result;
    }

    public void addData(List<CommentDate> result) {
        if (this.result != null) {
            this.result.addAll(result);
        }
    }

    public void addVO(CommentDate vo) {
        if (this.result != null) {
            this.result.add(0, vo);
        }
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setResult(List<CommentDate> result) {
        this.result = result;
    }

    public List<CommentDate> getResult() {
        return result;
    }

    public void setCommentoff(int commentoff) {
        this.commentoff = commentoff;
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
            CommentDate vo = result.get(position - 1);
            UtilGlide.loadHeadImg(activity, vo.getThumb(), itemViewHolder.comment_touxiang);
            itemViewHolder.comment_user_name.setText(vo.getName());
            itemViewHolder.comment_context.setText(vo.getString());
            itemViewHolder.mTvRankName.setText(vo.getRankname());
            itemViewHolder.mTvTime.setText(UtilDate.getDifferNowTime(vo.getTime(), System.currentTimeMillis() / 1000));
            if (commentoff == 0) {
                itemViewHolder.mTvReply.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(vo.getReply_num())) {
                    int num = Integer.valueOf(vo.getReply_num());
                    if (num == 0) {
                        itemViewHolder.mTvReply.setText("回复");
                    } else {
                        itemViewHolder.mTvReply.setText(num + "条回复");
                    }
                } else {
                    itemViewHolder.mTvReply.setText("回复");
                }
                itemViewHolder.mTvReply.setTag(vo);
                itemViewHolder.mTvReply.setOnClickListener(mListener);
            } else {
                itemViewHolder.mTvReply.setVisibility(View.GONE);
            }
        } else {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            if (mResult.isFavorite()) {
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
        return 1 + (result == null ? 0 : result.size());
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
        private TextView mTvRankName;
        private TextView mTvTime;
        private TextView mTvReply;

        public ItemViewHolder(View itemView) {
            super(itemView);
            comment_touxiang = (CircleImageView) itemView.findViewById(R.id.comment_touxiang);
            comment_user_name = (TextView) itemView.findViewById(R.id.comment_user_name);
            comment_context = (TextView) itemView.findViewById(R.id.comment_context);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvReply = (TextView) itemView.findViewById(R.id.tv_reply);
            mTvRankName = (TextView) itemView.findViewById(R.id.tv_rank_name);
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

        private TextView tvShare;
        private TextView tvCollect;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAnchor = itemView.findViewById(R.id.tv_anchor);
            tvIntroduce = (TextView) itemView.findViewById(R.id.tv_introduce);
            ivOpenClose = (ImageView) itemView.findViewById(R.id.iv_open_close);
            vLine = itemView.findViewById(R.id.v_line);
            llRewardCollect = (LinearLayout) itemView.findViewById(R.id.ll_reward_collect);
            rlCollect = (RelativeLayout) itemView.findViewById(R.id.rl_collect);
            rlCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListenBookDialog dialog = new ListenBookDialog(activity);
                    dialog.setData(mResult.getTingshuka());
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
                        if (AppData.liWuResult != null) {
                            GiftDialog dialog = new GiftDialog(activity);
                            dialog.setBookId(activity.getBookId());
                            dialog.show();
                        } else {
                            BaseObserver baseObserver = new BaseObserver<LiWuResult>(activity) {
                                @Override
                                public void success(LiWuResult data) {
                                    super.success(data);
                                    AppData.liWuResult = data;
                                    GiftDialog dialog = new GiftDialog(activity);
                                    dialog.setBookId(activity.getBookId());
                                    dialog.show();
                                }

                                @Override
                                public void error() {
                                }
                            };
                            activity.mDisposable.add(baseObserver);
                            UtilRetrofit.getInstance().create(HttpService.class).getRewardSymbol().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
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
            UtilGlide.loadImg(activity, mResult.getThumb(), ivCover);
            tvTitle.setText(mResult.getTitle());
            tvAnchor.setText("主播：" + mResult.getBroadercaster());
            tvIntroduce.setText("\t" + mResult.getTips());

            int line = tvIntroduce.getMaxLines();
            if (line <= 3) {
                ivOpenClose.setVisibility(View.GONE);
            } else {
                tvIntroduce.setMaxLines(3);
            }
            if (mResult.getTingshuka() != null && mResult.getTingshuka().size() > 0) {
                rlCollect.setVisibility(View.VISIBLE);
            } else {
                rlCollect.setVisibility(View.GONE);
                hView.setVisibility(View.GONE);
            }
            if (mResult.isReward()) {
                rlReward.setVisibility(View.VISIBLE);
                hView.setVisibility(View.VISIBLE);
            } else {
                rlReward.setVisibility(View.GONE);
                hView.setVisibility(View.GONE);
            }
            if (rlReward.getVisibility() == View.GONE && hView.getVisibility() == View.GONE && rlCollect.getVisibility() == View.GONE) {
                llRewardCollect.setVisibility(View.GONE);
                vLine.setVisibility(View.GONE);
            }
            tvComment = itemView.findViewById(R.id.tv_comment);
            if (mResult.getCommentoff() == 1) {
                tvComment.setVisibility(View.GONE);
            } else {
                tvComment.setVisibility(View.VISIBLE);
                tvComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TokenManager.isLogin(activity)) {
                            activity.intent(LoginMainActivity.class);
                        } else {
                            SendMessDialog dialog = new SendMessDialog(activity, subview);
                            dialog.setBookId(String.valueOf(activity.getBookId()));
                            dialog.show();
                        }
                    }
                });
            }
//            Glide.with(activity).asBitmap().load(mResult.getThumb()).into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                    if(resource != null) {
//                        Blurry.with(activity).radius(30).sampling(8).from(resource).into(activity.getIvBlurry());
//                    }
//                }
//            });
            tvShare = itemView.findViewById(R.id.tv_share);
            tvShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            tvCollect = itemView.findViewById(R.id.tv_collect);
            if (mResult.isFavorite()) {
                tvCollect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bookdetails_collect, 0, 0, 0);
            } else {
                tvCollect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bookdetails_uncollect, 0, 0, 0);
            }
            tvCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TokenManager.isLogin(activity)) {
                        activity.intent(LoginMainActivity.class);
                        return;
                    }
                    if (mResult.isFavorite()) {
                        uncollectBook();
                    } else {
                        collectBook();
                    }
                }
            });
            tvShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity.getChapterList() == null) {
                        activity.showToast("数据加载中，请稍后分享");
                        return;
                    }
                    ShareDialog shareDialog = new ShareDialog(activity);
                    shareDialog.setImageUrl(mResult.getThumb());
                    shareDialog.setBookname(mResult.getTitle());
                    shareDialog.setId(activity.getChapterList().get(0).getId() + "");
                    shareDialog.show();
                }
            });
        }
    }


    private class ItemOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            CommentDate date = (CommentDate) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putSerializable("vo", date);
            bundle.putInt("bookId", bookId);
            UtilIntent.intentDIY(activity, ReplyMessageActivity.class, bundle);
        }
    }


    /**
     * 收藏
     */
    private void collectBook() {
        BaseObserver baseObserver = new BaseObserver<BaseResult>(activity) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                activity.showToast("收藏成功");
                mResult.setIsFavorite(true);
                notifyDataSetChanged();
            }

            @Override
            public void error() {
            }
        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).setFavorite(TokenManager.getUid(activity), String.valueOf(bookId)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    /**
     * 取消收藏
     */
    private void uncollectBook() {
        BaseObserver baseObserver = new BaseObserver<BaseResult>(activity) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                activity.showToast("取消收藏");
                mResult.setIsFavorite(false);
                notifyDataSetChanged();
            }

            @Override
            public void error() {
            }
        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).deleteFavorite(TokenManager.getUid(activity), String.valueOf(bookId)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }
}
