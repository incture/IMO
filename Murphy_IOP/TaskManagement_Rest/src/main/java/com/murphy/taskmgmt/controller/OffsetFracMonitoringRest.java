package com.murphy.taskmgmt.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.murphy.taskmgmt.dto.CanaryStagingDto;
import com.murphy.taskmgmt.dto.FracDropDownResponseDto;
import com.murphy.taskmgmt.dto.FracHitDto;
import com.murphy.taskmgmt.dto.FracOrientationDto;
import com.murphy.taskmgmt.dto.FracOrientationResponseDto;
import com.murphy.taskmgmt.dto.FracPackEngViewResponseDto;
import com.murphy.taskmgmt.dto.FracScenarioDto;
import com.murphy.taskmgmt.dto.FracScenarioLookUpDto;
import com.murphy.taskmgmt.dto.FracScenarioLookUpResponseDto;
import com.murphy.taskmgmt.dto.FracWellStatusDto;
import com.murphy.taskmgmt.dto.FracWellStatusResponseDto;
import com.murphy.taskmgmt.dto.FracZoneDto;
import com.murphy.taskmgmt.dto.FracZoneResponseDto;
import com.murphy.taskmgmt.dto.OffsetFracPackDto;
import com.murphy.taskmgmt.dto.OffsetFracPackRequestDto;
import com.murphy.taskmgmt.dto.OffsetFracPackResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.OffsetFracPackFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/fracPack", produces = "application/json")
public class OffsetFracMonitoringRest {

	@Autowired
	OffsetFracPackFacadeLocal facadeLocal;
	
	
	@RequestMapping(value="/getPayload",method=RequestMethod.GET)
	public OffsetFracPackRequestDto showDto(){
		
		OffsetFracPackRequestDto request= new OffsetFracPackRequestDto();
		List<OffsetFracPackDto> list= new  ArrayList<>();
		OffsetFracPackDto dto= new OffsetFracPackDto();
	
		dto.setFieldCode("SomeF");
		dto.setBoed(122.3331);
		dto.setWellCode("well123");
		dto.setDistFrac(2134.23);
		dto.setEndAt(new Date());
		dto.setStartAt(new Date());
		dto.setMaxCasePressure(34567.567);
		dto.setMaxTubePressure(345.3456);
		dto.setOrientation("test");
		dto.setEstBolDate(new Date());
		dto.setProdImpact(3456.345);
		dto.setScenario("test scenario");
		dto.setZone("dfgh");
		list.add(dto);
		
		OffsetFracPackDto dto1= new OffsetFracPackDto();
		
		dto1.setFieldCode("SomeField");
		dto1.setBoed(122.3331);
		dto1.setWellCode("well124");
		dto1.setDistFrac(2134.23);
		dto1.setEndAt(new Date());
		dto1.setStartAt(new Date());
		dto1.setMaxCasePressure(34567.567);
		dto1.setMaxTubePressure(345.3456);
		dto1.setOrientation("test");
		dto1.setEstBolDate(new Date());
		dto1.setProdImpact(3456.345);
		dto1.setScenario("test scenario");
		dto1.setZone("dfgh");
		dto1.setUserId("user1");
		dto.setUserRole("ENG");
		list.add(dto1);
		
		request.setFracPacks(list);
		request.setDescription("Test Frac Packs");
		return request;
		
		
	}

	@RequestMapping(value = "/getFracPack", method = RequestMethod.GET)
	public OffsetFracPackResponseDto getFracPack(@RequestParam(value = "fracId") String fracId,
			@RequestParam(value = "fieldCode", defaultValue = "") String fieldCode,@RequestParam(value="wellCode",defaultValue="") String wellcode ) {
         return facadeLocal.getFracPack(fracId, fieldCode,wellcode);

		
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public ResponseMessage addFracPack(@RequestBody OffsetFracPackRequestDto dto){
		try{
		return facadeLocal.createFracPack(dto);}
		catch(Exception e){
			ResponseMessage failed= new ResponseMessage();
			failed.setMessage("Unable to persist");
			failed.setStatus(MurphyConstant.FAILURE);
			failed.setStatusCode(MurphyConstant.CODE_FAILURE);
			return failed;
		}
	}
     
	@RequestMapping(value="/EngView",method=RequestMethod.GET)
	public FracPackEngViewResponseDto getEngView(@RequestParam(value="userRole",defaultValue="")String userRole){
		if(ServicesUtil.isEmpty(userRole))
		return facadeLocal.getEngView(null);
		else
			return facadeLocal.getEngView(userRole);
	}
	
	@RequestMapping(value="/boed",method=RequestMethod.POST)
	public OffsetFracPackRequestDto getBoed(@RequestBody OffsetFracPackRequestDto requestDto){
		return facadeLocal.getBoed(requestDto);
	}
	
	@RequestMapping(value="/fracHit",method=RequestMethod.POST)
	public ResponseMessage insertFractHit(@RequestBody FracHitDto dto){
		if(ServicesUtil.isEmpty(dto.getFracHitTime()))
		  dto.setFracHitTime(new Date());
		System.err.println("------- Date " + dto.getFracHitTime());
	
		return facadeLocal.insertFractHit(dto);
	}
	@RequestMapping(value="/markCompleted",method=RequestMethod.POST)
	public ResponseMessage markCompleted(@RequestBody List<FracHitDto> requestDtos){
		try{
			return facadeLocal.markCompleteFracPack(requestDtos);}
			catch(Exception e){
				ResponseMessage failed= new ResponseMessage();
				failed.setMessage("Unable to Mark Complete ");
				failed.setStatus(MurphyConstant.FAILURE);
				failed.setStatusCode(MurphyConstant.CODE_FAILURE);
				return failed;
			}
	}
	@RequestMapping(value="/wellStatus",method=RequestMethod.POST)
	public ResponseMessage changeWellStatus(@RequestBody List<FracHitDto> requestDtos){
		try{
			return facadeLocal.changeWellStatus(requestDtos);}
			catch(Exception e){
				ResponseMessage failed= new ResponseMessage();
				failed.setMessage("Unable to update Status ");
				failed.setStatus(MurphyConstant.FAILURE);
				failed.setStatusCode(MurphyConstant.CODE_FAILURE);
				return failed;
			}
	}
	
	@RequestMapping(value="/ActiveValues",method=RequestMethod.POST)
	public List<CanaryStagingDto> getActiveValues(@ RequestBody List<String> muwis){
		return facadeLocal.getActiveValues(muwis);
	}
	
	//Adding for incident INC0078316
	@RequestMapping(value="/getFracScenario",method=RequestMethod.GET)
	public List<FracScenarioDto> getFracScenario(){
		return facadeLocal.getFracScenario();
	}
	
	//added as a part of Data maintenance - sprint 5
	//SOC
	@RequestMapping(value="/getActiveFracScenarios",method=RequestMethod.GET)
	public FracScenarioLookUpResponseDto getActiveFracScenarios(){
		return facadeLocal.getActiveFracScenarios();
	}
	@RequestMapping(value="/getActiveFracWellStatus",method=RequestMethod.GET)
	public FracWellStatusResponseDto getActiveFracWellStatus(){
		return facadeLocal.getActiveFracWellStatus();
	}
	
	@RequestMapping(value="/getActiveFracOrientation",method=RequestMethod.GET)
	public FracOrientationResponseDto getActiveFracOrientation(){
		return facadeLocal.getActiveFracOrientation();
	}
	
	@RequestMapping(value="/getActiveFracZone",method=RequestMethod.GET)
	public FracZoneResponseDto getActiveFracZone(){
		return facadeLocal.getActiveFracZone();
	}
	@RequestMapping(value="/getAllActiveFracDropDowns",method=RequestMethod.GET)
	public FracDropDownResponseDto getAllActiveFracDropDowns(){
		return facadeLocal.getAllActiveFracDropDowns();
	}
	//EOC
	
	@RequestMapping(value="/updateFracPack",method=RequestMethod.POST)
	public ResponseMessage updateFracPack(@RequestBody List<OffsetFracPackDto> offsetFracPackDtos){
		return facadeLocal.updateFracPack(offsetFracPackDtos);
	}
	
}
