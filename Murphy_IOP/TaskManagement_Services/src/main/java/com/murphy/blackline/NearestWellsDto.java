package com.murphy.blackline;

public class NearestWellsDto {
	
	private String locText;
	private String locCode;
	public String getLocCode() {
		return locCode;
	}
	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}
	@Override
	public String toString() {
		return "NearestWellsDto [locText=" + locText + ", locCode=" + locCode + "]";
	}
	public String getLocText() {
		return locText;
	}
	public void setLocText(String locText) {
		this.locText = locText;
	}

	
	
	

}
