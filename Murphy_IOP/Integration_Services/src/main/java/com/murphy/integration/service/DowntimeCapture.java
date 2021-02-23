package com.murphy.integration.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dao.DowntimeCaptureDao;
import com.murphy.integration.dto.DowntimeCaptureDto;
import com.murphy.integration.dto.DowntimeCaptureFetchResponseDto;
import com.murphy.integration.dto.DowntimeCapturedCADto;
import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.entity.DowntimeCaptureDo;
import com.murphy.integration.interfaces.DowntimeCaptureLocal;
import com.murphy.integration.util.DBConnections;
import com.murphy.integration.util.ProcountConstant;
import com.murphy.integration.util.ProdViewConstant;
import com.murphy.integration.util.ServicesUtil;

public class DowntimeCapture implements DowntimeCaptureLocal {

	private static final Logger logger = LoggerFactory.getLogger(DowntimeCapture.class);

	@Override
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

						DowntimeCaptureDo downtimeCaptureDoFetchFromDB = downtimeCaptureDao.getDataFromDB(connection,
								merrickId, originalDateInDB);

						if (downtimeCaptureDoFetchFromDB != null) {
							DowntimeCaptureDto downtimeCaptureDto = convertDowntimeCaptureDoToDTo(
									downtimeCaptureDoFetchFromDB);
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

	@Override
	public ResponseMessage insertOrUpdateCounts(DowntimeCaptureDto downtimeCaptureDto) {
		logger.info("[insertOrUpdateCounts] : INFO- Service Started");
		ResponseMessage responseMessage = new ResponseMessage();
		if (downtimeCaptureDto != null && downtimeCaptureDto.getOriginalDateEntered() != null
				&& downtimeCaptureDto.getUwiId() != null && downtimeCaptureDto.getUwiId().length() > 0) {
			DowntimeCaptureDao downtimeCaptureDao = null;
			DowntimeCaptureDo downtimeCaptureDoUI = null;

			Connection connection = DBConnections.createConnectionForProcount();

			if (connection != null) {
				downtimeCaptureDao = new DowntimeCaptureDao();
				try {
					logger.info("[insertOrUpdateCounts] : INFO- Connection to DB successful");

					downtimeCaptureDoUI = convertDowntimeCaptureDtoToDo(downtimeCaptureDto);

					int merrickId = downtimeCaptureDao.fetchMerrickFromDB(connection,
							downtimeCaptureDto.getUwiId().trim());
					logger.error("[insertOrUpdateCounts] : INFO  - merrickId " + merrickId);

					if (merrickId >= 0) {

						downtimeCaptureDoUI.setMerrickId(merrickId);

						DowntimeCaptureDo downtimeCaptureDoFetchFromDB = downtimeCaptureDao.getDataFromDB(connection,
								downtimeCaptureDoUI.getMerrickId(), downtimeCaptureDoUI.getOriginalDateEntered());

						if (downtimeCaptureDoFetchFromDB != null) {
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
						logger.error("[insertOrUpdateCounts] : ERROR- Rollback transactions because of exception "
								+ sqlException);
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
		} else {
			responseMessage.setMessage("Please provide all the necessary values.");
			responseMessage.setStatus("false");
		}
		return responseMessage;
	}

	private DowntimeCaptureDo convertDowntimeCaptureDtoToDo(DowntimeCaptureDto downtimeCaptureDto) {
		
		/*From UI
		createdAt: "2019-08-16 11:40:12"
			createdBy: "test_roc1@gmail.com"
			downtimeCode: 946
			downtimeText: "EFS Subsurface-Planned"
			durationByRocHour: 1
			durationByRocMinute: 15*/
		DowntimeCaptureDo downtimeCaptureDo = new DowntimeCaptureDo();
		Date originalDateEntered = downtimeCaptureDto.getOriginalDateEntered();
		Date StartDateEnteredInUI = downtimeCaptureDto.getStartDate();
		//downtimeCaptureDto.getStartDate() Fri Aug 16 10:51:00 UTC 2019
		logger.error("StartDateEnteredInUI 192line "+StartDateEnteredInUI);
		//StartDateEnteredInUI 192line Fri Aug 16 11:40:00 UTC 2019
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
			logger.error(
					"[convertDowntimeCaptureDtoToDo] : ERROR- Exception while Converting from String to Number " + e);
			throw e;
		}

		float downtimeHours = downtimeCaptureDto.getDurationInHours()
				+ ((float) downtimeCaptureDto.getDurationInMinutes() / 60);

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
			//StartDateEnteredInUI 234line Fri Aug 16 11:40:00 UTC 2019
			startDate = dateformatter.format(StartDateEnteredInUI);
			//2019-08-16
			startTime = timeformatter.format(StartDateEnteredInUI);
			//startTime 11:40:00

			calendar.setTime(StartDateEnteredInUI);
			calendar.add(Calendar.MINUTE, (int) (downtimeHours * 60));

			StartDateEnteredInUI = calendar.getTime();
			//StartDateEnteredInUI 244line Fri Aug 16 12:55:00 UTC 2019
			endDate = dateformatter.format(StartDateEnteredInUI);
			//endDate 2019-08-16
			endTime = timeformatter.format(StartDateEnteredInUI);
			//endTime 12:55:00
		}

		downtimeCaptureDo.setObjectType(ProcountConstant.OBJECT_TYPE);
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

	private DowntimeCaptureDto convertDowntimeCaptureDoToDTo(DowntimeCaptureDo downtimeCaptureDo)
			throws ParseException {
		DowntimeCaptureDto downtimeCaptureDto = new DowntimeCaptureDto();

		Date originalDateEntered = null;
		Date startDate = null;

		float downtimeHours = (ServicesUtil.isEmpty(downtimeCaptureDo.getDowntimeHours()) ? null
				: downtimeCaptureDo.getDowntimeHours());
		int downtimeMinutes = (int) (downtimeHours * 60);

		String originalDateInDB = (ServicesUtil.isEmpty(downtimeCaptureDo.getOriginalDateEntered()) ? null
				: downtimeCaptureDo.getOriginalDateEntered());
		String originalTimeInDB = (ServicesUtil.isEmpty(downtimeCaptureDo.getOriginalTimeEntered()) ? null
				: downtimeCaptureDo.getOriginalTimeEntered());
		String startDateInDB = (ServicesUtil.isEmpty(downtimeCaptureDo.getStartDate()) ? null
				: downtimeCaptureDo.getStartDate());
		String startTimeInDB = (ServicesUtil.isEmpty(downtimeCaptureDo.getStartTime()) ? null
				: downtimeCaptureDo.getStartTime());

		String comments = (ServicesUtil.isEmpty(downtimeCaptureDo.getComments()) ? null
				: downtimeCaptureDo.getComments());

		SimpleDateFormat dateFormatterWithTimestamp = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		SimpleDateFormat dateFormatterWithoutTimestamp = new SimpleDateFormat("yyyy-MM-dd");

		int merrickId = downtimeCaptureDo.getMerrickId();

		try {
			originalDateEntered = dateFormatterWithTimestamp
					.parse(originalDateInDB.trim().substring(0, 10) + originalTimeInDB.trim());
//			logger.error("[convertDowntimeCaptureDoToDTo] : INFO  " + originalDateInDB.trim().substring(0, 10) + "   "
//					+ originalTimeInDB.trim() + "   " + originalDateEntered);

			if (startDateInDB != null) {
				if (startTimeInDB != null) {
					startDate = dateFormatterWithTimestamp
							.parse(startDateInDB.trim().substring(0, 10) + startTimeInDB.trim());
					logger.error("[convertDowntimeCaptureDoToDTo] : INFO  " + startTimeInDB.trim() + "   "
							+ startDateInDB.trim().substring(0, 10) + "   " + startDate);
				} else {
					startDate = dateFormatterWithoutTimestamp.parse(startDateInDB.trim());
				}

			}
		} catch (ParseException e) {
			logger.error(
					"[convertDowntimeCaptureDoToDTo] : ERROR- Exception while Converting from String to Number " + e);
			throw e;
		}

		downtimeCaptureDto.setChildCode("" + downtimeCaptureDo.getDowntimeCode());
		downtimeCaptureDto.setDurationInHours(downtimeMinutes / 60);
		downtimeCaptureDto.setDurationInMinutes(downtimeMinutes % 60);
		downtimeCaptureDto.setOriginalDateEntered(originalDateEntered);
		downtimeCaptureDto.setStartDate(startDate);
		downtimeCaptureDto.setComments(comments);
		downtimeCaptureDto.setMerrickId(merrickId);

		logger.error("[convertDowntimeCaptureDoToDTo] : INFO downtimeCaptureDto " + downtimeCaptureDto);

		return downtimeCaptureDto;
	}

	// SOC: Rework For Downtime fetching data for list of Muwi Ids for location
	// History module from ProCount
	public DowntimeCaptureFetchResponseDto fetchRecordForProvidedUwiIds(List<String> muwiList, String fromDate,
			String toDate, int page, int page_size, boolean isCompressor) {
		DowntimeCaptureFetchResponseDto downtimeCaptureFetchResponseDto = new DowntimeCaptureFetchResponseDto();

		ResponseMessage responseMessage = new ResponseMessage();

		if ((!ServicesUtil.isEmpty(muwiList)) && (muwiList.size() > 0)) {
			DowntimeCaptureDao downtimeCaptureDao = null;
			DowntimeCaptureDto downtimeCaptureDto = null;
			int countResult = 0;
			List<DowntimeCaptureDto> dcListDto = new ArrayList<DowntimeCaptureDto>();
			Connection connection = DBConnections.createConnectionForProcount();

			if (connection != null) {
				downtimeCaptureDao = new DowntimeCaptureDao();
				
				try {
					logger.error("[fetchRecordForProvidedUwiIds] : INFO- Connection to DB successful");
					logger.error("isCompressor : "+ isCompressor);
					List<String> listOfMerrick = null;
					if(isCompressor){
						listOfMerrick = muwiList;
					}
					else {
					listOfMerrick = downtimeCaptureDao.getMerrickIdListFromProCount(connection, muwiList);
					}

					if (listOfMerrick.size() > 0) {
                        logger.error("listOfMerrick "+listOfMerrick);
						List<DowntimeCaptureDo> downtimeCaptureDoFetchFromDBList = downtimeCaptureDao
								.getDownTimeDetailsFromProCount(connection, listOfMerrick, fromDate, toDate, page,
										page_size);

						if (!ServicesUtil.isEmpty(downtimeCaptureDoFetchFromDBList)) {

							for (DowntimeCaptureDo doItem : downtimeCaptureDoFetchFromDBList) {
								downtimeCaptureDto = convertDowntimeCaptureDoToDTo(doItem);
								dcListDto.add(downtimeCaptureDto);
							}
							logger.error("list dcListDto :" +dcListDto);
							
							countResult = downtimeCaptureDao.getTotalCount(connection, listOfMerrick, fromDate, toDate);
							// downtimeCaptureDto.setUwiId(uwiId.trim());
							downtimeCaptureFetchResponseDto.setTotalCount(countResult);
							downtimeCaptureFetchResponseDto.setDcDtoList(dcListDto);
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
					logger.error(
							"[fetchRecordForProvidedUwiIds] : ERROR- Exception while interacting with database " + e.getMessage());
					responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
					responseMessage.setStatus("false");
				} finally {
					try {
						connection.close();
					} catch (SQLException e) {
						logger.error("[fetchRecordForProvidedUwiIds] : ERROR- Exception while closing Connection " + e);
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
		logger.error(
				"[fetchRecordForProvidedUwiIds] : downtimeCaptureFetchResponseDto " + downtimeCaptureFetchResponseDto);
		downtimeCaptureFetchResponseDto.setResponseMessage(responseMessage);
		return downtimeCaptureFetchResponseDto;
	}

	@Override
	public ResponseMessage insertOrUpdateDataToProdView(DowntimeCaptureDto downtimeCaptureDto,String location) {
		logger.info("[insertOrUpdateDataToProdView Edited] : INFO- Service Started");
		ResponseMessage responseMessage = new ResponseMessage();
		if (downtimeCaptureDto != null && downtimeCaptureDto.getOriginalDateEntered() != null
				&& downtimeCaptureDto.getUwiId() != null && downtimeCaptureDto.getUwiId().length() > 0) {
			DowntimeCaptureDao downtimeCaptureDao = null;
			DowntimeCapturedCADto downtimeCaptureDoUI = null;

			Connection connection = DBConnections.createConnectionForProdView();

			if (connection != null) {
				downtimeCaptureDao = new DowntimeCaptureDao();
				try {
					logger.info("[insertOrUpdateDataToProdView] : INFO- Connection to DB successful");

					downtimeCaptureDoUI = convertDwnTmeProdDtoToDo(downtimeCaptureDto);
					logger.error("DownTimeCaptureDoUI from convertDwnTmeProdDtoToDo"+downtimeCaptureDoUI);

					Map<String,String> pvUnitComp = downtimeCaptureDao.fetchIdFromPvtUnitComp(connection,
							downtimeCaptureDto.getUwiId().trim());
					logger.error("[insertOrUpdateDataToProdView] : INFO  - pvUnitCompMap " + pvUnitComp.toString());

					if (!ServicesUtil.isEmpty(pvUnitComp) && !ServicesUtil.isEmpty(pvUnitComp.get("idflownet")) && !ServicesUtil.isEmpty(pvUnitComp.get("idrec"))) {

						downtimeCaptureDoUI.setIdFlownet(pvUnitComp.get("idflownet"));
						downtimeCaptureDoUI.setIdecParent(pvUnitComp.get("idrec"));

						DowntimeCapturedCADto downtimeCaptureDoFetchFromDB = downtimeCaptureDao.getDataFromDwntTmeTable(connection,
								downtimeCaptureDoUI);
						logger.error("downtimeCaptureDoFetchFromDB from getDataFromDwntTmeTable"+downtimeCaptureDoFetchFromDB);


						if (downtimeCaptureDoFetchFromDB != null) {
							downtimeCaptureDoUI.setSysCreateDate(downtimeCaptureDoFetchFromDB.getSysCreateDate());
							downtimeCaptureDoUI.setIdrec(downtimeCaptureDoFetchFromDB.getIdrec());
							downtimeCaptureDoUI.setComments(downtimeCaptureDoFetchFromDB.getComments());
							logger.error("downtimeCaptureDoUI"+downtimeCaptureDoUI);
							downtimeCaptureDao.updateDataInProdViewDB(connection, downtimeCaptureDoUI,location);

						} else {
							logger.error("downtimeCaptureDoUI InsertData"+downtimeCaptureDoUI);
							downtimeCaptureDao.insertDataInProdViewDB(connection, downtimeCaptureDoUI);
						}

						connection.commit();
						responseMessage.setMessage("Successful");
						responseMessage.setStatus("true");
					} else {
						responseMessage.setMessage("Mapping Id is missing for wellId- "+downtimeCaptureDto.getUwiId());
						responseMessage.setStatus("false");

					}
				} catch (Exception e) {
					try {
						connection.rollback();
					} catch (SQLException sqlException) {
						logger.error("[insertOrUpdateCounts] : ERROR- Rollback transactions because of exception "
								+ sqlException);
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
		} else {
			responseMessage.setMessage("Please provide all the necessary values.");
			responseMessage.setStatus("false");
		}
		return responseMessage;
	}

	private DowntimeCapturedCADto convertDwnTmeProdDtoToDo(DowntimeCaptureDto dwnTmeCanadaDto){
		logger.error("[convertDwnTmeProdDtoToDo] : INFO  - downtimeCaptureDo" + dwnTmeCanadaDto);

		DowntimeCapturedCADto canadaDto=new DowntimeCapturedCADto();
		Date StartDateEnteredInUI = dwnTmeCanadaDto.getStartDate();
		Date originalDateEntered=dwnTmeCanadaDto.getOriginalDateEntered();
		Calendar calendar = Calendar.getInstance();
		String startDate = null;
		String endDate = null;
		String sysCreatedDate=null;
		
		float totalHours = dwnTmeCanadaDto.getDurationInHours()
				+ ((float) dwnTmeCanadaDto.getDurationInMinutes() / 60);
		float downtimeHours=totalHours/24;
//		downtimeHours=(float) (Math.round(downtimeHours * 100.0) / 100.0);
		SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sysCreateDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


		if (StartDateEnteredInUI != null) {
			//StartDateEnteredInUI 234line Fri Aug 16 11:40:00 UTC 2019
			startDate = dateformatter.format(StartDateEnteredInUI);
			
			calendar.setTime(StartDateEnteredInUI);
			calendar.add(Calendar.MINUTE, (int) (totalHours * 60));
			logger.error("[convertDwnTmeProdDtoToDo] : INFO  - StartDateEnteredInUI" + StartDateEnteredInUI+"TotalHours"+totalHours*60);

			StartDateEnteredInUI = calendar.getTime();
			logger.error("[convertDwnTmeProdDtoToDo] : INFO  - endDate in StartDateEnteredInUI" + StartDateEnteredInUI);

			endDate = dateformatter.format(StartDateEnteredInUI);
			logger.error("endDate" + StartDateEnteredInUI);

		}
		sysCreatedDate=sysCreateDateFormatter.format(originalDateEntered);
		canadaDto.setMuwi(dwnTmeCanadaDto.getUwiId());
		canadaDto.setTypDownTime(ProdViewConstant.DOWNTME_TYPE);
		canadaDto.setCodeDownTime1(dwnTmeCanadaDto.getCodeDownTime1());
		canadaDto.setCodeDownTime2(dwnTmeCanadaDto.getCodeDownTime2());
		canadaDto.setDurationDownTmeStartDay(downtimeHours);
		canadaDto.setDurationDownCalc(downtimeHours);
		canadaDto.setSysModDate(sysCreatedDate);
		canadaDto.setSysCreateDate(sysCreatedDate);
		canadaDto.setSysLockDate(sysCreatedDate);
		canadaDto.setDowntmeStart(startDate);
		canadaDto.setDowntmeEnd(endDate);
		canadaDto.setComments(dwnTmeCanadaDto.getComments());
		canadaDto.setSysModUser(ProdViewConstant.SVC_IOP);
		canadaDto.setSysCreateUser(ProdViewConstant.SVC_IOP);
		

		logger.error("[convertDowntimeCaptureDtoToDo] : INFO  - canadaDto" + canadaDto+"sysCreatedDate"+sysCreatedDate);
		return canadaDto;
	}
}
