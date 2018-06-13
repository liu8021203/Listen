package com.ting.constant;

// TODO: Auto-generated Javadoc
/**
 * 描述：常量.
 * 
 * @author amsoft.cn
 * @date：2013-1-15 下午3:12:06
 * @version v1.0
 */
public class CodeConstant {

	/** SharePreferences文件名. */
	public static final String SHAREPATH = "kting_info_sp";

	/** 图片处理：裁剪. */
	public static final int CUTIMG = 0;

	/** 图片处理：缩放. */
	public static final int SCALEIMG = 1;

	/** 图片处理：不处理. */
	public static final int ORIGINALIMG = 2;

	/** 返回码：成功. */
	public static final int RESULRCODE_OK = 0;

	/** 返回码：失败. */
	public static final int RESULRCODE_ERROR = -1;

	/** 显示Toast. */
	public static final int SHOW_TOAST = 0;

	/** 显示进度框. */
	public static final int SHOW_PROGRESS = 1;

	/** 删除进度框. */
	public static final int REMOVE_PROGRESS = 2;

	/** 删除底部进度框. */
	public static final int REMOVE_DIALOGBOTTOM = 3;

	/** 删除中间进度框. */
	public static final int REMOVE_DIALOGCENTER = 4;

	/** 删除顶部进度框. */
	public static final int REMOVE_DIALOGTOP = 5;

	/** View的类型. */
	public static final int LISTVIEW = 1;

	/** The Constant GRIDVIEW. */
	public static final int GRIDVIEW = 1;

	/** The Constant GALLERYVIEW. */
	public static final int GALLERYVIEW = 2;

	/** The Constant RELATIVELAYOUTVIEW. */
	public static final int RELATIVELAYOUTVIEW = 3;

	/** Dialog的类型. */
	public static final int DIALOGPROGRESS = 0;

	/** The Constant DIALOGBOTTOM. */
	public static final int DIALOGBOTTOM = 1;

	/** The Constant DIALOGCENTER. */
	public static final int DIALOGCENTER = 2;

	/** The Constant DIALOGTOP. */
	public static final int DIALOGTOP = 3;

	/**
	 * 显示正在加载的图片
	 */
//	public static final int TOASTIMAGETYPE_LOADING = 0;
	/**
	 * 显示没有网络的图片
	 */
	public static final int TOASTIMAGETYPE_NONET = 1;
	/**
	 * 显示没有数据的图片
	 */
	public static final int TOASTIMAGETYPE_NODATA = 2;
	/**
	 * 显示数据获取异常的图片
	 */
	public static final int TOASTIMAGETYPE_ERR = 3;

	
	/** 手机注册获取验证码类型 AUTHCODE_REGISTER */
	public static final int AUTHCODE_REGISTER = 1;

	/** 手机绑定,修改绑定获取验证码类型 AUTHCODE_BOUND */
	public static final int AUTHCODE_BOUND = 2;

	/** 修改密码获取验证码类型 AUTHCODE_MODIFY_BOUND */
	public static final int AUTHCODE_MODIFY_BOUND = 3;

	public static final int AUTHCODE_MODIFY_BOUND_FINISH = 4;
	
	
}
