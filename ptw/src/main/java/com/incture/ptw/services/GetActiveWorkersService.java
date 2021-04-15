package com.incture.ptw.services;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.ptw.dao.GetActiveWorkersDao;
import com.incture.ptw.util.ResponseDto;

@Service
@Transactional
public class GetActiveWorkersService {
	@Autowired
	private GetActiveWorkersDao getActiveWorkersDao;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseDto getActiveWorkers(String muwi, String facility) {
		logger.info("GetActiveWorkersService || getActiveWorkers muwi " + muwi + " faciltiy " + facility);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			responseDto.setData(getActiveWorkersDao.getActiveWorkers(muwi, facility));
			responseDto.setMessage("Data displayed successfully");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GetActiveWorkersService || getActiveWorkers " + e.getMessage());
			logger.error(e.getStackTrace().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());

		}

		logger.info("GetActiveWorkersService || getActiveWorkers" + responseDto);

		return responseDto;
	}

}
