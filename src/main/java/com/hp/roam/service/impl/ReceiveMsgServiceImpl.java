package com.hp.roam.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hp.roam.dao.ReceiveMsgMapper;
import com.hp.roam.model.ReceiveMsg;
import com.hp.roam.model.ReceiveMsgExample;
import com.hp.roam.service.ReceiveMsgService;

/**
 * @author ck
 * @date 2019年4月29日 下午3:06:37
 */
@Service
@Transactional
public class ReceiveMsgServiceImpl implements ReceiveMsgService{

	@Autowired
	private ReceiveMsgMapper receiveMsgMapper;

	/* (non-Javadoc)
	 * @see com.hp.roam.service.ReceiveMsgService#selectByExample(com.hp.roam.model.ReceiveMsgExample)
	 */
	@Override
	public List<ReceiveMsg> selectByExample(ReceiveMsgExample example) {
		return receiveMsgMapper.selectByExample(example);
	}

	
	
}
