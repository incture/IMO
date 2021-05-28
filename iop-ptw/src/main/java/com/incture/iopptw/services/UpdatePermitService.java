package com.incture.iopptw.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.dtos.PtwApprovalDto;
import com.incture.iopptw.dtos.PtwCloseOutDto;
import com.incture.iopptw.dtos.PtwPeopleDto;
import com.incture.iopptw.dtos.PtwRequiredDocumentDto;
import com.incture.iopptw.dtos.PtwTestResultsDto;
import com.incture.iopptw.dtos.UpdatePermitRequestDto;
import com.incture.iopptw.repositories.PtwApprovalDao;
import com.incture.iopptw.repositories.PtwCloseOutDao;
import com.incture.iopptw.repositories.PtwCseWorkTypeDao;
import com.incture.iopptw.repositories.PtwCwpWorkTypeDao;
import com.incture.iopptw.repositories.PtwHeaderDao;
import com.incture.iopptw.repositories.PtwHwpWorkTypeDao;
import com.incture.iopptw.repositories.PtwPeopleDao;
import com.incture.iopptw.repositories.PtwRequiredDocumentDao;
import com.incture.iopptw.repositories.PtwTestRecordDao;
import com.incture.iopptw.repositories.PtwTestResultsDao;
import com.incture.iopptw.utils.ResponseDto;

@Service
public class UpdatePermitService {

	@Autowired
	private PtwHeaderDao ptwHeaderDao;

	@Autowired
	private PtwPeopleDao ptwPeopleDao;

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

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseDto updatePermitDetails(UpdatePermitRequestDto updatePermitRequestDto) {
		logger.info("UpdatePermitService || updatePermitDetails | UpdatePermitRequestDto" + updatePermitRequestDto);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			Boolean ptwConditionCheck = false, isCwpFlag = false, isHwpFlag = false, isCseFlag = false;
			Integer taskPermitNum;
			if (updatePermitRequestDto.getPtwHeaderDtoList() != null
					|| !updatePermitRequestDto.getPtwHeaderDtoList().isEmpty()) {
				for (int i = 0; i < updatePermitRequestDto.getPtwHeaderDtoList().size(); i++) {
					if (updatePermitRequestDto.getPtwHeaderDtoList().get(i).getIsCWP() == 1) {
						ptwConditionCheck = true;
						isCwpFlag = true;
					}
					if (updatePermitRequestDto.getPtwHeaderDtoList().get(i).getIsHWP() == 1) {
						ptwConditionCheck = true;
						isHwpFlag = true;
					}
					if (updatePermitRequestDto.getPtwHeaderDtoList().get(i).getIsCSE() == 1) {
						ptwConditionCheck = true;
						isCseFlag = true;
					}
				}
			}

			if (ptwConditionCheck) {
				taskPermitNum = updatePermitRequestDto.getPtwHeaderDtoList().get(0).getPermitNumber();
				if (updatePermitRequestDto.getPtwHeaderDtoList() != null
						|| !updatePermitRequestDto.getPtwHeaderDtoList().isEmpty()) {
					for (int i = 0; i < updatePermitRequestDto.getPtwHeaderDtoList().size(); i++) {
						ptwHeaderDao.updatePtwHeader(updatePermitRequestDto.getPtwHeaderDtoList().get(i));
					}

				}

				if (!updatePermitRequestDto.getPtwPeopleDtoList().isEmpty()
						|| updatePermitRequestDto.getPtwPeopleDtoList() != null) {
					ptwPeopleDao.deletePtwPeople(taskPermitNum.toString());
					for (PtwPeopleDto p : updatePermitRequestDto.getPtwPeopleDtoList()) {
						ptwPeopleDao.insertPtwPeople(taskPermitNum.toString(), p);
					}
				}

				if (!updatePermitRequestDto.getPtwRequiredDocumentDtoList().isEmpty()
						|| updatePermitRequestDto.getPtwRequiredDocumentDtoList() != null) {
					for (PtwRequiredDocumentDto p : updatePermitRequestDto.getPtwRequiredDocumentDtoList())
						ptwRequiredDocumentDao.updatePtwRequiredDocument(taskPermitNum.toString(), p);
				}
				if (!updatePermitRequestDto.getPtwApprovalDtoList().isEmpty()
						|| updatePermitRequestDto.getPtwApprovalDtoList() != null) {
					for (PtwApprovalDto p : updatePermitRequestDto.getPtwApprovalDtoList())
						ptwApprovalDao.updatePtwApproval(taskPermitNum.toString(), p);
				}
				if (updatePermitRequestDto.getPtwTestRecordDto() != null) {
					ptwTestRecordDao.updatePtwTestRecord(updatePermitRequestDto.getPtwTestRecordDto());
				}
				if (!updatePermitRequestDto.getPtwTestResultsDtoList().isEmpty()
						|| updatePermitRequestDto.getPtwTestResultsDtoList() != null) {
					ptwTestResultsDao.deletePtwTestResults(taskPermitNum.toString());
					for (PtwTestResultsDto p : updatePermitRequestDto.getPtwTestResultsDtoList()) {
						ptwTestResultsDao.insertPtwTestResults(taskPermitNum.toString(), p);
					}
				}
				if (!updatePermitRequestDto.getPtwCloseOutDtoList().isEmpty()
						|| updatePermitRequestDto.getPtwCloseOutDtoList() != null) {
					for (PtwCloseOutDto p : updatePermitRequestDto.getPtwCloseOutDtoList()) {
						ptwCloseOutDao.updatePtwCloseOut(p);
					}
				}
				if (updatePermitRequestDto.getPtwCwpWorkTypeDto() != null && isCwpFlag == true) {
					ptwCwpWorkTypeDao.updatePtwCwpWorkType(updatePermitRequestDto.getPtwCwpWorkTypeDto());
				}
				if (updatePermitRequestDto.getPtwHwpWorkTypeDto() != null && isHwpFlag == true) {
					ptwHwpWorkTypeDao.updatePtwHwpWorkType(updatePermitRequestDto.getPtwHwpWorkTypeDto());
				}
				if (updatePermitRequestDto.getPtwCseWorkTypeDto() != null && isCseFlag == true) {
					ptwCseWorkTypeDao.updatePtwCseWorkType(updatePermitRequestDto.getPtwCseWorkTypeDto());
				}
				responseDto.setData("Permit " + taskPermitNum + " updated succesfully.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UpdatePermitService || updatePermitDetails " + e.getMessage());
			logger.error(e.getStackTrace().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setData("Permit failed to update.");
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());
		}
		logger.info("UpdatePermitService || updatePermitDetails " + responseDto);

		return responseDto;
	}

}
