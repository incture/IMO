package com.murphy.taskmgmt.dto;

public class AckAlarmReqDto {

	String pointIds;
	String key;
	String value;

	public String getPointIds() {
		return pointIds;
	}

	public void setPointIds(String pointIds) {
		this.pointIds = pointIds;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "AckAlarmReqDto [pointIds=" + pointIds + ", key=" + key + ", value=" + value + "]";
	}

}
