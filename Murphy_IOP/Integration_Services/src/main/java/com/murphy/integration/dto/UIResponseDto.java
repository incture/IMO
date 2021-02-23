package com.murphy.integration.dto;

import java.util.List;

public class UIResponseDto {

	private List<EnersightProveMonthlyDto> enersightProveMonthlyDtoList;
	private List<EnersightProveDailyDto> enersightProveDailyDtoList;
	private List<InvestigationHistoryDto> investigationHistoryDtoList;
	private CommentsTbDto commentsTbDto;
	private ResponseMessage responseMessage;
	
	public List<EnersightProveMonthlyDto> getEnersightProveMonthlyDtoList() {
		return enersightProveMonthlyDtoList;
	}
	public void setEnersightProveMonthlyDtoList(List<EnersightProveMonthlyDto> enersightProveMonthlyDtoList) {
		this.enersightProveMonthlyDtoList = enersightProveMonthlyDtoList;
	}
	public List<InvestigationHistoryDto> getInvestigationHistoryDtoList() {
		return investigationHistoryDtoList;
	}
	public void setInvestigationHistoryDtoList(List<InvestigationHistoryDto> investigationHistoryDtoList) {
		this.investigationHistoryDtoList = investigationHistoryDtoList;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public CommentsTbDto getCommentsTbDto() {
		return commentsTbDto;
	}
	public void setCommentsTbDto(CommentsTbDto commentsTbDto) {
		this.commentsTbDto = commentsTbDto;
	}
	public List<EnersightProveDailyDto> getEnersightProveDailyDtoList() {
		return enersightProveDailyDtoList;
	}
	public void setEnersightProveDailyDtoList(List<EnersightProveDailyDto> enersightProveDailyDtoList) {
		this.enersightProveDailyDtoList = enersightProveDailyDtoList;
	}
	
	@Override
	public String toString() {
		return "UIResponseDto [enersightProveMonthlyDtoList=" + enersightProveMonthlyDtoList
				+ ", enersightProveDailyDtoList=" + enersightProveDailyDtoList + ", investigationHistoryDtoList="
				+ investigationHistoryDtoList + ", commentsTbDto=" + commentsTbDto + ", responseMessage="
				+ responseMessage + "]";
	}

}
