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
@Table(name = "TM_MESSAGE")
public class MessageDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MESSAGE_ID", nullable = false)
	private Long messageId;

	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@Column(name = "CREATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "LOCATION_CODE", length = 80)
	private String locationCode;

	@Column(name = "STATUS", length = 20)
	private String status;

	@Column(name = "CURRENT_OWNER", length = 100)
	private String currentOwner;

	@Column(name = "UPDATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	@Column(name = "COUNTRY", length = 10)
	private String country;

	@Column(name = "CONVERSATION_ID", length = 100)
	private String conversationId;

	@Column(name = "TEAMS_CHANNEL_ID", length = 100)
	private String teamsChannelId;

	@Column(name = "TEAMS_TEAM_ID", length = 100)
	private String teamsTeamId;

	@Column(name = "MESSAGE", length = 2000)
	private String message;

	@Column(name = "COMMENT", length = 2000)
	private String comment;

	@Override
	public Object getPrimaryKey() {
		return messageId;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrentOwner() {
		return currentOwner;
	}

	public void setCurrentOwner(String currentOwner) {
		this.currentOwner = currentOwner;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	public String getTeamsChannelId() {
		return teamsChannelId;
	}

	public void setTeamsChannelId(String teamsChannelId) {
		this.teamsChannelId = teamsChannelId;
	}

	public String getTeamsTeamId() {
		return teamsTeamId;
	}

	public void setTeamsTeamId(String teamsTeamId) {
		this.teamsTeamId = teamsTeamId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "MessageDo [messageId=" + messageId + ", createdBy=" + createdBy + ", createdAt=" + createdAt
				+ ", locationCode=" + locationCode + ", status=" + status + ", currentOwner=" + currentOwner
				+ ", updatedAt=" + updatedAt + ", country=" + country + ", conversationId=" + conversationId
				+ ", teamsChannelId=" + teamsChannelId + ", teamsTeamId=" + teamsTeamId + ", message=" + message
				+ ", comment=" + comment + "]";
	}

}
