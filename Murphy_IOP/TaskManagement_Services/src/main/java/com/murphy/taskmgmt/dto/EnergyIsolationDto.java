package com.murphy.taskmgmt.dto;

import java.util.List;

public class EnergyIsolationDto {

	private String formId;
	private String permitNo;
	private String contractorPerformingWork;
	private String plannedDateofWork;
	private String estCompletionTime;
	private String facility;
	private String equipmentTag;
	private String permitHolder;
	private String workOrderNo;
	private String isolationReason;
	private String jsaIsReviewed;
	private String isLOTONotified;
	private String preJobPermitIssuer;
	private String preJobPermitHolder;
	private String preJobSafetyDate1;
	private String preJobSafetyDate2;
	private String otherHazards;
	private String isolationPermitHolder;
	private String isolationPermitIssuer;
	private String isolationHolderDate;
	private String isolationIssuerDate;
	private String isolationHolderTime;
	private String isolationIssuerTime;
	private String servicePermitHolder;
	private String servicePermitIssuer;
	private String serviceIssuerDate;
	private String serviceHolderDate;
	private String serviceHolderTime;
	private String serviceIssuerTime;
	private String isWorkAreaInspected;
	private String isAffectedLockRemoved;
	private String isLOTORemovalNotified;
	private String energyIsolatedType;
	private List<IsolationDetailsDto> isolationDetailsDtoList;
	private List<AffectedPersonnelDto> affectedPersonnelList;
	private String affectedPersonnelIdList;
	
	
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getPermitNo() {
		return permitNo;
	}
	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}
	public String getContractorPerformingWork() {
		return contractorPerformingWork;
	}
	public void setContractorPerformingWork(String contractorPerformingWork) {
		this.contractorPerformingWork = contractorPerformingWork;
	}
	public String getPlannedDateofWork() {
		return plannedDateofWork;
	}
	public void setPlannedDateofWork(String plannedDateofWork) {
		this.plannedDateofWork = plannedDateofWork;
	}
	public String getEstCompletionTime() {
		return estCompletionTime;
	}
	public void setEstCompletionTime(String estCompletionTime) {
		this.estCompletionTime = estCompletionTime;
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	public String getEquipmentTag() {
		return equipmentTag;
	}
	public void setEquipmentTag(String equipmentTag) {
		this.equipmentTag = equipmentTag;
	}
	public String getPermitHolder() {
		return permitHolder;
	}
	public void setPermitHolder(String permitHolder) {
		this.permitHolder = permitHolder;
	}
	public String getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}
	public String getIsolationReason() {
		return isolationReason;
	}
	public void setIsolationReason(String isolationReason) {
		this.isolationReason = isolationReason;
	}
	public String getJsaIsReviewed() {
		return jsaIsReviewed;
	}
	public void setJsaIsReviewed(String jsaIsReviewed) {
		this.jsaIsReviewed = jsaIsReviewed;
	}
	public String getIsLOTONotified() {
		return isLOTONotified;
	}
	public void setIsLOTONotified(String isLOTONotified) {
		this.isLOTONotified = isLOTONotified;
	}
	public String getPreJobPermitIssuer() {
		return preJobPermitIssuer;
	}
	public void setPreJobPermitIssuer(String preJobPermitIssuer) {
		this.preJobPermitIssuer = preJobPermitIssuer;
	}
	public String getPreJobPermitHolder() {
		return preJobPermitHolder;
	}
	public void setPreJobPermitHolder(String preJobPermitHolder) {
		this.preJobPermitHolder = preJobPermitHolder;
	}
	public String getPreJobSafetyDate1() {
		return preJobSafetyDate1;
	}
	public void setPreJobSafetyDate1(String preJobSafetyDate1) {
		this.preJobSafetyDate1 = preJobSafetyDate1;
	}
	public String getPreJobSafetyDate2() {
		return preJobSafetyDate2;
	}
	public void setPreJobSafetyDate2(String preJobSafetyDate2) {
		this.preJobSafetyDate2 = preJobSafetyDate2;
	}
	public String getOtherHazards() {
		return otherHazards;
	}
	public void setOtherHazards(String otherHazards) {
		this.otherHazards = otherHazards;
	}
	public String getIsolationPermitHolder() {
		return isolationPermitHolder;
	}
	public void setIsolationPermitHolder(String isolationPermitHolder) {
		this.isolationPermitHolder = isolationPermitHolder;
	}
	public String getIsolationPermitIssuer() {
		return isolationPermitIssuer;
	}
	public void setIsolationPermitIssuer(String isolationPermitIssuer) {
		this.isolationPermitIssuer = isolationPermitIssuer;
	}
	public String getIsolationHolderDate() {
		return isolationHolderDate;
	}
	public void setIsolationHolderDate(String isolationHolderDate) {
		this.isolationHolderDate = isolationHolderDate;
	}
	public String getIsolationIssuerDate() {
		return isolationIssuerDate;
	}
	public void setIsolationIssuerDate(String isolationIssuerDate) {
		this.isolationIssuerDate = isolationIssuerDate;
	}
	public String getIsolationHolderTime() {
		return isolationHolderTime;
	}
	public void setIsolationHolderTime(String isolationHolderTime) {
		this.isolationHolderTime = isolationHolderTime;
	}
	public String getIsolationIssuerTime() {
		return isolationIssuerTime;
	}
	public void setIsolationIssuerTime(String isolationIssuerTime) {
		this.isolationIssuerTime = isolationIssuerTime;
	}
	public String getServicePermitHolder() {
		return servicePermitHolder;
	}
	public void setServicePermitHolder(String servicePermitHolder) {
		this.servicePermitHolder = servicePermitHolder;
	}
	public String getServicePermitIssuer() {
		return servicePermitIssuer;
	}
	public void setServicePermitIssuer(String servicePermitIssuer) {
		this.servicePermitIssuer = servicePermitIssuer;
	}
	public String getServiceIssuerDate() {
		return serviceIssuerDate;
	}
	public void setServiceIssuerDate(String serviceIssuerDate) {
		this.serviceIssuerDate = serviceIssuerDate;
	}
	public String getServiceHolderDate() {
		return serviceHolderDate;
	}
	public void setServiceHolderDate(String serviceHolderDate) {
		this.serviceHolderDate = serviceHolderDate;
	}
	public String getServiceHolderTime() {
		return serviceHolderTime;
	}
	public void setServiceHolderTime(String serviceHolderTime) {
		this.serviceHolderTime = serviceHolderTime;
	}
	public String getServiceIssuerTime() {
		return serviceIssuerTime;
	}
	public void setServiceIssuerTime(String serviceIssuerTime) {
		this.serviceIssuerTime = serviceIssuerTime;
	}
	public String getIsWorkAreaInspected() {
		return isWorkAreaInspected;
	}
	public void setIsWorkAreaInspected(String isWorkAreaInspected) {
		this.isWorkAreaInspected = isWorkAreaInspected;
	}
	public String getIsAffectedLockRemoved() {
		return isAffectedLockRemoved;
	}
	public void setIsAffectedLockRemoved(String isAffectedLockRemoved) {
		this.isAffectedLockRemoved = isAffectedLockRemoved;
	}
	
	public String getIsLOTORemovalNotified() {
		return isLOTORemovalNotified;
	}
	public void setIsLOTORemovalNotified(String isLOTORemovalNotified) {
		this.isLOTORemovalNotified = isLOTORemovalNotified;
	}
	
	
	public String getEnergyIsolatedType() {
		return energyIsolatedType;
	}
	public void setEnergyIsolatedType(String energyIsolatedType) {
		this.energyIsolatedType = energyIsolatedType;
	}
	public List<IsolationDetailsDto> getIsolationDetailsDtoList() {
		return isolationDetailsDtoList;
	}
	public void setIsolationDetailsDtoList(List<IsolationDetailsDto> isolationDetailsDtoList) {
		this.isolationDetailsDtoList = isolationDetailsDtoList;
	}
	
	public List<AffectedPersonnelDto> getAffectedPersonnelList() {
		return affectedPersonnelList;
	}
	public void setAffectedPersonnelList(List<AffectedPersonnelDto> affectedPersonnelList) {
		this.affectedPersonnelList = affectedPersonnelList;
	}
	public String getAffectedPersonnelIdList() {
		return affectedPersonnelIdList;
	}
	public void setAffectedPersonnelIdList(String affectedPersonnelIdList) {
		this.affectedPersonnelIdList = affectedPersonnelIdList;
	}
	@Override
	public String toString() {
		return "EnergyIsolationDto [formId=" + formId + ", permitNo=" + permitNo + ", contractorPerformingWork="
				+ contractorPerformingWork + ", plannedDateofWork=" + plannedDateofWork + ", estCompletionTime="
				+ estCompletionTime + ", facility=" + facility + ", equipmentTag=" + equipmentTag + ", permitHolder="
				+ permitHolder + ", workOrderNo=" + workOrderNo + ", isolationReason=" + isolationReason
				+ ", jsaIsReviewed=" + jsaIsReviewed + ", isLOTONotified=" + isLOTONotified + ", preJobPermitIssuer="
				+ preJobPermitIssuer + ", preJobPermitHolder=" + preJobPermitHolder + ", preJobSafetyDate1="
				+ preJobSafetyDate1 + ", preJobSafetyDate2=" + preJobSafetyDate2 + ", otherHazards=" + otherHazards
				+ ", isolationPermitHolder=" + isolationPermitHolder + ", isolationPermitIssuer="
				+ isolationPermitIssuer + ", isolationHolderDate=" + isolationHolderDate + ", isolationIssuerDate="
				+ isolationIssuerDate + ", isolationHolderTime=" + isolationHolderTime + ", isolationIssuerTime="
				+ isolationIssuerTime + ", servicePermitHolder=" + servicePermitHolder + ", servicePermitIssuer="
				+ servicePermitIssuer + ", serviceIssuerDate=" + serviceIssuerDate + ", serviceHolderDate="
				+ serviceHolderDate + ", serviceHolderTime=" + serviceHolderTime + ", serviceIssuerTime="
				+ serviceIssuerTime + ", isWorkAreaInspected=" + isWorkAreaInspected + ", isAffectedLockRemoved="
				+ isAffectedLockRemoved + ", isLOTORemovalNotified=" + isLOTORemovalNotified + ", energyIsolatedType="
				+ energyIsolatedType + ", isolationDetailsDtoList=" + isolationDetailsDtoList
				+ ", affectedPersonnelList=" + affectedPersonnelList + ", affectedPersonnelIdList="
				+ affectedPersonnelIdList + "]";
	}
	
	
	
	
	
	
	
}
