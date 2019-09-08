package com.hp.roam.model;

import org.springframework.util.StringUtils;

/**
 * @author ck
 * @date 2019年4月2日 下午3:01:00
 */
public class MakeBoard {
	
	private String lengthOfSn;
	private String startSn;
	private String numOfSn;
	private String lengthOfUsername;
	private String lengthOfPassword;
	private String aesPassword;
	private String reAesPassword;
	
	
	public String getLengthOfSn() {
		return lengthOfSn;
	}
	public void setLengthOfSn(String lengthOfSn) {
		if (StringUtils.isEmpty(lengthOfSn)) {
			lengthOfSn = "8";
		}
		this.lengthOfSn = lengthOfSn;
	}
	public String getStartSn() {
		return startSn;
	}
	public void setStartSn(String startSn) {
		if (StringUtils.isEmpty(startSn)) {
			startSn = "0";
		}
		this.startSn = startSn;
	}
	public String getNumOfSn() {
		return numOfSn;
	}
	public void setNumOfSn(String numOfSn) {
		if (StringUtils.isEmpty(numOfSn)) {
			numOfSn = "100";
		}
		this.numOfSn = numOfSn;
	}
	public String getLengthOfUsername() {
		return lengthOfUsername;
	}
	public void setLengthOfUsername(String lengthOfUsername) {
		if (StringUtils.isEmpty(lengthOfUsername)) {
			lengthOfUsername = "16";
		}
		this.lengthOfUsername = lengthOfUsername;
	}
	public String getLengthOfPassword() {
		return lengthOfPassword;
	}
	public void setLengthOfPassword(String lengthOfPassword) {
		if (StringUtils.isEmpty(lengthOfPassword)) {
			lengthOfPassword = "16";
		}
		this.lengthOfPassword = lengthOfPassword;
	}
	public String getAesPassword() {
		return aesPassword;
	}
	public void setAesPassword(String aesPassword) {
		if(StringUtils.isEmpty(aesPassword)){
			this.aesPassword = null;
		}else{
			this.aesPassword = aesPassword;
		}
	}
	public String getReAesPassword() {
		return reAesPassword;
	}
	public void setReAesPassword(String reAesPassword) {
		if(StringUtils.isEmpty(reAesPassword)){
			this.reAesPassword = null;
		}else{
			this.reAesPassword = reAesPassword;
		}
	}
	

}
