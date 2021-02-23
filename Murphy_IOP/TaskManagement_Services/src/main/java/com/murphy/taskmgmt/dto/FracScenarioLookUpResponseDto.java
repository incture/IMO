package com.murphy.taskmgmt.dto;

import java.util.List;

public class FracScenarioLookUpResponseDto {
	private List<FracScenarioLookUpDto> fracScenarioLookUpdtoList;
	private ResponseMessage message;
	
	public List<FracScenarioLookUpDto> getFracScenarioLookUpdtoList() {
		return fracScenarioLookUpdtoList;
	}
	public void setFracScenarioLookUpdtoList(List<FracScenarioLookUpDto> fracScenarioLookUpdtoList) {
		this.fracScenarioLookUpdtoList = fracScenarioLookUpdtoList;
	}
	public ResponseMessage getMessage() {
		return message;
	}
	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

}
