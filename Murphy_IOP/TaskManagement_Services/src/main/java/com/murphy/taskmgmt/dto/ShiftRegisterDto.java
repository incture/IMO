package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class ShiftRegisterDto extends BaseDto  {
	
	private String id;
	private String empId;
	private String empName;
	private String empEmail;
	@Override
	public String toString() {
		return "ShiftRegisterDto [id=" + id + ", empId=" + empId + ", empName=" + empName + ", empEmail=" + empEmail
				+ ", mon=" + mon + ", tue=" + tue + ", wed=" + wed + ", thur=" + thur + ", fri=" + fri + ", sat=" + sat
				+ ", sun=" + sun + "]";
	}

	public String getEmpEmail() {
		return empEmail;
	}

	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}

	private int mon;
	private int tue;
	private int wed;
	private int thur;
	private int fri;
	private int sat;
	private int sun;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public int getMon() {
		return mon;
	}

	public void setMon(int mon) {
		this.mon = mon;
	}

	public int getTue() {
		return tue;
	}

	public void setTue(int tue) {
		this.tue = tue;
	}

	public int getWed() {
		return wed;
	}

	public void setWed(int wed) {
		this.wed = wed;
	}

	public int getThur() {
		return thur;
	}

	public void setThur(int thur) {
		this.thur = thur;
	}

	public int getFri() {
		return fri;
	}

	public void setFri(int fri) {
		this.fri = fri;
	}

	public int getSat() {
		return sat;
	}

	public void setSat(int sat) {
		this.sat = sat;
	}

	public int getSun() {
		return sun;
	}

	public void setSun(int sun) {
		this.sun = sun;
	}

	@Override
	public Boolean getValidForUsage() {
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}

}
