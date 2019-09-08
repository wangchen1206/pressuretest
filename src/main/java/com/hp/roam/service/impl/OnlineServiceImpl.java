package com.hp.roam.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hp.roam.dao.OnlineRateMapper;
import com.hp.roam.model.OnlineRate;
import com.hp.roam.model.OnlineRateExample;
import com.hp.roam.service.OnlineRateService;

/**
 * @author ck
 * @date 2019年5月20日 下午2:46:49
 */
@Service
@Transactional
public class OnlineServiceImpl implements OnlineRateService{

	@Autowired
	private OnlineRateMapper onlineMapper;
	
	@Override
	public int insert(OnlineRate record) {
		return onlineMapper.insert(record);
	}

	@Override
	public List<OnlineRate> selectByExample(OnlineRateExample example) {
		return onlineMapper.selectByExample(example);
	}

	@Override
	public int batchInsert(List<OnlineRate> onlineRates) {
		return onlineMapper.batchInsert(onlineRates);
	}

}
