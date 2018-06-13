package com.ting.bean.anchor;

import com.ting.bean.base.BaseParam;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/22.
 * 奖赏
 */
public class Reward extends BaseParam {
    private int lenght;
    private int page;//当前所在页数
    private int count;//当前每页条数
    private List<RewardMess> data;

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<RewardMess> getData() {
        return data;
    }

    public void setData(List<RewardMess> data) {
        this.data = data;
    }
}
