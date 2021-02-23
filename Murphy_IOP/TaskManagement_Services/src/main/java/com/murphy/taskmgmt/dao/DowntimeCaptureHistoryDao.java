package com.murphy.taskmgmt.dao;

import java.math.BigInteger;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.DowntimeCaptureHistoryDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.DowntimeCaptureHistoryDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("downtimeCaptureHistoryDao")
public class DowntimeCaptureHistoryDao extends BaseDao<DowntimeCaptureHistoryDo, DowntimeCaptureHistoryDto> {

	private static final Logger logger = LoggerFactory.getLogger(DowntimeCaptureHistoryDao.class);

	@Override
	protected DowntimeCaptureHistoryDo importDto(DowntimeCaptureHistoryDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		DowntimeCaptureHistoryDo historyDo = new DowntimeCaptureHistoryDo();
//		historyDo.setDownrtimeHistoryDoPK(new DowntimeCaptureHistoryDoPK());
		if (!ServicesUtil.isEmpty(fromDto.getLongDescription()))
			historyDo.setLongDescription(fromDto.getLongDescription());
		if (!ServicesUtil.isEmpty(fromDto.getAlarmCondition()))
			historyDo.setAlarmCondition(fromDto.getAlarmCondition());
		if (!ServicesUtil.isEmpty(fromDto.getDownTimeClassifier()))
			historyDo.setDownTimeClassifier(fromDto.getDownTimeClassifier());
		if (!ServicesUtil.isEmpty(fromDto.getPointId()))
			historyDo.setPointId(fromDto.getPointId());
		return historyDo;
	}

	@Override
	protected DowntimeCaptureHistoryDto exportDto(DowntimeCaptureHistoryDo entity) {
		DowntimeCaptureHistoryDto downtimeCaptureHistoryDto = new DowntimeCaptureHistoryDto();
		if (!ServicesUtil.isEmpty(entity.getAlarmCondition()))
			downtimeCaptureHistoryDto.setAlarmCondition(entity.getAlarmCondition());
		if (!ServicesUtil.isEmpty(entity.getDthId()))
			downtimeCaptureHistoryDto.setDthId(entity.getDthId());
		if (!ServicesUtil.isEmpty(entity.getDownTimeClassifier()))
			downtimeCaptureHistoryDto.setDownTimeClassifier(entity.getDownTimeClassifier());
		if (!ServicesUtil.isEmpty(entity.getLongDescription()))
			downtimeCaptureHistoryDto.setLongDescription(entity.getLongDescription());
		if (!ServicesUtil.isEmpty(entity.getPointId()))
			downtimeCaptureHistoryDto.setPointId(entity.getPointId());
		return downtimeCaptureHistoryDto;
	}

	@Transactional(value=TxType.REQUIRES_NEW)
	public ResponseMessage insertIntoHistory(DowntimeCaptureHistoryDto captureHistoryDto) {

		Session session = this.getSession();
		ResponseMessage responseMessage = new ResponseMessage();

		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setMessage(MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
//		logger.error("INFO : [insertIntoHistory][captureHistoryDto] : " + captureHistoryDto);

		String queryForRecord = "SELECT COUNT(*) FROM TM_DOWNTIME_CAPTURE_HISTORY WHERE LONG_DESCRIPTION = '"
				+ captureHistoryDto.getLongDescription() + "' AND ALARM_CONDITION "
						+ "= '" + captureHistoryDto.getLongDescription() +  " | " + captureHistoryDto.getAlarmCondition() +"'";
		Integer recordPresent = ((BigInteger) session.createSQLQuery(queryForRecord).uniqueResult()).intValue();
		if (recordPresent == 0) {
			try {
				captureHistoryDto.setDownTimeClassifier(MurphyConstant.ALARM_NON_DOWNTIME);
				captureHistoryDto.setAlarmCondition(captureHistoryDto.getLongDescription() + " | "+captureHistoryDto.getAlarmCondition());
				session.save(importDto(captureHistoryDto));
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setMessage(MurphyConstant.CREATED_SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			} catch (InvalidInputFault e) {
				logger.error("ERROR : [insertIntoHistory][InvalidInputFault][inserOrupdate] : " + e.getMessage());
			} catch (ExecutionFault e) {
				logger.error("ERROR : [insertIntoHistory][ExecutionFault][inserOrupdate] : " + e.getMessage());
			} catch (NoResultFault e) {
				logger.error("ERROR : [insertIntoHistory][NoResultFault][inserOrupdate] : " + e.getMessage());
			}
		}

		return responseMessage;
	}

	public ResponseMessage updateDowntimeHistory(DowntimeCaptureHistoryDto captureHistoryDto) {

		Session session = this.getSession();
		ResponseMessage responseMessage = new ResponseMessage();

		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		logger.error("INFO : [updateDowntimeHistory][captureHistoryDto]" + captureHistoryDto);

//		session.update(importDto(captureHistoryDto));

		String update  = "UPDATE TM_DOWNTIME_CAPTURE_HISTORY SET DOWNTIME_CLASSIFIER = '"+captureHistoryDto.getDownTimeClassifier()+"' WHERE LONG_DESCRIPTION = '"+captureHistoryDto.getLongDescription()+"' AND ALARM_CONDITION = '"+captureHistoryDto.getLongDescription() + " | "+ captureHistoryDto.getAlarmCondition() +"'";
		logger.error("updateQuery"+update);
		int upd = session.createSQLQuery(update).executeUpdate();
		if(upd == 1) {
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setMessage(MurphyConstant.UPDATE_SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}

		return responseMessage;
	}

	public ResponseMessage updateCanadaDowntimeHistory(DowntimeCaptureHistoryDto captureHistoryDto) {
		Session session = this.getSession();
		ResponseMessage responseMessage = new ResponseMessage();

		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		logger.error("INFO : [updateCanadaDowntimeHistory][captureHistoryDto]" + captureHistoryDto);

		String update  = "UPDATE TM_DOWNTIME_CAPTURE_HISTORY SET DOWNTIME_CLASSIFIER = '"+captureHistoryDto.getDownTimeClassifier()+"' WHERE LONG_DESCRIPTION = '"+captureHistoryDto.getLongDescription()+"' AND ALARM_CONDITION = '"+captureHistoryDto.getLongDescription() + " | "+ captureHistoryDto.getAlarmCondition() +"'";
		logger.error("updateQuery"+update);
		int upd = session.createSQLQuery(update).executeUpdate();
		if(upd == 1) {
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setMessage(MurphyConstant.UPDATE_SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}

		return responseMessage;
	}
	
	/*public ResponseMessage updateDowntimeHistoryClassifier(DowntimeCaptureHistoryDto captureHistoryDto) {
		
		Session session = this.getSession();
		ResponseMessage responseMessage = new ResponseMessage();

		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		logger.error("INFO : [updateDowntimeHistory][updateDowntimeClassifier]" + captureHistoryDto);
		
		if ((!ServicesUtil.isEmpty(captureHistoryDto.getLongDescription())
				&& (!ServicesUtil.isEmpty(captureHistoryDto.getAlarmCondition()))
				&& (!ServicesUtil.isEmpty(captureHistoryDto.getDownTimeClassifier())))) {
			
			try {
				session.update(importDto(captureHistoryDto));
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setMessage(MurphyConstant.UPDATE_SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			} catch (InvalidInputFault e) {
				logger.error("ERROR : [updateDowntimeHistory][updateDowntimeClassifier][InvalidInputFault][inserOrupdate] : " + e.getMessage());
			} catch (ExecutionFault e) {
				logger.error("ERROR : [updateDowntimeHistory][updateDowntimeClassifier][ExecutionFault][inserOrupdate] : " + e.getMessage());
			} catch (NoResultFault e) {
				logger.error("ERROR : [updateDowntimeHistory][updateDowntimeClassifier][NoResultFault][inserOrupdate] : " + e.getMessage());
			}
			
		}

		return responseMessage;
	}*/

}
