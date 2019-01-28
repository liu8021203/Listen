package com.ting.bean.apk;

import java.io.Serializable;

/**
 * Created by liu on 16/9/26.
 */

public class ApkResult implements Serializable {
    private String version;
    // 0:强制下载   1：非强制下载
    private int update;
    private String content;
    private String downloadUrl;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getUpdate() {
        return update;
    }

    public void setUpdate(int update) {
        this.update = update;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
