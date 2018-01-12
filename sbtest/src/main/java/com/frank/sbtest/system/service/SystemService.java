package com.frank.sbtest.system.service;

import org.springframework.transaction.annotation.Transactional;

public interface SystemService {
	
	@Transactional
	void testMapper();
}
