package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.geotab.NearestUserDtoResponse;
import com.murphy.taskmgmt.dto.ArcGISResponseDto;
import com.murphy.taskmgmt.dto.LocationHierarchyResponseDto;
import com.murphy.taskmgmt.service.interfaces.GeoTabFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/locationGT", produces = "application/json")
public class GeoTabRest {

	@Autowired
	GeoTabFacadeLocal facadeLocal;

	@RequestMapping(value = "/getNearestUsers", method = RequestMethod.GET)
	NearestUserDtoResponse getUsers(@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "locationCode", required = false) String locationCode, 
		@RequestParam(value ="groupId",required = false) String groupId,
		@RequestParam(value ="userType" ,required = false) String userType,
		@RequestParam(value ="taskType" ,required = false) String taskType){	
		return facadeLocal.getUsers(latitude, longitude, locationCode,groupId,userType, taskType);
	}
	
	
	@RequestMapping(value = "/getRoadDistanceOfUser", method = RequestMethod.GET)
	NearestUserDtoResponse getRoadDistanceOfUser(@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "locationCode", required = false) String locationCode) {
		return facadeLocal.getRoadDistanceOfUser(userId,locationCode);
	}
	
	
	
	

	@RequestMapping(value = "/getNearestFacilityUsers", method = RequestMethod.GET)
	NearestUserDtoResponse getUsers(@RequestParam(value = "location") String location,
			@RequestParam(value = "type") String type) {
		return facadeLocal.getUsers(location, type);
	}

//	@RequestMapping(value = "/getNearestUsersByMuwi", method = RequestMethod.GET)
//	NearestUserDtoResponse getUsers(@RequestParam(value = "muwi") String muwi) {
//		return facadeLocal.getUsers(muwi);
//	}

	@RequestMapping(value = "/getArcGISResponse", method = RequestMethod.GET)
	ArcGISResponseDto getArcGISResponse(
			@RequestParam(value = "locationCodeOne", required = false) String locationCodeOne,
			@RequestParam("locationCodeTwo") String locationCodeTwo,
			@RequestParam(value = "userId", required = false) String userId) {
		return facadeLocal.getArcGISDetails(locationCodeOne, locationCodeTwo, userId);
	}


	@RequestMapping(value = "/getWellForEmail", method = RequestMethod.GET)
	LocationHierarchyResponseDto getWellUsingEmail(@RequestParam(value = "emailId", required = true) String emailId) {
		return facadeLocal.getWellUsingEmailId(emailId);
	}
}
