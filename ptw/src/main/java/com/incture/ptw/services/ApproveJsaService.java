package com.incture.ptw.services;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.ptw.dao.ApproveJsaDao;
import com.incture.ptw.util.ResponseDto;

@Service
@Transactional
public class ApproveJsaService {
	@Autowired
	private ApproveJsaDao approveJsaDao;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseDto approveJsa(String jsaPermitNumber, String status, String approvedBy) {
		logger.info("ApproveJsa || approveJsa || jsaPermitNumber: " + jsaPermitNumber + " status: " + status
				+ " approvedBy: " + approvedBy);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			String permitNumber = approveJsaDao.approveJsa(jsaPermitNumber, status, approvedBy);
			responseDto.setMessage("Success: JSA " + permitNumber + " approved succesfully.");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ApproveJsa || approveJsa || Error" + e.getMessage());
			logger.error(e.getStackTrace().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());

		}

		logger.info("ApproveJsa " + responseDto);

		return responseDto;
	}

}
