package com.ting.search.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.bean.search.SearchBean;
import com.ting.play.BookDetailsActivity;
import com.ting.search.SearchActivity;

import java.util.List;

/**
 * Created by gengjiajia on 15/10/15.
 */
public class SearchListAdapter extends BaseAdapter {
    private SearchActivity activity;
    private List<SearchBean> result;
    private LayoutInflater mInflater;

    public SearchListAdapter(SearchActivity searchActivity, List<SearchBean> data) {
        this.activity = searchActivity;
        this.result = data;
        this.mInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return result == null ? 0 : result.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.search_item, null);
            viewHolder.search_item = (RelativeLayout) convertView.findViewById(R.id.search_item);
            viewHolder.search_item_name = (TextView) convertView.findViewById(R.id.search_item_name);

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.search_item_name.setText(result.get(position).getTitle());
        viewHolder.search_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();
                bundle.putInt("playskip", 1);
                bundle.putInt("bookID", result.get(position).getId());
                activity.intent(BookDetailsActivity.class, bundle);


            }
        });
        return convertView;
    }

    private class ViewHolder {
        private RelativeLayout search_item;//搜索条目
        private TextView search_item_name;//搜索书籍名称
    }
}
