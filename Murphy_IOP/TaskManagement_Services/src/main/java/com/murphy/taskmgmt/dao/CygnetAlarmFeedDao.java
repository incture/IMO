package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.CygnetAlarmFeedDto;
import com.murphy.taskmgmt.dto.CygnetAlarmFeedResponseDto;
import com.murphy.taskmgmt.dto.CygnetAlarmRequestDto;
import com.murphy.taskmgmt.dto.DowntimeCaptureHistoryDto;
import com.murphy.taskmgmt.dto.LocationTimeZoneMasterDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.CygnetAlarmFeedDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("cygnetAlarmFeedDao")
public class CygnetAlarmFeedDao extends BaseDao<CygnetAlarmFeedDo, CygnetAlarmFeedDto> {

	@Autowired
	private DowntimeCaptureHistoryDao downtimeCaptureDao;

	@Autowired
	HierarchyDao hierarchyDao;

	@Autowired
	LocationTimeZoneMasterDao locationMasterTimeZoneDao;

	private static final Logger logger = LoggerFactory.getLogger(CygnetAlarmFeedDao.class);

	@Override
	protected CygnetAlarmFeedDo importDto(CygnetAlarmFeedDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		CygnetAlarmFeedDo entity = new CygnetAlarmFeedDo();
		if (!ServicesUtil.isEmpty(fromDto.getPointId()))
			entity.setPointId(fromDto.getPointId());
		if (!ServicesUtil.isEmpty(fromDto.getAlarmCondition()))
			entity.setAlarmCondition(fromDto.getAlarmCondition());
		if (!ServicesUtil.isEmpty(fromDto.getAlarmValue()))
			entity.setAlarmValue(fromDto.getAlarmValue());
		if (!ServicesUtil.isEmpty(fromDto.getTimeStamp()))
			entity.setTimeStamp(fromDto.getTimeStamp());
		if (!ServicesUtil.isEmpty(fromDto.getTag()))
			entity.setTag(fromDto.getTag());
		if (!ServicesUtil.isEmpty(fromDto.getField()))
			entity.setField(fromDto.getField());
		if (!ServicesUtil.isEmpty(fromDto.getAlarmSeverity()))
			entity.setAlarmSeverity(fromDto.getAlarmSeverity());
		if (!ServicesUtil.isEmpty(fromDto.getUnacknowledged()))
			entity.setUnacknowledged(fromDto.getUnacknowledged());
		if (!ServicesUtil.isEmpty(fromDto.getAcknowledged()))
			entity.setAcknowledged(fromDto.getAcknowledged());
		if (!ServicesUtil.isEmpty(fromDto.getSuppressed()))
			entity.setSuppressed(fromDto.getSuppressed());
		if (!ServicesUtil.isEmpty(fromDto.getDownTimeClassifier()))
			entity.setDownTimeClassifier(fromDto.getDownTimeClassifier());
		if (!ServicesUtil.isEmpty(fromDto.getFacDescription()))
			entity.setFacDescription(fromDto.getFacDescription());
		if (!ServicesUtil.isEmpty(fromDto.getFacility()))
			entity.setFacility(fromDto.getFacility());
		if (!ServicesUtil.isEmpty(fromDto.getMuwi()))
			entity.setMuwi(fromDto.getMuwi());
		if (!ServicesUtil.isEmpty(fromDto.getIsAcknowledge()))
			entity.setIsAcknowledge(fromDto.getIsAcknowledge());
		if (!ServicesUtil.isEmpty(fromDto.getIsDispatch()))
			entity.setIsDispatch(fromDto.getIsDispatch());
		if (!ServicesUtil.isEmpty(fromDto.getIsDesignate()))
			entity.setIsDesignate(fromDto.getIsDesignate());
		if (!ServicesUtil.isEmpty(fromDto.getRoute()))
			entity.setRoute(fromDto.getRoute());
		if (!ServicesUtil.isEmpty(fromDto.getTier()))
			entity.setTier(fromDto.getTier());
		if (!ServicesUtil.isEmpty(fromDto.getLongDescription()))
			entity.setLongDescription(fromDto.getLongDescription());
		if (!ServicesUtil.isEmpty(fromDto.getHidden()))
			entity.setHidden(fromDto.getHidden());
		return entity;
	}

	@Override
	protected CygnetAlarmFeedDto exportDto(CygnetAlarmFeedDo entity) {
		CygnetAlarmFeedDto dto = new CygnetAlarmFeedDto();
		if (!ServicesUtil.isEmpty(entity.getPointId()))
			dto.setPointId(entity.getPointId());
		if (!ServicesUtil.isEmpty(entity.getAlarmCondition()))
			dto.setAlarmCondition(entity.getAlarmCondition());
		if (!ServicesUtil.isEmpty(entity.getAlarmValue()))
			dto.setAlarmValue(entity.getAlarmValue());
		if (!ServicesUtil.isEmpty(entity.getTimeStamp()))
			dto.setTimeStamp(entity.getTimeStamp());
		if (!ServicesUtil.isEmpty(entity.getTag()))
			dto.setTag(entity.getTag());
		if (!ServicesUtil.isEmpty(entity.getField()))
			dto.setField(entity.getField());
		if (!ServicesUtil.isEmpty(entity.getAlarmSeverity()))
			dto.setAlarmSeverity(entity.getAlarmSeverity());
		if (!ServicesUtil.isEmpty(entity.getUnacknowledged()))
			dto.setUnacknowledged(entity.getUnacknowledged());
		if (!ServicesUtil.isEmpty(entity.getAcknowledged()))
			dto.setAcknowledged(entity.getAcknowledged());
		if (!ServicesUtil.isEmpty(entity.getSuppressed()))
			dto.setSuppressed(entity.getSuppressed());
		if (!ServicesUtil.isEmpty(entity.getDownTimeClassifier()))
			dto.setDownTimeClassifier(entity.getDownTimeClassifier());
		if (!ServicesUtil.isEmpty(entity.getFacDescription()))
			dto.setFacDescription(entity.getFacDescription());
		if (!ServicesUtil.isEmpty(entity.getFacility()))
			dto.setFacility(entity.getFacility());
		if (!ServicesUtil.isEmpty(entity.getMuwi()))
			dto.setMuwi(entity.getMuwi());
		if (!ServicesUtil.isEmpty(entity.getIsAcknowledge()))
			dto.setIsAcknowledge(entity.getIsAcknowledge());
		if (!ServicesUtil.isEmpty(entity.getIsDispatch()))
			dto.setIsDispatch(entity.getIsDispatch());
		if (!ServicesUtil.isEmpty(entity.getIsDesignate()))
			dto.setIsDesignate(entity.getIsDesignate());
		if (!ServicesUtil.isEmpty(entity.getRoute()))
			dto.setRoute(entity.getRoute());
		if (!ServicesUtil.isEmpty(entity.getTier()))
			dto.setTier(entity.getTier());
		if (!ServicesUtil.isEmpty(entity.getLongDescription()))
			dto.setLongDescription(entity.getLongDescription());
		if (!ServicesUtil.isEmpty(entity.getHidden()))
			dto.setHidden(entity.getHidden());
		return dto;
	}

	@SuppressWarnings("unchecked")
	public CygnetAlarmFeedResponseDto getAlarmFeeds(CygnetAlarmRequestDto requestDto) {
		CygnetAlarmFeedResponseDto responseDto = new CygnetAlarmFeedResponseDto();
		ResponseMessage message = new ResponseMessage();
		message.setStatus(MurphyConstant.SUCCESS);
		message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		message.setMessage("Request is Empty");
		DateFormat df = null;

		List<CygnetAlarmFeedDto> alarmFeedDtos = null;
		CygnetAlarmFeedDto alarmFeedDto = null;
		DowntimeCaptureHistoryDto captureHistoryDto = null;

		String locations = null;
		String locationType = null;
		//
		String queryString = null;
		String countQueryString = null;
		//
		// String columnQuery = null;
		//// String columnQueryUnion = null;
		String countColumnQuery = null;
		//
		// String genericQuery = null;
		// String commonQuery = null;
		// String endQuery = null;
		// String facilityEndQuery = null;
		//
		//// String facilityHierarchyQuery = null;
		//// String hierarchyQuery = null;
		//
		// String wellQuery = null;
		// String acknowledgeQuery = null;
		// String historyJoinQuery = null;
		// String orderQuery = null;
		//
		Query query = null;
		Session session = null;
		// Boolean well = true;
		// Boolean validForQuery = true;
		Integer countResult = 0;
		List<Object[]> resultList = null;
		Date currentTime = null;

		if (!ServicesUtil.isEmpty(requestDto)) {

			session = this.getSession();
			// well = requestDto.Well();

			locationType = requestDto.getLocationType();
			locations = requestDto.getLocations();

			// columnQuery = "SELECT AF.POINT_ID, DCH.DOWNTIME_CLASSIFIER,
			// AF.FACILITY, AF.MUWI, AF.TIME_STAMP, AF.FAC_DESCRIPTION,
			// AF.LONG_DESCRIPTION, AF.TIER, AF.ROUTE, AF.ALARM_CONDITION,
			// AF.ALARM_SEVERITY, AF.VALUE, AF.IS_ACKNOWLEDGE, AF.IS_DESIGNATE,
			// AF.ACKNOWLEDGED, AF.UNACKNOWLEDGED, AF.IS_DISPATCH, AF.FIELD,
			// AF.TAG, LC.LATITUDE, LC.LONGITUDE, PL.LOCATION_CODE,
			// PL.LOCATION_TYPE ";
			//// columnQueryUnion = "SELECT AF.POINT_ID, AF.DOWNTIME_CLASSIFIER,
			// AF.FACILITY, AF.MUWI, AF.TIME_STAMP, AF.FAC_DESCRIPTION,
			// AF.LONG_DESCRIPTION, AF.TIER, AF.ROUTE, AF.ALARM_CONDITION,
			// AF.ALARM_SEVERITY, AF.VALUE, AF.IS_ACKNOWLEDGE, AF.IS_DESIGNATE,
			// AF.ACKNOWLEDGED, AF.UNACKNOWLEDGED, AF.IS_DISPATCH, AF.FIELD,
			// AF.TAG ";
			countColumnQuery = " SELECT COUNT(*) AS COUNT FROM ";
			// wellQuery = " AND (AF.MUWI <> '' OR AF.MUWI IS NOT NULL) ";
			// acknowledgeQuery = " AND ((AF.ACKNOWLEDGED = 'Y' AND
			// AF.UNACKNOWLEDGED = 'Y' AND AF.IS_ACKNOWLEDGE = 'Y' AND
			// AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N') OR (AF.ACKNOWLEDGED =
			// 'Y' AND AF.UNACKNOWLEDGED = 'Y' AND AF.IS_ACKNOWLEDGE = 'N' AND
			// AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N') OR (AF.ACKNOWLEDGED =
			// 'Y' AND AF.UNACKNOWLEDGED = 'N' AND AF.IS_ACKNOWLEDGE = 'Y' AND
			// AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N') OR (AF.ACKNOWLEDGED =
			// 'N' AND AF.UNACKNOWLEDGED = 'N' AND AF.IS_ACKNOWLEDGE = 'Y' AND
			// AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N') OR (AF.ACKNOWLEDGED =
			// 'Y' AND AF.UNACKNOWLEDGED = 'N' AND AF.IS_ACKNOWLEDGE = 'N' AND
			// AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N') OR (AF.ACKNOWLEDGED =
			// 'N' AND AF.UNACKNOWLEDGED = 'Y' AND AF.IS_ACKNOWLEDGE = 'Y' AND
			// AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N')) ";
			// historyJoinQuery = " LEFT OUTER JOIN TM_DOWNTIME_CAPTURE_HISTORY
			// DCH ON AF.LONG_DESCRIPTION = DCH.LONG_DESCRIPTION AND
			// CONCAT(AF.LONG_DESCRIPTION, CONCAT(' | ', AF.ALARM_CONDITION)) =
			// DCH.ALARM_CONDITION ";
			//// hierarchyQuery = " JOIN JSA_HIERARCHY JH ON JH.MUWI = AF.MUWI
			// ";
			//// hierarchyQuery = "";
			//// facilityHierarchyQuery = " JOIN JSA_HIERARCHY JH ON JH.FIELD =
			// AF.FIELD ";
			// orderQuery = " ORDER BY AF.TIME_STAMP DESC ";
			//
			// switch (locationType) {
			//
			// case (MurphyConstant.FIELD) :
			//// endQuery = " WHERE JH.FIELD IN (" + locations + ") ";
			// endQuery = " LEFT JOIN WELL_MUWI WM ON AF.MUWI = WM.MUWI JOIN
			// PRODUCTION_LOCATION PL ON WM.LOCATION_CODE = PL.LOCATION_CODE
			// LEFT JOIN LOCATION_COORDINATE LC ON WM.LOCATION_CODE =
			// LC.LOCATION_CODE WHERE AF.MUWI IN (SELECT DISTINCT(WM.MUWI) FROM
			// PRODUCTION_LOCATION P1 JOIN PRODUCTION_LOCATION P2 ON
			// P1.PARENT_CODE = P2.LOCATION_CODE JOIN PRODUCTION_LOCATION P3 ON
			// P2.PARENT_CODE = P3.LOCATION_CODE JOIN PRODUCTION_LOCATION P4 ON
			// P3.PARENT_CODE = P4.LOCATION_CODE JOIN WELL_MUWI WM ON
			// WM.LOCATION_CODE = P1.LOCATION_CODE WHERE P4.LOCATION_CODE IN ("
			// + locations + ")) ";
			//// facilityEndQuery = " WHERE AF.FACILITY IN (SELECT
			// DISTINCT(FM.FACILITY_ID) FROM FACILITY_MAPPING FM JOIN
			// JSA_HIERARCHY J ON FM.FACILITY_NAME = J.FACILITY WHERE J.FIELD IN
			// (" + locations + ")) ";
			// facilityEndQuery = " LEFT JOIN FACILITY_MAPPING FM ON AF.FACILITY
			// = FM.FACILITY_ID JOIN PRODUCTION_LOCATION PL ON FM.FACILITY_CODE
			// = PL.LOCATION_CODE LEFT JOIN LOCATION_COORDINATE LC ON
			// FM.FACILITY_CODE = LC.LOCATION_CODE WHERE AF.FACILITY IN (SELECT
			// DISTINCT(FACILITY_ID) FROM FACILITY_MAPPING WHERE FIELD_CODE IN
			// (" + locations + ")) ";
			//// facilityHierarchyQuery = " WHERE AF.FACILITY IN (SELECT
			// DISTINCT JH.FACILITY FROM JSA_HIERARCHY JH WHERE JH.FIELD IN (" +
			// locations + ")) ";
			// break;
			// case (MurphyConstant.FACILITY) :
			//// endQuery = " WHERE JH.FACILITY IN (" + locations + ") ";
			// endQuery = " LEFT JOIN WELL_MUWI WM ON AF.MUWI = WM.MUWI JOIN
			// PRODUCTION_LOCATION PL ON WM.LOCATION_CODE = PL.LOCATION_CODE
			// LEFT JOIN LOCATION_COORDINATE LC ON WM.LOCATION_CODE =
			// LC.LOCATION_CODE WHERE AF.MUWI IN (SELECT DISTINCT(WM.MUWI) FROM
			// PRODUCTION_LOCATION P1 JOIN PRODUCTION_LOCATION P2 ON
			// P1.PARENT_CODE = P2.LOCATION_CODE JOIN PRODUCTION_LOCATION P3 ON
			// P2.PARENT_CODE = P3.LOCATION_CODE JOIN WELL_MUWI WM ON
			// WM.LOCATION_CODE = P1.LOCATION_CODE WHERE P3.LOCATION_CODE IN ("
			// + locations + ")) ";
			//// facilityEndQuery = " WHERE AF.FACILITY IN (SELECT
			// DISTINCT(FM.FACILITY_ID) FROM FACILITY_MAPPING FM WHERE
			// FM.FACILITY_NAME IN (" + locations + ")) ";
			// facilityEndQuery = " LEFT JOIN FACILITY_MAPPING FM ON AF.FACILITY
			// = FM.FACILITY_ID JOIN PRODUCTION_LOCATION PL ON FM.FACILITY_CODE
			// = PL.LOCATION_CODE LEFT JOIN LOCATION_COORDINATE LC ON
			// FM.FACILITY_CODE = LC.LOCATION_CODE WHERE AF.FACILITY IN (SELECT
			// DISTINCT(FACILITY_ID) FROM FACILITY_MAPPING WHERE FACILITY_CODE
			// IN (" + locations + "))";
			//// facilityHierarchyQuery = " WHERE AF.FACILITY IN (" + locations
			// + ") ";
			// break;
			// case (MurphyConstant.WELLPAD) :
			//// endQuery = " WHERE JH.WELLPAD IN(" + locations + ") ";
			// endQuery = " JOIN WELL_MUWI WM ON WM.MUWI = AF.MUWI JOIN
			// PRODUCTION_LOCATION PL ON WM.LOCATION_CODE = PL.LOCATION_CODE
			// LEFT JOIN LOCATION_COORDINATE LC ON PL.LOCATION_CODE =
			// LC.LOCATION_CODE WHERE PL.LOCATION_CODE IN (SELECT
			// DISTINCT(LOCATION_CODE) FROM PRODUCTION_LOCATION WHERE
			// PARENT_CODE IN (" + locations + ")) ";
			// break;
			// case (MurphyConstant.WELL) :
			//// endQuery = " WHERE JH.WELL IN(" + locations + ") ";
			// endQuery = " JOIN WELL_MUWI WM ON WM.MUWI = AF.MUWI JOIN
			// PRODUCTION_LOCATION PL ON WM.LOCATION_CODE = PL.LOCATION_CODE
			// LEFT JOIN LOCATION_COORDINATE LC ON WM.LOCATION_CODE =
			// LC.LOCATION_CODE WHERE WM.LOCATION_CODE IN (" + locations + ") ";
			// break;
			// default :
			// validForQuery = false;
			// break;
			//
			// }
			//
			// if(well == false) {
			// wellQuery = " AND (AF.MUWI = '' OR AF.MUWI IS NULL) ";
			//// hierarchyQuery = facilityHierarchyQuery;
			// commonQuery = " FROM TM_CYGNET_ALARM_FEED AF " + historyJoinQuery
			// ;
			//// + hierarchyQuery +"";
			//// commonQuery = " FROM TM_CYGNET_ALARM_FEED AF " + hierarchyQuery
			// +"";
			// } else if (well == true) {
			// commonQuery = " FROM TM_CYGNET_ALARM_FEED AF " + historyJoinQuery
			// /*+ hierarchyQuery */ + endQuery +"";
			//// commonQuery = " FROM TM_CYGNET_ALARM_FEED AF " + hierarchyQuery
			// + endQuery +"";
			// }
			//
			// if(!requestDto.Acknowledged()) {
			// acknowledgeQuery = " AND ((AF.ACKNOWLEDGED = 'N' AND
			// AF.UNACKNOWLEDGED = 'N' AND AF.IS_ACKNOWLEDGE = 'N' AND
			// AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N') OR (AF.ACKNOWLEDGED =
			// 'N' AND AF.UNACKNOWLEDGED = 'Y' AND AF.IS_ACKNOWLEDGE = 'N' AND
			// AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N')) ";
			// }
			//
			// genericQuery = commonQuery + acknowledgeQuery + wellQuery;
			//
			// queryString = columnQuery + genericQuery ;
			//
			// // Adding Facility Alarms to the main Fetch List
			// {
			// // Union of Facility Alarms if the locationType selected is
			// either Field or Facility
			// if(locationType.equals(MurphyConstant.FIELD) ||
			// locationType.equals(MurphyConstant.FACILITY)) {
			// wellQuery = " AND (AF.MUWI = '' OR AF.MUWI IS NULL) ";
			// commonQuery = " FROM TM_CYGNET_ALARM_FEED AF ";
			// queryString += " UNION " + columnQuery + commonQuery +
			// historyJoinQuery + facilityEndQuery + acknowledgeQuery +
			// wellQuery ;
			// }
			// }
			//
			queryString = formAlarmQuery(locations, locationType, requestDto.Acknowledged());
			countQueryString = countColumnQuery + "(" + queryString + ")";
			// if(validForQuery)
			{
				query = session.createSQLQuery(countQueryString);
				countResult = ((BigInteger) query.uniqueResult()).intValue();
				// queryString=formAlarmQuery(locations, locationType);
				if (!ServicesUtil.isEmpty(requestDto.getPage()) && requestDto.getPage() > 0) {
					int first = (requestDto.getPage() - 1) * MurphyConstant.PAGE_SIZE;
					int last = MurphyConstant.PAGE_SIZE;
					queryString += " LIMIT " + last + " OFFSET " + first + "";
				}
				logger.error("[CygnetAlarmFeedDao][queryString] : " + queryString);
				query = session.createSQLQuery(queryString);
				resultList = query.list();
			}

			// Fetch LocationTimeZone Map
			Map<String, LocationTimeZoneMasterDto> timeZoneMasterMap = locationMasterTimeZoneDao
					.fetchTimeZoneByLocation();

			if (!ServicesUtil.isEmpty(resultList)) {
				alarmFeedDtos = new ArrayList<CygnetAlarmFeedDto>();
				for (Object[] obj : resultList) {
					try {
						df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						// df.setTimeZone(TimeZone.getTimeZone("CST"));
						currentTime = df.parse(df.format(new Date()));
						/*
						 * Calendar calender = Calendar.getInstance();
						 * calender.setTimeZone(TimeZone.getTimeZone(
						 * MurphyConstant.CST_ZONE));
						 * currentTime=calender.getTime();
						 * currentTime=ServicesUtil.convertFromZoneToZone(
						 * currentTime, null, MurphyConstant.CST_ZONE,
						 * MurphyConstant.UTC_ZONE,
						 * MurphyConstant.DATE_DB_FORMATE_SD,
						 * MurphyConstant.DATE_DB_FORMATE_SD);
						 */
					} catch (ParseException e) {
						logger.error("[CygnetAlarmFeedDao][getAlarmFeeds][ParseException] : " + e.getMessage());
					}
					alarmFeedDto = new CygnetAlarmFeedDto();
					if (!ServicesUtil.isEmpty(timeZoneMasterMap)) {
						String locationCode = (String) obj[18];
						if (!ServicesUtil.isEmpty(locationCode)) {
							LocationTimeZoneMasterDto masterTimeZoneDto = timeZoneMasterMap
									.get(locationCode.substring(0, 10));
							alarmFeedDto.setTimeZone(masterTimeZoneDto.getTimeZone());
						} else {
							alarmFeedDto.setTimeZone(MurphyConstant.CST_ZONE);
						}
					}
					alarmFeedDto.setPointId((String) obj[1]);
					alarmFeedDto.setDownTimeClassifier((String) obj[17]);
					alarmFeedDto.setFacility((String) obj[2]);
					alarmFeedDto.setMuwi((String) obj[3]);
					alarmFeedDto.setTimeStamp(ServicesUtil.isEmpty(obj[4]) ? null
							: ServicesUtil.convertFromZoneToZone(null, obj[4], MurphyConstant.CST_ZONE,
									MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
									MurphyConstant.DATE_DISPLAY_FORMAT));
					alarmFeedDto.setTimeStampAsString(ServicesUtil.isEmpty(obj[4]) ? null
							: ServicesUtil.convertFromZoneToZoneString(null, obj[4], MurphyConstant.CST_ZONE,
									MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
									MurphyConstant.DATE_DISPLAY_FORMAT));
					alarmFeedDto.setFacDescription((String) obj[5]);
					alarmFeedDto.setLongDescription((String) obj[6]);
					alarmFeedDto.setTier((String) obj[7]);
					alarmFeedDto.setRoute((String) obj[8]);
					alarmFeedDto.setAlarmCondition((String) obj[9]);
					alarmFeedDto.setAlarmSeverity((String) obj[10]);
					alarmFeedDto.setAlarmValue((String) obj[11]);
					alarmFeedDto.setIsAcknowledge((String) obj[14]);
					alarmFeedDto.setIsDesignate((String) obj[16]);
					alarmFeedDto.setAcknowledged((String) obj[12]);
					alarmFeedDto.setUnacknowledged((String) obj[13]);
					alarmFeedDto.setIsDispatch((String) obj[15]);
					// alarmFeedDto.setField((String) obj[17]);
					// alarmFeedDto.setTag((String) obj[18]);
					alarmFeedDto
							.setLatitude(ServicesUtil.isEmpty(obj[20]) ? null : ServicesUtil.getBigDecimal(obj[20]));
					alarmFeedDto
							.setLongitude(ServicesUtil.isEmpty(obj[21]) ? null : ServicesUtil.getBigDecimal(obj[21]));
					alarmFeedDto.setLocationCode((String) obj[18]);
					alarmFeedDto.setLocationType((String) obj[19]);
					if (alarmFeedDto.getAlarmSeverity().equals(MurphyConstant.ALARM_SEVERITY_1_ALARM)
							&& !ServicesUtil.isEmpty(alarmFeedDto.getTimeStamp())
							&& !ServicesUtil.isEmpty(currentTime)) {
						try {
							alarmFeedDto.setCygnetRecomendedTime(ServicesUtil.getDurationBreakdown(
									currentTime.getTime() - alarmFeedDto.getTimeStamp().getTime()));
						} catch (Exception ex) {
							logger.error("[CygnetAlarmFeedDao][getAlarmFeeds]Current time: " + currentTime.getTime()
									+ " alarmFeedDto.getTimeStamp().getTime(): "
									+ alarmFeedDto.getTimeStamp().getTime());
							logger.error(
									"[CygnetAlarmFeedDao][getAlarmFeeds][Exception] Date/Time : 24/10/2018 08:07 PM");
							logger.error("[CygnetAlarmFeedDao][getAlarmFeeds][Exception]DTO : " + alarmFeedDto);
						}
					}

					if (ServicesUtil.isEmpty(alarmFeedDto.getDownTimeClassifier())) {
						alarmFeedDto.setDownTimeClassifier(MurphyConstant.ALARM_NON_DOWNTIME);
					}

					{
						if ((!ServicesUtil.isEmpty(alarmFeedDto))) {
							captureHistoryDto = new DowntimeCaptureHistoryDto();
							captureHistoryDto.setAlarmCondition(alarmFeedDto.getAlarmCondition());
							captureHistoryDto.setPointId(alarmFeedDto.getPointId());
							captureHistoryDto.setDownTimeClassifier(alarmFeedDto.getDownTimeClassifier());
							captureHistoryDto.setLongDescription(alarmFeedDto.getLongDescription());

							// if(ServicesUtil.isEmpty(captureHistoryDto.getDownTimeClassifier()))
							// {
							//
							captureHistoryDto.setDownTimeClassifier(MurphyConstant.ALARM_NON_DOWNTIME);
							// }

							downtimeCaptureDao.insertIntoHistory(captureHistoryDto);
						}
					}

					alarmFeedDtos.add(alarmFeedDto);
				}
				message.setMessage("Alarm feed fetched successfully");
			} else {
				message.setMessage("No Alarms Found");
			}

		}
		responseDto.setPageCount(new BigDecimal(MurphyConstant.PAGE_SIZE));
		responseDto.setTotalCount(new BigDecimal(countResult));
		responseDto.setAlarmsList(alarmFeedDtos);
		responseDto.setResponseMessage(message);
		return responseDto;
	}

	/*
	 * @SuppressWarnings("unchecked") public CygnetAlarmFeedResponseDto
	 * getAlarmFeeds(CygnetAlarmRequestDto requestDto) {
	 * CygnetAlarmFeedResponseDto responseDto = new
	 * CygnetAlarmFeedResponseDto(); ResponseMessage message = new
	 * ResponseMessage(); message.setStatus(MurphyConstant.SUCCESS);
	 * message.setStatusCode(MurphyConstant.CODE_SUCCESS);
	 * message.setMessage("Request is Empty"); DateFormat df = null;
	 * List<CygnetAlarmFeedDto> alarmFeedDtos = null; CygnetAlarmFeedDto
	 * alarmFeedDto = null; DowntimeCaptureHistoryDto captureHistoryDto = null;
	 * String locations = null; String locationType = null; String queryString =
	 * null; String countQueryString = null; String columnQuery = null; //
	 * String columnQueryUnion = null; String countColumnQuery = null; String
	 * genericQuery = null; String commonQuery = null; String endQuery = null;
	 * String facilityEndQuery = null; // String facilityHierarchyQuery = null;
	 * // String hierarchyQuery = null; String wellQuery = null; String
	 * acknowledgeQuery = null; String historyJoinQuery = null; String
	 * orderQuery = null; Query query = null; Session session = null; Boolean
	 * well = true; Boolean validForQuery = true; Integer countResult = 0;
	 * List<Object[]> resultList = null; Date currentTime = null;
	 * 
	 * if (!ServicesUtil.isEmpty(requestDto)) { session = this.getSession(); //
	 * well = requestDto.Well();
	 * 
	 * locationType = requestDto.getLocationType(); locations =
	 * requestDto.getLocations();
	 * 
	 * columnQuery =
	 * "SELECT AF.POINT_ID, DCH.DOWNTIME_CLASSIFIER, AF.FACILITY, AF.MUWI, AF.TIME_STAMP, AF.FAC_DESCRIPTION, AF.LONG_DESCRIPTION, AF.TIER, AF.ROUTE, AF.ALARM_CONDITION, AF.ALARM_SEVERITY, AF.VALUE, AF.IS_ACKNOWLEDGE, AF.IS_DESIGNATE, AF.ACKNOWLEDGED, AF.UNACKNOWLEDGED, AF.IS_DISPATCH, AF.FIELD, AF.TAG, LC.LATITUDE, LC.LONGITUDE, PL.LOCATION_CODE, PL.LOCATION_TYPE "
	 * ; // columnQueryUnion = "SELECT AF.POINT_ID, AF.DOWNTIME_CLASSIFIER, //
	 * AF.FACILITY, AF.MUWI, AF.TIME_STAMP, AF.FAC_DESCRIPTION, //
	 * AF.LONG_DESCRIPTION, AF.TIER, AF.ROUTE, AF.ALARM_CONDITION, //
	 * AF.ALARM_SEVERITY, AF.VALUE, AF.IS_ACKNOWLEDGE, AF.IS_DESIGNATE, //
	 * AF.ACKNOWLEDGED, AF.UNACKNOWLEDGED, AF.IS_DISPATCH, AF.FIELD, // AF.TAG
	 * "; countColumnQuery = " SELECT COUNT(*) AS COUNT FROM "; wellQuery =
	 * " AND (AF.MUWI <> '' OR AF.MUWI IS NOT NULL) "; acknowledgeQuery =
	 * " AND ((AF.ACKNOWLEDGED = 'Y' AND AF.UNACKNOWLEDGED = 'Y' AND AF.IS_ACKNOWLEDGE = 'Y' AND AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N') OR (AF.ACKNOWLEDGED = 'Y' AND AF.UNACKNOWLEDGED = 'Y' AND AF.IS_ACKNOWLEDGE = 'N' AND AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N') OR (AF.ACKNOWLEDGED = 'Y' AND AF.UNACKNOWLEDGED = 'N' AND AF.IS_ACKNOWLEDGE = 'Y' AND AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N') OR (AF.ACKNOWLEDGED = 'N' AND AF.UNACKNOWLEDGED = 'N' AND AF.IS_ACKNOWLEDGE = 'Y' AND AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N') OR (AF.ACKNOWLEDGED = 'Y' AND AF.UNACKNOWLEDGED = 'N' AND AF.IS_ACKNOWLEDGE = 'N' AND AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N') OR (AF.ACKNOWLEDGED = 'N' AND AF.UNACKNOWLEDGED = 'Y' AND AF.IS_ACKNOWLEDGE = 'Y' AND AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N')) "
	 * ; historyJoinQuery =
	 * " LEFT OUTER JOIN TM_DOWNTIME_CAPTURE_HISTORY DCH ON AF.LONG_DESCRIPTION = DCH.LONG_DESCRIPTION AND CONCAT(AF.LONG_DESCRIPTION, CONCAT(' | ', AF.ALARM_CONDITION)) = DCH.ALARM_CONDITION "
	 * ; // hierarchyQuery = " JOIN JSA_HIERARCHY JH ON JH.MUWI = AF.MUWI "; //
	 * hierarchyQuery = ""; // facilityHierarchyQuery = " JOIN JSA_HIERARCHY JH
	 * ON JH.FIELD = // AF.FIELD "; orderQuery =
	 * " ORDER BY AF.TIME_STAMP DESC "; switch (locationType) { case
	 * (MurphyConstant.FIELD): // endQuery = " WHERE JH.FIELD IN (" + locations
	 * + ") "; endQuery =
	 * " LEFT JOIN WELL_MUWI WM ON AF.MUWI = WM.MUWI JOIN PRODUCTION_LOCATION PL ON WM.LOCATION_CODE = PL.LOCATION_CODE LEFT JOIN LOCATION_COORDINATE LC ON WM.LOCATION_CODE = LC.LOCATION_CODE WHERE AF.MUWI IN (SELECT DISTINCT(WM.MUWI) FROM PRODUCTION_LOCATION P1 JOIN PRODUCTION_LOCATION P2 ON P1.PARENT_CODE = P2.LOCATION_CODE JOIN PRODUCTION_LOCATION P3 ON P2.PARENT_CODE = P3.LOCATION_CODE JOIN PRODUCTION_LOCATION P4 ON P3.PARENT_CODE = P4.LOCATION_CODE JOIN WELL_MUWI WM ON WM.LOCATION_CODE = P1.LOCATION_CODE WHERE P4.LOCATION_CODE IN ("
	 * + locations + ")) "; // facilityEndQuery = " WHERE AF.FACILITY IN (SELECT
	 * // DISTINCT(FM.FACILITY_ID) FROM FACILITY_MAPPING FM JOIN //
	 * JSA_HIERARCHY J ON FM.FACILITY_NAME = J.FACILITY WHERE // J.FIELD IN
	 * (" + locations + ")) "; facilityEndQuery =
	 * " LEFT JOIN FACILITY_MAPPING FM ON AF.FACILITY = FM.FACILITY_ID JOIN PRODUCTION_LOCATION PL ON FM.FACILITY_CODE = PL.LOCATION_CODE LEFT JOIN LOCATION_COORDINATE LC ON FM.FACILITY_CODE = LC.LOCATION_CODE WHERE AF.FACILITY IN (SELECT DISTINCT(FACILITY_ID) FROM FACILITY_MAPPING WHERE FIELD_CODE IN ("
	 * + locations + ")) "; // facilityHierarchyQuery = " WHERE AF.FACILITY IN
	 * (SELECT // DISTINCT JH.FACILITY FROM JSA_HIERARCHY JH WHERE JH.FIELD IN
	 * // (" + locations + ")) "; break; case (MurphyConstant.FACILITY): //
	 * endQuery = " WHERE JH.FACILITY IN (" + locations + ") "; endQuery =
	 * " LEFT JOIN WELL_MUWI WM ON AF.MUWI = WM.MUWI JOIN PRODUCTION_LOCATION PL ON WM.LOCATION_CODE = PL.LOCATION_CODE LEFT JOIN LOCATION_COORDINATE LC ON WM.LOCATION_CODE = LC.LOCATION_CODE WHERE AF.MUWI IN (SELECT DISTINCT(WM.MUWI) FROM PRODUCTION_LOCATION P1 JOIN PRODUCTION_LOCATION P2 ON P1.PARENT_CODE = P2.LOCATION_CODE JOIN PRODUCTION_LOCATION P3 ON P2.PARENT_CODE = P3.LOCATION_CODE JOIN WELL_MUWI WM ON WM.LOCATION_CODE = P1.LOCATION_CODE WHERE P3.LOCATION_CODE IN ("
	 * + locations + ")) "; // facilityEndQuery = " WHERE AF.FACILITY IN (SELECT
	 * // DISTINCT(FM.FACILITY_ID) FROM FACILITY_MAPPING FM WHERE //
	 * FM.FACILITY_NAME IN (" + locations + ")) "; facilityEndQuery =
	 * " LEFT JOIN FACILITY_MAPPING FM ON AF.FACILITY = FM.FACILITY_ID JOIN PRODUCTION_LOCATION PL ON FM.FACILITY_CODE = PL.LOCATION_CODE LEFT JOIN LOCATION_COORDINATE LC ON FM.FACILITY_CODE = LC.LOCATION_CODE WHERE AF.FACILITY IN (SELECT DISTINCT(FACILITY_ID) FROM FACILITY_MAPPING WHERE FACILITY_CODE IN ("
	 * + locations + "))"; // facilityHierarchyQuery = " WHERE AF.FACILITY IN ("
	 * + // locations + ") "; break; case (MurphyConstant.WELLPAD): // endQuery
	 * = " WHERE JH.WELLPAD IN(" + locations + ") "; endQuery =
	 * " JOIN WELL_MUWI WM ON WM.MUWI = AF.MUWI JOIN PRODUCTION_LOCATION PL ON WM.LOCATION_CODE = PL.LOCATION_CODE LEFT JOIN LOCATION_COORDINATE LC ON PL.LOCATION_CODE = LC.LOCATION_CODE WHERE PL.LOCATION_CODE IN (SELECT DISTINCT(LOCATION_CODE) FROM PRODUCTION_LOCATION WHERE PARENT_CODE IN ("
	 * + locations + ")) "; break; case (MurphyConstant.WELL): // endQuery =
	 * " WHERE JH.WELL IN(" + locations + ") "; endQuery =
	 * " JOIN WELL_MUWI WM ON WM.MUWI = AF.MUWI JOIN PRODUCTION_LOCATION PL ON WM.LOCATION_CODE = PL.LOCATION_CODE LEFT JOIN LOCATION_COORDINATE LC ON WM.LOCATION_CODE = LC.LOCATION_CODE WHERE WM.LOCATION_CODE IN ("
	 * + locations + ")  "; break; default: validForQuery = false; break; }
	 * 
	 * if (well == false) { wellQuery =
	 * " AND (AF.MUWI = '' OR AF.MUWI IS NULL) "; // hierarchyQuery =
	 * facilityHierarchyQuery; commonQuery = " FROM TM_CYGNET_ALARM_FEED AF " +
	 * historyJoinQuery; // + hierarchyQuery +""; // commonQuery =
	 * " FROM TM_CYGNET_ALARM_FEED AF " + // hierarchyQuery +""; } else if (well
	 * == true) { commonQuery = " FROM TM_CYGNET_ALARM_FEED AF " +
	 * historyJoinQuery + hierarchyQuery + endQuery + ""; // commonQuery =
	 * " FROM TM_CYGNET_ALARM_FEED AF " + // hierarchyQuery + endQuery +""; }
	 * 
	 * if (!requestDto.Acknowledged()) { acknowledgeQuery =
	 * " AND ((AF.ACKNOWLEDGED = 'N' AND AF.UNACKNOWLEDGED = 'N' AND AF.IS_ACKNOWLEDGE = 'N' AND AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N') OR (AF.ACKNOWLEDGED = 'N' AND AF.UNACKNOWLEDGED = 'Y' AND AF.IS_ACKNOWLEDGE = 'N' AND AF.SUPPRESSED = 'N' AND AF.HIDDEN = 'N')) "
	 * ; } genericQuery = commonQuery + acknowledgeQuery + wellQuery;
	 * 
	 * queryString = columnQuery + genericQuery; // Adding Facility Alarms to
	 * the main Fetch List { // Union of Facility Alarms if the locationType
	 * selected is // either Field or Facility if
	 * (locationType.equals(MurphyConstant.FIELD) ||
	 * locationType.equals(MurphyConstant.FACILITY)) { wellQuery =
	 * " AND (AF.MUWI = '' OR AF.MUWI IS NULL) "; commonQuery =
	 * " FROM TM_CYGNET_ALARM_FEED AF "; queryString += " UNION " + columnQuery
	 * + commonQuery + historyJoinQuery + facilityEndQuery + acknowledgeQuery +
	 * wellQuery; } } countQueryString = countColumnQuery + "(" + queryString +
	 * ")"; queryString += orderQuery; if (validForQuery) { query =
	 * session.createSQLQuery(countQueryString); countResult = ((BigInteger)
	 * query.uniqueResult()).intValue(); if
	 * (!ServicesUtil.isEmpty(requestDto.getPage()) && requestDto.getPage() > 0)
	 * { int first = (requestDto.getPage() - 1) * MurphyConstant.PAGE_SIZE; int
	 * last = MurphyConstant.PAGE_SIZE; queryString += " LIMIT " + last +
	 * " OFFSET " + first + ""; } //
	 * System.err.println("[CygnetAlarmFeedDao][queryString] : //
	 * "+queryString); query = session.createSQLQuery(queryString); resultList =
	 * query.list(); } if (!ServicesUtil.isEmpty(resultList)) { alarmFeedDtos =
	 * new ArrayList<CygnetAlarmFeedDto>(); for (Object[] obj : resultList) {
	 * try { df = new SimpleDateFormat();
	 * df.setTimeZone(TimeZone.getTimeZone("CST")); currentTime =
	 * df.parse(df.format(new Date())); } catch (ParseException e) {
	 * logger.error("[CygnetAlarmFeedDao][getAlarmFeeds][ParseException] : " +
	 * e.getMessage()); } alarmFeedDto = new CygnetAlarmFeedDto();
	 * alarmFeedDto.setPointId((String) obj[0]);
	 * alarmFeedDto.setDownTimeClassifier((String) obj[1]);
	 * alarmFeedDto.setFacility((String) obj[2]); alarmFeedDto.setMuwi((String)
	 * obj[3]); alarmFeedDto.setTimeStamp(ServicesUtil.isEmpty(obj[4]) ? null :
	 * ServicesUtil.convertFromZoneToZone(null, obj[4], MurphyConstant.CST_ZONE,
	 * MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
	 * MurphyConstant.DATE_DISPLAY_FORMAT));
	 * alarmFeedDto.setTimeStampAsString(ServicesUtil.isEmpty(obj[4]) ? null :
	 * ServicesUtil.convertFromZoneToZoneString(null, obj[4],
	 * MurphyConstant.CST_ZONE, MurphyConstant.CST_ZONE,
	 * MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
	 * alarmFeedDto.setFacDescription((String) obj[5]);
	 * alarmFeedDto.setLongDescription((String) obj[6]);
	 * alarmFeedDto.setTier((String) obj[7]); alarmFeedDto.setRoute((String)
	 * obj[8]); alarmFeedDto.setAlarmCondition((String) obj[9]);
	 * alarmFeedDto.setAlarmSeverity((String) obj[10]);
	 * alarmFeedDto.setAlarmValue((String) obj[11]);
	 * alarmFeedDto.setIsAcknowledge((String) obj[12]);
	 * alarmFeedDto.setIsDesignate((String) obj[13]);
	 * alarmFeedDto.setAcknowledged((String) obj[14]);
	 * alarmFeedDto.setUnacknowledged((String) obj[15]);
	 * alarmFeedDto.setIsDispatch((String) obj[16]);
	 * alarmFeedDto.setField((String) obj[17]); alarmFeedDto.setTag((String)
	 * obj[18]); alarmFeedDto .setLatitude(ServicesUtil.isEmpty(obj[19]) ? null
	 * : ServicesUtil.getBigDecimal(obj[19])); alarmFeedDto
	 * .setLongitude(ServicesUtil.isEmpty(obj[20]) ? null :
	 * ServicesUtil.getBigDecimal(obj[20]));
	 * alarmFeedDto.setLocationCode((String) obj[21]);
	 * alarmFeedDto.setLocationType((String) obj[22]); if
	 * (alarmFeedDto.getAlarmSeverity().equals(MurphyConstant.
	 * ALARM_SEVERITY_1_ALARM) &&
	 * !ServicesUtil.isEmpty(alarmFeedDto.getTimeStamp()) &&
	 * !ServicesUtil.isEmpty(currentTime)) {
	 * alarmFeedDto.setCygnetRecomendedTime(ServicesUtil
	 * .getDurationBreakdown(currentTime.getTime() -
	 * alarmFeedDto.getTimeStamp().getTime())); } if
	 * (ServicesUtil.isEmpty(alarmFeedDto.getDownTimeClassifier())) {
	 * alarmFeedDto.setDownTimeClassifier(MurphyConstant.ALARM_NON_DOWNTIME); }
	 * { if ((!ServicesUtil.isEmpty(alarmFeedDto))) { captureHistoryDto = new
	 * DowntimeCaptureHistoryDto();
	 * captureHistoryDto.setAlarmCondition(alarmFeedDto.getAlarmCondition());
	 * captureHistoryDto.setPointId(alarmFeedDto.getPointId());
	 * captureHistoryDto.setDownTimeClassifier(alarmFeedDto.
	 * getDownTimeClassifier());
	 * captureHistoryDto.setLongDescription(alarmFeedDto.getLongDescription());
	 * // if(ServicesUtil.isEmpty(captureHistoryDto.getDownTimeClassifier())) //
	 * { //
	 * captureHistoryDto.setDownTimeClassifier(MurphyConstant.ALARM_NON_DOWNTIME
	 * ); // } downtimeCaptureDao.insertIntoHistory(captureHistoryDto); } }
	 * alarmFeedDtos.add(alarmFeedDto); }
	 * message.setMessage("Alarm feed fetched successfully"); } else {
	 * message.setMessage("No Alarms Found"); } } responseDto.setPageCount(new
	 * BigDecimal(MurphyConstant.PAGE_SIZE)); responseDto.setTotalCount(new
	 * BigDecimal(countResult)); responseDto.setAlarmsList(alarmFeedDtos);
	 * responseDto.setResponseMessage(message); return responseDto; }
	 */

	public String updateAlarmFeed(String pointIds, String key, String value) {
		String response = MurphyConstant.FAILURE;
		String subQuery = null, updateQuery = null;

		switch (key) {
		case (MurphyConstant.ALARM_ACKNOWLEDGE):
			subQuery = " IS_ACKNOWLEDGED = '" + value + "'";
			break;
		case (MurphyConstant.ALARM_DISPATCH):
			subQuery = " IS_DISPATCHED = '" + value + "'";
			break;
		case (MurphyConstant.ALARM_DESIGNATE):
			subQuery = " IS_DESIGNATED = '" + value + "'";
			break;
		default:
			subQuery = "";
			break;
		}

		if (!ServicesUtil.isEmpty(subQuery)) {
			updateQuery = "UPDATE TM_CYGNET_ALARM_ACTIONS AF SET " + subQuery + " WHERE AF.POINTIDLONG IN (" + pointIds
					+ ")";
			logger.error("updateQueryAck" + updateQuery);

			int updatedRow = this.getSession().createSQLQuery(updateQuery).executeUpdate();

			if (updatedRow > 0) {
				response = MurphyConstant.SUCCESS;
			}

		}
		return response;
	}

	public static void main(String[] args) {

		CygnetAlarmRequestDto dto = new CygnetAlarmRequestDto();
		// List<LocationHierarchyDto> hierarchyDto = new
		// ArrayList<LocationHierarchyDto>();
		// LocationHierarchyDto locationHierarchyDto = new
		// LocationHierarchyDto();
		// locationHierarchyDto.setLocation("CATARINA");
		// LocationHierarchyDto locationHierarchyDto2 = new
		// LocationHierarchyDto();
		// locationHierarchyDto2.setLocation("TILDEN");
		// hierarchyDto.add(locationHierarchyDto);
		// hierarchyDto.add(locationHierarchyDto2);
		dto.setLocationType("FIELD");
		dto.setLocations("'CATARINA', 'TILDEN'");
		dto.setAcknowledged(true);
		dto.setWell(true);

		// CygnetAlarmFeedDao cygnetAlarmFeedDao = new CygnetAlarmFeedDao();

		// cygnetAlarmFeedDao.getAlarmFeeds(dto);

		// cygnetAlarmFeedDao.updateAlarmFeed("pid1", true, "Non Downtime");

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List saveAndUpdateAlarmFeed() {
		List<Object[]> resultList = null;
		try {
			Query q = this.getSession().createSQLQuery(getQueryString().trim());
			resultList = q.list();
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagement][CygnetDataPullSchedular][saveAndUpdateAlarmFeed][error]"
					+ e.getMessage());
		}

		return resultList;
	}

	/*
	 * @Transactional(value = TxType.REQUIRES_NEW) public void
	 * saveAlarmFeedInstance(Object[] obj, CygnetAlarmFeedDo alarmFeedDo) { try
	 * { SetObjectToDo(obj, alarmFeedDo); // logger.error(
	 * "[Murphy][TaskManagement][CygnetDataPullSchedular][saveAndUpdateAlarmFeed][info][success]"
	 * +alarmFeedDo); boolean isExist = fecthPointId(obj, alarmFeedDo); if
	 * (isExist) { this.getSession().update(alarmFeedDo); } else {
	 * alarmFeedDo.setIsAcknowledge("N"); alarmFeedDo.setIsDesignate("N");
	 * alarmFeedDo.setIsDispatch("N"); this.getSession().save(alarmFeedDo); }
	 * 
	 * CygnetDataPullSchedular cygnetDataPullSchedular = null; // logger.error(
	 * "[Murphy][TaskManagement][CygnetDataPullSchedular][saveAndUpdateAlarmFeed][info][success]"
	 * +alarmFeedDo.getPointId());
	 * 
	 * boolean delete = findAlarmSeverityForPointId(alarmFeedDo.getPointId(),
	 * alarmFeedDo.getAlarmSeverity()); if(delete){
	 * updateAlarmFeedActions(alarmFeedDo.getPointId()); }
	 * 
	 * // load alarm for the point id and if its less then update the desig
	 * cygnetDataPullSchedular = new CygnetDataPullSchedular();
	 * cygnetDataPullSchedular.setDowntimeCounter(alarmFeedDo); } catch
	 * (Exception e) { logger.error(
	 * "[Murphy][TaskManagement][CygnetDataPullSchedular][saveAndUpdateAlarmFeed][error]"
	 * + e); }
	 * 
	 * }
	 */

	@SuppressWarnings("unchecked")
	public boolean fecthPointId(Object[] obj, CygnetAlarmFeedDo alarmFeedDo) {

		boolean isPointIdExist = false;
		String pointId = ServicesUtil.isEmpty(obj[0]) ? null : ((String) obj[0]);

		try {
			String queryString = "select af.IS_ACKNOWLEDGE, af.IS_DESIGNATE, af.IS_DISPATCH From TM_CYGNET_ALARM_ACTIONS af "
					+ "where af.POINTIDLONG ='" + pointId + "'";
			Query q = this.getSession().createSQLQuery(queryString.trim());
			List<Object[]> resultList = q.list();
			if (resultList.size() > 0) {
				boolean delete = findAlarmSeverityForPointId(alarmFeedDo.getPointId(), alarmFeedDo.getAlarmSeverity());
				// logger.error("[Murphy][TaskManagement][fecthPointId][delete]"+delete);
				if (!delete) {
					for (Object[] obj1 : resultList) {
						alarmFeedDo.setIsAcknowledge(ServicesUtil.isEmpty(obj1[0]) ? null : ((String) obj1[0]));
						alarmFeedDo.setIsDesignate(ServicesUtil.isEmpty(obj1[1]) ? null : ((String) obj1[1]));
						alarmFeedDo.setIsDispatch(ServicesUtil.isEmpty(obj1[2]) ? null : ((String) obj1[2]));
					}
				} else {
					alarmFeedDo.setIsAcknowledge("N");
					alarmFeedDo.setIsDesignate("N");
					alarmFeedDo.setIsDispatch("N");
				}
				isPointIdExist = true;
			}

		} catch (Exception e) {
			logger.error("Error in cygnet downtime" + e);
		}
		return isPointIdExist;
	}

	public void updateAlarmFeedActions(String pointId) {

		// logger.error("[Murphy][TaskManagement][updateAlarmFeedActions][error][started]"+pointId);
		String queryString = "Update TM_CYGNET_ALARM_ACTIONS set IS_ACKNOWLEDGED = 'N', IS_DESIGNATED = 'N', IS_DISPATCHED='N'  where POINTIDLONG ='"
				+ pointId + "'";
		try {
			Query q = this.getSession().createSQLQuery(queryString);
			q.executeUpdate();
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagement][updateAlarmFeedActions][error]" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private boolean findAlarmSeverityForPointId(String pointId, String alarm) {

		// logger.error("[Murphy][TaskManagement][CygnetDataPullSchedular][findAlarmSeverityForPointId][info][started]");
		int tempAlarm = Integer.parseInt(alarm.substring(0, 1).trim());
		boolean delete = false;

		try {
			String queryString = "select af.ALARM_SEVERITY From TM_CYGNET_ALARM_ACTIONS af " + "where af.POINTIDLONG ='"
					+ pointId + "'";
			Query q = this.getSession().createSQLQuery(queryString.trim());
			List<String> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (String obj : resultList) {
					String alarmSeverity = ServicesUtil.isEmpty(obj) ? null : ((obj));
					int tempAlarmSeverity = Integer.parseInt(alarmSeverity.substring(0, 1).trim());
					if (tempAlarmSeverity > tempAlarm) {
						delete = true;
					}
				}
			}

		} catch (Exception e) {
			logger.error("Error in cygnet downtime" + e);
		}

		return delete;

	}

	/* query to fecth data from cygnet schema */
	private String getQueryString() {

		String queryString = "SELECT POINTIDLONG, TIME, FACILITY_DESC,LONGDESCRIPTION, TIER, ROUTE,ALARM_CONDITION, VALUE,UNACKNOWLEDGED, ACKNOWLEDGED, "
				+ "SUPPRESSED, HIDDEN, ALARM_SEVERITY, MUWI, FIELD, FACILITYID, UDC "
				+ "FROM (SELECT ROW_NUMBER() OVER (PARTITION BY POINTIDLONG ORDER BY ALARM_SEVERITY) AS ROW_NUM,* "
				+ "FROM (SELECT TRIM(A.POINTIDLONG) as POINTIDLONG, A.TIME, "
				+ "TRIM(F.FACILITY_DESC) as FACILITY_DESC, TRIM(P.LONGDESCRIPTION) as LONGDESCRIPTION, "
				+ "TRIM(F.FACILITY_TABLE10) as TIER, TRIM(F.FACILITY_TABLE19) as ROUTE, " + "CASE "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT1 = 'Y' THEN 'Out of Range' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT2 = 'Y' THEN 'Analog Low Alarm' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT3 = 'Y' THEN 'Analog Low Warning' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT4 = 'Y' THEN 'Analog High Warning' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT5 = 'Y' THEN 'Analog High Alarm' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT6 = 'Y' THEN 'High Deviation' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT7 = 'Y' THEN 'Percent Deviation' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT9 = 'Y' THEN 'Frozen' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT10 = 'Y' THEN 'Negative Rate of change' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT11 = 'Y' THEN 'Positive Rate of change' "
				+ "WHEN A.DIGITALPOINT = 'Y' AND A.CONFIGBIT2 = 'Y' THEN 'Chattering' "
				+ "WHEN A.DIGITALPOINT = 'Y' AND A.CONFIGBIT4 = 'Y' THEN 'Digital Alarm' "
				+ "WHEN A.DIGITALPOINT = 'Y' AND A.CONFIGBIT5 = 'Y' THEN 'Digital Warning' "
				+ "WHEN A.DIGITALPOINT = 'Y' AND A.CONFIGBIT8 = 'Y' THEN 'Change Alarm' "
				+ "WHEN A.DIGITALPOINT = 'Y' AND A.CONFIGBIT9 = 'Y' THEN 'Frozen' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT1 = 'Y' THEN 'String Alarm 1' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT2 = 'Y' THEN 'String Alarm 2' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT3 = 'Y' THEN 'String Alarm 3' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT4 = 'Y' THEN 'String Alarm 4' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT5 = 'Y' THEN 'String Alarm 5' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT6 = 'Y' THEN 'String Alarm 6' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT8 = 'Y' THEN 'Change Alarm' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT9 = 'Y' THEN 'Frozen' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT1 = 'Y' THEN 'Enum Alarm 1' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT2 = 'Y' THEN 'Enum Alarm 2' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT3 = 'Y' THEN 'Enum Alarm 3' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT4 = 'Y' THEN 'Enum Alarm 4' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT5 = 'Y' THEN 'Enum Alarm 5' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT6 = 'Y' THEN 'Enum Alarm 6' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT8 = 'Y' THEN 'Change Alarm' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT9 = 'Y' THEN 'Frozen'" + "ELSE ''" + "END as ALARM_CONDITION, "
				+ "A.VALUE, A.SET1 as UNACKNOWLEDGED, A.ACKNOWLEDGED, A.ALARMSUPPRESSED as SUPPRESSED, A.IGNORED as HIDDEN, "
				+ "CASE " + "WHEN A.UNRELIABLEFLAG = 'Y' THEN '5-Unreliable' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT1 = 'Y' THEN '5-Unreliable' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT2 = 'Y' THEN '1-Alarm' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT3 = 'Y' THEN '2-Warning' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT4 = 'Y' THEN '2-Warning' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT5 = 'Y' THEN '1-Alarm' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT6 = 'Y' THEN '1-Alarm' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT7 = 'Y' THEN '1-Alarm' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT9 = 'Y' THEN '3-Frozen' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT10 = 'Y' THEN '1-Alarm' "
				+ "WHEN A.ANALOGPOINT = 'Y' AND A.CONFIGBIT11 = 'Y' THEN '1-Alarm' "
				+ "WHEN A.DIGITALPOINT = 'Y' AND A.CONFIGBIT2 = 'Y' THEN '5-Unreliable' "
				+ "WHEN A.DIGITALPOINT = 'Y' AND A.CONFIGBIT4 = 'Y' THEN '1-Alarm' "
				+ "WHEN A.DIGITALPOINT = 'Y' AND A.CONFIGBIT5 = 'Y' THEN '2-Warning' "
				+ "WHEN A.DIGITALPOINT = 'Y' AND A.CONFIGBIT8 = 'Y' THEN '1-Alarm' "
				+ "WHEN A.DIGITALPOINT = 'Y' AND A.CONFIGBIT9 = 'Y' THEN '3-Frozen' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT1 = 'Y' THEN '2-Warning' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT2 = 'Y' THEN '2-Warning' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT3 = 'Y' THEN '2-Warning' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT4 = 'Y' THEN '2-Warning' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT5 = 'Y' THEN '1-Alarm' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT6 = 'Y' THEN '1-Alarm' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT8 = 'Y' THEN '1-Alarm' "
				+ "WHEN A.STRINGPOINT = 'Y' AND A.CONFIGBIT9 = 'Y' THEN '3-Frozen' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT1 = 'Y' THEN '2-Warning' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT2 = 'Y' THEN '2-Warning' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT3 = 'Y' THEN '2-Warning' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT4 = 'Y' THEN '2-Warning' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT5 = 'Y' THEN '1-Alarm' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT6 = 'Y' THEN '1-Alarm' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT8 = 'Y' THEN '1-Alarm' "
				+ "WHEN A.ENUMPOINT = 'Y' AND A.CONFIGBIT9 = 'Y' THEN '3-Frozen' " + "ELSE '4-Normal' "
				+ "END as ALARM_SEVERITY, " + "TRIM(F.FACILITY_ATTR7) as MUWI, " + "TRIM(F.FACILITY_ATTR20) as FIELD, "
				+ "TRIM(A.FACILITYID) as FACILITYID, " + "TRIM(A.UDC) as UDC " + "FROM CYGNETPROD.PNTS1 P, "
				+ "CYGNETPROD.ALARMS A LEFT OUTER JOIN CYGNETPROD.FAC F ON TRIM(A.FACILITYID) = TRIM(F.FACILITY_ID) "
				+ " AND TRIM(A.SITE) = TRIM(F.FACILITY_SITE) " + " AND TRIM(A.SERVICE) = TRIM(F.FACILITY_SERVICE) "
				+ "WHERE TRIM(A.POINTID) = TRIM(P.POINTID) " + " AND TRIM(A.SITE) = TRIM(P.SITE) "
				+ " AND TRIM(A.SERVICE) = TRIM(P.SERVICE) " + " AND TRIM(A.POINTIDLONG) NOT LIKE 'Z%' "
				+ " AND TRIM(A.POINTIDLONG) NOT LIKE 'TEMP%' " + " AND TRIM(A.POINTIDLONG) NOT LIKE 'TEST%' " + ") "
				+ ") " + "WHERE ROW_NUM = 1 " + "ORDER BY TIME";

		return queryString;

	}

	/*
	 * private CygnetAlarmFeedDo SetObjectToDo(Object[] obj, CygnetAlarmFeedDo
	 * alarmFeedDo) {
	 * 
	 * alarmFeedDo.setPointId(ServicesUtil.isEmpty(obj[0]) ? null : ((String)
	 * obj[0])); alarmFeedDo.setTimeStamp((Date) (obj[1]));
	 * alarmFeedDo.setFacDescription(ServicesUtil.isEmpty(obj[2]) ? null :
	 * ((String) obj[2]));
	 * alarmFeedDo.setLongDescription(ServicesUtil.isEmpty(obj[3]) ? null :
	 * ((String) obj[3])); alarmFeedDo.setTier(ServicesUtil.isEmpty(obj[4]) ?
	 * null : ((String) obj[4]));
	 * alarmFeedDo.setRoute(ServicesUtil.isEmpty(obj[5]) ? null : ((String)
	 * obj[5])); alarmFeedDo.setAlarmCondition(ServicesUtil.isEmpty(obj[6]) ?
	 * null : ((String) obj[6]));
	 * alarmFeedDo.setAlarmValue(ServicesUtil.isEmpty(obj[7]) ? null : ((String)
	 * obj[7])); alarmFeedDo.setUnacknowledged(ServicesUtil.isEmpty(obj[8]) ?
	 * null : ((String) obj[8]));
	 * alarmFeedDo.setAcknowledged(ServicesUtil.isEmpty(obj[9]) ? null :
	 * ((String) obj[9]));
	 * alarmFeedDo.setSuppressed(ServicesUtil.isEmpty(obj[10]) ? null :
	 * ((String) obj[10])); alarmFeedDo.setHidden(ServicesUtil.isEmpty(obj[11])
	 * ? null : ((String) obj[11]));
	 * alarmFeedDo.setAlarmSeverity(ServicesUtil.isEmpty(obj[12]) ? null :
	 * ((String) obj[12])); alarmFeedDo.setMuwi(ServicesUtil.isEmpty(obj[13]) ?
	 * null : ((String) obj[13]));
	 * alarmFeedDo.setField(ServicesUtil.isEmpty(obj[14]) ? null : ((String)
	 * obj[14])); alarmFeedDo.setFacility(ServicesUtil.isEmpty(obj[15]) ? null :
	 * ((String) obj[15])); alarmFeedDo.setTag(ServicesUtil.isEmpty(obj[16]) ?
	 * null : ((String) obj[16]));
	 * 
	 * return alarmFeedDo; }
	 */

	private String formAlarmQuery(String locationCode, String locationType, boolean isAcknowleged) {

		String selectClause = "SELECT AL.POINTIDLONG, AL.FACILITYID, AL.MUWI, AL.TIME, AL.FACILITY_DESC, AL.LONGDESCRIPTION, WT.TIER, AL.ROUTE, AL.ALARM_CONDITION, AL.ALARM_SEVERITY, AL.VALUE, AL.ACKNOWLEDGED, AL.UNACKNOWLEDGED, AA.IS_ACKNOWLEDGED, AA.IS_DISPATCHED, AA.IS_DESIGNATED, DCH.DOWNTIME_CLASSIFIER, PL.LOCATION_CODE, PL.LOCATION_TYPE, LC.LATITUDE, LC.LONGITUDE, AL.DATE_MODIFIED ";
		String fromClause = " FROM CYGNETPROD.ALARMS2 AL ";
		String joinWellMuwiClause = " JOIN WELL_MUWI WM ON AL.MUWI = WM.MUWI ";
		String joinFacilityMappingClause = " JOIN FACILITY_MAPPING FM ON AL.FACILITYID = FM.FACILITY_ID ";
		String joinProductionLocationClause = " JOIN PRODUCTION_LOCATION PL ";
		String joinProdLocationCondWell = " ON WM.LOCATION_CODE = PL.LOCATION_CODE ";
		String joinProdLocationCondFac = " ON FM.FACILITY_CODE = PL.LOCATION_CODE ";
		String joinLocationCoordinateClause = " LEFT JOIN LOCATION_COORDINATE LC ON PL.LOCATION_CODE = LC.LOCATION_CODE ";
		String joinAlarmActionsClause = " LEFT JOIN TM_CYGNET_ALARM_ACTIONS AA ON AL.POINTIDLONG = AA.POINTIDLONG AND AL.TIME = AA.TIME";
		String joinDowntimeHistoryClause = " LEFT JOIN TM_DOWNTIME_CAPTURE_HISTORY DCH ON AL.LONGDESCRIPTION  = DCH.LONG_DESCRIPTION AND AL.LONGDESCRIPTION || ' | ' || AL.ALARM_CONDITION = DCH.ALARM_CONDITION ";
		String whereWellHierarchyClause = " WHERE 1 = 1 ";
		String whereFacilityHierarchyClause = " WHERE 1 = 1";
		String whereWellTypeClause = " AND (AL.MUWI IS NOT NULL AND AL.MUWI != '') ";
		String whereFacilityTypeClause = "  AND (AL.MUWI IS NULL OR TRIM(AL.MUWI) = '') ";
		String partitionClauseStart = "SELECT * FROM (SELECT ROW_NUMBER() OVER (PARTITION BY POINTIDLONG ORDER BY DATE_MODIFIED DESC) AS ROW_NUM, * FROM ";
		String partitionClauseEnd = " ) WHERE ROW_NUM = 1 ORDER BY TIME DESC ";
		String finalQuery = "";
		String customClause1 = "";
		String customClause2 = "";
		String customClause3 = "";
		String customClause4 = "";
		String wellAlarmQuery = "";
		String facilityAlarmQuery = "";
		Boolean facilityAlarmLevel = true;
		String genericQuery = "";
		String acknowledgeQuery = " AND ((AL.ACKNOWLEDGED = 'Y' AND AL.UNACKNOWLEDGED = 'Y' AND AA.IS_ACKNOWLEDGED = 'Y' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'Y' AND AL.UNACKNOWLEDGED = 'Y' AND AA.IS_ACKNOWLEDGED = 'N' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'Y' AND AL.UNACKNOWLEDGED = 'N' AND AA.IS_ACKNOWLEDGED = 'Y' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'N' AND AL.UNACKNOWLEDGED = 'N' AND AA.IS_ACKNOWLEDGED = 'Y' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'Y' AND AL.UNACKNOWLEDGED = 'N' AND AA.IS_ACKNOWLEDGED = 'N' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'N' AND AL.UNACKNOWLEDGED = 'Y' AND AA.IS_ACKNOWLEDGED = 'Y' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N')) ";
		String joinWellTierClause = "LEFT JOIN WELL_TIER WT ON WT.LOCATION_CODE=PL.LOCATION_CODE ";
		// String isAcknowledgeQuery=" AND AA.IS_ACKNOWLEDGED != 'Y' ";
		if (locationCode != null && locationType != null) {
			switch (locationType) {
			case ("Field"):
				whereWellHierarchyClause = " WHERE AL.MUWI IN ( SELECT WM1.MUWI FROM PRODUCTION_LOCATION P1 JOIN PRODUCTION_LOCATION P2 ON P1.PARENT_CODE = P2.LOCATION_CODE JOIN PRODUCTION_LOCATION P3 ON P2.PARENT_CODE = P3.LOCATION_CODE JOIN PRODUCTION_LOCATION P4 ON P3.PARENT_CODE = P4.LOCATION_CODE JOIN WELL_MUWI WM1 ON WM1.LOCATION_CODE = P1.LOCATION_CODE WHERE P4.LOCATION_CODE IN ("
						+ locationCode + ") ) ";
				whereFacilityHierarchyClause = " WHERE FM.FIELD_CODE IN (" + locationCode + ") ";
				break;
			case ("Facility"):
			case ("Central Facility"):
				whereWellHierarchyClause = " WHERE AL.MUWI IN ( SELECT WM1.MUWI FROM PRODUCTION_LOCATION P1 JOIN PRODUCTION_LOCATION P2 ON P1.PARENT_CODE = P2.LOCATION_CODE JOIN PRODUCTION_LOCATION P3 ON P2.PARENT_CODE = P3.LOCATION_CODE JOIN WELL_MUWI WM1 ON WM1.LOCATION_CODE = P1.LOCATION_CODE WHERE P3.LOCATION_CODE IN ("
						+ locationCode + ") ) ";
				whereFacilityHierarchyClause = " WHERE FM.FACILITY_CODE IN (" + locationCode + ") ";
				break;
			case ("Well Pad"):
				facilityAlarmLevel = false;
				whereWellHierarchyClause = " WHERE AL.MUWI IN ( SELECT WM1.MUWI FROM PRODUCTION_LOCATION P1 JOIN PRODUCTION_LOCATION P2 ON P1.PARENT_CODE = P2.LOCATION_CODE JOIN WELL_MUWI WM1 ON WM1.LOCATION_CODE = P1.LOCATION_CODE WHERE P2.LOCATION_CODE IN ("
						+ locationCode + ") ) ";
				break;
			case ("Well"):
				facilityAlarmLevel = false;
				whereWellHierarchyClause = " WHERE AL.MUWI IN ( SELECT WM1.MUWI FROM WELL_MUWI WM1 WHERE WM1.LOCATION_CODE IN ("
						+ locationCode + ") ) ";
				break;
			default:
				break;
			}

			customClause1 = joinWellMuwiClause;
			customClause2 = joinProdLocationCondWell;
			customClause3 = whereWellHierarchyClause;
			customClause4 = whereWellTypeClause;

			if (!isAcknowleged) {
				acknowledgeQuery = " AND ((AL.ACKNOWLEDGED = 'N' AND AL.UNACKNOWLEDGED = 'N' AND AA.IS_ACKNOWLEDGED = 'N' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'N' AND AL.UNACKNOWLEDGED = 'Y' AND AA.IS_ACKNOWLEDGED = 'N' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N')) ";
			}
			genericQuery = selectClause + fromClause + customClause1 + joinProductionLocationClause + customClause2
					+ joinWellTierClause + joinLocationCoordinateClause + joinAlarmActionsClause
					+ joinDowntimeHistoryClause + customClause3 + customClause4 + acknowledgeQuery;

			wellAlarmQuery = genericQuery;

			if (facilityAlarmLevel) {
				customClause1 = joinFacilityMappingClause;
				customClause2 = joinProdLocationCondFac;
				customClause3 = whereFacilityHierarchyClause;
				customClause4 = whereFacilityTypeClause;

				genericQuery = selectClause + fromClause + customClause1 + joinProductionLocationClause + customClause2
						+ joinWellTierClause + joinLocationCoordinateClause + joinAlarmActionsClause
						+ joinDowntimeHistoryClause + customClause3 + customClause4 + acknowledgeQuery;

				facilityAlarmQuery = " UNION ALL " + genericQuery;
			}

			finalQuery = wellAlarmQuery + facilityAlarmQuery;

			finalQuery = partitionClauseStart + "( " + finalQuery + ")" + partitionClauseEnd;
		}
		logger.error("FinalQuery" + finalQuery);
		// System.err.println(finalQuery);
		return finalQuery;
	}

	public String DeleteOlderAlarmRecords() {
		String response = MurphyConstant.FAILURE;
		String queryString = "Delete from CYGNETPROD.ALARMS2 a where a.DATE_MODIFIED in (select olderDate from "
				+ " (select DATE_MODIFIED as olderDate,POINTIDLONG as pointId, "
				+ " row_number() over( partition by POINTIDLONG order by DATE_MODIFIED desc) as rn "
				+ " from CYGNETPROD.ALARMS2 ) as T where rn > 1 and a.POINTIDLONG=pointId)";
		try {
			Query q = this.getSession().createSQLQuery(queryString);
			Integer result = (Integer) q.executeUpdate();
			response = MurphyConstant.SUCCESS + " rows Affected = " + result;
		} catch (Exception e) {
			logger.error("[Murphy][CygnetAlarmFeedDao][DeleteOlderAlarmRecords][queryString]" + queryString + "[error]"
					+ e.getMessage());
		}
		return response;
	}

	public CygnetAlarmFeedResponseDto getAlarmFeedsForCa(CygnetAlarmRequestDto alarmRequestDto) {
		CygnetAlarmFeedResponseDto responseDto = new CygnetAlarmFeedResponseDto();
		ResponseMessage message = new ResponseMessage();
		message.setStatus(MurphyConstant.SUCCESS);
		message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		message.setMessage("Request is Empty");
		DateFormat df = null;

		List<CygnetAlarmFeedDto> alarmFeedDtos = null;
		CygnetAlarmFeedDto alarmFeedDto = null;
		DowntimeCaptureHistoryDto captureHistoryDto = null;

		String locations = null;
		String locationType = null;
		String queryString = null;
		String countQueryString = null;
		String countColumnQuery = null;

		Query query = null;
		Session session = null;
		Integer countResult = 0;
		List<Object[]> resultList = null;
		Date currentTime = null;

		if (!ServicesUtil.isEmpty(alarmRequestDto)) {

			session = this.getSession();

			locationType = alarmRequestDto.getLocationType();
			locations = alarmRequestDto.getLocations();

			countColumnQuery = " SELECT COUNT(*) AS COUNT FROM ";

			//original code
			//queryString = formCanadaAlarmQuery(locations, locationType, alarmRequestDto.Acknowledged());
			
			//change added as part of an incident for incorrect canada locations in dispatch
			queryString = formCAlarmQuery(locations, locationType, alarmRequestDto.Acknowledged());
			countQueryString = countColumnQuery + "(" + queryString + ")";
			{
				query = session.createSQLQuery(countQueryString);
				countResult = ((BigInteger) query.uniqueResult()).intValue();
				if (!ServicesUtil.isEmpty(alarmRequestDto.getPage()) && alarmRequestDto.getPage() > 0) {
					int first = (alarmRequestDto.getPage() - 1) * MurphyConstant.PAGE_SIZE;
					int last = MurphyConstant.PAGE_SIZE;
					queryString += " LIMIT " + last + " OFFSET " + first + "";
				}
				logger.error("[CygnetAlarmFeedDao][queryString] : " + queryString);
				query = session.createSQLQuery(queryString);
				resultList = query.list();
			}

			// Fetch LocationTimeZone Map
			Map<String, LocationTimeZoneMasterDto> timeZoneMasterMap = locationMasterTimeZoneDao
					.fetchTimeZoneByLocation();

			if (!ServicesUtil.isEmpty(resultList)) {
				alarmFeedDtos = new ArrayList<CygnetAlarmFeedDto>();
				for (Object[] obj : resultList) {
					try {
						df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						// df.setTimeZone(TimeZone.getTimeZone("CST"));
						currentTime = df.parse(df.format(new Date()));
					} catch (Exception e) {
						logger.error("[CygnetAlarmFeedDao][getAlarmFeeds][ParseException] : " + e.getMessage());
					}
					alarmFeedDto = new CygnetAlarmFeedDto();
					if (!ServicesUtil.isEmpty(timeZoneMasterMap)) {
						String locationCode = (String) obj[18];
						if (!ServicesUtil.isEmpty(locationCode)) {
							LocationTimeZoneMasterDto masterTimeZoneDto = timeZoneMasterMap
									.get(locationCode.substring(0, 10));
							alarmFeedDto.setTimeZone(masterTimeZoneDto.getTimeZone());
						} else {
							alarmFeedDto.setTimeZone(MurphyConstant.MST_ZONE);
						}
					}
					alarmFeedDto.setPointId((String) obj[1]);
					alarmFeedDto.setDownTimeClassifier((String) obj[17]);
					alarmFeedDto.setFacility((String) obj[2]);
					alarmFeedDto.setMuwi((String) obj[3]);
//					alarmFeedDto.setMuwi(ServicesUtil.isEmpty((String) obj[3]) ? null
//							: hierarchyDao.getMuwiByLocationCode((String) obj[3]));
					alarmFeedDto.setTimeStamp(ServicesUtil.isEmpty(obj[4]) ? null
							: ServicesUtil.convertFromZoneToZone(null, obj[4], MurphyConstant.CST_ZONE,
									MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
									MurphyConstant.DATE_DISPLAY_FORMAT));
					alarmFeedDto.setTimeStampAsString(ServicesUtil.isEmpty(obj[4]) ? null
							: ServicesUtil.convertFromZoneToZoneString(null, obj[4], MurphyConstant.CST_ZONE,
									MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
									MurphyConstant.DATE_DISPLAY_FORMAT));
					alarmFeedDto.setFacDescription((String) obj[5]);
					alarmFeedDto.setLongDescription((String) obj[6]);
					alarmFeedDto.setTier((String) obj[7]);
					alarmFeedDto.setRoute((String) obj[8]);
					alarmFeedDto.setAlarmCondition((String) obj[9]);
					alarmFeedDto.setAlarmSeverity((String) obj[10]);
					alarmFeedDto.setAlarmValue((String) obj[11]);
					alarmFeedDto.setIsAcknowledge((String) obj[14]);
					alarmFeedDto.setIsDesignate((String) obj[16]);
					alarmFeedDto.setAcknowledged((String) obj[12]);
					alarmFeedDto.setUnacknowledged((String) obj[13]);
					alarmFeedDto.setIsDispatch((String) obj[15]);

					alarmFeedDto
							.setLatitude(ServicesUtil.isEmpty(obj[20]) ? null : ServicesUtil.getBigDecimal(obj[20]));
					alarmFeedDto
							.setLongitude(ServicesUtil.isEmpty(obj[21]) ? null : ServicesUtil.getBigDecimal(obj[21]));
					alarmFeedDto.setLocationCode((String) obj[18]);
					alarmFeedDto.setLocationType((String) obj[19]);
					if (alarmFeedDto.getAlarmSeverity().equals(MurphyConstant.ALARM_SEVERITY_1_ALARM)
							&& !ServicesUtil.isEmpty(alarmFeedDto.getTimeStamp())
							&& !ServicesUtil.isEmpty(currentTime)) {
						try {
							alarmFeedDto.setCygnetRecomendedTime(ServicesUtil.getDurationBreakdown(
									currentTime.getTime() - alarmFeedDto.getTimeStamp().getTime()));
						} catch (Exception ex) {
							logger.error("[CygnetAlarmFeedDao][getAlarmFeeds]Current time: " + currentTime.getTime()
									+ " alarmFeedDto.getTimeStamp().getTime(): "
									+ alarmFeedDto.getTimeStamp().getTime());
							logger.error(
									"[CygnetAlarmFeedDao][getAlarmFeeds][Exception] Date/Time : 24/10/2018 08:07 PM");
							logger.error("[CygnetAlarmFeedDao][getAlarmFeeds][Exception]DTO : " + alarmFeedDto);
						}
					}

					if (ServicesUtil.isEmpty(alarmFeedDto.getDownTimeClassifier())) {
						alarmFeedDto.setDownTimeClassifier(MurphyConstant.ALARM_NON_DOWNTIME);
					}

					{
						if ((!ServicesUtil.isEmpty(alarmFeedDto))) {
							captureHistoryDto = new DowntimeCaptureHistoryDto();
							captureHistoryDto.setAlarmCondition(alarmFeedDto.getAlarmCondition());
							captureHistoryDto.setPointId(alarmFeedDto.getPointId());
							captureHistoryDto.setDownTimeClassifier(alarmFeedDto.getDownTimeClassifier());
							captureHistoryDto.setLongDescription(alarmFeedDto.getLongDescription());

							captureHistoryDto.setDownTimeClassifier(MurphyConstant.ALARM_NON_DOWNTIME);
							downtimeCaptureDao.insertIntoHistory(captureHistoryDto);
						}
					}

					alarmFeedDtos.add(alarmFeedDto);
				}
				message.setMessage("Alarm feed fetched successfully");
			} else {
				message.setMessage("No Alarms Found");
			}

		}
		responseDto.setPageCount(new BigDecimal(MurphyConstant.PAGE_SIZE));
		responseDto.setTotalCount(new BigDecimal(countResult));
		responseDto.setAlarmsList(alarmFeedDtos);
		responseDto.setResponseMessage(message);
		return responseDto;
	}

	private String formCanadaAlarmQuery(String locationCode, String locationType, boolean isAcknowleged) {

		String selectClause = "SELECT AL.POINTIDLONG, AL.FACILITYID, AL.MUWI, AL.TIME, AL.FACILITY_DESC, AL.LONGDESCRIPTION, WT.TIER, AL.ROUTE, AL.ALARM_CONDITION, AL.ALARM_SEVERITY, AL.VALUE, AL.ACKNOWLEDGED, AL.UNACKNOWLEDGED, AA.IS_ACKNOWLEDGED, AA.IS_DISPATCHED, AA.IS_DESIGNATED, DCH.DOWNTIME_CLASSIFIER, PL.LOCATION_CODE, PL.LOCATION_TYPE, LC.LATITUDE, LC.LONGITUDE, AL.DATE_MODIFIED ";
		String fromClause = " FROM CYGNETPROD.ALARMS_CANADA AL ";
		String joinWellMuwiClause = " JOIN WELL_MUWI WM ON AL.MUWI = WM.MUWI ";
		String joinFacilityMappingClause = " JOIN FACILITY_MAPPING_CANADA FM ON FM.CYGNET_FAC_ID = AL.FACILITYID ";
		String joinProductionLocationClause = " JOIN PRODUCTION_LOCATION PL ";
		String joinProdLocationCondWell = " ON WM.LOCATION_CODE = PL.LOCATION_CODE ";
		String joinWellTierClause = "LEFT JOIN WELL_TIER WT ON WT.LOCATION_CODE=PL.LOCATION_CODE ";
		String joinLocationCoordinateClause = " LEFT JOIN LOCATION_COORDINATE LC ON PL.LOCATION_CODE = LC.LOCATION_CODE ";
		String joinAlarmActionsClause = " LEFT JOIN TM_CYGNET_ALARM_ACTIONS AA ON AL.POINTIDLONG = AA.POINTIDLONG AND AL.TIME = AA.TIME";
		String joinDowntimeHistoryClause = " LEFT JOIN TM_DOWNTIME_CAPTURE_HISTORY DCH ON AL.LONGDESCRIPTION  = DCH.LONG_DESCRIPTION AND AL.LONGDESCRIPTION || ' | ' || AL.ALARM_CONDITION = DCH.ALARM_CONDITION ";
		String whereWellTypeClause = " AND (AL.MUWI IS NOT NULL AND AL.MUWI != '') ";
		String whereFacilityTypeClause = "  AND (AL.MUWI IS NULL OR TRIM(AL.MUWI) = '') ";
		String acknowledgeQuery = " AND ((AL.ACKNOWLEDGED = 'Y' AND AL.UNACKNOWLEDGED = 'Y' AND AA.IS_ACKNOWLEDGED = 'Y' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'Y' AND AL.UNACKNOWLEDGED = 'Y' AND AA.IS_ACKNOWLEDGED = 'N' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'Y' AND AL.UNACKNOWLEDGED = 'N' AND AA.IS_ACKNOWLEDGED = 'Y' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'N' AND AL.UNACKNOWLEDGED = 'N' AND AA.IS_ACKNOWLEDGED = 'Y' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'Y' AND AL.UNACKNOWLEDGED = 'N' AND AA.IS_ACKNOWLEDGED = 'N' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'N' AND AL.UNACKNOWLEDGED = 'Y' AND AA.IS_ACKNOWLEDGED = 'Y' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N')) ";
		String partitionClauseEnd = " ) WHERE ROW_NUM = 1 ORDER BY TIME DESC ";
		String joinProdLocationCondFac = " ON FM.FACILITY_CODE = PL.LOCATION_CODE ";
		String whereWellHierarchyClause = " WHERE 1 = 1 ";
		String whereFacilityHierarchyClause = " WHERE 1 = 1";
		String partitionClauseStart = "SELECT * FROM (SELECT ROW_NUMBER() OVER (PARTITION BY POINTIDLONG ORDER BY DATE_MODIFIED DESC) AS ROW_NUM, * FROM ";
		String finalQuery = "";
		String customClause1 = "";
		String customClause2 = "";
		String customClause3 = "";
		String customClause4 = "";
		String wellAlarmQuery = "";
		String facilityAlarmQuery = "";
		Boolean facilityAlarmLevel = true;
		String genericQuery = "";

		// String isAcknowledgeQuery=" AND AA.IS_ACKNOWLEDGED != 'Y' ";
		if (locationCode != null && locationType != null) {
			switch (locationType) {
			case ("Field"):
				whereWellHierarchyClause = " WHERE AL.MUWI IN ( SELECT WM1.MUWI FROM PRODUCTION_LOCATION P1 JOIN PRODUCTION_LOCATION P2 ON P1.PARENT_CODE = P2.LOCATION_CODE JOIN PRODUCTION_LOCATION P3 ON P2.PARENT_CODE = P3.LOCATION_CODE JOIN PRODUCTION_LOCATION P4 ON P3.PARENT_CODE = P4.LOCATION_CODE JOIN WELL_MUWI WM1 ON WM1.LOCATION_CODE = P1.LOCATION_CODE WHERE P4.LOCATION_CODE IN ("
						+ locationCode + ") ) ";
				whereFacilityHierarchyClause = " WHERE FM.FIELD_CODE IN (" + locationCode + ") ";
				break;
			case ("Facility"):
			case ("Central Facility"):
				whereWellHierarchyClause = " WHERE AL.MUWI IN ( SELECT WM1.MUWI FROM PRODUCTION_LOCATION P1 JOIN PRODUCTION_LOCATION P2 ON P1.PARENT_CODE = P2.LOCATION_CODE JOIN PRODUCTION_LOCATION P3 ON P2.PARENT_CODE = P3.LOCATION_CODE JOIN WELL_MUWI WM1 ON WM1.LOCATION_CODE = P1.LOCATION_CODE WHERE P3.LOCATION_CODE IN ("
						+ locationCode + ") ) ";
				whereFacilityHierarchyClause = " WHERE FM.FACILITY_CODE IN (" + locationCode + ") ";
				break;
			case ("Well Pad"):
//				facilityAlarmLevel = false;
				whereWellHierarchyClause = " WHERE AL.MUWI IN ( SELECT WM1.MUWI FROM PRODUCTION_LOCATION P1 JOIN PRODUCTION_LOCATION P2 ON P1.PARENT_CODE = P2.LOCATION_CODE JOIN WELL_MUWI WM1 ON WM1.LOCATION_CODE = P1.LOCATION_CODE WHERE P2.LOCATION_CODE IN ("
						+ locationCode + ") ) ";
				whereFacilityHierarchyClause=" WHERE FM.WELLPAD_CODE IN (" + locationCode + ") ";
				break;
			case ("Well"):
//				facilityAlarmLevel = false;
				whereWellHierarchyClause = " WHERE AL.MUWI IN ( SELECT WM1.MUWI FROM WELL_MUWI WM1 WHERE WM1.LOCATION_CODE IN ("
						+ locationCode + ") ) ";
				whereFacilityHierarchyClause=" WHERE FM.WELL_CODE IN (" + locationCode + ") ";
				break;
			default:
				break;
			}

			customClause1 = joinWellMuwiClause;
			customClause2 = joinProdLocationCondWell;
			customClause3 = whereWellHierarchyClause;
			customClause4 = whereWellTypeClause;

			if (!isAcknowleged) {
				acknowledgeQuery = " AND ((AL.ACKNOWLEDGED = 'N' AND AL.UNACKNOWLEDGED = 'N' AND AA.IS_ACKNOWLEDGED = 'N' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'N' AND AL.UNACKNOWLEDGED = 'Y' AND AA.IS_ACKNOWLEDGED = 'N' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N')) ";
			}
			genericQuery = selectClause + fromClause + customClause1 + joinProductionLocationClause + customClause2
					+ joinWellTierClause + joinLocationCoordinateClause + joinAlarmActionsClause
					+ joinDowntimeHistoryClause + customClause3 + customClause4 + acknowledgeQuery;

			wellAlarmQuery = genericQuery;

			if (facilityAlarmLevel) {
				customClause1 = joinFacilityMappingClause;
				customClause2 = joinProdLocationCondFac;
				customClause3 = whereFacilityHierarchyClause;
				customClause4 = whereFacilityTypeClause;

				genericQuery = selectClause + fromClause + customClause1 + joinProductionLocationClause + customClause2
						+ joinWellTierClause + joinLocationCoordinateClause + joinAlarmActionsClause
						+ joinDowntimeHistoryClause + customClause3 + customClause4 + acknowledgeQuery;

				facilityAlarmQuery = " UNION ALL " + genericQuery;
			}

			finalQuery = wellAlarmQuery + facilityAlarmQuery;

			finalQuery = partitionClauseStart + "( " + finalQuery + ")" + partitionClauseEnd;
		}
		logger.error("FinalQuery" + finalQuery);
		// System.err.println(finalQuery);
		return finalQuery;
	}

	/*
	 * @SuppressWarnings("unchecked") public List<Object[]> getAllAlarms(){
	 * List<Object[]> response=null; String queryString =
	 * "SELECT  POINTIDLONG,ALARM_SEVERITY,TIME FROM CYGNET.ALARMS2"; try {
	 * Query q = this.getSession().createSQLQuery(queryString); List<Object[]>
	 * resultList = q.list(); if (!ServicesUtil.isEmpty(resultList)) { response
	 * = resultList; } } catch (Exception e) { logger.error(
	 * "[Murphy][CygnetAlarmFeedDao][DeleteOlderAlarmRecords][queryString]" +
	 * queryString + "[error]" + e.getMessage()); } return response; }
	 */
	
	//all below methods are added as part of incident for incorrect canada locations in dispatch
	private String formCAlarmQuery(String locationCode, String locationType, boolean isAcknowleged) {


		
		 String fromClause = " FROM CYGNETPROD.ALARMS_CANADA AL ";

		
		//facility mapping join
		String joinFacilityMappingClause =" JOIN FACILITY_MAPPING_CANADA FM ON "
				                         + " (FM.CYGNET_FAC_ID = AL.FACILITYID AND (AL.MUWI IS NULL OR AL.MUWI = '')) "; 
		//join production location
		String joinProductionLocationClause = " JOIN PRODUCTION_LOCATION PL ";

		//welltierjoin
		String joinWellTierClause = " LEFT JOIN WELL_TIER WT ON WT.LOCATION_CODE=PL.LOCATION_CODE  ";
		
		//locationcordinatejoin
		String joinLocationCoordinateClause = " LEFT JOIN LOCATION_COORDINATE LC ON PL.LOCATION_CODE = LC.LOCATION_CODE ";
		
		//cygnetalrmactionjoin
		String joinAlarmActionsClause = " LEFT JOIN TM_CYGNET_ALARM_ACTIONS AA ON AL.POINTIDLONG = AA.POINTIDLONG AND AL.TIME = AA.TIME";

       //downtimecapturehistory join
		String joinDowntimeHistoryClause=" LEFT JOIN TM_DOWNTIME_CAPTURE_HISTORY DCH ON AL.POINTIDLONG = DCH.POINT_ID "
				                        + "AND AL.LONGDESCRIPTION || ' | ' || AL.ALARM_CONDITION = DCH.ALARM_CONDITION ";
	
       //acknowledgeQueryIfTrue
		String acknowledgeQuery=" AND ((AL.ACKNOWLEDGED = 'Y' AND AL.UNACKNOWLEDGED = 'Y' AND AA.IS_ACKNOWLEDGED = 'Y' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'Y' AND AL.UNACKNOWLEDGED = 'Y' AND AA.IS_ACKNOWLEDGED = 'N' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'Y' AND AL.UNACKNOWLEDGED = 'N' AND AA.IS_ACKNOWLEDGED = 'Y' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'N' AND AL.UNACKNOWLEDGED = 'N' AND AA.IS_ACKNOWLEDGED = 'Y' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'Y' AND AL.UNACKNOWLEDGED = 'N' AND AA.IS_ACKNOWLEDGED = 'N' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'N' AND AL.UNACKNOWLEDGED = 'Y' AND AA.IS_ACKNOWLEDGED = 'Y' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N')) ";
		
		//last clause
		String partitionClauseEnd = " ) WHERE ROW_NUM = 1 ORDER BY TIME DESC ";

		String wellmuwijoin = " JOIN WELL_MUWI WM ON AL.MUWI = WM.MUWI ";
		String whereClauseForWell = " ";
		String whereClauseForField = " ";
		String whereClauseForWellPad = " ";
		String whereClauseForFacility = " ";
	    //first Caluse
		String partitionClauseStart = " SELECT * FROM (SELECT ROW_NUMBER() OVER (PARTITION BY POINTIDLONG ORDER BY DATE_MODIFIED DESC) AS ROW_NUM, * FROM ";
		
		//section common to all
		String startingclausecommontoalllocation =" SELECT AL.POINTIDLONG, AL.FACILITYID, AL.MUWI, AL.TIME, AL.FACILITY_DESC, "
				                                  + "AL.LONGDESCRIPTION, WT.TIER, AL.ROUTE, AL.ALARM_CONDITION, "
				                                  + "AL.ALARM_SEVERITY, AL.VALUE, AL.ACKNOWLEDGED, AL.UNACKNOWLEDGED, "
				                                  + "AA.IS_ACKNOWLEDGED, AA.IS_DISPATCHED, AA.IS_DESIGNATED, DCH.DOWNTIME_CLASSIFIER, "
				                                  + "PL.LOCATION_CODE, PL.LOCATION_TYPE, LC.LATITUDE, LC.LONGITUDE, AL.DATE_MODIFIED";
		
		
		
		String productionlocationjoinqueryForWell ="";
		String productionlocationjoinqueryForWellPad ="";
		String productionlocationjoinqueryForFacility ="";
		String productionlocationjoinqueryForField ="";
		String finalQuery = "";
		String wellLevelQuery = "";
		String wellPadLevelQuery = "";
		String facilityLevelQuery = "";
		String fieldLevelQuery = "";
		String muwiLevelJoin ="" ;
		String muwiLevelwhereClause="";
		String muwiLevelQuery="";
		String genericQuery="";
		String fieldCode = "";
		String wellCode="";
		String wellpadCode="";
		String facilityCode="";
		if (locationCode != null && locationType != null) {
			if(locationType.equalsIgnoreCase("Well") 
					|| locationType.equalsIgnoreCase("WellPad")
					|| locationType.equalsIgnoreCase("Facility"))
			fieldCode= "'"+getFieldCode(locationCode,locationType)+"'";
				//fieldCode="'MUR-CA-KAY'";
			else
			fieldCode = locationCode;
			
			if(locationType.equalsIgnoreCase("WellPad")
				|| locationType.equalsIgnoreCase("Facility")
				|| locationType.equalsIgnoreCase("Field"))
			wellCode = getWellCode(locationCode,locationType);
			else
				wellCode = locationCode;
			
			if(locationType.equalsIgnoreCase("Facility")
					|| locationType.equalsIgnoreCase("Field") 
					&& !(locationType.equalsIgnoreCase("Well")
					|| locationType.equalsIgnoreCase("Well Pad")))
				wellpadCode = getWellPadCode(locationCode,locationType);
			else
				wellpadCode=locationCode;
			
			if(locationType.equalsIgnoreCase("Field")
					&& !(locationType.equalsIgnoreCase("Well")
					|| locationType.equalsIgnoreCase("Well Pad")
					||locationType.equalsIgnoreCase("Facility")))
				facilityCode= getFacilityCode(locationCode,locationType);
			else
				facilityCode=locationCode;
			
			productionlocationjoinqueryForField = " ON (FM.FIELD_CODE = PL.LOCATION_CODE AND FM.FIELD_CODE <> '' "
                                           + " AND (FM.WELL_CODE = '' OR FM.WELL_CODE is null) AND "
                                           + " (FM.WELLPAD_CODE = '' OR FM.WELLPAD_CODE is null) AND "
                                           + " (FM.FACILITY_CODE = '' OR FM.FACILITY_CODE is null)) ";
			    whereClauseForField=" WHERE FM.FIELD_CODE IN (" + fieldCode + ") ";
				
			    productionlocationjoinqueryForFacility = " ON (FM.FACILITY_CODE = PL.LOCATION_CODE AND FM.FACILITY_CODE <> '' "
                                           + " AND (FM.WELL_CODE = '' OR FM.WELL_CODE is null) AND "
                                           + " (FM.WELLPAD_CODE = '' OR FM.WELLPAD_CODE is null)) ";
				
			    whereClauseForFacility = " WHERE FM.FACILITY_CODE  IN (" + facilityCode + ") ";
				
				productionlocationjoinqueryForWellPad=" ON (FM.WELLPAD_CODE = PL.LOCATION_CODE AND FM.WELLPAD_CODE <> '' "
                                                      + "AND (FM.WELL_CODE = '' OR FM.WELL_CODE is null) ) ";
				 
				whereClauseForWellPad=" WHERE FM.WELLPAD_CODE IN (" + wellpadCode + ") ";
				
			
			    muwiLevelJoin=" ON WM.LOCATION_CODE = PL.LOCATION_CODE ";
				muwiLevelwhereClause = " WHERE (AL.MUWI IS NOT NULL AND AL.MUWI != '') "
						+ " AND AL.MUWI IN "
						+ " (SELECT WM1.MUWI FROM PRODUCTION_LOCATION P1 JOIN PRODUCTION_LOCATION P2 "
						+ " ON P1.PARENT_CODE = P2.LOCATION_CODE "
						+ " JOIN PRODUCTION_LOCATION P3 ON P2.PARENT_CODE = P3.LOCATION_CODE "
						+ " JOIN PRODUCTION_LOCATION P4 ON P3.PARENT_CODE = P4.LOCATION_CODE JOIN WELL_MUWI WM1 "
						+ " ON WM1.LOCATION_CODE = P1.LOCATION_CODE WHERE P4.LOCATION_CODE IN ("+locationCode+")  ) ";
				
			    productionlocationjoinqueryForWell = " ON (FM.WELL_CODE = PL.LOCATION_CODE AND FM.WELL_CODE <> '') ";
				whereClauseForWell= " WHERE FM.WELL_CODE  IN (" + wellCode + ") ";
			

			
			if (!isAcknowleged) {
				acknowledgeQuery = " AND ((AL.ACKNOWLEDGED = 'N' AND AL.UNACKNOWLEDGED = 'N' AND AA.IS_ACKNOWLEDGED = 'N' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N') OR (AL.ACKNOWLEDGED = 'N' AND AL.UNACKNOWLEDGED = 'Y' AND AA.IS_ACKNOWLEDGED = 'N' AND AL.SUPPRESSED = 'N' AND AL.HIDDEN = 'N')) ";
			}
			
			//for muwi from cygnet alarms
			muwiLevelQuery = startingclausecommontoalllocation+fromClause+wellmuwijoin+joinProductionLocationClause
					   +muwiLevelJoin+joinWellTierClause+joinLocationCoordinateClause+joinAlarmActionsClause
					   +joinDowntimeHistoryClause+muwiLevelwhereClause+acknowledgeQuery;
		
			logger.error("[CygnetAlarmFeedDao][formCAAlarmQuery][muwiLevelQuery]" +muwiLevelQuery);
			
			//For wellcode
			wellLevelQuery = startingclausecommontoalllocation+fromClause+joinFacilityMappingClause+
				       joinProductionLocationClause+productionlocationjoinqueryForWell+joinWellTierClause
				       +joinLocationCoordinateClause+joinAlarmActionsClause+joinDowntimeHistoryClause
				       +whereClauseForWell+acknowledgeQuery;
		
			logger.error("[CygnetAlarmFeedDao][formCAAlarmQuery][wellLevelQuery]" +wellLevelQuery);
			
			//for wellPad
			wellPadLevelQuery =startingclausecommontoalllocation+fromClause+joinFacilityMappingClause+
				       joinProductionLocationClause+productionlocationjoinqueryForWellPad+joinWellTierClause
				       +joinLocationCoordinateClause+joinAlarmActionsClause+joinDowntimeHistoryClause
				       +whereClauseForWellPad+acknowledgeQuery;
					
	
			logger.error("[CygnetAlarmFeedDao][formCAAlarmQuery][wellPadLevelQuery]" +wellPadLevelQuery);
			
			//for facility
			facilityLevelQuery = startingclausecommontoalllocation+fromClause+joinFacilityMappingClause+
				       joinProductionLocationClause+productionlocationjoinqueryForFacility+joinWellTierClause
				       +joinLocationCoordinateClause+joinAlarmActionsClause+joinDowntimeHistoryClause
				       +whereClauseForFacility+acknowledgeQuery;
	
			logger.error("[CygnetAlarmFeedDao][formCAAlarmQuery][FacilityLevelQuery]" +facilityLevelQuery);
			
			//for field
			fieldLevelQuery = startingclausecommontoalllocation+fromClause+joinFacilityMappingClause+
				       joinProductionLocationClause+productionlocationjoinqueryForField+joinWellTierClause
				       +joinLocationCoordinateClause+joinAlarmActionsClause+joinDowntimeHistoryClause
				       +whereClauseForField+acknowledgeQuery;
			
			
			logger.error("[CygnetAlarmFeedDao][formCAAlarmQuery][FieldLevelQuery]" +fieldLevelQuery);
			

			if(locationType.equalsIgnoreCase("Well"))
				finalQuery= partitionClauseStart +"("+muwiLevelQuery+ "UNION ALL " +wellLevelQuery+")"+partitionClauseEnd;
				
			if(locationType.equalsIgnoreCase("Well Pad"))
					finalQuery = partitionClauseStart +"("+muwiLevelQuery+ "UNION ALL " +wellLevelQuery+ 
					" UNION ALL " + wellPadLevelQuery+")" +partitionClauseEnd;
					
			if(locationType.equalsIgnoreCase("Facility"))
						
				finalQuery = partitionClauseStart +"("+muwiLevelQuery+ "UNION ALL " +wellLevelQuery+ 
				" UNION ALL " + wellPadLevelQuery+ "UNION ALL "+facilityLevelQuery+")" +partitionClauseEnd;
			
			if(locationType.equalsIgnoreCase("Field"))
							finalQuery = partitionClauseStart +"("+muwiLevelQuery+ " UNION ALL " +wellLevelQuery+ 
									" UNION ALL " + wellPadLevelQuery+ " UNION ALL "+facilityLevelQuery+ " UNION ALL "
									+fieldLevelQuery+")"+ partitionClauseEnd;
			logger.error("[CygnetAlarmFeedDao][formCAAlarmQuery][finalQuery]" +finalQuery);
		}
		logger.error("FinalQuery"+finalQuery);
		
		return finalQuery;
	}
	
	@SuppressWarnings("unchecked")
	private String getFieldCode(String locationCode,String locationType) {
		String queryString="";
		
		String fieldCode="";
		Session session = null;
		try{
			
			String selectFieldCodeQuery="Select FIELD_CODE,CYGNET_SOURCE from FACILITY_MAPPING_CANADA where ";
			String whereClause="";
			if(locationType.equalsIgnoreCase("Well"))
				
				whereClause=" WELL_CODE in ("+locationCode+") ";
				
			if(locationType.equalsIgnoreCase("WellPad"))
				whereClause=" WELLPAD_CODE in ("+locationCode+") ";
			if(locationType.equalsIgnoreCase("Facility"))
				whereClause=" FACILITY_CODE in ("+locationCode+") ";
			queryString = selectFieldCodeQuery+whereClause;
			logger.error("field codequery :: [CygnetAlarmsFeedDao][getFieldCode] " +queryString);
			session= this.getSession();
			Query query = session.createSQLQuery(queryString);
			List<Object[]> resultList = query.list();
			if(!ServicesUtil.isEmpty(resultList))
			{
			
				for(Object[] obj : resultList)
				{
					fieldCode = (String) obj[0];
				}
				
			}

			
		}
		catch(Exception e)
		{
			logger.error("error in fetching field code :: [CygnetAlarmsFeedDao][getFieldCode]" + e.getMessage());
		}
		return fieldCode;
	}
	
	private String getWellCode(String locationCode,String locationType) {
		String queryString="";
		String wellCodeInQuery="";
		String wellCode="";
		List<String> wellCodeList = new ArrayList<String>();
		Session session = null;
		try{
			
			String selectFieldCodeQuery="Select WELL_CODE,CYGNET_SOURCE from FACILITY_MAPPING_CANADA where ";
			String whereClause="";
			
			if(locationType.equalsIgnoreCase("WellPad"))
				whereClause=" WELLPAD_CODE in ("+locationCode+") ";
			if(locationType.equalsIgnoreCase("Facility"))
				whereClause=" FACILITY_CODE in ("+locationCode+") ";
			if(locationType.equalsIgnoreCase("Field"))
				whereClause=" FIELD_CODE in ("+locationCode+") ";
			queryString = selectFieldCodeQuery+whereClause;
			logger.error("field codequery :: [CygnetAlarmsFeedDao][getFieldCode] " +queryString);
			session= this.getSession();
			Query query = session.createSQLQuery(queryString);
			List<Object[]> resultList = query.list();
			if(!ServicesUtil.isEmpty(resultList))
			{
			
				for(Object[] obj : resultList)
				{if(!ServicesUtil.isEmpty(obj[0]))
				{
					wellCode = (String) obj[0];
					wellCodeList.add(wellCode);
				}
				}
				
			}

			
			if(!ServicesUtil.isEmpty(wellCodeList))
				
				wellCodeInQuery=ServicesUtil.getStringFromList(wellCodeList);
			logger.error("wellcode in query");
		}
		catch(Exception e)
		{
			logger.error("error in fetching well code :: [CygnetAlarmsFeedDao][getWellCode]" + e.getMessage());
		}
		return wellCodeInQuery;
	}
	
	private String getWellPadCode(String locationCode, String locationType) {
        String queryString="";
        String wellPadCode="";
		String wellPadCodeInquery="";
		Session session = null;
		List<String> wellPadCodeList = new ArrayList<String>();
		try{
			
			String selectFieldCodeQuery="Select WELLPAD_CODE,CYGNET_SOURCE from FACILITY_MAPPING_CANADA where ";
			String whereClause="";
			
			if(locationType.equalsIgnoreCase("Facility"))
				whereClause=" FACILITY_CODE in ("+locationCode+") ";
			if(locationType.equalsIgnoreCase("Field"))
				whereClause=" FIELD_CODE in ("+locationCode+") ";
			queryString = selectFieldCodeQuery+whereClause;
			logger.error("field codequery :: [CygnetAlarmsFeedDao][getFieldCode] " +queryString);
			session= this.getSession();
			Query query = session.createSQLQuery(queryString);
			List<Object[]> resultList = query.list();
			if(!ServicesUtil.isEmpty(resultList))
			{
			
				for(Object[] obj : resultList)
				{
					if(!ServicesUtil.isEmpty(obj[0]))
					{
					wellPadCode = (String) obj[0];
					wellPadCodeList.add(wellPadCode);
					}
				}
				
			}
     if(!ServicesUtil.isEmpty(wellPadCodeList))
    	 wellPadCodeInquery = ServicesUtil.getStringFromList(wellPadCodeList);
     logger.error(wellPadCodeInquery);


			
		}
		catch(Exception e)
		{
			logger.error("error in fetching wellpad code :: [CygnetAlarmsFeedDao][getFieldCode]" + e.getMessage());
		}
		return wellPadCodeInquery;
	}
	private String getFacilityCode(String locationCode, String locationType) {
		 String queryString="";
			String facilityCodeInQuery="";
			String facilityCode="";
			List<String> facilityCodeList = new ArrayList<String>();
			Session session = null;
			try{
				
				String selectFieldCodeQuery="Select FACILITY_CODE,CYGNET_SOURCE from FACILITY_MAPPING_CANADA where ";
				String whereClause="";
				
				if(locationType.equalsIgnoreCase("Facility"))
					whereClause=" FACILITY_CODE in ("+locationCode+") ";
				if(locationType.equalsIgnoreCase("Field"))
					whereClause=" FIELD_CODE in ("+locationCode+") ";
				queryString = selectFieldCodeQuery+whereClause;
				logger.error("field codequery :: [CygnetAlarmsFeedDao][getFieldCode] " +queryString);
				session= this.getSession();
				Query query = session.createSQLQuery(queryString);
				List<Object[]> resultList = query.list();
				if(!ServicesUtil.isEmpty(resultList))
				{
				
					for(Object[] obj : resultList)
					{
						if(!ServicesUtil.isEmpty(obj[0]))
						{
						facilityCode = (String) obj[0];
						facilityCodeList.add(facilityCode);
						}
					}
					
				}

				if(!ServicesUtil.isEmpty(facilityCodeList))
					facilityCodeInQuery= ServicesUtil.getStringFromList(facilityCodeList);
				logger.error("facility code in query = "+facilityCodeInQuery );
			}
			catch(Exception e)
			{
				logger.error("error in fetching Facility code :: [CygnetAlarmsFeedDao][getFacilityCode]" + e.getMessage());
			}
			return facilityCodeInQuery;
	}
}
