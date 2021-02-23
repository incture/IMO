package com.murphy.taskmgmt.dto;

import java.util.List;

public class HseDocumentResponse {
	private ResponseMessage responseMessage;	
	int totalPages;
	int totalOccurenceOfString;
	private String paragraph;
	private List<HseStringList> hseStringList;
	
	
	public String getParagraph() {
		return paragraph;
	}
	public void setParagraph(String paragraph) {
		this.paragraph = paragraph;
	}
	public List<HseStringList> getHseStringList() {
		return hseStringList;
	}
	public void setHseStringList(List<HseStringList> hseStringList) {
		this.hseStringList = hseStringList;
	}
	public int getTotalOccurenceOfString() {
		return totalOccurenceOfString;
	}
	public void setTotalOccurenceOfString(int totalOccurenceOfString) {
		this.totalOccurenceOfString = totalOccurenceOfString;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
}
