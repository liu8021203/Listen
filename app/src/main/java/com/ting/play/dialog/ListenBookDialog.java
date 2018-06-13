package com.ting.play.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.bean.anchor.ListenBookVO;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.login.LoginMainActivity;
import com.ting.play.BookDetailsActivity;
import com.ting.play.adapter.ListenBookAdapter;
import com.ting.util.UtilRetrofit;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 16/8/3.
 * 听书卡
 */
public class ListenBookDialog extends Dialog implements View.OnClickListener{
    private TextView tvTitle;
    private RecyclerView mRecyclerView;
    private Button btnCancle;
    private Button btnOk;
    private BaseActivity mActivity;
    private List<ListenBookVO> data;
    private ListenBookAdapter adapter;
    private PlayListPayDialog.CallBackListener listener;

    public ListenBookDialog(BaseActivity context) {
        super(context, R.style.CustomDialog);
        this.mActivity = context;
    }

    public void setData(List<ListenBookVO> data) {
        this.data = data;
    }

    public void setListener(PlayListPayDialog.CallBackListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_listen_book);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        initView();
    }

    private void initView() {
        tvTitle = findViewById( R.id.tv_title );
        mRecyclerView =  findViewById( R.id.recycle_view );
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        btnCancle = findViewById( R.id.btn_cancle );
        btnOk = findViewById( R.id.btn_ok );
        btnCancle.setOnClickListener( this );
        btnOk.setOnClickListener( this );
        tvTitle.setText(data.get(0).getTitle());
        adapter = new ListenBookAdapter(mActivity);
        adapter.setData(data);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_cancle:
                dismiss();
                break;

            case R.id.btn_ok:
                if(adapter != null)
                {
                    int defId = adapter.getDefId();
                    if(defId == -1)
                    {
                        mActivity.showToast("请选择听书卡类型");
                        return;
                    }
                }
                if(!TokenManager.isLogin(mActivity))
                {
                    mActivity.intent(LoginMainActivity.class);
                    return;
                }
                if(adapter != null && adapter.getDefId() != -1)
                {
                    BaseObserver baseObserver = new BaseObserver<BaseResult>(mActivity){
                        @Override
                        public void success(BaseResult data) {
                            super.success(data);
                            dismiss();
                            listener.listBookCallBack();
                        }

                        @Override
                        public void error() {
                        }
                    };
                    mActivity.mDisposable.add(baseObserver);
                    UtilRetrofit.getInstance().create(HttpService.class).buy_tingshuka(TokenManager.getUid(mActivity), String.valueOf(adapter.getDefId())).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
                }
                break;
        }
    }

}
