package com.ting.bean.bookrack;

import com.ting.bean.BaseResult;

import java.util.List;

/**
 * Created by liu on 2017/12/8.
 */

public class RandomRackResult extends BaseResult{
    private List<RandomBookVO> data;

    public List<RandomBookVO> getData() {
        return data;
    }

    public void setData(List<RandomBookVO> data) {
        this.data = data;
    }
}
