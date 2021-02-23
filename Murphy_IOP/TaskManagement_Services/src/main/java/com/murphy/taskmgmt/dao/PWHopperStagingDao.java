package com.murphy.taskmgmt.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.integration.dto.UIRequestDto;
import com.murphy.integration.interfaces.EnersightProveDailyLocal;
import com.murphy.integration.interfaces.EnersightProveMonthlyLocal;
import com.murphy.integration.service.EnersightProveDaily;
import com.murphy.integration.service.EnersightProveMonthly;
import com.murphy.taskmgmt.dto.CheckListDto;
import com.murphy.taskmgmt.dto.CheckListItemDto;
import com.murphy.taskmgmt.dto.PWHopperDto;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.entity.TaskEventsDo;
import com.murphy.taskmgmt.entity.TaskEventsDoPK;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("PWHopperDao")
@Transactional
public class PWHopperStagingDao extends BaseDao<TaskEventsDo, TaskEventsDto> {

	private static final Logger logger = LoggerFactory.getLogger(PWHopperStagingDao.class);

	public PWHopperStagingDao() {
	}

	EnersightProveDailyLocal enersightProveDailyLocal;

	@Autowired
	HierarchyDao locDao;

	@Override
	protected TaskEventsDto exportDto(TaskEventsDo entity) {
		TaskEventsDto taskEventsDto = new TaskEventsDto();
		taskEventsDto.setTaskId(entity.getTaskEventsDoPK().getTaskId());
		taskEventsDto.setProcessId(entity.getTaskEventsDoPK().getProcessId());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			taskEventsDto.setDescription(entity.getDescription());
		if (!ServicesUtil.isEmpty(entity.getName()))
			taskEventsDto.setName(entity.getName());
		if (!ServicesUtil.isEmpty(entity.getSubject()))
			taskEventsDto.setSubject(entity.getSubject());
		if (!ServicesUtil.isEmpty(entity.getStatus()))
			taskEventsDto.setStatus(entity.getStatus());
		if (!ServicesUtil.isEmpty(entity.getCurrentProcessor()))
			taskEventsDto.setCurrentProcessor(entity.getCurrentProcessor());
		// if (!ServicesUtil.isEmpty(entity.getPriority()))
		// taskEventsDto.setPriority(entity.getPriority());
		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			taskEventsDto.setCreatedAt(entity.getCreatedAt());
		if (!ServicesUtil.isEmpty(entity.getCompletedAt()))
			taskEventsDto.setCompletedAt(entity.getCompletedAt());
		if (!ServicesUtil.isEmpty(entity.getCompletionDeadLine()))
			taskEventsDto.setCompletionDeadLine(entity.getCompletionDeadLine());
		if (!ServicesUtil.isEmpty(entity.getProcessName()))
			taskEventsDto.setProcessName(entity.getProcessName());
		if (!ServicesUtil.isEmpty(entity.getTaskMode()))
			taskEventsDto.setTaskMode(entity.getTaskMode());
		if (!ServicesUtil.isEmpty(entity.getStatusFlag()))
			taskEventsDto.setStatusFlag(entity.getStatusFlag());
		if (!ServicesUtil.isEmpty(entity.getCurrentProcessorDisplayName()))
			taskEventsDto.setCurrentProcessorDisplayName(entity.getCurrentProcessorDisplayName());
		if (!ServicesUtil.isEmpty(entity.getTaskType()))
			taskEventsDto.setTaskType(entity.getTaskType());
		if (!ServicesUtil.isEmpty(entity.getForwardedBy()))
			taskEventsDto.setForwardedBy(entity.getForwardedBy());
		if (!ServicesUtil.isEmpty(entity.getForwardedAt()))
			taskEventsDto.setForwardedAt(entity.getForwardedAt());
		if (!ServicesUtil.isEmpty(entity.getOrigin()))
			taskEventsDto.setOrigin(entity.getOrigin());
		if (!ServicesUtil.isEmpty(entity.getUrl()))
			taskEventsDto.setDetailUrl(entity.getUrl());
		if (!ServicesUtil.isEmpty(entity.getUpdatedAt()))
			taskEventsDto.setUpdatedAt(entity.getUpdatedAt());
		if (!ServicesUtil.isEmpty(entity.getUpdatedBy()))
			taskEventsDto.setUpdatedBy(entity.getUpdatedBy());
		// if (!ServicesUtil.isEmpty(entity.getLocationCode()))
		// taskEventsDto.setLocationCode(entity.getLocationCode());

		return taskEventsDto;
	}

	@Override
	protected TaskEventsDo importDto(TaskEventsDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		TaskEventsDo entity = new TaskEventsDo();
		entity.setTaskEventsDoPK(new TaskEventsDoPK());
		if (!ServicesUtil.isEmpty(fromDto.getTaskId()))
			entity.getTaskEventsDoPK().setTaskId(fromDto.getTaskId());
		if (!ServicesUtil.isEmpty(fromDto.getProcessId()))
			entity.getTaskEventsDoPK().setProcessId(fromDto.getProcessId());
		if (!ServicesUtil.isEmpty(fromDto.getStatus()))
			entity.setStatus(fromDto.getStatus());
		if (!ServicesUtil.isEmpty(fromDto.getCurrentProcessor()))
			entity.setCurrentProcessor(fromDto.getCurrentProcessor());
		// if (!ServicesUtil.isEmpty(fromDto.getPriority()))
		// entity.setPriority(fromDto.getPriority());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			entity.setCreatedAt(fromDto.getCreatedAt());
		if (!ServicesUtil.isEmpty(fromDto.getCompletedAt()))
			entity.setCompletedAt(fromDto.getCompletedAt());
		if (!ServicesUtil.isEmpty(fromDto.getCompletionDeadLine()))
			entity.setCompletionDeadLine(fromDto.getCompletionDeadLine());
		if (!ServicesUtil.isEmpty(fromDto.getCurrentProcessorDisplayName()))
			entity.setCurrentProcessorDisplayName(fromDto.getCurrentProcessorDisplayName());
		if (!ServicesUtil.isEmpty(fromDto.getDescription()))
			entity.setDescription(fromDto.getDescription());
		if (!ServicesUtil.isEmpty(fromDto.getName()))
			entity.setName(fromDto.getName());
		if (!ServicesUtil.isEmpty(fromDto.getSubject()))
			entity.setSubject(fromDto.getSubject());
		if (!ServicesUtil.isEmpty(fromDto.getProcessName()))
			entity.setProcessName(fromDto.getProcessName());
		if (!ServicesUtil.isEmpty(fromDto.getTaskMode()))
			entity.setTaskMode(fromDto.getTaskMode());
		if (!ServicesUtil.isEmpty(fromDto.getStatusFlag()))
			entity.setStatusFlag(fromDto.getStatusFlag());
		if (!ServicesUtil.isEmpty(fromDto.getTaskType()))
			entity.setTaskType(fromDto.getTaskType());
		if (!ServicesUtil.isEmpty(fromDto.getForwardedBy()))
			entity.setForwardedBy(fromDto.getForwardedBy());
		if (!ServicesUtil.isEmpty(fromDto.getForwardedAt()))
			entity.setForwardedAt(fromDto.getForwardedAt());
		if (!ServicesUtil.isEmpty(fromDto.getOrigin()))
			entity.setOrigin(fromDto.getOrigin());
		if (!ServicesUtil.isEmpty(fromDto.getDetailUrl()))
			entity.setUrl(fromDto.getDetailUrl());
		if (!ServicesUtil.isEmpty(fromDto.getUpdatedAt()))
			entity.setUpdatedAt(fromDto.getUpdatedAt());
		if (!ServicesUtil.isEmpty(fromDto.getUpdatedBy()))
			entity.setUpdatedBy(fromDto.getUpdatedBy());
		// if (!ServicesUtil.isEmpty(fromDto.getLocationCode()))
		// entity.setLocationCode(fromDto.getLocationCode());

		return entity;
	}

	@SuppressWarnings("unchecked")
	public String getFLSOPforSubClassification(String classification, String subClassification) {
		String responseString = "";
		try {
			String queryString = "select flsop from tm_flsop where CLASSIFICATION = '" + classification
					+ "' and SUB_CLASSIFICATION = '" + subClassification + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				responseString = response.get(0);
			}
		} catch (Exception e) {
			System.err.println("[Murphy][TaskEventsDao][getFLSOPforSubClassification][error]" + e.getMessage());
		}
		return responseString;
	}

	@SuppressWarnings("unchecked")
	public List<CheckListDto> getCheckList(String userTypeInp, String locationCode, String investigationId,
			Boolean isPro) {
		List<CheckListDto> responseDto = null;
		if (!ServicesUtil.isEmpty(userTypeInp) && !ServicesUtil.isEmpty(locationCode)) {
			// TODO Auto-generated method stub
			boolean isTresholdCrossed = isTresholdCrossed(locationCode);
			boolean dataExistsWithInvestigation = false;
			if (!ServicesUtil.isEmpty(investigationId)) {
				dataExistsWithInvestigation = checkIfInvestigationDataExists(investigationId);
			}
			boolean isProactiveCandidate = isProactiveCandidate(locationCode);
			List<String> userTypeList = new ArrayList<String>();
			
			
			if (MurphyConstant.USER_TYPE_ENG.equals(userTypeInp)) {
				userTypeList.add(MurphyConstant.USER_TYPE_WW);
				userTypeList.add(MurphyConstant.USER_TYPE_RE);
				userTypeList.add(MurphyConstant.USER_TYPE_ALS);
			} else if(MurphyConstant.USER_TYPE_POT.equalsIgnoreCase(userTypeInp)) {
				userTypeList.add(MurphyConstant.USER_TYPE_POT);
			} else if(MurphyConstant.USER_TYPE_ENG_POT.equalsIgnoreCase(userTypeInp)){
				//SOC: Addition when user has both POT and ENG role for CANADA Only
				userTypeList.add(MurphyConstant.USER_TYPE_POT);
				userTypeList.add(MurphyConstant.USER_TYPE_WW);
				userTypeList.add(MurphyConstant.USER_TYPE_RE);
				userTypeList.add(MurphyConstant.USER_TYPE_ALS);
				//EOC: Addition when user has both POT and ENG role for CANADA Only
			}
				
			try {
				System.err.println("[Murphy][PWHopperStagingDao][getCheckList]\n[investigationId]" + investigationId
						+ "\n[dataExistsWithInvestigation]" + dataExistsWithInvestigation + "" + "\n[isTresholdCrossed]"
						+ isTresholdCrossed + "\n[isProactiveCandidate]" + isProactiveCandidate + "\n[userTypeInp]"
						+ userTypeInp);

				if ((isProactiveCandidate) || (userTypeList.contains(MurphyConstant.USER_TYPE_POT)
						&& (dataExistsWithInvestigation || isTresholdCrossed)) || isPro) {
					String queryString = "Select st.list_id, st.check_list_item ,st.user_type , st.data_type , ";
					if (dataExistsWithInvestigation) {
						queryString = queryString + " ins.ins_value from  tm_pwhopper_staging st "
								+ " , tm_pwhopper_instances ins  , tm_pwhopper_well_insts wi "
								+ " where ins.CHECK_LIST_ITEM_ID = st.list_id and  wi.hopper_id = ins.hopper_id and wi.location_Code ='"
								+ locationCode + "'";
					} else {
						queryString = queryString + "'' as value   from  tm_pwhopper_staging st ";
					}

					logger.error("[Murphy][PWHopperStagingDao][getCheckList][Query]" + queryString);
					Query q = this.getSession().createSQLQuery(queryString);
					List<Object[]> response = (List<Object[]>) q.list();
					if (!ServicesUtil.isEmpty(response)) {
						responseDto = new ArrayList<CheckListDto>();
						Map<String, Integer> locMap = new HashMap<String, Integer>();
						CheckListDto listDto = null;
						CheckListItemDto itemDto = null;
						for (Object[] obj : response) {
							String userType = (String) obj[2];
							itemDto = new CheckListItemDto();
							itemDto.setListId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
							itemDto.setCheckListItem(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
							itemDto.setDataType(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
							itemDto.setValue(ServicesUtil.isEmpty(obj[4])
									? (MurphyConstant.DATATYPE_CB.equals(itemDto.getDataType()) ? "false" : "")
									: (String) obj[4]);
							if (MurphyConstant.DATATYPE_CB.equals(itemDto.getDataType())) {
								itemDto.setSelected(itemDto.getValue().equals("true") ? true : false);
								itemDto.setEditable(
										((userTypeList.contains(MurphyConstant.USER_TYPE_POT) || isProactiveCandidate)
												&& userTypeList.contains((String) obj[2])
												&& !(MurphyConstant.TRUE.equals(itemDto.getValue()))) ? true : false);
							} else {
								itemDto.setEditable(
										((userTypeList.contains(MurphyConstant.USER_TYPE_POT) || isProactiveCandidate)
												&& userTypeList.contains((String) obj[2])
												&& (ServicesUtil.isEmpty(itemDto.getValue()))) ? true : false);
							}
							if (locMap.containsKey(userType)) {
								responseDto.get(locMap.get(userType)).getCheckList().add(itemDto);
							} else {
								listDto = new CheckListDto();
								listDto.setCheckList(new ArrayList<CheckListItemDto>());
								listDto.getCheckList().add(itemDto);
								listDto.setUserType(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
								locMap.put(userType, responseDto.size());
								responseDto.add(listDto);
							}

						}
					}
				}
			} catch (Exception e) {
				System.err.println("[Murphy][PWHopperStagingDao][getCheckList][error]" + e.getMessage());
			}
		} else {
			System.err.println("[Murphy][PWHopperStagingDao][getCheckList][locationCode] or [userTypeInp] is empty");

		}

		return responseDto;
	}

	public boolean checkIfInvestigationDataExists(String investigationId) {

		boolean response = false;
		String queryString = "";
		try {
			queryString = "Select count(*) from tm_pwhopper_well_insts ins where ins.INVESTIGATION_ID =  '"
					+ investigationId + "'";
			Query q = this.getSession().createSQLQuery(queryString);

			BigInteger queryResponse = (BigInteger) q.uniqueResult();

			if (!ServicesUtil.isEmpty(queryResponse) && queryResponse.intValue() > 0) {
				response = true;
			}
		} catch (Exception e) {
			logger.error("[Murphy][PWHopperStagingDao][checkIfInvestigationDataExists][error]" + e.getMessage()
					+ "\n [queryString]" + queryString);
		}
		return response;
	}

	private boolean isTresholdCrossed(String locationCode) {
		boolean response = false;
		try {
			enersightProveDailyLocal = new EnersightProveDaily();
			List<String> locCodeList = new ArrayList<String>();
			locCodeList.add(locationCode);
			List<String> muwiList = locDao.getMuwiByLocationTypeAndCode(MurphyConstant.WELL, locCodeList);
			Map<String, String> configMap = getPWHConfigValue();
			String thresholdS = configMap.get(MurphyConstant.PWH_PERCENT_VARIANCE);
			String durationS = configMap.get(MurphyConstant.PWH_DURATION_IN_DAYS);
			if (!ServicesUtil.isEmpty(durationS) && !ServicesUtil.isEmpty(thresholdS)) {
				int threshold = Integer.parseInt(thresholdS), duration = Integer.parseInt(durationS);
				String country = ServicesUtil.getCountryCodeByLocation(locationCode);
				List<String> muwiListEnersight = enersightProveDailyLocal.getMuwiWherVarLessThanThres(muwiList,
						duration, threshold, configMap.get(MurphyConstant.PWH_VERSION),country);
				if (!ServicesUtil.isEmpty(muwiListEnersight)) {
					response = true;
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][PWHopperStagingDao][isTresholdCrossed][error]" + e.getMessage() + "\n [locationCode]"
					+ locationCode);
		}
		return response;
	}

	public boolean isProactiveCandidate(String locationCode) {

		boolean response = false;
		String queryString = "";
		try {
			queryString = "select t.is_proactive from tm_pwhopper_well_insts t where location_code = '" + locationCode
					+ "' ";
			// Select count(ins) from tm_pwhopper_instances ins where
			// ins.INVESTIGATION_ID = '"+ investigationId+"'";
			Query q = this.getSession().createSQLQuery(queryString);
			Byte queryResponse = (Byte) q.uniqueResult();

			if (!ServicesUtil.isEmpty(queryResponse) && queryResponse == 1) {
				response = true;
			}
		} catch (Exception e) {
			logger.error("[Murphy][PWHopperStagingDao][isProactiveCandidate][error]" + e.getMessage()
					+ "\n [queryString]" + queryString);
		}
		return response;

	}

	public String getHopperId(String locationCode) {

		String response = "";
		String queryString = "";
		try {
			queryString = "select max(t.hopper_id) from tm_pwhopper_well_insts t where location_code = '" + locationCode
					+ "' ";
			Query q = this.getSession().createSQLQuery(queryString);
			String queryResponse = (String) q.uniqueResult();

			if (!ServicesUtil.isEmpty(queryResponse)) {
				response = queryResponse;
			}
		} catch (Exception e) {
			logger.error("[Murphy][PWHopperStagingDao][getHopperId][error]" + e.getMessage() + "\n [queryString]"
					+ queryString);
		}
		return response;

	}

	public String createInvstInstance(String muwiId, String locationCode, String investigationId,
			List<CheckListDto> checkList, String hopperId, String loggedInUser, Boolean fromProveProActive) {
		String response = MurphyConstant.FAILURE;
		String queryString = "";
		try {
			if(!ServicesUtil.isEmpty(checkList)){
				for (CheckListDto checkListObj : checkList) {
					for (CheckListItemDto item : checkListObj.getCheckList()) {
						queryString = "INSERT INTO TM_PWHOPPER_INSTANCES VALUES(" + "'" + ServicesUtil.getUUID() + "',"
								+ "'" + locationCode + "'," + "'" + hopperId + "'," + "'" + investigationId + "'," + "'"
								+ item.getListId() + "',";
						if (MurphyConstant.DATATYPE_CB.equals(item.getDataType())) {
							queryString = queryString + "'" + item.isSelected() + "',";
							if (item.isSelected()) {
								queryString = queryString + "'" + loggedInUser + "')";
							} else {
								queryString = queryString + "'')";
							}
						} else {
							queryString = queryString + "'" + item.getValue() + "', '' )";
						}
						logger.error("[Murphy][PWHopperStagingDao][createInvstInstance][queryString]"  + queryString);
						Query q = this.getSession().createSQLQuery(queryString);
						q.executeUpdate();
					}
				}
			}
			response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][PWHopperStagingDao][createInvstInstance][error]" + e.getMessage()
					+ "\n [queryString]" + queryString);
		}
		return response;
	}

	public String updateInvstInstance(String investigationId, String userType, List<CheckListDto> checkList,
			String loggedInUser) {
		String response = MurphyConstant.FAILURE;
		String queryString = "";
		try {
			for (CheckListDto checkListObj : checkList) {
				if (checkListObj.getUserType().equals(userType) || (MurphyConstant.USER_TYPE_ENG.equals(userType))) {
					for (CheckListItemDto item : checkListObj.getCheckList()) {

						if (item.isEditable()) {
							if (MurphyConstant.DATATYPE_CB.equals(item.getDataType())) {
								item.setValue(item.isSelected() + "");
							}

							queryString = "update TM_PWHOPPER_INSTANCES set INS_VALUE = '" + item.getValue()
									+ "', UPDATED_BY = '" + loggedInUser + "' where INVESTIGATION_ID = '"
									+ investigationId + "' " + "and CHECK_LIST_ITEM_ID = '" + item.getListId() + "' ";

							Query q = this.getSession().createSQLQuery(queryString);
							q.executeUpdate();
						}
					}
				}
			}
			response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][PWHopperStagingDao][updateInvstInstance][error]" + e.getMessage()
					+ "\n [queryString]" + queryString);
		}
		return response;
	}

	public String updateProactive(String locationCode, boolean status, String investigationId, String hopperId,
			boolean updateProActive) {
		String response = MurphyConstant.FAILURE;
		String queryString = "";
		try {
			queryString = "update tm_pwhopper_well_insts set investigation_id = '" + investigationId
					+ "' , HOPPER_ID = '" + hopperId + "'";
			if (updateProActive) {
				queryString = queryString + " , is_proactive = " + status;

			}
			queryString = queryString + " where location_code = '" + locationCode + "'";

			Query q = this.getSession().createSQLQuery(queryString);
			Integer queryResponse = (Integer) q.executeUpdate();
			logger.error("[PWHopperStagingDao][updateProactive][Query] "+queryString);
			if (!ServicesUtil.isEmpty(queryResponse) && queryResponse > 1) {
				response = MurphyConstant.SUCCESS;
			}
		} catch (Exception e) {
			logger.error("[Murphy][PWHopperStagingDao][updateProactive][error]" + e.getMessage() + "\n [queryString]"
					+ queryString);
		}
		return response;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<PWHopperDto> getpwHopperList(UIRequestDto requestDto) {
		enersightProveDailyLocal = new EnersightProveDaily();
		Map<String, String> configMap = getPWHConfigValue();
		String thresholdS = configMap.get(MurphyConstant.PWH_PERCENT_VARIANCE);
		String durationS = configMap.get(MurphyConstant.PWH_DURATION_IN_DAYS);
		List<PWHopperDto> responseDto = null;
		String queryString = "";
		String baseQueryEnd = "";
		Map<String,String> muwiEnersightMap = null;
		List<String> muwiListEnersight =null;
		try {
			if (!ServicesUtil.isEmpty(durationS) && !ServicesUtil.isEmpty(thresholdS)) {
				int threshold = Integer.parseInt(thresholdS), duration = Integer.parseInt(durationS);
				String country = ServicesUtil.getCountryCodeByLocation(requestDto.getLocationCodeList().get(0));
				List<String> muwiList = locDao.getMuwiByLocationTypeAndCode(requestDto.getLocationType(),
						requestDto.getLocationCodeList());
				
				if(country.equalsIgnoreCase(MurphyConstant.EFS_CODE))
				{
				muwiListEnersight = enersightProveDailyLocal.getMuwiWherVarLessThanThres(muwiList,
						duration, threshold, configMap.get(MurphyConstant.PWH_VERSION),country);
				logger.error("[Murphy][PWHopperStagingDao][getpwHopperList][muwiList]" + muwiList
						+ "\n[muwiListEnersight]" + muwiListEnersight);
				}	
				if(country.equalsIgnoreCase(MurphyConstant.CA_CODE))
				{
					muwiEnersightMap = enersightProveDailyLocal.getMuwiWherVarLessThanThresForCanada(muwiList,
							duration, threshold, configMap.get(MurphyConstant.PWH_VERSION),country);
					logger.error("[Murphy][PWHopperStagingDao][getpwHopperList][muwiEnersightMap]" + muwiEnersightMap);
					
					muwiListEnersight = new ArrayList(muwiEnersightMap.keySet());
					logger.error("list from map" + muwiListEnersight);
				}
				
				

				String boeQuery = "select max(ins_value) from  tm_pwhopper_instances where CHECK_LIST_ITEM_ID in ('21') and hopper_id = ins.hopper_id ";
				String puQuery = "select  max(ins_value) from  tm_pwhopper_instances where CHECK_LIST_ITEM_ID in ('22') and hopper_id = ins.hopper_id ";
				String wocQuery = "select  max(ins_value) from  tm_pwhopper_instances where CHECK_LIST_ITEM_ID in ('28') and hopper_id = ins.hopper_id ";

				String investigationTaskId = "(select max(te.task_id) from  tm_task_evnts te where te.process_id = ins.investigation_id and te.created_at = (select max(tr.created_At) from  tm_task_evnts tr where tr.process_id = ins.investigation_id))";
				String investigationProcessId = "(select max(te.process_id) from  tm_task_evnts te where te.process_id = ins.investigation_id and te.created_at = (select max(tr.created_At) from  tm_task_evnts tr where tr.process_id = ins.investigation_id))";

				// String investigationTaskIdInProgress = "(select
				// max(te.task_id) from tm_task_evnts te where te.process_id
				// = ins.investigation_id and te.status <> 'COMPLETED')";
				String investigationTaskIdInProgress = "(select max(te.task_id) from  tm_task_evnts te left outer join tm_proc_evnts pe on te.process_id = pe.process_id "
						+ " where pe.loc_code = ins.location_code  and te.status <> 'COMPLETED' and te.origin = '"
						+ MurphyConstant.INVESTIGATON + "')";
				String investigationProcessIdInProgress = "(select max(te.process_id) from  tm_task_evnts te left outer join tm_proc_evnts pe on te.process_id = pe.process_id "
						+ " where pe.loc_code = ins.location_code  and te.status <> 'COMPLETED' and te.origin = '"
						+ MurphyConstant.INVESTIGATON + "' )";
				String baseQueryStart = "select ins.muwi_id , ins.location_code , ins.location , ins.hopper_id , "
						+ investigationTaskId + " as investigationId , ins.is_proactive ,";
				/*
				 * String baseQueryEnd = " ," + investigationTaskIdInProgress +
				 * " as isInvestInProgress , " + investigationProcessId +
				 * " as investigationProcessId , " +
				 * investigationProcessIdInProgress +
				 * " as investigationProcessIdInProgress " +
				 * " from  tm_pwhopper_well_insts ins where ins.muwi_id in (" +
				 * ServicesUtil.getStringFromList(muwiListEnersight) + ")";
				 */

				// Incident:INC0078343 Showing list of wells if marked
				// Pro-active irrespective of meeting 20days and -40% variance
				// criteria
				// to be pushed later
				// Changes started
				
				// Getting wells according to country
				String country_like = null;
				if(country.equalsIgnoreCase("CA"))
					country_like = "MUR-CA%";
				else if(country.equalsIgnoreCase("EFS"))
					country_like = "MUR-US%";
				
				if (!ServicesUtil.isEmpty(muwiListEnersight)) {
					baseQueryEnd = " ," + investigationTaskIdInProgress + " as isInvestInProgress , "
							+ investigationProcessId + " as investigationProcessId , "
							+ investigationProcessIdInProgress + " as investigationProcessIdInProgress "
							+ " from  tm_pwhopper_well_insts ins where ins.muwi_id in ("
							+ ServicesUtil.getStringFromList(muwiListEnersight) + ") OR (ins.is_proactive = true"
							+ " and ins.location_code like '"+ country_like +"')";
				} else {
					baseQueryEnd = " ," + investigationTaskIdInProgress + " as isInvestInProgress , "
							+ investigationProcessId + " as investigationProcessId , "
							+ investigationProcessIdInProgress + " as investigationProcessIdInProgress "
							+ " from  tm_pwhopper_well_insts ins where ins.is_proactive = true and ins.muwi_id in ("
							+ ServicesUtil.getStringFromList(muwiList) + ")";
				}
				// Changes end

				String statusQueryStart = " (select  max(pi.check_list_item_id) from  tm_pwhopper_instances pi "
						+ "left outer join  tm_pwhopper_staging st on st.list_id = pi.check_list_item_id where pi.hopper_id = ins.hopper_id ";
				String inProgressQueryEnd = " and ((pi.ins_value = 'true' and st.data_type = '"
						+ MurphyConstant.DATATYPE_CB + "') or "
						+ "((pi.ins_value != '' and pi.ins_value is not null) and st.data_type = '"
						+ MurphyConstant.DATATYPE_INPUT + "') ))";

				String notCompleteQueryEnd = " and ((pi.ins_value = 'false' and st.data_type = 'CheckBox') "
						+ "or ((pi.ins_value = '' or pi.ins_value is null) and st.data_type = 'Input') ))";
				String potInProgress = statusQueryStart + " and  st.user_type = '" + MurphyConstant.USER_TYPE_POT + "'"
						+ inProgressQueryEnd;
				String alsInProgress = statusQueryStart + " and  st.user_type = '" + MurphyConstant.USER_TYPE_ALS + "'"
						+ inProgressQueryEnd;
				String reInProgress = statusQueryStart + " and  st.user_type = '" + MurphyConstant.USER_TYPE_RE + "'"
						+ inProgressQueryEnd;
				String wwInProgress = statusQueryStart + " and  st.user_type = '" + MurphyConstant.USER_TYPE_WW + "'"
						+ inProgressQueryEnd;

				String potNotCompleted = statusQueryStart + " and  st.user_type = '" + MurphyConstant.USER_TYPE_POT
						+ "'" + notCompleteQueryEnd;
				String alsNotCompleted = statusQueryStart + " and  st.user_type = '" + MurphyConstant.USER_TYPE_ALS
						+ "'" + notCompleteQueryEnd;
				String reNotCompleted = statusQueryStart + " and  st.user_type = '" + MurphyConstant.USER_TYPE_RE + "'"
						+ notCompleteQueryEnd;
				String wwNotCompleted = statusQueryStart + " and  st.user_type = '" + MurphyConstant.USER_TYPE_WW + "'"
						+ notCompleteQueryEnd;

				queryString = baseQueryStart + potInProgress + " as isPOTInProgress ,  " + "" + potNotCompleted
						+ " as potNotCompleted ," + alsInProgress + " as isALSInProgress ," + alsNotCompleted
						+ " as alsNotCompleted ," + "" + reInProgress + " as reInProgress ," + "" + reNotCompleted
						+ " as reNotCompleted ," + wwInProgress + " as wwInProgress ," + wwNotCompleted
						+ " as wwNotCompleted ," + "(" + boeQuery + ") as boeValue ," + "(" + puQuery + ") as puValue ,"
						+ "(" + wocQuery + ") as wocValue " + baseQueryEnd;

				Query q = this.getSession().createSQLQuery(queryString);
				logger.error("[Murphy][PWHopperStagingDao][getpwHopperList][queryString]" + queryString
						+ "\n[muwiList][new]" + muwiListEnersight);
				List<Object[]> response = (List<Object[]>) q.list();
				if (!ServicesUtil.isEmpty(response)) {
					PWHopperDto dto = null;
					responseDto = new ArrayList<PWHopperDto>();
					for (Object[] obj : response) {
						dto = new PWHopperDto();
						dto.setMuwi(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
						dto.setLocationCode(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
						if(country.equalsIgnoreCase(MurphyConstant.CA_CODE))
						{
							String location = muwiEnersightMap.get(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
							if(!ServicesUtil.isEmpty(location))
							{
								dto.setLocation(location);
							}
							
							else{
								dto.setLocation(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
							}
						
						}
						else
						{
						dto.setLocation(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
						}
						dto.setHopperId(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
						// dto.setInvestigationTaskId(ServicesUtil.isEmpty(obj[4])?
						// null : (String) obj[4]);
						dto.setProactive(ServicesUtil.isEmpty(obj[5]) ? false : ((Byte) obj[5] == 0 ? false : true));
						dto.setLocationType(MurphyConstant.WELL);
						if (!ServicesUtil.isEmpty(obj[17])) {
							dto.setHasInvestigation(true);
							dto.setInvestigationTaskId(ServicesUtil.isEmpty(obj[17]) ? null : (String) obj[17]);
							dto.setInvestigationProcessId(ServicesUtil.isEmpty(obj[19]) ? null : (String) obj[19]);
						} else {
							dto.setInvestigationTaskId(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
							dto.setInvestigationProcessId(ServicesUtil.isEmpty(obj[18]) ? null : (String) obj[18]);
						}
						if (dto.isProactive()) {
							dto.setPotStatus(returnColour(obj[6], obj[7]));
							dto.setAlsStatus(returnColour(obj[8], obj[9]));
							dto.setReStatus(returnColour(obj[10], obj[11]));
							dto.setWwStatus(returnColour(obj[12], obj[13]));
						} else {
							if (ServicesUtil.isEmpty(obj[4])) {
								dto.setPotStatus(MurphyConstant.RED);
							} else {
								dto.setPotStatus(returnColour(obj[6], obj[7]));
							}
							dto.setAlsStatus(MurphyConstant.GREY);
							dto.setReStatus(MurphyConstant.GREY);
							dto.setWwStatus(MurphyConstant.GREY);
						}

						dto.setBoe(ServicesUtil.isEmpty(obj[14]) ? null : (String) obj[14]);
						dto.setPotentialUplift(ServicesUtil.isEmpty(obj[15]) ? null : (String) obj[15]);
						dto.setWorkOverCost(ServicesUtil.isEmpty(obj[16]) ? null : (String) obj[16]);

						responseDto.add(dto);

					}
				}
				// }
			}
		} catch (Exception e) {
			logger.error("[Murphy][PWHopperStagingDao][getpwHopperList][error]" + e.getMessage() + "\n [queryString]"
					+ queryString);
		}
		return responseDto;
	}

	public String returnColour(Object isInProgress, Object isNotCompleted) {
		String response = "";
		if (ServicesUtil.isEmpty(isNotCompleted)) {
			// completed
			response = MurphyConstant.GREEN;
		} else if (ServicesUtil.isEmpty(isInProgress)) {
			response = MurphyConstant.RED;
		} else {
			response = MurphyConstant.ORANGE;
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getPWHConfigValue() {
		Map<String, String> configMap = null;
		String queryString = "";
		try {
			queryString = "	select config_id , config_desc_value from tm_config_values where config_id in " + "('"
					+ MurphyConstant.PWH_PERCENT_VARIANCE + "' ,'" + MurphyConstant.PWH_DURATION_IN_DAYS + "' ,'"
					+ MurphyConstant.PWH_VERSION + "') ";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> queryResponse = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(queryResponse)) {
				configMap = new HashMap<String, String>();
				for (Object[] obj : queryResponse) {
					configMap.put((String) obj[0], (String) obj[1]);
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][PWHopperStagingDao][getPWHConfigValue][error]" + e.getMessage() + "\n [queryString]"
					+ queryString);
		}
		return configMap;
	}

	public String setDataForPWHopperWell() {
		String response = MurphyConstant.FAILURE;
		String queryString = "";
		try {
			queryString = "	create table TM_PWHOPPER_WELL_INSTS as (select wm.muwi as MUWI_ID , wm.location_code as LOCATION_CODE , p1.location_text as LOCATION , 0 as IS_PROACTIVE  , '' AS HOPPER_ID , '' AS  INVESTIGATION_ID  from production_location p1  join well_muwi wm on wm.location_code = p1.location_code where p1.LOCATION_TYPE = 'Well' ); ";
			Query q = this.getSession().createSQLQuery(queryString);
			Integer queryResponse = (Integer) q.executeUpdate();
			if (!ServicesUtil.isEmpty(queryResponse) && queryResponse > 0) {
				response = MurphyConstant.SUCCESS;
			}
		} catch (Exception e) {
			logger.error("[Murphy][PWHopperStagingDao][setDataForPWHopperWell][error]" + e.getMessage()
					+ "\n [queryString]" + queryString);
		}
		return response;
	}
	
	public boolean checkIfPwHopperExists(String location_code) {

		boolean response = false;
		String queryString = "";
		try {
			queryString = "Select count(*) from tm_pwhopper_well_insts ins where ins.location_code =  '"
					+ location_code + "'";
			Query q = this.getSession().createSQLQuery(queryString);

			BigInteger queryResponse = (BigInteger) q.uniqueResult();

			if (!ServicesUtil.isEmpty(queryResponse) && queryResponse.intValue() > 0) {
				response = true;
			}
		} catch (Exception e) {
			logger.error("[Murphy][PWHopperStagingDao][checkIfPwHopperExists][error]" + e.getMessage()
					+ "\n [queryString]" + queryString);
		}
		return response;
	}
	
	public String insertProactive(String muwi, String locationCode, String location, String hopperId, String investigationId, 
			boolean status) {
		String response = MurphyConstant.FAILURE;
		String queryString = "";
		try {
			if(ServicesUtil.isEmpty(location))
				location = locDao.getLocationByLocCode(locationCode);
			queryString = "insert into tm_pwhopper_well_insts values ('" + muwi + "','" + locationCode + "','" + location + "','" + hopperId + "','" + investigationId 
					+ "'," + status + ", '') ";

			Query q = this.getSession().createSQLQuery(queryString);
			Integer queryResponse = (Integer) q.executeUpdate();
			logger.error("[PWHopperStagingDao][insertProactive][Query] "+queryString);
			if (!ServicesUtil.isEmpty(queryResponse) && queryResponse > 1) {
				response = MurphyConstant.SUCCESS;
			}
		} catch (Exception e) {
			logger.error("[Murphy][PWHopperStagingDao][insertProactive][error]" + e.getMessage() + "\n [queryString]"
					+ queryString);
		}
		return response;

	}

	public static void main(String args[]) {
		/*
		 * boolean isTresholdCrossed = false; boolean
		 * dataExistsWithInvestigation = true; boolean isProactiveCandidate =
		 * false; String userTypeInp = "ENG"; String type = "POT"; List<String>
		 * userTypeList = Arrays.asList(userTypeInp.split(","));
		 * System.out.println(userTypeList); if((isProactiveCandidate) ||
		 * (userTypeList.contains(type) &&(dataExistsWithInvestigation ||
		 * isTresholdCrossed ))){ System.out.println("true"); }else{
		 * System.out.println("false"); }
		 */
		PWHopperStagingDao dao = new PWHopperStagingDao();
		System.err.println(dao.returnColour("2", "2"));

	}

}