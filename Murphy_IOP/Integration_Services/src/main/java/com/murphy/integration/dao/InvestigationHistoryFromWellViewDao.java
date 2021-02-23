package com.murphy.integration.dao;

import java.sql.Connection;
import java.sql.DriverManager;
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

public class InvestigationHistoryFromWellViewDao {
	
	private static final Logger logger = LoggerFactory.getLogger(InvestigationHistoryDao.class);

		
	
	private static final String WELLVIEW_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public synchronized static Connection testtOnprimeCDbonnection(String url,String userName,String password) {
		try {
			ServicesUtil.setupSOCKS(ApplicationConstant.CLOUD_HOUST_LOCATION);
			Class.forName(WELLVIEW_DRIVER_CLASS);

			logger.error("wellview db url : " +url);
			
			logger.error("user id : " +userName);
			logger.error("pwd : " +password);
			
			return DriverManager.getConnection(url,
					userName, password);

		} catch (ClassNotFoundException e) {
			logger.error("[createConnectionForWellView] : Error- Class Not Found Exception while creating connection " + e); 
		} catch (SQLException e) {
			logger.error("[createConnectionForWellView] : Error- SQL Exception while creating connection " + e); 
		} catch (Exception e) {
			logger.error("[createConnectionForWellView] : Error- Exception while creating connection " + e); 
		}
		return null;
	}
	

	
	
	
	
	public List<InvestigationHistoryDto> fetchInvestigationData(Connection connection, String uwiId) throws Exception {
		List<InvestigationHistoryDto> investigationHistoryDtoList = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try {
			
			String query = " SELECT a.wellname, a.wellidd, "
					+ "ISNULL(CONVERT(VARCHAR(30),b.dttmstart),'-') AS dttmstart,"
					+ "ISNULL(CONVERT(VARCHAR(30),b.dttmend),'-') AS dttmend,"
					+ "ISNULL(CONVERT(VARCHAR(30),b.jobtyp),'-') AS jobtyp, "
					+ "ISNULL(CONVERT(VARCHAR(30),b.jobsubtyp),'-') AS jobsubtyp, "
					+ "ISNULL(CONVERT(VARCHAR(30),b.status2),'-') AS status2, "
					+ "ISNULL(CONVERT(VARCHAR(30),b.wvtyp),'-') AS wvtyp, "
					+ "ISNULL(CONVERT(VARCHAR(30),c.usertxt1),'-') AS Global_Summary, "
					+ "ISNULL(CONVERT(VARCHAR(30),c.summaryops),'-') AS Operations_Summary, "
					+ "ISNULL(CONVERT(VARCHAR(30),c.plannextrptops),'-') AS t24_Hr_forecast, "
					+ "ISNULL(CONVERT(VARCHAR(30),c.remarks),'-') AS remarks "
					+ "FROM wvt_wvwellheader a, wvt_wvjob b ,wvt_wvjobreport c "
					+ "WHERE a.idwell = b.idwell AND a.idwell=c.idwell "
					+ "AND b.idrec=c.idrecparent AND wellidd= ? AND b.wvtyp = 'Intervention' "
					+ " ORDER BY dttmend DESC";
			
			logger.error("[fetchInvestigationDataFromWellView] : INFO  - Query -" + query);
			System.err.println("fetch query : " +query);
			stmt = connection.prepareStatement(query);
			stmt.setString(1, uwiId);
			resultSet = stmt.executeQuery();
			
			boolean hasNext = resultSet.next();

			if (resultSet != null && hasNext) {
				investigationHistoryDtoList = new ArrayList<InvestigationHistoryDto>();
				
				while (hasNext) {
					
					InvestigationHistoryDto investigationHistoryDto = new InvestigationHistoryDto();
					
				
					investigationHistoryDto.setJobStartDate("-".equals(resultSet.getString("dttmstart")) ? "-":
					ServicesUtil.convertToEpoch(resultSet.getString("dttmstart"), ApplicationConstant.WELL_VIEW_DATE_FORMAT));
					
					//investigationHistoryDto.setShutInReason(resultSet.getString("wvtyp"));
					
			
					
					investigationHistoryDto.setJobCompleteionDate("-".equals(resultSet.getString("dttmend")) ? "-": 
					ServicesUtil.convertToEpoch(resultSet.getString("dttmend"), ApplicationConstant.WELL_VIEW_DATE_FORMAT));	
					
					investigationHistoryDto.setJobType(resultSet.getString("jobtyp"));
					investigationHistoryDto.setJobSubtype(resultSet.getString("jobsubtyp"));
					investigationHistoryDto.setGlobalSummary(resultSet.getString("Global_Summary"));
					investigationHistoryDto.setOperationsSummary(resultSet.getString("Operations_Summary"));
					investigationHistoryDto.setTwentyFourhrForecast(resultSet.getString("t24_Hr_forecast"));
					investigationHistoryDto.setRemarks(resultSet.getString("remarks"));
					
					investigationHistoryDto.setSource("WellView");
					
					logger.error("[fetchInvestigationDataForWellViewDto] : INFO  - investigationHistoryDto" + investigationHistoryDto);
					investigationHistoryDtoList.add(investigationHistoryDto);
					hasNext = resultSet.next();
				}
			}
		} catch (Exception e) {

			logger.error("[fetchInvestigationDataForWellView] : ERROR- Exception while fetching data from database " + e);
			throw e;

		} finally {
			try {
				
				resultSet.close();
				stmt.close();
			} catch (SQLException e) {
				logger.error("[fetchInvestigationData] : ERROR- Exception while cleaning environment" + e);
			}
		}
//		logger.error("[fetchInvestigationData] : INFO  - investigationHistoryDtoList " + investigationHistoryDtoList);
		return investigationHistoryDtoList;
	}

}
