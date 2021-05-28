package com.incture.iopptw.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.dtos.ActiveWorkersPayloadDto;
import com.incture.iopptw.repositories.ActiveWorkersDao;
import com.incture.iopptw.utils.ResponseDto;

@Service
public class ActiveWorkersService {
	@Autowired
	private ActiveWorkersDao getActiveWorkersDao;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseDto getActiveWorkers(String muwi, String facility) {
		logger.info("GetActiveWorkersService || getActiveWorkers muwi " + muwi + " faciltiy " + facility);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			List<ActiveWorkersPayloadDto> list = getActiveWorkersDao.getActiveWorkers(muwi, facility);
			if (list != null) {
				responseDto.setData(list);
				responseDto.setMessage("Data displayed successfully");
			} else {
				responseDto.setData(new ArrayList<>());
				responseDto.setMessage("Data not found!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GetActiveWorkersService || getActiveWorkers " + e.getMessage());
			logger.error(e.getStackTrace().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setData(new ArrayList<>());
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());

		}

		logger.info("GetActiveWorkersService || getActiveWorkers" + responseDto);

		return responseDto;
	}

}
