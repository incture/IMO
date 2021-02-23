package com.murphy.taskmgmt.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.murphy.taskmgmt.dao.AuditDao;
import com.murphy.taskmgmt.dao.MessageDao;
import com.murphy.taskmgmt.dao.RootCauseInstDao;
import com.murphy.taskmgmt.dao.UserIDPMappingDao;
import com.murphy.taskmgmt.dto.AuditDto;
import com.murphy.taskmgmt.dto.MessageDto;
import com.murphy.taskmgmt.dto.MessageUIDetailDto;
import com.murphy.taskmgmt.dto.MessageUIResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.RootCauseInstDto;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.TeamsResponseDto;
import com.murphy.taskmgmt.dto.UpdateRequestDto;
import com.murphy.taskmgmt.entity.UserIDPMappingDo;
import com.murphy.taskmgmt.service.interfaces.MessageFacadeLocal;
import com.murphy.taskmgmt.util.DestinationUtil;
import com.murphy.taskmgmt.util.MailAlertUtil;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;
import com.murphy.taskmgmt.util.UniqueSeq;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

import weka.gui.SysErrLog;

@Service("MessageFacade")
public class MessageFacade implements MessageFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(MessageFacade.class);

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private UserIDPMappingDao mappingDao;
	
	@Autowired
	private AuditDao auditDao;
	
	@Autowired
	private RootCauseInstDao rootCauseDao;

	@SuppressWarnings("unchecked")
	@Override
	public TeamsResponseDto create(JSONObject json, String auth) {
		MessageDto dto = new MessageDto();
		Long refNum = UniqueSeq.getInstance().getNext();
		String text = json.get("text").toString();
		String mention = text.substring(text.lastIndexOf("<at>") + 4, text.lastIndexOf("</at>"));
		dto.setMessageId(refNum);
		dto.setMessage(text.replace("<at>" + mention + "</at>", ""));
		dto.setCreatedBy(((LinkedHashMap<String, String>) json.get("from")).get("name"));
		dto.setCreatedAt(Calendar.getInstance().getTimeInMillis());
		dto.setStatus(MurphyConstant.NEW_TASK);
		dto.setCurrentOwner("ROC");
		dto.setUpdatedAt(Calendar.getInstance().getTimeInMillis());
		dto.setTeamsTeamId(((LinkedHashMap<String, String>) json.get("channelData")).get("teamsTeamId"));
		dto.setConversationId(((LinkedHashMap<String, String>) json.get("conversation")).get("id"));
		dto.setTeamsChannelId(((LinkedHashMap<String, String>) json.get("channelData")).get("teamsChannelId"));
		TeamsResponseDto response = new TeamsResponseDto();
		response.setType("message");
		response.setText("Sorry, your message didn't reach " + mention.trim() + ". Please try again after sometime.");
		if (messageDao.save(dto).equals(MurphyConstant.SUCCESS)) {
			response.setText("Your message has been posted to " + mention.trim() + ". Reference No. #" + refNum);
		}
		return response;
	}

	@Override
	public ResponseMessage update(MessageDto dto) {
		ResponseMessage response = new ResponseMessage();
		response.setMessage("Failed to update Message");
		response.setStatus(MurphyConstant.FAILURE);
		response.setStatusCode(MurphyConstant.CODE_FAILURE);
		if (!ServicesUtil.isEmpty(dto.getMessageId())) {
			dto.setUpdatedAt(Calendar.getInstance().getTimeInMillis());
			if (messageDao.save(dto).equals(MurphyConstant.SUCCESS)) {
				response.setMessage("Message updated Successfully");
				response.setStatus(MurphyConstant.SUCCESS);
				response.setStatusCode(MurphyConstant.CODE_SUCCESS);
				if (dto.getStatus().equals(MurphyConstant.RESOLVE) && !ServicesUtil.isEmpty(dto.getComment())) {
					String url = messageDao.getChannelUrl(dto.getTeamsTeamId(), dto.getTeamsChannelId());
					String rejectMessage = composeRejectMessage(dto);
					messageTeams(rejectMessage, url);
					sendMailToEngineer(dto.getCreatedBy(), "Message ID#"+dto.getMessageId()+" has been resolved", rejectMessage);
				}
			}
		}
		return response;
	}

	@Override
	public MessageUIResponseDto getAllActiveMessagesOwnedBy(String user, List<String> userType, List<String> country,
			Integer pageNo) {
		MessageUIResponseDto responseList = new MessageUIResponseDto();
		ResponseMessage resp = new ResponseMessage();
		List<MessageDto> messageDtos = null;
		int totalCount = 0;
		resp.setMessage("Failed to fetch the Message list");
		resp.setStatus(MurphyConstant.FAILURE);
		resp.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			messageDtos = messageDao.getAllActiveMessagesOwnedBy(user, userType, country, pageNo);
			totalCount = messageDao.getAllActiveMessageCount(user, userType, country);
			resp.setMessage("Message list fetched successfully");
			resp.setStatus(MurphyConstant.SUCCESS);
			resp.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][MessageFacade][getAllActiveMessagesOwnedBy][error] " + e.getMessage());
		}

		responseList.setMessageList(messageDtos);
		responseList.setTotalCount(totalCount);
		responseList.setPageCount(totalCount > 0 ? totalCount / 20 + (totalCount % 20 > 0 ? 1 : 0) : 0);
		responseList.setResponse(resp);
		return responseList;
	}

	@Override
	public MessageUIDetailDto getMessage(Long messageId) {
		MessageUIDetailDto messageUIDetailDto = new MessageUIDetailDto();
		MessageDto dto = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage("Failed to fetch the Message");
		resp.setStatus(MurphyConstant.FAILURE);
		resp.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			dto = new MessageDto();
			dto.setMessageId(messageId);
			dto = messageDao.getByKeys(dto);
			if (!ServicesUtil.isEmpty(dto.getLocationCode())) {
				String[] location = messageDao.getLocationDetail(dto.getLocationCode());
				dto.setLocationText(location[0]);
				dto.setMuwi(location[1]);
				dto.setTier(location[2]);
				dto.setLocationType(location[3]);
			}
			resp.setMessage("Message fetched successfully");
			resp.setStatus(MurphyConstant.SUCCESS);
			resp.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][MessageFacade][getMessage][error] " + e.getMessage());
		}
		messageUIDetailDto.setMessage(dto);
		messageUIDetailDto.setResponseMessage(resp);
		return messageUIDetailDto;
	}

	@Override
	public void resolveMessage(Long messageId, String status, TaskEventsDto taskEvent) {
		try {
			logger.error("Resolving Message: " + messageId);
			MessageDto msgDto = getMessage(messageId).getMessage();
			if (!ServicesUtil.isEmpty(msgDto)) {
				if (!msgDto.getStatus().equals(MurphyConstant.RESOLVE)
						&& (status.equals(MurphyConstant.COMPLETE)
								|| status.equals(MurphyConstant.RESOLVE))) {

					msgDto.setStatus(MurphyConstant.RESOLVE);
					if (update(msgDto).getStatus().equals(MurphyConstant.SUCCESS)) {
						String url = messageDao.getChannelUrl(msgDto.getTeamsTeamId(), msgDto.getTeamsChannelId());
						String resolveMessage = composeResolvedMessage(msgDto, taskEvent);
						messageTeams(resolveMessage, url);
						sendMailToEngineer(msgDto.getCreatedBy(),
								taskEvent.getOrigin() + " Resolved for Message ID#" + msgDto.getMessageId(),
								resolveMessage);
					}
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][MessageFacade][resolveMessage][error] " + e.getMessage());
		}
	}

	private String composeResolvedMessage(MessageDto msgDto, TaskEventsDto taskEvent) {
		ZoneId zoneId = ZoneId.of("US/Central");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy hh:mm:ss a");

		Instant i = Instant.ofEpochMilli(msgDto.getCreatedAt());
		String created = formatter.format(ZonedDateTime.ofInstant(i, zoneId));
		
		
		String text = "<b>" + taskEvent.getOrigin() + " Resolved for Message ID#" + msgDto.getMessageId()
					+ "</b><br><b>Message:</b> " + msgDto.getMessage() 
					+ "<br><b>Requestor:</b> " + msgDto.getCreatedBy()
					+ "<br><b>Created At:</b> " + created 
					+ "<br><b>Location: </b> " + msgDto.getLocationText()
					+ "<br><b>Audit Log:</b><br><ol>";
		List<AuditDto> auditLog = auditDao.getReportByTask(taskEvent.getTaskId());
		for(AuditDto dto : auditLog){
			text = text + "<li>"+taskEvent.getOrigin()+" "+dto.getAction().toLowerCase()+" by "+dto.getActionBy()+" at "+formatter.format(ZonedDateTime.ofInstant(dto.getCreatedAt().toInstant(), zoneId))+"</li>";
		}
		text = text+"</ol>";
		List<RootCauseInstDto> rootCauseInstDtos = rootCauseDao.getRootInstById(taskEvent.getTaskId(),
				MurphyConstant.RESOLVE);
		if (taskEvent.getOrigin().equals(MurphyConstant.DISPATCH_ORIGIN) && !ServicesUtil.isEmpty(rootCauseInstDtos)) {
			text = text+ "<br><b>Root cause:</b> " + (ServicesUtil.isEmpty(rootCauseInstDtos.get(0).getRootCause())?"":rootCauseInstDtos.get(0).getRootCause())
					+ "<br><b>Description:</b> "+ (ServicesUtil.isEmpty(rootCauseInstDtos.get(0).getDescription())?"":rootCauseInstDtos.get(0).getDescription());
		} else if (taskEvent.getOrigin().equals(MurphyConstant.INVESTIGATON)) {

			if (!ServicesUtil.isEmpty(rootCauseInstDtos)) {
				text = text + "<br><b>Classification:</b> "
						+ (ServicesUtil.isEmpty(rootCauseInstDtos.get(0).getRootCause()) ? ""
								: rootCauseInstDtos.get(0).getRootCause())
						+ "<br><b>Sub-classification:</b> "
						+ (ServicesUtil.isEmpty(rootCauseInstDtos.get(1).getRootCause()) ? ""
								: rootCauseInstDtos.get(1).getRootCause());
			} else {
				text = text + "<br><b>Classification:</b> "
						+ "<br><b>Sub-classification:</b> ";
			}
		}		
			
		return text;
	}

	private String composeRejectMessage(MessageDto msgDto) {
		ZoneId zoneId = ZoneId.of("US/Central");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy hh:mm:ss a");

		Instant i = Instant.ofEpochMilli(msgDto.getCreatedAt());
		String created = formatter.format(ZonedDateTime.ofInstant(i, zoneId));
		i = Instant.ofEpochMilli(msgDto.getUpdatedAt());
		String updated = formatter.format(ZonedDateTime.ofInstant(i, zoneId));
		UserIDPMappingDo user = mappingDao.getUserByEmail(msgDto.getCurrentOwner());
		String assignedTo = msgDto.getCurrentOwner();
		String commentsFrom ="";
		if (!ServicesUtil.isEmpty(user)) {
			assignedTo = user.getUserFirstName() + " " + user.getUserLastName();
			if(user.getUserRole().contains("ROC"))
				commentsFrom = " from ROC";
			else if(user.getUserRole().contains("POT"))
				commentsFrom = " from POT";
		}
		String text = "<b>Message ID#" + msgDto.getMessageId() + " has been resolved" 
				+ "</b><br><b>Message:</b> " + msgDto.getMessage() 
				+ "<br><b>Requestor:</b> " + msgDto.getCreatedBy() 
				+ "<br><b>Created At:</b> "	+ created 
				+ "<br><b>Assigned To:</b> " + assignedTo 
				+ "<br><b>Updated At:</b> " + updated
				+ "<br><b>Location: </b> " + msgDto.getLocationText()
				+ "<br><b>Comments"+commentsFrom+":</b> " + msgDto.getComment();

		return text;
	}

	private void messageTeams(String text, String url) {
		TeamsResponseDto responseDto = new TeamsResponseDto();
		responseDto.setType("message");
		responseDto.setText(text);
		try {
			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("content-type", "application/json");
			httpPost.setEntity(new StringEntity(new Gson().toJson(responseDto)));
			httpClient.execute(httpPost);
		} catch (Exception e) {
			logger.error("[Murphy][MessageFacade][messageTeams][error] " + e.getMessage());
		}
	}

	private void sendMailToEngineer(String userName, String subject, String message) {

		try {

			DestinationConfiguration destinationConfiguration = DestinationUtil.getDest(MurphyConstant.UMA_API_DEST);
//			String umaApiUrl = destinationConfiguration.getProperty("URL") + MurphyConstant.UMA_EMAIL_API+(userName + " (Contractor)").replace(" ", "%20");
			String umaApiUrl = destinationConfiguration.getProperty("URL") + MurphyConstant.UMA_EMAIL_API+userName.replace(" ", "%20");
//			String umaApiUrl = "https://umarestflt3duj166.us2.hana.ondemand.com/umaRest/rest/getEmailIdByEmpName?empName="+ (userName + " (Contractor)").replace(" ", "%20");
			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
//			System.err.println("sendMailToEngineer: "+ (userName + " (Contractor)"));
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet httpGet = new HttpGet(umaApiUrl);
			HttpResponse httpResponse = httpClient.execute(httpGet);

			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			InputStream inputStream = httpResponse.getEntity().getContent();
			byte[] data = new byte[1024];
			int length = 0;
			while ((length = inputStream.read(data)) > 0) {
				bytes.write(data, 0, length);
			}

			String respBody = new String(bytes.toByteArray(), "UTF-8");
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(respBody);
			System.err.println(json);
			if (!ServicesUtil.isEmpty(json) && !ServicesUtil.isEmpty(json.get("data"))) {
				JSONObject userData = (JSONObject) json.get("data");
				String email = userData.get("sfEmailId").toString();
				if (!ServicesUtil.isEmpty(email)) {
					System.err.println(email);
					new MailAlertUtil().sendMailWithCC(email, null, subject, message, userName);
				}
			}

		} catch (Exception e) {
			System.err.println("[Murphy][MessageFacade][sendMailToEngineer][error] " + e.getMessage());
		}

	}
	
	@Override
	public void setMessageInProgress(Long messageId){
		try {
			MessageDto dto = getMessage(messageId).getMessage();
			dto.setStatus(MurphyConstant.INPROGRESS);
			update(dto);
		}catch (Exception e) {
			logger.error("[Murphy][MessageFacade][setMessageInProgress][error] " + e.getMessage());
		}
	}
}
