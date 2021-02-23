package com.murphy.taskmgmt.dto;

public class UserTaskCount {

	private String userName;
	private int count;


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "UserTaskCount [userName=" + userName + ", count=" + count + "]";
	}

	
}
