package com.incture.iopptw.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.dtos.IOPJsaDto;
import com.incture.iopptw.repositories.IOPJsaDao;
import com.incture.iopptw.utils.ResponseDto;

@Service
public class IOPJsaService {
	@Autowired
	private IOPJsaDao iopJsaDao;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public ResponseDto getJsaList(String facilityOrSite, Integer isActive){
		logger.info("IOPJsaService || getJsaList facilityOrSite " + facilityOrSite + " isActive " + isActive);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		responseDto.setMessage("Success");
		try {
			List<IOPJsaDto> data = iopJsaDao.getJsaList(facilityOrSite, isActive);
			if(data == null){
				responseDto.setMessage("No data found!!");
			}
			responseDto.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("IOPJsaService || getJsaList facilityOrSite " + e.getMessage());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setData(new ArrayList<>());
			responseDto.setMessage(e.getMessage());
		}
		logger.info("IOPJsaService || getJsaList facilityOrSite " + responseDto);
		return responseDto;
	}
}
