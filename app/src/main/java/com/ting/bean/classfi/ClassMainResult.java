package com.ting.bean.classfi;

import com.ting.bean.BaseResult;
import com.ting.bean.classfi.ClassMainData;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/24.
 * 分类主界面返回结果集
 */
public class ClassMainResult extends BaseResult{
    private List<ClassMainData> data;

    public List<ClassMainData> getData() {
        return data;
    }

    public void setData(List<ClassMainData> data) {
        this.data = data;
    }
}
