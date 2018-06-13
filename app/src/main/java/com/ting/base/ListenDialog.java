package com.ting.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ting.R;

/**
 * Created by liu on 15/12/7.
 */
public class ListenDialog extends Dialog implements View.OnClickListener{
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    private TextView tvLeft;
    private TextView tvRight;
    private TextView tvTitle;
    private TextView tvContent;
    private CallBackListener listener;
    private boolean left;
    private boolean right;
    private String leftStr;
    private String rightStr;
    private String title;
    private String content;
    private View line;

    public ListenDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reward_sure);
        init();
    }


    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLeftStr(String leftStr) {
        this.leftStr = leftStr;
    }

    public void setRightStr(String rightStr) {
        this.rightStr = rightStr;
    }

    public void setListener(CallBackListener listener) {
        this.listener = listener;
    }

    private void init() {
        tvTitle =  findViewById(R.id.tv_title);
        tvContent =  findViewById(R.id.tv_content);
        tvLeft =  findViewById(R.id.tv_left);
        tvRight =  findViewById(R.id.tv_right);
        line = findViewById(R.id.line);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    public static ListenDialog makeListenDialog(Context context, String title, String content, boolean left, String leftStr, boolean right, String rightStr, CallBackListener listener){
        ListenDialog dialog = new ListenDialog(context);
        dialog.setListener(listener);
        dialog.setTitle(title);
        dialog.setContent(content);
        dialog.setLeft(left);
        dialog.setLeftStr(leftStr);
        dialog.setRight(right);
        dialog.setRightStr(rightStr);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_left:
                if(listener != null){
                    listener.callback(this, LEFT);
                }
                break;

            case R.id.tv_right:
                if(listener != null){
                    listener.callback(this, RIGHT);
                }
                break;
        }
    }


    @Override
    public void show() {
        super.show();
        if(TextUtils.isEmpty(title)){
            tvTitle.setVisibility(View.GONE);
        }else{
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        if(TextUtils.isEmpty(content)){
            tvContent.setVisibility(View.GONE);
        }else{
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(content);
        }
        if(left && right){
            line.setVisibility(View.VISIBLE);
        }else{
            line.setVisibility(View.GONE);
        }
        if(left){
            tvLeft.setVisibility(View.VISIBLE);
            tvLeft.setText(leftStr);
        }else{
            tvLeft.setVisibility(View.GONE);
        }
        if(right){
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText(rightStr);
        }else{
            tvRight.setVisibility(View.GONE);
        }
    }

    public interface CallBackListener
    {
        public void callback(ListenDialog dialog, int mark);
    }

}
