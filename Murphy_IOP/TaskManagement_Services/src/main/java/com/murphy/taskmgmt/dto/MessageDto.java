package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class MessageDto extends BaseDto {

	private Long messageId;
	private String createdBy;
	private Long createdAt;
	private String locationCode;
	private String locationText;
	private String muwi;
	private String tier;
	private String locationType;
	private String status;
	private String currentOwner;
	private Long updatedAt;
	private String message;
	private String country;
	private String conversationId;
	private String teamsChannelId;
	private String teamsTeamId;
	private String comment;
	private Boolean hasInvestigation;
	private Boolean hasDispatch;
	private String dispatchTaskId;
	private String investigationTaskId;
	private String investigationProcessId;

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

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
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

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
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

	public String getLocationText() {
		return locationText;
	}

	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}

	public String getMuwi() {
		return muwi;
	}

	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public Boolean getHasInvestigation() {
		return hasInvestigation;
	}

	public void setHasInvestigation(Boolean hasInvestigation) {
		this.hasInvestigation = hasInvestigation;
	}

	public Boolean getHasDispatch() {
		return hasDispatch;
	}

	public void setHasDispatch(Boolean hasDispatch) {
		this.hasDispatch = hasDispatch;
	}

	public String getDispatchTaskId() {
		return dispatchTaskId;
	}

	public void setDispatchTaskId(String dispatchTaskId) {
		this.dispatchTaskId = dispatchTaskId;
	}

	public String getInvestigationTaskId() {
		return investigationTaskId;
	}

	public void setInvestigationTaskId(String investigationTaskId) {
		this.investigationTaskId = investigationTaskId;
	}

	public String getInvestigationProcessId() {
		return investigationProcessId;
	}

	public void setInvestigationProcessId(String investigationProcessId) {
		this.investigationProcessId = investigationProcessId;
	}

	@Override
	public String toString() {
		return "MessageDto [messageId=" + messageId + ", createdBy=" + createdBy + ", createdAt=" + createdAt
				+ ", locationCode=" + locationCode + ", locationText=" + locationText + ", muwi=" + muwi + ", tier="
				+ tier + ", locationType=" + locationType + ", status=" + status + ", currentOwner=" + currentOwner
				+ ", updatedAt=" + updatedAt + ", message=" + message + ", country=" + country + ", conversationId="
				+ conversationId + ", teamsChannelId=" + teamsChannelId + ", teamsTeamId=" + teamsTeamId + ", comment="
				+ comment + ", hasInvestigation=" + hasInvestigation + ", hasDispatch=" + hasDispatch
				+ ", dispatchTaskId=" + dispatchTaskId + ", investigationTaskId=" + investigationTaskId
				+ ", investigationProcessId=" + investigationProcessId + "]";
	}

	@Override
	public Boolean getValidForUsage() {
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {

	}

}
