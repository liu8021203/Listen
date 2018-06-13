package com.ting.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ting.common.AppData;


/**
 * 文件管理 ： 读取，写入等操作
 */
public class UtilFileManage {

	private static String TAG = "UtilFileManage";

	
	/**
	 * 把数据写入文件
	 * 
	 * @param path
	 *            文件路径
	 * @param name
	 *            文件名称
	 * @param content
	 *            文件内容
	 */
	public static void writeSDData(String path, String name, String content) {
		try {
			if (content == null) {
				return;
			}
			File dfile = new File(path);
			if (!dfile.exists()) {
				dfile.mkdirs();
			}
			// 文件名加密
			File file = new File(dfile, name);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file, false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.newLine();
			bw.flush();
			fw.close();
		} catch (IOException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 读取本地文件数据
	 * 
	 * @param path
	 *            文件路径
	 * @param name
	 *            文件名
	 * @return String
	 */
	public static String readSDData(String path, String name) {
		// 字符编码(可解决中文乱码问题 )
		String encoding = "utf-8";
		File file = new File(path +"/" +  name);
		StringBuffer sb = new StringBuffer();
		if (file.exists() && file.isFile()) {
			try {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);
				BufferedReader br = new BufferedReader(read);
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
			} catch (UnsupportedEncodingException e) {
				Log.v(TAG, e.getMessage());
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				Log.v(TAG, e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				Log.v(TAG, e.getMessage());
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 删除文件
	 * ``111	`
	 * @param file
	 *            删除文件对象
	 */
	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				// file.delete();
				return;
			}
			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			// file.delete();
		}
	}

	/**
	 * 单一文件复制
	 * 
	 * @param oldpath
	 *            原始被复制文件路径
	 * @param newpath
	 *            新文件生成路径
	 */
	@SuppressWarnings("resource")
	public static void copyFile(String oldpath, String newpath) {
		File oldF = new File(oldpath);
		File newFile = new File(newpath);
		if (newFile.exists()) {
			return;
		}
		try {
			if (oldF.exists()) {
				int bytesum = 0;
				int byteread = 0;
				// 读入原文件
				InputStream inStream = new FileInputStream(oldpath);
				
				FileOutputStream fs = new FileOutputStream(newpath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					// System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				fs.close();
				inStream.close();
				oldF.delete();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.v(TAG, "copyFile");
		} catch (IOException e) {
			e.printStackTrace();
			Log.v(TAG, "copyFile");
		}
	}

	/**
	 * 多文件复制
	 * 
	 * @param oldpath
	 *            原始文件路径
	 * @param newpath
	 *            新文件路径
	 */
	public static void copyFolder(String oldpath, String newpath) {
		// 如果文件夹不存在 则建立新文件夹
		try {
			File newFile = new File(newpath);
			if (!newFile.exists()) {
				newFile.mkdirs();
			}
			File oldF = new File(oldpath);
			String[] file = oldF.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldpath.endsWith(File.separator)) {
					temp = new File(oldpath + file[i]);
				} else {
					temp = new File(oldpath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newpath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 2];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				// 如果是子文件夹
				if (temp.isDirectory()) {
					copyFolder(oldpath + "/" + file[i], newpath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			Log.v(TAG, "copyFolder");
			e.printStackTrace();
		}

	}

	public static List<String> getEmojiFile(Context context) {
		try {
			List<String> list = new ArrayList<String>();
			InputStream in = context.getResources().getAssets().open("emoji");
			BufferedReader br = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			String str = null;
			while ((str = br.readLine()) != null) {
				list.add(str);
			}

			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取文件夹大小
	 * 
	 * @param file
	 *            File实例
	 * @return long 单位为M
	 * @throws Exception
	 */
	public static double getFolderSize(File file) throws Exception {

		if (!file.exists()) {
			file.mkdirs();
		}
		double size = 0;
		File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				size = size + getFolderSize(fileList[i]);
			} else {
				size = size + fileList[i].length();
			}
		}
		return size;
		// return size * 1.0 / 1048576;
	}

	/**
	 * 删除指定目录下文件及目录
	 * 
	 * @param deleteThisPath
	 * @param filepath
	 * @return
	 */
	public static void deleteFolderFile(String filePath) {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			if(!file.exists())
			{
				return;
			}
			if (file.isDirectory()) {// 处理目录
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFolderFile(files[i].getAbsolutePath());
				}
			}
			file.delete();
//			if (deleteThisPath) {
//				if (!file.isDirectory()) {// 如果是文件，删除
//					file.delete();
//				} else {// 目录
//					if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
//						file.delete();
//					}
//				}
//			}
		}
	}
	/**
	 * 将文件大小转换成字符串
	 * 
	 * @param size
	 * @return
	 */
	public static String fileSizeFormat(double size) {
		double filesize = ((int) ((size / (1024 * 1024 * 1.0)) * 100)) / 100.0;
		return (filesize + " M");
	}




	public static File createTempFile(String namePart, int byteSize) {
		try {
			File f = File.createTempFile(namePart, "_handled", new File(AppData.PATH));
			FileOutputStream fos = new FileOutputStream(f);
			Random r = new Random();
			byte[] buffer = new byte[byteSize];
			r.nextBytes(buffer);
			fos.write(buffer);
			fos.flush();
			fos.close();
			return f;
		} catch (Throwable t) {
			Log.e("d", "createTempFile failed", t);
		}
		return null;
	}
}
