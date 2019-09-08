package com.hp.roam;

import java.util.Date;

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
 * @date 2019年6月5日 下午4:49:40
 */
public class MQTTPub {

	String TOPIC = "cc";
	String TOPIC125 = "cc125";
	String content = "hello mqtt";
	int qos = 2;
	 String broker = "tcp://localhost:1883";
//	String broker = "tcp://114.215.135.121:1883";
	String clientId = "clientId1";
	String userName = "test0";
	String password = "123";

	private MqttClient client;
	private MqttTopic topic;
	private MqttTopic topic125;
	private MqttMessage message;

	public MQTTPub() throws MqttException {
		// MemoryPersistence设置clientid的保存形式，默认为以内存保存
		client = new MqttClient(broker, clientId, new MemoryPersistence());
		connect();
	}

	private void connect() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(false);
//		options.setUserName(userName);
//		options.setPassword(password.toCharArray());
		// 设置超时时间
		options.setConnectionTimeout(10);
		// 设置会话心跳时间
		options.setKeepAliveInterval(20);
		client.setCallback(new PushCallback(client));
		try {
			client.connect(options);
		} catch (MqttException e) {
			e.printStackTrace();
		}
		topic = client.getTopic(TOPIC);
		topic125 = client.getTopic(TOPIC125);
	}

	public void publish(MqttTopic topic, MqttMessage message)
			throws MqttPersistenceException, MqttException {
		MqttDeliveryToken token = topic.publish(message);
		token.waitForCompletion();
		System.out.println(
				"message is published completely! " + token.isComplete());
	}

	public static void main(String[] args) throws MqttException {
		MQTTPub server = new MQTTPub();

		// server.message = new MqttMessage();
		// server.message.setQos(2);
		// server.message.setRetained(true);
		// server.message.setPayload((new Date()+"给客户端2推送的信息").getBytes());
		// server.publish(server.topic, server.message);

		server.message = new MqttMessage();
		server.message.setQos(2);
		server.message.setRetained(true);
		server.message.setPayload((new Date() + "给client3推送的信息").getBytes());
		server.publish(server.topic125, server.message);

		System.out.println(server.message.isRetained() + "------ratained状态");

	}
}
