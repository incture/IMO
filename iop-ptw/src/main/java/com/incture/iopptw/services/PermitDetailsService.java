package com.incture.iopptw.services;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.dtos.PtwDetailsDto;
import com.incture.iopptw.repositories.PermitDetailsDao;
import com.incture.iopptw.utils.ResponseDto;

@Service
public class PermitDetailsService {
	@Autowired
	private PermitDetailsDao permitDetailsDao;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseDto getPermitDetails(String permitNumber, String permitType) {
		logger.info("PermitDetailsService");

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			PtwDetailsDto ptwDetailsDto = permitDetailsDao.getPermitDetails(permitNumber, permitType);
			if (ptwDetailsDto != null) {
				responseDto.setData(ptwDetailsDto);
				responseDto.setMessage("Data displayed successfully");
			} else {
				responseDto.setData(new ArrayList<>());
				responseDto.setMessage("Data not found!");
			}
		} catch (Exception e) {

			logger.error("PermitDetailsService " + e.getMessage());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setData(new ArrayList<>());
			responseDto.setMessage(e.getMessage());

		}

		logger.info("DownloadDataService " + responseDto);

		return responseDto;

	}
}
