package com.hp.roam.dao;

import com.hp.roam.model.Offline;
import com.hp.roam.model.OfflineExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OfflineMapper {
    long countByExample(OfflineExample example);

    int deleteByExample(OfflineExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Offline record);

    int insertSelective(Offline record);

    List<Offline> selectByExample(OfflineExample example);

    Offline selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Offline record, @Param("example") OfflineExample example);

    int updateByExample(@Param("record") Offline record, @Param("example") OfflineExample example);

    int updateByPrimaryKeySelective(Offline record);

    int updateByPrimaryKey(Offline record);
}