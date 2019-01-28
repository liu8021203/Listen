package com.ting.common;

import com.ting.bean.vo.GiftVO;

import java.util.List;

public class GiftManager {
    public static List<GiftVO> gifts;

    public static void setGifts(List<GiftVO> gifts) {
        GiftManager.gifts = gifts;
    }

    public static List<GiftVO> getGifts() {
        return gifts;
    }
}
