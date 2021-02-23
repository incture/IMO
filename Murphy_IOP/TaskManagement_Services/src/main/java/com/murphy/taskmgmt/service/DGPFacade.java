package com.murphy.taskmgmt.service;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.print.attribute.standard.Severity;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murphy.integration.dao.EnersightProveDailyDao;
import com.murphy.integration.dto.UIRequestDto;
import com.murphy.integration.util.DBConnections;
import com.murphy.taskmgmt.dao.ConfigDao;
import com.murphy.taskmgmt.dao.FracHitDao;
import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dao.IOPTagDefaultDao;
import com.murphy.taskmgmt.dao.ProductionVarianceDao;
import com.murphy.taskmgmt.dto.DOPVarianceDto;
import com.murphy.taskmgmt.dto.DOPVarianceResponseDto;
import com.murphy.taskmgmt.dto.PlotlyRequestDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("dgpFacade")
public class DGPFacade {

	private static final Logger logger = LoggerFactory.getLogger(DGPFacade.class);

	@Autowired
	IOPTagDefaultDao tagDefaultDao;

	@Autowired
	FracHitDao fracHitDao;

	@Autowired
	ProductionVarianceDao productionVarianceDao;

	@Autowired
	HierarchyDao hierarchyDao;
	
	@Autowired
	ConfigDao configDao;

	public DOPVarianceResponseDto fetchDGPDOPData(UIRequestDto uiRequestDto) {
		DOPVarianceResponseDto responseDto = new DOPVarianceResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {

			logger.error("In fetchDGPDOPData");
			List<DOPVarianceDto> dopVarianceDtoList = fetchDailyDataForDOPDGP(uiRequestDto);
			Date date = new Date();
			if (uiRequestDto.getDuration() == 1) {

				date = ServicesUtil.roundDateToNearInterval(date, 60, MurphyConstant.MINUTES);

				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.HOUR, -1);
				calendar.setTime(date);
				calendar.set(Calendar.SECOND, 00);
				calendar.set(Calendar.MINUTE, 00);
				date = calendar.getTime();
			} else {
				date = productionVarianceDao.scaleDownTimeToSeventhHour(date).getTime();
			}
			responseDto.setDataAsOffDisplay(ServicesUtil.convertFromZoneToZoneString(date, null, "", "", "",
					MurphyConstant.DATE_DISPLAY_FORMAT));

			// Converting to epoch
			String str = responseDto.getDataAsOffDisplay();
			SimpleDateFormat df = new SimpleDateFormat(MurphyConstant.DATE_DISPLAY_FORMAT);
			Date d1 = df.parse(str);
			long epoch = d1.getTime();
			responseDto.setDataAsOffDisplayEpoch(epoch);
			logger.error(String.valueOf(epoch));
			//

			responseDto.setLocationType(uiRequestDto.getLocationType());
			if (!ServicesUtil.isEmpty(dopVarianceDtoList)) {
				responseDto.setDopVarianceDtoList(dopVarianceDtoList);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				if (uiRequestDto.getCountryCode().equals(MurphyConstant.CA_CODE)
						&& (uiRequestDto.getLocationType().equals(MurphyConstant.FIELD)
								|| uiRequestDto.getLocationType().equals(MurphyConstant.BASE)))
					responseMessage.setMessage("Please select Facility to see data");
				else
					responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);

		} catch (Exception e) {
			logger.error("[Murphy][VarianceFacade][fetchDGPDOPData][error]" + e.getMessage());
			e.printStackTrace();
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;

	}

	public List<DOPVarianceDto> fetchDailyData(UIRequestDto uiRequestDto) {
		List<DOPVarianceDto> varianceDataList = null;
		DOPVarianceDto dto = null;
		try {
			if (!ServicesUtil.isEmpty(uiRequestDto) && !ServicesUtil.isEmpty(uiRequestDto.getLocationCodeList())
					&& !ServicesUtil.isEmpty(uiRequestDto.getReportId())
					&& !ServicesUtil.isEmpty(uiRequestDto.getCountryCode())) {

				if (uiRequestDto.getCountryCode().equals(MurphyConstant.CA_CODE)
						&& uiRequestDto.getLocationType().equals(MurphyConstant.FIELD)) {
					return null;
				} else {
					varianceDataList = new ArrayList<>();
					Connection connection = DBConnections.createConnectionForProve();
					// When no rolled up
					if ((MurphyConstant.DGP_REPORT_ID.equalsIgnoreCase(uiRequestDto.getReportId())
							|| MurphyConstant.DOP_REPORT_ID.equalsIgnoreCase(uiRequestDto.getReportId()))
							&& !uiRequestDto.isRolledUp()) {
						Set<String> muwiSet = ServicesUtil.convertListToSet(hierarchyDao.getMuwiByLocationTypeAndCode(
								uiRequestDto.getLocationType(), uiRequestDto.getLocationCodeList()));
						List<String> muwiList = ServicesUtil.convertSetToList(muwiSet);
						logger.error("muwiList : " + muwiList);
						varianceDataList = getData(connection, varianceDataList, uiRequestDto, muwiList,
								uiRequestDto.getLocationCodeList().get(0));
					} else if ((MurphyConstant.DGP_REPORT_ID.equalsIgnoreCase(uiRequestDto.getReportId())
							|| MurphyConstant.DOP_REPORT_ID.equalsIgnoreCase(uiRequestDto.getReportId()))
							&& uiRequestDto.isRolledUp()) {
						// When rolled up is true
						// Key : Main Location Code , Value: List of Muwi
						// present in that Location code
						HashMap<String, List<String>> levelMuwiMap = new HashMap<>();
						List<String> list = null;
						for (String s : uiRequestDto.getLocationCodeList()) {
							list = new ArrayList<>();
							list.add(s);
							dto = new DOPVarianceDto();
							dto.setLocationCode(s);
							dto.setHasInvestigation(false);
							dto.setDispatch(false);
							dto.setLocationType(uiRequestDto.getLocationType());
							dto.setLocation(hierarchyDao.getLocationByLocCode(s));
							dto.setTier("N/A");
							varianceDataList.add(dto);
							Set<String> muwiSet = ServicesUtil.convertListToSet(
									hierarchyDao.getMuwiByLocationTypeAndCode(uiRequestDto.getLocationType(), list));
							levelMuwiMap.put(s, ServicesUtil.convertSetToList(muwiSet));
						}
						for (Map.Entry<String, List<String>> map : levelMuwiMap.entrySet())
							getData(connection, varianceDataList, uiRequestDto, map.getValue(), map.getKey());
					}
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][DGPFacade][fetchDailyGasData][Exception]" + e.getMessage());
		}

		return varianceDataList;
	}

	private List<DOPVarianceDto> getData(Connection connection, List<DOPVarianceDto> varianceDataList,
			UIRequestDto uiRequestDto, List<String> muwiList, String locCode) throws Exception {
		try {

			HashMap<String, Double> muwiForecastData = null;

			// Get Actual data from Canary based on their muwi
			HashMap<String, Double> muwiCanaryData = fetchActualDataFromCanary(muwiList, locCode, uiRequestDto);
			settingDataForDGPDOP(muwiCanaryData, varianceDataList, MurphyConstant.DOP_CANARY, locCode,
					uiRequestDto.isRolledUp());

			// Getting Forecast data from Enersight
			if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID)
					&& !ServicesUtil.isEmpty(connection)
					&& uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE))
				muwiForecastData = new EnersightProveDailyDao().fetchDOPForecastValueFromEnersight(connection,
						muwiList);

			else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID)
					&& !ServicesUtil.isEmpty(connection)
					&& uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE))
				muwiForecastData = new EnersightProveDailyDao().fetchEFSGasForecastValueFromEnersight(connection,
						muwiList);

			else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID)
					&& !ServicesUtil.isEmpty(connection)
					&& uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE))
				muwiForecastData = new EnersightProveDailyDao().fetchCanadaGasForecastFromEnerSight(connection,
						muwiList);

			else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID)
					&& !ServicesUtil.isEmpty(connection)
					&& uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE))
				muwiForecastData = new EnersightProveDailyDao().fetchCanadaOilForecastFromEnerSight(connection,
						muwiList);

			if (!ServicesUtil.isEmpty(muwiForecastData) && muwiForecastData.size() > 0)
				settingDataForDGPDOP(muwiForecastData, varianceDataList, MurphyConstant.DOP_FORECAST, locCode,
						uiRequestDto.isRolledUp());

			// Getting Projected data
			Date currentDate = roundDateToNearest(new Date(), 60, MurphyConstant.MINUTES, MurphyConstant.CST_ZONE);
			Calendar c = Calendar.getInstance();
			c.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
			c.setTime(currentDate);
			int currentHour = c.get(Calendar.HOUR_OF_DAY);
			HashMap<String, Double> muwiProjectedData = calculateProjectedData(currentHour, muwiCanaryData,
					uiRequestDto);
			settingDataForDGPDOP(muwiProjectedData, varianceDataList, MurphyConstant.DOP_PROJECTED, locCode,
					uiRequestDto.isRolledUp());

			// Getting the Rest of details( InvgId,dispatchId etc)
			// When NO Rolled Up data
			if (!uiRequestDto.isRolledUp()) {

				// Preparing map from variance list : muwi(key) and
				// DOPVarianceDto(value)
				Map<String, DOPVarianceDto> mapVarainceDataList = new HashMap<String, DOPVarianceDto>();
				for (DOPVarianceDto obj : varianceDataList) {
					mapVarainceDataList.put(obj.getMuwi(), obj);
				}
				// clearing contents of varianceDataList
				varianceDataList.clear();

				// On selection of multiple fields, batching is done field-wise
				if (uiRequestDto.getLocationType().equalsIgnoreCase(MurphyConstant.FIELD)
						&& uiRequestDto.getLocationCodeList().size() > 1) {
					for (String field : uiRequestDto.getLocationCodeList()) {
						List<String> list = new ArrayList<String>();
						list.add(field);
						String dgpQuery = productionVarianceDao.dgpQueryForOtherDetails(list,
								uiRequestDto.getLocationType(), uiRequestDto.getDuration(), uiRequestDto.getUserType(),
								uiRequestDto.isRolledUp());
						logger.error(" For field :  " + field + " DATA Query : " + dgpQuery);
						varianceDataList = productionVarianceDao.setWellDataForDGPDOP(dgpQuery, varianceDataList,
								uiRequestDto, mapVarainceDataList);
						list.remove(0);
					}
				} else {
					String dgpQuery = productionVarianceDao.dgpQueryForOtherDetails(uiRequestDto.getLocationCodeList(),
							uiRequestDto.getLocationType(), uiRequestDto.getDuration(), uiRequestDto.getUserType(),
							uiRequestDto.isRolledUp());
					logger.error(" DATA Query : " + dgpQuery);
					varianceDataList = productionVarianceDao.setWellDataForDGPDOP(dgpQuery, varianceDataList,
							uiRequestDto, mapVarainceDataList);
				}
			}

		} catch (Exception e) {
			logger.error("[Murphy][DGPFacade][getDataForGas][Exception]" + e.getMessage());
			throw e;
		}
		return varianceDataList;
	}

	private void settingDataForDGPDOP(HashMap<String, Double> mapData, List<DOPVarianceDto> varianceDataList,
			String value, String locCode, boolean isRolledUp) {
		try {
			DecimalFormat df = new DecimalFormat("#.##");
			if (!ServicesUtil.isEmpty(mapData) && value.equalsIgnoreCase(MurphyConstant.DOP_CANARY)) {
				for (Map.Entry<String, Double> map : mapData.entrySet()) {
					if (isRolledUp) {
						// For Rolled Up Data
						for (DOPVarianceDto dto : varianceDataList) {
							if (locCode.equalsIgnoreCase(dto.getLocationCode())) {
								if (ServicesUtil.isEmpty(dto.getActualBoed()))
									dto.setActualBoed(Double.valueOf(df.format(map.getValue())));
								else
									dto.setActualBoed(Double.valueOf(df
											.format(dto.getActualBoed() + Double.valueOf(df.format(map.getValue())))));
								break;
							}
						}
					} else {
						// At well level
						DOPVarianceDto dto = new DOPVarianceDto();
						dto.setActualBoed(
								ServicesUtil.isEmpty(map.getValue()) ? 0d : Double.valueOf(df.format(map.getValue())));
						dto.setMuwi(ServicesUtil.isEmpty(map.getKey()) ? null : map.getKey());
						varianceDataList.add(dto);
					}
				}

			} else if (!ServicesUtil.isEmpty(mapData) && value.equalsIgnoreCase(MurphyConstant.DOP_FORECAST)) {
				for (Map.Entry<String, Double> map : mapData.entrySet()) {
					for (DOPVarianceDto dto : varianceDataList) {
						if (!isRolledUp && dto.getMuwi().equalsIgnoreCase(map.getKey())) {
							dto.setForecastBoed(Double.valueOf(df.format(map.getValue())));
						} else if (isRolledUp && locCode.equalsIgnoreCase(dto.getLocationCode())) {
							if (ServicesUtil.isEmpty(dto.getForecastBoed()))
								dto.setForecastBoed(Double.valueOf(df.format(map.getValue())));
							else
								dto.setForecastBoed(Double.valueOf(
										df.format(dto.getForecastBoed() + Double.valueOf(df.format(map.getValue())))));
							break;
						}
					}
				}
			} else if (!ServicesUtil.isEmpty(mapData) && value.equalsIgnoreCase(MurphyConstant.DOP_PROJECTED)) {
				for (Map.Entry<String, Double> map : mapData.entrySet()) {
					for (DOPVarianceDto dto : varianceDataList) {
						if (!isRolledUp && dto.getMuwi().equalsIgnoreCase(map.getKey())) {
							dto.setProjectedBoed(map.getValue());
							// Setting variance
							if (!ServicesUtil.isEmpty(dto.getProjectedBoed())
									&& !ServicesUtil.isEmpty(dto.getForecastBoed())) {
								dto.setVariance(
										Double.valueOf(df.format(dto.getProjectedBoed() - dto.getForecastBoed())));
								if (dto.getProjectedBoed() != 0d && dto.getForecastBoed() != 0d) {
									dto.setVariancePercent(
											Double.valueOf(df.format(dto.getVariance() / dto.getForecastBoed() * 100)));
								}
							}
						} else if (isRolledUp && locCode.equalsIgnoreCase(dto.getLocationCode())) {
							if (ServicesUtil.isEmpty(dto.getProjectedBoed()))
								dto.setProjectedBoed(Double.valueOf(df.format(map.getValue())));
							else {
								dto.setProjectedBoed(Double.valueOf(
										df.format(dto.getProjectedBoed() + Double.valueOf(df.format(map.getValue())))));
								if (!ServicesUtil.isEmpty(dto.getProjectedBoed())
										&& !ServicesUtil.isEmpty(dto.getForecastBoed())) {
									dto.setVariance(
											Double.valueOf(df.format(dto.getProjectedBoed() - dto.getForecastBoed())));
									if (dto.getProjectedBoed() != 0d && dto.getForecastBoed() != 0d) {
										dto.setVariancePercent(Double
												.valueOf(df.format(dto.getVariance() / dto.getForecastBoed() * 100)));
									}
								}
							}
							break;
						}

					}
				}

			}
		} catch (Exception e) {
			logger.error("Exception [settingDataForDGPDOP] [Error] : " + e.getMessage());
			e.printStackTrace();
		}
	}

	private HashMap<String, Double> calculateProjectedData(int currentHour, HashMap<String, Double> muwiCanaryData,
			UIRequestDto uiRequestDto) throws Exception {
		HashMap<String, Double> projectedMap = null;
		DecimalFormat df = new DecimalFormat("#.##");
		try {
			if (!ServicesUtil.isEmpty(muwiCanaryData)
					&& uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE)) {
				if (currentHour < 7)
					currentHour = currentHour + 24;

				projectedMap = new HashMap<>();
				for (Map.Entry<String, Double> map : muwiCanaryData.entrySet()) {
					double maxValue = Double.valueOf(df.format(map.getValue()));
					double dataValue = (maxValue / (currentHour - 6)) * 24;
					projectedMap.put(map.getKey(), Double.valueOf(df.format(dataValue)));
				}
			} else if (!ServicesUtil.isEmpty(muwiCanaryData)
					&& uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE)) {
				if (currentHour < 9) {
					currentHour = currentHour + 24;
				}
				projectedMap = new HashMap<>();
				for (Map.Entry<String, Double> map : muwiCanaryData.entrySet()) {
					double maxValue = Double.valueOf(df.format(map.getValue()));
					double dataValue = (maxValue / (currentHour - 8)) * 24;
					projectedMap.put(map.getKey(), Double.valueOf(df.format(dataValue)));
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][DGPFacade][calculateProjectedData][Exception]" + e.getMessage());
			throw e;
		}
		return projectedMap;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Double> fetchActualDataFromCanary(List<String> muwiList, String locCode,
			UIRequestDto uiRequestDto) throws Exception {
		HashMap<String, Double> muwiCanaryData = null;
		DecimalFormat df = new DecimalFormat("#.##");
		try {
			// Generate UserToken
			String userToken = getUserToken();

			// Setting Payload for Canary
			int maxSize = 10000;
			String continuation = null;
			String aggregateInterval = "0:01:00:00";
			JSONArray inputJsonArray = new JSONArray();
			String tagName = "";
			String startTime = calculateStartTime(uiRequestDto.getCountryCode());
			Date currentDate = roundDateToNearest(new Date(), 60, MurphyConstant.MINUTES, MurphyConstant.CST_ZONE);
			String endTime = ServicesUtil.convertFromZoneToZoneString(currentDate, null, "", MurphyConstant.CST_ZONE,
					"", MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);

			// EFS
			if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE)) {
				// EFS DOP
				if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID))
					tagName += MurphyConstant.CANARY_PARAM_PV[0];
				// EFS DGP
				else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID))
					tagName += MurphyConstant.CANARY_PARAM[3];
			}
			// Canada
			else if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE)) {
				// Kaybob DOP
				if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID))
					tagName += MurphyConstant.CANARY_CANADA_PARAM[0];

				else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID)) {
					String muwi = "";
					if (!ServicesUtil.isEmpty(muwiList.get(0)))
						muwi = muwiList.get(0);
					else if (muwiList.size() > 1)
						muwi = muwiList.get(1);
					String facilityText = hierarchyDao.getFacilityDetailsforMuwi(muwi).getFacility();
					if (facilityText.toLowerCase().contains(MurphyConstant.KAYBOB_LOC.toLowerCase()))
						tagName += MurphyConstant.CANARY_CANADA_PARAM[1];
					else if (facilityText.toLowerCase().contains(MurphyConstant.TUPPER_MAIN_LOC.toLowerCase()))
						tagName += MurphyConstant.CANARY_CANADA_PARAM[2];
					else if (facilityText.toLowerCase().contains(MurphyConstant.TUPPER_WEST_LOC.toLowerCase()))
						tagName += MurphyConstant.CANARY_CANADA_PARAM[3];
				}
			}

			if (!ServicesUtil.isEmpty(tagName)) {
				for (String muwi : muwiList)
					inputJsonArray.add("MUWI_Prod." + muwi + "." + tagName);
				String url = MurphyConstant.CANARY_API_HOST + "api/v1/getTagData";
				String httpMethod = MurphyConstant.HTTP_METHOD_POST;

				String payload = "{\"userToken\":\"" + userToken + "\"" + ",\"tags\":" + inputJsonArray
						+ ",\"startTime\":\"" + startTime + "\"" + ",\"endTime\":\"" + endTime + "\""
						+ ",\"aggregateName\":\"" + MurphyConstant.AGGR_NAME_MAX + "\"" + ",\"aggregateInterval\":\""
						+ aggregateInterval + "\"" + ",\"continuation\":" + continuation + ",\"maxSize\":" + maxSize
						+ "}";

				logger.error("DGP-DOP Canary Request Payload:::" + payload);

				org.json.JSONObject canaryResponse = RestUtil.callRest(url, payload, httpMethod);

				// Revoke User Token
				revokeUserTokenService(userToken);

				// Data setting from response to HashMap
				if (!ServicesUtil.isEmpty(canaryResponse) && canaryResponse.toString().contains("data")) {
					JSONParser parser = new JSONParser();
					org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser
							.parse(canaryResponse.toString());
					logger.error("Canary Json : " + json);
					if (!json.get("data").toString().equals("{}")) {
						muwiCanaryData = new HashMap<>();
						JsonNode node = new ObjectMapper().readTree(json.toString());
						Iterator<String> itera = node.get("data").fieldNames();
						while (itera.hasNext()) {
							String fieldName = itera.next();
							int arraySize = node.get("data").get(fieldName).size();
							if (!ServicesUtil.isEmpty(node.get("data").get(fieldName)) && arraySize > 0) {
								// logger.error("Data Value : " +
								// node.get("data").get(fieldName).get(arraySize
								// - 1).get(1));
								int size = arraySize - 1;
								muwiCanaryData.put(fieldName.split("\\.")[1], 0d);
								while (size > 0) {
									// logger.error("Outside Well Muwi : "+
									// fieldName.split("\\.")[1]+" [Node
									// ActualData :] "
									// +node.get("data").get(fieldName).get(size).get(1));
									if (!node.get("data").get(fieldName).get(size).get(1).isNull()
											&& node.get("data").get(fieldName).get(size).get(1) != null
											&& node.get("data").get(fieldName).get(size).get(1).toString() != "null") {
										// logger.error("Inside Well Muwi : "+
										// fieldName.split("\\.")[1]+
										// " [Node ActualData :] "
										// +node.get("data").get(fieldName).get(size).get(1));
										muwiCanaryData.put(fieldName.split("\\.")[1], Double.valueOf(df
												.format(node.get("data").get(fieldName).get(size).get(1).asDouble())));
										break;
									}
									size--;
								}
							}
						}
					} else
						logger.error(
								"Tag not available in canary.Please Contact Administrator. Or there is Error on tags from Canary");
				}
			} else {
				logger.error("No Tag passed to Canary");
				throw new Exception("No Tag passed to Canary");
			}
		} catch (Exception e) {
			logger.error("[DGPFacade][fetchActualDataFromCanary][Exception]" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		logger.error("muwiCanaryData : " + muwiCanaryData.toString());
		return muwiCanaryData;

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

	private String calculateStartTime(String countryCode) {
		String originalDate = null;
		try {
			Calendar c = Calendar.getInstance();
			c.setTimeZone(TimeZone.getTimeZone(MurphyConstant.IST_ZONE));
			DateFormat canaryDateFormat = new SimpleDateFormat(MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
			originalDate = ServicesUtil.convertFromZoneToZoneString(c.getTime(), null, null, MurphyConstant.CST_ZONE,
					"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss");

			Date dateNew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originalDate);
			Boolean time = timeBetweenOrNot(dateNew, countryCode);
			logger.error("[Murphy][DGPFacade][calculateStartTime][dateNew]" + dateNew);
			logger.error("[Murphy][DGPFacade][calculateStartTime][time]" + time);
			if (time.equals(true)) {
				originalDate = canaryDateFormat.format(getPrevDate(dateNew, countryCode));
				System.out.println("[Murphy][DGPFacade][calculateStartTime][getPrevDate- originalDate]" + originalDate);
			} else {
				Calendar calender = Calendar.getInstance();
				calender.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
				if (countryCode.equalsIgnoreCase(MurphyConstant.EFS_CODE))
					c.set(Calendar.HOUR, 7);
				else if (countryCode.equalsIgnoreCase(MurphyConstant.CA_CODE))
					c.set(Calendar.HOUR, 9);
				calender.set(Calendar.MINUTE, 0);
				calender.set(Calendar.SECOND, 0);
				calender.set(Calendar.MILLISECOND, 0);
				originalDate = ServicesUtil.convertFromZoneToZoneString(calender.getTime(), null, null, null,
						MurphyConstant.DATEFORMAT_FOR_CANARY_FULL, MurphyConstant.DATEFORMAT_FOR_CANARY_FULL);
				System.out.println("[Murphy][DGPFacade][calculateStartTime][originalDate]" + originalDate);
			}
		} catch (Exception e) {
			logger.error("Error while calculing startTime" + e.getMessage());
		}
		logger.error("Original Date : " + originalDate);
		return originalDate;
	}

	public Boolean timeBetweenOrNot(Date date, String countryCode) {

		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		String date0 = df.format(date);
		String date1 = "00:00:00";
		String date2 = "";
		if (countryCode.equalsIgnoreCase(MurphyConstant.EFS_CODE))
			date2 = "06:59:59";
		else if (countryCode.equalsIgnoreCase(MurphyConstant.CA_CODE))
			date2 = "08:59:59";

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

	public static Date getPrevDate(Date date, String countryCode) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -1);
		if (countryCode.equalsIgnoreCase(MurphyConstant.EFS_CODE))
			c.set(Calendar.HOUR, 7);
		else if (countryCode.equalsIgnoreCase(MurphyConstant.CA_CODE))
			c.set(Calendar.HOUR, 9);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return date = c.getTime();
	}

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

	public List<DOPVarianceDto> getReport(UIRequestDto uiRequestDto) {
		return fetchDGPDOPData(uiRequestDto).getDopVarianceDtoList();
	}

	// adding below 3 methods for the requirement of the incident of unit conver
	// to bbl and e3m3 for dop/dgp

	public List<DOPVarianceDto> fetchDailyDataForDOPDGP(UIRequestDto uiRequestDto) {
		List<DOPVarianceDto> varianceDataList = null;
		DOPVarianceDto dto = null;
		try {
			if (!ServicesUtil.isEmpty(uiRequestDto) && !ServicesUtil.isEmpty(uiRequestDto.getLocationCodeList())
					&& !ServicesUtil.isEmpty(uiRequestDto.getReportId())
					&& !ServicesUtil.isEmpty(uiRequestDto.getCountryCode())) {

				if (uiRequestDto.getCountryCode().equals(MurphyConstant.CA_CODE)
						&& uiRequestDto.getLocationType().equals(MurphyConstant.FIELD)) {
					return null;
				} else {
					varianceDataList = new ArrayList<>();
					Connection connection = DBConnections.createConnectionForProve();
					// When no rolled up
					if ((MurphyConstant.DGP_REPORT_ID.equalsIgnoreCase(uiRequestDto.getReportId())
							|| MurphyConstant.DOP_REPORT_ID.equalsIgnoreCase(uiRequestDto.getReportId()))
							&& !uiRequestDto.isRolledUp()) {
						Set<String> muwiSet = ServicesUtil.convertListToSet(hierarchyDao.getMuwiByLocationTypeAndCode(
								uiRequestDto.getLocationType(), uiRequestDto.getLocationCodeList()));
						List<String> muwiList = ServicesUtil.convertSetToList(muwiSet);
						logger.error("muwiList : " + muwiList);
						varianceDataList = test( varianceDataList, uiRequestDto, muwiList,
								uiRequestDto.getLocationCodeList().get(0));
//						varianceDataList = getDataForDOPDGP(connection, varianceDataList, uiRequestDto, muwiList,
//								uiRequestDto.getLocationCodeList().get(0));
					} else if ((MurphyConstant.DGP_REPORT_ID.equalsIgnoreCase(uiRequestDto.getReportId())
							|| MurphyConstant.DOP_REPORT_ID.equalsIgnoreCase(uiRequestDto.getReportId()))
							&& uiRequestDto.isRolledUp()) {
						// When rolled up is true
						// Key : Main Location Code , Value: List of Muwi
						// present in that Location code
						HashMap<String, List<String>> levelMuwiMap = new HashMap<>();
						List<String> list = null;
						for (String s : uiRequestDto.getLocationCodeList()) {
							list = new ArrayList<>();
							list.add(s);
							dto = new DOPVarianceDto();
							dto.setLocationCode(s);
							dto.setHasInvestigation(false);
							dto.setDispatch(false);
							dto.setLocationType(uiRequestDto.getLocationType());
							dto.setLocation(hierarchyDao.getLocationByLocCode(s));
							dto.setTier("N/A");
							varianceDataList.add(dto);
							Set<String> muwiSet = ServicesUtil.convertListToSet(
									hierarchyDao.getMuwiByLocationTypeAndCode(uiRequestDto.getLocationType(), list));
							levelMuwiMap.put(s, ServicesUtil.convertSetToList(muwiSet));
						}
						for (Map.Entry<String, List<String>> map : levelMuwiMap.entrySet())
							varianceDataList =test(varianceDataList, uiRequestDto, map.getValue(), map.getKey());
							//varianceDataList = getDataForDOPDGP(connection, varianceDataList, uiRequestDto, map.getValue(), map.getKey());
					}
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][DGPFacade][fetchDailyDataForDOPDGP][Exception]" + e.getMessage());
		}

		return varianceDataList;
	}

	private List<DOPVarianceDto> getDataForDOPDGP(Connection connection, List<DOPVarianceDto> varianceDataList,
			UIRequestDto uiRequestDto, List<String> muwiList, String locCode) throws Exception {
		try {

			HashMap<String, Double> muwiForecastData = null;
			// Get Actual data from Canary based on their muwi
		      //Timestamp timestampActualsStart = new Timestamp(System.currentTimeMillis());
		   // logger.error("timestamp : " +timestampActualsStart);
            HashMap<String, Double> muwiCanaryData = fetchActualDataFromCanary(muwiList, locCode, uiRequestDto);

			settingDataForDGPDOPWithUntiConversion(muwiCanaryData, varianceDataList, MurphyConstant.DOP_CANARY, locCode,
					uiRequestDto.isRolledUp(), uiRequestDto);
			// Timestamp timestampActualsEnd = new Timestamp(System.currentTimeMillis());
			// logger.error("timestamp : " +timestampActualsStart);
			// Getting Forecast data from Enersight
			if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID)
					&& !ServicesUtil.isEmpty(connection)
					&& uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE))
				muwiForecastData = new EnersightProveDailyDao().fetchDOPForecastValueFromEnersight(connection,
						muwiList);

			else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID)
					&& !ServicesUtil.isEmpty(connection)
					&& uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE))
				muwiForecastData = new EnersightProveDailyDao().fetchEFSGasForecastValueFromEnersight(connection,
						muwiList);

			else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID)
					&& !ServicesUtil.isEmpty(connection)
					&& uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE))
				muwiForecastData = new EnersightProveDailyDao().fetchCanadaGasForecastFromEnerSight(connection,
						muwiList);

			else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID)
					&& !ServicesUtil.isEmpty(connection)
					&& uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE))
				muwiForecastData = new EnersightProveDailyDao().fetchCanadaOilForecastFromEnerSight(connection,
						muwiList);

			if (!ServicesUtil.isEmpty(muwiForecastData) && muwiForecastData.size() > 0)
				settingDataForDGPDOPWithUntiConversion(muwiForecastData, varianceDataList, MurphyConstant.DOP_FORECAST,
						locCode, uiRequestDto.isRolledUp(), uiRequestDto);

			// Getting Projected data
			Date currentDate = roundDateToNearest(new Date(), 60, MurphyConstant.MINUTES, MurphyConstant.CST_ZONE);
			Calendar c = Calendar.getInstance();
			c.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
			c.setTime(currentDate);
			int currentHour = c.get(Calendar.HOUR_OF_DAY);
			HashMap<String, Double> muwiProjectedData = calculateProjectedData(currentHour, muwiCanaryData,
					uiRequestDto);
			settingDataForDGPDOPWithUntiConversion(muwiProjectedData, varianceDataList, MurphyConstant.DOP_PROJECTED,
					locCode, uiRequestDto.isRolledUp(), uiRequestDto);

			// Getting the Rest of details( InvgId,dispatchId etc)
			// When NO Rolled Up data
			if (!uiRequestDto.isRolledUp()) {

				// Preparing map from variance list : muwi(key) and
				// DOPVarianceDto(value)
				Map<String, DOPVarianceDto> mapVarainceDataList = new HashMap<String, DOPVarianceDto>();
				for (DOPVarianceDto obj : varianceDataList) {
					mapVarainceDataList.put(obj.getMuwi(), obj);
				}
				// clearing contents of varianceDataList
				varianceDataList.clear();

				// On selection of multiple fields, batching is done field-wise
				if (uiRequestDto.getLocationType().equalsIgnoreCase(MurphyConstant.FIELD)
						&& uiRequestDto.getLocationCodeList().size() > 1) {
					for (String field : uiRequestDto.getLocationCodeList()) {
						List<String> list = new ArrayList<String>();
						list.add(field);
						String dgpQuery = productionVarianceDao.dgpQueryForOtherDetails(list,
								uiRequestDto.getLocationType(), uiRequestDto.getDuration(), uiRequestDto.getUserType(),
								uiRequestDto.isRolledUp());
						logger.error(" For field :  " + field + " DATA Query : " + dgpQuery);
						varianceDataList = productionVarianceDao.setWellDataForDGPDOP(dgpQuery, varianceDataList,
								uiRequestDto, mapVarainceDataList);
						list.remove(0);
					}
				} else {
					String dgpQuery = productionVarianceDao.dgpQueryForOtherDetails(uiRequestDto.getLocationCodeList(),
							uiRequestDto.getLocationType(), uiRequestDto.getDuration(), uiRequestDto.getUserType(),
							uiRequestDto.isRolledUp());
					logger.error(" DATA Query : " + dgpQuery);
					varianceDataList = productionVarianceDao.setWellDataForDGPDOP(dgpQuery, varianceDataList,
							uiRequestDto, mapVarainceDataList);
				}
			}

		} catch (Exception e) {
			logger.error("[Murphy][DGPFacade][getDataForGas][Exception]" + e.getMessage());
			throw e;
		}
		return varianceDataList;
	}

	private void settingDataForDGPDOPWithUntiConversion(HashMap<String, Double> mapData,
			List<DOPVarianceDto> varianceDataList, String value, String locCode, boolean isRolledUp,
			UIRequestDto uiRequestDto) {
		try {
			DecimalFormat df = new DecimalFormat("#.##");
			if (!ServicesUtil.isEmpty(mapData) && value.equalsIgnoreCase(MurphyConstant.DOP_CANARY)) {
				Double actualsvalue = null;
				for (Map.Entry<String, Double> map : mapData.entrySet()) {
					if (isRolledUp) {
						// For Rolled Up Data
						for (DOPVarianceDto dto : varianceDataList) {
							if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE)) {
								if (locCode.equalsIgnoreCase(dto.getLocationCode())) {

									if (ServicesUtil.isEmpty(dto.getActualBoed())) {
										actualsvalue = Double.valueOf(df.format(map.getValue()));
										if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID)) {
											actualsvalue = actualsvalue * MurphyConstant.OIL_FACTOR;
										}
										if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID)) {
											actualsvalue = actualsvalue * MurphyConstant.GAS_FACTOR;
										}

										dto.setActualBoed(Double.valueOf(df.format(actualsvalue)));
									} else {
										actualsvalue = Double.valueOf(df.format(map.getValue()));

										if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID)) {
											actualsvalue = actualsvalue * MurphyConstant.OIL_FACTOR;
											actualsvalue = Double.valueOf(df.format(dto.getActualBoed()))
													+ actualsvalue;
										}
										if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID)) {
											actualsvalue = actualsvalue * MurphyConstant.GAS_FACTOR;
											actualsvalue = Double.valueOf(df.format(dto.getActualBoed()))
													+ actualsvalue;
										}
										dto.setActualBoed(Double.valueOf(df.format(actualsvalue)));
									}
									break;

								}
							}

							// efs
							if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE)) {
								if (locCode.equalsIgnoreCase(dto.getLocationCode())) {
									if (ServicesUtil.isEmpty(dto.getActualBoed()))
										dto.setActualBoed(Double.valueOf(df.format(map.getValue())));
									else
										dto.setActualBoed(Double.valueOf(df.format(
												dto.getActualBoed() + Double.valueOf(df.format(map.getValue())))));
									break;
								}
							}

						}
					} else {
						// At well level
						if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE)) {
							DOPVarianceDto dto = new DOPVarianceDto();
							actualsvalue = ServicesUtil.isEmpty(map.getValue()) ? 0d
									: Double.valueOf(df.format(map.getValue()));
							if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID)) {
								actualsvalue = actualsvalue * MurphyConstant.OIL_FACTOR;
							}
							if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID)) {
								actualsvalue = actualsvalue * MurphyConstant.GAS_FACTOR;
							}
							dto.setActualBoed(Double.valueOf(df.format(actualsvalue)));
							dto.setMuwi(ServicesUtil.isEmpty(map.getKey()) ? null : map.getKey());

							varianceDataList.add(dto);
						} else {
							DOPVarianceDto dto = new DOPVarianceDto();
							dto.setActualBoed(ServicesUtil.isEmpty(map.getValue()) ? 0d
									: Double.valueOf(df.format(map.getValue())));
							dto.setMuwi(ServicesUtil.isEmpty(map.getKey()) ? null : map.getKey());
							varianceDataList.add(dto);
						}
					}
				}

			} else if (!ServicesUtil.isEmpty(mapData) && value.equalsIgnoreCase(MurphyConstant.DOP_FORECAST)) {
				
				for (Map.Entry<String, Double> map : mapData.entrySet()) {
					for (DOPVarianceDto dto : varianceDataList) {
						if (!isRolledUp && dto.getMuwi().equalsIgnoreCase(map.getKey())) {
							dto.setForecastBoed(Double.valueOf(df.format(map.getValue())));
						} else if (isRolledUp && locCode.equalsIgnoreCase(dto.getLocationCode())) {
							if (ServicesUtil.isEmpty(dto.getForecastBoed()))
								dto.setForecastBoed(Double.valueOf(df.format(map.getValue())));
							else
								dto.setForecastBoed(Double.valueOf(df.format(dto.getForecastBoed() + Double.valueOf(df.format(map.getValue())))));
							break;
						}
					}
				}
//				Double forecastValue;
//				for (Map.Entry<String, Double> map : mapData.entrySet()) {
//					for (DOPVarianceDto dto : varianceDataList) {
//						if (!isRolledUp && dto.getMuwi().equalsIgnoreCase(map.getKey())) {
//							forecastValue = Double.valueOf(df.format(map.getValue()));
//							
//							dto.setForecastBoed(forecastValue);
////							if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE)) {
////
////								if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID)) {
////									forecastValue = forecastValue * MurphyConstant.OIL_FACTOR;
////								} else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID)) {
////									forecastValue = forecastValue * MurphyConstant.GAS_FACTOR;
////								}
////								dto.setForecastBoed(forecastValue);
////							}
////
////							else if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE)) {
//								//dto.setForecastBoed(forecastValue);
//							//}
//
//						}
//
//						else if (isRolledUp && locCode.equalsIgnoreCase(dto.getLocationCode())) {
//							if (ServicesUtil.isEmpty(dto.getForecastBoed())) {
//								forecastValue = Double.valueOf(df.format(map.getValue()));
//								dto.setForecastBoed(forecastValue);
////								if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE)) {
////
////									if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID)) {
////										forecastValue = forecastValue * MurphyConstant.OIL_FACTOR;
////									} else if (uiRequestDto.getReportId()
////											.equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID)) {
////										forecastValue = forecastValue * MurphyConstant.GAS_FACTOR;
////									}
////									dto.setForecastBoed(forecastValue);
////								} else if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE)) {
////									dto.setForecastBoed(forecastValue);
////								}
//							} else {
//								
//								forecastValue = Double.valueOf(df.format(dto.getForecastBoed() + Double.valueOf(df.format(map.getValue()))));
//								dto.setForecastBoed(forecastValue);
////								forecastValue = Double.valueOf(
////										df.format(dto.getForecastBoed() + Double.valueOf(df.format(map.getValue()))));
////								forecastValue = Double.valueOf(df.format(map.getValue()));
////								if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE)) {
////
////									if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID)) {
////										forecastValue = forecastValue * MurphyConstant.OIL_FACTOR;
////										forecastValue= Double.valueOf(df.format(dto.getForecastBoed())) + forecastValue;
////									} else if (uiRequestDto.getReportId()
////											.equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID)) {
////										forecastValue = forecastValue * MurphyConstant.GAS_FACTOR;
////										forecastValue= Double.valueOf(df.format(dto.getForecastBoed())) + forecastValue;
////									}
////									dto.setForecastBoed(forecastValue);
////								} else if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE)) {
////									forecastValue = Double.valueOf(df.format(dto.getForecastBoed() + Double.valueOf(df.format(map.getValue()))));
////									dto.setForecastBoed(forecastValue);
////								}
//
//							}
//							break;
//						}
//					}
//				}
			} else if (!ServicesUtil.isEmpty(mapData) && value.equalsIgnoreCase(MurphyConstant.DOP_PROJECTED)) {
				Double projectedValue;
				for (Map.Entry<String, Double> map : mapData.entrySet()) {
					for (DOPVarianceDto dto : varianceDataList) {
						if (!isRolledUp && dto.getMuwi().equalsIgnoreCase(map.getKey())) {

							projectedValue = Double.valueOf(df.format(map.getValue()));
							// canada alldata
							if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE)) {
								if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID)) {
									projectedValue = projectedValue * MurphyConstant.OIL_FACTOR;
								} else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID)) {
									projectedValue = projectedValue * MurphyConstant.GAS_FACTOR;
								}
								Double projectedvalue=Double.valueOf(df.format(projectedValue));
								projectedvalue=Math.round(projectedvalue * 100.0) / 100.0;
								dto.setProjectedBoed(projectedvalue);
								//dto.setProjectedBoed(Double.valueOf(df.format(projectedValue)));
								
								// Setting variance
								if (!ServicesUtil.isEmpty(dto.getProjectedBoed())
										&& !ServicesUtil.isEmpty(dto.getForecastBoed())) {
									dto.setVariance(
											Double.valueOf(df.format(dto.getProjectedBoed() - dto.getForecastBoed())));
									if (dto.getProjectedBoed() != 0d && dto.getForecastBoed() != 0d) {
										dto.setVariancePercent(Double
												.valueOf(df.format(dto.getVariance() / dto.getForecastBoed() * 100)));
									}
								}

							}

							// efs all data
							if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE)) {
								
								Double projectedvalue=Double.valueOf(df.format(map.getValue()));
								projectedvalue=Math.round(projectedvalue * 100.0) / 100.0;
								dto.setProjectedBoed(projectedvalue);
								//dto.setProjectedBoed(map.getValue());
								// Setting variance
								if (!ServicesUtil.isEmpty(dto.getProjectedBoed())
										&& !ServicesUtil.isEmpty(dto.getForecastBoed())) {
									dto.setVariance(
											Double.valueOf(df.format(dto.getProjectedBoed() - dto.getForecastBoed())));
									if (dto.getProjectedBoed() != 0d && dto.getForecastBoed() != 0d) {
										dto.setVariancePercent(Double
												.valueOf(df.format(dto.getVariance() / dto.getForecastBoed() * 100)));
									}
								}
							}
						} else if (isRolledUp && locCode.equalsIgnoreCase(dto.getLocationCode())) {

							if (ServicesUtil.isEmpty(dto.getProjectedBoed())) {
								projectedValue = Double.valueOf(df.format(map.getValue()));
								// canada rolled up
								if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE)) {
									if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID)) {

										projectedValue = projectedValue * MurphyConstant.OIL_FACTOR;
									} else if (uiRequestDto.getReportId()
											.equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID)) {
										projectedValue = projectedValue * MurphyConstant.GAS_FACTOR;
									}
									
									Double projectedvalue=Double.valueOf(df.format(projectedValue));
									projectedvalue=Math.round(projectedvalue * 100.0) / 100.0;
									dto.setProjectedBoed(projectedvalue);
									//dto.setProjectedBoed(Double.valueOf(df.format(projectedValue)));
								}
								// efs rolled up
								else if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE)) {
									Double projectedvalue=projectedValue;
									projectedvalue=Math.round(projectedvalue * 100.0) / 100.0;
									dto.setProjectedBoed(projectedvalue);
									//dto.setProjectedBoed(projectedValue);
								}

							}

							else {

								if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE)) {
                                
//									dto.setProjectedBoed(Double.valueOf(df.format(
//											dto.getProjectedBoed() + Double.valueOf(df.format(map.getValue())))));
									 Double projectedvalue= Double.valueOf(df.format(
												dto.getProjectedBoed() + Double.valueOf(df.format(map.getValue()))));
									 projectedvalue=Math.round(projectedvalue * 100.0) / 100.0;
									 dto.setProjectedBoed(projectedvalue);
									if (!ServicesUtil.isEmpty(dto.getProjectedBoed())
											&& !ServicesUtil.isEmpty(dto.getForecastBoed())) {
										dto.setVariance(Double
												.valueOf(df.format(dto.getProjectedBoed() - dto.getForecastBoed())));
										if (dto.getProjectedBoed() != 0d && dto.getForecastBoed() != 0d) {
											dto.setVariancePercent(Double.valueOf(
													df.format(dto.getVariance() / dto.getForecastBoed() * 100)));
										}
									}
								}

								if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE)) {

//									projectedValue = Double.valueOf(df.format(
//											dto.getProjectedBoed() + Double.valueOf(df.format(map.getValue()))));
									
									projectedValue = Double.valueOf(df.format(map.getValue()));
									if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID)) {

										projectedValue = projectedValue * MurphyConstant.OIL_FACTOR;
										projectedValue= Double.valueOf(df.format(dto.getProjectedBoed()))+ projectedValue;
									} else if (uiRequestDto.getReportId()
											.equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID)) {
										projectedValue = projectedValue * MurphyConstant.GAS_FACTOR;
										projectedValue= Double.valueOf(df.format(dto.getProjectedBoed()))+ projectedValue;
									}
									
									
									//dto.setProjectedBoed(Double.valueOf(df.format(projectedValue)));
									
									Double projectedvalue= Double.valueOf(df.format(projectedValue));
									projectedvalue=Math.round(projectedvalue * 100.0) / 100.0;
									dto.setProjectedBoed(projectedvalue);
									if (!ServicesUtil.isEmpty(dto.getProjectedBoed())
											&& !ServicesUtil.isEmpty(dto.getForecastBoed())) {
										dto.setVariance(Double
												.valueOf(df.format(dto.getProjectedBoed() - dto.getForecastBoed())));
										if (dto.getProjectedBoed() != 0d && dto.getForecastBoed() != 0d) {
											dto.setVariancePercent(Double.valueOf(
													df.format(dto.getVariance() / dto.getForecastBoed() * 100)));
										}
									}
								}
							}
							break;
						}

					}
				}

			}
		} catch (Exception e) {
			logger.error("Exception [settingDataForDGPDOP] [Error] : " + e.getMessage());
			e.printStackTrace();
		}
	}
	private List<DOPVarianceDto> test(List<DOPVarianceDto> varianceDataList,
			UIRequestDto uiRequestDto, List<String> muwiList, String locCode) throws Exception {
		try {
			int roundOffInterval = 60;
			String roundOffIntervalType = MurphyConstant.MINUTES;
			HashMap<String, Double> muwiForecastData = null;
			// Get Actual data from Canary based on their muwi
		    Date currentDate = new Date();

			currentDate = roundDateToNearest(currentDate, roundOffInterval, roundOffIntervalType,MurphyConstant.UTC_ZONE);
			String currentDateInDbformat=ServicesUtil.convertFromZoneToZoneString(currentDate, null, "",MurphyConstant.UTC_ZONE, "",
					MurphyConstant.DATE_DB_FORMATE_SD);
            HashMap<String, Double> muwiCanaryData = productionVarianceDao.fetchActualDataFromHana(muwiList, locCode, uiRequestDto,currentDateInDbformat);

			settingDataForDGPDOPWithUntiConversion(muwiCanaryData, varianceDataList, MurphyConstant.DOP_CANARY, locCode,
					uiRequestDto.isRolledUp(), uiRequestDto);
			String zoneoffset = "";
			String resetHour="";
			String zone="";
//			forecast
			
			//if EFS
			if(uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE))
			{
			if(uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID))
			{
		    zoneoffset = configDao.getConfigurationByRef(MurphyConstant.ZONE_OFFSET_CANARY_EFS_OIL);
		    resetHour = configDao.getConfigurationByRef(MurphyConstant.RESET_HOUR_EFS_OIL);
			
			}
			if(uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID))
			{
		    zoneoffset = configDao.getConfigurationByRef(MurphyConstant.ZONE_OFFSET_CANARY_EFS_GAS);
		    resetHour = configDao.getConfigurationByRef(MurphyConstant.RESET_HOUR_EFS_GAS);
			
			}
			}
			
			//if Canada
			if(uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE))
			{
			if(uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID))
			{
		    zoneoffset = configDao.getConfigurationByRef(MurphyConstant.ZONE_OFFSET_CANARY_CA_OIL);
		    resetHour = configDao.getConfigurationByRef(MurphyConstant.RESET_HOUR_CA_OIL);
			
			}
			if(uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID))
			{
				String muwiId = "";
				String facilityText="";
				if (!ServicesUtil.isEmpty(muwiList.get(0)))
					muwiId = muwiList.get(0);
				else if (muwiList.size() > 1)
					muwiId = muwiList.get(1);
				facilityText = hierarchyDao.getFacilityDetailsforMuwi(muwiId).getFacility();
				
				if (facilityText.toLowerCase().contains(MurphyConstant.KAYBOB_LOC.toLowerCase()))
				{
				zoneoffset = configDao.getConfigurationByRef(MurphyConstant.ZONE_OFFSET_CANARY_CA_KAYBOB_GAS);
			    resetHour = configDao.getConfigurationByRef(MurphyConstant.RESET_HOUR_CA_KAYBOB_GAS);
				}
			    else if (facilityText.toLowerCase().contains(MurphyConstant.TUPPER_MAIN_LOC.toLowerCase()))
			    {
			    zoneoffset = configDao.getConfigurationByRef(MurphyConstant.ZONE_OFFSET_CANARY_CA_MONTNEY_GAS_ONE);
			    resetHour = configDao.getConfigurationByRef(MurphyConstant.RESET_HOUR_CA_MONTNEY_GAS_ONE);
			    }
				
			    else if (facilityText.toLowerCase().contains(MurphyConstant.TUPPER_WEST_LOC.toLowerCase()))
			    {
			    zoneoffset = configDao.getConfigurationByRef(MurphyConstant.ZONE_OFFSET_CANARY_CA_MONTNEY_GAS_TWO);
			    resetHour = configDao.getConfigurationByRef(MurphyConstant.RESET_HOUR_CA_MONTNEY_GAS_TWO);
			    }
		    
			
			}
			}
			
			zone = getZoneFromOffset(zoneoffset);
			int resetHr = 0;
			if(!ServicesUtil.isEmpty(resetHour))
			{
			resetHr= Integer.valueOf(resetHour);
			}
			Date endOfDay = null;
			Calendar c = Calendar.getInstance();
			c.setTimeZone(TimeZone.getTimeZone(zone));
			c.setTime(currentDate);
			int currentHour = c.get(Calendar.HOUR_OF_DAY);
			if (currentHour >= resetHr)
			c.add(Calendar.DATE, 1);
			c.set(Calendar.HOUR_OF_DAY,resetHr);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			endOfDay = c.getTime();
			String nextDay = ServicesUtil.convertFromZoneToZoneString(endOfDay, null, "", MurphyConstant.UTC_ZONE,
					"", MurphyConstant.DATE_DB_FORMATE_SD);

			muwiForecastData = productionVarianceDao.fetchForecastDataFromHana(muwiList, locCode, uiRequestDto,nextDay);
			if (!ServicesUtil.isEmpty(muwiForecastData) && muwiForecastData.size() > 0)
				settingDataForDGPDOPWithUntiConversion(muwiForecastData, varianceDataList, MurphyConstant.DOP_FORECAST,
						locCode, uiRequestDto.isRolledUp(), uiRequestDto);

		//projected data
			
			HashMap<String, Double> muwiProjectedData= productionVarianceDao.fetchprojectedDataFromHana(muwiList, locCode, uiRequestDto,nextDay);
			settingDataForDGPDOPWithUntiConversion(muwiProjectedData, varianceDataList, MurphyConstant.DOP_PROJECTED,
					locCode, uiRequestDto.isRolledUp(), uiRequestDto);

			// Getting the Rest of details( InvgId,dispatchId etc)
			// When NO Rolled Up data
			if (!uiRequestDto.isRolledUp()) {

				// Preparing map from variance list : muwi(key) and
				// DOPVarianceDto(value)
				Map<String, DOPVarianceDto> mapVarainceDataList = new HashMap<String, DOPVarianceDto>();
				for (DOPVarianceDto obj : varianceDataList) {
					mapVarainceDataList.put(obj.getMuwi(), obj);
				}
				// clearing contents of varianceDataList
				varianceDataList.clear();

				// On selection of multiple fields, batching is done field-wise
				if (uiRequestDto.getLocationType().equalsIgnoreCase(MurphyConstant.FIELD)
						&& uiRequestDto.getLocationCodeList().size() > 1) {
					for (String field : uiRequestDto.getLocationCodeList()) {
						List<String> list = new ArrayList<String>();
						list.add(field);
						String dgpQuery = productionVarianceDao.dgpQueryForOtherDetails(list,
								uiRequestDto.getLocationType(), uiRequestDto.getDuration(), uiRequestDto.getUserType(),
								uiRequestDto.isRolledUp());
						logger.error(" For field :  " + field + " DATA Query : " + dgpQuery);
						varianceDataList = productionVarianceDao.setWellDataForDGPDOP(dgpQuery, varianceDataList,
								uiRequestDto, mapVarainceDataList);
						list.remove(0);
					}
				} else {
					String dgpQuery = productionVarianceDao.dgpQueryForOtherDetails(uiRequestDto.getLocationCodeList(),
							uiRequestDto.getLocationType(), uiRequestDto.getDuration(), uiRequestDto.getUserType(),
							uiRequestDto.isRolledUp());
					logger.error(" DATA Query : " + dgpQuery);
					varianceDataList = productionVarianceDao.setWellDataForDGPDOP(dgpQuery, varianceDataList,
							uiRequestDto, mapVarainceDataList);
				}
			}

		} catch (Exception e) {
			logger.error("[Murphy][DGPFacade][getDataForGas][Exception]" + e.getMessage());
			throw e;
		}
		return varianceDataList;
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
}
