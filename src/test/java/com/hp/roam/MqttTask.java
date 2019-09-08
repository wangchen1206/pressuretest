package com.hp.roam;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * @author ck
 * @date 2019年3月20日 下午4:08:02
 */
public class MqttTask implements Runnable{
	
	private String clientId;
	
	

	public String getClientId() {
		return clientId;
	}



	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	
	public MqttTask(String clientId) {
		super();
		this.clientId = clientId;
	}



	@Override
	public void run() {
		MqttClient mqttClient = null;
		try {
			mqttClient = new MqttClient("tcp://127.0.0.1:61613", clientId, new MemoryPersistence());
		} catch (MqttException e1) {
			e1.printStackTrace();
		}
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
//        options.setUserName("admin");
//        options.setPassword("admin".toCharArray());
        options.setConnectionTimeout(20000);
        options.setKeepAliveInterval(20000);
        try {
        	mqttClient.setCallback(new MqttCallback() {
				
				@Override
				public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void deliveryComplete(IMqttDeliveryToken arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void connectionLost(Throwable arg0) {
					// TODO Auto-generated method stub
					
				}
			});
        	mqttClient.connect(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
