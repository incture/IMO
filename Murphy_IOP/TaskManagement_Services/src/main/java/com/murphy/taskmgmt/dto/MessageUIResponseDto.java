package com.murphy.taskmgmt.dto;

import java.util.List;

public class MessageUIResponseDto {

	private List<MessageDto> messageList;
	private int totalCount;
	private int pageCount;
	private ResponseMessage response;

	public List<MessageDto> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<MessageDto> messageList) {
		this.messageList = messageList;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public ResponseMessage getResponse() {
		return response;
	}

	public void setResponse(ResponseMessage response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "MessageUIResponseDto [messageList=" + messageList + ", totalCount=" + totalCount + ", pageCount="
				+ pageCount + ", response=" + response + "]";
	}

}
