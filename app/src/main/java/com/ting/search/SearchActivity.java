package com.ting.search;

import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.AppSearchResult;
import com.ting.bean.BaseResult;
import com.ting.bean.vo.HotSearchVO;
import com.ting.category.adapter.CategoryListAdapter;
import com.ting.common.http.HttpService;
import com.ting.search.adapter.SearchHostAdapter;
import com.ting.util.UtilPixelTransfrom;
import com.ting.util.UtilRetrofit;
import com.ting.util.UtilStr;
import com.ting.view.CustomItemDecoration;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/10/15.
 * 搜索frame
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private NestedScrollView mScrollView;
    private EditText search_editext;
    private RecyclerView mHostRecyclerView;
    private RecyclerView mBookRecyclerView;
    private TagFlowLayout flowLayout;
    private RelativeLayout rlLayoutHot;
    private LinearLayout llContent;
    private LinearLayout llEmpty;
    private TextView tvEmpty;
    private Map<String, String> map = new Hashtable<>();
    private TextView tvSearch;
    private TextView tvHostState;
    private TextView tvBookState;
    private View hostLine;
    private View bookLine;
    private SearchHostAdapter mHostAdapter;
    private CategoryListAdapter mCategoryListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_search);
    }

    @Override
    protected String setTitle() {
        return null;
    }


    @Override
    protected void initView() {
        ivBack =  findViewById(R.id.search_left_image);//返回
        search_editext =  findViewById(R.id.search_editext);//搜索输入框
        mHostRecyclerView =  findViewById(R.id.recycler_view_host);//搜索列表
        mHostRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager manager1 = new LinearLayoutManager(mActivity);
        mHostRecyclerView.setLayoutManager(manager1);
        CustomItemDecoration decoration1 = new CustomItemDecoration(1);
        mHostRecyclerView.addItemDecoration(decoration1);
        mBookRecyclerView = findViewById(R.id.recycler_view_book);
        mBookRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mBookRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1);
        mBookRecyclerView.addItemDecoration(decoration);
        ivBack.setOnClickListener(this);
        search_editext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                getSearchDate();
                return false;
            }
        });

        search_editext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                if (UtilStr.isEmpty(temp)) {
                    llEmpty.setVisibility(View.GONE);
                    llContent.setVisibility(View.VISIBLE);
                    rlLayoutHot.setVisibility(View.VISIBLE);
                    flowLayout.setVisibility(View.VISIBLE);
                    mScrollView.setVisibility(View.GONE);
                }
            }
        });
        flowLayout =  findViewById(R.id.flow_layout);
        rlLayoutHot =  findViewById(R.id.rl_layout_hot);
        tvSearch =  findViewById(R.id.tv_search);
        tvSearch.setOnClickListener(this);
        mScrollView = findViewById(R.id.scrollView);
        tvEmpty = findViewById(R.id.tv_desc);
        llEmpty = findViewById(R.id.empty_layout);
        llContent = findViewById(R.id.ll_content);
        tvHostState = findViewById(R.id.tv_host_state);
        tvBookState = findViewById(R.id.tv_book_state);
        hostLine = findViewById(R.id.host_line);
        bookLine = findViewById(R.id.book_line);
    }

    @Override
    protected void initData() {
        BaseObserver baseObserver = new BaseObserver<BaseResult<List<HotSearchVO>>>(this, BaseObserver.MODEL_SHOW_DIALOG_TOAST){
            @Override
            public void success(final BaseResult<List<HotSearchVO>> data) {
                super.success(data);
                flowLayout.setAdapter(new TagAdapter<HotSearchVO>(data.getData()) {
                    @Override
                    public View getView(FlowLayout parent, int position, HotSearchVO o) {
                        TextView textView = new TextView(mActivity);
                        textView.setText(o.getName());
                        textView.setBackgroundResource(R.drawable.search_gridview_bg);
                        textView.setPadding(30,5,30,5);
                        return textView;
                    }
                });
                flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        search_editext.setText(data.getData().get(position).getName());
                        getSearchDate();
                        return true;
                    }
                });
            }

            @Override
            public void error() {
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).hotSearchList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_left_image:
                onBackPressed();
                break;

            case R.id.tv_search:
                getSearchDate();
                break;
            default:
                break;

        }
    }

    /***
     * 搜索输入框数据
     **/
    private void getSearchDate() {
        String hot_words = search_editext.getText().toString().trim();
        if (hot_words.equals("")) {
            showToast("请输入查询字段");
        } else {
            map.put("search", hot_words);
            BaseObserver baseObserver = new BaseObserver<BaseResult<AppSearchResult>>(mActivity, BaseObserver.MODEL_SHOW_DIALOG_TOAST){
                @Override
                public void success(BaseResult<AppSearchResult> data) {
                    super.success(data);
                    AppSearchResult result = data.getData();
                    if(result.getBookData() == null && result.getHostData() == null){
                        llEmpty.setVisibility(View.VISIBLE);
                        llContent.setVisibility(View.GONE);
                        tvEmpty.setText("没有相关信息~~");
                        return;
                    }
                    rlLayoutHot.setVisibility(View.GONE);
                    flowLayout.setVisibility(View.GONE);
                    mScrollView.setVisibility(View.VISIBLE);
                    if(result.getHostData() != null && !result.getHostData().isEmpty()){
                        mHostRecyclerView.setVisibility(View.VISIBLE);
                        tvHostState.setVisibility(View.VISIBLE);
                        hostLine.setVisibility(View.VISIBLE);
                        if(mHostAdapter == null){
                            mHostAdapter = new SearchHostAdapter(mActivity);
                            mHostAdapter.setData(result.getHostData());
                            mHostRecyclerView.setAdapter(mHostAdapter);
                        }else{
                            mHostAdapter.setData(result.getHostData());
                            mHostAdapter.notifyDataSetChanged();
                        }
                    }else{
                        mHostRecyclerView.setVisibility(View.GONE);
                        tvHostState.setVisibility(View.GONE);
                        hostLine.setVisibility(View.GONE);
                    }

                    if(result.getBookData() != null && !result.getBookData().isEmpty()){
                        mBookRecyclerView.setVisibility(View.VISIBLE);
                        tvBookState.setVisibility(View.VISIBLE);
                        bookLine.setVisibility(View.VISIBLE);
                        if(mCategoryListAdapter == null){
                            mCategoryListAdapter = new CategoryListAdapter(mActivity);
                            mCategoryListAdapter.setData(result.getBookData());
                            mBookRecyclerView.setAdapter(mCategoryListAdapter);
                        }else{
                            mCategoryListAdapter.setData(result.getBookData());
                            mCategoryListAdapter.notifyDataSetChanged();
                        }
                    }else{
                        mBookRecyclerView.setVisibility(View.GONE);
                        tvBookState.setVisibility(View.GONE);
                        bookLine.setVisibility(View.GONE);
                    }
                    if(tvBookState.getVisibility() == View.VISIBLE && tvHostState.getVisibility() == View.VISIBLE){
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UtilPixelTransfrom.dip2px(mActivity, 40));
                        params.setMargins(0, UtilPixelTransfrom.dip2px(mActivity, 20), 0,0);
                        tvBookState.setLayoutParams(params);
                    }
                }

            };
            mActivity.mDisposable.add(baseObserver);
            UtilRetrofit.getInstance().create(HttpService.class).appSearch(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
        }
    }


}
