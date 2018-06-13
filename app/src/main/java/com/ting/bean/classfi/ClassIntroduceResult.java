package com.ting.bean.classfi;

import com.ting.bean.BaseResult;
import com.ting.bean.classfi.ClassIntroMess;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/24.
 * 分类详情返回结果集
 */
public class ClassIntroduceResult extends BaseResult {
    private int lenght;//记录结果条数
    private int page;//记录结果当前页数
    private int count;//记录结果每页数量

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
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

    private List<ClassIntroMess> data;//分类数据

    public List<ClassIntroMess> getData() {
        return data;
    }

    public void setData(List<ClassIntroMess> data) {
        this.data = data;
    }
}
