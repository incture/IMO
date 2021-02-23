package com.murphy.taskmgmt.dto;

import java.util.ArrayList;
import java.util.List;

public class OffsetFracPackResponseDto {

	List<OffsetFracPackDto> fracPacks;
	
	ResponseMessage responseMessage;

	public List<OffsetFracPackDto> getFracPacks() {
		return fracPacks;
	}

	public void setFracPacks(List<OffsetFracPackDto> fracPacks) {
		this.fracPacks = fracPacks;
	}
    
	
	public void addFracPack(OffsetFracPackDto fracPack){
		List<OffsetFracPackDto> fracks= new ArrayList<>();
		fracks.add(fracPack);
		setFracPacks(fracks);
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	
	
	
}
