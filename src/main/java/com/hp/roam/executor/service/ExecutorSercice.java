package com.hp.roam.executor.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.Executor;

import javax.annotation.Resource;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jxmpp.jid.Jid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.hp.roam.model.MqttUser;
import com.hp.roam.model.OfUser;
import com.hp.roam.model.Offline;
import com.hp.roam.mqtt.PushCallback;
import com.hp.roam.service.OfflineService;

/**
 * @author ck
 * @date 2019年3月13日 下午5:01:05
 */
@Service
public class ExecutorSercice {

	public static final String Offline = "offline";
	public static final String Online = "online";
	public static final String MQTTPWD = "123";

	@Value("${openfire.domain}")
	private String domain;
	@Value("${openfire.host}")
	private String host;
	@Value("${openfire.port}")
	private String port;

	@Value("${mqtt.uri}")
	private String mqttURI;

	@Resource(name = "getAsyncExecutor")
	private Executor executor;

	@Autowired
	private OfflineService offlineService;

	// @Autowired
	// private XMPPTCPConnectionConfiguration configBuilder;

	@Async
	public void executeAsyncTask(OfUser user, String sendTime,
			Offline offlineConfig) {
		Offline offline = new Offline();
		offline.setUsername(user.getUsername());
		offline.setCpu(offlineConfig.getCpu());
		offline.setNetwork(offlineConfig.getNetwork());
		offline.setServerIP(offlineConfig.getServerIP());
		offline.setRam(offlineConfig.getRam());
		System.out.println(
				Thread.currentThread().getName() + "---" + user.getUsername());
		try {

			Long connStart = System.currentTimeMillis();
			XMPPTCPConnectionConfiguration configBuilder;
			configBuilder = XMPPTCPConnectionConfiguration.builder()
					.setXmppDomain(domain)
					.setSecurityMode(SecurityMode.disabled)
					// .setSocketFactory(new DummySSLSocketFactory())
					.setConnectTimeout(60000).setDebuggerEnabled(false)
					// .setCustomSSLContext(SSLContext.getDefault())
					// .setEnabledSSLProtocols(sslProtocols)
					.setUsernameAndPassword(user.getUsername(), "123")
					.setHost(host).setPort(Integer.valueOf(port))
					.setSendPresence(true).build();
			XMPPTCPConnection connection = new XMPPTCPConnection(configBuilder);
			connection.connect().login();

			String msg = "{'command':'newjob','jobs':[{'createdTime':1489125659759,'id':'hp90913123','inUrl':'http://15.96.132.47/lespms/source/file/111231234345.jpg','originalFileName':'mypic.jpg','outUrl':'http://15.96.132.47/lespms/source/file/111231234345.png','pages':0,'pagesPrinted':0,'printSetting':{'copies':5,'duplex':false,'paperSize':'A4','paperType':'plain','trayIndex':0},'printerUuid':'G235POUIYERTUFJFK6544FKIFJFJKCKC','status':'new'}],'randomId':'1234567'}";
			// Presence presence = new Presence(Presence.Type.available);
			Long connEnd = 0L;
			if (connection.isAuthenticated()) {
				connEnd = System.currentTimeMillis();
				offline.setConnectTime(connEnd - connStart);

				Roster roster = Roster.getInstanceFor(connection);
				roster.addRosterListener(new RosterListener() {

					@Override
					public void presenceChanged(Presence arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void entriesUpdated(Collection<Jid> arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void entriesDeleted(Collection<Jid> arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void entriesAdded(Collection<Jid> arg0) {
						// TODO Auto-generated method stub

					}
				});
				roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
				if (!roster.isLoaded()) {
					roster.reloadAndWait();
				}
			}
			System.out.println(Thread.currentThread().getName() + "执行");
			while (true) {
				Thread.sleep(Integer.valueOf(sendTime));
				if (!connection.isConnected()) {
					connection.connect().login();
				}
				if (!connection.isAuthenticated()) {
					connection.login();
				}

				offline.setStatus(Online);
				offline.setCreateDate(new Date());
				offline.setError(null);
				offlineService.insert(offline);
				// connection.sendStanza(presence);
				sendMsg(connection, user.getUsername() + "@" + domain,
						"adminjob@" + domain, msg, Type.chat);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof NoResponseException) {
				offline.setError("No Respnose");
			} else if (e instanceof NotConnectedException) {
				offline.setError("Not Connected");
			} else {
				offline.setError("Error");
			}
			offline.setStatus(Offline);
			offline.setCreateDate(new Date());
			offlineService.insert(offline);
		}
	}

	@Async
	public void executeAsyncTaskStop(OfUser user) {
		System.out.println(
				Thread.currentThread().getName() + "---" + user.getUsername());
		try {
			XMPPTCPConnectionConfiguration configBuilder;
			configBuilder = XMPPTCPConnectionConfiguration.builder()
					.setXmppDomain(domain)
					.setSecurityMode(SecurityMode.disabled)
					// .setSocketFactory(new DummySSLSocketFactory())
					.setConnectTimeout(45000)
					// .setCustomSSLContext(SSLContext.getDefault())
					// .setEnabledSSLProtocols(sslProtocols)
					.setUsernameAndPassword(user.getUsername(), "123")
					.setHost(host).setPort(Integer.valueOf(port))
					.setSendPresence(true).build();
			XMPPTCPConnection connection = new XMPPTCPConnection(configBuilder);
			connection.connect().login();
			Presence presence = new Presence(Presence.Type.unavailable);
			connection.sendStanza(presence);

		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMsg(XMPPTCPConnection connection, String sendfrom,
			String sendTo, String msg, Type type) throws Exception {

		try {
			int i = 200;
			Message message = new Message();
			message.setBody(msg);
			// message.setTo(sendTo);
			message.setType(type);// 离线支持
			DeliveryReceiptRequest deliveryReceiptRequest = new DeliveryReceiptRequest();
			message.addExtension(deliveryReceiptRequest);
			// Presence presence = new Presence(Presence.Type.unavailable);
			connection.sendStanza(message);

			System.out.println(Thread.currentThread().getName() + ":" + i);

		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}

	@Async
	public void testTask1(Integer integer) throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + "任务" + integer);
		// Thread.sleep(2000);
	}

	
	
	// MqttTest ------------------------------------------
	@Async
	public void executeAsyncMqttTask(MqttUser mqttUser, String sendTime,
			Offline offlineConfig) {
		Offline offline = new Offline();
		offline.setTag(offlineConfig.getTag());
		offline.setUsername(mqttUser.getUsername());
		offline.setCpu(offlineConfig.getCpu());
		offline.setNetwork(offlineConfig.getNetwork());
		offline.setServerIP(offlineConfig.getServerIP());
		offline.setRam(offlineConfig.getRam());
		System.out.println(Thread.currentThread().getName() + "---"
				+ mqttUser.getUsername());
			try {
				Long connStart = System.currentTimeMillis();
				// 开始连接
				MqttClient client = new MqttClient(mqttURI, mqttUser.getUsername(),
						new MemoryPersistence());
				MqttConnectOptions options = new MqttConnectOptions();
				// 服务器是否清除session,清除session，服务器不会保持session,非持久化订阅
				options.setCleanSession(true);
//				options.setUserName(mqttUser.getUsername());
//				options.setPassword(MQTTPWD.toCharArray());
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
					MqttTopic topic = client
							.getTopic("topic-" + mqttUser.getUsername());
					// 发送包
					publish(topic, message);
					connEnd = System.currentTimeMillis();
					Thread.sleep(Integer.valueOf(sendTime));

				}
			} catch (IllegalArgumentException | MqttException
					| InterruptedException e) {
				e.printStackTrace();
				if(e instanceof MqttException){
					offline.setError(((MqttException)e).getMessage());
					offline.setStatus(Offline);
					offline.setCreateDate(new Date());
					offlineService.insert(offline);
				}
			}
	}
	
	
	@Async
	public void executeAsyncMqttTask(String sendTime,int i) {
		System.out.println(Thread.currentThread().getName());
			try {
				// 开始连接
				MqttClient client = new MqttClient(mqttURI, "client"+i,
						new MemoryPersistence());
				MqttConnectOptions options = new MqttConnectOptions();
				// 服务器是否清除session,清除session，服务器不会保持session,非持久化订阅
				options.setCleanSession(true);
//				options.setUserName(mqttUser.getUsername());
//				options.setPassword(MQTTPWD.toCharArray());
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
				System.out.println(Thread.currentThread().getName() + "执行");
				while (true) {
					MqttTopic topic = client
							.getTopic("topic-" + i);
					// 发送包
					publish(topic, message);
					Thread.sleep(Integer.valueOf(sendTime));

				}
			} catch (IllegalArgumentException | MqttException
					| InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	
	

	public void publish(MqttTopic topic, MqttMessage message)
			throws MqttPersistenceException, MqttException {
		MqttDeliveryToken token = topic.publish(message);
		token.waitForCompletion();
		System.out.println(
				"message is published completely! " + token.isComplete());
	}

}
