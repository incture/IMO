package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SHIFT_REGISTER")
public class ShiftRegisterDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", length = 40)
	private String id;
	

	@Column(name = "EMP_ID")
	private String empId;
	
	@Column(name = "EMP_EMAIL")
	private String empEmail;

	@Override
	public String toString() {
		return "ShiftRegisterDo [id=" + id + ", empId=" + empId + ", empEmail=" + empEmail + ", empName=" + empName
				+ ", mon=" + mon + ", tue=" + tue + ", wed=" + wed + ", thur=" + thur + ", fri=" + fri + ", sat=" + sat
				+ ", sun=" + sun + "]";
	}

	public String getEmpEmail() {
		return empEmail;
	}

	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}

	@Column(name = "EMP_NAME")
	private String empName;

	@Column(name = "MONDAY")
	private int mon;

	@Column(name = "TUESDAY")
	private int tue;

	@Column(name = "WEDNESDAY")
	private int wed;

	@Column(name = "THURSDAY")
	private int thur;

	@Column(name = "FRIDAY")
	private int fri;

	@Column(name = "SATURDAY")
	private int sat;

	@Column(name = "SUNDAY")
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
	public Object getPrimaryKey() {
		return id;
	}
}
