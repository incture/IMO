package com.murphy.taskmgmt.scheduler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.murphy.integration.dao.EnersightProveDailyDao;
import com.murphy.integration.dto.EnersightProveDailyDto;
import com.murphy.taskmgmt.dao.ConfigDao;
import com.murphy.taskmgmt.dao.DOPStagingDao;
import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dto.CanaryRequestDto;
import com.murphy.taskmgmt.dto.CanaryStagingDto;
import com.murphy.taskmgmt.ita.ITADOPActionDto;
import com.murphy.taskmgmt.ita.ITARulesServiceFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.ItaFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;

@Component
public class CanaryStagingScheduler {

	private static final Logger logger = LoggerFactory.getLogger(CanaryStagingScheduler.class);

	@Autowired
	private DOPStagingDao dopStagingDao;

	@Autowired
	private HierarchyDao hierarchyDao;

	@Autowired
	private ConfigDao configDao;
	
	@Autowired
	ITARulesServiceFacadeLocal itaRulesServiceFacadeLocal;
	
	@Autowired
	ItaFacadeLocal itaFacade;

	/***
	 * <ul>
	 * <li>Used by Trends in Alarms, PROVE and Frac Monitor</li>
	 * <li>Copies MAX data for each tags observed for past 15 minutes from
	 * Canary to HANA</li>
	 * </ul>
	 * Note: This code will not try to recover/catch up for missing data
	 */
	//@Scheduled(cron = "0 0/15 * * * ?")
	public void stageData() {
		logger.error("CanaryStagingScheduler.stageData()");
		String endTimeInString = null;// Defaults to currentTime
		try {
			stageData(endTimeInString);
		} catch (Exception e) {
			logger.error("CanaryStagingScheduler.stageData()" + e.getMessage());
		}
	}

	/**
	 * Clears staged data, and copy data from Canary for that slot
	 */
	public int stageData(String endTimeInString) throws Exception {
		logger.error("CanaryStagingScheduler.stageData()" + endTimeInString);
		CanaryRequestDto dto = prepParams(endTimeInString);
		logger.error("CanaryStagingScheduler.stageData()" + dto);
		dopStagingDao.clearStaging(dto.getEndTimeDBFormat());
		if (dto.getCurrentHour() == 0) {
			dopStagingDao.deleteAllStagedBeforeDate(dto.getDeleteTimeDBFormat());
		}
		return copyCanaryData(dto);
	}
	
	public int stagedatatemp(String startTimeDBFormat, String endTimeDBFormat) throws Exception{
		 SimpleDateFormat sdf = new SimpleDateFormat(MurphyConstant.DATE_DB_FORMATE_SD);
		 Date start = sdf.parse(startTimeDBFormat);
		 Date end = sdf.parse(endTimeDBFormat);	
		 Calendar cal = Calendar.getInstance();
		 int rows =0 ;
		 String start_date = null;
		
		 while(end.compareTo(start) > 0){
			 start_date = sdf.format(start);
			 logger.error("start time for this run : " + start_date);
			 rows += stageData(start_date);
			 cal.setTime(start);
			 cal.add(Calendar.MINUTE, 15);	
			 start = cal.getTime();
		 }
		 return rows;
	}

	/**
	 * Prepares parameters to help staging data
	 */
	CanaryRequestDto prepParams(String endTimeDBFormat) {
		logger.debug("CanaryStagingScheduler.prepParams(" + endTimeDBFormat + ")");
		int insertInterval = 15;
		String insertIntervalType = MurphyConstant.MINUTES;
		int roundOffInterval = 15;
		String roundOffIntervalType = MurphyConstant.MINUTES;
		String[] canaryParam = MurphyConstant.CANARY_PARAM;
		String aggregateName = MurphyConstant.AGGR_NAME_MAX;
		Date currentDate = null;
		if (!ServicesUtil.isEmpty(endTimeDBFormat)) {
			DateFormat dfInDBFormat = new SimpleDateFormat(MurphyConstant.DATE_DB_FORMATE_SD);
			try {
				currentDate = dfInDBFormat.parse(endTimeDBFormat);
			} catch (ParseException e) {
				logger.error("CanaryStagingScheduler.prepParams()" + e.getMessage());
				currentDate = new Date();
			}
		} else {
			currentDate = new Date();
		}
		currentDate = roundDateToNearest(currentDate, roundOffInterval, roundOffIntervalType, MurphyConstant.CST_ZONE);
		String endTimeCanaryFormat = ServicesUtil.convertFromZoneToZoneString(currentDate, null, "",
				MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T);
		endTimeDBFormat = ServicesUtil.convertFromZoneToZoneString(currentDate, null, "", MurphyConstant.CST_ZONE, "",
				MurphyConstant.DATE_DB_FORMATE_SD);
		Date startDate = ServicesUtil.getDateWithInterval(currentDate, -insertInterval, insertIntervalType);
		String startTimeCanaryFormat = ServicesUtil.convertFromZoneToZoneString(startDate, null, "",
				MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T);
		String startTimeDBFormat = ServicesUtil.convertFromZoneToZoneString(startDate, null, "",
				MurphyConstant.CST_ZONE, "", MurphyConstant.DATE_DB_FORMATE_SD);
		int deleteInterval = 30;
		String deleteIntervalType = MurphyConstant.DAYS;
		Date deleteDate = ServicesUtil.getDateWithInterval(currentDate, -deleteInterval, deleteIntervalType);
		String deleteTimeDBFormat = ServicesUtil.convertFromZoneToZoneString(deleteDate, null, "",
				MurphyConstant.CST_ZONE, "", MurphyConstant.DATE_DB_FORMATE_SD);
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
		c.setTime(currentDate);
		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		String endOfDayDBFormat = null; // Not used
		return new CanaryRequestDto(startTimeCanaryFormat, endTimeCanaryFormat, startTimeDBFormat, endTimeDBFormat,
				deleteTimeDBFormat, endOfDayDBFormat, currentHour, insertInterval, insertIntervalType, canaryParam,
				aggregateName);
	}

	/**
	 * <ol>
	 * <li>get token to read from Canary</li>
	 * <li>using token get data from Canary for all wells and tags</li>
	 * <li>revoke token</li>
	 * <li>write loaded data to HANA</li>
	 * <li>delete data before 30 days</li>
	 * <li>This code will not try to recover/catch up missing data</li>
	 * </ol>
	 */
	int copyCanaryData(CanaryRequestDto dto) throws Exception {
		logger.debug("CanaryStagingScheduler.copyCanaryData(" + dto + ")");
		// STEP 1: Get token
		String userToken = getUserToken();
		if (ServicesUtil.isEmpty(userToken)) {
			logger.error("CanaryStagingScheduler.copyCanaryData(); getUserToken() returned empty for " + dto);
			return -1; // Flow stops here!
		}
		// STEP 2: Get data
		List<CanaryStagingDto> loadedData = getCanaryData(userToken, dto);
		// STEP 3: Revoke that token
		revokeUserToken(userToken);// Response not required
		if (ServicesUtil.isEmpty(loadedData)) {
			logger.error("CanaryStagingScheduler.copyCanaryData(); getCanaryData() returned empty for " + dto);
			return -1; // Flow stops here!
		}
		logger.debug("CanaryStagingScheduler.copyCanaryData(); getCanaryData() " + loadedData);
		// STEP 4: Write to HANA
		return dopStagingDao.insertToCanaryStaging(loadedData);
	}

	/***
	 * <ul>
	 * <li>If current time is 7AM, load planned production for Enersight and
	 * insert default records for plotting,
	 * <li>else, Copy and save TOTAL data from Canary for last 1hr for QTQILD
	 * tags of all wells, then calculate projected value</li>
	 * <li>Used by DOP Trends</li>
	 * <li>This code will not try to recover/catch up for missing data</li>
	 * </ul>
	 */
	@SuppressWarnings("unchecked")
	//@Scheduled(cron = "0 0/12 17 * * ?") //5:12 pm
    @Scheduled(cron = "0 0 0/1 * * ?")
	public void stageDataForDOP() {
		logger.error("CanaryStagingScheduler.stageDataForDOP()");
		String endTimeInString = null;// Defaults to currentTime
		try {
			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			stagingDataForDOPDGP(endTimeInString);
			
			// ITA-DOP start
			List<ITADOPActionDto> itaDOPActionDto = new ArrayList<ITADOPActionDto>();
			Date currentDate = new Date();
			
			Calendar c = Calendar.getInstance();
			c.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
			c.setTime(currentDate);
			int hour_cst = c.get(Calendar.HOUR_OF_DAY);
			String configuration = configDao.getConfigurationByRef("ITA_DOP_ENABLED");
			/*hour_cst = 4;
			itaFacade.createDopTask(hour_cst);*/
			if(configuration.equalsIgnoreCase("TRUE")){
				itaDOPActionDto = (List<ITADOPActionDto>) itaRulesServiceFacadeLocal.getITARulesByType(MurphyConstant.ITADOPType);
				 if (!ServicesUtil.isEmpty(itaDOPActionDto)) {
					 for (ITADOPActionDto obj : itaDOPActionDto) {
						    int toTime = obj.getTo();
							if(hour_cst == toTime)
							{
								logger.error("CanaryStagingScheduler.ITA-DOP called for hour : "+hour_cst);
								itaFacade.createDopTask(toTime);
								break;
							}
					 }
				 }
			}
			// ITA-DOP end
		} catch (Exception e) {
			logger.error("CanaryStagingScheduler.stageDataForDOP()" + e.getMessage());
		}
	}

	public int stageDataForDOP(String endTimeInString) throws Exception {
		CanaryRequestDto dto = prepParamsForDOP(endTimeInString);
		logger.error("CanaryStagingScheduler.stageDataForDOP()" + dto);
		dopStagingDao.clearDOP(dto.getEndTimeDBFormat());
		if (dto.getCurrentHour() == 7) {
			return setupDefaultsForDOP(dto.getEndTimeDBFormat(), dto.getNextDayDBFormat());
		} else {
			copyCanaryDataForDOP(dto);
			return dopStagingDao.updateDOPProjected(dto.getCurrentHour(), dto.getNextDayDBFormat());
		}
	}

	public int setupDefaultsForDOP(String endTimeDBFormat, String nextDayDBFormat) {
		logger.error("CanaryStagingScheduler.setupDefaultsForDOP()" + endTimeDBFormat + ", " + nextDayDBFormat);
		try {
			dopStagingDao.deleteAllDOPStaged();
			dopStagingDao.insertDefaultDOPRecords(endTimeDBFormat, "QTOILD", MurphyConstant.DOP_CANARY);
			dopStagingDao.insertDefaultDOPRecords(endTimeDBFormat, "QTOILD", MurphyConstant.DOP_PROJECTED);
			dopStagingDao.insertDefaultDOPRecords(nextDayDBFormat, "QTOILD", MurphyConstant.DOP_PROJECTED);
			dopStagingDao.insertDefaultDOPRecords(endTimeDBFormat, "QTOILD", MurphyConstant.DOP_FORECAST);
			
//			dopStagingDao.insertDefaultDOPRecords(endTimeDBFormat, "SEP_CNDTOTTDY", MurphyConstant.DOP_CANARY);
//			dopStagingDao.insertDefaultDOPRecords(endTimeDBFormat, "SEP_CNDTOTTDY", MurphyConstant.DOP_PROJECTED);
//			dopStagingDao.insertDefaultDOPRecords(nextDayDBFormat, "SEP_CNDTOTTDY", MurphyConstant.DOP_PROJECTED);
//			dopStagingDao.insertDefaultDOPRecords(endTimeDBFormat, "SEP_CNDTOTTDY", MurphyConstant.DOP_FORECAST);
			return copyEnerSightDataToHANA(endTimeDBFormat, nextDayDBFormat);

		} catch (Exception e) {
			logger.error("CanaryStagingScheduler.setupDefaultsForDOP()" + e.getMessage());
		}
		return -1;
	}

	int copyEnerSightDataToHANA(String endTimeDBTime, String endOfDay) throws Exception {
		logger.error("CanaryStagingScheduler.copyEnerSightDataToHANA(" + endTimeDBTime + "," + endOfDay + ")");
		/*String versionName = dopStagingDao.getVersionNameOfMonthFromconfig();
		if (ServicesUtil.isEmpty(versionName)) {
			logger.error("CanaryStagingScheduler.copyEnerSightData(); getVersionNameOfMonth returned empty");
		} else {*/
		//Using [IOP_Read].[dbo].[EnersightProveDaily] table instead of the HANA ENERSIGHT_PROVE_DAILY
			List<EnersightProveDailyDto> enersightProveDailyDtoList = new EnersightProveDailyDao().fetchProveDailyData(endTimeDBTime);
			if (!ServicesUtil.isEmpty(enersightProveDailyDtoList)) {
				return dopStagingDao.insertToEnersightStaging(endOfDay, enersightProveDailyDtoList);
			}
		
		return -1;
	}

	public CanaryRequestDto prepParamsForDOP(String endTimeDBFormat) {
		logger.debug("CanaryStagingScheduler.prepParamsForDOP(" + endTimeDBFormat + ")");
		int insertInterval = 1;
		String insertIntervalType = MurphyConstant.HOURS;
		int roundOffInterval = 60;
		String roundOffIntervalType = MurphyConstant.MINUTES;
		String[] canaryParam = MurphyConstant.CANARY_PARAM_PV;
		String aggregateName = MurphyConstant.AGGR_NAME_TOTAL;
		Date currentDate = null;
		if (!ServicesUtil.isEmpty(endTimeDBFormat)) {
			DateFormat dfInDBFormat = new SimpleDateFormat(MurphyConstant.DATE_DB_FORMATE_SD);
			try {
				currentDate = dfInDBFormat.parse(endTimeDBFormat);
			} catch (ParseException e) {
				logger.error("CanaryStagingScheduler.prepParamsForDOP()" + e.getMessage());
				currentDate = new Date();
			}
		} else {
			currentDate = new Date();
		}
		currentDate = roundDateToNearest(currentDate, roundOffInterval, roundOffIntervalType, MurphyConstant.CST_ZONE);
		String endTimeCanaryFormat = ServicesUtil.convertFromZoneToZoneString(currentDate, null, "",
				MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T);
		endTimeDBFormat = ServicesUtil.convertFromZoneToZoneString(currentDate, null, "", MurphyConstant.CST_ZONE, "",
				MurphyConstant.DATE_DB_FORMATE_SD);
		
		Date startDate = ServicesUtil.getDateWithInterval(currentDate, -insertInterval, insertIntervalType);
		String startTimeDBFormat = ServicesUtil.convertFromZoneToZoneString(startDate, null, "",
				MurphyConstant.CST_ZONE, "", MurphyConstant.DATE_DB_FORMATE_SD);
		String startTimeCanaryFormat = ServicesUtil.convertFromZoneToZoneString(startDate, null, "",
				MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T);
		String deleteTimeDBFormat = startTimeDBFormat;
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
		c.setTime(currentDate);
		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		if (currentHour >= 7)
			c.add(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 7);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date endOfDay = c.getTime();
		String endOfDayDBFormat = ServicesUtil.convertFromZoneToZoneString(endOfDay, null, "", MurphyConstant.CST_ZONE,
				"", MurphyConstant.DATE_DB_FORMATE_SD);
		return new CanaryRequestDto(startTimeCanaryFormat, endTimeCanaryFormat, startTimeDBFormat, endTimeDBFormat,
				deleteTimeDBFormat, endOfDayDBFormat, currentHour, insertInterval, insertIntervalType, canaryParam,
				aggregateName);
	}

	int copyCanaryDataForDOP(CanaryRequestDto dto) throws Exception {
		logger.debug("CanaryStagingScheduler.copyCanaryDataForDOP(" + dto + ")");
		// STEP 1: Get token
		String userToken = getUserToken();
		if (ServicesUtil.isEmpty(userToken)) {
			logger.error("CanaryStagingScheduler.copyCanaryDataForDOP(); getUserToken() returned empty for " + dto);
			return -1; // Flow stops here!
		}
		// STEP 2: Get data
		List<CanaryStagingDto> loadedData = getCanaryData(userToken, dto);

		// STEP 3: Revoke that token
		revokeUserToken(userToken);// Response not required
		if (ServicesUtil.isEmpty(loadedData)) {
			logger.error("CanaryStagingScheduler.copyCanaryDataForDOP(); getCanaryData() returned empty for " + dto);
			return -1; // Flow stops here!
		}
		// STEP 4: Write to HANA
		return dopStagingDao.insertToDOPStaging(loadedData);
	}

	public String getUserToken() {
		logger.debug("CanaryStagingScheduler.getUserToken()");
		try {
			
			String username = configDao.getConfigurationByRef(MurphyConstant.CANARY_API_USERID_REF);
			String password = configDao.getConfigurationByRef(MurphyConstant.CANARY_API_PASSWORD_REF);
			String userTokenPayload = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\","
					+ "\"timeZone\":\"" + MurphyConstant.CANARY_TIMEZONE + "\",\"application\":\""
					+ MurphyConstant.CANARY_APP + "\"}";
			String url = MurphyConstant.CANARY_API_HOST + "api/v1/getUserToken";
			String httpMethod = MurphyConstant.HTTP_METHOD_POST;

			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			org.json.JSONObject canaryResponseObject = RestUtil.callRest(url, userTokenPayload, httpMethod, username,
					password);
			if (!ServicesUtil.isEmpty(canaryResponseObject) && canaryResponseObject.toString().contains("userToken")) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(canaryResponseObject.toString());
				return (String) json.get("userToken");
			} else {
				logger.error("CanaryStagingScheduler.getUserToken()[error]: Canary API Response empty");
			}
		} catch (Exception e) {
			logger.error("CanaryStagingScheduler.getUserToken()[error]" + e.getMessage());
		}
		return null;
	}

	public boolean revokeUserToken(String userToken) {
		logger.debug("CanaryStagingScheduler.revokeUserToken(" + userToken + ")");
		try {
			String url = MurphyConstant.CANARY_API_HOST + "api/v1/revokeUserToken";
			String userTokenPayload = "{\"userToken\":\"" + userToken + "\"}";
			String httpMethod = MurphyConstant.HTTP_METHOD_POST;
			String username = configDao.getConfigurationByRef(MurphyConstant.CANARY_API_USERID_REF);
			String password = configDao.getConfigurationByRef(MurphyConstant.CANARY_API_PASSWORD_REF);
			org.json.JSONObject json = RestUtil.callRest(url, userTokenPayload, httpMethod, username, password);
			if (json != null && "Good".equals(json.get("statusCode")))
				return true;
		} catch (Exception e) {
			logger.error("CanaryStagingScheduler.revokeUserToken()[error]" + e.getMessage());
		}
		return false;
	}

	private List<CanaryStagingDto> getCanaryData(String userToken, CanaryRequestDto dto) {
		logger.debug("CanaryStagingScheduler.getCanaryData(" + userToken + ", " + dto + ")");
		try {
			// STEP 1: Prepare request
			List<String> wells = hierarchyDao.getAllWells();
			if (ServicesUtil.isEmpty(wells)) {
				logger.error("CanaryStagingScheduler.getCanaryData(): No wells found in Hierarchy");
				return null;
			}

			String payload = getPayloadInString(wells, dto.getCanaryParam());

			logger.error("CanaryStagingScheduler.getCanaryData.payload" + payload);
			// STEP 2: Execute request
			JSONObject canaryData = getCanaryData(userToken, payload, dto.getStartTimeCanaryFormat(),
					dto.getEndTimeCanaryFormat(), dto.getInsertInterval(), dto.getInsertIntervalType(),
					dto.getAggregateName());
			if (ServicesUtil.isEmpty(canaryData)) {
				logger.error("CanaryStagingScheduler.getCanaryData(): No response from Canary");
				return null;
			}

			// STE 3: Export JSON response to CanaryStagingDto
			List<CanaryStagingDto> resultList = new ArrayList<CanaryStagingDto>();
			for (Object key : canaryData.keySet()) {
				String keyInString = (String) key;
				CanaryStagingDto canaryStagingDto = new CanaryStagingDto();
				int indexOf = keyInString.indexOf('.', 10);
				canaryStagingDto.setMuwiId(keyInString.substring(10, indexOf));
				canaryStagingDto.setParameterType(keyInString.substring(indexOf + 1));
				canaryStagingDto.setCreatedAtInString(dto.getEndTimeDBFormat());
				JSONArray wellData = (JSONArray) canaryData.get(key);
				if (!ServicesUtil.isEmpty(wellData)) {
					for (Object value : wellData) {
						JSONArray wellList = (JSONArray) value;
						Object dataValue = wellList.get(1);
						if (dataValue != null && dataValue != "null") {
							if (dataValue.getClass().getName().equals("java.lang.Double")) {
								canaryStagingDto.setDataValue((Double) dataValue);
							} else if (dataValue.getClass().getName().equals("java.lang.Long")) {
								canaryStagingDto.setDataValue(((Long) dataValue).doubleValue());
							} else if (dataValue.getClass().getName().equals("java.lang.Integer")) {
								canaryStagingDto.setDataValue(((Integer) dataValue).doubleValue());
							}
							if (MurphyConstant.AGGR_NAME_TOTAL.equals(dto.getAggregateName())) {
								// NOTE: dataValue would be divide by 3600,
								// scale down is required to match with
								// AGGR_NAME_MAX output
								double x = ServicesUtil.changeTimeUnits(dto.getInsertInterval(),
										dto.getInsertIntervalType(), MurphyConstant.SECONDS);
								if (x != 0 && canaryStagingDto.getDataValue() != null) {
									canaryStagingDto.setDataValue(canaryStagingDto.getDataValue() / x);
								}
							}
						}
					}
				}
				resultList.add(canaryStagingDto);
			}
			return resultList;
		} catch (Exception e) {
			logger.error("CanaryStagingScheduler.getCanaryData()[error]" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private String getPayloadInString(List<String> wells, String[] canaryParam) {
		List<String> payloadList = new ArrayList<String>();
		for (String well : wells) {
			for (String param : canaryParam) {
				payloadList.add("MUWI_Prod." + well + "." + param);
			}
		}
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
		logger.debug("CanaryStagingScheduler.getCanaryData(userToken=" + userToken + ", payload=" + payload
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", insertInterval=" + insertInterval
				+ ", insertIntervalType=" + insertIntervalType + ", aggregateName=" + aggregateName + ")");
		try {
			String canaryUrl = "api/v1/getTagData";

			String stringInterval = "00";
			if (insertInterval < 10) {
				stringInterval = "0" + insertInterval;
			} else {
				stringInterval = "" + insertInterval;
			}

			String aggregateInterval = "";
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
					+ ":00\"," + "\"endTime\": \"" + endTime + ":00\"," + "\"aggregateName\": \"" + aggregateName
					+ "\"," + "\"aggregateInterval\": \"" + aggregateInterval + "\"," + "\"includeQuality\": false,"
					+ " \"MaxSize\": 4000000," + "\"continuation\": null," + "\"tags\": " + payload + "" + "}";

			logger.error("CanaryStagingScheduler.getCanaryData()[canaryPayload]" + canaryPayload);

			String userName = configDao.getConfigurationByRef(MurphyConstant.CANARY_API_USERID_REF);
			String password = configDao.getConfigurationByRef(MurphyConstant.CANARY_API_PASSWORD_REF);
			// String userName = MurphyConstant.CANARY_USERNAME;
			// String password = MurphyConstant.CANARY_PASSWORD;

			org.json.JSONObject canaryResponseObject = RestUtil.callRest(MurphyConstant.CANARY_API_HOST + canaryUrl,
					canaryPayload, MurphyConstant.HTTP_METHOD_POST, userName, password);
//			if (!ServicesUtil.isEmpty(canaryResponseObject) && canaryResponseObject.toString().contains("data")) {
//				String canaryResponse = canaryResponseObject.toString();
//				logger.debug("CanaryStagingScheduler.getCanaryData()([canaryResponse]" + canaryResponse.toString());
//				JSONParser parser = new JSONParser();
//				JSONObject canaryJson = (JSONObject) parser.parse(canaryResponse.toString());
//				return (JSONObject) canaryJson.get("data");
//			}
//             if (!ServicesUtil.isEmpty(canaryResponseObject) && canaryResponseObject.toString().contains("data")) {
//				
//				String canaryResponse = canaryResponseObject.toString();
//				
//				JSONParser parser = new JSONParser();
//				JSONObject canaryJson = (JSONObject) parser.parse(canaryResponse.toString());
//				
//				if (canaryJson.get("errors").toString().equals("[]"))
//				{
//				logger.debug("CanaryStagingScheduler.getCanaryData()([canaryResponse]" + canaryResponse.toString());
//				
//				return (JSONObject) canaryJson.get("data");
//				}
//				else if(!ServicesUtil.isEmpty(canaryJson.get("errors")))
//				{
//					org.json.simple.JSONArray errorArray= (JSONArray) canaryJson.get("errors");
//					org.json.simple.JSONObject dataObject = (JSONObject) canaryJson.get("data");
//					for(Object obj : errorArray)
//					{
//						String str = obj.toString();
//						int beginIndex = str.indexOf("(");
//						int endIndex= str.indexOf(")");
//						str= str.substring(beginIndex+1, endIndex);
//						if(dataObject.containsKey(str))
//						dataObject.remove(str);
//					}
//					return dataObject;
//				}
//			}
if (!ServicesUtil.isEmpty(canaryResponseObject) && canaryResponseObject.toString().contains("data")) {
				
				String canaryResponse = canaryResponseObject.toString();
				
				JSONParser parser = new JSONParser();
				JSONObject canaryJson = (JSONObject) parser.parse(canaryResponse.toString());
				
				if (canaryJson.get("errors").toString().equals("[]"))
				{
				logger.debug("CanaryStagingScheduler.getCanaryData()([canaryResponse]" + canaryResponse.toString());
				
				return (JSONObject) canaryJson.get("data");
				}
				else if(!ServicesUtil.isEmpty(canaryJson.get("errors")))
				{
					String tagError ="";
					String keyValue="";
					List<String> keysHavingTagerror = new ArrayList<String>();
					org.json.simple.JSONArray errorArray= (JSONArray) canaryJson.get("errors");
					org.json.simple.JSONObject dataObject = (JSONObject) canaryJson.get("data");
					for (Object key : dataObject.keySet()) 
					{
						keyValue =(String) key;
						tagError ="Tag Error ("+keyValue+"): Tag does not exist in the source view";
						if(errorArray.contains(tagError))
							keysHavingTagerror.add(keyValue);
					}
					
					if(!ServicesUtil.isEmpty(keysHavingTagerror))
					{
						for(String errorKey : keysHavingTagerror)
						{
						if(dataObject.containsKey(errorKey))
					    dataObject.remove(errorKey);
						}
					}
//					for(Object obj : errorArray)
//					{
//						String str = obj.toString();
//						int beginIndex = str.indexOf("(");
//						int endIndex= str.indexOf(")");
//						str= str.substring(beginIndex+1, endIndex);
//						if(dataObject.containsKey(str))
//						dataObject.remove(str);
//					}
					return dataObject;
				}
			}
		} catch (Exception e) {
			logger.error("CanaryStagingScheduler.getCanaryData()[error]" + e.getMessage());
			throw e;
		}
		return null;
	}

	Date roundDateToNearest(Date currentDate, int interval, String intervalType, String zoneId) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone(zoneId));
		calendar.setTime(currentDate);
		int value = 0;
		if (MurphyConstant.DAYS.equals(intervalType)) {
			value = calendar.get(Calendar.DATE);
			value -= value % interval;
			calendar.set(Calendar.DATE, value);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} else if (MurphyConstant.HOURS.equals(intervalType)) {
			value = calendar.get(Calendar.HOUR);
			value -= value % interval;
			calendar.set(Calendar.HOUR, value);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} else if (MurphyConstant.MINUTES.equals(intervalType)) {
			value = calendar.get(Calendar.MINUTE);
			value -= value % interval;
			calendar.set(Calendar.MINUTE, value);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} else if (MurphyConstant.SECONDS.equals(intervalType)) {
			value = calendar.get(Calendar.SECOND);
			value -= value % interval;
			calendar.set(Calendar.SECOND, value);
			calendar.set(Calendar.MILLISECOND, 0);
		}
		return calendar.getTime();
	}

	//// @Scheduled(cron = "0 0/15 * * * ?")
	// public void setStagingDataPer15Mins() {
	// System.err.println("setStagingDataPer15Mins started" );
	// canaryStagingDao.setStagingDataForCanary(15, MurphyConstant.MINUTES, 30,
	//// MurphyConstant.DAYS, "TM_CANARY_STAGING_TABLE",15,
	//// MurphyConstant.CANARY_PARAM ,MurphyConstant.AGGR_NAME_MAX);
	// }
	//
	// public Map<Date, List<String>> callingMissingDataProcedure() {
	// System.err.println("callingMissingDataProcedure Entering");
	// return canaryStagingDao.fetchingMissingDataInfoFromDB("2018-08-22
	//// 03:45");
	// }
	//
	// // @Scheduled(fixedDelay = 3600000)
	// // public void setStagingData() {
	// // stagingDao.setStagingData(15, MurphyConstant.MINUTES, 3,
	//// MurphyConstant.DAYS, "TM_CANARY_STAGING",15,
	//// MurphyConstant.CANARY_PARAM ,MurphyConstant.AGGR_NAME_MAX);
	// // }
	//
	// /*
	// * This Scheduler is running in every 15 minutes to fetch data from Canary
	//// for "PRCASXIN", "PRTUBXIN", "PRSTAXIN", "QTGASD", "QTOILD", "QTH2OD"
	//// tags.
	// */
	// /*@Scheduled(cron = "0 0/15 * * * ?")
	// public void setStagingDataPer15Mins() {
	// System.err.println("setStagingDataPer15Mins started" );
	// stagingDao.setStagingData(15, MurphyConstant.MINUTES, 30,
	//// MurphyConstant.DAYS, "TM_CANARY_STAGING_DATA",15,
	//// MurphyConstant.CANARY_PARAM ,MurphyConstant.AGGR_NAME_MAX);
	// }*/
	//
	// /* changes to be done from here*/
	// // @Scheduled(cron = "0 0 0/1 * * ?")
	// public void setStagingDataForHr() {
	// canaryStagingDao.setStagingData(1, MurphyConstant.HOURS, 30,
	//// MurphyConstant.DAYS, "TM_CANARY_STAGING_NDV",60,
	//// MurphyConstant.CANARY_PARAM_NDV ,MurphyConstant.AGGR_NAME_MAX);
	// }
	// /* End */
	//
	//
	// /*Changes to be removed in next deployment */
	// // public void setStagingDataForRemainingParams(Date startDate, Date
	//// endDate) {
	// // stagingDao.setStagingDataForRest(1, MurphyConstant.HOURS, 30,
	//// MurphyConstant.DAYS, "TM_CANARY_STAGING_NDV",60,
	//// MurphyConstant.CANARY_PARAM_REMAINING_NDV
	//// ,MurphyConstant.AGGR_NAME_MAX, startDate, endDate);
	// // }
	//
	// /*End*/
	//
	//
	//// @Scheduled(cron = "0 0 0/1 * * ?")
	// // @Scheduled(fixedDelay = 36000000)
	// public void productionVarianceSch() {
	// // System.err.println("inside schedulare fixed delay");
	// List<LocationHierarchyDto> locList = locDao.getAllWellsWithName();
	// canaryStagingDao.setStagingData(1, MurphyConstant.HOURS, 1 ,
	//// MurphyConstant.DAYS, MurphyConstant.DOP_TABLE,60,
	//// MurphyConstant.CANARY_PARAM_PV ,MurphyConstant.AGGR_NAME_TOTAL);
	// Date currentDate = ServicesUtil.roundDateToNearInterval(new Date(), 60,
	//// MurphyConstant.MINUTES);
	// currentDate = ServicesUtil.getDateWithInterval(currentDate, -300,
	//// MurphyConstant.MINUTES);
	// Calendar c = Calendar.getInstance();
	// c.setTime(currentDate);
	// int currentHr = c.get(Calendar.HOUR_OF_DAY);
	// int currentDay = c.get(Calendar.DAY_OF_WEEK);
	//// int currentDateOfMonth = c.get(Calendar.DAY_OF_MONTH);
	//// int currentMonth = c.get(Calendar.MONTH) + 1;
	// c.set(Calendar.SECOND, 0);
	// c.set(Calendar.MILLISECOND, 0);
	//
	// currentDate = c.getTime();
	//
	//
	// String currentDateInString =
	//// ServicesUtil.convertFromZoneToZoneString(currentDate, null , "", "",
	//// "", MurphyConstant.DATE_DB_FORMATE_SD);
	//
	// String endOfDayString =
	//// ServicesUtil.convertFromZoneToZoneString(scaleDownTimeToEndTime(currentDate).getTime(),
	//// null , "", "", "", MurphyConstant.DATE_DB_FORMATE_SD);
	// Date endOfWeekDate =
	//// productionVarianceDao.getMeNextMonday(currentDate).getTime();
	// String endOfWeekDateString =
	//// ServicesUtil.convertFromZoneToZoneString(productionVarianceDao.scaleDownTimeToSeventhHourOfDay(endOfWeekDate).getTime(),
	//// null , "", "", "", MurphyConstant.DATE_DB_FORMATE_SD);
	// // String currentDateAt12 =
	//// ServicesUtil.convertFromZoneToZoneString(scaleDownTimeToZeroHr(currentDate).getTime(),
	//// null , "", "", "", MurphyConstant.DATE_DB_FORMATE_SD);
	//
	// System.err.println("[CanaryStagingScheduler][productionVarianceSch]\n[currentDate]"+currentDate+"\n[currentDateInString]"+currentDateInString+""
	// +
	//// "\n[currentHr]"+currentHr+"\n[currentDay]"+currentDay+"\n[endOfDayString]"+endOfDayString+"\n[endOfWeekDate]"+endOfWeekDate+"\nendOfWeekDateString"+endOfWeekDateString);
	//
	// if(currentHr != 7 ){
	// if(currentHr < 7){
	// currentHr = currentHr + 24;
	// }
	//
	// // c.add(Calendar.HOUR_OF_DAY, -1);
	// // String currentDateInSt =
	//// ServicesUtil.convertFromZoneToZoneString(c.getTime(), null , "", "",
	//// "", MurphyConstant.DATE_DB_FORMATE_SD);
	// productionVarianceDao.updateDailyVariance(currentHr,currentDateInString,
	//// endOfDayString);
	// }else{
	// Calendar calNext = Calendar.getInstance();
	// calNext.setTime(currentDate);
	// calNext.add(Calendar.DATE, 1);
	// Date nextDate = calNext.getTime();
	// String nextDateInString =
	//// ServicesUtil.convertFromZoneToZoneString(nextDate, null , "", "", "",
	//// MurphyConstant.DATE_DB_FORMATE_SD);
	//
	// if(currentDay == 2){
	// productionVarianceDao.deleteWeeklyData(currentDateInString);
	//
	// }else{
	//
	// int currentDayForVar = 1;
	//
	// if(currentDay == 1){
	// currentDayForVar = 6;
	// }else{
	// currentDayForVar = currentDay-2;
	// }
	// productionVarianceDao.updateWeeklyVariance(currentDayForVar,currentDateInString,
	//// endOfWeekDateString);
	// productionVarianceDao.updateDailyOfWeeklyVariance(currentDateInString);
	// productionVarianceDao.deleteDailyData(currentDateInString);
	// }
	//
	// productionVarianceDao.createRecord();
	//
	// for(LocationHierarchyDto dto : locList ){
	// if(currentDay == 2){
	// productionVarianceDao.createRecordForStagingVariance(endOfWeekDateString
	// , 0, dto.getMuwi(), "QTOILD", dto.getLocationText(),
	//// MurphyConstant.DOP_PROJECTED, MurphyConstant.WEEKLY);
	// //
	//// productionVarianceDao.createRecordForStagingVariance(currentDateInString
	// // , 0, dto.getMuwi(), "QTOILD", dto.getLocationText(),
	//// MurphyConstant.DOP_PROJECTED, MurphyConstant.WEEKLY);
	// //
	//// productionVarianceDao.createRecordForStagingVariance(currentDateInString
	// // , 0, dto.getMuwi(), "QTOILD", dto.getLocationText(),
	//// MurphyConstant.DOP_CANARY, MurphyConstant.WEEKLY);
	//
	// }
	//
	//
	// productionVarianceDao.createRecordForStagingVariance(nextDateInString
	// , 0, dto.getMuwi(), "QTOILD", dto.getLocationText(),
	//// MurphyConstant.DOP_CANARY, MurphyConstant.WEEKLY);
	// productionVarianceDao.createRecordForStagingVariance(currentDateInString
	// , 0, dto.getMuwi(), "QTOILD", dto.getLocationText(),
	//// MurphyConstant.DOP_CANARY, MurphyConstant.DAILY);
	// productionVarianceDao.createRecordForStagingVariance(endOfDayString
	// , 0, dto.getMuwi(), "QTOILD", dto.getLocationText(),
	//// MurphyConstant.DOP_PROJECTED, MurphyConstant.DAILY);
	// productionVarianceDao.createRecordForStagingVariance(currentDateInString
	// , 0, dto.getMuwi(), "QTOILD", dto.getLocationText(),
	//// MurphyConstant.DOP_PROJECTED, MurphyConstant.DAILY);
	// //Ritika
	// productionVarianceDao.createRecordForStagingVariance(currentDateInString
	// , 0, dto.getMuwi(), "QTOILD", dto.getLocationText(),
	//// MurphyConstant.DOP_FORECAST, MurphyConstant.DAILY);
	// }
	//
	// }
	// }
	//
	//
	//// @Scheduled(cron = "0 0 12 * * ?")
	//// // @Scheduled(fixedDelay = 100000)
	//// public void stagingProveData() {
	//// String s1 = productionVarianceDao.createRecord();
	//// System.err.println("[Murphy][ProductionVarianceSchedular][stagingProveDataForDOP][end][Message]
	//// :"+ s1+"[At]"+new Date());
	//// }
	//
	// public Calendar scaleDownTimeToEndTime(Date date) {
	// Calendar c = Calendar.getInstance();
	// c.setTime(date);
	// if(c.get(Calendar.HOUR_OF_DAY) >= 7) {
	// c.add(Calendar.DATE, +1);
	// }
	// c.set(Calendar.HOUR_OF_DAY, 07);
	// c.set(Calendar.MINUTE, 0);
	// c.set(Calendar.SECOND, 0);
	// c.set(Calendar.MILLISECOND, 0);
	// return c;
	// }
	//
	// public Calendar scaleDownTimeToZeroHr(Date date) {
	// Calendar c = Calendar.getInstance();
	// c.setTime(date);
	// c.set(Calendar.HOUR_OF_DAY, 00);
	// c.set(Calendar.MINUTE, 0);
	// c.set(Calendar.SECOND, 0);
	// c.set(Calendar.MILLISECOND, 0);
	// return c;
	// }
	//
	// /* commented for validation of weekly data */
	// // int dayInCurrentMonth ,daysInPrevsMonth;
	// //
	// // if(currentDay == 0){
	// // dayInCurrentMonth = 1;
	// // daysInPrevsMonth = 6;
	// // }else{
	// // dayInCurrentMonth = (8 - currentDay);
	// // daysInPrevsMonth = 7 - dayInCurrentMonth;
	// // }
	//
	//
	// // if(currentDateOfMonth != 1 ){
	// //
	//// productionVarianceDao.updatePlannedVariance(0,7,currentMonth,currentDateAt12);
	// // }
	//
	//
	//
	// // if(currentDateOfMonth == 1 ){
	// //
	//// productionVarianceDao.updatePlannedVariance(daysInPrevsMonth,dayInCurrentMonth,currentMonth,currentDateAt12);
	// // }
	
	
	//added by ayesha for dop/dgp
	public int stagingDataForDOPDGP(String endTimeInString) throws Exception {

		int returnValue=0;
		int restefsOil=0;
		int resetefsgas=0;
		int resetcaoil=0;
		int resetgaskaybob=0;
		int mntny1gas=0;
		int mntny2gas=0;
		
		
		//EFS oil
		String zoneoffsetEFSOil = configDao.getConfigurationByRef("EFS_QTOILD_ZONE_CANARY");
		String restHourEFSOil = configDao.getConfigurationByRef("EFS_QTOILD_REST_TIME_CANARY");
		
		String zoneEFSOil = getZoneFromOffset(zoneoffsetEFSOil);
		
		int currentHourEFSOil = getCurrentHour(zoneEFSOil,restHourEFSOil);
		
//		//EFS GAS
		String zoneoffsetEFSGas = configDao.getConfigurationByRef("EFS_QTGASD_ZONE_CANARY");
		String restHourEFSGas = configDao.getConfigurationByRef("EFS_QTGASD_REST_TIME_CANARY");
			
		String zoneEFSGas = getZoneFromOffset(zoneoffsetEFSGas);
		int currentHourEFSGas = getCurrentHour(zoneEFSGas,restHourEFSGas);
		
		
		//Canada Oil
		String zoneoffsetCAOil = configDao.getConfigurationByRef("CA_SEP_CNDTOTTDY_ZONE_CANARY");
		String restHourCAOil = configDao.getConfigurationByRef("CA_SEP_CNDTOTTDY_REST_TIME_CANARY");
			
		String zoneCAOil = getZoneFromOffset(zoneoffsetCAOil);
		int currentHourCAOil = getCurrentHour(zoneCAOil,restHourCAOil);
		
		//CanadaGasKaybob
		String zoneoffsetCAGasKaybob = configDao.getConfigurationByRef("CA_SEP_PRDTOTTDY_ZONE_CANARY");
		String restHourCAGasKaybob = configDao.getConfigurationByRef("CA_SEP_PRDTOTTDY_REST_TIME_CANARY");
		
		String zoneCAGasKaybob = getZoneFromOffset(zoneoffsetCAGasKaybob);
		int currentHourCAGasKaybob = getCurrentHour(zoneCAGasKaybob,restHourCAGasKaybob);
		
		//CanadaGasMontneyOne
		String zoneoffsetCAGasMontneyOne = configDao.getConfigurationByRef("CA_AFLOW_ZONE_CANARY");
		String restHourCAMontneyOne = configDao.getConfigurationByRef("CA_AFLOW_REST_TIME_CANARY");
		
		String zoneCAGasMontneyOne = getZoneFromOffset(zoneoffsetCAGasMontneyOne);
		int currentHourCAMontneyOne = getCurrentHour(zoneCAGasMontneyOne,restHourCAMontneyOne);
		
		//CanadaGasMontneyTwo
		String zoneoffsetCAGasMontneyTwo = configDao.getConfigurationByRef("CA_PG_FLOWD_ZONE_CANARY");
		String restHourCAMontneyTwo = configDao.getConfigurationByRef("CA_PG_FLOWD_REST_TIME_CANARY");
		

		String zoneCAGasMontneyTwo = getZoneFromOffset(zoneoffsetCAGasMontneyTwo);
		int currentHourCAMontneyTwo = getCurrentHour(zoneCAGasMontneyTwo,restHourCAMontneyTwo);
				
		//rest check efs oil
		
		if(!ServicesUtil.isEmpty(restHourEFSOil))
		restefsOil =Integer.valueOf(restHourEFSOil);
			
		if (currentHourEFSOil == restefsOil) {
//			
			String[] canarayParam = {MurphyConstant.CANARY_PARAM_PV[0]};
			CanaryRequestDto dto = prepParamsForDOPDGP(zoneEFSOil,canarayParam,zoneoffsetEFSOil,
					true,false,false,false,false,false,endTimeInString);
			List<String> wells= hierarchyDao.getAllWells(true,false);
			List<String> muwisHavingReuiredTagdata = browseTags(wells,MurphyConstant.CANARY_PARAM_PV[0]);
			returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_PARAM_PV[0]);
		}
		else {
			String param = MurphyConstant.CANARY_PARAM_PV[0] ;
			String[] canarayParam = {param};
			CanaryRequestDto dto = prepParamsForDOPDGP(zoneEFSOil,canarayParam,
					zoneoffsetEFSOil,true,false,false,false,false,false,endTimeInString);
			//fetch actuals and insert to hana
			copyCanaryDataForDOP(dto,true,false,param);
			logger.error("projected next date efsoil " +dto.getNextDayDBFormat());
			returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),
					param,true,false,false,false,false,false,restefsOil);
		}
		
		//reset check efs gas
		if(!ServicesUtil.isEmpty(restHourEFSGas))
			resetefsgas =Integer.valueOf(restHourEFSGas);
		if (currentHourEFSGas == resetefsgas) {
			String param = MurphyConstant.CANARY_PARAM[3] ;
			String[] canarayParam = {param};
			CanaryRequestDto dto = prepParamsForDOPDGP(zoneEFSGas,canarayParam,zoneoffsetEFSGas,
					false,true,false,false,false,false,endTimeInString);
			List<String> wells= hierarchyDao.getAllWells(true,false);
			List<String> muwisHavingReuiredTagdata = browseTags(wells,MurphyConstant.CANARY_PARAM[3]);
			returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_PARAM[3]);
		} else {
			String param = MurphyConstant.CANARY_PARAM[3] ;
			String[] canarayParam = {param};
			CanaryRequestDto dto = prepParamsForDOPDGP(zoneEFSGas,canarayParam,
					zoneoffsetEFSGas,false,true,false,false,false,false,endTimeInString);
			copyCanaryDataForDOP(dto,true,false,param);
			logger.error("projected next date efsgas " +dto.getNextDayDBFormat());
			returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),
					param,false,true,false,false,false,false,resetefsgas);
		}

		//reset check ca oil
		if(!ServicesUtil.isEmpty(restHourCAOil))
		resetcaoil = Integer.valueOf(restHourCAOil);
		if (currentHourCAOil == resetcaoil) {
			String param = MurphyConstant.CANARY_CANADA_PARAM[0] ;
			String[] canarayParam = {param};
			CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAOil,canarayParam,zoneoffsetCAOil,
					false,false,true,false,false,false,endTimeInString);
			List<String> wells= hierarchyDao.getAllWells(false,true);
			List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
			returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_CANADA_PARAM[0]);
		} 
		else {
			String param = MurphyConstant.CANARY_CANADA_PARAM[0] ;
			String[] canarayParam = {param};
			CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAOil,canarayParam,zoneoffsetCAOil,
					false,false,true,false,false,false,endTimeInString);
			copyCanaryDataForDOP(dto,false,true,param);
			logger.error("projected next date caoil " +dto.getNextDayDBFormat());
			returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),param,
					false,false,true,false,false,false,resetcaoil);
		}
//		
//		//reset check ca gas kaybob
		if (currentHourCAGasKaybob == Integer.valueOf(restHourCAGasKaybob)) {
			String param = MurphyConstant.CANARY_CANADA_PARAM[1] ;
			String[] canarayParam = {param};
			List<String> wells= hierarchyDao.getAllWells(false,true);
			//List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
			List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
			CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasKaybob,canarayParam,zoneoffsetCAGasKaybob,
					false,false,false,true,false,false,endTimeInString);
			returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_CANADA_PARAM[1]);
		} 
		else {
			String param = MurphyConstant.CANARY_CANADA_PARAM[1] ;
			String[] canarayParam = {param};
			CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasKaybob,canarayParam,zoneoffsetCAGasKaybob,
					false,false,false,true,false,false,endTimeInString);
			copyCanaryDataForDOP(dto,false,true,param);
			logger.error("projected next date cagas " +dto.getNextDayDBFormat());
			returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),
					param,false,false,false,true,false,false,Integer.valueOf(restHourCAGasKaybob));
		}
		
		//reset check ca gas monteyone
		
		if (currentHourCAMontneyOne == Integer.valueOf(restHourCAMontneyOne)) {
			String param = MurphyConstant.CANARY_CANADA_PARAM[2] ;
			String[] canarayParam = {param};
			List<String> wells= hierarchyDao.getAllWells(false,true);
			//List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
			List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
			CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasMontneyOne,canarayParam,zoneoffsetCAGasMontneyOne,
					false,false,false,false,true,false,endTimeInString);
			returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_CANADA_PARAM[2]);
		} 
		else {
			String param = MurphyConstant.CANARY_CANADA_PARAM[2] ;
			String[] canarayParam = {param};
			CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasMontneyOne,canarayParam,zoneoffsetCAGasMontneyOne,
					false,false,false,false,true,false,endTimeInString);
			copyCanaryDataForDOP(dto,false,true,param);
			logger.error("projected next date mtnygas1 " +dto.getNextDayDBFormat());
			returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),
					param,false,false,false,false,true,false,Integer.valueOf(restHourCAMontneyOne));
		}
		
		//reset check ca gas monteytwo
		if (currentHourCAMontneyTwo == Integer.valueOf(restHourCAMontneyTwo)) {
			String param = MurphyConstant.CANARY_CANADA_PARAM[3] ;
			String[] canarayParam = {param};
			List<String> wells= hierarchyDao.getAllWells(false,true);
			List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
			CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasMontneyTwo,canarayParam,zoneoffsetCAGasMontneyTwo,
					false,false,false,false,false,true,endTimeInString);
			returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_CANADA_PARAM[3]);
		} 
		
		else {
			String param = MurphyConstant.CANARY_CANADA_PARAM[3] ;
			String[] canarayParam = {param};
			CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasMontneyTwo,canarayParam,zoneoffsetCAGasMontneyTwo,
					false,false,false,false,false,true,endTimeInString);
			copyCanaryDataForDOP(dto,false,true,param);
			logger.error("projected next date mtnygas2 " +dto.getNextDayDBFormat());
			returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),
					param,false,false,false,false,false,true,Integer.valueOf(restHourCAMontneyTwo));
		}
		return returnValue;
		
		
	}

	private String getZoneFromOffset(String zoneOffset) {
		String zone= "";
		String str="";
		int offset=0;
	if(zoneOffset.contains(":"))
	{
		 int posA = zoneOffset.indexOf(":");
		        if (posA == -1) {
		        }
		        str = zoneOffset.substring(0, posA);
		        System.out.println(""+posA);
		       str =str.replaceAll("[a-zA-Z]","");
		      System.out.println("hour " +str);
		      String a=":";
		      int hourValue= Integer.valueOf(str);
		      int offsethr = hourValue*60*60*1000;
		       int adjustedPosA = posA + a.length();
		        if (adjustedPosA >= zoneOffset.length()) {
		          
		        }
		        String min= zoneOffset.substring(adjustedPosA);
		        System.out.println("min "+ zoneOffset.substring(adjustedPosA));
		        int mins = Integer.valueOf(min);
		        int offsetmin = mins * 60 * 1000;
		        offset= offsethr+offsetmin;
	}
				
				else{
					 
				      str =zoneOffset.replaceAll("[a-zA-Z]","");
				      System.out.println("hour " +str);
				      int hourValue= Integer.valueOf(str);
				      offset= hourValue*60*60*1000;
				}
					
	                 TimeZone tz = TimeZone.getTimeZone("UTC");
		         
				     String[] availableIDs = tz.getAvailableIDs(offset);
				    
				     for(int i = 0; i < availableIDs.length; i++) {
					   TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of(availableIDs[i]));
					   System.out.println("zone = " +timeZone.getDisplayName());
					   zone = timeZone.getDisplayName(false, TimeZone.SHORT);
					   break;
				     }
					return zone;	
	}
	
	private int getCurrentHour(String zone, String resetHour) {

		int roundOffInterval = 60;
		String roundOffIntervalType = MurphyConstant.MINUTES;
		Date currentDate = null ;
		int currentHour = 0;
		try
		{
			currentDate = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
			format.setTimeZone(TimeZone.getTimeZone(zone));
			String dateAccordingToZone =format.format(new Date());
			currentDate = format.parse(dateAccordingToZone);
			
			currentDate = roundDateToNearest(currentDate, roundOffInterval, roundOffIntervalType,zone);
			Calendar c = Calendar.getInstance();
			c.setTimeZone(TimeZone.getTimeZone(zone));
			c.setTime(currentDate);
			currentHour = c.get(Calendar.HOUR_OF_DAY);
		}
		catch(Exception e)
		{
			logger.error("Canary Staging method : getCurrentHour "+e.getMessage());
		}
		return currentHour;
	}
	
	public CanaryRequestDto prepParamsForDOPDGP(String zone,String[] canarayParam,String offset
			,boolean isEFSOil, boolean isEFSGas, boolean isCAOil, boolean isCAKaybobGas,
			boolean isCAMontney1Gas, boolean isCAMontney2Gas,String endTimeInString) {
		
		int insertInterval = 1;
		String insertIntervalType = MurphyConstant.HOURS;
		int roundOffInterval = 60;
		String roundOffIntervalType = MurphyConstant.MINUTES;
		String aggregateName = MurphyConstant.AGGR_NAME_TOTAL;
		Date currentDate = null;
		String endTimeDBFormat = "";
		if (!ServicesUtil.isEmpty(endTimeInString)) {
			DateFormat dfInDBFormat = new SimpleDateFormat(MurphyConstant.DATE_DB_FORMATE_SD);
			try {
				currentDate = dfInDBFormat.parse(endTimeInString);
			} catch (ParseException e) {
				logger.error("CanaryStagingScheduler.prepParamsForDOP()" + e.getMessage());
				currentDate = new Date();
			}
		}
		else
		{
		currentDate = new Date();
		}
		offset= offset.replaceAll("[a-zA-Z]", "");
	
		String dateFormatForCanary = MurphyConstant.DATEFORMAT_T_CANARY +".0000000"+offset;
		
		currentDate = roundDateToNearest(currentDate, roundOffInterval, roundOffIntervalType,zone);
	    
		Date startDate = ServicesUtil.getDateWithInterval(currentDate, -insertInterval, insertIntervalType);
	    
		SimpleDateFormat format = new SimpleDateFormat(MurphyConstant.DATEFORMAT_T_CANARY );
		format.setTimeZone(TimeZone.getTimeZone(zone));
		String startTimeCanaryFormat  =format.format(startDate);
		startTimeCanaryFormat= startTimeCanaryFormat+".0000000"+offset;
		
		String endTimeCanaryFormat = format.format(currentDate);
		endTimeCanaryFormat= endTimeCanaryFormat+".0000000"+offset;
		
		
		String startTimeDBFormat = ServicesUtil.convertFromZoneToZoneString(startDate, null, "",
				MurphyConstant.UTC_ZONE, "", MurphyConstant.DATE_DB_FORMATE_SD);
		
		endTimeDBFormat = ServicesUtil.convertFromZoneToZoneString(currentDate, null, "",MurphyConstant.UTC_ZONE, "",
				MurphyConstant.DATE_DB_FORMATE_SD);
		
	
	
		String deleteTimeDBFormat = startTimeDBFormat;
		Date endOfDay = null;
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone(zone));
		c.setTime(currentDate);
		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		if(isEFSOil)
		{
		if (currentHour >= 6)
		c.add(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 6);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		}
		if(isEFSGas)
		{
			if (currentHour >= 8)
			c.add(Calendar.DATE, 1);
			c.set(Calendar.HOUR_OF_DAY, 8);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			
		}
		if(isCAOil)
		{
			if (currentHour >= 8)
			c.add(Calendar.DATE, 1);
			c.set(Calendar.HOUR_OF_DAY, 8);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			
		}
		if(isCAKaybobGas)
		{
			if (currentHour >= 8)
			c.add(Calendar.DATE, 1);
			c.set(Calendar.HOUR_OF_DAY, 8);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			
		}
		if(isCAMontney1Gas)
		{
			if (currentHour >= 8)
			c.add(Calendar.DATE, 1);
			c.set(Calendar.HOUR_OF_DAY, 8);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			
		}
		if(isCAMontney2Gas)
		{
			if (currentHour >= 8)
			c.add(Calendar.DATE, 1);
			c.set(Calendar.HOUR_OF_DAY, 8);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			
		}
		endOfDay = c.getTime();
		String endOfDayDBFormat = ServicesUtil.convertFromZoneToZoneString(endOfDay, null, "", MurphyConstant.UTC_ZONE,
				"", MurphyConstant.DATE_DB_FORMATE_SD);
		return new CanaryRequestDto(startTimeCanaryFormat, endTimeCanaryFormat, startTimeDBFormat, endTimeDBFormat,
				deleteTimeDBFormat, endOfDayDBFormat, currentHour, insertInterval, insertIntervalType, canarayParam,
				aggregateName);
	}
	public List<String> browseTags(List<String> wells,String tag)
	{
		List<String> muwiList = new ArrayList<String>();
		try{
		// Canary Url for Fetching Tags
        String url = MurphyConstant.CANARY_API_HOST + "api/v1/browseTags";
        
       
        String userToken =getUserToken();
        for(String well:wells)
        {

        String path = "MUWI_Prod."+well+"";
        String payLoad = "{\"userToken\":\"" + userToken + "\",\"path\":\"" + path + "\"}";
        String httpMethod = MurphyConstant.HTTP_METHOD_POST;
        logger.error("Canary payload for browseNodes " + payLoad);



        org.json.JSONObject canaryResponseObject = RestUtil.callRest(url, payLoad, httpMethod);
        if (!ServicesUtil.isEmpty(canaryResponseObject) && canaryResponseObject.toString().contains("tags")) {
			String canaryResponse = canaryResponseObject.toString();
			logger.debug("CanaryStagingScheduler.getCanaryData()([canaryResponse]" + canaryResponse.toString());
			JSONParser parser = new JSONParser();
			JSONObject canaryJson = (JSONObject) parser.parse(canaryResponse.toString());
			if(!ServicesUtil.isEmpty(canaryJson))
			{
				canaryJson.containsValue("MUWI.Prod"+well+tag);
				muwiList.add(well);
				
			}
        }
      
        }
        revokeUserToken(userToken);
		}
		
		catch(Exception e)
		{
			logger.error("browsetags : canarystagingscheduler " +e.getMessage());
		}
		return muwiList;
	}
	private int setupDefaultValuesForDOPDGP(List<String> muwiList,String endTimeDBFormat, String nextDayDBFormat,String tag) {
		logger.error("CanaryStagingScheduler.setupDefaultsForDOP()" + endTimeDBFormat + ", " + nextDayDBFormat);
		try {
			dopStagingDao.deleteAllDOPStagedDataForTags(tag);
			
			if(tag.equalsIgnoreCase(MurphyConstant.CANARY_PARAM_PV[0]) 
					|| tag.equalsIgnoreCase(MurphyConstant.CANARY_PARAM[3]) )
			{
			dopStagingDao.insertDefaultDOPRecordsForEFS(endTimeDBFormat,tag, MurphyConstant.DOP_CANARY,muwiList);
			dopStagingDao.insertDefaultDOPRecordsForEFS(endTimeDBFormat,tag, MurphyConstant.DOP_PROJECTED,muwiList);
			dopStagingDao.insertDefaultDOPRecordsForEFS(nextDayDBFormat,tag, MurphyConstant.DOP_PROJECTED,muwiList);
			dopStagingDao.insertDefaultDOPRecordsForEFS(endTimeDBFormat,tag, MurphyConstant.DOP_FORECAST,muwiList);
			logger.error("endTimeDbFormat : "+endTimeDBFormat);
			logger.error("nextDayDbFormat : "+nextDayDBFormat);
			copyEnerSightDataToHANA(endTimeDBFormat, nextDayDBFormat,tag);
					
			}
			
			else{
				dopStagingDao.insertDefaultDOPRecordsForCanada(endTimeDBFormat,tag, MurphyConstant.DOP_CANARY,muwiList);
				dopStagingDao.insertDefaultDOPRecordsForCanada(endTimeDBFormat,tag, MurphyConstant.DOP_PROJECTED,muwiList);
				dopStagingDao.insertDefaultDOPRecordsForCanada(nextDayDBFormat,tag, MurphyConstant.DOP_PROJECTED,muwiList);
				dopStagingDao.insertDefaultDOPRecordsForCanada(endTimeDBFormat,tag, MurphyConstant.DOP_FORECAST,muwiList);
				logger.error("endTimeDbFormat : "+endTimeDBFormat);
				logger.error("nextDayDbFormat : "+nextDayDBFormat);
				copyEnerSightDataToHANA(endTimeDBFormat, nextDayDBFormat,tag);
			}
			
			

		} catch (Exception e) {
			logger.error("CanaryStagingScheduler.setupDefaultsForDOP()" + e.getMessage());
		}
		return -1;
		
	}
	
	 int copyCanaryDataForDOP(CanaryRequestDto dto, boolean isEFS,boolean isCA,String tag) throws Exception {
			logger.debug("CanaryStagingScheduler.copyCanaryDataForDOP(" + dto + ")");
			
			// STEP 1: Get token
			String userToken = getUserToken();
			if (ServicesUtil.isEmpty(userToken)) {
				logger.error("CanaryStagingScheduler.copyCanaryDataForDOP(); getUserToken() returned empty for " + dto);
				return -1; // Flow stops here!
			}
			// STEP 2: Get data
			List<CanaryStagingDto> loadedData = getCanaryData(userToken, dto ,isEFS,isCA,tag);

			// STEP 3: Revoke that token
			revokeUserToken(userToken);// Response not required
			if (ServicesUtil.isEmpty(loadedData)) {
				logger.error("CanaryStagingScheduler.copyCanaryDataForDOP(); getCanaryData() returned empty for " + dto);
				return -1; // Flow stops here!
			}
			// STEP 4: Write to HANA
			return dopStagingDao.insertToDOPStaging(loadedData);
			
		}
	 

		int copyEnerSightDataToHANA(String endTimeDBTime, String endOfDay,String tag) throws Exception {
			logger.error("CanaryStagingScheduler.copyEnerSightDataToHANA(" + endTimeDBTime + "," + endOfDay + ")");
			
				List<EnersightProveDailyDto> enersightProveDailyDtoList = new EnersightProveDailyDao().fetchProveDailyData(endTimeDBTime,tag);
				if (!ServicesUtil.isEmpty(enersightProveDailyDtoList)) {
					return dopStagingDao.insertToEnersightStaging(endOfDay, enersightProveDailyDtoList,tag);
				}
			
			return -1;
		}
		
		 private List<CanaryStagingDto> getCanaryData(String userToken, CanaryRequestDto dto,boolean isEFS, boolean isCA,String tag) {
				logger.debug("CanaryStagingScheduler.getCanaryData(" + userToken + ", " + dto + ")");
				try {
					// STEP 1: Prepare request
					List<String> wells = hierarchyDao.getAllWells(isEFS,isCA);
					if (ServicesUtil.isEmpty(wells)) {
						logger.error("CanaryStagingScheduler.getCanaryData(): No wells found in Hierarchy");
						return null;
					}
					
					//List<String> muwis = browseTags(wells,tag);

					String payload = getPayloadInString(wells, dto.getCanaryParam());

					
					logger.error("CanaryStagingScheduler.getCanaryData.payload" + payload);
					// STEP 2: Execute request
					JSONObject canaryData = getCanaryData(userToken, payload, dto.getStartTimeCanaryFormat(),
							dto.getEndTimeCanaryFormat(), dto.getInsertInterval(), dto.getInsertIntervalType(),
							dto.getAggregateName());
					if (ServicesUtil.isEmpty(canaryData)) {
						logger.error("CanaryStagingScheduler.getCanaryData(): No response from Canary");
						return null;
					}

					// STE 3: Export JSON response to CanaryStagingDto
					List<CanaryStagingDto> resultList = new ArrayList<CanaryStagingDto>();
					for (Object key : canaryData.keySet()) {
						String keyInString = (String) key;
						CanaryStagingDto canaryStagingDto = new CanaryStagingDto();
						int indexOf = keyInString.indexOf('.', 10);
						canaryStagingDto.setMuwiId(keyInString.substring(10, indexOf));
						canaryStagingDto.setParameterType(keyInString.substring(indexOf + 1));
						canaryStagingDto.setCreatedAtInString(dto.getEndTimeDBFormat());
						JSONArray wellData = (JSONArray) canaryData.get(key);
						if (!ServicesUtil.isEmpty(wellData)) {
							for (Object value : wellData) {
								JSONArray wellList = (JSONArray) value;
								Object dataValue = wellList.get(1);
								if (dataValue != null && dataValue != "null") {
									if (dataValue.getClass().getName().equals("java.lang.Double")) {
										canaryStagingDto.setDataValue((Double) dataValue);
									} else if (dataValue.getClass().getName().equals("java.lang.Long")) {
										canaryStagingDto.setDataValue(((Long) dataValue).doubleValue());
									} else if (dataValue.getClass().getName().equals("java.lang.Integer")) {
										canaryStagingDto.setDataValue(((Integer) dataValue).doubleValue());
									}
									if (MurphyConstant.AGGR_NAME_TOTAL.equals(dto.getAggregateName())) {
										// NOTE: dataValue would be divide by 3600,
										// scale down is required to match with
										// AGGR_NAME_MAX output
										double x = ServicesUtil.changeTimeUnits(dto.getInsertInterval(),
												dto.getInsertIntervalType(), MurphyConstant.SECONDS);
										if (x != 0 && canaryStagingDto.getDataValue() != null) {
											canaryStagingDto.setDataValue(canaryStagingDto.getDataValue() / x);
										}
									}
								}
							}
						}
						resultList.add(canaryStagingDto);
					}
					return resultList;
				} catch (Exception e) {
					logger.error("CanaryStagingScheduler.getCanaryData()[error]" + e.getMessage());
					e.printStackTrace();
				}
				return null;
			}
		
		 public int stagingDataForDOPDGPThroughAPICall(String endTimeInString) throws Exception 
		 {
			 int returnValue=0;
				int restefsOil=0;
				int resetefsgas=0;
				int resetcaoil=0;
				int resetgaskaybob=0;
				int mntny1gas=0;
				int mntny2gas=0;
				Date currentDate= null;
				
				if(!ServicesUtil.isEmpty(endTimeInString))
				{
					DateFormat dfInDBFormat = new SimpleDateFormat(MurphyConstant.DATE_DB_FORMATE_SD);
					try {
						currentDate = dfInDBFormat.parse(endTimeInString);
					} catch (ParseException e) {
						logger.error("CanaryStagingScheduler.stagingDataForDOPDGPThroughAPICall" + e.getMessage());
						currentDate = new Date();
					}
					String endTimeDBFormat = ServicesUtil.convertFromZoneToZoneString(currentDate, null, "",MurphyConstant.UTC_ZONE, "",
							MurphyConstant.DATE_DB_FORMATE_SD);
					
					//clear all data for the end time
					dopStagingDao.clearDOP(endTimeDBFormat);
					
					String zoneoffsetEFSOil = configDao.getConfigurationByRef("EFS_QTOILD_ZONE_CANARY");
					String restHourEFSOil = configDao.getConfigurationByRef("EFS_QTOILD_REST_TIME_CANARY");
					
					String zoneEFSOil = getZoneFromOffset(zoneoffsetEFSOil);
					
					int currentHourEFSOil = getHourForEndTime(zoneEFSOil,restHourEFSOil,endTimeDBFormat);
					
					//EFS GAS
					String zoneoffsetEFSGas = configDao.getConfigurationByRef("EFS_QTGASD_ZONE_CANARY");
					String restHourEFSGas = configDao.getConfigurationByRef("EFS_QTGASD_REST_TIME_CANARY");
						
					String zoneEFSGas = getZoneFromOffset(zoneoffsetEFSGas);
					int currentHourEFSGas = getHourForEndTime(zoneEFSGas,restHourEFSGas,endTimeDBFormat);
					
					
					//Canada Oil
					String zoneoffsetCAOil = configDao.getConfigurationByRef("CA_SEP_CNDTOTTDY_ZONE_CANARY");
					String restHourCAOil = configDao.getConfigurationByRef("CA_SEP_CNDTOTTDY_REST_TIME_CANARY");
						
					String zoneCAOil = getZoneFromOffset(zoneoffsetCAOil);
					int currentHourCAOil = getHourForEndTime(zoneCAOil,restHourCAOil,endTimeDBFormat);
					
					//CanadaGasKaybob
					String zoneoffsetCAGasKaybob = configDao.getConfigurationByRef("CA_SEP_PRDTOTTDY_ZONE_CANARY");
					String restHourCAGasKaybob = configDao.getConfigurationByRef("CA_SEP_PRDTOTTDY_REST_TIME_CANARY");
					
					String zoneCAGasKaybob = getZoneFromOffset(zoneoffsetCAGasKaybob);
					int currentHourCAGasKaybob = getHourForEndTime(zoneCAGasKaybob,restHourCAGasKaybob,endTimeDBFormat);
					
					//CanadaGasMontneyOne
					String zoneoffsetCAGasMontneyOne = configDao.getConfigurationByRef("CA_AFLOW_ZONE_CANARY");
					String restHourCAMontneyOne = configDao.getConfigurationByRef("CA_AFLOW_REST_TIME_CANARY");
					
					String zoneCAGasMontneyOne = getZoneFromOffset(zoneoffsetCAGasMontneyOne);
					int currentHourCAMontneyOne = getHourForEndTime(zoneCAGasMontneyOne,restHourCAMontneyOne,endTimeDBFormat);
					
					//CanadaGasMontneyTwo
					String zoneoffsetCAGasMontneyTwo = configDao.getConfigurationByRef("CA_PG_FLOWD_ZONE_CANARY");
					String restHourCAMontneyTwo = configDao.getConfigurationByRef("CA_PG_FLOWD_REST_TIME_CANARY");
					

					String zoneCAGasMontneyTwo = getZoneFromOffset(zoneoffsetCAGasMontneyTwo);
					int currentHourCAMontneyTwo = getHourForEndTime(zoneCAGasMontneyTwo,restHourCAMontneyTwo,endTimeDBFormat);
							
					//rest check efs oil
					
					if(!ServicesUtil.isEmpty(restHourEFSOil))
					restefsOil =Integer.valueOf(restHourEFSOil);
						
					if (currentHourEFSOil == restefsOil) {
//						
						String[] canarayParam = {MurphyConstant.CANARY_PARAM_PV[0]};
						CanaryRequestDto dto = prepParamsForDOPDGP(zoneEFSOil,canarayParam,zoneoffsetEFSOil,
								true,false,false,false,false,false,endTimeInString);
						List<String> wells= hierarchyDao.getAllWells(true,false);
						List<String> muwisHavingReuiredTagdata =browseTags(wells,MurphyConstant.CANARY_PARAM_PV[0]);
						returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_PARAM_PV[0]);
					}
					else {
						String param = MurphyConstant.CANARY_PARAM_PV[0] ;
						String[] canarayParam = {param};
						CanaryRequestDto dto = prepParamsForDOPDGP(zoneEFSOil,canarayParam,
								zoneoffsetEFSOil,true,false,false,false,false,false,endTimeInString);
						//fetch actuals and insert to hana
						copyCanaryDataForDOP(dto,true,false,param);
						logger.error("projected next date efsoil " +dto.getNextDayDBFormat());
						returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),
								param,true,false,false,false,false,false,restefsOil);
					}
					
					//reset check efs gas
					if(!ServicesUtil.isEmpty(restHourEFSGas))
						resetefsgas =Integer.valueOf(restHourEFSGas);
					if (currentHourEFSGas == resetefsgas) {
						String param = MurphyConstant.CANARY_PARAM[3] ;
						String[] canarayParam = {param};
						CanaryRequestDto dto = prepParamsForDOPDGP(zoneEFSGas,canarayParam,zoneoffsetEFSGas,
								false,true,false,false,false,false,endTimeInString);
						List<String> wells= hierarchyDao.getAllWells(true,false);
						List<String> muwisHavingReuiredTagdata = browseTags(wells,MurphyConstant.CANARY_PARAM[3]);
						returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_PARAM[3]);
					} else {
						String param = MurphyConstant.CANARY_PARAM[3] ;
						String[] canarayParam = {param};
						CanaryRequestDto dto = prepParamsForDOPDGP(zoneEFSGas,canarayParam,
								zoneoffsetEFSGas,false,true,false,false,false,false,endTimeInString);
						copyCanaryDataForDOP(dto,true,false,param);
						logger.error("projected next date efsgas " +dto.getNextDayDBFormat());
						returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),
								param,false,true,false,false,false,false,resetefsgas);
					}

					//reset check ca oil
					if(!ServicesUtil.isEmpty(restHourCAOil))
					resetcaoil = Integer.valueOf(restHourCAOil);
					if (currentHourCAOil == resetcaoil) {
						String param = MurphyConstant.CANARY_CANADA_PARAM[0] ;
						String[] canarayParam = {param};
						CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAOil,canarayParam,zoneoffsetCAOil,
								false,false,true,false,false,false,endTimeInString);
						List<String> wells= hierarchyDao.getAllWells(false,true);
						List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
						returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_CANADA_PARAM[0]);
					} 
					else {
						String param = MurphyConstant.CANARY_CANADA_PARAM[0] ;
						String[] canarayParam = {param};
						CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAOil,canarayParam,zoneoffsetCAOil,
								false,false,true,false,false,false,endTimeInString);
						copyCanaryDataForDOP(dto,false,true,param);
						logger.error("projected next date caoil " +dto.getNextDayDBFormat());
						returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),param,
								false,false,true,false,false,false,resetcaoil);
					}
//					
//					//reset check ca gas kaybob
					if (currentHourCAGasKaybob == Integer.valueOf(restHourCAGasKaybob)) {
						String param = MurphyConstant.CANARY_CANADA_PARAM[1] ;
						String[] canarayParam = {param};
						List<String> wells= hierarchyDao.getAllWells(false,true);
						//List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
						List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
						CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasKaybob,canarayParam,zoneoffsetCAGasKaybob,
								false,false,false,true,false,false,endTimeInString);
						returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_CANADA_PARAM[1]);
					} 
					else {
						String param = MurphyConstant.CANARY_CANADA_PARAM[1] ;
						String[] canarayParam = {param};
						CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasKaybob,canarayParam,zoneoffsetCAGasKaybob,
								false,false,false,true,false,false,endTimeInString);
						copyCanaryDataForDOP(dto,false,true,param);
						logger.error("projected next date cagas " +dto.getNextDayDBFormat());
						returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),
								param,false,false,false,true,false,false,Integer.valueOf(restHourCAGasKaybob));
					}
					
					//reset check ca gas monteyone
					
					if (currentHourCAMontneyOne == Integer.valueOf(restHourCAMontneyOne)) {
						String param = MurphyConstant.CANARY_CANADA_PARAM[2] ;
						String[] canarayParam = {param};
						List<String> wells= hierarchyDao.getAllWells(false,true);
						//List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
						List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
						CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasMontneyOne,canarayParam,zoneoffsetCAGasMontneyOne,
								false,false,false,false,true,false,endTimeInString);
						returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_CANADA_PARAM[2]);
					} 
					else {
						String param = MurphyConstant.CANARY_CANADA_PARAM[2] ;
						String[] canarayParam = {param};
						CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasMontneyOne,canarayParam,zoneoffsetCAGasMontneyOne,
								false,false,false,false,true,false,endTimeInString);
						copyCanaryDataForDOP(dto,false,true,param);
						logger.error("projected next date mtnygas1 " +dto.getNextDayDBFormat());
						returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),
								param,false,false,false,false,true,false,Integer.valueOf(restHourCAMontneyOne));
					}
					
					//reset check ca gas monteytwo
					if (currentHourCAMontneyTwo == Integer.valueOf(restHourCAMontneyTwo)) {
						String param = MurphyConstant.CANARY_CANADA_PARAM[3] ;
						String[] canarayParam = {param};
						List<String> wells= hierarchyDao.getAllWells(false,true);
						List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
						CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasMontneyTwo,canarayParam,zoneoffsetCAGasMontneyTwo,
								false,false,false,false,false,true,endTimeInString);
						returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_CANADA_PARAM[3]);
					} 
					
					else {
						String param = MurphyConstant.CANARY_CANADA_PARAM[3] ;
						String[] canarayParam = {param};
						CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasMontneyTwo,canarayParam,zoneoffsetCAGasMontneyTwo,
								false,false,false,false,false,true,endTimeInString);
						copyCanaryDataForDOP(dto,false,true,param);
						logger.error("projected next date mtnygas2 " +dto.getNextDayDBFormat());
						returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),
								param,false,false,false,false,false,true,Integer.valueOf(restHourCAMontneyTwo));
				}
				}
				//EFS oil
				
				else
				{
				String zoneoffsetEFSOil = configDao.getConfigurationByRef("EFS_QTOILD_ZONE_CANARY");
				String restHourEFSOil = configDao.getConfigurationByRef("EFS_QTOILD_REST_TIME_CANARY");
				
				String zoneEFSOil = getZoneFromOffset(zoneoffsetEFSOil);
				
				int currentHourEFSOil = getCurrentHour(zoneEFSOil,restHourEFSOil);
				
//				//EFS GAS
				String zoneoffsetEFSGas = configDao.getConfigurationByRef("EFS_QTGASD_ZONE_CANARY");
				String restHourEFSGas = configDao.getConfigurationByRef("EFS_QTGASD_REST_TIME_CANARY");
					
				String zoneEFSGas = getZoneFromOffset(zoneoffsetEFSGas);
				int currentHourEFSGas = getCurrentHour(zoneEFSGas,restHourEFSOil);
				
				
				//Canada Oil
				String zoneoffsetCAOil = configDao.getConfigurationByRef("CA_SEP_CNDTOTTDY_ZONE_CANARY");
				String restHourCAOil = configDao.getConfigurationByRef("CA_SEP_CNDTOTTDY_REST_TIME_CANARY");
					
				String zoneCAOil = getZoneFromOffset(zoneoffsetCAOil);
				int currentHourCAOil = getCurrentHour(zoneCAOil,restHourCAOil);
				
				//CanadaGasKaybob
				String zoneoffsetCAGasKaybob = configDao.getConfigurationByRef("CA_SEP_PRDTOTTDY_ZONE_CANARY");
				String restHourCAGasKaybob = configDao.getConfigurationByRef("CA_SEP_PRDTOTTDY_REST_TIME_CANARY");
				
				String zoneCAGasKaybob = getZoneFromOffset(zoneoffsetCAGasKaybob);
				int currentHourCAGasKaybob = getCurrentHour(zoneCAGasKaybob,restHourCAGasKaybob);
				
				//CanadaGasMontneyOne
				String zoneoffsetCAGasMontneyOne = configDao.getConfigurationByRef("CA_AFLOW_ZONE_CANARY");
				String restHourCAMontneyOne = configDao.getConfigurationByRef("CA_AFLOW_REST_TIME_CANARY");
				
				String zoneCAGasMontneyOne = getZoneFromOffset(zoneoffsetCAGasMontneyOne);
				int currentHourCAMontneyOne = getCurrentHour(zoneCAGasMontneyOne,restHourCAMontneyOne);
				
				//CanadaGasMontneyTwo
				String zoneoffsetCAGasMontneyTwo = configDao.getConfigurationByRef("CA_PG_FLOWD_ZONE_CANARY");
				String restHourCAMontneyTwo = configDao.getConfigurationByRef("CA_PG_FLOWD_REST_TIME_CANARY");
				

				String zoneCAGasMontneyTwo = getZoneFromOffset(zoneoffsetCAGasMontneyTwo);
				int currentHourCAMontneyTwo = getCurrentHour(zoneCAGasMontneyTwo,restHourCAMontneyTwo);
						
				//rest check efs oil
				
				if(!ServicesUtil.isEmpty(restHourEFSOil))
				restefsOil =Integer.valueOf(restHourEFSOil);
					
				if (currentHourEFSOil == restefsOil) {
//					
					String[] canarayParam = {MurphyConstant.CANARY_PARAM_PV[0]};
					CanaryRequestDto dto = prepParamsForDOPDGP(zoneEFSOil,canarayParam,zoneoffsetEFSOil,
							true,false,false,false,false,false,endTimeInString);
					List<String> wells= hierarchyDao.getAllWells(true,false);
					List<String> muwisHavingReuiredTagdata = browseTags(wells,MurphyConstant.CANARY_PARAM_PV[0]);
					returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_PARAM_PV[0]);
				}
				else {
					String param = MurphyConstant.CANARY_PARAM_PV[0] ;
					String[] canarayParam = {param};
					CanaryRequestDto dto = prepParamsForDOPDGP(zoneEFSOil,canarayParam,
							zoneoffsetEFSOil,true,false,false,false,false,false,endTimeInString);
					//fetch actuals and insert to hana
					copyCanaryDataForDOP(dto,true,false,param);
					logger.error("projected next date efsoil " +dto.getNextDayDBFormat());
					returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),
							param,true,false,false,false,false,false,restefsOil);
				}
				
				//reset check efs gas
				if(!ServicesUtil.isEmpty(restHourEFSGas))
					resetefsgas =Integer.valueOf(restHourEFSGas);
				if (currentHourEFSGas == resetefsgas) {
					String param = MurphyConstant.CANARY_PARAM[3] ;
					String[] canarayParam = {param};
					CanaryRequestDto dto = prepParamsForDOPDGP(zoneEFSGas,canarayParam,zoneoffsetEFSGas,
							false,true,false,false,false,false,endTimeInString);
					List<String> wells= hierarchyDao.getAllWells(true,false);
					List<String> muwisHavingReuiredTagdata = browseTags(wells,MurphyConstant.CANARY_PARAM[3]);
					returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_PARAM[3]);
				} else {
					String param = MurphyConstant.CANARY_PARAM[3] ;
					String[] canarayParam = {param};
					CanaryRequestDto dto = prepParamsForDOPDGP(zoneEFSGas,canarayParam,
							zoneoffsetEFSGas,false,true,false,false,false,false,endTimeInString);
					copyCanaryDataForDOP(dto,true,false,param);
					logger.error("projected next date efsgas " +dto.getNextDayDBFormat());
					returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),
							param,false,true,false,false,false,false,resetefsgas);
				}

				//reset check ca oil
				if(!ServicesUtil.isEmpty(restHourCAOil))
				resetcaoil = Integer.valueOf(restHourCAOil);
				if (currentHourCAOil == resetcaoil) {
					String param = MurphyConstant.CANARY_CANADA_PARAM[0] ;
					String[] canarayParam = {param};
					CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAOil,canarayParam,zoneoffsetCAOil,
							false,false,true,false,false,false,endTimeInString);
					List<String> wells= hierarchyDao.getAllWells(false,true);
					List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
					returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_CANADA_PARAM[0]);
				} 
				else {
					String param = MurphyConstant.CANARY_CANADA_PARAM[0] ;
					String[] canarayParam = {param};
					CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAOil,canarayParam,zoneoffsetCAOil,
							false,false,true,false,false,false,endTimeInString);
					copyCanaryDataForDOP(dto,false,true,param);
					logger.error("projected next date caoil " +dto.getNextDayDBFormat());
					returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),param,
							false,false,true,false,false,false,resetcaoil);
				}
//				
//				//reset check ca gas kaybob
				if (currentHourCAGasKaybob == Integer.valueOf(restHourCAGasKaybob)) {
					String param = MurphyConstant.CANARY_CANADA_PARAM[1] ;
					String[] canarayParam = {param};
					List<String> wells= hierarchyDao.getAllWells(false,true);
					//List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
					List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
					CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasKaybob,canarayParam,zoneoffsetCAGasKaybob,
							false,false,false,true,false,false,endTimeInString);
					returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_CANADA_PARAM[1]);
				} 
				else {
					String param = MurphyConstant.CANARY_CANADA_PARAM[1] ;
					String[] canarayParam = {param};
					CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasKaybob,canarayParam,zoneoffsetCAGasKaybob,
							false,false,false,true,false,false,endTimeInString);
					copyCanaryDataForDOP(dto,false,true,param);
					logger.error("projected next date cagas " +dto.getNextDayDBFormat());
					returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),
							param,false,false,false,true,false,false,Integer.valueOf(restHourCAGasKaybob));
				}
				
				//reset check ca gas monteyone
				
				if (currentHourCAMontneyOne == Integer.valueOf(restHourCAMontneyOne)) {
					String param = MurphyConstant.CANARY_CANADA_PARAM[2] ;
					String[] canarayParam = {param};
					List<String> wells= hierarchyDao.getAllWells(false,true);
					//List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
					List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
					CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasMontneyOne,canarayParam,zoneoffsetCAGasMontneyOne,
							false,false,false,false,true,false,endTimeInString);
					returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_CANADA_PARAM[2]);
				} 
				else {
					String param = MurphyConstant.CANARY_CANADA_PARAM[2] ;
					String[] canarayParam = {param};
					CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasMontneyOne,canarayParam,zoneoffsetCAGasMontneyOne,
							false,false,false,false,true,false,endTimeInString);
					copyCanaryDataForDOP(dto,false,true,param);
					logger.error("projected next date mtnygas1 " +dto.getNextDayDBFormat());
					returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),
							param,false,false,false,false,true,false,Integer.valueOf(restHourCAMontneyOne));
				}
				
				//reset check ca gas monteytwo
				if (currentHourCAMontneyTwo == Integer.valueOf(restHourCAMontneyTwo)) {
					String param = MurphyConstant.CANARY_CANADA_PARAM[3] ;
					String[] canarayParam = {param};
					List<String> wells= hierarchyDao.getAllWells(false,true);
					List<String> muwisHavingReuiredTagdata =browseTags(wells,param);
					CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasMontneyTwo,canarayParam,zoneoffsetCAGasMontneyTwo,
							false,false,false,false,false,true,endTimeInString);
					returnValue= setupDefaultValuesForDOPDGP(muwisHavingReuiredTagdata,dto.getEndTimeDBFormat(), dto.getNextDayDBFormat(),MurphyConstant.CANARY_CANADA_PARAM[3]);
				} 
				
				else {
					String param = MurphyConstant.CANARY_CANADA_PARAM[3] ;
					String[] canarayParam = {param};
					CanaryRequestDto dto = prepParamsForDOPDGP(zoneCAGasMontneyTwo,canarayParam,zoneoffsetCAGasMontneyTwo,
							false,false,false,false,false,true,endTimeInString);
					copyCanaryDataForDOP(dto,false,true,param);
					logger.error("projected next date mtnygas2 " +dto.getNextDayDBFormat());
					returnValue =dopStagingDao.updateDOPProjectedValues(dto.getCurrentHour(), dto.getNextDayDBFormat(),
							param,false,false,false,false,false,true,Integer.valueOf(restHourCAMontneyTwo));
				}
				
				}
				return returnValue;
		 }

		private int getHourForEndTime(String zone, String restHour, String endTimeDBFormat) {
			Date currentDate= null;
			int roundOffInterval = 60;
			String roundOffIntervalType = MurphyConstant.MINUTES;
			int currentHour = 0;
			try{
				if (!ServicesUtil.isEmpty(endTimeDBFormat)) {
					DateFormat dfInDBFormat = new SimpleDateFormat(MurphyConstant.DATE_DB_FORMATE_SD);
					try {
						currentDate = dfInDBFormat.parse(endTimeDBFormat);
					} catch (ParseException e) {
						logger.error("CanaryStagingScheduler.getHourForEndTime()" + e.getMessage());
						currentDate = new Date();
					}
				} else {
					currentDate = new Date();
				}
				currentDate = roundDateToNearest(currentDate, roundOffInterval, roundOffIntervalType,zone);
				Calendar c = Calendar.getInstance();
				c.setTimeZone(TimeZone.getTimeZone(zone));
				c.setTime(currentDate);
				currentHour = c.get(Calendar.HOUR_OF_DAY);
				logger.error("hour of the day : "+zone+" for hour" +currentHour);
			}
			catch(Exception e)
			{
				logger.error("Exception e while getiing hour of day from end time" +e.getMessage());
			}
			
			return currentHour;
		}
}