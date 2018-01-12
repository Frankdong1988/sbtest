package com.frank.sbtest.common.utils;

import java.util.Map;

import com.thoughtworks.xstream.XStream;

/**
 * 数据格式化工具类
 * @author dgf
 */
public class DataFormatUtil {
	
	/**
	 * bean对象转xml格式数据
	 * @param t 泛型对象(需要转xml格式的bean对象)
	 * @param map 需要替换的标签(key：替换后的标签，value：替换之前对象的class对象)
	 * 如：要转的Bean为user。未进行替换之前 转出来的第一级标签为：<com....pojo.User>...</com....pojo.User>
	 * 在map中进行如下map.put("xml",user.getClass);
	 * 得到的xml一级目录如下：<xml>...</xml> 
	 * @return
	 */
	public static <T> String beanToXml(T t,Map<String, Class> map) {
		XStream xmls = new XStream();
		if(null != map && !map.isEmpty()) {
			for(String key:map.keySet()) {
				xmls.alias(key,map.get(key));
			}
		}
		String xml = xmls.toXML(t);
		return xml;
	}
	

	public static void main(String[] args) {
		System.out.println(DataFormatUtil.class.getResource("").getPath());
	}
	
}
