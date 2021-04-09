package com.incture.ptw.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.incture.ptw.dao.GetJsaDetailsDao;
import com.incture.ptw.util.ResponseDto;

@Service
@Transactional
public class GetJsaDetailsService {

	@Autowired
	private GetJsaDetailsDao getJsaDetailsDao;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseDto downloadDataService() {
		logger.info("DownloadDataService "  );

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			responseDto.setData(getJsaDetailsDao.downloaadData());
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
