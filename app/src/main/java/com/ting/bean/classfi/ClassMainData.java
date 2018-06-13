package com.ting.bean.classfi;

import com.ting.bean.base.BaseParam;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/24.
 * 分类主界面信息
 */
public class ClassMainData {
   private int id;//分类ID
   private String name;//分类的标题
   private String thumb;//分类的封面URL
   private List<ClassChilderMess>children;//分类子集合

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public List<ClassChilderMess> getChildren() {
        return children;
    }

    public void setChildren(List<ClassChilderMess> children) {
        this.children = children;
    }
}
