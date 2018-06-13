package com.ting.bean.myself;

import com.ting.bean.BaseResult;
import com.ting.bean.myself.WorkInfo;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/29.
 * 我的作品结果集
 */
public class MyWorkResult extends BaseResult {
    private int lenght;//搜索结果条数
    private int page;//搜索结果当前页数
    private int count;//搜索结果每页数量
    private int player;//总的播放章数
    private List<WorkInfo> data;//作品数据


    public int getLength() {
        return lenght;
    }

    public void setLength(int length) {
        this.lenght = length;
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


    public List<WorkInfo> getData() {
        return data;
    }

    public void setData(List<WorkInfo> data) {
        this.data = data;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
}
