package com.murphy.taskmgmt.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.DowntimeCapturedDao;
import com.murphy.taskmgmt.dao.HandoverNotesDataDao;
import com.murphy.taskmgmt.dao.HandoverNotesToROCDao;
import com.murphy.taskmgmt.dao.ProductionLocationDao;
import com.murphy.taskmgmt.dao.TaskNotesDao;
import com.murphy.taskmgmt.dto.HandoverNotesDataDto;
import com.murphy.taskmgmt.dto.HandoverNotesDto;
import com.murphy.taskmgmt.dto.HandoverNotesRequestDto;
import com.murphy.taskmgmt.dto.HandoverNotesToROCResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskNoteDto;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.service.interfaces.HandoverNotesToROCFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("HandoverNotesToROCFacde")
public class HandoverNotesToROCFacde implements HandoverNotesToROCFacadeLocal {

	@Autowired
	HandoverNotesToROCDao handoverNotesToROCDao;

	@Autowired
	HandoverNotesDataDao handoverNotesDataDao;

	@Autowired
	DowntimeCapturedDao downTimeCapturedDao;

	@Autowired
	ProductionLocationDao productionLocationDao;

	@Autowired
	TaskNotesDao taskNotesDao;

	private static final Logger logger = LoggerFactory.getLogger(HandoverNotesToROCFacde.class);

	public ResponseMessage saveOrUpdateHandoverNotes(List<HandoverNotesDto> handoverNotesDtoList) {
		HandoverNotesDataDto handoverNotesDataDto = new HandoverNotesDataDto();
		Integer primaryKey = null;
		String message = "";
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			primaryKey = handoverNotesToROCDao.saveHandoverNote(handoverNotesDtoList.get(0));
			if (!ServicesUtil.isEmpty(primaryKey)) {

				for (HandoverNotesDto handoverNotesDto : handoverNotesDtoList) {
					handoverNotesDataDto.setNoteId(primaryKey);
					handoverNotesDataDto.setNoteCategoryId(handoverNotesDto.getNoteCategoryId());
					handoverNotesDataDto.setNote(handoverNotesDto.getNote());

					message = handoverNotesDataDao.saveHandoverNote(handoverNotesDataDto);
				}
				if (message.equalsIgnoreCase(MurphyConstant.CREATED_SUCCESS)) {
					responseMessage.setMessage(MurphyConstant.CREATED_SUCCESS);
					responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
					responseMessage.setStatus(MurphyConstant.SUCCESS);
				} else {
					responseMessage.setMessage(MurphyConstant.CREATE_FAILURE + "in Handover Notes Data table");
					responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
					responseMessage.setStatus(MurphyConstant.FAILURE);
				}
			} else {
				responseMessage.setMessage(MurphyConstant.CREATE_FAILURE + "in Handover Notes table");
				responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
				responseMessage.setStatus(MurphyConstant.FAILURE);
			}

		} catch (Exception e) {

			logger.error(
					"[Murphy][HandoverNotesToROCFacade][saveOrUpdateHandoverNotes][Exception]  : " + e.getMessage());
			e.printStackTrace();
			responseMessage.setMessage(MurphyConstant.CREATE_FAILURE);
			responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
			responseMessage.setStatus(MurphyConstant.FAILURE);
		}
		return responseMessage;
	}

	public HandoverNotesToROCResponseDto getNotesForTheNoteIds(Integer noteId) {
		HandoverNotesToROCResponseDto responseDto = new HandoverNotesToROCResponseDto();
		ResponseMessage message = new ResponseMessage();
		message.setMessage("Data Fetched Successfully");
		message.setStatus(MurphyConstant.SUCCESS);
		message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		HandoverNotesDto handoverNotesDto = null;
		try {
			handoverNotesDto = handoverNotesToROCDao.getHandOverNotesDetailsById(noteId);
			responseDto.setHandoverNotesDtoList(handoverNotesToROCDao.getNotesForTheNoteIds(noteId));
			responseDto.setSsvCloseResponseList(downTimeCapturedDao.fetchSSVCloseDetails(handoverNotesDto));

			// Set Task Notes
			List<String> tasksOrigin = new ArrayList<String>();
			tasksOrigin.add("Dispatch");
			tasksOrigin.add("Investigation");
			responseDto.setTaskList(getAllTaskNotes(handoverNotesDto, tasksOrigin));

			// Set Inquiry Notes
			tasksOrigin = new ArrayList<String>();
			tasksOrigin.add("Inquiry");
			responseDto.setEnquiryList(getAllTaskNotes(handoverNotesDto, tasksOrigin));

		} catch (Exception e) {
			logger.error("[Murphy][HandoverNotesToROCFacade][getNotesForTheNoteIds][Exception]  : " + e.getMessage());
			e.printStackTrace();
			message.setMessage("Data Fetch Failed");
			message.setStatusCode(MurphyConstant.CODE_FAILURE);
			message.setStatus(MurphyConstant.FAILURE);
		}
		responseDto.setResponseMessage(message);
		return responseDto;
	}

	private List<TaskNoteDto> getAllTaskNotes(HandoverNotesDto handoverNotesDto, List<String> tasksOrigin)
			throws NoResultFault, InvalidInputFault {
		List<TaskNoteDto> taskNoteDtos = null;
		String locationCode = productionLocationDao.getLocationCodeByLocationtext(handoverNotesDto.getField());

		Boolean time = false;
		Date nextDate = null;
		Date currentDate = null;
		String currentDateInString = null;
		String nextDateInString=null;

		if (!ServicesUtil.isEmpty(handoverNotesDto)) {
			if (handoverNotesDto.getField().equalsIgnoreCase("Kaybob")
					|| handoverNotesDto.getField().equalsIgnoreCase("Montney")) {

				String dateStr = ServicesUtil.convertFromZoneToZoneString(null, handoverNotesDto.getDate(),
						MurphyConstant.UTC_ZONE, MurphyConstant.MST_ZONE, MurphyConstant.DATE_STANDARD,
						MurphyConstant.DATE_DB_FORMATE_SD);
				currentDate = ServicesUtil.resultAsDateDowntime(dateStr);
				nextDate = ServicesUtil.getNextDate(currentDate);
				time = timeBetweenOrNot(currentDate, MurphyConstant.CA_CODE);

				if (time.equals(true)) {
					nextDate = currentDate;
					currentDate = ServicesUtil.getPrevDate(currentDate);
				}

				if (handoverNotesDto.getShift().equalsIgnoreCase("Day")) {

					taskNoteDtos = taskNotesDao.getAllTasksDetails(handoverNotesDto.getUserId(), locationCode,
							ServicesUtil.convertFromZoneToZoneString(currentDate, null, "", "", "",
									MurphyConstant.DATE_STANDARD) + " 14:00:00",
							ServicesUtil.convertFromZoneToZoneString(nextDate, null, "", "", "",
									MurphyConstant.DATE_STANDARD) + " 01:59:59",
							tasksOrigin);

				} else {

					taskNoteDtos = taskNotesDao.getAllTasksDetails(handoverNotesDto.getUserId(), locationCode,
							ServicesUtil.convertFromZoneToZoneString(currentDate, null, "", "", "",
									MurphyConstant.DATE_STANDARD) + " 02:00:00",
							ServicesUtil.convertFromZoneToZoneString(nextDate, null, "", "", "",
									MurphyConstant.DATE_STANDARD) + " 13:59:59",
							tasksOrigin);
				}
			} else {

				//Find the Current Time in CST
//				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
//				format1.setTimeZone(TimeZone.getTimeZone("CST"));
//				String dateStr = format1.format(new Date());
//				String timeString=format2.format(new Date());
//				currentDate = ServicesUtil.resultAsDateDowntime(dateStr);
//				nextDate = ServicesUtil.getNextDate(currentDate);
//				
//				
//				//Check CreatedDate for Notes
//				String dateStrForNotes=handoverNotesDto.getDate()+" "+timeString;
//				currentDateNotes = ServicesUtil.resultAsDateDowntime(dateStrForNotes);
//				nextDateNotes = ServicesUtil.getNextDate(currentDateNotes);
//				time = timeBetweenOrNot(currentDate, MurphyConstant.EFS_CODE);
//
//				if (time.equals(true)) {
//					nextDate = currentDateNotes;
//					currentDate = ServicesUtil.getPrevDate(currentDateNotes);
//				}else{
//					currentDate = ServicesUtil.resultAsDateDowntime(dateStrForNotes);
//					nextDate = ServicesUtil.getNextDate(currentDateNotes);
//				}
				
				//Find Current and Next Day By Handover Created Date
				try{
				currentDate=new SimpleDateFormat("yyyy-MM-dd").parse(handoverNotesDto.getDate());
				nextDate = ServicesUtil.getNextDate(currentDate);
				}
				catch(ParseException e){
					logger.error("Parse Exception In Inquiry and Task data fetch"+e.getMessage());
				}
				
//				String dateStr = ServicesUtil.convertFromZoneToZoneString(null, handoverNotesDto.getDate(),
//						MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE, MurphyConstant.DATE_STANDARD,
//						MurphyConstant.DATE_DB_FORMATE_SD);
//
//				currentDate = ServicesUtil.resultAsDateDowntime(dateStr);
//				nextDate = ServicesUtil.getNextDate(currentDate);
//				time = timeBetweenOrNot(currentDate, MurphyConstant.EFS_CODE);
//
//				if (time.equals(true)) {
//					nextDate = currentDate;
//					currentDate = ServicesUtil.getPrevDate(currentDate);
//				}
				currentDateInString = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
				nextDateInString = new SimpleDateFormat("yyyy-MM-dd").format(nextDate);
				if (handoverNotesDto.getShift().equalsIgnoreCase("Day")) {
					
					taskNoteDtos = taskNotesDao.getAllTasksDetails(handoverNotesDto.getUserId(), locationCode,
							ServicesUtil.convertFromZoneToZoneString(null,currentDateInString+" 07:00:00",MurphyConstant.CST_ZONE,MurphyConstant.UTC_ZONE,MurphyConstant.DATE_DB_FORMATE_SD,
									MurphyConstant.DATE_DB_FORMATE_SD),
							ServicesUtil.convertFromZoneToZoneString(null,currentDateInString+" 18:59:59",MurphyConstant.CST_ZONE,MurphyConstant.UTC_ZONE,MurphyConstant.DATE_DB_FORMATE_SD,
									MurphyConstant.DATE_DB_FORMATE_SD),
							tasksOrigin);
				} else {
					taskNoteDtos = taskNotesDao.getAllTasksDetails(handoverNotesDto.getUserId(), locationCode,
							ServicesUtil.convertFromZoneToZoneString(null,currentDateInString+" 19:00:00",MurphyConstant.CST_ZONE,MurphyConstant.UTC_ZONE,MurphyConstant.DATE_DB_FORMATE_SD,
									MurphyConstant.DATE_DB_FORMATE_SD),
							ServicesUtil.convertFromZoneToZoneString(null,nextDateInString+" 06:59:59",MurphyConstant.CST_ZONE,MurphyConstant.UTC_ZONE,MurphyConstant.DATE_DB_FORMATE_SD,
									MurphyConstant.DATE_DB_FORMATE_SD),
							tasksOrigin);
				}
			}

		}
		return taskNoteDtos;
	}

	public HandoverNotesToROCResponseDto getNoteByNoteType(HandoverNotesRequestDto requestDto) {
		HandoverNotesToROCResponseDto responseDto = new HandoverNotesToROCResponseDto();
		responseDto = handoverNotesToROCDao.getNoteByNoteType(requestDto);
		return responseDto;
	}

	public Boolean timeBetweenOrNot(Date date, String location) {

		// date1 and date2 are in UTC for Canada 12AM -8PM
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		String date0 = null;
		String date1 = null;
		String date2 = null;
		if (MurphyConstant.EFS_CODE.equalsIgnoreCase(location)) {
			date0 = df.format(date);
			date1 = "00:00:00";
			date2 = "06:59:59";
		} else {

			date0 = df.format(date);
			date1 = "00:00:00";
			date2 = "07:59:59";
		}
		// logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][DATE to be
		// checked]" + date0 + " intial DATE " + date1
		// + " final DATE " + date2);
		DateFormat newDf = new SimpleDateFormat("HH:mm:ss");

		Date time0 = null, time1 = null, time2 = null;

		try {
			time0 = newDf.parse(date0);
			time1 = newDf.parse(date1);
			time2 = newDf.parse(date2);

			// logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][TIME to be
			// checked]" + time0 + " intial TIME " + time1
			// + " final TIME " + time2);
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

	@Override
	public HandoverNotesToROCResponseDto getDefaultNotes(HandoverNotesDto handOverNotesDto) {
		HandoverNotesToROCResponseDto responseDto = new HandoverNotesToROCResponseDto();
		ResponseMessage message = new ResponseMessage();
		message.setMessage("Data Fetched Successfully");
		message.setStatus(MurphyConstant.SUCCESS);
		message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		try{
			
			responseDto.setSsvCloseResponseList(downTimeCapturedDao.fetchSSVCloseDetails(handOverNotesDto));

			// Set Task Notes
			List<String> tasksOrigin = new ArrayList<String>();
			tasksOrigin.add("Dispatch");
			tasksOrigin.add("Investigation");
			responseDto.setTaskList(getAllTaskNotes(handOverNotesDto, tasksOrigin));

			// Set Inquiry Notes
			tasksOrigin = new ArrayList<String>();
			tasksOrigin.add("Inquiry");
			responseDto.setEnquiryList(getAllTaskNotes(handOverNotesDto, tasksOrigin));

		}
		catch(Exception e){
			logger.error("[Murphy][HandoverNotesToROCFacade][getNotesForTheNoteIds][Exception]  : " + e.getMessage());
			e.printStackTrace();
			message.setMessage("Data Fetch Failed");
			message.setStatusCode(MurphyConstant.CODE_FAILURE);
			message.setStatus(MurphyConstant.FAILURE);
		}
		responseDto.setResponseMessage(message);
		return responseDto;
	}

}
