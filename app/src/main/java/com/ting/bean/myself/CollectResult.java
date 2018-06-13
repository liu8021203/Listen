package com.ting.bean.myself;

import com.ting.bean.BaseResult;
import com.ting.bean.record.CollectInfo;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/28.
 * 收藏结果集
 */
public class CollectResult extends BaseResult {

    private List<CollectInfo> collection;

    public List<CollectInfo> getData() {
        return collection;
    }

    public void setData(List<CollectInfo> data) {
        this.collection = data;
    }
}
