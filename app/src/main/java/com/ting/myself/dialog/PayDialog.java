package com.ting.myself.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.AppWxPayResult;
import com.ting.bean.BaseResult;
import com.ting.bean.PayResult;
import com.ting.bean.vo.MoneyListVO;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.myself.MyDouActivity;
import com.ting.util.UtilRetrofit;
import com.ting.view.MyToast;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/9/16.
 * 充值dialog
 */
public class PayDialog extends Dialog implements View.OnClickListener {
    private RelativeLayout wx_pay_layout;
    private RelativeLayout ali_pay_layout;
    private MyDouActivity activity;
    private MoneyListVO mVO;
    private IWXAPI api;
    private static final int SDK_PAY_FLAG = 1;


    public PayDialog(BaseActivity baseActivity) {
        super(baseActivity, R.style.SettingDialog);
        this.activity = (MyDouActivity) baseActivity;
    }

    public void setVO(MoneyListVO VO) {
        mVO = VO;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(true);
        initView();
        api = WXAPIFactory.createWXAPI(activity, AppData.WX_APP_ID);
    }

    private void initView() {
        wx_pay_layout = findViewById(R.id.wx_pay_layout);//微信支付
        ali_pay_layout = findViewById(R.id.ali_pay_layout);//支付宝支付
        wx_pay_layout.setOnClickListener(this);
        ali_pay_layout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wx_pay_layout: {
                if (!api.isWXAppInstalled()) {
                    activity.showToast("请安装微信");
                    return;
                }
                if (!api.isWXAppSupportAPI()) {
                    activity.showToast("微信版本不支持支付，安装微信新版本");
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("uid", TokenManager.getUid(activity));
                map.put("moneyId", mVO.getMoneyId());
                BaseObserver baseObserver = new BaseObserver<BaseResult<AppWxPayResult>>(activity, BaseObserver.MODEL_SHOW_DIALOG_TOAST) {
                    @Override
                    public void success(BaseResult<AppWxPayResult> data) {
                        super.success(data);
                        dismiss();
                        AppWxPayResult result = data.getData();
                        if (result != null) {
                            PayReq req = new PayReq();
                            req.appId = result.getAppid();//微信分配的公众账号ID
                            req.partnerId = result.getPartnerid();//微信支付分配的商户号

                            req.nonceStr = result.getNoncestr();

                            req.prepayId = result.getPrepayid();//微信返回的支付交易会话ID

                            req.timeStamp = String.valueOf(result.getTimestamp());////时间戳，请见接口规则-参数规定
                            req.packageValue = "Sign=WXPay";//暂填写固定值Sign=WXPay

                            req.sign = result.getSign();//签名
                            api.sendReq(req);
                        }
                    }

                    @Override
                    public void error(BaseResult<AppWxPayResult> value, Throwable e) {
                        super.error(value, e);
                    }
                };
                activity.mDisposable.add(baseObserver);
                UtilRetrofit.getInstance().create(HttpService.class).appWxPay(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
            }
            break;

            case R.id.ali_pay_layout: {

                Map<String, String> map = new HashMap<>();
                map.put("uid", TokenManager.getUid(activity));
                map.put("moneyId", mVO.getMoneyId());
                BaseObserver baseObserver = new BaseObserver<BaseResult<String>>(activity, BaseObserver.MODEL_SHOW_DIALOG_TOAST) {
                    @Override
                    public void success(BaseResult<String> data) {
                        super.success(data);
                        dismiss();
                        String result = data.getData();
                        aliPay(result);
                    }

                    @Override
                    public void error(BaseResult<String> value, Throwable e) {
                        super.error(value, e);
                    }
                };
                activity.mDisposable.add(baseObserver);
                UtilRetrofit.getInstance().create(HttpService.class).appAliPay(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
            }
            break;

            default:
                break;
        }


    }


    private void aliPay(final String str){
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(str, true);
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

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {

                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        activity.showToast("支付成功");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        activity.showToast("支付失败");
                    }
                    break;
                }

                default:
                    break;
            }
        }

    };

}
