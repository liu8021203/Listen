package com.ting.download;

import android.util.Log;

import com.ting.common.AppData;
import com.ting.db.DBChapter;
import com.ting.util.UtilAES;
import com.ting.util.UtilMD5Encryption;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadManager {
    private static final OkHttpClient client = new OkHttpClient();
    private boolean isPause = false;
    private boolean isCancle = false;




    public void setPause()
    {
        Log.d("aaa", "DownloadManager==========setPause");
        isPause = true;
        isCancle = false;
    }

    public void setCancle() {
        isPause = false;
        isCancle = true;
    }

    public void download(final DBChapter chapter, final File saveFile, final DownLoadService.DownLoadListener listener) {
        isPause = false;
        isCancle = false;
        Request request = new Request.Builder().url(chapter.getUrl()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onError(chapter);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                CipherInputStream cipherInputStream = null;

                // 储存下载文件的目录
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    chapter.setSize(total);
                    File file = new File(saveFile, UtilMD5Encryption.getMd5Value(chapter.getChapterId()) + ".tsj");
                    fos = new FileOutputStream(file);

                    Cipher cipher = UtilAES.initAESCipher(Cipher.ENCRYPT_MODE);
                    //以加密流写入文件
                    cipherInputStream = new CipherInputStream(is, cipher);
                    long currentTime = 0;
                    long sum = 0;
                    while ((len = cipherInputStream.read(buf)) != -1 && !isPause) {
                        Log.d("aaa", "写入数据======" + isPause);
                        fos.write(buf, 0, len);
                        sum += len;
                        if((System.currentTimeMillis() - currentTime) >= 500){
                            currentTime = System.currentTimeMillis();
                            chapter.setCompleteSize(sum);
                            if(!isPause){
                                listener.onProgress(chapter);
                            }
                        }
                    }
                    fos.flush();
                    if(!isPause) {
                        listener.onComplete(chapter);
                    }
                    else {
                        listener.onCancle(chapter);
                    }
                } catch (Exception e) {
                    listener.onError(chapter);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if(cipherInputStream != null){
                            cipherInputStream.close();
                        }
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }


}
