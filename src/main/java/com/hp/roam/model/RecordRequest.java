package com.hp.roam.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author ck
 * @date 2019年5月7日 下午2:15:10
 */
public class RecordRequest {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startTime;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;
	
	private String deviceId;
	
	private List<String> deviceIds = new ArrayList<>();
	
	private int pageNum;
	
	private int pageSize;
	
	
	
	public List<String> getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(List<SysPlatformBoard> sysPlatformBoards) {
		for (SysPlatformBoard sysPlatformBoard : sysPlatformBoards) {
			this.deviceIds.add(sysPlatformBoard.getOpenfire_username());
		}
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "RecordRequest [startTime=" + startTime + ", endTime=" + endTime
				+ ", deviceId=" + deviceId + "]";
	}
	
	
}
