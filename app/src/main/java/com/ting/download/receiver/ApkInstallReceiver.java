package com.ting.download.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;

import com.ting.util.UtilSPutil;

import java.io.File;

/**
 * Created by liu on 16/9/18.
 */
public class ApkInstallReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        {
            long apkId = UtilSPutil.getInstance(context).getLong("APK_ID", -1L);
            installApk(context,apkId);
        }
    }

    private void installApk(Context context, long downloadApkId) {
        DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Intent install = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if(Build.VERSION.SDK_INT >= 24){
            uri = FileProvider.getUriForFile(context,"com.ting.fileprovider", new File(getUrl(dManager, (int) downloadApkId)));
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else {
            uri = Uri.fromFile(new File(getUrl(dManager, (int) downloadApkId)));
        }
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }

    private String getUrl(DownloadManager dm, int id)
    {
        Cursor c = dm.query(new DownloadManager.Query().setFilterById(id));
        String realPath = "file://";
        if(c != null){
            if(c.moveToFirst()) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    int fileUriIdx = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                    String fileUri = c.getString(fileUriIdx);
                    realPath = Uri.parse(fileUri).getPath();
                } else {
                    realPath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                }
                c.close();
            }
        }
        return realPath;
    }
}
