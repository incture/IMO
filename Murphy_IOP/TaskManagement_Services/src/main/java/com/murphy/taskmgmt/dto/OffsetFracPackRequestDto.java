package com.murphy.taskmgmt.dto;

import java.util.List;

public class OffsetFracPackRequestDto {
private String description; 
private List<OffsetFracPackDto> fracPacks;
private ResponseMessage responseMessage;



public ResponseMessage getResponseMessage() {
	return responseMessage;
}

public void setResponseMessage(ResponseMessage responseMessage) {
	this.responseMessage = responseMessage;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public List<OffsetFracPackDto> getFracPacks() {
	return fracPacks;
}

public void setFracPacks(List<OffsetFracPackDto> fracPacks) {
	this.fracPacks = fracPacks;
}

}
