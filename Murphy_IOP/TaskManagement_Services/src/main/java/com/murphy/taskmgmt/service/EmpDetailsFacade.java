package com.murphy.taskmgmt.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.EmpDetailsDao;
import com.murphy.taskmgmt.dto.EmpDetailsDto;
import com.murphy.taskmgmt.dto.EmpDetailsResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

import com.murphy.taskmgmt.service.interfaces.EmpDetailsFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;

@Service("EmpDetailsFacade")
public class EmpDetailsFacade implements EmpDetailsFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(EmpDetailsFacade.class);

	@Autowired
	private EmpDetailsDao empDetailsDao;
	
	@Override
	public ResponseMessage empDetails(List<EmpDetailsDto> dto) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Employee Details Insertion " + MurphyConstant.FAILURE);
		String response;
		try {
			response = empDetailsDao.saveOrUpadte(dto);
			if(response.equalsIgnoreCase(MurphyConstant.SUCCESS)){
			responseDto.setStatus(MurphyConstant.SUCCESS);
			responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseDto.setMessage("Employee Details Inserted " + MurphyConstant.SUCCESSFULLY);
			}

			return responseDto;
		} catch (Exception e) {
			logger.error("[Murphy][EmpDetailsFacade][empDetails][Exception]" + e.getMessage());
		}
		return responseDto;
	}

	@Override
	public ResponseMessage updateEmpDetails(EmpDetailsDto dto) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Employee Details Upadte" + MurphyConstant.FAILURE);

		try {
			empDetailsDao.updateEmpDetails(dto);
			responseDto.setStatus(MurphyConstant.SUCCESS);
			responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseDto.setMessage("Employee Details Updated " + MurphyConstant.SUCCESSFULLY);

		} catch (Exception e) {
			logger.error("[Murphy][EmpDetailsFacade][updateEmpDetails][Exception]" + e.getMessage());
		}
		return responseDto;
	}

	@Override
	public ResponseMessage delete(EmpDetailsDto dto) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Employee Details Deletion " + MurphyConstant.FAILURE);

		try {

			empDetailsDao.delete(dto);
			responseDto.setStatus(MurphyConstant.SUCCESS);
			responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseDto.setMessage("Employee Details Deleted " + MurphyConstant.SUCCESSFULLY);

		} catch (Exception e) {
			logger.error("[Murphy][EmpDetailsFacade][delete][Exception]" + e.getMessage());
		}
		return responseDto;
	}

	public EmpDetailsResponseDto getempDetails(EmpDetailsDto dto) {
		EmpDetailsResponseDto empDetailsResponseDto = new EmpDetailsResponseDto();
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Employee Details Fetch " + MurphyConstant.FAILURE);
		EmpDetailsDto empDetailsDto;
		try {
			empDetailsDto = empDetailsDao.getByKeys(dto);
			responseDto.setStatus(MurphyConstant.SUCCESS);
			responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseDto.setMessage("Employee Details Fetched " + MurphyConstant.SUCCESSFULLY);
			empDetailsResponseDto.setResponseMessage(responseDto);
			empDetailsResponseDto.setEmpDetailsDto(empDetailsDto);

		} catch (Exception e) {
			logger.error("[Murphy][EmpDetailsFacade][getempDetails][Exception]" + e.getMessage());
		}
		return empDetailsResponseDto;
	}

//	@Override
//	public List<EmpDetailsResponseDto> getUserLoginName() {
//
//		try {
//			return empDetailsDao.getUserLoginName();
//		} catch (Exception e) {
//			logger.error("[Murphy][EmpDetailsFacade][getUserLoginName][error]" + e.getMessage());
//		}
//		return null;
//
//	}
	
	@Override
	public List<Integer> getEmailId() {
		try {
			return empDetailsDao.getEmailList();

		} catch (Exception e) {
			logger.error("[Murphy][EmpDetailsFacade][getEmailId][Exception]" + e.getMessage());
		}
		return null;
	}
}
