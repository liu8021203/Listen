package com.ting.bookrack;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.ting.R;

/**
 * Created by liu on 2017/12/27.
 */

public class BookRackPopupWindow {
    private PopupWindow mPopupWindow;
    private RelativeLayout rlEditor;
    private RelativeLayout rlMessage;
    private RelativeLayout rlDownload;
    private ImageView ivRed;
    private CallBackListener mListener;


    public BookRackPopupWindow(Context context) {

        mPopupWindow = new PopupWindow();
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setContentView(LayoutInflater.from(context).inflate(R.layout.popupwindow_notice, null));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        rlEditor = mPopupWindow.getContentView().findViewById(R.id.rl_editor);
        rlEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.editor();
                }
            }
        });
        rlMessage = mPopupWindow.getContentView().findViewById(R.id.rl_message);
        rlMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.message();
                }
            }
        });
        rlDownload = mPopupWindow.getContentView().findViewById(R.id.rl_download);
        rlDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.download();
                }
            }
        });
        ivRed = mPopupWindow.getContentView().findViewById(R.id.iv_red);
    }

    public void setListener(CallBackListener listener) {
        mListener = listener;
    }

    public void isShowRed(int visibility){
        ivRed.setVisibility(visibility);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void show(View view) {
        int height = 0;
        int resourceId = view.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = view.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        mPopupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, view.getContext().getResources().getDimensionPixelOffset(R.dimen.actionbarHeight) + height + 20);
    }

    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    public interface CallBackListener {
        void editor();

        void message();

        void download();
    }

    public void dismiss(){
        mPopupWindow.dismiss();
    }
}
