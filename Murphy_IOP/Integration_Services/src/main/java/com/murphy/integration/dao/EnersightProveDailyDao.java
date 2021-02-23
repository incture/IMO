package com.murphy.integration.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dto.EnersightProveDailyDto;
import com.murphy.integration.dto.EnersightProveMonthlyDto;
import com.murphy.integration.util.DBConnections;
import com.murphy.integration.util.ProdViewConstant;
import com.murphy.integration.util.ServicesUtil;


public class EnersightProveDailyDao {
	private static final Logger logger = LoggerFactory.getLogger(EnersightProveDailyDao.class);

	/**
	 * This method is used by DOP Forecast Scheduler
	 */
	public List<EnersightProveDailyDto> fetchProveDailyData(String startTimeInString) throws Exception {
		logger.debug("EnersightProveDailyDao.fetchProveDailyData(" + startTimeInString + ")");

		Connection connection = DBConnections.createConnectionForProve();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<EnersightProveDailyDto> returnList = new ArrayList<EnersightProveDailyDto>();
		if (connection != null) {
			try {
				String query = "SELECT UWI , AVG(EnersightOil) AS FORECAST FROM [IOP_Read].[dbo].[EnersightProveDaily] WHERE RecorDate = '"
						+ startTimeInString + "' GROUP BY UWI";
				stmt = connection.prepareStatement(query);

				// STEP1: Fetch records
				resultSet = stmt.executeQuery();

				// STEP 2: Export to resultList to returnList
				if (!ServicesUtil.isEmpty(resultSet)) {

					while (resultSet.next()) {
						EnersightProveDailyDto dailyDto = new EnersightProveDailyDto();
						dailyDto.setMuwiId(resultSet.getString("UWI"));
						dailyDto.setForecastBoed(resultSet.getDouble("FORECAST"));
						returnList.add(dailyDto);
					}

				}

			} catch (Exception e) {
				logger.error("EnersightProveDailyDao.fetchProveDailyData()[error]" + e.getMessage());
				throw e;
			} finally {
				try {
					stmt.close();
					resultSet.close();
					connection.close();
				} catch (SQLException e) {
					logger.error("[fetchProveDailyData] : ERROR- Exception while cleaning environment" + e);
				}
			}

		} else {
			logger.error(
					"[Murphy][EnersightProveDaily][fetchProveDailyData][error] Connection to Database is not possible");
		}
		return returnList;
	}

	/**
	 * This method is used by PROVE report to fetch production data for the 7
	 * days Average or 30 Days Average
	 *
	 */
	public List<EnersightProveMonthlyDto> fetchProveData(Connection connection, Map<String, String> uwiIdMap,
			Integer duration, List<String> InvestigationNIPlocationCodeList, List<String> inquiryNIPlocationCodeList,
			String page) throws Exception {
		List<EnersightProveMonthlyDto> enersightProveMonthlyDtoList = null;
		EnersightProveMonthlyDto enersightProveMonthlyDto = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String uwiIdListQuery = ServicesUtil.getQueryString(uwiIdMap.keySet(), "UWI", new ArrayList<String>());

		String firstDateOfLastMonth = null;
		String lastDateOfLastMonth = null;

		SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dayBaseQuery = null;

		try {
			if (duration == 7) {
				dayBaseQuery = "[RecorDate]>= Dateadd(\"DD\",?, SYSDATETIME()) and [RecorDate]<= Dateadd(\"DD\",-2, SYSDATETIME())";
			} else if (duration == 30) {

				firstDateOfLastMonth = SimpleDateFormat.format(calculatefirstDateOfLastMonth());
				lastDateOfLastMonth = SimpleDateFormat.format(calculateLastDateOfLastMonth());
				dayBaseQuery = "[RecorDate]>= ? and [RecorDate]<= ?";

			}

			String query = "SELECT UWI, round(X.ACTUAL_BOED,2) AS ACTUAL_BOED, round(X.FORECAST_BOED,2) AS FORECAST_BOED, round(X.ACTUAL_BOED-X.FORECAST_BOED,2) AS VAR_TO_FORECAST_BOED, "
					+ " case when X.FORECAST_BOED = 0 then null else round(100*(X.ACTUAL_BOED-X.FORECAST_BOED)/X.FORECAST_BOED,2) end AS PER_VAR_TO_FORECAST_BOED, "
					+ " round(X.OIL,2) AS OIL, round(X.WATER,2) AS WATER, round(X.GAS,2) AS GAS, X.WELL_NAME, X.PRODUCTION_DATE FROM ( "
					+ " select UWI,AVG(MerrickGrossBOE) AS ACTUAL_BOED, AVG(EnersightGrossBOE) AS FORECAST_BOED, "
					+ " AVG(MerrickGrossOilProd) AS OIL, AVG(MerrickGrossWaterProd) AS WATER, AVG(MerrickGrossGasProd) AS GAS, WellName AS WELL_NAME, CAST(MAX(RecorDate) AS DATE) AS PRODUCTION_DATE "
					+ " from [IOP_Read].[dbo].[EnersightProveDaily] WHERE (" + uwiIdListQuery + ") AND " + dayBaseQuery
					+ " group by UWI, WellName) X ORDER BY VAR_TO_FORECAST_BOED DESC";

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
						enersightProveMonthlyDto.setAvgWater(resultSet.getDouble("WATER"));
						enersightProveMonthlyDto.setAvgGas(resultSet.getDouble("GAS"));
						enersightProveMonthlyDto.setLastProdDate(ServicesUtil.convertFromZoneToZoneString(null,
								resultSet.getString("PRODUCTION_DATE"), "", "", "yyyy-MM-dd", "dd-MMM-yy"));
						enersightProveMonthlyDto.setLastProdDateField(ServicesUtil.convertFromZoneToZone(null,
								resultSet.getString("PRODUCTION_DATE"), "", "", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"));
						enersightProveMonthlyDto.setMaxThresholdValue(100);
						String muwiId = resultSet.getString("UWI");
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

				logger.error("[fetchProveData] : ERROR- Exception while fetching data from database " + e);
				throw e;

			} finally {
				try {
					stmt.close();
					resultSet.close();
				} catch (SQLException e) {
					logger.error("[fetchProveData] : ERROR- Exception while cleaning environment" + e);
				}
			}
		} catch (Exception e) {
			logger.error("[fetchProveData] : ERROR- Date Format Exception " + e);
			throw e;
		}
		return enersightProveMonthlyDtoList;
	}

	/**
	 * This method fetches the forecast data for the first date of current month
	 * 
	 */
	public List<EnersightProveDailyDto> fetchProveDailyData(Connection connection) throws Exception {

		List<EnersightProveDailyDto> enersightProveDailyDtoList = null;

		EnersightProveDailyDto enersightProveDailyDto = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		String firstDateOfCurrentMonth = null;

		SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {

			firstDateOfCurrentMonth = SimpleDateFormat.format(calculatefirstDateOfCurrentMonth());

			String query = "SELECT UWI AS MUWID, WellName AS WELL_NAME, RecorDate AS PRODUCTION_DATE, EnersightGrossBOE AS FORECAST_BOED "
					+ "FROM [IOP_Read].[dbo].[EnersightProveDaily] " + "WHERE [RecorDate] = ?";

			try {
				stmt = connection.prepareStatement(query);

				stmt.setString(1, firstDateOfCurrentMonth);
				resultSet = stmt.executeQuery();

				if (resultSet != null) {
					enersightProveDailyDtoList = new ArrayList<>();
					while (resultSet.next()) {

						enersightProveDailyDto = new EnersightProveDailyDto();
						enersightProveDailyDto.setMuwiId(resultSet.getString("MUWID"));
						enersightProveDailyDto.setWell(resultSet.getString("WELL_NAME"));
						enersightProveDailyDto.setLastProdDate(ServicesUtil.convertFromZoneToZoneString(null,
								resultSet.getString("PRODUCTION_DATE"), "", "", "yyyy-MM-dd", "dd-MMM-yy"));
						enersightProveDailyDto.setLastProdDateField(ServicesUtil.convertFromZoneToZone(null,
								resultSet.getString("PRODUCTION_DATE"), "", "", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"));
						enersightProveDailyDto.setForecastBoed(resultSet.getDouble("FORECAST_BOED"));

						enersightProveDailyDtoList.add(enersightProveDailyDto);
					}
					logger.error("[fetchProveDailyData] : INFO - enersightProveMonthlyDtoList Size"
							+ enersightProveDailyDtoList.size());
				}
			} catch (Exception e) {

				logger.error("[fetchProveDailyData] : ERROR- Exception while fetching data from database " + e);
				throw e;

			} finally {
				try {
					stmt.close();
					resultSet.close();
				} catch (SQLException e) {
					logger.error("[fetchProveDailyData] : ERROR- Exception while cleaning environment" + e);
				}
			}

		} catch (Exception e) {
			logger.error("[fetchProveDailyData] : ERROR- Date Format Exception " + e);
			throw e;
		}
		return enersightProveDailyDtoList;
	}

	/**
	 * This method fetches the list of PW Hopper Wells which have production
	 * less than threshold for the stipulated duration
	 */
	public List<String> getMuwiWherVarLessThanThres(Connection connection, List<String> muwiList, double duration,
			double thresholdPercent, String version, String country) {
		List<String> reponseMuwiList = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String muwiString = ServicesUtil.getStringFromList(muwiList);
		String query = null;

		if (country.equalsIgnoreCase("EFS")) {
			query = "select uwi as MUWI  FROM [IOP_Read].[dbo].[EnersightProveDaily] where uwi in ( " + muwiString
					+ " ) and " + " RecorDate>= Dateadd(\"DD\", -" + (duration + 2)
					+ ", SYSDATETIME()) and RecorDate<= Dateadd(\"DD\",-2, SYSDATETIME()) "
					+ "  and (((MerrickGrossBOE - EnersightGrossBOE) / EnersightGrossBOE ) *100) <= -"
					+ thresholdPercent + " and  EnersightGrossBOE != 0 " + " group by uwi having count(*) = " + duration
					+ "";
		} else if (country.equalsIgnoreCase("CA")) {
			query = "select muwi as MUWI FROM [IOP_Read].[dbo].[PV_CAN_Prove_Daily] where muwi in ( " + muwiString
					+ " ) and RecordDate >= Dateadd(\"DD\", -" + (duration + 2) + ", SYSDATETIME()) and"
					+ " RecordDate<= Dateadd(\"DD\",-2, SYSDATETIME()) and"
					+ " (((((PV_GROSSOILPROD_BBL + PV_GROSS_GAS_MCF)/6) - TARGET_BOE) / TARGET_BOE) * 100) <= -"
					+ thresholdPercent + " and TARGET_BOE != 0 group by muwi having count(*) = " + duration + "";
		}

		try {
			System.err.println("[getMuwiWherVarLessThanThres][query]" + query);
			stmt = connection.prepareStatement(query);
			resultSet = stmt.executeQuery();

			if (!ServicesUtil.isEmpty(resultSet)) {
				reponseMuwiList = new ArrayList<String>();

				while (resultSet.next()) {
					reponseMuwiList.add(resultSet.getString("MUWI"));
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][EnersightProveDailyDao][getMuwiWherVarLessThanThres][error]" + e.getMessage()
					+ "query" + query);
		}

		return reponseMuwiList;
	}

	/**
	 * This is used by Frac module to fetch the Actual production 2 days ago.
	 */
	public Map<String, String> fetchFracBOED(Connection connection, Set<String> uwiIdList) throws Exception {

		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		String uwiIdListQuery = ServicesUtil.getQueryString(uwiIdList, "UWI", new ArrayList<String>());
		logger.error("[fetchFracBOED] : INFO - uwiIdListQuery" + uwiIdListQuery);
		Map<String, String> fracBoedMap = new HashMap<>();

		try {

			String query = "SELECT UWI, round(MerrickGrossBOE,2) AS ACTUAL_BOED"
					+ " from [IOP_Read].[dbo].[EnersightProveDaily] WHERE (" + uwiIdListQuery + ") "
					+ " AND RecorDate = cast(Dateadd(DD,-2, SYSDATETIME()) as date) ";

			logger.error("[fetchFracBOED] : INFO - query" + query);
			try {
				stmt = connection.prepareStatement(query);
				resultSet = stmt.executeQuery();

				if (resultSet != null) {
					while (resultSet.next()) {
						String muwi = resultSet.getString("UWI");
						String actualBOED = resultSet.getString("ACTUAL_BOED");
						logger.error("[fetchFracBOED] : INFO - uwi" + muwi);
						logger.error("[fetchFracBOED] : INFO - ACTUAL_BOED" + actualBOED);
						fracBoedMap.put(muwi, actualBOED);
					}
				}
			} catch (Exception e) {
				logger.error("[fetchProveData] : ERROR- Exception while fetching data from database " + e);
				throw e;
			} finally {
				try {
					stmt.close();
					resultSet.close();
				} catch (SQLException e) {
					logger.error("[fetchProveData] : ERROR- Exception while cleaning environment" + e);
				}
			}
		} catch (Exception e) {
			logger.error("[fetchProveData] : ERROR- Date Format Exception " + e);
			throw e;
		}
		return fracBoedMap;

	}

	/**
	 * This method is used in Pigging module to assist in Calculation of pig
	 * retrieval time for a Specific Equipment 2 days ago
	 */
	public Map<String, String> fetchPiggingValue(Connection connection, Set<String> uwiIdList) throws Exception {

		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		String uwiIdListQuery = ServicesUtil.getQueryString(uwiIdList, "UWI", new ArrayList<String>());
		logger.error("[fetchPiggingValue] : INFO - uwiIdListQuery" + uwiIdListQuery);
		Map<String, String> piggingValueMap = new HashMap<>();

		try {

			String query = "SELECT sum(round(MerrickGrossWaterProd,2)) as WATER_PRODUCTION, sum(round(EnersightOil,2)) AS OIL, sum(round(EnersightGas,2)) AS GAS"
					+ " from [IOP_Read].[dbo].[EnersightProveDaily] WHERE (" + uwiIdListQuery + ") "
					+ " AND RecorDate = cast(Dateadd(DD,-2, SYSDATETIME()) as date) ";

			logger.error("[fetchPiggingValue] : INFO - query" + query);
			try {
				stmt = connection.prepareStatement(query);
				resultSet = stmt.executeQuery();

				if (resultSet != null) {
					while (resultSet.next()) {
						String totalWater = resultSet.getString("WATER_PRODUCTION");
						String totalOil = resultSet.getString("OIL");
						String totalGas = resultSet.getString("GAS");
						logger.error("[fetchPiggingValue] : INFO - water" + totalWater);
						logger.error("[fetchPiggingValue] : INFO - gas" + totalGas);
						logger.error("[fetchPiggingValue] : INFO - oil" + totalOil);
						piggingValueMap.put("WaterProduction", totalWater);
						piggingValueMap.put("Oil", totalOil);
						piggingValueMap.put("Gas", totalGas);
					}
				}
			} catch (Exception e) {
				logger.error("[fetchPiggingValue] : ERROR- Exception while fetching data from database " + e);
				throw e;
			} finally {
				try {
					stmt.close();
					resultSet.close();
				} catch (SQLException e) {
					logger.error("[fetchPiggingValue] : ERROR- Exception while cleaning environment" + e);
				}
			}
		} catch (Exception e) {
			logger.error("[fetchPiggingValue] : ERROR- Date Format Exception " + e);
			throw e;
		}
		return piggingValueMap;
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

	public Date calculatefirstDateOfCurrentMonth() {
		Calendar aCalendar = Calendar.getInstance();
		// for current month
		aCalendar.add(Calendar.MONTH, 0);
		// set DATE to 1, so first date of current month
		aCalendar.set(Calendar.DATE, 1);

		Date firstDateOfPreviousMonth = aCalendar.getTime();
		return firstDateOfPreviousMonth;
	}

	@SuppressWarnings("unchecked")
	public JSONObject fetchForecastValueFromEnersight(String muwi, String reportId, Map<String, String> dateValueMap,
			String location) {
		JSONObject forecastJSONObject = new JSONObject();
		long[] epochSecond = null;
		double[] tagValue = null;
		String query = "";
		try {
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			Connection connection = DBConnections.createConnectionForProve();

			if (ProdViewConstant.EFS_BASE_LOC_CODE.equalsIgnoreCase(location)) {
				if (ProdViewConstant.DOP_REPORT_ID.equalsIgnoreCase(reportId)) {
					query = "select AVG(EnersightOil) AS FORECAST_BOED from [IOP_Read].[dbo].[EnersightProveDaily] where"
							+ " RecorDate = DATEADD(DAY, 0, CAST( GETDATE() AS Date )) " + "and uwi = '" + muwi + "'";
				} else {
					query = "select AVG(EnersightGas) AS FORECAST_BOED from [IOP_Read].[dbo].[EnersightProveDaily] where "
							+ "RecorDate = DATEADD(DAY, 0, CAST( GETDATE() AS Date )) " + "and uwi = '" + muwi + "'";
				}
			} else {
				if (ProdViewConstant.DOP_REPORT_ID.equalsIgnoreCase(reportId)) {
					query = "select AVG(PV_TARGET_OIL_BBL) AS FORECAST_BOED from [IOP_Read].[dbo].[PV_CAN_Prove_Daily] where "
							+ "RecordDate = DATEADD(DAY, 0, CAST( GETDATE() AS Date ))" + "and MUWI = '" + muwi + "'";
				} else {
					query = "select AVG(PV_TARGET_GAS_MCF) AS FORECAST_BOED from [IOP_Read].[dbo].[PV_CAN_Prove_Daily] where "
							+ "RecordDate = DATEADD(DAY, 0, CAST( GETDATE() AS Date ))" + "and MUWI = '" + muwi + "'";
				}
			}

			stmt = connection.prepareStatement(query);
			resultSet = stmt.executeQuery();

			tagValue = new double[2];
			epochSecond = new long[2];

			// Setting Default values
			tagValue[0] = 0;
			epochSecond[0] = ZonedDateTime.parse(dateValueMap.get("previousDate")).toEpochSecond();

			// Forecast value from Enersight
			if (resultSet != null) {
				while (resultSet.next()) {
					forecastJSONObject = new JSONObject();
					tagValue[1] = resultSet.getDouble("FORECAST_BOED");
					epochSecond[1] = ZonedDateTime.parse(dateValueMap.get("currentDate")).toEpochSecond();

				}
			}
			forecastJSONObject.put("y", new org.json.JSONArray(tagValue));
			forecastJSONObject.put("epochSecond", new org.json.JSONArray(epochSecond));
			forecastJSONObject.put("message", "SUCCESS");
		} catch (Exception e) {
			forecastJSONObject.put("message", "Exception encountered while fetching forecast Value");
			logger.error("[fetchPiggingValue] : ERROR- Date Format Exception " + e.getMessage());
			e.printStackTrace();
		}

		return forecastJSONObject;
	}

	@SuppressWarnings("resource")
	public HashMap<String, Double> fetchDOPForecastValueFromEnersight(Connection connection, List<String> muwiIdList) {
		String query = "";
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		HashMap<String, Double> fracBoedMap = new HashMap<>();
		DecimalFormat df = new DecimalFormat("#.##");
		try {
			Set<String> muwiIdSet = new HashSet<String>();
			for (String x : muwiIdList)
				muwiIdSet.add(x);
			String uwiIdListQuery = ServicesUtil.getQueryString(muwiIdSet, "UWI", new ArrayList<String>());

			// EFS DOP -EnersightProveDaily : col - EnersightOil
			query = "select UWI, EnersightOil AS FORECAST_BOED  from [IOP_Read].[dbo].[EnersightProveDaily] where"
					+ " RecorDate = DATEADD(DAY, 0, CAST( GETDATE() AS Date ))  and  (" + uwiIdListQuery + ") ";
			logger.error("[fetchDOPForecastValueFromEnersight][Query] " + query);

			// EFS DGP- EnersightProveDaily : col- EnersightGas -?
			stmt = connection.prepareStatement(query);
			resultSet = stmt.executeQuery();

			stmt = connection.prepareStatement(query);
			resultSet = stmt.executeQuery();
			logger.error("ResultSet [fetchDOPForecastValueFromEnersight] :" + resultSet);
			if (resultSet != null) {
				while (resultSet.next()) {
					String muwi = resultSet.getString("UWI");
					Double forecastBOED = Double.valueOf(df.format(resultSet.getDouble("FORECAST_BOED")));
					fracBoedMap.put(muwi, forecastBOED);
				}
			}
		} catch (Exception e) {
			logger.error("[fetchDOPForecastValueFromEnersight] : ERROR- Exception while fetching data from database "
					+ e.getMessage());
		} finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[fetchDOPForecastValueFromEnersight] : ERROR- Exception while cleaning environment" + e);
			}
		}
		logger.error("[fracBoedMap][fetchDOPForecastValueFromEnersight] : " + fracBoedMap);
		return fracBoedMap;
	}

	@SuppressWarnings("resource")
	public HashMap<String, Double> fetchEFSGasForecastValueFromEnersight(Connection connection,
			List<String> muwiIdList) {
		String query = "";
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		HashMap<String, Double> fracBoedMap = new HashMap<>();
		DecimalFormat df = new DecimalFormat("#.##");
		try {
			Set<String> muwiIdSet = new HashSet<String>();
			for (String x : muwiIdList)
				muwiIdSet.add(x);
			String uwiIdListQuery = ServicesUtil.getQueryString(muwiIdSet, "UWI", new ArrayList<String>());

			query = "select UWI, EnersightGas AS FORECAST_BOED  from [IOP_Read].[dbo].[EnersightProveDaily] where"
					+ " RecorDate = DATEADD(DAY, 0, CAST( GETDATE() AS Date ))  and  (" + uwiIdListQuery + ") ";
			logger.error("[fetchEFSGasForecastValueFromEnersight][Query] " + query);

			stmt = connection.prepareStatement(query);
			resultSet = stmt.executeQuery();

			stmt = connection.prepareStatement(query);
			resultSet = stmt.executeQuery();
			logger.error("ResultSet [fetchEFSDGPForecastValueFromEnersight] :" + resultSet);
			if (resultSet != null) {
				while (resultSet.next()) {
					String muwi = resultSet.getString("UWI");
					Double forecastBOED = Double.valueOf(df.format(resultSet.getDouble("FORECAST_BOED")));
					fracBoedMap.put(muwi, forecastBOED);
				}
			}
		} catch (Exception e) {
			logger.error("[fetchEFSGasForecastValueFromEnersight] : ERROR- Exception while fetching data from database "
					+ e.getMessage());
		} finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error(
						"[fetchEFSGasForecastValueFromEnersight] : ERROR- Exception while cleaning environment" + e);
			}
		}
		logger.error("[fracBoedMap][fetchEFSGasForecastValueFromEnersight] : " + fracBoedMap);
		return fracBoedMap;
	}

	public HashMap<String, Double> fetchCanadaOilForecastFromEnerSight(Connection connection, List<String> muwiIdList)
			throws Exception {

		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		Set<String> muwiIdSet = new HashSet<String>();
		for (String x : muwiIdList)
			muwiIdSet.add(x);
		String uwiIdListQuery = ServicesUtil.getQueryString(muwiIdSet, "MUWI", new ArrayList<String>());
		logger.error("[fetchDGPForecastFromEnerSight] : INFO - uwiIdListQuery" + uwiIdListQuery);
		HashMap<String, Double> fracBoedMap = new HashMap<>();
		DecimalFormat df = new DecimalFormat("#.##");
		try {
			// Canada Gas : TARGET_BOE
			String query = "select MUWI, PV_TARGET_OIL_BBL AS FORECAST_BOED from [IOP_Read].[dbo].[PV_CAN_Prove_Daily] "
					+ "where RecordDate = DATEADD(DAY, 0, CAST( GETDATE() AS Date )) and  (" + uwiIdListQuery + ") ";

			logger.error("[fetchCanadaOilForecastFromEnerSight] : INFO - query" + query);
			try {
				stmt = connection.prepareStatement(query);
				resultSet = stmt.executeQuery();
				logger.error("ResultSet [fetchCanadaOilForecastFromEnerSight] :" + resultSet);
				if (resultSet != null) {
					while (resultSet.next()) {
						String muwi = resultSet.getString("MUWI");
						Double forecastBOED = Double.valueOf(df.format(resultSet.getDouble("FORECAST_BOED")));
						fracBoedMap.put(muwi, forecastBOED);
					}
				}
			} catch (Exception e) {
				logger.error(
						"[fetchCanadaOilForecastFromEnerSight] : ERROR- Exception while fetching data from database "
								+ e);
				throw e;
			} finally {
				try {
					stmt.close();
					resultSet.close();
				} catch (SQLException e) {
					logger.error(
							"[fetchCanadaOilForecastFromEnerSight] : ERROR- Exception while cleaning environment" + e);
				}
			}
		} catch (Exception e) {
			logger.error("[fetchCanadaOilForecastFromEnerSight] : ERROR- Date Format Exception " + e);
			throw e;
		}
		logger.error("[fracBoedMap][fetchCanadaOilForecastFromEnerSight] : " + fracBoedMap);
		return fracBoedMap;

	}

	public HashMap<String, Double> fetchCanadaGasForecastFromEnerSight(Connection connection, List<String> muwiIdList)
			throws Exception {

		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		Set<String> muwiIdSet = new HashSet<String>();
		for (String x : muwiIdList)
			muwiIdSet.add(x);
		String uwiIdListQuery = ServicesUtil.getQueryString(muwiIdSet, "MUWI", new ArrayList<String>());
		logger.error("[fetchCanadaGasForecastFromEnerSight] : INFO - uwiIdListQuery" + uwiIdListQuery);
		HashMap<String, Double> fracBoedMap = new HashMap<>();
		DecimalFormat df = new DecimalFormat("#.##");
		try {
			// Canada Gas
			String query = "select MUWI, PV_TARGET_GAS_MCF AS FORECAST_BOED from [IOP_Read].[dbo].[PV_CAN_Prove_Daily] "
					+ "where RecordDate = DATEADD(DAY, 0, CAST( GETDATE() AS Date )) and  (" + uwiIdListQuery + ") ";

			logger.error("[fetchCanadaDGPForecastFromEnerSight] : INFO - query" + query);
			try {
				stmt = connection.prepareStatement(query);
				resultSet = stmt.executeQuery();
				logger.error("ResultSet [fetchDGPForecastFromEnerSight] :" + resultSet);
				if (resultSet != null) {
					while (resultSet.next()) {
						String muwi = resultSet.getString("MUWI");
						Double forecastBOED = Double.valueOf(df.format(resultSet.getDouble("FORECAST_BOED")));
						fracBoedMap.put(muwi, forecastBOED);
					}
				}
			} catch (Exception e) {
				logger.error(
						"[fetchCanadaGasForecastFromEnerSight] : ERROR- Exception while fetching data from database "
								+ e);
				throw e;
			} finally {
				try {
					stmt.close();
					resultSet.close();
				} catch (SQLException e) {
					logger.error(
							"[fetchCanadaGasForecastFromEnerSight] : ERROR- Exception while cleaning environment" + e);
				}
			}
		} catch (Exception e) {
			logger.error("[fetchCanadaGasForecastFromEnerSight] : ERROR- Date Format Exception " + e);
			throw e;
		}
		logger.error("[fracBoedMap][fetchCanadaGasForecastFromEnerSight] : " + fracBoedMap);
		return fracBoedMap;

	}
	
	//added for pw hooper canada
	public Map<String,String> getMuwiWherVarLessThanThresForCanada(Connection connection, List<String> muwiList, double duration,
			double thresholdPercent, String version, String country) {
		Map<String,String> reponseMuwiWellNameMap = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String muwiString = ServicesUtil.getStringFromList(muwiList);
		String query = null;
        if (country.equalsIgnoreCase("CA")) {
			query = "select muwi as MUWI,completion_name as WELLNAME FROM [IOP_Read].[dbo].[PV_CAN_Prove_Daily] where muwi in ( " + muwiString
					+ " ) and RecordDate >= Dateadd(\"DD\", -" + (duration + 2) + ", SYSDATETIME()) and"
					+ " RecordDate<= Dateadd(\"DD\",-2, SYSDATETIME()) and"
					+ " (((((PV_GROSSOILPROD_BBL + PV_GROSS_GAS_MCF)/6) - TARGET_BOE) / TARGET_BOE) * 100) <= -"
					+ thresholdPercent + " and TARGET_BOE != 0 group by muwi,completion_name having count(*) = " + duration + "";
		}

		try {
			System.err.println("[getMuwiWherVarLessThanThresForCanada][query]" + query);
			stmt = connection.prepareStatement(query);
			resultSet = stmt.executeQuery();

			if (!ServicesUtil.isEmpty(resultSet)) {
				reponseMuwiWellNameMap = new HashMap<String,String>();
             logger.error("values for canada  : " +resultSet);
				while (resultSet.next()) {
					reponseMuwiWellNameMap.put(resultSet.getString("MUWI"),resultSet.getString("WELLNAME"));
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][EnersightProveDailyDao][getMuwiWherVarLessThanThresForCanada][error]" + e.getMessage()
					+ "query" + query);
		}

		return reponseMuwiWellNameMap;
	}
	
	
	
	//added for dop query performance issue
	public List<EnersightProveDailyDto> fetchProveDailyData(String startTimeInString,String tag) throws Exception {
		logger.debug("EnersightProveDailyDao.fetchProveDailyData(" + startTimeInString + ")");
 
		logger.error("begin connection establishment for enersight while staging dop/dgp data");
		Connection connection = DBConnections.createConnectionForProve();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String query = null;
		List<EnersightProveDailyDto> returnList = new ArrayList<EnersightProveDailyDto>();
		String muwiIdValue="";
		
		if (connection != null) {
			logger.error("connection established for enersight while staging dop/dgp data" +connection);
			try {
				
				if (!ServicesUtil.isEmpty(startTimeInString)) {

				    Date date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTimeInString); 
				    
				    if(!ServicesUtil.isEmpty(date))
				    {
				    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					dateFormat.format(date);
				    
				    startTimeInString = dateFormat.format(date);
				    
				    }
				    logger.error("final startTimeInString for forecast fetch " +startTimeInString);
				}
				if(tag.equalsIgnoreCase("QTOILD"))
				{
				query= "SELECT UWI AS MUWI, AVG(EnersightOil) AS FORECAST FROM [IOP_Read].[dbo].[EnersightProveDaily] WHERE RecorDate = '"
						+ startTimeInString + "' GROUP BY UWI";
				
					
				}
				if(tag.equalsIgnoreCase("QTGASD"))
				{
					query = "select UWI AS MUWI, AVG(EnersightGas) AS FORECAST from [IOP_Read].[dbo].[EnersightProveDaily] WHERE RecorDate = '"
						+ startTimeInString + "' GROUP BY UWI";
					
				}
				if(tag.equalsIgnoreCase("SEP_CNDTOTTDY"))
				{
					query = "select MUWI AS MUWI, AVG(PV_TARGET_OIL_BBL) AS FORECAST from [IOP_Read].[dbo].[PV_CAN_Prove_Daily] WHERE RecordDate = '"
						+ startTimeInString + "' GROUP BY MUWI";
					
				}
					
				if(tag.equalsIgnoreCase("SEP_PRDTOTTDY")
						||tag.equalsIgnoreCase("AFLOW")
						||tag.equalsIgnoreCase("PG_FLOWD"))
				{
					query = "select MUWI AS MUWI, AVG(PV_TARGET_GAS_MCF) AS FORECAST from [IOP_Read].[dbo].[PV_CAN_Prove_Daily] WHERE RecordDate = '"
						+ startTimeInString + "' GROUP BY MUWI";
					
				}
				logger.error("query to fetch forecast data from enersight " +query);
				
				stmt = connection.prepareStatement(query);
				logger.error("successfully prepared query stmt" +stmt);

				// STEP1: Fetch records
				resultSet = stmt.executeQuery();
				logger.error("successfully executed the query");

				// STEP 2: Export to resultList to returnList
				if (!ServicesUtil.isEmpty(resultSet)) {
					logger.error("resultset available and not empty : " +resultSet.toString());

					while (resultSet.next()) {
						EnersightProveDailyDto dailyDto = new EnersightProveDailyDto();
						logger.error("muwi in resultset: " +resultSet.getString("MUWI"));
						dailyDto.setMuwiId(resultSet.getString("MUWI"));
						dailyDto.setForecastBoed(resultSet.getDouble("FORECAST"));
						returnList.add(dailyDto);
					}

				}
				

			} catch (Exception e) {
				logger.error("EnersightProveDailyDao.fetchProveDailyData()[error]" + e.getMessage());
				throw e;
			} finally {
				try {
					stmt.close();
					resultSet.close();
					connection.close();
					com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
					
				} catch (SQLException e) {
					logger.error("[fetchProveDailyData] : ERROR- Exception while cleaning environment" + e);
				}
			}

		} else {
			logger.error(
					"[Murphy][EnersightProveDaily][fetchProveDailyData][error] Connection to Database is not possible");
		}
		return returnList;
	}
	
	
}
