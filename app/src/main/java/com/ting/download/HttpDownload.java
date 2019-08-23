package com.ting.download;



import com.ting.common.AppData;
import com.ting.db.DBChapter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by liu on 15/6/12.
 */
public class HttpDownload {

    private Map<String, DBChapter> downloadMap = new HashMap<String, DBChapter>();
    public static HttpDownload downloadWork = null;

    private Map<String, DownloadManager> downloadThreadMap = new HashMap<String, DownloadManager>();
    private List<String> waitingList = new Vector();
    private List<String> threadList = new Vector();
    private boolean loading = false;

    private DownLoadService.DownLoadListener downloadListener;

    public HttpDownload(DownLoadService.DownLoadListener listener) {
        this.downloadListener = listener;
    }

    public static HttpDownload getInstance(DownLoadService.DownLoadListener listener) {
        if (downloadWork == null) {
            downloadWork = new HttpDownload(listener);
        }
        return downloadWork;
    }

    private void setDownloadWaitingList(DBChapter result) {
        String key = result.getBookId() + "" + result.getChapterId();
        this.downloadMap.put(key, result);
        if (!waitingList.contains(key)) {
            waitingList.add(key);
            downloadListener.onStart(result);
        }

    }

    public void download(final DBChapter result) {
        setDownloadWaitingList(result);
        if (!loading) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        //等待数量
                        int waitingSize = 0;
                        //下载数量
                        int threadlistSize = 0;
                        while ((waitingSize = waitingList.size()) > 0) {
                            loading = true;
                            if ((threadlistSize = threadList.size()) < 3 && threadlistSize < waitingSize) {
                                threadlistSize = threadList.size();
                                String key = waitingList.get(threadlistSize);
                                if (!threadList.contains(key)) {
                                    threadList.add(key);
                                    DBChapter vo = downloadMap.get(key);
                                    File file = new File(AppData.FILE_PATH + vo.getBookId());
                                    if(!file.exists()){
                                        file.mkdirs();
                                    }
                                    DownloadManager manager = new DownloadManager();
                                    manager.download(vo, file, downloadListener);
                                    downloadThreadMap.put(key, manager);
                                }
                            }
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }

                    loading = false;
                }
            }.start();
        }
    }

    public void stopDownload(DBChapter result) {
        String keyString = result.getBookId() + "" + result.getChapterId();
        DownloadManager thread = downloadThreadMap.get(keyString);
        if (thread != null) {
            thread.setPause();
        }
        cancle(result);
    }

    public void completeDownload(DBChapter vo) {
        String key = vo.getBookId() + "" + vo.getChapterId();
        if (threadList.contains(key)) {
            threadList.remove(key);
        }
        if (waitingList.contains(key)) {
            waitingList.remove(key);
        }
        downloadMap.remove(key);
        downloadThreadMap.remove(key);
    }

    public void cancle(DBChapter result) {
        String key = result.getBookId() + "" + result.getChapterId();
        if (threadList.contains(key)) {
            threadList.remove(key);
        }
        if (waitingList.contains(key)) {
            waitingList.remove(key);
        }
        downloadMap.remove(key);
        downloadThreadMap.remove(key);
    }
}
