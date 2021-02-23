package com.murphy.downtime.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.murphy.downtime.entity.DowntimeCaptureDo;

@Repository("downtimeCaptureDao")
public class DowntimeCaptureDao {

	private static final Logger logger = LoggerFactory.getLogger(DowntimeCaptureDao.class);

	public DowntimeCaptureDao() {
		super();
	}

	public int fetchMerrickFromDB(Connection connection, String uwiId) throws Exception {
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = connection.prepareStatement("SELECT MerrickID FROM DBO.CompletionTb WHERE UWI = ?");
			stmt.setString(1, uwiId);
			resultSet = stmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					return resultSet.getInt("MerrickID");
				}
			}
		} catch (Exception e) {
			logger.error("[fetchMerrickFromDB] : ERROR- Exception while fetching MerrickId from database " + e);
			throw e;
		} finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[fetchMerrickFromDB] : ERROR- Exception while cleaning environment" + e);
			}
		}
		return -1;
	}

	public DowntimeCaptureDo getDataFromDB(Connection connection, int merrickId, String originalDateEntered) throws Exception {
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		DowntimeCaptureDo downtimeCaptureDo = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM DBO.DowntimeReasonTb WHERE ObjectMerrickID = ? AND OriginalDateEntered= ?");
			stmt.setInt(1, merrickId);
			stmt.setString(2, originalDateEntered);
			resultSet = stmt.executeQuery();

			boolean hasNext = resultSet.next();

			if (resultSet != null && hasNext) {
				downtimeCaptureDo = new DowntimeCaptureDo();
				while (hasNext) {
					downtimeCaptureDo.setMerrickId(resultSet.getInt("ObjectMerrickID"));
					downtimeCaptureDo.setObjectType(resultSet.getInt("ObjectType"));
					downtimeCaptureDo.setOriginalDateEntered(resultSet.getString("OriginalDateEntered"));
					downtimeCaptureDo.setOriginalTimeEntered(resultSet.getString("OriginalTimeEntered"));
					downtimeCaptureDo.setDowntimeCode(resultSet.getInt("DowntimeCode"));
					downtimeCaptureDo.setDowntimeHours(resultSet.getFloat("DowntimeHours"));
					downtimeCaptureDo.setStartDate(resultSet.getString("StartDate"));
					downtimeCaptureDo.setEndDate(resultSet.getString("EndDate"));
					downtimeCaptureDo.setStartTime(resultSet.getString("StartTime"));
					downtimeCaptureDo.setEndTime(resultSet.getString("EndTime"));
					downtimeCaptureDo.setDeleteFlag(resultSet.getInt("DeleteFlag"));
					downtimeCaptureDo.setComments(resultSet.getString("Comments"));
					logger.error("[getDataFromDB] : INFO  - downtimeCaptureDo" + downtimeCaptureDo);

					return downtimeCaptureDo;
				}
			}

		} catch (Exception e) {

			logger.error("[getDataFromDB] : ERROR- Exception while fetching data from database " + e);
			throw e;

		} finally {

			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[getDataFromDB] : ERROR- Exception while cleaning environment" + e);
			}

		}

		logger.error("[getDataFromDB] : INFO  - No record is present for merricId " + merrickId + " on date " + originalDateEntered);
		return downtimeCaptureDo;
	}

	public void insertDataInDB(Connection connection, DowntimeCaptureDo downtimeCaptureDo) throws Exception {
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(
					"INSERT INTO DBO.DowntimeReasonTb(ObjectMerrickID,ObjectType,OriginalDateEntered,OriginalTimeEntered,UpDownFlag,DateEntryFlag"
							+ ",DowntimeCode,DowntimeHours,RepairCosts,LostProduction,CalcDowntimeFlag,StartDate,EndDate,StartTime,EndTime,StartProductionDate"
							+ ",EndProductionDate,Comments,Reason,MessageSendFlag,DestinationPerson,RioDowntimeID,LastDayHoursDown,DeleteFlag,LastTransmission"
							+ ",LastLoadDate,LastLoadTime,TransmitFlag,DateTimeStamp,BLogicDateStamp,BLogicTimeStamp,UserDateStamp,UserTimeStamp,UserID) "
							+ "VALUES (?, ?,'" + downtimeCaptureDo.getOriginalDateEntered()
							+ "', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, downtimeCaptureDo.getMerrickId());
			stmt.setInt(2, downtimeCaptureDo.getObjectType());
			stmt.setString(3, downtimeCaptureDo.getOriginalTimeEntered());
			stmt.setInt(4, downtimeCaptureDo.getUpDownFlag());
			stmt.setInt(5, downtimeCaptureDo.getDateEntryFlag());
			stmt.setInt(6, downtimeCaptureDo.getDowntimeCode());
			stmt.setFloat(7, downtimeCaptureDo.getDowntimeHours());
			stmt.setFloat(8, downtimeCaptureDo.getRepairCosts());
			stmt.setFloat(9, downtimeCaptureDo.getLostProduction());
			stmt.setInt(10, downtimeCaptureDo.getCalCDowntimeFlag());
			logger.error("[insertDataInDB] : INFO - StartDate " + downtimeCaptureDo.getStartDate() + " EndDate " + downtimeCaptureDo.getEndDate());
			stmt.setString(11, downtimeCaptureDo.getStartDate());
			stmt.setString(12, downtimeCaptureDo.getEndDate());
			stmt.setString(13, downtimeCaptureDo.getStartTime());
			stmt.setString(14, downtimeCaptureDo.getEndTime());
			stmt.setString(15, downtimeCaptureDo.getStartProductionDate());
			stmt.setString(16, downtimeCaptureDo.getEndProductionDate());
			stmt.setString(17, downtimeCaptureDo.getComments());
			stmt.setString(18, downtimeCaptureDo.getReason());
			stmt.setInt(19, downtimeCaptureDo.getMessageSendFlag());
			stmt.setInt(20, downtimeCaptureDo.getDestinationPerson());
			stmt.setString(21, downtimeCaptureDo.getRioDowntimeId());
			stmt.setFloat(22, downtimeCaptureDo.getLastDayHoursDown());
			stmt.setInt(23, downtimeCaptureDo.getDeleteFlag());
			stmt.setInt(24, downtimeCaptureDo.getLastTransmission());
			stmt.setString(25, downtimeCaptureDo.getLastLoadDate());
			stmt.setString(26, downtimeCaptureDo.getLastLoadTime());
			stmt.setInt(27, downtimeCaptureDo.getTransmitFlag());
			stmt.setString(28, downtimeCaptureDo.getDateTimeStamp());
			stmt.setString(29, downtimeCaptureDo.getbLogicDateStamp());
			stmt.setString(30, downtimeCaptureDo.getbLogicTimeStamp());
			stmt.setString(31, downtimeCaptureDo.getUserDateStamp());
			stmt.setString(32, downtimeCaptureDo.getUserTimeStamp());
			stmt.setString(33, downtimeCaptureDo.getUserId());

			int insertedRowCount = stmt.executeUpdate();

			if (insertedRowCount > 0) {
				logger.error("[insertDataInDB] : INFO - Record inserted succesfully");
			}

		} catch (Exception e) {
			logger.error("[insertDataInDB] : ERROR- Exception while Inserting data in database " + e);
			throw e;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("[insertDataInDB] : ERROR- Exception while cleaning environment" + e);
			}
		}
	}

	public void updateDataInDB(Connection connection, DowntimeCaptureDo downtimeCaptureDo) throws Exception {
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(
					"UPDATE DBO.DowntimeReasonTb SET DowntimeHours = ?, StartDate = ?, EndDate= ?, StartTime=? , EndTime=?, DowntimeCode=? , Comments=?"
							+ " WHERE ObjectMerrickID = ? AND OriginalDateEntered= ?");
			stmt.setFloat(1, downtimeCaptureDo.getDowntimeHours());
			logger.error("[updateDataInDB] : INFO - StartDate " + downtimeCaptureDo.getStartDate() + " EndDate " + downtimeCaptureDo.getEndDate());
			stmt.setString(2, downtimeCaptureDo.getStartDate());
			stmt.setString(3, downtimeCaptureDo.getEndDate());
			stmt.setString(4, downtimeCaptureDo.getStartTime());
			stmt.setString(5, downtimeCaptureDo.getEndTime());
			stmt.setInt(6, downtimeCaptureDo.getDowntimeCode());
			stmt.setString(7, downtimeCaptureDo.getComments());
			stmt.setInt(8, downtimeCaptureDo.getMerrickId());
			stmt.setString(9, downtimeCaptureDo.getOriginalDateEntered());
			int updatedRowCount = stmt.executeUpdate();

			if (updatedRowCount > 0) {
				logger.error("[updateDataInDB] : INFO - Record updated succesfully");
			}

		} catch (Exception e) {
			logger.error("[updateDataInDB] : ERROR- Exception while updating data in database " + e);
			throw e;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("[insertDataInDB] : ERROR- Exception while cleaning environment" + e);
			}
		}
	}

	public void deleteDataFromDB(Connection connection, int merrickId, String originalDateEntered) throws Exception {
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("DELETE DBO.DowntimeReasonTb WHERE ObjectMerrickID = ? AND OriginalDateEntered= ?");
			stmt.setFloat(1, merrickId);
			stmt.setString(2, originalDateEntered);
			int updatedRowCount = stmt.executeUpdate();

			if (updatedRowCount > 0) {
				logger.error("[deleteDataInDB] : INFO - Record Deleted succesfully");
			}

		} catch (Exception e) {
			logger.error("[deleteDataInDB] : ERROR- Exception while updating data in database " + e);
			throw e;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("[insertDataInDB] : ERROR- Exception while cleaning environment" + e);
			}
		}
	}

}
