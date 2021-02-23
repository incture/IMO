package com.murphy.taskmgmt.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.murphy.blackline.Coordinates;
import com.murphy.blackline.Device;
import com.murphy.blackline.NearestWellsDto;
import com.murphy.blackline.NearestWellsDtoResponse;
import com.murphy.geotab.NearestUserDto;
import com.murphy.geotab.NearestUserDtoResponse;
import com.murphy.taskmgmt.dao.BlackLineDao;
import com.murphy.taskmgmt.dao.EmpDetailsDao;
import com.murphy.taskmgmt.dao.GeoTabDao;
import com.murphy.taskmgmt.dao.UserIDPMappingDao;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.OperatorsAvailabilityDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.UserIDPMappingDto;

@Component("BlackLineUtil")
public class BlackLineUtil {

	private static final Logger logger = LoggerFactory.getLogger(BlackLineUtil.class);
	
//	public static NearestUserDtoResponse getUsers(Double latitude, Double longitude, String locationCode) {
//		ResponseMessage message = new ResponseMessage();
//		message.setMessage("Users Fetch success");
//		message.setStatus(MurphyConstant.SUCCESS);
//		message.setStatusCode(MurphyConstant.CODE_SUCCESS);
//		NearestUserDtoResponse nearestUserDtoResponse = null;
//		Coordinates cordinateDto = null;
//		try {
//			BlackLineDao blackLineDao = SpringContextBridge.services().getBlackLineDao();
//			if (!ServicesUtil.isEmpty(locationCode) && ServicesUtil.isEmpty(latitude)
//					&& ServicesUtil.isEmpty(longitude)) {
//				cordinateDto = blackLineDao.getLatLongByLocationCode(locationCode);
//				if (!ServicesUtil.isEmpty(cordinateDto) && !ServicesUtil.isEmpty(cordinateDto.getLatitude())
//						&& !ServicesUtil.isEmpty(cordinateDto.getLongitude())) {
//					nearestUserDtoResponse = getUsersByLocation(cordinateDto.getLatitude(),
//							cordinateDto.getLongitude());
//					return nearestUserDtoResponse;
//				}
//			} else if (!ServicesUtil.isEmpty(latitude) && !ServicesUtil.isEmpty(longitude)) {
//				nearestUserDtoResponse = getUsersByLocation(latitude, longitude);
//				return nearestUserDtoResponse;
//			}
//		} catch (Exception ex) {
//			message.setMessage("Getting Default IDP Users as Exception : " + ex.getMessage());
//			message.setStatus(MurphyConstant.FAILURE);
//			message.setStatusCode(MurphyConstant.CODE_FAILURE);
//		}
//		nearestUserDtoResponse = getUsersByLocation(null, null);
//		return nearestUserDtoResponse;
//	}

	public static NearestUserDtoResponse getUsersByLocation(Double latitude, Double longitude,String groupId,String userType) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(GeoTabConstants.FAILURE);
		responseMessage.setStatusCode(GeoTabConstants.CODE_FAILURE);

		UserIDPMappingDao mappingDao = SpringContextBridge.services().getUserIDPMappingDao();

		List<NearestUserDto> nearestUserDtos = new ArrayList<NearestUserDto>();
		List<NearestUserDto> otherUsers = null;
		Map<String, NearestUserDto> userDtoMap = null;

		JSONObject inDeviceStatusInfoObject = null;
		Coordinates center = null;
		NearestUserDto nearestUserDto = null;

		List<UserIDPMappingDto> baseUsers = null;
		NearestUserDtoResponse nearestUserDtoResponse = new NearestUserDtoResponse();
		// Device device = null;
		Device deviceStatus = null;
		Coordinates deviceLoc = null;
		Double distance = null;
		BlackLineDao blackLineDao = SpringContextBridge.services().getBlackLineDao();
		List<String> roles = null;
		//Changes made for ALS Inquiry and Investigation
		if(!ServicesUtil.isEmpty(groupId) && !ServicesUtil.isEmpty(userType) && userType.equalsIgnoreCase(MurphyConstant.USER_TYPE_ALS) ){
			roles=mappingDao.getRolesForNDVTasks( groupId, userType);
			baseUsers = blackLineDao.getUserAndTaskCountDetails(roles);
		}else{
			baseUsers = blackLineDao.getUserAndTaskCountDetails(roles);
		}

		
		userDtoMap = new HashMap<String, NearestUserDto>();
		otherUsers = new ArrayList<>();
		if (!ServicesUtil.isEmpty(baseUsers)) {
			for (UserIDPMappingDto mappingDto : baseUsers) {
				nearestUserDto = new NearestUserDto();
				nearestUserDto.setBlacklineId(mappingDto.getBlackLineId());
				nearestUserDto.setFirstName(mappingDto.getUserFirstName());
				nearestUserDto.setLastName(mappingDto.getUserLastName());
				nearestUserDto.setEmailId(mappingDto.getUserEmail());
				nearestUserDto.setTaskCount(mappingDto.getTaskCount());
				nearestUserDto.setTaskAssignable(Boolean.valueOf(mappingDto.getTaskAssignable()));
				nearestUserDto.setpId(mappingDto.getpId());
				if (!ServicesUtil.isEmpty(nearestUserDto.getBlacklineId())) {
					userDtoMap.put(nearestUserDto.getBlacklineId(), nearestUserDto);
				} else {
					otherUsers.add(nearestUserDto);
				}

			}
		}

//		logger.error("userDtoMap" + userDtoMap);
//		logger.error("otherUsers" + otherUsers);

		if (!ServicesUtil.isEmpty(latitude) && !ServicesUtil.isEmpty(longitude) && !ServicesUtil.isEmpty(userDtoMap)) {
			center = new Coordinates(latitude, longitude);
			List<JSONObject> json2 = null;
			try {
				json2 = BlackLineUtil.blackLineApiResponse();
				logger.error("final json " + json2);
				
                if(ServicesUtil.isEmpty(json2)){
                	nearestUserDtoResponse =GeoTabUtil.getUsersByLocation(latitude, longitude, groupId, userType);
                return nearestUserDtoResponse;
                }
			} catch (Exception e) {
				System.err.println("Exception : " + e.getMessage());
			}

			if (!ServicesUtil.isEmpty(json2)) {
				for (int i = 0; i < json2.size(); i++) {
					inDeviceStatusInfoObject = (JSONObject) json2.get(i);
					JSONObject dev1 = inDeviceStatusInfoObject.getJSONObject("status");
					JSONObject dev2 = inDeviceStatusInfoObject.getJSONObject("status").getJSONObject("location");
					deviceStatus = new Device();
					deviceStatus.setUserId(inDeviceStatusInfoObject.getBigInteger("id"));
					deviceStatus.setLatitude(dev2.getDouble("latitude"));
					deviceStatus.setLongitude(dev2.getDouble("longitude"));
					deviceStatus.setIsOnline(dev1.getBoolean("is_online"));
					// deviceStatus.setBlacklineId(deviceMap.get(deviceStatus.getUserId()).getBlacklineId());
					deviceStatus.setBlacklineId(inDeviceStatusInfoObject.getString("name"));

					deviceLoc = new Coordinates(deviceStatus.getLatitude(), deviceStatus.getLongitude());

					NearestUserDto parUserDto = userDtoMap.get(deviceStatus.getBlacklineId());
					if (!ServicesUtil.isEmpty(parUserDto)) {
						distance = getDistance(deviceLoc, center);
						parUserDto.setUnRoundedDistance(ServicesUtil.isEmpty(distance) ? null : distance);
						parUserDto.setDistanceFromLocation(ServicesUtil.isEmpty(distance) ? null
								: Math.round(parUserDto.getUnRoundedDistance() * 10D) / 10D);
						parUserDto.setOnline(deviceStatus.getIsOnline());
						parUserDto.setUserId(parUserDto.getEmailId());
						nearestUserDtos.add(parUserDto);
					}
				}

				responseMessage.setStatus(GeoTabConstants.SUCCESS);
				responseMessage.setStatusCode(GeoTabConstants.CODE_SUCCESS);
				responseMessage.setMessage("Users Fetched Successfully");
			}
			if (!ServicesUtil.isEmpty(nearestUserDtos)) {
				Collections.sort(nearestUserDtos, new DistanceComparator());
			}

		}
		if (ServicesUtil.isEmpty(nearestUserDtos)) {
			for (Map.Entry<String, NearestUserDto> entry : userDtoMap.entrySet()) {
				nearestUserDtos.add(entry.getValue());
			}
		}

		logger.error("nearestUserDtoResponse before other users i.e who has blackline id " + nearestUserDtos);
		if (!ServicesUtil.isEmpty(otherUsers) && otherUsers.size() > 0) {
			for (NearestUserDto userDto : otherUsers) {
				if (userDto.isTaskAssignable()) {
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

		logger.error("nearestUserDtoResponse at the end " + nearestUserDtoResponse);
		return nearestUserDtoResponse;

	}

	// This method filters users based on country
	public static NearestUserDtoResponse getUsersByLocationAndCountry(Double latitude, Double longitude,String groupId,String userType, String country){


		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(GeoTabConstants.FAILURE);
		responseMessage.setStatusCode(GeoTabConstants.CODE_FAILURE);

		UserIDPMappingDao mappingDao = SpringContextBridge.services().getUserIDPMappingDao();

		List<NearestUserDto> nearestUserDtos = new ArrayList<NearestUserDto>();
		List<NearestUserDto> otherUsers = null;
		Map<String, NearestUserDto> userDtoMap = null;

		JSONObject inDeviceStatusInfoObject = null;
		Coordinates center = null;
		NearestUserDto nearestUserDto = null;

		List<UserIDPMappingDto> baseUsers = null;
		NearestUserDtoResponse nearestUserDtoResponse = new NearestUserDtoResponse();
		Device deviceStatus = null;
		Coordinates deviceLoc = null;
		Double distance = null;
		BlackLineDao blackLineDao = SpringContextBridge.services().getBlackLineDao();
		List<String> roles = null;
		//Changes made for ALS Inquiry and Investigation
		if(!ServicesUtil.isEmpty(groupId) && !ServicesUtil.isEmpty(userType) && userType.equalsIgnoreCase(MurphyConstant.USER_TYPE_ALS) ){
			roles=mappingDao.getRolesForNDVTasks( groupId, userType);
			baseUsers = blackLineDao.getUserAndTaskCountDetails(roles);
		}else if(!ServicesUtil.isEmpty(country)){
			if(country.equals(MurphyConstant.EFS_CODE)){
				roles = new ArrayList<String>();
				roles.add(MurphyConstant.PRO_CATARINA);
				roles.add(MurphyConstant.PRO_KARNES);
				roles.add(MurphyConstant.PRO_TILDEN);
			}else if(country.equals(MurphyConstant.CA_CODE)){
				roles = new ArrayList<String>();
				roles.add(MurphyConstant.PRO_KAYBOB);
				roles.add(MurphyConstant.PRO_MONTNEY);
			}
			baseUsers = blackLineDao.getUserAndTaskCountDetails(roles);
		}else{
			baseUsers = blackLineDao.getUserAndTaskCountDetails(roles);
		}

		
		userDtoMap = new HashMap<String, NearestUserDto>();
		otherUsers = new ArrayList<>();
		if (!ServicesUtil.isEmpty(baseUsers)) {
			for (UserIDPMappingDto mappingDto : baseUsers) {
				nearestUserDto = new NearestUserDto();
				nearestUserDto.setBlacklineId(mappingDto.getBlackLineId());
				nearestUserDto.setFirstName(mappingDto.getUserFirstName());
				nearestUserDto.setLastName(mappingDto.getUserLastName());
				nearestUserDto.setEmailId(mappingDto.getUserEmail());
				nearestUserDto.setTaskCount(mappingDto.getTaskCount());
				nearestUserDto.setTaskAssignable(Boolean.valueOf(mappingDto.getTaskAssignable()));
				nearestUserDto.setpId(mappingDto.getpId());
				nearestUserDto.setUserId(mappingDto.getUserEmail());
				if (!ServicesUtil.isEmpty(nearestUserDto.getBlacklineId())) {
					userDtoMap.put(nearestUserDto.getBlacklineId(), nearestUserDto);
				} else {
					otherUsers.add(nearestUserDto);
				}

			}
		}

		if (!ServicesUtil.isEmpty(latitude) && !ServicesUtil.isEmpty(longitude) && !ServicesUtil.isEmpty(userDtoMap)) {
			center = new Coordinates(latitude, longitude);
			List<JSONObject> json2 = null;
			try {
				json2 = BlackLineUtil.blackLineApiResponse();
				logger.error("final json " + json2);
				
                if(ServicesUtil.isEmpty(json2)){
                	nearestUserDtoResponse =GeoTabUtil.getUsersByLocation(latitude, longitude, groupId, userType);
                return nearestUserDtoResponse;
                }
			} catch (Exception e) {
				System.err.println("Exception : " + e.getMessage());
			}

			if (!ServicesUtil.isEmpty(json2)) {
				for (int i = 0; i < json2.size(); i++) {
					inDeviceStatusInfoObject = (JSONObject) json2.get(i);
					JSONObject dev1 = inDeviceStatusInfoObject.getJSONObject("status");
					JSONObject dev2 = inDeviceStatusInfoObject.getJSONObject("status").getJSONObject("location");
					deviceStatus = new Device();
					deviceStatus.setUserId(inDeviceStatusInfoObject.getBigInteger("id"));
					deviceStatus.setLatitude(dev2.getDouble("latitude"));
					deviceStatus.setLongitude(dev2.getDouble("longitude"));
					deviceStatus.setIsOnline(dev1.getBoolean("is_online"));
					deviceStatus.setBlacklineId(inDeviceStatusInfoObject.getString("name"));

					deviceLoc = new Coordinates(deviceStatus.getLatitude(), deviceStatus.getLongitude());

					NearestUserDto parUserDto = userDtoMap.get(deviceStatus.getBlacklineId());
					if (!ServicesUtil.isEmpty(parUserDto)) {
						distance = getDistance(deviceLoc, center);
						parUserDto.setUnRoundedDistance(ServicesUtil.isEmpty(distance) ? null : distance);
						parUserDto.setDistanceFromLocation(ServicesUtil.isEmpty(distance) ? null
								: Math.round(parUserDto.getUnRoundedDistance() * 10D) / 10D);
						parUserDto.setOnline(deviceStatus.getIsOnline());
						parUserDto.setUserId(parUserDto.getEmailId());
						nearestUserDtos.add(parUserDto);
					}
				}

				responseMessage.setStatus(GeoTabConstants.SUCCESS);
				responseMessage.setStatusCode(GeoTabConstants.CODE_SUCCESS);
				responseMessage.setMessage("Users Fetched Successfully");
			}
			if (!ServicesUtil.isEmpty(nearestUserDtos)) {
				Collections.sort(nearestUserDtos, new DistanceComparator());
			}

		}
		if (ServicesUtil.isEmpty(nearestUserDtos)) {
			for (Map.Entry<String, NearestUserDto> entry : userDtoMap.entrySet()) {
				nearestUserDtos.add(entry.getValue());
			}
		}

		logger.error("[Murphy][BlackLineUtil][getUsersByLocationAndCountry] nearestUserDtoResponse before other users i.e who has blackline id " + nearestUserDtos);
		if (!ServicesUtil.isEmpty(otherUsers) && otherUsers.size() > 0) {
			for (NearestUserDto userDto : otherUsers) {
				if (userDto.isTaskAssignable()) {
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

		logger.error("[Murphy][BlackLineUtil][getUsersByLocationAndCountry] nearestUserDtoResponse at the end " + nearestUserDtoResponse);
		return nearestUserDtoResponse;

	
	}
	
	@SuppressWarnings("unused")
	private static List<JSONObject> blackLineApiResponse() {
		List<JSONObject> json2 = null;
		try {
			String clientId = MurphyConstant.BLACKLINE_CLIENT_ID;
			String clientSecret = MurphyConstant.BLACKLINE_CLIENT_SECRET;
			HttpClient httpClient = HttpClientBuilder.create().build();

			String url1 = BlackLineConstants.BLACKILINE_AUTHORIZE_URL.replaceAll("blackline_cleint_id", clientId);
			logger.error("url1 ="+url1);
			HttpGet httpGet = new HttpGet(url1);
			httpGet.addHeader("content-type", "application/json; charset=UTF-8");
			JSONObject json = callRest1(httpClient, httpGet);
			logger.error("json 1 "+json);
			String code = (String) json.get("code");
			logger.error("code ="+code);
			String url2 = BlackLineConstants.BLACKLINE_ACCESS_TOKEN_URL.replaceAll("AuthorizeCode", code)
					.replaceAll("blackline_cleint_id", clientId).replaceAll("blackline_cleint_secret", clientSecret);
			HttpGet httpGet1 = new HttpGet(url2);
			httpGet1.addHeader("content-type", "application/json; charset=UTF-8");
			JSONObject json1 = callRest1(httpClient, httpGet1);
			String access_token = (String) json1.get("access_token");
			logger.error("access_token ="+access_token);

			String url3 = BlackLineConstants.BLACKLINE_BASE_URL.replaceAll("AccessToken", access_token);
			logger.error("url3 = " + url3);
			HttpGet httpGet2 = new HttpGet(url3);
			json2 = callRest2(httpClient, httpGet2);
			return json2;
		} catch (Exception e) {
			logger.error("[MURPHY][BlackLineutil][blackLineApiResponse][Exception]" + e.getMessage());
		}
		return null;
	}

	public static List<JSONObject> callRest2(HttpClient httpClient, HttpGet httpGet) {
		try {
			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			HttpResponse response = httpClient.execute(httpGet);
			String str = EntityUtils.toString(response.getEntity());

			List<JSONObject> listjson = new ArrayList<JSONObject>();
			JSONArray arr = new JSONArray(str);
			System.err.println("arr lenght " + arr.length());
			for (int i = 0; i < arr.length(); i++) {
				listjson.add(arr.getJSONObject(i));
			}
			logger.error("[MURPHY][callRest][listjson]" + listjson);

			return listjson;
		} catch (Exception e) {
			logger.error("[MURPHY][callRest][Exception]" + e.getMessage());
		}

		return null;
	}

	public static JSONObject callRest1(HttpClient httpClient, HttpGet httpGet) {
		try {
			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			HttpResponse response = httpClient.execute(httpGet);
			logger.error("http response ="+response);
			String str = EntityUtils.toString(response.getEntity());
			logger.error("string json ="+str);
			JSONObject obj = new JSONObject(str);
			logger.error("[MURPHY][callRest1][end json Object] " + obj);
			return obj;
		} catch (Exception e) {
			logger.error("[MURPHY][BlackLineUtil][callRest1][Exception]" + e.getMessage());
		}
		return null;
	}

	static class DistanceComparator implements Comparator<NearestUserDto> {

		@Override
		public int compare(NearestUserDto o1, NearestUserDto o2) {
			return Double.compare(o1.getUnRoundedDistance(), o2.getUnRoundedDistance());
		}

	}

	public static Double getDistance(Coordinates p1, Coordinates p2) {

		Double dLat = BlackLineUtil.degreeToRad(p2.getLatitude() - p1.getLatitude());
		Double dLon = BlackLineUtil.degreeToRad(p2.getLongitude() - p1.getLongitude());

		Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(BlackLineUtil.degreeToRad(p1.getLatitude()))
				* Math.cos(BlackLineUtil.degreeToRad(p2.getLatitude())) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		Double d = BlackLineConstants.EARTH_RADIUS_IN_MILES * c;
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

	public static NearestWellsDtoResponse getWells(String userEmail) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(MurphyConstant.SUCCESS);
		responseMessage.setStatusCode(GeoTabConstants.CODE_SUCCESS);
		responseMessage.setMessage("No Wells Found Within the area");
		NearestWellsDtoResponse nearestWellsDtoResponse = new NearestWellsDtoResponse();
		List<NearestWellsDto> nearestWellsDtoList = new ArrayList<NearestWellsDto>();
		NearestWellsDto nearestWellsDto = null;
		String blackLineId = null;
		List<JSONObject> json = null;
		JSONObject inDeviceStatusInfoObject = null;
		Coordinates centre = null;
		Coordinates point = null;

		try {
			BlackLineDao blackLineDao = SpringContextBridge.services().getBlackLineDao();

			blackLineId = blackLineDao.getBlackLineIdbyEmail(userEmail);
			logger.error("blackLineId from UserEmail is =" + blackLineId);
			json = BlackLineUtil.blackLineApiResponse();
			logger.error("json from blackline api hit =" + json);
			if (!ServicesUtil.isEmpty(json)) {
				for (int i = 0; i < json.size(); i++) {
					inDeviceStatusInfoObject = (JSONObject) json.get(i);
					if (blackLineId.equalsIgnoreCase(inDeviceStatusInfoObject.getString("name"))) {
						logger.error("Blackline id matched with json response is ="
								+ inDeviceStatusInfoObject.getString("name"));
						JSONObject dev2 = inDeviceStatusInfoObject.getJSONObject("status").getJSONObject("location");
						centre = new Coordinates();
						centre.setLatitude(dev2.getDouble("latitude"));
						centre.setLongitude(dev2.getDouble("longitude"));
						logger.error("Centre to draw a circle is =" + centre);
						break;
					}
					if(i==json.size()-1 && !blackLineId.equalsIgnoreCase(inDeviceStatusInfoObject.getString("name"))){
						responseMessage.setMessage("BlackLineId didn't matched for Entered Email");
					}
				}
			}

			logger.error("Centre to draw a circle after for loop =" + centre);

			GeoTabDao geoTabDao = SpringContextBridge.services().getGeoTabDao();
			List<LocationHierarchyDto> allWellDetails = geoTabDao.getAllWellDetails();

			logger.error("all well details =" + allWellDetails);

			if (!ServicesUtil.isEmpty(allWellDetails)) {
				for (LocationHierarchyDto locationHierarchyDto : allWellDetails) {
					point = new Coordinates(locationHierarchyDto.getLatValue(), locationHierarchyDto.getLongValue());
					logger.error("each well latittude and longitude =" + point);
					logger.error("centre point while checking inside wells for loop =" + centre);
					Boolean isWellInside = CheckIfPointInside(centre, point,
							BlackLineConstants.WELLS_INSIDE_RADIUS_IN_MILES);
					logger.error("boolean value isWellInside =" + isWellInside);
					if (isWellInside) {
						nearestWellsDto = new NearestWellsDto();
						nearestWellsDto.setLocText(locationHierarchyDto.getLocationText());
						nearestWellsDto.setLocCode(locationHierarchyDto.getLocation());
						nearestWellsDtoList.add(nearestWellsDto);
						logger.error("well inside the circle is =" + nearestWellsDto);
					}
				}
			}

			logger.error("total well list inside the circle is =" + nearestWellsDtoList);
		} catch (Exception e) {
			logger.error("[MURPHY][BlackLineUtil][getWells][Exception]" + e.getMessage());
		}
		nearestWellsDtoResponse.setNearestWellsDto(nearestWellsDtoList);
		if (!ServicesUtil.isEmpty(nearestWellsDtoList)) {
			responseMessage.setMessage("Successfully Found The Wells Within The Area");
		}
		nearestWellsDtoResponse.setResponseMessage(responseMessage);
		return nearestWellsDtoResponse;
	}

	public static Boolean CheckIfPointInside(Coordinates center, Coordinates point, Double radius) {
		Double distance = BlackLineUtil.getDistance(point, center);
		logger.error("distance between the well and the centre " + distance);
		if (distance > radius) {
			return false;
		} else if (distance <= radius) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unused")
	public static NearestUserDtoResponse getUsersByAbility(OperatorsAvailabilityDto dto) {
		
		NearestUserDtoResponse nearestUserDtoResponse = new NearestUserDtoResponse();
		try{
			BlackLineDao blackLineDao = SpringContextBridge.services().getBlackLineDao();
			nearestUserDtoResponse = blackLineDao.getUsersByAbility(dto);
			//logger.error("[MURPHY][BlackLineUtil][getUsersByAbility][nearestUserDtoResponse]" + nearestUserDtoResponse);
		    return nearestUserDtoResponse;
		}catch(Exception e){
			logger.error("[MURPHY][BlackLineUtil][getUsersByAbility][Exception]" + e.getMessage());
		}
		return null;
	}
	
	public static Map<String, List<String>> getEmpByShift() {
		
		try{
			BlackLineDao blackLineDao = SpringContextBridge.services().getBlackLineDao();
			return blackLineDao.getEmpByShift();
			
			
		}catch(Exception e){
			logger.error("[StopTimeDao][getEmpByShift][Exception] "+e.getMessage());
		}
		return null;
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	//added to handle blackline for locationCode
	public static NearestUserDtoResponse getUserByLocationAndCountry(Double latitude, Double longitude,String groupId,String userType, String locationCode) throws Exception
	{


		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(GeoTabConstants.FAILURE);
		responseMessage.setStatusCode(GeoTabConstants.CODE_FAILURE);

		UserIDPMappingDao mappingDao = SpringContextBridge.services().getUserIDPMappingDao();

		List<NearestUserDto> nearestUserDtos = new ArrayList<NearestUserDto>();
		List<NearestUserDto> otherUsers = null;
		Map<String, NearestUserDto> userDtoMap = null;
		Map<String,NearestUserDto>userEmailtoDetailsMap=new HashMap<>();

		JSONObject inDeviceStatusInfoObject = null;
		Coordinates center = null;
		NearestUserDto nearestUserDto = null;

		List<UserIDPMappingDto> baseUsers = null;
		NearestUserDtoResponse nearestUserDtoResponse = new NearestUserDtoResponse();
		Device deviceStatus = null;
		Coordinates deviceLoc = null;
		Double distance = null;
		BlackLineDao blackLineDao = SpringContextBridge.services().getBlackLineDao();
		List<String> roles = null;
		//added to handle blackline for locationCode 
		String country = ServicesUtil.getCountryCodeByLocation(locationCode);
		//Changes made for ALS Inquiry and Investigation
		if(!ServicesUtil.isEmpty(groupId) && !ServicesUtil.isEmpty(userType) && userType.equalsIgnoreCase(MurphyConstant.USER_TYPE_ALS) ){
			roles=mappingDao.getRolesForNDVTasks( groupId, userType);
			baseUsers = blackLineDao.getUserAndTaskCountDetails(roles);
		}else if(!ServicesUtil.isEmpty(country)){
			if(country.equals(MurphyConstant.EFS_CODE)){
				roles = new ArrayList<String>();
				roles.add(MurphyConstant.PRO_CATARINA);
				roles.add(MurphyConstant.PRO_KARNES);
				roles.add(MurphyConstant.PRO_TILDEN);
				roles.add(MurphyConstant.ALS_EAST);
				roles.add(MurphyConstant.ALS_WEST);
				roles.add(MurphyConstant.OBX_CATARINA);
				roles.add(MurphyConstant.OBX_KARNES);
				roles.add(MurphyConstant.OBX_TILDEN);
			} else if (country.equals(MurphyConstant.CA_CODE)) {
				roles = new ArrayList<String>();
				// added to handle blackline for locationCode
				if (locationCode.startsWith("MUR-CA-M") || locationCode.startsWith("MUR-CA-T")) {
					roles.add(MurphyConstant.PRO_MONTNEY);
					roles.add(MurphyConstant.ALS_MONTNEY);
				}
				if (locationCode.startsWith("MUR-CA-K")) {
					roles.add(MurphyConstant.PRO_KAYBOB);
					roles.add(MurphyConstant.ALS_KAYBOB);
				}

				// old code roles.add(MurphyConstant.PRO_MONTNEY);
				// roles.add(MurphyConstant.PRO_KAYBOB);
			}
			baseUsers = blackLineDao.getUserAndTaskCountDetails(roles);
		}else{
			baseUsers = blackLineDao.getUserAndTaskCountDetails(roles);
		}

		
		userDtoMap = new HashMap<String, NearestUserDto>();
		otherUsers = new ArrayList<>();
		if (!ServicesUtil.isEmpty(baseUsers)) {
			for (UserIDPMappingDto mappingDto : baseUsers) {
				nearestUserDto = new NearestUserDto();
				nearestUserDto.setBlacklineId(mappingDto.getBlackLineId());
				nearestUserDto.setFirstName(mappingDto.getUserFirstName());
				nearestUserDto.setLastName(mappingDto.getUserLastName());
				nearestUserDto.setEmailId(mappingDto.getUserEmail());
				nearestUserDto.setTaskCount(mappingDto.getTaskCount());
				nearestUserDto.setTaskAssignable(Boolean.valueOf(mappingDto.getTaskAssignable()));
				nearestUserDto.setpId(mappingDto.getpId());
				nearestUserDto.setUserId(mappingDto.getUserEmail());
				if (!ServicesUtil.isEmpty(nearestUserDto.getBlacklineId())) {
					userDtoMap.put(nearestUserDto.getBlacklineId(), nearestUserDto);
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
			List<JSONObject> json2 = null;
			try {
				//added to handle blackline for locationCode
				json2 = BlackLineUtil.blackLineApiResponseBasedOnLocationCode(locationCode);
				logger.error("BlackLineUtil.getUserByLocationAndCountry() black line json "+locationCode+" "+ json2);
				
                if(ServicesUtil.isEmpty(json2)){
                	nearestUserDtoResponse =GeoTabUtil.getUsersByLocation(latitude, longitude, groupId, userType);
                return nearestUserDtoResponse;
                }
			} catch (Exception e) {
				System.err.println("BlackLineUtil.getUserByLocationAndCountry() Exception : " + e.getMessage());
			}

			if (!ServicesUtil.isEmpty(json2)) {
				for (int i = 0; i < json2.size(); i++) {
					inDeviceStatusInfoObject = (JSONObject) json2.get(i);
					JSONObject dev1 = inDeviceStatusInfoObject.getJSONObject("status");
					JSONObject dev2 = inDeviceStatusInfoObject.getJSONObject("status").getJSONObject("location");
					deviceStatus = new Device();
					deviceStatus.setUserId(inDeviceStatusInfoObject.getBigInteger("id"));
					deviceStatus.setLatitude(dev2.getDouble("latitude"));
					deviceStatus.setLongitude(dev2.getDouble("longitude"));
					deviceStatus.setIsOnline(dev1.getBoolean("is_online"));
					deviceStatus.setBlacklineId(String.valueOf(inDeviceStatusInfoObject.getBigInteger("id")));

					deviceLoc = new Coordinates(deviceStatus.getLatitude(), deviceStatus.getLongitude());

					
					NearestUserDto parUserDto = userDtoMap.get(deviceStatus.getBlacklineId());
					
					if (!ServicesUtil.isEmpty(parUserDto)) {
						distance = getDistance(deviceLoc, center);
						parUserDto.setUnRoundedDistance(ServicesUtil.isEmpty(distance) ? null : distance);
						parUserDto.setDistanceFromLocation(ServicesUtil.isEmpty(distance) ? null
								: Math.round(parUserDto.getUnRoundedDistance() * 10D) / 10D);
						parUserDto.setOnline(deviceStatus.getIsOnline());
						parUserDto.setUserId(parUserDto.getEmailId());
						nearestUserDtos.add(parUserDto);
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

		logger.error("[Murphy][BlackLineUtil][getUserByLocationAndCountry] nearestUserDtoResponse before other users i.e who has blackline id " + nearestUserDtos);
		if (!ServicesUtil.isEmpty(otherUsers) && otherUsers.size() > 0) {
			for (NearestUserDto userDto : otherUsers) {
				if (userDto.isTaskAssignable()) {
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

		logger.error("[Murphy][BlackLineUtil][getUserByLocationAndCountry] nearestUserDtoResponse at the end " + nearestUserDtoResponse);
		return nearestUserDtoResponse;

	
	}
	
	public static NearestUserDtoResponse getUserByLocation(Double latitude, Double longitude,String groupId,String userType,String locationCode) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(GeoTabConstants.FAILURE);
		responseMessage.setStatusCode(GeoTabConstants.CODE_FAILURE);

		UserIDPMappingDao mappingDao = SpringContextBridge.services().getUserIDPMappingDao();

		List<NearestUserDto> nearestUserDtos = new ArrayList<NearestUserDto>();
		List<NearestUserDto> otherUsers = null;
		Map<String, NearestUserDto> userDtoMap = null;

		JSONObject inDeviceStatusInfoObject = null;
		Coordinates center = null;
		NearestUserDto nearestUserDto = null;

		List<UserIDPMappingDto> baseUsers = null;
		NearestUserDtoResponse nearestUserDtoResponse = new NearestUserDtoResponse();
		// Device device = null;
		Device deviceStatus = null;
		Coordinates deviceLoc = null;
		Double distance = null;
		BlackLineDao blackLineDao = SpringContextBridge.services().getBlackLineDao();
		List<String> roles = null;
		//Changes made for ALS Inquiry and Investigation
		if(!ServicesUtil.isEmpty(groupId) && !ServicesUtil.isEmpty(userType) && userType.equalsIgnoreCase(MurphyConstant.USER_TYPE_ALS) ){
			roles=mappingDao.getRolesForNDVTasks( groupId, userType);
			baseUsers = blackLineDao.getUserAndTaskCountDetails(roles);
		}else{
			baseUsers = blackLineDao.getUserAndTaskCountDetails(roles);
		}

		
		userDtoMap = new HashMap<String, NearestUserDto>();
		otherUsers = new ArrayList<>();
		if (!ServicesUtil.isEmpty(baseUsers)) {
			for (UserIDPMappingDto mappingDto : baseUsers) {
				nearestUserDto = new NearestUserDto();
				nearestUserDto.setBlacklineId(mappingDto.getBlackLineId());
				nearestUserDto.setFirstName(mappingDto.getUserFirstName());
				nearestUserDto.setLastName(mappingDto.getUserLastName());
				nearestUserDto.setEmailId(mappingDto.getUserEmail());
				nearestUserDto.setTaskCount(mappingDto.getTaskCount());
				nearestUserDto.setTaskAssignable(Boolean.valueOf(mappingDto.getTaskAssignable()));
				nearestUserDto.setpId(mappingDto.getpId());
				if (!ServicesUtil.isEmpty(nearestUserDto.getBlacklineId())) {
					userDtoMap.put(nearestUserDto.getBlacklineId(), nearestUserDto);
				} else {
					otherUsers.add(nearestUserDto);
				}

			}
		}

//		logger.error("userDtoMap" + userDtoMap);
//		logger.error("otherUsers" + otherUsers);

		if (!ServicesUtil.isEmpty(latitude) && !ServicesUtil.isEmpty(longitude) && !ServicesUtil.isEmpty(userDtoMap)) {
			center = new Coordinates(latitude, longitude);
			List<JSONObject> json2 = null;
			try {
				//json2 = BlackLineUtil.blackLineApiResponse(); -- old code
				//added to handle blackline for locationCode
				
				json2 = BlackLineUtil.blackLineApiResponseBasedOnLocationCode(locationCode);
				logger.error("final json " + json2);
				
                if(ServicesUtil.isEmpty(json2)){
                	nearestUserDtoResponse =GeoTabUtil.getUsersByLocation(latitude, longitude, groupId, userType);
                return nearestUserDtoResponse;
                }
			} catch (Exception e) {
				System.err.println("Exception : " + e.getMessage());
			}

			if (!ServicesUtil.isEmpty(json2)) {
				for (int i = 0; i < json2.size(); i++) {
					inDeviceStatusInfoObject = (JSONObject) json2.get(i);
					JSONObject dev1 = inDeviceStatusInfoObject.getJSONObject("status");
					JSONObject dev2 = inDeviceStatusInfoObject.getJSONObject("status").getJSONObject("location");
					deviceStatus = new Device();
					deviceStatus.setUserId(inDeviceStatusInfoObject.getBigInteger("id"));
					deviceStatus.setLatitude(dev2.getDouble("latitude"));
					deviceStatus.setLongitude(dev2.getDouble("longitude"));
					deviceStatus.setIsOnline(dev1.getBoolean("is_online"));
					// deviceStatus.setBlacklineId(deviceMap.get(deviceStatus.getUserId()).getBlacklineId());
					deviceStatus.setBlacklineId(inDeviceStatusInfoObject.getString("name"));

					deviceLoc = new Coordinates(deviceStatus.getLatitude(), deviceStatus.getLongitude());

					NearestUserDto parUserDto = userDtoMap.get(deviceStatus.getBlacklineId());
					if (!ServicesUtil.isEmpty(parUserDto)) {
						distance = getDistance(deviceLoc, center);
						parUserDto.setUnRoundedDistance(ServicesUtil.isEmpty(distance) ? null : distance);
						parUserDto.setDistanceFromLocation(ServicesUtil.isEmpty(distance) ? null
								: Math.round(parUserDto.getUnRoundedDistance() * 10D) / 10D);
						parUserDto.setOnline(deviceStatus.getIsOnline());
						parUserDto.setUserId(parUserDto.getEmailId());
						nearestUserDtos.add(parUserDto);
					}
				}

				responseMessage.setStatus(GeoTabConstants.SUCCESS);
				responseMessage.setStatusCode(GeoTabConstants.CODE_SUCCESS);
				responseMessage.setMessage("Users Fetched Successfully");
			}
			if (!ServicesUtil.isEmpty(nearestUserDtos)) {
				Collections.sort(nearestUserDtos, new DistanceComparator());
			}

		}
		if (ServicesUtil.isEmpty(nearestUserDtos)) {
			for (Map.Entry<String, NearestUserDto> entry : userDtoMap.entrySet()) {
				nearestUserDtos.add(entry.getValue());
			}
		}

		logger.error("nearestUserDtoResponse before other users i.e who has blackline id " + nearestUserDtos);
		if (!ServicesUtil.isEmpty(otherUsers) && otherUsers.size() > 0) {
			for (NearestUserDto userDto : otherUsers) {
				if (userDto.isTaskAssignable()) {
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

		logger.error("nearestUserDtoResponse at the end getUserByLocation" + nearestUserDtoResponse);
		return nearestUserDtoResponse;

	}
	private static List<JSONObject> blackLineApiResponseBasedOnLocationCode(String locationCode) {
		List<JSONObject> json2 = null;
		try {
			String clientId = "";
			String clientSecret ="";
			
			//MONTNEY
			if(locationCode.startsWith("MUR-CA-M") || locationCode.startsWith("MUR-CA-T"))
			{
				clientId = MurphyConstant.MONTNEY_BLACKLINE_CLIENT_ID;
				clientSecret = MurphyConstant.MONTNEY_BLACKLINE_CLIENT_SECRET;
			}
			
			// KAYBOB
			if (locationCode.startsWith("MUR-CA-K")) {
				clientId = MurphyConstant.KAYBOB_BLACKLINE_CLIENT_ID;
				clientSecret = MurphyConstant.KAYBOB_BLACKLINE_CLIENT_SECRET;
			}
			
			// EFS
			if (locationCode.startsWith("MUR-US-EFS")) {
				clientId = MurphyConstant.BLACKLINE_CLIENT_ID;
				clientSecret = MurphyConstant.BLACKLINE_CLIENT_SECRET;
			}
			
			HttpClient httpClient = HttpClientBuilder.create().build();

			String url1 = BlackLineConstants.BLACKILINE_AUTHORIZE_URL.replaceAll("blackline_cleint_id", clientId);
			logger.error("url1 ="+url1);
			HttpGet httpGet = new HttpGet(url1);
			httpGet.addHeader("content-type", "application/json; charset=UTF-8");
			JSONObject json = callRest1(httpClient, httpGet);
			logger.error("json 1 "+json);
			String code = (String) json.get("code");
			logger.error("code ="+code);
			String url2 = BlackLineConstants.BLACKLINE_ACCESS_TOKEN_URL.replaceAll("AuthorizeCode", code)
					.replaceAll("blackline_cleint_id", clientId).replaceAll("blackline_cleint_secret", clientSecret);
			HttpGet httpGet1 = new HttpGet(url2);
			httpGet1.addHeader("content-type", "application/json; charset=UTF-8");
			JSONObject json1 = callRest1(httpClient, httpGet1);
			String access_token = (String) json1.get("access_token");
			logger.error("access_token ="+access_token);

			String url3 = BlackLineConstants.BLACKLINE_BASE_URL.replaceAll("AccessToken", access_token);
			logger.error("url3 = " + url3);
			HttpGet httpGet2 = new HttpGet(url3);
			json2 = callRest2(httpClient, httpGet2);
			return json2;
		} catch (Exception e) {
			logger.error("[MURPHY][BlackLineutil][blackLineApiResponseBasedOnLocationCode][Exception]" + e.getMessage());
		}
		return null;
	}
	
	private static Map<String, NearestUserDto> getNearestUsersByBaseLocationDistance(Map<String, NearestUserDto> usersDetailsMap,
			Coordinates center) {

		Map<String, NearestUserDto> responseUserEmailtoDetailsMap = new HashMap<>();

		EmpDetailsDao empDetailsDao = SpringContextBridge.services().getEmpDetailsDao();
		Map<String, com.murphy.blackline.Coordinates> userBaseLocationCoordinatesMap = empDetailsDao
				.getUsersBaseLocationCoordinates(new ArrayList<>(usersDetailsMap.keySet()));

		
		if (!ServicesUtil.isEmpty(userBaseLocationCoordinatesMap)&&userBaseLocationCoordinatesMap.size()>0) {

			for (String userEmailId : userBaseLocationCoordinatesMap.keySet()) {
				NearestUserDto parUserDto = usersDetailsMap.get(userEmailId);
				if (!ServicesUtil.isEmpty(parUserDto)) {
					com.murphy.blackline.Coordinates coordinates = userBaseLocationCoordinatesMap.get(userEmailId);
					Double distance = getDistance(coordinates, center);
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
}
