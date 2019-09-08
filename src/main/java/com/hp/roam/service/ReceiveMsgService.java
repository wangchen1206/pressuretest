package com.hp.roam.service;

import java.util.List;

import com.hp.roam.model.ReceiveMsg;
import com.hp.roam.model.ReceiveMsgExample;

/**
 * @author ck
 * @date 2019年4月29日 下午3:06:16
 */
public interface ReceiveMsgService {

	List<ReceiveMsg> selectByExample(ReceiveMsgExample example);
}
