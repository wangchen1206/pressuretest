package com.hp.roam.dao;

import com.hp.roam.model.SysPlatformBoard;
import com.hp.roam.model.SysPlatformBoardExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysPlatformBoardMapper {
	
	List<SysPlatformBoard> selectAllAsc();
	
    long countByExample(SysPlatformBoardExample example);

    int deleteByExample(SysPlatformBoardExample example);

    int deleteByPrimaryKey(String board_uuid);

    int insert(SysPlatformBoard record);

    int insertSelective(SysPlatformBoard record);

    List<SysPlatformBoard> selectByExample(SysPlatformBoardExample example);

    SysPlatformBoard selectByPrimaryKey(String board_uuid);

    int updateByExampleSelective(@Param("record") SysPlatformBoard record, @Param("example") SysPlatformBoardExample example);

    int updateByExample(@Param("record") SysPlatformBoard record, @Param("example") SysPlatformBoardExample example);

    int updateByPrimaryKeySelective(SysPlatformBoard record);

    int updateByPrimaryKey(SysPlatformBoard record);
}