package com.incture.ptw.services;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.ptw.dao.PtwCloseOutDao;
import com.incture.ptw.dto.CloseOutReqDto;
import com.incture.ptw.util.ResponseDto;

@Service
@Transactional
public class CloseOutService {
	@Autowired
	private PtwCloseOutDao ptwCloseOutDao;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	public ResponseDto closeOutService (CloseOutReqDto closeOutReqDto){
		logger.info("CloseOutService | closeOutService |closeOutReqDto" + closeOutReqDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try{
			responseDto.setData(ptwCloseOutDao.closeOutService(closeOutReqDto));
			responseDto.setMessage("Success");
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("CloseOutService | closeOutService " + e.getMessage());
			logger.error(e.getStackTrace().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());
		}
		logger.info("CloseOutService | closeOutService" + responseDto);
		return responseDto;
	}
}
