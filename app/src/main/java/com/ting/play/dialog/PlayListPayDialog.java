package com.ting.play.dialog;

import android.app.Dialog;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.vo.BuyChapterWayVO;
import com.ting.bean.vo.CardVO;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.db.DBChapter;
import com.ting.play.adapter.BuyChapterWaydapter;
import com.ting.util.UtilRetrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 15/11/5.
 */
public class PlayListPayDialog extends Dialog implements OnClickListener{
    private Button btn_cancle;
    private Button btn_buy;
    //type：0为单集购买，1为全集购买，2为购买VIP
    private int type = 0;
    private BaseActivity activity;
    private DBChapter vo;
    private int price;
    private CallBackListener listener;
    private int bookId;
    private List<CardVO> cardData;
    private RecyclerView mRecyclerView;
    private BuyChapterWaydapter mWaydapter;



    public PlayListPayDialog(BaseActivity activity) {
        super(activity, R.style.CustomDialog);
        this.activity = activity;
    }



    public void setVo(DBChapter vo, int price) {
        this.vo = vo;
        this.price = price;
    }

    public void setCardData(List<CardVO> cardData) {
        this.cardData = cardData;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setListener(CallBackListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay_select);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        init();
    }


    private void init() {
        btn_buy =  findViewById(R.id.btn_ok);
        btn_buy.setOnClickListener(this);
        btn_cancle =  findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(this);
        mRecyclerView =  findViewById( R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(manager);
        btn_buy =  findViewById(R.id.btn_ok);
        btn_buy.setOnClickListener(this);
        btn_cancle =  findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(this);
        List<BuyChapterWayVO> data = new ArrayList<>();
        if(cardData != null && !cardData.isEmpty()){
            BuyChapterWayVO vo1 = new BuyChapterWayVO();
            vo1.setBookId(vo.getBookId());
            vo1.setCardId(vo.getChapterId());
            vo1.setType(0);
            vo1.setDesc("购买单集《" + vo.getTitle() + "》" + vo.getPrice() + "听豆");
            data.add(vo1);
            BuyChapterWayVO vo2 = new BuyChapterWayVO();
            vo2.setBookId(vo.getBookId());
            vo2.setCardId(vo.getChapterId());
            vo2.setType(1);
            vo2.setDesc("购买全集" + vo.getBookPrice() + "听豆");
            data.add(vo2);
            for (int i = 0; i < cardData.size(); i++){
                BuyChapterWayVO cardVO = new BuyChapterWayVO();
                cardVO.setBookId(vo.getBookId());
                cardVO.setCardId(vo.getChapterId());
                cardVO.setType(2);
                cardVO.setDesc("主播听书卡：" + cardData.get(i).getCardDesc());
                cardVO.setCardId(cardData.get(i).getId());
                data.add(cardVO);
            }
        }else{
            BuyChapterWayVO vo1 = new BuyChapterWayVO();
            vo1.setBookId(vo.getBookId());
            vo1.setCardId(vo.getChapterId());
            vo1.setType(0);
            vo1.setDesc("购买单集《" + vo.getTitle() + "》" + vo.getPrice() + "听豆");
            data.add(vo1);
            BuyChapterWayVO vo2 = new BuyChapterWayVO();
            vo2.setBookId(vo.getBookId());
            vo2.setCardId(vo.getChapterId());
            vo2.setType(1);
            vo2.setDesc("购买全集" + vo.getBookPrice() + "听豆");
            data.add(vo2);
        }
        mWaydapter = new BuyChapterWaydapter(activity);
        mWaydapter.setData(data);
        mRecyclerView.setAdapter(mWaydapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_cancle:
                dismiss();
                break;
            case R.id.btn_ok:{
                if(mWaydapter == null){
                    activity.showToast("请选择购买的服务");
                    return;
                }
                if(mWaydapter.getDefVO() == null){
                    activity.showToast("请选择购买的服务");
                    return;
                }
                if(mWaydapter.getDefVO().getType() == 0){
                    buySingle();
                }else if(mWaydapter.getDefVO().getType() == 1){
                    buyAll();
                }else{
                    buyListCard(mWaydapter.getDefVO());
                }
            }
                break;

        }
    }




    public interface CallBackListener
    {
        void singleCallBack();
        void allCallBack();
        void listBookCallBack();
    }

    /**
     * 购买单集
     */
    private void buySingle(){
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(activity));
        map.put("bookId", vo.getBookId());
        map.put("chapterId", vo.getChapterId());
        BaseObserver baseObserver = new BaseObserver<BaseResult<DBChapter>>(activity, BaseObserver.MODEL_SHOW_DIALOG_TOAST){
            @Override
            public void success(BaseResult<DBChapter> data) {
                super.success(data);
                if(data.getData() != null) {
                    vo.setUrl(data.getData().getUrl());
                    listener.singleCallBack();
                }
                dismiss();
            }
        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).buyChapter(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    /**
     * 购买全集
     */
    private void buyAll(){
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(activity));
        map.put("bookId", vo.getBookId());
        BaseObserver baseObserver = new BaseObserver<BaseResult>(activity, BaseObserver.MODEL_SHOW_DIALOG_TOAST){
            @Override
            public void success(BaseResult data) {
                super.success(data);
                if(listener != null) {
                    listener.allCallBack();
                }
                dismiss();
            }


        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).buyBook(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    private void buyListCard(BuyChapterWayVO wayVO){
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(activity));
        map.put("cardId", wayVO.getCardId());
        BaseObserver baseObserver = new BaseObserver<BaseResult>(activity, BaseObserver.MODEL_SHOW_DIALOG_TOAST){
            @Override
            public void success(BaseResult data) {
                super.success(data);
                if(listener != null) {
                    listener.listBookCallBack();
                }
                dismiss();
            }


        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).buyCard(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

}
