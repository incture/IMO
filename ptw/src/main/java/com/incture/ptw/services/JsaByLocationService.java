package com.incture.ptw.services;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.ptw.dao.JsaByLocationDao;
import com.incture.ptw.util.ResponseDto;

@Service
@Transactional
public class JsaByLocationService {

	@Autowired
	private JsaByLocationDao jsaByLocationDao;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseDto getJsaByLocation(String muwi, String facility) {
		logger.info("JsaByLocationService ");

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			responseDto.setData(jsaByLocationDao.getJsaByLocation(muwi, facility));
			responseDto.setMessage("Data displayed successfully");

		} catch (Exception e) {

			logger.error("JsaByLocationService " + e.getMessage());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());

		}

		logger.info("JsaByLocationService " + responseDto);

		return responseDto;

	}

}
