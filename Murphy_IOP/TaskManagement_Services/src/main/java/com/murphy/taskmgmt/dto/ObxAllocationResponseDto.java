package com.murphy.taskmgmt.dto;

import java.math.BigInteger;
import java.util.List;

public class ObxAllocationResponseDto {

	private List<ObxTaskAllocationDto>  obxList;
	private List<GroupsUserDto> proOperatorList;
	private List<GroupsUserDto> obxOperatorList;
	private ResponseMessage responseMessage;
	private BigInteger unAssignedWellsNum;
	
	/**
	 * @return the obxList
	 */
	public List<ObxTaskAllocationDto> getObxList() {
		return obxList;
	}
	/**
	 * @param obxList the obxList to set
	 */
	public void setObxList(List<ObxTaskAllocationDto> obxList) {
		this.obxList = obxList;
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
	/**
	 * @return the proOperatorList
	 */
	public List<GroupsUserDto> getProOperatorList() {
		return proOperatorList;
	}
	/**
	 * @param proOperatorList the proOperatorList to set
	 */
	public void setProOperatorList(List<GroupsUserDto> proOperatorList) {
		this.proOperatorList = proOperatorList;
	}
	/**
	 * @return the obxOperatorList
	 */
	public List<GroupsUserDto> getObxOperatorList() {
		return obxOperatorList;
	}
	/**
	 * @param obxOperatorList the obxOperatorList to set
	 */
	public void setObxOperatorList(List<GroupsUserDto> obxOperatorList) {
		this.obxOperatorList = obxOperatorList;
	}
	/**
	 * @return the unAssignedWellsNum
	 */
	public BigInteger getUnAssignedWellsNum() {
		return unAssignedWellsNum;
	}
	/**
	 * @param unAssignedWellsNum the unAssignedWellsNum to set
	 */
	public void setUnAssignedWellsNum(BigInteger unAssignedWellsNum) {
		this.unAssignedWellsNum = unAssignedWellsNum;
	}
	
	
}
