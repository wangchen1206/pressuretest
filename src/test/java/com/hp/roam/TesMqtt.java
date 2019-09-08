package com.hp.roam;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.hp.roam.model.MqttUser;
import com.hp.roam.mqtt.PushCallback;

/**
 * @author ck
 * @date 2019年6月6日 下午2:17:41
 */
public class TesMqtt {
	
//	static String broker = "tcp://localhost:1883";
//	static String broker = "tcp://114.215.135.121:1883";
//	static String broker = "tcp://118.184.208.70:1883";
//	static String broker = "tcp://118.184.208.75:1883";
	static String broker = "tcp://118.184.218.201:80";
	String clientId = "clientId1";
	static String userName = "test0";
	static String password = "123";
	
	public static void publish(MqttTopic topic, MqttMessage message)
			throws MqttPersistenceException, MqttException {
		MqttDeliveryToken token = topic.publish(message);
		token.waitForCompletion();
		System.out.println(
				"message is published completely! " + token.isComplete());
	}
	
	public static void main(String[] args) {
		try {
			Long connStart = System.currentTimeMillis();
			// 开始连接
			MqttClient client = new MqttClient(broker, userName,
					new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			// 服务器是否清除session,清除session，服务器不会保持session,非持久化订阅
			options.setCleanSession(true);
//			options.setUserName(userName);
//			options.setPassword(password.toCharArray());
			// 设置超时时间
			options.setConnectionTimeout(60);
			// 设置会话心跳时间 服务端会给客户端发消息，以确认客服端是否在线，不会重连。
			options.setKeepAliveInterval(60);
			client.setCallback(new PushCallback(client));
			client.connect(options);
			String msg = "{'command':'newjob','jobs':[{'createdTime':1489125659759,'id':'hp90913123','inUrl':'http://15.96.132.47/lespms/source/file/111231234345.jpg','originalFileName':'mypic.jpg','outUrl':'http://15.96.132.47/lespms/source/file/111231234345.png','pages':0,'pagesPrinted':0,'printSetting':{'copies':5,'duplex':false,'paperSize':'A4','paperType':'plain','trayIndex':0},'printerUuid':'G235POUIYERTUFJFK6544FKIFJFJKCKC','status':'new'}],'randomId':'1234567'}";
			MqttMessage message = new MqttMessage();
			//设置此消息的服务质量
			message.setQos(0);
			//消息传递发送引擎是否应保留发送消息
			message.setRetained(false);
			//设置消息.字节数组
			message.setPayload(msg.getBytes());
			Long connEnd = 0L;
			System.out.println(Thread.currentThread().getName() + "执行");
			while (true) {
				if(!client.isConnected()){
					client.reconnect();
				}
					Thread.sleep(20000);
				MqttTopic topic = client
						.getTopic("topic-" + userName);
				// 发送包
				publish(topic, message);
				connEnd = System.currentTimeMillis();
			}
		} catch (IllegalArgumentException | MqttException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
