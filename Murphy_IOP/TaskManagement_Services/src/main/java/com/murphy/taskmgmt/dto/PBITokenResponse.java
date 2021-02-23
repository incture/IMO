package com.murphy.taskmgmt.dto;

public class PBITokenResponse {

	private String groupId;
	private String reportId;
	private String accessToken;
	private ResponseMessage responseMessage;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	@Override
	public String toString() {
		return "PBITokenResponse [groupId=" + groupId + ", reportId=" + reportId + ", accessToken=" + accessToken
				+ ", responseMessage=" + responseMessage + "]";
	}

}
