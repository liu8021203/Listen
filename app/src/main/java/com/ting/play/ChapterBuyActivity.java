package com.ting.play;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.base.MessageEventBus;
import com.ting.base.listener.CallBackListener;
import com.ting.bean.BaseResult;
import com.ting.bean.ChapterResult;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.play.adapter.ChapterBuyAdapter;
import com.ting.play.dialog.ChapterSelectDialog;
import com.ting.util.UtilGson;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChapterBuyActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private TextView tvChapterSelect;
    private TextView tvChapterSelectTotal;
    private TextView tvPriceTotal;
    private TextView tvChapterTotal;
    private Button btnBuyBook;
    private Button btnChapter;
    private ChapterBuyAdapter adapter;
    private String bookId;
    private int page = 1;
    private int count;
    private int bookPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_buy);
    }

    @Override
    protected String setTitle() {
        return "批量购买";
    }

    @Override
    protected void initView() {
        showRightText("全部选择");
        mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1);
        mRecyclerView.addItemDecoration(decoration);
        tvChapterSelect = findViewById(R.id.tv_chapter_select);
        tvChapterSelect.setOnClickListener(this);
        tvChapterSelectTotal = findViewById(R.id.tv_chapter_select_total);
        tvPriceTotal = findViewById(R.id.tv_price_total);
        btnBuyBook = findViewById(R.id.btn_buy_book);
        btnBuyBook.setOnClickListener(this);
        btnChapter = findViewById(R.id.btn_chapter);
        btnChapter.setOnClickListener(this);
        tvChapterTotal = findViewById(R.id.tv_chapter_total);
    }

    @Override
    protected void initData() {
        tvChapterSelectTotal.setText("已选择0集");
        tvPriceTotal.setText("0听豆");
        Map<String, String> map = new HashMap<>();
        map.put("bookId", bookId);
        map.put("page", String.valueOf(page));
        map.put("size", "50");
        if (TokenManager.isLogin(mActivity)) {
            map.put("uid", TokenManager.getUid(mActivity));
        }
        BaseObserver observer = new BaseObserver<BaseResult<ChapterResult>>(mActivity, BaseObserver.MODEL_ALL) {
            @Override
            public void success(BaseResult<ChapterResult> data) {
                super.success(data);
                if(data.getData() == null || data.getData().getList() == null || data.getData().getList().isEmpty()){
                    return;
                }
                count = data.getData().getCount();
                tvChapterTotal.setText("集数：" + count);
                bookPrice = data.getData().getList().get(0).getBookPrice();
                if (adapter == null) {
                    adapter = new ChapterBuyAdapter(mActivity);
                    adapter.setListener(new CallBackListener() {
                        @Override
                        public void callback() {
                            if (adapter.getSelectData() != null && !adapter.getSelectData().isEmpty()) {
                                tvChapterSelectTotal.setText("已选择" + adapter.getSelectData().size() + "集");
                                int totalPrice = 0;
                                for (int i = 0; i < adapter.getSelectData().size(); i++) {
                                    totalPrice += Integer.valueOf(adapter.getSelectData().get(i).getPrice());
                                }
                                tvPriceTotal.setText(totalPrice + "听豆");
                            } else {
                                tvChapterSelectTotal.setText("已选择0集");
                                tvPriceTotal.setText("0听豆");
                            }
                            int total = 0;
                            for (int i = 0; i < adapter.getData().size(); i++) {
                                if (TextUtils.isEmpty(adapter.getData().get(i).getUrl())) {
                                    total++;
                                }
                            }
                            if (total == adapter.getSelectData().size()) {
                                showRightText("全部取消");
                            } else {
                                showRightText("全部选择");
                            }
                        }
                    });
                    adapter.setData(data.getData().getList());
                    mRecyclerView.setAdapter(adapter);
                } else {
                    adapter.setData(data.getData().getList());
                    adapter.notifyDataSetChanged();
                }
            }

        };
        mActivity.mDisposable.add(observer);
        UtilRetrofit.getInstance().create(HttpService.class).chapter(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    @Override
    protected void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        bookId = bundle.getString("bookId");
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_chapter_select:
                ChapterSelectDialog dialog = new ChapterSelectDialog(mActivity);
                dialog.setData(count);
                dialog.setListener(new ChapterSelectDialog.ChapterSelectCallBackListener() {
                    @Override
                    public void callback(int page) {
                        ChapterBuyActivity.this.page = page;
                        initData();
                    }
                });
                dialog.show();
                break;

            case R.id.tv_right:
                if (adapter == null) {
                    return;
                }
                String str = mTvRight.getText().toString();
                if (str.equals("全部选择")) {
                    showRightText("全部取消");
                    adapter.allSelect();
                    adapter.notifyDataSetChanged();
                } else {
                    showRightText("全部选择");
                    adapter.allUnSelect();
                    adapter.notifyDataSetChanged();
                }
                if (adapter.getSelectData() != null && !adapter.getSelectData().isEmpty()) {
                    tvChapterSelectTotal.setText("已选择" + adapter.getSelectData().size() + "集");
                    int totalPrice = 0;
                    for (int i = 0; i < adapter.getSelectData().size(); i++) {
                        totalPrice += Integer.valueOf(adapter.getSelectData().get(i).getPrice());
                    }
                    tvPriceTotal.setText(totalPrice + "听豆");
                } else {
                    tvChapterSelectTotal.setText("已选择0集");
                    tvPriceTotal.setText("0听豆");
                }
                break;


            case R.id.btn_buy_book:
                new AlertDialog.Builder(mActivity).setTitle("提醒").setMessage("购买此书需要花费" + bookPrice + "听豆").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buyBook();
                    }
                }).show();
                break;

            case R.id.btn_chapter:
                if(adapter == null || adapter.getSelectData().isEmpty()){
                    showToast("请选择章节");
                    return;
                }
                buyChapter();
                break;
        }
    }

    private void buyChapter(){
        Map<String, String> map = new HashMap<>();
        map.put("bookId", bookId);
        if (TokenManager.isLogin(mActivity)) {
            map.put("uid", TokenManager.getUid(mActivity));
        }
        map.put("chapterList", UtilGson.toJson(adapter.getSelectData()));
        BaseObserver observer = new BaseObserver<BaseResult>(mActivity, BaseObserver.MODEL_SHOW_DIALOG_TOAST) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                initData();
                EventBus.getDefault().post(new MessageEventBus(MessageEventBus.BATCH_BUY_CHAPTER));
                showToast("购买成功");
            }
        };
        mActivity.mDisposable.add(observer);
        UtilRetrofit.getInstance().create(HttpService.class).batchBuyChapter(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }


    private void buyBook() {
        Map<String, String> map = new HashMap<>();
        map.put("bookId", bookId);
        if (TokenManager.isLogin(mActivity)) {
            map.put("uid", TokenManager.getUid(mActivity));
        }
        BaseObserver observer = new BaseObserver<BaseResult>(mActivity, BaseObserver.MODEL_SHOW_DIALOG_TOAST) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                initData();
                showToast("购买成功");
                EventBus.getDefault().post(new MessageEventBus(MessageEventBus.BUY_BOOK));
            }
        };
        mActivity.mDisposable.add(observer);
        UtilRetrofit.getInstance().create(HttpService.class).buyBook(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }
}
