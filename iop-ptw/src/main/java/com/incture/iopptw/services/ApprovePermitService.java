package com.incture.iopptw.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.dtos.ApprovePermitDto;
import com.incture.iopptw.repositories.ApprovePermitDao;
import com.incture.iopptw.utils.ResponseDto;

@Service
public class ApprovePermitService {
	@Autowired
	private ApprovePermitDao approvePermitDao;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseDto approvePermit(ApprovePermitDto approvePermitDto) {
		logger.info("ApprovePermit: " + approvePermitDto);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			Integer permitNumber = approvePermitDao.approvePermit(approvePermitDto);
			responseDto.setData("Success: " + permitNumber + " approved succesfully.");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ApprovePermit: " + e.getMessage());
			logger.error(e.getStackTrace().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());

		}

		logger.info("ApprovePermit: " + responseDto);

		return responseDto;
	}

}
