package com.ting.bean.apk;

import java.io.Serializable;

/**
 * Created by liu on 16/9/26.
 */

public class ApkBean implements Serializable {
    private String file_path;
    private String version;
    private int update_status;
    private String content;

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getUpdate_status() {
        return update_status;
    }

    public void setUpdate_status(int update_status) {
        this.update_status = update_status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
