package com.murphy.taskmgmt.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.integration.dto.UIRequestDto;
import com.murphy.taskmgmt.dao.ProductionVarianceDao;
import com.murphy.taskmgmt.dto.DOPVarianceDto;
import com.murphy.taskmgmt.dto.DOPVarianceResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.VarianceFacadeLocal;
import com.murphy.taskmgmt.service.DGPFacade;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;


@Service("varianceFacade")
public class VarianceFacade implements VarianceFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(VarianceFacade.class);

	public VarianceFacade() {
	}


	@Autowired
	private ProductionVarianceDao varianceDao;
	

	@Override
	public DOPVarianceResponseDto fetchVarianceData(UIRequestDto uiRequestDto) {
		DOPVarianceResponseDto responseDto = new DOPVarianceResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{

			List<DOPVarianceDto> dopVarianceDtoList = varianceDao.fetchVarianceDataForWells(uiRequestDto);
			Date date = new Date();
			if(uiRequestDto.getDuration() == 1){
		
			date = ServicesUtil.roundDateToNearInterval(date, 60, MurphyConstant.MINUTES);
			
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR ,-1);
			calendar.setTime(date);
			calendar.set(Calendar.SECOND, 00);
			calendar.set(Calendar.MINUTE, 00);
			date = calendar.getTime();
			}
			else {
				date = varianceDao.scaleDownTimeToSeventhHour(date).getTime();
			}
			responseDto.setDataAsOffDisplay(ServicesUtil.convertFromZoneToZoneString(date, null, "", "", "", MurphyConstant.DATE_DISPLAY_FORMAT));
			
			//Converting to epoch
			String str = responseDto.getDataAsOffDisplay();
			SimpleDateFormat df = new SimpleDateFormat(MurphyConstant.DATE_DISPLAY_FORMAT);
			Date d1 = df.parse(str);
			long epoch = d1.getTime();
			responseDto.setDataAsOffDisplayEpoch(epoch);
			logger.error(String.valueOf(epoch));
			//
			
			responseDto.setLocationType(uiRequestDto.getLocationType());
			if(!ServicesUtil.isEmpty(dopVarianceDtoList)){
				responseDto.setDopVarianceDtoList(dopVarianceDtoList);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			}else{
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			
		}catch(Exception e){
			logger.error("[Murphy][VarianceFacade][fetchVarianceData][error]"+e.getMessage());
		}
		
		responseDto.setResponseMessage(responseMessage);
		return responseDto;
	}
	
	@Override
	public String createRecord() {
		
		String response = varianceDao.createRecord();
		return response;
		
	}

}
