package com.ting.myself;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.ting.myself.dialog.PayDialog;
import com.ting.util.UtilGlide;
import com.ting.util.UtilRetrofit;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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

    private CircleImageView ivImg;
    private TextView tvName;
    private EditText etNum;
    private Button btnPay;

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
        mRecyclerView = findViewById(R.id.recycle_view);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(manager);
        etNum = findViewById(R.id.et_num);
        btnPay = findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(this);
        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if(!TextUtils.isEmpty(str)){
                    int price = Integer.valueOf(str) * 5;
                    DecimalFormat df = new DecimalFormat("#.##");
                    String payPrice = df.format((float)price / 100);
                    btnPay.setText("支付" + payPrice + "元");
                }else{
                    btnPay.setText("支付");
                }
            }
        });

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
            case R.id.btn_pay:
                String num = etNum.getText().toString();
                if(TextUtils.isEmpty(num)){
                    showToast("请输入要购买的听豆数量");
                    return;
                }
                PayDialog dialog = new PayDialog(mActivity);
                dialog.setNum(num);
                int price = Integer.valueOf(num) * 5;
                dialog.setPrice(String.valueOf(price));
                dialog.setDesc(num + "个听豆");
                dialog.show();
                break;

        }
    }


}
