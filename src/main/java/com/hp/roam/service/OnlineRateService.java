package com.hp.roam.service;

import java.util.List;

import com.hp.roam.model.OnlineRate;
import com.hp.roam.model.OnlineRateExample;

/**
 * @author ck
 * @date 2019年5月20日 下午2:45:51
 */
public interface OnlineRateService {
	
	 int batchInsert(List<OnlineRate> onlineRates);
	
	 int insert(OnlineRate record);
	 
	 List<OnlineRate> selectByExample(OnlineRateExample example);
}
