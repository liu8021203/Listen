package com.ting.download;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.orhanobut.logger.Logger;
import com.ting.util.UtilSPutil;

import java.io.File;

/**
 * Created by liu on 16/9/18.
 */
public class ApkDownload {
    private DownloadManager dm = null;
    private Context context;
    private static ApkDownload download = null;

    public ApkDownload(Context context) {
        this.context = context;
        dm = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public ApkDownload getInstance(Context context)
    {
        if(download == null)
        {
            download = new ApkDownload(context);
        }
        return download;
    }

    /**
     * 开始下载
     * @param url
     * @param titile
     * @param desc
     */
    public void startDownload(String url, String titile, String desc)
    {
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));
        req.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "update.apk");
        req.setTitle(titile);
        req.setDescription(desc);
        req.setMimeType("application/vnd.android.package-archive");
        UtilSPutil.getInstance(context).setLong("APK_ID", dm.enqueue(req));
    }

    /**
     * 下载版本的逻辑处理
     * @param url
     * @param title
     * @param desc
     */
    public void download(String url, String title, String desc)
    {
        long apkId = UtilSPutil.getInstance(context).getLong("APK_ID", -1L);
        if(apkId != -1L)
        {
            int status = getDonwloadStatus(apkId);
            if(status == DownloadManager.STATUS_SUCCESSFUL)
            {
                Uri uri = getDownloadUri(apkId);
                if(uri != null)
                {
                    Cursor c = dm.query(new DownloadManager.Query().setFilterById(apkId));
                    String realPath = "";
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
                    if(compare(getApkInfo(context, realPath), context))
                    {
                        startInstall(context, realPath);
                        dm.remove(apkId);
                        UtilSPutil.getInstance(context).setLong("APK_ID", -1L);
                        return;
                    }
                    else {
                        dm.remove(apkId);
                        startDownload(url, title, "");
                    }
                }
                else {
                    startDownload(url, title, "");
                }
            }
            else if(status == DownloadManager.STATUS_FAILED)
            {
                startDownload(url, title, "");
            }else if(status == DownloadManager.STATUS_PENDING){
                Logger.d("apk is already pending");
            }else if(status == DownloadManager.STATUS_RUNNING){
                Logger.d("apk is already running");
            } else {
                startDownload(url, title, "");
            }
        }
        else {
            startDownload(url, title, "");
        }
    }

    /**
     * 获取下载状态的信息
     * @param downloadId
     * @return
     */
    public int getDonwloadStatus(long downloadId)
    {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = dm.query(query);
        if(cursor != null)
        {
            try {
                if(cursor.moveToFirst())
                {
                    return cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                cursor.close();
            }
        }
        return -1;
    }

    /**
     * 获取下载文件的地址
     * @param id
     * @return
     */
    private Uri getDownloadUri(long id)
    {
        return dm.getUriForDownloadedFile(id);
    }

    /**
     * 获取Apk程序信息
     * @param context
     * @param path
     * @return
     */
    private PackageInfo getApkInfo(Context context, String path)
    {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if(info != null)
        {
            return info;
        }
        return null;
    }

    /**
     * 下载Apk
     * @param info
     * @param context
     * @return
     */
    private boolean compare(PackageInfo info, Context context)
    {
        if(info == null)
        {
            return false;
        }
        String packageName = context.getPackageName();
        if(info.packageName.equals(packageName))
        {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
                if (info.versionCode > packageInfo.versionCode)
                {
                    return true;
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 安装
     * @param context
     * @param url
     */
    private void startInstall(Context context, String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if(Build.VERSION.SDK_INT >= 24){
            uri = FileProvider.getUriForFile(context,"com.ting.fileprovider", new File(url));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else {
            uri = Uri.fromFile(new File(url));
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
