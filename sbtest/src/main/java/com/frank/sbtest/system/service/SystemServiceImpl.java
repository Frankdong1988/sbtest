package com.frank.sbtest.system.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frank.sbtest.system.dao.SystemDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



@Service
public class SystemServiceImpl implements SystemService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SystemDao systemDao;
	
	public void testMapper() {
		List<Map> map = systemDao.testMapper();
		logger.info("数据库查询结束："+JSONArray.fromObject(map).toString());
	}

}
