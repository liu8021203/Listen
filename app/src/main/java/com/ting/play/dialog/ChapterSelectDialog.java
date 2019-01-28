package com.ting.play.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;


import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.bean.vo.ChapterSelectVO;
import com.ting.play.adapter.ChapterSelectAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2017/11/22.
 */

public class ChapterSelectDialog extends BottomSheetDialog {
    private AppCompatImageButton ivClose;
    private RecyclerView mRecyclerView;
    private ChapterSelectAdapter mAdapter;
    private BaseActivity mActivity;
    private List<ChapterSelectVO> data;
    private ChapterSelectCallBackListener mListener;

    public ChapterSelectDialog(@NonNull Context context) {
        super(context);
        this.mActivity = (BaseActivity) context;
    }

    public void setListener(ChapterSelectCallBackListener listener) {
        mListener = listener;
    }

    public ChapterSelectCallBackListener getListener() {
        return mListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_chapter_select);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        initView();
    }

    public void setData(int count) {
        if (count == 0) {
            return;
        } else {
            data = new ArrayList<>();
            int num = count / 50;
            if(count % 50 == 0){
                for (int i = 1; i <= num; i++) {
                    ChapterSelectVO vo = new ChapterSelectVO();
                    vo.setPage(i);
                    vo.setCount(count);
                    vo.setName(((i - 1) * 50 + 1) + "~" + (i * 50));
                    data.add(vo);
                }
            }else{
                for (int i = 1; i <= num + 1; i++) {
                    ChapterSelectVO vo = new ChapterSelectVO();
                    vo.setPage(i);
                    vo.setCount(count);
                    vo.setName(((i - 1) * 50 + 1) + "~" + (i * 50));
                    data.add(vo);
                }
            }
        }
    }

    private void initView() {
        ivClose =  findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mRecyclerView =  findViewById(R.id.recycle_view);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 3);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ChapterSelectAdapter(this);
        mAdapter.setData(data);
        mRecyclerView.setAdapter(mAdapter);
    }


    public interface ChapterSelectCallBackListener{
        void callback(int page);
    }
}
