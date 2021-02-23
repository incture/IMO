package com.murphy.taskmgmt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dto.FieldResponseDto;
import com.murphy.taskmgmt.dto.HierarchyRequestDto;
import com.murphy.taskmgmt.dto.HierarchyResponseDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.LocationHierarchyResponseDto;
import com.murphy.taskmgmt.dto.LocationResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.LocationHierarchyLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("locationHierarchy")
public class LocationHierarchy implements LocationHierarchyLocal {
	
	private static final Logger logger = LoggerFactory.getLogger(LocationHierarchy.class);

	@Autowired
	private HierarchyDao locDao;

	@Override
	public LocationResponseDto getHierarchy(HierarchyRequestDto dto) {
		LocationResponseDto locResponseDto = new LocationResponseDto();
		ResponseMessage message = new ResponseMessage();
		message.setMessage(MurphyConstant.READ_FAILURE);
		message.setMessage(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			HierarchyResponseDto responseDto = locDao.getHierarchy(dto);
			if (ServicesUtil.isEmpty(responseDto)) {
				message.setMessage("LOCATION TYPE NOT FOUND");
			} else if (ServicesUtil.isEmpty(responseDto.getLocationHierarchy())) {
				message.setMessage(MurphyConstant.NO_RESULT);
			} else {
				locResponseDto.setDto(responseDto);
			}
			message.setMessage(MurphyConstant.SUCCESS);
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}catch(Exception e){
			logger.error("[Murphy][LocationHierarchy][getHierarchy][error]"+e.getMessage());
		}
		locResponseDto.setMessage(message);
		return locResponseDto;
	}


	@Override
	public FieldResponseDto getFeild(String loc,String locType) {
		return locDao.getFeild(loc, locType,false);
	}
	
	@Override
	public LocationHierarchyDto getWellDetails(String muwi){
		return locDao.getWellDetailsForMuwi(muwi);
	}

	@Override
	public LocationHierarchyResponseDto getParentByCompressor(String compressorId) {
		return locDao.getParentOfCompressor(compressorId);
	}
	
	@Override
	public FieldResponseDto getFieldTextForLocCode(List<String> locCode,String locType) {
		return locDao.getFieldText(locCode, locType);
	}
	
}
