package com.hp.roam;
/**
 * @author ck
 * @date 2019年4月19日 下午1:35:12
 */
public class Fu {
	public String name;
	public String sex;
	private String fuName;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getFuName() {
		return fuName;
	}
	public void setFuName(String fuName) {
		this.fuName = fuName;
	}
	
	private void say(){
		System.out.println("jing");
	}
}
