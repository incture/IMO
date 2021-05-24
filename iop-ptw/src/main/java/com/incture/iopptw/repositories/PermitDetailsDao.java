package com.incture.iopptw.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaReviewDto;
import com.incture.iopptw.dtos.PtwApprovalDto;
import com.incture.iopptw.dtos.PtwCloseOutDto;
import com.incture.iopptw.dtos.PtwCseWorkTypeDto;
import com.incture.iopptw.dtos.PtwCwpWorkTypeDto;
import com.incture.iopptw.dtos.PtwDetailsDto;
import com.incture.iopptw.dtos.PtwHeaderDto;
import com.incture.iopptw.dtos.PtwHwpWorkTypeDto;
import com.incture.iopptw.dtos.PtwPeopleDto;
import com.incture.iopptw.dtos.PtwRequiredDocumentDto;
import com.incture.iopptw.dtos.PtwTestRecordDto;
import com.incture.iopptw.dtos.PtwTestResultsDto;

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
		try {

			if (permitType.equals("COLD")) {
				ptwPermitNumber = "CWP" + permitNumber;
			} else if (permitType.equals("HOT")){
				ptwPermitNumber = "HWP" + permitNumber;
			} else if (permitType.equals("CSE")) {
				ptwPermitNumber = "CSE" + permitNumber;

			}
			PtwHeaderDto ptwHeaderDto = ptwHeaderDao.getPermitNumber(ptwPermitNumber);
			pmNumber = ptwHeaderDto.getPermitNumber().toString();
			isCwp = ptwHeaderDto.getIsCWP().toString();
			isHwp = ptwHeaderDto.getIsHWP().toString();
			isCse = ptwHeaderDto.getIsCSE().toString();

			JsaReviewDto jsaReviewDto = jsaReviewDao.getJsaReview(pmNumber);
			ptwDetailsDto.setJsaReviewDto(jsaReviewDto);

			List<PtwHeaderDto> ptwHeaderDtoList = ptwHeaderDao.getPtwHeader(ptwPermitNumber);
			ptwDetailsDto.setPtwHeaderDtoList(ptwHeaderDtoList);

			List<PtwRequiredDocumentDto> ptwRequiredDocumentDtoList = ptwRequiredDocumentDao.getPtwReqDoc(pmNumber,
					isCwp, isHwp, isCse);
			ptwDetailsDto.setPtwRequiredDocumentDtoList(ptwRequiredDocumentDtoList);

			List<PtwApprovalDto> ptwApprovalDtoList = ptwApprovalDao.getPtwApproval(pmNumber, isCwp, isHwp, isCse);
			ptwDetailsDto.setPtwApprovalDtoList(ptwApprovalDtoList);

			PtwTestRecordDto ptwTestRecordDto = ptwTestRecordDao.getPtwTestRec(pmNumber);
			ptwDetailsDto.setPtwTestRecordDto(ptwTestRecordDto);

			List<PtwTestResultsDto> ptwTestResultsDtoList = ptwTestResultsDao.getPtwTestRes(pmNumber);
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
