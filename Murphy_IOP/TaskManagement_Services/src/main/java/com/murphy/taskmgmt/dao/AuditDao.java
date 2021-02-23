package com.murphy.taskmgmt.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.murphy.taskmgmt.dto.AuditDto;
import com.murphy.taskmgmt.dto.AuditReportDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.AuditDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("AuditDao")
//@Transactional
public class AuditDao extends BaseDao<AuditDo, AuditDto> {

	private static final Logger logger = LoggerFactory.getLogger(AuditDao.class);

	public AuditDao() {
	}

	@Autowired
	private NotificationDao notificationDao;
	
	@Override
	protected AuditDo importDto(AuditDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {

		AuditDo entity = new AuditDo();
		if (!ServicesUtil.isEmpty(fromDto.getAuditId()))
			entity.setAuditId(fromDto.getAuditId());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			entity.setCreatedAt(fromDto.getCreatedAt());
		if (!ServicesUtil.isEmpty(fromDto.getTaskId()))
			entity.setTaskId(fromDto.getTaskId());
		if (!ServicesUtil.isEmpty(fromDto.getAction()))
			entity.setAction(fromDto.getAction());
		if (!ServicesUtil.isEmpty(fromDto.getActionBy()))
			entity.setActionBy(fromDto.getActionBy());
		return entity;
	}

	@Override
	protected AuditDto exportDto(AuditDo entity) {

		AuditDto dto = new AuditDto();
		if (!ServicesUtil.isEmpty(entity.getAuditId()))
			dto.setAuditId(entity.getAuditId());
		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			dto.setCreatedAt(entity.getCreatedAt());
		if (!ServicesUtil.isEmpty(entity.getTaskId()))
			dto.setTaskId(entity.getTaskId());
		if (!ServicesUtil.isEmpty(entity.getAction()))
			dto.setAction(entity.getAction());
		if (!ServicesUtil.isEmpty(entity.getActionBy()))
			dto.setActionBy(entity.getActionBy());
		return dto;
	}


	@Transactional
	public ResponseMessage createInstance(AuditDto dto){

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			dto.setCreatedAt(new Date());
			create(dto);
			responseMessage.setMessage(MurphyConstant.CREATED_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][AuditDao][createInstance][error]"+e.getMessage());
		}

		return responseMessage;

	}

	//@Transactional(value=TxType.REQUIRES_NEW)
	@Transactional(value=TxType.REQUIRES_NEW)
	public ResponseMessage createInstance(String action ,String actionBy ,String taskId){

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			AuditDto dto = new AuditDto();
			dto.setAction(action);
			dto.setActionBy(actionBy);
			dto.setTaskId(taskId);
			dto.setCreatedAt(new Date());
			create(dto);
			notificationDao.createAlertForStatusChange(taskId,MurphyConstant.TASK,action);
			responseMessage.setMessage(MurphyConstant.CREATED_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][AuditDao][createInstanceWithParams][error]"+e.getMessage());
		}

		return responseMessage;

	}
	
	
	/*
	public ResponseMessage createInstanceForInqAndDispatch(String action ,String actionBy ,String taskId){

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			Session session = this.getSession();
			Transaction tx = session.beginTransaction();
			String auditId=UUID.randomUUID().toString().replaceAll("-", "");
			Query query= session.createSQLQuery("insert into iop_mcq.TM_AUDIT_TRAIL values( :AUDIT_ID , :ACTION , :ACTION_BY , :CREATED_AT , :TASK_ID)");
			query.setParameter("AUDIT_ID", auditId);
			query.setParameter("ACTION", action);
			query.setParameter("ACTION_BY", actionBy);
			query.setParameter("CREATED_AT", new Date());
			query.setParameter("TASK_ID", taskId);
			query.executeUpdate();
			session.flush();
			tx.commit();
			
			
			notificationDao.createAlertForStatusChange(taskId,MurphyConstant.TASK,action);
			responseMessage.setMessage(MurphyConstant.CREATED_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][AuditDao][createInstanceWithParams][error]"+e.getMessage());
		}

		return responseMessage;

	}*/

	@SuppressWarnings("unchecked")
	@Transactional
	public List<AuditReportDto> getReport(){

		List<AuditReportDto> responseDto = new ArrayList<AuditReportDto>();
		//		List<AuditReportDto> responseList = new ArrayList<AuditReportDto>();
		try{
			//			String queryString = "SELECT  pe.REQUEST_ID AS REQUEST_ID, te.origin AS ORIGIN,te.TSK_SUBJECT AS SUBJECT,"
			//					+ " ( SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i  WHERE ATTR_TEMP_ID in ('123','NDV1')  AND i.TASK_ID in(te.TASK_ID,pe.process_id) ) AS LOC,"
			//					+ " ( SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i   WHERE ATTR_TEMP_ID in ('12345','NDO5')  AND i.TASK_ID in(te.TASK_ID,pe.process_id)) AS CLASSIFICATION, "
			//					+ " ( SELECT i.INS_VALUE  FROM TM_ATTR_INSTS AS i WHERE ATTR_TEMP_ID in ('123456','NDO6')  AND i.TASK_ID in(te.TASK_ID,pe.process_id)) AS SUB_CLASSIFICATION, "
			//					+ " pe.STARTED_BY AS CREATED_BY,"
			//					+ " ( SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i  WHERE ATTR_TEMP_ID in ('1234','NDO3') AND te.TASK_ID = i.TASK_ID ) AS ASSIGN_TO, "
			//					+ " te.CREATED_AT AS CREATED_AT,"
			//					+ " (  SELECT max(i.CREATED_AT) FROM TM_AUDIT_TRAIL AS i  WHERE ACTION = 'IN PROGRESS'  AND te.TASK_ID = i.TASK_ID) AS ACKNOWLEDGED_AT,"
			//					+ " (SELECT max(i.CREATED_AT) FROM TM_AUDIT_TRAIL AS i  WHERE ACTION = 'RESOLVED' AND te.TASK_ID = i.TASK_ID) AS RESOLVED_AT,"
			//					+ " te.COMPLETED_AT AS COMPLETED_AT, te.STATUS AS STATUS "
			//					+ " FROM TM_TASK_EVNTS AS te  LEFT OUTER JOIN TM_PROC_EVNTS AS pe ON te.PROCESS_ID = pe.PROCESS_ID  "


			String queryString =	"SELECT  pe.REQUEST_ID AS REQUEST_ID, '' AS ORIGIN,te.TSK_SUBJECT AS SUBJECT,"
					+ " ( SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i  WHERE ATTR_TEMP_ID in ('123')  AND i.TASK_ID in(te.TASK_ID,pe.process_id) ) AS LOC,"
					+ " ( SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i   WHERE ATTR_TEMP_ID in ('12345')  AND i.TASK_ID in(te.TASK_ID,pe.process_id)) AS CLASSIFICATION, "
					+ " ( SELECT i.INS_VALUE  FROM TM_ATTR_INSTS AS i WHERE ATTR_TEMP_ID in ('123456')  AND i.TASK_ID in(te.TASK_ID,pe.process_id)) AS SUB_CLASSIFICATION, "
					+ " pe.STARTED_BY AS CREATED_BY,"
					+ " ( SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i  WHERE ATTR_TEMP_ID in ('1234') AND te.TASK_ID = i.TASK_ID ) AS ASSIGN_TO, "
					+ " te.CREATED_AT AS CREATED_AT,"
					+ " (  SELECT max(i.CREATED_AT) FROM TM_AUDIT_TRAIL AS i  WHERE ACTION = 'IN PROGRESS'  AND te.TASK_ID = i.TASK_ID) AS ACKNOWLEDGED_AT,"
					+ " (SELECT max(i.CREATED_AT) FROM TM_AUDIT_TRAIL AS i  WHERE ACTION = 'RESOLVED' AND te.TASK_ID = i.TASK_ID) AS RESOLVED_AT,"
					+ " te.COMPLETED_AT AS COMPLETED_AT, te.STATUS AS STATUS "
					+ " FROM TM_TASK_EVNTS AS te  LEFT OUTER JOIN TM_PROC_EVNTS AS pe ON te.PROCESS_ID = pe.PROCESS_ID  where te.origin <> 'Investigation'"

			+ " UNION "
			+ " SELECT pe.REQUEST_ID AS REQUEST_ID,'Non-dispatch' AS ORIGIN ,nd.DESCRIPTION AS SUBJECT ,nd.ND_LOC AS LOC,'' AS CLASSIFICATION,'' AS SUB_CLASSIFICATION,"
			+ " nd.CREATED_BY AS CREATED_BY,'' AS ASSIGN_TO, nd.CREATED_AT AS CREATED_AT,null AS ACKNOWLEDGED_AT,null AS RESOLVED_AT, nd.COMPLETED_AT AS COMPLETED_AT,map.STATUS AS STATUS "
			+ " FROM TM_ND_MAPPING AS map LEFT OUTER JOIN TM_NON_DISPTCH AS nd ON nd.TASK_ID = map.ND_TASK_ID"
			+ " LEFT OUTER JOIN TM_TASK_EVNTS AS te ON map.TASK_ID = te.TASK_ID"
			+ " LEFT OUTER JOIN TM_PROC_EVNTS AS pe ON te.PROCESS_ID = pe.PROCESS_ID order by 1,9";

			logger.error("[Murphy][getReport][query]"+queryString);
			Query q =  this.getSession().createSQLQuery(queryString.trim());
			List<Object[]> resultList = q.list();
			if(!ServicesUtil.isEmpty(resultList)){
				AuditReportDto dto = null;
				for(Object[] obj : resultList){
					dto = new AuditReportDto();
					dto.setRequestId(ServicesUtil.isEmpty(obj[0])?null:((BigInteger) obj[0]).longValue());
					dto.setNdTaskId(ServicesUtil.isEmpty(obj[1])?null:((String) obj[1]));
					dto.setDescription(ServicesUtil.isEmpty(obj[2])?null:((String) obj[2]));
					dto.setLocation(ServicesUtil.isEmpty(obj[3])?null:((String) obj[3]));
					dto.setClassification(ServicesUtil.isEmpty(obj[4])?null:((String) obj[4]));
					dto.setSubClassification(ServicesUtil.isEmpty(obj[5])?null:((String) obj[5]));
					dto.setCreatedBy(ServicesUtil.isEmpty(obj[6])?null:((String) obj[6]));
					dto.setAssignedTo(ServicesUtil.isEmpty(obj[7])?null:((String) obj[7]));
					dto.setCreatedAt(obj[8] == null ? null : ServicesUtil.convertFromZoneToZone(null, obj[8] ,MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
					dto.setCreatedAtDisplay(obj[8] == null ? null : ServicesUtil.convertFromZoneToZoneString(null, obj[8] ,MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
					dto.setAcknowledgedAt(obj[9] == null ? null : ServicesUtil.convertFromZoneToZone(null, obj[9] ,MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
					dto.setAcknowledgedAtDisplay(obj[9] == null ? null : ServicesUtil.convertFromZoneToZoneString(null, obj[9] ,MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
					dto.setResolvedAt(obj[10] == null ? null : ServicesUtil.convertFromZoneToZone(null, obj[10] ,MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
					dto.setResolvedAtDisplay(obj[10] == null ? null : ServicesUtil.convertFromZoneToZoneString(null, obj[10] ,MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
					dto.setCompletedAt(obj[11] == null ? null : ServicesUtil.convertFromZoneToZone(null, obj[11] ,MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
					dto.setCompletedAtDisplay(obj[11] == null ? null : ServicesUtil.convertFromZoneToZoneString(null, obj[11] ,MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
					dto.setStatus(ServicesUtil.isEmpty(obj[12])?null:(String) obj[12]);
					responseDto.add(dto);
				}
//				logger.error("[Murphy][TaskManagement][AudtiDao][getReport][responseDto]"+responseDto);
				return responseDto;
			}
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][AudtiDao][getReport][error]"+e.getMessage());
		}
		return null;
	}
	
	//Audit trail for a task - for replying to message to ROC
	@SuppressWarnings("unchecked")
	@Transactional
	public List<AuditDto> getReportByTask(String taskId) {
		List<AuditDto> responseDto = new ArrayList<AuditDto>();
		try {
			String queryString = "SELECT ACTION, USER_FIRST_NAME, USER_LAST_NAME, CREATED_AT, AUDIT_ID, TASK_ID FROM TM_AUDIT_TRAIL LEFT JOIN TM_USER_IDP_MAPPING ON ACTION_BY=USER_EMAIL WHERE TASK_ID='"
					+ taskId + "' ORDER BY CREATED_AT ASC";
			Query q = this.getSession().createSQLQuery(queryString.trim());
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				AuditDto dto = null;
				for (Object[] obj : resultList) {
					dto = new AuditDto();
					dto.setAction(ServicesUtil.isEmpty(obj[0])?null:((String) obj[0]));
					dto.setActionBy((ServicesUtil.isEmpty(obj[1])?"":((String) obj[1]))+" "+(ServicesUtil.isEmpty(obj[2])?"":((String) obj[2])));
					dto.setCreatedAt(obj[3] == null ? null : ServicesUtil.convertFromZoneToZone(null, obj[3] ,MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
					dto.setAuditId(ServicesUtil.isEmpty(obj[4])?null:((String) obj[4]));
					dto.setTaskId(ServicesUtil.isEmpty(obj[5])?null:((String) obj[5]));
					responseDto.add(dto);
				}
			}
			return responseDto;

		} catch (Exception e) {
			logger.error("[Murphy][TaskManagement][AudtiDao][getReportByTask][error]" + e.getMessage());
		}
		return null;
	}
}
