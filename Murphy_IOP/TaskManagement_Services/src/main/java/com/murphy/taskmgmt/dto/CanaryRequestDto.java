package com.murphy.taskmgmt.dto;

import java.util.Arrays;

public class CanaryRequestDto {
	private String startTimeCanaryFormat;
	private String endTimeCanaryFormat;
	private String startTimeDBFormat;
	private String endTimeDBFormat;
	private String deleteTimeDBFormat;
	private String nextDayDBFormat;
	private int currentHour;
	private int insertInterval;
	private String insertIntervalType;
	private String[] canaryParam;
	private String aggregateName;

	public CanaryRequestDto(String startTimeCanaryFormat, String endTimeCanaryFormat, String startTimeDBFormat,
			String endTimeDBFormat, String deleteTimeDBFormat, String nextDayDBFormat, int currentHour,
			int insertInterval, String insertIntervalType, String[] canaryParam, String aggregateName) {
		super();
		this.startTimeCanaryFormat = startTimeCanaryFormat;
		this.endTimeCanaryFormat = endTimeCanaryFormat;
		this.startTimeDBFormat = startTimeDBFormat;
		this.endTimeDBFormat = endTimeDBFormat;
		this.deleteTimeDBFormat = deleteTimeDBFormat;
		this.nextDayDBFormat = nextDayDBFormat;
		this.currentHour = currentHour;
		this.insertInterval = insertInterval;
		this.insertIntervalType = insertIntervalType;
		this.canaryParam = canaryParam;
		this.aggregateName = aggregateName;
	}

	public String getStartTimeCanaryFormat() {
		return startTimeCanaryFormat;
	}

	public void setStartTimeCanaryFormat(String startTimeCanaryFormat) {
		this.startTimeCanaryFormat = startTimeCanaryFormat;
	}

	public String getEndTimeCanaryFormat() {
		return endTimeCanaryFormat;
	}

	public void setEndTimeCanaryFormat(String endTimeCanaryFormat) {
		this.endTimeCanaryFormat = endTimeCanaryFormat;
	}

	public String getStartTimeDBFormat() {
		return startTimeDBFormat;
	}

	public void setStartTimeDBFormat(String startTimeDBFormat) {
		this.startTimeDBFormat = startTimeDBFormat;
	}

	public String getEndTimeDBFormat() {
		return endTimeDBFormat;
	}

	public void setEndTimeDBFormat(String endTimeDBFormat) {
		this.endTimeDBFormat = endTimeDBFormat;
	}

	public String getDeleteTimeDBFormat() {
		return deleteTimeDBFormat;
	}

	public void setDeleteTimeDBFormat(String deleteTimeDBFormat) {
		this.deleteTimeDBFormat = deleteTimeDBFormat;
	}

	public String getNextDayDBFormat() {
		return nextDayDBFormat;
	}

	public void setNextDayDBFormat(String nextDayDBFormat) {
		this.nextDayDBFormat = nextDayDBFormat;
	}

	public int getCurrentHour() {
		return currentHour;
	}

	public void setCurrentHour(int currentHour) {
		this.currentHour = currentHour;
	}

	public int getInsertInterval() {
		return insertInterval;
	}

	public void setInsertInterval(int insertInterval) {
		this.insertInterval = insertInterval;
	}

	public String getInsertIntervalType() {
		return insertIntervalType;
	}

	public void setInsertIntervalType(String insertIntervalType) {
		this.insertIntervalType = insertIntervalType;
	}

	public String[] getCanaryParam() {
		return canaryParam;
	}

	public void setCanaryParam(String[] canaryParam) {
		this.canaryParam = canaryParam;
	}

	public String getAggregateName() {
		return aggregateName;
	}

	public void setAggregateName(String aggregateName) {
		this.aggregateName = aggregateName;
	}

	@Override
	public String toString() {
		return "CanaryRequestDto [startTimeCanaryFormat=" + startTimeCanaryFormat + ", endTimeCanaryFormat="
				+ endTimeCanaryFormat + ", startTimeDBFormat=" + startTimeDBFormat + ", endTimeDBFormat="
				+ endTimeDBFormat + ", deleteTimeDBFormat=" + deleteTimeDBFormat + ", nextDayDBFormat="
				+ nextDayDBFormat + ", currentHour=" + currentHour + ", insertInterval=" + insertInterval
				+ ", insertIntervalType=" + insertIntervalType + ", canaryParam=" + Arrays.toString(canaryParam)
				+ ", aggregateName=" + aggregateName + "]";
	}
}