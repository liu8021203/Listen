package com.ting.myself;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.myself.MessageJavaResult;
import com.ting.bean.vo.MessageListVO;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.myself.adapter.MessageJavaAdapter;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 2018/1/22.
 */

public class MessageJavaActivity extends BaseActivity{
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }

    @Override
    protected String setTitle() {
        return "消息中心";
    }

    @Override
    protected void initView() {
        mRecyclerView = flContent.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1, 0xffcccccc);
        mRecyclerView.addItemDecoration(decoration);
        clearMessage();
    }

    @Override
    protected void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(mActivity));
        BaseObserver baseObserver = new  BaseObserver<BaseResult<List<MessageListVO>>>(this, BaseObserver.MODEL_ALL){

            @Override
            public void success(BaseResult<List<MessageListVO>> data) {
                super.success(data);
                if(data != null && data.getData() != null && !data.getData().isEmpty()) {
                    MessageJavaAdapter adapter = new MessageJavaAdapter();
                    adapter.setData(data.getData());
                    mRecyclerView.setAdapter(adapter);
                }else{
                    showErrorEmpty("还没有相关消息");
                }
            }

            @Override
            public void error() {
                super.error();
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getMessageListByUserId(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }


    private void clearMessage(){
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(mActivity));
        BaseObserver baseObserver = new BaseObserver<BaseResult>();
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).readMessageByUserId(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }
}
