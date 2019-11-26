package com.ting.bookrack;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseApplication;
import com.ting.base.BaseFragment;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
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
import com.ting.util.UtilIntent;
import com.ting.util.UtilRetrofit;
import com.ting.view.MusicAnimView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    private RelativeLayout rlMenu;
    private RelativeLayout rlActionbar;
    private TextView tvComplete;
    private BookRackPopupWindow window;

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
    }


    @Override
    protected void initData() {
        getListenData();
        recommend();
    }

    private void getListenData() {
        DBListenHistoryDao dao = BaseApplication.getInstance().getDaoSession().getDBListenHistoryDao();
        List<DBListenHistory> data = dao.queryRaw("group by BOOK_ID");
        if (mAdapter == null) {
            mAdapter = new BookRackAdapter(mActivity);
            mAdapter.setData(data);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(data);
            mAdapter.notifyDataSetChanged();
        }
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
                            if (TokenManager.isLogin(mActivity)) {
                                mActivity.intent(MessageJavaActivity.class);
                                ivRed.setVisibility(View.GONE);
                            } else {
                                mActivity.intent(LoginMainActivity.class);
                            }
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
}
