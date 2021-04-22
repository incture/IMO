package com.incture.ptw.services;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.ptw.dao.GetJsaByPermitNumDao;
import com.incture.ptw.util.ResponseDto;

@Service
@Transactional
public class GetJsaByPermitNumService {
	@Autowired
	private GetJsaByPermitNumDao getJsaByPermitNumDao;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	public ResponseDto getJsaByPermitNum (String permitNumber){
		logger.info("GetJsaByPermitNumServic || getJsaByPermitNum permitNumber " + permitNumber);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try{
			responseDto.setData(getJsaByPermitNumDao.getJsaByPermitNum(permitNumber));
			responseDto.setMessage("Data displayed successfully");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("GetJsaByPermitNumServic || getJsaByPermitNum " + e.getMessage());
			logger.error(e.getStackTrace().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());
		}
		logger.info("GetJsaByPermitNumServic || getJsaByPermitNum " + responseDto);

		return responseDto;
	}
}
