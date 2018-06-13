package com.ting.bean.play;

import com.ting.bean.base.BaseParam;

import java.util.List;

/**
 * Created by gengjiajia on 15/10/8.
 * 评论
 */
public class Comment extends BaseParam{
    private int lenght;//评论数量
    private int page;//评论当前页数
    private int count;//评论每页数量
    private List<CommentDate>data;//评论详情

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

    public List<CommentDate> getData() {
        return data;
    }

    public void setData(List<CommentDate> data) {
        this.data = data;
    }
}
