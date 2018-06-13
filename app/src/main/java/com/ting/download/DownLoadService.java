package com.ting.download;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.ting.db.DBChapter;
import com.ting.util.UtilNetStatus;

/**
 * Created by liu on 15/6/12.
 */
public class DownLoadService extends Service{
    /**
     * 下载
     */
    public static final int ACTION_DOWN = 1;
    /**
     * 暂停
     */
    public static final int ACTION_STOP = 2;
    /**
     * 删除
     */
    public static final int ACTION_REMOVE = 3;
    /**
     * 下载进度条
     */
    public static final String BROADCAST_ACTION_PROGRESS = "com.listen.download.progress";
    /**
     * 下载完成
     */
    public static final String BROADCAST_ACTION_COMPLETE = "com.listen.download.complete";
    /**
     * 下载开始
     */
    public static final String BROADCAST_ACTION_START = "com.listen.download.start";
    /**
     * 下载错误
     */
    public static final String BROADCAST_ACTION_ERROR = "com.listen.download.error";
    /**
     * 下载取消
     */
    public static final String BROADCAST_ACTION_CANCLE = "com.listen.download.cancle";

    public static final int INIT = 1;
    public static final int DOWNLOADING = 2;
    public static final int PAUSE = 3;
    public static final int FINISH = 4;

    private static final int HandlerStart = 1;
    private static final int HandlerProgress = 2;
    private static final int HandlerComplete = 3;
    private static final int HandlerError = 4;
    private static final int HandleCancle = 5;

    private HttpDownload download;

    private String sName;
    private String pic;
    private DownloadController controller;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int action = msg.what;
            Intent intent = new Intent();
            switch (action) {
                case HandlerStart:
                    intent.setAction(BROADCAST_ACTION_START);
                    intent.putExtra("data", bundle);
                    sendBroadcast(intent);
                    break;

                case HandlerProgress:
                    intent.setAction(BROADCAST_ACTION_PROGRESS);
                    intent.putExtra("data", bundle);
                    sendBroadcast(intent);
                    break;

                case HandlerComplete:

                    intent.setAction(BROADCAST_ACTION_COMPLETE);
                    intent.putExtra("data", bundle);
                    sendBroadcast(intent);
                    break;

                case HandlerError:
                    intent.setAction(BROADCAST_ACTION_ERROR);
                    intent.putExtra("data", bundle);
                    sendBroadcast(intent);
                    break;

                case HandleCancle:
                    intent.setAction(BROADCAST_ACTION_CANCLE);
                    intent.putExtra("data", bundle);
                    sendBroadcast(intent);
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public void onCreate() {
        super.onCreate();
        download = HttpDownload.getInstance(new DownLoadListener());
        controller = new DownloadController();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null)
        {
            return 0;
        }
        int msg = intent.getIntExtra("MSG", 0);
        DBChapter vo =  intent.getParcelableExtra("vo");
        if(msg == ACTION_DOWN)
        {
            downAction(vo);
        }
        else
        {
            if(msg == ACTION_STOP)
            {
                stopAction(vo);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 开始下载
     * @param result
     */
    private void downAction(DBChapter result)
    {
        if(result == null || result.getChapterUrl() == null)
        {
            return;
        }
        if(!UtilNetStatus.isHasConnection(this))
        {
            Toast.makeText(DownLoadService.this, "没有网络无法下载！", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        download.download(result);
    }

    /**
     * 暂停下载
     * @param result
     */
    private void stopAction(DBChapter result)
    {
        if(result == null || result.getChapterUrl() == null)
        {
            return;
        }
        download.stopDownload(result);
    }

    public class DownLoadListener
    {
        public void onStart(DBChapter result)
        {
            System.out.println("开始下载");
            result.setState(1);
            controller.insert(result);
            Message message = new Message();
            message.what = HandlerStart;
            Bundle bundle = new Bundle();
            bundle.putParcelable("vo", result);
            message.setData(bundle);
            handler.sendMessage(message);
        }

        public void onProgress(DBChapter result)
        {
            System.out.println("下载进行中");
            result.setState(2);
            controller.update(result);
            Message message = new Message();
            message.what = HandlerProgress;
            Bundle bundle = new Bundle();
            bundle.putParcelable("vo", result);
            message.setData(bundle);
            handler.sendMessage(message);
        }

        public void onComplete(DBChapter result)
        {
            System.out.println("下载完成");
            result.setState(4);
            controller.update(result);
            Message message = new Message();
            message.what = HandlerComplete;
            Bundle bundle = new Bundle();
            bundle.putParcelable("vo", result);
            message.setData(bundle);
            download.completeDownload(result);
            handler.sendMessage(message);
        }

        public void onError(DBChapter result)
        {
            System.out.println("下载错误");
            result.setState(0);
            controller.update(result);
            Message message = new Message();
            message.what = HandlerError;
            Bundle bundle = new Bundle();
            bundle.putParcelable("vo", result);
            message.setData(bundle);
            download.cancle(result);
            handler.sendMessage(message);
        }

        public void onCancle(DBChapter result)
        {
            System.out.println("下载暂停");
            result.setState(3);
            controller.update(result);
            Message message = new Message();
            message.what = HandleCancle;
            Bundle bundle = new Bundle();
            bundle.putParcelable("vo", result);
            message.setData(bundle);
            download.cancle(result);
            handler.sendMessage(message);
        }
    }
}
