package com.incture.ptw.dto;

import lombok.Data;

@Data
public class PtwPeopleDto {
	private Integer serialNo;
	private Integer permitNumber;
	private String firstName;
	private String lastName;
	private String contactNumber;
	private Integer hasSignedJsa;
	private Integer hasSignedCwp;
	private Integer hasSignedHwp;
	private Integer hasSignedCse;
}
