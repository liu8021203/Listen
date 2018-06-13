package com.ting.receiver;

import android.content.Context;
import android.content.Intent;

import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

/**
 * Created by liu on 16/9/27.
 */

public class AppMessageReceiver extends PushMessageReceiver{

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageClicked(context, miPushMessage);
//        Intent intent
    }
}
