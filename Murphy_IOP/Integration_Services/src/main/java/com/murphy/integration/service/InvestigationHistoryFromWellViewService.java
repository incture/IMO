package com.murphy.integration.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dao.InvestigationHistoryDao;
import com.murphy.integration.dao.InvestigationHistoryFromWellViewDao;
import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.dto.UIResponseDto;
import com.murphy.integration.interfaces.InvestigationHistoryFromWellViewServiceLocal;
import com.murphy.integration.util.DBConnections;

public class InvestigationHistoryFromWellViewService implements InvestigationHistoryFromWellViewServiceLocal{

	private static final Logger logger = LoggerFactory.getLogger(InvestigationHistoryService.class);

	@Override
	public UIResponseDto fetchInvestigationDataFromWellView(String uwiId) {

		logger.info("[fetchInvestigationDataFromWellView] : INFO- Service Started");
		UIResponseDto uiResponseDto = new UIResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		InvestigationHistoryFromWellViewDao investigationHistoryFromWellViewDao = null;
		Connection connection = null;

		if (uwiId != null && uwiId.length() > 0) {

			
			connection = DBConnections.createConnectionForWellView();
			if(connection== null)
			{
				logger.info("incomming connection variable:");
			}

			if (connection != null) {
				investigationHistoryFromWellViewDao = new InvestigationHistoryFromWellViewDao();
				try {
					logger.info("[fetchInvestigationDataFromWellView] : INFO- Connection to DB successful");

					logger.info("connection established : " +connection.toString());
					logger.info("connection parameters : " +connection);
					uiResponseDto.setInvestigationHistoryDtoList(investigationHistoryFromWellViewDao.fetchInvestigationData(connection, uwiId));
					responseMessage.setMessage("Successful");
					responseMessage.setStatus("SUCCESS");
					responseMessage.setStatusCode("0");
				} catch (Exception e) {
					logger.error("[fetchInvestigationData] : ERROR- Exception while interacting with database " + e);
					responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
					responseMessage.setStatus("FAILURE");
					responseMessage.setStatusCode("1");
				} finally {
					try {
						connection.close();
					} catch (SQLException e) {
						logger.error("[fetchInvestigationData] : ERROR- Exception while closing Connection " + e);
					}
				}
			} else {
				responseMessage.setMessage("Connection to Database is not possible");
				responseMessage.setStatus("FAILURE");
				responseMessage.setStatusCode("1");
			}
		} else {
			responseMessage.setMessage("uwiId is null");
			responseMessage.setStatus("FAILURE");
			responseMessage.setStatusCode("1");
		}
//		logger.info("[fetchInvestigationData] : UIResponseDto " + uiResponseDto);
		uiResponseDto.setResponseMessage(responseMessage);
		return uiResponseDto;
	}

}
