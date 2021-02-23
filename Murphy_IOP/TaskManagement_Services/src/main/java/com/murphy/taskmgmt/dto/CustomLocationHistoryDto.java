package com.murphy.taskmgmt.dto;

import java.math.BigDecimal;
import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class CustomLocationHistoryDto extends BaseDto{

	private List<TaskListDto>  taskList;
	private List<NonDispatchTaskDto> ndTaskList;
	private List<LocationHistoryRolledUpDto> statusCountList ;
	private ResponseMessage responseMessage;
	private BigDecimal totalCount;
	private BigDecimal pageCount;

	public BigDecimal getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(BigDecimal totalCount) {
		this.totalCount = totalCount;
	}

	public BigDecimal getPageCount() {
		return pageCount;
	}

	public void setPageCount(BigDecimal pageCount) {
		this.pageCount = pageCount;
	}
	
	public List<NonDispatchTaskDto> getNdTaskList() {
		return ndTaskList;
	}

	public void setNdTaskList(List<NonDispatchTaskDto> ndTaskList) {
		this.ndTaskList = ndTaskList;
	}


	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}


	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}

	public List<TaskListDto> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<TaskListDto> taskList) {
		this.taskList = taskList;
	}

	@Override
	public String toString() {
		return "CustomLocationHistoryDto [taskList=" + taskList + ", ndTaskList=" + ndTaskList + ", statusCountList="
				+ statusCountList + ", responseMessage=" + responseMessage + ", totalCount=" + totalCount
				+ ", pageCount=" + pageCount + "]";
	}

	public List<LocationHistoryRolledUpDto> getStatusCountList() {
		return statusCountList;
	}

	public void setStatusCountList(List<LocationHistoryRolledUpDto> statusCountList) {
		this.statusCountList = statusCountList;
	}
	
}
