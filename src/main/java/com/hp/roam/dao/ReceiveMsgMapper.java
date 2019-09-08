package com.hp.roam.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hp.roam.model.ReceiveMsg;
import com.hp.roam.model.ReceiveMsgExample;

public interface ReceiveMsgMapper {
	
	List<ReceiveMsg> selectByReceiveTime(@Param("startDate") Date startDate,@Param("endDate") Date endDate);
	
    long countByExample(ReceiveMsgExample example);

    int deleteByExample(ReceiveMsgExample example);

    int deleteByPrimaryKey(String random_id);

    int insert(ReceiveMsg record);

    int insertSelective(ReceiveMsg record);

    List<ReceiveMsg> selectByExampleWithBLOBs(ReceiveMsgExample example);

    List<ReceiveMsg> selectByExample(ReceiveMsgExample example);

    ReceiveMsg selectByPrimaryKey(String random_id);

    int updateByExampleSelective(@Param("record") ReceiveMsg record, @Param("example") ReceiveMsgExample example);

    int updateByExampleWithBLOBs(@Param("record") ReceiveMsg record, @Param("example") ReceiveMsgExample example);

    int updateByExample(@Param("record") ReceiveMsg record, @Param("example") ReceiveMsgExample example);

    int updateByPrimaryKeySelective(ReceiveMsg record);

    int updateByPrimaryKeyWithBLOBs(ReceiveMsg record);

    int updateByPrimaryKey(ReceiveMsg record);
}