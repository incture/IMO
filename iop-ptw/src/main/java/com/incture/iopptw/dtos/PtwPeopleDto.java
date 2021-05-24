package com.incture.iopptw.dtos;

import lombok.Data;

@Data
public class PtwPeopleDto {
	private Integer serialNo;
	private Integer permitNumber;
	private String firstName;
	private String lastName;
	private String contactNumber;
	private Integer hasSignedJSA;
	private Integer hasSignedCWP;
	private Integer hasSignedHWP;
	private Integer hasSignedCSE;
}
