package com.incture.ptw.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaReviewDto;
import com.incture.ptw.dto.PtwApprovalDto;
import com.incture.ptw.dto.PtwCloseOutDto;
import com.incture.ptw.dto.PtwCseWorkTypeDto;
import com.incture.ptw.dto.PtwCwpWorkTypeDto;
import com.incture.ptw.dto.PtwDetailsDto;
import com.incture.ptw.dto.PtwHeaderDto;
import com.incture.ptw.dto.PtwHwpWorkTypeDto;
import com.incture.ptw.dto.PtwPeopleDto;
import com.incture.ptw.dto.PtwRequiredDocumentDto;
import com.incture.ptw.dto.PtwTestRecordDto;
import com.incture.ptw.dto.PtwTestResultsDto;

@Repository("PermitDetailsDao")
public class PermitDetailsDao extends BaseDao {
	@Autowired
	private PtwHeaderDao ptwHeaderDao;
	
	@Autowired
	private JsaReviewDao jsaReviewDao;
	
	@Autowired
	private PtwRequiredDocumentDao ptwRequiredDocumentDao;
	
	@Autowired
	private PtwApprovalDao ptwApprovalDao;
	
	@Autowired
	private PtwTestRecordDao ptwTestRecordDao;
	
	@Autowired
	private PtwTestResultsDao ptwTestResultsDao;
	
	@Autowired
	private PtwCloseOutDao ptwCloseOutDao;
	
	@Autowired
	private PtwCwpWorkTypeDao ptwCwpWorkTypeDao;
	
	@Autowired
	private PtwHwpWorkTypeDao ptwHwpWorkTypeDao;
	
	@Autowired
	private PtwCseWorkTypeDao ptwCseWorkTypeDao;
	
	@Autowired
	private PtwPeopleDao ptwPeopleDao;
	
	
	

	public PtwDetailsDto getPermitDetails(String permitNumber, String permitType) {
		PtwDetailsDto ptwDetailsDto = new PtwDetailsDto();
		String ptwPermitNumber = null;
		String isCwp;
		String isHwp;
		String isCse;
		String pmNumber;
		try{
			
		
			if (permitType == "COLD") {
				ptwPermitNumber = "CWP" + permitNumber;
			}
			else if (permitType == "HOT") {
				ptwPermitNumber = "HWP" + permitNumber;
			}
			else if (permitType == "CSE") {
				ptwPermitNumber = "CSE" + permitNumber;
	
			}
			PtwHeaderDto ptwHeaderDto = ptwHeaderDao.getPermitNumber(ptwPermitNumber);
			pmNumber = ptwHeaderDto.getPermitNumber().toString();
			isCwp = ptwHeaderDto.getIsCwp().toString();
			isHwp = ptwHeaderDto.getIsHwp().toString();
			isCse = ptwHeaderDto.getIsCse().toString();
			
			JsaReviewDto jsaReviewDto = jsaReviewDao.getJsaReview(pmNumber);
			ptwDetailsDto.setJsaReviewDto(jsaReviewDto);
		
			List<PtwHeaderDto> ptwHeaderDtoList = ptwHeaderDao.getPtwHeader(ptwPermitNumber);
			ptwDetailsDto.setPtwHeaderDtoList(ptwHeaderDtoList);
			
			List<PtwRequiredDocumentDto> ptwRequiredDocumentDtoList = ptwRequiredDocumentDao.getPtwReqDoc(pmNumber,isCwp,isHwp,isCse);
			ptwDetailsDto.setPtwRequiredDocumentDtoList(ptwRequiredDocumentDtoList);
			
			List<PtwApprovalDto> ptwApprovalDtoList = ptwApprovalDao.getPtwApproval(pmNumber,isCwp,isHwp,isCse);
			ptwDetailsDto.setPtwApprovalDtoList(ptwApprovalDtoList);
			
			PtwTestRecordDto ptwTestRecordDto = ptwTestRecordDao.getPtwTestRec(pmNumber);
			ptwDetailsDto.setPtwTestRecordDto(ptwTestRecordDto);
			
			List<PtwTestResultsDto> ptwTestResultsDtoList= ptwTestResultsDao.getPtwTestRes(pmNumber);
			ptwDetailsDto.setPtwTestResultsDtoList(ptwTestResultsDtoList);
			
			List<PtwCloseOutDto> ptwCloseOutDtolist = ptwCloseOutDao.getPtwCloseOut(pmNumber);
			ptwDetailsDto.setPtwCloseOutDtolist(ptwCloseOutDtolist);
			
			PtwCwpWorkTypeDto ptwCwpWorkTypeDto = ptwCwpWorkTypeDao.getPtwCwpWork(pmNumber);
			ptwDetailsDto.setPtwCwpWorkTypeDto(ptwCwpWorkTypeDto);
			
			PtwHwpWorkTypeDto ptwHwpWorkTypeDto = ptwHwpWorkTypeDao.getPtwHwpWork(pmNumber);
			ptwDetailsDto.setPtwHwpWorkTypeDto(ptwHwpWorkTypeDto);
			
			PtwCseWorkTypeDto ptwCseWorkTypeDto = ptwCseWorkTypeDao.getPtwCseWork(pmNumber);
			ptwDetailsDto.setPtwCseWorkTypeDto(ptwCseWorkTypeDto);
			
			List<PtwPeopleDto> ptwPeopleDtoList = ptwPeopleDao.getPtwPeople(pmNumber);
			ptwDetailsDto.setPtwPeopleDtoList(ptwPeopleDtoList);
			
			return ptwDetailsDto;
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
			return null;
		}


}
