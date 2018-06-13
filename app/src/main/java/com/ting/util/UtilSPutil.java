package com.ting.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * sharepreference 处理缓存数据
 */
public class UtilSPutil {

	private static UtilSPutil usp = null;
	private static SharedPreferences sp;

	public static UtilSPutil getInstance(Context context) {
		if (usp == null) {
			usp = new UtilSPutil();
//			try {
			if (context!=null) {
				sp = context.getSharedPreferences("info", Context.MODE_PRIVATE);
			}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
		}
		return usp;
	}

	/**
	 * 存储String值
	 * 
	 * @param key
	 *            存储键
	 * @param value
	 *            存储值
	 */
	public void setString(String key, String value) {
		sp.edit().putString(key, value).commit();
	}

	/**
	 * 存储int值
	 * 
	 * @param key
	 *            存储键
	 * @param value
	 *            存储值
	 */
	public void setInt(String key, int value) {
		sp.edit().putInt(key, value).commit();
	}

	/**
	 * 存储long值
	 * 
	 * @param key
	 *            存储键
	 * @param value
	 *            存储值long类型
	 */
	public void setLong(String key, long value) {
		sp.edit().putLong(key, value).commit();
	}

	/**
	 * 存储float值
	 * 
	 * @param key
	 *            存储键
	 * @param value
	 *            存储值float类型
	 */
	public void setFloat(String key, float value) {
		sp.edit().putFloat(key, value).commit();
	}


	/**
	 * 存储boolean值
	 * 
	 * @param key
	 *            存储键
	 * @param value
	 *            存储值 boolean类型
	 */
	public void setBoolean(String key, boolean value) {
		sp.edit().putBoolean(key, value).commit();
	}

	/**
	 * 取得对应key保存的String值
	 * 
	 * @param key
	 *            存储键
	 * @return String类型的值,如果没有则默认为空字符串 “”
	 */
	public String getString(String key) {
		return getString(key, "");
	}
	public String getString(String key,String v) {
		return sp.getString(key, v);
	}
	/**
	 * 取得对应key保存的int值
	 * 
	 * @param key
	 *            存储键
	 * @return int类型的值，如果没有则默认为 0 
	 */
	public int getInt(String key) {
		return getInt(key, 0);
	}
	public int getInt(String key,int i) {
		return sp.getInt(key, i);
	}
	/**
	 * 取得对应key保存的long值
	 * 
	 * @param key
	 *            存储键
	 * @return long类型的值，如果没有则默认为 -1
	 */
	public Long getLong(String key) {
		return getLong(key, -1);
	}
	public Long getLong(String key,long l) {
		return sp.getLong(key, l);
	}
	/**
	 * 取得对应key保存的float值
	 * 
	 * @param key
	 *            存储键
	 * @return float类型的值，如果没有则默认为 -1f
	 */
	public float getFloat(String key) {
		return getFloat(key, -1f);
	}
	public float getFloat(String key,float f) {
		return sp.getFloat(key, f);
	}
	/**
	 * 取得对应key保存的boolean值
	 * 
	 * @param key
	 *            存储键
	 * @return boolean类型的值，如果没有则默认为 false
	 */
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}
	public boolean getBoolean(String key,boolean flag) {
		return sp.getBoolean(key,flag);
	}

}
