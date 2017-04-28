package com.camera;

import java.io.File;
import java.io.RandomAccessFile;

import android.util.Log;

public class FileUtilss extends File {


	
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileUtilss(String path) {
		super(path);
		// TODO Auto-generated constructor stub
	}

	// 将字符串写入到文本文件中
	public void writeTxtToFile(String strcontent, String filePath, String fileName) {
	    //生成文件夹之后，再生成文件，不然会出错
	    makeFilePath(filePath, fileName);
	    
	    String strFilePath = filePath+fileName;
	    // 每次写入时，都换行写
	    String strContent = strcontent + "\r\n";
	    try {
	        File file = new File(strFilePath);
	        if (file.exists()) {
	        	file.delete();
	            Log.d("jochen", "Create the file:" + strFilePath);
	            file.getParentFile().mkdirs();
	            file.createNewFile();
	        }
	        
	        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
	        raf.seek(0);
	        raf.write(strContent.getBytes());
	        raf.close();
	    } catch (Exception e) {
	        Log.e("jochen", "Error on write File:" + e);
	    }
	}
	 
	// 生成文件
	public File makeFilePath(String filePath, String fileName) {
	    File file = null;
	    makeRootDirectory(filePath);
	    try {
	        file = new File(filePath + fileName);
	        if (!file.exists()) {
	            file.createNewFile();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return file;
	}
	 
	// 生成文件夹
	public static void makeRootDirectory(String filePath) {
	    File file = null;
	    try {
	        file = new File(filePath);
	        if (!file.exists()) {
	            file.mkdir();
	        }
	    } catch (Exception e) {
	        Log.i("jochen:", e+"");
	    }
	}	

}
