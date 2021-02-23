package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.FlareCodeResponseDto;
import com.murphy.taskmgmt.dto.FlareDowntimeResponseDto;
import com.murphy.taskmgmt.dto.FlareDowntimeUpdateDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.FlareDowntimeCaptureFacade;
import com.murphy.taskmgmt.service.interfaces.FlareDowntimeCaptureFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/flareDowntime", produces = "application/json")
public class FlareDowntimeCaptureRest {
	
	@Autowired
	FlareDowntimeCaptureFacadeLocal flareFacadeLocal;
	
	@Autowired
	FlareDowntimeCaptureFacade flareCaptureLocal;

	@RequestMapping(value = "/getDowntimeCodes", method = RequestMethod.GET)
	public FlareCodeResponseDto getDowntimeCodes() {
		return flareFacadeLocal.getFlareDowntimeCodes();
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseMessage createDowntimeCapture(@RequestBody FlareDowntimeUpdateDto  dto){
		return flareFacadeLocal.createFlareDowntime(dto);
	}
	
	@RequestMapping(value = "/getDowntime", method = RequestMethod.POST)
	public FlareDowntimeResponseDto getAllDowntimeCapture(@RequestBody DowntimeRequestDto dtoGet){
		return flareFacadeLocal.getDowntimeHierarchy(dtoGet);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseMessage updateDowntimeCapture(@RequestBody FlareDowntimeUpdateDto  dto){
		return flareFacadeLocal.updateFlareDowntime(dto);
	}

}
