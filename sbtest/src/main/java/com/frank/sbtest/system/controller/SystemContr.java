package com.frank.sbtest.system.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.frank.sbtest.system.service.SystemService;

import net.sf.json.JSONObject;


@Controller
public class SystemContr {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SystemService systemService;
	
	@RequestMapping("/")
	@ResponseBody
	public String testMapper() {
		try {
			systemService.testMapper();
		}catch(Exception e) {
			logger.error("系统异常了：",e);
		}
		return "success";
	}
}
