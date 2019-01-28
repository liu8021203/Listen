package com.ting.search.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.bean.search.SearchBean;
import com.ting.bean.vo.HotSearchVO;
import com.ting.play.BookDetailsActivity;
import com.ting.search.SearchActivity;

import java.util.List;

/**
 * Created by liu on 16/7/25.
 */
public class SearchHotAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private SearchActivity activity;
    private List<HotSearchVO> data;
    private ItemOnClickListener listener;

    // Constructors
    public SearchHotAdapter(SearchActivity activity) {
        this.mInflater = LayoutInflater.from( activity );
        this.activity = activity;
        this.listener = new ItemOnClickListener();
    }

    public void setResult(List<HotSearchVO> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2016-07-25 22:19:50 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private static class ViewHolder {
        public final LinearLayout rootView;
        public final TextView tvName;

        private ViewHolder(LinearLayout rootView, TextView tvName) {
            this.rootView = rootView;
            this.tvName = tvName;
        }

        public static ViewHolder create(LinearLayout rootView) {
            TextView tvName = (TextView)rootView.findViewById( R.id.tv_name );
            return new ViewHolder( rootView, tvName);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if ( convertView == null ) {
            View view = mInflater.inflate( R.layout.gridview_search_hot_item, parent, false );
            vh = ViewHolder.create( (LinearLayout)view );
            view.setTag( vh );
        } else {
            vh = (ViewHolder)convertView.getTag();
        }
        HotSearchVO vo = data.get(position);

        // TODOBind your data to the views here
        vh.tvName.setText(vo.getName());
        vh.tvName.setTag(vo);
        vh.tvName.setOnClickListener(listener);
        return vh.rootView;
    }

    private class ItemOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            SearchBean vo = (SearchBean) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putInt("bookID", vo.getId());
            activity.intent(BookDetailsActivity.class, bundle);
        }
    }

}
