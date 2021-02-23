package com.murphy.integration.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dto.DailyReportCommentsDto;
import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.entity.CommentsTbDo;
import com.murphy.integration.util.DBConnections;
import com.murphy.integration.util.ProcountConstant;
import com.murphy.integration.util.ServicesUtil;

public class DailyReportForeManCmtsDao {

	private static final Logger logger = LoggerFactory.getLogger(DailyReportForeManCmtsDao.class);

	public DailyReportForeManCmtsDao() {
		super();
	}

	public DailyReportCommentsDto fetchHierarchyDetailsBymuwi(Connection connection,
			DailyReportCommentsDto prodComments) throws Exception {
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = connection
					.prepareStatement("SELECT TOP 1 * FROM [dbo].[SOR_Daily_Production_mobile_well_v1] WHERE MUWI=?");
			stmt.setString(1, prodComments.getMuwi());
			resultSet = stmt.executeQuery();

			boolean hasNext = resultSet.next();

			if (resultSet != null && hasNext) {
				while (hasNext) {
					prodComments.setArea(resultSet.getString("AREA_NAME"));
					prodComments.setPad(resultSet.getString("PADNAME"));
					prodComments.setWellName(resultSet.getString("COMPLETIONNAME"));

					logger.error("[getDataFromCommentsDB] : INFO  - commentsTbDo" + prodComments);

					return prodComments;
				}
			}
		} catch (Exception e) {

			logger.error("[getDataFromCommentsDB] : ERROR- Exception while fetching data from database " + e);
			throw e;

		} finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[getDataFromCommentsDB] : ERROR- Exception while cleaning environment" + e);
			}
		}
		return prodComments;
	}

	public DailyReportCommentsDto getHierarchyDetails(DailyReportCommentsDto prodComments) throws Exception {
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		Connection connection = null;
		if (!ServicesUtil.isEmpty(prodComments) && !ServicesUtil.isEmpty(prodComments.getMuwi())) {

			// Connecting to Prod DB
			connection = DBConnections.createConnectionForDORADB2();


			if (connection != null) {
				logger.error("[saveCommentsToDoraReport] : INFO- new connection is created " + connection);

				try {
					stmt = connection.prepareStatement(
							"SELECT TOP 1 * FROM [dbo].[SOR_Daily_Production_mobile_well_v1] WHERE MUWI=?");
					stmt.setString(1, prodComments.getMuwi());
					resultSet = stmt.executeQuery();

					boolean hasNext = resultSet.next();

					if (resultSet != null && hasNext) {
						while (hasNext) {
							prodComments.setArea(resultSet.getString("AREA_NAME"));
							prodComments.setPad(resultSet.getString("PADNAME"));
							prodComments.setWellName(resultSet.getString("COMPLETIONNAME"));

							logger.error("[getDataFromCommentsDB] : INFO  - commentsTbDo" + prodComments);

							return prodComments;
						}
					}
				} catch (Exception e) {

					logger.error("[getDataFromCommentsDB] : ERROR- Exception while fetching data from database " + e.getMessage());
					throw e;

				} finally {
					try {
						stmt.close();
						resultSet.close();
					} catch (SQLException e) {
						logger.error("[getDataFromCommentsDB] : ERROR- Exception while cleaning environment" + e);
					}
				}
			}
		}

		return prodComments;
	}

	public String getAreaByFacility(String facility) throws Exception {
		String fieldAreaName = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DBConnections.createConnectionForDORADB(ProcountConstant.DAILY_REPORTS);
			if (connection != null) {
				stmt = connection.prepareStatement(
						"SELECT DISTINCT OPPORTUNITY_NAME FROM [dbo].[Daily_Report_Production_v50_Area_Detail_vw] WHERE AREA=?");
				stmt.setString(1, facility);
				resultSet = stmt.executeQuery();

				boolean hasNext = resultSet.next();

				if (resultSet != null && hasNext) {
					while (hasNext) {
						return resultSet.getString("OPPORTUNITY_NAME");

					}
				}
			}
		} catch (Exception e) {
			logger.error("[getAreaByWellPad] : ERROR- Exception while fetching data from database " + e.getMessage());
			throw e;

		} finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[getAreaByWellPad] : ERROR- Exception while cleaning environment" + e.getMessage());
			}
		}
		return fieldAreaName;
	}

	public ResponseMessage saveCommentsToDoraReport(DailyReportCommentsDto dailyReportDto) {
		ResponseMessage responseMessage = new ResponseMessage();
		PreparedStatement stmt = null;
		Connection connection = null;

		if (!ServicesUtil.isEmpty(dailyReportDto)) {

			connection = DBConnections.createConnectionForDORADB(ProcountConstant.DAILY_REPORTS);

			logger.error("[saveCommentsToDoraReport] : INFO- new connection is created " + connection);

			if (connection != null) {
				try {

					stmt = connection.prepareStatement("INSERT INTO Daily_Report_Foreman (Area, Pad, WellName,"
							+ "Comment, Foreman_Name, Date, Timestamp, Asset) " + "VALUES (?,?,?,?,?,?,?,?)");

					stmt.setString(1, dailyReportDto.getArea());
					stmt.setString(2, dailyReportDto.getPad());
					stmt.setString(3, dailyReportDto.getWellName());
					stmt.setString(4, dailyReportDto.getComments());
					stmt.setString(5, dailyReportDto.getForemanName());
					stmt.setDate(6, dailyReportDto.getDate());
					stmt.setString(7, dailyReportDto.getTimeStamp());
					stmt.setString(8, dailyReportDto.getAsset());

					int insertedRowCount = stmt.executeUpdate();

					if (insertedRowCount > 0) {
						logger.error("[saveCommentsToDoraReport] : INFO - Record inserted succesfully");
						responseMessage.setMessage("Comment Inserted successfully");
						responseMessage.setStatus("SUCCESS");
						responseMessage.setStatusCode("0");
					}

				} catch (Exception e) {
					logger.error("[saveCommentsToDoraReport] : ERROR- Exception while interacting with database " + e);
					responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
					responseMessage.setStatus("FAILURE");
					responseMessage.setStatusCode("1");
				} finally {
					try {
						connection.close();
					} catch (SQLException e) {
						logger.error("[saveCommentsToDoraReport] : ERROR- Exception while closing Connection " + e);
					}
				}
			} else {
				responseMessage.setMessage("Connection to Database is not possible");
				responseMessage.setStatus("FAILURE");
				responseMessage.setStatusCode("1");
			}
		} else {
			responseMessage.setMessage("Please provide all the necessary values");
			responseMessage.setStatus("FAILURE");
			responseMessage.setStatusCode("1");
		}
		logger.info("[saveCommentsToDoraReport] : UIResponseDto " + responseMessage);
		return responseMessage;
	}

}
