package com.murphy.taskmgmt.dao;

/**
 * @author Rashmendra.Sai
 *
 */
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.geotab.Coordinates;
import com.murphy.taskmgmt.dto.ATSOpertaorDetailsDto;
import com.murphy.taskmgmt.dto.ATSTaskListDto;
import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.StopTimeDto;
import com.murphy.taskmgmt.scheduler.CanaryStagingScheduler;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("AutoTaskSchedulingDao")
@Transactional
public class AutoTaskSchedulingDao {

	private static final Logger logger = LoggerFactory.getLogger(AutoTaskSchedulingDao.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	HierarchyDao locDao;

	@Autowired
	CanaryStagingScheduler canaryStaging;

	@Autowired
	StopTimeDao stDao;

	public AutoTaskSchedulingDao() {
	}

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][AutoTaskSchedulingDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}
	}

	// SOC: Task Fetching for ATS only for Pigging and ITA with status NEW
	@SuppressWarnings("unchecked")
	public List<ATSTaskListDto> getTaskIdsforATSVOne() {
		List<ATSTaskListDto> dtoList = null;
		ATSTaskListDto evntsDto = null;
		try {
			String fetchQuery = "SELECT te.task_id,pe.process_id,pe.loc_code FROM TM_TASK_EVNTS te join TM_PROC_EVNTS pe on te.process_id = pe.process_id"
					+ " WHERE PARENT_ORIGIN in ('" + MurphyConstant.P_ITA + "'" + ",'" + MurphyConstant.P_ITA_DOP
					+ "') and te.status in ('" + MurphyConstant.NEW_TASK + "') order by te.created_at desc";
			logger.error("[AutoTaskSchedulingDao][Query]" + fetchQuery);
			Query q = this.getSession().createSQLQuery(fetchQuery);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				dtoList = new ArrayList<ATSTaskListDto>();
				for (Object[] obj : resultList) {
					evntsDto = new ATSTaskListDto();
					evntsDto.setTaskId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					evntsDto.setProcessId(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					evntsDto.setLocationCode(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					evntsDto.setLocText(locDao.getLocationByLocCode(evntsDto.getLocationCode()));
					evntsDto.setKoneWestFacilityCode("MUR-US-EFS-CT00-KWCF");
					// Catarina CF- Kone West
					if (!ServicesUtil.isEmpty(evntsDto.getLocationCode())) {
						evntsDto.setMuwiID(
								ServicesUtil.isEmpty(locDao.getMuwiByLocationCode(evntsDto.getLocationCode())) ? null
										: locDao.getMuwiByLocationCode(evntsDto.getLocationCode()));
						logger.error("[AutoTaskSchedulingDao][MuwiId] "+evntsDto.getMuwiID() + " locCode "+evntsDto.getLocationCode() );
						// if (!ServicesUtil.isEmpty(evntsDto.getMuwiID())) {
						/*Coordinates coordinates = locDao.getCoordByCode(evntsDto.getLocationCode());
						evntsDto.setWellLatCoord(coordinates.getLatitude());
						evntsDto.setWellLongCoord(coordinates.getLongitude());*/

						// Catarina- Kone West
						if ((evntsDto.getLocationCode().substring(0, 15).trim().equalsIgnoreCase("MUR-US-EFS-CT00"))) {
							evntsDto.setFacilityLocCode("MUR-US-EFS-CT00-KWCF");
							evntsDto.setFaciltyText("Kone West");
							evntsDto.setFieldName("CATARINA");
						}
						// Tilden - Jamberg West
						else if ((evntsDto.getLocationCode().substring(0, 15).trim()
								.equalsIgnoreCase("MUR-US-EFS-TC00"))
								|| (evntsDto.getLocationCode().substring(0, 15).trim()
										.equalsIgnoreCase("MUR-US-EFS-TE00"))
								|| (evntsDto.getLocationCode().substring(0, 15).trim()
										.equalsIgnoreCase("MUR-US-EFS-TN00"))
								|| (evntsDto.getLocationCode().substring(0, 15).trim()
										.equalsIgnoreCase("MUR-US-EFS-TW00"))) {
							evntsDto.setFacilityLocCode("MUR-US-EFS-TC00-JWCF");
							evntsDto.setFaciltyText("Jamberg West");
							evntsDto.setFieldName("TILDEN");
						}
						// Karnes- Drees South
						else if ((evntsDto.getLocationCode().substring(0, 15).trim()
								.equalsIgnoreCase("MUR-US-EFS-KS00"))
								|| (evntsDto.getLocationCode().substring(0, 15).trim()
										.equalsIgnoreCase("MUR-US-EFS-KN00"))) {
							evntsDto.setFacilityLocCode("MUR-US-EFS-KS00-DSCF");
							evntsDto.setFaciltyText("Drees South");
							evntsDto.setFieldName("KARNES");
						}

						List<String> locCordList = new ArrayList<>();
						locCordList.add(evntsDto.getFacilityLocCode());
						LocationHierarchyDto hDto = locDao.getCoordWithLocationCode(locCordList).get(0);
						if (!ServicesUtil.isEmpty(hDto)) {
							evntsDto.setFacilityLatCoord(ServicesUtil.isEmpty(hDto.getLatValue().doubleValue()) ? null
									: hDto.getLatValue().doubleValue());
							evntsDto.setFacilityLongCoord(ServicesUtil.isEmpty(hDto.getLongValue().doubleValue()) ? null
									: hDto.getLongValue().doubleValue());
						}
						// }
					}
					dtoList.add(evntsDto);
				}
			}
			// dtoList = getTaskIdsforATSVTwo(dtoList);
		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingDao][getTaskIdsforATSVOne][Error] " + e.getMessage());
		}
		logger.error("[AutoTaskSchedulingDao][getTaskIdsforATSVOne][dtoList] "+dtoList);
		return dtoList;
	}

	// SOC: Task Fetching for ATS
	// Parent_Origin:Custom, Classification: Downtime, status: NEW
	@SuppressWarnings("unchecked")
	public List<ATSTaskListDto> getTaskIdsforATSVTwo(List<ATSTaskListDto> dtoList) {
		ATSTaskListDto evntsDto = null;
		try {

			String fetchQuery = "select te.task_id,pe.process_id,pe.loc_code from TM_ATTR_INSTS i inner join tm_task_evnts te on i.task_id = te.task_id "
					+ "join tm_proc_evnts pe on te.process_id = pe.process_id on " + "where INS_VALUE = '"
					+ MurphyConstant.DOWNTIME_CLASSIFICATION + "' and i.ATTR_TEMP_ID = '12345' "
					+ "and (te.status in ('" + MurphyConstant.NEW_TASK + "') and te.parent_origin in ('"
					+ MurphyConstant.CUSTOM + "'))" + "order by te.created_at desc";
			logger.error("[getTaskIdsforATSVTwo][Query]" + fetchQuery);
			Query q = this.getSession().createSQLQuery(fetchQuery);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {
					evntsDto = new ATSTaskListDto();
					evntsDto.setTaskId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					evntsDto.setProcessId(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					evntsDto.setLocationCode(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					// Catarina CF- Kone West
					evntsDto.setKoneWestFacilityCode("MUR-US-EFS-CT00-KWCF");
					if (!ServicesUtil.isEmpty(evntsDto.getLocationCode())) {
						evntsDto.setMuwiID(
								ServicesUtil.isEmpty(locDao.getMuwiByLocationCode(evntsDto.getLocationCode())) ? null
										: locDao.getMuwiByLocationCode(evntsDto.getLocationCode()));
						if (!ServicesUtil.isEmpty(evntsDto.getMuwiID())) {
							Coordinates coordinates = locDao.getCoordByCode(evntsDto.getLocationCode());
							evntsDto.setWellLatCoord(coordinates.getLatitude());
							evntsDto.setWellLongCoord(coordinates.getLongitude());
							LocationHierarchyDto hDto = locDao.getFacilityDetailsforMuwi(evntsDto.getMuwiID());
							evntsDto.setFacilityLocCode(
									ServicesUtil.isEmpty(hDto.getLocation()) ? null : hDto.getLocation());
							evntsDto.setFacilityLatCoord(ServicesUtil.isEmpty(hDto.getLatValue().doubleValue()) ? null
									: hDto.getLatValue().doubleValue());
							evntsDto.setFacilityLongCoord(ServicesUtil.isEmpty(hDto.getLongValue().doubleValue()) ? null
									: hDto.getLongValue().doubleValue());
							evntsDto.setFaciltyText(
									ServicesUtil.isEmpty(hDto.getFacility()) ? null : hDto.getFacility());
						}
					}
					dtoList.add(evntsDto);
				}
			}
			logger.error("[AutoTaskSchedulingDao][getTaskIdsforATSVTwo][dtoList] " + dtoList);
		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingDao][getTaskIdsforATSVTwo][Error] " + e.getMessage());
		}
		return dtoList;
	}

	// SOC: Fetching classification and sub- classification for taskIDs
	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, List<String>> getCategoryforTaskIds(List<String> taskIdList) {
		LinkedHashMap<String, List<String>> taskCorresCategory = null;
		try {
			String taskIds = ServicesUtil.getStringFromList(taskIdList);
			String fetchQuery = "Select ins.task_id,temp.LABEL,ins.INS_VALUE" + " from TM_ATTR_INSTS ins"
					+ " inner join TM_ATTR_TEMP temp on temp.ATTR_ID = ins.ATTR_TEMP_ID " + "where ins.TASK_ID in( "
					+ taskIds + ") and temp.label in ('Task Classification','Sub Classification') ";

			logger.error("[getCategoryforTaskIds][Query]" + fetchQuery);
			CustomAttrTemplateDto dto = null;
			Query q = this.getSession().createSQLQuery(fetchQuery);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				taskCorresCategory = new LinkedHashMap<>();
				for (Object[] obj : resultList) {
					dto = new CustomAttrTemplateDto();
					dto.setTaskId((ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]));
					dto.setLabel((ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]));
					dto.setLabelValue((ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]));
					if (taskCorresCategory.containsKey(dto.getTaskId())) {
						taskCorresCategory.get(dto.getTaskId()).add(dto.getLabelValue());
					} else {
						taskCorresCategory.put(dto.getTaskId(), new ArrayList<String>());
						taskCorresCategory.get(dto.getTaskId()).add(dto.getLabelValue());
					}
				}
			} else
				logger.error("[AutoTaskSchedulingDao][getCategoryforTaskIds] [Empty Response] ");
		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingDao][getCategoryforTaskIds][Error] " + e.getMessage());
		}
		return taskCorresCategory;
	}

	// Fetching previous load of operators if any task is not complete
	@SuppressWarnings("unchecked")
	public Map<String, List<String>> findLoadOfOperator(Map<String, List<String>> shiftList,
			List<ATSOpertaorDetailsDto> opertaorDetailsList) {
		List<String> emailIdList = new ArrayList<>();
		try {

			for (List<String> i : shiftList.values()) {
				for (String s : i)
					emailIdList.add(s);
			}
			String emailIdInString = ServicesUtil.getStringFromList(emailIdList);

			Calendar start = Calendar.getInstance();
			// CST :5am, UTC: 10am
			start.set(Calendar.HOUR_OF_DAY, 9);
			start.set(Calendar.MINUTE, 59);
			start.set(Calendar.SECOND, 00);
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH24:mi:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat dates = new SimpleDateFormat("yyyy/MM/dd");
			String strtTimeString = sd.format(start.getTime());
			logger.error("Line 262: " + strtTimeString);
			String onlyDateString = dates.format(start.getTime());
			logger.error("Line 264: " + onlyDateString);

			Calendar end = Calendar.getInstance();
			// CST :10am, UTC: 3pm
			end.set(Calendar.HOUR_OF_DAY, 15);
			end.set(Calendar.MINUTE, 00);
			end.set(Calendar.SECOND, 00);
			String endTimeString = sd.format(end.getTime());
			logger.error("Line 273: " + endTimeString);

			String removeQuery = "select TASK_OWNER from tm_task_owner tw where ((tw.start_time between TO_TIMESTAMP('"
					+ strtTimeString + "','yyyy-MM-dd HH24:mi:ss') " + "and TO_TIMESTAMP('" + endTimeString
					+ "','yyyy-MM-dd HH24:mi:ss')) " + "and tw.end_time between TO_TIMESTAMP('" + strtTimeString
					+ "','yyyy-MM-dd HH24:mi:ss') " + "and TO_TIMESTAMP('" + endTimeString
					+ "','yyyy-MM-dd HH24:mi:ss')) and " + "tw.TASK_OWNER_EMAIL in (" + emailIdInString + ")";

			logger.error("[findLoadOfOperator][removeQuery]" + removeQuery);
			Query removeQ = this.getSession().createSQLQuery(removeQuery);
			List<Object[]> removeResult = removeQ.list();
			if (!ServicesUtil.isEmpty(removeResult)) {
				for (Object[] obj : removeResult) {
					emailIdList.remove((String) obj[0]);
				}
			}

			String refinedEmailInString = ServicesUtil.getStringFromList(emailIdList);
			logger.error("[findLoadOfOperator][refinedEmailInString]" + refinedEmailInString);

			// As of now ITA does not take Inquiry and Investigation , so does
			// ATS
			String newQuery = "Select TASK_OWNER,totalTime,LOC_CODE,end_time,ROW_NUMBER() OVER(Partition by TASK_OWNER ORDER BY TASK_OWNER) AS ROWNUM from ("
					+ " SELECT tw.TASK_OWNER AS TASK_OWNER,tw.END_TIME as end_time,pe.loc_code as LOC_CODE,"
					+ " SUM(tw.EST_RESOLVE_TIME + tw.EST_DRIVE_TIME) as totalTime "
					+ " FROM TM_TASK_EVNTS AS te LEFT OUTER JOIN TM_TASK_OWNER AS tw"
					+ " ON te.TASK_ID = tw.TASK_ID join tm_proc_evnts pe on pe.process_id = te.process_id"
					+ " where te.STATUS IN ('ASSIGNED','IN PROGRESS') and te.origin not in ('Investigation','Inquiry') and tw.TASK_OWNER_EMAIL in ("
					+ refinedEmailInString + ") and tw.end_time <= TO_TIMESTAMP('" + strtTimeString
					+ "', 'yyyy-MM-dd HH24:mi:ss')) " + "and to_date(tw.end_time) = '" + onlyDateString + "'"
					+ " group by task_owner, tw.END_TIME,pe.LOC_CODE order by tw.END_TIME desc " + ")";

			logger.error("[findLoadOfOperator][newQuery]" + newQuery);

			String mainQuery = "Select TASK_OWNER,totalTime,LOC_CODE ,end_time from (" + newQuery
					+ ") where ROWNUM = 1";

			for (String s : emailIdList) {
				for (List<String> k : shiftList.values())
					k.removeIf(value -> value.contains(s));
			}

			logger.error("[findLoadOfOperator][mainQuery]" + mainQuery);
			Query q = this.getSession().createSQLQuery(mainQuery);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				/*
				 * Date latestEndTime = new Date(); Date newlatestEndStartTime =
				 * null; for (Object[] obj : resultList) { latestEndTime =
				 * ServicesUtil.convertFromZoneToZone(null, obj[3], "", "",
				 * MurphyConstant.DATE_DB_FORMATE,
				 * MurphyConstant.DATE_DB_FORMATE); newlatestEndStartTime =
				 * (ServicesUtil.isEmpty(latestEndTime) ? null :
				 * ServicesUtil.convertFromZoneToZone(null, latestEndTime,
				 * MurphyConstant.UTC_ZONE, MurphyConstant.UTC_ZONE,
				 * MurphyConstant.DATE_DB_FORMATE,
				 * MurphyConstant.DATE_DISPLAY_FORMAT)); for
				 * (ATSOpertaorDetailsDto a : opertaorDetailsList) { if
				 * (a.getEmailID().equalsIgnoreCase((String) obj[0])) { //
				 * fixedTime.add(Calendar.MINUTE, (int) Math.round(-(time -
				 * (Double) ott.getValue()[0]))); //
				 * a.setStartTimeInString(sdf.format(fixedTime.getTime()));
				 * a.setStartTimeForNextTask(newlatestEndStartTime);
				 * a.setPrevTaskLocCode((String) obj[2]); } }
				 * 
				 * }
				 */

				/*
				 * Double time = (double) 180; Calendar fixedTime =
				 * Calendar.getInstance(); fixedTime.set(Calendar.HOUR_OF_DAY,
				 * 10); fixedTime.set(Calendar.MINUTE, 00);
				 * fixedTime.set(Calendar.SECOND, 00); SimpleDateFormat sdf =
				 * new SimpleDateFormat("HH:mm:ss"); // ownerTotalTimeLocMap -
				 * Key : emailId, // Value :Object[workload,locCode]
				 * HashMap<String, Object[]> ownerEmailTotalTimeLocMap = new
				 * HashMap<>(); for (Object[] obj : resultList) { Object
				 * objectArray[] = new Object[2]; if
				 * (ownerEmailTotalTimeLocMap.containsKey((String) obj[0])) {
				 * Double d = (Double) (ownerEmailTotalTimeLocMap.get((String)
				 * obj[0])[0]); BigDecimal bd = (BigDecimal) obj[1]; d +=
				 * bd.doubleValue(); objectArray[0] = d; objectArray[1] =
				 * (String) (ownerEmailTotalTimeLocMap.get((String) obj[0])[1]);
				 * ownerEmailTotalTimeLocMap.put((String) obj[0], objectArray);
				 * } else { BigDecimal bd = (BigDecimal) obj[1]; objectArray[0]
				 * = bd.doubleValue(); objectArray[1] = (String) obj[2];
				 * ownerEmailTotalTimeLocMap.put((String) obj[0], objectArray);
				 * } }
				 */
				/*
				 * logger.error("ownerEmailTotalTimeLocMap " +
				 * ownerEmailTotalTimeLocMap); for (Entry<String, Object[]> ott
				 * : ownerEmailTotalTimeLocMap.entrySet()) { if ((Double)
				 * (ott.getValue()[0]) >= time) { for (List<String> k :
				 * shiftList.values()) k.removeIf(value ->
				 * value.contains(ott.getKey())); } else {
				 */
				// Updating in the same DTOList with updated Start Time
				// for next task, Prev Loc Code
				/*
				 * for (ATSOpertaorDetailsDto a : opertaorDetailsList) { if
				 * (a.getEmailID().equalsIgnoreCase(ott.getKey())) {
				 * fixedTime.add(Calendar.MINUTE, (int) Math.round(-(time -
				 * (Double) ott.getValue()[0])));
				 * a.setStartTimeInString(sdf.format(fixedTime.getTime()));
				 * a.setStartTimeForNextTask(fixedTime.getTime());
				 * a.setPrevTaskLocCode((String) ott.getValue()[1]); } }
				 */
				// }
				// }
				logger.error("[Final ATSOpertaorDetailsDto List] " + opertaorDetailsList);
			} else {
				logger.error("No work-load for the emailId passed");
			}
		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingDao][findLoadOfOperator][Error] " + e.getMessage());
		}
		logger.error("[Returning ShiftList] : " + shiftList);
		return shiftList;
	}

	// Fetching resolve/stop time of operators based of classification and sub
	// classification
	@SuppressWarnings("unchecked")
	public StopTimeDto getStopTimeBasedOnClassification(String classfication, String subClassification) {
		StopTimeDto stDto = null;
		try {
			if (!ServicesUtil.isEmpty(classfication) && !ServicesUtil.isEmpty(subClassification)) {
				String queryString = "select PRO_A, PRO_B,OBX,SSE from TM_TASK_STOP_TIME_BY_ROLE"
						+ " where CLASSIFICATION = '" + classfication.trim() + "' and SUB_CLASSIFICATION = '"
						+ subClassification.trim() + "'";
				Query q = this.getSession().createSQLQuery(queryString);
				List<Object[]> resultList = q.list();
				if (!ServicesUtil.isEmpty(resultList)) {
					for (Object[] obj : resultList) {
						stDto = new StopTimeDto();
						stDto.setProA((int) obj[0]);
						stDto.setProB((int) obj[1]);
						stDto.setSse((int) obj[2]);
						stDto.setObx((int) obj[3]);
					}
					logger.error("[getStopTimeBasedOnClassification][STOPTIMEDTO] " + stDto);
				} else {
					logger.error("[getStopTimeBasedOnClassification][No data present in DB for the parameters passed]");
					stDto = new StopTimeDto();
					stDto.setProA(0);
					stDto.setProB(0);
					stDto.setSse(0);
					stDto.setObx(0);
					logger.error("[getStopTimeBasedOnClassification][Assigning stop time as 0]");
				}
			} else
				logger.error("[AutoTaskSchedulingDao][getStopTimeBasedOnClassification] [Empty Categories] ");
		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingDao][getStopTimeBasedOnClassification][Error] " + e.getMessage());
		}
		return stDto;
	}

	// Fetch OperatorsRoadDriveTime between well locations except from facility
	public Double getRoadDriveTime(String fromLocCode, String toLocCode) {
		Double roadDriveTimeBetweenWell = null;
		try {
			if (!ServicesUtil.isEmpty(fromLocCode) && !ServicesUtil.isEmpty(toLocCode)) {
				String queryString = "select ROAD_DRIVE_TIME from LOCATION_DISTANCES"
						+ " where FROM_LOCATION_CODE in ('" + fromLocCode.trim() + "') and TO_LOCATION_CODE in ('"
						+ toLocCode.trim() + "')";
				Query q = this.getSession().createSQLQuery(queryString);
				Object obj = q.uniqueResult();
				logger.error("obj " + obj);
				logger.error("FROM_LOCATION_CODE: " + fromLocCode + " TO_LOCATION_CODE: " + toLocCode);
				if (!ServicesUtil.isEmpty(obj)) {
					roadDriveTimeBetweenWell = ((BigDecimal) obj).doubleValue();
					logger.error("[getRoadDriveTime][roadDriveTimeBetweenWell] " + roadDriveTimeBetweenWell);
				} else
					logger.error("[getRoadDriveTime][No data present in DB for the parameters passed]");

			} else
				logger.error("[AutoTaskSchedulingDao][getRoadDriveTime] [Empty Parameters ] ");
		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingDao][getRoadDriveTime][Error] " + e.getMessage());
		}
		return roadDriveTimeBetweenWell;
	}

	// Fetch Designation of Operators based on EmailID
	public String getFieldDesignationOfOperators(String emailId) {
		String userFieldDesig = null;
		try {
			if (!ServicesUtil.isEmpty(emailId)) {
				String queryString = "select user_role from TM_USER_IDP_MAPPING" + " where USER_EMAIL in ('"
						+ emailId.trim() + "')";
				Query q = this.getSession().createSQLQuery(queryString);
				Object obj = q.uniqueResult();
				if (!ServicesUtil.isEmpty(obj)) {
					if (((String) obj).trim().contains("PRO_KARNES"))
						userFieldDesig = "PRO_KARNES";
					else if (((String) obj).trim().contains("PRO_CATARINA"))
						userFieldDesig = "PRO_CATARINA";
					else if (((String) obj).trim().contains("PRO_TILDEN"))
						userFieldDesig = "PRO_TILDEN";
				} else
					logger.error("[getFieldDesignationOfOperators][No data present in DB for the parameters passed] "
							+ emailId);

			} else
				logger.error("[AutoTaskSchedulingDao][getFieldDesignationOfOperators] [Empty Parameters ] ");
		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingDao][getFieldDesignationOfOperators][Error] " + e.getMessage());
		}
		return userFieldDesig;
	}

	// Query to fetch operator's availability between CST 5am to 10am
	/*@SuppressWarnings("unchecked")
	public LinkedHashMap<String, List<ATSOpertaorDetailsDto>> getAvailabilityOfOperator(
			Map<String, List<String>> shiftList) {
		LinkedHashMap<String, List<ATSOpertaorDetailsDto>> opAvail = null; // Key:OwnerEmail,
																			// Value:List<ATSOpertaorDetailsDto>
		List<String> emailIdList = new ArrayList<>();
		List<ATSOpertaorDetailsDto> dtoList = null;
		ATSOpertaorDetailsDto dto = null;
		try {
			for (List<String> i : shiftList.values()) {
				for (String s : i)
					emailIdList.add(s);
			}
			String opListInString = ServicesUtil.getStringFromList(emailIdList);
			Calendar start = Calendar.getInstance();
			// CST :5am, UTC: 10am
			start.set(Calendar.HOUR_OF_DAY, 10);
			start.set(Calendar.MINUTE, 00);
			start.set(Calendar.SECOND, 00);
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH24:mi:ss");
			String strtTimeString = sd.format(start.getTime());

			// CST :10AM, UTC: 3PM
			Calendar end = Calendar.getInstance();
			end.set(Calendar.HOUR_OF_DAY, 15);
			end.set(Calendar.MINUTE, 00);
			end.set(Calendar.SECOND, 00);
			String endTimeString = sd.format(end.getTime());

			String query = "select tw.task_owner,pe.loc_code,tw.start_time,tw.end_time from "
					+ "tm_task_owner tw left join tm_task_evnts te on tw.task_id=te.task_id "
					+ "inner join tm_proc_evnts pe on pe.process_id = te.process_id"
					+ " where (tw.end_time <= TO_TIMESTAMP('" + endTimeString + "', 'yyyy-MM-dd HH24:mi:ss')"
					+ "and tw.end_time >= TO_TIMESTAMP('" + strtTimeString + "', 'yyyy-MM-dd HH24:mi:ss')) and "
					+ "te.status in ('IN PROGRESS','ASSIGNED') and  tw.task_owner in (" + opListInString + ") "
					+ "group by tw.task_owner,tw.end_time,pe.loc_code, tw.start_time,tw.end_time"
					+ "order by tw.task_owner, tw.end_time asc";
			Query q = this.getSession().createSQLQuery(query);
			List<Object[]> result = q.list();
			if (!ServicesUtil.isEmpty(result)) {
				opAvail = new LinkedHashMap<>();
				for (Object[] obj : result) {
					dto = new ATSOpertaorDetailsDto();
					dtoList = new ArrayList<>();
					if (opAvail.containsKey((String) obj[0])) {
						dto.setEmailID((String) obj[0]);
						dto.setUserFieldDesignation(getFieldDesignationOfOperators((String) obj[0]));
						dto.setPrevTaskLocCode((String) obj[1]);
						dto.setStartTimeForNextTask((Date) obj[2]);
						dto.setEndTimeForTask((Date) obj[3]);
						for (Entry<String, List<String>> s : shiftList.entrySet()) {
							for (String k : s.getValue()) {
								if (dto.getEmailID().equalsIgnoreCase(k)) {
									dto.setDesignation(s.getKey());
									break;
								}
							}
						}
						dtoList.add(dto);
						opAvail.get((String) obj[0]).addAll(dtoList);
					} else {
						dto.setEmailID((String) obj[0]);
						dto.setUserFieldDesignation(getFieldDesignationOfOperators((String) obj[0]));
						dto.setPrevTaskLocCode((String) obj[1]);
						dto.setStartTimeForNextTask((Date) obj[2]);
						dto.setEndTimeForTask((Date) obj[3]);
						for (Entry<String, List<String>> s : shiftList.entrySet()) {
							for (String k : s.getValue()) {
								if (dto.getEmailID().equalsIgnoreCase(k)) {
									dto.setDesignation(s.getKey());
									break;
								}
							}
						}
						dtoList.add(dto);
						opAvail.put(dto.getEmailID(), dtoList);
					}
				}
			} else {
				logger.error("[AutoTaskSchedulingDao][getAvailabilityOfOperator][No operator free in this time]");
				opAvail = new LinkedHashMap<>();
				dtoList = new ArrayList<>();
				for (String i : emailIdList) {
					dto = new ATSOpertaorDetailsDto();
					dto.setPrevTaskLocCode(null);
					dto.setEmailID(i);
					dto.setStartTimeForNextTask((Date) start.getTime());
					dto.setUserFieldDesignation((getFieldDesignationOfOperators(i)));
					dto.setEndTimeForTask(null);
					for (Entry<String, List<String>> s : shiftList.entrySet()) {
						if (dto.getEmailID().equalsIgnoreCase(i)) {
							dto.setDesignation(s.getKey());
							break;
						}
					}
					dtoList.add(dto);
					opAvail.put(i, dtoList);
				}
			}
		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingDao][getAvailabilityOfOperator][Error] " + e.getMessage());
		}
		return opAvail;
	}*/

}
