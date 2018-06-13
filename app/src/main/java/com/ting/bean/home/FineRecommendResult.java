package com.ting.bean.home;

import com.ting.bean.BaseResult;
import com.ting.bean.Recommend;
import com.ting.bean.Slider;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/22.
 * 精品推荐返回结果集
 */
public class FineRecommendResult extends BaseResult{
    private List<Slider> slider;
    private List<Recommend>recommend;

    public List<Slider> getSlider() {
        return slider;
    }

    public void setSlider(List<Slider> slider) {
        this.slider = slider;
    }

    public List<Recommend> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<Recommend> recommend) {
        this.recommend = recommend;
    }
}
