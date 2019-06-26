package com.ting.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ting.R;


public abstract class BaseCommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_ITEM = 0;
    private static final int CONTENT_ITEM = 1;
    private static final int FOOTER_ITEM = 2;
    //是否显示加载更多内容
    private boolean isShowLoadMore = false;
    //显示底部信息
    private boolean isShowFooterDesc = true;
    //点击加载更多数据
    private boolean isClickLoadMore = false;
    private String footerDesc = "";
    private LoadMoreOnClickListener mListener;
    //能否加载更多
    private boolean isEnableLoadMore = false;
    //是否有头部
    private boolean isHeader = false;

    public void setHeader(boolean header) {
        isHeader = header;
    }

    protected abstract int count();

    protected abstract RecyclerView.ViewHolder getContentViewHolder(ViewGroup parent);

    protected RecyclerView.ViewHolder getHeaderViewHolder(ViewGroup parent){
        return null;
    }

    protected abstract void setContentData(RecyclerView.ViewHolder holder, int position);


    public void setListener(LoadMoreOnClickListener listener) {
        mListener = listener;
    }

    public void setEnableLoadMore(boolean enableLoadMore) {
        isEnableLoadMore = enableLoadMore;
    }

    public boolean isEnableLoadMore() {
        return isEnableLoadMore;
    }

    /**
     * 是否在加载中
     * @return
     */
    public boolean isLoadMore() {
        return isShowLoadMore;
    }

    public void loadMore() {
        isShowLoadMore = true;
        isShowFooterDesc = false;
        isClickLoadMore = false;
        footerDesc = "";
    }

    public void loadMoreComplete(){
        isClickLoadMore = false;
        isShowLoadMore = false;
        isShowFooterDesc = true;
        footerDesc = "---- 加载中 ----";
    }

    public boolean isShowFooterDesc() {
        return isShowFooterDesc;
    }

    public void setShowFooterDesc(String footerDesc) {
        isShowFooterDesc = true;
        isShowLoadMore = false;
        isClickLoadMore = false;
        isEnableLoadMore = false;
        this.footerDesc = footerDesc;
    }

    public void setClickLoadMore(boolean b) {
        if(b){
            isClickLoadMore = true;
            isShowFooterDesc = false;
            isShowLoadMore = false;
            isEnableLoadMore = false;
        }else{
            isClickLoadMore = false;
            isShowFooterDesc = true;
            isShowLoadMore = false;
            isEnableLoadMore = false;
        }

    }

    public boolean isClickLoadMore() {
        return isClickLoadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case CONTENT_ITEM: {
                holder = getContentViewHolder(parent);
            }
            break;

            case FOOTER_ITEM: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_load_more, parent, false);
                holder = new FooterViewHolder(view);
            }
            break;

            case HEADER_ITEM:{
                holder = getHeaderViewHolder(parent);
            }
            break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case CONTENT_ITEM:
                if(!isHeader) {
                    setContentData(holder, position);
                }else{
                    setContentData(holder, position - 1);
                }
                break;

            case FOOTER_ITEM:
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                if(isClickLoadMore){
                    footerViewHolder.tvDesc.setVisibility(View.VISIBLE);
                    footerViewHolder.mProgressBar.setVisibility(View.GONE);
                    footerViewHolder.tvDesc.setText("点击加载更多");
                    footerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(mListener != null){
                                mListener.click();
                            }
                        }
                    });
                }else{
                    if (isShowFooterDesc) {
                        footerViewHolder.tvDesc.setVisibility(View.VISIBLE);
                        footerViewHolder.mProgressBar.setVisibility(View.GONE);
                        footerViewHolder.tvDesc.setText(footerDesc);
                    } else if (isShowLoadMore) {
                        footerViewHolder.tvDesc.setVisibility(View.GONE);
                        footerViewHolder.mProgressBar.setVisibility(View.VISIBLE);
                    }
                    footerViewHolder.itemView.setOnClickListener(null);
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(!isHeader) {
            if (position + 1 == getItemCount()) {
                return FOOTER_ITEM;
            } else {
                return CONTENT_ITEM;
            }
        }else{
            if(position == 0){
                return HEADER_ITEM;
            }else if(position + 1 == getItemCount()){
                return FOOTER_ITEM;
            }else{
                return CONTENT_ITEM;
            }
        }
    }

    @Override
    public int getItemCount() {
        if(!isHeader) {
            return count() + 1;
        }else{
            return count() + 2;
        }
    }

    protected class FooterViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;
        private TextView tvDesc;

        public FooterViewHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.progressBar);
            tvDesc = itemView.findViewById(R.id.tv_desc);
        }
    }



    public interface LoadMoreOnClickListener{
        void click();
    }
}
