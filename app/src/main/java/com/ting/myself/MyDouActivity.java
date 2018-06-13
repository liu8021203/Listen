package com.ting.myself;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.base.ListenDialog;
import com.ting.base.MessageEventBus;
import com.ting.bean.BaseResult;
import com.ting.bean.UserInfoResult;
import com.ting.bean.myself.DouPayRank;
import com.ting.bean.myself.GetVIPTingdouResult;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.myself.adapter.MyDouAdapter;
import com.ting.myself.dialog.PayDialog;
import com.ting.bean.myself.BaoResult;
import com.ting.bean.myself.MyDouResult;
import com.ting.bean.myself.PayResult;
import com.ting.util.SignUtils;
import com.ting.util.UtilGlide;
import com.ting.util.UtilRetrofit;
import com.ting.view.GridItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    private Map<String, String> map = new HashMap<>();
    private TextView tvMoney;
    private String payType;
    private PayResult payResult;
    private DouPayRank mDouPayRank;
    private Button btnVIP;
    private static final int SDK_PAY_FLAG = 1;
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAMdQDARfXcntAYBkQk2czFTi1LCKZoAGx96LGNOynprfQexpuRrabOF9g88lxbmQlc9cl/XSgudckNf0lEot2Br87+i4Bf3abYV5b1EDrNMtjlXeg+14peKLzIRJNUk94iHzeUPxpZCyQkuzbGsepp/wtcG4kEoX5mXQgroKTvg5AgMBAAECgYEAtcrNKy/Q29zRAcpwr1nVBZffZybVVvDYXKOCgZTqFViNSJUlwNh5dzKEhuxs7FR0UC6kbBMWUvCbLF5o4z/tWxOt5IZJFHsq9bvtf3zPGlRH0+Hw7SZCLAcE4Xeo6exNXsJGDWjUFpnam6cyUT4sOfEF9e+7JdqdC8NPo+rk9gECQQDufUQ+UyapopXYBDEjSQIZawKNW8YGSTrXmVUZKvMiLgxcZllbh4UDUIOGf7L8tAHIpCiIWXVSU87uU8B521crAkEA1fJm2xRNTh9roHH/FCFjSokdaqBqObJd7zGj9vw2xWcEh5E0Y9DBNU/W9/cqxMSavf8RSKwMl8mEYYSY1Xv8KwJBAIJ8w6QeNt9F+BqzYHdf8OgbZtAVUKoL00g3uxoEJ/ehnl3IGkg8RAjmOrCSmkot63Pubcb8u3aKKIS6CmwiGPUCQQCnI+NE3bxy/sr3InezQ04tiaboiP/TqUwHNNGyf2FOoGeCfWbfX9PBEWmQeJD5/doh+mWHT5na7vyzroIzrdPXAkEAkuZAJKOmKEBzGbBq8nsL0+Te/wVK3EDUzPbKbyUiBGByWnNJ9qQm7KtKcu/WbvwROj9BAcPHHdBneQFYuYsDKw==";
    // 商户PID
    public static final String PARTNER = "2088121134122241";
    // 商户收款账号
    public static final String SELLER = "15911501888@139.com";
    private CircleImageView person_touxiang;
    private TextView tvName;
    private IWXAPI api;
    private String vipPrice = null;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    BaoResult payResult = new BaoResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        showToast("支付成功");
                        getData();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getApplicationContext(), "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(getApplicationContext(), "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }

                default:
                    break;
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_my_dou);
        api = WXAPIFactory.createWXAPI(this, AppData.WX_APP_ID);
        showRightText("明细");
    }

    @Override
    protected String setTitle() {
        return "我的听豆";
    }



    @Override
    protected void initView() {
        person_touxiang =  findViewById(R.id.person_touxiang);//用户头像
        tvName =  findViewById(R.id.tv_name);//用户名称
        tvMoney =  findViewById(R.id.tv_money);//余额
        btnVIP = findViewById(R.id.btn_vip);
        btnVIP.setOnClickListener(this);
        if (TokenManager.getInfo(this) != null) {
            tvName.setText("昵称：" + TokenManager.getInfo(this).getNickname() + "");
            UtilGlide.loadAnchorImg(this, TokenManager.getInfo(this).getThumb(), person_touxiang);
            if(TokenManager.getInfo(this).getIsvip() == 0){
                tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.mipmap.no_vip, 0);
                btnVIP.setText("成为VIP会员");
            }else{
                tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.mipmap.vip, 0);
                btnVIP.setText("VIP会员续费");
            }
        }
        mRecyclerView = findViewById(R.id.recycle_view);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(manager);


    }

    @Override
    protected void initData() {

    }

    private void getData() {
        BaseObserver baseObserver = new BaseObserver<MyDouResult>(this){
            @Override
            public void success(MyDouResult data) {
                super.success(data);
                tvMoney.setText("余额:" + data.getBalance() + "听豆");
                if (data.getType() != null && data.getType().size() > 0) {
                    MyDouAdapter adapter = new MyDouAdapter((MyDouActivity) mActivity);
                    adapter.setData(data.getType().get(0).getGrade());
                    mRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void error() {
                super.error();
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getUserBalance(TokenManager.getUid(this)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }

    public void showPayDialog(DouPayRank douPayRank) {
        this.mDouPayRank = douPayRank;
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        PayDialog dialog = new PayDialog(this, douPayRank, isPaySupported);
        dialog.show();
    }



    /**
     * 去支付
     */
    public void toPay(String payStype) {
        this.payType = payStype;
        map.clear();
        map.put("uid", TokenManager.getUid(mActivity));
        map.put("cid", String.valueOf(mDouPayRank.getId()));
        map.put("type", payStype);
        BaseObserver baseObserver = new BaseObserver<PayResult>(this){
            @Override
            public void success(PayResult data) {
                super.success(data);
                MyDouActivity.this.payResult = data;
                if (payType.equals("wechat")) {
                    WXPay();
                } else if (payType.equals("alipay")) {
                    AliPay();
                }
            }

            @Override
            public void error() {
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).callbackCharge(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    /**
     * 微信支付
     **/
    private void WXPay() {
        PayReq req = new PayReq();
        req.appId = AppData.WX_APP_ID;//微信分配的公众账号ID
        req.partnerId = payResult.getPartnerId();//微信支付分配的商户号

        req.nonceStr = payResult.getNonceStr();

        req.prepayId = payResult.getWechat();//微信返回的支付交易会话ID

        req.timeStamp = payResult.getTime();////时间戳，请见接口规则-参数规定
        req.packageValue = "Sign=WXPay";//暂填写固定值Sign=WXPay

        req.sign = payResult.getSign();//签名


//            req.extData = "app data"; // optional
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信

        api.sendReq(req);
    }


    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }




    /**
     * 支付宝支付
     **/
    private void AliPay() {
        // 订单
        String orderInfo = getOrderInfo(payResult, mDouPayRank.getMoney().replace("元", ""));


        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        // 完整的符合支付宝参数规范的订单信息

        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();


        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(MyDouActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);

            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();


    }

    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }


    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(PayResult payResult, String money) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";
        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + payResult.getOrdersn() + "\"";
        // 商品名称
        orderInfo += "&subject=" + "\"" + "听世界听豆" + "\"";
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + payResult.getCallbackurl()
                + "\"";
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + money + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空

        return orderInfo;
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
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
            case R.id.tv_right:
                intent(DouDetailsActivity.class);
                break;
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
        BaseObserver baseObserver = new BaseObserver<BaseResult>(this) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                UserInfoResult result = TokenManager.getInfo(mActivity);
                result.setIsvip(1);
                TokenManager.setInfo(mActivity, result);
                btnVIP.setEnabled(true);
                if(TokenManager.getInfo(mActivity).getIsvip() == 0){
                    tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.mipmap.no_vip, 0);
                    btnVIP.setText("成为VIP会员");
                }else{
                    tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.mipmap.vip, 0);
                    btnVIP.setText("VIP会员续费");
                }
                EventBus.getDefault().post(new MessageEventBus(MessageEventBus.BUY_VIP));
            }

            @Override
            public void error() {
                btnVIP.setEnabled(true);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).buy_vip(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }
}
