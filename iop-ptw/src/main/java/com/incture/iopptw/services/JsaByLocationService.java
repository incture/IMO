package com.incture.iopptw.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.dtos.JsaDetailsDto;
import com.incture.iopptw.repositories.JsaByLocationDao;
import com.incture.iopptw.utils.ResponseDto;

@Service
public class JsaByLocationService {

	@Autowired
	private JsaByLocationDao jsaByLocationDao;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseDto getJsaByLocation(String muwi, String facility) {
		logger.info("JsaByLocationService || getJsaByLocation muwi " + muwi + " faciltiy " + facility);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			List<JsaDetailsDto> l = jsaByLocationDao.getJsaByLocation(muwi, facility);
			if (l != null) {
				responseDto.setData(l);
				responseDto.setMessage("Data displayed successfully");
			} else {
				responseDto.setMessage("Data not found!");
				responseDto.setData(new ArrayList<>());
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("JsaByLocationService || getJsaByLocation" + e.getMessage());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setData(new ArrayList<>());
			responseDto.setMessage(e.getMessage());
		}
		logger.info("JsaByLocationService || getJsaByLocation " + responseDto);
		return responseDto;

	}

}
