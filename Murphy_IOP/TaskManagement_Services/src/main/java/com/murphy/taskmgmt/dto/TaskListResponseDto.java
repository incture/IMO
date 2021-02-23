package com.murphy.taskmgmt.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class TaskListResponseDto{
	
	private List<TaskListDto>  taskList;	
	private ResponseMessage responseMessage;	
	private BigDecimal totalCount;
	private int pageCount;	
	
	Set<String> locationFilterList;
	Set<String> taskTypeFilterList;
	Set<String> assignedToFilterList;
	Set<String> taskClassficationFilterList ;
	Set<String> subClassficationFilterList ;
	
	public Set<String> getLocationFilterList() {
		return locationFilterList;
	}
	public void setLocationFilterList(Set<String> locationFilterList) {
		this.locationFilterList = locationFilterList;
	}
	public Set<String> getTaskTypeFilterList() {
		return taskTypeFilterList;
	}
	public void setTaskTypeFilterList(Set<String> taskTypeFilterList) {
		this.taskTypeFilterList = taskTypeFilterList;
	}
	public Set<String> getAssignedToFilterList() {
		return assignedToFilterList;
	}
	public void setAssignedToFilterList(Set<String> assignedToFilterList) {
		this.assignedToFilterList = assignedToFilterList;
	}
	public Set<String> getTaskClassficationFilterList() {
		return taskClassficationFilterList;
	}
	public void setTaskClassficationFilterList(Set<String> taskClassficationFilterList) {
		this.taskClassficationFilterList = taskClassficationFilterList;
	}
	public Set<String> getSubClassficationFilterList() {
		return subClassficationFilterList;
	}
	public void setSubClassficationFilterList(Set<String> subClassficationFilterList) {
		this.subClassficationFilterList = subClassficationFilterList;
	}
	public Set<String> getIssueClassficationFilterList() {
		return issueClassficationFilterList;
	}
	public void setIssueClassficationFilterList(Set<String> issueClassficationFilterList) {
		this.issueClassficationFilterList = issueClassficationFilterList;
	}
	public Set<String> getCreatedByFilterList() {
		return createdByFilterList;
	}
	public void setCreatedByFilterList(Set<String> createdByFilterList) {
		this.createdByFilterList = createdByFilterList;
	}

	Set<String> issueClassficationFilterList;
	Set<String> createdByFilterList;
	
	public BigDecimal getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(BigDecimal totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageSize) {
		this.pageCount = pageSize;
	}
	public List<TaskListDto> getTaskList() {
		return taskList;
	}
	public void setTaskList(List<TaskListDto> taskList) {
		this.taskList = taskList;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	@Override
	public String toString() {
		return "TaskListResponseDto [taskList=" + taskList + ", responseMessage=" + responseMessage + "]";
	}
	
	
	
	
	
	
}
