package com.ting.search.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.bean.search.SearchMess;
import com.ting.bean.vo.HotSearchVO;
import com.ting.play.BookDetailsActivity;
import com.ting.search.SearchActivity;
import com.ting.util.UtilGlide;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/8.
 * 搜索字段结果集合
 */
public class SearchResultAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<HotSearchVO> result;
    private SearchActivity activity;

    public SearchResultAdapter(SearchActivity searchActivity) {
        this.activity = searchActivity;
        inflater = inflater.from(activity);
    }

    public void setResult(List<HotSearchVO> result) {
        this.result = result;
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
            convertView = inflater.inflate(R.layout.item_surch_result, null);
            viewHolder.search_result_item =  convertView.findViewById(R.id.search_result_item);
            viewHolder.search_result_image =  convertView.findViewById(R.id.search_result_image);
            viewHolder.search_result_name =  convertView.findViewById(R.id.search_result_name);
            viewHolder.search_result_anchor =  convertView.findViewById(R.id.search_result_anchor);
            viewHolder.search_result_zang_number =  convertView.findViewById(R.id.search_result_zang_number);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


//        UtilGlide.loadImg(activity,result.get(position).getThumb(),viewHolder.search_result_image);
//
//        viewHolder.search_result_name.setText(result.get(position).getTitle());
//        viewHolder.search_result_zang_number.setText("章数:" + result.get(position).getCount() + "章");
//        viewHolder.search_result_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Bundle bundle = new Bundle();
//                bundle.putInt("bookID", result.get(position).getId());
//                activity.intent(BookDetailsActivity.class, bundle);
//
//
//            }
//        });
        return convertView;
    }

    private class ViewHolder {
        private RelativeLayout search_result_item;
        private ImageView search_result_image;//分类详情图片
        private TextView search_result_name;//分类小说名
        private TextView search_result_anchor;//分类主播
        private TextView search_result_zang_number;//章数
    }
}
