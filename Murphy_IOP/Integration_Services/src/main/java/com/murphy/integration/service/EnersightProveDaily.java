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

import com.murphy.integration.dao.EnersightProveDailyDao;
import com.murphy.integration.dao.ProdViewCanadaProveDailyDao;
import com.murphy.integration.dto.EnersightProveMonthlyDto;
import com.murphy.integration.dto.PROVEUIResponseDto;
import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.dto.UIRequestDto;
import com.murphy.integration.dto.UIResponseDto;
import com.murphy.integration.interfaces.EnersightProveDailyLocal;
import com.murphy.integration.util.DBConnections;
import com.murphy.integration.util.ServicesUtil;

public class EnersightProveDaily implements EnersightProveDailyLocal {
	
	private static final Logger logger = LoggerFactory.getLogger(EnersightProveDaily.class);
	private EnersightProveMonthly proveMonthly = new EnersightProveMonthly();
	
	@Override
	public UIResponseDto fetchProveDailyData() {

		//		logger.info("[fetchProveDailyData] : INFO- Service Started");
		UIResponseDto uiResponseDto = new UIResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		EnersightProveDailyDao enersightProveDailyDao = null;
		Connection connection = null;

		try {

			connection = DBConnections.createConnectionForProve();
			if (connection != null) {
				enersightProveDailyDao = new EnersightProveDailyDao();

				try {

					//					logger.info("[fetchProveDailyData] : INFO- Connection to DB successful");
					uiResponseDto
					.setEnersightProveDailyDtoList(enersightProveDailyDao.fetchProveDailyData(connection));
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
	public PROVEUIResponseDto fetchProveData(UIRequestDto uiRequestDto) {

		PROVEUIResponseDto proveUIResponseDto = new PROVEUIResponseDto();
		UIResponseDto uiResponseDto = new UIResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		Connection connection = null;
		Integer duration = uiRequestDto.getDuration();
		String locationType = uiRequestDto.getLocationType();
		List<String> locationCodeList = uiRequestDto.getLocationCodeList();
		int totalCount = 0;
		if (uiRequestDto != null && locationType != null && locationCodeList != null && locationCodeList.size() > 0
				&& duration != null) {

			try {
				
				String countryCode=ServicesUtil.getCountryCodeByLocation(locationCodeList.get(0));
				
				Map<String, String> uwiIdMap = proveMonthly.getMuwiByLocationTypeAndCode(locationType, locationCodeList);

				if (uwiIdMap != null && uwiIdMap.size() > 0) {
					
					totalCount = uwiIdMap.size();
					
					/* InvestigationNIP = Investigation Not In Progress */
					List<String> investigationNIPlocationCodeList = proveMonthly.fetchLocationCodeInvestigation(
							new HashSet<>(uwiIdMap.values()));
					connection = DBConnections.createConnectionForProve();

					/* InquiryNIP = Inquiry Not In Progress */
					List<String> inquiryNIPlocationCodeList = proveMonthly.fetchLocationCodeInquire(
							new HashSet<>(uwiIdMap.values()),uiRequestDto.getUserType());


					if (connection != null) {
						try {
							// Adding page parameter to add pagination in PROVE

							List<EnersightProveMonthlyDto> proveResultList = getProveData(connection, uwiIdMap,
									duration, investigationNIPlocationCodeList, inquiryNIPlocationCodeList,
									uiRequestDto.getPage(), countryCode,uiRequestDto.getUom());

							uiResponseDto.setEnersightProveMonthlyDtoList(proveResultList);
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
				logger.error("[fetchProveData] : ERROR- Exception while interacting with Hana database " + e.getMessage());
				responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB." +e.getMessage());
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
		proveUIResponseDto.setUiResponseDto(uiResponseDto);
		proveUIResponseDto.setTotalCount(totalCount);
		proveUIResponseDto.setPageCount(totalCount>0?totalCount/20+(totalCount%20>0?1:0) : 0);
		return proveUIResponseDto;
	
	}

	private List<EnersightProveMonthlyDto> getProveData(Connection connection, Map<String, String> uwiIdMap,
			Integer duration, List<String> investigationNIPlocationCodeList, List<String> inquiryNIPlocationCodeList,
			String page, String country, String uom) throws Exception {

		if (ServicesUtil.EFS_CODE.equals(country)) {
			EnersightProveDailyDao enersightProveDailyDao = new EnersightProveDailyDao();
			return enersightProveDailyDao.fetchProveData(connection, uwiIdMap, duration,
					investigationNIPlocationCodeList, inquiryNIPlocationCodeList, page);

		} else if (ServicesUtil.CA_CODE.equals(country)) {
			ProdViewCanadaProveDailyDao prodViewCanadaProveDailyDao = new ProdViewCanadaProveDailyDao(connection);
			return prodViewCanadaProveDailyDao.fetchProveData(uwiIdMap, duration, investigationNIPlocationCodeList,
					inquiryNIPlocationCodeList, page,uom);
		}

		return null;

	}

	@Override
	public List<String> getMuwiWherVarLessThanThres(List<String> muwiList , double duration , double thresholdPercent , 
			String version, String country) {
		List<String> responseList = null;
		Connection connection = null;
		EnersightProveDailyDao enersightProveDailyDao = null;
		if(!ServicesUtil.isEmpty(muwiList)){
			connection = DBConnections.createConnectionForProve();
			if (connection != null) {
				enersightProveDailyDao = new EnersightProveDailyDao();
				responseList =	enersightProveDailyDao.getMuwiWherVarLessThanThres(connection, muwiList, duration , thresholdPercent ,version,country);
			}else {
				logger.error("[Murphy][EnersightProveDaily][getMuwiWherVarLessThanThres][error] Connection to Database is not possible");
			}
		}
		return responseList;
	}
	
	@Override
	public Map<String, String> fetchFracData(Set<String> uwiIdSet) {
		Connection connection = DBConnections.createConnectionForProve();
		EnersightProveDailyDao enersightProveDailyDao = new EnersightProveDailyDao();
		try {
			 return enersightProveDailyDao.fetchFracBOED(connection, uwiIdSet);
		} catch (Exception e) {
			logger.error("[Murphy][EnersightProveDaily][fetchFracData][error]failed to fetch data for Frac BOED");
		}
		return new HashMap<String, String>();
		
	}
	
	@Override
	public Map<String, String> fetchPiggingValue(Set<String> uwiIdSet) {
		Connection connection = DBConnections.createConnectionForProve();
		EnersightProveDailyDao enersightProveDailyDao = new EnersightProveDailyDao();
		try {
			return enersightProveDailyDao.fetchPiggingValue(connection, uwiIdSet);
		} catch (Exception e) {
			logger.error("[Murphy][EnersightProveDaily][fetchFracData][error]failed to fetch data for Frac BOED");
		}
		return new HashMap<String, String>();
	}
	
	
	//added for pw hooper canada
	public Map<String,String> getMuwiWherVarLessThanThresForCanada(List<String> muwiList , double duration , double thresholdPercent , 
			String version, String country) {
		Map<String,String> responseMap = null;
		Connection connection = null;
		EnersightProveDailyDao enersightProveDailyDao = null;
		if(!ServicesUtil.isEmpty(muwiList)){
			connection = DBConnections.createConnectionForProve();
			if (connection != null) {
				enersightProveDailyDao = new EnersightProveDailyDao();
				responseMap =	enersightProveDailyDao.getMuwiWherVarLessThanThresForCanada(connection, muwiList, duration , thresholdPercent ,version,country);
			}else {
				logger.error("[Murphy][EnersightProveDaily][getMuwiWherVarLessThanThresForCanada][error] Connection to Database is not possible");
			}
		}
		return responseMap;
	}
}
