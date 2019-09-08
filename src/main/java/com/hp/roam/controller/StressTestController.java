package com.hp.roam.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.roam.executor.service.ExecutorSercice;
import com.hp.roam.model.MqttUser;
import com.hp.roam.model.OfUser;
import com.hp.roam.model.Offline;
import com.hp.roam.service.MqttUserService;
import com.hp.roam.service.OfUserService;
import com.hp.roam.util.HttpUtils;

/**
 * @author ck
 * @date 2019年2月28日 上午11:46:14
 */
@Controller
public class StressTestController {

	
	@Autowired
	private ExecutorSercice executorSercice;
	
	@Autowired
	private OfUserService ofUserService;
	
	@Autowired
	private MqttUserService mqttUserService;
	
	@RequestMapping("/")
	public String index(Model model){
		return "home";
	}
	
	@RequestMapping("/testXmpp")
	public String startTestXmpp(){
		return "testXmpp";
	}
	
	@RequestMapping("/testMqtt")
	public String startTestMqtt(){
		return "testMqtt";
	}
	
	@PostMapping("/testUsers")
	@ResponseBody
	public String testUsers(OfUser ofUser) throws JsonProcessingException{
		Map<String, String> msg = new HashMap<>();
		OfUser ofUser2 = ofUserService.selectByPrimaryKey(ofUser.getUsername());
		if(ofUser2 != null){
			msg.put("msg", "success");
		}else{
			msg.put("msg", "not found");
		}
		return HttpUtils.mapper.writeValueAsString(msg);
	} 
	
	
	
	@PostMapping("xmpptest")
	public String startTasks(String number,String startTime,String sendTime,Offline offlineConfig) throws InterruptedException{
		System.out.println(number+"--"+startTime);
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<OfUser> users = ofUserService.selectAll();
				List<OfUser> users1 = users.subList(5, Integer.valueOf(number)+5);
				for (OfUser ofUser : users1) {
					try {
						Thread.sleep(Integer.valueOf(startTime));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(ofUser.getUsername().contains("40")){
						ofUser.getUsername().replace("\40", "\\40");
					}
					System.out.println(ofUser.getUsername());
					executorSercice.executeAsyncTask(ofUser,sendTime,offlineConfig);
				}
			}
		}).start();
		
		return "success";
	}
	@GetMapping("stopTask")
	public String stopTasks(){
		List<OfUser> users = ofUserService.selectAll();
		List<OfUser> users1 = users.subList(3, 50);
		for (OfUser ofUser : users1) {
			if(ofUser.getUsername().contains("40")){
				ofUser.getUsername().replace("\40", "\\40");
			}
			System.out.println(ofUser.getUsername());
			executorSercice.executeAsyncTaskStop(ofUser);
		}
		return "success";
	}
	
	
	//MQTT 压力测试
	@PostMapping("mqtttest1")
	public String startMQTTTest1(String startTime,String sendTime,String number,Offline offlineConfig){
		System.out.println("start MQTT Test");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				List<MqttUser> mqttUsers = mqttUserService.selectByExample(null);
				List<MqttUser> mqttUsers2 = mqttUsers.subList(0, Integer.valueOf(number));
				System.out.println("mqttUsers's size is "+mqttUsers2.size());
				for (MqttUser mqttUser : mqttUsers2) {
					try {
						Thread.sleep(Integer.valueOf(startTime));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(mqttUser.getUsername());
					
					//开始执行异步任务
					executorSercice.executeAsyncMqttTask(mqttUser, sendTime, offlineConfig);
				}
			}
		}).start();
		
		return "success";
	} 
	
	
	//MQTT 压力测试
		@PostMapping("mqtttest")
		public String startMQTTTest(String startTime,String sendTime,String startNum,String number,Offline offlineConfig){
			System.out.println("start MQTT Test");
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					for (int i = Integer.valueOf(startNum) ; i< Integer.valueOf(number); i++) {
						try {
							Thread.sleep(Integer.valueOf(startTime));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						//开始执行异步任务
						executorSercice.executeAsyncMqttTask(sendTime, i);
					}
				}
			}).start();
			
			return "success";
		} 
	
	
}
