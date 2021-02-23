package com.murphy.integration.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dao.EnersightProveMonthlyDao;
import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.dto.UIRequestDto;
import com.murphy.integration.dto.UIResponseDto;
import com.murphy.integration.interfaces.EnersightProveMonthlyLocal;
import com.murphy.integration.util.DBConnections;
import com.murphy.integration.util.ServicesUtil;

public class EnersightProveMonthly implements EnersightProveMonthlyLocal {

	private static final Logger logger = LoggerFactory.getLogger(EnersightProveMonthly.class);

	@Override
	public UIResponseDto fetchProveData(UIRequestDto uiRequestDto) {
		// TODO Auto-generated method stub
		//		logger.info("[fetchProveData] : INFO- Service Started");
		UIResponseDto uiResponseDto = new UIResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		EnersightProveMonthlyDao enersightProveMonthlyDao = null;
		Connection connection = null;
		Integer duration = uiRequestDto.getDuration();
		String locationType = uiRequestDto.getLocationType();
		List<String> locationCodeList = uiRequestDto.getLocationCodeList();

		if (uiRequestDto != null && locationType != null && locationCodeList != null && locationCodeList.size() > 0
				&& duration != null) {

			// connection = DBConnections.getProveConnection();
			// logger.error("[fetchProveData] : INFO- Connection " + connection);
			//
			// if (connection == null) {
			try {
				Map<String, String> uwiIdMap = getMuwiByLocationTypeAndCode(locationType, locationCodeList);
				//				logger.error("[fetchProveData] : INFO- Response from Location hierarchy " + uwiIdMap);

				if (uwiIdMap != null && uwiIdMap.size() > 0) {

					/* InvestigationNIP = Investigation Not In Progress */
					List<String> InvestigationNIPlocationCodeList = fetchLocationCodeInvestigation(
							new HashSet<>(uwiIdMap.values()));
					connection = DBConnections.createConnectionForProve();

					/* InquiryNIP = Inquiry Not In Progress */
					List<String> InquiryNIPlocationCodeList = fetchLocationCodeInquire(
							new HashSet<>(uwiIdMap.values()),uiRequestDto.getUserType());

					// }
					//					logger.error("[fetchProveData] : INFO- new connection is created " + connection);

					if (connection != null) {
						enersightProveMonthlyDao = new EnersightProveMonthlyDao();
						try {
							//							logger.info("[fetchProveData] : INFO- Connection to DB successful");
							uiResponseDto.setEnersightProveMonthlyDtoList(enersightProveMonthlyDao
									.fetchProveData(connection, uwiIdMap, duration, InvestigationNIPlocationCodeList,InquiryNIPlocationCodeList));
							responseMessage.setMessage("Successful");
							responseMessage.setStatus("SUCCESS");
							responseMessage.setStatusCode("0");

						} catch (Exception e) {
							logger.error("[fetchProveData] : ERROR- Exception while interacting with database " + e);
							responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
							responseMessage.setStatus("FAILURE");
							responseMessage.setStatusCode("1");
						} finally {
							try {
								connection.close();
							} catch (SQLException e) {
								logger.error("[fetchProveData] : ERROR- Exception while closing Connection " + e);
							}
						}
					} else {
						responseMessage.setMessage("Connection to Database is not possible");
						responseMessage.setStatus("FAILURE");
						responseMessage.setStatusCode("1");
					}

				} else {
					responseMessage.setMessage("No uwiIds for provided input");
					responseMessage.setStatus("FAILURE");
					responseMessage.setStatusCode("1");
				}
			} catch (Exception e) {
				logger.error("[fetchProveData] : ERROR- Exception while interacting with Hana database " + e);
				responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
				responseMessage.setStatus("FAILURE");
				responseMessage.setStatusCode("1");
			}
		} else {
			responseMessage.setMessage("Please provide all the mandatory details");
			responseMessage.setStatus("FAILURE");
			responseMessage.setStatusCode("1");
		}

		//		logger.info("[fetchProveData] : UIResponseDto " + uiResponseDto);
		uiResponseDto.setResponseMessage(responseMessage);
		return uiResponseDto;
	}

	@Override
	public List<String> fetchLocationCodeInvestigation(Set<String> locationCodeSet) throws Exception {

		//		logger.info("[fetchLocationCodeInvestigation] : INFO- Service Started");
		EnersightProveMonthlyDao enersightProveMonthlyDao = null;
		Connection connection = null;

		connection = DBConnections.createConnectionForHana();

		//		logger.error("[fetchLocationCodeInvestigation] : INFO- new connection is created " + connection);

		if (connection != null) {
			enersightProveMonthlyDao = new EnersightProveMonthlyDao();
			try {
				//				logger.info("[fetchLocationCodeInvestigation] : INFO- Connection to DB successful");

				return enersightProveMonthlyDao.fetchLocationCodeInvestigation(connection, locationCodeSet);

			} catch (Exception e) {
				logger.error(
						"[fetchLocationCodeInvestigation] : ERROR- Exception while interacting with database " + e);
				throw e;
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("[fetchLocationCodeInvestigation] : ERROR- Exception while closing Connection " + e);
				}
			}
		}

		return null;
	}

	@Override
	public Map<String, String> getMuwiByLocationTypeAndCode(String locationType, List<String> locationCodeList)
			throws Exception {

		//		logger.info("[getMuwiByLocationTypeAndCode] : INFO- Service Started");
		EnersightProveMonthlyDao enersightProveMonthlyDao = null;
		Connection connection = null;

		connection = DBConnections.createConnectionForHana();

		//		logger.error("[getMuwiByLocationTypeAndCode] : INFO- new connection is created " + connection);

		if (connection != null) {
			enersightProveMonthlyDao = new EnersightProveMonthlyDao();
			try {
				//				logger.info("[getMuwiByLocationTypeAndCode] : INFO- Connection to DB successful");

				return enersightProveMonthlyDao.getMuwiByLocationTypeAndCode(connection, locationType,
						locationCodeList);

			} catch (Exception e) {
				logger.error("[getMuwiByLocationTypeAndCode] : ERROR- Exception while interacting with database " + e);
				throw e;
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("[getMuwiByLocationTypeAndCode] : ERROR- Exception while closing Connection " + e);
				}
			}
		}
		return null;
	}

	@Override
	public UIResponseDto fetchProveDailyData() {

		//		logger.info("[fetchProveDailyData] : INFO- Service Started");
		UIResponseDto uiResponseDto = new UIResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		EnersightProveMonthlyDao enersightProveMonthlyDao = null;
		Connection connection = null;

		try {

			connection = DBConnections.createConnectionForProve();
			if (connection != null) {
				enersightProveMonthlyDao = new EnersightProveMonthlyDao();

				try {

					//					logger.info("[fetchProveDailyData] : INFO- Connection to DB successful");
					uiResponseDto
					.setEnersightProveDailyDtoList(enersightProveMonthlyDao.fetchProveDailyData(connection));
					responseMessage.setMessage("Successful");
					responseMessage.setStatus("SUCCESS");
					responseMessage.setStatusCode("0");

				} catch (Exception e) {
					logger.error("[fetchProveDailyData] : ERROR- Exception while interacting with database " + e);
					responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
					responseMessage.setStatus("FAILURE");
					responseMessage.setStatusCode("1");

				} finally {
					try {
						connection.close();
					} catch (SQLException e) {
						logger.error("[fetchProveDailyData] : ERROR- Exception while closing Connection " + e);
					}
				}

			} else {
				responseMessage.setMessage("Connection to Database is not possible");
				responseMessage.setStatus("FAILURE");
				responseMessage.setStatusCode("1");
			}

		} catch (Exception e) {
			logger.error("[fetchProveDailyData] : ERROR- Exception while interacting with Prove database " + e);
			responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
			responseMessage.setStatus("FAILURE");
			responseMessage.setStatusCode("1");
		}
		//		logger.info("[fetchProveDailyData] : UIResponseDto " + uiResponseDto);
		uiResponseDto.setResponseMessage(responseMessage);
		return uiResponseDto;
	}

	@Override
	public List<String> fetchLocationCodeInquire(Set<String> locationCodeSet,String userType) throws Exception {

		//		logger.info("[fetchLocationCodeInvestigation] : INFO- Service Started");
		EnersightProveMonthlyDao enersightProveMonthlyDao = null;
		Connection connection = null;

		connection = DBConnections.createConnectionForHana();

		//		logger.error("[fetchLocationCodeInvestigation] : INFO- new connection is created " + connection);

		if (connection != null) {
			enersightProveMonthlyDao = new EnersightProveMonthlyDao();
			try {
				//				logger.info("[fetchLocationCodeInvestigation] : INFO- Connection to DB successful");

				return enersightProveMonthlyDao.fetchLocationCodeInquire(connection, locationCodeSet,userType);

			} catch (Exception e) {
				logger.error(
						"[fetchLocationCodeInvestigation] : ERROR- Exception while interacting with database " + e);
				throw e;
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("[fetchLocationCodeInvestigation] : ERROR- Exception while closing Connection " + e);
				}
			}
		}

		return null;
	}

	@Override
	public List<String> getMuwiWherVarLessThanThres(List<String> muwiList , double duration , double thresholdPercent , String version) {
		List<String> responseList = null;
		Connection connection = null;
		EnersightProveMonthlyDao enersightProveMonthlyDao = null;
		if(!ServicesUtil.isEmpty(muwiList)){
			connection = DBConnections.createConnectionForProve();
			if (connection != null) {
				enersightProveMonthlyDao = new EnersightProveMonthlyDao();
				responseList =	enersightProveMonthlyDao.getMuwiWherVarLessThanThres(connection, muwiList, duration , thresholdPercent ,version);
			}else {
				logger.error("[Murphy][EnersightProveMonthly][getMuwiWherVarLessThanThres][error] Connection to Database is not possible");
			}
		}
		return responseList;
	}
	
	@Override
	public Map<String, String> fetchFracData(Set<String> uwiIdSet) {
		Connection connection = DBConnections.createConnectionForProve();
		EnersightProveMonthlyDao enersightProveMonthlyDao = new EnersightProveMonthlyDao();
		//October 18 OL Monthly
		try {
			 return enersightProveMonthlyDao.fetchFracBOED(connection, uwiIdSet);
		} catch (Exception e) {
			logger.error("[Murphy][EnersightProveMonthly][fetchFracData][error]failed to fetch data for Frac BOED");
		}
		// TODO Review why send instead of empty object?
		return new HashMap<String, String>();
		
	}

	@Override
	public Map<String, String> fetchPiggingValue(Set<String> uwiIdSet) {
		Connection connection = DBConnections.createConnectionForProve();
		EnersightProveMonthlyDao enersightProveMonthlyDao = new EnersightProveMonthlyDao();
		//October 18 OL Monthly
		try {
			return enersightProveMonthlyDao.fetchPiggingValue(connection, uwiIdSet);
		} catch (Exception e) {
			logger.error("[Murphy][EnersightProveMonthly][fetchFracData][error]failed to fetch data for Frac BOED");
		}
		// TODO Review why send instead of empty object?
		return new HashMap<String, String>();
	}
	

}
