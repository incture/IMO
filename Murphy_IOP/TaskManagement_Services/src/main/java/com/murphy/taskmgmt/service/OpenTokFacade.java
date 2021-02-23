/**
 * 
 */
package com.murphy.taskmgmt.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service;
import org.json.JSONObject;

import com.murphy.taskmgmt.dao.ConfigDao;
import com.murphy.taskmgmt.dao.OpenTokDao;
import com.murphy.taskmgmt.dto.OpenTokDto;
import com.murphy.taskmgmt.dto.OpenTokResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.OpenTokFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;
import com.opentok.OpenTok;

/**
 * @author Kamlesh.Choubey
 *
 */

@Service("openTokFacade")
public class OpenTokFacade implements OpenTokFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(OpenTokFacade.class);
	
	@Autowired
	SsdByPassLogFacade ssdByPassLogFacade;
	
	@Autowired
	OpenTokDao openTokDao;  

	/*
	 * public String generateTokenId(String sessionId) { String tokenId = "";
	 * try { tokenId = opentok.generateToken(sessionId); } catch (Exception e) {
	 * logger.error("[Murphy][OpenTokFacade][generateTokenId][error]" +
	 * e.getMessage()); e.printStackTrace(); }
	 * 
	 * return tokenId; }
	 * 
	 * public String generateSessionId(int apiKey , String apiSecret) { String
	 * sessionId = ""; try { OpenTok opentok = new OpenTok(apiKey, apiSecret);
	 * sessionId = opentok.createSession().getSessionId(); } catch (Exception e)
	 * { logger.error("[Murphy][OpenTokFacade][generateSessionId][error]" +
	 * e.getMessage()); e.printStackTrace(); }
	 * 
	 * return sessionId; }
	 */
	
	@Override
	public OpenTokResponseDto createCall(OpenTokDto openTokDto) {
		
		OpenTokResponseDto openTokResponseDto = new OpenTokResponseDto();
		String apiKey = "";
		String apiSecret = "";
		String sessionId = "";
		String tokenId = "";
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		openTokResponseDto.setResponseMessage(responseMessage);
		try {
			apiKey = openTokDao.getConfigDescById("API key");
			apiSecret = openTokDao.getConfigDescById("SecretKey");
			OpenTok opentok = new OpenTok(Integer.parseInt(apiKey), apiSecret);
			sessionId = opentok.createSession().getSessionId();	
			tokenId = opentok.generateToken(sessionId);
			openTokDto.setApiKey(apiKey);
			openTokDto.setApiSecret(apiSecret);
			openTokDto.setSessionId(sessionId);
			openTokDto.setTokenId(tokenId);
			openTokDto.setPublisherStatus(MurphyConstant.INCALL);
			openTokDto.setSubscriberStatus(MurphyConstant.INCALL);
			openTokDto.setNotificationType(MurphyConstant.AR_NOTIFICATION_TYPE);
			if (!ServicesUtil.isEmpty(openTokDto.getSubscriberId())){
				List<String> userIds = new ArrayList<String>();
				JSONObject jSONObject = new JSONObject();
				jSONObject.put("sessionId", sessionId);
				jSONObject.put("apiKey", apiKey);
				jSONObject.put("tokenId", tokenId);
				jSONObject.put("notificationType", MurphyConstant.AR_NOTIFICATION_TYPE);
				jSONObject.put("subscriberName", openTokDto.getSubsciberName());
				jSONObject.put("publisherName", openTokDto.getPublisherName());
				jSONObject.put("publisherId", openTokDto.getPublisherId());
				jSONObject.put("subscriberId", openTokDto.getSubscriberId());
				userIds.add(openTokDto.getSubscriberId());
				String alert = openTokDto.getPublisherName() +" is waiting for you on video call";
				String response = openTokDao.createCall(openTokDto);
				if(response.equalsIgnoreCase(MurphyConstant.CONNECTED)){
					ssdByPassLogFacade.pushBypassLogNotification(userIds , alert , jSONObject.toString());
					responseMessage.setMessage(MurphyConstant.CONNECTED);
					responseMessage.setStatus(MurphyConstant.SUCCESS);
					responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
					openTokResponseDto.setOpenTokDto(openTokDto);
				} else if(response.equalsIgnoreCase(MurphyConstant.BUSY)){
					responseMessage.setMessage(MurphyConstant.BUSY.toLowerCase());
					responseMessage.setStatus(MurphyConstant.SUCCESS);
					responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
					openTokResponseDto.setOpenTokDto(openTokDto);
				}
				openTokResponseDto.setResponseMessage(responseMessage);
			}

		} catch (Exception e) {
			logger.error("[Murphy][OpenTokFacade][getOpenTokDetails][error]" + e.getMessage());
			e.printStackTrace();
		}
		return openTokResponseDto;

	}

	@Override
	public String updateARCallResponse(OpenTokDto openTokDto, String responseBy , String actionType , String sendNotification) {
		String response = "";
		try{
			
			response = openTokDao.updateARCallResponse(openTokDto, responseBy , actionType);
			
			 if(responseBy.equalsIgnoreCase(MurphyConstant.SUBSCRIBER) && openTokDto.getSubscriberStatus().equalsIgnoreCase(MurphyConstant.OUTCALL)){
				 List<String> userIds = new ArrayList<String>();
					JSONObject jSONObject = new JSONObject();
					jSONObject.put("sessionId", openTokDto.getSessionId());
					jSONObject.put("apiKey", openTokDto.getApiKey());
					jSONObject.put("tokenId", openTokDto.getTokenId());
					jSONObject.put("notificationType", "AR_VIDEO_CALL_ENDED");
					jSONObject.put("subscriberName", openTokDto.getSubsciberName());
					userIds.add(openTokDto.getPublisherId());
					String alert = "Call Disconnected";
					if(actionType.equalsIgnoreCase(MurphyConstant.DECLINED)){
						 alert = openTokDto.getSubsciberName() +" declined the video call";
					}else if(actionType.equalsIgnoreCase(MurphyConstant.ENDED)){
						alert = openTokDto.getSubsciberName() +" ended the video call";
					}
					
					if(sendNotification.equalsIgnoreCase("true")){
						ssdByPassLogFacade.pushBypassLogNotification(userIds , alert , jSONObject.toString());
					}
					
			 }
			 if (!ServicesUtil.isEmpty(actionType)){
				 if(responseBy.equalsIgnoreCase(MurphyConstant.PUBLISHER)){
					 responseBy = MurphyConstant.SUBSCRIBER;
				 } else if(responseBy.equalsIgnoreCase(MurphyConstant.SUBSCRIBER)){
					 responseBy = MurphyConstant.PUBLISHER;
				 }
				  openTokDao.updateARCallResponse(openTokDto, responseBy , actionType);
			 }
			 
			 
			/* Calendar cal = Calendar.getInstance();
				cal.add(Calendar.SECOND, 10);
			 Timer timer = new Timer();
			 timer.schedule(new TimerTask() {
					@Override
					public void run() {
						 openTokDao.updateARCallResponse(openTokDto, MurphyConstant.SUBSCRIBER);
						 openTokDao.updateARCallResponse(openTokDto, MurphyConstant.PUBLISHER);
						 if(responseBy.equalsIgnoreCase(MurphyConstant.SUBSCRIBER) && openTokDto.getSubscriberStatus().equalsIgnoreCase(MurphyConstant.OUTCALL)){
							 List<String> userIds = new ArrayList<String>();
								JSONObject jSONObject = new JSONObject();
								jSONObject.put("sessionId", openTokDto.getSessionId());
								jSONObject.put("apiKey", openTokDto.getApiKey());
								jSONObject.put("tokenId", openTokDto.getTokenId());
								jSONObject.put("notificationType", "AR_VIDEO_CALL_ENDED");
								jSONObject.put("subscriberName", openTokDto.getSubsciberName());
								userIds.add(openTokDto.getPublisherId());
								String alert = openTokDto.getSubsciberName() +" declined the video call";
								ssdByPassLogFacade.pushBypassLogNotification(userIds , alert , jSONObject.toString());
						 }
						
					}
				}, cal.getTime());*/
		
		}catch (Exception e) {
			logger.error("[Murphy][OpenTokFacade][updateARCallResponse][error]" + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

}
