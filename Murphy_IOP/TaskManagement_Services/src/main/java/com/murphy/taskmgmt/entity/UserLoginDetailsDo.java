package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "USER_LOGIN_DETAILS")
public class UserLoginDetailsDo implements BaseDo, Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "USER_EMAIL", length = 100)
	private String userEmail;
	
	@Column(name = "LAST_LOGIN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoginTime;

	@Override
	public Object getPrimaryKey() {
		return userEmail;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Override
	public String toString() {
		return "UserLoginDetailsDo [userEmail=" + userEmail + ", lastLoginTime=" + lastLoginTime + "]";
	}	
	
}
