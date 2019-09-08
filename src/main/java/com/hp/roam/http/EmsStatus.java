package com.hp.roam.http;
/**
 * @author ck
 * @date 2019年5月23日 下午1:22:24
 */
public class EmsStatus {
	
	private String status;
	
	private String reason;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return "EmsStatus [status=" + status + ", reason=" + reason + "]";
	}
	
	
}
