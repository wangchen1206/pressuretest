package com.hp.roam;

import java.io.IOException;

import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;

import com.hp.roam.model.OfUser;

public class ThreadTask implements Runnable{

	 int port = 5222;
	 String host = "114.215.135.121";
	 String SERVERNAME3 = "dev-beta";
	 String[] sslProtocols = {"TLS", "TLSv1", "TLSv1.1", "TLSv1.2"};
	
	private OfUser user;
	
	
	public ThreadTask(OfUser user) {
		super();
		this.user = user;
	}

	@Override
	public void run() {
		System.out.println(user);
		try {
			Long connStart = System.currentTimeMillis();
			XMPPTCPConnectionConfiguration configBuilder;
			configBuilder = XMPPTCPConnectionConfiguration.builder()
					.setXmppDomain(SERVERNAME3)
					.setUsernameAndPassword(user.getUsername(), "123")
					.setSecurityMode(SecurityMode.disabled)
//					.setSocketFactory(new DummySSLSocketFactory())
					.setConnectTimeout(45000)
//					.setCustomSSLContext(sslContext)
//					.setHostnameVerifier(hostnameVerifier)
//					.setEnabledSSLProtocols(sslProtocols)
					.setHost(host).setPort(port).setSendPresence(true).build();
			XMPPTCPConnection connection = new XMPPTCPConnection(configBuilder);
			connection.connect().login();
			Long connEnd = System.currentTimeMillis();
			System.out.println(user.getUsername()+"connect use time is -------"+(connEnd - connStart)+" ms");
			String msg = "heart beat";
			while (true) {
				sendMsg(connection, "admin_pms@" + SERVERNAME3,
						"adminjob@" + SERVERNAME3, msg, Type.chat);
			}
			
//			XMPPTCPConnectionConfiguration configBuilder;
//			configBuilder = XMPPTCPConnectionConfiguration.builder()
//					.setXmppDomain(SERVERNAME3)
//					.setSecurityMode(SecurityMode.disabled)
////				.setSocketFactory(new DummySSLSocketFactory())
//					.setConnectTimeout(60000)
//					.setDebuggerEnabled(false)
////				.setCustomSSLContext(SSLContext.getDefault())
////				.setEnabledSSLProtocols(sslProtocols)
//					.setUsernameAndPassword(user.getUsername(), "123")
//					.setHost(host).setPort(Integer.valueOf(port)).setSendPresence(true).build();
//			XMPPTCPConnection connection = new XMPPTCPConnection(configBuilder);
//			connection.connect().login();
//			String msg = "{'command':'newjob','jobs':[{'createdTime':1489125659759,'id':'hp90913123','inUrl':'http://15.96.132.47/lespms/source/file/111231234345.jpg','originalFileName':'mypic.jpg','outUrl':'http://15.96.132.47/lespms/source/file/111231234345.png','pages':0,'pagesPrinted':0,'printSetting':{'copies':5,'duplex':false,'paperSize':'A4','paperType':'plain','trayIndex':0},'printerUuid':'G235POUIYERTUFJFK6544FKIFJFJKCKC','status':'new'}],'randomId':'1234567'}";
//			//Presence presence = new Presence(Presence.Type.available);
//			if(connection.isAuthenticated()){
//				Roster roster = Roster.getInstanceFor(connection);
//				roster.addRosterListener(new RosterListener() {
//					
//					@Override
//					public void presenceChanged(Presence arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void entriesUpdated(Collection<Jid> arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void entriesDeleted(Collection<Jid> arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void entriesAdded(Collection<Jid> arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
//				roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
//				if(!roster.isLoaded()){
//					roster.reloadAndWait();
//				}
//			}
//			System.out.println(Thread.currentThread().getName()+"执行");
//			while (true) {
//				Thread.sleep(3000);
//				if(!connection.isConnected()){
//					connection.connect().login();
//				}
//				if(!connection.isAuthenticated()){
//					connection.login();
//				}
//				
//				//connection.sendStanza(presence);
//				sendMsg(connection, user.getUsername()+"@" + SERVERNAME3,
//						"adminjob@"+SERVERNAME3, msg, Type.chat);
//			}
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
	
	
}
