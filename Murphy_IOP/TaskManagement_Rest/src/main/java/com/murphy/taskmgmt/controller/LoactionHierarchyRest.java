package com.murphy.taskmgmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.FieldResponseDto;
import com.murphy.taskmgmt.dto.HierarchyRequestDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.LocationHierarchyResponseDto;
import com.murphy.taskmgmt.dto.LocationResponseDto;
import com.murphy.taskmgmt.service.interfaces.LocationHierarchyLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/location", produces = "application/json")
public class LoactionHierarchyRest {

	@Autowired
	LocationHierarchyLocal locationLocal;

	
	@RequestMapping(value = "/getLocation", method = RequestMethod.POST)
	LocationResponseDto getLocation(@RequestBody HierarchyRequestDto dto){
		return locationLocal.getHierarchy(dto);
	}
	
	@RequestMapping(value = "/getField", method = RequestMethod.GET)
	FieldResponseDto getField(@RequestParam(value ="location", required=false) String location,@RequestParam(value ="locType", required=false) String locType){
		return locationLocal.getFeild(location,locType);
	}
	
	@RequestMapping(value = "/getWellForMuwi", method = RequestMethod.GET)
	LocationHierarchyDto getField(@RequestParam(value ="muwi") String muwi){
		return locationLocal.getWellDetails(muwi);
	}

	
	@RequestMapping(value = "/getParentOfCompressor", method = RequestMethod.GET)
	LocationHierarchyResponseDto getParentByCompressor(@RequestParam(value ="compressorId") String compressorId){
		return locationLocal.getParentByCompressor(compressorId);
	}
	
	@RequestMapping(value = "/getFieldTextLoc", method = RequestMethod.GET)
	FieldResponseDto getFieldTextPerLocCode(@RequestParam(value ="locationList", required=false) List<String> locationList,@RequestParam(value ="locType", required=false) String locType){
		return locationLocal.getFieldTextForLocCode(locationList,locType);
	}
	
}
