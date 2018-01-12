package com.frank.sbtest.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.frank.sbtest.common.interceptor.AuthorityInterceptor;
import com.frank.sbtest.common.interceptor.LoginInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	/**
	 * 添加自定义拦截器
	 * 添加的上下位置决定执行的顺序
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(new AuthorityInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}
}
