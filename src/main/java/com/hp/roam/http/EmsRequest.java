package com.hp.roam.http;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ck
 * @date 2019年5月22日 上午10:46:56
 */
public class EmsRequest {
	
	private String content;
	
	private String subject;
	
	private List<String> to;
	
	private List<String> attachments;
	
	
	public List<String> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<String> attachments) {
		this.attachments = attachments;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(String tos) {
		List<String> receiver = new ArrayList<>();
		if(tos.contains(",")){
			String[] tos1 = tos.split(",");
			for (String to1 : tos1) {
				receiver.add(to1);
			}
		}else{
			receiver.add(tos);
		}
		this.to = receiver;
	}
	
	
}
