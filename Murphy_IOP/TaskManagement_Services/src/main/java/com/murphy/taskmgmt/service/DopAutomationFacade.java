package com.murphy.taskmgmt.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.ProductionVarianceDao;
import com.murphy.taskmgmt.dto.DOPResponseDto;
import com.murphy.taskmgmt.dto.DopDummyDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.DopAutomationFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;

//these classes are no longer used
@Service
public class DopAutomationFacade implements DopAutomationFacadeLocal{

	private static final Logger logger = LoggerFactory.getLogger(DopAutomationFacade.class);
	
	@Autowired
	private ProductionVarianceDao varianceDao;
	
	
	@Override
	public List<DopDummyDto> fetchVarianceData(){
		List<DopDummyDto> dopDtoList= new ArrayList<DopDummyDto>();
		DopDummyDto dummyDto= new DopDummyDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			
			dopDtoList = varianceDao.fetchVarianceData();
			
		}catch(Exception e){
			logger.error("[Murphy][VarianceFacade][fetchVarianceData][error]"+e.getMessage());
		}
		
		dummyDto.setResponseMessage(responseMessage);
		dopDtoList.add(dummyDto);
		return dopDtoList;
	}	
	
	
	public DOPResponseDto dgpQueryForOtherDetails(String locationCodesList)
	{
		return varianceDao.dgpQueryForOtherDetails(locationCodesList);
	}
}
