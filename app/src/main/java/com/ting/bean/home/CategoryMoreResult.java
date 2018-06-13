package com.ting.bean.home;

import com.ting.bean.BaseResult;

import java.util.List;

/**
 * Created by liu on 16/7/28.
 */
public class CategoryMoreResult extends BaseResult{

    private int catid;
    private List<BookCityVO> tuijianlist;
    private CategoryListResult categorylist;

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public List<BookCityVO> getTuijianlist() {
        return tuijianlist;
    }

    public void setTuijianlist(List<BookCityVO> tuijianlist) {
        this.tuijianlist = tuijianlist;
    }

    public CategoryListResult getCategorylist() {
        return categorylist;
    }

    public void setCategorylist(CategoryListResult categorylist) {
        this.categorylist = categorylist;
    }
}
