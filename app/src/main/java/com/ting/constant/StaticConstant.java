package com.ting.constant;

import com.ting.bean.anchor.LiWuResult;
import com.ting.bean.play.PlayingVO;
import com.ting.bean.UserInfoResult;

import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc

/**
 * The Class AbAppData.
 */
public class StaticConstant {


    public static Map<String, String> UrlMap;
    public static UserInfoResult userInfoResult;//用户的uid
    public static LiWuResult liWuResult;//礼物的信息
    public static Boolean getHotAnchor = true;//是否获取热门主播数据
    public static Boolean clearSate = false;//是否清除了帐号信息
    public static Boolean getPersonMess = true;//是否清除了帐号信息
    public static Boolean cleaMess = false;
    public static boolean isUpdateInfo = false;

    /**
     * 是否获取收藏数据
     */
    public static boolean motifyCollectio = false;


    /**
     * 当前播放作品的信息
     */
    public static PlayingVO playingInfo = null;
    /**
     * 当前播放作品的章节集合
     */
    public static List<PlayingVO> playingVOList = null;

    //书籍名称
    public static String bookname = null;
    public static String host = null;
    public static int rank = 0;
    public static String pic = null;
    //章节名称
    public static String cateName = null;
    /**
     * 最新更新时间
     */
    public static long LATELY_UP_TIME = 0;

    private static String[] remind = null;// 公用的BaseInfo


    public static String[] setRemindWord(List<String> list) {
        if (list != null) {
            remind = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                remind[i] = (String) list.get(i);
            }

        }
        return remind;
    }


    /**
     * 定时关闭
     */
    public static boolean IsAutoClose = false;

    /**
     * 自动关闭 -1，未设置
     * <p/>
     * 0，定时关闭
     * <p/>
     * <p/>
     * 1，定章节关闭
     */
    public static int AutoClosetype = -1;
    public static final int AUTOCLOSE_TYPE_BY_TIME = 0;
    public static final int AUTOCLOSE_TYPE_BY_SECTION = 1;

    /**
     * 定时关闭值，-1表示未定时，0==10分钟；1==20分钟；2==30分钟；3==60分钟；4==90分钟；
     */
    public static int AutoCloseByTimeValue = -1;
    /**
     * 定章节关闭值，-1表示未定章节，0==本章节；1==2章节；2==3章节；3==4章节；4==5章节；
     */
    public static int AutoCloseBySectionValue = -1;

    public static String[] colsebytiemSTR = new String[]{"10分钟后停止",
            "20分钟后停止", "30分钟后停止", "60分钟后停止", "90分钟后停止"};

    public static String[] colsebysectionSTR = new String[]{"本章节播放完停止",
            "2章节后停止", "3章节后停止", "4章节后停止", "5章节后停止"};


    public static long getLATELY_UP_TIME() {
        return LATELY_UP_TIME;
    }

    public static void setLATELY_UP_TIME(long lATELY_UP_TIME) {
        LATELY_UP_TIME = lATELY_UP_TIME;
    }


    /*
     * －－－－－－－－－－－－Play相关－－－－－－－－－－－－－－
     */
    public static PlayingVO getPlayingVO() {
        if (playingInfo == null) {
            playingInfo = new PlayingVO();
        }
        return playingInfo;
    }
    //----------------------play 相关END-----------------------


    

}
