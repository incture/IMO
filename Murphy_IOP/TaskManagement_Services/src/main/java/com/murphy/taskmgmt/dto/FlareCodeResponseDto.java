package com.murphy.taskmgmt.dto;

import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class FlareCodeResponseDto extends BaseDto{

	private List<FlareCodeDto> dtoList;
	private ResponseMessage message;
	
	public List<FlareCodeDto> getDtoList() {
		return dtoList;
	}

	public void setDtoList(List<FlareCodeDto> dtoList) {
		this.dtoList = dtoList;
	}

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
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

}
