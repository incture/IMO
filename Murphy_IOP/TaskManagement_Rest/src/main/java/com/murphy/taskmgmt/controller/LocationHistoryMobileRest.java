package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.LocationHistoryMobileDto;
import com.murphy.taskmgmt.service.interfaces.LocationHistoryMobileFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;
import com.murphy.taskmgmt.service.interfaces.LocationHistoryCanadaMobileFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/locationHistoryMobile", produces = "application/json")
public class LocationHistoryMobileRest {
	
	@Autowired
	LocationHistoryMobileFacadeLocal facadeLocal;
	
	@Autowired
	LocationHistoryCanadaMobileFacadeLocal canadaDowntimeFacadeLocal;

	@RequestMapping(value = "/getDowntimeMob", method = RequestMethod.POST)
	public LocationHistoryMobileDto getAllDowntimeCapture(@RequestBody DowntimeRequestDto dtoGet) {
		// return facadeLocal.getMobDowntimeHistory(dtoGet);
		if (!ServicesUtil.isEmpty(dtoGet.getCountryCode())
				&& dtoGet.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE)) {
			return canadaDowntimeFacadeLocal.getMobDowntimeHistoryForCanada(dtoGet);
		} else {

			return facadeLocal.getMobDowntimeHistory(dtoGet);
		}

	}

}
