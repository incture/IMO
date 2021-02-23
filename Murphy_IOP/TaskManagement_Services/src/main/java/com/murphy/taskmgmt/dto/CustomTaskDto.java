package com.murphy.taskmgmt.dto;

import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;
public class CustomTaskDto extends BaseDto {

	private String pointId;
	private String muwiId;
	private TaskEventsDto taskEventDto;
	private List<CollaborationDto> collabrationDtos;
	private List<CustomAttrTemplateDto> customAttr;
	private List<NonDispatchTaskDto> ndTaskList;
	private List<CustomAttrTemplateDto> varianceCustomAttr;
	private List<CustomAttrTemplateDto> observationCustomAttr;
	private List<TaskSubmittedDto> taskSubmittedList;
	private List<RootCauseInstDto> rootCauseList;
	private List<LocationDto> locHierarchy;
	private ResponseMessage responseMessage;
	
	private List<CheckListDto> checkList; 
	private String forwardToGrp;
	private boolean isProactive;
	private boolean isFutureTask;
	private String userType;
	private String hopperId;
	private String loggedInUserId;
	private String loggedInUserGrp;
	private String updateType;
	
	//	private String status;
	//	private String userType;
	//	private List<String> nonDispatchTasks;
	//	private List<AttachmentDto> attachmentDtos;
	//	private List<InvestigationHistoryDto> investigationHistoryList;

	public List<CheckListDto> getCheckList() {
		return checkList;
	}

	public void setCheckList(List<CheckListDto> checkList) {
		this.checkList = checkList;
	}

	public List<CustomAttrTemplateDto> getVarianceCustomAttr() {
		return varianceCustomAttr;
	}

	public void setVarianceCustomAttr(List<CustomAttrTemplateDto> varianceCustomAttr) {
		this.varianceCustomAttr = varianceCustomAttr;
	}

	public List<CustomAttrTemplateDto> getObservationCustomAttr() {
		return observationCustomAttr;
	}

	public void setObservationCustomAttr(List<CustomAttrTemplateDto> observationCustomAttr) {
		this.observationCustomAttr = observationCustomAttr;
	}

	public List<TaskSubmittedDto> getTaskSubmittedList() {
		return taskSubmittedList;
	}

	public void setTaskSubmittedList(List<TaskSubmittedDto> taskSubmittedList) {
		this.taskSubmittedList = taskSubmittedList;
	}

	//	public String getStatus() {
	//		return status;
	//	}
	//
	//	public void setStatus(String status) {
	//		this.status = status;
	//	}

	//	public String getUserType() {
	//		return userType;
	//	}
	//
	//	public void setUserType(String userType) {
	//		this.userType = userType;
	//	}

	public List<RootCauseInstDto> getRootCauseList() {
		return rootCauseList;
	}

	public void setRootCauseList(List<RootCauseInstDto> rootCauseList) {
		this.rootCauseList = rootCauseList;
	}

	public String getMuwiId() {
		return muwiId;
	}

	public void setMuwiId(String muwiId) {
		this.muwiId = muwiId;
	}

	public List<NonDispatchTaskDto> getNdTaskList() {
		return ndTaskList;
	}

	public void setNdTaskList(List<NonDispatchTaskDto> ndTaskList) {
		this.ndTaskList = ndTaskList;
	}

	public TaskEventsDto getTaskEventDto() {
		return taskEventDto;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	/*public List<String> getNonDispatchTasks() {
		return nonDispatchTasks;
	}

	public void setNonDispatchTasks(List<String> nonDispatchTasks) {
		this.nonDispatchTasks = nonDispatchTasks;
	}*/

	public List<CustomAttrTemplateDto> getCustomAttr() {
		return customAttr;
	}

	public void setCustomAttr(List<CustomAttrTemplateDto> customAttr) {
		this.customAttr = customAttr;
	}

	public void setTaskEventDto(TaskEventsDto taskEventDto) {
		this.taskEventDto = taskEventDto;
	}

	public List<CollaborationDto> getCollabrationDtos() {
		return collabrationDtos;
	}

	public void setCollabrationDtos(List<CollaborationDto> collabrationDtos) {
		this.collabrationDtos = collabrationDtos;
	}

	//	public List<AttachmentDto> getAttachmentDtos() {
	//		return attachmentDtos;
	//	}
	//
	//	public void setAttachmentDtos(List<AttachmentDto> attachmentDtos) {
	//		this.attachmentDtos = attachmentDtos;
	//	}

	//	public List<InvestigationHistoryDto> getInvestigationHistoryList() {
	//	return investigationHistoryList;
	// }
	//
	// public void setInvestigationHistoryList(List<InvestigationHistoryDto> list) {
	//	this.investigationHistoryList = list;
	// }

	
	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	public String getForwardToGrp() {
		return forwardToGrp;
	}

	public void setForwardToGrp(String forwardToGrp) {
		this.forwardToGrp = forwardToGrp;
	}

	public boolean isProactive() {
		return isProactive;
	}

	public void setProactive(boolean isProactive) {
		this.isProactive = isProactive;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getHopperId() {
		return hopperId;
	}

	public void setHopperId(String hopperId) {
		this.hopperId = hopperId;
	}

	public String getLoggedInUserId() {
		return loggedInUserId;
	}

	public void setLoggedInUserId(String loggedInUserId) {
		this.loggedInUserId = loggedInUserId;
	}

	public String getLoggedInUserGrp() {
		return loggedInUserGrp;
	}

	public void setLoggedInUserGrp(String loggedInUserGrp) {
		this.loggedInUserGrp = loggedInUserGrp;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public List<LocationDto> getLocHierarchy() {
		return locHierarchy;
	}

	public void setLocHierarchy(List<LocationDto> locHierarchy) {
		this.locHierarchy = locHierarchy;
	}

	public boolean isFutureTask() {
		return isFutureTask;
	}

	public void setFutureTask(boolean isFutureTask) {
		this.isFutureTask = isFutureTask;
	}

	@Override
	public String toString() {
		return "CustomTaskDto [pointId=" + pointId + ", muwiId=" + muwiId + ", taskEventDto=" + taskEventDto
				+ ", collabrationDtos=" + collabrationDtos + ", customAttr=" + customAttr + ", ndTaskList=" + ndTaskList
				+ ", varianceCustomAttr=" + varianceCustomAttr + ", observationCustomAttr=" + observationCustomAttr
				+ ", taskSubmittedList=" + taskSubmittedList + ", rootCauseList=" + rootCauseList + ", locHierarchy="
				+ locHierarchy + ", responseMessage=" + responseMessage + ", checkList=" + checkList + ", forwardToGrp="
				+ forwardToGrp + ", isProactive=" + isProactive + ", isFutureTask=" + isFutureTask + ", userType="
				+ userType + ", hopperId=" + hopperId + ", loggedInUserId=" + loggedInUserId + ", loggedInUserGrp="
				+ loggedInUserGrp + ", updateType=" + updateType + "]";
	}
	
	

}
