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

}
