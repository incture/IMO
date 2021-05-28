package com.incture.iopptw.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.repositories.JsaDetailsDao;
import com.incture.iopptw.utils.ResponseDto;

@Service
public class JsaDetailsService {

	@Autowired
	private JsaDetailsDao getJsaDetailsDao;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseDto downloadDataService() {
		logger.info("DownloadDataService ");

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			responseDto.setData(getJsaDetailsDao.downloadData());
			responseDto.setMessage("Data displayed successfully");

		} catch (Exception e) {

			logger.error("DownloadDataService " + e.getMessage());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());

		}

		logger.info("DownloadDataService " + responseDto);

		return responseDto;

	}

}
