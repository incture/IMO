package com.murphy.integration.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dao.FlareDowntimeCaptureDao;
import com.murphy.integration.dto.FlareCaptureFetchResponseDto;
import com.murphy.integration.dto.FlareDowntimeCaptureDto;
import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.entity.FlareDowntimeCaptureDo;
import com.murphy.integration.interfaces.FlareDowntimeCaptureLocal;
import com.murphy.integration.util.DBConnections;
import com.murphy.integration.util.ServicesUtil;

public class FlareDowntimeCapture implements FlareDowntimeCaptureLocal {
	
	private static final Logger logger = LoggerFactory.getLogger(CompressorDowntimeCapture.class);

	private FlareDowntimeCaptureDao flareDowntimeCaptureDao = new FlareDowntimeCaptureDao();

	@Override
	public ResponseMessage insertOrUpdateFlareDowntime(FlareDowntimeCaptureDto flareDowntimeCaptureDto) {
		
		logger.error("[Integration Services][FlareDowntimeCapture][insertOrUpdateFlareDowntime] : INFO- Service Started [flareDowntimeCaptureDto][Record Date]" + flareDowntimeCaptureDto.getRecordDate() );
		ResponseMessage responseMessage = new ResponseMessage();

//		String recordDate[]= flareDowntimeCaptureDto.getRecordDate().split(" ");
//		flareDowntimeCaptureDto.setRecordDate(recordDate[0] + " 00:00:00");
		if (flareDowntimeCaptureDto != null && flareDowntimeCaptureDto.getRecordDate() != null && flareDowntimeCaptureDto.getFlareCode() != null && flareDowntimeCaptureDto.getMerrickId() > 0) {


			Connection connection = DBConnections.createConnectionForProcount();

			if (connection != null) {
				try {
					logger.error("[insertOrUpdateFlareDowntime] : INFO- Connection to DB successful");

					/*int merrickId = downtimeCaptureDao.fetchMerrickFromDB(connection, downtimeCaptureDto.getUwiId().trim());
					logger.error("[insertOrUpdateCounts] : INFO  - merrickId " + merrickId);*/
					if (flareDowntimeCaptureDto.getMerrickId() >= 0) {
						FlareDowntimeCaptureDo downtimeCaptureDoFetchFromDB = flareDowntimeCaptureDao.getDataFromDB(connection,
								flareDowntimeCaptureDto.getMerrickId(), flareDowntimeCaptureDto.getRecordDate());
						logger.error("[Procount][Flare][downtimeCaptureDoFetchFromDB] :" + downtimeCaptureDoFetchFromDB);
						if (downtimeCaptureDoFetchFromDB != null) {
							flareDowntimeCaptureDao.updateDataInDB(connection, flareDowntimeCaptureDto);

						} else {
							flareDowntimeCaptureDao.insertDataInDB(connection, flareDowntimeCaptureDto);
						}

						connection.commit();
						responseMessage.setMessage("Successful");
						responseMessage.setStatus("true");
					} else {
						responseMessage.setMessage("No merrick Id is maintained");
						responseMessage.setStatus("false");

					}
				} catch (Exception e) {
					try {
						connection.rollback();
					} catch (SQLException sqlException) {
						logger.error("[insertOrUpdateFlareDowntime] : ERROR- Rollback transactions because of exception " + sqlException);
					}
					logger.error("[insertOrUpdateFlareDowntime] : ERROR- Exception while interacting with database " + e);
					responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
					responseMessage.setStatus("false");
				} finally {
					try {
						connection.close();
					} catch (SQLException e) {
						logger.error("[insertOrUpdateFlareDowntime] : ERROR- Exception while closing Connection " + e);
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
	
	// SOC: Rework For Downtime fetching data for list of Merrick Ids for location
		// History module from ProCount
		public FlareCaptureFetchResponseDto fetchRecordForMerrickIds(List<String> listOfMerrick, String fromDate,
				String toDate, int page, int page_size) {
			FlareCaptureFetchResponseDto flareCaptureFetchResponseDto = new FlareCaptureFetchResponseDto();

			ResponseMessage responseMessage = new ResponseMessage();

			if ((!ServicesUtil.isEmpty(listOfMerrick)) && (listOfMerrick.size() > 0)) {
				FlareDowntimeCaptureDao flareDowntimeCaptureDao = null;
				FlareDowntimeCaptureDto flareDowntimeDto = null;
				int countResult = 0;
				List<FlareDowntimeCaptureDto> flListDto = new ArrayList<FlareDowntimeCaptureDto>();
				Connection connection = DBConnections.createConnectionForProcount();

				if (connection != null) {
					flareDowntimeCaptureDao = new FlareDowntimeCaptureDao();
					
					try {
						logger.error("[fetchRecordForMerrickIds] : INFO- Connection to DB successful");
						if (listOfMerrick.size() > 0) {
	                        logger.error("listOfMerrick flare "+listOfMerrick);
	                        List<FlareDowntimeCaptureDo> flareCaptureDoFetchFromDBList = flareDowntimeCaptureDao
									.getDownTimeDetailsFromProCount(connection, listOfMerrick, fromDate, toDate, page,page_size);

							if (!ServicesUtil.isEmpty(flareCaptureDoFetchFromDBList)) {

								for (FlareDowntimeCaptureDo doItem : flareCaptureDoFetchFromDBList) {
									flareDowntimeDto = convertDowntimeCaptureDoToDTo(doItem);
									flListDto.add(flareDowntimeDto);
								}
								logger.error("list flListDto :" +flListDto);
								
								countResult = flareDowntimeCaptureDao.getTotalCount(connection, listOfMerrick, fromDate, toDate);
								flareCaptureFetchResponseDto.setTotalCount(countResult);
								flareCaptureFetchResponseDto.setDcDtoList(flListDto);
								responseMessage.setMessage("Successful");
								responseMessage.setStatus("true");
							} else {
								responseMessage.setMessage("No record is present for provided values.");
								responseMessage.setStatus("false");
							}

						} else {
							responseMessage.setMessage("No merrick Id is provided.");
							responseMessage.setStatus("false");
						}

					} catch (Exception e) {
						logger.error(
								"[fetchRecordForMerrickIds] : ERROR- Exception while interacting with database " + e.getMessage());
						responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
						responseMessage.setStatus("false");
					} finally {
						try {
							connection.close();
						} catch (SQLException e) {
							logger.error("[fetchRecordForMerrickIds] : ERROR- Exception while closing Connection " + e);
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
					"[fetchRecordForMerrickIds] : downtimeCaptureFetchResponseDto " + flareCaptureFetchResponseDto);
			flareCaptureFetchResponseDto.setResponseMessage(responseMessage);
			return flareCaptureFetchResponseDto;
		}
		
		public FlareDowntimeCaptureDto convertDowntimeCaptureDoToDTo(FlareDowntimeCaptureDo flareDowntimeCaptureDo) {
			FlareDowntimeCaptureDto flareDowntimeCaptureDto = new FlareDowntimeCaptureDto();
			
		    int merrickId = (ServicesUtil.isEmpty(flareDowntimeCaptureDo.getMerrickId()) ? null : flareDowntimeCaptureDo.getMerrickId());
			float flareVolume = (ServicesUtil.isEmpty(flareDowntimeCaptureDo.getFlareVolume()) ? null : flareDowntimeCaptureDo.getFlareVolume());
		    String flareCode = (ServicesUtil.isEmpty(flareDowntimeCaptureDo.getFlareCode()) ? null : flareDowntimeCaptureDo.getFlareCode());
			String recordDate = (ServicesUtil.isEmpty(flareDowntimeCaptureDo.getRecordDate()) ? null : flareDowntimeCaptureDo.getRecordDate());

			flareDowntimeCaptureDto.setMerrickId(merrickId);
			flareDowntimeCaptureDto.setFlareVolume(flareVolume);
			flareDowntimeCaptureDto.setFlareCode(flareCode);
			flareDowntimeCaptureDto.setRecordDate(recordDate);
		

			logger.error("[convertDowntimeCaptureDoToDTo] : INFO flareDowntimeCaptureDto " + flareDowntimeCaptureDto);

			return flareDowntimeCaptureDto;
		}

}
