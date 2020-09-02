package com.ting.play.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;


import com.lechuan.midunovel.view.holder.FoxNativeAdHelper;
import com.lechuan.midunovel.view.holder.FoxTextLintAd;
import com.lechuan.midunovel.view.interfaces.FoxTextLinkHolder;
import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.bean.vo.ChapterSelectVO;
import com.ting.common.TokenManager;
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
    private FoxTextLinkHolder nativeInfoHolder;
    private LinearLayout llAd;

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
        llAd = findViewById(R.id.ll_ad);
        nativeInfoHolder = FoxNativeAdHelper.getFoxTextLinkHolder();
        nativeInfoHolder.loadInfoAd(mActivity, 360467, TokenManager.getUid(mActivity), new FoxTextLinkHolder.LoadInfoAdListener() {
            @Override
            public void onError(String s) {

            }

            @Override
            public void infoAdSuccess(FoxTextLintAd ad) {
                View view = null;
                if (ad != null) {
                    view = ad.getView();
                }
                if (view != null && llAd != null && !mActivity.isFinishing()) {
                    llAd.removeAllViews();
                    llAd.addView(view);
                }
            }

            @Override
            public void onReceiveAd() {

            }

            @Override
            public void onFailedToReceiveAd() {

            }

            @Override
            public void onLoadFailed() {

            }

            @Override
            public void onCloseClick() {

            }

            @Override
            public void onAdClick() {

            }

            @Override
            public void onAdExposure() {

            }

            @Override
            public void onAdActivityClose(String s) {

            }
        });
    }


    public interface ChapterSelectCallBackListener{
        void callback(int page);
    }

    @Override
    public void dismiss() {
        if (nativeInfoHolder != null) {
            nativeInfoHolder.destroy();
        }
        super.dismiss();
    }
}
