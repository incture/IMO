package com.incture.ptw.services;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.incture.ptw.dto.JsaDetailsDto;
import com.incture.ptw.dao.JsaByLocationDao;
import com.incture.ptw.util.ResponseDto;

@Service
@Transactional
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
			if (l!=null) {
				responseDto.setData(l);
				responseDto.setMessage("Data displayed successfully");
			}
			responseDto.setMessage("Data not found!");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("JsaByLocationService || getJsaByLocation" + e.getMessage());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());
		}
		logger.info("JsaByLocationService || getJsaByLocation " + responseDto);
		return responseDto;

	}

}
