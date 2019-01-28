package com.ting.bean;

import com.ting.bean.vo.MoneyListVO;

import java.util.List;

public class MoneyResult {
    private UserInfoResult userInfo;
    private List<MoneyListVO> moneyList;

    public UserInfoResult getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoResult userInfo) {
        this.userInfo = userInfo;
    }

    public List<MoneyListVO> getMoneyList() {
        return moneyList;
    }

    public void setMoneyList(List<MoneyListVO> moneyList) {
        this.moneyList = moneyList;
    }
}
