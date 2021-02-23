package com.murphy.taskmgmt.controller;
import java.util.List;
import java.util.Map;

import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.murphy.taskmgmt.dto.EmpDetailsDto;
import com.murphy.taskmgmt.dto.EmpDetailsRequestDto;
import com.murphy.taskmgmt.dto.EmpDetailsResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.EmpDetailsFacadeLocal;

@RestController
@ComponentScan("com.murphy")
@RequestMapping(value = "/EmpDetails", produces = "application/json")
public class EmpDetailsRest {
	
	@Autowired
	EmpDetailsFacadeLocal empDetailsFacadeLocal;

	@RequestMapping(value = "/createOrUpdate", method = RequestMethod.POST)
	public ResponseMessage EmpDetails(@RequestBody EmpDetailsRequestDto dto) {
		return empDetailsFacadeLocal.empDetails(dto.getEmployee());
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public EmpDetailsResponseDto getEmpDetails(@RequestBody EmpDetailsDto dto) {
		return empDetailsFacadeLocal.getempDetails(dto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseMessage updateEmpDetails(@RequestBody EmpDetailsDto dto) {
		
		return empDetailsFacadeLocal.updateEmpDetails(dto);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public ResponseMessage removeStudent(@RequestBody EmpDetailsDto dto) {
		return empDetailsFacadeLocal.delete(dto);

	}
	
//	@RequestMapping(value = "/UserLoginName", method = RequestMethod.GET)
//	public List<EmpDetailsResponseDto> getUserLoginName() {
//		return empDetailsFacadeLocal.getUserLoginName();
//	}
	
	@RequestMapping(value = "/getEmailId", method = RequestMethod.GET)
	public List<Integer> getEmailId() {
		return empDetailsFacadeLocal.getEmailId();
	}
	
	@RequestMapping(value = "/testttg", method = RequestMethod.GET)
	public String testing() {
		return "Success";
	}
	
	
	
	

}
