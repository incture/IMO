package com.incture.ptw.services;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.ptw.dao.PtwApprovalDao;
import com.incture.ptw.dao.PtwCloseOutDao;
import com.incture.ptw.dao.PtwCseWorkTypeDao;
import com.incture.ptw.dao.PtwCwpWorkTypeDao;
import com.incture.ptw.dao.PtwHeaderDao;
import com.incture.ptw.dao.PtwHwpWorkTypeDao;
import com.incture.ptw.dao.PtwPeopleDao;
import com.incture.ptw.dao.PtwRequiredDocumentDao;
import com.incture.ptw.dao.PtwTestRecordDao;
import com.incture.ptw.dao.PtwTestResultsDao;
import com.incture.ptw.dto.PtwApprovalDto;
import com.incture.ptw.dto.PtwPeopleDto;
import com.incture.ptw.dto.PtwRequiredDocumentDto;
import com.incture.ptw.dto.UpdatePermitRequestDto;
import com.incture.ptw.util.ResponseDto;

@Service
@Transactional
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
						&& !updatePermitRequestDto.getPtwHeaderDtoList().isEmpty()) {
					for(int i = 0; i < updatePermitRequestDto.getPtwHeaderDtoList().size(); i++){
						ptwHeaderDao.updatePtwHeader(updatePermitRequestDto.getPtwHeaderDtoList().get(i));
					}
					
				}
				
				if(!updatePermitRequestDto.getPtwPeopleDtoList().isEmpty()
                        && updatePermitRequestDto.getPtwPeopleDtoList() != null){
					ptwPeopleDao.deletePtwPeople(taskPermitNum.toString());
					for (PtwPeopleDto p : updatePermitRequestDto.getPtwPeopleDtoList()) {
                        ptwPeopleDao.insertPtwPeople(taskPermitNum.toString(), p);
                    }
				}
				
				if(!updatePermitRequestDto.getPtwRequiredDocumentDtoList().isEmpty()
                        && updatePermitRequestDto.getPtwRequiredDocumentDtoList() != null){
					 for (PtwRequiredDocumentDto p : updatePermitRequestDto.getPtwRequiredDocumentDtoList())
	                        ptwRequiredDocumentDao.updatePtwRequiredDocument(taskPermitNum.toString(), p);
				}
				if (!updatePermitRequestDto.getPtwApprovalDtoList().isEmpty()
                        && updatePermitRequestDto.getPtwApprovalDtoList() != null) {
                    for (PtwApprovalDto p : updatePermitRequestDto.getPtwApprovalDtoList())
                        ptwApprovalDao.updatePtwApproval(taskPermitNum.toString(), p);
                }
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UpdatePermitService || updatePermitDetails " + e.getMessage());
			logger.error(e.getStackTrace().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setData(new ArrayList<>());
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());
		}
		logger.info("UpdatePermitService || updatePermitDetails " + responseDto);

		return responseDto;
	}

}
