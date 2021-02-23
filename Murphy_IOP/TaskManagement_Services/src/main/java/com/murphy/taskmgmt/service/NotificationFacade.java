package com.murphy.taskmgmt.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.dto.UIRequestDto;
import com.murphy.taskmgmt.dao.FracNotificationDao;
import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dao.NotificationDao;
import com.murphy.taskmgmt.dao.PWHopperStagingDao;
import com.murphy.taskmgmt.dto.AlarmNotificationDto;
import com.murphy.taskmgmt.dto.BypassNotificationDto;
import com.murphy.taskmgmt.dto.EINotificationDto;
import com.murphy.taskmgmt.dto.FracNotificationDto;
import com.murphy.taskmgmt.dto.NotificationDto;
import com.murphy.taskmgmt.dto.PwHopperNotificationDto;
import com.murphy.taskmgmt.dto.TaskNotificationDto;
import com.murphy.taskmgmt.service.interfaces.NotificationFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;
import com.murphy.taskmgmt.websocket.FracMessage;
import com.murphy.taskmgmt.websocket.FracServiceEndPoint;

@Service("notificationFacade")
@Transactional
public class NotificationFacade implements NotificationFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(NotificationFacade.class);

	@Autowired
	FracNotificationDao fracNotificationDao;

	@Autowired
	NotificationDao notificationDao;

	public void getNotificationList() {
		FracMessage fracMessage = null;
		FracServiceEndPoint endPoint = new FracServiceEndPoint();
		TaskNotificationDto taskNotificationDto = null;
		AlarmNotificationDto alarmNotificationDto = null;
		FracNotificationDto fracAlertMessageDto = null;
		PwHopperNotificationDto hopperNotificationDto = null;
		BypassNotificationDto bypassNotificationDto = null;
		EINotificationDto eiNotificationDto = null;
		try {
			Set<String> userNameList = endPoint.getAllClientUsers();
			for (String userDetails : userNameList) {
				fracMessage=new FracMessage();
				taskNotificationDto = new TaskNotificationDto();
				fracAlertMessageDto = new FracNotificationDto();
				alarmNotificationDto=new AlarmNotificationDto();
				hopperNotificationDto = new PwHopperNotificationDto();
				bypassNotificationDto = new BypassNotificationDto();
				eiNotificationDto = new EINotificationDto();
				String[] arrSplit = userDetails.split("#%#");
				String userName = arrSplit[0];
				String userGroup = arrSplit[1];
				boolean isROC = userGroup.indexOf("ROC") != -1 ? true : false;
				boolean isPOT = userGroup.indexOf("POT") != -1 ? true : false;
				boolean isEnginner = userGroup.indexOf("Engineer") != -1 ? true : false;
//				logger.error("ISROC" + isROC + " isPOT" + isPOT + "isENg" + isEnginner + "userGroup" + userGroup);
//				if ((isROC == true && isEnginner == true) || isROC == true || isEnginner == true) {
//					if(userGroup.toUpperCase().contains("MONTNEY") || userGroup.toUpperCase().contains("KAYBOB")){
//						//Frac Notification For Canada Not applicable
//					}
//					else{
//						fracAlertMessageDto = fracNotificationDao.getFracHitDetails(userName);
//					}
//				}
				// fix for incident number INC0106746 soc
				if (((isROC) && (userGroup.toUpperCase().contains("CATARINA") || userGroup.toUpperCase().contains("KARNES") 
                		|| userGroup.toUpperCase().contains("TILDEN"))) || 
						((isPOT || isEnginner) && (userGroup.toUpperCase().contains("EAST") 
        						|| userGroup.toUpperCase().contains("WEST")))) {
                    
                        //Frac Notification For Canada Not applicable
                        fracAlertMessageDto = fracNotificationDao.getFracHitDetails(userName);
                    
                }
				//eoc
					
				if ((isEnginner == true && isPOT == true) || isEnginner == true || isPOT == true) {
					
					//POT_MONTNEY,POT_Kaybob,POT_EAST,POT_WEST
					//Engineer_montney,Engineer Kaybob,Engineer East,Engineer West
					boolean isCanada=false;
					boolean isEFS=false;
					if(userGroup.toUpperCase().contains("MONTNEY") || userGroup.toUpperCase().contains("KAYBOB")){
						 isCanada=true;
					}
					if(userGroup.toUpperCase().contains("EAST") || userGroup.toUpperCase().contains("WEST")){
						 isEFS=true;
					}
					
					logger.error("[Murphy][updatePwHopper]isCanada"+isCanada+"isEFS"+isEFS);
					updatePwHopper(userName,isCanada,isEFS);
					hopperNotificationDto = notificationDao.getHopperWells(userName, userGroup);
				}
				if (isROC == true) {
//					notificationDao.fetchAllAlarm();
//					alarmNotificationDto = notificationDao.getAlarmNotification(userName, userGroup);
					bypassNotificationDto = notificationDao.getBypassNotification(userGroup);
					if(userGroup.toUpperCase().contains("CATARINA") || userGroup.toUpperCase().contains("TILDEN") || userGroup.toUpperCase().contains("KARNES")){
					eiNotificationDto = notificationDao.fetchEnergyIsolationNotification(userGroup);
					}
				}
				taskNotificationDto = notificationDao.getTaskDetails(userName, userGroup);
				fracMessage.setConnectionMessage("Connected");
				fracMessage.setUserName(userDetails);
				fracMessage.setRequest(userDetails + " Connected");
				fracMessage.setFracNotificationDto(fracAlertMessageDto);
				fracMessage.setAlarmNotificationDto(alarmNotificationDto);
				fracMessage.setTaskNotificationDto(taskNotificationDto);
				fracMessage.setPwHopperNotificationDto(hopperNotificationDto);
				fracMessage.setByPassLogNotificationDto(bypassNotificationDto);
				fracMessage.setEnergyIsolationNotificationDto(eiNotificationDto);
				fracMessage.setNotifTotalCount(taskNotificationDto.getTaskCount() + alarmNotificationDto.getAlarmCount()
						+ fracAlertMessageDto.getFracCount() + hopperNotificationDto.getHopperCount()
						+ bypassNotificationDto.getByPassLogCount() + eiNotificationDto.getEnergyIsolationCount());
				endPoint.sendFracAlertToClient(fracMessage);
			}
		} catch (Exception ex) {
			logger.error("[NotificationFacade][getNotificationList][error]" + ex.getMessage());
		}
	}

	public void updatePwHopper(String userName,boolean isCanada,boolean isEFS) {
		UIRequestDto requestDto = new UIRequestDto();
		try {
			
			requestDto.setLocationType(MurphyConstant.FIELD);
//			if(isCanada){
//			requestDto.setLocationCodeList(efsFields);
//			notificationDao.fetchHopperWells(requestDto, userName,MurphyConstant.CA_CODE);
//			}
//			if(isEFS){
//			requestDto.setLocationCodeList(caFields);
//			notificationDao.fetchHopperWells(requestDto, userName,MurphyConstant.EFS_CODE);
//			}
			

           notificationDao.fetchHopperWells(requestDto, userName,isCanada,isEFS);

		} catch (Exception ex) {
			logger.error("[NotificationFacade][updatePwHopper][error]" + ex.getMessage());
		}
	}

	public ResponseMessage updateBubbleAckDetails(List<NotificationDto> notifyDto) {
		ResponseMessage message = new ResponseMessage();
		message.setMessage("Status Acknowledged Updated Successfully");
		message.setStatus(MurphyConstant.SUCCESS);
		message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		try {
			notificationDao.updateAcknowledgedWells(notifyDto);
		} catch (Exception ex) {
			logger.error("Exception While Updating Acknowledged Wells" + ex.getMessage());
			message.setMessage("Failed While Acknowledging Status");
			message.setStatus(MurphyConstant.FAILURE);
			message.setStatusCode(MurphyConstant.CODE_FAILURE);
		}
		return message;

	}

	@Override
	public ResponseMessage updateSafetyAppStatusDetails(List<NotificationDto> bypassDtoList) {
		ResponseMessage message = new ResponseMessage();
		
		try {
			message=notificationDao.updateSafetyAppAcknowledgedWells(bypassDtoList);
		} catch (Exception ex) {
			logger.error("Exception While Updating Acknowledged Wells" + ex.getMessage());
			message.setMessage("Failed While Acknowledging Status");
			message.setStatus(MurphyConstant.FAILURE);
			message.setStatusCode(MurphyConstant.CODE_FAILURE);
		}
		return message;
	}
	
	public ResponseMessage sendNotificationForFutureTask() {
		ResponseMessage message = new ResponseMessage();
		message.setStatus(MurphyConstant.SUCCESS);
		message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		try {
			String result = notificationDao.sendNotificationForFutureTask();
			message.setMessage(result);
		} catch (Exception ex) {
			logger.error("Exception While sending notifications for future tasks " + ex.getMessage());
			message.setMessage("Failed While sending notifications for future tasks");
			message.setStatus(MurphyConstant.FAILURE);
			message.setStatusCode(MurphyConstant.CODE_FAILURE);
		}
		return message;

	}

}
