package com.hp.roam;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.hp.roam.api.ApiService;
import com.hp.roam.http.BaseResponse;
import com.hp.roam.http.EmsRequest;
import com.hp.roam.model.OfUser;
import com.hp.roam.model.OnlineRate;
import com.hp.roam.model.SysUser;
import com.hp.roam.util.AESUtil;
import com.hp.roam.util.HttpUtils;
import com.mysql.jdbc.authentication.Sha256PasswordPlugin;

/**
 * @author ck
 * @date 2019年2月28日 下午2:38:53
 */
public class TestSecurity {
	/** 默认日期时间格式 */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@Test
	public void testEms(){
//		List<String> urList = new ArrayList<>();
//		urList.add("http://15.96.132.45/test/Board.xlsx");
//		EmsRequest emsRequest = new EmsRequest();
//		emsRequest.setSubject("Online Rate Report");
//		emsRequest.setContent("Attachment is the report of the "+ZonedDateTime.now()
//						.withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
//						.toLocalDateTime().toLocalDate().toString()+" 'Board Online Rate'");
//		
//		emsRequest.setAttachments(urList);
//		emsRequest.setTo("406585395@qq.com");
//		BaseResponse<String> msgResponse = ApiService.testEmsSend(emsRequest);
//		HttpUtils.logger.info("the ems messageId is {}",msgResponse.getData());
		
//		Map<String, String> map = new HashMap<>();
//		map.put("status", "OK");
//		map.put("reason", "Fail");
//		System.out.println(map.toString());
		
	}
	
	
	@Test
	public void testLocalDateTimeDec() throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        objectMapper.registerModule(javaTimeModule);
        Map<String, String> result = new HashMap<>();
        result.put("date", "2019-05-21 11:01:01");
        BaseResponse<String> response = objectMapper.readValue(objectMapper.writeValueAsString(result), BaseResponse.class);
        System.out.println(response.getDate());
	}
	
	@Test
	public void testCommonIO() throws MalformedURLException, IOException{
		//FileUtils.copyURLToFile(new URL("http://15.96.132.45/test/Board.xlsx"), new File("C:\\Java\\test\\test.xlsx"));
	}
	
	@Test
	public void testDate() throws UnsupportedEncodingException{
//		List<String> list = new ArrayList<>();
//		list.add("33");
//		list.add("22");
//		list.add("44");
//		list.add("55");
//		System.out.println(list.subList(0, 2));
//		System.out.println(list);
//		System.out.println(new Double((5548.0/2284.0)-(5548/2284))>0.0);
//		Random random = new Random();
//		System.out.println(random.nextLong());
//		System.out.println(random.nextInt(100000000));
//		System.out.println(random.nextLong());
//		System.out.println("a3bef71de1d5c8ba9c6c3d70ffc675ab5cf38ebbc4174a1179".length());
//		System.out.println(Base64Utils.encodeToString("sdfdffdfd".getBytes()));
//		System.out.println(new String(Base64Utils.decodeFromString("c2RmZGZmZGZk")));
//		System.out.println((MD5Util.md5Encode(System.currentTimeMillis()+"", null)).length());
//		Date date = new Date();
//		date.setTime(1552619503922L);
//		System.out.println(date);
		
		String msg = "jingdong";
		String password = "ck";
		String msgAes = AESUtil.encrypt(msg, password);
		System.out.println(msgAes);
		System.out.println(AESUtil.decrypt("jcgDRG37WLPy/0bwHomTzA==", "cc"));
		
	}

	@Test
	public void testBCryptPassword(){
//		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//		System.out.println(bCryptPasswordEncoder.encode("admin").trim());
//		System.out.println(bCryptPasswordEncoder.encode("abel").trim());
		
//		LocalDateTime localDateTime = LocalDateTime.now();
//		localDateTime = localDateTime.minusDays(1);
//		localDateTime = localDateTime.of(localDateTime.getYear(), localDateTime.getMonth(),
//				localDateTime.getDayOfMonth(), 23, 59, 59);
		
//		LocalDateTime localDateTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Europe/Paris")).toLocalDateTime();
//		System.out.println(localDateTime);
//		System.out.println(ZonedDateTime.now().getZone());
//		int online = 4;
//		online += 5;
//		System.out.println(online);
		
		
		OnlineRate onlineRate = new OnlineRate();
		double daySeconds = 24*60*60; 
		double rate = (daySeconds-1321)*1.0/daySeconds;
		System.out.println(rate);
		BigDecimal bg = new BigDecimal(rate).setScale(4, RoundingMode.DOWN);
		onlineRate.setOnlineRate(bg.doubleValue());
		
		
		System.out.println("aaa   "+onlineRate.getOnlineRate()*100+"%");
		BigDecimal bg1 = new BigDecimal(1-onlineRate.getOnlineRate()).setScale(4, RoundingMode.UP);
		System.out.println("sss "+bg1.doubleValue()*100+"%");
		
		HttpUtils.logger.info("the url {} is true", "http://");
	}
	
	static int port = 5223;
	static String host = "127.0.0.1";
	static String SERVERNAME3 = "wachen7.auth.hpicorp.net";
	static String[] sslProtocols = {"TLS", "TLSv1", "TLSv1.1", "TLSv1.2"};
	

	@Test
	public void testXmpp() throws Exception {
		
		KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(getClass().getResourceAsStream("C:\\Users\\wachen\\java\\jre\\lib\\security\\jssecacerts"), "changeit".toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(trustStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
		
		
		// @在openfire中是特殊字符需要转义成特殊的字符串\40 ，这里smack会对’\‘当成转义字符，所以再加上一个\.
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			
			@Override
			public boolean verify(String SERVERNAME3, SSLSession session) {
				// TODO Auto-generated method stub
				return true;
			}
		};
		XMPPTCPConnectionConfiguration configBuilder;
		configBuilder = XMPPTCPConnectionConfiguration.builder()
				.setXmppDomain(SERVERNAME3)
				.setUsernameAndPassword("dodo1000\\40openfire.com", "123")
				.setSecurityMode(SecurityMode.required)
//				.setSocketFactory(new DummySSLSocketFactory())
				.setConnectTimeout(45000)
				.setCustomSSLContext(sslContext)
				.setHostnameVerifier(hostnameVerifier)
//				.setEnabledSSLProtocols(sslProtocols)
				.setHost(host).setPort(port).setSendPresence(true).build();
		XMPPTCPConnection connection = new XMPPTCPConnection(configBuilder);
		connection.connect().login();
		String msg = "heart beat";
		while (true) {
			sendMsg(connection, "admin_pms@" + SERVERNAME3,
					"adminjob@" + SERVERNAME3, msg, Type.chat);
		}
	}

	public static void sendMsg(XMPPTCPConnection connection, String sendfrom,
			String sendTo, String msg, Type type) throws Exception {

		try {
			int i = 200;
			Message message = new Message();
			message.setBody(msg);
			message.setTo(sendTo);
			message.setType(type);// 离线支持
			DeliveryReceiptRequest deliveryReceiptRequest = new DeliveryReceiptRequest();
			message.addExtension(deliveryReceiptRequest);
			connection.sendStanza(message);

			try {
				Thread.sleep(1000);
				System.out.println(i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testTreadTask(){
		SysUser user1 = new SysUser();
		user1.setUsername("admin_pms");
		user1.setPassword("123");
		SysUser user2 = new SysUser();
		user2.setUsername("test@test.com");
		user2.setPassword("123");
		List<SysUser> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);
		//Thread thread = new Thread(new ThreadTask(user2));
		//thread.start();
//		for (SysUser sysUser : users) {
//		}
	}
	public static void main(String[] args) throws InterruptedException {
		
//		for (int i = 0; i < 10; i++) {
//			if(i == 40){
//				continue;
//			}
			OfUser user = new OfUser();
			user.setUsername("adminjob");
			Thread.sleep(3000);
			Thread thread = new Thread(new ThreadTask(user));
			thread.start();
//		}
		
		
		
	}
	
	@Test
	public void test11(){
		SysUser user1 = new SysUser();
		user1.setUsername("admin_pms");
		user1.setPassword("123");
		SysUser user2 = new SysUser();
		user2.setUsername("test\40test.com");
		user2.setPassword("123");
		List<SysUser> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);
//		Thread thread = new Thread(new ThreadTask(user2));
//		thread.start();
	}
	

	@Test
	public void testMqtt() throws MqttException, InterruptedException{
		Thread thread1 = new Thread(new MqttTask("client1"));
		Thread thread2 = new Thread(new MqttTask("client2"));
		thread1.start();
		thread2.start();
		Thread.sleep(20000);
	}
	
	@Test
	public void testBoard(){
		
	}
}
