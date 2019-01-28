package com.ting.download;

import com.ting.db.DBChapter;
import com.ting.util.UtilMD5Encryption;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by liu on 15/6/12.
 */
public class DownloadThread extends Thread{
    /**
     * 下载初始化
     */
    public static final int DOWNLOADINIT = 0;
    /**
     * 下载中
     */
    public static final int DOWNLOADING = 1;
    /**
     * 下载暂停
     */
    public static final int DOWNLOADPAUSE = 2;
    /**
     * 下载完成
     */
    public static final int DOWNLOADFINISH = 3;

    private String fileURL;
    private File saveFile;
    private long completeSize = 0L;

    private boolean isPause = false;

    private DBChapter result;
    private DownLoadService.DownLoadListener listener;


    public DownloadThread(DBChapter result, File fileSaveDir, DownLoadService.DownLoadListener listener)
    {
        this.result = result;
        this.listener = listener;
        this.fileURL = result.getUrl();
        if(!fileSaveDir.exists())
        {
            fileSaveDir.mkdirs();
        }
        this.saveFile = new File(fileSaveDir, UtilMD5Encryption.getMd5Value(result.getChapterId()) + ".tsj");
    }

    public void setPause()
    {
        isPause = true;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        RandomAccessFile threadFile = null;
        try{

            long fileSize = getResourceSize(fileURL);
            completeSize = result.getCompleteSize();
            URL url = new URL(fileURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Language", "zh-CN");
            connection.setRequestProperty("Referer", url.toString());
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Range", "bytes="+ completeSize + "-" + fileSize);//设置获取实体数据的范围
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            connection.setRequestProperty("Connection", "Keep-Alive");
            if(connection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL)
            {
                result.setSize(fileSize);
                inputStream = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int offset = 0;
                threadFile = new RandomAccessFile(this.saveFile, "rwd");
                threadFile.seek(completeSize);
                long currentTime = 0;
                while((offset = inputStream.read(buffer)) != -1 && !isPause)
                {
                    threadFile.write(buffer, 0, offset);
                    completeSize += offset;
                    if((System.currentTimeMillis() - currentTime) >= 3000)
                    {
                        currentTime = System.currentTimeMillis();
                        result.setCompleteSize(completeSize);
                        listener.onProgress(result);
                    }
                }
                result.setCompleteSize(completeSize);
                //isPause是暂停标志字段
                if(!isPause) {
                    listener.onComplete(result);
                }
                else {
                    listener.onCancle(result);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            listener.onError(result);
        }finally {
            try{
                if(inputStream != null)
                {
                    inputStream.close();
                }
                if(threadFile != null)
                {
                    threadFile.close();
                }
                if(connection != null)
                {
                    connection.disconnect();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                listener.onError(result);
            }
        }

    }


    /**
     * 网络获取下载文件的大小
     * @param url
     * @return
     */
    private long getResourceSize(String url)
    {
        long size = 0;
        try{
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            InputStream inputStream = connection.getInputStream();
            size = connection.getContentLength();
            inputStream.close();
            connection.disconnect();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return size;
    }
}
