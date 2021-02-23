package com.murphy.taskmgmt.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.murphy.geotab.Coordinates;
import com.murphy.geotab.Device;
import com.murphy.geotab.HierarchyDto;
import com.murphy.geotab.NearestUserDto;
import com.murphy.geotab.NearestUserDtoResponse;
import com.murphy.geotab.UserNameDto;
import com.murphy.taskmgmt.dao.AutoSignInDao;
import com.murphy.taskmgmt.dao.ConfigDao;
import com.murphy.taskmgmt.dao.EmpDetailsDao;
import com.murphy.taskmgmt.dao.GeoTabDao;
import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dao.UserIDPMappingDao;
import com.murphy.taskmgmt.dto.DeviceStatusInfoDto;
import com.murphy.taskmgmt.dto.FieldResponseDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.UserIDPMappingDto;

@Component("GeoTabUtil")
public class GeoTabUtil {

	@Autowired
	static HierarchyDao hierarchyDao;
	

	private static final Logger logger = LoggerFactory.getLogger(GeoTabUtil.class);

	/**
	 * @param entity
	 *            - JSONEntity which is to be sent as Request Payload
	 * @return - JSONObject returned by the Rest Call
	 */
	private static JSONObject callRest(String entity) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		JSONObject obj = null;
		int count = 0;
		try {
			HttpPost httpPost = new HttpPost(GeoTabConstants.GEO_TAB_BASE_URL);

			// converting the jsonEntity as String Entity
			StringEntity params = new StringEntity(entity);
			httpPost.addHeader("content-type", "application/json; charset=UTF-8");
			httpPost.setEntity(params);

			// executing the post method as HttpClient
			HttpResponse response = httpClient.execute(httpPost);

			// receiving the response as Json String
			String json = EntityUtils.toString(response.getEntity());
			obj = new JSONObject(json);
			// logger.info("[geotab] Entity Response from API : "+obj);
			if (verifyGeoTabResponse(obj)) {
				refreshGeoTabSessionId();
				count++;
				if (count < 3) {
					obj = GeoTabUtil.callRest(entity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Exception : " + e.getMessage());
		}
		return obj;
	}

	public static Coordinates getLocBySerialId(String serialId) {
		String deviceSearchEntity = null;
		JSONObject deviceObject = null;
		JSONObject deviceStatusObject = null;
		String deviceId = null;
		String deviceStatusInfoEntity = null;
		Double latitude = null;
		Double longitude = null;
		Coordinates coordinates = null;
		try {
			deviceSearchEntity = formDeviceSearchEntity(serialId);
		} catch (ParseException e) {
			logger.debug("[GeoTabUtil][getLocBySerialId][deviceSearchEntity]Exception : " + e.getMessage());
		}
		if (!ServicesUtil.isEmpty(deviceSearchEntity))
			deviceObject = callRest(deviceSearchEntity);
		if (!ServicesUtil.isEmpty(deviceObject) && deviceObject.toString().contains("id")
				&& !deviceObject.toString().contains("error")) {
			deviceId = deviceObject.getJSONArray("result").getJSONObject(0).getString("id");
			try {
				deviceStatusInfoEntity = formDeviceStatusInfoEntity(deviceId);
			} catch (ParseException e) {
				logger.debug("[GeoTabUtil][getLocBySerialId][deviceStatusInfoEntity]Exception : " + e.getMessage());
			}
			if (!ServicesUtil.isEmpty(deviceStatusInfoEntity))
				deviceStatusObject = callRest(deviceStatusInfoEntity);
			if (!ServicesUtil.isEmpty(deviceStatusObject) && deviceStatusObject.toString().contains("latitude")) {
				latitude = deviceStatusObject.getJSONArray("result").getJSONObject(0).getDouble("latitude");
				longitude = deviceStatusObject.getJSONArray("result").getJSONObject(0).getDouble("longitude");
			}
		}
		if (!ServicesUtil.isEmpty(latitude) && !ServicesUtil.isEmpty(longitude))
			coordinates = new Coordinates(latitude, longitude);
		return coordinates;
	}

	public static boolean verifyGeoTabResponse(JSONObject jsonObject) {
		if (jsonObject.toString().contains("error")) {
			System.err.println("jsonObject [error]  : " + jsonObject.toString());
			JSONObject errors = jsonObject.getJSONObject("error").getJSONArray("errors").getJSONObject(0);
			if (errors.getString("name").equalsIgnoreCase("InvalidUserException")) {
				return true;
			}
		}
		return false;
	}

	public static void refreshGeoTabSessionId() {

		ConfigDao configDao = SpringContextBridge.services().getConfigDao();

		String newSessionId = GeoTabUtil.generateSessionId();
		int count = 0;
		try {
			configDao.saveOrUpdateConfigByRef(MurphyConstant.GEOTAB_SESSION_ID_REF, newSessionId);
			configDao.saveOrUpdateConfigByRef(MurphyConstant.GEOTAB_SESSION_ID_REFRESH_DATE_REF,
					new SimpleDateFormat(MurphyConstant.DATE_DB_FORMATE).format(new Date()));
			// ServicesUtil.setProperty("geotab.sessionId", newSessionId,
			// MurphyConstant.IOP_PROPERTY_FILE_LOCATION);
			// ServicesUtil.setProperty("geotab.sessionId.refreshtime", new
			// Date(), MurphyConstant.IOP_PROPERTY_FILE_LOCATION);
		} catch (Exception ex) {
			logger.error("Refreshing GeoTab Session Id Exception : " + ex);
			if (count < 3) {
				GeoTabUtil.refreshGeoTabSessionId();
				count++;
			}
		}
	}

	public static String generateSessionId() {
		JSONObject jsonObject = null;
		String sessionId = null;
		try {
			jsonObject = GeoTabUtil.callRest(formAuthenticationEntity());
		} catch (ParseException e) {
			logger.error("[GeoTabUtil][generateSessionId][ParseException] : " + e);
		}
		if (!ServicesUtil.isEmpty(jsonObject)) {
			JSONObject credentials = jsonObject.getJSONObject("result").getJSONObject("credentials");
			sessionId = credentials.getString("sessionId");
		}
		return sessionId;
	}

	private static String formAuthenticationEntity() throws ParseException {

		ConfigDao configDao = SpringContextBridge.services().getConfigDao();

		Map<String, String> credentials = new HashMap<String, String>();
		credentials.put("database", configDao.getConfigurationByRef(MurphyConstant.GEOTAB_DATABASE_REF));
		credentials.put("userName", configDao.getConfigurationByRef(MurphyConstant.GEOTAB_USERNAME_REF));
		credentials.put("password", configDao.getConfigurationByRef(MurphyConstant.GEOTAB_PASSWORD_REF));

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("params", credentials);
		parameters.put("method", GeoTabConstants.GEO_TAB_METHOD_AUTHENTICATE);
		String jsonString = JsonUtil.addObjects(null, parameters);
		return jsonString;
	}

	/**
	 * @param p1
	 *            - coordinates of the device
	 * @param p2
	 *            - coordinated of the center
	 * @return distance between two points
	 */
	public static Double getDistance(Coordinates p1, Coordinates p2) {

		Double dLat = GeoTabUtil.degreeToRad(p2.getLatitude() - p1.getLatitude());
		Double dLon = GeoTabUtil.degreeToRad(p2.getLongitude() - p1.getLongitude());

		Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(GeoTabUtil.degreeToRad(p1.getLatitude()))
				* Math.cos(GeoTabUtil.degreeToRad(p2.getLatitude())) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		Double d = GeoTabConstants.EARTH_RADIUS_IN_MILES * c;
		return d;
	}

	/**
	 * @param degree
	 *            - degree to be converted
	 * @return - Double value of converted radian
	 */
	public static Double degreeToRad(Double degree) {
		return degree * (Math.PI / 180);
	}

	/**
	 * @param center
	 *            - well location
	 * @param point
	 *            - device location
	 * @param radius
	 *            - radius in Miles
	 * @return - Boolean if a device is inside formed perimeter or not
	 */
	@SuppressWarnings("unused")
	private Boolean CheckIfPointInside(Coordinates center, Coordinates point, Double radius) {
		Double distance = GeoTabUtil.getDistance(point, center);
		if (distance > radius) {
			return false;
		} else if (distance <= radius) {
			return true;
		} else {
			return false;
		}
	}

	public static NearestUserDtoResponse getUsers(String location, String type) {
		NearestUserDtoResponse userDtoResponse = new NearestUserDtoResponse();
		List<NearestUserDto> usersDto = null;
		List<HierarchyDto> listHierarchy = null;

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(GeoTabConstants.FAILURE);
		responseMessage.setStatusCode(GeoTabConstants.CODE_FAILURE);

		// GeoTabDao geoTabDao = SpringContextBridge.services().getGeoTabDao();

		if (!ServicesUtil.isEmpty(location) && !ServicesUtil.isEmpty(type)) {
			listHierarchy = new GeoTabUtil().getHierarchy(location, type);
			List<Coordinates> coordinates = null;
			Coordinates coordinate = null;
			if (!ServicesUtil.isEmpty(listHierarchy)) {
				coordinate = new Coordinates();
				coordinates = new ArrayList<Coordinates>();
				usersDto = new ArrayList<NearestUserDto>();
				for (HierarchyDto hierarchyDto : listHierarchy) {
					// usersDto.addAll(getUsers(hierarchyDto.getLatitude(),
					// hierarchyDto.getLongitude()).getNearestUsers());
					coordinates.add(new Coordinates(hierarchyDto.getLatitude(), hierarchyDto.getLongitude()));
				}
				coordinate = getAverageCoordinate(coordinates);
				usersDto.addAll(getUsers(coordinate.getLatitude(), coordinate.getLongitude(), null, null, null)
						.getNearestUsers());
				/*
				 * Collections.sort(usersDto, new DistanceComparator());
				 * List<NearestUserDto> listFromDB = geoTabDao.getUsersFromDB();
				 * if(!ServicesUtil.isEmpty(listFromDB)){
				 * usersDto.addAll(listFromDB); }
				 */
				responseMessage.setStatus(GeoTabConstants.SUCCESS);
				responseMessage.setStatusCode(GeoTabConstants.CODE_SUCCESS);
				responseMessage.setMessage("Users Fetched Successfully");
			}
		} else {
			responseMessage.setMessage("Location and Type Required");
		}
		userDtoResponse.setResponseMessage(responseMessage);
		userDtoResponse.setNearestUsers(usersDto);
		return userDtoResponse;
	}

	private static Coordinates getAverageCoordinate(List<Coordinates> coordinates) {
		List<Double> latitudes = null;
		List<Double> longitudes = null;
		OptionalDouble averageLatitude = null;
		OptionalDouble averageLongitude = null;
		Coordinates averageCoordinate = null;
		if (!ServicesUtil.isEmpty(coordinates)) {
			latitudes = new ArrayList<Double>();
			longitudes = new ArrayList<Double>();
			for (Coordinates coordinate : coordinates) {
				latitudes.add(coordinate.getLatitude());
				longitudes.add(coordinate.getLongitude());
			}
			averageLatitude = latitudes.stream().mapToDouble(a -> a).average();
			averageLongitude = longitudes.stream().mapToDouble(a -> a).average();

			averageCoordinate = new Coordinates(averageLatitude.getAsDouble(), averageLongitude.getAsDouble());
		}
		return averageCoordinate;
	}

	private List<HierarchyDto> getHierarchy(String location, String type) {

		GeoTabDao geoTabDao = SpringContextBridge.services().getGeoTabDao();

		List<HierarchyDto> listHierarchy = null;
		String query = "";
		if (!ServicesUtil.isEmpty(location) && !ServicesUtil.isEmpty(type)) {
			listHierarchy = new ArrayList<HierarchyDto>();
			query = "SELECT JH.BUSINESSENTITY AS BUSINESSENTITY, JH.BUISNESSUNIT AS BUISNESSUNIT, JH.FIELD AS FIELD, JH.FACILITY AS FACILITY, JH.WELLPAD AS WELLPAD, JH.WELL AS WELL, JH.MUWI AS MUWI, JHL.LATITIUDE AS LATITIUDE, JHL.LONGITUDE AS LONGITUDE FROM JSA_HIERARCHY_LOCATION JHL LEFT JOIN JSA_HIERARCHY JH ON JH.MUWI = JHL.MUWI ";
			// query = "SELECT LC.LATITUDE, LC.LONGITUDE FROM
			// PRODUCTION_LOCATION PL JOIN LOCATION_COORDINATE LC ON
			// PL.LOCATION_CODE = LC.LOCATION_CODE ";
			switch (type) {
			case GeoTabConstants.LOC_FIELD:
				query += " WHERE JHL.MUWI IN (SELECT DISTINCT(FACILITY) FROM JSA_HIERARCHY WHERE FIELD = '" + location
						+ "') AND JHL.LOCATION_TYPE = 'FACILITY'";
				listHierarchy = geoTabDao.getHierarchy(query);
				break;
			case GeoTabConstants.LOC_CF:
				query += " WHERE JHL.MUWI = '" + location + "' AND JHL.LOCATION_TYPE = 'FACILITY'";
				listHierarchy = geoTabDao.getHierarchy(query);
				break;
			case GeoTabConstants.LOC_WELLPAD:
				query += " WHERE JH.WELLPAD = '" + location + "' AND JHL.LOCATION_TYPE = 'WELL'";
				// query += " WHERE PL.PARENT_CODE = '"+location+"' ";
				listHierarchy = geoTabDao.getHierarchy(query);
				break;
			/*
			 * case GeoTabConstants.LOC_WELL :
			 * System.out.println(GeoTabConstants.LOC_WELL); query +=
			 * "WHERE JH.WELL = '"+location+"'"; listHierarchy =
			 * geoTabDao.getHierarchy(query); break;
			 */
			default:
				System.out.println("Please send the correct Facility Type");
				break;
			}
		}
		return listHierarchy;
	}

	/*
	 * @SuppressWarnings("unchecked") private List<HierarchyDto>
	 * getHierarchy(String query) { // ResultSet resultSet = null; HierarchyDto
	 * hierarchyDto = null; List<HierarchyDto> listHierarchy = null;
	 * if(!ServicesUtil.isEmpty(query)) { listHierarchy = new
	 * ArrayList<HierarchyDto>(); // resultSet =
	 * DBConnect.getDbCon().query(query); Query q =
	 * this.getSession().createSQLQuery(query.trim()); List<Object[]> resultList
	 * = q.list(); if(!ServicesUtil.isEmpty(resultList)){ for(Object[] obj :
	 * resultList){ hierarchyDto = new HierarchyDto();
	 * hierarchyDto.setBusinessEntity(ServicesUtil.isEmpty(obj[0])?null:(String)
	 * obj[0]);
	 * hierarchyDto.setBusinessUnit(ServicesUtil.isEmpty(obj[1])?null:(String)
	 * obj[1]); hierarchyDto.setField(ServicesUtil.isEmpty(obj[2])?null:(String)
	 * obj[2]);
	 * hierarchyDto.setFacility(ServicesUtil.isEmpty(obj[3])?null:(String)
	 * obj[3]);
	 * hierarchyDto.setWellpad(ServicesUtil.isEmpty(obj[4])?null:(String)
	 * obj[4]); hierarchyDto.setWell(ServicesUtil.isEmpty(obj[5])?null:(String)
	 * obj[5]); hierarchyDto.setMuwi(ServicesUtil.isEmpty(obj[6])?null:(String)
	 * obj[6]);
	 * hierarchyDto.setLatitude(ServicesUtil.isEmpty(obj[7])?null:(Double)
	 * (obj[7]));
	 * hierarchyDto.setLongitude(ServicesUtil.isEmpty(obj[8])?null:(Double)
	 * obj[8]);
	 * 
	 * listHierarchy.add(hierarchyDto); } } } return listHierarchy; }
	 */

	/*
	 * public static NearestUserDtoResponse getUsers(String muwi) {
	 * LocationHierarchyDao hierarchyDao =
	 * SpringContextBridge.services().getLocationHierarchyDao();
	 * LocationHierarchyDto hierarchyDto =
	 * hierarchyDao.getWellDetailsForMuwi(muwi); NearestUserDtoResponse
	 * userDtoResponse = new NearestUserDtoResponse();
	 * if(!ServicesUtil.isEmpty(hierarchyDto)) { userDtoResponse =
	 * getUsers(hierarchyDto.getLatValue().doubleValue(),
	 * hierarchyDto.getLongValue().doubleValue(), null); } else {
	 * ResponseMessage responseMessage = new ResponseMessage();
	 * responseMessage.setMessage("Well Information Not Available");
	 * responseMessage.setStatus(MurphyConstant.SUCCESS);
	 * responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS); } return
	 * userDtoResponse; }
	 */

	public static ResponseMessage signInUsers() {

		ResponseMessage response = new ResponseMessage();
		response.setMessage("Users not Signed In");
		response.setStatus(MurphyConstant.FAILURE);
		response.setStatusCode(MurphyConstant.CODE_FAILURE);

		GeoTabDao geoTabDao = SpringContextBridge.services().getGeoTabDao();

		TimeZone defaultTZ = TimeZone.getDefault();

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		JSONObject deviceStatusInfo = null;
		JSONObject inDeviceStatusInfoObject = null;
		JSONObject deviceDetailsInfo = null;
		List<DeviceStatusInfoDto> deviceStatusInfos = null;
		String deviceId = "";
		DeviceStatusInfoDto deviceInfo = null;
		Long seperateDateTime = 0L;
		try {
			deviceStatusInfo = GeoTabUtil.callRest(formDeviceStatusInfoEntity());
			deviceDetailsInfo = GeoTabUtil.callRest(formDeviceSearchEntity());

			// logger.debug("deviceStatusInfo : "+deviceStatusInfo);
			// logger.debug("deviceInfo : "+deviceInfo);

		} catch (ParseException e) {
			logger.error("Exception : " + e.getMessage());
		}

		String serialNumber = null;
		String deviceIdentifier = null;

		if (!ServicesUtil.isEmpty(deviceStatusInfo)) {
			deviceStatusInfos = new ArrayList<DeviceStatusInfoDto>();
			JSONArray deviceStatusInfoArray = deviceStatusInfo.getJSONArray("result");
			for (Object inDeviceStatusInfoArrayObject : deviceStatusInfoArray) {
				inDeviceStatusInfoObject = (JSONObject) inDeviceStatusInfoArrayObject;
				if ((!inDeviceStatusInfoObject.getBoolean("isDriving"))) {

					JSONObject dev = inDeviceStatusInfoObject.getJSONObject("device");

					DeviceStatusInfoDto statusInfoDto = new DeviceStatusInfoDto();

					deviceIdentifier = dev.getString("id");

					serialNumber = getSerialNumber(deviceDetailsInfo, deviceIdentifier);
					statusInfoDto.setDeviceId(serialNumber);
					statusInfoDto.setBearing(inDeviceStatusInfoObject.getInt("bearing"));
					statusInfoDto.setCurrentStateDuration(inDeviceStatusInfoObject.getString("currentStateDuration"));
					statusInfoDto.setDateTime(inDeviceStatusInfoObject.getString("dateTime"));
					statusInfoDto.setDeviceCommunicating(inDeviceStatusInfoObject.getBoolean("isDeviceCommunicating"));
					statusInfoDto.setDriving(inDeviceStatusInfoObject.getBoolean("isDriving"));
					statusInfoDto.setLatitude(inDeviceStatusInfoObject.getDouble("latitude"));
					statusInfoDto.setLongitude(inDeviceStatusInfoObject.getDouble("longitude"));
					statusInfoDto.setSpeed(inDeviceStatusInfoObject.getInt("speed"));

					deviceStatusInfos.add(statusInfoDto);
					System.out.println("statusInfoDto : " + statusInfoDto);
				}
			}
		}

		if (!ServicesUtil.isEmpty(deviceStatusInfos)) {

			List<LocationHierarchyDto> allWellDetails = geoTabDao.getAllWellDetails();
			for (LocationHierarchyDto locationHierarchyDto : allWellDetails) {
				for (DeviceStatusInfoDto deviceStatus : deviceStatusInfos) {

					Coordinates well = new Coordinates(locationHierarchyDto.getLatValue(),
							locationHierarchyDto.getLongValue());
					Coordinates device = new Coordinates(deviceStatus.getLatitude(), deviceStatus.getLongitude());

					Double crowFlyDistance = GeoTabUtil.getDistance(well, device);
					if (crowFlyDistance < GeoTabConstants.GEO_TAB_AUTO_SIGNIN_DISTANCE) {
						deviceInfo = new DeviceStatusInfoDto();

						deviceId = deviceStatus.getDeviceId();
						deviceInfo.setDeviceId(deviceId);
						deviceInfo.setBearing(deviceStatus.getBearing());
						deviceInfo.setCurrentStateDuration(deviceStatus.getCurrentStateDuration());
						deviceInfo.setDateTime(deviceStatus.getDateTime());
						deviceInfo.setDeviceCommunicating(deviceStatus.isDeviceCommunicating());
						deviceInfo.setDriving(deviceStatus.isDriving());
						deviceInfo.setLatitude(deviceStatus.getLatitude());
						deviceInfo.setLongitude(deviceStatus.getLongitude());
						deviceInfo.setSpeed(deviceStatus.getSpeed());
						deviceInfo.setCrowFlyDistance(crowFlyDistance);
						// deviceInfo.setFacility(locationHierarchyDto.getFacility());
						// deviceInfo.setField(locationHierarchyDto.getField());
						// deviceInfo.setWellPad(locationHierarchyDto.getWellpad());
						deviceInfo.setWellName(locationHierarchyDto.getWell());
						deviceInfo.setMuwi(locationHierarchyDto.getMuwi());
						deviceInfo.setRoadDistance(ArcGISUtil.getRoadDistance(well, device).getTotalLength());
						deviceInfo.setWellLat(well.getLatitude());
						deviceInfo.setWellLon(well.getLongitude());

						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
						dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

						try {
							seperateDateTime = GeoTabUtil.seperateDateTime(deviceInfo.getCurrentStateDuration());
							Long parkedTime = (dateFormat.parse(deviceInfo.getDateTime())).getTime() - seperateDateTime;
							Date parkedDate = new Date();
							parkedDate.setTime(parkedTime);

							deviceInfo.setParkedDateTime(dateFormat.format(parkedDate));

						} catch (java.text.ParseException e) {
							logger.error("[GeoTabUtil][getParkedtime] : Parse Exception : " + e.getMessage());
						}

						// System.err.println("INFO :
						// [GeoTabUtil][signInUsers][crowFlyDistance][userSignedIn][user]
						// : "+deviceStatus);

						geoTabDao.saveOrUpdateAutoSignIn(deviceInfo);
						response.setMessage("Users Signed In Successfully");
						response.setStatus(MurphyConstant.SUCCESS);
						response.setStatusCode(MurphyConstant.CODE_SUCCESS);
					}
				}
			}
		}
		TimeZone.setDefault(defaultTZ);
		return response;
	}

	public static ResponseMessage signOutUsers() {

		ResponseMessage response = new ResponseMessage();
		response.setMessage("Users not Signed In");
		response.setStatus(MurphyConstant.FAILURE);
		response.setStatusCode(MurphyConstant.CODE_FAILURE);

		GeoTabDao geoTabDao = SpringContextBridge.services().getGeoTabDao();

		TimeZone defaultTZ = TimeZone.getDefault();

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		JSONObject deviceStatusInfo = null;
		JSONObject deviceInfo = null;
		JSONObject inDeviceStatusInfoObject = null;
		List<DeviceStatusInfoDto> deviceStatusInfos = null;
		Long seperateDateTime = 0L;
		try {
			deviceStatusInfo = GeoTabUtil.callRest(formDeviceStatusInfoEntity());
			deviceInfo = GeoTabUtil.callRest(formDeviceSearchEntity());

			// logger.debug("deviceStatusInfo : "+deviceStatusInfo);
			// logger.debug("deviceInfo : "+deviceInfo);

		} catch (ParseException e) {
			logger.error("Exception : " + e.getMessage());
		}

		String serialNumber = null;
		String deviceIdentifier = null;

		if (!ServicesUtil.isEmpty(deviceStatusInfo)) {
			deviceStatusInfos = new ArrayList<DeviceStatusInfoDto>();
			JSONArray deviceStatusInfoArray = deviceStatusInfo.getJSONArray("result");
			for (Object inDeviceStatusInfoArrayObject : deviceStatusInfoArray) {
				inDeviceStatusInfoObject = (JSONObject) inDeviceStatusInfoArrayObject;
				if ((inDeviceStatusInfoObject.getBoolean("isDriving"))) {

					JSONObject dev = inDeviceStatusInfoObject.getJSONObject("device");

					deviceIdentifier = dev.getString("id");
					serialNumber = getSerialNumber(deviceInfo, deviceIdentifier);

					DeviceStatusInfoDto statusInfoDto = new DeviceStatusInfoDto();
					statusInfoDto.setDeviceId(serialNumber);
					statusInfoDto.setBearing(inDeviceStatusInfoObject.getInt("bearing"));
					statusInfoDto.setCurrentStateDuration(inDeviceStatusInfoObject.getString("currentStateDuration"));
					statusInfoDto.setDateTime(inDeviceStatusInfoObject.getString("dateTime"));
					statusInfoDto.setDeviceCommunicating(inDeviceStatusInfoObject.getBoolean("isDeviceCommunicating"));
					statusInfoDto.setDriving(inDeviceStatusInfoObject.getBoolean("isDriving"));
					statusInfoDto.setLatitude(inDeviceStatusInfoObject.getDouble("latitude"));
					statusInfoDto.setLongitude(inDeviceStatusInfoObject.getDouble("longitude"));
					statusInfoDto.setSpeed(inDeviceStatusInfoObject.getInt("speed"));

					deviceStatusInfos.add(statusInfoDto);
					System.out.println("statusInfoDto : " + statusInfoDto);
				}
			}
		}

		List<String> signedInUsers = geoTabDao.getUsersSignedIn();

		if (!ServicesUtil.isEmpty(deviceStatusInfos) && !ServicesUtil.isEmpty(signedInUsers)) {
			for (DeviceStatusInfoDto deviceDto : deviceStatusInfos) {
				if (signedInUsers.contains(deviceDto.getDeviceId())) {
					try {
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
						dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
						seperateDateTime = GeoTabUtil.seperateDateTime(deviceDto.getCurrentStateDuration());
						Long parkedTime = (dateFormat.parse(deviceDto.getDateTime())).getTime() - seperateDateTime;
						Date parkedDate = new Date();
						parkedDate.setTime(parkedTime);

						deviceDto.setParkEndTime(parkedDate);

						geoTabDao.updateParkEndTime(deviceDto);

						response.setMessage("Users Signed Out Successfully");
						response.setStatus(MurphyConstant.SUCCESS);
						response.setStatusCode(MurphyConstant.CODE_SUCCESS);

					} catch (java.text.ParseException e) {
						logger.error("[GeoTabUtil][getParkedtime] : Parse Exception : " + e.getMessage());
					}
				} else {
					response.setMessage("No Users to Signout");
				}
			}
		}

		TimeZone.setDefault(defaultTZ);
		return response;
	}

	private static String getSerialNumber(JSONObject deviceDetailsInfo, String deviceIdentifier) {
		JSONObject deviceDetailsInfoObject = null;
		if (!ServicesUtil.isEmpty(deviceDetailsInfo)) {
			JSONArray deviceInfoArray = deviceDetailsInfo.getJSONArray("result");
			for (Object inDeviceInfoObject : deviceInfoArray) {
				deviceDetailsInfoObject = (JSONObject) inDeviceInfoObject;
				if (deviceDetailsInfoObject.toString().contains("serialNumber")) {
					if (deviceIdentifier.equals(deviceDetailsInfoObject.getString("id"))) {
						return deviceDetailsInfoObject.getString("serialNumber");
					}
				}
			}
		}
		return deviceIdentifier;
	}

	public static ResponseMessage deleteRecords() {
		AutoSignInDao autoSignInDao = SpringContextBridge.services().getAutoSignInDao();
		return autoSignInDao.deleteRecords();
	}

	/**
	 * @param latitude
	 * @param longitude
	 * @param userType
	 * @param groupId
	 * @return Nearest Users to the given latitude and longitude
	 */
	@SuppressWarnings("static-access")
	public static NearestUserDtoResponse getUsers(Double latitude, Double longitude, String locationCode,
			String groupId, String userType) {
		ResponseMessage message = new ResponseMessage();
		message.setMessage("Users Fetch success");
		message.setStatus(MurphyConstant.SUCCESS);
		message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		NearestUserDtoResponse nearestUserDtoResponse = null;
		Coordinates cordinateDto = null;
		// NearestUserDtoResponseBl NearestUserDtoResponseBl = null;
		try {
			GeoTabDao geoTabDao = SpringContextBridge.services().getGeoTabDao();
			BlackLineUtil blackLineUtil = new BlackLineUtil();
			if (!ServicesUtil.isEmpty(locationCode) && ServicesUtil.isEmpty(latitude)
					&& ServicesUtil.isEmpty(longitude)) {
				cordinateDto = geoTabDao.getLatLongByLocationCode(locationCode);
				if (!ServicesUtil.isEmpty(cordinateDto) && !ServicesUtil.isEmpty(cordinateDto.getLatitude())
						&& !ServicesUtil.isEmpty(cordinateDto.getLongitude())) {
					String apiCallCheck = geoTabOrBlackLineCheck(locationCode);
					logger.error("apiCallCheck = " + apiCallCheck);
					if (apiCallCheck.equalsIgnoreCase("Geo_Tab")) {
						if (!ServicesUtil.isEmpty(groupId) && !ServicesUtil.isEmpty(userType))
							nearestUserDtoResponse = getUsersByLocation(cordinateDto.getLatitude(),
									cordinateDto.getLongitude(), groupId, userType);
						else
							nearestUserDtoResponse = getUsersByLocation(cordinateDto.getLatitude(),
									cordinateDto.getLongitude(), null, null);
						return nearestUserDtoResponse;
					} else if (apiCallCheck.equalsIgnoreCase("Black_Line")) {
						if (!ServicesUtil.isEmpty(groupId) && !ServicesUtil.isEmpty(userType)){
//							nearestUserDtoResponse = blackLineUtil.getUsersByLocation(cordinateDto.getLatitude(),
//									cordinateDto.getLongitude(), groupId, userType);
							
							//added to handle blackline for locationCode
//							nearestUserDtoResponse = blackLineUtil.getUserByLocation(cordinateDto.getLatitude(),
//									cordinateDto.getLongitude(), groupId, userType,locationCode);
							//TODO
							nearestUserDtoResponse = blackLineUtil.getUserByLocationAndCountry(cordinateDto.getLatitude(), cordinateDto.getLongitude(), groupId,
									userType, locationCode);
						
						}
						else {
							//nearestUserDtoResponse = blackLineUtil.getUsersByLocationAndCountry(cordinateDto.getLatitude(),
							//		cordinateDto.getLongitude(), null, null, ServicesUtil.getCountryCodeByLocation(locationCode));
							//added to handle blackline for locationCode
							nearestUserDtoResponse = blackLineUtil.getUserByLocationAndCountry(cordinateDto.getLatitude(), cordinateDto.getLongitude(), null,
									null, locationCode);
						}
						return nearestUserDtoResponse;

					}
				} else {
					//Incase LocCode does not have any lattitude and longitude coordinates
					String apiCallCheck = geoTabOrBlackLineCheck(locationCode);
					logger.error("apiCallCheck = " + apiCallCheck);
					if (apiCallCheck.equalsIgnoreCase("Geo_Tab")) {
						if (!ServicesUtil.isEmpty(groupId) && !ServicesUtil.isEmpty(userType))
							nearestUserDtoResponse = getUsersByLocation(null,null, groupId, userType);
						else
							nearestUserDtoResponse = getUsersByLocation(null,null, null, null);
						return nearestUserDtoResponse;
					} else if (apiCallCheck.equalsIgnoreCase("Black_Line")) {
						if (!ServicesUtil.isEmpty(groupId) && !ServicesUtil.isEmpty(userType)){
							//nearestUserDtoResponse = blackLineUtil.getUsersByLocation(null,null, groupId, userType);
							//added to handle blackline for locationCode
							nearestUserDtoResponse = blackLineUtil.getUserByLocationAndCountry(null,
									null, groupId, userType,locationCode);
						}
						else{
							//nearestUserDtoResponse = blackLineUtil.getUsersByLocationAndCountry(null,null, null, null, ServicesUtil.getCountryCodeByLocation(locationCode));
							//added to handle blackline for locationCode
							nearestUserDtoResponse = blackLineUtil.getUserByLocationAndCountry(null, null, null,
									null, locationCode);
						}
						return nearestUserDtoResponse;

					}
					
				}
			} else {
				if (!ServicesUtil.isEmpty(latitude) && !ServicesUtil.isEmpty(longitude)) {
					String locCode = hierarchyDao.getLocationCodeByCoord(latitude, longitude);
					if (!ServicesUtil.isEmpty(locCode)) {
						String apiCallCheck = geoTabOrBlackLineCheck(locationCode);
						logger.error("apiCallCheck = " + apiCallCheck);
						if (apiCallCheck.equalsIgnoreCase("Geo_Tab")) {
							if (!ServicesUtil.isEmpty(groupId) && !ServicesUtil.isEmpty(userType))
								nearestUserDtoResponse = getUsersByLocation(latitude, longitude, groupId, userType);
							else
								nearestUserDtoResponse = getUsersByLocation(latitude, longitude, null, null);
							return nearestUserDtoResponse;
						} else if (apiCallCheck.equalsIgnoreCase("Black_Line")) {
							if (!ServicesUtil.isEmpty(groupId) && !ServicesUtil.isEmpty(userType)){
//								nearestUserDtoResponse = blackLineUtil.getUsersByLocation(latitude, longitude, groupId,
//										userType);
								
								//added to handle blackline for locationCode
								nearestUserDtoResponse = blackLineUtil.getUserByLocationAndCountry(latitude,
										longitude, groupId, userType,locationCode);
							}
							else{
//								nearestUserDtoResponse = blackLineUtil.getUsersByLocationAndCountry(latitude, longitude, null,
//										null, ServicesUtil.getCountryCodeByLocation(locationCode));
								
								
								//added to handle blackline for locationCode
								nearestUserDtoResponse = blackLineUtil.getUserByLocationAndCountry(latitude, longitude, null,
										null, locationCode);
							}
							return nearestUserDtoResponse;

						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("GeoTabUtil.getUsers() Exception "+ex.getMessage());
			ex.printStackTrace();
			message.setMessage("Getting Default IDP Users as Exception : " + ex.getMessage());
			message.setStatus(MurphyConstant.FAILURE);
			message.setStatusCode(MurphyConstant.CODE_FAILURE);
		}
		return nearestUserDtoResponse;
	}

	private static String geoTabOrBlackLineCheck(String locationCode) {
		String locationType = null;
		String fieldLocCode = null;
		FieldResponseDto fieldResponseDto = null;
		try {
			HierarchyDao hierarchyDao = SpringContextBridge.services().gethierarchyDao();
			GeoTabDao geoTabDao = SpringContextBridge.services().getGeoTabDao();
			locationType = hierarchyDao.getLocationtypeByLocCode(locationCode);
			logger.error("locationType is " + locationType);
			if(!locationType.equalsIgnoreCase(MurphyConstant.FIELD)){
			fieldResponseDto = hierarchyDao.getFeild(locationCode, locationType, true);
			logger.error("field is " + fieldResponseDto.getField());
			fieldLocCode = hierarchyDao.getLocationCodeByLocText(fieldResponseDto.getField());
			logger.error("field Location_Code is " + fieldLocCode);
			return geoTabDao.findApiDevice(fieldLocCode);
			}

			return geoTabDao.findApiDevice(locationCode);

		} catch (Exception e) {
			logger.error("[MURPHY][GeoTabutil][geoTabOrBlackLineCheck][Exception]" + e.getMessage());
		}

		return null;
	}

	public static NearestUserDtoResponse getUsersByLocation(Double latitude, Double longitude, String groupId,
			String userType) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(GeoTabConstants.FAILURE);
		responseMessage.setStatusCode(GeoTabConstants.CODE_FAILURE);

		// GeoTabDao geoTabDao = SpringContextBridge.services().getGeoTabDao();
		UserIDPMappingDao mappingDao = SpringContextBridge.services().getUserIDPMappingDao();

		List<NearestUserDto> nearestUserDtos = new ArrayList<NearestUserDto>();
		List<NearestUserDto> otherUsers = null;
		Map<String, NearestUserDto> userDtoMap = null;
		Map<String, Device> deviceMap = null;
		Map<String,NearestUserDto>userEmailtoDetailsMap=new HashMap<>();

		JSONObject inDeviceStatusInfoObject = null;
		Coordinates center = null;
		NearestUserDto nearestUserDto = null;
		JSONObject deviceStatusInfo = null;
		JSONObject deviceInfo = null;
		List<UserIDPMappingDto> baseUsers = null;
		NearestUserDtoResponse nearestUserDtoResponse = new NearestUserDtoResponse();
		Device device = null;
		Device deviceStatus = null;
		Coordinates deviceLoc = null;
		Double distance = null;

		List<String> roles = null;
		// Changes made for ALS Inquiry and Investigation
		if (!ServicesUtil.isEmpty(groupId) && !ServicesUtil.isEmpty(userType)
				&& userType.equalsIgnoreCase(MurphyConstant.USER_TYPE_ALS)) {
			roles = mappingDao.getRolesForNDVTasks(groupId, userType);
			baseUsers = mappingDao.getUserAndTaskCountDetails(roles,longitude,latitude);
		} else {
			roles = new ArrayList<>();
			roles.add(MurphyConstant.PRO_CATARINA);
			roles.add(MurphyConstant.PRO_KARNES);
			roles.add(MurphyConstant.PRO_TILDEN);
			roles.add(MurphyConstant.ALS_EAST);
			roles.add(MurphyConstant.ALS_WEST);
			roles.add(MurphyConstant.OBX_CATARINA);
			roles.add(MurphyConstant.OBX_KARNES);
			roles.add(MurphyConstant.OBX_TILDEN);

			baseUsers = mappingDao.getUserAndTaskCountDetails(roles, longitude, latitude);
		}

		userDtoMap = new HashMap<String, NearestUserDto>();
		otherUsers = new ArrayList<>();
		if (!ServicesUtil.isEmpty(baseUsers)) {
			for (UserIDPMappingDto mappingDto : baseUsers) {
				nearestUserDto = new NearestUserDto();
				nearestUserDto.setSerialId(mappingDto.getSerialId());
				nearestUserDto.setFirstName(mappingDto.getUserFirstName());
				nearestUserDto.setLastName(mappingDto.getUserLastName());
				nearestUserDto.setEmailId(mappingDto.getUserEmail());
				nearestUserDto.setTaskCount(mappingDto.getTaskCount());
				nearestUserDto.setTaskAssignable(Boolean.valueOf(mappingDto.getTaskAssignable()));
				nearestUserDto.setpId(mappingDto.getpId());
				nearestUserDto.setUserId(mappingDto.getUserEmail());
				if (!ServicesUtil.isEmpty(nearestUserDto.getSerialId())) {
					userDtoMap.put(nearestUserDto.getSerialId(), nearestUserDto);
					userEmailtoDetailsMap.put(nearestUserDto.getEmailId(),nearestUserDto);
				} else {
					otherUsers.add(nearestUserDto);
					if(nearestUserDto.isTaskAssignable()){
						userEmailtoDetailsMap.put(nearestUserDto.getEmailId(),nearestUserDto);
					}
				}
			}
		}

		if (!ServicesUtil.isEmpty(latitude) && !ServicesUtil.isEmpty(longitude) && !ServicesUtil.isEmpty(userDtoMap)) {
			center = new Coordinates(latitude, longitude);
			try {
				deviceStatusInfo = GeoTabUtil.callRest(formDeviceStatusInfoEntity());
				deviceInfo = GeoTabUtil.callRest(formDeviceSearchEntity());

			} catch (ParseException e) {
				System.err.println("Exception : " + e.getMessage());
			}

			if (!ServicesUtil.isEmpty(deviceInfo)) {
				deviceMap = new HashMap<>();
				JSONObject inDeviceInfoObject = null;
				JSONArray deviceInfoArray = deviceInfo.getJSONArray("result");
				for (Object inDeviceInfoArrayObject : deviceInfoArray) {
					inDeviceInfoObject = (JSONObject) inDeviceInfoArrayObject;
					device = new Device();
					device.setId(inDeviceInfoObject.getString("id"));
					device.setSerialNumber(inDeviceInfoObject.getString("serialNumber"));
					deviceMap.put(device.getId(), device);
				}
			}

			if (!ServicesUtil.isEmpty(deviceStatusInfo)) {
				JSONArray deviceStatusInfoArray = deviceStatusInfo.getJSONArray("result");
				for (Object inDeviceStatusInfoArrayObject : deviceStatusInfoArray) {
					inDeviceStatusInfoObject = (JSONObject) inDeviceStatusInfoArrayObject;
					JSONObject dev = inDeviceStatusInfoObject.getJSONObject("device");
					deviceStatus = new Device();
					deviceStatus.setId(dev.getString("id"));
					deviceStatus.setLatitude(inDeviceStatusInfoObject.getDouble("latitude"));
					deviceStatus.setLongitude(inDeviceStatusInfoObject.getDouble("longitude"));
					deviceStatus.setIsDriving(inDeviceStatusInfoObject.getBoolean("isDriving"));
					deviceStatus.setSerialNumber(deviceMap.get(deviceStatus.getId()).getSerialNumber());

					deviceLoc = new Coordinates(deviceStatus.getLatitude(), deviceStatus.getLongitude());

					NearestUserDto parUserDto = userDtoMap.get(deviceStatus.getSerialNumber());
					if (!ServicesUtil.isEmpty(parUserDto)) {
						distance = getDistance(deviceLoc, center);
						// distance = ArcGISUtil.getRoadDistance(deviceLoc,
						// center).getTotalLength();
						parUserDto.setUnRoundedDistance(ServicesUtil.isEmpty(distance) ? null : distance);
						parUserDto.setDistanceFromLocation(ServicesUtil.isEmpty(distance) ? null
								: Math.round(parUserDto.getUnRoundedDistance() * 10D) / 10D);
						parUserDto.setIsDriving(deviceStatus.getIsDriving());
						/*
						 * Commented for sending emailId in place of UserId -
						 * 24/01/2018 - INC00718
						 */
						// parUserDto.setUserId(deviceStatus.getId());
						parUserDto.setUserId(parUserDto.getEmailId());
						nearestUserDtos.add(parUserDto);
						// userDtoMap.remove(parUserDto.getSerialId());
						userEmailtoDetailsMap.remove(parUserDto.getEmailId());
					}
				}
				responseMessage.setStatus(GeoTabConstants.SUCCESS);
				responseMessage.setStatusCode(GeoTabConstants.CODE_SUCCESS);
				responseMessage.setMessage("Users Fetched Successfully");
			}
			
			
			

		}
		
		if (!ServicesUtil.isEmpty(latitude) && !ServicesUtil.isEmpty(longitude)
				&& !ServicesUtil.isEmpty(userEmailtoDetailsMap) && userEmailtoDetailsMap.size() > 0) {
			center = new Coordinates(latitude, longitude);
			userEmailtoDetailsMap = getNearestUsersByBaseLocationDistance(userEmailtoDetailsMap, center);
			if (!ServicesUtil.isEmpty(userEmailtoDetailsMap)) {
				nearestUserDtos.addAll(new ArrayList<>(userEmailtoDetailsMap.values()));
			}

		}

		if (!ServicesUtil.isEmpty(nearestUserDtos)) {
			Collections.sort(nearestUserDtos, new DistanceComparator());
		}
		
		
		
		if (ServicesUtil.isEmpty(nearestUserDtos)) {
			for (Map.Entry<String, NearestUserDto> entry : userDtoMap.entrySet()) {
				nearestUserDtos.add(entry.getValue());
				userEmailtoDetailsMap.remove(entry.getValue().getEmailId());
			}

			if (!ServicesUtil.isEmpty(userEmailtoDetailsMap) && userEmailtoDetailsMap.size() > 0) {

				for (Map.Entry<String, NearestUserDto> entry : userEmailtoDetailsMap.entrySet()) {
					nearestUserDtos.add(entry.getValue());
				}
			}

		}
		if (!ServicesUtil.isEmpty(otherUsers) && otherUsers.size() > 0) {
			for (NearestUserDto userDto : otherUsers) {
				if (userDto.isTaskAssignable() && !userEmailtoDetailsMap.containsKey(userDto.getEmailId())) {
					/*
					 * Added for sending emailId in place of UserId - 24/01/2018
					 * - INC00718
					 */
					userDto.setUserId(userDto.getEmailId());
					nearestUserDtos.add(userDto);
				}
			}
			responseMessage.setMessage("Users fetched successfully");
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}

		nearestUserDtoResponse.setNearestUsers(nearestUserDtos);
		nearestUserDtoResponse.setResponseMessage(responseMessage);

		return nearestUserDtoResponse;
	}

	private static Map<String, NearestUserDto> getNearestUsersByBaseLocationDistance(Map<String, NearestUserDto> usersDetailsMap,
			Coordinates center) {

		logger.error("GeoTabUtil.getNearestUsersByBaseLocationDistance() BEGIN");
		Map<String, NearestUserDto> responseUserEmailtoDetailsMap = new HashMap<>();
		EmpDetailsDao empDetailsDao = SpringContextBridge.services().getEmpDetailsDao();
		Map<String, com.murphy.blackline.Coordinates> userBaseLocationCoordinatesMap = empDetailsDao
				.getUsersBaseLocationCoordinates(new ArrayList<>(usersDetailsMap.keySet()));

		if (!ServicesUtil.isEmpty(userBaseLocationCoordinatesMap)) {

			for (String userEmailId : userBaseLocationCoordinatesMap.keySet()) {
				NearestUserDto parUserDto = usersDetailsMap.get(userEmailId);
				if (!ServicesUtil.isEmpty(parUserDto)) {
					com.murphy.blackline.Coordinates coordinates = userBaseLocationCoordinatesMap.get(userEmailId);
					Coordinates deviceLoc = new Coordinates(coordinates.getLatitude(), coordinates.getLongitude());
					Double distance = getDistance(deviceLoc, center);
					parUserDto.setUnRoundedDistance(ServicesUtil.isEmpty(distance) ? null : distance);
					parUserDto.setDistanceFromLocation(ServicesUtil.isEmpty(distance) ? null
							: Math.round(parUserDto.getUnRoundedDistance() * 10D) / 10D);
					parUserDto.setUserId(parUserDto.getEmailId());
					responseUserEmailtoDetailsMap.put(userEmailId, parUserDto);
				}

			}
		}

		return responseUserEmailtoDetailsMap;
	}

	static class DistanceComparator implements Comparator<NearestUserDto> {

		@Override
		public int compare(NearestUserDto o1, NearestUserDto o2) {
			return Double.compare(o1.getUnRoundedDistance(), o2.getUnRoundedDistance());
		}
	}

	private static String formDeviceSearchEntity() throws ParseException {

		ConfigDao configDao = SpringContextBridge.services().getConfigDao();

		Map<String, String> cred = new HashMap<String, String>();
		cred.put("database", configDao.getConfigurationByRef(MurphyConstant.GEOTAB_DATABASE_REF));
		cred.put("userName", configDao.getConfigurationByRef(MurphyConstant.GEOTAB_USERNAME_REF));
		// cred.put("sessionId",
		// ServicesUtil.getProperty(MurphyConstant.GEOTAB_SESSION_ID_PROPERTY_NAME,
		// MurphyConstant.IOP_PROPERTY_FILE_LOCATION).toString());
		cred.put("sessionId", configDao.getConfigurationByRef(MurphyConstant.GEOTAB_SESSION_ID_REF));

		Map<String, Object> crede = new HashMap<String, Object>();
		crede.put("credentials", cred);
		crede.put("typeName", GeoTabConstants.TYPE_NAME_DEVICE_SEARCH);

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("params", crede);
		parameters.put("method", GeoTabConstants.GEO_TAB_METHOD_GET);

		String jsonString = JsonUtil.addObjects(null, parameters);

		return jsonString;
	}

	private static String formDeviceSearchEntity(String serialId) throws ParseException {

		ConfigDao configDao = SpringContextBridge.services().getConfigDao();

		Map<String, String> cred = new HashMap<String, String>();
		cred.put("database", configDao.getConfigurationByRef(MurphyConstant.GEOTAB_DATABASE_REF));
		cred.put("userName", configDao.getConfigurationByRef(MurphyConstant.GEOTAB_USERNAME_REF));
		// cred.put("sessionId",
		// ServicesUtil.getProperty(MurphyConstant.GEOTAB_SESSION_ID_PROPERTY_NAME,
		// MurphyConstant.IOP_PROPERTY_FILE_LOCATION).toString());
		cred.put("sessionId", configDao.getConfigurationByRef(MurphyConstant.GEOTAB_SESSION_ID_REF));

		Map<String, String> search = new HashMap<>();
		search.put("serialNumber", serialId);

		Map<String, Object> crede = new HashMap<String, Object>();
		crede.put("credentials", cred);
		crede.put("typeName", GeoTabConstants.TYPE_NAME_DEVICE_SEARCH);
		crede.put("search", search);

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("params", crede);
		parameters.put("method", GeoTabConstants.GEO_TAB_METHOD_GET);

		String jsonString = JsonUtil.addObjects(null, parameters);

		return jsonString;
	}

	private static String formDeviceStatusInfoEntity() throws ParseException {

		ConfigDao configDao = SpringContextBridge.services().getConfigDao();

		Map<String, String> cred = new HashMap<String, String>();
		cred.put("database", configDao.getConfigurationByRef(MurphyConstant.GEOTAB_DATABASE_REF));
		cred.put("userName", configDao.getConfigurationByRef(MurphyConstant.GEOTAB_USERNAME_REF));
		// cred.put("sessionId",
		// ServicesUtil.getProperty(MurphyConstant.GEOTAB_SESSION_ID_PROPERTY_NAME,
		// MurphyConstant.IOP_PROPERTY_FILE_LOCATION).toString());
		cred.put("sessionId", configDao.getConfigurationByRef(MurphyConstant.GEOTAB_SESSION_ID_REF));

		Map<String, Object> crede = new HashMap<String, Object>();
		crede.put("credentials", cred);
		crede.put("typeName", GeoTabConstants.TYPE_NAME_DEVICESTATUSINFO);

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("params", crede);
		parameters.put("method", GeoTabConstants.GEO_TAB_METHOD_GET);

		String jsonString = JsonUtil.addObjects(null, parameters);

		return jsonString;
	}

	private static String formDeviceStatusInfoEntity(String deviceId) throws ParseException {

		ConfigDao configDao = SpringContextBridge.services().getConfigDao();

		Map<String, String> cred = new HashMap<String, String>();
		cred.put("database", configDao.getConfigurationByRef(MurphyConstant.GEOTAB_DATABASE_REF));
		cred.put("userName", configDao.getConfigurationByRef(MurphyConstant.GEOTAB_USERNAME_REF));
		// cred.put("sessionId",
		// ServicesUtil.getProperty(MurphyConstant.GEOTAB_SESSION_ID_PROPERTY_NAME,
		// MurphyConstant.IOP_PROPERTY_FILE_LOCATION).toString());
		cred.put("sessionId", configDao.getConfigurationByRef(MurphyConstant.GEOTAB_SESSION_ID_REF));

		Map<String, String> deviceSearch = new HashMap<>();
		deviceSearch.put("id", deviceId);

		Map<String, Object> search = new HashMap<>();
		search.put("deviceSearch", deviceSearch);

		Map<String, Object> crede = new HashMap<String, Object>();
		crede.put("credentials", cred);
		crede.put("typeName", GeoTabConstants.TYPE_NAME_DEVICESTATUSINFO);
		crede.put("search", search);

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("params", crede);
		parameters.put("method", GeoTabConstants.GEO_TAB_METHOD_GET);

		String jsonString = JsonUtil.addObjects(null, parameters);

		return jsonString;
	}

	@SuppressWarnings("unused")
	private static UserNameDto trimName(String fullName) {
		UserNameDto userNameDto = new UserNameDto();
		try {
			if ((Character.isDigit(fullName.charAt(0))) && (fullName.contains(",") && (fullName.contains("-")))) {
				String trim = "-";
				String subStr = fullName.substring(fullName.indexOf(trim) + trim.length());
				String[] strArr = new String[2];
				strArr = subStr.split(",");
				userNameDto.setTruckId(fullName.substring(0, fullName.indexOf(trim.substring(0))).trim());
				userNameDto.setLastName(strArr[0].trim());
				userNameDto.setFirstName(strArr[1].trim());
				userNameDto.setFullName(fullName.trim());
			} else if ((Character.isDigit(fullName.charAt(0)) && fullName.contains("-"))) {
				String trim = "- ";
				userNameDto.setTruckId(fullName.substring(0, fullName.indexOf(trim.substring(0))).trim());
				String[] strArr = new String[2];
				strArr = fullName.split("-");
				userNameDto.setFirstName(strArr[1].trim());
				userNameDto.setFullName(fullName);
			} else {
				userNameDto.setFirstName(fullName.trim());
			}
		} catch (Exception e) {
			System.err.println("Unknown GeoTab name Format : " + fullName);
			userNameDto.setFirstName(fullName);
		}
		return userNameDto;
	}

	/*
	 * public static BigDecimal getBigDecimal( Object value ) { BigDecimal ret =
	 * null; if( value != null ) { if( value instanceof BigDecimal ) { ret =
	 * (BigDecimal) value; } else if( value instanceof String ) { ret = new
	 * BigDecimal( (String) value ); } else if( value instanceof BigInteger ) {
	 * ret = new BigDecimal( (BigInteger) value ); } else if( value instanceof
	 * Number ) { ret = new BigDecimal( ((Number)value).doubleValue() ); } else
	 * { throw new
	 * ClassCastException("Not possible to coerce ["+value+"] from class "+value
	 * .getClass()+" into a BigDecimal."); } } return ret; }
	 */

	public static Long seperateDateTime(String dateTimeString) {
		String date = "";
		String milliSeconds = "";
		String[] timeString;
		String hours = "";
		String minutes = "";
		String seconds = "";

		Long ret = 0L;

		if (dateTimeString.contains(".")) {
			String[] array = dateTimeString.split("\\.");

			if (array.length == 3) {
				date = array[0];
				milliSeconds = array[2];
				timeString = array[1].split("\\:");
				hours = timeString[0];
				minutes = timeString[1];
				seconds = timeString[2];

				milliSeconds = new BigDecimal("." + milliSeconds).stripTrailingZeros().toPlainString().replaceAll("0.",
						"");

				System.out.println("milliSeconds : " + milliSeconds);

				ret = GeoTabUtil.convertToMilliSeconds(Long.valueOf(date), GeoTabConstants.GEO_TAB_TIME_DAY)
						+ GeoTabUtil.convertToMilliSeconds(Long.valueOf(hours), GeoTabConstants.GEO_TAB_TIME_HOUR)
						+ GeoTabUtil.convertToMilliSeconds(Long.valueOf(minutes), GeoTabConstants.GEO_TAB_TIME_MINUTE)
						+ GeoTabUtil.convertToMilliSeconds(Long.valueOf(seconds), GeoTabConstants.GEO_TAB_TIME_SECOND)
						+ GeoTabUtil.convertToMilliSeconds(Long.valueOf(milliSeconds),
								GeoTabConstants.GEO_TAB_TIME_MILLISECOND);

			} else if ((array.length == 2) && dateTimeString.length() > 14) {
				date = "00";
				milliSeconds = array[1];
				timeString = array[0].split("\\:");
				hours = timeString[0];
				minutes = timeString[1];
				seconds = timeString[2];

				milliSeconds = new BigDecimal("." + milliSeconds).stripTrailingZeros().toPlainString().replaceAll("0.",
						"");

				System.out.println("milliSeconds : " + milliSeconds);

				ret = GeoTabUtil.convertToMilliSeconds(Long.valueOf(date), GeoTabConstants.GEO_TAB_TIME_DAY)
						+ GeoTabUtil.convertToMilliSeconds(Long.valueOf(hours), GeoTabConstants.GEO_TAB_TIME_HOUR)
						+ GeoTabUtil.convertToMilliSeconds(Long.valueOf(minutes), GeoTabConstants.GEO_TAB_TIME_MINUTE)
						+ GeoTabUtil.convertToMilliSeconds(Long.valueOf(seconds), GeoTabConstants.GEO_TAB_TIME_SECOND)
						+ GeoTabUtil.convertToMilliSeconds(Long.valueOf(milliSeconds),
								GeoTabConstants.GEO_TAB_TIME_MILLISECOND);

			} else if ((array.length == 2) && dateTimeString.length() < 14) {
				date = array[0];
				milliSeconds = "00";
				timeString = array[1].split("\\:");
				hours = timeString[0];
				minutes = timeString[1];
				seconds = timeString[2];

				milliSeconds = new BigDecimal("." + milliSeconds).stripTrailingZeros().toPlainString().replaceAll("0.",
						"");

				System.out.println("milliSeconds : " + milliSeconds);

				ret = GeoTabUtil.convertToMilliSeconds(Long.valueOf(date), GeoTabConstants.GEO_TAB_TIME_DAY)
						+ GeoTabUtil.convertToMilliSeconds(Long.valueOf(hours), GeoTabConstants.GEO_TAB_TIME_HOUR)
						+ GeoTabUtil.convertToMilliSeconds(Long.valueOf(minutes), GeoTabConstants.GEO_TAB_TIME_MINUTE)
						+ GeoTabUtil.convertToMilliSeconds(Long.valueOf(seconds), GeoTabConstants.GEO_TAB_TIME_SECOND)
						+ GeoTabUtil.convertToMilliSeconds(Long.valueOf(milliSeconds),
								GeoTabConstants.GEO_TAB_TIME_MILLISECOND);

			} else {
				logger.error("[seperateDateTime][dateTimeString] : " + dateTimeString);
			}
		} else {
			timeString = dateTimeString.split("\\:");
			hours = timeString[0];
			minutes = timeString[1];
			seconds = timeString[2];

			ret = GeoTabUtil.convertToMilliSeconds(Long.valueOf(hours), GeoTabConstants.GEO_TAB_TIME_HOUR)
					+ GeoTabUtil.convertToMilliSeconds(Long.valueOf(minutes), GeoTabConstants.GEO_TAB_TIME_MINUTE)
					+ GeoTabUtil.convertToMilliSeconds(Long.valueOf(seconds), GeoTabConstants.GEO_TAB_TIME_SECOND);
		}

		return ret;
	}

	private static Long convertToMilliSeconds(Long value, String type) {
		System.out.println("value : " + value + "type : " + type);
		switch (type) {
		case (GeoTabConstants.GEO_TAB_TIME_HOUR):
			return TimeUnit.HOURS.toMillis(value);
		case (GeoTabConstants.GEO_TAB_TIME_MINUTE):
			return TimeUnit.MINUTES.toMillis(value);
		case (GeoTabConstants.GEO_TAB_TIME_SECOND):
			return TimeUnit.SECONDS.toMillis(value);
		case (GeoTabConstants.GEO_TAB_TIME_DAY):
			return TimeUnit.DAYS.toMillis(value);
		case (GeoTabConstants.GEO_TAB_TIME_MILLISECOND):
			return TimeUnit.MILLISECONDS.toMillis(value);
		default:
			return 0L;
		}
	}

	public static void main(String[] args) throws ParseException, java.text.ParseException {
		// logger.debug(GeoTabUtil.formDeviceStatusInfoEntity());
		// System.out.println(GeoTabUtil.getUsers(28.4248829, -99.6108322));

		// DateFormat dateFormat = new
		// SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		// dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		//
		// Long sep = GeoTabUtil.seperateDateTime("00:18:47.8730000");
		// System.out.println(sep);
		// Date parkedDate = new Date();
		// Date dt = dateFormat.parse("2018-07-03T11:34:03.000Z");
		// Calendar cal = Calendar.getInstance();
		// cal.setTime(dt);
		// cal.add(Calendar.MILLISECOND, -sep.intValue());
		// parkedDate.setTime(cal.getTime().getTime());
		//
		// System.out.println(dateFormat.format(parkedDate));

		// System.out.println(formDeviceSearchEntity("SERIA1!"));

	}

	public static NearestUserDtoResponse getUsersRoadDistance(String userId, String locationCode) {

		ResponseMessage message = new ResponseMessage();
		message.setMessage("Users Fetch success");
		message.setStatus(MurphyConstant.SUCCESS);
		message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		NearestUserDtoResponse nearestUserDtoResponse = null;
		Coordinates cordinateDto = null;
		try {
			GeoTabDao geoTabDao = SpringContextBridge.services().getGeoTabDao();
			if (!ServicesUtil.isEmpty(locationCode)) {
				cordinateDto = geoTabDao.getLatLongByLocationCode(locationCode);
				if (!ServicesUtil.isEmpty(cordinateDto) && !ServicesUtil.isEmpty(cordinateDto.getLatitude())
						&& !ServicesUtil.isEmpty(cordinateDto.getLongitude())) {
					// Using ArcGIS only if location in US, crow fly if Canada - Prakash Kumar
					if (locationCode.startsWith("MUR-US")) {
						nearestUserDtoResponse = getUsersByLocationByRoad(userId, cordinateDto.getLatitude(),
								cordinateDto.getLongitude());
					} else if(locationCode.startsWith("MUR-CA")){
						nearestUserDtoResponse = getUsersByLocationCanada(userId, cordinateDto.getLatitude(),
								cordinateDto.getLongitude());
					}
					return nearestUserDtoResponse;
				}
			}
			/*
			 * } else if(!ServicesUtil.isEmpty(latitude) &&
			 * !ServicesUtil.isEmpty(longitude)) { nearestUserDtoResponse =
			 * getUsersByLocationByRoad(userId,latitude, longitude); return
			 * nearestUserDtoResponse; }
			 */
		} catch (Exception ex) {
			message.setMessage("Getting Default IDP Users as Exception : " + ex.getMessage());
			message.setStatus(MurphyConstant.FAILURE);
			message.setStatusCode(MurphyConstant.CODE_FAILURE);
		}
		nearestUserDtoResponse = getUsersByLocation(null, null, null, null);
		return nearestUserDtoResponse;

	}

	private static NearestUserDtoResponse getUsersByLocationByRoad(String userId, Double latitude, Double longitude) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(GeoTabConstants.FAILURE);
		responseMessage.setStatusCode(GeoTabConstants.CODE_FAILURE);

		UserIDPMappingDao mappingDao = SpringContextBridge.services().getUserIDPMappingDao();

		// Map<String, Device> deviceMap = null;
		JSONObject inDeviceStatusInfoObject = null;
		Coordinates center = null;
		NearestUserDto nearestUserDto = null;
		JSONObject deviceStatusInfo = null;
		JSONObject deviceInfo = null;
		NearestUserDtoResponse nearestUserDtoResponse = new NearestUserDtoResponse();
		Device deviceStatus = null;
		Coordinates deviceLoc = null;
		Double distance = null;

		nearestUserDto = mappingDao.getUserDetailsByUser(userId);

		if (!ServicesUtil.isEmpty(latitude) && !ServicesUtil.isEmpty(longitude)
				&& !ServicesUtil.isEmpty(nearestUserDto)) {
			center = new Coordinates(latitude, longitude);
			try {
				deviceStatusInfo = GeoTabUtil.callRest(formDeviceStatusInfoEntity());
				deviceInfo = GeoTabUtil.callRest(formDeviceSearchEntity());
			} catch (ParseException e) {
				System.err.println("Exception : " + e.getMessage());
			}
			String deviceId = "";
			if (!ServicesUtil.isEmpty(deviceInfo)) {
				JSONObject inDeviceInfoObject = null;
				JSONArray deviceInfoArray = deviceInfo.getJSONArray("result");
				for (Object inDeviceInfoArrayObject : deviceInfoArray) {
					inDeviceInfoObject = (JSONObject) inDeviceInfoArrayObject;
					if (nearestUserDto.getSerialId().equals(inDeviceInfoObject.getString("serialNumber"))) {
						deviceId = inDeviceInfoObject.getString("id");
						break;
					}
				}
			}

			if (!ServicesUtil.isEmpty(deviceStatusInfo)) {
				JSONArray deviceStatusInfoArray = deviceStatusInfo.getJSONArray("result");
				for (Object inDeviceStatusInfoArrayObject : deviceStatusInfoArray) {
					inDeviceStatusInfoObject = (JSONObject) inDeviceStatusInfoArrayObject;
					JSONObject dev = inDeviceStatusInfoObject.getJSONObject("device");
					deviceStatus = new Device();
					if (deviceId.equals(dev.getString("id"))) {
						deviceStatus.setId(dev.getString("id"));
						deviceStatus.setLatitude(inDeviceStatusInfoObject.getDouble("latitude"));
						deviceStatus.setLongitude(inDeviceStatusInfoObject.getDouble("longitude"));
						deviceStatus.setIsDriving(inDeviceStatusInfoObject.getBoolean("isDriving"));
						deviceStatus.setSerialNumber(nearestUserDto.getSerialId());
						deviceLoc = new Coordinates(deviceStatus.getLatitude(), deviceStatus.getLongitude());
						distance = ArcGISUtil.getRoadDistance(deviceLoc, center).getTotalLength();
						nearestUserDto.setUnRoundedDistance(ServicesUtil.isEmpty(distance) ? null : distance);
						nearestUserDto.setDistanceFromLocation(ServicesUtil.isEmpty(distance) ? null
								: Math.round(nearestUserDto.getUnRoundedDistance() * 10D) / 10D);
						nearestUserDto.setIsDriving(deviceStatus.getIsDriving());
						nearestUserDto.setUserId(nearestUserDto.getEmailId());
						break;
					}
				}
			}
			responseMessage.setStatus(GeoTabConstants.SUCCESS);
			responseMessage.setStatusCode(GeoTabConstants.CODE_SUCCESS);
			responseMessage.setMessage("Users Fetched Successfully");
			nearestUserDtoResponse.setNearestUserDto(nearestUserDto);
		}
		nearestUserDtoResponse.setResponseMessage(responseMessage);
		return nearestUserDtoResponse;
	}

	private static NearestUserDtoResponse getUsersByLocationCanada(String userId, Double latitude, Double longitude) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(GeoTabConstants.FAILURE);
		responseMessage.setStatusCode(GeoTabConstants.CODE_FAILURE);

		UserIDPMappingDao mappingDao = SpringContextBridge.services().getUserIDPMappingDao();

		// Map<String, Device> deviceMap = null;
		JSONObject inDeviceStatusInfoObject = null;
		Coordinates center = null;
		NearestUserDto nearestUserDto = null;
		JSONObject deviceStatusInfo = null;
		JSONObject deviceInfo = null;
		NearestUserDtoResponse nearestUserDtoResponse = new NearestUserDtoResponse();
		Device deviceStatus = null;
		Coordinates deviceLoc = null;
		Double distance = null;

		nearestUserDto = mappingDao.getUserDetailsByUser(userId);

		if (!ServicesUtil.isEmpty(latitude) && !ServicesUtil.isEmpty(longitude)
				&& !ServicesUtil.isEmpty(nearestUserDto)) {
			center = new Coordinates(latitude, longitude);
			try {
				deviceStatusInfo = GeoTabUtil.callRest(formDeviceStatusInfoEntity());
				deviceInfo = GeoTabUtil.callRest(formDeviceSearchEntity());
			} catch (ParseException e) {
				System.err.println("Exception : " + e.getMessage());
			}
			String deviceId = "";
			if (!ServicesUtil.isEmpty(deviceInfo)) {
				JSONObject inDeviceInfoObject = null;
				JSONArray deviceInfoArray = deviceInfo.getJSONArray("result");
				for (Object inDeviceInfoArrayObject : deviceInfoArray) {
					inDeviceInfoObject = (JSONObject) inDeviceInfoArrayObject;
					if (nearestUserDto.getSerialId().equals(inDeviceInfoObject.getString("serialNumber"))) {
						deviceId = inDeviceInfoObject.getString("id");
						break;
					}
				}
			}

			if (!ServicesUtil.isEmpty(deviceStatusInfo)) {
				JSONArray deviceStatusInfoArray = deviceStatusInfo.getJSONArray("result");
				for (Object inDeviceStatusInfoArrayObject : deviceStatusInfoArray) {
					inDeviceStatusInfoObject = (JSONObject) inDeviceStatusInfoArrayObject;
					JSONObject dev = inDeviceStatusInfoObject.getJSONObject("device");
					deviceStatus = new Device();
					if (deviceId.equals(dev.getString("id"))) {
						deviceStatus.setId(dev.getString("id"));
						deviceStatus.setLatitude(inDeviceStatusInfoObject.getDouble("latitude"));
						deviceStatus.setLongitude(inDeviceStatusInfoObject.getDouble("longitude"));
						deviceStatus.setIsDriving(inDeviceStatusInfoObject.getBoolean("isDriving"));
						deviceStatus.setSerialNumber(nearestUserDto.getSerialId());
						deviceLoc = new Coordinates(deviceStatus.getLatitude(), deviceStatus.getLongitude());
						distance = ArcGISUtil.getCanadaRoadDistance(deviceLoc, center).getTotalLength();
						nearestUserDto.setUnRoundedDistance(ServicesUtil.isEmpty(distance) ? null : distance);
						nearestUserDto.setDistanceFromLocation(ServicesUtil.isEmpty(distance) ? null
								: Math.round(nearestUserDto.getUnRoundedDistance() * 10D) / 10D);
						nearestUserDto.setIsDriving(deviceStatus.getIsDriving());
						nearestUserDto.setUserId(nearestUserDto.getEmailId());
						break;
					}
				}
			}
			responseMessage.setStatus(GeoTabConstants.SUCCESS);
			responseMessage.setStatusCode(GeoTabConstants.CODE_SUCCESS);
			responseMessage.setMessage("Users Fetched Successfully");
			nearestUserDtoResponse.setNearestUserDto(nearestUserDto);
		}
		nearestUserDtoResponse.setResponseMessage(responseMessage);
		return nearestUserDtoResponse;
	}
		
}
