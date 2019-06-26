package com.ting.myself;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.vo.HostVO;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.myself.adapter.MySeeAdapter;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/9/10.
 * 我的关注frame;
 */
public class MySeeActivity extends BaseActivity {
    private MySeeAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Map<String, String> map = new HashMap<>();
    private TextView have_see_number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_my_see);
    }

    @Override
    protected String setTitle() {
        return "我的关注";
    }

    @Override
    protected void initView() {
        have_see_number =  findViewById(R.id.have_see_number);
        mRecyclerView =  findViewById(R.id.swipe_target);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1, 0xffebebeb);
        mRecyclerView.addItemDecoration(decoration);
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




    /**
     * 加载数据
     */
    private void getData(){
        map.put("uid", TokenManager.getUid(mActivity));
        BaseObserver baseObserver = new BaseObserver<BaseResult<List<HostVO>>>(this, BaseObserver.MODEL_ALL){


            @Override
            public void success(BaseResult<List<HostVO>> data) {
                super.success(data);
                if(data.getData() != null && !data.getData().isEmpty()){
                    if(mAdapter == null) {
                        mAdapter = new MySeeAdapter(MySeeActivity.this);
                        mAdapter.setResult(data.getData());
                        mRecyclerView.setAdapter(mAdapter);
                    }else{
                        mAdapter.setResult(data.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                    have_see_number.setText(String.valueOf(data.getData().size()));
                }else{
                    showErrorEmpty("还没有关注主播哦~~");
                }
            }

        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getHostListByUid(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }


    @Override
    protected void reload() {
        getData();
    }

    public void showEmpty(){
        showErrorEmpty("还没有关注主播哦~~");
    }


    public void updateNum(int num){
        have_see_number.setText(String.valueOf(num));
    }
}
