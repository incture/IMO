package com.murphy.taskmgmt.dto;

import java.util.Date;
import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class CollaborationDto extends BaseDto {

	private String messageId;
	private String processId;
	private String taskId;
	private String userId;
	private String userDisplayName;
	private Date createdAt;
	private String message;
	private List<AttachmentDto> attachmentDetails;
	private String createdAtDisplay;
//	private String documentUrl;

/*	private String chatId;
	private String chatDisplayName;
	private String taggedUserId;*/
//	private List<MessageDto> messageDto;



//	public String getDocumentUrl() {
//		return documentUrl;
//	}
//	public void setDocumentUrl(String documentUrl) {
//		this.documentUrl = documentUrl;
//	}
	public List<AttachmentDto> getAttachmentDetails() {
		return attachmentDetails;
	}
	public void setAttachmentDetails(List<AttachmentDto> attachmentDetails) {
		this.attachmentDetails = attachmentDetails;
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

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getCreatedAtDisplay() {
		return createdAtDisplay;
	}
	public void setCreatedAtDisplay(String createdAtDisplay) {
		this.createdAtDisplay = createdAtDisplay;
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
	} 
	public List<MessageDto> getMessageDto() {
	return messageDto;
	}

	public void setMessageDto(List<MessageDto> messageDto) {
	this.messageDto = messageDto;
	}
*/

	
@Override
public String toString() {
	return "CollaborationDto [messageId=" + messageId + ", processId=" + processId + ", taskId=" + taskId + ", userId="
			+ userId + ", userDisplayName=" + userDisplayName + ", createdAt=" + createdAt + ", message=" + message
			+ ", attachmentDetails=" + attachmentDetails + ", createdAtDisplay=" + createdAtDisplay + "]";
}

	@Override
	public Boolean getValidForUsage() {
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {

	}
}
