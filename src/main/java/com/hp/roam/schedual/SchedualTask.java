package com.hp.roam.schedual;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hp.roam.api.ApiService;
import com.hp.roam.model.OnlineRate;
import com.hp.roam.model.OnlineRateExample;
import com.hp.roam.model.ReceiveMsg;
import com.hp.roam.model.ReceiveMsgExample;
import com.hp.roam.model.ReceiveMsgExample.Criteria;
import com.hp.roam.model.Record;
import com.hp.roam.model.SysPlatformBoard;
import com.hp.roam.service.OnlineRateService;
import com.hp.roam.service.ReceiveMsgService;
import com.hp.roam.service.RecordService;
import com.hp.roam.service.SysPlatformBoardService;
import com.hp.roam.util.ExcelUtils;
import com.hp.roam.util.HttpUtils;

/**
 * 状态记录 定时任务
 * 
 * @author ck
 * @date 2019年5月13日 上午11:00:02
 */
@Component
public class SchedualTask {

	private static final int Duration = 120;

	/* 消息发送成功 */
	private static final String EMS_SEND_OK = "SUCCESSED";
	/* 消息发送失败 */
	private static final String EMS_SEND_FAIL = "FAILED";

	@Value("${api.ems.baseFilePath}")
	private String baseFilePath;

	@Value("${api.ems.baseFileUrl}")
	private String baseFileUrl;

	@Value("${api.ems.to}")
	private String to;

	@Autowired
	private ApiService apiService;

	@Autowired
	private OnlineRateService onlineRateService;

	@Autowired
	private RecordService recordService;

	@Autowired
	private ReceiveMsgService receiveMsgService;

	@Autowired
	private SysPlatformBoardService sysPlatformBoardService;

	/**
	 * 每天晚上12点05执行，查询前一天的数据，得出前一天状态记录records,存入数据库。 数据库中查出要得出状态的Board.
	 * 京东云时间是UTCzone,所以应该是 0 5 16 * * ?
	 */
	//@Scheduled(cron = "0 5 16 * * ? ")
//	 @Scheduled(cron = "0 55 2 * * ? ")
	public void getRecordsEveryday() {
		// 数据库中查出得出状态的Boards
		List<SysPlatformBoard> sysPlatformBoards = sysPlatformBoardService
				.selectByExample(null);
		if (sysPlatformBoards != null && sysPlatformBoards.size() != 0) {

			// 将京东云的时间转换为 Asia/Shanghai 时区的LocalDateTime
			LocalDateTime startTime = toLastDayStart(ZonedDateTime.now()
					.withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
					.toLocalDateTime());
			HttpUtils.logger
					.info("the startTime local-datetime is " + startTime);
			LocalDateTime endTime = toLastDayEnd(ZonedDateTime.now()
					.withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
					.toLocalDateTime());
			HttpUtils.logger.info("the endTime local-datetime is " + endTime);
			ReceiveMsgExample receiveMsgExample = new ReceiveMsgExample();
			receiveMsgExample.setOrderByClause("receive_time desc");
			Criteria receiveMsgCriteria = receiveMsgExample.createCriteria();
			// 因为京东云mysql存如数据库的receive_time比现在少13小时，所以查询数据库的时间 减去 13小时
			receiveMsgCriteria.andReceive_timeBetween(startTime.minusHours(13),
					endTime.minusHours(13));
			List<ReceiveMsg> receiveMsgs = receiveMsgService
					.selectByExample(receiveMsgExample);
			List<String> usernames = new ArrayList<>();
			// 先找出 这段时间内有在线的username
			for (int i = 0; i < receiveMsgs.size(); i++) {
				// receive_time时间加13个小时
				receiveMsgs.get(i).setReceive_time(
						receiveMsgs.get(i).getReceive_time().plusHours(13));
				if (!usernames.contains(receiveMsgs.get(i).getUsername())) {
					usernames.add(receiveMsgs.get(i).getUsername());
				}
			}

			// sysPlatformBoards中不在usernames中的 这段时间内状态都是掉线。
			List<String> usernameOffline = new ArrayList<>();
			List<Record> recordsOffline = new ArrayList<>();
			for (int i = 0; i < sysPlatformBoards.size(); i++) {
				if (!usernames.contains(
						sysPlatformBoards.get(i).getOpenfire_username())) {
					usernameOffline.add(
							sysPlatformBoards.get(i).getOpenfire_username());
					Record record = new Record();
					record.setDeviceId(
							sysPlatformBoards.get(i).getOpenfire_username());
					record.setStartTime(startTime);
					record.setEndTime(endTime);
					record.setRecordDate(toDateString(endTime));
					record.setType(0);
					recordsOffline.add(record);
				}
			}
			// recordService.batchInsert(records);
			// 开始分析 在线的 中间掉线的 区间
			// 先分类，与usernames中的 匹配，然后在筛选 其中的offline时间段。((i+1)-i)>16->
			// receive_time相减得出时间段
			// 比较各个board第一条消息的receive_time与endTime相比得出这个时间段内的状态，各个board最后一条消息的receive_time与startTime相比得出这个时间段内的状态。小于16s->online；大于16s->offline
			// 分类
			List<List<ReceiveMsg>> lists = new ArrayList<>();
			for (int i = 0; i < usernames.size(); i++) {
				List<ReceiveMsg> receiveMsgList = new ArrayList<>();
				for (int j = 0; j < receiveMsgs.size(); j++) {
					if (usernames.get(i)
							.equals(receiveMsgs.get(j).getUsername())) {
						receiveMsgList.add(receiveMsgs.get(j));
					}
				}
				lists.add(receiveMsgList);
			}

			// 1.startTime至endTime 中未出现过offline 全部都为online

			Map<String, List<Record>> offlineDeviceRecords = new HashMap<>();
			// 筛选offline
			for (int i = 0; i < lists.size(); i++) {
				List<Record> deviceRecordsOffline = new ArrayList<>();
				String deviceId = lists.get(i).get(0).getUsername();
				for (int j = 0; j < lists.get(i).size(); j++) {
					// endTime 与第一条msg相比
					if (j == 0 && (endTime.toEpochSecond(ZoneOffset.of("+8"))
							- lists.get(i).get(j).getReceive_time()
									.toEpochSecond(
											ZoneOffset.of("+8")) > Duration)) {
						Record record = new Record();
						record.setDeviceId(lists.get(i).get(j).getUsername());
						record.setRecordDate(toDateString(endTime));
						record.setStartTime(
								lists.get(i).get(j).getReceive_time());
						record.setEndTime(endTime);
						record.setType(0);
						deviceRecordsOffline.add(record);
					}

					// startTime与最后一条msg相比
					if (j == lists.get(i).size() - 1
							&& (lists.get(i).get(j).getReceive_time()
									.toEpochSecond(ZoneOffset.of("+8"))
									- startTime.toEpochSecond(
											ZoneOffset.of("+8")) > Duration)) {
						Record record = new Record();
						record.setDeviceId(lists.get(i).get(j).getUsername());
						record.setRecordDate(toDateString(endTime));
						record.setStartTime(startTime);
						record.setEndTime(
								lists.get(i).get(j).getReceive_time());
						record.setType(0);
						deviceRecordsOffline.add(record);
					}

					if (j < lists.get(i).size() - 1 && (lists.get(i).get(j)
							.getReceive_time()
							.toEpochSecond(ZoneOffset.of("+8"))
							- lists.get(i).get(j + 1).getReceive_time()
									.toEpochSecond(
											ZoneOffset.of("+8"))) > Duration) {
						Record record = new Record();
						record.setDeviceId(lists.get(i).get(j).getUsername());
						record.setRecordDate(toDateString(endTime));
						record.setStartTime(
								lists.get(i).get(j + 1).getReceive_time());
						record.setEndTime(
								lists.get(i).get(j).getReceive_time());
						record.setType(0);
						deviceRecordsOffline.add(record);
					}
				}
				offlineDeviceRecords.put(deviceId, deviceRecordsOffline);
			}

			List<Record> recordsOnlineMerged = new ArrayList<>();
			// 一整天都是online的
			List<Record> recordsOnlineAllDay = new ArrayList<>();
			List<Record> records = new ArrayList<>();
			// 通过offline筛选出online Record

			for (String deviceId : offlineDeviceRecords.keySet()) {

				// 如果offlineDeviceRecords.get(deviceId).size == 0 说明
				// 在startTime~endTime之间 都是online
				if (offlineDeviceRecords.get(deviceId).size() == 0) {
					Record record = new Record();
					record.setDeviceId(deviceId);
					record.setRecordDate(toDateString(endTime));
					record.setStartTime(startTime);
					record.setEndTime(endTime);
					record.setType(1);
					recordsOnlineMerged.add(record);
					recordsOnlineAllDay.add(record);
				} else {
					HttpUtils.logger.info("Offline size " + deviceId + "----"
							+ offlineDeviceRecords.get(deviceId).size());
					records.addAll(offlineDeviceRecords.get(deviceId));

					for (int k2 = 0; k2 < offlineDeviceRecords.get(deviceId)
							.size(); k2++) {

						if (k2 == 0 && offlineDeviceRecords.get(deviceId)
								.get(k2).getEndTime().isBefore(endTime)) {
							Record record = new Record();
							record.setDeviceId(offlineDeviceRecords
									.get(deviceId).get(k2).getDeviceId());
							record.setRecordDate(offlineDeviceRecords
									.get(deviceId).get(k2).getRecordDate());
							record.setStartTime(offlineDeviceRecords
									.get(deviceId).get(k2).getEndTime());
							record.setEndTime(endTime);
							record.setType(1);
							recordsOnlineMerged.add(record);
						}

						if (k2 == offlineDeviceRecords.get(deviceId).size() - 1
								&& offlineDeviceRecords.get(deviceId)
										.get(offlineDeviceRecords.get(deviceId)
												.size() - 1)
										.getStartTime().isAfter(startTime)) {
							Record record = new Record();
							record.setDeviceId(offlineDeviceRecords
									.get(deviceId).get(k2).getDeviceId());
							record.setRecordDate(offlineDeviceRecords
									.get(deviceId).get(k2).getRecordDate());
							record.setStartTime(startTime);
							record.setEndTime(offlineDeviceRecords.get(deviceId)
									.get(k2).getStartTime());
							record.setType(1);
							recordsOnlineMerged.add(record);
						}

						if (k2 < offlineDeviceRecords.get(deviceId).size() - 1
								&& offlineDeviceRecords.get(deviceId).get(k2)
										.getStartTime()
										.isAfter(offlineDeviceRecords
												.get(deviceId).get(
														k2 + 1)
												.getEndTime())) {
							Record record = new Record();
							record.setDeviceId(offlineDeviceRecords
									.get(deviceId).get(k2).getDeviceId());
							record.setRecordDate(offlineDeviceRecords
									.get(deviceId).get(k2).getRecordDate());
							record.setStartTime(offlineDeviceRecords
									.get(deviceId).get(k2 + 1).getEndTime());
							record.setEndTime(offlineDeviceRecords.get(deviceId)
									.get(k2).getStartTime());
							record.setType(1);
							recordsOnlineMerged.add(record);
						}
					}
				}

			}

			HttpUtils.logger
					.info("OnlineList size is " + recordsOnlineMerged.size());
			// 添加在查询的时间范围内都是offline的Record
			records.addAll(recordsOffline);
			records.addAll(recordsOnlineMerged);
			// 存入数据库中
			recordService.batchInsert(records);

			// 开始分析每天的上线率 backupDeviceRecords recordsOnlineAllDay recordsOffline

			List<OnlineRate> onlineRates = new ArrayList<>();

			// 一整天都掉线
			for (Record record : recordsOffline) {
				OnlineRate onlineRate = new OnlineRate();
				onlineRate.setDeviceId(record.getDeviceId());
				onlineRate.setOnlineRate(0.00);
				onlineRate.setCreateDate(ZonedDateTime.now()
						.withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
						.toLocalDateTime());
				onlineRates.add(onlineRate);
			}
			// 分析掉线的，得出上线的。
			for (String deviceId : offlineDeviceRecords.keySet()) {
				long offline = 0;
				OnlineRate onlineRate = new OnlineRate();
				onlineRate.setDeviceId(deviceId);
				onlineRate.setCreateDate(ZonedDateTime.now()
						.withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
						.toLocalDateTime());
				if (offlineDeviceRecords.get(deviceId).size() == 0
						|| offlineDeviceRecords.get(deviceId) == null) {
					onlineRate.setOnlineRate(1.00);
				} else {
					for (Record record : offlineDeviceRecords.get(deviceId)) {
						if (record.getType() == 0) {
							// offline
							offline += record.getEndTime()
									.toEpochSecond(ZoneOffset.of("+8"))
									- record.getStartTime()
											.toEpochSecond(ZoneOffset.of("+8"));
						}
					}
					// 计算百分比
					double daySeconds = 24 * 60 * 60;
					double rate = (daySeconds - offline) * 1.0 / daySeconds;
					HttpUtils.logger.info("the board " + deviceId
							+ "'s rate of online is " + rate);
					BigDecimal bg = new BigDecimal(rate).setScale(4,
							RoundingMode.DOWN);
					onlineRate.setOnlineRate(bg.doubleValue());
				}
				onlineRates.add(onlineRate);
			}
			// 插入数据库中
			onlineRateService.batchInsert(onlineRates);
		}

	}

	/**
	 * 调用smsApi 发送邮件通知。
	 */
	//@Scheduled(cron = "0 25 16 * * ? ")
//	@Scheduled(cron = "0 45 9 * * ? ")
	public void smsApi(){
		OnlineRateExample onlineRateExample = new OnlineRateExample();
		//onlineRateExample.setOrderByClause("deviceId asc");
		onlineRateExample.setOrderByClause("(SUBSTRING(deviceId,6)+0) asc");
		com.hp.roam.model.OnlineRateExample.Criteria onlineRateCriteria = onlineRateExample.createCriteria();
		onlineRateCriteria.andCreateDateBetween(ZonedDateTime.now().minusHours(1)
						.withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
						.toLocalDateTime(), ZonedDateTime.now()
						.withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
						.toLocalDateTime());
		List<OnlineRate> onlineRates = new ArrayList<>();
		onlineRates = onlineRateService.selectByExample(onlineRateExample);
		
		//1.将onlineRates 导入Excel,然后将excel放入本地
		String urlPath = baseFileUrl+"/"+ExcelUtils.exportOnlineRateExcel(baseFilePath, onlineRates);
		HttpUtils.logger.info("the urlPath is "+urlPath);
		
		
		/*//发送邮件通知服务
		//调用smsApi,将结果发送。
		List<String> urList = new ArrayList<>();
		urList.add(urlPath);
//		urList.add("http://15.96.132.45/test/Board.xlsx");
		EmsRequest emsRequest = new EmsRequest();
		emsRequest.setSubject("Online Rate Report");
		emsRequest.setContent("Attachment is the report of the "+ZonedDateTime.now()
						.withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
						.toLocalDateTime().toLocalDate().toString()+" 'Board Online Rate'");
		
		emsRequest.setAttachments(urList);
		emsRequest.setTo(to);
		BaseResponse<String> msgResponse = apiService.emsSend(emsRequest);
		HttpUtils.logger.info("the ems messageId is {}",msgResponse.getData());
		//如果data为空，说明没有发送 成功，做个校验，循环请求
		int count = 0;
		while(msgResponse.getData() == null||StringUtils.isEmpty(msgResponse.getData())){
			count++;
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			msgResponse = apiService.emsSend(emsRequest);
			if(count == 5){
				break;
			}
		}
		BaseResponse<Map<String, String>> statusResponse = null;
		if(msgResponse.getData() != null&&!StringUtils.isEmpty(msgResponse.getData())){
			try {
				Thread.sleep(30000);
				statusResponse = apiService.checkEmsStatusByMsgId(msgResponse.getData());
				int j = 0;
				while(statusResponse.getData() ==null){
					j++;
					Thread.sleep(30000);
					statusResponse = apiService.checkEmsStatusByMsgId(msgResponse.getData());
					if(j == 5){
						break;
					}
				}
				if(statusResponse.getData() != null && statusResponse.getData().get("status").equals(EMS_SEND_OK)){
					HttpUtils.logger.info("EMS Send OK !");
				}else if(statusResponse.getData() != null && statusResponse.getData().get("status").equals(EMS_SEND_FAIL)){
					HttpUtils.logger.info("EMS Send Fail");
				}else{
					HttpUtils.logger.info(statusResponse.getData().toString());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}else{
			HttpUtils.logger.info("Ems Service is Unavaliable !");
		}*/
		
		
	}

	/**
	 * @param date
	 * @return
	 */
	private String toDateString(LocalDateTime date) {
		return date.toLocalDate().toString().replace("-", "");
	}

	@SuppressWarnings("static-access")
	public LocalDateTime toLastDayStart(LocalDateTime localDateTime) {
		localDateTime = localDateTime.minusDays(1);
		localDateTime = localDateTime.of(localDateTime.getYear(),
				localDateTime.getMonth(), localDateTime.getDayOfMonth(), 0, 0,
				0);
		return localDateTime;
	}

	@SuppressWarnings("static-access")
	public LocalDateTime toLastDayEnd(LocalDateTime localDateTime) {
		localDateTime = localDateTime.minusDays(1);
		localDateTime = localDateTime.of(localDateTime.getYear(),
				localDateTime.getMonth(), localDateTime.getDayOfMonth(), 23, 59,
				59);
		return localDateTime;
	}
}
