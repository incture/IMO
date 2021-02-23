package com.murphy.taskmgmt.dto;

import java.util.Date;
import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class EIFormDto extends BaseDto {
	private String id; //permit No
	private String idOrg; //
	private String formId;
	private String locationId;
	private String locationName; //facility
	private String permHoldName;   //preJobPermitHolder
	private String permIssueName; //PreJobPermitIssuer
	private String permIssueLoginName;
//	private String contractorName;
	private Date plannedDate;
	private Date date; //plannedDateOfWork
	private Date expectTime; //estimationTimeOfCompletion
	private Date permIssueTime; //preJobSafetyDate1(Time Stamp)
	private Date permHoldTime; //permit Holder Time(Time Stamp)
	private String equipId; //equipmentId/tag
	private String workOrderNum; //WorkOrder No
	private String energyType; //energyIsolationType
	private String reason; //reasonForIsolation
	private String otherHazards; //Other Hazards
	private String status;
	private String userGroup;
	private boolean isEmpNotified; //Is LOTO Notified
	private boolean isLotoRemoved; //LOTO removed
	private boolean isLockTagRemoved; //affectedEmployeesnLock Tag
	private boolean isWAInspected;	//Work Area Inspected
	private boolean isJSAReviewed; //Jsa Reviewed
	private boolean isUpdate;
	private boolean isText;
	private boolean isAcknowledged;
	private Date createdAt;
	private Date updatedAt;
	private String shift;
	private Date lastShiftChange;
	List<IsolationDetailDto> isolationList;
	List<ActivityLogDto> activityLogList;
	List<EiAttachmentDto> attachmentList;
	List<EICommentDto> commentList;
	List<EIContractorDto> contractorDetailList;
	private String affectedPersonnelIdList;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdOrg() {
		return idOrg;
	}
	public void setIdOrg(String idOrg) {
		this.idOrg = idOrg;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public Date getPlannedDate() {
		return plannedDate;
	}
	public void setPlannedDate(Date plannedDate) {
		this.plannedDate = plannedDate;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getPermHoldName() {
		return permHoldName;
	}
	public void setPermHoldName(String permHoldName) {
		this.permHoldName = permHoldName;
	}
	public String getPermIssueName() {
		return permIssueName;
	}
	public void setPermIssueName(String permIssueName) {
		this.permIssueName = permIssueName;
	}
//	public String getContractorName() {
//		return contractorName;
//	}
//	public void setContractorName(String contractorName) {
//		this.contractorName = contractorName;
//	}
	public Date getExpectTime() {
		return expectTime;
	}
	public void setExpectTime(Date expectTime) {
		this.expectTime = expectTime;
	}
	public Date getPermIssueTime() {
		return permIssueTime;
	}
	public void setPermIssueTime(Date permIssueTime) {
		this.permIssueTime = permIssueTime;
	}
	public Date getPermHoldTime() {
		return permHoldTime;
	}
	public void setPermHoldTime(Date permHoldTime) {
		this.permHoldTime = permHoldTime;
	}
	public String getEquipId() {
		return equipId;
	}
	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}
	public String getWorkOrderNum() {
		return workOrderNum;
	}
	public void setWorkOrderNum(String workOrderNum) {
		this.workOrderNum = workOrderNum;
	}
	public String getEnergyType() {
		return energyType;
	}
	public void setEnergyType(String energyType) {
		this.energyType = energyType;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getOtherHazards() {
		return otherHazards;
	}
	public void setOtherHazards(String otherHazards) {
		this.otherHazards = otherHazards;
	}
	public boolean isEmpNotified() {
		return isEmpNotified;
	}
	public void setEmpNotified(boolean isEmpNotified) {
		this.isEmpNotified = isEmpNotified;
	}
	public boolean isLotoRemoved() {
		return isLotoRemoved;
	}
	public void setLotoRemoved(boolean isLotoRemoved) {
		this.isLotoRemoved = isLotoRemoved;
	}
	public boolean isLockTagRemoved() {
		return isLockTagRemoved;
	}
	public void setLockTagRemoved(boolean isLockTagRemoved) {
		this.isLockTagRemoved = isLockTagRemoved;
	}
	public boolean isWAInspected() {
		return isWAInspected;
	}
	public void setWAInspected(boolean isWAInspected) {
		this.isWAInspected = isWAInspected;
	}
	public boolean isJSAReviewed() {
		return isJSAReviewed;
	}
	public void setJSAReviewed(boolean isJSAReviewed) {
		this.isJSAReviewed = isJSAReviewed;
	}
	public boolean isUpdate() {
		return isUpdate;
	}
	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public List<IsolationDetailDto> getIsolationList() {
		return isolationList;
	}
	public void setIsolationList(List<IsolationDetailDto> isolationList) {
		this.isolationList = isolationList;
	}
	public List<ActivityLogDto> getActivityLogList() {
		return activityLogList;
	}
	public void setActivityLogList(List<ActivityLogDto> activityLogList) {
		this.activityLogList = activityLogList;
	}
	public List<EiAttachmentDto> getAttachmentList() {
		return attachmentList;
	}
	public void setAttachmentList(List<EiAttachmentDto> attachmentList) {
		this.attachmentList = attachmentList;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isText() {
		return isText;
	}
	public void setText(boolean isText) {
		this.isText = isText;
	}
	public String getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	public boolean isAcknowledged() {
		return isAcknowledged;
	}
	public void setAcknowledged(boolean isAcknowledged) {
		this.isAcknowledged = isAcknowledged;
	}
	public String getPermIssueLoginName() {
		return permIssueLoginName;
	}
	public void setPermIssueLoginName(String permIssueLoginName) {
		this.permIssueLoginName = permIssueLoginName;
	}
	public List<EICommentDto> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<EICommentDto> commentList) {
		this.commentList = commentList;
	}
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public Date getLastShiftChange() {
		return lastShiftChange;
	}
	public void setLastShiftChange(Date lastShiftChange) {
		this.lastShiftChange = lastShiftChange;
	}
	public List<EIContractorDto> getContractorDetailList() {
		return contractorDetailList;
	}
	public void setContractorDetailList(List<EIContractorDto> contractorDetailList) {
		this.contractorDetailList = contractorDetailList;
	}
	
	public String getAffectedPersonnelIdList() {
		return affectedPersonnelIdList;
	}
	public void setAffectedPersonnelIdList(String affectedPersonnelIdList) {
		this.affectedPersonnelIdList = affectedPersonnelIdList;
	}
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}
	
	@Override
	public Boolean getValidForUsage() {
		return null;
	}
	@Override
	public String toString() {
		return "EIFormDto [id=" + id + ", idOrg=" + idOrg + ", formId=" + formId + ", locationId=" + locationId
				+ ", locationName=" + locationName + ", permHoldName=" + permHoldName + ", permIssueName="
				+ permIssueName + ", permIssueLoginName=" + permIssueLoginName + ", plannedDate=" + plannedDate
				+ ", date=" + date + ", expectTime=" + expectTime + ", permIssueTime=" + permIssueTime
				+ ", permHoldTime=" + permHoldTime + ", equipId=" + equipId + ", workOrderNum=" + workOrderNum
				+ ", energyType=" + energyType + ", reason=" + reason + ", otherHazards=" + otherHazards + ", status="
				+ status + ", userGroup=" + userGroup + ", isEmpNotified=" + isEmpNotified + ", isLotoRemoved="
				+ isLotoRemoved + ", isLockTagRemoved=" + isLockTagRemoved + ", isWAInspected=" + isWAInspected
				+ ", isJSAReviewed=" + isJSAReviewed + ", isUpdate=" + isUpdate + ", isText=" + isText
				+ ", isAcknowledged=" + isAcknowledged + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", shift=" + shift + ", lastShiftChange=" + lastShiftChange + ", isolationList=" + isolationList
				+ ", activityLogList=" + activityLogList + ", attachmentList=" + attachmentList + ", commentList="
				+ commentList + ", contractorDetailList=" + contractorDetailList + ", affectedPersonnelIdList="
				+ affectedPersonnelIdList + "]";
	}

}
