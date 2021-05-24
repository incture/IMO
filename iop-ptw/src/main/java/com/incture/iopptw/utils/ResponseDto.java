package com.incture.iopptw.utils;

import lombok.Data;

@Data
public class ResponseDto {
	private boolean status;
	private Integer statusCode;
	private String message;
	private Object data;

}
