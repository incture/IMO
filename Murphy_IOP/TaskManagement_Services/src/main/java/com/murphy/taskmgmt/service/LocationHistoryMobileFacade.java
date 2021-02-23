package com.murphy.taskmgmt.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
import com.murphy.taskmgmt.service.interfaces.LocationHistoryMobileFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("LocationHistoryMobileFacade")
public class LocationHistoryMobileFacade implements LocationHistoryMobileFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(LocationHistoryMobileFacade.class);

	@Autowired
	DowntimeCapturedFacade downtimeFacade;

	@Autowired
	CompressorDowntimeFacade compressorDowntimeFacade;

	@Autowired
	FlareDowntimeCaptureFacade flareFacade;

	@Override
	public LocationHistoryMobileDto getMobDowntimeHistory(DowntimeRequestDto dtoGet) {
		LocationHistoryMobileDto locationHistoryMobileDto = new LocationHistoryMobileDto();
		List<CustomDowntimeResponseDto> cusDownRes = new ArrayList<CustomDowntimeResponseDto>();
		CustomDowntimeResponseDto cusDowntimeWell = null;
		BigDecimal totalCount = BigDecimal.ZERO;
		BigDecimal count1 = BigDecimal.ZERO, count2 = BigDecimal.ZERO, count3 = BigDecimal.ZERO;

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
			
			
			if(!ServicesUtil.isEmpty(downtimeFacade.getDowntimeHierarchy(dtoGet).getTotalCount())){
				count1 = downtimeFacade.getDowntimeHierarchy(dtoGet).getTotalCount();
			    rolledUpWell = downtimeFacade.getDowntimeHierarchy(dtoGet).getItemList();
			}	
			if(!ServicesUtil.isEmpty(compressorDowntimeFacade.getDowntimeHierarchy(dtoGet).getTotalCount())){
				count2 = compressorDowntimeFacade.getDowntimeHierarchy(dtoGet).getTotalCount();
				rolledUpCom = compressorDowntimeFacade.getDowntimeHierarchy(dtoGet).getItemList();
			}	
			if(!ServicesUtil.isEmpty(flareFacade.getDowntimeHierarchy(dtoGet).getTotalCount())){
				count3 = flareFacade.getDowntimeHierarchy(dtoGet).getTotalCount();
				rolledUpFlare = flareFacade.getDowntimeHierarchy(dtoGet).getItemList();
			}	
	
			well = downtimeFacade.getDowntimeHierarchy(dtoGet).getDtoList();
			compressor = compressorDowntimeFacade.getDowntimeHierarchy(dtoGet).getDtoList();
			flare = flareFacade.getDowntimeHierarchy(dtoGet).getDtoList();
			
			
			if(!ServicesUtil.isEmpty(dtoGet.getType_selected()))
			{
				 String type_selected = dtoGet.getType_selected();
				 List<String> result = Arrays.asList(type_selected.split(","));
				 
				 if(!ServicesUtil.isEmpty(well) && !result.contains("Well")){
					 well = null;
					 count1 = BigDecimal.ZERO;
					 rolledUpWell = null;
				 }
				 
				 if(!ServicesUtil.isEmpty(compressor) && !result.contains("Compressor")) {
					 compressor = null;
					 count2 = BigDecimal.ZERO;
					 rolledUpCom = null;
				 }
				 if(!ServicesUtil.isEmpty(flare) && !result.contains("Flare")) {
					 flare = null;
					 count3 = BigDecimal.ZERO;
					 rolledUpFlare = null;
				 }
				 if(result.contains("None"))
				 {
					 well = null;
					 count1 = BigDecimal.ZERO;
					 rolledUpWell = null;
					 compressor = null;
					 count2 = BigDecimal.ZERO;
					 rolledUpCom = null;
					 flare = null;
					 count3 = BigDecimal.ZERO;
					 rolledUpFlare = null;
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
					cusDowntimeWell.setChildCode(obj.getChildCode().toString());
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

			if (!ServicesUtil.isEmpty(compressor)) {
				for (DowntimeCapturedDto obj : compressor) {
					cusDowntimeWell = new CustomDowntimeResponseDto();

					cusDowntimeWell.setId(obj.getId());
					cusDowntimeWell.setType(obj.getType());
					cusDowntimeWell.setWell(obj.getWell());
					cusDowntimeWell.setFacility(obj.getFacility());
					cusDowntimeWell.setDowntimeText(obj.getDowntimeText());
					cusDowntimeWell.setChildCode(obj.getChildCode().toString());
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
				/*	cusDowntimeWell.setCreated_At(format.parse(cusDowntimeWell.getCreatedAt()));
//					Note : On deploying in PROD ,change zone to CST
					DateFormat formatterIST = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
					//formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Use for MCQ
					formatterIST.setTimeZone(TimeZone.getTimeZone("CST")); // Use for PROD
					//For Mobile
					Date date = formatterIST.parse(cusDowntimeWell.getCreatedAt());
					cusDowntimeWell.setInMilliSec(String.valueOf((date.getTime())));
					DateFormat formatterUTC = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
					formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
					//For Web
					cusDowntimeWell.setCreatedAtUTC(formatterUTC.format(date)); */

					cusDownRes.add(cusDowntimeWell);
				}
			}

			if (!ServicesUtil.isEmpty(flare)) {
				for (FlareDowntimeDto obj : flare) {
					cusDowntimeWell = new CustomDowntimeResponseDto();

					cusDowntimeWell.setId(obj.getId());
					cusDowntimeWell.setType(obj.getType());
					cusDowntimeWell.setMeter(obj.getMeter());
					cusDowntimeWell.setWell(obj.getMeter());
					cusDowntimeWell.setChildText(obj.getChildText());
					cusDowntimeWell.setChildCode(obj.getChildCode());
					cusDowntimeWell.setCreatedAt(obj.getCreatedAt());
					cusDowntimeWell.setMerrickId(obj.getMerrickId());
					cusDowntimeWell.setStartTime(obj.getStartTime());
					cusDowntimeWell.setEndTime(obj.getEndTime());
					cusDowntimeWell.setStatus(obj.getStatus());
					cusDowntimeWell.setFlareVolume(obj.getFlareVolume());
					cusDowntimeWell.setDurationByRocHour(obj.getDurationByRocHour());
					cusDowntimeWell.setDurationByRocMinute(obj.getDurationByRocMinute());
					
					// Changed to Epoch
					cusDowntimeWell.setCreated_At(obj.getCreatedAt());
					cusDowntimeWell.setInMilliSec(obj.getInMilliSec());
					cusDowntimeWell.setCreatedAtUTC(obj.getCreatedAtUTC());
					
				/*	DateFormat formatterIST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
					Date date_for_sort = formatterIST.parse(cusDowntimeWell.getCreatedAt());
					String date_sort = format.format(date_for_sort);
					cusDowntimeWell.setCreated_At(format.parse(date_sort)); //cusDowntimeWell.getCreatedAt()
					
//					Note : On deploying in PROD ,change zone to CST
					//formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Use for MCQ
					formatterIST.setTimeZone(TimeZone.getTimeZone("CST"));  // Use for PROD
					Date date = formatterIST.parse(cusDowntimeWell.getCreatedAt());
					//For Mobile
					cusDowntimeWell.setInMilliSec(String.valueOf((date.getTime())));
					
					// Increment date by one day
					Calendar cal = Calendar.getInstance();
					try{
						   cal.setTime(formatterIST.parse(formatterIST.format(date)));
						}catch(Exception e){
						   e.printStackTrace();
						}
					cal.add(Calendar.DAY_OF_MONTH, 1);
					
					DateFormat formatterUTC = new SimpleDateFormat("dd-MMM-yy");
					formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
					//For Web
					cusDowntimeWell.setCreatedAtUTC(formatterUTC.format(cal.getTime()));
					logger.error("cusDowntimeWell.getInMilliSec "+cusDowntimeWell.getInMilliSec()); */

					cusDownRes.add(cusDowntimeWell);
				}
			}
			if (!ServicesUtil.isEmpty(cusDownRes)) {
				locationHistoryMobileDto.setCustm(cusDownRes);
				Collections.sort(locationHistoryMobileDto.getCustm(), Collections.reverseOrder());

				totalCount = (count1.add(count2)).add(count3);
				locationHistoryMobileDto.setTotalCount(totalCount);
				locationHistoryMobileDto.setPageCount(new BigDecimal(dtoGet.getPage_size()));
				
				List<LocationHistoryRolledUpDto> itemList = new ArrayList<LocationHistoryRolledUpDto>();
				if(!ServicesUtil.isEmpty(rolledUpWell))
					itemList.addAll(rolledUpWell);
				if(!ServicesUtil.isEmpty(rolledUpCom))
					itemList.addAll(rolledUpCom);
				if(!ServicesUtil.isEmpty(rolledUpFlare))
					itemList.addAll(rolledUpFlare);
				
				if(!ServicesUtil.isEmpty(itemList))
					locationHistoryMobileDto.setItemsList(itemList);
				
				logger.error("itemsList list : " + locationHistoryMobileDto.getItemsList());
				logger.error("Dto list : " + locationHistoryMobileDto.getCustm().toString());
				logger.error("Page Count : " + locationHistoryMobileDto.getPageCount());
				logger.error("Total Count : " + locationHistoryMobileDto.getTotalCount());

				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][LocationHistoryMobileFacade][getMobDowntimeHistory][error]" + e.getMessage());
		}

		locationHistoryMobileDto.setResponseMessage(responseMessage);
		return locationHistoryMobileDto;
	}

}
