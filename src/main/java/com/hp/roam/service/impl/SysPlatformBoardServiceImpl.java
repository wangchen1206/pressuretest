package com.hp.roam.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.hp.roam.dao.SysPlatformBoardMapper;
import com.hp.roam.model.RecordRequest;
import com.hp.roam.model.SysPlatformBoard;
import com.hp.roam.model.SysPlatformBoardExample;
import com.hp.roam.service.SysPlatformBoardService;

/**
 * @author ck
 * @date 2019年5月13日 上午11:41:08
 */
@Service
@Transactional
public class SysPlatformBoardServiceImpl implements SysPlatformBoardService{

	@Autowired
	private SysPlatformBoardMapper sysPlatformBoardMapper;

	@Override
	public List<SysPlatformBoard> selectByExample(
			SysPlatformBoardExample example) {
		return sysPlatformBoardMapper.selectByExample(example);
	}

	/* (non-Javadoc)
	 * @see com.hp.roam.service.SysPlatformBoardService#countByExample(com.hp.roam.model.SysPlatformBoardExample)
	 */
	@Override
	public long countByExample(SysPlatformBoardExample example) {
		return sysPlatformBoardMapper.countByExample(example);
	}

	@Override
	public List<SysPlatformBoard> selectPage(RecordRequest recordRequest) {
		PageHelper.startPage(recordRequest.getPageNum(), recordRequest.getPageSize());
		return sysPlatformBoardMapper.selectAllAsc();
	}
}
