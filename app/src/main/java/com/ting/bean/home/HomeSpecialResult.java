package com.ting.bean.home;

import com.ting.bean.BaseResult;

import java.util.List;

/**
 * Created by liu on 2017/8/7.
 */

public class HomeSpecialResult extends BaseResult{
    private String title;
    private List<HomeSpecialVO> booklist;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HomeSpecialVO> getBooklist() {
        return booklist;
    }

    public void setBooklist(List<HomeSpecialVO> booklist) {
        this.booklist = booklist;
    }
}
