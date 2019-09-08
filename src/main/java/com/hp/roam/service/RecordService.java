package com.hp.roam.service;

import java.util.List;

import com.hp.roam.model.Record;
import com.hp.roam.model.RecordExample;
import com.hp.roam.model.RecordRequest;

/**
 * @author ck
 * @date 2019年4月29日 上午11:56:31
 */
public interface RecordService {
	
	int insert(Record record);
	
	int batchInsert(List<Record> records);
	
	List<Record> getRecords(RecordRequest recordRequest);
	
	public List<Record> getRecordsByDeviceIdAndDate(RecordRequest recordRequest);
	
	List<Record> selectByExample(RecordExample example);

	/**
	 * @param recordRequest
	 * @return
	 */
	List<Record> getRecordsByDeviceIds(RecordRequest recordRequest);
}
