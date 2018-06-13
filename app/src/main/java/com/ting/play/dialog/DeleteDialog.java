package com.ting.play.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ting.R;
import com.ting.db.DBChapter;

/**
 * Created by liu on 15/11/11.
 */
public class DeleteDialog extends Dialog implements OnClickListener{
    private TextView no_sure_reawad;
    private TextView yes_sure_reward;
    private TextView reward_sure_remind;
    private CallBackListener listener;
    private DBChapter vo;
    private int type = -1;


    public DeleteDialog(Context context) {
        super(context, R.style.CustomDialog);
    }


    public void setListener(CallBackListener listener) {
        this.listener = listener;
    }

    public void setVo(DBChapter vo, int type) {
        this.vo = vo;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reward_sure);
        init();
    }

    private void init() {
        reward_sure_remind = (TextView) findViewById(R.id.tv_content);
        no_sure_reawad = (TextView) findViewById(R.id.tv_left);
        yes_sure_reward = (TextView) findViewById(R.id.tv_right);
        no_sure_reawad.setOnClickListener(this);
        yes_sure_reward.setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
        switch (type)
        {
            case 0:
                reward_sure_remind.setText("是否要删除" + vo.getChapterTitle());

                break;
            case 1:
                reward_sure_remind.setText("是否要删除" + vo.getBookName());
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_left:
                dismiss();
                break;

            case R.id.tv_right:
                listener.callback(vo);
                dismiss();
                break;
        }
    }

    public interface CallBackListener
    {
        public void callback(DBChapter vo);
    }
}
