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
@Table(name = "TM_COLLABORATION")

public class CollaborationDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MESSAGE_ID", length = 70)
	private String messageId;

	@Column(name = "PROCESS_ID", length = 32)
	private String processId;

	@Column(name = "TASK_ID", length = 32)
	private String taskId;

	@Column(name = "USER_ID", length = 70)
	private String userId;

	@Column(name = "USER_DISPLAY_NAME", length = 255)
	private String userDisplayName;

	@Column(name = "CREATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "MESSAGE", length = 2000)
	private String message;

	/*@Column(name = "CHAT_ID", length = 70)
	private String chatId;

	@Column(name = "CHAT_DISPLAY_NAME", length = 70)
	private String chatDisplayName;

	@Column(name = "TAGGED_USER", length = 70)
	private String taggedUserId;*/


	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

/*	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	public String getChatDisplayName() {
		return chatDisplayName;
	}
	public void setChatDisplayName(String chatDisplayName) {
		this.chatDisplayName = chatDisplayName;
	}
	public String getTaggedUserId() {
		return taggedUserId;
	}
	public void setTaggedUserId(String taggedUserId) {
		this.taggedUserId = taggedUserId;
	}*/

	@Override
	public String toString() {
		return "CollaborationDo [messageId=" + messageId + ", processId=" + processId + ", taskId=" + taskId
				+ ", userId=" + userId + ", userDisplayName=" + userDisplayName + ", createdAt=" + createdAt
				+ ", message=" + message + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return messageId;
	}

}