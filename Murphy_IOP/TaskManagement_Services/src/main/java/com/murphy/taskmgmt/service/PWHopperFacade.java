package com.murphy.taskmgmt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.integration.dto.UIRequestDto;
import com.murphy.taskmgmt.dao.PWHopperStagingDao;
import com.murphy.taskmgmt.dto.CheckListDto;
import com.murphy.taskmgmt.dto.CheckListResponseDto;
import com.murphy.taskmgmt.dto.PWHopperDto;
import com.murphy.taskmgmt.dto.PWHopperResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.PWHopperFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("pwHopperFacade")
public class PWHopperFacade implements PWHopperFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(PWHopperFacade.class);

	public PWHopperFacade() {
	}

	@Autowired
	private PWHopperStagingDao pwHopperDao;



	@Override
	public CheckListResponseDto getCheckList(String userType, String locationCode ,String investigationId) {
		CheckListResponseDto responseDto = new CheckListResponseDto();
		ResponseMessage responseMessage  = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			List<CheckListDto> checkList =  pwHopperDao.getCheckList(userType, locationCode ,investigationId, false);
			if(!ServicesUtil.isEmpty(checkList)){
				responseDto.setCheckList(checkList);
				boolean isProactive = pwHopperDao.isProactiveCandidate(locationCode);
				responseDto.setIsProactive(ServicesUtil.isEmpty(isProactive)? false :isProactive);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			}
			else{
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][PWHopperFacade][getCheckList][error]"+e.getMessage());
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;

	}

	@Override
	public ResponseMessage saveOrUpdateInvestInsts(CheckListResponseDto requestDto , boolean isProactive){
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.SAVE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		try{
			if(pwHopperDao.checkIfInvestigationDataExists(requestDto.getInvestigationId())){
				pwHopperDao.updateInvstInstance(requestDto.getInvestigationId(), requestDto.getUserType(), requestDto.getCheckList(),requestDto.getLoggedInUser() );
			}else{
				String hopperId = ServicesUtil.getUUID();
				pwHopperDao.updateProactive(requestDto.getLocationCode(), isProactive, requestDto.getInvestigationId(), hopperId, true);
				pwHopperDao.createInvstInstance("", "", requestDto.getInvestigationId(), requestDto.getCheckList() ,hopperId,requestDto.getLoggedInUser(),false);			}
			responseMessage.setMessage(MurphyConstant.SAVE_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}catch(Exception e){
			System.err.println("[Murphy][TaskEventsDao][saveOrUpdateInvestInsts][error]" + e.getMessage());
		}
		return responseMessage;

	}

	@Override
	public ResponseMessage removeProactive(String locationCode) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		try{
			pwHopperDao.updateProactive(locationCode,false, "" ,"" , true);
			responseMessage.setMessage(MurphyConstant.UPDATE_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}catch(Exception e){
			System.err.println("[Murphy][TaskEventsDao][removeProactive][error]" + e.getMessage());
		}
		return responseMessage;

	}

	@Override
	public PWHopperResponseDto getpwHopperList(UIRequestDto requestDto) {
		PWHopperResponseDto responseDto = new PWHopperResponseDto();
		ResponseMessage responseMessage  = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			List<PWHopperDto> hopperDtoList = pwHopperDao.getpwHopperList(requestDto);
			/*List<PWHopperDto> hopperDtoList =  new ArrayList<PWHopperDto>();
			PWHopperDto dto = new PWHopperDto();
			dto.setAlsStatus("Grey");
			dto.setPotStatus("Red");
			dto.setHasInvestigation(false);
			dto.setPotentialUplift("234");
			dto.setBoe("123");
			dto.setLocation("loc1");
			dto.setLocationCode("Code1");
			dto.setMuwi("muwi");
			dto.setProactive(false);
			dto.setReStatus("Grey");
			dto.setWwStatus("Grey");
			dto.setWorkOverCost("567");
			hopperDtoList.add(dto);*/
			if(!ServicesUtil.isEmpty(hopperDtoList)){
				responseDto.setHopperDtoList(hopperDtoList);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			}
			else{
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][PWHopperFacade][getpwHopperList][error]"+e.getMessage());
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;
	}

	@Override
	public String setDataForPWHopperWell() {
		return pwHopperDao.setDataForPWHopperWell();
	}


}
