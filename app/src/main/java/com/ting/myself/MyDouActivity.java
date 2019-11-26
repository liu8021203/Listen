package com.ting.myself;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.base.ListenDialog;
import com.ting.bean.BaseResult;
import com.ting.bean.MoneyResult;
import com.ting.bean.myself.GetVIPTingdouResult;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.myself.adapter.MyDouAdapter;
import com.ting.util.UtilGlide;
import com.ting.util.UtilRetrofit;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/9/10.
 * 我的听豆frame;
 */
public class MyDouActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private TextView tvMoney;
    private Button btnVIP;

    private CircleImageView ivImg;
    private TextView tvName;
    private String vipPrice = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_my_dou);
    }

    @Override
    protected String setTitle() {
        return "我的听豆";
    }



    @Override
    protected void initView() {
        ivImg =  findViewById(R.id.person_touxiang);//用户头像
        tvName =  findViewById(R.id.tv_name);//用户名称
        tvMoney =  findViewById(R.id.tv_money);//余额
        btnVIP = findViewById(R.id.btn_vip);
        btnVIP.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.recycle_view);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(manager);


    }

    @Override
    protected void initData() {

    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(mActivity));
        map.put("type", "1");
        BaseObserver baseObserver = new BaseObserver<BaseResult<MoneyResult>>(this){
            @Override
            public void success(BaseResult<MoneyResult> data) {
                super.success(data);
                MoneyResult result = data.getData();
                if(result != null) {
                    TokenManager.setInfo(result.getUserInfo());
                    tvMoney.setText("余额:" + result.getUserInfo().getMoney() + "听豆");
                    UtilGlide.loadHeadImg(mActivity, result.getUserInfo().getImage(), ivImg);
                    tvName.setText(result.getUserInfo().getNickname());
                    if (result.getMoneyList() != null && !result.getMoneyList().isEmpty()) {
                        MyDouAdapter adapter = new MyDouAdapter((MyDouActivity) mActivity);
                        adapter.setData(result.getMoneyList());
                        mRecyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void error(BaseResult<MoneyResult> value, Throwable e) {
                super.error(value, e);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).money(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }








    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){

            case R.id.btn_vip:
                if(TextUtils.isEmpty(vipPrice)) {
                    btnVIP.setEnabled(false);
                    BaseObserver baseObserver = new BaseObserver<GetVIPTingdouResult>(this) {
                        @Override
                        public void success(GetVIPTingdouResult data) {
                            super.success(data);
                            btnVIP.setEnabled(true);
                            vipPrice = data.getData();
                            ListenDialog.makeListenDialog(mActivity, null, "确认花费" + data.getData() + "听豆办理一个月会员", true, "取消", true, "确定", new ListenDialog.CallBackListener() {
                                @Override
                                public void callback(ListenDialog dialog, int mark) {
                                    dialog.dismiss();
                                    if (mark == ListenDialog.RIGHT) {
                                        buyVip();
                                    }
                                }
                            }).show();
                        }

                        @Override
                        public void error() {
                            btnVIP.setEnabled(true);
                        }
                    };
                    mDisposable.add(baseObserver);
                    UtilRetrofit.getInstance().create(HttpService.class).get_vip_fee_tingdou().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
                }else{
                    ListenDialog.makeListenDialog(mActivity, null, "确认花费" + vipPrice + "听豆办理一个月会员", true, "取消", true, "确定", new ListenDialog.CallBackListener() {
                        @Override
                        public void callback(ListenDialog dialog, int mark) {
                            dialog.dismiss();
                            if (mark == ListenDialog.RIGHT) {
                                buyVip();
                            }
                        }
                    }).show();
                }
                break;
        }
    }


    private void buyVip(){
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(mActivity));
        map.put("month","1");
//        BaseObserver baseObserver = new BaseObserver<BaseResult>(this) {
//            @Override
//            public void success(BaseResult data) {
//                super.success(data);
//                UserInfoResult result = TokenManager.getInfo(mActivity);
//                result.setIsvip(1);
//                TokenManager.setInfo(mActivity, result);
//                btnVIP.setEnabled(true);
//                if(TokenManager.getInfo(mActivity).getIsvip() == 0){
//                    tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.mipmap.no_vip, 0);
//                    btnVIP.setText("成为VIP会员");
//                }else{
//                    tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.mipmap.vip, 0);
//                    btnVIP.setText("VIP会员续费");
//                }
//                EventBus.getDefault().post(new MessageEventBus(MessageEventBus.BUY_VIP));
//            }
//
//            @Override
//            public void error() {
//                btnVIP.setEnabled(true);
//            }
//        };
//        mDisposable.add(baseObserver);
//        UtilRetrofit.getInstance().create(HttpService.class).buy_vip(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }
}
