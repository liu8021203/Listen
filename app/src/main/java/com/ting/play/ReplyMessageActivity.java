package com.ting.play;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.play.CommentDate;
import com.ting.bean.play.ReplyResult;
import com.ting.common.TokenManager;
import com.ting.common.http.BaseSubscriber;
import com.ting.common.http.HttpService;
import com.ting.login.LoginMainActivity;
import com.ting.play.adapter.ReplyAdapter;
import com.ting.util.UtilDate;
import com.ting.util.UtilGlide;
import com.ting.util.UtilRetrofit;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 2017/6/23.
 */

public class ReplyMessageActivity extends BaseActivity implements View.OnClickListener{
    private CircleImageView mCivImg;
    private TextView mTvName;
    private TextView mTvContext;
    private ImageView mIvVip;
    private TextView mTvTime;
    private TextView mRankName;
    private CommentDate mVO;
    private RecyclerView mRecyclerView;
    private Button mBtnSend;
    private EditText mEtContent;
    private ReplyAdapter mAdapter;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        mVO = (CommentDate) bundle.getSerializable("vo");
        bookId = bundle.getInt("bookId", -1);
        if(mVO == null || bookId == -1){
            showToast("数据有误，请联系客服");
            return;
        }
        setContentView(R.layout.activity_reply);
    }

    @Override
    protected String setTitle() {
        return "评论";
    }
    @Override
    protected void initView() {
        mCivImg = (CircleImageView) findViewById(R.id.civ_img);
        mTvName = (TextView) findViewById(R.id.comment_user_name);
        mTvContext = (TextView) findViewById(R.id.comment_context);
        mIvVip = (ImageView) findViewById(R.id.iv_vip);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mRankName = (TextView) findViewById(R.id.tv_rank_name);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mEtContent = (EditText) findViewById(R.id.et_reply);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }

    @Override
    protected void initData(){
        UtilGlide.loadHeadImg(this, mVO.getThumb(), mCivImg);
        mTvName.setText(mVO.getName());
        mTvContext.setText(mVO.getString());
        if(mVO.isVip())
        {
            mIvVip.setVisibility(View.VISIBLE);
            mTvName.setTextColor(0xfff64721);
        }
        else
        {
            mIvVip.setVisibility(View.GONE);
            mTvName.setTextColor(0xff000000);
        }
        mTvTime.setText(UtilDate.getDifferNowTime(mVO.getTime(), System.currentTimeMillis() / 1000));
        mRankName.setText(mVO.getRankname());
        mAdapter = new ReplyAdapter(this);
        mAdapter.setData(mVO.getReply_list());
        mRecyclerView.setAdapter(mAdapter);
        getReplyData();
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_activity_title_left:
                onBackPressed();
                break;

            case R.id.btn_send:
                if(!TokenManager.isLogin(mActivity)){
                    intent(LoginMainActivity.class);
                    return;
                }
                String content = mEtContent.getText().toString();
                if(TextUtils.isEmpty(content)){
                    showToast("请输入评论的内容");
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("bookID", String.valueOf(bookId));
                map.put("uid", TokenManager.getUid(mActivity));
                map.put("message", content);
                map.put("commentid", String.valueOf(mVO.getId()));
                UtilRetrofit.getInstance().create(HttpService.class).setPostComment(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<BaseResult>(){
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        removeProgressDialog();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        removeProgressDialog();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(BaseResult result) {
                        super.onNext(result);
                        getReplyData();
                    }
                });
                break;
        }
    }

    private void getReplyData(){
        Map<String,String> map = new HashMap<>();
        map.put("commentid", String.valueOf(mVO.getId()));
        map.put("count", "50");
        map.put("page", "1");
        BaseObserver baseObserver = new BaseObserver<ReplyResult>(this){
            @Override
            public void success(ReplyResult data) {
                super.success(data);
                mEtContent.setText("");
                if (mAdapter == null) {
                    mAdapter = new ReplyAdapter(ReplyMessageActivity.this);
                    mAdapter.setData(data.getData());
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    mAdapter.setData(data.getData());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void error() {
                super.error();
            }
        };
        UtilRetrofit.getInstance().create(HttpService.class).getComment_replay(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }
}
