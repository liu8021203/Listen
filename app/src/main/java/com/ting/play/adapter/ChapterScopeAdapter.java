package com.ting.play.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ting.R;
import com.ting.play.BookDetailsActivity;
import com.ting.bean.play.chapterScopeVO;
import com.ting.play.subview.PlayListSubView;

import java.util.List;

/**
 * Created by liu on 16/7/12.
 */
public class ChapterScopeAdapter extends BaseAdapter{
    private BookDetailsActivity activity;
    private List<chapterScopeVO> data;
    private ItemOnClickListener listener;
    private PlayListSubView view;

    public ChapterScopeAdapter(BookDetailsActivity activity, PlayListSubView view) {
        this.activity = activity;
        this.listener = new ItemOnClickListener();
        this.view = view;
    }

    public void setData(List<chapterScopeVO> data) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.grid_chapter_scope_item, null);
            holder.tvScope =  convertView.findViewById(R.id.tv_scope);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        chapterScopeVO vo = data.get(position);
        holder.tvScope.setText(vo.getName());
        holder.tvScope.setTag(vo);
        holder.tvScope.setOnClickListener(listener);
        return convertView;
    }


    private class ViewHolder
    {
        TextView tvScope;
    }

    private class ItemOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            chapterScopeVO vo = (chapterScopeVO) v.getTag();
            view.getData(0, vo.getPage());
            view.closeTiaozhuan();
        }
    }
}
