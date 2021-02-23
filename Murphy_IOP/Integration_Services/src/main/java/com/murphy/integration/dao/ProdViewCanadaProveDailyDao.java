package com.murphy.integration.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dto.EnersightProveMonthlyDto;
import com.murphy.integration.util.ApplicationConstant;
import com.murphy.integration.util.ServicesUtil;

public class ProdViewCanadaProveDailyDao {

	private Connection connection;

	private static final Logger logger = LoggerFactory.getLogger(ProdViewCanadaProveDailyDao.class);

	public ProdViewCanadaProveDailyDao(Connection connection) {
		this.connection = connection;
	}

	
	/**
	 * This method is used by PROVE report to fetch production data for the 7
	 * days Average or 30 Days Average
	 * @param uom 
	 *
	 */
	public List<EnersightProveMonthlyDto> fetchProveData(Map<String, String> uwiIdMap, Integer duration,
			List<String> InvestigationNIPlocationCodeList, List<String> inquiryNIPlocationCodeList, String page, String uom)
			throws Exception {
		List<EnersightProveMonthlyDto> enersightProveMonthlyDtoList = null;
		EnersightProveMonthlyDto enersightProveMonthlyDto = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String uwiIdListQuery = ServicesUtil.getQueryString(uwiIdMap.keySet(), "MUWI", new ArrayList<String>());

		String firstDateOfLastMonth = null;
		String lastDateOfLastMonth = null;

		SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dayBaseQuery = null;
		try {
			if (duration == 7) {
				dayBaseQuery = "[RecordDate]>= Dateadd(\"DD\",?, SYSDATETIME()) and [RecordDate]<= Dateadd(\"DD\",-2, SYSDATETIME())";
			} else if (duration == 30) {
				

				firstDateOfLastMonth = SimpleDateFormat.format(calculatefirstDateOfLastMonth());
				lastDateOfLastMonth = SimpleDateFormat.format(calculateLastDateOfLastMonth());
				dayBaseQuery = "[RecordDate]>= ? and [RecordDate]<= ?";

			}
			
			
			String query=null;
			if (ApplicationConstant.UOM_E3M3.equalsIgnoreCase(uom)) {
				
				 query = "SELECT MUWI, round(X.ACTUAL_BOED,2) AS ACTUAL_BOED, round(X.FORECAST_BOED,2) AS FORECAST_BOED, round(X.ACTUAL_BOED-X.FORECAST_BOED,2) AS VAR_TO_FORECAST_BOED, "
						+ " case when X.FORECAST_BOED = 0 then null else round(100*(X.ACTUAL_BOED-X.FORECAST_BOED)/X.FORECAST_BOED,2) end AS PER_VAR_TO_FORECAST_BOED, "
						+ " round(X.OIL,2) AS OIL,round(X.GAS,2) AS GAS, X.WELL_NAME, X.PRODUCTION_DATE FROM ( "
						+ " select MUWI,AVG(PV_ALLOC_GAS_MCF/35.3146667) AS ACTUAL_BOED, AVG(PV_TARGET_GAS_MCF/35.3146667) AS FORECAST_BOED, "
						+ " AVG(PV_GROSSOILPROD_BBL) AS OIL,AVG(PV_ALLOC_GAS_MCF/ 35.3146667) AS GAS, COMPLETION_NAME AS WELL_NAME, CAST(MAX(RecordDate) AS DATE) AS PRODUCTION_DATE "
						+ " from [IOP_Read].[dbo].[PV_CAN_Prove_Daily] WHERE (" + uwiIdListQuery + ") AND " + dayBaseQuery
						+ " group by MUWI, COMPLETION_NAME) X ORDER BY VAR_TO_FORECAST_BOED DESC";

			}else{
				
				 query = "SELECT MUWI, round(X.ACTUAL_BOED,2) AS ACTUAL_BOED, round(X.FORECAST_BOED,2) AS FORECAST_BOED, round(X.ACTUAL_BOED-X.FORECAST_BOED,2) AS VAR_TO_FORECAST_BOED, "
						+ " case when X.FORECAST_BOED = 0 then null else round(100*(X.ACTUAL_BOED-X.FORECAST_BOED)/X.FORECAST_BOED,2) end AS PER_VAR_TO_FORECAST_BOED, "
						+ " round(X.OIL,2) AS OIL,round(X.GAS,2) AS GAS, X.WELL_NAME, X.PRODUCTION_DATE FROM ( "
						+ " select MUWI,AVG(PV_GROSSOILPROD_BBL+(PV_ALLOC_GAS_MCF/6)) AS ACTUAL_BOED, AVG(TARGET_BOE) AS FORECAST_BOED, "
						+ " AVG(PV_GROSSOILPROD_BBL) AS OIL,AVG(PV_ALLOC_GAS_MCF) AS GAS, COMPLETION_NAME AS WELL_NAME, CAST(MAX(RecordDate) AS DATE) AS PRODUCTION_DATE "
						+ " from [IOP_Read].[dbo].[PV_CAN_Prove_Daily] WHERE (" + uwiIdListQuery + ") AND " + dayBaseQuery
						+ " group by MUWI, COMPLETION_NAME) X ORDER BY VAR_TO_FORECAST_BOED DESC";
			}
			
			
			


			if (!ServicesUtil.isEmpty(page)) {
				int pageNo = Integer.parseInt(page);
				if (pageNo > 0) {
					query = query + " offset " + (pageNo - 1) * 20 + " ROWS fetch NEXT " + 20 + " ROWS ONLY";
				}
			}

			try {
				stmt = connection.prepareStatement(query);

				if (duration == 7) {
					stmt.setInt(1, -(duration + 2));

				} else if (duration == 30) {
					stmt.setString(1, firstDateOfLastMonth);
					stmt.setString(2, lastDateOfLastMonth);

				}
				logger.error("[EnersightProveDailyDao] [fetchProveData] [Query] : [ " + query + " ]");
				resultSet = stmt.executeQuery();

				if (resultSet != null) {
					enersightProveMonthlyDtoList = new ArrayList<>();
					while (resultSet.next()) {
						enersightProveMonthlyDto = new EnersightProveMonthlyDto();
						enersightProveMonthlyDto.setWell(resultSet.getString("WELL_NAME"));
						enersightProveMonthlyDto.setAvgActualBoed(resultSet.getDouble("ACTUAL_BOED"));
						enersightProveMonthlyDto.setAvgForecastBoed(resultSet.getDouble("FORECAST_BOED"));
						enersightProveMonthlyDto.setAvgVarToForecastBoed(resultSet.getDouble("VAR_TO_FORECAST_BOED"));
						enersightProveMonthlyDto
								.setAvgPerVarToForecastBoed(resultSet.getDouble("PER_VAR_TO_FORECAST_BOED"));
						enersightProveMonthlyDto.setAvgOil(resultSet.getDouble("OIL"));
						// enersightProveMonthlyDto.setAvgWater(resultSet.getDouble("WATER"));
						enersightProveMonthlyDto.setAvgGas(resultSet.getDouble("GAS"));
						enersightProveMonthlyDto.setLastProdDate(ServicesUtil.convertFromZoneToZoneString(null,
								resultSet.getString("PRODUCTION_DATE"), "", "", "yyyy-MM-dd", "dd-MMM-yy"));
						enersightProveMonthlyDto.setLastProdDateField(ServicesUtil.convertFromZoneToZone(null,
								resultSet.getString("PRODUCTION_DATE"), "", "", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"));
						enersightProveMonthlyDto.setMaxThresholdValue(100);
						String muwiId = resultSet.getString("MUWI");
						enersightProveMonthlyDto.setMuwiId(muwiId);
						if (muwiId != null) {
							String locationCode = uwiIdMap.get(muwiId);
							enersightProveMonthlyDto.setLocationCode(locationCode);
							if (locationCode != null) {
								if (InvestigationNIPlocationCodeList.contains(locationCode)) {
									enersightProveMonthlyDto.setInvestigationInProgress(true);
								}
								if (inquiryNIPlocationCodeList.contains(locationCode)) {
									enersightProveMonthlyDto.setInquiryInProgress(true);
								}
							}
						}
						enersightProveMonthlyDtoList.add(enersightProveMonthlyDto);
					}
				}
			} catch (Exception e) {
				logger.error("[ProdViewCanadaProveDailyDao.fetchProveData(): ERROR- Exception while fetching data from database " + e);
				throw e;

			} finally {
				try {
					stmt.close();
					resultSet.close();
				} catch (SQLException e) {
					logger.error("ProdViewCanadaProveDailyDao.fetchProveData() : ERROR- Exception while cleaning environment" + e);
				}
			}
		} catch (Exception e) {
			logger.error("ProdViewCanadaProveDailyDao.fetchProveData() " + e.getMessage());
			throw e;
		}
		return enersightProveMonthlyDtoList;

	}

	public Date calculatefirstDateOfLastMonth() {
		Calendar aCalendar = Calendar.getInstance();
		// add -1 month to current month
		aCalendar.add(Calendar.MONTH, -1);
		// set DATE to 1, so first date of previous month
		aCalendar.set(Calendar.DATE, 1);

		Date firstDateOfPreviousMonth = aCalendar.getTime();

		return firstDateOfPreviousMonth;
	}

	public Date calculateLastDateOfLastMonth() {
		Calendar aCalendar = Calendar.getInstance();
		// add -1 month to current month
		aCalendar.add(Calendar.MONTH, -1);
		aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		// read it
		Date lastDateOfPreviousMonth = aCalendar.getTime();
		return lastDateOfPreviousMonth;
	}

}
