package com.murphy.integration.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dto.DowntimeCapturedCADto;
import com.murphy.integration.entity.DowntimeCaptureDo;
import com.murphy.integration.util.ProdViewConstant;
import com.murphy.integration.util.ServicesUtil;

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

	public DowntimeCaptureDo getDataFromDB(Connection connection, int merrickId, String originalDateEntered)
			throws Exception {
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		DowntimeCaptureDo downtimeCaptureDo = null;
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM DBO.DowntimeReasonTb WHERE ObjectMerrickID = ? AND OriginalDateEntered= ?");
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
					// logger.error("[getDataFromDB] : INFO - downtimeCaptureDo"
					// + downtimeCaptureDo);

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

		// logger.error("[getDataFromDB] : INFO - No record is present for
		// merricId " + merrickId + " on date " + originalDateEntered);
		return downtimeCaptureDo;
	}

	
	public DowntimeCapturedCADto getDataFromDwntTmeTable(Connection connection, DowntimeCapturedCADto downTmeCaptureDtoUI) throws Exception {
		 logger.error("[getDataFromDwntTmeTable] : Inside - downtimeCaptureDo");
		DowntimeCapturedCADto prodDataDto=null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM dbo.pvt_pvunitcompdowntm WHERE idflownet = ? AND idrecparent= ? AND cast(syscreatedate as date)=?");
			stmt.setString(1, downTmeCaptureDtoUI.getIdFlownet());
			stmt.setString(2, downTmeCaptureDtoUI.getIdecParent());
			stmt.setString(3,ServicesUtil.convertFromZoneToZoneString(null, downTmeCaptureDtoUI.getSysCreateDate(), null,
					null,ProdViewConstant.DATE_STANDARD,ProdViewConstant.DATE_STANDARD));
			 logger.error("[getDataFromDwntTmeTable] sysCreatedDate :"+ServicesUtil.convertFromZoneToZoneString(null, downTmeCaptureDtoUI.getSysCreateDate(), null,
						null,ProdViewConstant.DATE_DB_FORMATE,ProdViewConstant.DATE_STANDARD));
			resultSet = stmt.executeQuery();

			boolean hasNext = resultSet.next();

			if (resultSet != null && hasNext) {
				prodDataDto=new DowntimeCapturedCADto();
				while (hasNext) {
					prodDataDto.setIdrec(resultSet.getString(("idrec")));
					prodDataDto.setSysCreateDate(resultSet.getString(("syscreatedate")));
					prodDataDto.setTypDownTime(resultSet.getString(("typdowntm")));
					prodDataDto.setCodeDownTime1(resultSet.getString(("codedowntm1")));
					prodDataDto.setCodeDownTime2(resultSet.getString(("codedowntm2")));
					prodDataDto.setDowntmeStart(resultSet.getString("dttmstart"));
					prodDataDto.setDowntmeEnd(resultSet.getString("dttmend"));
					prodDataDto.setComments(resultSet.getString("com"));
					 logger.error("[getDataFromDB] : INFO - downtimeCaptureDo"
					 + prodDataDto);

					return prodDataDto;
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
		return prodDataDto;
	}
	
	
	public void insertDataInDB(Connection connection, DowntimeCaptureDo downtimeCaptureDo) throws Exception {
		PreparedStatement stmt = null;
		try {
//			StringBuilder queryDetails=new StringBuilder();
//			
//			queryDetails.append(downtimeCaptureDo.getMerrickId()).append(",");
//			queryDetails.append(downtimeCaptureDo.getObjectType()).append(",");
//			queryDetails.append(downtimeCaptureDo.getOriginalDateEntered()).append(",");
//			queryDetails.append(downtimeCaptureDo.getOriginalTimeEntered()).append(",");
//			queryDetails.append(downtimeCaptureDo.getUpDownFlag()).append(",");
//			queryDetails.append(downtimeCaptureDo.getDateEntryFlag()).append(",");
//			queryDetails.append(downtimeCaptureDo.getDowntimeCode()).append(",");
//			queryDetails.append(downtimeCaptureDo.getDowntimeHours()).append(",");
//			queryDetails.append(downtimeCaptureDo.getRepairCosts()).append(",");
//			queryDetails.append(downtimeCaptureDo.getLostProduction()).append(",");
//			queryDetails.append(downtimeCaptureDo.getCalCDowntimeFlag()).append(",");
//			queryDetails.append(downtimeCaptureDo.getStartDate()).append(",");
//			queryDetails.append(downtimeCaptureDo.getEndDate()).append(",");
//			queryDetails.append( downtimeCaptureDo.getStartTime()).append(",");
//			queryDetails.append( downtimeCaptureDo.getEndTime()).append(",");
//			queryDetails.append(downtimeCaptureDo.getStartProductionDate()).append(",");
//			queryDetails.append(downtimeCaptureDo.getEndProductionDate()).append(",");
//			queryDetails.append(downtimeCaptureDo.getComments()).append(",");
//			queryDetails.append(downtimeCaptureDo.getReason()).append(",");
//			queryDetails.append(downtimeCaptureDo.getMessageSendFlag()).append(",");
//			queryDetails.append(downtimeCaptureDo.getDestinationPerson()).append(",");
//			queryDetails.append( downtimeCaptureDo.getRioDowntimeId()).append(",");
//			queryDetails.append(downtimeCaptureDo.getLastDayHoursDown()).append(",");
//			queryDetails.append(downtimeCaptureDo.getLastTransmission()).append(",");
//			queryDetails.append(downtimeCaptureDo.getLastLoadDate()).append(",");
//			queryDetails.append(downtimeCaptureDo.getLastLoadTime()).append(",");
//			queryDetails.append(downtimeCaptureDo.getTransmitFlag()).append(",");
//			queryDetails.append(downtimeCaptureDo.getDateTimeStamp()).append(",");
//			queryDetails.append(downtimeCaptureDo.getbLogicDateStamp()).append(",");
//			queryDetails.append(downtimeCaptureDo.getUserDateStamp()).append(",");
//			queryDetails.append(downtimeCaptureDo.getUserTimeStamp()).append(",");
//			queryDetails.append(downtimeCaptureDo.getUserId());
//			logger.error("Print Procount Insert Query::"+queryDetails.toString());


			stmt = connection.prepareStatement(
					"INSERT INTO DBO.DowntimeReasonTb(ObjectMerrickID,ObjectType,OriginalDateEntered,OriginalTimeEntered,UpDownFlag,DateEntryFlag"
							+ ",DowntimeCode,DowntimeHours,RepairCosts,LostProduction,CalcDowntimeFlag,StartDate,EndDate,StartTime,EndTime,StartProductionDate"
							+ ",EndProductionDate,Comments,Reason,MessageSendFlag,DestinationPerson,RioDowntimeID,LastDayHoursDown,DeleteFlag,LastTransmission"
							+ ",LastLoadDate,LastLoadTime,TransmitFlag,DateTimeStamp,BLogicDateStamp,BLogicTimeStamp,UserDateStamp,UserTimeStamp,UserID) "
							+ "VALUES (?, ?,'" + downtimeCaptureDo.getOriginalDateEntered()
							+ "', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
							+ " ?, ?, ?, ?, ?, ?, ?)");
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
			 logger.error("[insertDataInDB]Line128 : INFO - StartDate: " +
			 downtimeCaptureDo.getStartDate() + " EndDate: " +
			 downtimeCaptureDo.getEndDate() + " StartTime: "+downtimeCaptureDo.getStartTime() + " EndTime:"+downtimeCaptureDo.getEndTime());
			 //[insertDataInDB]Line128 : INFO - StartDate: 2019-08-16 EndDate: 2019-08-16 StartTime: 11:40:00 EndTime:12:55:00
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
			logger.error("[updateDataInDB] : INFO - StartDate " + downtimeCaptureDo.getStartDate() + " EndDate "
					+ downtimeCaptureDo.getEndDate());
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
			stmt = connection.prepareStatement(
					"DELETE DBO.DowntimeReasonTb WHERE ObjectMerrickID = ? AND OriginalDateEntered= ?");
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

	// SOC: For the list of Muwi from ProCount
	public List<String> getMerrickIdListFromProCount(Connection connection, List<String> muwiList) {
		List<String> reponseMerrickList = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try {
			String muwiString = ServicesUtil.getStringFromList(muwiList);
			String query = "select MerrickID as merrickIds from [DBO].[CompletionTb] where UWI " + " in ( "
					+ muwiString + " )";
			logger.error("[getMerrickIdListFromProCount][query]" + query);
			stmt = connection.prepareStatement(query);
			resultSet = stmt.executeQuery();

			if (resultSet != null) {
				reponseMerrickList = new ArrayList<String>();
				boolean hasNext = resultSet.next();
				while (hasNext) {
					reponseMerrickList.add(String.valueOf(resultSet.getInt("merrickIds")));
					hasNext = resultSet.next();
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][DowntimeCaptureDao][getMerrickIdListFromProCount][error]" + e.getMessage());
		}finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[getMerrickIdListFromProCount] : ERROR- Exception while cleaning environment" + e.getMessage());
			}
		}
		logger.error("[getMerrickIdListFromProCount][reponseMerrickList]" + reponseMerrickList);
		return reponseMerrickList;
	}

	// SOC: For getting details from ProCount of Downtime Created between certain dates
	public List<DowntimeCaptureDo> getDownTimeDetailsFromProCount(Connection connection, List<String> listOfMerrick,
			String fromDate, String toDate, int page, int page_size) {
		DowntimeCaptureDo downtimeCaptureDo = null;
		List<DowntimeCaptureDo> downtimeCaptureDoList = null;
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
			String query = "select  ObjectMerrickID ,OriginalDateEntered,DowntimeCode, DowntimeHours,OriginalTimeEntered, StartTime,"
					+ " StartDate, EndDate from [DBO].[DowntimeReasonTb]"
					+ " where ObjectMerrickID in ( " + merrickString + " ) and OriginalDateEntered >= '" + fromDate
					+ "' and OriginalDateEntered <= '" + toDate + "'"
					+ "order by OriginalDateEntered desc";
			
			String finalQuery = query + paginationQuery;
			
			logger.error("[getDownTimeDetailsFromProCount][finalQuery]" + finalQuery);
			stmt = connection.prepareStatement(finalQuery);
			resultSet = stmt.executeQuery();
			boolean hasNext = resultSet.next();  
			if (resultSet != null && hasNext) {
				downtimeCaptureDoList = new ArrayList<DowntimeCaptureDo>();
				while (hasNext) {
					downtimeCaptureDo = new DowntimeCaptureDo();
					downtimeCaptureDo.setMerrickId(resultSet.getInt("ObjectMerrickID"));
					downtimeCaptureDo.setOriginalDateEntered(resultSet.getString("OriginalDateEntered"));
					downtimeCaptureDo.setDowntimeCode(resultSet.getInt("DowntimeCode"));
					downtimeCaptureDo.setDowntimeHours(resultSet.getFloat("DowntimeHours"));
					downtimeCaptureDo.setOriginalTimeEntered(resultSet.getString("OriginalTimeEntered"));
					downtimeCaptureDo.setStartTime(resultSet.getString("StartTime"));
					downtimeCaptureDo.setStartDate(resultSet.getString("StartDate"));
					downtimeCaptureDo.setEndDate(resultSet.getString("EndDate"));
					downtimeCaptureDoList.add(downtimeCaptureDo);
					hasNext = resultSet.next();
				}
			}
		} catch (Exception e) {
			logger.error("[DowntimeCaptureDao][getDownTimeDetailsFromProCount][error]" + e.getMessage());
		}finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[getDownTimeDetailsFromProCount] : ERROR- Exception while cleaning environment" + e.getMessage());
			}
		}
		logger.error("[getDownTimeDetailsFromProCount][downtimeCaptureDoList] " + downtimeCaptureDoList);
		return downtimeCaptureDoList;
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
					+ " from [DBO].[DowntimeReasonTb]"
					+ " where ObjectMerrickID in ( " + merrickIdString + " ) and OriginalDateEntered >= '" + fromDate
					+ "' and OriginalDateEntered <= '" + toDate + "')"
					+ "as x";
			
			/*SELECT count(x.row) from(select 1 as row
					from [procount_Test].[dbo].[DowntimeReasonTb]
					where ObjectMerrickID in ( '72','73') and
					OriginalDateEntered >= '2019-07-15' and OriginalDateEntered <= '2019-08-14' ) as x ;*/
			
			logger.error("[getTotalCount][countQuery]" + countQuery);
			stmt = connection.prepareStatement(countQuery);
			resultSet = stmt.executeQuery();
			boolean hasNext = resultSet.next();
			if (resultSet != null && hasNext) {
				while (hasNext) {
					count = resultSet.getInt("countx");
					logger.error("Line 354 : "+count);
					hasNext = resultSet.next();
				}
			}
		} catch (Exception e) {
			logger.error("[DowntimeCaptureDao][getTotalCount][error]" + e.getMessage());
		}finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[getTotalCount] : ERROR- Exception while cleaning environment" + e.getMessage());
			}
		}
		logger.error("[getTotalCount][downtimeCaptureDoList] " + count);
		return count;
	}

	public Map<String,String> fetchIdFromPvtUnitComp(Connection connection, String muwi) throws Exception{
		Map<String,String> pvUnitCom=new HashMap<String,String>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = connection.prepareStatement("SELECT TOP 1 idflownet,idrec FROM dbo.pvt_pvunitcomp WHERE wellidd = ? order by syscreatedate Desc");
			stmt.setString(1, muwi);
			resultSet = stmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					pvUnitCom.put("idflownet",resultSet.getString("idflownet"));
					pvUnitCom.put("idrec",resultSet.getString("idrec"));
				}
			}
		} catch (Exception e) {
			logger.error("[fetchMerrickFromDB] : ERROR- Exception while fetching MerrickId from database " + e.getMessage());
			throw e;
		} finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[fetchMerrickFromDB] : ERROR- Exception while cleaning environment" + e);
			}
		}
		return pvUnitCom;
	}

	public void updateDataInProdViewDB(Connection connection, DowntimeCapturedCADto downtimeCaptureDo,String location) throws Exception {
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(
					"UPDATE DBO.pvt_pvunitcompdowntm SET durdownstartday = ?, dttmstart = ?, dttmend= ?, durdownendday=? "
					+ ", durdowncalc=?, codedowntm1=? , codedowntm2=? ,sysmoddate = ? ,syslockdate = ? ,sysmoduser = ?"
							+ " WHERE idflownet = ? AND idrecparent=? AND idrec= ? AND cast(syscreatedate as date)=?");
			
			stmt.setFloat(1, downtimeCaptureDo.getDurationDownTmeStartDay());
			stmt.setString(2, downtimeCaptureDo.getDowntmeStart());
			stmt.setString(3, downtimeCaptureDo.getDowntmeEnd());
			stmt.setString(4, downtimeCaptureDo.getDurationDownEndDay());
			stmt.setFloat(5, downtimeCaptureDo.getDurationDownCalc());
			stmt.setString(6, downtimeCaptureDo.getCodeDownTime1());
			stmt.setString(7, downtimeCaptureDo.getCodeDownTime2());
			if(ProdViewConstant.KAY_BASE_LOC_CODE.equalsIgnoreCase(location)){
			stmt.setString(8, ServicesUtil.convertFromZoneToZoneString(new Date(),null,ProdViewConstant.UTC_ZONE,ProdViewConstant.MST_ZONE, ProdViewConstant.DATEFORMAT_T,
					ProdViewConstant.DATE_DB_FORMATE));
			stmt.setString(9, ServicesUtil.convertFromZoneToZoneString(new Date(),null,ProdViewConstant.UTC_ZONE,ProdViewConstant.MST_ZONE, ProdViewConstant.DATEFORMAT_T,
				ProdViewConstant.DATE_DB_FORMATE));
			}else{
				stmt.setString(8, ServicesUtil.convertFromZoneToZoneString(new Date(),null,ProdViewConstant.UTC_ZONE,ProdViewConstant.PST_ZONE, ProdViewConstant.DATEFORMAT_T,
						ProdViewConstant.DATE_DB_FORMATE));
				stmt.setString(9, ServicesUtil.convertFromZoneToZoneString(new Date(),null,ProdViewConstant.UTC_ZONE,ProdViewConstant.PST_ZONE, ProdViewConstant.DATEFORMAT_T,
					ProdViewConstant.DATE_DB_FORMATE));	
			}
			stmt.setString(10, ProdViewConstant.SVC_IOP);
			stmt.setString(11, downtimeCaptureDo.getIdFlownet());
			stmt.setString(12, downtimeCaptureDo.getIdecParent());
			stmt.setString(13, downtimeCaptureDo.getIdrec());
			stmt.setString(14,downtimeCaptureDo.getSysCreateDate());
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

	public void insertDataInProdViewDB(Connection connection, DowntimeCapturedCADto downtimeCaptureDoUI) throws Exception {
		logger.error("[insertDataInProdViewDB] :Inside Method");
		PreparedStatement stmt = null;
		String idrec=null;
		try {

			stmt = connection.prepareStatement(
					"INSERT INTO [dbo].[pvt_pvunitcompdowntm]([idflownet],[idrecparent],[idrec],[typdowntm],[dttmstart],[durdownstartday],[codedowntm1],[codedowntm2]"
					+ ",[dttmend],[durdowncalc],"
					+ "[syslockdate],[sysmoddate],[sysmoduser],[syscreatedate],[syscreateuser],[com]) "
					+ "VALUES (?, ?,REPLACE(NEWID(),'-','') , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? )");
			stmt.setString(1, downtimeCaptureDoUI.getIdFlownet());
			stmt.setString(2, downtimeCaptureDoUI.getIdecParent());
			stmt.setString(3,downtimeCaptureDoUI.getTypDownTime());
			stmt.setString(4, downtimeCaptureDoUI.getDowntmeStart());
			stmt.setFloat(5, downtimeCaptureDoUI.getDurationDownTmeStartDay());
			stmt.setString(6, downtimeCaptureDoUI.getCodeDownTime1());
			stmt.setString(7, downtimeCaptureDoUI.getCodeDownTime2());
			stmt.setString(8, downtimeCaptureDoUI.getDowntmeEnd());
			stmt.setFloat(9, downtimeCaptureDoUI.getDurationDownCalc());
			stmt.setString(10, downtimeCaptureDoUI.getSysLockDate());
			stmt.setString(11, downtimeCaptureDoUI.getSysModDate());
			stmt.setString(12, downtimeCaptureDoUI.getSysModUser());
			stmt.setString(13, downtimeCaptureDoUI.getSysCreateDate());
			stmt.setString(14, downtimeCaptureDoUI.getSysCreateUser());
			stmt.setString(15,"");
			
			logger.error("[Murphy][insertDataInProdViewDB]{field}idrec"+idrec+",TypeDowntime"+downtimeCaptureDoUI.getTypDownTime()+",DownTimeStart"+downtimeCaptureDoUI.getDowntmeStart()+",DownTimeStartDay"+downtimeCaptureDoUI.getDurationDownTmeStartDay()
			+",CodeDownTme1"+downtimeCaptureDoUI.getCodeDownTime1()+",CodeDowntme2"+downtimeCaptureDoUI.getCodeDownTime2()+",DoownTmeEnd"+downtimeCaptureDoUI.getDowntmeEnd()
			+",DuartionDownCalc"+downtimeCaptureDoUI.getDurationDownCalc()+",Com"+downtimeCaptureDoUI.getComments()+",SyslockDate"+downtimeCaptureDoUI.getSysLockDate()
			+",SysModDate"+downtimeCaptureDoUI.getSysModDate()+",SysModUser"+downtimeCaptureDoUI.getSysModUser()+",SysCreateDate"+downtimeCaptureDoUI.getSysCreateDate());
			
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

	

	
}
