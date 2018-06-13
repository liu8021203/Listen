package com.ting.bean.myself;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/29.
 *充值豆参数
 */
public class DouPayBean {
    private String name;//充值选项名称
    private List<DouPayRank> grade;//充值等级数组

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DouPayRank> getGrade() {
        return grade;
    }

    public void setGrade(List<DouPayRank> grade) {
        this.grade = grade;
    }
}
