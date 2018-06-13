package com.ting.bean.anchor;

import com.ting.bean.BaseResult;
import com.ting.bean.anchor.LiWuMess;

import java.util.List;

/**
 * Created by gengjiajia on 15/10/11.
 * 打赏物品数据
 */
public class LiWuResult extends BaseResult {
    private int lenght;//数据长度
    private List<LiWuMess> data;//数据本体;

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public List<LiWuMess> getData() {
        return data;
    }

    public void setData(List<LiWuMess> data) {
        this.data = data;
    }
}
