package com.incture.iopptw.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.dtos.DftGetReviewerPayloadDto;
import com.incture.iopptw.dtos.DftGetReviewerResponseDto;
import com.incture.iopptw.repositories.DftGetReviewerByLocDao;
import com.incture.iopptw.utils.ResponseDto;

@Service
public class DftGetReviewerService {
	@Autowired
	private DftGetReviewerByLocDao dftGetReviewerByLocDao;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseDto getReviewerByLoc(DftGetReviewerPayloadDto d) {
		logger.info("DftGetReviewerService || getReviewerByLoc " + d);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			List<DftGetReviewerResponseDto> data = dftGetReviewerByLocDao.getReviewerByLoc(d);
			responseDto.setData(data);
		} catch (Exception e) {
			logger.error("DftGetReviewerService || getReviewerByLoc " + e.getMessage());
			logger.error(e.getCause().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setData(new ArrayList<>());
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());
		}
		logger.info("DftGetReviewerService || getReviewerByLoc " + responseDto);
		return responseDto;
	}

}
