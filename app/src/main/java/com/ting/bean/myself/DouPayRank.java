package com.ting.bean.myself;

import com.ting.bean.base.BaseParam;

/**
 * Created by gengjiajia on 15/9/29.
 * 充值等级数组
 */
public class DouPayRank extends BaseParam {
    private int id;//显示的充值ID
    private String money;//显示的充值文本
    private int credit;//充值的听豆
    private String name;//充值的名称

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
