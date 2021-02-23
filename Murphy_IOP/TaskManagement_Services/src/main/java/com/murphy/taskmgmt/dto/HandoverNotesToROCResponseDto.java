package com.murphy.taskmgmt.dto;

import java.util.List;

public class HandoverNotesToROCResponseDto {

	List<HandoverNotesDto> handoverNotesDtoList;
	List<SSVCloseResponseDto> ssvCloseResponseList;
	List<TaskNoteDto> taskList;
	List<TaskNoteDto> enquiryList;
	ResponseMessage responseMessage;

	public List<SSVCloseResponseDto> getSsvCloseResponseList() {
		return ssvCloseResponseList;
	}

	public void setSsvCloseResponseList(List<SSVCloseResponseDto> ssvCloseResponseList) {
		this.ssvCloseResponseList = ssvCloseResponseList;
	}

	public List<HandoverNotesDto> getHandoverNotesDtoList() {
		return handoverNotesDtoList;
	}

	public void setHandoverNotesDtoList(List<HandoverNotesDto> handoverNotesDtoList) {
		this.handoverNotesDtoList = handoverNotesDtoList;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	public List<TaskNoteDto> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<TaskNoteDto> taskList) {
		this.taskList = taskList;
	}

	public List<TaskNoteDto> getEnquiryList() {
		return enquiryList;
	}

	public void setEnquiryList(List<TaskNoteDto> enquiryList) {
		this.enquiryList = enquiryList;
	}

	@Override
	public String toString() {
		return "HandoverNotesToROCResponseDto [handoverNotesDtoList=" + handoverNotesDtoList + ", ssvCloseResponseList="
				+ ssvCloseResponseList + ", responseMessage=" + responseMessage + "]";
	}

}
