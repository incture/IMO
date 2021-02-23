package com.murphy.geotab;

import java.util.List;

import com.murphy.taskmgmt.dto.ResponseMessage;

/**
 * @author INC00718
 *
 */
public class NearestUserDtoResponse {
	
	private List<NearestUserDto> nearestUsers;
	private NearestUserDto nearestUserDto;
	ResponseMessage responseMessage;
	
	/**
	 * @return the nearestUsers
	 */
	public List<NearestUserDto> getNearestUsers() {
		return nearestUsers;
	}
	/**
	 * @param nearestUsers the nearestUsers to set
	 */
	public void setNearestUsers(List<NearestUserDto> nearestUsers) {
		this.nearestUsers = nearestUsers;
	}
	/**
	 * @return the responseMessage
	 */
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	/**
	 * @param responseMessage the responseMessage to set
	 */
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NearestUserDtoResponse [nearestUsers=" + nearestUsers + ", responseMessage=" + responseMessage + "]";
	}
	public NearestUserDto getNearestUserDto() {
		return nearestUserDto;
	}
	public void setNearestUserDto(NearestUserDto nearestUserDto) {
		this.nearestUserDto = nearestUserDto;
	}
}
