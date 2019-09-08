package com.hp.roam.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hp.roam.dao.MqttUserMapper;
import com.hp.roam.model.MqttUser;
import com.hp.roam.model.MqttUserExample;
import com.hp.roam.service.MqttUserService;

/**
 * @author ck
 * @date 2019年6月6日 上午10:20:47
 */
@Service
@Transactional
public class MqttUserServiceImpl implements MqttUserService{

	@Autowired
	private MqttUserMapper mqttUserMapper;
	
	@Override
	public List<MqttUser> selectByExample(MqttUserExample example) {
		return mqttUserMapper.selectByExample(example);
	}

	@Override
	public int insertSelective(MqttUser record) {
		return mqttUserMapper.insertSelective(record);
	}

	@Override
	public int batchInsert(List<MqttUser> mqttUsers) {
		return mqttUserMapper.batchInsert(mqttUsers);
	}
	
}
