package com.murphy.taskmgmt.dto;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class EmpDetailsDto extends BaseDto{

	
	@Override
	public String toString() {
		return "EmpDetailsDto [empId=" + empId + ", fieldTaskUser=" + fieldTaskUser + ", foremanId=" + foremanId
				+ ", superintendentId=" + superintendentId + ", empEmail=" + empEmail + ", empName=" + empName
				+ ", foremanName=" + foremanName + ", foremanEmail=" + foremanEmail + ", foremanDesignation="
				+ foremanDesignation + ", superintendentName=" + superintendentName + ", superintendentEmail="
				+ superintendentEmail + ", superintendentDesignation=" + superintendentDesignation + ", empDesignation="
				+ empDesignation + ", startDate=" + startDate + ", emplStatus=" + emplStatus + ", endDate=" + endDate
				+ "]";
	}
	private String empId;
	private int fieldTaskUser;
	private String foremanId;
	private String superintendentId;
	private String empEmail;
	private String empName;
	private String foremanName;
	private String foremanEmail;
	private String foremanDesignation;
	private String superintendentName;
	private String superintendentEmail;
	private String superintendentDesignation;
	private String empDesignation;
	private String startDate;
	private String emplStatus;
	private String endDate;
	


	
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getEmplStatus() {
		return emplStatus;
	}
	public void setEmplStatus(String emplStatus) {
		this.emplStatus = emplStatus;
	}
	public int getFieldTaskUser() {
		return fieldTaskUser;
	}
	public void setFieldTaskUser(int fieldTaskUser) {
		this.fieldTaskUser = fieldTaskUser;
	}
	
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getForemanId() {
		return foremanId;
	}
	public void setForemanId(String foremanId) {
		this.foremanId = foremanId;
	}
	public String getSuperintendentId() {
		return superintendentId;
	}
	public void setSuperintendentId(String superintendentId) {
		this.superintendentId = superintendentId;
	}
	public String getEmpEmail() {
		return empEmail;
	}
	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}
	
	public String getForemanName() {
		return foremanName;
	}
	public void setForemanName(String foremanName) {
		this.foremanName = foremanName;
	}
	public String getForemanEmail() {
		return foremanEmail;
	}
	public void setForemanEmail(String foremanEmail) {
		this.foremanEmail = foremanEmail;
	}
	public String getForemanDesignation() {
		return foremanDesignation;
	}
	public void setForemanDesignation(String foremanDesignation) {
		this.foremanDesignation = foremanDesignation;
	}
	public String getSuperintendentName() {
		return superintendentName;
	}
	public void setSuperintendentName(String superintendentName) {
		this.superintendentName = superintendentName;
	}
	public String getSuperintendentEmail() {
		return superintendentEmail;
	}
	public void setSuperintendentEmail(String superintendentEmail) {
		this.superintendentEmail = superintendentEmail;
	}
	public String getSuperintendentDesignation() {
		return superintendentDesignation;
	}
	public void setSuperintendentDesignation(String superintendentDesignation) {
		this.superintendentDesignation = superintendentDesignation;
	}
	public String getEmpDesignation() {
		return empDesignation;
	}
	public void setEmpDesignation(String empDesignation) {
		this.empDesignation = empDesignation;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
}
