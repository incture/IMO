package com.incture.iopptw.dtos;

import lombok.Data;

@Data
public class DftGetReviewerResponseDto {
	private String department;
	private String emailID;
	private String firstName;
	private String fullName;
	private String lastName;
	private String pUserId;
	private String role;
}
