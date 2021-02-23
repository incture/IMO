package com.murphy.taskmgmt.dto;

import java.util.List;

public class SsdBypassLogResponseDto {

	private SsdBypassHeaderDto ssdBypassLogHeaderdto;
	private List<SsdBypassCommentDto> ssdBypassCommentDtoList;
	private List<SsdBypassAttachementDto> ssdBypassAttachementDtoList;
	private List<SsdBypassActivityLogDto> ssdBypassActivityLogDtoList;
	private ResponseMessage ResponseMessage;

	/**
	 * @return the ssdBypassLogHeaderdto
	 */
	public SsdBypassHeaderDto getSsdBypassLogHeaderdto() {
		return ssdBypassLogHeaderdto;
	}

	/**
	 * @param ssdBypassLogHeaderdto
	 *            the ssdBypassLogHeaderdto to set
	 */
	public void setSsdBypassLogHeaderdto(SsdBypassHeaderDto ssdBypassLogHeaderdto) {
		this.ssdBypassLogHeaderdto = ssdBypassLogHeaderdto;
	}

	/**
	 * @return the ssdBypassCommentDtoList
	 */
	public List<SsdBypassCommentDto> getSsdBypassCommentDtoList() {
		return ssdBypassCommentDtoList;
	}

	/**
	 * @param ssdBypassCommentDtoList
	 *            the ssdBypassCommentDtoList to set
	 */
	public void setSsdBypassCommentDtoList(List<SsdBypassCommentDto> ssdBypassCommentDtoList) {
		this.ssdBypassCommentDtoList = ssdBypassCommentDtoList;
	}

	/**
	 * @return the ssdBypassAttachementDtoList
	 */
	public List<SsdBypassAttachementDto> getSsdBypassAttachementDtoList() {
		return ssdBypassAttachementDtoList;
	}

	/**
	 * @param ssdBypassAttachementDtoList
	 *            the ssdBypassAttachementDtoList to set
	 */
	public void setSsdBypassAttachementDtoList(List<SsdBypassAttachementDto> ssdBypassAttachementDtoList) {
		this.ssdBypassAttachementDtoList = ssdBypassAttachementDtoList;
	}

	/**
	 * @return the ssdBypassActivityLogDtoList
	 */
	public List<SsdBypassActivityLogDto> getSsdBypassActivityLogDtoList() {
		return ssdBypassActivityLogDtoList;
	}

	/**
	 * @param ssdBypassActivityLogDtoList
	 *            the ssdBypassActivityLogDtoList to set
	 */
	public void setSsdBypassActivityLogDtoList(List<SsdBypassActivityLogDto> ssdBypassActivityLogDtoList) {
		this.ssdBypassActivityLogDtoList = ssdBypassActivityLogDtoList;
	}

	/**
	 * @return the responseMessage
	 */
	public ResponseMessage getResponseMessage() {
		return ResponseMessage;
	}

	/**
	 * @param responseMessage
	 *            the responseMessage to set
	 */
	public void setResponseMessage(ResponseMessage responseMessage) {
		ResponseMessage = responseMessage;
	}

}
