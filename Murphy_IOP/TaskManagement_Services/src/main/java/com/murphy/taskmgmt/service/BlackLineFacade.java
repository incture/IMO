package com.murphy.taskmgmt.service;

import org.springframework.stereotype.Service;


import com.murphy.blackline.NearestUserDtoResponseBl;
import com.murphy.blackline.NearestWellsDtoResponse;
import com.murphy.geotab.NearestUserDtoResponse;
import com.murphy.taskmgmt.dto.OperatorsAvailabilityDto;
import com.murphy.taskmgmt.service.interfaces.BlackLineFacadeLocal;
import com.murphy.taskmgmt.util.BlackLineUtil;

@Service("BlackLineFacade")
public class BlackLineFacade implements BlackLineFacadeLocal {

//	@Override
//	public NearestUserDtoResponseBl getUsers(Double latitude, Double longitude, String locationCode) {
//		return BlackLineUtil.getUsers(latitude, longitude, locationCode);
//	}

	@Override
	public NearestWellsDtoResponse getWells(String userEmail) {
		return BlackLineUtil.getWells(userEmail);
	}

	@Override
	public NearestUserDtoResponse operatorsAvailablitycheck(OperatorsAvailabilityDto dto) {
		return BlackLineUtil.getUsersByAbility(dto);
	}
}
