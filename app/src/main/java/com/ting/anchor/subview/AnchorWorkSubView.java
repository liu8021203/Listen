package com.ting.anchor.subview;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.bean.vo.BookVO;
import com.ting.category.adapter.CategoryListAdapter;
import com.ting.view.CustomItemDecoration;

import java.util.List;


/**
 * Created by gengjiajia on 15/9/1.
 * 主播作品
 */
public class AnchorWorkSubView extends LinearLayout {
    private AnchorMainActivity activity;
    private RecyclerView mRecyclerView;
    private LayoutInflater inflater;
    private View mView;
    private TextView anchor_work_zhang_shu;
    private CategoryListAdapter adapter;

    public AnchorWorkSubView(AnchorMainActivity activity) {
        super(activity);
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        mView = inflater.inflate(R.layout.subview_anchor_work, this);
        intView();
    }

    public void setData(List<BookVO> data) {
        if (data != null && !data.isEmpty()) {
            anchor_work_zhang_shu.setText(String.valueOf(data.size()));
            adapter = new CategoryListAdapter(activity);
            adapter.setData(data);
            mRecyclerView.setAdapter(adapter);
        }
    }


    private void intView() {
        mRecyclerView = mView.findViewById(R.id.swipe_target);
        anchor_work_zhang_shu = mView.findViewById(R.id.anchor_work_zhang_shu);//章数
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1);
        mRecyclerView.addItemDecoration(decoration);
    }


}
