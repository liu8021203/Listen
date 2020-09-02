package com.ting.myself;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lechuan.midunovel.base.util.FoxBaseCommonUtils;
import com.lechuan.midunovel.base.util.FoxBaseGsonUtil;
import com.lechuan.midunovel.view.FoxCustomerTm;
import com.lechuan.midunovel.view.FoxNsTmListener;
import com.lechuan.midunovel.view.video.bean.FoxResponseBean;
import com.ting.R;
import com.ting.base.BaseFragment;
import com.ting.base.BaseObserver;
import com.ting.base.MessageEventBus;
import com.ting.bean.BaseResult;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.login.LoginMainActivity;
import com.ting.bean.UserInfoResult;
import com.ting.util.UtilGlide;
import com.ting.util.UtilRetrofit;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by gengjiajia on 15/8/31.
 * 我的主frame
 */
public class MineMainFrame extends BaseFragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private RelativeLayout rlMoney;
    private RelativeLayout rlScard;
    private RelativeLayout rlCollect;
    private RelativeLayout rlFocus;
    private RelativeLayout rlFeed;
    private RelativeLayout rlSetting;
    private RelativeLayout rlAbout;
    private RelativeLayout rlBuy;
    private RelativeLayout rlMoneyDetails;
    private CircleImageView person_touxiang;
    private TextView tvLogin;
    private TextView tvLoginHint;
    private TextView tvName;
    private TextView tvMoney;
    private RelativeLayout rlAd;
    private TextView tvAdTitle;
    private TextView tvAdDesc;
    private ImageView ivAd;
    //    private RelativeLayout rlJifen;
    private FoxCustomerTm mOxCustomerTm;
    private FoxResponseBean.DataBean mDataBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mOxCustomerTm != null) {
            mOxCustomerTm.destroy();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEventBus event) {
        if (event.getType() == MessageEventBus.LOGIN) {
            loginState(true);
            setUserInfo(TokenManager.getUserInfo());
        } else if (event.getType() == 2) {
            loginState(false);
            person_touxiang.setImageResource(R.mipmap.mine_head_img);
        } else if (event.getType() == MessageEventBus.BUY_VIP) {
//            if(TokenManager.getInfo(mActivity).getIsvip() == 0){
//                tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.mipmap.no_vip, 0);
//            }else{
//                tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.mipmap.vip, 0);
//            }
        } else if (event.getType() == MessageEventBus.MODIFY) {
            setUserInfo(TokenManager.getUserInfo());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        if (TokenManager.isLogin(mActivity)) {
            loginState(true);
        } else {
            loginState(false);
        }
    }

    /**
     * 登录状态
     *
     * @param b true是登录  false：未登录
     */
    private void loginState(boolean b) {
        if (b) {
            tvLogin.setVisibility(View.GONE);
            tvLoginHint.setVisibility(View.GONE);
            tvName.setVisibility(View.VISIBLE);
            tvMoney.setVisibility(View.VISIBLE);
        } else {
            tvLogin.setVisibility(View.VISIBLE);
            tvLoginHint.setVisibility(View.VISIBLE);
            tvName.setVisibility(View.GONE);
            tvMoney.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected void initView() {
        person_touxiang = flContent.findViewById(R.id.person_touxiang);//头像
        rlMoney = flContent.findViewById(R.id.rl_money);
        rlScard = flContent.findViewById(R.id.rl_scard);
        rlCollect = flContent.findViewById(R.id.rl_collect);
        rlFocus = flContent.findViewById(R.id.rl_focus);
        rlFeed = flContent.findViewById(R.id.rl_feed);
        rlSetting = flContent.findViewById(R.id.rl_setting);
        rlAbout = flContent.findViewById(R.id.rl_about);
        tvLogin = flContent.findViewById(R.id.tv_login);
        tvLoginHint = flContent.findViewById(R.id.tv_login_hint);
        tvName = flContent.findViewById(R.id.tv_name);
        tvMoney = flContent.findViewById(R.id.tv_money);
        rlMoney.setOnClickListener(this);
        rlScard.setOnClickListener(this);
        rlCollect.setOnClickListener(this);
        rlFocus.setOnClickListener(this);
        rlFeed.setOnClickListener(this);
        rlSetting.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        rlBuy = flContent.findViewById(R.id.rl_buy);
        rlBuy.setOnClickListener(this);
        rlMoneyDetails = flContent.findViewById(R.id.rl_money_details);
        rlMoneyDetails.setOnClickListener(this);
//        rlJifen = flContent.findViewById(R.id.rl_jifen);
//        rlJifen.setOnClickListener(this);

        rlAd = flContent.findViewById(R.id.rl_ad);
        rlAd.setOnClickListener(this);
        ivAd = flContent.findViewById(R.id.iv_ad);
        tvAdTitle = flContent.findViewById(R.id.tv_ad_title);
        tvAdDesc = flContent.findViewById(R.id.tv_ad_desc);

        mOxCustomerTm = new FoxCustomerTm(mActivity);
        mOxCustomerTm.setAdListener(new FoxNsTmListener() {
            @Override
            public void onReceiveAd(String result) {
                Log.d("========", "onReceiveAd:" + result);
                if (!FoxBaseCommonUtils.isEmpty(result)) {
                    FoxResponseBean.DataBean dataBean = FoxBaseGsonUtil.GsonToBean(result, FoxResponseBean.DataBean.class);
                    if (dataBean != null) {
                        mDataBean = dataBean;
                        rlAd.setVisibility(View.VISIBLE);
                        UtilGlide.loadImg(mActivity, dataBean.getImageUrl(), ivAd);
                        tvAdTitle.setText(dataBean.getExtTitle());
                        tvAdDesc.setText(dataBean.getExtDesc());
                    }
                    //素材加载成功时候调用素材加载曝光方法
                    mOxCustomerTm.adExposed();
                }
            }

            @Override
            public void onFailedToReceiveAd() {
                Log.d("========", "onFailedToReceiveAd");
            }

            @Override
            public void onAdActivityClose(String s) {
                Log.d("========", "onAdActivityClose" + s);
                if (!FoxBaseCommonUtils.isEmpty(s)) {
//                    ToastUtils.showShort(s);
                }
            }

        });
        mOxCustomerTm.loadAd(360453, TokenManager.getUid(mActivity));
    }

    @Override
    protected void initData() {
        if (TokenManager.isLogin(mActivity)) {
            Map<String, String> map = new HashMap<>();
            map.put("uid", TokenManager.getUid(mActivity));
            map.put("token", TokenManager.getToken(mActivity));
            BaseObserver baseObserver = new BaseObserver<BaseResult<UserInfoResult>>(this, BaseObserver.MODEL_ONLY_SHOW_DIALOG) {
                @Override
                public void success(BaseResult<UserInfoResult> data) {
                    super.success(data);
                    UserInfoResult result = data.getData();
                    if (result != null) {
                        setUserInfo(result);
                    }
                }


                @Override
                public void error(BaseResult<UserInfoResult> value, Throwable e) {
                    super.error(value, e);
                    mActivity.intent(LoginMainActivity.class);
                }
            };
            mDisposable.add(baseObserver);
            UtilRetrofit.getInstance().create(HttpService.class).getUserInfo(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
        }
    }

    private void setUserInfo(UserInfoResult result) {
        UtilGlide.loadHeadImg(mActivity, result.getImage(), person_touxiang);
        tvName.setText(result.getNickname());
        tvMoney.setText("听豆余额：" + result.getMoney());
        TokenManager.setInfo(result);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frame_mine;
    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected int setActionBar() {
        return 0;
    }

    @Override
    protected void reload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_about:
                intent(AboutMeActivity.class);
                break;
            case R.id.rl_feed:
                intent(SuggestBackActivity.class);
                break;
            case R.id.rl_scard:
                if (TokenManager.isLogin(mActivity)) {
                    intent(MyCardActivity.class);
                } else {
                    intent(LoginMainActivity.class);
                }
                break;
            case R.id.rl_focus:
                if (TokenManager.isLogin(mActivity)) {
                    intent(MySeeActivity.class);
                } else {
                    intent(LoginMainActivity.class);
                }
                break;
            case R.id.rl_money:
                if (TokenManager.isLogin(mActivity)) {
                    intent(MyDouActivity.class);
                } else {
                    intent(LoginMainActivity.class);
                }
                break;
            case R.id.rl_setting:
                if (TokenManager.isLogin(mActivity)) {
                    intent(SettingActivity.class);
                } else {
                    intent(LoginMainActivity.class);
                }
                break;
            case R.id.rl_collect:
                if (TokenManager.isLogin(mActivity)) {
                    mActivity.intent(CollectActivity.class);
                } else {
                    intent(LoginMainActivity.class);
                }
                break;
            case R.id.tv_login:
                mActivity.intent(LoginMainActivity.class);
                break;

            case R.id.rl_buy:
                if (TokenManager.isLogin(mActivity)) {
                    mActivity.intent(BuyBookActivity.class);
                } else {
                    intent(LoginMainActivity.class);
                }
                break;

            case R.id.rl_ad:
                if (mOxCustomerTm != null && mDataBean != null && !FoxBaseCommonUtils.isEmpty(mDataBean.getActivityUrl())) {
                    //素材点击时候调用素材点击曝光方法
                    mOxCustomerTm.adClicked();
                    mOxCustomerTm.openFoxActivity(mDataBean.getActivityUrl());
                }
                break;

            case R.id.rl_money_details:
                if (TokenManager.isLogin(mActivity)) {
                    intent(DouDetailsActivity.class);
                } else {
                    intent(LoginMainActivity.class);
                }
                break;

//            case R.id.rl_jifen:
//                String[] perms = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                if(EasyPermissions.hasPermissions(mActivity, perms)){
//                    OffersManager.getInstance(mActivity).showOffersWall();
//                }else{
//                    EasyPermissions.requestPermissions(this, "获取权限",
//                            999, perms);
//                }


//                break;
            default:
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


}
