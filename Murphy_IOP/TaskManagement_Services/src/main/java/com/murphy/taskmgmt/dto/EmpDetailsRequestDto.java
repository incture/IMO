package com.murphy.taskmgmt.dto;

import java.util.List;

public class EmpDetailsRequestDto {
	
	private List<EmpDetailsDto> employee;

	@Override
	public String toString() {
		return "EmpDetailsRequestDto [employee=" + employee + "]";
	}

	public List<EmpDetailsDto> getEmployee() {
		return employee;
	}

	public void setEmployee(List<EmpDetailsDto> employee) {
		this.employee = employee;
	}

}
