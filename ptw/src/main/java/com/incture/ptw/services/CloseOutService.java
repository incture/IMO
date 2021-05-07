package com.incture.ptw.services;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.ptw.dao.PtwCloseOutDao;
import com.incture.ptw.dao.PtwHeaderDao;
import com.incture.ptw.dao.PtwTestResultsDao;
import com.incture.ptw.dto.CloseOutReqDto;
import com.incture.ptw.dto.PtwCloseOut1Dto;
import com.incture.ptw.dto.PtwTestResultsDto;
import com.incture.ptw.util.ResponseDto;

@Service
@Transactional
public class CloseOutService {
	@Autowired
	private PtwCloseOutDao ptwCloseOutDao;

	@Autowired
	private PtwHeaderDao ptwHeaderDao;
	
	@Autowired
	private PtwTestResultsDao ptwTestResultsDao;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseDto closeOutService(CloseOutReqDto closeOutReqDto) {
		logger.info("CloseOutService | closeOutService |closeOutReqDto" + closeOutReqDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		Integer permitNumber = 0;
		Boolean flag = false;
		try {
			if (closeOutReqDto.getPtwCloseOutDtoList() != null && !closeOutReqDto.getPtwCloseOutDtoList().isEmpty()) {
				permitNumber = closeOutReqDto.getPtwCloseOutDtoList().get(0).getPermitNumber();
				ptwHeaderDao.updatePtwHeader(closeOutReqDto.getPtwCloseOutDtoList().get(0), closeOutReqDto.getStatus());
				for(PtwCloseOut1Dto p : closeOutReqDto.getPtwCloseOutDtoList()){
					ptwCloseOutDao.insertPtwCloseOut(p);
				}
				flag = true;
			}
			if(closeOutReqDto.getPtwTestResultsDtoList() != null && ! closeOutReqDto.getPtwTestResultsDtoList().isEmpty()){
				permitNumber = closeOutReqDto.getPtwTestResultsDtoList().get(0).getPermitNumber();
				ptwTestResultsDao.deletePtwTestResults(permitNumber.toString());
				for(PtwTestResultsDto p : closeOutReqDto.getPtwTestResultsDtoList()){
					ptwTestResultsDao.insertPtwTestResults(p.getPermitNumber().toString(), p);
				}
				flag = true;
			}
			if(flag){
				responseDto.setData("Permit Number " + permitNumber + " closed");
				responseDto.setMessage("Success");
			}else{
				responseDto.setData("Permit failed to close");
				responseDto.setMessage("Failure");
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CloseOutService | closeOutService " + e.getMessage());
			logger.error(e.getStackTrace().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());
			responseDto.setData("Permit failed to close");
		}
		logger.info("CloseOutService | closeOutService" + responseDto);
		return responseDto;
	}
}
