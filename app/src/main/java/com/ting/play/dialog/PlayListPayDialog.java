package com.ting.play.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.anchor.ListenBookVO;
import com.ting.bean.play.PlayingVO;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.play.BookDetailsActivity;
import com.ting.bean.play.PayResult;
import com.ting.util.UtilRetrofit;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 15/11/5.
 */
public class PlayListPayDialog extends Dialog implements OnClickListener{
    private RadioButton radio_single;
    private RadioButton radio_all;
    private RadioButton radio_vip;
    private Button btn_cancle;
    private Button btn_buy;
    //type：0为单集购买，1为全集购买，2为购买VIP
    private int type = 0;
    private BaseActivity activity;
    private PlayingVO vo;
    private int price;
    private CallBackListener listener;
    private int bookId;
    private List<ListenBookVO> tingshuka;



    public PlayListPayDialog(BaseActivity activity) {
        super(activity, R.style.CustomDialog);
        this.activity = activity;
    }



    public void setVo(PlayingVO vo, int price) {
        this.vo = vo;
        this.price = price;
    }

    public void setTingshuka(List<ListenBookVO> tingshuka) {
        this.tingshuka = tingshuka;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setListener(CallBackListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay_select);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        init();
    }


    private void init() {
        btn_buy =  findViewById(R.id.btn_ok);
        btn_buy.setOnClickListener(this);
        btn_cancle =  findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(this);
        radio_single =  findViewById(R.id.radio_single);
        radio_all =  findViewById(R.id.radio_all);
        radio_vip =  findViewById(R.id.radio_vip);
        radio_single.setChecked(true);
        radio_single.setText("单集打赏" + vo.getPrice() + "听豆");
        radio_single.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = 0;
                }
            }
        });
        radio_all.setText("全集打赏" + price + "听豆");
        radio_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    type = 1;
                }
            }
        });
        radio_vip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    type = 2;
                }
            }
        });
        btn_buy =  findViewById(R.id.btn_ok);
        btn_buy.setOnClickListener(this);
        btn_cancle =  findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(this);
        if(tingshuka != null && !tingshuka.isEmpty())
        {
            radio_vip.setVisibility(View.VISIBLE);
        }
        else {
            radio_vip.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_cancle:
                dismiss();
                break;
            case R.id.btn_ok:{
                switch (type)
                {
                    case 0:
                    {
                        buySingle();
                    }
                        break;

                    case 1:
                    {
                        buyAll();
                    }
                        break;

                    case 2:
                    {
                        dismiss();
                        ListenBookDialog dialog = new ListenBookDialog(activity);
                        dialog.setData(tingshuka);
                        dialog.show();
                    }
                    break;
                }
            }
                break;

        }
    }




    public interface CallBackListener
    {
        void singleCallBack();
        void allCallBack();
        void listBookCallBack();
    }

    /**
     * 购买单集
     */
    private void buySingle(){
        BaseObserver baseObserver = new BaseObserver<PayResult>(activity){
            @Override
            public void success(PayResult data) {
                super.success(data);
                if(data.getBlistinfo() != null) {
                    vo.setUrl(data.getBlistinfo().getUrl());
                    listener.singleCallBack();
                }
                dismiss();
            }

            @Override
            public void error() {
            }
        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).buyBookSingle(String.valueOf(vo.getId()), TokenManager.getUid(activity), String.valueOf(bookId)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    /**
     * 购买全集
     */
    private void buyAll(){
        BaseObserver baseObserver = new BaseObserver<PayResult>(activity){
            @Override
            public void success(PayResult data) {
                super.success(data);
                listener.allCallBack();
                dismiss();
            }

            @Override
            public void error() {
            }
        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).buyBookAll(TokenManager.getUid(activity), String.valueOf(bookId)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

}
