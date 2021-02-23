package com.murphy.taskmgmt.dto;

import java.util.List;

import com.murphy.integration.dto.InvestigationHistoryDto;

public class AttrTempResponseDto {


	private List<CustomAttrTemplateDto> customAttr;
	private ResponseMessage responseMessage;
	private List<LocationDto> locHierarchy;
	private List<CustomAttrTemplateDto> VarianceCustomAttr;
	private List<CustomAttrTemplateDto> ObservationCustomAttr;
	private List<InvestigationHistoryDto> investigationHistoryList;
	private List<CheckListDto> checkList;
	private boolean isProactive;

	
	public List<InvestigationHistoryDto> getInvestigationHistoryList() {
		return investigationHistoryList;
	}



	public void setInvestigationHistoryList(List<InvestigationHistoryDto> investigationHistoryList) {
		this.investigationHistoryList = investigationHistoryList;
	}



	public List<CustomAttrTemplateDto> getVarianceCustomAttr() {
		return VarianceCustomAttr;
	}



	public void setVarianceCustomAttr(List<CustomAttrTemplateDto> varianceCustomAttr) {
		VarianceCustomAttr = varianceCustomAttr;
	}



	public List<CustomAttrTemplateDto> getObservationCustomAttr() {
		return ObservationCustomAttr;
	}



	public void setObservationCustomAttr(List<CustomAttrTemplateDto> observationCustomAttr) {
		ObservationCustomAttr = observationCustomAttr;
	}



	public List<LocationDto> getLocHierarchy() {
		return locHierarchy;
	}



	public void setLocHierarchy(List<LocationDto> locHierarchy) {
		this.locHierarchy = locHierarchy;
	}



	public List<CustomAttrTemplateDto> getCustomAttr() {
		return customAttr;
	}



	public void setCustomAttr(List<CustomAttrTemplateDto> customAttr) {
		this.customAttr = customAttr;
	}



	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}



	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}



	@Override
	public String toString() {
		return "AttrTempResponseDto [customAttr=" + customAttr + ", responseMessage=" + responseMessage
				+ ", locHierarchy=" + locHierarchy + ", VarianceCustomAttr=" + VarianceCustomAttr
				+ ", ObservationCustomAttr=" + ObservationCustomAttr + ", investigationHistoryList="
				+ investigationHistoryList + "]";
	}


	public List<CheckListDto> getCheckList() {
		return checkList;
	}



	public void setCheckList(List<CheckListDto> checkList) {
		this.checkList = checkList;
	}



	public boolean isProactive() {
		return isProactive;
	}



	public void setProactive(boolean isProactive) {
		this.isProactive = isProactive;
	}
	
	
}
