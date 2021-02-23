package com.murphy.taskmgmt.dao;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.CanaryStagingDto;
import com.murphy.taskmgmt.entity.CanaryStagingDo;
import com.murphy.taskmgmt.entity.CanaryStagingPK;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("canaryStagingDao")
public class CanaryStagingDao extends BaseDao<CanaryStagingDo, CanaryStagingDto> {

	private static final Logger logger = LoggerFactory.getLogger(CanaryStagingDao.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	HierarchyDao locDao;

	@Autowired
	private ProductionVarianceDao productionVarianceDao;

	@Autowired
	private ConfigDao configDao;

	String tableName = "";
	Map<String, String> locList = null;

	public CanaryStagingDao() {
	}

	@Override
	protected CanaryStagingDo importDto(CanaryStagingDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		CanaryStagingDo entity = new CanaryStagingDo();
		CanaryStagingPK pk = new CanaryStagingPK();
		/*
		 * if (!ServicesUtil.isEmpty(fromDto.getStagingId()))
		 * entity.setStagingId(fromDto.getStagingId());
		 */
		if (!ServicesUtil.isEmpty(fromDto.getMuwiId()))
			pk.setMuwiId(fromDto.getMuwiId());
		if (!ServicesUtil.isEmpty(fromDto.getParameterType()))
			pk.setParameterType(fromDto.getParameterType());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			pk.setCreatedAt(fromDto.getCreatedAt());
		if (!ServicesUtil.isEmpty(fromDto.getDataValue()))
			entity.setDataValue(fromDto.getDataValue());
		entity.setCanaryStagingPK(pk);
		return entity;
	}

	@Override
	protected CanaryStagingDto exportDto(CanaryStagingDo entity) {

		CanaryStagingDto dto = new CanaryStagingDto();
		CanaryStagingPK pk = entity.getCanaryStagingPK();

		/*
		 * if (!ServicesUtil.isEmpty(entity.getStagingId()))
		 * dto.setStagingId(entity.getStagingId());
		 */
		if (!ServicesUtil.isEmpty(pk.getMuwiId()))
			dto.setMuwiId(pk.getMuwiId());
		if (!ServicesUtil.isEmpty(pk.getParameterType()))
			dto.setParameterType(pk.getParameterType());
		if (!ServicesUtil.isEmpty(pk.getCreatedAt()))
			dto.setCreatedAt(pk.getCreatedAt());
		if (!ServicesUtil.isEmpty(entity.getDataValue()))
			dto.setDataValue(entity.getDataValue());

		return dto;
	}

	// public ResponseMessage createStaging(CanaryStagingDto dto) {
	// ResponseMessage responseDto = new ResponseMessage();
	// responseDto.setStatus(MurphyConstant.FAILURE);
	// responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
	// try {
	// this.getSession().saveOrUpdate(importDto(dto));
	// responseDto.setMessage(MurphyConstant.CREATED_SUCCESS);
	// responseDto.setStatus(MurphyConstant.SUCCESS);
	// responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
	// } catch (Exception e) {
	// logger.error("[Murphy][CanaryStagingDao][createRecord][error]" +
	// e.getMessage());
	// responseDto.setMessage(MurphyConstant.CREATE_FAILURE);
	// }
	// return responseDto;
	// }

	@SuppressWarnings("unused")
	@Transactional
	public void setStagingData(int insertInterval, String insertIntervalType, int deleteInterval,
			String deleteIntervalType, String table, int roundOffInterval, String[] params, String aggregateName) {
		locList = locDao.getAllWellsWithNameMap();
		tableName = table;

		Date endDate = ServicesUtil.getDateWithInterval(null, insertInterval, insertIntervalType);
		endDate = ServicesUtil.roundDateToNearInterval(endDate, roundOffInterval, MurphyConstant.MINUTES);
		String endTime = ServicesUtil.convertFromZoneToZoneString(endDate, null, "", MurphyConstant.CST_ZONE, "",
				MurphyConstant.DATEFORMAT_T);
		String timeToDelete = "";
		if (!MurphyConstant.DOP_TABLE.equals(tableName)) {
			deleteInterval = -1 * deleteInterval;
			timeToDelete = ServicesUtil.convertFromZoneToZoneString(
					ServicesUtil.getDateWithInterval(endDate, deleteInterval, deleteIntervalType), null, "",
					MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T).replace("T", " ");
			String s1 = deleteAllDataBeforeDate(timeToDelete);
		} else {
			timeToDelete = ServicesUtil.convertFromZoneToZoneString(
					productionVarianceDao.scaleDownTimeToSeventhHour(
							ServicesUtil.getDateWithInterval(endDate, -300, MurphyConstant.MINUTES)).getTime(),
					null, "", "", "", MurphyConstant.DATEFORMAT_T).replace("T", " ");
			String startTime = ServicesUtil.convertFromZoneToZoneString(
					ServicesUtil.getDateWithInterval(endDate, insertInterval, insertIntervalType), null, "",
					MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T);
			setStagingData(endTime, startTime, params, insertInterval, insertIntervalType, aggregateName);
			// logger.error("[Murphy][CanaryStagingDao][setCanaryData][setCanaryData][endTime]"+tableName+"[insertInterval]"+insertInterval+"[deleteInterval]"+deleteInterval+"[timeToDelete]+"+timeToDelete+"[endTime]"+endTime
			// +"s1"+"s2"+s2 +"endDate"+endDate);
		}
		String s2 = setMissingData(timeToDelete, endTime.replace("T", " "), insertInterval, params, insertIntervalType,
				aggregateName);
		if (MurphyConstant.DOP_TABLE.equals(tableName)) {
			logger.error("[Murphy][CanaryStagingDao][setCanaryData][setCanaryData][tableName]" + tableName
					+ "[insertInterval]" + insertInterval + "[deleteInterval]" + deleteInterval + "[timeToDelete]+"
					+ timeToDelete + "[endTime]" + endTime + "s1" + "s2" + s2 + "endDate" + endDate);

		}
	}

	private String setCanaryData(String startTime, String endTime, String[] params, int insertInterval,
			String insertIntervalType, String aggregateName) {
		String response = MurphyConstant.FAILURE;
		try {
			setStagingData(startTime, endTime, params, insertInterval, insertIntervalType, aggregateName);
			response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingDao][setCanaryData][error]" + e.getMessage());
		}
		return response;
	}

	private String getUserToken() {
		String response = null;
		try {
			String userTokenPayload = "{\"username\":\"" + MurphyConstant.CANARY_USERNAME + "\",\"password\":\""
					+ MurphyConstant.CANARY_PASSWORD + "\"," + "\"timeZone\":\"" + MurphyConstant.CANARY_TIMEZONE
					+ "\",\"application\":\"" + MurphyConstant.CANARY_APP + "\"}";
			String url = "api/v1/getUserToken";

			// String jsonString =
			// DestinationUtil.executeWithDest(MurphyConstant.DEST_CANARY, url,
			// MurphyConstant.HTTP_METHOD_POST, MurphyConstant.APPLICATION_JSON,
			// "",userTokenPayload,"",false);

			org.json.JSONObject canaryResponseObject = RestUtil.callRest(MurphyConstant.CANARY_API_HOST + url,
					userTokenPayload, MurphyConstant.HTTP_METHOD_POST,
					configDao.getConfigurationByRef(MurphyConstant.CANARY_API_USERID_REF),
					configDao.getConfigurationByRef(MurphyConstant.CANARY_API_PASSWORD_REF));

			String canaryResponse = null;
			JSONParser parser = null;
			JSONObject json = null;
			if (!ServicesUtil.isEmpty(canaryResponseObject) && canaryResponseObject.toString().contains("userToken")) {
				canaryResponse = canaryResponseObject.toString();
				parser = new JSONParser();
				json = (JSONObject) parser.parse(canaryResponse.toString());
				return (String) json.get("userToken");
			}

			// JSONObject json = (JSONObject) parser.parse(jsonString);
			// response = (String) json.get("userToken");
		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingDao][getUserToken][error]" + e.getMessage());
		}
		return response;
	}

	private void revokeUserToken(String userToken) {
		try {
			String userTokenPayload = "{\"userToken\":\"" + userToken + "\"}";
			String url = "api/v1/revokeUserToken";

			// org.json.JSONObject canaryResponseObject =
			RestUtil.callRest(MurphyConstant.CANARY_API_HOST + url, userTokenPayload, MurphyConstant.HTTP_METHOD_POST,
					configDao.getConfigurationByRef(MurphyConstant.CANARY_API_USERID_REF),
					configDao.getConfigurationByRef(MurphyConstant.CANARY_API_PASSWORD_REF));

		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingDao][getUserToken][error]" + e.getMessage());
			logger.error("[Murphy][CanaryStagingDao][getUserToken][error] userToken" + userToken);
		}

	}

	private String getPayloadInString(List<String> payloadList) {
		String payload = "";

		for (String st : payloadList) {
			payload = payload + "\"" + st + "\",";
		}
		payload = payload.substring(0, payload.length() - 1);
		payload = "[" + payload + "]";
		return payload;

	}

	private JSONObject getCanaryData(String userToken, String payload, String startTime, String endTime,
			int insertInterval, String insertIntervalType, String aggregateName) throws Exception {
		try {
			String canaryUrl = "api/v1/getTagData";
			String stringInterval = "00";
			String aggregateInterval = "";

			if (insertInterval < 10) {
				stringInterval = "0" + insertInterval;
			} else {
				stringInterval = "" + insertInterval;
			}
			if (MurphyConstant.DAYS.equals(insertIntervalType)) {
				aggregateInterval = stringInterval + ":00:00:00";
			} else if (MurphyConstant.HOURS.equals(insertIntervalType)) {
				aggregateInterval = "0:" + stringInterval + ":00:00";
			} else if (MurphyConstant.MINUTES.equals(insertIntervalType)) {
				aggregateInterval = "0:00:" + stringInterval + ":00";
			} else if (MurphyConstant.SECONDS.equals(insertIntervalType)) {
				aggregateInterval = "0:00:00:" + stringInterval + "";
			}

			String canaryPayload = "{" + "\"userToken\": \"" + userToken + "\"," + "\"startTime\": \"" + startTime
					+ ":00.0000000-05:00\"," + "\"endTime\": \"" + endTime + ":00.0000000-05:00\","
					+ "\"aggregateName\": \"" + aggregateName + "\"," + "\"aggregateInterval\": \"" + aggregateInterval
					+ "\"," + "\"includeQuality\": false," + " \"MaxSize\": 4000000," + "\"continuation\": null,"
					+ "\"tags\": " + payload + "" + "}";

			if (MurphyConstant.DOP_TABLE.equals(tableName)) {
				logger.error("[Murphy][CanaryStagingScheduler][getStagingData][canaryPayload]" + canaryPayload);
			}

			// String canaryResponse =
			// DestinationUtil.executeWithDest(MurphyConstant.DEST_CANARY,
			// canaryUrl,
			// MurphyConstant.HTTP_METHOD_POST, MurphyConstant.APPLICATION_JSON,
			// "",canaryPayload,"",false);

			org.json.JSONObject canaryResponseObject = RestUtil.callRest(MurphyConstant.CANARY_API_HOST + canaryUrl,
					canaryPayload, MurphyConstant.HTTP_METHOD_POST,
					configDao.getConfigurationByRef(MurphyConstant.CANARY_API_USERID_REF),
					configDao.getConfigurationByRef(MurphyConstant.CANARY_API_PASSWORD_REF));
			String canaryResponse = null;
			JSONParser parser = null;
			JSONObject canaryJson = null;
			if (!ServicesUtil.isEmpty(canaryResponseObject) && canaryResponseObject.toString().contains("data")) {
				canaryResponse = canaryResponseObject.toString();
				System.err.println("[canaryResponse.toString()]" + canaryResponse.toString());
				parser = new JSONParser();
				canaryJson = (JSONObject) parser.parse(canaryResponse.toString());
				return (JSONObject) canaryJson.get("data");

			}
		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingDao][getCanaryData][error]" + e.getMessage());
			throw e;
		}
		return null;
	}

	// https://murphy.canarylabs.online:55236/api/v1/getUserToken

	private void setStagingData(String startTime, String endTime, String[] params, int insertInterval,
			String insertIntervalType, String aggregateName) {
		// logger.error("[Murphy][CanaryStagingScheduler][getStagingData][init]");

		try {
			String userToken = getUserToken();
			if (!ServicesUtil.isEmpty(userToken)) {
				List<String> payloadList = new ArrayList<String>();
				List<String> wells = locDao.getAllWells();
				for (String well : wells) {
					for (String param : params) {
						payloadList.add("MUWI_Prod." + well + "." + param);
					}
				}
				if (!ServicesUtil.isEmpty(payloadList)) {
					String payload = getPayloadInString(payloadList);
					JSONObject canaryData = getCanaryData(userToken, payload, startTime, endTime, insertInterval,
							insertIntervalType, aggregateName);
					if (!ServicesUtil.isEmpty(canaryData)) {
						createTasks(canaryData, payloadList, insertInterval, insertIntervalType, aggregateName);
					}

				}
			}

		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingDao][setStagingData][error]" + e.getMessage());
			e.printStackTrace();
		}

	}

	private void createTasks(JSONObject data, List<String> payloadList, int insertInterval, String insertIntervalType,
			String aggregateName) {
		// logger.error("[Murphy][CanaryStagingScheduler][createTasks][init]"+
		// data);
		int k = 0;
		for (String well : payloadList) {
			JSONArray wellData = (JSONArray) data.get(well);
			if (!ServicesUtil.isEmpty(wellData)) {
				for (int i = 0; i < wellData.size(); i++) {
					k++;
					JSONArray wellList = (JSONArray) wellData.get(i);
					CanaryStagingDto canaryDto = new CanaryStagingDto();
					String[] paramList = well.split("\\.");
					canaryDto.setMuwiId(paramList[1]);
					canaryDto.setParameterType(paramList[2]);
					String dateValue = wellList.get(0).toString();

					OffsetDateTime dateTime = OffsetDateTime.parse(dateValue, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
					Date d = Date.from(dateTime.toInstant());
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(d);
					calendar.add(Calendar.HOUR_OF_DAY, 1);
					Date newd = calendar.getTime();
					canaryDto.setCreatedAt(ServicesUtil.convertFromZoneToZone(newd, "", MurphyConstant.UTC_ZONE,
							MurphyConstant.CST_ZONE, MurphyConstant.DATEFORMAT_T_CANARY,
							MurphyConstant.DATE_DB_FORMATE_SD));
					canaryDto.setCreatedAtInString(ServicesUtil.convertFromZoneToZoneString(newd, "",
							MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE, MurphyConstant.DATEFORMAT_T_CANARY,
							MurphyConstant.DATE_DB_FORMATE_SD));
					// System.err.println("[test2][dateValue]"+dateValue
					// +"\n[dateTime]"+ dateTime
					// +"\n[getCreatedAtInString]"+canaryDto.getCreatedAtInString()+"\n[canaryDto.setCreatedAt(]"+canaryDto.getCreatedAt()
					// +"\n[newd]"+newd);

					if (wellList.get(1) != null && wellList.get(1) != "null") {
						if (wellList.get(1).getClass().getName().equals("java.lang.Double")) {
							canaryDto.setDataValue((Double) wellList.get(1));
						} else if (wellList.get(1).getClass().getName().equals("java.lang.Long")) {
							canaryDto.setDataValue(((Long) wellList.get(1)).doubleValue());
						} else if (wellList.get(1).getClass().getName().equals("java.lang.Integer")) {
							canaryDto.setDataValue(((Integer) wellList.get(1)).doubleValue());
						}
						if (MurphyConstant.AGGR_NAME_TOTAL.equals(aggregateName)) {
							double x = ServicesUtil.changeTimeUnits(insertInterval, insertIntervalType,
									MurphyConstant.SECONDS);
							if (x != 0) {
								canaryDto.setDataValue(canaryDto.getDataValue() / x);
							}
						}

					}
					createStaging(canaryDto);
				}
			}
		}
		logger.error("[Murphy][CanaryStagingDao][createTasks][end][newmanu][totalCount]" + k);
	}

	@SuppressWarnings("unchecked")
	public List<CanaryStagingDto> getActiveValues(List<String> muwi) {
		List<CanaryStagingDto> result = new ArrayList<>();
		List<CanaryStagingDo> dos = new ArrayList<>();

		List<String> paramList = new ArrayList<>();
		paramList.add("PRTUBXIN");
		paramList.add("PRCASXIN");

		// String hql = "select d1 from CanaryStagingDo d1, d2 where
		// d1.canaryStagingPK.parameterType in (:paramList) and
		// d1.canaryStagingPK.muwiId in (:muwi) and d1.dataValue is not null "
		// + " And d1.createdAt = MAX(d2.createdAt) " + " and
		// d2.canaryStagingPK.parameterType = d1.canaryStagingPK.parameterType "
		// + " and d2.canaryStagingPK.muwiId = d1.canaryStagingPK.muwiId" + "
		// and d1.dataValue is not null ";
		String hql1 = "select d1 from CanaryStagingDo d1 where d1.canaryStagingPK.parameterType in (:paramList) and d1.canaryStagingPK.muwiId in (:muwi) and d1.dataValue is not null"
				+ " And d1.canaryStagingPK.createdAt ="
				+ " ( select max(d2.canaryStagingPK.createdAt) from CanaryStagingDo d2 where d2.canaryStagingPK.parameterType = d1.canaryStagingPK.parameterType"
				+ " and d2.canaryStagingPK.muwiId = d1.canaryStagingPK.muwiId" + " and d2.dataValue is not null)";

		Query query = getSession().createQuery(hql1);
		query.setParameterList("paramList", paramList);
		query.setParameterList("muwi", muwi);

		dos = query.list();
		logger.error("CanarystagingDo response :" + dos);

		for (CanaryStagingDo csdo : dos) {
			result.add(exportDto(csdo));

		}

		/*
		 * String activeTube="PRTUBXIN"; String activeCase="PRCASXIN"; - *
		 * List<Double> output= new ArrayList<>(); - * - * Criteria criteria= -
		 * * getSession().createCriteria(CanaryStagingDo.class); - *
		 * criteria.add(Restrictions.eq("canaryStagingPK.muwiId",muwi)); - *
		 * criteria.add(Restrictions.eq("canaryStagingPK.parameterType", - *
		 * activeTube)); - *
		 * criteria.addOrder(Order.desc("canaryStagingPK.createdAt")). - *
		 * setMaxResults(1); - *
		 * criteria.setProjection(Projections.property("dataValue")); - *
		 * output.add((double) criteria.uniqueResult()); - * - * Criteria
		 * criteria1= - * getSession().createCriteria(CanaryStagingDo.class); -
		 * * criteria1.add(Restrictions.eq("canaryStagingPK.muwiId",muwi)); - *
		 * criteria1.add(Restrictions.eq("canaryStagingPK.parameterType", - *
		 * activeCase)); - *
		 * criteria1.addOrder(Order.desc("canaryStagingPK.createdAt")). - *
		 * setMaxResults(1); - *
		 * criteria1.setProjection(Projections.property("dataValue")); - *
		 * output.add((double) criteria.uniqueResult()); -
		 */

		return result;

	}

	private String setMissingData(String timeToDelete, String endTime, int insertInterval, String[] params,
			String insertIntervalType, String aggregateName) {
		String response = MurphyConstant.FAILURE;
		try {

			List<Object> dateList = null;

			dateList = getAllDatesInDesc(timeToDelete, endTime);

			if (ServicesUtil.isEmpty(dateList)) {
				dateList = new ArrayList<Object>();
			}
			if (dateList.size() == 0) {
				dateList.add(timeToDelete + ":00.000");
				dateList.add(endTime + ":00.000");
			} else {
				dateList.add(endTime + ":00.000");
			}
			List<Date> calendarList = new LinkedList<>();
			for (Object date : dateList) {
				Date calDate = (ServicesUtil.convertFromZoneToZone(null, date, MurphyConstant.CST_ZONE,
						MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DB_FORMATE));
				calendarList.add(calDate);
			}

			double insertIntervalInMillis = ServicesUtil.changeTimeUnits(insertInterval, insertIntervalType,
					MurphyConstant.MILLISEC);

			for (int i = 0; i < calendarList.size() - 1; i++) {

				// System.err.println("calendarList.get(i+1).getTime()"+calendarList.get(i+1).getTime()+"calendarList.get(i).getTime()"
				// +calendarList.get(i).getTime()+"dateList.size()"+dateList.size());
				if (((calendarList.get(i + 1).getTime()) - calendarList.get(i).getTime() > insertIntervalInMillis)) {
					setCanaryData(
							ServicesUtil.convertFromZoneToZoneString(
									ServicesUtil.getDateWithInterval(calendarList.get(i), insertInterval,
											insertIntervalType),
									null, "", MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T),
							ServicesUtil.convertFromZoneToZoneString1(calendarList.get(i + 1), null, "",
									MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T, insertInterval),
							params, insertInterval, insertIntervalType, aggregateName);
				}
			}
			response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingDao][setMissingData][error]" + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	public int createStaging(CanaryStagingDto dto) {
		int result = 0;
		String queryString = "";
		try {
			// String locName = locList.get(dto.getMuwiId());
			queryString = "INSERT INTO " + tableName + "(CREATED_AT,DATA_VALUE,MUWI_ID,PARAM_TYPE) VALUES('"
					+ "TO_TIMESTAMP('" + dto.getCreatedAtInString() + "', 'yyyy-MM-dd HH24:mi:ss'),"
					+ dto.getDataValue() + ",'" + dto.getMuwiId() + "','" + dto.getParameterType() + "'";
			if (MurphyConstant.DOP_TABLE.equals(tableName)) {
				queryString = "INSERT INTO TM_PRODUCTION_VARIANCE_STAGING(CREATED_AT,DATA_VALUE,MUWI_ID,PARAM_TYPE, SOURCE) "
						+ "VALUES('TO_TIMESTAMP('" + dto.getCreatedAtInString() + "', 'yyyy-MM-dd HH24:mi:ss'),"
						+ dto.getDataValue() + ",'" + dto.getMuwiId() + "','" + dto.getParameterType() + "','"
						+ MurphyConstant.DOP_CANARY + "'";
				// productionVarianceDao.createRecordForStagingVariance(dto.getCreatedAtInString(),
				// dto.getDataValue(), dto.getMuwiId(), dto.getParameterType(),
				// locName, MurphyConstant.DOP_FORECAST, MurphyConstant.DAILY);
			}
			queryString = queryString + ")";
			Query q = this.getSession().createSQLQuery(queryString);
			// logger.error("[Murphy][CanaryStagingDao][createStaging][queryString]"
			// + queryString);
			result = (Integer) q.executeUpdate();
		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingDao][createStaging][queryString]" + queryString + "[error]"
					+ e.getMessage());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getAllDatesInDesc(String date, String currentDate) {
		List<Object> result = null;
		String queryString = "";
		try {
			queryString = "select distinct(created_at) from " + tableName + " where( created_at > TO_TIMESTAMP('" + date
					+ ":00', 'yyyy-mm-dd hh24:mi:ss') and created_at < TO_TIMESTAMP('" + currentDate
					+ ":00', 'yyyy-mm-dd hh24:mi:ss')) order by created_at";
			Query q = this.getSession().createSQLQuery(queryString);
			// logger.error("[Murphy][CanaryStagingDao][deleteAllDataBeforeDate][queryString]"
			// + queryString);
			result = (List<Object>) q.list();

		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingDao][getAllDatesInDesc][queryString]" + queryString + "[error]"
					+ e.getMessage());
		}
		return result;
	}

	@SuppressWarnings("unused")
	public String deleteAllDataBeforeDate(String date) {
		String response = MurphyConstant.FAILURE;
		String queryString = "";
		try {
			queryString = "delete from " + tableName + " where created_at < TO_TIMESTAMP('" + date
					+ ":00', 'yyyy-mm-dd hh24:mi:ss') ";
			Query q = this.getSession().createSQLQuery(queryString);
			Integer result = (Integer) q.executeUpdate();
			response = MurphyConstant.SUCCESS;

		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingDao][deleteAllDataBeforeDate][queryString]" + queryString + "[error]"
					+ e.getMessage());
		}
		return response;

	}

	public static void main(String[] args) {
		Date endDate = Calendar.getInstance().getTime();
		System.out.println(endDate);
		endDate = ServicesUtil.roundDateToNearInterval(endDate, 15, MurphyConstant.MINUTES);
		System.out.println(endDate);
		String endTime = ServicesUtil.convertFromZoneToZoneString(endDate, null, "", MurphyConstant.CST_ZONE, "",
				MurphyConstant.DATEFORMAT_T);
		System.out.println(endTime);
		System.out.println(ServicesUtil.getDateWithInterval(endDate, -30, MurphyConstant.DAYS));
		String timeToDelete = ServicesUtil
				.convertFromZoneToZoneString(ServicesUtil.getDateWithInterval(endDate, -30, MurphyConstant.DAYS), null,
						"", MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T)
				.replace("T", " ");
		System.out.println(timeToDelete);
	}

	@SuppressWarnings("unused")
	@Transactional
	public void setStagingDataForCanary(int insertInterval, String insertIntervalType, int deleteInterval,
			String deleteIntervalType, String table, int roundOffInterval, String[] params, String aggregateName) {
		locList = locDao.getAllWellsWithNameMap();
		// tableName = table;

		Calendar calendar = Calendar.getInstance();
		Date endDate = calendar.getTime();
		endDate = ServicesUtil.roundDateToNearInterval(endDate, roundOffInterval, MurphyConstant.MINUTES);
		String endTime = ServicesUtil.convertFromZoneToZoneString(endDate, null, "", MurphyConstant.CST_ZONE, "",
				MurphyConstant.DATEFORMAT_T);
		calendar.setTime(endDate);
		calendar.setTimeZone(TimeZone.getTimeZone("CST"));
		String timeToDelete = "";
		deleteInterval = -1 * deleteInterval;
		timeToDelete = ServicesUtil.convertFromZoneToZoneString(
				ServicesUtil.getDateWithIntervalForCalendar(calendar, deleteInterval, deleteIntervalType), null, "",
				MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T).replace("T", " ");
		String s1 = deleteAllDataBeforeDateForCanary(timeToDelete, table);
		String s2 = setMissingDataForCanary(timeToDelete, endTime.replace("T", " "), insertInterval, params,
				insertIntervalType, aggregateName, table);
	}

	@SuppressWarnings("unused")
	public String deleteAllDataBeforeDateForCanary(String date, String table) {

		logger.error("deleteAllDataBeforeDateForCanary Starting table name " + table);
		String response = MurphyConstant.FAILURE;
		String queryString = "";
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			queryString = "delete from " + table + " where created_at < TO_TIMESTAMP('" + date
					+ ":00', 'yyyy-mm-dd hh24:mi:ss') ";
			Query q = session.createSQLQuery(queryString);
			logger.error("[Murphy][CanaryStagingDao][deleteAllDataBeforeDate][queryString]" + queryString);
			Integer result = (Integer) q.executeUpdate();
			response = MurphyConstant.SUCCESS;
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("[Murphy][CanaryStagingDao][deleteAllDataBeforeDate][queryString]" + queryString + "[error]"
					+ e.getMessage());
		} finally {
			try {
				session.close();
			} catch (Exception e) {
				logger.error("[fetchingMissingDataInfoFromDB] Exception While Closing Session " + e.getMessage());
			}
		}
		return response;

	}

	private String setMissingDataForCanary(String timeToDelete, String endTime, int insertInterval, String[] params,
			String insertIntervalType, String aggregateName, String table) {
		String response = MurphyConstant.FAILURE;
		try {

			Map<Date, List<String>> missingDataInfoMap = fetchingMissingDataInfoFromDB(timeToDelete);

			Set<Date> dateSet = missingDataInfoMap.keySet();

			for (Date date : dateSet) {

				setDataForCanary(
						ServicesUtil.convertFromZoneToZoneString(date, null, "", MurphyConstant.CST_ZONE, "",
								MurphyConstant.DATEFORMAT_T),
						ServicesUtil.convertFromZoneToZoneString1(
								ServicesUtil.getDateWithInterval(date, insertInterval, insertIntervalType), null, "",
								MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T, insertInterval),
						params, insertInterval, insertIntervalType, aggregateName, missingDataInfoMap.get(date), table);

			}
			response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingDao][setMissingData][error]" + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public Map<Date, List<String>> fetchingMissingDataInfoFromDB(String minimumDate) {
		logger.error("[fetchingMissingDataInfoFromDB] Starting minimumDate " + minimumDate);
		Map<Date, List<String>> missingDataInfoMap = null;
		List<Object[]> result = null;
		String queryString = "";
		Query q = null;
		Session session = null;
		Transaction tx = null;
		try {

			session = sessionFactory.openSession();
			tx = session.beginTransaction();

			try {
				queryString = "CALL SCHEDULER_MISSING_DATA_PROC(TO_TIMESTAMP('" + minimumDate
						+ ":00', 'yyyy-mm-dd hh24:mi:ss'))";
				q = session.createSQLQuery(queryString);
				logger.error("[Murphy][CanaryStagingDao][fetchingMissingDataInfoFromDB][queryString]" + queryString);
				q.executeUpdate();
			} catch (Exception e) {
				logger.error(
						"[fetchingMissingDataInfoFromDB] Exception While Calling Procedure Error" + e.getMessage());
			}

			try {
				queryString = "SELECT CREATED_AT, MUWI_ID FROM DUMMY_TB ORDER BY CREATED_AT";
				q = session.createSQLQuery(queryString);
				result = (List<Object[]>) q.list();
				// logger.error("[fetchingMissingDataInfoFromDB] INFO RESULT " +
				// result);
				logger.error("[fetchingMissingDataInfoFromDB] INFO RESULT " + result.size());
				if (!ServicesUtil.isEmpty(result)) {
					logger.error("[fetchingMissingDataInfoFromDB] INFO RESULT " + result.size());
					missingDataInfoMap = new HashMap<>();
					Object object0 = null;
					Object object1 = null;
					Date date;
					List<String> muwiList = null;
					for (Object[] objects : result) {
						object0 = objects[0];
						object1 = objects[1];
						if (!ServicesUtil.isEmpty(object0) && !ServicesUtil.isEmpty(object1)) {

							date = ServicesUtil.convertFromZoneToZone(null, object0, MurphyConstant.CST_ZONE,
									MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
									MurphyConstant.DATE_DB_FORMATE);

							if (missingDataInfoMap.containsKey(date)) {
								muwiList = missingDataInfoMap.get(date);
								muwiList.add(object1.toString());
							} else {
								muwiList = new ArrayList<>();
								muwiList.add(object1.toString());
								missingDataInfoMap.put(date, muwiList);
							}
						}
					}
					logger.error(
							"[fetchingMissingDataInfoFromDB] INFO missingDataInfoMap " + missingDataInfoMap.size());
				}
			} catch (Exception e) {
				logger.error("[fetchingMissingDataInfoFromDB] Exception While Fetching Data From DB" + e.getMessage());
			}
			try {
				queryString = "DELETE FROM DUMMY_TB";
				q = session.createSQLQuery(queryString);
				long count = q.executeUpdate();
				logger.error("[fetchingMissingDataInfoFromDB] INFO count " + count);
			} catch (Exception e) {
				logger.error("[fetchingMissingDataInfoFromDB] Exception While Deleting Data From DB" + e.getMessage());
			}
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error(
					"[fetchingMissingDataInfoFromDB] Exception in fetchingMissingDataInfoFromDB " + e.getMessage());
		} finally {
			try {
				session.close();
			} catch (Exception e) {
				logger.error("[fetchingMissingDataInfoFromDB] Exception While Closing Session " + e.getMessage());
			}
		}
		return missingDataInfoMap;
	}

	private String setDataForCanary(String startTime, String endTime, String[] params, int insertInterval,
			String insertIntervalType, String aggregateName, List<String> muwiList, String table) {
		String response = MurphyConstant.FAILURE;
		try {
			setStagingDataForCanary(startTime, endTime, params, insertInterval, insertIntervalType, aggregateName,
					muwiList, table);
			response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingDao][setCanaryData][error]" + e.getMessage());
		}
		return response;
	}

	private void setStagingDataForCanary(String startTime, String endTime, String[] params, int insertInterval,
			String insertIntervalType, String aggregateName, List<String> muwiList, String table) {
		// logger.error("[Murphy][CanaryStagingScheduler][getStagingData][init]");

		try {
			String userToken = getUserToken();
			if (!ServicesUtil.isEmpty(userToken)) {
				List<String> payloadList = new ArrayList<String>();
				for (String well : muwiList) {
					for (String param : params) {
						payloadList.add("MUWI_Prod." + well + "." + param);
					}
				}
				if (!ServicesUtil.isEmpty(payloadList)) {
					String payload = getPayloadInString(payloadList);
					JSONObject canaryData = getCanaryDataFor15Mins(userToken, payload, startTime, endTime,
							insertInterval, insertIntervalType, aggregateName);
					revokeUserToken(userToken);
					if (!ServicesUtil.isEmpty(canaryData)) {
						createTasksForCanary(canaryData, payloadList, insertInterval, insertIntervalType, aggregateName,
								startTime + ":00", table);
					}

				}
			}

		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingDao][setStagingData][error]" + e.getMessage());
			e.printStackTrace();
		}

	}

	private JSONObject getCanaryDataFor15Mins(String userToken, String payload, String startTime, String endTime,
			int insertInterval, String insertIntervalType, String aggregateName) throws Exception {
		try {
			String canaryUrl = "api/v1/getTagData";
			String stringInterval = "00";
			String aggregateInterval = "";

			if (insertInterval < 10) {
				stringInterval = "0" + insertInterval;
			} else {
				stringInterval = "" + insertInterval;
			}
			if (MurphyConstant.DAYS.equals(insertIntervalType)) {
				aggregateInterval = stringInterval + ":00:00:00";
			} else if (MurphyConstant.HOURS.equals(insertIntervalType)) {
				aggregateInterval = "0:" + stringInterval + ":00:00";
			} else if (MurphyConstant.MINUTES.equals(insertIntervalType)) {
				aggregateInterval = "0:00:" + stringInterval + ":00";
			} else if (MurphyConstant.SECONDS.equals(insertIntervalType)) {
				aggregateInterval = "0:00:00:" + stringInterval + "";
			}

			// String canaryPayload = "{" + "\"userToken\": \"" + userToken +
			// "\"," + "\"startTime\": \"" + startTime + ":00.0000000-06:00\","
			// + "\"endTime\": \"" + endTime
			// + ":00.0000000-05:00\"," + "\"aggregateName\": \"" +
			// aggregateName + "\"," + "\"aggregateInterval\": \"" +
			// aggregateInterval + "\","
			// + "\"includeQuality\": false," + " \"MaxSize\": 4000000," +
			// "\"continuation\": null," + "\"tags\": " + payload + "" + "}";

			String canaryPayload = "{" + "\"userToken\": \"" + userToken + "\"," + "\"startTime\": \"" + startTime
					+ ":00\"," + "\"endTime\": \"" + endTime + ":00\"," + "\"aggregateName\": \"" + aggregateName
					+ "\"," + "\"aggregateInterval\": \"" + aggregateInterval + "\"," + "\"includeQuality\": false,"
					+ " \"MaxSize\": 4000000," + "\"continuation\": null," + "\"tags\": " + payload + "" + "}";

			if (MurphyConstant.DOP_TABLE.equals(tableName)) {
				logger.error("[Murphy][CanaryStagingScheduler][getStagingData][canaryPayload]" + canaryPayload);
			}

			// String canaryResponse =
			// DestinationUtil.executeWithDest(MurphyConstant.DEST_CANARY,
			// canaryUrl,
			// MurphyConstant.HTTP_METHOD_POST, MurphyConstant.APPLICATION_JSON,
			// "",canaryPayload,"",false);

			org.json.JSONObject canaryResponseObject = RestUtil.callRest(MurphyConstant.CANARY_API_HOST + canaryUrl,
					canaryPayload, MurphyConstant.HTTP_METHOD_POST,
					configDao.getConfigurationByRef(MurphyConstant.CANARY_API_USERID_REF),
					configDao.getConfigurationByRef(MurphyConstant.CANARY_API_PASSWORD_REF));
			String canaryResponse = null;
			JSONParser parser = null;
			JSONObject canaryJson = null;
			if (!ServicesUtil.isEmpty(canaryResponseObject) && canaryResponseObject.toString().contains("data")) {
				canaryResponse = canaryResponseObject.toString();
				System.err.println("[canaryResponse.toString()]" + canaryResponse.toString());
				parser = new JSONParser();
				canaryJson = (JSONObject) parser.parse(canaryResponse.toString());
				return (JSONObject) canaryJson.get("data");

			}
		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingDao][getCanaryData][error]" + e.getMessage());
			throw e;
		}
		return null;
	}

	private void createTasksForCanary(JSONObject data, List<String> payloadList, int insertInterval,
			String insertIntervalType, String aggregateName, String date, String table) {
		// logger.error("[Murphy][CanaryStagingScheduler][createTasks][init]"+
		// data);

		int k = 0;
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			for (String well : payloadList) {
				JSONArray wellData = (JSONArray) data.get(well);
				if (!ServicesUtil.isEmpty(wellData)) {
					for (int i = 0; i < wellData.size(); i++) {
						k++;
						JSONArray wellList = (JSONArray) wellData.get(i);
						CanaryStagingDto canaryDto = new CanaryStagingDto();
						String[] paramList = well.split("\\.");
						canaryDto.setMuwiId(paramList[1]);
						canaryDto.setParameterType(paramList[2]);

						// String dateValue = wellList.get(0).toString();

						canaryDto.setCreatedAt(ServicesUtil.convertFromZoneToZone(null, date, "", "",
								MurphyConstant.DATEFORMAT_FOR_CANARY, MurphyConstant.DATE_DB_FORMATE_SD));
						canaryDto.setCreatedAtInString(ServicesUtil.convertFromZoneToZoneString(null, date, "", "",
								MurphyConstant.DATEFORMAT_FOR_CANARY, MurphyConstant.DATE_DB_FORMATE_SD));

						if (wellList.get(1) != null && wellList.get(1).toString().trim() != "null"
								&& wellList.get(1).toString().trim() != "") {

							System.err.println("wellList.get(1).toString().trim()" + wellList.get(1).toString().trim());
							canaryDto.setDataValue(Double.parseDouble(wellList.get(1).toString().trim()));

							// if
							// (wellList.get(1).getClass().getName().equals("java.lang.Double"))
							// {
							// canaryDto.setDataValue((Double) wellList.get(1));
							// } else if
							// (wellList.get(1).getClass().getName().equals("java.lang.Long"))
							// {
							// canaryDto.setDataValue(((Long)
							// wellList.get(1)).doubleValue());
							// } else if
							// (wellList.get(1).getClass().getName().equals("java.lang.Integer"))
							// {
							// canaryDto.setDataValue(((Integer)
							// wellList.get(1)).doubleValue());
							// }else{
							// System.err.println("wellList.get(1).getClass().getName()"
							// +wellList.get(1).getClass().getName());
							// }
							// if
							// (MurphyConstant.AGGR_NAME_TOTAL.equals(aggregateName))
							// {
							// double x =
							// ServicesUtil.changeTimeUnits(insertInterval,
							// insertIntervalType, MurphyConstant.SECONDS);
							// if (x != 0) {
							// canaryDto.setDataValue(canaryDto.getDataValue() /
							// x);
							// }
							// }

						}
						createStagingForCanary(canaryDto, session, table);
					}
				} else {
					CanaryStagingDto canaryDto = new CanaryStagingDto();
					String[] paramList = well.split("\\.");
					canaryDto.setMuwiId(paramList[1]);
					canaryDto.setParameterType("Data Not Present for PARAM " + paramList[2]);
					canaryDto.setCreatedAt(ServicesUtil.convertFromZoneToZone(null, date, "", "",
							MurphyConstant.DATEFORMAT_FOR_CANARY, MurphyConstant.DATE_DB_FORMATE_SD));
					canaryDto.setCreatedAtInString(ServicesUtil.convertFromZoneToZoneString(null, date, "", "",
							MurphyConstant.DATEFORMAT_FOR_CANARY, MurphyConstant.DATE_DB_FORMATE_SD));
					createStagingForCanary(canaryDto, session, table);
				}
			}
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("[fetchingMissingDataInfoFromDB] Exception in createTasks " + e.getMessage());
		} finally {
			try {
				session.close();
			} catch (Exception e) {
				logger.error("[createTasks] Exception While Closing Session " + e.getMessage());
			}
		}
		logger.error("[Murphy][CanaryStagingDao][createTasks][end][totalCount]" + k);
	}

	public int createStagingForCanary(CanaryStagingDto dto, Session session, String table) {
		int result = 0;
		String queryString = "";
		try {
			queryString = "INSERT INTO " + table + " VALUES(TO_TIMESTAMP('" + dto.getCreatedAtInString()
					+ "', 'yyyy-MM-dd HH24:mi:ss')/*CREATED_AT <VARCHAR(100)>*/," + dto.getDataValue()
					+ "/*DATA_VALUE <DOUBLE>*/," + "'" + dto.getMuwiId() + "'/*MUWI_ID <VARCHAR(100)>*/,'"
					+ dto.getParameterType() + "'/*PARAM_TYPE <VARCHAR(100)>*/";
			queryString = queryString + ")";
			Query q = session.createSQLQuery(queryString);
			// logger.error("[Murphy][CanaryStagingDao][createStaging][queryString]"
			// + queryString);
			result = (Integer) q.executeUpdate();
		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingDao][createStaging][queryString]" + queryString + "[error]"
					+ e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

}