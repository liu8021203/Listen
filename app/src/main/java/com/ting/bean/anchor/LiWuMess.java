package com.ting.bean.anchor;

import com.ting.bean.base.BaseParam;

/**
 * Created by gengjiajia on 15/10/13.
 * 礼物信息
 */
public class LiWuMess extends BaseParam {
    private int id;//物品ID
    private String thumb;//封面URL
    private String name;//物体名称
    private int price;//消耗听豆

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
