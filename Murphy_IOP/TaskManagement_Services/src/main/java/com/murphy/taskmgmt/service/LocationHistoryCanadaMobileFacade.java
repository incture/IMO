package com.murphy.taskmgmt.service;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dto.CustomDowntimeResponseDto;
import com.murphy.taskmgmt.dto.DowntimeCapturedDto;
import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.FlareDowntimeDto;
import com.murphy.taskmgmt.dto.LocationHistoryMobileDto;
import com.murphy.taskmgmt.dto.LocationHistoryRolledUpDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.LocationHistoryCanadaMobileFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("LocationHistoryCanadaMobileFacade")
public class LocationHistoryCanadaMobileFacade implements LocationHistoryCanadaMobileFacadeLocal{

	
private static final Logger logger = LoggerFactory.getLogger(LocationHistoryCanadaMobileFacade.class);
	

	@Autowired
	DowntimeCapturedFacade downtimeFacade;

	@Autowired
	CompressorDowntimeFacade compressorDowntimeFacade;

	@Autowired
	FlareDowntimeCaptureFacade flareFacade;
	
//	@Override
	public LocationHistoryMobileDto getMobDowntimeHistoryForCanada(DowntimeRequestDto dtoGet) {
		LocationHistoryMobileDto locationHistoryMobileDto = new LocationHistoryMobileDto();
		List<CustomDowntimeResponseDto> cusDownRes = new ArrayList<CustomDowntimeResponseDto>();
		CustomDowntimeResponseDto cusDowntimeWell = null;
		BigDecimal totalCount = BigDecimal.ZERO;
		BigDecimal count1 = BigDecimal.ZERO;

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		if (ServicesUtil.isEmpty(dtoGet.getStatusType()))
			dtoGet.setStatusType("SUBMITTED");

		try {
			List<DowntimeCapturedDto> well = null;
			List<DowntimeCapturedDto> compressor = null; 
			List<FlareDowntimeDto> flare = null;
			
			List<LocationHistoryRolledUpDto> rolledUpWell = null;
			List<LocationHistoryRolledUpDto> rolledUpCom = null;
			List<LocationHistoryRolledUpDto> rolledUpFlare = null;
		
			
			if(!ServicesUtil.isEmpty(downtimeFacade.getDowntimeHierarchyForCanada(dtoGet).getTotalCount())){
				count1 = downtimeFacade.getDowntimeHierarchyForCanada(dtoGet).getTotalCount();
				System.err.println("count for first check :: " +count1);
			    rolledUpWell = downtimeFacade.getDowntimeHierarchyForCanada(dtoGet).getItemList();
			}	

	
			well = downtimeFacade.getDowntimeHierarchyForCanada(dtoGet).getDtoList();
			
			
			if(!ServicesUtil.isEmpty(dtoGet.getType_selected()))
			{
				 String type_selected = dtoGet.getType_selected();
				 List<String> result = Arrays.asList(type_selected.split(","));
				 
				 if(!ServicesUtil.isEmpty(well) && !result.contains("Well")){
					 System.err.println("entering for well count");
					 well = null;
					 count1 = BigDecimal.ZERO;
					 rolledUpWell = null;
				 }
				 
				
				 if(result.contains("None"))
				 {
					 well = null;
					 count1 = BigDecimal.ZERO;
					 rolledUpWell = null;
				
				 }
			}

			SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");

			if (!ServicesUtil.isEmpty(well)) {
				for (DowntimeCapturedDto obj : well) {
					cusDowntimeWell = new CustomDowntimeResponseDto();

					cusDowntimeWell.setId(obj.getId());
					cusDowntimeWell.setType(obj.getType());
					cusDowntimeWell.setWell(obj.getWell());
					cusDowntimeWell.setFacility(obj.getFacility());
					cusDowntimeWell.setDowntimeText(obj.getDowntimeText());
					if(!ServicesUtil.isEmpty(obj.getChildCode()))
					{
					cusDowntimeWell.setChildCode(obj.getChildCode().toString());
					}
					cusDowntimeWell.setChildText(obj.getChildText());
					cusDowntimeWell.setCreatedAt(obj.getCreatedAt());
					cusDowntimeWell.setMuwi(obj.getMuwi());
					cusDowntimeWell.setEquipmentId(obj.getEquipmentId());
					cusDowntimeWell.setStartTime(obj.getStartTime());
					cusDowntimeWell.setEndTime(obj.getEndTime());
					cusDowntimeWell.setStatus(obj.getStatus());
					cusDowntimeWell.setDurationByCygnateHours(obj.getDurationByCygnateHours());
					cusDowntimeWell.setDurationByCygnateMinute(obj.getDurationByCygnateMinute());
					cusDowntimeWell.setDurationByRocHour(obj.getDurationByRocHour());
					cusDowntimeWell.setDurationByRocMinute(obj.getDurationByRocMinute());
					// Changed to Epoch
					cusDowntimeWell.setCreated_At(obj.getCreatedAt());
					cusDowntimeWell.setInMilliSec(obj.getInMilliSec());
					cusDowntimeWell.setCreatedAtUTC(obj.getCreatedAtUTC());

					cusDownRes.add(cusDowntimeWell);
				}
			}

	
			if (!ServicesUtil.isEmpty(cusDownRes)) {
				locationHistoryMobileDto.setCustm(cusDownRes);
				Collections.sort(locationHistoryMobileDto.getCustm(), Collections.reverseOrder());

				totalCount = count1;
				locationHistoryMobileDto.setTotalCount(totalCount);
				locationHistoryMobileDto.setPageCount(new BigDecimal(dtoGet.getPage_size()));
				
				List<LocationHistoryRolledUpDto> itemList = new ArrayList<LocationHistoryRolledUpDto>();
				if(!ServicesUtil.isEmpty(rolledUpWell))
					itemList.addAll(rolledUpWell);
				
				
				if(!ServicesUtil.isEmpty(itemList))
					locationHistoryMobileDto.setItemsList(itemList);
				
				logger.error("itemsList list in canada: " + locationHistoryMobileDto.getItemsList());
				logger.error("Dto list in canada : " + locationHistoryMobileDto.getCustm().toString());
				logger.error("Page Count for canada : " + locationHistoryMobileDto.getPageCount());
				logger.error("Total Count for canada : " + locationHistoryMobileDto.getTotalCount());

				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][LocationHistoryMobileFacade][getMobDowntimeHistoryForCanada][error]" + e.getMessage());
		}

		locationHistoryMobileDto.setResponseMessage(responseMessage);
		return locationHistoryMobileDto;
	}
}
