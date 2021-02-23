package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.blackline.NearestUserDtoResponseBl;
import com.murphy.blackline.NearestWellsDtoResponse;
import com.murphy.geotab.NearestUserDtoResponse;
import com.murphy.taskmgmt.dto.EmpDetailsDto;
import com.murphy.taskmgmt.dto.OperatorsAvailabilityDto;
import com.murphy.taskmgmt.service.interfaces.BlackLineFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/locationBL", produces = "application/json")
public class BlackLineRest {

	@Autowired
	BlackLineFacadeLocal blackLineFacadeLocal;
//	
//	@RequestMapping(value = "/getNearestUsers", method = RequestMethod.GET)
//	NearestUserDtoResponseBl getUsers(@RequestParam(value = "latitude", required = false) Double latitude,
//			@RequestParam(value = "longitude", required = false) Double longitude,
//			@RequestParam(value = "locationCode", required = false) String locationCode) {
//		return blackLineFacadeLocal.getUsers(latitude, longitude, locationCode);
//	}
//	
	@RequestMapping(value = "/getNearestWells", method = RequestMethod.GET)
	NearestWellsDtoResponse getWells(@RequestParam(value = "userEmail", required = false) String userEmail) {
		return blackLineFacadeLocal.getWells(userEmail);
	}
	
	@RequestMapping(value = "/operatorsAvailable", method = RequestMethod.POST)
	NearestUserDtoResponse operatorsAvailablitycheck(@RequestBody OperatorsAvailabilityDto dto) {
		return blackLineFacadeLocal.operatorsAvailablitycheck(dto);
	}
	
	
	
	
}
