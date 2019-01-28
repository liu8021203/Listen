package com.ting.play.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.vo.CommentListVO;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.bean.play.MessageResult;
import com.ting.play.subview.PlayIntroduceSubView;
import com.ting.util.UtilRetrofit;
import com.ting.util.UtilSystem;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/11/4.
 * 发送信息dialog
 */
public class SendMessDialog extends Dialog implements View.OnClickListener {
    private BaseActivity mActivity;
    private EditText pinglun_comment;
    private RelativeLayout send_layout;
    private String bookId;
    private SendMessageCallBackListener mListener;

    public SendMessDialog(BaseActivity activity) {
        super(activity, R.style.SendMessageDialog);
        this.mActivity = activity;
    }

    public void setListener(SendMessageCallBackListener listener) {
        mListener = listener;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_send_mess);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        initView();

    }

    private void initView() {
        pinglun_comment =  findViewById(R.id.pinglun_comment);
        send_layout =  findViewById(R.id.send_layout);
        send_layout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_layout:
                String context = pinglun_comment.getText().toString().trim();
                if(TextUtils.isEmpty(context)){
                    mActivity.showToast("请输入内容");
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("commentsType", "1");
                map.put("bookId", bookId);
                map.put("commentsUserId", TokenManager.getUid(mActivity));
                map.put("commentsContent", context);
                BaseObserver baseObserver = new BaseObserver<BaseResult<CommentListVO>>(mActivity, BaseObserver.MODEL_SHOW_DIALOG_TOAST){
                    @Override
                    public void success(BaseResult<CommentListVO> data) {
                        super.success(data);
                        CommentListVO vo = data.getData();
                        if(mListener != null){
                            mListener.callback(vo);
                        }
                        dismiss();
                    }

                    @Override
                    public void error(BaseResult<CommentListVO> value, Throwable e) {
                        super.error(value, e);
                    }
                };
                mActivity.mDisposable.add(baseObserver);
                UtilRetrofit.getInstance().create(HttpService.class).comments(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
                break;
        }
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UtilSystem.showKeyword(pinglun_comment);
        }
    };

    @Override
    public void dismiss() {
        super.dismiss();
        UtilSystem.hidKeyword(pinglun_comment);
    }

    @Override
    public void show() {
        super.show();
        handler.sendEmptyMessageDelayed(0, 200);
    }


    public interface SendMessageCallBackListener{
        void callback(CommentListVO vo);
    }
}
