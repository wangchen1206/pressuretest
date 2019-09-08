package com.hp.roam.model;
/**
 * @author ck
 * @date 2019年3月19日 下午2:30:16
 */
public class Board {
	
	private String sn;
	private String username;
	private String password;
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Board(String sn, String username, String password) {
		super();
		this.sn = sn;
		this.username = username;
		this.password = password;
	}
	public Board() {
		super();
	}
	@Override
	public String toString() {
		return "Board [sn=" + sn + ", username=" + username + ", password="
				+ password + "]";
	}
	

}
