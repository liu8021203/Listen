package com.ting.common;

import android.os.Environment;

import com.ting.bean.anchor.LiWuResult;
import com.ting.bean.myself.PersonMessResult;
import com.ting.bean.play.PlayListVO;
import com.ting.bean.vo.GiftVO;

import java.lang.ref.SoftReference;
import java.util.List;


/**
 * Created by liu on 15/6/1.
 */
public class AppData {
    // APP_ID 替换为你的应用从官方网站申请到的合法appId
    public static final String WX_APP_ID = "wx65ef305f9d0ebdec";
    public static String BASE_URL = "http://www.tingshijie.com/index.php?";
    public static String uid = null;
    public static PersonMessResult info = null;
    public static SoftReference<PlayListVO> playListVos = null;
    public static String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/QQ/";
    public static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/listen/";
    public static String FILE_PATH = PATH + "mp3/";
    public static String PATH_VOLLEY = PATH + "volley/";
    public static final String CACHE_PATH = PATH + "file/";
    public static final int PERMISSION_CODE = 999;

    /**
     * 每页请求的数量
     */
    public static final String offset = "10";

    /**
     * 关闭章节 0是关闭 1是本章结束，2是两章之后
     */
    public static int shutDownProgramNum;

    /**
     * 是否正在播放
     */
    public static boolean isPlaying = false;
    /**
     * 是否单曲循环
     */
    public static boolean isRepeat = false;
    /**
     * 是否更新热门主播数据
     */
    public static boolean refushHotAnchor = true;
    /**
     * 当前播放位置
     */
    public static int position = 0;

    /**
     * 是否触摸seekbar, false是未触摸， true是触摸
     */
    public static boolean isSeekbarFoucs = false;
    /**
     * 当前加载的key
     */
    public static String loadingKey = null;

    /**
     * 当前播放的key
     **/
    public static String playKey = null;
    /**
     * 当前正在播放的书籍id
     */
    public static String currPlayBookId = null;

    /** 关闭时间 单位ms 0为关闭 */
    public static int shutDownTimer=0;
    /**是否设置了时间定时器**/
    public static boolean ifTimeSetting=false;

    public static PersonMessResult getInfo() {
        if (info == null) {
            info = new PersonMessResult();
        }
        return info;
    }



    //倍速播放         1:  1x,  2:1.25x,  3:1.5x,  4:1.75x,   5: 2x
    public static int speedType = 1;
}
