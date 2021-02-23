package com.murphy.taskmgmt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TM_USER_IDP_MAPPING")
public class UserIDPMappingDo implements BaseDo {

	@Column(name = "SERIAL_ID", length = 150)
	private String serialId;
	
	@Column(name = "BLACKLINE_ID", length = 150)
	private String blackLineId;
	
	@Column(name = "SAP_ID", length = 150)
	private String sapId;

	public String getSapId() {
		return sapId;
	}

	public void setSapId(String sapId) {
		this.sapId = sapId;
	}

	public String getBlackLineId() {
		return blackLineId;
	}

	public void setBlackLineId(String blackLineId) {
		this.blackLineId = blackLineId;
	}

	@Column(name = "USER_FIRST_NAME", length = 100)
	private String userFirstName;

	@Column(name = "USER_LAST_NAME", length = 100)
	private String userLastName;

	@Id
	@Column(name = "USER_EMAIL", length = 100)
	private String userEmail;

	@Column(name = "USER_ROLE", length = 80)
	private String userRole;


	@Column(name = "USER_LOGIN_NAME", length = 100)
	private String userLoginName;
	
	@Column(name = "TASK_ASSIGNABLE", length = 10)
	private String taskAssignable;
	
	
	@Column(name = "P_ID",length =10)
	private String pId;
	
	

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserLoginName() {
		return userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	public String getTaskAssignable() {
		return taskAssignable;
	}

	public void setTaskAssignable(String taskAssignable) {
		this.taskAssignable = taskAssignable;
	}

	@Override
	public String toString() {
		return "UserIDPMappingDo [serialId=" + serialId + ", blackLineId=" + blackLineId + ", sapId=" + sapId
				+ ", userFirstName=" + userFirstName + ", userLastName=" + userLastName + ", userEmail=" + userEmail
				+ ", userRole=" + userRole + ", userLoginName=" + userLoginName + ", taskAssignable=" + taskAssignable
				+ ", pId=" + pId + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return userLoginName;
	}

}
