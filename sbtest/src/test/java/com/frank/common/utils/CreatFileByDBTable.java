package com.frank.common.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class CreatFileByDBTable {
	/** 项目绝对路径 **/
	private String projectAbsolutelyPath = "C:\\develop\\Tools\\git\\gitHubRepository\\sbtest\\sbtest";
	private String projectPackagePath = "src\\main\\java\\com\\frank\\sbtest";
	private String modelName = "";
	private String dbName = "";
	private String tableName = "";
	
	
	
	
	
	/**
	 * 根据内容和路径生成文件
	 * @param str
	 * @param filePath
	 */
	private static void creatFile(StringBuilder str,String filePath) {
		File file = new File(filePath);
		System.out.println("file.isDirectory()："+file.isDirectory());
		System.out.println("file.isFile()："+file.isFile());
		if(file.exists()) {
			throw new RuntimeException("创建文件失败："+filePath+" 文件已经存在");
		}
		try {
			file.createNewFile();
		} catch (IOException e1) {
			System.out.println("创建空文件异常：");
			e1.printStackTrace();
			return;
		}
		if(!file.isFile()) {
			throw new RuntimeException("创建文件失败："+filePath+" 不是一个文件路径，格式不正确");
		}
		FileWriter filewrit = null;
		try {
			filewrit = new FileWriter(filePath);
		} catch (Exception e) {
			System.out.println("创建文件流异常：");
			e.printStackTrace();
			return;
		}
		try {
			filewrit.write(str.toString());
			filewrit.close();
		} catch (IOException e) {
			System.out.println("输出文件异常：");
			e.printStackTrace();
		}
		System.out.println("结束");
	}
	
	
	
}
