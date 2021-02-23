package com.murphy.downtime.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.murphy.downtime.dao.DowntimeCaptureDao;
import com.murphy.downtime.dto.DowntimeCaptureDto;
import com.murphy.downtime.dto.DowntimeCaptureFetchResponseDto;
import com.murphy.downtime.dto.ResponseMessage;
import com.murphy.downtime.entity.DowntimeCaptureDo;
import com.murphy.downtime.service.interfaces.DowntimeCaptureLocal;
import com.murphy.downtime.util.DBConnection;
import com.murphy.downtime.util.DowntimeConstant;
import com.murphy.downtime.util.DowntimeServicesUtil;

@Service("downtimeCapture")
public class DowntimeCapture implements DowntimeCaptureLocal {

	private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);

	@Override
	public DowntimeCaptureFetchResponseDto fetchRecordForProvidedUwiIdAndDate(Date originalDateEntered, String uwiId) {
		logger.info("[insertOrUpdateCounts] : INFO- Service Started");

		DowntimeCaptureFetchResponseDto downtimeCaptureFetchResponseDto = new DowntimeCaptureFetchResponseDto();

		ResponseMessage responseMessage = new ResponseMessage();

		if (originalDateEntered != null && uwiId != null && uwiId.length() > 0) {
			DowntimeCaptureDao downtimeCaptureDao = null;

//			try {
//				ServicesUtil.setupSOCKS();
//			} catch (Exception e) {
//				logger.error("[insertOrUpdateCounts] : ERROR- Exception while setting SOCKS " + e);
//			}
			Connection connection = DBConnection.getConnection();

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
					try {
						DowntimeServicesUtil.unSetupSOCKS();
					} catch (Exception e) {
						logger.error("[insertOrUpdateCounts] : ERROR- Exception while unsetting SOCKS " + e);
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

	@Override
	public ResponseMessage insertOrUpdateCounts(DowntimeCaptureDto downtimeCaptureDto) {
		logger.info("[insertOrUpdateCounts] : INFO- Service Started");
		ResponseMessage responseMessage = new ResponseMessage();
		if (downtimeCaptureDto != null && downtimeCaptureDto.getOriginalDateEntered() != null && downtimeCaptureDto.getUwiId() != null
				&& downtimeCaptureDto.getUwiId().length() > 0) {
			DowntimeCaptureDao downtimeCaptureDao = null;
			DowntimeCaptureDo downtimeCaptureDoUI = null;

//			try {
//				ServicesUtil.setupSOCKS();
//			} catch (Exception e) {
//				logger.error("[insertOrUpdateCounts] : ERROR- Exception while setting SOCKS " + e);
//			}
			Connection connection = DBConnection.getConnection();

			if (connection != null) {
				downtimeCaptureDao = new DowntimeCaptureDao();
				try {
					logger.info("[insertOrUpdateCounts] : INFO- Connection to DB successful");

					downtimeCaptureDoUI = convertDowntimeCaptureDtoToDo(downtimeCaptureDto);

					int merrickId = downtimeCaptureDao.fetchMerrickFromDB(connection, downtimeCaptureDto.getUwiId().trim());
					logger.error("[insertOrUpdateCounts] : INFO  - merrickId " + merrickId);

					if (merrickId >= 0) {

						downtimeCaptureDoUI.setMerrickId(merrickId);

						DowntimeCaptureDo downtimeCaptureDoFetchFromDB = downtimeCaptureDao.getDataFromDB(connection,
								downtimeCaptureDoUI.getMerrickId(), downtimeCaptureDoUI.getOriginalDateEntered());

						if (downtimeCaptureDoFetchFromDB != null) {
							// downtimeCaptureDao.deleteDataFromDB(connection,
							// downtimeCaptureDo.getMerrickId(),
							// downtimeCaptureDo.getOriginalDateEntered());
							// downtimeCaptureDao.getDataFromDB(connection,
							// downtimeCaptureDo.getMerrickId(),
							// downtimeCaptureDo.getOriginalDateEntered());
							// downtimeCaptureDao.insertDataInDB(connection, downtimeCaptureDo);

							downtimeCaptureDao.updateDataInDB(connection, downtimeCaptureDoUI);

						} else {
							downtimeCaptureDao.insertDataInDB(connection, downtimeCaptureDoUI);
						}

						// for testing, will remove later
						downtimeCaptureDao.getDataFromDB(connection, downtimeCaptureDoUI.getMerrickId(),
								downtimeCaptureDoUI.getOriginalDateEntered());

						connection.commit();
						responseMessage.setMessage("Successful");
						responseMessage.setStatus("true");
					} else {
						responseMessage.setMessage("No merrick Id is maintained for prvided uwiId");
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
					try {
						DowntimeServicesUtil.unSetupSOCKS();
					} catch (Exception e) {
						logger.error("[insertOrUpdateCounts] : ERROR- Exception while unsetting SOCKS " + e);
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
		// String dateInHourFormat = null;
		String startDate = null;
		String endDate = null;
		String startTime = null;
		String endTime = null;
		int downtimeCode;

		try {
			if (downtimeCaptureDto.getChildCode() != null) {
				downtimeCode = Integer.parseInt(downtimeCaptureDto.getChildCode());
			} else {
				downtimeCode = Integer.parseInt(downtimeCaptureDto.getParentCode());
			}
		} catch (NumberFormatException e) {
			logger.error("[convertDowntimeCaptureDtoToDo] : ERROR- Exception while Converting from String to Number " + e);
			throw e;
		}

		float downtimeHours = downtimeCaptureDto.getDurationInHours() + ((float) downtimeCaptureDto.getDurationInMinutes() / 60);

		SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm:ss");

		// calendar.setTime(originalDateEntered);
		// int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		// if (hourOfDay < 7) {
		// calendar.add(Calendar.DAY_OF_MONTH, -1);
		// }
		// originalDateEntered = calendar.getTime();

		dateInYearFormat = dateformatter.format(originalDateEntered);

		// dateInHourFormat = timeformatter.format(originalDateEntered);

		if (StartDateEnteredInUI != null) {
			startDate = dateformatter.format(StartDateEnteredInUI);
			startTime = timeformatter.format(StartDateEnteredInUI);

			calendar.setTime(StartDateEnteredInUI);
			calendar.add(Calendar.MINUTE, (int) (downtimeHours * 60));

			StartDateEnteredInUI = calendar.getTime();
			endDate = dateformatter.format(StartDateEnteredInUI);
			endTime = timeformatter.format(StartDateEnteredInUI);
		}

		downtimeCaptureDo.setObjectType(DowntimeConstant.OBJECT_TYPE);
		downtimeCaptureDo.setOriginalDateEntered(dateInYearFormat);
		downtimeCaptureDo.setOriginalTimeEntered(DowntimeConstant.ORIGINAL_TIME_ENTERED);
		downtimeCaptureDo.setUpDownFlag(DowntimeConstant.UPDOWN_FLAG);
		downtimeCaptureDo.setDateEntryFlag(DowntimeConstant.DATEENTRY_FLAG);
		downtimeCaptureDo.setDowntimeCode(downtimeCode);
		downtimeCaptureDo.setDowntimeHours(downtimeHours);
		downtimeCaptureDo.setRepairCosts(DowntimeConstant.REPAIR_COSTS);
		downtimeCaptureDo.setLostProduction(DowntimeConstant.LOST_PRODUCTION);
		downtimeCaptureDo.setCalCDowntimeFlag(DowntimeConstant.CALC_DOWNTIME_FLAG);
		downtimeCaptureDo.setStartDate(startDate);
		downtimeCaptureDo.setStartTime(startTime);
		downtimeCaptureDo.setEndDate(endDate);
		downtimeCaptureDo.setEndTime(endTime);
		downtimeCaptureDo.setStartProductionDate(DowntimeConstant.START_PRODUCTION_DATE);
		downtimeCaptureDo.setEndProductionDate(DowntimeConstant.END_PRODUCTION_DATE);
		downtimeCaptureDo.setComments(comments);
		downtimeCaptureDo.setReason(DowntimeConstant.REASON);
		downtimeCaptureDo.setMessageSendFlag(DowntimeConstant.MESSAGE_SEND_FLAG);
		downtimeCaptureDo.setDestinationPerson(DowntimeConstant.DESTINATION_PERSON);
		downtimeCaptureDo.setRioDowntimeId(DowntimeConstant.RIO_DOWNTIME_ID);
		downtimeCaptureDo.setLastDayHoursDown(DowntimeConstant.LAST_DAY_HOURS_DOWN);
		downtimeCaptureDo.setDeleteFlag(DowntimeConstant.DELETE_FLAG);
		downtimeCaptureDo.setLastTransmission(DowntimeConstant.LAST_TRANSMISSION);
		downtimeCaptureDo.setLastLoadDate(DowntimeConstant.LAST_LOAD_DATE);
		downtimeCaptureDo.setLastLoadTime(DowntimeConstant.LAST_LOAD_TIME);
		downtimeCaptureDo.setTransmitFlag(DowntimeConstant.TRANSMIT_FLAG);
		downtimeCaptureDo.setDateTimeStamp(DowntimeConstant.DATETIMESTAMP);
		downtimeCaptureDo.setbLogicDateStamp(DowntimeConstant.BLOGICDATESTAMP);
		downtimeCaptureDo.setbLogicTimeStamp(DowntimeConstant.BLOGICTIMESTAMP);
		downtimeCaptureDo.setUserDateStamp(DowntimeConstant.USERDATESTAMP);
		downtimeCaptureDo.setUserTimeStamp(DowntimeConstant.USERTIMESTAMP);
		downtimeCaptureDo.setUserId(DowntimeConstant.USERID);

		logger.error("[convertDowntimeCaptureDtoToDo] : INFO  - downtimeCaptureDo" + downtimeCaptureDo);
		return downtimeCaptureDo;
	}

	private DowntimeCaptureDto convertDowntimeCaptureDoToDTo(DowntimeCaptureDo downtimeCaptureDo) throws ParseException {
		DowntimeCaptureDto downtimeCaptureDto = new DowntimeCaptureDto();

		Date originalDateEntered = null;
		Date startDate = null;

		float downtimeHours = downtimeCaptureDo.getDowntimeHours();
		int downtimeMinutes = (int) (downtimeHours * 60);

		String originalDateInDB = downtimeCaptureDo.getOriginalDateEntered();
		String originalTimeInDB = downtimeCaptureDo.getOriginalTimeEntered();
		String startDateInDB = downtimeCaptureDo.getStartDate();
		String startTimeInDB = downtimeCaptureDo.getStartTime();
		String comments = downtimeCaptureDo.getComments();

		SimpleDateFormat dateFormatterWithTimestamp = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		SimpleDateFormat dateFormatterWithoutTimestamp = new SimpleDateFormat("yyyy-MM-dd");

		try {
			originalDateEntered = dateFormatterWithTimestamp.parse(originalDateInDB.trim().substring(0, 10) + originalTimeInDB.trim());
			logger.error("[convertDowntimeCaptureDoToDTo] : INFO  " + originalDateInDB.trim().substring(0, 10)  + "   " + originalTimeInDB.trim() + "   "
					+ originalDateEntered);

			if (startDateInDB != null) {
				if (startTimeInDB != null) {
					startDate = dateFormatterWithTimestamp.parse(startDateInDB.trim().substring(0, 10)  + startTimeInDB.trim());
					logger.error(
							"[convertDowntimeCaptureDoToDTo] : INFO  " + startTimeInDB.trim() + "   " + startDateInDB.trim().substring(0, 10)  + "   " + startDate);
				} else {
					startDate = dateFormatterWithoutTimestamp.parse(startDateInDB.trim());
				}

			}
		} catch (ParseException e) {
			logger.error("[convertDowntimeCaptureDoToDTo] : ERROR- Exception while Converting from String to Number " + e);
			throw e;
		}

		downtimeCaptureDto.setChildCode("" + downtimeCaptureDo.getDowntimeCode());
		downtimeCaptureDto.setDurationInHours(downtimeMinutes / 60);
		downtimeCaptureDto.setDurationInMinutes(downtimeMinutes % 60);
		downtimeCaptureDto.setOriginalDateEntered(originalDateEntered);
		downtimeCaptureDto.setStartDate(startDate);
		downtimeCaptureDto.setComments(comments);

		logger.error("[convertDowntimeCaptureDoToDTo] : INFO downtimeCaptureDto " + downtimeCaptureDto);

		return downtimeCaptureDto;
	}
}
