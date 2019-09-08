package com.hp.roam.service;

import java.util.List;

import com.hp.roam.model.MqttUser;
import com.hp.roam.model.MqttUserExample;

/**
 * @author ck
 * @date 2019年6月6日 上午10:19:15
 */
public interface MqttUserService {
	
	List<MqttUser> selectByExample(MqttUserExample example);
	
	int insertSelective(MqttUser record);
	
	int batchInsert(List<MqttUser> mqttUsers);
}
