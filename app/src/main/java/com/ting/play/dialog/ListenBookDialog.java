package com.ting.play.dialog;

import android.app.Dialog;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.vo.CardVO;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.login.LoginMainActivity;
import com.ting.play.adapter.ListenBookAdapter;
import com.ting.util.UtilRetrofit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 16/8/3.
 * 听书卡
 */
public class ListenBookDialog extends Dialog implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private Button btnCancle;
    private Button btnOk;
    private BaseActivity mActivity;
    private List<CardVO> data;
    private ListenBookAdapter adapter;
    private PlayListPayDialog.CallBackListener listener;

    public ListenBookDialog(BaseActivity context) {
        super(context, R.style.CustomDialog);
        this.mActivity = context;
    }

    public void setData(List<CardVO> data) {
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
        mRecyclerView =  findViewById( R.id.recycle_view );
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        btnCancle = findViewById( R.id.btn_cancle );
        btnOk = findViewById( R.id.btn_ok );
        btnCancle.setOnClickListener( this );
        btnOk.setOnClickListener( this );
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
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", TokenManager.getUid(mActivity));
                    map.put("cardId", String.valueOf(adapter.getDefId()));
                    BaseObserver baseObserver = new BaseObserver<BaseResult>(mActivity, BaseObserver.MODEL_SHOW_DIALOG_TOAST){
                        @Override
                        public void success(BaseResult data) {
                            super.success(data);
                            mActivity.showToast("购买成功");
                            dismiss();
                        }


                        @Override
                        public void error(BaseResult value, Throwable e) {
                            super.error(value, e);
                        }
                    };
                    mActivity.mDisposable.add(baseObserver);
                    UtilRetrofit.getInstance().create(HttpService.class).buyCard(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
                }
                break;
        }
    }

}
