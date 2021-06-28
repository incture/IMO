package com.incture.iopptw.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.dtos.IOPPtwDto;
import com.incture.iopptw.repositories.IOPPtwDao;
import com.incture.iopptw.utils.ResponseDto;

@Service
public class IOPPtwService {
	@Autowired
	private IOPPtwDao iopPtwDao;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseDto getPtwList(int isCwp, int isHwp, int isCse, String location, int isActive) {
		logger.info("IOPPtwService || getPtwList isCwp " + isCwp + " isHwp " + isHwp + " isCse " + isCse + " location "
				+ location + " isActive " + isActive);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		responseDto.setMessage("Success");
		try {
			List<IOPPtwDto> data = iopPtwDao.getPtwList(isCwp, isHwp, isCse, location, isActive);
			if(data == null){
				responseDto.setMessage("No data found!!");
			}
			responseDto.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("IOPPtwService || getPtwList " + e.getMessage());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setData(new ArrayList<>());
			responseDto.setMessage(e.getMessage());
		}
		logger.info("IOPPtwService || getPtwList " + responseDto);
		return responseDto;
	}

}
