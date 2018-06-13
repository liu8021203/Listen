package com.ting.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.common.http.HttpService;
import com.ting.search.adapter.SearchHotAdapter;
import com.ting.search.adapter.SearchResultAdapter;
import com.ting.bean.search.SearchHotResult;
import com.ting.bean.search.SearchResult;
import com.ting.util.UtilRetrofit;
import com.ting.util.UtilStr;

import java.util.Hashtable;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/10/15.
 * 搜索frame
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private EditText search_editext;
    private ListView search_lsitView;
    private GridView gridView;
    private RelativeLayout rlLayoutHot;
    private SearchHotAdapter adapter;
    private Map<String, String> map = new Hashtable<>();
    private SearchResultAdapter searchResultAdapter;
    private TextView tvSearch;


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
        ivBack = (ImageView) findViewById(R.id.search_left_image);//返回
        search_editext = (EditText) findViewById(R.id.search_editext);//搜索输入框
        search_lsitView = (ListView) findViewById(R.id.search_lsitView);//搜索列表
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
                    rlLayoutHot.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.VISIBLE);
                    search_lsitView.setVisibility(View.GONE);
                }
            }
        });
        gridView = (GridView) findViewById(R.id.gridview);
        rlLayoutHot = (RelativeLayout) findViewById(R.id.rl_layout_hot);
        tvSearch = (TextView) findViewById(R.id.tv_search);
        tvSearch.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        BaseObserver baseObserver = new BaseObserver<SearchHotResult>(this){
            @Override
            public void success(SearchHotResult data) {
                super.success(data);
                showResult(data);
            }

            @Override
            public void error() {
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getSearchTop().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
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
            map.put("keywords", hot_words);
            BaseObserver baseObserver = new BaseObserver<SearchResult>(){
                @Override
                public void success(SearchResult data) {
                    super.success(data);
                    showSeachResult(data);
                }

                @Override
                public void error() {
                }
            };
            mActivity.mDisposable.add(baseObserver);
            UtilRetrofit.getInstance().create(HttpService.class).getSearchResult(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
        }
    }

    public void showResult(SearchHotResult searchHotResult) {
        adapter = new SearchHotAdapter(this);
        adapter.setResult(searchHotResult.getData());
        gridView.setAdapter(adapter);
    }

    public void showSeachResult(SearchResult searchResult) {
        if (searchResult.getData() != null && searchResult.getData().size() > 0) {
            rlLayoutHot.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            search_lsitView.setVisibility(View.VISIBLE);
            if (searchResultAdapter == null) {
                searchResultAdapter = new SearchResultAdapter(this);
                searchResultAdapter.setResult(searchResult.getData());
                search_lsitView.setAdapter(searchResultAdapter);
            } else
            {
                searchResultAdapter.setResult(searchResult.getData());
                searchResultAdapter.notifyDataSetChanged();
            }
        } else
        {
            showToast("未搜索的结果");
        }
    }

}
