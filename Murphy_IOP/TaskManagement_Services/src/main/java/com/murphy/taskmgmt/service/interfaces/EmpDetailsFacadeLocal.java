package com.murphy.taskmgmt.service.interfaces;

import java.util.List;

import com.murphy.taskmgmt.dto.EmpDetailsDto;
import com.murphy.taskmgmt.dto.EmpDetailsResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;


public interface EmpDetailsFacadeLocal {

	ResponseMessage empDetails(List<EmpDetailsDto> dto);

	ResponseMessage updateEmpDetails(EmpDetailsDto dto);

	ResponseMessage delete(EmpDetailsDto dto);

	EmpDetailsResponseDto getempDetails(EmpDetailsDto dto);

	//List<EmpDetailsResponseDto> getUserLoginName();

	List<Integer> getEmailId();


}
