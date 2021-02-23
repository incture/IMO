package com.murphy.taskmgmt.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.murphy.geotab.Coordinates;
import com.murphy.taskmgmt.dao.EmailNotificationMasterDao;
import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dto.ArcGISBestSequesnceDto;
import com.murphy.taskmgmt.dto.ArcGISBestSequesnceListDto;
import com.murphy.taskmgmt.dto.ArcGISResponseDto;
import com.murphy.taskmgmt.dto.ObxTaskDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.EmailNotificationMasterDo;
import com.murphy.taskmgmt.service.interfaces.ObxSchedulerFacadeLocal;
@Component
public class ArcGISUtil {
	
	@Autowired
	ObxSchedulerFacadeLocal obxSchedulerFacadeLocal;

	@Autowired
	private HierarchyDao hierarchyDao;
	
	
	static EmailNotificationMasterDao emailNotificationMasterDao;
	
	@Autowired
	public void initalizeemailNotificationMasterDao(EmailNotificationMasterDao emailNotificationMasterDao){
		ArcGISUtil.emailNotificationMasterDao = emailNotificationMasterDao;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(ArcGISUtil.class);

	public static ArcGISResponseDto getRoadDistance(Coordinates coordOne, Coordinates coordTwo) {
		MailAlertUtil mail = new MailAlertUtil();
		
		ArcGISResponseDto responseDto = null;
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		String serviceURL = MurphyConstant.ARCGIS_SERVICE_URL + coordOne.getLongitude() + "," + coordOne.getLatitude()
				+ ";" + coordTwo.getLongitude() + "," + coordTwo.getLatitude()
				+ MurphyConstant.ARCGIS_SERVICE_URL_PAYLOAD;

		String arcGisResult = null;
//		String arcGisResult = DestinationUtil.executeWithDest(MurphyConstant.ARCGIS_DEST_NAME, serviceURL,
//				MurphyConstant.HTTP_METHOD_GET, MurphyConstant.APPLICATION_JSON, "", "",
//				MurphyConstant.ON_PREMISE_PROXY, false);
		
		logger.error("[ArcGISUtil][getRoadDistance][Argis URL] : "+ MurphyConstant.ARCGIS_API_HOST+serviceURL);
		arcGisResult = RestUtil.callRestURL(MurphyConstant.ARCGIS_API_HOST+serviceURL, null, MurphyConstant.HTTP_METHOD_GET, true).toString();
		responseDto = new ArcGISResponseDto();
		if(!ServicesUtil.isEmpty(arcGisResult)) {
			if (arcGisResult.contains("directions") && arcGisResult.contains("summary")) {
				JSONObject arcGisObject = new JSONObject(arcGisResult);
				JSONArray directions = arcGisObject.getJSONArray("directions");
				JSONObject summary = directions.getJSONObject(0).getJSONObject("summary");
				responseDto.setTotalLength(summary.getDouble("totalLength"));
				responseDto.setTotalTime(summary.getDouble("totalTime"));
				responseDto.setTotalDriveTime(summary.getDouble("totalDriveTime"));
				if (responseDto.getTotalDriveTime() > 120.0) { // arcGISResponseDto.getTotalDriveTime()>120mins
					logger.error("[Murphy][ArcGISUtil][getRoadDistance] Mail to Carl " + arcGisResult);	
					List<EmailNotificationMasterDo> emailList = emailNotificationMasterDao.getEmailByConfigItemAndRecpType(MurphyConstant.DRIVE_TIME,"TO");
//					logger.error(emailList.toString());
					String toEmail = null;
					String ccEmail = null;
					if(!ServicesUtil.isEmpty(emailList))
					toEmail = emailList.stream().map(EmailNotificationMasterDo::getConfigValue).collect(Collectors.joining(","));
//					logger.error(toEmail);

					emailList = emailNotificationMasterDao.getEmailByConfigItemAndRecpType(MurphyConstant.DRIVE_TIME,"CC");
					if(!ServicesUtil.isEmpty(emailList))
					ccEmail = emailList.stream().map(EmailNotificationMasterDo::getConfigValue).collect(Collectors.joining(","));
//					logger.error(ccEmail);
					
					if(!ServicesUtil.isEmpty(toEmail) && !ServicesUtil.isEmpty(ccEmail))
					mail.sendMailWithCC(toEmail,
							ccEmail, "IOP - Drive time coming more than expected",
							"For the following set of locations, the drive time is more than 120 minutes:</br></br>Location1 : lat ="
									+ coordOne.getLatitude() + " , long =" + coordOne.getLongitude() + "</br>"
									+ "Location2 : lat =" + coordTwo.getLatitude() + " ,long ="
									+ coordTwo.getLongitude() + "</br>Drive Time: "
									+ Math.floor(responseDto.getTotalDriveTime())
									+ " minutes</br></br>Could you please review and suggest.</br></br> Regards,</br>Murphy SAP Cloud Platform Support Team</br>",
							"Carl");
					responseDto.setTotalDriveTime(120.0); // if greater than
															// 120mins fix the
															// drivetime as
															// 120mins
					responseDto.setTotalTime(120.0);
			}
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				responseMessage.setMessage("ArcGIS Fetch Success");
			} else if (arcGisResult.contains("error")) {
				responseMessage.setMessage(arcGisResult);
			}
		} else {
			Double distance = GeoTabUtil.getDistance(coordOne, coordTwo);
			responseDto.setTotalLength(distance);
			Double totalTime = (distance * 60) / 40;
			responseDto.setTotalTime(totalTime);
			responseDto.setTotalDriveTime(totalTime);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseMessage.setMessage("Crow fly distance");
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;
	}
	
	/*
	 * This method calculates crow fly distance and drive time for Canada - Prakash Kumar
	 * */
	public static ArcGISResponseDto getCanadaRoadDistance(Coordinates coordOne, Coordinates coordTwo) {

		ArcGISResponseDto responseDto = new ArcGISResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		Double distance = GeoTabUtil.getDistance(coordOne, coordTwo);
		responseDto.setTotalLength(distance);
		Double totalTime = (distance * 60) / 40;
		responseDto.setTotalTime(totalTime);
		responseDto.setTotalDriveTime(totalTime);
		responseMessage.setStatus(MurphyConstant.SUCCESS);
		responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		responseMessage.setMessage("Crow fly distance");

		responseDto.setResponseMessage(responseMessage);
		return responseDto;
	}

	public static ArcGISResponseDto getRoadDistance(List<Coordinates> coordList) {

		ArcGISResponseDto responseDto = null;

		String coordinatesString = "";

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		for (Coordinates coord : coordList) {
			coordinatesString += coord.getLongitude() + "," + coord.getLatitude() + ";";
		}

		String serviceURL = MurphyConstant.ARCGIS_SERVICE_URL + coordinatesString
				+ MurphyConstant.ARCGIS_SERVICE_URL_PAYLOAD;
		String arcGisResult = RestUtil.callRestURL(MurphyConstant.ARCGIS_API_HOST+serviceURL, null, MurphyConstant.HTTP_METHOD_GET, true).toString();
//		String arcGisResult = DestinationUtil.executeWithDest(MurphyConstant.ARCGIS_DEST_NAME, serviceURL,
//				MurphyConstant.HTTP_METHOD_GET, MurphyConstant.APPLICATION_JSON, "", "",
//				MurphyConstant.ON_PREMISE_PROXY, false);
		responseDto = new ArcGISResponseDto();
		if (arcGisResult.contains("directions") && arcGisResult.contains("summary")) {
			JSONObject arcGisObject = new JSONObject(arcGisResult);
			JSONArray directions = arcGisObject.getJSONArray("directions");
			JSONObject summary = directions.getJSONObject(0).getJSONObject("summary");
			responseDto.setTotalLength(summary.getDouble("totalLength"));
			responseDto.setTotalTime(summary.getDouble("totalTime"));
			responseDto.setTotalDriveTime(summary.getDouble("totalDriveTime"));

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseMessage.setMessage("ArcGIS Fetch Success");
		} else if (arcGisResult.contains("error")) {
			responseMessage.setMessage(arcGisResult);
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;
	}
	
	public static ArcGISBestSequesnceListDto getRoadDistance_BestRoute(List<Coordinates> coordList,String URLPayload) {

		ArcGISBestSequesnceListDto responseDto =new ArcGISBestSequesnceListDto();
		ArcGISBestSequesnceDto bestSequeseDto=null;
		String coordinatesString = "";

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		for (Coordinates coord : coordList) {
			coordinatesString += coord.getLongitude() + "," + coord.getLatitude() + ";";
		}

		String serviceURL = MurphyConstant.ARCGIS_SERVICE_URL + coordinatesString
				+ URLPayload;
		logger.error("[ArcGISUtil][getRoadDistance_BestRoute][Argis URL] : " +serviceURL );
		try{
		String arcGisResult = RestUtil.callRestURL(MurphyConstant.ARCGIS_API_HOST+serviceURL, null, MurphyConstant.HTTP_METHOD_GET, true).toString();
//		 arcGisResult = DestinationUtil.executeWithDest(MurphyConstant.ARCGIS_DEST_NAME, serviceURL,
//				MurphyConstant.HTTP_METHOD_GET, MurphyConstant.APPLICATION_JSON, "", "",
//				MurphyConstant.ON_PREMISE_PROXY, false);
		if(!ServicesUtil.isEmpty(arcGisResult)){
		if (arcGisResult.contains("stops") && arcGisResult.contains("routes")) {
			JSONObject arcGisObject = new JSONObject(arcGisResult);
			JSONObject routes = arcGisObject.getJSONObject("routes");
			JSONArray routesFeature=routes.getJSONArray("features");
			JSONObject cumulativeFeature=routesFeature.getJSONObject(0);
			JSONObject cumulativeAttribute=cumulativeFeature.getJSONObject("attributes");
			responseDto.setCumulativeDriveTime(cumulativeAttribute.getDouble("Total_Time"));
			JSONObject stops = arcGisObject.getJSONObject("stops");
			JSONArray features = stops.getJSONArray("features");
			for(int i=0;i<features.length();i++){
				JSONObject feature=features.getJSONObject(i);
				JSONObject attributes=feature.getJSONObject("attributes");
				bestSequeseDto=new ArcGISBestSequesnceDto(attributes.getInt("ObjectID"),attributes.getInt("Sequence"),attributes.getDouble("Cumul_Time"));
				responseDto.getBestSequenceList().add(bestSequeseDto);
			}
		} 
		else if (arcGisResult.contains("error")) {
			responseMessage.setMessage(arcGisResult);
		}
		responseMessage.setStatus(MurphyConstant.SUCCESS);
		responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		responseMessage.setMessage("ArcGIS Fetch Success");
		}
		else{
			logger.error("Error while Calling Carl's API null is returned");
		}
		}
		catch (Exception e) {
			logger.error("Error while Calling Carl's API in getRoadDistance_BestRoute  Exception "+e.getMessage());
			throw e;
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;
	}
	
	public void mailDriveTimeOutliers() {
		MailAlertUtil mail = new MailAlertUtil();
		List<ObxTaskDto> updateDriveTimelist = new ArrayList<>();
		String latLng = "For the following set of locations, the drive time is more than 120 minutes:</br></br>";
		ObxTaskDto prev = null;
		boolean mailFlag = false;
		try {
			List<ObxTaskDto> listofOBXAllo = obxSchedulerFacadeLocal.getAllOBXAllo();

			for (ObxTaskDto curr : listofOBXAllo) {

				if (curr.getDriveTime() > 120.0) { // driveTime greater than
													// 120mins
					logger.error("[Murphy][ArcGISUtil][mailDriveTimeOutliers] Mail to Carl "+curr.toString());
					mailFlag = true;
					if (!ServicesUtil.isEmpty(prev) && curr.getDay() == prev.getDay()
							&& curr.getClusterNumber() == prev.getClusterNumber()
							&& curr.getRole().equals(prev.getRole())) {
						latLng += "Location 1:  lat = " + prev.getLatitude() + " long = " + prev.getLongitude()
								+ "</br> Location 2: lat = " + curr.getLatitude() + " long = " + curr.getLongitude()
								+ "</br>" + "Drive Time:" + Math.floor(curr.getDriveTime()) + " minutes </br></br>";
					} else {
						String centralFacility = null;
						if (curr.getRole().equals("OBX_CATARINA"))
							centralFacility = MurphyConstant.CENTRAL_FACILITY_CATARINA;
						else if (curr.getRole().equals("OBX_KARNES"))
							centralFacility = MurphyConstant.CENTRAL_FACILITY_KARNES;
						else
							centralFacility = MurphyConstant.CENTRAL_FACILITY_TILDEN;

						Coordinates prevLocation = hierarchyDao.getCoordByCode(centralFacility);

						latLng += "Location 1:  lat = " + prevLocation.getLatitude() + " long = "
								+ prevLocation.getLongitude() + "</br> Location 2: lat = " + curr.getLatitude()
								+ " long = " + curr.getLongitude() + "</br>" + "Drive Time:"
								+ Math.floor(curr.getDriveTime()) + " minutes </br></br>";
					}
					curr.setDriveTime(120.0);
					updateDriveTimelist.add(curr);
				}
				prev = curr;
			}
			// sending mail only if there is a obx task whose drive time greater
			// than 120
			if (mailFlag) {
				List<EmailNotificationMasterDo> emailList = emailNotificationMasterDao.getEmailByConfigItemAndRecpType(MurphyConstant.DRIVE_TIME,"TO");
				String toEmail = null;
				String ccEmail = null;
				if(!ServicesUtil.isEmpty(emailList))
				toEmail = emailList.stream().map(EmailNotificationMasterDo::getConfigValue).collect(Collectors.joining(","));
				emailList = emailNotificationMasterDao.getEmailByConfigItemAndRecpType(MurphyConstant.DRIVE_TIME,"CC");
				if(!ServicesUtil.isEmpty(emailList))
				ccEmail = emailList.stream().map(EmailNotificationMasterDo::getConfigValue).collect(Collectors.joining(","));
				//carl_sunderman@murphyoilcorp.com,SAPOps_Shared_AS_Digital_Apps@murphyoilcorp.com,Sergio_Rivera@murphyoilcorp.com
				if(!ServicesUtil.isEmpty(toEmail) && !ServicesUtil.isEmpty(ccEmail))
				mail.sendMailWithCC(toEmail,
						ccEmail, "IOP - Drive time coming more than expected",
						latLng + "Could you please review and suggest.</br></br> Regards,</br>Murphy SAP Cloud Platform Support Team</br>",
						"Carl");
				obxSchedulerFacadeLocal.setDriveTime(updateDriveTimelist);
			}
		} catch (Exception e) {
			logger.error("[Murphy][ArcGISUtil][mailDriveTimeOutliers]Exception" + e);
		}

	}
	
	public static void main(String[] args) {
		System.out.println(ArcGISUtil.getRoadDistance(new Coordinates(28.4644, -99.0236), new Coordinates(28.4758, -98.6424)));
	}
}
