package com.ting.bean.anchor;

import com.ting.bean.BaseResult;

import java.util.List;

/**
 * Created by liu on 2017/11/15.
 */

public class AnchorResult extends BaseResult{
    private List<AnchorVO> data;

    public List<AnchorVO> getData() {
        return data;
    }

    public void setData(List<AnchorVO> data) {
        this.data = data;
    }
}
