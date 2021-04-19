package com.incture.ptw.services;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.ptw.dao.PermitsByLocDao;
import com.incture.ptw.util.ResponseDto;

@Service
@Transactional
public class PermitsByLocService {
	@Autowired
	private PermitsByLocDao permitsByLocDao;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public ResponseDto getPermitsByLoc(String muwi, String facility){
		logger.info("PermitsByLocService || getPermitsByLoc muwi " + muwi + " faciltiy " + facility);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			responseDto.setData(permitsByLocDao.getPermitsByLoc(muwi, facility));
			responseDto.setMessage("Data displayed successfully");

		}catch (Exception e) {
			e.printStackTrace();
			logger.error("PermitsByLocService || getPermitsByLoc " + e.getMessage());
			logger.error(e.getStackTrace().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());

		}
		logger.info("PermitsByLocService || getPermitsByLoc" + responseDto);

		return responseDto;
	}
	

}
