package com.hp.roam.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hp.roam.model.MqttUser;
import com.hp.roam.model.MqttUserExample;

public interface MqttUserMapper {
	
	int batchInsert(@Param("mqttUsers") List<MqttUser> mqttUsers);
	
    long countByExample(MqttUserExample example);

    int deleteByExample(MqttUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MqttUser record);
    

    int insertSelective(MqttUser record);

    List<MqttUser> selectByExample(MqttUserExample example);

    MqttUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MqttUser record, @Param("example") MqttUserExample example);

    int updateByExample(@Param("record") MqttUser record, @Param("example") MqttUserExample example);

    int updateByPrimaryKeySelective(MqttUser record);

    int updateByPrimaryKey(MqttUser record);
}