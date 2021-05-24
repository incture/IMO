package com.incture.iopptw.services;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.dtos.PtwRecordResultResponse;
import com.incture.iopptw.repositories.PtwRecordResultDao;
import com.incture.iopptw.utils.ResponseDto;

@Service
public class PtwRecordResultService {
	@Autowired
	private PtwRecordResultDao ptwRecordResultDao;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseDto getPtwRecordResult(String permitNumber) {

		logger.info("PtwRecordResultService || getPtwRecordResult permitNumber :" + permitNumber);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			PtwRecordResultResponse l = ptwRecordResultDao.getPtwRecordResult(permitNumber);
			if (l != null) {
				responseDto.setData(l);
				responseDto.setMessage("Data displayed successfully");
			} else {
				responseDto.setMessage("Data not found!");
				responseDto.setData(new ArrayList<>());
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("PtwRecordResultService || getPtwRecordResult " + e.getMessage());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setData(new ArrayList<>());
			responseDto.setMessage(e.getMessage());
		}
		logger.info("PtwRecordResultService || getPtwRecordResult " + responseDto);
		return responseDto;

	}

}
