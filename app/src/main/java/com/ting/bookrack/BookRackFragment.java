package com.ting.bookrack;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.hms.api.Api;
import com.lechuan.midunovel.base.util.FoxBaseCommonUtils;
import com.lechuan.midunovel.base.util.FoxBaseGsonUtil;
import com.lechuan.midunovel.view.FoxCustomerTm;
import com.lechuan.midunovel.view.FoxNsTmListener;
import com.lechuan.midunovel.view.holder.FoxNativeAdHelper;
import com.lechuan.midunovel.view.holder.FoxTextLintAd;
import com.lechuan.midunovel.view.interfaces.FoxTextLinkHolder;
import com.lechuan.midunovel.view.video.bean.FoxResponseBean;
import com.ting.R;
import com.ting.base.BaseApplication;
import com.ting.base.BaseFragment;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.vo.BookListUpdateVO;
import com.ting.bean.vo.BookListenVO;
import com.ting.bean.vo.BookVO;
import com.ting.bookrack.adapter.BookRackAdapter;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.db.DBListenHistory;
import com.ting.db.DBListenHistoryDao;
import com.ting.login.LoginMainActivity;
import com.ting.myself.MessageJavaActivity;
import com.ting.play.BookDetailsActivity;
import com.ting.play.PlayActivity;
import com.ting.play.controller.MusicDBController;
import com.ting.record.DownloadActivity;
import com.ting.util.UtilGlide;
import com.ting.util.UtilGson;
import com.ting.util.UtilIntent;
import com.ting.util.UtilRetrofit;
import com.ting.util.UtilSPutil;
import com.ting.view.MusicAnimView;
import com.umeng.message.PushAgent;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by liu on 2017/11/21.
 */

public class BookRackFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private BookRackAdapter mAdapter;
    private ImageView ivHeadImg;
    private TextView tvHeadTitle;
    private TextView tvDesc;
    private TextView tvListen;
    private TextView tvAnchor;
    private RelativeLayout rlSign;
    private TextView tvSign;
    private MusicAnimView mMusicAnimView;
    private ImageView ivMenu;
    private ImageView ivRed;
    private ImageView ivMessage;
    private RelativeLayout rlMenu;
    private RelativeLayout rlActionbar;
    private TextView tvComplete;
    private BookRackPopupWindow window;
    private QBadgeView badgeView;
    private FoxTextLinkHolder nativeInfoHolder;
    private RelativeLayout rlAd;
    private ImageView ivAd;
    private TextView tvAdDesc;
    private FoxCustomerTm mOxCustomerTm;
    private FoxResponseBean.DataBean mDataBean;

    @Override
    protected void initView() {
        ivHeadImg = flContent.findViewById(R.id.iv_img);
        tvHeadTitle = flContent.findViewById(R.id.tv_title);
        tvDesc = flContent.findViewById(R.id.tv_desc);
        tvAnchor = flContent.findViewById(R.id.tv_anchor);
        tvListen = flContent.findViewById(R.id.tv_listen);
        mRecyclerView = flContent.findViewById(R.id.recycle_view);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 3);
        mRecyclerView.setLayoutManager(manager);
        rlSign = flContent.findViewById(R.id.rl_sign);
        tvSign = flContent.findViewById(R.id.tv_sign);
        tvSign.setOnClickListener(this);
        mMusicAnimView = flContent.findViewById(R.id.music_view);
        mMusicAnimView.setOnClickListener(this);
        ivMenu = flContent.findViewById(R.id.iv_menu);
        ivMenu.setOnClickListener(this);
        ivRed = flContent.findViewById(R.id.iv_red);
        rlMenu = flContent.findViewById(R.id.rl_menu);
        rlMenu.setOnClickListener(this);
        rlActionbar = flContent.findViewById(R.id.rl_actionbar);
        tvComplete = flContent.findViewById(R.id.tv_complete);
        tvComplete.setOnClickListener(this);
        if (TokenManager.isLogin(mActivity)) {
            isSign();
        }
        ivMessage = flContent.findViewById(R.id.iv_message);
        ivMessage.setOnClickListener(this);

        rlAd = flContent.findViewById(R.id.rl_ad_layout);
        rlAd.setOnClickListener(this);
        ivAd = flContent.findViewById(R.id.iv_ad_img);
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
        mOxCustomerTm.loadAd(360702, TokenManager.getUid(mActivity));
    }


    @Override
    protected void initData() {
        getListenData();
        recommend();
        getNoticeNum();
    }

    private void getListenData() {
        DBListenHistoryDao dao = BaseApplication.getInstance().getDaoSession().getDBListenHistoryDao();
        final List<DBListenHistory> historyList = dao.queryRaw("group by BOOK_ID order by SYSTEM_TIME desc");


        List<BookListenVO> list = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < historyList.size(); i++) {
            if (i == 21) {
                break;
            }
            BookListenVO vo = new BookListenVO();
            vo.setBookId(historyList.get(i).getBookId());
            vo.setSystemTime(historyList.get(i).getSystemTime() / 1000);
            list.add(vo);
            tags.add("bookId" + historyList.get(i).getBookId());
        }

        if (!tags.isEmpty()) {
            String[] strings = new String[tags.size()];
            tags.toArray(strings);
            PushAgent.getInstance(mActivity).getTagManager().addTags(new TagManager.TCallBack() {
                @Override
                public void onMessage(boolean b, ITagManager.Result result) {
                    Log.d("aaa", "添加推送标签---" + b);
                }
            }, strings);
        }


        Map<String, String> map = new HashMap<>();
        map.put("list", UtilGson.toJson(list));
        BaseObserver observer = new BaseObserver<BaseResult<List<BookListUpdateVO>>>(this, BaseObserver.MODEL_NO) {
            @Override
            public void success(BaseResult<List<BookListUpdateVO>> data) {
                List<BookListUpdateVO> listUpdateVOS = data.getData();
                for (int i = 0; i < historyList.size(); i++) {
                    if (i == 21) {
                        break;
                    }
                    for (int j = 0; j < listUpdateVOS.size(); j++) {
                        if (TextUtils.equals(historyList.get(i).getBookId(), listUpdateVOS.get(j).getBookId())) {
                            historyList.get(i).setUpdate(listUpdateVOS.get(j).isUpdate());
                            break;
                        }
                    }
                }

                if (historyList != null && !historyList.isEmpty()) {
                    if (mAdapter == null) {
                        mAdapter = new BookRackAdapter(mActivity);
                        mAdapter.setData(historyList);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        mAdapter.setData(historyList);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }


            @Override
            public void error(BaseResult<List<BookListUpdateVO>> value, Throwable e) {
                super.error(value, e);
                if (historyList != null && !historyList.isEmpty()) {
                    if (mAdapter == null) {
                        mAdapter = new BookRackAdapter(mActivity);
                        mAdapter.setData(historyList);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        mAdapter.setData(historyList);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }


        };
        mDisposable.add(observer);
        UtilRetrofit.getInstance().create(HttpService.class).isBookUpdateStatus(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookrack;
    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    protected void getIntentData() {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_sign:
                if (TokenManager.isLogin(mActivity)) {
                    sign();
                } else {
                    mActivity.intent(LoginMainActivity.class);
                }
                break;
            case R.id.music_view: {
                String bookId = null;
                MusicDBController controller = new MusicDBController();
                List<DBListenHistory> historys = controller.getListenHistory();
                if (historys != null && !historys.isEmpty()) {
                    DBListenHistory history = historys.get(0);
                    bookId = history.getBookId();
                }
                if (bookId != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("bookId", bookId);
                    bundle.putBoolean("play", !AppData.isPlaying);
                    UtilIntent.intentDIYBottomToTop(mActivity, PlayActivity.class, bundle);
                } else {
                    mActivity.showToast("快去书城收听喜欢的书籍吧！！！");
                }
            }
            break;

            case R.id.tv_listen: {
                String bookId = (String) v.getTag();
                Bundle bundle = new Bundle();
                bundle.putString("bookId", bookId);
                mActivity.intent(BookDetailsActivity.class, bundle);
            }
            break;

            case R.id.rl_ad_layout:
                if (mOxCustomerTm != null && mDataBean != null && !FoxBaseCommonUtils.isEmpty(mDataBean.getActivityUrl())) {
                    //素材点击时候调用素材点击曝光方法
                    mOxCustomerTm.adClicked();
                    mOxCustomerTm.openFoxActivity(mDataBean.getActivityUrl());
                }
                break;


            case R.id.iv_message: {
                if (TokenManager.isLogin(mActivity)) {
                    mActivity.intent(MessageJavaActivity.class);
                    if (badgeView != null) {
                        badgeView.hide(false);
                    }
                } else {
                    mActivity.intent(LoginMainActivity.class);
                }
            }
            break;

            case R.id.tv_complete:
                rlMenu.setVisibility(View.VISIBLE);
                mMusicAnimView.setVisibility(View.VISIBLE);
                tvComplete.setVisibility(View.GONE);
                if (mAdapter != null) {
                    mAdapter.setState(0);
                    mAdapter.notifyDataSetChanged();
                }
                break;

            case R.id.rl_menu:
                if (window == null) {
                    window = new BookRackPopupWindow(mActivity);
                    window.setListener(new BookRackPopupWindow.CallBackListener() {
                        @Override
                        public void editor() {
                            window.dismiss();
                            rlMenu.setVisibility(View.GONE);
                            mMusicAnimView.setVisibility(View.GONE);
                            tvComplete.setVisibility(View.VISIBLE);
                            if (mAdapter != null) {
                                mAdapter.setState(1);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void message() {
                            window.dismiss();

                        }

                        @Override
                        public void download() {
                            window.dismiss();
                            mActivity.intent(DownloadActivity.class);
                        }
                    });
                }
                window.isShowRed(View.GONE);
                window.show(rlActionbar);
                break;
        }
    }


    private void sign() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(mActivity));
        BaseObserver baseObserver = new BaseObserver<BaseResult>(mActivity, BaseObserver.MODEL_SHOW_DIALOG_TOAST) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                showToast(data.getMessage());
                ObjectAnimator animator = ObjectAnimator.ofFloat(rlSign, "translationY", 0, -rlSign.getHeight() * 1.5f);
                ObjectAnimator alpha = ObjectAnimator.ofFloat(rlSign, "alpha", 1f, 0f);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(animator).with(alpha);
                animSet.setDuration(300);
                animSet.start();
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(mRecyclerView, "translationY", 0, -rlSign.getHeight() * 1.5f);
                animator.setDuration(500);
                animator1.start();
            }

        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).sign(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    @Override
    protected int setActionBar() {
        return 0;
    }

    @Override
    protected void reload() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("aaa", hidden + "------hidden");
        if (!hidden) {
            getListenData();
            if (AppData.isPlaying) {
                mMusicAnimView.start();
            } else {
                mMusicAnimView.stop();
            }
        } else {
            mMusicAnimView.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("aaa", "------书架onResume");
        getListenData();
        if (AppData.isPlaying) {
            mMusicAnimView.start();
        } else {
            mMusicAnimView.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("aaa", "------书架onPause");
    }

    public void startAnim() {
        DBListenHistoryDao dao = BaseApplication.getInstance().getDaoSession().getDBListenHistoryDao();
        List<DBListenHistory> data = dao.queryBuilder().orderDesc(DBListenHistoryDao.Properties.Id).list();
        if (data != null && !data.isEmpty()) {
            DBListenHistory history = data.get(0);
//            if (AppData.currPlayBookId == history.getBookid()) {
//                tvListen.setText("正在收听");
//            } else {
//                tvListen.setText("继续收听");
//            }
        }
        mMusicAnimView.start();
    }

    public void stopAnim() {
        DBListenHistoryDao dao = BaseApplication.getInstance().getDaoSession().getDBListenHistoryDao();
        List<DBListenHistory> data = dao.queryBuilder().orderDesc(DBListenHistoryDao.Properties.Id).list();
        if (data != null && !data.isEmpty()) {
            DBListenHistory history = data.get(0);
//            if (AppData.currPlayBookId == history.getBookid()) {
//                tvListen.setText("正在收听");
//            } else {
//                tvListen.setText("继续收听");
//            }
        }
        mMusicAnimView.stop();
    }


    private void isSign() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(mActivity));
        BaseObserver baseObserver = new BaseObserver<BaseResult<Integer>>(mActivity, BaseObserver.MODEL_NO) {
            @Override
            public void success(BaseResult<Integer> data) {
                super.success(data);
                Integer aBoolean = data.getData();
                if (aBoolean == 1) {
                    rlSign.setVisibility(View.GONE);
                } else {
                    rlSign.setVisibility(View.VISIBLE);
                }
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).isSign(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    private void recommend() {
        BaseObserver baseObserver = new BaseObserver<BaseResult<BookVO>>(mActivity, BaseObserver.MODEL_NO) {
            @Override
            public void success(BaseResult<BookVO> data) {
                super.success(data);
                BookVO vo = data.getData();
                if (vo != null) {
                    UtilGlide.loadImg(mActivity, vo.getBookImage(), ivHeadImg);
                    tvHeadTitle.setText(vo.getBookTitle());
                    tvDesc.setText(vo.getBookDesc());
                    tvAnchor.setText(vo.getBookAnchor());
                    tvListen.setTag(vo.getId());
                    tvListen.setText("推荐收听");
                    tvListen.setOnClickListener(BookRackFragment.this);
                }
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).bookrackRecommend().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    private void getNoticeNum() {
        Map<String, String> map = new HashMap<>();
        long systemTime = UtilSPutil.getInstance(mActivity).getLong("systemTime", -1L);
        map.put("systemTime", String.valueOf(systemTime));
        if (TokenManager.isLogin(mActivity)) {
            map.put("uid", TokenManager.getUid(mActivity));
        }
        BaseObserver baseObserver = new BaseObserver<BaseResult<Integer>>(this, BaseObserver.MODEL_ONLY_SHOW_DIALOG) {
            @Override
            public void success(BaseResult<Integer> data) {
                super.success(data);
                int count = data.getData();
                if (count > 0) {
                    badgeView = new QBadgeView(mActivity);
                    badgeView.bindTarget(ivMessage);
                    badgeView.setBadgeGravity(Gravity.END | Gravity.TOP);
                    badgeView.setBadgeNumber(count);
                }
            }


            @Override
            public void error(BaseResult<Integer> value, Throwable e) {
                super.error(value, e);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getNoticeNum(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    @Override
    public void onDestroy() {
        if (mOxCustomerTm != null) {
            mOxCustomerTm.destroy();
        }
        super.onDestroy();
    }
}
