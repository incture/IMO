package com.murphy.taskmgmt.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.murphy.integration.dao.EnersightProveDailyDao;
import com.murphy.taskmgmt.dao.FracHitDao;
import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dao.IOPTagDefaultDao;
import com.murphy.taskmgmt.dao.ProductionVarianceDao;
import com.murphy.taskmgmt.dao.WellMuwiDao;
import com.murphy.taskmgmt.dto.AlsInvestigationDto;
import com.murphy.taskmgmt.dto.IOPTagDefaultDto;
import com.murphy.taskmgmt.dto.PlotlyRequestDto;
import com.murphy.taskmgmt.dto.ProductionVarianceDto;
import com.murphy.taskmgmt.dto.Response;
import com.murphy.taskmgmt.service.interfaces.PlotlyFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("plotlyFacade")
public class PlotlyFacade implements PlotlyFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(PlotlyFacade.class);

	@Autowired
	IOPTagDefaultDao tagDefaultDao;

	@Autowired
	FracHitDao fracHitDao;

	@Autowired
	ProductionVarianceDao productionVarianceDao;

	@Autowired
	WellMuwiDao wellMuwiDao;

	@Autowired
	HierarchyDao hierarchyDao;

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<Response<JSONObject>> generatePlotlyChart(PlotlyRequestDto plotRequestDto) {

		Response<JSONObject> response = new Response<JSONObject>();
		JSONObject jsonResult = new JSONObject();
		List<AlsInvestigationDto> alsDtoList = null;
		List<Date> fracHitTimeDetailList = null;
		try {
			// get locationCode by MUWI [TODO Need to correct country property
			// to location]
			String location = ServicesUtil
					.getLocationByLocationCode(wellMuwiDao.getLocationCodeByMUWI(plotRequestDto.getMuwi()));
			plotRequestDto.setLocation(location);

			logger.error("PlotlyFacade.generatePlotlyChart() location " + location);

			if (!ServicesUtil.isEmpty(plotRequestDto) && !ServicesUtil.isEmpty(plotRequestDto.getMuwi())
					&& !ServicesUtil.isEmpty(plotRequestDto.getReportId())
					&& !ServicesUtil.isEmpty(plotRequestDto.getLocation())) {

				// Fetch WellName for Alarms Report
				if (MurphyConstant.ALARM_REPORT_ID.equalsIgnoreCase(plotRequestDto.getReportId())) {
					// String
					// locationCode=hierarchyDao.getLocationCodeByMuwi(plotRequestDto.getMuwi());
					plotRequestDto
							.setWellName(ServicesUtil.isEmpty(hierarchyDao.getLocationByMuwi(plotRequestDto.getMuwi()))
									? "" : hierarchyDao.getLocationByMuwi(plotRequestDto.getMuwi()));
				}

				if (MurphyConstant.DOP_REPORT_ID.equalsIgnoreCase(plotRequestDto.getReportId())
						|| MurphyConstant.DGP_REPORT_ID.equalsIgnoreCase(plotRequestDto.getReportId())) {
					logger.error("DOP/DGP");
					jsonResult = getActualDataFromCanary(plotRequestDto);
					response.setOutput(jsonResult);

				} else {
					// Fetch CanaryTag names by report id
					List<IOPTagDefaultDto> tagDefaultDtoList = tagDefaultDao.fetchTagNamesbyReportID(plotRequestDto);

					// Fetch FracHitTime for MUWI
					if (plotRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.FRAC_REPORT_ID)) {
						fracHitTimeDetailList = fracHitDao.getFracHitTimeByMuwi(plotRequestDto.getMuwi());
					}

					// Fetch Trends Details for Prove
					if (plotRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.PROVE_REPORT_ID)) {
						alsDtoList = fracHitDao.fetchHistInvestigationDetails(plotRequestDto.getMuwi());
					}

					if (!ServicesUtil.isEmpty(tagDefaultDtoList)) {

						// Fetch Ymin and Ymax Range
						Map<String, String> tagMappingUnits = tagDefaultDao.fetchYminYmaxByunit();

						// Fetch data from Canary
						Map<String, String> timeMap = fetchTimeRangeForWell(plotRequestDto);
						if (!ServicesUtil.isEmpty(timeMap)
								&& !ServicesUtil.isEmpty(timeMap.get(MurphyConstant.START_TIME))
								&& !ServicesUtil.isEmpty(timeMap.get(MurphyConstant.END_TIME))) {
							List<JSONObject> jsonObjectList = fetchDataFromCanary(
									timeMap.get(MurphyConstant.START_TIME), timeMap.get(MurphyConstant.END_TIME),
									tagDefaultDtoList, null, new ArrayList<JSONObject>());
							if (!ServicesUtil.isEmpty(jsonObjectList)) {
								jsonResult = parseCanaryResponseToUI(jsonObjectList, plotRequestDto, tagDefaultDtoList,
										tagMappingUnits);
								jsonResult.put("trendsDtoList", alsDtoList);
								jsonResult.put("fracHitTimeList", fracHitTimeDetailList);
								response.setOutput(jsonResult);
							}
						} else {
							response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
							response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
							response.setMessage("Failed to fetch Canary Start/EndTime");
							response.setOutput(jsonResult);
						}
					} else {
						response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
						response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
						response.setMessage("No Canary Tags associated with report id " + plotRequestDto.getReportId());
						response.setOutput(jsonResult);
					}

				}
			} else {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
				response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setMessage("Mandatory Fields Missing");
				response.setOutput(jsonResult);
			}
		} catch (Exception e) {
			logger.error("[Murphy][PlotlyFacade][generatePlotlyChart][Exception]" + e.getMessage());
			response.setStatus(MurphyConstant.FAILURE);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		}

		return ResponseEntity.ok(response);
	}

	@SuppressWarnings("unchecked")
	private JSONObject getActualDataFromCanary(PlotlyRequestDto plotlyRequestDto) throws Exception {
		JSONObject resultantJsonObj = new JSONObject();
		JSONObject actualJsonData = null;
		JSONObject foreCastData = null;
		JSONObject projectedData = null;
		double conversionFactor = 1.0d;
		String startTime = calculateStartTime(plotlyRequestDto.getLocation());
		Date currentDate = roundDateToNearest(new Date(), 60, MurphyConstant.MINUTES, MurphyConstant.CST_ZONE);
		String endTime = ServicesUtil.convertFromZoneToZoneString(currentDate, null, "", MurphyConstant.CST_ZONE, "",
				MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
		
		// Calculate Current Hour in CST
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
		c.setTime(currentDate);
		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		String tagName = "";
		//getting the tag based on location and reportid
		logger.error("Dto :" + plotlyRequestDto.toString());
		if(MurphyConstant.CANADA_BASE_LOC_CODE.equals(plotlyRequestDto.getLocation()))
		{
			if(MurphyConstant.DOP_REPORT_ID.equalsIgnoreCase(plotlyRequestDto.getReportId())){
				conversionFactor = MurphyConstant.OIL_FACTOR;
			}
			else if(MurphyConstant.DGP_REPORT_ID.equalsIgnoreCase(plotlyRequestDto.getReportId()))
			{
				conversionFactor = MurphyConstant.GAS_FACTOR;
			}
		}
		if (MurphyConstant.EFS_BASE_LOC_CODE.equalsIgnoreCase(plotlyRequestDto.getLocation())) {

			if (MurphyConstant.DOP_REPORT_ID.equalsIgnoreCase(plotlyRequestDto.getReportId())) {
				tagName = MurphyConstant.EFS_OIL_CANARY_TAG;
			} else {
				tagName = MurphyConstant.EFS_GAS_CANARY_TAG;
			}

		} else if (MurphyConstant.KAY_BASE_LOC_CODE.equalsIgnoreCase(plotlyRequestDto.getLocation())) {
			if (MurphyConstant.DOP_REPORT_ID.equalsIgnoreCase(plotlyRequestDto.getReportId())) {
				tagName = MurphyConstant.KAYBOB_OIL_CANARY_TAG;
			} else {
				tagName = MurphyConstant.KAYBOB_GAS_CANARY_TAG;
			}
		} else if (MurphyConstant.MTM_BASE_LOC_CODE.equalsIgnoreCase(plotlyRequestDto.getLocation())) {
			if (MurphyConstant.DGP_REPORT_ID.equalsIgnoreCase(plotlyRequestDto.getReportId())) {
				tagName = MurphyConstant.TUPPER_GAS_CANARY_TAG;

			}

		} else {
			if (MurphyConstant.DGP_REPORT_ID.equalsIgnoreCase(plotlyRequestDto.getReportId())) {
				tagName = MurphyConstant.TUPPER_WEST_GAS_CANARY_TAG;
			}
		}
		Map<String, String> dateValueMap = getProjectedForecastDate(currentHour, plotlyRequestDto.getLocation());
		//fetching all the records from TM_PRODUCTIOn_VARIANCE_STAGING table for the given muwi and tag
		logger.error("Tag :" + tagName);

		List<ProductionVarianceDto> productionVarianceList = productionVarianceDao.fetchTrendsData(plotlyRequestDto.getMuwi(), tagName);
		
		logger.error("List :" + productionVarianceList.toString());

		if(!ServicesUtil.isEmpty(productionVarianceList)){
			final Double convFactor = conversionFactor;
			List<ProductionVarianceDto> actualsList = null;
			List<ProductionVarianceDto> forecastList = null;
			List<ProductionVarianceDto> projectedList = null;
			List<Long> epochList = null;
			List<Double> valueList = null;
			List<Double> convertedList = null;

			actualsList = productionVarianceList.stream().filter(x-> x.getSource()!=null &&x.getSource().equals(MurphyConstant.DOP_CANARY)).collect(Collectors.toList());
			logger.error("Actuals " + actualsList.toString());
			forecastList = productionVarianceList.stream().filter(x->x.getSource()!=null && x.getSource().equals(MurphyConstant.DOP_FORECAST)).collect(Collectors.toList());
			logger.error("forecast " + forecastList.toString());
			projectedList = productionVarianceList.stream().filter(x-> x.getSource()!=null &&x.getSource().equals(MurphyConstant.DOP_PROJECTED)).collect(Collectors.toList());
			logger.error("projected " +projectedList.toString());
			
			actualJsonData = new JSONObject();
			if(!ServicesUtil.isEmpty(actualsList)){
				epochList= actualsList.stream().filter(x->x.getCreatedAt()!=null).map(x->x.getCreatedAt().getTime()/1000).collect(Collectors.toList());
				valueList = actualsList.stream().map(x->x.getDataValue()).collect(Collectors.toList());
				convertedList = valueList.stream().map(x->x*convFactor).collect(Collectors.toList());
			actualJsonData.put("y", convertedList);
			actualJsonData.put("epochSecond", epochList);
			actualJsonData.put("message", MurphyConstant.SUCCESS);
			}
			else
			{
				//if there is no record for actuals
				actualJsonData.put("y", new int[]{0});
				actualJsonData.put("epochSecond", new long[]{currentDate.getTime()});
				actualJsonData.put("message", MurphyConstant.SUCCESS);
			}
			foreCastData = new JSONObject();
			if(!ServicesUtil.isEmpty(forecastList)){
				epochList = forecastList.stream().filter(x->x.getCreatedAt()!=null).map(x->x.getCreatedAt().getTime()/1000).collect(Collectors.toList());
				
				valueList = forecastList.stream().map(x->x.getDataValue()).collect(Collectors.toList());
				
				
				foreCastData.put("y", valueList);
				foreCastData.put("epochSecond",epochList );
				foreCastData.put("message", MurphyConstant.SUCCESS);
			}
			else
			{
				foreCastData.put("y", new int[]{0,0});
				foreCastData.put("epochSecond", new long[]{ZonedDateTime.parse(dateValueMap.get("previousDate")).toEpochSecond(),ZonedDateTime.parse(dateValueMap.get("currentDate")).toEpochSecond()});
				foreCastData.put("message", MurphyConstant.SUCCESS);
			}
			projectedData = new JSONObject();
			if(!ServicesUtil.isEmpty(projectedList)){
				epochList = projectedList.stream().filter(x->x.getCreatedAt()!=null).map(x->x.getCreatedAt().getTime()/1000).collect(Collectors.toList());
				
				valueList = projectedList.stream().map(x->x.getDataValue()).collect(Collectors.toList());
				convertedList = valueList.stream().map(x->x*convFactor).collect(Collectors.toList());
				projectedData.put("y", convertedList);
				projectedData.put("epochSecond", epochList);
				projectedData.put("message", MurphyConstant.SUCCESS);
			}
			else
			{
				projectedData.put("y", new int[]{0,0});
				projectedData.put("epochSecond", new long[]{ZonedDateTime.parse(dateValueMap.get("previousDate")).toEpochSecond(),ZonedDateTime.parse(dateValueMap.get("currentDate")).toEpochSecond()});
				projectedData.put("message", MurphyConstant.SUCCESS);
				
			}
			
		}
		else
		{
			//If the list is empty
			actualJsonData = new JSONObject();
			actualJsonData.put("y", 0);
			actualJsonData.put("epochSecond", currentDate.getTime());
			actualJsonData.put("message", MurphyConstant.SUCCESS);
			foreCastData = new JSONObject();
			foreCastData.put("y", new int[]{0,0});
			foreCastData.put("epochSecond", new long[]{ZonedDateTime.parse(dateValueMap.get("previousDate")).toEpochSecond(),ZonedDateTime.parse(dateValueMap.get("currentDate")).toEpochSecond()});
			foreCastData.put("message", MurphyConstant.SUCCESS);
			projectedData = new JSONObject();
			projectedData.put("y", new int[]{0,0});
			projectedData.put("epochSecond", new long[]{ZonedDateTime.parse(dateValueMap.get("previousDate")).toEpochSecond(),ZonedDateTime.parse(dateValueMap.get("currentDate")).toEpochSecond()});
			projectedData.put("message", MurphyConstant.SUCCESS);

			
		}
		


		
		// Get date for Projected and Forecast
		
		
		
//		// Get Actual,Forecast and Projected Data
//		actualJsonData = fetchDOPActualDataFromCanary(plotlyRequestDto.getMuwi(), plotlyRequestDto.getReportId(),
//				startTime, endTime, plotlyRequestDto.getLocation());
//		foreCastData = new EnersightProveDailyDao().fetchForecastValueFromEnersight(plotlyRequestDto.getMuwi(),
//				plotlyRequestDto.getReportId(), dateValueMap, plotlyRequestDto.getLocation());
//		projectedData = calculateProjectedData(plotlyRequestDto.getLocation(), currentHour, actualJsonData,
//				dateValueMap);

		//resultantJsonObj.put("actual", actualJsonData.get("actualData"));
		resultantJsonObj.put("actual", actualJsonData);
		resultantJsonObj.put("forecast", foreCastData);
		resultantJsonObj.put("projected", projectedData);
		resultantJsonObj.put("muwi", plotlyRequestDto.getMuwi());
		resultantJsonObj.put("wellName", plotlyRequestDto.getWellName());
		return resultantJsonObj;

	}

	private Map<String, String> getProjectedForecastDate(int currentHour, String location) {
		Map<String, String> mapDate = new HashMap<String, String>();
		Calendar calender = Calendar.getInstance();
		String currentDate = null;
		String previousDate = null;

		if (MurphyConstant.EFS_BASE_LOC_CODE.equalsIgnoreCase(location)) {
			calender.setTimeZone(TimeZone.getTimeZone("CST"));
			calender.set(Calendar.HOUR_OF_DAY, 7);
			calender.set(Calendar.MINUTE, 0);
			calender.set(Calendar.SECOND, 0);
			calender.set(Calendar.MILLISECOND, 0);
			if (currentHour < 7) {
				currentDate = ServicesUtil.convertFromZoneToZoneString(calender.getTime(), null, null, "CST",
						MurphyConstant.DATEFORMAT_FOR_CANARY_FULL, MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
				calender.add(Calendar.DATE, -1);
				previousDate = ServicesUtil.convertFromZoneToZoneString(calender.getTime(), null, null, "CST",
						MurphyConstant.DATEFORMAT_FOR_CANARY_FULL, MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
			} else {
				previousDate = ServicesUtil.convertFromZoneToZoneString(calender.getTime(), null, null, "CST",
						MurphyConstant.DATEFORMAT_FOR_CANARY_FULL, MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
				calender.add(Calendar.DATE, 1);
				currentDate = ServicesUtil.convertFromZoneToZoneString(calender.getTime(), null, null, "CST",
						MurphyConstant.DATEFORMAT_FOR_CANARY_FULL, MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
			}
		} else {
			calender.setTimeZone(TimeZone.getTimeZone("CST"));
			calender.set(Calendar.HOUR_OF_DAY, 9);
			calender.set(Calendar.MINUTE, 0);
			calender.set(Calendar.SECOND, 0);
			calender.set(Calendar.MILLISECOND, 0);
			if (currentHour < 9) {
				currentDate = ServicesUtil.convertFromZoneToZoneString(calender.getTime(), null, null, "CST",
						MurphyConstant.DATEFORMAT_FOR_CANARY_FULL, MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
				calender.add(Calendar.DATE, -1);
				previousDate = ServicesUtil.convertFromZoneToZoneString(calender.getTime(), null, null, "CST",
						MurphyConstant.DATEFORMAT_FOR_CANARY_FULL, MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
			} else {
				previousDate = ServicesUtil.convertFromZoneToZoneString(calender.getTime(), null, null, "CST",
						MurphyConstant.DATEFORMAT_FOR_CANARY_FULL, MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
				calender.add(Calendar.DATE, 1);
				currentDate = ServicesUtil.convertFromZoneToZoneString(calender.getTime(), null, null, "CST",
						MurphyConstant.DATEFORMAT_FOR_CANARY_FULL, MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
			}
		}

		mapDate.put("currentDate", currentDate);
		mapDate.put("previousDate", previousDate);
		logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][currenrDate]" + mapDate.get("currentDate"));
		logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][previousDate]" + mapDate.get("previousDate"));

		return mapDate;
	}

	@SuppressWarnings("unchecked")
	private JSONObject calculateProjectedData(String location, int currentHour, JSONObject actualJsonData,
			Map<String, String> dateValueMap) throws Exception {
		JSONObject jsonObj = new JSONObject();
		long[] epochSecond = new long[2];
		double[] tagValue = new double[2];

		if (MurphyConstant.EFS_BASE_LOC_CODE.equalsIgnoreCase(location)) {
			if (currentHour < 7) {
				currentHour = currentHour + 24;
			}

			// Setting Default Projected Value
			tagValue[0] = 0;
			epochSecond[0] = ZonedDateTime.parse(dateValueMap.get("previousDate")).toEpochSecond();

			// Calculating original Projected value
			double maxValue = (double) actualJsonData.get("maxValue");
			double dataValue = (maxValue / (currentHour - 6)) * 24;
			tagValue[1] = dataValue;
			epochSecond[1] = ZonedDateTime.parse(dateValueMap.get("currentDate")).toEpochSecond();
		} else if (MurphyConstant.KAY_BASE_LOC_CODE.equalsIgnoreCase(location)) {
			if (currentHour < 9) {
				currentHour = currentHour + 24;
			}

			// Setting Default Projected Value
			tagValue[0] = 0;
			epochSecond[0] = ZonedDateTime.parse(dateValueMap.get("previousDate")).toEpochSecond();

			// Calculating original Projected value
			double maxValue = (double) actualJsonData.get("maxValue");
			double dataValue = (maxValue / (currentHour - 8)) * 24;
			tagValue[1] = dataValue;
			epochSecond[1] = ZonedDateTime.parse(dateValueMap.get("currentDate")).toEpochSecond();
		} else {
			if (currentHour < 10) {
				currentHour = currentHour + 24;
			}

			// Setting Default Projected Value
			tagValue[0] = 0;
			epochSecond[0] = ZonedDateTime.parse(dateValueMap.get("previousDate")).toEpochSecond();

			// Calculating original Projected value
			double maxValue = (double) actualJsonData.get("maxValue");
			double dataValue = (maxValue / (currentHour - 9)) * 24;
			tagValue[1] = dataValue;
			epochSecond[1] = ZonedDateTime.parse(dateValueMap.get("currentDate")).toEpochSecond();
		}
		jsonObj.put("y", new org.json.JSONArray(tagValue));
		jsonObj.put("epochSecond", new org.json.JSONArray(epochSecond));
		jsonObj.put("message", MurphyConstant.SUCCESS);
		return jsonObj;
	}

	@SuppressWarnings("unchecked")
	private JSONObject fetchDOPActualDataFromCanary(String muwi, String reportId, String startTime, String endTime,
			String location) throws Exception {
		JSONObject finalJSON = new JSONObject();
		JSONObject resultJSONObject = new JSONObject();
		long[] epochSecond = null;
		double[] tagValue = null;
		String responseMsg = null;
		double maxDataValue = 0;
		String canaryTag = null;

		// Generate UserToken
		String userToken = getUserToken();

		if (MurphyConstant.EFS_BASE_LOC_CODE.equalsIgnoreCase(location)) {

			if (MurphyConstant.DOP_REPORT_ID.equalsIgnoreCase(reportId)) {
				canaryTag = "MUWI_Prod." + muwi + "." + MurphyConstant.EFS_OIL_CANARY_TAG;
			} else {
				canaryTag = "MUWI_Prod." + muwi + "." + MurphyConstant.EFS_GAS_CANARY_TAG;
			}

		} else if (MurphyConstant.KAY_BASE_LOC_CODE.equalsIgnoreCase(location)) {
			if (MurphyConstant.DOP_REPORT_ID.equalsIgnoreCase(reportId)) {
				canaryTag = "MUWI_Prod." + muwi + "." + MurphyConstant.KAYBOB_OIL_CANARY_TAG;
			} else {
				canaryTag = "MUWI_Prod." + muwi + "." + MurphyConstant.KAYBOB_GAS_CANARY_TAG;
			}
		} else if (MurphyConstant.MTM_BASE_LOC_CODE.equalsIgnoreCase(location)) {
			if (MurphyConstant.DGP_REPORT_ID.equalsIgnoreCase(reportId)) {
				canaryTag = "MUWI_Prod." + muwi + "." + MurphyConstant.TUPPER_GAS_CANARY_TAG;

			}
			// else {
			// canaryTag = "MUWI_Prod." + muwi + "." +
			// MurphyConstant.TUPPER_OIL_CANARY_TAG;
			// }
		} else {
			if (MurphyConstant.DGP_REPORT_ID.equalsIgnoreCase(reportId)) {
				canaryTag = "MUWI_Prod." + muwi + "." + MurphyConstant.TUPPER_WEST_GAS_CANARY_TAG;
			}
			// else {
			// canaryTag = "MUWI_Prod." + muwi + "." +
			// MurphyConstant.TUPPER_WEST_OIL_CANARY_TAG;
			//
			// }
		}

		// Canary Request Payload
		String payload = "{" + "\"userToken\": \"" + userToken + "\"," + "\"startTime\": \"" + startTime + "\","
				+ "\"endTime\": \"" + endTime + "\"," + "\"aggregateName\": \"Total\","
				+ "\"aggregateInterval\": \"0:01:00:00\"," + "\"includeQuality\": false," + " \"MaxSize\": 10000,"
				+ "\"continuation\": null" + ",\"tags\":[\"" + canaryTag + "\"]}";

		logger.error("Canary Request Payload:::::::::" + payload);
		String url = MurphyConstant.CANARY_API_HOST + "api/v1/getTagData";

		String httpMethod = MurphyConstant.HTTP_METHOD_POST;
		org.json.JSONObject canaryResponse = RestUtil.callRest(url, payload, httpMethod);

		// Revoke User Token
		revokeUserTokenService(userToken);

		if (!ServicesUtil.isEmpty(canaryResponse) && canaryResponse.toString().contains("data")) {

			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject json;
			try {
				json = (org.json.simple.JSONObject) parser.parse(canaryResponse.toString());

				if (json.get("errors").toString().equals("[]")) {
					responseMsg = MurphyConstant.SUCCESS;
				} else if (startTime.equalsIgnoreCase(endTime)) {
					responseMsg = MurphyConstant.SUCCESS;
					// Setting up default value as zero
					resultJSONObject.put("y", 0);
					resultJSONObject.put("epochSecond", ZonedDateTime.parse(endTime).toEpochSecond());
					resultJSONObject.put("message", responseMsg);
				} else {
					responseMsg = "Tag not available in canary.Please Contact Administrator.";
				}

				if (!json.get("data").toString().equals("{}")) {

					json = (JSONObject) json.get("data");

					JSONArray jsonArray = (JSONArray) json.get(canaryTag);
					epochSecond = new long[jsonArray.size()];
					tagValue = new double[jsonArray.size()];
					if (!jsonArray.toString().equals("[]")) {

						for (int i = 0; i < jsonArray.size(); i++) {

							// Fetching first array object of Tag
							JSONArray json1 = (JSONArray) jsonArray.get(i);
							if (!json1.toString().equals("[]")) {
								if (!ServicesUtil.isEmpty(json1.get(1))) {
									epochSecond[i] = ZonedDateTime.parse(json1.get(0).toString()).toEpochSecond();
									double x = Double.parseDouble(json1.get(1).toString());
									if (x == (int) x) {
										tagValue[i] = (long) json1.get(1) / 3600;
									} else if (!(x == (int) x)) {
										tagValue[i] = (double) json1.get(1) / 3600;
									}
								}

							}
						}
						// int insertInterval = 1;
						//
						// double x =
						// ServicesUtil.changeTimeUnits(insertInterval,
						// MurphyConstant.HOURS,
						// MurphyConstant.SECONDS);

						for (int i = 0; i < tagValue.length; i++) {

							// if (x != 0 && !ServicesUtil.isEmpty(tagValue[i]))
							// {
							// tagValue[i] = tagValue[i] / x;
							//
							// }
							if (tagValue[i] > maxDataValue) {
								maxDataValue = tagValue[i];
							}
						}

						resultJSONObject.put("y", new org.json.JSONArray(tagValue));
						resultJSONObject.put("epochSecond", new org.json.JSONArray(epochSecond));
						resultJSONObject.put("message", responseMsg);

					}

					finalJSON.put("actualData", resultJSONObject);
					finalJSON.put("maxValue", maxDataValue);

				}

			} catch (Exception e) {
				logger.error("[Murphy][PlotlyFacade][fetchDOPActualDataFromCanary][Exception]" + e.getMessage());
				throw e;
			}
		}
		return finalJSON;

	}

	Date roundDateToNearest(Date currentDate, int interval, String intervalType, String zoneId) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone(zoneId));
		calendar.setTime(currentDate);
		int value = 0;
		if ("days".equals(intervalType)) {
			value = calendar.get(Calendar.DATE);
			value -= value % interval;
			calendar.set(Calendar.DATE, value);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} else if ("hours".equals(intervalType)) {
			value = calendar.get(Calendar.HOUR);
			value -= value % interval;
			calendar.set(Calendar.HOUR, value);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} else if ("minute".equals(intervalType)) {
			value = calendar.get(Calendar.MINUTE);
			value -= value % interval;
			calendar.set(Calendar.MINUTE, value);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} else if ("seconds".equals(intervalType)) {
			value = calendar.get(Calendar.SECOND);
			value -= value % interval;
			calendar.set(Calendar.SECOND, value);
			calendar.set(Calendar.MILLISECOND, 0);
		}
		return calendar.getTime();
	}

	private String calculateStartTime(String location) throws Exception {
		String originalDate = null;
		try {
			Calendar c = Calendar.getInstance();
			c.setTimeZone(TimeZone.getTimeZone("IST"));
			DateFormat canaryDateFormat = new SimpleDateFormat(MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
			originalDate = ServicesUtil.convertFromZoneToZoneString(c.getTime(), null, null, MurphyConstant.CST_ZONE,
					"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss");

			Date dateNew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originalDate);
			Boolean time = timeBetweenOrNot(dateNew, location);
			logger.error("[Murphy][DowntimeCaptureDao][PLOTLY][dateNew]" + dateNew);
			logger.error("[Murphy][DowntimeCaptureDao][PLOTLY][time]" + time);
			if (time.equals(true)) {
				originalDate = canaryDateFormat.format(getPrevDate(dateNew, location));
				System.out.println("[Murphy][DowntimeCaptureDao][PLOTLY][getPrevDate- originalDate]" + originalDate);
			} else {
				Calendar calender = Calendar.getInstance();
				if (MurphyConstant.EFS_BASE_LOC_CODE.equalsIgnoreCase(location)) {
					calender.setTimeZone(TimeZone.getTimeZone("CST"));
					calender.set(Calendar.HOUR_OF_DAY, 7);
					calender.set(Calendar.MINUTE, 0);
					calender.set(Calendar.SECOND, 0);
					calender.set(Calendar.MILLISECOND, 0);
				} else if (MurphyConstant.KAY_BASE_LOC_CODE.equalsIgnoreCase(location)) {
					calender.setTimeZone(TimeZone.getTimeZone("CST"));
					calender.set(Calendar.HOUR_OF_DAY, 9);
					calender.set(Calendar.MINUTE, 0);
					calender.set(Calendar.SECOND, 0);
					calender.set(Calendar.MILLISECOND, 0);
				} else {
					calender.setTimeZone(TimeZone.getTimeZone("CST"));
					calender.set(Calendar.HOUR_OF_DAY, 10);
					calender.set(Calendar.MINUTE, 0);
					calender.set(Calendar.SECOND, 0);
					calender.set(Calendar.MILLISECOND, 0);
				}
				originalDate = ServicesUtil.convertFromZoneToZoneString(calender.getTime(), null, null, null,
						MurphyConstant.DATEFORMAT_FOR_CANARY_FULL, MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
				System.out.println("[Murphy][DowntimeCaptureDao][PLOTLY][originalDate]" + originalDate);
			}
		} catch (Exception e) {
			logger.error("Error while calculing startTime" + e.getMessage());
			throw e;
		}
		return originalDate;
	}

	public Boolean timeBetweenOrNot(Date date, String location) {

		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		String date0 = df.format(date);
		String date1 = null;
		String date2 = null;
		if (MurphyConstant.EFS_BASE_LOC_CODE.equalsIgnoreCase(location)) {
			date1 = "00:00:00";
			date2 = "06:59:59";
		} else {
			date1 = "00:00:00";
			date2 = "08:59:59";
		}

		DateFormat newDf = new SimpleDateFormat("HH:mm:ss");

		Date time0 = null, time1 = null, time2 = null;

		try {
			time0 = newDf.parse(date0);
			time1 = newDf.parse(date1);
			time2 = newDf.parse(date2);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (!ServicesUtil.isEmpty(time0) && !ServicesUtil.isEmpty(time1) && !ServicesUtil.isEmpty(time1)) {
			if (time0.after(time1) && time0.before(time2)) {
				return true;
			} else
				return false;
		}
		return false;
	}

	public static Date getPrevDate(Date date, String location) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (MurphyConstant.EFS_BASE_LOC_CODE.equalsIgnoreCase(location)) {
			c.add(Calendar.DATE, -1);
			c.set(Calendar.HOUR, 7);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
		} else {
			c.add(Calendar.DATE, -1);
			c.set(Calendar.HOUR, 9);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
		}
		return date = c.getTime();
	}

	// @SuppressWarnings("unchecked")
	// private JSONObject parseDopVarianceResptoUI(PlotlyRequestDto
	// plotlyRequestDto,
	// List<ProductionVarianceDto> productionVarianceDtoList) throws Exception {
	// JSONObject jsonObject = null;
	// JSONArray actualMapList = null;
	// JSONArray projectedMapList = null;
	// JSONArray foreCastMapList = null;
	// if (!ServicesUtil.isEmpty(productionVarianceDtoList)) {
	// jsonObject = new JSONObject();
	// actualMapList = new JSONArray();
	// projectedMapList = new JSONArray();
	// foreCastMapList = new JSONArray();
	//
	// for (ProductionVarianceDto productionVarienceDto :
	// productionVarianceDtoList) {
	// JSONObject actualMap = new JSONObject();
	// JSONObject projectedMap = new JSONObject();
	// JSONObject foreCastMap = new JSONObject();
	// if
	// (MurphyConstant.DOP_CANARY.equalsIgnoreCase(productionVarienceDto.getSource()))
	// {
	// actualMap.put("y", productionVarienceDto.getDataValue());
	// actualMap.put("epochSecond", productionVarienceDto.getCreatedAt());
	// actualMapList.add(actualMap);
	// } else if
	// (MurphyConstant.DOP_PROJECTED.equalsIgnoreCase(productionVarienceDto.getSource()))
	// {
	// projectedMap.put("y", productionVarienceDto.getDataValue());
	// projectedMap.put("epochSecond", productionVarienceDto.getCreatedAt());
	// projectedMapList.add(projectedMap);
	// } else if
	// (MurphyConstant.DOP_FORECAST.equalsIgnoreCase(productionVarienceDto.getSource()))
	// {
	// foreCastMap.put("y", productionVarienceDto.getDataValue());
	// foreCastMap.put("epochSecond", productionVarienceDto.getCreatedAt());
	// foreCastMapList.add(foreCastMap);
	// }
	//
	// }
	// jsonObject.put("actual", actualMapList);
	// jsonObject.put("projected", projectedMapList);
	// jsonObject.put("forecast", foreCastMapList);
	// jsonObject.put("muwi", plotlyRequestDto.getMuwi());
	// jsonObject.put("wellName", plotlyRequestDto.getWellName());
	// }
	//
	// return jsonObject;
	// }

	private Map<String, String> fetchTimeRangeForWell(PlotlyRequestDto plotRequestDto) throws Exception {
		Map<String, String> timeMap = new HashMap<String, String>();
		String startTime = "";
		String endTime = "";

		if (!ServicesUtil.isEmpty(plotRequestDto.getDuration()) && plotRequestDto.getDuration() > 0) {
			DateFormat sdf = new SimpleDateFormat(MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);

			endTime = ServicesUtil.convertFromZoneToZoneString(null, sdf.format(new Date()), MurphyConstant.UTC_ZONE,
					MurphyConstant.CST_ZONE, MurphyConstant.DATEFORMAT_FOR_CANARY_FULL,
					MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MurphyConstant.CANARY_TIME_FORMAT);

			LocalDateTime localDateTime = LocalDateTime.parse(endTime.split("\\.")[0], formatter);

			localDateTime = localDateTime.minusDays(plotRequestDto.getDuration());
			startTime = localDateTime + "." + endTime.split("\\.")[1];

		} else if (!ServicesUtil.isEmpty(plotRequestDto.getStartDate())
				&& !ServicesUtil.isEmpty(plotRequestDto.getEndDate())) {

			DateFormat sdf = new SimpleDateFormat(MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
			endTime = ServicesUtil.convertFromZoneToZoneString(null, sdf.format(plotRequestDto.getEndDate()),
					MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE, MurphyConstant.DATEFORMAT_FOR_CANARY_FULL,
					MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
			startTime = ServicesUtil.convertFromZoneToZoneString(null, sdf.format(plotRequestDto.getStartDate()),
					MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE, MurphyConstant.DATEFORMAT_FOR_CANARY_FULL,
					MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);

		}
		timeMap.put(MurphyConstant.START_TIME, startTime);
		timeMap.put(MurphyConstant.END_TIME, endTime);
		return timeMap;
	}

	private List<JSONObject> fetchDataFromCanary(String startTime, String endTime,
			List<IOPTagDefaultDto> tagDefaultDtoList, JSONArray continuationTagArray, List<JSONObject> jsonObjectList)
			throws Exception {
		List<String> canaryTagList = new ArrayList<String>();

		for (IOPTagDefaultDto tagDto : tagDefaultDtoList) {
			canaryTagList.add("\"MUWI_Prod." + tagDto.getMuwi() + "." + tagDto.getTagName() + "\"");
		}

		// Fetching UserToken
		String userToken = getUserToken();
		if (!ServicesUtil.isEmpty(userToken)) {
			int maxSize = tagDefaultDtoList.size() * 10000;
			String aggregateInterval = "0:00:01";// 15 minutes interval

			// Generate Canary payload
			String payload = "{\"userToken\":\"" + userToken + "\"" + ",\"tags\":" + canaryTagList + ",\"startTime\":\""
					+ startTime + "\"" + ",\"endTime\":\"" + endTime + "\"" + ",\"aggregateInterval\":\""
					+ aggregateInterval + "\"" + " ,\"maxSize\": " + maxSize + "," + "\"continuation\":"
					+ continuationTagArray + "}";

			// String payload = "{\"userToken\":\"" + userToken + "\"" +
			// ",\"tags\":" + canaryTagList + ",\"startTime\":\""
			// + startTime + "\"" + ",\"endTime\":\"" + endTime + "\"" +
			// ",\"aggregateInterval\":\""
			// + aggregateInterval + "\"" + ",\"aggregateName\":\"" +
			// MurphyConstant.AGGR_NAME_MAX + "\""
			// + " ,\"maxSize\": " + maxSize + "," + "\"continuation\":" +
			// continuationTagArray + "}";

			logger.error("Canary OffsetRequest Payload" + payload);

			String url = MurphyConstant.CANARY_API_HOST + "api/v1/getTagData";

			String httpMethod = MurphyConstant.HTTP_METHOD_POST;
			org.json.JSONObject canaryResponse = RestUtil.callRest(url, payload, httpMethod);
			revokeUserTokenService(userToken);

			if (!ServicesUtil.isEmpty(canaryResponse) && canaryResponse.toString().contains("data")) {

				JSONParser parser = new JSONParser();
				org.json.simple.JSONObject json;
				try {
					json = (org.json.simple.JSONObject) parser.parse(canaryResponse.toString());

					if (!json.get("data").toString().equals("{}")) {

						if (json.toString().contains("continuation")) {
							org.json.simple.JSONArray arr = (org.json.simple.JSONArray) json.get("continuation");
							if (arr != null) {
								jsonObjectList.add(json);

								// Recursive call for fetching all points from
								// Canary
								jsonObjectList = fetchDataFromCanary(startTime, endTime, tagDefaultDtoList, arr,
										jsonObjectList);
							} else {
								jsonObjectList.add(json);
							}

						}
					} else {
						logger.error("Offset Canary Response is Empty" + json.get("errors").toString());
					}

				} catch (Exception e) {
					throw e;
				}
			}
		}
		return jsonObjectList;

	}

	// Parsing Canary Response to UI
	@SuppressWarnings({ "unchecked", "null" })
	private JSONObject parseCanaryResponseToUI(List<JSONObject> jsonObjectList, PlotlyRequestDto plotRequestDto,
			List<IOPTagDefaultDto> tagDefaultDtoList, Map<String, String> tagYRangeUnits) throws Exception {
		StringBuffer errorCanaryBuffer = new StringBuffer("");
		JSONObject outputStructure = new JSONObject();
		JSONArray array1 = null;
		long[] epochSecond = null;
		double[] xminEpochSecond = null;
		double[] tagValue = null;
		String[] canaryTime = null;
		String displayName = null;
		IOPTagDefaultDto tagDefaultDto = null;
		int jsonArrayListLength = 0;

		// Handling Error Message for Canary
		JSONObject json = jsonObjectList.get(0);
		JSONArray errorObject = (JSONArray) json.get("errors");
		if (!errorObject.toString().equals("[]")) {
			errorCanaryBuffer.append("Tag not available in Canary for ");
		}

		// Initializing JSONObject to have list of JSONObject
		JSONObject jsonTagObject = new JSONObject();

		// Iterating over EachTags
		for (int j = 0; j < tagDefaultDtoList.size(); j++) {
			String[] rangeArray = null;
			array1 = new JSONArray();
			jsonArrayListLength = 0;
			tagDefaultDto = (IOPTagDefaultDto) tagDefaultDtoList.get(j);

			// Parse JSON structure for EFS and montney fields
			if (!(MurphyConstant.KAYBOB_GASDAILY_TAG.equalsIgnoreCase(tagDefaultDto.getTagName())
					|| MurphyConstant.KAYBOB_INJECTIONDAILY_TAG.equalsIgnoreCase(tagDefaultDto.getTagName()))) {

				String tagPath = MurphyConstant.CANARY_PLOTLY_FOLDERNAME + plotRequestDto.getMuwi() + "."
						+ tagDefaultDto.getTagName();

				// Iterate over List of JSONObject as per continuation tag Logic
				for (JSONObject object : jsonObjectList) {
					JSONObject jsonObj = (JSONObject) object.get("data");
					// Fetch JSONArray specific to Tag
					if (!jsonObj.toString().equals("{}")) {
						if (jsonObj.containsKey(tagPath)) {
							JSONArray jsonArray = (JSONArray) jsonObj.get(tagPath);
							jsonArrayListLength = jsonArrayListLength + jsonArray.size();
							array1.addAll(jsonArray);
						}
					}

				}

				// Canary Error message
				if (jsonArrayListLength == 0) {
					if (ServicesUtil.isEmpty(errorCanaryBuffer)) {
						errorCanaryBuffer.append("No data available in Canary for ");
					}
					errorCanaryBuffer = errorCanaryBuffer.append(tagDefaultDto.getDisplayName()).append(",");
				}

				// Get DisplayNames with units
				if (ServicesUtil.isEmpty(tagDefaultDto.getUnit())) {
					displayName = tagDefaultDto.getDisplayName();
				} else {
					displayName = tagDefaultDto.getDisplayName() + " (" + tagDefaultDto.getUnit().toLowerCase() + ")";
				}

				// Fetch Ymin and Ymax values for units
				if (tagYRangeUnits.containsKey(tagDefaultDto.getUnit())) {
					rangeArray = tagYRangeUnits.get(tagDefaultDto.getUnit()).split("#&#");
				}

				// Creating JSONObject Per Tag
				JSONObject tagJSONObject = new JSONObject();

				tagJSONObject.put("displayName", displayName);
				tagJSONObject.put("axis", tagDefaultDto.getyAxis());
				tagJSONObject.put("ymin", ServicesUtil.isEmpty(rangeArray) ? "" : rangeArray[0]);
				tagJSONObject.put("ymax", ServicesUtil.isEmpty(rangeArray) ? "" : rangeArray[1]);

				// initializing array object
				epochSecond = new long[jsonArrayListLength];
				tagValue = new double[jsonArrayListLength];
				xminEpochSecond = new double[jsonArrayListLength];
				canaryTime = new String[jsonArrayListLength];

				// Iterating over array to fetch value&epoch second specific to
				// tag
				for (int i = 0; i < jsonArrayListLength; i++) {

					// Fetching first array object of Tag
					JSONArray json1 = (JSONArray) array1.get(i);

					// Adding Elements to Seperate Array
					if (!json1.toString().equals("[]")) {
						String jsonstr = (String) json1.get(0);
						String startCanaryTime = jsonstr.substring(0, 10) + " " + jsonstr.substring(11, 19);
						epochSecond[i] = ZonedDateTime.parse(json1.get(0).toString()).toEpochSecond();
						if (!ServicesUtil.isEmpty(json1.get(1))) {
							double x = Double.parseDouble(json1.get(1).toString());
							if (x == (int) x) {
								tagValue[i] = (long) json1.get(1);
								canaryTime[i] = startCanaryTime;
							} else if (!(x == (int) x)) {
								tagValue[i] = (double) json1.get(1);
								canaryTime[i] = startCanaryTime;
							}
						}
					}
				}

				tagJSONObject.put("y", new org.json.JSONArray(tagValue));
				tagJSONObject.put("x_epoch", new org.json.JSONArray(canaryTime));
				tagJSONObject.put("epochSecond", new org.json.JSONArray(epochSecond));

				// Assign Object for respective tag to JSONObjectList
				jsonTagObject.put(tagDefaultDto.getTagName(), tagJSONObject);
			}
		}

		// Calculate GasDaily for Kaybob Location
		// Gas Daily Formula=GasDaily-InjectedDaily
		if (MurphyConstant.KAY_BASE_LOC_CODE.equalsIgnoreCase(plotRequestDto.getLocation())) {

			String tagPath = MurphyConstant.CANARY_PLOTLY_FOLDERNAME + plotRequestDto.getMuwi() + ".";
			JSONObject kaybobJSONObject = new JSONObject();
			JSONArray gasArray = new JSONArray();
			// Iterate over List of JSONObject to fetch Canary Response for GAS
			// DAILY
			for (JSONObject object : jsonObjectList) {
				JSONObject jsonObj = (JSONObject) object.get("data");
				// Fetch JSONArray specific to Tag
				if (!jsonObj.toString().equals("{}")) {
					if (jsonObj.containsKey(tagPath + MurphyConstant.KAYBOB_GASDAILY_TAG)) {
						JSONArray jsonArray = (JSONArray) jsonObj.get(tagPath + MurphyConstant.KAYBOB_GASDAILY_TAG);
						gasArray.addAll(jsonArray);
					}
				}

			}

			JSONArray injectionArray = new JSONArray();
			// Iterate over List of JSONObject to fetch Canary Response for
			// INJECTION DAILY
			for (JSONObject object : jsonObjectList) {
				JSONObject jsonObj = (JSONObject) object.get("data");
				// Fetch JSONArray specific to Tag
				if (!jsonObj.toString().equals("{}")) {
					if (jsonObj.containsKey(tagPath + MurphyConstant.KAYBOB_INJECTIONDAILY_TAG)) {
						JSONArray jsonArray = (JSONArray) jsonObj
								.get(tagPath + MurphyConstant.KAYBOB_INJECTIONDAILY_TAG);
						injectionArray.addAll(jsonArray);
					}
				}

			}

			if (!ServicesUtil.isEmpty(gasArray) && !ServicesUtil.isEmpty(injectionArray)) {

				// initializing array object
				epochSecond = new long[gasArray.size()];
				tagValue = new double[gasArray.size()];
				xminEpochSecond = new double[gasArray.size()];
				canaryTime = new String[gasArray.size()];

				for (int i = 0; i < gasArray.size() && i < injectionArray.size(); i++) {

					JSONArray gasDaily = (JSONArray) gasArray.get(i);
					String gTime = gasDaily.get(0).toString();
					double gValue = 0.0;
					if (!ServicesUtil.isEmpty(gasDaily.get(1))) {
						double x = Double.parseDouble(gasDaily.get(1).toString());
						if (x == (int) x) {
							gValue = (long) gasDaily.get(1);
						} else if (!(x == (int) x)) {
							gValue = (double) gasDaily.get(1);
						}
					}

					JSONArray injectionDaily = (JSONArray) injectionArray.get(i);
					String iTime = injectionDaily.get(0).toString();
					double iValue = 0.0;
					if (!ServicesUtil.isEmpty(injectionDaily.get(1))) {
						double x = Double.parseDouble(injectionDaily.get(1).toString());
						if (x == (int) x) {
							iValue = (long) injectionDaily.get(1);
						} else if (!(x == (int) x)) {
							iValue = (double) injectionDaily.get(1);
						}
					}

					String startCanaryTime = gTime.substring(0, 10) + " " + gTime.substring(11, 19);
					epochSecond[i] = ZonedDateTime.parse(gTime).toEpochSecond();
					if (gTime.equalsIgnoreCase(iTime)) {
						tagValue[i] = gValue - iValue;
						canaryTime[i] = startCanaryTime;

					}
				}
				kaybobJSONObject.put("y", new org.json.JSONArray(tagValue));
				kaybobJSONObject.put("x_epoch", new org.json.JSONArray(canaryTime));
				kaybobJSONObject.put("epochSecond", new org.json.JSONArray(epochSecond));
			} else {
				// Canary Error message
				if (gasArray.size() == 0 || injectionArray.size() == 0) {
					if (ServicesUtil.isEmpty(errorCanaryBuffer)) {
						errorCanaryBuffer.append("No data available in Canary for ");
					}
					errorCanaryBuffer = errorCanaryBuffer.append("Gas Daily").append(",");
				}

				kaybobJSONObject.put("y", new org.json.JSONArray());
				kaybobJSONObject.put("x_epoch", new org.json.JSONArray());
				kaybobJSONObject.put("epochSecond", new org.json.JSONArray());

			}
			kaybobJSONObject.put("axis", "");
			kaybobJSONObject.put("ymin", "");
			kaybobJSONObject.put("ymax", "");
			kaybobJSONObject.put("displayName", "Gas Daily (mcf)");
			jsonTagObject.put(MurphyConstant.KAYBOB_GASDAILY_TAG, kaybobJSONObject);

		}
		if (!ServicesUtil.isEmpty(errorCanaryBuffer)) {
			errorCanaryBuffer = errorCanaryBuffer.delete(errorCanaryBuffer.lastIndexOf(","),
					errorCanaryBuffer.length());
			errorCanaryBuffer = errorCanaryBuffer.append(". Please contact Canary Administrator.");
			outputStructure.put("message", errorCanaryBuffer.toString());
		} else {
			outputStructure.put("message", "Success");
		}

		outputStructure.put("muwi", plotRequestDto.getMuwi());
		outputStructure.put("wellName", plotRequestDto.getWellName());
		outputStructure.put("values", jsonTagObject);
		String finalStrut = outputStructure.toString();
		JSONParser parser = new JSONParser();
		JSONObject json1 = (JSONObject) parser.parse(finalStrut);
		return json1;
	}

	// Generate User Token
	public static String getUserToken() {

		try {

			String username = MurphyConstant.CANARY_USERNAME;

			String password = MurphyConstant.CANARY_PASSWORD;

			String userTokenPayload = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\","
					+ "\"timeZone\":\"" + MurphyConstant.CANARY_TIMEZONE + "\",\"application\":\""
					+ MurphyConstant.CANARY_APP + "\"}";

			String url = MurphyConstant.CANARY_API_HOST + "api/v1/getUserToken";

			String httpMethod = "POST";

			org.json.JSONObject canaryResponseObject = RestUtil.callUserTokenRest(url, userTokenPayload, httpMethod,
					username, password);

			if (!ServicesUtil.isEmpty(canaryResponseObject) && canaryResponseObject.toString().contains("userToken")) {

				JSONParser parser = new JSONParser();

				org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser
						.parse(canaryResponseObject.toString());

				return (String) json.get("userToken");
			} else {
				logger.error("CanaryStagingScheduler.getUserToken()[error]: Canary API Response empty");
			}
		} catch (Exception e) {
			logger.error("CanaryStagingScheduler.getUserToken()[error]" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	// Revoking User Token
	public static String revokeUserTokenService(String userToken) {

		try {

			String payload = "{\"userToken\":\"" + userToken + "\"" + "}";

			String httpMethod = MurphyConstant.HTTP_METHOD_POST;

			String url = MurphyConstant.CANARY_API_HOST + "api/v1/revokeUserToken";

			org.json.JSONObject canaryResponse = RestUtil.callRest(url, payload, httpMethod);

			if (!ServicesUtil.isEmpty(canaryResponse) && canaryResponse.toString().contains("statusCode")) {
				JSONParser parser = new JSONParser();
				org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(canaryResponse.toString());
				return json.get("statusCode").toString();
			} else {
				logger.error("CRTCanaryService.revokeUserTokenService()[error]: Canary API Response empty");
			}
		} catch (Exception e) {
			logger.error("CRTCanaryService.revokeUserTokenService()[error]" + e.getMessage());
			e.printStackTrace();
			return "RevokeUserToken_Exception: " + e;
		}

		return null;
	}

}
