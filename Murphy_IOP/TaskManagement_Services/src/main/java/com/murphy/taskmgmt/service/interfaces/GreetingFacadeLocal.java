package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.GreetingResponseDto;

public interface GreetingFacadeLocal {

	GreetingResponseDto checkIfGreeted(String userEmail);

}
