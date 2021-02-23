package com.murphy.taskmgmt.dto;

public class DateTimeDto {

	private Long day;
	private Long hour;
	private Long minute;
	private Long second;
	private Long milliSecond;
	private String dateTimeInString;

	public Long getSecond() {
		return second;
	}

	public void setSecond(Long second) {
		this.second = second;
	}

	public Long getMinute() {
		return minute;
	}

	public void setMinute(Long minute) {
		this.minute = minute;
	}

	public Long getHour() {
		return hour;
	}

	public void setHour(Long hour) {
		this.hour = hour;
	}

	public Long getDay() {
		return day;
	}

	public void setDay(Long day) {
		this.day = day;
	}

	public Long getMilliSecond() {
		return milliSecond;
	}

	public void setMilliSecond(Long milliSecond) {
		this.milliSecond = milliSecond;
	}

	public String getDateTimeInString() {
		return dateTimeInString;
	}

	public void setDateTimeInString(String dateTimeInString) {
		this.dateTimeInString = dateTimeInString;
	}

	@Override
	public String toString() {
		return day + " days " + hour + " hours " + minute + " minutes " + second + " seconds " + milliSecond + " milliSecond ";
	}

}
