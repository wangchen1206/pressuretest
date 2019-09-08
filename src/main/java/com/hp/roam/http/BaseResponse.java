package com.hp.roam.http;

import java.time.LocalDateTime;

/**
 * @author ck
 * @param <T>
 * @date 2019年5月22日 上午10:50:25
 */
public class BaseResponse<T> {
	
	private Integer code;
	
	private String msg;
	
	private T data;
	
	private LocalDateTime date;
	
	private boolean success;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "BaseResponse [code=" + code + ", msg=" + msg + ", data=" + data
				+ ", date=" + date + ", success=" + success + "]";
	}

	
}
