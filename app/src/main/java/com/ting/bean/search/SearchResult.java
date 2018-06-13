package com.ting.bean.search;

import com.ting.bean.BaseResult;
import com.ting.bean.search.SearchMess;

import java.util.List;

/**
 * Created by gengjiajia on 15/10/15.
 * 搜索结果集
 */
public class SearchResult extends BaseResult {
    private List<SearchMess> data;

    public List<SearchMess> getData() {
        return data;
    }

    public void setData(List<SearchMess> data) {
        this.data = data;
    }
}
