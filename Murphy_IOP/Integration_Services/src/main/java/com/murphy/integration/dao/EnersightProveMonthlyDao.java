package com.murphy.integration.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dto.EnersightProveDailyDto;
import com.murphy.integration.dto.EnersightProveMonthlyDto;
import com.murphy.integration.util.ApplicationConstant;
import com.murphy.integration.util.DBConnections;
import com.murphy.integration.util.ServicesUtil;

public class EnersightProveMonthlyDao {

	private static final Logger logger = LoggerFactory.getLogger(EnersightProveMonthlyDao.class);

	public EnersightProveMonthlyDao() {
		super();
	}

	public List<EnersightProveMonthlyDto> fetchProveData(Connection connection, Map<String, String> uwiIdMap,
			Integer duration, List<String> InvestigationNIPlocationCodeList, List<String> inquiryNIPlocationCodeList)
			throws Exception {
		List<EnersightProveMonthlyDto> enersightProveMonthlyDtoList = null;
		List<String> versionList = new ArrayList<>();
		EnersightProveMonthlyDto enersightProveMonthlyDto = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;

		String uwiIdListQuery = ServicesUtil.getQueryString(uwiIdMap.keySet(), "UWI", new ArrayList<String>());
		// logger.error("[fetchProveData] : INFO - uwiIdListQuery" +
		// uwiIdListQuery);

		String firstDateOfLastMonth = null;
		String lastDateOfLastMonth = null;

		SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dayBaseQuery = null;
		// String versionName = null;
		// String versionNameCurrentMonth = null;
		// String versionNameLastMonth = null;

		try {
			if (duration == 7) {
				dayBaseQuery = "[ProductionDate]>= Dateadd(\"DD\",?, SYSDATETIME()) and [ProductionDate]<= Dateadd(\"DD\",-2, SYSDATETIME())";
			} else if (duration == 30) {
				firstDateOfLastMonth = SimpleDateFormat.format(calculatefirstDateOfLastMonth());
				lastDateOfLastMonth = SimpleDateFormat.format(calculateLastDateOfLastMonth());
				// logger.error("[fetchProveData] : INFO - firstDateOfLastMonth"
				// + firstDateOfLastMonth
				// + " lastDateOfLastMonth " + lastDateOfLastMonth);
				dayBaseQuery = "[ProductionDate]>= ? and [ProductionDate]<= ?";
			}
			// versionNameCurrentMonth = fetchVersionNameofCurrentMonth();
			// versionNameLastMonth = fetchVersionNameofLastMonth();

			// logger.error("fetchProveDailyData- versionNameCurrentMonth " +
			// versionNameCurrentMonth);
			// logger.error("fetchProveDailyData- versionNameLastMonth " +
			// versionNameLastMonth);

			// versionName = fetchVersionName();
			// logger.error("versionName = " + versionName);

			/*
			 * String query =
			 * "SELECT UWI,round(sum(X.ACTUAL_BOED),2) AS ACTUAL_BOED, round(sum(X.FORECAST_BOED),2) AS FORECAST_BOED, round(sum(X.ACTUAL_BOED-X.FORECAST_BOED),2) AS VAR_TO_FORECAST_BOED, "
			 * +
			 * " case when sum(X.FORECAST_BOED) = 0 then null else round(100*(sum(X.ACTUAL_BOED)-sum(X.FORECAST_BOED))/sum(X.FORECAST_BOED),2) end AS PER_VAR_TO_FORECAST_BOED, "
			 * +
			 * " round(sum(X.OIL),2) AS OIL, round(sum(X.WATER),2) AS WATER, round(sum(X.GAS),2) AS GAS, X.WELL_NAME, X.PRODUCTION_DATE FROM ( "
			 * +
			 * " select UWI,AVG(M_GBOE) AS ACTUAL_BOED, AVG(E_GBOE) AS FORECAST_BOED, "
			 * +
			 * " AVG(M_OIL) AS OIL, AVG(WaterProduction) AS WATER, AVG(M_GAS) AS GAS, WellName AS WELL_NAME, CAST(MAX(ProductionDate) AS DATE) AS PRODUCTION_DATE "
			 * + " from [IOP_Read].[dbo].[EnersightProveMonthly] WHERE (" +
			 * uwiIdListQuery + ") AND " + dayBaseQuery +
			 * " group by UWI, WellName, VersionName ) X group by X.UWI, X.WELL_NAME, X.PRODUCTION_DATE ORDER BY VAR_TO_FORECAST_BOED DESC"
			 * ;
			 */

			String versionQuery = "select distinct versionName from [IOP_Read].[dbo].[EnersightProveMonthly] ";
			try {
				stmt1 = connection.prepareStatement(versionQuery);

				resultSet1 = stmt1.executeQuery();
				if (resultSet1 != null) {
					while (resultSet1.next()) {
						versionList.add(resultSet1.getString("versionName"));
					}
				}
			} catch (Exception e) {

				logger.error("[fetchProveDailyData] : ERROR- Exception while fetching data from database " + e);
				throw e;

			} finally {
				try {
					stmt1.close();
					resultSet1.close();
				} catch (SQLException e) {
					logger.error("[fetchProveDailyData] : ERROR- Exception while cleaning environment" + e);
				}
			}
			logger.error("versionList- " + versionList);

			String query = "SELECT UWI, round(X.ACTUAL_BOED,2) AS ACTUAL_BOED, round(X.FORECAST_BOED,2) AS FORECAST_BOED, round(X.ACTUAL_BOED-X.FORECAST_BOED,2) AS VAR_TO_FORECAST_BOED, "
					+ " case when X.FORECAST_BOED = 0 then null else round(100*(X.ACTUAL_BOED-X.FORECAST_BOED)/X.FORECAST_BOED,2) end AS PER_VAR_TO_FORECAST_BOED, "
					+ " round(X.OIL,2) AS OIL, round(X.WATER,2) AS WATER, round(X.GAS,2) AS GAS, X.WELL_NAME, X.PRODUCTION_DATE FROM ( "
					+ " select UWI,AVG(M_GBOE) AS ACTUAL_BOED, AVG(E_GBOE) AS FORECAST_BOED, "
					+ " AVG(M_OIL) AS OIL, AVG(WaterProduction) AS WATER, AVG(M_GAS) AS GAS, WellName AS WELL_NAME, CAST(MAX(ProductionDate) AS DATE) AS PRODUCTION_DATE "
					+ " from [IOP_Read].[dbo].[EnersightProveMonthly] WHERE (" + uwiIdListQuery + ") AND "
					+ dayBaseQuery + " AND VersionName = ? "
					+ " group by UWI, WellName) X ORDER BY VAR_TO_FORECAST_BOED DESC";

			try {
				stmt = connection.prepareStatement(query);

				if (duration == 7) {
					stmt.setInt(1, -(duration + 2));
					/*
					 * if(versionList.contains(versionNameLastMonth)){
					 * stmt.setString(2, versionNameLastMonth); }else{
					 * stmt.setString(2, versionNameCurrentMonth); }
					 */
					// if(versionList.contains(versionNameCurrentMonth)){
					// stmt.setString(2, versionNameCurrentMonth);
					// }else if(versionList.contains(versionNameLastMonth)){
					// stmt.setString(2, versionNameLastMonth);
					// }else{ //Added else part to fetch DOP version from HANA
					// DB
					// stmt.setString(2,
					// getVersionNameOfMonthFromconfig("Enersight_Version"));
					// }
					stmt.setString(2, getVersionNameOfMonthFromconfig("Enersight_Version"));
				} else if (duration == 30) {
					stmt.setString(1, firstDateOfLastMonth);
					stmt.setString(2, lastDateOfLastMonth);
					// if(versionList.contains(versionNameLastMonth)){
					// stmt.setString(3, versionNameLastMonth);
					// }else{
					// stmt.setString(3, versionNameCurrentMonth);
					// }

					// if(versionList.contains(versionNameCurrentMonth)){
					// stmt.setString(3, versionNameCurrentMonth);
					// }else if(versionList.contains(versionNameLastMonth)){
					// stmt.setString(3, versionNameLastMonth);
					// }else{ //Added else part to fetch DOP version from HANA
					// DB
					// stmt.setString(3,
					// getVersionNameOfMonthFromconfig("Enersight_Version"));
					// }
					// stmt.setString(3, versionName);
					stmt.setString(3, getVersionNameOfMonthFromconfig("Enersight_Version"));
				}

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
					// logger.error("[fetchProveData] : INFO -
					// enersightProveMonthlyDtoList Size"
					// + enersightProveMonthlyDtoList.size());
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

	public List<String> fetchLocationCodeInvestigation(Connection connection, Set<String> locationCodeSet)
			throws Exception {
		List<String> locationCodeList = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		String locationCodeListQuery = ServicesUtil.getQueryString(locationCodeSet, "pe.LOC_CODE",
				new ArrayList<String>());
		// logger.error("[fetchLocationCodeInvestigation] : INFO -
		// uwiIdListQuery" + locationCodeListQuery);

		String query = "SELECT pe.LOC_CODE AS locationCode from TM_TASK_EVNTS TM  , TM_PROC_EVNTS PE Where  pe.process_id = tm.process_id and ("
				+ locationCodeListQuery + ") AND TM.ORIGIN =? AND TM.STATUS<> ? ";

		// logger.error("[fetchLocationCodeInvestigation] : INFO - Query -" +
		// query);

		try {
			stmt = connection.prepareStatement(query);

			stmt.setString(1, ApplicationConstant.TM_ORIGIN);
			stmt.setString(2, ApplicationConstant.TM_STATUS);
			resultSet = stmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					locationCodeList.add(resultSet.getString("locationCode"));
				}
				// logger.error(
				// "[fetchLocationCodeInvestigation] : INFO - locationCodeList
				// Size" + locationCodeList.size());
			}
		} catch (Exception e) {

			logger.error("[fetchLocationCodeInvestigation] : ERROR- Exception while fetching data from database " + e);
			throw e;

		} finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[fetchLocationCodeInvestigation] : ERROR- Exception while cleaning environment" + e);
			}
		}
		// logger.error("[fetchLocationCodeInvestigation] : INFO -
		// locationCodeList " + locationCodeList);
		return locationCodeList;
	}

	public Map<String, String> getMuwiByLocationTypeAndCode(Connection connection, String locationType,
			List<String> locationCodeList) throws Exception {
		Map<String, String> uwidMap = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String locationCodeString = null;
		try {
			for (int i = 0; i < locationCodeList.size(); i++) {
				if (i == 0) {
					locationCodeString = "'" + locationCodeList.get(i) + "'";
				} else
					locationCodeString = locationCodeString + ", '" + locationCodeList.get(i) + "'";
			}
			StringBuilder stringBuilder = new StringBuilder(
					"select wm.muwi as well, wm.location_code as locationCode from production_location p1 ");

			if (locationType.equalsIgnoreCase(ApplicationConstant.WELL)) {
				stringBuilder.append(" join well_muwi wm on wm.location_code=p1.location_code where p1.location_code ");
			} else if (locationType.equalsIgnoreCase(ApplicationConstant.WELLPAD)) {
				stringBuilder
						.append(" join production_location p2 on p1.parent_code=p2.location_code join well_muwi wm  on wm.location_code=p1.location_code where "
								+ " p2.location_code ");
			} else if (locationType.equalsIgnoreCase(ApplicationConstant.FACILITY)) {
				stringBuilder
						.append(" join production_location p2 on p1.parent_code=p2.location_code join production_location p3 on p2.parent_code=p3.location_code "
								+ " join well_muwi wm on wm.location_code=p1.location_code where p3.location_code ");
			} else if (locationType.equalsIgnoreCase(ApplicationConstant.FIELD)) {
				stringBuilder
						.append(" join production_location p2 on p1.parent_code=p2.location_code join production_location p3 on "
								+ " p2.parent_code=p3.location_code join production_location p4 on p3.parent_code=p4.location_code join well_muwi wm "
								+ " on wm.location_code=p1.location_code where p4.location_code ");
			}

			stringBuilder.append(" in (" + locationCodeString + ")");

			// logger.error("[getMuwiByLocationTypeAndCode] : INFO - Query" +
			// stringBuilder.toString());
			stmt = connection.prepareStatement(stringBuilder.toString());
			resultSet = stmt.executeQuery();
			boolean hasNext = resultSet.next();
			if (resultSet != null && hasNext) {
				uwidMap = new HashMap<>();
				while (hasNext) {
					uwidMap.put(resultSet.getString("well"), resultSet.getString("locationCode"));
					hasNext = resultSet.next();
				}
				// logger.error("[getMuwiByLocationTypeAndCode] : INFO - uwidMap
				// length " + uwidMap.size());
			}

		} catch (Exception e) {

			logger.error("[getMuwiByLocationTypeAndCode] : ERROR- Exception while fetching data from database " + e);
			throw e;

		} finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[getMuwiByLocationTypeAndCode] : ERROR- Exception while cleaning environment" + e);
			}
		}

		// logger.error("[getMuwiByLocationTypeAndCode] : INFO - uwidMap " +
		// uwidMap);
		return uwidMap;

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

	public List<EnersightProveDailyDto> fetchProveDailyData(Connection connection) throws Exception {

		List<EnersightProveDailyDto> enersightProveDailyDtoList = null;
		// List<String> versionList = new ArrayList<>();
		EnersightProveDailyDto enersightProveDailyDto = null;
		PreparedStatement stmt = null;
		// PreparedStatement stmt1 = null;
		ResultSet resultSet = null;
		// ResultSet resultSet1 = null;

		String firstDateOfCurrentMonth = null;
		// String firstDateOfLastMonth = null;
		// String versionNameCurrentMonth = null;
		// String versionNameLastMonth = null;

		SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {

			firstDateOfCurrentMonth = SimpleDateFormat.format(calculatefirstDateOfCurrentMonth());
			// firstDateOfLastMonth =
			// SimpleDateFormat.format(calculatefirstDateOfLastMonth());
			// versionNameCurrentMonth = fetchVersionNameofCurrentMonth();
			// versionNameLastMonth = fetchVersionNameofLastMonth();

			// logger.error("fetchProveDailyData- versionNameCurrentMonth " +
			// versionNameCurrentMonth);
			// logger.error("fetchProveDailyData- versionNameLastMonth " +
			// versionNameLastMonth);

			/*
			 * String query =
			 * "SELECT WellName AS WELL_NAME, ProductionDate AS PRODUCTION_DATE, VersionName AS VERSION_NAME, E_GBOE AS FORECAST_BOED "
			 * + "FROM [IOP_Read].[dbo].[EnersightProveMonthly] " +
			 * "WHERE (([PRODUCTIONDATE])>= (DATEADD(\"DD\",-9, SYSDATETIME()))) AND (([PRODUCTIONDATE])<= (DATEADD(\"DD\",-2, SYSDATETIME()))) "
			 * + "ORDER BY PRODUCTIONDATE DESC ";
			 */

			// String versionQuery = "select distinct versionName from
			// [IOP_Read].[dbo].[EnersightProveMonthly] ";
			// try {
			// stmt1 = connection.prepareStatement(versionQuery);
			//
			// resultSet1 = stmt1.executeQuery();
			// if (resultSet1 != null) {
			// while (resultSet1.next()) {
			// versionList.add(resultSet1.getString("versionName"));
			// }
			// }
			// }catch (Exception e) {
			//
			// logger.error("[fetchProveDailyData] : ERROR- Exception while
			// fetching data from database " + e);
			// throw e;
			//
			// } finally {
			// try {
			// stmt1.close();
			// resultSet1.close();
			// } catch (SQLException e) {
			// logger.error("[fetchProveDailyData] : ERROR- Exception while
			// cleaning environment" + e);
			// }
			// }
			// logger.error("versionList- " + versionList);

			String query = "SELECT UWI AS MUWID, WellName AS WELL_NAME, ProductionDate AS PRODUCTION_DATE, VersionName AS VERSION_NAME, E_GBOE AS FORECAST_BOED "
					+ "FROM [IOP_Read].[dbo].[EnersightProveMonthly] "
					+ "WHERE [ProductionDate] = ? and  VersionName = ? ";

			try {
				stmt = connection.prepareStatement(query);

				// if(versionList.contains(versionNameLastMonth)){
				// stmt.setString(1, firstDateOfLastMonth);
				// stmt.setString(2, versionNameLastMonth);
				//
				// } else {
				// stmt.setString(1, firstDateOfCurrentMonth);
				// stmt.setString(2, versionNameCurrentMonth);
				// }
				stmt.setString(1, firstDateOfCurrentMonth);
				stmt.setString(2, getVersionNameOfMonthFromconfig("DOP_Version"));
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
						//enersightProveDailyDto.setVersionName(resultSet.getString("VERSION_NAME"));
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

	public Date calculatefirstDateOfCurrentMonth() {
		Calendar aCalendar = Calendar.getInstance();
		// for current month
		aCalendar.add(Calendar.MONTH, 0);
		// set DATE to 1, so first date of current month
		aCalendar.set(Calendar.DATE, 1);

		Date firstDateOfPreviousMonth = aCalendar.getTime();
		return firstDateOfPreviousMonth;
	}

	public String fetchVersionName() {

		String[] monthName = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		Calendar cal = Calendar.getInstance();
		String month = monthName[cal.get(Calendar.MONTH)];

		// logger.error("Month name: " + month);

		String[] shortMonths = new DateFormatSymbols().getShortMonths();
		String shortMonth = null;
		String versionName = null;

		for (int i = 0; i < (shortMonths.length - 1); i++) {
			shortMonth = shortMonths[i];
			// logger.error("shortMonth = " + shortMonth);

			if (month.contains(shortMonth)) {

				versionName = shortMonth + " OL Monthly";
				// logger.error("versionName = " + versionName);
			}
		}
		return versionName;
	}

	// public String fetchVersionNameofCurrentMonth() {
	//
	// String[] monthName = { "January", "February", "March", "April", "May",
	// "June", "July", "August", "September",
	// "October", "November", "December" };
	//
	// Calendar cal = Calendar.getInstance();
	// String month = monthName[cal.get(Calendar.MONTH)];
	//
	// // logger.error("Month name: " + month);
	//
	// String[] shortMonths = new DateFormatSymbols().getShortMonths();
	// String shortMonth = null;
	// String versionName = null;
	//
	// for (int i = 0; i < (shortMonths.length - 1); i++) {
	// shortMonth = shortMonths[i];
	// // logger.error("shortMonth = " + shortMonth);
	//
	// if (month.contains(shortMonth)) {
	//
	// versionName = shortMonth + " OL Monthly";
	// logger.error("versionName = " + versionName);
	//
	// }
	// }
	// versionName = month + " 18 OL Monthly";
	// return versionName;
	// }

	// public String fetchVersionNameofLastMonth() {
	//
	// String[] monthName = { "January", "February", "March", "April", "May",
	// "June", "July", "August", "September",
	// "October", "November", "December" };
	//
	// Calendar cal = Calendar.getInstance();
	// String prevMonth = monthName[cal.get(Calendar.MONTH) - 1];
	//
	// // logger.error("Month name: " + month);
	//
	// String[] shortMonths = new DateFormatSymbols().getShortMonths();
	// String shortMonth = null;
	// String versionName = null;
	//
	// for (int i = 0; i < (shortMonths.length - 1); i++) {
	// shortMonth = shortMonths[i];
	// // logger.error("shortMonth = " + shortMonth);
	//
	// if(prevMonth.contains(shortMonth)) {
	//
	// versionName = shortMonth + " OL Monthly";
	// logger.error("versionName = " + versionName);
	// }
	//
	//
	//
	// }
	// versionName = prevMonth + " 18 OL Monthly";
	// return versionName;
	// }
	public List<String> fetchLocationCodeInquire(Connection connection, Set<String> locationCodeSet, String userType)
			throws Exception {
		List<String> locationCodeList = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		String locationCodeListQuery = ServicesUtil.getQueryString(locationCodeSet, "pe.LOC_CODE",
				new ArrayList<String>());
		// logger.error("[fetchLocationCodeInvestigation] : INFO -
		// uwiIdListQuery" + locationCodeListQuery);

		String query = "SELECT pe.LOC_CODE AS locationCode from TM_TASK_EVNTS TM  , TM_PROC_EVNTS PE Where  pe.process_id = tm.process_id and ("
				+ locationCodeListQuery + ") AND TM.ORIGIN =? AND TM.STATUS<> ? AND upper(PE.USER_GROUP) LIKE '%"
				+ userType + "%'";

		// logger.error("[fetchLocationCodeInvestigation] : INFO - Query -" +
		// query);

		try {
			stmt = connection.prepareStatement(query);

			stmt.setString(1, ApplicationConstant.IQ_ORIGIN);
			stmt.setString(2, ApplicationConstant.TM_STATUS);
			resultSet = stmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					locationCodeList.add(resultSet.getString("locationCode"));
				}
				// logger.error(
				// "[fetchLocationCodeInvestigation] : INFO - locationCodeList
				// Size" + locationCodeList.size());
			}
		} catch (Exception e) {

			logger.error("[fetchLocationCodeInvestigation] : ERROR- Exception while fetching data from database " + e);
			throw e;

		} finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[fetchLocationCodeInvestigation] : ERROR- Exception while cleaning environment" + e);
			}
		}
		// logger.error("[fetchLocationCodeInvestigation] : INFO -
		// locationCodeList " + locationCodeList);
		return locationCodeList;
	}

	public String getVersion(Connection connection, String versionInDb) {
		String response = "";
		// String versionNameCurrentMonth = fetchVersionNameofCurrentMonth();
		// String versionNameLastMonth = fetchVersionNameofLastMonth();
		List<String> versionList = null;
		String versionQuery = "select distinct versionName from [IOP_Read].[dbo].[EnersightProveMonthly] ";
		try {
			PreparedStatement stmt1 = connection.prepareStatement(versionQuery);

			ResultSet resultSet1 = stmt1.executeQuery();
			if (resultSet1 != null) {
				versionList = new ArrayList<String>();
				while (resultSet1.next()) {
					versionList.add(resultSet1.getString("versionName"));
				}

				// if(versionList.contains(versionNameCurrentMonth)){
				// response = versionNameCurrentMonth;
				// }else if(versionList.contains(versionNameLastMonth)){
				// response = versionNameLastMonth;
				// }else{
				// response = versionInDb;
				// }
				response = versionInDb;
			}
		} catch (Exception e) {
			logger.error("[Murphy][EnersightProveMonthlyDao][getVersion][error]" + e.getMessage());
		}
		return response;
	}

	public List<String> getMuwiWherVarLessThanThres(Connection connection, List<String> muwiList, double duration,
			double thresholdPercent, String version) {
		List<String> reponseMuwiList = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String muwiString = ServicesUtil.getStringFromList(muwiList);
		version = getVersion(connection, version);
		// String query = "select distinct(uwi) as MUWI FROM
		// [IOP_Read].[dbo].[EnersightProveMonthly] where uwi in (
		// "+muwiString+" ) and "
		// + "ProductionDate>= Dateadd(\"DD\",-"+duration+", SYSDATETIME()) and
		// ProductionDate<= Dateadd(\"DD\",-2, SYSDATETIME()) "
		// + "and (((m_gboe - e_gboe) / e_gboe ) *100) < -"+thresholdPercent+"
		// and versionName = '"+version+"' and e_gboe != 0 ";

		String query = "select   uwi as MUWI  FROM [IOP_Read].[dbo].[EnersightProveMonthly] where  versionName = '"
				+ version + "' and uwi in ( " + muwiString + " ) and " + " ProductionDate>= Dateadd(\"DD\", -"
				+ (duration + 2) + ", SYSDATETIME()) and ProductionDate<= Dateadd(\"DD\",-2, SYSDATETIME()) "
				+ "  and (((m_gboe - e_gboe) / e_gboe ) *100) <= -" + thresholdPercent + " and  e_gboe != 0 "
				+ " group by uwi having count(*) = " + duration + "";

		try {
			System.err.println("[getMuwiWherVarLessThanThres][query]" + query);
			stmt = connection.prepareStatement(query);
			resultSet = stmt.executeQuery();

			if (resultSet != null) {
				reponseMuwiList = new ArrayList<String>();

				while (resultSet.next()) {
					reponseMuwiList.add(resultSet.getString("MUWI"));
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][EnersightProveMonthlyDao][getMuwiWherVarLessThanThres][error]" + e.getMessage()
					+ "query" + query);
		}
		// logger.error("[Murphy][EnersightProveMonthlyDao][getMuwiWherVarLessThanThres][reponseMuwiList]"
		// +reponseMuwiList+"query"+query);
		return reponseMuwiList;
	}

	public String getVersionNameOfMonthFromconfig(String configId) throws Exception {

		Connection connection = null;
		connection = DBConnections.createConnectionForHana();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String VersionNameofMonth = null;

		String qryString = "SELECT TCV.CONFIG_DESC_VALUE  AS config_id FROM TM_CONFIG_VALUES TCV WHERE TCV.CONFIG_ID = '"
				+ configId + "'";

		try {
			stmt = connection.prepareStatement(qryString);
			resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				VersionNameofMonth = resultSet.getString("config_id");
			}
		} catch (Exception e) {
			logger.error("[getVersionNameOfMonthFromconfig] : ERROR- VersionNameofMonth" + VersionNameofMonth);
			throw e;
		} finally {
			try {
				stmt.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.error("[getVersionNameOfMonthFromconfig] : ERROR- Exception while cleaning environment" + e);
			}
		}
		return VersionNameofMonth;
	}

	public Map<String, String> fetchFracBOED(Connection connection, Set<String> uwiIdList) throws Exception {

		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		String uwiIdListQuery = ServicesUtil.getQueryString(uwiIdList, "UWI", new ArrayList<String>());
		logger.error("[fetchFracBOED] : INFO - uwiIdListQuery" + uwiIdListQuery);
		Map<String, String> facrBoedMap = new HashMap<>();

		try {

			String query = "SELECT UWI, round(M_GBOE,2) AS ACTUAL_BOED"
					+ " from [IOP_Read].[dbo].[EnersightProveMonthly] WHERE (" + uwiIdListQuery + ") "
					+ " AND VersionName = ?  AND ProductionDate = cast(Dateadd(DD,-2, SYSDATETIME()) as date) ";

			logger.error("[fetchFracBOED] : INFO - query" + query);
			try {
				stmt = connection.prepareStatement(query);
				stmt.setString(1, getVersionNameOfMonthFromconfig("NDTPV_Version"));

				logger.error(
						"[fetchFracBOED] : INFO - NDTPV_Version" + getVersionNameOfMonthFromconfig("NDTPV_Version"));
				resultSet = stmt.executeQuery();

				if (resultSet != null) {
					while (resultSet.next()) {
						logger.error("[fetchFracBOED] : INFO - uwi" + resultSet.getString("UWI"));
						logger.error("[fetchFracBOED] : INFO - ACTUAL_BOED" + resultSet.getString("ACTUAL_BOED"));
						facrBoedMap.put(resultSet.getString("UWI"), resultSet.getString("ACTUAL_BOED"));
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
		return facrBoedMap;
	}

	public Map<String, String> fetchPiggingValue(Connection connection, Set<String> uwiIdList) throws Exception {

		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		String uwiIdListQuery = ServicesUtil.getQueryString(uwiIdList, "UWI", new ArrayList<String>());
		logger.error("[fetchPiggingValue] : INFO - uwiIdListQuery" + uwiIdListQuery);
		Map<String, String> piggingValueMap = new HashMap<>();

		try {

			String query = "SELECT round(WaterProduction,2) as WATER_PRODUCTION, round(E_Oil,2) AS OIL, round(E_Gas,2) AS GAS"
					+ " from [IOP_Read].[dbo].[EnersightProveMonthly] WHERE (" + uwiIdListQuery + ") "
					+ " AND VersionName = ?  AND ProductionDate = cast(Dateadd(DD,-2, SYSDATETIME()) as date) ";

			logger.error("[fetchPiggingValue] : INFO - query" + query);
			try {
				stmt = connection.prepareStatement(query);
				stmt.setString(1, getVersionNameOfMonthFromconfig("NDTPV_Version"));

				logger.error("[fetchPiggingValue] : INFO - NDTPV_Version"
						+ getVersionNameOfMonthFromconfig("NDTPV_Version"));
				resultSet = stmt.executeQuery();
				float totalWater = 0;
				float totalOil = 0;
				float totalGas = 0;
				if (resultSet != null) {
					while (resultSet.next()) {
						logger.error("[fetchPiggingValue] : INFO - water" + resultSet.getString("WATER_PRODUCTION"));
						logger.error("[fetchPiggingValue] : INFO - gas" + resultSet.getString("GAS"));
						logger.error("[fetchPiggingValue] : INFO - oil" + resultSet.getString("OIL"));
						totalWater = totalWater + Float.valueOf(resultSet.getString("WATER_PRODUCTION"));
						totalOil = totalOil + Float.valueOf(resultSet.getString("OIL"));
						totalGas = totalGas + Float.valueOf(resultSet.getString("GAS"));
					}
					piggingValueMap.put("WaterProduction", Float.toString(totalWater));
					piggingValueMap.put("Oil", Float.toString(totalOil));
					piggingValueMap.put("Gas", Float.toString(totalGas));
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
}
