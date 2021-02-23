package com.murphy.integration.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dto.CommentsTbDto;
import com.murphy.integration.dto.DailyReportCommentsDto;
import com.murphy.integration.dto.DowntimeCapturedCADto;
import com.murphy.integration.entity.CommentsTbDo;
import com.murphy.integration.entity.ProdViewCommentsDo;
import com.murphy.integration.util.ProcountConstant;
import com.murphy.integration.util.ProdViewConstant;
import com.murphy.integration.util.ServicesUtil;

public class CommentsTbDao {

	private static final Logger logger = LoggerFactory.getLogger(CommentsTbDao.class);

	public CommentsTbDao() {
		super();
		// TODO Auto-generated constructor stub
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

	public CommentsTbDo getDataFromCommentsDB(Connection connection, int merrickId, String originalDateEntered)
			throws Exception {
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		CommentsTbDo commentsTbDo = null;

		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM DBO.CommentsTb WHERE ReferenceMerrickItem = ? AND OriginalDateEntered= ?");
			stmt.setInt(1, merrickId);
			stmt.setString(2, originalDateEntered);
			resultSet = stmt.executeQuery();

			boolean hasNext = resultSet.next();

			if (resultSet != null && hasNext) {
				commentsTbDo = new CommentsTbDo();
				while (hasNext) {
					commentsTbDo.setReferenceMerrickItem(resultSet.getInt("ReferenceMerrickItem"));
					commentsTbDo.setReferenceMerrickType(resultSet.getInt("ReferenceMerrickType"));
					commentsTbDo.setOriginalDateEntered(resultSet.getString("OriginalDateEntered"));
					commentsTbDo.setOriginalTimeEntered(resultSet.getString("OriginalTimeEntered"));
					commentsTbDo.setCommentType(resultSet.getInt("CommentType"));
					commentsTbDo.setCommentPurpose(resultSet.getString("CommentPurpose"));
					commentsTbDo.setCommentGeneral(resultSet.getString("CommentsGeneral"));
					commentsTbDo.setRioCommentCode(resultSet.getInt("RioCommentCode"));
					commentsTbDo.setRioProdCommentCode(resultSet.getString("RioProdCommentCode"));
					commentsTbDo.setPriorityType(resultSet.getInt("PriorityType"));
					commentsTbDo.setMessageSendFlag(resultSet.getInt("MessageSendFlag"));
					commentsTbDo.setDestinationPerson(resultSet.getInt("DestinationPerson"));
					commentsTbDo.setTempInteger(resultSet.getInt("TempInteger"));
					commentsTbDo.setLastTransmission(resultSet.getInt("LastTransmission"));
					commentsTbDo.setLastLoadDate(resultSet.getString("LastLoadDate"));
					commentsTbDo.setLastLoadTime(resultSet.getString("LastLoadTime"));
					commentsTbDo.setTransmitFlag(resultSet.getInt("TransmitFlag"));
					commentsTbDo.setDateTimeStamp(resultSet.getString("DateTimeStamp"));
					commentsTbDo.setUserDateStamp(resultSet.getString("UserDateStamp"));
					commentsTbDo.setUserTimeStamp(resultSet.getString("UserTimeStamp"));
					commentsTbDo.setUserID(resultSet.getInt("UserID"));
					commentsTbDo.setRowUID(resultSet.getString("RowUID"));
					commentsTbDo.setCommentServiceID(resultSet.getInt("CommentServiceID"));

					logger.error("[getDataFromCommentsDB] : INFO  - commentsTbDo" + commentsTbDo);

					return commentsTbDo;
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
		logger.error("[getDataFromCommentsDB] : INFO  - No record is present for merricId " + merrickId + " on date "
				+ originalDateEntered);
		return commentsTbDo;
	}

	public void insertDataInCommentsDB(Connection connection, CommentsTbDo commentsTbDo) throws Exception {

		PreparedStatement stmt = null;
		try {

			stmt = connection.prepareStatement(
					"INSERT INTO dbo.CommentsTb (ReferenceMerrickItem, ReferenceMerrickType, OriginalDateEntered, OriginalTimeEntered,"
							+ " CommentType, CommentPurpose, CommentsGeneral, RioCommentCode, RioProdCommentCode, PriorityType, "
							+ "MessageSendFlag, DestinationPerson, TempInteger, LastTransmission, LastLoadDate, "
							+ "LastLoadTime, TransmitFlag, DateTimeStamp, UserDateStamp, UserTimeStamp, UserID, RowUID, CommentServiceID) "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ");

			stmt.setInt(1, commentsTbDo.getReferenceMerrickItem());
			stmt.setInt(2, commentsTbDo.getReferenceMerrickType());
			stmt.setString(3, commentsTbDo.getOriginalDateEntered());
			stmt.setString(4, commentsTbDo.getOriginalTimeEntered());
			stmt.setInt(5, commentsTbDo.getCommentType());
			stmt.setString(6, commentsTbDo.getCommentPurpose());
			stmt.setString(7, commentsTbDo.getCommentGeneral());
			stmt.setInt(8, commentsTbDo.getRioCommentCode());
			stmt.setString(9, commentsTbDo.getRioProdCommentCode());
			stmt.setInt(10, commentsTbDo.getPriorityType());
			stmt.setInt(11, commentsTbDo.getMessageSendFlag());
			stmt.setInt(12, commentsTbDo.getDestinationPerson());
			stmt.setInt(13, commentsTbDo.getTempInteger());
			stmt.setInt(14, commentsTbDo.getLastTransmission());
			stmt.setString(15, commentsTbDo.getLastLoadDate());
			stmt.setString(16, commentsTbDo.getLastLoadTime());
			stmt.setInt(17, commentsTbDo.getTransmitFlag());
			stmt.setString(18, commentsTbDo.getDateTimeStamp());
			stmt.setString(19, commentsTbDo.getUserDateStamp());
			stmt.setString(20, commentsTbDo.getUserTimeStamp());
			stmt.setInt(21, commentsTbDo.getUserID());
			stmt.setString(22, commentsTbDo.getRowUID());
			stmt.setInt(23, commentsTbDo.getCommentServiceID());

			int insertedRowCount = stmt.executeUpdate();

			if (insertedRowCount > 0) {
				logger.error("[insertDataInCommentsDB] : INFO - Record inserted succesfully");
			}

		} catch (Exception e) {
			logger.error("[insertDataInCommentsDB] : ERROR- Exception while Inserting data in database " + e);
			throw e;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("[insertDataInCommentsDB] : ERROR- Exception while cleaning environment" + e);
			}
		}

	}

	public void updateDataInCommentsDB(Connection connection, CommentsTbDo commentsTbDo) throws Exception {
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(
					"UPDATE DBO.CommentsTb SET CommentsGeneral = ? WHERE ReferenceMerrickItem = ? AND OriginalDateEntered= ?");

			stmt.setString(1, commentsTbDo.getCommentGeneral());
			stmt.setInt(2, commentsTbDo.getReferenceMerrickItem());
			stmt.setString(3, commentsTbDo.getOriginalDateEntered());

			int updatedRowCount = stmt.executeUpdate();

			if (updatedRowCount > 0) {
				logger.error("[updateDataInCommentsDB] : INFO - Record updated succesfully");
			}

		} catch (Exception e) {
			logger.error("[updateDataInCommentsDB] : ERROR- Exception while updating data in database " + e);
			throw e;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("[updateDataInCommentsDB] : ERROR- Exception while cleaning environment" + e);
			}
		}
	}

	public CommentsTbDto getProcountCommentsForCanada(Connection connection, String wellName, String originalDateInDB)
			throws Exception {
		CommentsTbDto commentsDto = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String finalComment = null;
		String userName = null;
		String timeStamp = null;
		String comments = null;
		try {
			logger.error("[getProcountCommentsForCanadaNames][Print][WellName]" + wellName + "::OriginalDate"
					+ originalDateInDB);
			stmt = connection.prepareStatement(
					"SELECT Foreman_Name,Timestamp,Comment FROM [dbo].[Daily_Report_Foreman] WHERE WellName = ? AND Date= ? ORDER BY ID Desc");
			stmt.setString(1, wellName);
			stmt.setString(2, originalDateInDB);
			resultSet = stmt.executeQuery();

			boolean hasNext = resultSet.next();
			if (resultSet != null && hasNext) {
				commentsDto = new CommentsTbDto();

				while (hasNext) {
					userName = resultSet.getString("Foreman_Name");
					timeStamp = resultSet.getString("Timestamp");
					comments = resultSet.getString("Comment");

					if (!ServicesUtil.isEmpty(finalComment)) {
						finalComment = finalComment + ProcountConstant.COMMENT_IDENTIFIER;
					}
					if (!ServicesUtil.isEmpty(comments)) {
						finalComment = (ServicesUtil.isEmpty(finalComment) ? "" : finalComment) + userName + " ["
								+ timeStamp + "] : " + comments;
					}
					hasNext = resultSet.next();
				}
				commentsDto.setComments(finalComment);

			}
		} catch (Exception e) {

			logger.error("[getProcountCommentsForCanada] : ERROR- Exception while fetching data from database "
					+ e.getMessage());
			throw e;

		} finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[getProcountCommentsForCanada] : ERROR- Exception while cleaning environment" + e);
			}
		}
		return commentsDto;

	}

	public String fetchWellNameByMuwi(Connection connection, String uwId) throws Exception {
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String wellName = null;
		try {
			stmt = connection.prepareStatement(
					"SELECT TOP 1 COMPLETIONNAME FROM  [dbo].[SOR_Daily_Production_mobile_well_v1] WHERE MUWI=?");
			stmt.setString(1, uwId);
			resultSet = stmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					wellName = resultSet.getString("COMPLETIONNAME");
					return wellName;
				}
			}
		} catch (Exception e) {
			logger.error(
					"[fetchMerrickFromDB] : ERROR- Exception while fetching MerrickId from database " + e.getMessage());
			throw e;
		} finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[fetchMerrickFromDB] : ERROR- Exception while cleaning environment" + e.getMessage());
			}
		}
		return wellName;
	}

	public Map<String, String> fetchIdFromPvtUnitComp(Connection connection, String muwi) throws Exception {
		Map<String,String> pvUnitCom=new HashMap<String,String>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = connection.prepareStatement("SELECT TOP 1 idflownet,idrecparent FROM dbo.pvt_pvunitcomp WHERE wellidd = ? order by syscreatedate Desc");
			stmt.setString(1, muwi);
			resultSet = stmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					pvUnitCom.put("idflownet",resultSet.getString("idflownet"));
					pvUnitCom.put("idrecparent",resultSet.getString("idrecparent"));
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

	public ProdViewCommentsDo getDataFromProdViewCommentsDB(Connection connection,ProdViewCommentsDo prodViewCommentsDo) throws Exception {
			 logger.error("[getDataFromProdViewCommentsDB] : Inside - ProdView");
			 ProdViewCommentsDo prodDataDto=null;
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			try {
				stmt = connection.prepareStatement(
						"SELECT * FROM dbo.[pvt_pvunitremark] WHERE idflownet = ? AND idrecparent= ? AND cast(syscreatedate as date)=?");
				stmt.setString(1, prodViewCommentsDo.getIdflownet());
				stmt.setString(2, prodViewCommentsDo.getIdrecParent());
				stmt.setString(3,ServicesUtil.convertFromZoneToZoneString(null, prodViewCommentsDo.getSysCreateDate(), null,
						null,ProdViewConstant.DATE_STANDARD,ProdViewConstant.DATE_STANDARD));
				 logger.error("[getDataFromProdViewCommentsDB] sysCreatedDate :"+ServicesUtil.convertFromZoneToZoneString(null, prodViewCommentsDo.getSysCreateDate(), null,
							null,ProdViewConstant.DATE_DB_FORMATE,ProdViewConstant.DATE_STANDARD));
				resultSet = stmt.executeQuery();

				boolean hasNext = resultSet.next();

				if (resultSet != null && hasNext) {
					prodDataDto=new ProdViewCommentsDo();
					while (hasNext) {
						prodDataDto.setIdflownet(resultSet.getString(("idflownet")));
						prodDataDto.setIdrecParent(resultSet.getString(("idrecparent")));
						prodDataDto.setIdrec(resultSet.getString(("idrec")));
						prodDataDto.setCom(resultSet.getString(("com")));
						prodDataDto.setDttm(resultSet.getString(("dttm")));
						prodDataDto.setSysCreateDate(resultSet.getString(("syscreatedate")));
						 logger.error("[getDataFromDB] : INFO - getDataFromProdViewCommentsDB"
						 + prodDataDto);

						return prodDataDto;
					}
				}

			} catch (Exception e) {

				logger.error("[getDataFromProdViewCommentsDB] : ERROR- Exception while fetching data from database " + e.getMessage());
				throw e;

			} finally {

				try {
					stmt.close();
					resultSet.close();
				} catch (SQLException e) {
					logger.error("[getDataFromProdViewCommentsDB] : ERROR- Exception while cleaning environment" + e.getMessage());
				}

			}
			return prodDataDto;
		}

	public void updateDataInProdViewCommentsDB(Connection connection, ProdViewCommentsDo prodViewCommentsDo) throws Exception {
		PreparedStatement stmt = null;
		logger.error("updateDataInProdViewCommentsDB"+prodViewCommentsDo);

		try {
			stmt = connection.prepareStatement(
					"UPDATE DBO.pvt_pvunitremark SET com = ?, sysmoddate = ?,sysmoduser = ? ,syslockdate = cast(syscreatedate-1 as date)"
							+ " WHERE idflownet = ? AND idrec= ? AND cast(syscreatedate as date)=?");
			
			stmt.setString(1, prodViewCommentsDo.getCom());
			stmt.setString(2, ServicesUtil.convertFromZoneToZoneString(new Date(),null,ProdViewConstant.UTC_ZONE,ProdViewConstant.MST_ZONE, ProdViewConstant.DATEFORMAT_T,
					ProdViewConstant.DATE_DB_FORMATE));
			stmt.setString(3, ProcountConstant.SVC_IOP);
			stmt.setString(4, prodViewCommentsDo.getIdflownet());
			stmt.setString(5, prodViewCommentsDo.getIdrec());
			stmt.setString(6, prodViewCommentsDo.getSysCreateDate());

			logger.error("ProdViewCommentsDB"+prodViewCommentsDo);
			
			int updatedRowCount = stmt.executeUpdate();

			if (updatedRowCount > 0) {
				logger.error("[updateDataInProdViewCommentsDB] : INFO - Record updated succesfully");
			}

		} catch (Exception e) {
			logger.error("[updateDataInProdViewCommentsDB] : ERROR- Exception while updating data in database " + e.getMessage());
			throw e;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("[updateDataInProdViewCommentsDB] : ERROR- Exception while cleaning environment" + e.getMessage());
			}
		}		
	}

	public void insertDataInProdViewCommentsDB(Connection connection, ProdViewCommentsDo prodViewCommentsDo) throws Exception {
		logger.error("[insertDataInProdViewDB] :Inside Method");
		PreparedStatement stmt = null;
		String idrec=null;
		try {

			stmt = connection.prepareStatement(
					"INSERT INTO [dbo].[pvt_pvunitremark]([idflownet],[idrecparent],[idrec],[dttm],[com],[idrecitem],[idrecitemtk],[syslockdate]"
					+ ",[sysmoddate],[sysmoduser],"
					+ "[syscreatedate],[syscreateuser],[systag]) "
					+ "VALUES (?, ?,REPLACE(NEWID(),'-','') , ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
			stmt.setString(1, prodViewCommentsDo.getIdflownet());
			stmt.setString(2, prodViewCommentsDo.getIdrecParent());
			stmt.setString(3,prodViewCommentsDo.getDttm());
			stmt.setString(4, prodViewCommentsDo.getCom());
			stmt.setString(5, prodViewCommentsDo.getIdrecParent());
			stmt.setString(6, prodViewCommentsDo.getIdrecItemtk());
			stmt.setString(7, prodViewCommentsDo.getSysLockDate());
			stmt.setString(8, prodViewCommentsDo.getSysModDate());
			stmt.setString(9, prodViewCommentsDo.getSysModUser());
			stmt.setString(10, prodViewCommentsDo.getSysCreateDate());
			stmt.setString(11, prodViewCommentsDo.getSysCreateUser());
			stmt.setString(12, prodViewCommentsDo.getSystag());
			
			
			logger.error("[Murphy][insertDataInProdViewDB]{field}idflownet"+prodViewCommentsDo.getIdflownet()+",idrecparent"+prodViewCommentsDo.getIdrecParent()+",Dttm"+prodViewCommentsDo.getDttm()+",Com"+prodViewCommentsDo.getCom()
			+",idrecitem"+prodViewCommentsDo.getIdrecItem()+",idrecitemtk"+prodViewCommentsDo.getIdrecItemtk()+",syslockdate"+prodViewCommentsDo.getSysLockDate()
			+",sysmoddate"+prodViewCommentsDo.getSysModDate()+",sysmoduser"+prodViewCommentsDo.getSysModUser()+",sysCreatedate"+prodViewCommentsDo.getSysCreateDate());
			
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
