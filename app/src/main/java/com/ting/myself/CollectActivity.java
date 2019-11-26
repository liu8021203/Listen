package com.ting.myself;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.vo.BookVO;
import com.ting.category.adapter.CategoryListAdapter;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 2017/8/7.
 */

public class CollectActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private CategoryListAdapter mAdapter;
    private Map<String, String> map = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
    }

    @Override
    protected String setTitle() {
        return "我的收藏";
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }



    @Override
    protected void initView() {
        mRecyclerView =  findViewById(R.id.swipe_target);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1,0xffebebeb);
        mRecyclerView.addItemDecoration(decoration);
    }



    private void getData(){
        map.put("uid", TokenManager.getUid(this));
        BaseObserver baseObserver = new BaseObserver<BaseResult<List<BookVO>>>(this, BaseObserver.MODEL_ALL){
            @Override
            public void success(BaseResult<List<BookVO>> data) {
                super.success(data);
                List<BookVO> list = data.getData();
                if(list != null && !list.isEmpty()){
                    mAdapter = new CategoryListAdapter(mActivity);
                    mAdapter.setData(data.getData());
                    mRecyclerView.setAdapter(mAdapter);
                } else{
                    showErrorEmpty("还没有收藏书籍~~");
                }
            }

        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).collectList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
