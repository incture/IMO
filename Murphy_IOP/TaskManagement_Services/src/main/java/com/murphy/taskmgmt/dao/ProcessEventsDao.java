package com.murphy.taskmgmt.dao;


import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.ProcessEventsDto;
import com.murphy.taskmgmt.entity.ProcessEventsDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("ProcessEventsDao")
@Transactional
public class ProcessEventsDao extends BaseDao<ProcessEventsDo, ProcessEventsDto> {

	private static final Logger logger = LoggerFactory.getLogger(ProcessEventsDao.class);

	public ProcessEventsDao() {
	}
	@Autowired
	private HierarchyDao locDao;

	@Override
	protected ProcessEventsDo importDto(ProcessEventsDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		ProcessEventsDo entity = new ProcessEventsDo();
		if (!ServicesUtil.isEmpty(fromDto.getProcessId()))
			entity.setProcessId(fromDto.getProcessId());
		if (!ServicesUtil.isEmpty(fromDto.getName()))
			entity.setName(fromDto.getName());
		if (!ServicesUtil.isEmpty(fromDto.getStartedBy()))
			entity.setStartedBy(fromDto.getStartedBy());
		if (!ServicesUtil.isEmpty(fromDto.getStatus()))
			entity.setStatus(fromDto.getStatus());
		if (!ServicesUtil.isEmpty(fromDto.getSubject()))
			entity.setSubject(fromDto.getSubject());
		if (!ServicesUtil.isEmpty(fromDto.getCompletedAt()))
			entity.setCompletedAt(fromDto.getCompletedAt());
		if (!ServicesUtil.isEmpty(fromDto.getStartedAt()))
			entity.setStartedAt(fromDto.getStartedAt());
		if (!ServicesUtil.isEmpty(fromDto.getRequestId()))
			entity.setRequestId(fromDto.getRequestId());
		if (!ServicesUtil.isEmpty(fromDto.getStartedByDisplayName()))
			entity.setStartedByDisplayName(fromDto.getStartedByDisplayName());
		if (!ServicesUtil.isEmpty(fromDto.getGroup()))
			entity.setGroup(fromDto.getGroup());
		if (!ServicesUtil.isEmpty(fromDto.getLocationCode()))
			entity.setLocationCode(fromDto.getLocationCode());
		if (!ServicesUtil.isEmpty(fromDto.getProcessType()))
			entity.setProcessType(fromDto.getProcessType());
		if(!ServicesUtil.isEmpty(fromDto.getExtraRole()))
			entity.setExtraRole(fromDto.getExtraRole());
		return entity;
	}

	@Override
	protected ProcessEventsDto exportDto(ProcessEventsDo entity) {
		ProcessEventsDto processEventsDto = new ProcessEventsDto();
		if (!ServicesUtil.isEmpty(entity.getProcessId()))
			processEventsDto.setProcessId(entity.getProcessId());
		if (!ServicesUtil.isEmpty(entity.getName()))
			processEventsDto.setName(entity.getName());
		if (!ServicesUtil.isEmpty(entity.getStartedBy()))
			processEventsDto.setStartedBy(entity.getStartedBy());
		if (!ServicesUtil.isEmpty(entity.getStatus()))
			processEventsDto.setStatus(entity.getStatus());
		if (!ServicesUtil.isEmpty(entity.getSubject()))
			processEventsDto.setSubject(entity.getSubject());
		if (!ServicesUtil.isEmpty(entity.getCompletedAt()))
			processEventsDto.setCompletedAt(entity.getCompletedAt());
		if (!ServicesUtil.isEmpty(entity.getStartedAt()))
			processEventsDto.setStartedAt(entity.getStartedAt());
		if (!ServicesUtil.isEmpty(entity.getRequestId()))
			processEventsDto.setRequestId(entity.getRequestId());
		if (!ServicesUtil.isEmpty(entity.getStartedByDisplayName()))
			processEventsDto.setStartedByDisplayName(entity.getStartedByDisplayName());
		if (!ServicesUtil.isEmpty(entity.getGroup()))
			processEventsDto.setGroup(entity.getGroup());
		if (!ServicesUtil.isEmpty(entity.getLocationCode()))
			processEventsDto.setLocationCode(entity.getLocationCode());
		if (!ServicesUtil.isEmpty(entity.getProcessType()))
			processEventsDto.setProcessType(entity.getProcessType());
		if (!ServicesUtil.isEmpty(entity.getExtraRole()))
			processEventsDto.setExtraRole(entity.getExtraRole());
		return processEventsDto;
	}


	/*	@SuppressWarnings("unchecked")
	public List<String> getAllProcessName() throws NoResultFault {
		Query query = this.getEntityManager().createQuery("select DISTINCT p.name from ProcessEventsDo p");
		List<String> processNameList = (List<String>) query.getResultList();
		if (ServicesUtil.isEmpty(processNameList)) {
			throw new NoResultFault("NO RECORD FOUND");
		}
		return processNameList;
	}*/


	public String createProcessInstance(ProcessEventsDto dto) {

		//	  logger.error("[Murphy][ProcessEventsDao][createProcessInstance]initiated with " + dto);


		//	  logger.error("[Murphy][ProcessEventsDao][createProcessInstance]initiated with " + dto);
		try {
			create(dto);
			return MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][ProcessEventsDao][createProcessInstance][error] " + e.getMessage());
		}
		return MurphyConstant.FAILURE;
	}

	public String updateProcessInstance(ProcessEventsDto dto) {
		//	logger.error("[Murphy][ProcessEventsDao][updateProcessInstance]initiated with " + dto);

		try {
			update(dto);
			return MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][ProcessEventsDao][updateProcessInstance][error] " + e.getMessage());
		}
		return MurphyConstant.FAILURE;

	}
	
	public String getMuwiByProcessId(String processId) {
		String query = "SELECT WM.MUWI FROM TM_PROC_EVNTS PE JOIN PRODUCTION_LOCATION PL ON PE.LOC_CODE = PL.LOCATION_CODE JOIN WELL_MUWI WM ON PL.LOCATION_CODE = WM.LOCATION_CODE WHERE PE.PROCESS_ID = '"+processId+"'";
		Object result = this.getSession().createSQLQuery(query).uniqueResult();
		if(!ServicesUtil.isEmpty(result)) {
			return (String) result;
		}
		return null;
	}

	public String getLocCodeByProcessId(String processId) {
		String query = "SELECT PE.LOC_CODE FROM TM_PROC_EVNTS PE  WHERE PE.PROCESS_ID = '"+processId+"'";
		Object result = this.getSession().createSQLQuery(query).uniqueResult();
		if(!ServicesUtil.isEmpty(result)) {
			return (String) result;
		}
		return null;
	}

	public String updateProcessStatusToComp(ProcessEventsDo entity) {
			logger.error("[Murphy][ProcessEventsDao][updateProcessInstance]initiated with " + entity);
		try {

			ProcessEventsDo updateDo = find(entity);
			updateDo.setStatus(entity.getStatus());
			if(MurphyConstant.COMPLETE.equals(updateDo.getStatus())){
				updateDo.setCompletedAt(new Date());
			}
			logger.error("[Murphy][ProcessEventsDao][updateProcessInstance] before merge " + updateDo);
			
			merge(updateDo);
			return MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][ProcessEventsDao][updateProcessInstance][error] " + e.getMessage());
		}
		return MurphyConstant.FAILURE;
	}
	
//UPDATE DB FOR AUTO CLOSE STATUS 	
public String updateProcessStatusToComplete(ProcessEventsDo processEventsDo) {
		
		logger.error("[Murphy][ProcessEventsDao][updateProcessInstance]initiated with " + processEventsDo);
		try {

			ProcessEventsDo updateDo = find(processEventsDo);
			updateDo.setStatus(processEventsDo.getStatus());
		
		String updateQuery = "update tm_proc_evnts set status='" + MurphyConstant.COMPLETE
				+ "' ,  COMPLETED_AT =  TO_TIMESTAMP('"
				+ ServicesUtil.convertFromZoneToZoneString(null, null, "", "", "", MurphyConstant.DATE_DB_FORMATE_SD)
				+ "', 'yyyy-mm-dd hh24:mi:ss')  where PROCESS_ID ='" + processEventsDo.getProcessId() + "'" ;				

		Query q = this.getSession().createSQLQuery(updateQuery);
		
		logger.error("[Murphy][TaskEventsDao][updateAutoChangeStatus][error]" +updateQuery);
		Integer result = q.executeUpdate();
		if (result > 0)
			return MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][ProcessEventsDao][updateProcessInstance][error] " + e.getMessage());
		}
		
			return MurphyConstant.NO_RECORD;
		
	}

//UPDATE DB FOR UPDATING STATUS FOR AUTO CLOSE
public String updateAutoChangeStatus(String taskId, String status) {
	String updateQuery = "update tm_task_evnts set status='" + MurphyConstant.COMPLETE
			+ "' ,UPDATED_BY= '" + MurphyConstant.SYSTEM
			+ "' , COMPLETED_AT =  TO_TIMESTAMP('"
			+ ServicesUtil.convertFromZoneToZoneString(null, null, "", "", "", MurphyConstant.DATE_DB_FORMATE_SD)
			+ "', 'yyyy-mm-dd hh24:mi:ss')  where task_id ='" + taskId + "' AND PARENT_ORIGIN IN ('" + MurphyConstant.ALARM +"','"+ MurphyConstant.CUSTOM + "')"  ;				

	Query q = this.getSession().createSQLQuery(updateQuery);
	logger.error("[Murphy][TaskEventsDao][updateAutoChangeStatus][error]" +updateQuery);
	Integer result = q.executeUpdate();
	logger.error("[Murphy][TaskEventsDao][updateAutoChangeStatus][result]" + result);
	if (result > 0)
		return MurphyConstant.SUCCESS;
	else
		return MurphyConstant.NO_RECORD;

}
  
//update user group according to location
    public String updateUserGrp (String task_id){
    	String loc_code = null, process_id = null;
    	//Fetch location code of the task
		String queryString = "SELECT pe.loc_code,pe.process_id FROM TM_PROC_EVNTS pe INNER JOIN TM_TASK_EVNTS te ON te.process_id = pe.process_id"
				+ " AND te.task_id = '" + task_id + "'";
		Query query = this.getSession().createSQLQuery(queryString);
		List<Object[]> response = (List<Object[]>) query.list();
		if (!ServicesUtil.isEmpty(response)) {
			for (Object[] obj : response) {
				loc_code  = ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0];
				process_id = ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1];
			}
		}
		// Fetch field(location text) from location code to check role(ROC)
		String field = locDao.getLocationByLocCode(loc_code.substring(0, 15));

		if ("Tilden Central".equalsIgnoreCase(field.trim()) || "Tilden North".equalsIgnoreCase(field.trim())
				|| "Tilden East".equalsIgnoreCase(field.trim()))
			field = "CentralTilden";
		else if ("Tilden West".equalsIgnoreCase(field.trim()))
			field = "WestTilden";
		else if ("Karnes North".equalsIgnoreCase(field.trim()) || "Karnes South".equalsIgnoreCase(field.trim()))
			field = "Karnes";
		logger.error("[Murphy][returnAllStatus][field]" + field);
		String group = "IOP_TM_ROC_" + field.trim();
		
		// Update user group
		String queryGroup = "UPDATE TM_PROC_EVNTS SET USER_GROUP = '" +group + "' WHERE PROCESS_ID = '"+process_id + "'" ;
		Query q = this.getSession().createSQLQuery(queryGroup);
		Integer result = (Integer) q.executeUpdate();
			
		return group;
	  
    } 
	
	
}
