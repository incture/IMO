package com.murphy.integration.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dto.FlareDowntimeCaptureDto;
import com.murphy.integration.entity.DowntimeCaptureDo;
import com.murphy.integration.entity.FlareDowntimeCaptureDo;
import com.murphy.integration.util.ServicesUtil;

public class FlareDowntimeCaptureDao {
	
	private static final Logger logger = LoggerFactory.getLogger(DowntimeCaptureDao.class);
	
	public FlareDowntimeCaptureDao() {
		super();
	}
	
	public FlareDowntimeCaptureDo getDataFromDB(Connection connection, int merrickId, String recordDate) throws Exception {
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		FlareDowntimeCaptureDo flareDowntimeCaptureDo = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM DBO.MeterDailyTb WHERE MerrickID = ? AND Recorddate= ?");
			stmt.setInt(1, merrickId);
			stmt.setString(2, recordDate);
			resultSet = stmt.executeQuery();
			logger.error("[getDataFromDB] : INFO  - flareDowntimeCaptureDo Started");
			boolean hasNext = resultSet.next();

			if (resultSet != null && hasNext) {
				flareDowntimeCaptureDo = new FlareDowntimeCaptureDo();
				while (hasNext) {
					flareDowntimeCaptureDo.setMerrickId(resultSet.getInt("MerrickID"));
					flareDowntimeCaptureDo.setRecordDate(resultSet.getString("Recorddate"));
					flareDowntimeCaptureDo.setFlareVolume(resultSet.getFloat("EstGasVolMCF"));
					flareDowntimeCaptureDo.setFlareCode(resultSet.getString("springsize"));
//					logger.error("[getDataFromDB] : INFO  - downtimeCaptureDo" + downtimeCaptureDo);
					logger.error("[getDataFromDB] : INFO  - flareDowntimeCaptureDo" + flareDowntimeCaptureDo);
					return flareDowntimeCaptureDo;
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

//		logger.error("[getDataFromDB] : INFO  - No record is present for merricId " + merrickId + " on date " + originalDateEntered);
		return flareDowntimeCaptureDo;
	}


	public void insertDataInDB(Connection connection, FlareDowntimeCaptureDto flareDowntimeCaptureDto) throws Exception {
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(
					"INSERT INTO DBO.MeterDailyTb(MerrickID, EstGasVolMCF, SpringSize, Recorddate, ProductionDate) "
							+ "VALUES (?, ?, ?, ?,?)");
			stmt.setInt(1, flareDowntimeCaptureDto.getMerrickId());
			stmt.setFloat(2, flareDowntimeCaptureDto.getFlareVolume());
			stmt.setString(3, flareDowntimeCaptureDto.getFlareCode());
			stmt.setString(4, flareDowntimeCaptureDto.getRecordDate());
			//SOC : Incident INC0077904
			stmt.setString(5, flareDowntimeCaptureDto.getProductionDate());
			//EOC : Incident INC0077904
			int insertedRowCount = stmt.executeUpdate();

			if (insertedRowCount > 0) {
				logger.error("[insertDataInDB] : INFO - Record inserted succesfully in MeterDailyTb flareDowntimeCaptureDto.getMerrickId()");
				logger.error("[insertDataInDB] : INFO - Record inserted succesfully in MeterDailyTb");
			}
		}catch (Exception e) {
			logger.error("[insertDataInDB] : ERROR- Exception while Inserting data in database in MeterDailyTb " + e);
			throw e;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("[insertDataInDB] : ERROR- Exception while cleaning environment in MeterDailyTb" + e);
			}
		}
		
	}
	
	public void updateDataInDB(Connection connection, FlareDowntimeCaptureDto flareDowntimeCaptureDto) throws Exception {
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("UPDATE DBO.MeterDailyTb SET EstGasVolMCF = ?, SpringSize = ? WHERE MerrickID = ? AND Recorddate = ?");
			stmt.setFloat(1, flareDowntimeCaptureDto.getFlareVolume());
			stmt.setString(2, flareDowntimeCaptureDto.getFlareCode());
			stmt.setInt(3, flareDowntimeCaptureDto.getMerrickId());
			stmt.setString(4, flareDowntimeCaptureDto.getRecordDate());
			int updatedRowCount = stmt.executeUpdate();

			if (updatedRowCount > 0) {
				logger.error("[updateDataInDB] : INFO - Record updated succesfully in MeterDailyTb");
			}

		} catch (Exception e) {
			logger.error("[updateDataInDB] : ERROR- Exception while updating data in database in MeterDailyTb" + e);
			throw e;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("[insertDataInDB] : ERROR- Exception while cleaning environment in MeterDailyTb" + e);
			}
		}
	}
	
	// SOC: For getting details from ProCount of Downtime Created between certain dates
		public List<FlareDowntimeCaptureDo> getDownTimeDetailsFromProCount(Connection connection, List<String> listOfMerrick,
				String fromDate, String toDate, int page, int page_size) {
			FlareDowntimeCaptureDo flareDowntimeCaptureDo = null;
			List<FlareDowntimeCaptureDo> flareDowntimeCaptureDoList = null;
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			try {
				String merrickString = ServicesUtil.getStringFromList(listOfMerrick);
				String paginationQuery = " ";
				if(page > 0){
					int first = (page - 1) * page_size;
					int last = page_size;
					paginationQuery += " OFFSET " + first + " ROWS FETCH NEXT "+ last +" ROWS ONLY";
					
				}
				String query = "select  MerrickID, EstGasVolMCF, SpringSize, Recorddate, ProductionDate"
						+ " from [DBO].[MeterDailyTb]"
						+ " where MerrickID in ( " + merrickString + " ) and Recorddate >= '" + fromDate
						+ "' and Recorddate <= '" + toDate + "'"
						+ "order by Recorddate desc";
				
				String finalQuery = query + paginationQuery;
				
				logger.error("[getFlareDownTimeDetailsFromProCount][finalQuery]" + finalQuery);
				stmt = connection.prepareStatement(finalQuery);
				resultSet = stmt.executeQuery();
				boolean hasNext = resultSet.next();
				if (resultSet != null && hasNext) {
					flareDowntimeCaptureDoList = new ArrayList<FlareDowntimeCaptureDo>();
					while (hasNext) {
						flareDowntimeCaptureDo = new FlareDowntimeCaptureDo();
						flareDowntimeCaptureDo.setMerrickId(resultSet.getInt("MerrickID"));
						flareDowntimeCaptureDo.setFlareVolume(resultSet.getFloat("EstGasVolMCF"));
						flareDowntimeCaptureDo.setFlareCode(resultSet.getString("SpringSize"));
						flareDowntimeCaptureDo.setRecordDate(resultSet.getString("Recorddate"));
						flareDowntimeCaptureDoList.add(flareDowntimeCaptureDo);
						hasNext = resultSet.next();
					}
				}
			} catch (Exception e) {
				logger.error("[DowntimeCaptureDao][getFlareDownTimeDetailsFromProCount][error]" + e.getMessage());
			}finally {
				try {
					stmt.close();
					resultSet.close();
				} catch (SQLException e) {
					logger.error("[getFlareDownTimeDetailsFromProCount] : ERROR- Exception while cleaning environment" + e.getMessage());
				}
			}
			logger.error("[getFlareDownTimeDetailsFromProCount][flareDowntimeCaptureDoList] " + flareDowntimeCaptureDoList);
			return flareDowntimeCaptureDoList;
		}

		
		// SOC: For getting details of Downtime Created between certain dates
		public int getTotalCount(Connection connection, List<String> merrickIdList,
				String fromDate, String toDate) {
			int count = 0;
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			try {
				String merrickIdString = ServicesUtil.getStringFromList(merrickIdList);
				String countQuery = "select  count(x.row) as countx from (select 1 as row"
						+ " from [DBO].[MeterDailyTb]"
						+ " where MerrickID in ( " + merrickIdString + " ) and Recorddate >= '" + fromDate
						+ "' and Recorddate <= '" + toDate + "')"
						+ "as x";
				
				logger.error("[FlareDowntimeCaptureDao][getTotalCount][countQuery]" + countQuery);
				stmt = connection.prepareStatement(countQuery);
				resultSet = stmt.executeQuery();
				boolean hasNext = resultSet.next();
				if (resultSet != null && hasNext) {
					while (hasNext) {
						count = resultSet.getInt("countx");
						logger.error("Line 210 : "+count);
						hasNext = resultSet.next();
					}
				}
			} catch (Exception e) {
				logger.error("[FlareDowntimeCaptureDao][getTotalCount][error]" + e.getMessage());
			}finally {
				try {
					stmt.close();
					resultSet.close();
				} catch (SQLException e) {
					logger.error("[FlareDowntimeCaptureDao][getTotalCount] : ERROR- Exception while cleaning environment" + e.getMessage());
				}
			}
			logger.error("[getTotalCount][downtimeCaptureDoList] " + count);
			return count;
		}
}
