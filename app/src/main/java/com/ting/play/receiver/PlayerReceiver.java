package com.ting.play.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ting.bean.play.PlayListVO;
import com.ting.bean.play.PlayingVO;
import com.ting.play.PlayActivity;
import com.ting.play.service.MusicService;
import com.ting.play.subview.PlayListSubView;

import java.util.ArrayList;

/**
 * Created by liu on 2017/12/11.
 */

public class PlayerReceiver extends BroadcastReceiver {

    private PlayActivity mActivity;

    public PlayerReceiver(PlayActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
    }
}
