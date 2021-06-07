package com.incture.iopptw.services;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.repositories.DftDepartmentsDao;
import com.incture.iopptw.utils.ResponseDto;

@Service
public class DftDepartmentsService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private DftDepartmentsDao dftDepartmentsDao;

	public ResponseDto getAllDepartments() {
		logger.info("DftDepartmentsService || getAllDepartments");

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			responseDto.setData(dftDepartmentsDao.getAll());
			responseDto.setMessage("Success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("DftDepartmentsService || getAllDepartments" + e.getMessage());
			logger.error(e.getStackTrace().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setData(new ArrayList<>());
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());
		}
		return responseDto;
	}

}
