/**
 * 
 */
package com.murphy.taskmgmt.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.integration.util.ApplicationConstant;
import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dao.SsdBypassActivityLogDao;
import com.murphy.taskmgmt.dao.SsdBypassAttachmentDao;
import com.murphy.taskmgmt.dao.SsdBypassCommentDao;
import com.murphy.taskmgmt.dao.SsdBypassHeaderDao;
import com.murphy.taskmgmt.dto.BypassRiskLevelDto;
import com.murphy.taskmgmt.dto.DocServiceDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.RiskListResponseDto;
import com.murphy.taskmgmt.dto.SsdBypassActivityLogDto;
import com.murphy.taskmgmt.dto.SsdBypassAttachementDto;
import com.murphy.taskmgmt.dto.SsdBypassCommentDto;
import com.murphy.taskmgmt.dto.SsdBypassHeaderDto;
import com.murphy.taskmgmt.dto.SsdBypassListDto;
import com.murphy.taskmgmt.dto.SsdBypassLogListResponseDto;
import com.murphy.taskmgmt.dto.SsdBypassLogResponseDto;
import com.murphy.taskmgmt.service.interfaces.SsdBypassLogFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;
import com.murphy.taskmgmt.util.UniqueSeq;
import com.murphy.taskmgmt.websocket.FracMessage;
import com.murphy.taskmgmt.websocket.FracServiceEndPoint;

/**
 * @author Kamlesh.Choubey
 *
 */

@Service("byPassLogFacade")
public class SsdByPassLogFacade implements SsdBypassLogFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(SsdByPassLogFacade.class);

	@Autowired
	SsdBypassHeaderDao sdBypassHeaderDao;

	@Autowired
	SsdBypassCommentDao ssdBypassCommentDao;

	@Autowired
	SsdBypassAttachmentDao ssdBypassAttachmentDao;

	@Autowired
	SsdBypassActivityLogDao ssdBypassActivityLogDao;

	@Autowired
	HierarchyDao locDao;

	@Override
	public String test() {
		String s = "Failure";
		try {
			//List<String> operatorIds = bypassLogListForEscalation(12);
			// pushBypassLogNotification(List<String> operatorIds, String alert,
			// JSONObject dataJson);
			s = "success";
		} catch (Exception e) {
			System.err.println("inside test catch");

		}
		return s;
	}

	@Override
	public ResponseMessage createBypassLog(SsdBypassLogResponseDto dto) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.STATUS + " " + MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {

			List<SsdBypassAttachementDto> ssdBypassAttachementDtoList = new ArrayList<>();
			Map<String, String> docFileUrlDetails = new HashMap<>();
			SsdBypassHeaderDto ssdBypassHeaderDto = dto.getSsdBypassLogHeaderdto();
			String ssdBypassId = UUID.randomUUID().toString().replaceAll("-", "");
			String activityLogId = UUID.randomUUID().toString().replaceAll("-", "");
			ssdBypassHeaderDto.setSsdBypassId(ssdBypassId);
			String device = ssdBypassHeaderDto.getDeviceBypassed();
			String location = locDao.getLocationByLocCode(ssdBypassHeaderDto.getLocationCode());
			String bypassNum = String.valueOf(UniqueSeq.getInstance().getNext());
			ssdBypassHeaderDto.setSsdBypassNum(bypassNum);
			ssdBypassHeaderDto.setBypassStartTime(new Date());
			ssdBypassHeaderDto.setShiftChangeAcknowledged(true);
			Map<String, String> foremanDetails = sdBypassHeaderDao
					.getManagerDetails(ssdBypassHeaderDto.getOperatorId() , "foreman");
			Map<String, String> superintendentDetails = sdBypassHeaderDao
					.getManagerDetails(ssdBypassHeaderDto.getOperatorId() , "superintendent");
			String foremanName = "";
			String superintendentName=""; 
			for (Map.Entry<String, String> me : foremanDetails.entrySet()) {
				foremanName = me.getValue();
			}
			
			for (Map.Entry<String, String> me : superintendentDetails.entrySet()) {
				superintendentName = me.getValue();
			}
			logger.error("Bypass dto values operator_id : " + ssdBypassHeaderDto.getOperatorId() + "foreman : " + foremanName 
					+ "superintendent : "+ superintendentName + "equipments : "+ ssdBypassHeaderDto.getEquipmentDesc());
			ssdBypassHeaderDto.setFirstLineSupvApprovalInitBy(foremanName);
			ssdBypassHeaderDto.setFirstLineSupvApprovalStatus("pending");
			ssdBypassHeaderDto.setFieldPlantSuptApprovalInitBy(superintendentName);
			ssdBypassHeaderDto.setFieldPlantSuptApprovalStatus("pending");
			sdBypassHeaderDao.createBypassLog(ssdBypassHeaderDto);

			List<String> iocIdList = new ArrayList<>();
			List<String> locationCodeList = new ArrayList<>();
			locationCodeList.add(ssdBypassHeaderDto.getLocationCode());
			
			if (!ServicesUtil.isEmpty(activityLogId)) {
				SsdBypassActivityLogDto ssdBypassActivityLogDto = new SsdBypassActivityLogDto();
				ssdBypassActivityLogDto.setSsdBypassLogId(activityLogId);
				ssdBypassActivityLogDto.setSsdBypassId(ssdBypassId);
				ssdBypassActivityLogDto.setPersonResponsible(ssdBypassHeaderDto.getBypassStartedBy());
				ssdBypassActivityLogDto.setBypassStatusReviewedAt(new Date());
				ssdBypassActivityLogDto.setIsApprovalObtained(true);
				ssdBypassActivityLogDto.setOperatorType("operator");
				ssdBypassActivityLogDto.setPersonId(ssdBypassHeaderDto.getOperatorId());
				ssdBypassActivityLogDto.setActivityType("ASSIGNED");
				ssdBypassActivityLogDao.createBypassActivityLog(ssdBypassActivityLogDto);
				
			}

			List<String> locationCodes = new ArrayList<>();
			locationCodes.add(ssdBypassHeaderDto.getLocationCode());
			String userGroup = sdBypassHeaderDao.getLocationByLocationCode(locationCodes);
			if ("Catarina".equalsIgnoreCase(userGroup.trim())) {
				userGroup = "IOP_TM_ROC_Catarina";
			} else if ("Tilden Central".equalsIgnoreCase(userGroup.trim())
					|| "Tilden North".equalsIgnoreCase(userGroup.trim())
					|| "Tilden East".equalsIgnoreCase(userGroup.trim())) {

				userGroup = "IOP_TM_ROC_CentralTilden";
			} else if ("Tilden West".equalsIgnoreCase(userGroup.trim())) {

				userGroup = "IOP_TM_ROC_WestTilden";
			} else if ("Karnes North".equalsIgnoreCase(userGroup.trim())
					|| "Karnes South".equalsIgnoreCase(userGroup.trim())) {
				userGroup = "IOP_TM_ROC_Karnes";
			}
			else if ("Montney".equalsIgnoreCase(userGroup.trim())){
				userGroup = "IOP_TM_ROC_Montney";
			}
			else if("Kaybob".equalsIgnoreCase(userGroup.trim())){
				userGroup = "IOP_TM_ROC_Kaybob";
			}
			// Check for severity(EFS)/risk(Canada) 
			String severity = null;
			if(!ServicesUtil.isEmpty(ssdBypassHeaderDto.getSeverity()))
				severity = ssdBypassHeaderDto.getSeverity();
			else if(!ServicesUtil.isEmpty(ssdBypassHeaderDto.getRisk()))
				severity = ssdBypassHeaderDto.getRisk();

			if (!ServicesUtil.isEmpty(severity)) {
				if (severity.equalsIgnoreCase("critical") || severity.equalsIgnoreCase("HIGH")) {
					SsdBypassActivityLogDto ssdBypassActivityLogDto = new SsdBypassActivityLogDto();
					String activityId = UUID.randomUUID().toString().replaceAll("-", "");
					List<String> operatorIdList = new ArrayList<>();
					/*Map<String, String> managerDetails = sdBypassHeaderDao
							.getManagerDetails(ssdBypassHeaderDto.getOperatorId());*/
					for (Map.Entry<String, String> me : foremanDetails.entrySet()) {
						operatorIdList.add(me.getKey());
						ssdBypassActivityLogDto.setPersonId(me.getKey());
						ssdBypassActivityLogDto.setPersonResponsible(me.getValue());
					}

					// push notification to foreman
					
					String alertForeman = "Bypass Log " + Long.parseLong(bypassNum) % 1000000
							+ " at "+location+" has been created. Critical Equipment selected approval required.";
					String dataJsonForman = createDataForPushNotification(ssdBypassHeaderDto.getSsdBypassId(),
							activityId, "BYPASSLOG_ESCALATION_ACTION");
					
					/*sdBypassHeaderDao.updateEscalationInfoInBypassHeader(ssdBypassHeaderDto.getSsdBypassId(), "pending",
							ssdBypassActivityLogDto.getPersonResponsible(), "first");*/
					pushBypassLogNotification(operatorIdList, alertForeman, dataJsonForman);
					// push notification to roc
					sdBypassHeaderDao.insertDataForPushNotificationToRoc(ssdBypassHeaderDto.getSsdBypassId(),
							activityLogId, "BypassLog", location, "false", userGroup,
							ssdBypassHeaderDto.getOperatorId(), "Created", ssdBypassHeaderDto.getLocationCode(),
							severity, bypassNum);

					// another entry of escalation in activity log for critical bypass

					ssdBypassActivityLogDto.setSsdBypassLogId(activityId);
					ssdBypassActivityLogDto.setSsdBypassId(ssdBypassId);

					ssdBypassActivityLogDto.setBypassStatusReviewedAt(new Date());
					//ssdBypassActivityLogDto.setIsApprovalObtained(false);
					ssdBypassActivityLogDto.setOperatorType("foreman");

					ssdBypassActivityLogDto.setActivityType("escalation");
					ssdBypassActivityLogDao.createBypassActivityLog(ssdBypassActivityLogDto);

				} else {
					
					sdBypassHeaderDao.insertDataForPushNotificationToRoc(ssdBypassHeaderDto.getSsdBypassId(),
							activityLogId, "BypassLog", location, "false", userGroup,
							ssdBypassHeaderDto.getOperatorId(), "Created", ssdBypassHeaderDto.getLocationCode(),
							severity, String.valueOf(Long.parseLong(bypassNum) % 1000000));
				}
			}

			if (!ServicesUtil.isEmpty(dto)) {
				if (!ServicesUtil.isEmpty(dto.getSsdBypassAttachementDtoList())) {
					docFileUrlDetails = updateAttachmentToDocServices(dto);
					for (SsdBypassAttachementDto attachDto : dto.getSsdBypassAttachementDtoList()) {
						SsdBypassAttachementDto ssdBypassAttachementDto = new SsdBypassAttachementDto();
						ssdBypassAttachementDto = attachDto;
						ssdBypassAttachementDto.setAttachmentUrl(docFileUrlDetails.get(attachDto.getFileName()));
						ssdBypassAttachementDto.setBypassId(ssdBypassId);
						ssdBypassAttachmentDao.createBypassLogAttachment(ssdBypassAttachementDto);

					}

				}

				if (!ServicesUtil.isEmpty(dto.getSsdBypassCommentDtoList())) {
					for (SsdBypassCommentDto ssdBypassCommentDto : dto.getSsdBypassCommentDtoList()) {
						ssdBypassCommentDto.setSsdBypassId(ssdBypassId);
						ssdBypassCommentDao.createBypassLogComment(ssdBypassCommentDto);
					}

				}

				System.err.println(dto.toString());

			}

			responseMessage.setMessage("Bypass Log " + Long.parseLong(bypassNum) % 1000000 + " created successfully");
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);

		} catch (

		Exception e) {
			logger.error("[Murphy][SsdByPassLogFacade][createBypassLog][error]" + e.getMessage());
			e.printStackTrace();
		}

		return responseMessage;
	}

	@Override
	public SsdBypassLogResponseDto getBypassLogById(String bypassId) {
		SsdBypassLogResponseDto ssdBypassLogResponseDto = new SsdBypassLogResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		ssdBypassLogResponseDto.setResponseMessage(responseMessage);
		try {
			ssdBypassLogResponseDto = sdBypassHeaderDao.getBypassLogById(bypassId);
			responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			ssdBypassLogResponseDto.setResponseMessage(responseMessage);
		} catch (

		Exception e) {
			logger.error("[Murphy][SsdByPassLogFacade][getBypassLogById][error]" + e.getMessage());
		}

		return ssdBypassLogResponseDto;
	}

	@Override
	public ResponseMessage updateBypassLog(SsdBypassLogResponseDto dto) {
		List<SsdBypassAttachementDto> ssdBypassAttachementDtoList = new ArrayList<>();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		String bypassNum = "";
		Map<String, String> docFileUrlDetails = new HashMap<>();
		try {
			String ssdBypassId = dto.getSsdBypassLogHeaderdto().getSsdBypassId();
			if (!ServicesUtil.isEmpty(dto)) {
				if (!ServicesUtil.isEmpty(dto.getSsdBypassAttachementDtoList())) {

					docFileUrlDetails = updateAttachmentToDocServices(dto);

					for (SsdBypassAttachementDto attachDto : dto.getSsdBypassAttachementDtoList()) {
						SsdBypassAttachementDto ssdBypassAttachementDto = new SsdBypassAttachementDto();
						ssdBypassAttachementDto = attachDto;
						ssdBypassAttachementDto.setAttachmentUrl(docFileUrlDetails.get(attachDto.getFileName()));
						ssdBypassAttachementDto.setBypassId(ssdBypassId);
						ssdBypassAttachmentDao.createBypassLogAttachment(ssdBypassAttachementDto);

					}

				}
				if (!ServicesUtil.isEmpty(dto.getSsdBypassCommentDtoList())) {
					for (SsdBypassCommentDto ssdBypassCommentDto : dto.getSsdBypassCommentDtoList()) {
						ssdBypassCommentDto.setSsdBypassId(ssdBypassId);
						ssdBypassCommentDao.createBypassLogComment(ssdBypassCommentDto);
					}

				}
				SsdBypassHeaderDto ssdBypassHeaderDto = dto.getSsdBypassLogHeaderdto();

				if (!ServicesUtil.isEmpty(ssdBypassHeaderDto)) {
					bypassNum = sdBypassHeaderDao.getBypassField(ssdBypassId, "SSD_BYPASS_NUMBER");
					if (!ServicesUtil.isEmpty(bypassNum)) {
						// bypassNum =
						// String.valueOf(Long.parseLong(sdBypassHeaderDao.getBypassNum(ssdBypassId)));
						ssdBypassHeaderDto.setSsdBypassNum(bypassNum);
					}

					sdBypassHeaderDao.updateBypassLog(dto.getSsdBypassLogHeaderdto());
				}
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			if (dto.getSsdBypassLogHeaderdto().getBypassStatus().equalsIgnoreCase("closed")) {
				List<String> approversIdList = sdBypassHeaderDao.getApproversList(ssdBypassId);
				// for(String approverId:approversIdList){
				String alert = "Bypass Log " + Long.parseLong(bypassNum) % 1000000 + " has been closed.";
				String dataJsonForman = createDataForPushNotification(ssdBypassId, "", "BYPASSLOG_REMINDER");
				System.err.println("Bypass Log closed: " + dataJsonForman);
				pushBypassLogNotification(approversIdList, alert, dataJsonForman);
				// }
				responseMessage
						.setMessage("Bypass Log " + Long.parseLong(bypassNum) % 1000000 + " closed successfully");
			} else {
				responseMessage
						.setMessage("Bypass Log " + Long.parseLong(bypassNum) % 1000000 + " updated successfully");
			}

		} catch (Exception e) {
			logger.error("[Murphy][SsdByPassLogFacade][updateBypassLog][error]" + e.getMessage());
		}
		return responseMessage;
	} 

	@Override
	public SsdBypassLogListResponseDto getBypassLogList(String location, int timePeriod, int pageNo, int pageSize,
			String bypassLogStatus , String locationType,boolean isActive) {
		// System.err.println("inside getBypassLogList" + location);
		SsdBypassLogListResponseDto ssdBypassLogListResponseDto = new SsdBypassLogListResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		ssdBypassLogListResponseDto.setResponseMessage(responseMessage);
		List<Map<String, Object>> statusList = new ArrayList<>();
		Map<String, Object> status = null;
		try {
			Map<List<SsdBypassListDto>, Map<String, Integer>> bypassLogMap = sdBypassHeaderDao
					.getBypassLogList(location, timePeriod, pageNo, pageSize, bypassLogStatus , locationType,isActive);

			if (!ServicesUtil.isEmpty(bypassLogMap)) {
				Iterator iterator = bypassLogMap.entrySet().iterator();
				List<SsdBypassListDto> ssdBypassListDto = null;
				Map<String, Integer> statusMap = null;
				int count = 0;
				while (iterator.hasNext()) {
					Map.Entry<List<SsdBypassListDto>, Map<String, Integer>> entry = (Map.Entry<List<SsdBypassListDto>, Map<String, Integer>>) iterator
							.next();
					ssdBypassListDto = entry.getKey();
					// count = entry.getValue();
					statusMap = entry.getValue();

					for (Map.Entry<String, Integer> me : statusMap.entrySet()) {
						// count = count + me.getValue();
						if (me.getKey().equalsIgnoreCase("totalCount")) {
							count = me.getValue();
						} else {
							status = new LinkedHashMap<String, Object>();
							status.put("status", me.getKey());
							status.put("count", me.getValue());
							statusList.add(status);
						}

					}

				}
				ssdBypassLogListResponseDto.setStatusCountList(statusList);
				ssdBypassLogListResponseDto.setSsdBypassListDto(ssdBypassListDto);
				ssdBypassLogListResponseDto.setTotalCount(count);

			}
			responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			ssdBypassLogListResponseDto.setResponseMessage(responseMessage);

		} catch (Exception e) {
			logger.error("[Murphy][SsdByPassLogFacade][getBypassLogList][error]" + e.getMessage());
		}
		return ssdBypassLogListResponseDto;
	}

	private JSONArray createJsonArrayOfDocServices(ArrayList<DocServiceDto> ar) {
		JSONArray aProjects = new JSONArray();
		try {
			for (int i = 0; i < ar.size(); i++) {
				JSONObject requestDto = new JSONObject();
				requestDto.put("folderName", MurphyConstant.BYPASS_ATTACHMENT_FOLDER_NAME);
				requestDto.put("fileType", ar.get(i).getFileType());
				requestDto.put("fileName", ar.get(i).getFileName());
				requestDto.put("fileContent", ar.get(i).getFileContent());
				aProjects.put(requestDto);
			}
		} catch (Exception e) {
			logger.error("Exception while creating Json Array in bypass log" + e.getMessage());
		}
		return aProjects;

	}

	private Map<String, String> updateAttachmentToDocServices(SsdBypassLogResponseDto dto) {
		int i = 0;
		String fileName = "";
		String dstName = "";
		String fileType = "";
		String fileContent = "";
		Map<String, String> mapDocumentDetails = null;
		try {

			System.err.println("inside updateAttachmentToDocServices:" + dto);
			if (!ServicesUtil.isEmpty(dto) && !ServicesUtil.isEmpty(dto.getSsdBypassAttachementDtoList())) {
				List<SsdBypassAttachementDto> attachmentDto = dto.getSsdBypassAttachementDtoList();
				DocServiceDto docServiceDto = new DocServiceDto();
				ArrayList<DocServiceDto> serviceDto = new ArrayList<DocServiceDto>();
				for (SsdBypassAttachementDto attachment : attachmentDto) {

					dstName = attachment.getFileName();

					fileType = attachment.getFileType();
					fileContent = attachment.getFileDoc();
					docServiceDto = createDocServiceDto(dstName, fileType, fileContent);
					serviceDto.add(docServiceDto);

				}
				mapDocumentDetails = updateToDocServices(serviceDto);
			} else {
				logger.error("[SsdByPassLogFacade][updateAttachmentToDocServices][INFO]:Null input found");
			}
		} catch (Exception e) {
			logger.error("[SsdByPassLogFacade][updateAttachmentToDocServices][ERROR]" + e.getMessage());
		}
		return mapDocumentDetails;
	}

	private Map<String, String> updateToDocServices(ArrayList<DocServiceDto> docList) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Failure While Updating to DocumentServices");
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		Map<String, String> mapFileWithUrl = new HashMap<String, String>();
		JSONArray jsonArray = createJsonArrayOfDocServices(docList);
		RestUtil restClass = new RestUtil();
		HttpEntity responseEntity;
		String responseBody = null;
		int statusCode = 0;
		JSONArray responseJson = null;

		String jsonObject = "{" + "\"documentDetailDtoList\": " + jsonArray + "}";
		// System.err.println("JsonObject"+jsonObject);
		try {
			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			HttpResponse response = restClass.callDocService(ApplicationConstant.DOCUMENTSERVICES, jsonObject);
			responseEntity = response.getEntity();
			if (responseEntity != null) {
				responseBody = EntityUtils.toString(responseEntity);
			}
			statusCode = response.getStatusLine().getStatusCode();
			logger.error("[Murphy][SsdByPassLogFacade][updateAttachmentToDocServices][info]" + statusCode
					+ "ResponseBody" + responseBody);
			responseJson = new JSONArray(responseBody);
			for (int i = 0; i < responseJson.length(); i++) {
				JSONObject jsonobject = responseJson.getJSONObject(i);
				JSONObject innerJsonObj = jsonobject.getJSONObject("documentDetailDto");
				if (innerJsonObj != null) {
					String documentUrl = innerJsonObj.getString("documentUrl");
					String documentName = innerJsonObj.getString("fileName");
					mapFileWithUrl.put(documentName, documentUrl);
				}

			}

			logger.error("[updateToDocServices] ResponseStatus " + statusCode);

		} catch (Exception e) {
			logger.error("[SsdByPassLogFacade][updateToDocServices][ERROR]" + e.getMessage());
		}
		return mapFileWithUrl;

	}

	private DocServiceDto createDocServiceDto(String fileName, String fileType, String fileContent) {
		DocServiceDto docDto = new DocServiceDto();
		try {
			docDto.setFileName(fileName);
			docDto.setFileType(fileType);
			docDto.setFileContent(fileContent);
		} catch (Exception e) {
			logger.error("Exception While create DocService Dto , bypass log" + e.getMessage());
		}
		return docDto;
	}

	/*@Override
	public List<String> bypassLogListForEscalation(int hours) {
		
		 * This list will contain all the ids to push the notification for
		 * escalation
		 
		List<String> listOfIds = new ArrayList<>();
		try {
			Map<String, String> operatorList = sdBypassHeaderDao.getOperatorsIdForNotification(hours);
			Iterator<Map.Entry<String, String>> iterator = operatorList.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				Map<String, String> managerDetails = sdBypassHeaderDao.getManagerDetails(entry.getValue());
				for (Map.Entry<String, String> me : managerDetails.entrySet()) {
					listOfIds.add(me.getKey());
				}
				// listOfIds.add(sdBypassHeaderDao.getManagerId(entry.getValue()));

			}

		} catch (Exception e) {
			logger.error("[SsdByPassLogFacade][bypassLogListForEscalation][ERROR]" + e.getMessage());
		}
		return listOfIds;
	}*/

	@Override
	public void pushBypassLogNotification(List<String> operatorIds, String alert, String dataJson) {
		try {
			JSONObject rootJson = new JSONObject();
			JSONObject notificationJson = new JSONObject();
			notificationJson.put("alert", alert);
			if (!ServicesUtil.isEmpty(dataJson)) {
				notificationJson.put("data", dataJson.toString());
			}

			notificationJson.put("badge", 1);
			notificationJson.put("sound", "iphone_alarm.caf");
			if(dataJson.contains("ARVIDEO_CALLING")){
				notificationJson.put("sound", "iphone_alarm_remote.caf");
			}
			rootJson.put("notification", notificationJson);
			rootJson.put("users", operatorIds);
			// String url =
			// "https://mobile-d7e367960.us2.hana.ondemand.com/restnotification/application/com.murphy.hse/user/";

			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			JSONObject jsonObject = RestUtil.callRest(MurphyConstant.PUSH_NOTIFICATION_URL, rootJson.toString(), "POST",
					MurphyConstant.PUSH_NOTIFICATION_USERNAME, MurphyConstant.PUSH_NOTIFICATION_PASSWORD);
			System.err.println("inside try " + jsonObject);
			logger.info("Response onject:" + jsonObject);
		} catch (Exception e) {
			logger.error("[Murphy][SsdByPassLogFacade][pushBypassLogEsclsnNotification][error]" + e.getMessage());

		}

	}

	/* to send push notification for escalation(1st & 2nd level) */
	@Override
	public ResponseMessage sendNotificationForEscalation(int hours ,String notificationTime,String zone) {
		System.err.println(hours);
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		String alert = "";
		String bypassId = "";
		String escalationLevel = "";
		String activityLogId = UUID.randomUUID().toString().replaceAll("-", "");
		try {
			SsdBypassActivityLogDto ssdBypassActivityLogDto = new SsdBypassActivityLogDto();

			if (hours == 12 || hours == 24) {
				logger.error("[sendNotificationForEscalation][hours] : "+ hours + " [notificationTime] : " + notificationTime);
				Map<String, String> operatorList = null;
				if (!ServicesUtil.isEmpty(notificationTime)) {
					operatorList = sdBypassHeaderDao.outShiftNotificationOperatorIds(notificationTime , hours,zone);
				}else{
					operatorList = sdBypassHeaderDao.getOperatorsIdForNotification(hours);
				}
				 
				 
				Iterator<Map.Entry<String, String>> iterator = operatorList.entrySet().iterator();

				List<String> operatorIds = new ArrayList<>();
				while (iterator.hasNext()) {
					Map.Entry<String, String> entry = iterator.next();
					bypassId = entry.getKey();
					SsdBypassHeaderDto ssdBypassHeaderDto = sdBypassHeaderDao.getBypassLogById(bypassId).getSsdBypassLogHeaderdto();
					activityLogId = UUID.randomUUID().toString().replaceAll("-", "");
					
					// operatorIds.add(sdBypassHeaderDao.getManagerId(entry.getValue()));
					ssdBypassActivityLogDto.setSsdBypassId(entry.getKey());
					ssdBypassActivityLogDto.setBypassStatusReviewedAt(new Date());
					if (hours == 12) {
						ssdBypassActivityLogDto.setOperatorType("foreman");
						String equipment = null, bypassStartTime = null;
						// Changing start time according to EFS/Canada
						if(!ServicesUtil.isEmpty(ssdBypassHeaderDto.getRisk())){
							bypassStartTime = ServicesUtil.convertFromZoneToZoneString(null, ssdBypassHeaderDto.getBypassStartTime().toString().substring(0, 19),MurphyConstant.UTC_ZONE, "MST" ,
									 "yyyy-MM-dd hh:mm:ss","yyyy-MM-dd hh:mm:ss");
						}
						else{
							bypassStartTime = ServicesUtil.convertFromZoneToZoneString(null, ssdBypassHeaderDto.getBypassStartTime().toString().substring(0, 19),MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE ,
									 "yyyy-MM-dd hh:mm:ss","yyyy-MM-dd hh:mm:ss");
						}
						// Setting device according to EFS/Canada
						if(!ServicesUtil.isEmpty(ssdBypassHeaderDto.getDeviceBypassed())){
							equipment = ssdBypassHeaderDto.getDeviceBypassed();
						}	
						else if(!ServicesUtil.isEmpty(ssdBypassHeaderDto.getEquipmentDesc())){
							equipment = ssdBypassHeaderDto.getEquipmentDesc();
						}
						    
						alert = "Equipment " + equipment
								+ " has been in Bypass since "+hours+" hours. Your approval is needed for it to continue in this state. If Rejected, this will close-out the form and equipment will be brought back in service by responsible operator.";
						escalationLevel = "first";
						Map<String, String> managerDetails = sdBypassHeaderDao.getManagerDetails(entry.getValue() , "foreman");
						for (Map.Entry<String, String> me : managerDetails.entrySet()) {
							operatorIds.add(me.getKey());
							ssdBypassActivityLogDto.setPersonId(me.getKey());
							ssdBypassActivityLogDto.setPersonResponsible(me.getValue());
						}
					}

					if (hours == 24) {
						ssdBypassActivityLogDto.setOperatorType("superintendent");
                        String equipment = null, bypassStartTime = null;
						// Changing start time according to EFS/Canada
                        if(!ServicesUtil.isEmpty(ssdBypassHeaderDto.getRisk())){
							bypassStartTime = ServicesUtil.convertFromZoneToZoneString(null, ssdBypassHeaderDto.getBypassStartTime().toString().substring(0, 19),MurphyConstant.UTC_ZONE, "MST" ,
									 "yyyy-MM-dd hh:mm:ss","yyyy-MM-dd hh:mm:ss");
						}
						else{
							bypassStartTime = ServicesUtil.convertFromZoneToZoneString(null, ssdBypassHeaderDto.getBypassStartTime().toString().substring(0, 19),MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE ,
									 "yyyy-MM-dd hh:mm:ss","yyyy-MM-dd hh:mm:ss");
						}
                        // Setting device according to EFS/Canada
						if(!ServicesUtil.isEmpty(ssdBypassHeaderDto.getDeviceBypassed())){
							equipment = ssdBypassHeaderDto.getDeviceBypassed();
						}	
						else if(!ServicesUtil.isEmpty(ssdBypassHeaderDto.getEquipmentDesc())){
							equipment = ssdBypassHeaderDto.getEquipmentDesc();
						}

						alert = "Equipment " + equipment
								+ " has been in Bypass since "+hours+" hours. Your approval is needed for it to continue in this state. If Rejected, this will close-out the form and equipment will be brought back in service by responsible operator.";
						escalationLevel = "second";
						Map<String, String> managerDetails = sdBypassHeaderDao.getManagerDetails(entry.getValue() , "superintendent");
						for (Map.Entry<String, String> me : managerDetails.entrySet()) {
							operatorIds.add(me.getKey());
							ssdBypassActivityLogDto.setPersonId(me.getKey());
							ssdBypassActivityLogDto.setPersonResponsible(me.getValue());
						}
					}
					
					// List<String> operatorIds = bypassLogListForEscalation(hours);

					ssdBypassActivityLogDto.setSsdBypassLogId(activityLogId);
					ssdBypassActivityLogDto.setActivityType("escalation");
					// pushBypassLogNotification(operatorIds);

					String dataJson = createDataForPushNotification(bypassId, activityLogId, "BYPASSLOG_ESCALATION_ACTION");
					if (operatorIds.size() > 0) {
						ssdBypassActivityLogDao.createBypassActivityLog(ssdBypassActivityLogDto);
						pushBypassLogNotification(operatorIds, alert, dataJson);
						sdBypassHeaderDao.updateEscalationInfoInBypassHeader(bypassId, "pending",
								ssdBypassActivityLogDto.getPersonResponsible(), escalationLevel);

					}

				}

//				// List<String> operatorIds = bypassLogListForEscalation(hours);
//
//				ssdBypassActivityLogDto.setSsdBypassLogId(activityLogId);
//				ssdBypassActivityLogDto.setActivityType("escalation");
//				// pushBypassLogNotification(operatorIds);
//
//				String dataJson = createDataForPushNotification(bypassId, activityLogId, "BYPASSLOG_ESCALATION_ACTION");
//				if (operatorIds.size() > 0) {
//					ssdBypassActivityLogDao.createBypassActivityLog(ssdBypassActivityLogDto);
//					pushBypassLogNotification(operatorIds, alert, dataJson);
//					sdBypassHeaderDao.updateEscalationInfoInBypassHeader(bypassId, "pending",
//							ssdBypassActivityLogDto.getPersonResponsible(), escalationLevel);
//
//				}

			} else if (hours == 70) {
				Map<String, String> operatorList = sdBypassHeaderDao.getOperatorsIdForNotification(hours);
				Iterator<Map.Entry<String, String>> iterator = operatorList.entrySet().iterator();

				List<String> operatorIds = new ArrayList<>();
				while (iterator.hasNext()) {
					Map.Entry<String, String> entry = iterator.next();
					bypassId = entry.getKey();
					//operatorIds.add(entry.getValue());
					Map<String, String> managerDetails = sdBypassHeaderDao
							.getManagerDetails(entry.getValue() , "foreman");
					for (Map.Entry<String, String> me : managerDetails.entrySet()) {
						operatorIds.add(me.getKey());
						//formanId = me.getKey();
						/*ssdBypassActivityLogDto.setPersonId(me.getKey());
						ssdBypassActivityLogDto.setPersonResponsible(me.getValue());*/
					}
				}
				// String bypassNum = sdBypassHeaderDao.getBypassField(bypassId,
				// "SSD_BYPASS_NUMBER");
				String bypassNum = sdBypassHeaderDao.getBypassField(bypassId, "SSD_BYPASS_NUMBER");
				alert = "Please submit eMOC id for the Bypass Log " + Long.parseLong(bypassNum) % 1000000;
				String dataJson = createDataForPushNotification(bypassId, activityLogId, "BYPASSLOG_REMINDER");
				pushBypassLogNotification(operatorIds, alert, dataJson);

			} else {

				List<String> operatorIds = new ArrayList<>();
				Map<String, String> operatorList = sdBypassHeaderDao.getOperatorsIdForNotification(hours);
				Iterator<Map.Entry<String, String>> iterator = operatorList.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, String> entry = iterator.next();
					bypassId = entry.getKey();
					Map<String, String> managerDetails = sdBypassHeaderDao.getManagerDetails(entry.getValue() , "foreman");
					for (Map.Entry<String, String> me : managerDetails.entrySet()) {
						operatorIds.add(me.getKey());

					}
				}
				// pushBypassLogNotification(operatorIds);
				// String bypassNum = sdBypassHeaderDao.getBypassField(bypassId,
				// "SSD_BYPASS_NUMBER");
				if (hours == 10) {
					String bypassNum = sdBypassHeaderDao.getBypassField(bypassId, "SSD_BYPASS_NUMBER");
					alert = "Crtical Bypass Log " + Long.parseLong(bypassNum) % 1000000
							+ " has been in progress for 10 hours ";
				}
				if (hours == 22) {
					String bypassNum = sdBypassHeaderDao.getBypassField(bypassId, "SSD_BYPASS_NUMBER");
					alert = " Regular Bypass Log " + Long.parseLong(bypassNum) % 1000000
							+ " has been in progress for 22 hours ";
				}

				String dataJson = createDataForPushNotification(bypassId, activityLogId, "BYPASSLOG_REMINDER");
				pushBypassLogNotification(operatorIds, alert, dataJson);
			}

			responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][SsdByPassLogFacade][sendNotificationForEscalation][error]" + e.getMessage());

		}
		return responseMessage;

	}

	/* push notification to roc for shift change */

	@Override
	public ResponseMessage sendNotificationForShiftChange(String zone) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			String response = sdBypassHeaderDao.sendNotificationForShiftChange(zone);
			/*
			 * // pushBypassLogNotification(rocIds); String alert =
			 * "Please assign the active bypass logs to another user for the next shift"
			 * ; String dataJson = createDataForPushNotification(null, null,
			 * "BYPASSLOG_REMINDER"); pushBypassLogNotification(rocIds, alert,
			 * dataJson);
			 */
			if (response.equalsIgnoreCase("success")) {
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			}

		} catch (Exception e) {
			logger.error("[Murphy][SsdByPassLogFacade][sendNotificationForEscalation][error]" + e.getMessage());

		}
		return responseMessage;
	}

	// this method is used to create the activity log and check the response of
	// operator
	@Override
	public ResponseMessage createBypassActivityLog(SsdBypassActivityLogDto dto) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.STATUS + " " + MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			if (!ServicesUtil.isEmpty(dto)) {
				String activityLogId = UUID.randomUUID().toString().replaceAll("-", "");
				dto.setSsdBypassLogId(activityLogId);
				ssdBypassActivityLogDao.createBypassActivityLog(dto);
				if (!ServicesUtil.isEmpty(dto.getOperatorType())) {
					if (dto.getOperatorType().equalsIgnoreCase("operator")) {
						/*if(dto.getActivityType().equalsIgnoreCase("ASSIGNED")){
							
						}*/
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MINUTE, 15);
						List<String> operatorList = new ArrayList<>();
						operatorList.add(dto.getPersonId());
						String bypassId = dto.getSsdBypassId();
						SsdBypassHeaderDto ssdBypassHeaderDto = sdBypassHeaderDao.getBypassLogById(bypassId)
								.getSsdBypassLogHeaderdto();
						String location = "";
						String equipment = "";
						if (!ServicesUtil.isEmpty(ssdBypassHeaderDto.getLocation())) {
							location = ssdBypassHeaderDto.getLocation();
						}
						//For EFS 
						if (!ServicesUtil.isEmpty(ssdBypassHeaderDto.getDeviceBypassed())) {
							equipment = ssdBypassHeaderDto.getDeviceBypassed();
						}
						// For Canada
						if (!ServicesUtil.isEmpty(ssdBypassHeaderDto.getEquipmentDesc())) {
							equipment = ssdBypassHeaderDto.getEquipmentDesc();
						}
						String alert = "You have been assigned the Active Bypass at " + location + " located on the "
								+ equipment + ". Please Accept to keep equipment in Bypass.";

						String dataJson = createDataForPushNotification(bypassId, activityLogId,
								"BYPASSLOG_SHIFTCHANGE_ACTION");
						logger.error("sending notification to mobile for id : " +bypassId + " at time :  " + new Date());
						pushBypassLogNotification(operatorList, alert, dataJson);
						logger.error("timer started for bypass " +bypassId + " at time :  " + new Date());
						Timer timer = new Timer();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								if (ssdBypassActivityLogDao.checkOperatorResponse(activityLogId) == null) {
									List<String> foremanId = new ArrayList<>();
									// foremanId.add(sdBypassHeaderDao.getManagerId(dto.getPersonId()));
									Map<String, String> managerDetails = sdBypassHeaderDao
											.getManagerDetails(dto.getPersonId() , "foreman");
									for (Map.Entry<String, String> me : managerDetails.entrySet()) {
										foremanId.add(me.getKey());
									}
									// pushBypassLogNotification(foremanId);
									String alert = "Operator " + dto.getPersonId()
											+ " did not respond for the assigned task within specific time";
									String bypassId = dto.getSsdBypassId();
									String dataJson = createDataForPushNotification(bypassId, activityLogId,
											"BYPASSLOG_REMINDER");
									logger.error("sending auto reject notification to foreman mobile for id : " +bypassId + " at time :  " + new Date());
									pushBypassLogNotification(foremanId, alert, dataJson);

									// push notification to roc
									updateOperatorResponse(dto.getSsdBypassLogId(), false, dto.getOperatorType(),
											dto.getPersonId());

								}
							}
						}, cal.getTime());

					}
				}

			}
			responseMessage.setMessage(MurphyConstant.STATUS + " " + MurphyConstant.CREATED_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);

		} catch (Exception e) {
			logger.error("[Murphy][SsdByPassLogFacade][createBypassActivityLog][error]" + e.getMessage());

		}
		return responseMessage;
	}

	@Override
	public ResponseMessage updateOperatorResponse(String activityLogId, boolean responseValue, String opType,
			String opId) {
		int response = 0;
		String operatorType = "";
		String operatorId = "";
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		SsdBypassHeaderDto ssdBypassHeaderDto = new SsdBypassHeaderDto();
		try {
			response = sdBypassHeaderDao.updateOperatorResponse(activityLogId, responseValue);
			SsdBypassActivityLogDto ssdBypassActivityLogDto = new SsdBypassActivityLogDto();
			try {
				ssdBypassActivityLogDto = ssdBypassActivityLogDao.getActivityLogById(activityLogId);
				operatorType = ssdBypassActivityLogDto.getOperatorType();
				operatorId = ssdBypassActivityLogDto.getPersonId();
				String perssonResponsible = ssdBypassActivityLogDto.getPersonResponsible();
				//SsdBypassLogResponseDto ssdBypassLogResponseDto = getBypassLogById(ssdBypassActivityLogDto.getSsdBypassId());
				ssdBypassHeaderDto =  getBypassLogById(ssdBypassActivityLogDto.getSsdBypassId()).getSsdBypassLogHeaderdto();
				String bypassNum = ssdBypassHeaderDto.getSsdBypassNum();
				if (operatorType.equals("operator") && responseValue == false) {
					
					//String formanId = "";
					
					
					List<String> id = new ArrayList<>();
					Map<String, String> managerDetails = sdBypassHeaderDao
							.getManagerDetails(ssdBypassActivityLogDto.getPersonId() , "foreman");
					for (Map.Entry<String, String> me : managerDetails.entrySet()) {
						id.add(me.getKey());
						//formanId = me.getKey();
						/*ssdBypassActivityLogDto.setPersonId(me.getKey());
						ssdBypassActivityLogDto.setPersonResponsible(me.getValue());*/
					}
					//id.add(operatorId);
					String alert = "" + perssonResponsible + " has rejected the Bypass Log "+ Long.parseLong(bypassNum)%1000000;
					String dataJson = createDataForPushNotification(ssdBypassActivityLogDto.getSsdBypassId(),
							activityLogId, "BYPASSLOG_REMINDER");
					pushBypassLogNotification(id, alert, dataJson);
					 
							
					String location = ssdBypassHeaderDto.getLocation();
					String locationCode = ssdBypassHeaderDto.getLocationCode();
					List<String> locationCodes = new ArrayList<>();
					locationCodes.add(locationCode);
					String userGroup = sdBypassHeaderDao.getLocationByLocationCode(locationCodes);
					if ("Catarina".equalsIgnoreCase(userGroup.trim())) {
						userGroup = "IOP_TM_ROC_Catarina";
					} else if ("Tilden Central".equalsIgnoreCase(userGroup.trim())
							|| "Tilden North".equalsIgnoreCase(userGroup.trim())
							|| "Tilden East".equalsIgnoreCase(userGroup.trim())) {

						userGroup = "IOP_TM_ROC_CentralTilden";
					} else if ("Tilden West".equalsIgnoreCase(userGroup.trim())) {

						userGroup = "IOP_TM_ROC_WestTilden";
					} else if ("Karnes North".equalsIgnoreCase(userGroup.trim())
							|| "Karnes South".equalsIgnoreCase(userGroup.trim())) {
						userGroup = "IOP_TM_ROC_Karnes";
					}
					else if ("Montney".equalsIgnoreCase(userGroup.trim())){
						userGroup = "IOP_TM_ROC_Montney";
					}
					else if("Kaybob".equalsIgnoreCase(userGroup.trim())){
						userGroup = "IOP_TM_ROC_Kaybob";
					}
					// Check for severity(EFS)/risk(Canada) 
					String severity = null;
					if(!ServicesUtil.isEmpty(ssdBypassHeaderDto.getSeverity()))
						severity = ssdBypassHeaderDto.getSeverity();
					else if(!ServicesUtil.isEmpty(ssdBypassHeaderDto.getRisk()))
						severity = ssdBypassHeaderDto.getRisk();

					sdBypassHeaderDao.insertDataForPushNotificationToRoc(ssdBypassActivityLogDto.getSsdBypassId(),
							ssdBypassActivityLogDto.getSsdBypassLogId(), "BypassLog", location, "false", userGroup,
							sdBypassHeaderDao.getBypassLogById(ssdBypassActivityLogDto.getSsdBypassId())
									.getSsdBypassLogHeaderdto().getOperatorId(),
							"Denied", ssdBypassHeaderDto.getLocationCode(), severity,
							ssdBypassHeaderDto.getSsdBypassNum());
				}else if (operatorType.equals("operator") && responseValue == true) {
					FracServiceEndPoint fracServiceEndPoint = new FracServiceEndPoint();
					Set<String> userNameList = fracServiceEndPoint.getAllClientUsers();
					String locationCode = ssdBypassHeaderDto.getLocationCode();
					List<String> locationCodes = new ArrayList<>();
					locationCodes.add(locationCode);
					String userGroup = sdBypassHeaderDao.getLocationByLocationCode(locationCodes);
					if ("Catarina".equalsIgnoreCase(userGroup.trim())) {
						userGroup = "IOP_TM_ROC_Catarina";
					} else if ("Tilden Central".equalsIgnoreCase(userGroup.trim())
							|| "Tilden North".equalsIgnoreCase(userGroup.trim())
							|| "Tilden East".equalsIgnoreCase(userGroup.trim())) {

						userGroup = "IOP_TM_ROC_CentralTilden";
					} else if ("Tilden West".equalsIgnoreCase(userGroup.trim())) {

						userGroup = "IOP_TM_ROC_WestTilden";
					} else if ("Karnes North".equalsIgnoreCase(userGroup.trim())
							|| "Karnes South".equalsIgnoreCase(userGroup.trim())) {
						userGroup = "IOP_TM_ROC_Karnes";
					}
					else if ("Montney".equalsIgnoreCase(userGroup.trim())){
						userGroup = "IOP_TM_ROC_Montney";
					}
					else if("Kaybob".equalsIgnoreCase(userGroup.trim())){
						userGroup = "IOP_TM_ROC_Kaybob";
					}
					
					for (String userDetails : userNameList) {
						System.err.println("UserName" + userDetails);
						String[] arrSplit = userDetails.split("#%#");
						//String userName = arrSplit[0];
						List<String> existingUserGroup =Arrays.asList(arrSplit[1].split(","));
						if(existingUserGroup.contains(userGroup)){
							FracMessage fracMessage = new FracMessage();
							fracMessage.setShiftChnage("ShiftChange");
							fracMessage.setAction("Accepted for Bypass number " + Long.parseLong(ssdBypassHeaderDto.getSsdBypassNum())%1000000);
							fracMessage.setUserName(userDetails);
							fracServiceEndPoint.sendFracAlertToClient(fracMessage);
							logger.info(fracMessage.toString());
							System.err.println(fracMessage.toString());
						}
					}

					
				}else if (operatorType.equals("foreman") && responseValue == false) {
					List<String> ids = new ArrayList<>();
					ids.add(sdBypassHeaderDao.getAssignedPersonId(ssdBypassActivityLogDto.getSsdBypassId()));
					String alert = "" + perssonResponsible + " has rejected the Bypass Log "+ Long.parseLong(bypassNum)%1000000;
					String dataJson = createDataForPushNotification(ssdBypassActivityLogDto.getSsdBypassId(),
							activityLogId, "BYPASSLOG_REMINDER");
					pushBypassLogNotification(ids, alert, dataJson);
					
				}
				
				else if (operatorType.equals("superintendent") && responseValue == false) {
					List<String> ids = new ArrayList<>();
					ids.add(sdBypassHeaderDao.getAssignedPersonId(ssdBypassActivityLogDto.getSsdBypassId()));
					String alert = "" + perssonResponsible + " has rejected the Bypass Log "+ Long.parseLong(bypassNum)%1000000;
					String dataJson = createDataForPushNotification(ssdBypassActivityLogDto.getSsdBypassId(),
							activityLogId, "BYPASSLOG_REMINDER");
					pushBypassLogNotification(ids, alert, dataJson);
					
				}
			} catch (Exception e) {
				logger.error(
						"[Murphy][SsdByPassLogFacade][updateOperatorResponse : while sending notification to foreman for the  rejected task by the operator][error]"
								+ e.getMessage());
			}

			if (response > 0) {
				responseMessage.setStatus("Updated rows: " + response);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				if (operatorType.equalsIgnoreCase("operator") ) {
					if (responseValue == true) {
						responseMessage.setMessage("Bypass Log "+ Long.parseLong(ssdBypassHeaderDto.getSsdBypassNum())%1000000+" accepted.");
					} else {
						responseMessage.setMessage("Bypass Log "+ Long.parseLong(ssdBypassHeaderDto.getSsdBypassNum())%1000000 +" rejected.");
					}

				} else if( (operatorType.equalsIgnoreCase("foreman")) || (operatorType.equalsIgnoreCase("superintendent"))){
					if (responseValue == true) {
						responseMessage.setMessage("Bypass Log "+Long.parseLong(ssdBypassHeaderDto.getSsdBypassNum())%1000000 +" approved.");
					} else {
						responseMessage.setMessage("Bypass Log "+Long.parseLong(ssdBypassHeaderDto.getSsdBypassNum())%1000000 +" rejected.");
					}

				}

			}
		} catch (

		Exception e) {
			logger.error("[Murphy][SsdByPassLogFacade][updateOperatorRsponse][error]" + e.getMessage());

		}

		return responseMessage;
	}

	public String createDataForPushNotification(String bypassId, String activityLogId, String notificationType) {
		// JSONObject dataJson = new JSONObject();
		String data = "";
		try {
			data = notificationType + "," + bypassId + "," + activityLogId;

		} catch (Exception e) {
			logger.error("[Murphy][SsdByPassLogFacade][createDataForPushNotification][error]" + e.getMessage());

		}
		return data;
	}

	public List<SsdBypassListDto> getBypassLogListByUserGroup(String technicalRole, String businessRole) {
		List<SsdBypassListDto> ssdBypassListDtoList = new ArrayList<>();
		SsdBypassLogListResponseDto ssdBypassLogListResponseDto = new SsdBypassLogListResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		ssdBypassLogListResponseDto.setResponseMessage(responseMessage);
		try {
			ssdBypassListDtoList = sdBypassHeaderDao.getBypassLogListByUserGroup(technicalRole, businessRole);
			responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			ssdBypassLogListResponseDto.setResponseMessage(responseMessage);
		} catch (Exception e) {
			logger.error("[Murphy][SsdByPassLogFacade][getBypassLogListByUserGroup][error]" + e.getMessage());

		}
		return ssdBypassListDtoList;
	}

	@Override
	public HashMap<String,String> getUserLoginByPid(String pid) {
		HashMap<String,String> responseMap = new HashMap<>();
		responseMap.put("status", "FAILURE");
		responseMap.put("statusCode", "1");
		responseMap.put("message", "Data fetch failed");
		try{
			String userLoginName = sdBypassHeaderDao.getUserLoginByPid(pid);
			responseMap.put("status", "SUCCESS");
			responseMap.put("statusCode", "0");
			responseMap.put("message", "Data fetched successfully");
			responseMap.put("userLoginName", userLoginName);
			
		} catch (Exception e) {
			logger.error("[Murphy][SsdByPassLogFacade][getUserLoginByPid][error]" + e.getMessage());

		}
		return responseMap;
	}
	
	public RiskListResponseDto getRiskLevelList()
	{
		RiskListResponseDto riskListResponseDto = new RiskListResponseDto();
		List<BypassRiskLevelDto> listDto = new ArrayList<>();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		riskListResponseDto.setResponseMessage(responseMessage);
		try {
			listDto = sdBypassHeaderDao.getRiskLevelList();
			
			if(!ServicesUtil.isEmpty(listDto))
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			
			riskListResponseDto.setResponseMessage(responseMessage);
			riskListResponseDto.setDto(listDto);
		} catch (Exception e) {
			logger.error("[Murphy][SsdByPassLogFacade][getRiskLevelList][error]" + e.getMessage());

		}
		return riskListResponseDto;		
	}
	
	/*@Override
	public ResponseMessage outShiftNotification(String notificationTime , int hours){
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		String alert = "";
		String bypassId = "";
		String escalationLevel = "";
		String activityLogId = UUID.randomUUID().toString().replaceAll("-", "");
		try {
			SsdBypassActivityLogDto ssdBypassActivityLogDto = new SsdBypassActivityLogDto();

			if (hours == 12 || hours == 24) {
				Map<String, String> operatorList = sdBypassHeaderDao.outShiftNotificationOperatorIds(notificationTime , hours);
				Iterator<Map.Entry<String, String>> iterator = operatorList.entrySet().iterator();

				List<String> operatorIds = new ArrayList<>();
				while (iterator.hasNext()) {
					Map.Entry<String, String> entry = iterator.next();
					bypassId = entry.getKey();
					
					// operatorIds.add(sdBypassHeaderDao.getManagerId(entry.getValue()));
					ssdBypassActivityLogDto.setSsdBypassId(entry.getKey());
					ssdBypassActivityLogDto.setBypassStatusReviewedAt(new Date());
					if (hours == 12) {
						ssdBypassActivityLogDto.setOperatorType("foreman");
						String equipment = sdBypassHeaderDao.getBypassField(bypassId, "DEVICE_BYPASSED");
						alert = "Equipment " + equipment
								+ " has been in Bypass for 12 hours. Your approval is needed for it to continue in this state. If Rejected, this will close-out the form and equipment will be brought back in service by responsible operator.";
						escalationLevel = "first";
						Map<String, String> managerDetails = sdBypassHeaderDao.getManagerDetails(entry.getValue() , "foreman");
						for (Map.Entry<String, String> me : managerDetails.entrySet()) {
							operatorIds.add(me.getKey());
							ssdBypassActivityLogDto.setPersonId(me.getKey());
							ssdBypassActivityLogDto.setPersonResponsible(me.getValue());
						}
					}

					if (hours == 24) {
						ssdBypassActivityLogDto.setOperatorType("superintendent");
						String equipment = sdBypassHeaderDao.getBypassField(bypassId, "DEVICE_BYPASSED");
						alert = "Equipment " + equipment
								+ " has been in Bypass for 24 hours. Your approval is needed for it to continue in this state. If Rejected, this will close-out the form and equipment will be brought back in service by responsible operator.";
						escalationLevel = "second";
						Map<String, String> managerDetails = sdBypassHeaderDao.getManagerDetails(entry.getValue() , "superintendent");
						for (Map.Entry<String, String> me : managerDetails.entrySet()) {
							operatorIds.add(me.getKey());
							ssdBypassActivityLogDto.setPersonId(me.getKey());
							ssdBypassActivityLogDto.setPersonResponsible(me.getValue());
						}
					}

				}

				// List<String> operatorIds = bypassLogListForEscalation(hours);

				ssdBypassActivityLogDto.setSsdBypassLogId(activityLogId);
				ssdBypassActivityLogDto.setActivityType("escalation");
				// pushBypassLogNotification(operatorIds);

				String dataJson = createDataForPushNotification(bypassId, activityLogId, "BYPASSLOG_ESCALATION_ACTION");
				if (operatorIds.size() > 0) {
					ssdBypassActivityLogDao.createBypassActivityLog(ssdBypassActivityLogDto);
					pushBypassLogNotification(operatorIds, alert, dataJson);
					sdBypassHeaderDao.updateEscalationInfoInBypassHeader(bypassId, "pending",
							ssdBypassActivityLogDto.getPersonResponsible(), escalationLevel);

				}

			} 

			responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][SsdByPassLogFacade][sendNotificationForEscalation][error]" + e.getMessage());

		}
		return responseMessage;	
		
		
	}*/
	
	
}
