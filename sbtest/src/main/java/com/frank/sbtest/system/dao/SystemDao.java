package com.frank.sbtest.system.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


@Resource
public interface SystemDao {
	
	/**
	 * 测试数据层
	 */
	List<Map> testMapper();
	/**
	 * 添加
	 * @return
	 */
	Integer add();
	/**
	 * 修改
	 * @return
	 */
	Integer update();

}
