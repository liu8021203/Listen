package com.ting.bean.search;

import com.ting.bean.BaseResult;
import com.ting.bean.search.SearchBean;

import java.util.List;

/**
 * Created by gengjiajia on 15/10/15.
 * 搜索结果集
 */
public class SearchHotResult extends BaseResult{
    private List<SearchBean>data;//最热的前几条

    public List<SearchBean> getData() {
        return data;
    }

    public void setData(List<SearchBean> data) {
        this.data = data;
    }
}
