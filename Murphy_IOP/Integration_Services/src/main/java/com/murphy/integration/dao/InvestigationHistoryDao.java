package com.murphy.integration.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dto.InvestigationHistoryDto;
import com.murphy.integration.util.ApplicationConstant;
import com.murphy.integration.util.ServicesUtil;


public class InvestigationHistoryDao {

	private static final Logger logger = LoggerFactory.getLogger(InvestigationHistoryDao.class);

	public InvestigationHistoryDao() {
		super();
	}
		
	

	public List<InvestigationHistoryDto> fetchInvestigationData(Connection connection, String uwiId) throws Exception {
		List<InvestigationHistoryDto> investigationHistoryDtoList = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try {
			
			String query = "SELECT ISNULL(CONVERT(VARCHAR(30),AO.SHUT_IN_DATE),'-') AS SHUT_IN_DATE, (ISNULL(AO.SHUT_IN_REASON,'-')) AS SHUT_IN_REASON, " + 
					"ISNULL(CONVERT(VARCHAR(30),AO.RETURN_TO_PRODUCTION_DATE),'-') AS RETURN_TO_PRODUCTION_DATE, " + 
					"ISNULL(CONVERT(VARCHAR(30), CAST (AO.RIG_DOWN_MOVE_OUT_DATE AS DATE)),'-') AS RIG_DOWN_MOVE_OUT_DATE, " + 
					"ISNULL(CONVERT(VARCHAR(30),PB.DATE),'-') AS DATE, (ISNULL(PB.SUSPECTED_FAILURE,'-')) AS SUSPECTED_FAILURE, " + 
					"(ISNULL(WO.SCOPE_OF_WORK,'-')) AS SCOPE_OF_WORK, " + 
					"(ISNULL(WO.SUMMARY_COMMENT,'-')) AS SUMMARY_COMMENT, AO.PPRODUCTION_PERIOD " + 
					"FROM [ALS_DB].[DBO].[ALS_OPERATING_INFORMATION] AS AO " + 
					"LEFT OUTER JOIN [ALS_DB].[DBO].[PROBLEM_WELL_REPORT] AS PB " + 
					"ON AO.PMUWI = PB.PMUWI AND  AO.PPRODUCTION_PERIOD = PB.PPRODUCTION_PERIOD " + 
					"LEFT OUTER JOIN [ALS_DB].[DBO].[WORK_OVER_INSPECTION] AS WO " + 
					"ON AO.PMUWI = WO.PMUWI AND  AO.PPRODUCTION_PERIOD = WO.PPRODUCTION_PERIOD " + 
					"WHERE AO.PMUWI= ? " + 
					"AND (AO.SHUT_IN_DATE IS NOT NULL OR AO.SHUT_IN_REASON IS NOT NULL OR AO.RETURN_TO_PRODUCTION_DATE IS NOT NULL OR " + 
					"AO.RIG_DOWN_MOVE_OUT_DATE IS NOT NULL OR PB.DATE IS NOT NULL OR PB.SUSPECTED_FAILURE IS NOT NULL OR " + 
					"WO.SCOPE_OF_WORK IS NOT NULL OR WO.SUMMARY_COMMENT IS NOT NULL) ";
			
//			logger.error("[fetchInvestigationData] : INFO  - Query -" + query);
			
			stmt = connection.prepareStatement(query);
			stmt.setString(1, uwiId);
			resultSet = stmt.executeQuery();
			
			boolean hasNext = resultSet.next();

			if (resultSet != null && hasNext) {
				investigationHistoryDtoList = new ArrayList<InvestigationHistoryDto>();
				
				while (hasNext) {
					
					InvestigationHistoryDto investigationHistoryDto = new InvestigationHistoryDto();
					
					//investigationHistoryDto.setShutInDate("-".equals(resultSet.getString("Shut_in_Date")) ? "-": ServicesUtil.convertFromZoneToZoneString(null, resultSet.getString("Shut_in_Date"), "", "", ApplicationConstant.ALS_DATE_FORMAT, ApplicationConstant.DATE_DISPLAY_FORMAT_NOTIME));
					investigationHistoryDto.setShutInDate("-".equals(resultSet.getString("Shut_in_Date")) ? "-": ServicesUtil.convertToEpoch(resultSet.getString("Shut_in_Date"), ApplicationConstant.ALS_DATE_FORMAT));
					
					investigationHistoryDto.setShutInReason(resultSet.getString("Shut_in_Reason"));
					
					//investigationHistoryDto.setRtpDate("-".equals(resultSet.getString("Return_to_Production_Date")) ? "-": ServicesUtil.convertFromZoneToZoneString(null, resultSet.getString("Return_to_Production_Date"), "", "", ApplicationConstant.ALS_DATE_FORMAT, ApplicationConstant.DATE_DISPLAY_FORMAT_NOTIME));
					investigationHistoryDto.setRtpDate("-".equals(resultSet.getString("Return_to_Production_Date")) ? "-": ServicesUtil.convertToEpoch(resultSet.getString("Return_to_Production_Date"), ApplicationConstant.ALS_DATE_FORMAT));
					
					if(investigationHistoryDto.getRtpDate().equalsIgnoreCase("-")){
						investigationHistoryDto.setRtpComment("-");
					}else{
						investigationHistoryDto.setRtpComment("RTP");
					}
					//investigationHistoryDto.setPwrDate("-".equals(resultSet.getString("Date")) ? "-": ServicesUtil.convertFromZoneToZoneString(null, resultSet.getString("Date"), "", "", ApplicationConstant.ALS_DATE_FORMAT, ApplicationConstant.DATE_DISPLAY_FORMAT_NOTIME));
					investigationHistoryDto.setPwrDate("-".equals(resultSet.getString("Date")) ? "-": ServicesUtil.convertToEpoch(resultSet.getString("Date"), ApplicationConstant.ALS_DATE_FORMAT));
					
					investigationHistoryDto.setSuspectedFailure(resultSet.getString("Suspected_Failure"));
					
					//investigationHistoryDto.setJobCompleteionDate("-".equals(resultSet.getString("Rig_Down_Move_Out_Date")) ? "-": ServicesUtil.convertFromZoneToZoneString(null, resultSet.getString("Rig_Down_Move_Out_Date"), "", "", ApplicationConstant.ALS_DATE_FORMAT, ApplicationConstant.DATE_DISPLAY_FORMAT_NOTIME));
					
					//investigationHistoryDto.setJobCompleteionDate("-".equals(resultSet.getString("Rig_Down_Move_Out_Date")) ? "-": ServicesUtil.convertToEpoch(resultSet.getString("Rig_Down_Move_Out_Date"), ApplicationConstant.ALS_DATE_FORMAT));	
					
//					INC0106753
//					IOP: Missing Investigation Well History
//soc
					investigationHistoryDto.setJobCompleteionDate("-".equals(resultSet.getString("Rig_Down_Move_Out_Date")) ? null: ServicesUtil.convertToEpoch(resultSet.getString("Rig_Down_Move_Out_Date"), ApplicationConstant.ALS_DATE_FORMAT));	
					//eoc
					investigationHistoryDto.setJobType(resultSet.getString("Scope_Of_Work"));
					investigationHistoryDto.setJobSummary(resultSet.getString("Summary_Comment"));
					investigationHistoryDto.setProductionPeriod(resultSet.getString("PProduction_Period"));
					investigationHistoryDto.setSource("ALS");
					
//					logger.error("[fetchInvestigationData] : INFO  - investigationHistoryDto" + investigationHistoryDto);
					investigationHistoryDtoList.add(investigationHistoryDto);
					hasNext = resultSet.next();
				}
			}
		} catch (Exception e) {

			logger.error("[fetchInvestigationData] : ERROR- Exception while fetching data from database " + e);
			throw e;

		} finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[fetchInvestigationData] : ERROR- Exception while cleaning environment" + e);
			}
		}
//		logger.error("[fetchInvestigationData] : INFO  - investigationHistoryDtoList " + investigationHistoryDtoList);
		return investigationHistoryDtoList;
	}
	
	
}
