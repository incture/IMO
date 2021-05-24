package com.incture.iopptw.dtos;

import lombok.Data;

@Data
public class ActiveWorkersRecordDto {
	private String contactNumber;
	private String firstName;
	private String lastName;
	private int permitNumber;
	private String facilityOrSite;
}
