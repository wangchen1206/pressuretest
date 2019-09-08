package com.hp.roam.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hp.roam.model.Record;
import com.hp.roam.model.RecordExample;

public interface RecordMapper {

	int batchInsert(@Param("records") List<Record> records);
	
    long countByExample(RecordExample example);

    int deleteByExample(RecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Record record);

    int insertSelective(Record record);

    List<Record> selectByExample(RecordExample example);

    Record selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Record record, @Param("example") RecordExample example);

    int updateByExample(@Param("record") Record record, @Param("example") RecordExample example);

    int updateByPrimaryKeySelective(Record record);

    int updateByPrimaryKey(Record record);
}