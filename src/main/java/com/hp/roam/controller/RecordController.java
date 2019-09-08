package com.hp.roam.controller;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.roam.model.OnlineRate;
import com.hp.roam.model.OnlineRateExample;
import com.hp.roam.model.OnlineRateExample.Criteria;
import com.hp.roam.model.Record;
import com.hp.roam.model.RecordExample;
import com.hp.roam.model.RecordRequest;
import com.hp.roam.model.SysPlatformBoard;
import com.hp.roam.service.OnlineRateService;
import com.hp.roam.service.RecordService;
import com.hp.roam.service.SysPlatformBoardService;
import com.hp.roam.util.HttpUtils;

/**
 * @author ck
 * @date 2019年4月29日 上午11:58:12
 */
@Controller
public class RecordController {

	@Autowired
	private RecordService recordService;

	@Autowired
	private OnlineRateService onlineRateService;

	@Autowired
	private SysPlatformBoardService sysPlatformBoardService;

	/*
	 * private static String[] Boards = {"board20", "board21", "board22",
	 * "board23", "board24", "board25", "board26", "board27", "board28",
	 * "board29"};
	 * 
	 * private static int Duration = 16;
	 */

	@GetMapping("/{path}")
	public String singledevice(@PathVariable("path") String path) {
		return path;
	}

	@GetMapping("records")
	public String records() {
		return "records";
	}

	/*
	 * @GetMapping("recordsByDate1")
	 * 
	 * @ResponseBody public String
	 * getRecordsBySingleDay1(@RequestParam(required=false) LocalDateTime
	 * startTime,@RequestParam(required=false) LocalDateTime endTime) throws
	 * JsonProcessingException { long start = System.currentTimeMillis();
	 * ReceiveMsgExample receiveMsgExample = new ReceiveMsgExample();
	 * receiveMsgExample.setOrderByClause("receive_time desc"); Criteria
	 * receiveMsgCriteria = receiveMsgExample.createCriteria();
	 * receiveMsgCriteria.andReceive_timeBetween(startTime,endTime);
	 * List<ReceiveMsg> receiveMsgs = receiveMsgService
	 * .selectByExample(receiveMsgExample); long end =
	 * System.currentTimeMillis();
	 * 
	 * List<String> usernames = new ArrayList<>(); // 先找出 这段时间内有在线的username for
	 * (int i = 0; i < receiveMsgs.size(); i++) { if
	 * (!usernames.contains(receiveMsgs.get(i).getUsername())) {
	 * usernames.add(receiveMsgs.get(i).getUsername()); } }
	 * HttpUtils.logger.info("查询时间： " + (end - start));
	 * 
	 * // Boards中不在usernames中的 这段时间内状态都是掉线。 List<String> usernameOffline = new
	 * ArrayList<>(); List<Record> recordsOffline = new ArrayList<>();
	 * List<Record> recordsOnline = new ArrayList<>(); for (int i = 0; i <
	 * Boards.length; i++) { if (!usernames.contains(Boards[i])) {
	 * usernameOffline.add(Boards[i]); Record record = new Record();
	 * record.setDeviceId(Boards[i]); record.setStartTime(startTime);
	 * record.setEndTime(endTime);
	 * record.setRecordDate(toDateString(startTime)); record.setType(0);
	 * recordsOffline.add(record); } } // recordService.batchInsert(records); //
	 * 开始分析 在线的 中间掉线的 区间 // 先分类，与usernames中的 匹配，然后在筛选
	 * 其中的offline时间段。((i+1)-i)>16-> // receive_time相减得出时间段
	 * //比较各个board第一条消息的receive_time与endTime相比得出这个时间段内的状态，
	 * 各个board最后一条消息的receive_time与startTime相比得出这个时间段内的状态。小于16s->online；大于16s->
	 * offline // 分类 List<List<ReceiveMsg>> lists = new ArrayList<>(); for (int
	 * i = 0; i < usernames.size(); i++) { List<ReceiveMsg> receiveMsgList = new
	 * ArrayList<>(); for (int j = 0; j < receiveMsgs.size(); j++) { if
	 * (usernames.get(i).equals(receiveMsgs.get(j).getUsername())) {
	 * receiveMsgList.add(receiveMsgs.get(j)); } } lists.add(receiveMsgList); }
	 * 
	 * HttpUtils.logger.info("online----------------" + lists.size()); //
	 * 筛选offline for (int i = 0; i < lists.size(); i++) {
	 * HttpUtils.logger.info(lists.get(i).get(0).getUsername() + "  size is " +
	 * lists.get(i).size()); for (int j = 0; j < lists.get(i).size() - 1; j++) {
	 * //endTime 与第一条msg相比 if(j ==
	 * 0&&(endTime.toEpochSecond(ZoneOffset.of("+8"))-lists.get(i).get(j).
	 * getReceive_time() .toEpochSecond(ZoneOffset.of("+8")))>Duration){ Record
	 * record = new Record();
	 * record.setDeviceId(lists.get(i).get(j).getUsername());
	 * record.setRecordDate(toDateString(startTime)); record.setStartTime(
	 * lists.get(i).get(j).getReceive_time()); record.setEndTime(endTime);
	 * record.setType(0); recordsOffline.add(record); }else { Record record =
	 * new Record(); record.setDeviceId(lists.get(i).get(j).getUsername());
	 * record.setRecordDate(toDateString(startTime)); record.setStartTime(
	 * lists.get(i).get(j).getReceive_time()); record.setEndTime(endTime);
	 * record.setType(1); recordsOnline.add(record); }
	 * 
	 * //startTime与最后一条msg相比 if(j == lists.get(i).size()-1 &&
	 * (lists.get(i).get(j).getReceive_time()
	 * .toEpochSecond(ZoneOffset.of("+8"))-startTime.toEpochSecond(ZoneOffset.of
	 * ("+8"))>Duration)){ Record record = new Record();
	 * record.setDeviceId(lists.get(i).get(j).getUsername());
	 * record.setRecordDate(toDateString(startTime));
	 * record.setStartTime(startTime);
	 * record.setEndTime(lists.get(i).get(j).getReceive_time());
	 * record.setType(0); recordsOffline.add(record); }else { Record record =
	 * new Record(); record.setDeviceId(lists.get(i).get(j).getUsername());
	 * record.setRecordDate(toDateString(startTime));
	 * record.setStartTime(startTime);
	 * record.setEndTime(lists.get(i).get(j).getReceive_time());
	 * record.setType(1); recordsOnline.add(record); }
	 * 
	 * if ((lists.get(i).get(j).getReceive_time()
	 * .toEpochSecond(ZoneOffset.of("+8")) - lists.get(i).get(j +
	 * 1).getReceive_time() .toEpochSecond(ZoneOffset.of("+8"))) > Duration) {
	 * Record record = new Record();
	 * record.setDeviceId(lists.get(i).get(j).getUsername());
	 * record.setRecordDate(toDateString(startTime)); record.setStartTime(
	 * lists.get(i).get(j + 1).getReceive_time());
	 * record.setEndTime(lists.get(i).get(j).getReceive_time());
	 * record.setType(0); recordsOffline.add(record); }else { Record record =
	 * new Record(); record.setDeviceId(lists.get(i).get(j).getUsername());
	 * record.setRecordDate(toDateString(startTime)); record.setStartTime(
	 * lists.get(i).get(j + 1).getReceive_time());
	 * record.setEndTime(lists.get(i).get(j).getReceive_time());
	 * record.setType(1); recordsOnline.add(record); } } } List<Record> records
	 * = new ArrayList<>(); records.addAll(recordsOnline);
	 * records.addAll(recordsOffline);
	 * HttpUtils.logger.info(recordsOnline.size()+"---"+recordsOffline.size());
	 * return HttpUtils.mapper.writeValueAsString(records); }
	 */

	/**
	 * 1.筛选出查询期间内没有msg的都是offline的record. 2.>16s 的都是offline 3.通过offline筛选出online
	 * a.在startTime~endTime之间有数据并且都没有offline的，都是online.
	 * b.除去offline的时间段，剩余的都是online的。
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws JsonProcessingException
	 */
	@GetMapping("recordsByDate")
	@ResponseBody
	public List<Record> getRecordsBySingleDay(RecordRequest recordRequest)
			throws JsonProcessingException {

		// 获取offline和online的Record
		List<Record> records = recordService.getRecords(recordRequest);
		HttpUtils.logger.info("records size is " + records.size());
		return records;
	}

	/**
	 * 通过Date 获取单天状态记录,并根据Device数量分页，10个Device为一页 根据10个DeviceId从数据库中查询出相应的记录。
	 * 
	 * @param recordRequest
	 * @return
	 */
	@GetMapping("recordsSingleDay")
	@ResponseBody
	public Map<String, Object> getRecordsDate(RecordRequest recordRequest) {
		HttpUtils.logger.info("参数是:  " + recordRequest);
		Map<String, Object> result = new HashMap<>();

		// 开始根据pageNum pageSize 来分页得到要展示到页面Boards ，根据openfire_username asc排列
		List<SysPlatformBoard> sysPlatformBoards = sysPlatformBoardService
				.selectPage(recordRequest);
		HttpUtils.logger.info("PageInfo size is " + sysPlatformBoards.size());

		recordRequest.setDeviceIds(sysPlatformBoards);
		result.put("deviceIds", recordRequest.getDeviceIds());

		// 开始根据sysPlatformBoards来获取状态记录
		List<Record> records = new ArrayList<>();
		// 如果是查询当天的状态记录。只能暂时生成。
		if (recordRequest.getStartTime().toLocalDate()
				.equals(ZonedDateTime.now()
						.withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
						.toLocalDateTime().toLocalDate())) {
			// 获取offline和online的Record
			records = recordService.getRecordsByDeviceIds(recordRequest);
			HttpUtils.logger.info("records size is " + records.size());
			result.put("records", records);
		} else {
			// 查询以前的状态，从数据库中取出。时间不用减去13小时。
			RecordExample recordExample = new RecordExample();
			com.hp.roam.model.RecordExample.Criteria recordCriteria = recordExample
					.createCriteria();
			recordCriteria
					.andStartTimeGreaterThanOrEqualTo(
							recordRequest.getStartTime())
					.andEndTimeLessThanOrEqualTo(recordRequest.getEndTime());
			records = recordService.selectByExample(recordExample);
			// 去重
			List<String> recordDeviceIds = new ArrayList<>();
			for (Record record : records) {
				if (!recordDeviceIds.contains(record.getDeviceId())) {
					recordDeviceIds.add(record.getDeviceId());
				}
			}
			// 造记录
			for (String deviceId : recordRequest.getDeviceIds()) {
				if (!recordDeviceIds.contains(deviceId)) {
					Record record = new Record();
					record.setDeviceId(deviceId);
					record.setStartTime(recordRequest.getStartTime());
					record.setEndTime(recordRequest.getEndTime());
					record.setRecordDate(recordRequest.getStartTime()
							.toLocalDate().toString().replace("-", ""));
					record.setType(0);
					records.add(record);
				}
			}
			result.put("records", records);
		}
		return result;
	}

	@GetMapping("countDevices")
	@ResponseBody
	public Map<String, Object> getCountDevices() {
		Map<String, Object> result = new HashMap<>();
		long deviceCount = sysPlatformBoardService.countByExample(null);
		result.put("deviceCount", deviceCount);
		return result;
	}

	/**
	 * 获取特定Device单天状态记录，以及时间Rate
	 * 
	 * @param recordRequest
	 * @return
	 */
	@GetMapping("recordsBySingleDayAndDeviceId")
	@ResponseBody
	public Map<String, Object> getRecordsBySingleDayAndDeviceId(
			RecordRequest recordRequest) {
		Map<String, Object> result = new HashMap<>();
		HttpUtils.logger.info("参数是： " + recordRequest);
		List<Record> records = new ArrayList<>();

		Double offline = 0.0;
		Double online = 0.0;

		if (recordRequest.getStartTime().toLocalDate()
				.equals(ZonedDateTime.now()
						.withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
						.toLocalDateTime().toLocalDate())) {
			// 查询特定Device当天的状态
			records = recordService.getRecordsByDeviceIdAndDate(recordRequest);
			// 开始计算百分比

			for (Record record : records) {
				if (record.getType() == 0) {
					// Offline Rate
					offline += record.getEndTime()
							.toEpochSecond(ZoneOffset.of("+8"))
							- record.getStartTime()
									.toEpochSecond(ZoneOffset.of("+8"));
				} else {
					// Online
					online += record.getEndTime()
							.toEpochSecond(ZoneOffset.of("+8"))
							- record.getStartTime()
									.toEpochSecond(ZoneOffset.of("+8"));
				}
			}
		} else {
			// 查询特定Device以前的状态，从数据库中取出的时间，不用 -13小时，因为没从云系统中取时间
			RecordExample recordExample = new RecordExample();
			com.hp.roam.model.RecordExample.Criteria recordCriteria = recordExample
					.createCriteria();

			recordCriteria
					.andStartTimeGreaterThanOrEqualTo(
							recordRequest.getStartTime())
					.andEndTimeLessThanOrEqualTo(recordRequest.getEndTime())
					.andDeviceIdEqualTo(recordRequest.getDeviceId());
			records = recordService.selectByExample(recordExample);
			// 去重
			List<String> recordDeviceIds = new ArrayList<>();
			for (Record record : records) {
				if (!recordDeviceIds.contains(record.getDeviceId())) {
					recordDeviceIds.add(record.getDeviceId());
				}
			}
			if (!recordDeviceIds.contains(recordRequest.getDeviceId())) {
				Record record = new Record();
				record.setDeviceId(recordRequest.getDeviceId());
				record.setStartTime(recordRequest.getStartTime());
				record.setEndTime(recordRequest.getEndTime());
				record.setRecordDate(recordRequest.getStartTime().toLocalDate()
						.toString().replace("-", ""));
				record.setType(0);
				records.add(record);

				offline = 1.0;
				online = 0.0;
			}
			OnlineRateExample onlineRateExample = new OnlineRateExample();
			Criteria criterion = onlineRateExample.createCriteria();
			criterion.andCreateDateBetween(
					recordRequest.getStartTime().plusDays(1),
					recordRequest.getEndTime().plusDays(1))
					.andDeviceIdEqualTo(recordRequest.getDeviceId());
		    List<OnlineRate> onlineRates = onlineRateService.selectByExample(onlineRateExample);
		    if(onlineRates!= null && onlineRates.size()>0){
		    	online = onlineRates.get(0).getOnlineRate();
		    	offline = 1-online;
		    }

		}
		result.put("records", records);

		result.put("Online", online);
		result.put("Offline", offline);
		return result;
	}
}
