package com.murphy.taskmgmt.service.interfaces;


import com.murphy.blackline.NearestUserDtoResponseBl;
import com.murphy.blackline.NearestWellsDtoResponse;
import com.murphy.geotab.NearestUserDtoResponse;
import com.murphy.taskmgmt.dto.OperatorsAvailabilityDto;

public interface BlackLineFacadeLocal {

	//NearestUserDtoResponseBl getUsers(Double latitude, Double longitude, String locationCode);

	NearestWellsDtoResponse getWells(String userEmail);

	NearestUserDtoResponse operatorsAvailablitycheck(OperatorsAvailabilityDto dto);

}
