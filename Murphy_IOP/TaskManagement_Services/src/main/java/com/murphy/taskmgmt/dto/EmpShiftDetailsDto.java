package com.murphy.taskmgmt.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmpShiftDetailsDto {

	private String empId;
	private String empEmail;
	private String IsMorning;
	private String IsEvening;
	private String date;
	private String designation;
	private String empName;
	private String userRole;
	private String location;
	private List<RouteLocationDto> routeLocationDtoList;
	private List<Map<String,String>> facilityList;
	private ShiftAuditLogDto shiftAuditDto;

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpEmail() {
		return empEmail;
	}

	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}

	public String getIsMorning() {
		return IsMorning;
	}

	public void setIsMorning(String isMorning) {
		IsMorning = isMorning;
	}

	public String getIsEvening() {
		return IsEvening;
	}

	public void setIsEvening(String isEvening) {
		IsEvening = isEvening;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ShiftAuditLogDto getShiftAuditDto() {
		return shiftAuditDto;
	}

	public void setShiftAuditDto(ShiftAuditLogDto shiftAuditDto) {
		this.shiftAuditDto = shiftAuditDto;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public List<RouteLocationDto> getRouteLocationDtoList() {
		return routeLocationDtoList;
	}

	public void setRouteLocationDtoList(List<RouteLocationDto> routeLocationDtoList) {
		this.routeLocationDtoList = routeLocationDtoList;
	}

	public List<Map<String, String>> getFacilityList() {
		return facilityList;
	}

	public void setFacilityList(List<Map<String, String>> facilityList) {
		this.facilityList = facilityList;
	}

	@Override
	public String toString() {
		return "EmpShiftDetailsDto [empId=" + empId + ", empEmail=" + empEmail + ", IsMorning=" + IsMorning
				+ ", IsEvening=" + IsEvening + ", date=" + date + ", designation=" + designation + ", empName="
				+ empName + ", userRole=" + userRole + ", location=" + location + ", routeLocationDtoList="
				+ routeLocationDtoList + ", facilityList=" + facilityList + ", shiftAuditDto=" + shiftAuditDto + "]";
	}

	

}
