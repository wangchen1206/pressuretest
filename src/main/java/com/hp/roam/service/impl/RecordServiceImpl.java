package com.hp.roam.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hp.roam.dao.RecordMapper;
import com.hp.roam.model.ReceiveMsg;
import com.hp.roam.model.ReceiveMsgExample;
import com.hp.roam.model.ReceiveMsgExample.Criteria;
import com.hp.roam.model.Record;
import com.hp.roam.model.RecordExample;
import com.hp.roam.model.RecordRequest;
import com.hp.roam.service.ReceiveMsgService;
import com.hp.roam.service.RecordService;
import com.hp.roam.util.HttpUtils;

/**
 * @author ck
 * @date 2019年4月29日 上午11:56:47
 */
@Service
@Transactional
public class RecordServiceImpl implements RecordService{
	
	//这个记录需从数据库中查出来
	private static String[] Boards = {"board20", "board21", "board22",
			"board23", "board24", "board25", "board26", "board27", "board28",
			"board29"};
	
	private static int Duration = 120;

	@Autowired
	private RecordMapper recordMapper;
	
	@Autowired
	private ReceiveMsgService receiveMsgService;
	
	@Override
	public int insert(Record record) {
		return recordMapper.insert(record);
	}

	@Override
	public int batchInsert(List<Record> records) {
		return recordMapper.batchInsert(records);
	}

	/**
	 * 获取状态。
	 */
	@Override
	public List<Record> getRecords(RecordRequest recordRequest) {
		LocalDateTime startTime = recordRequest.getStartTime();
		LocalDateTime endTime = recordRequest.getEndTime();
		long start = System.currentTimeMillis();
		ReceiveMsgExample receiveMsgExample = new ReceiveMsgExample();
		receiveMsgExample.setOrderByClause("receive_time desc");
		Criteria receiveMsgCriteria = receiveMsgExample.createCriteria();
		// 因为京东云mysql存如数据库的receive_time比现在少13小时，所以查询数据库的时间 减去 13小时
		receiveMsgCriteria.andReceive_timeBetween(
				startTime.minusHours(13),
				endTime.minusHours(13));
		List<ReceiveMsg> receiveMsgs = receiveMsgService
				.selectByExample(receiveMsgExample);
		long end = System.currentTimeMillis();
		HttpUtils.logger.info("查询时间： " + (end - start));
		
		List<String> usernames = new ArrayList<>();
		// 先找出 这段时间内有在线的username
		for (int i = 0; i < receiveMsgs.size(); i++) {
			//receive_time时间加13个小时
			receiveMsgs.get(i).setReceive_time(receiveMsgs.get(i).getReceive_time().plusHours(13));
			if (!usernames.contains(receiveMsgs.get(i).getUsername())) {
				usernames.add(receiveMsgs.get(i).getUsername());
			}
		}
		

		// Boards中不在usernames中的 这段时间内状态都是掉线。
		List<String> usernameOffline = new ArrayList<>();
		List<Record> recordsOffline = new ArrayList<>();
		for (int i = 0; i < Boards.length; i++) {
			if (!usernames.contains(Boards[i])) {
				usernameOffline.add(Boards[i]);
				Record record = new Record();
				record.setDeviceId(Boards[i]);
				record.setStartTime(startTime);
				record.setEndTime(endTime);
				record.setRecordDate(toDateString(startTime));
				record.setType(0);
				recordsOffline.add(record);
			}
		}
		// recordService.batchInsert(records);
		// 开始分析 在线的 中间掉线的 区间
		// 先分类，与usernames中的 匹配，然后在筛选 其中的offline时间段。((i+1)-i)>16->
		// receive_time相减得出时间段
		//比较各个board第一条消息的receive_time与endTime相比得出这个时间段内的状态，各个board最后一条消息的receive_time与startTime相比得出这个时间段内的状态。小于16s->online；大于16s->offline
		// 分类
		List<List<ReceiveMsg>> lists = new ArrayList<>();
		for (int i = 0; i < usernames.size(); i++) {
			List<ReceiveMsg> receiveMsgList = new ArrayList<>();
			for (int j = 0; j < receiveMsgs.size(); j++) {
				if (usernames.get(i).equals(receiveMsgs.get(j).getUsername())) {
					receiveMsgList.add(receiveMsgs.get(j));
				}
			}
			lists.add(receiveMsgList);
		}

		
		//1.startTime至endTime 中未出现过offline 全部都为online
		
		Map<String, List<Record>> offlineDeviceRecords = new HashMap<>();
		// 筛选offline
		for (int i = 0; i < lists.size(); i++) {
			List<Record> deviceRecordsOffline = new ArrayList<>();
			String deviceId = lists.get(i).get(0).getUsername();
			for (int j = 0; j < lists.get(i).size(); j++) {
				//endTime 与第一条msg相比
				if(j == 0 &&(endTime.toEpochSecond(ZoneOffset.of("+8"))-lists.get(i).get(j).getReceive_time()
						.toEpochSecond(ZoneOffset.of("+8"))>Duration)){
					Record record = new Record();
					record.setDeviceId(lists.get(i).get(j).getUsername());
					record.setRecordDate(toDateString(startTime));
					record.setStartTime(
							lists.get(i).get(j).getReceive_time());
					record.setEndTime(endTime);
					record.setType(0);
					deviceRecordsOffline.add(record);
				}
				
				//startTime与最后一条msg相比
				if(j == lists.get(i).size()-1 && (lists.get(i).get(j).getReceive_time()
						.toEpochSecond(ZoneOffset.of("+8"))-startTime.toEpochSecond(ZoneOffset.of("+8"))>Duration)){
					Record record = new Record();
					record.setDeviceId(lists.get(i).get(j).getUsername());
					record.setRecordDate(toDateString(startTime));
					record.setStartTime(startTime);
					record.setEndTime(lists.get(i).get(j).getReceive_time());
					record.setType(0);
					deviceRecordsOffline.add(record);
				}
				
				if (j < lists.get(i).size()-1 && (lists.get(i).get(j).getReceive_time()
						.toEpochSecond(ZoneOffset.of("+8"))
						- lists.get(i).get(j + 1).getReceive_time()
								.toEpochSecond(ZoneOffset.of("+8"))) > Duration) {
					Record record = new Record();
					record.setDeviceId(lists.get(i).get(j).getUsername());
					record.setRecordDate(toDateString(startTime));
					record.setStartTime(
							lists.get(i).get(j + 1).getReceive_time());
					record.setEndTime(lists.get(i).get(j).getReceive_time());
					record.setType(0);
					deviceRecordsOffline.add(record);
				}
			}
			offlineDeviceRecords.put(deviceId, deviceRecordsOffline);
		}
		
		List<Record> recordsOnlineMerged = new ArrayList<>();
		List<Record> records = new ArrayList<>();
		//通过offline筛选出online Record
		
		for(String deviceId:offlineDeviceRecords.keySet()){
			HttpUtils.logger.info(deviceId+"----"+offlineDeviceRecords.get(deviceId).size());
			records.addAll(offlineDeviceRecords.get(deviceId));
			
			//如果offlineDeviceRecords.get(deviceId).size == 0 说明 在startTime~endTime之间 都是online
			if(offlineDeviceRecords.get(deviceId).size() == 0){
				Record record = new Record();
				record.setDeviceId(deviceId);
				record.setRecordDate(toDateString(startTime));
				record.setStartTime(startTime);
				record.setEndTime(endTime);
				record.setType(1);
				recordsOnlineMerged.add(record);
			}else{
				for (int k2 = 0; k2 < offlineDeviceRecords.get(deviceId).size(); k2++) {
					if(k2 == 0 && offlineDeviceRecords.get(deviceId).get(k2).getEndTime().isBefore(endTime)){
						Record record = new Record();
						record.setDeviceId(offlineDeviceRecords.get(deviceId).get(k2).getDeviceId());
						record.setRecordDate(offlineDeviceRecords.get(deviceId).get(k2).getRecordDate());
						record.setStartTime(offlineDeviceRecords.get(deviceId).get(k2).getEndTime());
						record.setEndTime(endTime);
						record.setType(1);
						recordsOnlineMerged.add(record);
					}
					
					if(k2 == offlineDeviceRecords.get(deviceId).size()-1 && offlineDeviceRecords.get(deviceId).get(offlineDeviceRecords.get(deviceId).size()-1).getStartTime().isAfter(startTime)){
						Record record = new Record();
						record.setDeviceId(offlineDeviceRecords.get(deviceId).get(k2).getDeviceId());
						record.setRecordDate(offlineDeviceRecords.get(deviceId).get(k2).getRecordDate());
						record.setStartTime(startTime);
						record.setEndTime(offlineDeviceRecords.get(deviceId).get(k2).getStartTime());
						record.setType(1);
						recordsOnlineMerged.add(record);
					}
					
					if(k2 < offlineDeviceRecords.get(deviceId).size()-1 && offlineDeviceRecords.get(deviceId).get(k2).getStartTime().isAfter(offlineDeviceRecords.get(deviceId).get(k2+1).getEndTime())){
						Record record = new Record();
						record.setDeviceId(offlineDeviceRecords.get(deviceId).get(k2).getDeviceId());
						record.setRecordDate(offlineDeviceRecords.get(deviceId).get(k2).getRecordDate());
						record.setStartTime(offlineDeviceRecords.get(deviceId).get(k2+1).getEndTime());
						record.setEndTime(offlineDeviceRecords.get(deviceId).get(k2).getStartTime());
						record.setType(1);
						recordsOnlineMerged.add(record);
					}
				}
			}
			
		}
		
		for (int k = 0; k < recordsOnlineMerged.size(); k++) {
			HttpUtils.logger.info("online---"+recordsOnlineMerged.get(k));
		}
		HttpUtils.logger.info("OnlineList size is "+recordsOnlineMerged.size());
		//添加在查询的时间范围内都是offline的Record
		records.addAll(recordsOffline);
		records.addAll(recordsOnlineMerged);
		return records;
	}
	
	/**
	 * 获取状态。
	 */
	@Override
	public List<Record> getRecordsByDeviceIds(RecordRequest recordRequest) {
		LocalDateTime startTime = recordRequest.getStartTime();
		LocalDateTime endTime = recordRequest.getEndTime();
		long start = System.currentTimeMillis();
		ReceiveMsgExample receiveMsgExample = new ReceiveMsgExample();
		receiveMsgExample.setOrderByClause("receive_time desc");
		Criteria receiveMsgCriteria = receiveMsgExample.createCriteria();
		// 因为京东云mysql存如数据库的receive_time比现在少13小时，所以查询数据库的时间 减去 13小时
		receiveMsgCriteria.andReceive_timeBetween(
				startTime.minusHours(13),
				endTime.minusHours(13))
				.andUsernameIn(recordRequest.getDeviceIds());//根据usernames 来查询记录
		List<ReceiveMsg> receiveMsgs = receiveMsgService
				.selectByExample(receiveMsgExample);
		long end = System.currentTimeMillis();
		HttpUtils.logger.info("查询时间： " + (end - start));
		
		List<String> usernames = new ArrayList<>();
		// 先找出 这段时间内有在线的username
		for (int i = 0; i < receiveMsgs.size(); i++) {
			//receive_time时间加13个小时
			receiveMsgs.get(i).setReceive_time(receiveMsgs.get(i).getReceive_time().plusHours(13));
			if (!usernames.contains(receiveMsgs.get(i).getUsername())) {
				usernames.add(receiveMsgs.get(i).getUsername());
			}
		}
		

		// deviceIds中不在usernames中的 这段时间内状态都是掉线。
		List<Record> recordsOffline = new ArrayList<>();
		for (int i = 0; i < recordRequest.getDeviceIds().size(); i++) {
			if (!usernames.contains(recordRequest.getDeviceIds().get(i))) {
				Record record = new Record();
				record.setDeviceId(recordRequest.getDeviceIds().get(i));
				record.setStartTime(startTime);
				record.setEndTime(endTime);
				record.setRecordDate(toDateString(startTime));
				record.setType(0);
				recordsOffline.add(record);
			}
		}
		// recordService.batchInsert(records);
		// 开始分析 在线的 中间掉线的 区间
		// 先分类，与usernames中的 匹配，然后在筛选 其中的offline时间段。((i+1)-i)>16->
		// receive_time相减得出时间段
		//比较各个board第一条消息的receive_time与endTime相比得出这个时间段内的状态，各个board最后一条消息的receive_time与startTime相比得出这个时间段内的状态。小于16s->online；大于16s->offline
		// 分类
		List<List<ReceiveMsg>> lists = new ArrayList<>();
		for (int i = 0; i < usernames.size(); i++) {
			List<ReceiveMsg> receiveMsgList = new ArrayList<>();
			for (int j = 0; j < receiveMsgs.size(); j++) {
				if (usernames.get(i).equals(receiveMsgs.get(j).getUsername())) {
					receiveMsgList.add(receiveMsgs.get(j));
				}
			}
			lists.add(receiveMsgList);
		}

		
		//1.startTime至endTime 中未出现过offline 全部都为online
		
		Map<String, List<Record>> offlineDeviceRecords = new HashMap<>();
		// 筛选offline
		for (int i = 0; i < lists.size(); i++) {
			List<Record> deviceRecordsOffline = new ArrayList<>();
			String deviceId = lists.get(i).get(0).getUsername();
			for (int j = 0; j < lists.get(i).size(); j++) {
				//endTime 与第一条msg相比
				if(j == 0 &&(endTime.toEpochSecond(ZoneOffset.of("+8"))-lists.get(i).get(j).getReceive_time()
						.toEpochSecond(ZoneOffset.of("+8"))>Duration)){
					Record record = new Record();
					record.setDeviceId(lists.get(i).get(j).getUsername());
					record.setRecordDate(toDateString(startTime));
					record.setStartTime(
							lists.get(i).get(j).getReceive_time());
					record.setEndTime(endTime);
					record.setType(0);
					deviceRecordsOffline.add(record);
				}
				
				//startTime与最后一条msg相比
				if(j == lists.get(i).size()-1 && (lists.get(i).get(j).getReceive_time()
						.toEpochSecond(ZoneOffset.of("+8"))-startTime.toEpochSecond(ZoneOffset.of("+8"))>Duration)){
					Record record = new Record();
					record.setDeviceId(lists.get(i).get(j).getUsername());
					record.setRecordDate(toDateString(startTime));
					record.setStartTime(startTime);
					record.setEndTime(lists.get(i).get(j).getReceive_time());
					record.setType(0);
					deviceRecordsOffline.add(record);
				}
				
				if (j < lists.get(i).size()-1 && (lists.get(i).get(j).getReceive_time()
						.toEpochSecond(ZoneOffset.of("+8"))
						- lists.get(i).get(j + 1).getReceive_time()
								.toEpochSecond(ZoneOffset.of("+8"))) > Duration) {
					Record record = new Record();
					record.setDeviceId(lists.get(i).get(j).getUsername());
					record.setRecordDate(toDateString(startTime));
					record.setStartTime(
							lists.get(i).get(j + 1).getReceive_time());
					record.setEndTime(lists.get(i).get(j).getReceive_time());
					record.setType(0);
					deviceRecordsOffline.add(record);
				}
			}
			offlineDeviceRecords.put(deviceId, deviceRecordsOffline);
		}
		
		List<Record> recordsOnlineMerged = new ArrayList<>();
		List<Record> records = new ArrayList<>();
		//通过offline筛选出online Record
		
		for(String deviceId:offlineDeviceRecords.keySet()){
			HttpUtils.logger.info(deviceId+"----"+offlineDeviceRecords.get(deviceId).size());
			records.addAll(offlineDeviceRecords.get(deviceId));
			
			//如果offlineDeviceRecords.get(deviceId).size == 0 说明 在startTime~endTime之间 都是online
			if(offlineDeviceRecords.get(deviceId).size() == 0){
				Record record = new Record();
				record.setDeviceId(deviceId);
				record.setRecordDate(toDateString(startTime));
				record.setStartTime(startTime);
				record.setEndTime(endTime);
				record.setType(1);
				recordsOnlineMerged.add(record);
			}else{
				for (int k2 = 0; k2 < offlineDeviceRecords.get(deviceId).size(); k2++) {
					if(k2 == 0 && offlineDeviceRecords.get(deviceId).get(k2).getEndTime().isBefore(endTime)){
						Record record = new Record();
						record.setDeviceId(offlineDeviceRecords.get(deviceId).get(k2).getDeviceId());
						record.setRecordDate(offlineDeviceRecords.get(deviceId).get(k2).getRecordDate());
						record.setStartTime(offlineDeviceRecords.get(deviceId).get(k2).getEndTime());
						record.setEndTime(endTime);
						record.setType(1);
						recordsOnlineMerged.add(record);
					}
					
					if(k2 == offlineDeviceRecords.get(deviceId).size()-1 && offlineDeviceRecords.get(deviceId).get(offlineDeviceRecords.get(deviceId).size()-1).getStartTime().isAfter(startTime)){
						Record record = new Record();
						record.setDeviceId(offlineDeviceRecords.get(deviceId).get(k2).getDeviceId());
						record.setRecordDate(offlineDeviceRecords.get(deviceId).get(k2).getRecordDate());
						record.setStartTime(startTime);
						record.setEndTime(offlineDeviceRecords.get(deviceId).get(k2).getStartTime());
						record.setType(1);
						recordsOnlineMerged.add(record);
					}
					
					if(k2 < offlineDeviceRecords.get(deviceId).size()-1 && offlineDeviceRecords.get(deviceId).get(k2).getStartTime().isAfter(offlineDeviceRecords.get(deviceId).get(k2+1).getEndTime())){
						Record record = new Record();
						record.setDeviceId(offlineDeviceRecords.get(deviceId).get(k2).getDeviceId());
						record.setRecordDate(offlineDeviceRecords.get(deviceId).get(k2).getRecordDate());
						record.setStartTime(offlineDeviceRecords.get(deviceId).get(k2+1).getEndTime());
						record.setEndTime(offlineDeviceRecords.get(deviceId).get(k2).getStartTime());
						record.setType(1);
						recordsOnlineMerged.add(record);
					}
				}
			}
			
		}
		
		for (int k = 0; k < recordsOnlineMerged.size(); k++) {
			HttpUtils.logger.info("online---"+recordsOnlineMerged.get(k));
		}
		HttpUtils.logger.info("OnlineList size is "+recordsOnlineMerged.size());
		//添加在查询的时间范围内都是offline的Record
		records.addAll(recordsOffline);
		records.addAll(recordsOnlineMerged);
		return records;
	}
	
	/**
	 * 获取单个设备当天的状态Records
	 * @param recordRequest
	 * @return
	 */
	public List<Record> getRecordsByDeviceIdAndDate(RecordRequest recordRequest){
		LocalDateTime startTime = recordRequest.getStartTime();
		LocalDateTime endTime = recordRequest.getEndTime();
		String deviceId = recordRequest.getDeviceId();
		List<Record> records = new ArrayList<>();
		long start = System.currentTimeMillis();
		ReceiveMsgExample receiveMsgExample = new ReceiveMsgExample();
		receiveMsgExample.setOrderByClause("receive_time desc");
		Criteria receiveMsgCriteria = receiveMsgExample.createCriteria();
		// 因为京东云mysql存如数据库的receive_time比现在少13小时，所以查询数据库的时间 减去 13小时
		receiveMsgCriteria.andReceive_timeBetween(
				startTime.minusHours(13),
				endTime.minusHours(13))
				.andUsernameEqualTo(deviceId);
		List<ReceiveMsg> receiveMsgs = receiveMsgService
				.selectByExample(receiveMsgExample);
		long end = System.currentTimeMillis();
		HttpUtils.logger.info("查询时间： " + (end - start));
		
		List<String> usernames = new ArrayList<>();
		usernames.add(deviceId);
		
		
		if(receiveMsgs !=null && receiveMsgs.size()!=0){
			for (int i = 0; i < receiveMsgs.size(); i++) {
				//receive_time时间加13个小时
				receiveMsgs.get(i).setReceive_time(receiveMsgs.get(i).getReceive_time().plusHours(13));
			}
			
			// recordService.batchInsert(records);
			// 开始分析 在线的 中间掉线的 区间
			// 先分类，与usernames中的 匹配，然后在筛选 其中的offline时间段。((i+1)-i)>16->
			// receive_time相减得出时间段
			//比较各个board第一条消息的receive_time与endTime相比得出这个时间段内的状态，各个board最后一条消息的receive_time与startTime相比得出这个时间段内的状态。小于16s->online；大于16s->offline
			// 分类
			List<List<ReceiveMsg>> lists = new ArrayList<>();
			for (int i = 0; i < usernames.size(); i++) {
				List<ReceiveMsg> receiveMsgList = new ArrayList<>();
				for (int j = 0; j < receiveMsgs.size(); j++) {
					if (usernames.get(i).equals(receiveMsgs.get(j).getUsername())) {
						receiveMsgList.add(receiveMsgs.get(j));
					}
				}
				lists.add(receiveMsgList);
			}
			
			
			//1.startTime至endTime 中未出现过offline 全部都为online
			
			Map<String, List<Record>> offlineDeviceRecords = new HashMap<>();
			// 筛选offline
			for (int i = 0; i < lists.size(); i++) {
				List<Record> deviceRecordsOffline = new ArrayList<>();
				String deviceId1 = lists.get(i).get(0).getUsername();
				for (int j = 0; j < lists.get(i).size(); j++) {
					//endTime 与第一条msg相比
					if(j == 0 &&(endTime.toEpochSecond(ZoneOffset.of("+8"))-lists.get(i).get(j).getReceive_time()
							.toEpochSecond(ZoneOffset.of("+8"))>Duration)){
						Record record = new Record();
						record.setDeviceId(lists.get(i).get(j).getUsername());
						record.setRecordDate(toDateString(startTime));
						record.setStartTime(
								lists.get(i).get(j).getReceive_time());
						record.setEndTime(endTime);
						record.setType(0);
						deviceRecordsOffline.add(record);
					}
					
					//startTime与最后一条msg相比
					if(j == lists.get(i).size()-1 && (lists.get(i).get(j).getReceive_time()
							.toEpochSecond(ZoneOffset.of("+8"))-startTime.toEpochSecond(ZoneOffset.of("+8"))>Duration)){
						Record record = new Record();
						record.setDeviceId(lists.get(i).get(j).getUsername());
						record.setRecordDate(toDateString(startTime));
						record.setStartTime(startTime);
						record.setEndTime(lists.get(i).get(j).getReceive_time());
						record.setType(0);
						deviceRecordsOffline.add(record);
					}
					
					if (j < lists.get(i).size()-1 && (lists.get(i).get(j).getReceive_time()
							.toEpochSecond(ZoneOffset.of("+8"))
							- lists.get(i).get(j + 1).getReceive_time()
							.toEpochSecond(ZoneOffset.of("+8"))) > Duration) {
						Record record = new Record();
						record.setDeviceId(lists.get(i).get(j).getUsername());
						record.setRecordDate(toDateString(startTime));
						record.setStartTime(
								lists.get(i).get(j + 1).getReceive_time());
						record.setEndTime(lists.get(i).get(j).getReceive_time());
						record.setType(0);
						deviceRecordsOffline.add(record);
					}
				}
				offlineDeviceRecords.put(deviceId1, deviceRecordsOffline);
			}
			
			List<Record> recordsOnlineMerged = new ArrayList<>();
			//通过offline筛选出online Record
			
			for(String deviceId1:offlineDeviceRecords.keySet()){
				HttpUtils.logger.info(deviceId1+"----"+offlineDeviceRecords.get(deviceId1).size());
				records.addAll(offlineDeviceRecords.get(deviceId1));
				
				//如果offlineDeviceRecords.get(deviceId).size == 0 说明 在startTime~endTime之间 都是online
				if(offlineDeviceRecords.get(deviceId1).size() == 0){
					Record record = new Record();
					record.setDeviceId(deviceId1);
					record.setRecordDate(toDateString(startTime));
					record.setStartTime(startTime);
					record.setEndTime(endTime);
					record.setType(1);
					recordsOnlineMerged.add(record);
				}else{
					for (int k2 = 0; k2 < offlineDeviceRecords.get(deviceId1).size(); k2++) {
						if(k2 == 0 && offlineDeviceRecords.get(deviceId1).get(k2).getEndTime().isBefore(endTime)){
							Record record = new Record();
							record.setDeviceId(offlineDeviceRecords.get(deviceId1).get(k2).getDeviceId());
							record.setRecordDate(offlineDeviceRecords.get(deviceId1).get(k2).getRecordDate());
							record.setStartTime(offlineDeviceRecords.get(deviceId1).get(k2).getEndTime());
							record.setEndTime(endTime);
							record.setType(1);
							recordsOnlineMerged.add(record);
						}
						
						if(k2 == offlineDeviceRecords.get(deviceId1).size()-1 && offlineDeviceRecords.get(deviceId1).get(offlineDeviceRecords.get(deviceId1).size()-1).getStartTime().isAfter(startTime)){
							Record record = new Record();
							record.setDeviceId(offlineDeviceRecords.get(deviceId1).get(k2).getDeviceId());
							record.setRecordDate(offlineDeviceRecords.get(deviceId1).get(k2).getRecordDate());
							record.setStartTime(startTime);
							record.setEndTime(offlineDeviceRecords.get(deviceId1).get(k2).getStartTime());
							record.setType(1);
							recordsOnlineMerged.add(record);
						}
						
						if(k2 < offlineDeviceRecords.get(deviceId1).size()-1 && offlineDeviceRecords.get(deviceId1).get(k2).getStartTime().isAfter(offlineDeviceRecords.get(deviceId1).get(k2+1).getEndTime())){
							Record record = new Record();
							record.setDeviceId(offlineDeviceRecords.get(deviceId1).get(k2).getDeviceId());
							record.setRecordDate(offlineDeviceRecords.get(deviceId1).get(k2).getRecordDate());
							record.setStartTime(offlineDeviceRecords.get(deviceId1).get(k2+1).getEndTime());
							record.setEndTime(offlineDeviceRecords.get(deviceId1).get(k2).getStartTime());
							record.setType(1);
							recordsOnlineMerged.add(record);
						}
					}
				}
				
			}
			
			for (int k = 0; k < recordsOnlineMerged.size(); k++) {
				HttpUtils.logger.info("online---"+recordsOnlineMerged.get(k));
			}
			HttpUtils.logger.info("OnlineList size is "+recordsOnlineMerged.size());
			records.addAll(recordsOnlineMerged);
		}else{
			Record record = new Record();
			record.setDeviceId(deviceId);
			record.setRecordDate(toDateString(startTime));
			record.setStartTime(startTime);
			record.setEndTime(endTime);
			record.setType(0);
			records.add(record);
		}
		return records;
	}
	
	/**
	 * @param date
	 * @return
	 */
	private String toDateString(LocalDateTime date) {
		return date.toLocalDate().toString().replace("-", "");
	}

	@SuppressWarnings("static-access")
	public LocalDateTime toStartDate(LocalDateTime date) {

		LocalDateTime localDateTime = LocalDateTime.now();
		localDateTime = localDateTime.of(date.getYear(), date.getMonth(),
				date.getDayOfMonth(), 0, 0, 0);
		return localDateTime;
	}

	@SuppressWarnings("static-access")
	public LocalDateTime toEndDate(LocalDateTime date) {

		LocalDateTime localDateTime = LocalDateTime.now();
		localDateTime = localDateTime.of(date.getYear(), date.getMonth(),
				date.getDayOfMonth(), 23, 59, 59);
		return localDateTime;
	}

	
	
	@Override
	public List<Record> selectByExample(RecordExample example) {
		return recordMapper.selectByExample(example);
	}
	
}
