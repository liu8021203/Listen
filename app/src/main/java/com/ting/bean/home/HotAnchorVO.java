package com.ting.bean.home;

/**
 * Created by liu on 2017/8/2.
 */

public class HotAnchorVO {

    /**
     * id : 321
     * username : 坟头长草
     * thumb : http://fdfs.xmcdn.com/group5/M04/81/02/wKgDtVRkk2rgd1oPAACAti2la_4153_web_x_large.jpg
     * focus : 0
     */

    private String id;
    private String username;
    private String thumb;
    private int focus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }
}
