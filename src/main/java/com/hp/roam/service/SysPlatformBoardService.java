package com.hp.roam.service;

import java.util.List;

import com.hp.roam.model.RecordRequest;
import com.hp.roam.model.SysPlatformBoard;
import com.hp.roam.model.SysPlatformBoardExample;

/**
 * @author ck
 * @date 2019年5月13日 上午11:40:07
 */
public interface SysPlatformBoardService {

	long countByExample(SysPlatformBoardExample example);
	
	List<SysPlatformBoard> selectByExample(SysPlatformBoardExample example);
	
	List<SysPlatformBoard> selectPage(RecordRequest recordRequest);
}
