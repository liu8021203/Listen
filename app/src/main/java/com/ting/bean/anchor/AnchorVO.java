package com.ting.bean.anchor;

/**
 * Created by liu on 2017/11/15.
 */

public class AnchorVO {
    private String thumb;
    private String name;
    private int id;
    private boolean isFollowed;
    private int focusFans;
    private int worksCount;
    private String groupname;
    private String firstStr;

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public int getFocusFans() {
        return focusFans;
    }

    public void setFocusFans(int focusFans) {
        this.focusFans = focusFans;
    }

    public int getWorksCount() {
        return worksCount;
    }

    public void setWorksCount(int worksCount) {
        this.worksCount = worksCount;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getFirstStr() {
        return firstStr;
    }

    public void setFirstStr(String firstStr) {
        this.firstStr = firstStr;
    }
}
