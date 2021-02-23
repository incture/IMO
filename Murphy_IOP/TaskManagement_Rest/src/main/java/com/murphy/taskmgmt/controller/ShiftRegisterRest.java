package com.murphy.taskmgmt.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.EmpShiftDetailsDto;
import com.murphy.taskmgmt.dto.Response;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.RouteLocationDto;
import com.murphy.taskmgmt.dto.ShiftAuditLogDto;
import com.murphy.taskmgmt.dto.ShiftRegisterDto;
import com.murphy.taskmgmt.dto.ShiftRegisterResponseDto;
import com.murphy.taskmgmt.service.interfaces.ShiftRegisterFacadeLocal;

@RestController
@ComponentScan("com.murphy")
@RequestMapping(value = "/ShiftRegister", produces = "application/json")
public class ShiftRegisterRest {
	
	@Autowired
	ShiftRegisterFacadeLocal shiftRegisterFacadeLocal;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseMessage createShiftRegister(@RequestBody ShiftRegisterDto dto) {
		return shiftRegisterFacadeLocal.createShiftRegister(dto);
		
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ShiftRegisterDto getEmpDetails(@RequestBody ShiftRegisterDto dto) {
		return shiftRegisterFacadeLocal.getShiftRegister(dto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseMessage updateEmp(@RequestBody EmpShiftDetailsDto dto) {
		return shiftRegisterFacadeLocal.updateShiftRegister(dto);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public ResponseMessage removeEmp(@RequestBody ShiftRegisterDto dto) {
		return shiftRegisterFacadeLocal.deleteShiftRegister(dto);

	}
	
	//Fetch UserDetails for Shift Register Tab
	@RequestMapping(value = "/empShiftDetails", method = RequestMethod.GET)
	public ShiftRegisterResponseDto empShiftDetails(@RequestParam(value ="date" ,required =true) String date,
			@RequestParam(value ="email" ,required =false) String email,
			@RequestParam(value ="userRole" ,required =false) List<String> userRole) {
		return shiftRegisterFacadeLocal.empShiftDetails(date,email,userRole);

	}
	
	@RequestMapping(value = "/shiftRegisterScheduler", method = RequestMethod.GET)
	public ResponseMessage shiftRegisterScheduler() {
		return shiftRegisterFacadeLocal.shiftRegisterScheduler();

	}
	
	@RequestMapping(value = "/getShiftDetails", method = RequestMethod.GET)
	public boolean getShiftDetails(@RequestParam(value ="emp_email" ,required =true) String emp_email) {
		return shiftRegisterFacadeLocal.getShiftDetailsForEmp(emp_email);
	}
	
	//Fetch Audit details from Shift Audit Tab
	@RequestMapping(value = "/fetchShiftAuditDetails", method = RequestMethod.GET)
	public ResponseEntity<Response<JSONObject>> getAuditDetails(@RequestParam(value ="month" ,required =true) int month,@RequestParam(value ="page" ,required =true) int pageNo) {
		return shiftRegisterFacadeLocal.getAuditDetails(month,pageNo);
	}
	
	//Descoped
	@RequestMapping(value = "/getWellPadLocation", method = RequestMethod.GET)
	public ResponseEntity<Response<List<RouteLocationDto>>> fetchWellPad(@RequestParam(value ="empId" ,required =true) String empId,@RequestParam(value ="facility" ,required =true) String facility) {
		return shiftRegisterFacadeLocal.fetchWellPad(empId,facility);
	}

}
