package com.hp.roam;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;

import com.hp.roam.api.ApiService;
import com.hp.roam.dao.SysPlatformBoardMapper;
import com.hp.roam.executor.service.ExecutorSercice;
import com.hp.roam.http.BaseResponse;
import com.hp.roam.http.EmsRequest;
import com.hp.roam.model.MqttUser;
import com.hp.roam.model.OfUser;
import com.hp.roam.model.OnlineRate;
import com.hp.roam.model.OnlineRateExample;
import com.hp.roam.model.ReceiveMsg;
import com.hp.roam.model.ReceiveMsgExample;
import com.hp.roam.model.ReceiveMsgExample.Criteria;
import com.hp.roam.model.Record;
import com.hp.roam.model.RecordRequest;
import com.hp.roam.model.SysPlatformBoard;
import com.hp.roam.model.SysUser;
import com.hp.roam.service.MqttUserService;
import com.hp.roam.service.OfUserService;
import com.hp.roam.service.OnlineRateService;
import com.hp.roam.service.ReceiveMsgService;
import com.hp.roam.service.RecordService;
import com.hp.roam.service.SysPlatformBoardService;
import com.hp.roam.util.HttpUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootDemoApplicationTests {

	private static String[] Boards = {"board20", "board21", "board22",
			"board23", "board24", "board25", "board26", "board27", "board28",
			"board29"};

	@Value("${api.ems.to}")
	private String to;
	
	@Autowired
	private OnlineRateService onlineRateService;
	
	@Autowired
	private ExecutorSercice executorSercice;

	@Autowired
	private OfUserService ofUserService;

	@Autowired
	private ReceiveMsgService receiveMsgService;

	@Autowired
	private RecordService recordService;
	
	@Autowired
	private SysPlatformBoardMapper sysPlatformBoardMapper;
	
	@Autowired
	private SysPlatformBoardService sysPlatformBoardService ;
	
	@Autowired
	private ApiService ApiService;
	
	@Autowired 
	private MqttUserService mqttUserService;
	
	
	//准备mqtt test users
	@Test
	public void insertMoreMqttUser(){
		List<MqttUser> mqttUsers = new ArrayList<>();
		for (int i = 100000; i < 200000; i++) {
			MqttUser mqttUser = new MqttUser();
			mqttUser.setUsername("test"+i);
			mqttUser.setPassword("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
			mqttUser.setIs_superuser(false);
			mqttUser.setCreated(new Date());
			mqttUsers.add(mqttUser);
		}
		
		int count = mqttUserService.batchInsert(mqttUsers);
		System.out.println("insert list number is "+count);
	}
	

	@Test
	public void testOnlineRate1(){
		OnlineRateExample onlineRateExample = new OnlineRateExample();
		//onlineRateExample.setOrderByClause("deviceId asc");
		onlineRateExample.setOrderByClause("(SUBSTRING(deviceId,6)+0) asc");
		com.hp.roam.model.OnlineRateExample.Criteria onlineRateCriteria = onlineRateExample.createCriteria();
		onlineRateCriteria.andCreateDateBetween(ZonedDateTime.now().minusHours(17).minusMinutes(50)
						.withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
						.toLocalDateTime(), ZonedDateTime.now()
						.withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
						.toLocalDateTime());
		List<OnlineRate> onlineRates = new ArrayList<>();
		onlineRates = onlineRateService.selectByExample(onlineRateExample);
//		System.out.println(onlineRates.size());
//		System.out.println(onlineRates.get(0).getDeviceId()+onlineRates.get(0).getCreateDate()+onlineRates.get(0).getOnlineRate());
//		System.out.println(onlineRates.get(onlineRates.size()-1));
		onlineRates.stream().forEach(e->System.out.println(e.getDeviceId()));
	}
	
	
	@Test
	public void testEms(){
		List<String> urList = new ArrayList<>();
		urList.add("http://15.96.132.45/test/Board.xlsx");
		EmsRequest emsRequest = new EmsRequest();
		emsRequest.setSubject("Online Rate Report");
		emsRequest.setContent("Attachment is the report of the "+ZonedDateTime.now()
						.withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
						.toLocalDateTime().toLocalDate().toString()+" 'Board Online Rate'");
		
		emsRequest.setAttachments(urList);
		emsRequest.setTo("406585395@qq.com");
		BaseResponse<String> msgResponse = ApiService.testEmsSend(emsRequest);
		HttpUtils.logger.info("the ems messageId is {}",msgResponse.getData());
		
//		EmsRequest emsRequest1 = new EmsRequest();
//		emsRequest1.setTo(to);
//		emsRequest1.getTo().stream().forEach(t->HttpUtils.logger.info(t));
//		System.out.println("the email receiver are "+emsRequest1.getTo());
	}
	
	
	@Test
	public void testOnlineRate(){
		OnlineRate onlineRate = new OnlineRate();
		onlineRate.setDeviceId("board20");
		onlineRate.setOnlineRate(0.0147);
		onlineRate.setCreateDate(LocalDateTime.now());
		OnlineRate onlineRate1 = new OnlineRate();
		onlineRate1.setDeviceId("board21");
		onlineRate1.setOnlineRate(0.0146);
		onlineRate1.setCreateDate(LocalDateTime.now());
		List<OnlineRate> onlineRates = new ArrayList<>();
		onlineRates.add(onlineRate);
		onlineRates.add(onlineRate1);
		onlineRateService.batchInsert(onlineRates);
	}
	
	
	@Test
	public void testSysBoard(){
		List<SysPlatformBoard> sysPlatformBoards = sysPlatformBoardMapper.selectAllAsc();
		System.out.println(sysPlatformBoards.size());
		RecordRequest recordRequest = new RecordRequest();
		recordRequest.setPageNum(1);
		recordRequest.setPageSize(5);
		List<SysPlatformBoard> sysPlatformBoards2 = sysPlatformBoardService.selectPage(recordRequest);
		System.out.println(sysPlatformBoards2.size());
	}
	
	
	@SuppressWarnings("static-access")
	@Test
	public void testReceiveMsg() {
		long start = System.currentTimeMillis();
		ReceiveMsgExample receiveMsgExample = new ReceiveMsgExample();
		receiveMsgExample.setOrderByClause("receive_time desc");
		Criteria receiveMsgCriteria = receiveMsgExample.createCriteria();
		// Date date = new Date();
		// date.setDate(28);
		// System.out.println(date);
		LocalDateTime localDateTime = LocalDateTime.now();
		localDateTime = localDateTime.of(localDateTime.getYear(),
				localDateTime.getMonth().APRIL, 28, localDateTime.getHour(),
				localDateTime.getMinute(), localDateTime.getSecond());
		receiveMsgCriteria.andReceive_timeBetween(toStartDate(localDateTime),
				toEndDate(localDateTime));
		List<ReceiveMsg> receiveMsgs = receiveMsgService
				.selectByExample(receiveMsgExample);
		long end = System.currentTimeMillis();

		List<String> usernames = new ArrayList<>();
		// 先找出 包含的username
		for (int i = 0; i < receiveMsgs.size(); i++) {
			if (!usernames.contains(receiveMsgs.get(i).getUsername())) {
				usernames.add(receiveMsgs.get(i).getUsername());
			}
		}
		System.out.println("查询时间： " + (end - start));
		System.out.println(usernames);

		// Boards中不在usernames中的 今天状态都是掉线。
		List<String> usernameOffline = new ArrayList<>();
		List<Record> records = new ArrayList<>();
		for (int i = 0; i < Boards.length; i++) {
			if (!usernames.contains(Boards[i])) {
				usernameOffline.add(Boards[i]);
				Record record = new Record();
				record.setDeviceId(Boards[i]);
				record.setStartTime(toStartDate(localDateTime));
				record.setEndTime(toEndDate(localDateTime));
				record.setRecordDate(toDateString(localDateTime));
				record.setType(0);
				records.add(record);
			}
		}
		// recordService.batchInsert(records);
		// 开始分析 在线的 当天中间掉线的 board
		// 先分类，与usernames中的 匹配，然后在筛选 其中的offline时间段。((i+1)-i)>16->
		// receive_time相减得出时间段
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
		
		System.out.println("online----------------"+lists.size());
		// 筛选offline
		for (int i = 0; i < lists.size(); i++) {
			System.out.println(lists.get(i).get(0).getUsername()+"  size is "+lists.get(i).size());
			for (int j = 0; j < lists.get(i).size()-1; j++) {
				if ((lists.get(i).get(j).getReceive_time()
						.toEpochSecond(ZoneOffset.of("+8"))
						- lists.get(i).get(j+1).getReceive_time()
								.toEpochSecond(ZoneOffset.of("+8"))) > 16) {
					System.out.println(lists.get(i).get(j + 1).getUsername()+"----offline");
					Record record = new Record();
					record.setDeviceId(lists.get(i).get(j + 1).getUsername());
					record.setRecordDate(toDateString(localDateTime));
					record.setStartTime(lists.get(i).get(j+1).getReceive_time());
					record.setEndTime(lists.get(i).get(j).getReceive_time());
					record.setType(0);
					records.add(record);
				}
			}
		}
		
		for (int i = 0; i < records.size(); i++) {
			System.out.println(records.get(i));
		}
	}

	@Test
	public void testLocalDate() {
		// System.out.println(toDateString(LocalDateTime.now()));

		System.out.println(
				LocalDateTime.now().plusHours(1));

		System.out.println(new Date().getTime());
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
		// Date startDate = new Date();
		// startDate.setDate(date.getDate());
		// startDate.setHours(0);
		// startDate.setMinutes(0);
		// startDate.setSeconds(0);
		// System.out.println(startDate);
		// return startDate;

		LocalDateTime localDateTime = LocalDateTime.now();
		localDateTime = localDateTime.of(date.getYear(), date.getMonth(),
				date.getDayOfMonth(), 0, 0, 0);
		return localDateTime;
	}

	@SuppressWarnings("static-access")
	public LocalDateTime toEndDate(LocalDateTime date) {
		// Date endDate = new Date();
		// endDate.setDate(date.getDate());
		// endDate.setHours(23);
		// endDate.setMinutes(59);
		// endDate.setSeconds(59);
		// System.out.println(endDate);
		// return endDate;

		LocalDateTime localDateTime = LocalDateTime.now();
		localDateTime = localDateTime.of(date.getYear(), date.getMonth(),
				date.getDayOfMonth(), 23, 59, 59);
		return localDateTime;
	}

	@Test
	public void testThreadTasks() {
		List<OfUser> users = ofUserService.selectAll();
		List<OfUser> users1 = users.subList(3, 3000);
		for (OfUser ofUser : users1) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (ofUser.getUsername().contains("40")) {
				ofUser.getUsername().replace("\40", "\\40");
			}
			System.out.println(ofUser.getUsername());
			Thread thread = new Thread(new ThreadTask(ofUser));
			thread.start();
		}
	}

	@Test
	public void testExecutorTask() {
		SysUser user1 = new SysUser();
		user1.setUsername("admin_pms");
		user1.setPassword("123");
		SysUser user2 = new SysUser();
		user2.setUsername("test@test.com");
		user2.setPassword("123");
		List<SysUser> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);
		// executorSercice.executeAsyncTask(user1);
		// for(SysUser user:users){
		// }
		// for (int i = 0; i < 30; i++) {
		// try {
		// executorSercice.testTask1(i);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
	}

}
