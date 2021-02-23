package com.murphy.integration.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.modelmbean.InvalidTargetObjectTypeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dao.ItaGasBlowByDao;
import com.murphy.integration.dto.GasBlowByDto;
import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.interfaces.ItaFacadeLocal;
import com.murphy.integration.util.DBConnections;
import com.murphy.integration.util.ServicesUtil;

public class ItaFacadeService implements ItaFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(ItaFacadeService.class);

	@Override
	public ResponseMessage getLocCodeListForGasBlowBy(String oilMeterMerrickId, String gasMeterMerrickId, String meterName, Double dailyOilValue) {

		ResponseMessage responseMessage = new ResponseMessage();
		ItaGasBlowByDao blowByDao = null;
		Connection connection = null;

		Map<Integer,GasBlowByDto> oilGasMap=new HashMap<>();

		
		//[uncomment this when deploying to PROD and comment the below one]
		connection = DBConnections.createConnectionForProcount();
		
		//FOR SIT[use this and comment above]
		//connection = DBConnections.createConnectionForProcountForGasBlowByTest();

		if (connection != null) {
			try {
				blowByDao = new ItaGasBlowByDao();

				oilGasMap = blowByDao.getDailyOil(connection,oilMeterMerrickId,meterName,dailyOilValue);
				
				if(!ServicesUtil.isEmpty(oilGasMap))
				{

				oilGasMap = blowByDao.getDailyGas(connection, oilGasMap,gasMeterMerrickId,meterName);

				oilGasMap = blowByDao.getYearlyOil(connection, oilGasMap,oilMeterMerrickId,meterName);

				oilGasMap = blowByDao.getYearlyGas(connection, oilGasMap,gasMeterMerrickId,meterName);

				oilGasMap=blowByDao.getOilGasRatio(oilGasMap);
				}

			
				
				
				
				
				
				responseMessage.setData(oilGasMap);
				responseMessage.setMessage("Retrieval successful");
				responseMessage.setStatus("SUCCESS");
				responseMessage.setStatusCode("0");

			} catch (Exception e) {
				logger.error("[getLocCodeListForGasBlowBy] : ERROR- Exception while interacting with database " + e);
				responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
				responseMessage.setStatus("FAILURE");
				responseMessage.setStatusCode("1");
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("[getLocCodeListForGasBlowBy] : ERROR- Exception while closing Connection " + e);
				}
			}
		} else {

			responseMessage.setMessage("Connection to Database is not possible");
			responseMessage.setStatus("FAILURE");
			responseMessage.setStatusCode("1");

		}
		return responseMessage;
	}
}
