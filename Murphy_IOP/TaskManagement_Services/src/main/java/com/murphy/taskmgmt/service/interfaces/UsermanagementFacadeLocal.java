package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.UserDetailsDto;

public interface UsermanagementFacadeLocal {

	UserDetailsDto getUserDetails(String userName);
	
	String getTrackDirection();

}
