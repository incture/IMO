package com.incture.iopptw.services;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.dtos.PermitsByLocPayloadDto;
import com.incture.iopptw.repositories.PermitsByLocDao;
import com.incture.iopptw.utils.ResponseDto;

@Service
@Transactional
public class PermitsByLocService {
	@Autowired
	private PermitsByLocDao permitsByLocDao;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseDto getPermitsByLoc(String muwi, String facility) {
		logger.info("PermitsByLocService || getPermitsByLoc muwi " + muwi + " faciltiy " + facility);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			PermitsByLocPayloadDto permitsByLocPayloadDto = permitsByLocDao.getPermitsByLoc(muwi, facility);
			if (permitsByLocPayloadDto != null) {
				responseDto.setData(permitsByLocPayloadDto);
				responseDto.setMessage("Data displayed successfully");
			} else {
				responseDto.setData(new ArrayList<>());
				responseDto.setMessage("Data not found!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("PermitsByLocService || getPermitsByLoc " + e.getMessage());
			logger.error(e.getStackTrace().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setData(new ArrayList<>());
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());

		}
		logger.info("PermitsByLocService || getPermitsByLoc" + responseDto);

		return responseDto;
	}

}
