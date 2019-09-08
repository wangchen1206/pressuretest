package com.hp.roam.dao;

import com.hp.roam.model.OnlineRate;
import com.hp.roam.model.OnlineRateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OnlineRateMapper {
	
	int batchInsert(@Param("onlineRates") List<OnlineRate> onlineRates);
	
    long countByExample(OnlineRateExample example);

    int deleteByExample(OnlineRateExample example);

    int insert(OnlineRate record);

    int insertSelective(OnlineRate record);

    List<OnlineRate> selectByExample(OnlineRateExample example);

    int updateByExampleSelective(@Param("record") OnlineRate record, @Param("example") OnlineRateExample example);

    int updateByExample(@Param("record") OnlineRate record, @Param("example") OnlineRateExample example);
}