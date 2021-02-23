package com.murphy.integration.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dao.CompressorDowntimeCaptureDao;
import com.murphy.integration.dto.DowntimeCaptureDto;
import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.entity.DowntimeCaptureDo;
import com.murphy.integration.interfaces.CompressorDowntimeCaptureLocal;
import com.murphy.integration.util.DBConnections;
import com.murphy.integration.util.ProcountConstant;

public class CompressorDowntimeCapture implements CompressorDowntimeCaptureLocal {

	private static final Logger logger = LoggerFactory.getLogger(CompressorDowntimeCapture.class);

	/*@Override
	public DowntimeCaptureFetchResponseDto fetchRecordForProvidedUwiIdAndDate(Date originalDateEntered, String uwiId) {
		logger.info("[insertOrUpdateCounts] : INFO- Service Started");

		DowntimeCaptureFetchResponseDto downtimeCaptureFetchResponseDto = new DowntimeCaptureFetchResponseDto();

		ResponseMessage responseMessage = new ResponseMessage();

		if (originalDateEntered != null && uwiId != null && uwiId.length() > 0) {
			DowntimeCaptureDao downtimeCaptureDao = null;

			Connection connection = DBConnections.createConnectionForProcount();

			if (connection != null) {
				downtimeCaptureDao = new DowntimeCaptureDao();
				try {
					logger.info("[insertOrUpdateCounts] : INFO- Connection to DB successful");

					SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
					String originalDateInDB = dateformatter.format(originalDateEntered);
					logger.error("[insertOrUpdateCounts] : INFO  - originalDateInDB " + originalDateInDB);

					int merrickId = downtimeCaptureDao.fetchMerrickFromDB(connection, uwiId.trim());

					logger.error("[insertOrUpdateCounts] : INFO  - merrickId " + merrickId);

					if (merrickId >= 0) {

						DowntimeCaptureDo downtimeCaptureDoFetchFromDB = downtimeCaptureDao.getDataFromDB(connection, merrickId, originalDateInDB);

						if (downtimeCaptureDoFetchFromDB != null) {
							DowntimeCaptureDto downtimeCaptureDto = convertDowntimeCaptureDoToDTo(downtimeCaptureDoFetchFromDB);
							downtimeCaptureDto.setUwiId(uwiId.trim());
							downtimeCaptureFetchResponseDto.setDowntimeCaptureDto(downtimeCaptureDto);
							responseMessage.setMessage("Successful");
							responseMessage.setStatus("true");
						} else {
							responseMessage.setMessage("No record is present for provided values.");
							responseMessage.setStatus("false");
						}

					} else {
						responseMessage.setMessage("No merrick Id is maintained for prvided uwiId.");
						responseMessage.setStatus("false");
					}

				} catch (Exception e) {
					logger.error("[insertOrUpdateCounts] : ERROR- Exception while interacting with database " + e);
					responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
					responseMessage.setStatus("false");
				} finally {
					try {
						connection.close();
					} catch (SQLException e) {
						logger.error("[insertOrUpdateCounts] : ERROR- Exception while closing Connection " + e);
					}
				}
			} else {
				responseMessage.setMessage("Connection to Database is not possible");
				responseMessage.setStatus("false");
			}
		} else {
			responseMessage.setMessage("Please provide all the necessary values");
			responseMessage.setStatus("false");
		}
		logger.info("[insertOrUpdateCounts] : INFO- Connection to DB successful");
		logger.info("[insertOrUpdateCounts] : downtimeCaptureFetchResponseDto " + downtimeCaptureFetchResponseDto);
		downtimeCaptureFetchResponseDto.setResponseMessage(responseMessage);
		return downtimeCaptureFetchResponseDto;
	}
*/
	@Override
	public ResponseMessage insertOrUpdateCounts(DowntimeCaptureDto downtimeCaptureDto) {
		logger.info("[insertOrUpdateCounts] : INFO- Service Started");
		ResponseMessage responseMessage = new ResponseMessage();
		if (downtimeCaptureDto != null && downtimeCaptureDto.getOriginalDateEntered() != null) {
			CompressorDowntimeCaptureDao downtimeCaptureDao = null;
			DowntimeCaptureDo downtimeCaptureDoUI = null;

			Connection connection = DBConnections.createConnectionForProcount();

			if (connection != null) {
				downtimeCaptureDao = new CompressorDowntimeCaptureDao();
				try {
					logger.info("[insertOrUpdateCounts] : INFO- Connection to DB successful");

					downtimeCaptureDoUI = convertDowntimeCaptureDtoToDo(downtimeCaptureDto);

					/*int merrickId = downtimeCaptureDao.fetchMerrickFromDB(connection, downtimeCaptureDto.getUwiId().trim());
					logger.error("[insertOrUpdateCounts] : INFO  - merrickId " + merrickId);*/
					
					int merrickId = Integer.parseInt(downtimeCaptureDto.getUwiId());

					downtimeCaptureDto.setMerrickId(merrickId);
					if (merrickId >= 0) {

						downtimeCaptureDoUI.setMerrickId(merrickId);

						DowntimeCaptureDo downtimeCaptureDoFetchFromDB = downtimeCaptureDao.getDataFromDB(connection,
								downtimeCaptureDoUI.getMerrickId(), downtimeCaptureDoUI.getOriginalDateEntered());

						if (downtimeCaptureDoFetchFromDB != null) {

							downtimeCaptureDao.updateDataInDB(connection, downtimeCaptureDoUI);

						} else {
							downtimeCaptureDao.insertDataInDB(connection, downtimeCaptureDoUI);
						}

						connection.commit();
						responseMessage.setMessage("Successful");
						responseMessage.setStatus("true");
					} else {
						responseMessage.setMessage("Equipment not found in ProCount");
						responseMessage.setStatus("false");

					}
				} catch (Exception e) {
					try {
						connection.rollback();
					} catch (SQLException sqlException) {
						logger.error("[insertOrUpdateCounts] : ERROR- Rollback transactions because of exception " + sqlException);
					}
					logger.error("[insertOrUpdateCounts] : ERROR- Exception while interacting with database " + e);
					responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
					responseMessage.setStatus("false");
				} finally {
					try {
						connection.close();
					} catch (SQLException e) {
						logger.error("[insertOrUpdateCounts] : ERROR- Exception while closing Connection " + e);
					}
				}
			} else {
				responseMessage.setMessage("Connection to Database is not possible.");
				responseMessage.setStatus("false");
			}
		} else{
			responseMessage.setMessage("Please provide all the necessary values.");
			responseMessage.setStatus("false");
		}
		return responseMessage;
	}

	private DowntimeCaptureDo convertDowntimeCaptureDtoToDo(DowntimeCaptureDto downtimeCaptureDto) {
		DowntimeCaptureDo downtimeCaptureDo = new DowntimeCaptureDo();
		Date originalDateEntered = downtimeCaptureDto.getOriginalDateEntered();
		Date StartDateEnteredInUI = downtimeCaptureDto.getStartDate();
        String comments = downtimeCaptureDto.getComments();
		
		Calendar calendar = Calendar.getInstance();
		String dateInYearFormat = null;
		String startDate = null;
		String endDate = null;
		String startTime = null;
		String endTime = null;
		int downtimeCode;

		downtimeCode = Integer.parseInt(downtimeCaptureDto.getChildCode());
		float downtimeHours = downtimeCaptureDto.getDurationInHours() + ((float) downtimeCaptureDto.getDurationInMinutes() / 60);

		SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm:ss");

		dateInYearFormat = dateformatter.format(originalDateEntered);

		if (StartDateEnteredInUI != null) {
			startDate = dateformatter.format(StartDateEnteredInUI);
			startTime = timeformatter.format(StartDateEnteredInUI);

			calendar.setTime(StartDateEnteredInUI);
			calendar.add(Calendar.MINUTE, (int) (downtimeHours * 60));

			StartDateEnteredInUI = calendar.getTime();
			endDate = dateformatter.format(StartDateEnteredInUI);
			endTime = timeformatter.format(StartDateEnteredInUI);
		}

		downtimeCaptureDo.setObjectType(ProcountConstant.COMPRESSOR_OBJECT_TYPE);
		downtimeCaptureDo.setOriginalDateEntered(dateInYearFormat);
		downtimeCaptureDo.setOriginalTimeEntered(ProcountConstant.ORIGINAL_TIME_ENTERED);
		downtimeCaptureDo.setUpDownFlag(ProcountConstant.UPDOWN_FLAG);
		downtimeCaptureDo.setDateEntryFlag(ProcountConstant.DATEENTRY_FLAG);
		downtimeCaptureDo.setDowntimeCode(downtimeCode);
		downtimeCaptureDo.setDowntimeHours(downtimeHours);
		downtimeCaptureDo.setRepairCosts(ProcountConstant.REPAIR_COSTS);
		downtimeCaptureDo.setLostProduction(ProcountConstant.LOST_PRODUCTION);
		downtimeCaptureDo.setCalCDowntimeFlag(ProcountConstant.CALC_DOWNTIME_FLAG);
		downtimeCaptureDo.setStartDate(startDate);
		downtimeCaptureDo.setStartTime(startTime);
		downtimeCaptureDo.setEndDate(endDate);
		downtimeCaptureDo.setEndTime(endTime);
		downtimeCaptureDo.setStartProductionDate(ProcountConstant.START_PRODUCTION_DATE);
		downtimeCaptureDo.setEndProductionDate(ProcountConstant.END_PRODUCTION_DATE);
		downtimeCaptureDo.setComments(comments);
		downtimeCaptureDo.setReason(ProcountConstant.REASON);
		downtimeCaptureDo.setMessageSendFlag(ProcountConstant.MESSAGE_SEND_FLAG);
		downtimeCaptureDo.setDestinationPerson(ProcountConstant.DESTINATION_PERSON);
		downtimeCaptureDo.setRioDowntimeId(ProcountConstant.RIO_DOWNTIME_ID);
		downtimeCaptureDo.setLastDayHoursDown(ProcountConstant.LAST_DAY_HOURS_DOWN);
		downtimeCaptureDo.setDeleteFlag(ProcountConstant.DELETE_FLAG);
		downtimeCaptureDo.setLastTransmission(ProcountConstant.LAST_TRANSMISSION);
		downtimeCaptureDo.setLastLoadDate(ProcountConstant.LAST_LOAD_DATE);
		downtimeCaptureDo.setLastLoadTime(ProcountConstant.LAST_LOAD_TIME);
		downtimeCaptureDo.setTransmitFlag(ProcountConstant.TRANSMIT_FLAG);
		downtimeCaptureDo.setDateTimeStamp(ProcountConstant.DATETIMESTAMP);
		downtimeCaptureDo.setbLogicDateStamp(ProcountConstant.BLOGICDATESTAMP);
		downtimeCaptureDo.setbLogicTimeStamp(ProcountConstant.BLOGICTIMESTAMP);
		downtimeCaptureDo.setUserDateStamp(ProcountConstant.USERDATESTAMP);
		downtimeCaptureDo.setUserTimeStamp(ProcountConstant.USERTIMESTAMP);
		downtimeCaptureDo.setUserId(ProcountConstant.USERID);

		logger.error("[convertDowntimeCaptureDtoToDo] : INFO  - downtimeCaptureDo" + downtimeCaptureDo);
		return downtimeCaptureDo;
	}

//	private DowntimeCaptureDto convertDowntimeCaptureDoToDTo(DowntimeCaptureDo downtimeCaptureDo) throws ParseException {
//		DowntimeCaptureDto downtimeCaptureDto = new DowntimeCaptureDto();
//
//		Date originalDateEntered = null;
//		Date startDate = null;
//
//		float downtimeHours = downtimeCaptureDo.getDowntimeHours();
//		int downtimeMinutes = (int) (downtimeHours * 60);
//
//		String originalDateInDB = downtimeCaptureDo.getOriginalDateEntered();
//		String originalTimeInDB = downtimeCaptureDo.getOriginalTimeEntered();
//		String startDateInDB = downtimeCaptureDo.getStartDate();
//		String startTimeInDB = downtimeCaptureDo.getStartTime();
//		String comments = downtimeCaptureDo.getComments();
//
//		SimpleDateFormat dateFormatterWithTimestamp = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
//		SimpleDateFormat dateFormatterWithoutTimestamp = new SimpleDateFormat("yyyy-MM-dd");
//
//		try {
//			originalDateEntered = dateFormatterWithTimestamp.parse(originalDateInDB.trim().substring(0, 10) + originalTimeInDB.trim());
//			logger.error("[convertDowntimeCaptureDoToDTo] : INFO  " + originalDateInDB.trim().substring(0, 10)  + "   " + originalTimeInDB.trim() + "   "
//					+ originalDateEntered);
//
//			if (startDateInDB != null) {
//				if (startTimeInDB != null) {
//					startDate = dateFormatterWithTimestamp.parse(startDateInDB.trim().substring(0, 10)  + startTimeInDB.trim());
//					logger.error(
//							"[convertDowntimeCaptureDoToDTo] : INFO  " + startTimeInDB.trim() + "   " + startDateInDB.trim().substring(0, 10)  + "   " + startDate);
//				} else {
//					startDate = dateFormatterWithoutTimestamp.parse(startDateInDB.trim());
//				}
//
//			}
//		} catch (ParseException e) {
//			logger.error("[convertDowntimeCaptureDoToDTo] : ERROR- Exception while Converting from String to Number " + e);
//			throw e;
//		}
//
//		downtimeCaptureDto.setChildCode("" + downtimeCaptureDo.getDowntimeCode());
//		downtimeCaptureDto.setDurationInHours(downtimeMinutes / 60);
//		downtimeCaptureDto.setDurationInMinutes(downtimeMinutes % 60);
//		downtimeCaptureDto.setOriginalDateEntered(originalDateEntered);
//		downtimeCaptureDto.setStartDate(startDate);
//		downtimeCaptureDto.setComments(comments);
//
//		logger.error("[convertDowntimeCaptureDoToDTo] : INFO downtimeCaptureDto " + downtimeCaptureDto);
//
//		return downtimeCaptureDto;
//	}
}
