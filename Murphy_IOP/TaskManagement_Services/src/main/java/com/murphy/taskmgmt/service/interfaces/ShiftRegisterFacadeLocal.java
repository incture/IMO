package com.murphy.taskmgmt.service.interfaces;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

import com.murphy.taskmgmt.dto.EmpShiftDetailsDto;
import com.murphy.taskmgmt.dto.Response;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.RouteLocationDto;
import com.murphy.taskmgmt.dto.ShiftRegisterDto;
import com.murphy.taskmgmt.dto.ShiftRegisterResponseDto;

public interface ShiftRegisterFacadeLocal {

	ResponseMessage createShiftRegister(ShiftRegisterDto dto);

	ResponseMessage updateShiftRegister(EmpShiftDetailsDto dto);

	ResponseMessage deleteShiftRegister(ShiftRegisterDto dto);

	ShiftRegisterDto getShiftRegister(ShiftRegisterDto dto);

	ShiftRegisterResponseDto empShiftDetails(String date, String email, List<String> userRole);

	ResponseMessage shiftRegisterScheduler();
	
	boolean getShiftDetailsForEmp(String emp_email);

	ResponseEntity<Response<JSONObject>> getAuditDetails(int month,int pageNo);

	ResponseEntity<Response<List<RouteLocationDto>>> fetchWellPad(String empId,String facility);

}
