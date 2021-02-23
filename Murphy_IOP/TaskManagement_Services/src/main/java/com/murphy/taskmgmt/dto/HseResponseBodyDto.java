package com.murphy.taskmgmt.dto;

import java.util.List;

public class HseResponseBodyDto {
	private List<String> stringList;
	private ResponseMessage responseMessage;
	private String docUrl;
	private int version;
	private String documnetId;
	private String docContent;
	private String searchString;
	
	public List<String> getStringList() {
		return stringList;
	}
	public void setStringList(List<String> stringList) {
		this.stringList = stringList;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public String getDocContent() {
		return docContent;
	}
	public void setDocContent(String docContent) {
		this.docContent = docContent;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getDocUrl() {
		return docUrl;
	}
	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getDocumnetId() {
		return documnetId;
	}
	public void setDocumnetId(String documnetId) {
		this.documnetId = documnetId;
	}
	
	
	
	
	
	

}
