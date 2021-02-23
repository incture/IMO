package com.murphy.taskmgmt.dto;

public class HseStringList {

	private int stringCount;
	private String searchedText;
	private int pageNumber;
	private String line;
	private String paragraph;
	
	

	public String getParagraph() {
		return paragraph;
	}
	public void setParagraph(String paragraph) {
		this.paragraph = paragraph;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	
	public int getStringCount() {
		return stringCount;
	}
	public void setStringCount(int stringCount) {
		this.stringCount = stringCount;
	}
	public String getSearchedText() {
		return searchedText;
	}
	public void setSearchedText(String searchedText) {
		this.searchedText = searchedText;
	}
	
	



}
