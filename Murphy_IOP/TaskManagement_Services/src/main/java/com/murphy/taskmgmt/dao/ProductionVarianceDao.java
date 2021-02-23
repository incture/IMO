package com.murphy.taskmgmt.dao;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional.TxType;

import org.hibernate.Query;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.integration.dto.EnersightProveDailyDto;
import com.murphy.integration.dto.UIRequestDto;
import com.murphy.integration.dto.UIResponseDto;
import com.murphy.integration.interfaces.EnersightProveDailyLocal;
import com.murphy.integration.service.EnersightProveDaily;
import com.murphy.integration.util.ApplicationConstant;
import com.murphy.taskmgmt.dto.DOPResponseDto;
import com.murphy.taskmgmt.dto.DOPVarianceDto;
import com.murphy.taskmgmt.dto.DopDummyDto;
import com.murphy.taskmgmt.dto.ProductionVarianceDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.ThresholdDto;
import com.murphy.taskmgmt.entity.ProductionVarianceDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.service.DGPFacade;
import com.murphy.taskmgmt.service.interfaces.EnersightProveMonthlyLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("productionVarianceDao")
public class ProductionVarianceDao extends BaseDao<ProductionVarianceDo, ProductionVarianceDto> {

	private static final Logger logger = LoggerFactory.getLogger(ProductionVarianceDao.class);

	@Autowired
	EnersightProveMonthlyLocal enersightProveMonthlyLocal;

	@Autowired
	HierarchyDao locDao;

	@Autowired
	ItaDopDao itaDopDao;
	
	@Autowired
	private ConfigDao configdao;
	
	@Override
	protected ProductionVarianceDo importDto(ProductionVarianceDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		ProductionVarianceDo entity = new ProductionVarianceDo();

		if (!ServicesUtil.isEmpty(fromDto.getWell()))
			entity.setWell(fromDto.getWell());
		if (!ServicesUtil.isEmpty(fromDto.getProductionDate()))
			entity.setProductionDate(fromDto.getProductionDate());
		if (!ServicesUtil.isEmpty(fromDto.getVersionName()))
			entity.setVersionName(fromDto.getVersionName());
		if (!ServicesUtil.isEmpty(fromDto.getForecastBoed()))
			entity.setForecastBoed(fromDto.getForecastBoed());
		if (!ServicesUtil.isEmpty(fromDto.getMuwi()))
			entity.setMuwi(fromDto.getMuwi());

		return entity;
	}

	@Override
	protected ProductionVarianceDto exportDto(ProductionVarianceDo entity) {

		ProductionVarianceDto dto = new ProductionVarianceDto();

		if (!ServicesUtil.isEmpty(entity.getWell()))
			dto.setWell(entity.getWell());
		if (!ServicesUtil.isEmpty(entity.getProductionDate()))
			dto.setProductionDate(entity.getProductionDate());
		if (!ServicesUtil.isEmpty(entity.getVersionName()))
			dto.setVersionName(entity.getVersionName());
		if (!ServicesUtil.isEmpty(entity.getForecastBoed()))
			dto.setForecastBoed(entity.getForecastBoed());
		if (!ServicesUtil.isEmpty(entity.getMuwi()))
			dto.setMuwi(entity.getMuwi());

		return dto;
	}

	public String createRecord() {

		String response = MurphyConstant.FAILURE;
		try {
			Calendar cal = Calendar.getInstance();
			int date = cal.get(Calendar.DATE);
			int day = cal.get(Calendar.DAY_OF_WEEK);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.HOUR_OF_DAY, 7);
			cal.set(Calendar.MINUTE, 0);
			Calendar calNext = Calendar.getInstance();
			Calendar calNextMon = Calendar.getInstance();
			calNext.setTime(cal.getTime());
			calNextMon.setTime(cal.getTime());
			calNext.add(Calendar.DATE, 1);
			calNextMon.add(Calendar.DATE, 7);
			logger.error("calNextMon.getTime()" + calNextMon.getTime() + "calNext.getTime()" + calNext.getTime() + "");
			EnersightProveDailyLocal enersightProveDailyLocal = new EnersightProveDaily();
			UIResponseDto uiResponseDto = new UIResponseDto();
			ProductionVarianceDto productionVarianceDto = null;
			uiResponseDto = enersightProveDailyLocal.fetchProveDailyData();

			List<EnersightProveDailyDto> enersightProveDailyDtoList = uiResponseDto.getEnersightProveDailyDtoList();
			if (!ServicesUtil.isEmpty(enersightProveDailyDtoList)) {
				for (EnersightProveDailyDto enersightProveDailyDto : enersightProveDailyDtoList) {

					if (date != 1) {
						productionVarianceDto = new ProductionVarianceDto();
						productionVarianceDto.setMuwi(enersightProveDailyDto.getMuwiId());
						productionVarianceDto.setWell(enersightProveDailyDto.getWell());
						productionVarianceDto.setProductionDate(enersightProveDailyDto.getLastProdDateField());
						// productionVarianceDto.setVersionName(enersightProveDailyDto.getVersionName());
						productionVarianceDto.setForecastBoed(enersightProveDailyDto.getForecastBoed());

						saveOrUpdate(importDto(productionVarianceDto));
					}
					if (day == 2) {
						createRecordForStagingVariance(
								ServicesUtil.convertFromZoneToZoneString(calNextMon.getTime(), "", "", "", "",
										MurphyConstant.DATE_DB_FORMATE_SD),
								(enersightProveDailyDto.getForecastBoed() * 7), enersightProveDailyDto.getMuwiId(),
								"QTOILD", enersightProveDailyDto.getWell(), MurphyConstant.DOP_FORECAST,
								MurphyConstant.WEEKLY);

						createRecordForStagingVariance(
								ServicesUtil.convertFromZoneToZoneString(cal.getTime(), "", "", "", "",
										MurphyConstant.DATE_DB_FORMATE_SD),
								0, enersightProveDailyDto.getMuwiId(), "QTOILD", enersightProveDailyDto.getWell(),
								MurphyConstant.DOP_FORECAST, MurphyConstant.WEEKLY);

					}

					createRecordForStagingVariance(
							ServicesUtil.convertFromZoneToZoneString(cal.getTime(), "", "", "", "",
									MurphyConstant.DATE_DB_FORMATE_SD),
							0, enersightProveDailyDto.getMuwiId(), "QTOILD", enersightProveDailyDto.getWell(),
							MurphyConstant.DOP_FORECAST, MurphyConstant.DAILY);

					createRecordForStagingVariance(
							ServicesUtil.convertFromZoneToZoneString(calNext.getTime(), "", "", "", "",
									MurphyConstant.DATE_DB_FORMATE_SD),
							enersightProveDailyDto.getForecastBoed(), enersightProveDailyDto.getMuwiId(), "QTOILD",
							enersightProveDailyDto.getWell(), MurphyConstant.DOP_FORECAST, MurphyConstant.DAILY);

					// createStagingForVariance( productionVarianceDto);
					response = MurphyConstant.SUCCESS;
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][ProductionVarianceDao][createRecord][error] " + e);
		}
		return response;
	}

	public int createStagingForVariance(ProductionVarianceDto dto) {
		int result = 0;
		try {

			createRecordForStagingVariance(
					ServicesUtil.convertFromZoneToZoneString(dto.getCreatedAt(), "", "", "", "",
							MurphyConstant.DATE_DB_FORMATE_SD),
					(dto.getForecastBoed() * 7), dto.getMuwi(), "QTOILD", dto.getWell(), MurphyConstant.DOP_FORECAST,
					MurphyConstant.WEEKLY);
			result = 1;
		} catch (Exception e) {
			logger.error("[Murphy][ProductionVarianceDao][createStagingForVariance][error]" + e.getMessage());
		}
		return result;
	}

	@Transactional
	public int createRecordForStagingVariance(String createdAt, double dataValue, String muwi, String paramType,
			String locName, String source, String varianceType) {
		int result = 0;
		String queryString = "";
		try {
			// if(ServicesUtil.isEmpty(locName)){
			// locName = locDao.getLocationByMuwi(muwi);
			// }

			// String selectQuery = "SELECT MUWI_ID from
			// TM_PRODUCTION_VARIANCE_STAGING where CREATED_AT = "
			// + " TO_TIMESTAMP('" + createdAt + "', 'yyyy-MM-dd HH24:mi:ss')
			// and MUWI_ID = '"+muwi+"' "
			// + " and PARAM_TYPE = '"+paramType+"' and VARIANCE_TYPE =
			// '"+varianceType+"' and SOURCE = '"+source+"' ";

			// Query sQuery = this.getSession().createSQLQuery(selectQuery);
			// List<String> selectResult = (List<String> ) sQuery.list();
			//
			// if(!ServicesUtil.isEmpty(selectResult) && selectResult.size() >
			// 0){
			queryString = "INSERT INTO TM_PRODUCTION_VARIANCE_STAGING(CREATED_AT,DATA_VALUE,MUWI_ID,PARAM_TYPE,SOURCE) "
					+ "VALUES('TO_TIMESTAMP('" + createdAt + "', 'yyyy-MM-dd HH24:mi:ss')," + dataValue + ",'" + muwi
					+ "','" + paramType + "','" + MurphyConstant.DOP_CANARY + "'";

			// queryString = "INSERT INTO TM_PRODUCTION_VARIANCE_STAGING
			// VALUES('"
			// + UUID.randomUUID().toString().replaceAll("-", "") +
			// "'/*STAGING_ID <VARCHAR(32)>*/,"
			// + "TO_TIMESTAMP('" + createdAt + "', 'yyyy-MM-dd
			// HH24:mi:ss')/*CREATED_AT <VARCHAR(100)>*/,"
			// + dataValue + "/*DATA_VALUE <DOUBLE>*/," + "'" + muwi +
			// "'/*MUWI_ID <VARCHAR(100)>*/,'" + paramType
			// + "'/*PARAM_TYPE <VARCHAR(100)>*/" + ",'" + locName +
			// "'/*WELL_NAME <VARCHAR(100)>*/,'" + source
			// + "'/*SOURCE <VARCHAR(100)>*/,'" + varianceType +
			// "'/*VARIANCE_TYPE <VARCHAR(100)>*/" + ")";
			Query q = this.getSession().createSQLQuery(queryString);
			result = (Integer) q.executeUpdate();
			// }else{
			// logger.error(
			// "[Murphy][ProductionVarianceDao][createRecordForStagingVariance]\n"
			// + " [Record already exists] - "+selectQuery);
			// }
		} catch (Exception e) {
			logger.error("[Murphy][ProductionVarianceDao][createRecordForStagingVariance][queryString]" + queryString
					+ "[error]" + e.getMessage());
		}
		return result;
	}

	// @Transactional
	// public int deleteDailyData(String createdAt) {
	// int result = 0;
	// String queryString = "";
	// try {
	// queryString = "delete from TM_PRODUCTION_VARIANCE_STAGING where
	// variance_type = '"+MurphyConstant.DAILY+"' and created_at <=
	// TO_TIMESTAMP('"+createdAt +"', 'yyyy-MM-dd HH24:mi:ss')";
	// // + " and hour(created_at) <> 6 ";
	// Query q = this.getSession().createSQLQuery(queryString);
	// result = (Integer) q.executeUpdate();
	// } catch (Exception e) {
	// logger.error("[Murphy][ProductionVarianceDao][deleteDailyData][queryString]"
	// + queryString +"[error]" + e.getMessage());
	// }
	// return result;
	// }

	@Transactional
	public int deleteDailyData(String createdAt) {
		int result = 0;
		String queryString = "";
		try {
			queryString = "delete from TM_PRODUCTION_VARIANCE_STAGING where created_at <= TO_TIMESTAMP('" + createdAt
					+ "', 'yyyy-MM-dd HH24:mi:ss') ";
			// queryString = "delete from TM_PRODUCTION_VARIANCE_STAGING where
			// variance_type = '" + MurphyConstant.DAILY
			// + "' and created_at <= TO_TIMESTAMP('" + createdAt + "',
			// 'yyyy-MM-dd HH24:mi:ss') ";
			Query q = this.getSession().createSQLQuery(queryString);
			result = (Integer) q.executeUpdate();
		} catch (Exception e) {
			logger.error("[Murphy][ProductionVarianceDao][deleteDailyForecastData][queryString]" + queryString
					+ "[error]" + e.getMessage());
		}
		return result;
	}

	@Transactional
	public int deleteWeeklyForecastData(String createdAt) {
		int result = 0;
		String queryString = "";
		try {
			queryString = "delete from TM_PRODUCTION_VARIANCE_STAGING where source  in ( '"
					+ MurphyConstant.DOP_PROJECTED + "','" + MurphyConstant.DOP_FORECAST
					+ "')  and created_at <= TO_TIMESTAMP('" + createdAt + "', 'yyyy-MM-dd HH24:mi:ss') ";
			Query q = this.getSession().createSQLQuery(queryString);
			result = (Integer) q.executeUpdate();
		} catch (Exception e) {
			logger.error("[Murphy][ProductionVarianceDao][deleteWeeklyForecastData][queryString]" + queryString
					+ "[error]" + e.getMessage());
		}
		return result;
	}

	@Transactional
	public int deleteWeeklyData(String createdAt) {
		int result = 0;
		String queryString = "";
		try {
			queryString = "delete from TM_PRODUCTION_VARIANCE_STAGING where  created_at <= TO_TIMESTAMP('" + createdAt
					+ "', 'yyyy-MM-dd HH24:mi:ss') ";
			// + "where variance_type <> '"+MurphyConstant.DOP_FORECAST+"'";
			Query q = this.getSession().createSQLQuery(queryString);
			result = (Integer) q.executeUpdate();
		} catch (Exception e) {
			logger.error("[Murphy][ProductionVarianceDao][deleteWeeklyData][queryString]" + queryString + "[error]"
					+ e.getMessage());
		}
		return result;
	}

	@Transactional
	public int updateDailyVariance(int hrs, String currentDate, String endDate) {
		int result = 0;
		String queryString = "";
		try {
			queryString = "Update TM_PRODUCTION_VARIANCE_STAGING as st  set st.data_value = ((( Select max(data_value) from  TM_PRODUCTION_VARIANCE_STAGING ins  where"
					// + " ins.created_at = to_timestamp('" + currentDate+
					// "','yyyy-MM-dd HH24:mi:ss') and"
					+ " ins.muwi_id = st.muwi_id  and ins.source = '" + MurphyConstant.DOP_CANARY
					// + "' and ins.variance_type = '" + MurphyConstant.DAILY
					+ "')/( " + (hrs - 7) + "))*24)" + " where source = '" + MurphyConstant.DOP_PROJECTED
					+ "' and created_at = to_timestamp('" + endDate + "','yyyy-MM-dd HH24:mi:ss')";

			Query q = this.getSession().createSQLQuery(queryString);
			logger.error("[Murphy][ProductionVarianceDao][updateDailyVariance][queryString]" + queryString);
			result = (Integer) q.executeUpdate();
		} catch (Exception e) {
			logger.error("[Murphy][ProductionVarianceDao][updateDailyVariance][queryString]" + queryString + "[error]"
					+ e.getMessage());
		}
		return result;
	}

	// @Transactional
	// public int updateWeeklyVariance(int days, String currentDate, String
	// endDate) {
	// int result = 0;
	// String queryString = "";
	// try {
	//
	// queryString = "Update TM_PRODUCTION_VARIANCE_STAGING as st set
	// st.data_value = "
	// + "( Select sum(data_value) from TM_PRODUCTION_VARIANCE_STAGING ins where
	// "
	// + " ins.created_at < to_timestamp('" + currentDate
	// + "','yyyy-MM-dd HH24:mi:ss') and ins.muwi_id = st.muwi_id and ins.source
	// = '"
	// + MurphyConstant.DOP_CANARY + "' and ins.variance_type = '" +
	// MurphyConstant.WEEKLY + "') /( "
	// + (days) + "))*7)" + " where st.source = '" +
	// MurphyConstant.DOP_PROJECTED
	// + "' and st.created_at = to_timestamp('" + endDate
	// + "','yyyy-MM-dd HH24:mi:ss') and st.variance_type = '" +
	// MurphyConstant.WEEKLY + "'";
	//
	// Query q = this.getSession().createSQLQuery(queryString);
	// logger.error("[Murphy][ProductionVarianceDao][updateWeeklyVariance][queryString]"
	// + queryString);
	// result = (Integer) q.executeUpdate();
	// } catch (Exception e) {
	// logger.error("[Murphy][ProductionVarianceDao][updateWeeklyVariance][queryString]"
	// + queryString + "[error]"
	// + e.getMessage());
	// }
	// return result;
	// }
	//
	// @Transactional
	// public int updateDailyOfWeeklyVariance(String currentDate) {
	// int result = 0;
	// String queryString = "";
	// try {
	//
	// queryString = "Update TM_PRODUCTION_VARIANCE_STAGING as st set
	// st.data_value = "
	// + "( Select sum(data_value) from TM_PRODUCTION_VARIANCE_STAGING ins where
	// "
	// + " ins.created_at <= to_timestamp('" + currentDate
	// + "','yyyy-MM-dd HH24:mi:ss') and ins.muwi_id = st.muwi_id and ins.source
	// = '"
	// + MurphyConstant.DOP_CANARY + "' and ins.variance_type = '" +
	// MurphyConstant.WEEKLY + "')"
	// + " where st.source = '" + MurphyConstant.DOP_CANARY + "' and
	// st.created_at = to_timestamp('"
	// + currentDate + "','yyyy-MM-dd HH24:mi:ss') and st.variance_type = '" +
	// MurphyConstant.WEEKLY + "'";
	//
	// Query q = this.getSession().createSQLQuery(queryString);
	// logger.error("[Murphy][ProductionVarianceDao][updateWeeklyVariance][queryString]"
	// + queryString);
	// result = (Integer) q.executeUpdate();
	// } catch (Exception e) {
	// logger.error("[Murphy][ProductionVarianceDao][updateWeeklyVariance][queryString]"
	// + queryString + "[error]"
	// + e.getMessage());
	// }
	// return result;
	// }

	@Transactional
	public int updateCurrentForecast(String currentDate) {
		int result = 0;
		String queryString = "";
		try {

			queryString = "Update TM_PRODUCTION_VARIANCE_STAGING as st  set st.data_value = 0" + " where"
			// + " st.source = '"+MurphyConstant.DOP_CANARY+"'"
					+ " and st.created_at = to_timestamp('" + currentDate + "','yyyy-MM-dd HH24:mi:ss') ";
			// + "and st.variance_type = '"+MurphyConstant.WEEKLY+"'";

			Query q = this.getSession().createSQLQuery(queryString);
			logger.error("[Murphy][ProductionVarianceDao][updateWeeklyVariance][queryString]" + queryString);
			result = (Integer) q.executeUpdate();
		} catch (Exception e) {
			logger.error("[Murphy][ProductionVarianceDao][updateWeeklyVariance][queryString]" + queryString + "[error]"
					+ e.getMessage());
		}
		return result;
	}

	/*
	 * @SuppressWarnings("unchecked") public List<DOPVarianceDto>
	 * fetchVarianceDataForWells(UIRequestDto uiRequestDto) {
	 * logger.error("[PVD]uiRequestDto : "+uiRequestDto); List<DOPVarianceDto>
	 * result = new ArrayList<DOPVarianceDto>();
	 * 
	 * String queryString = ""; // List<Date> dates =
	 * getStartTimeForCanaryWeekly(); try { // Date canaryStart = null; Date
	 * canaryEnd = null; // Date current =
	 * ServicesUtil.convertFromZoneToZone(null, null, //
	 * MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE, "", //
	 * MurphyConstant.DATE_DB_FORMATE); Date current =
	 * ServicesUtil.getDateWithInterval(new Date(), -300,
	 * MurphyConstant.MINUTES);
	 * 
	 * if (uiRequestDto.getDuration() == 1) { // canaryStart =
	 * scaleDownTimeToSeventhHour(current).getTime(); canaryEnd = current; }
	 * else { // canaryStart = getMeMonday(current).getTime(); canaryEnd =
	 * scaleDownTimeToSeventhHour(current).getTime(); } Calendar calEnd =
	 * Calendar.getInstance(); calEnd.setTime(canaryEnd);
	 * 
	 * DecimalFormat df = new DecimalFormat("#.##"); List<String> muwiIdList =
	 * null; if (!ServicesUtil.isEmpty(uiRequestDto.getLocationCodeList())) {
	 * muwiIdList = getMuwiByLocationTypeAndCode(uiRequestDto.getLocationType(),
	 * uiRequestDto.getLocationCodeList()); }
	 * 
	 * String varianceType = ""; if (uiRequestDto.getDuration() == 1) {
	 * varianceType = MurphyConstant.DAILY; } else { varianceType =
	 * MurphyConstant.WEEKLY; }
	 * 
	 * queryString = "select distinct ins.muwi_id, " +
	 * " (select max(st.data_value) from TM_PRODUCTION_VARIANCE_STAGING st where st.variance_type = '"
	 * + varianceType + "' " + " and st.source = '" + MurphyConstant.DOP_CANARY
	 * + "' and st.muwi_id = ins.muwi_id ) as actual_value, " +
	 * " ins.well_name,d.tier,d.location_code, " +
	 * " (select  max(st.data_value) from TM_PRODUCTION_VARIANCE_STAGING st where st.variance_type = '"
	 * + varianceType + "' and " + "st.source = '" +
	 * MurphyConstant.DOP_PROJECTED +
	 * "' and st.muwi_id = ins.muwi_id ) as forecast, " +
	 * "(select f.process_id from tm_proc_evnts as e left outer join tm_task_evnts as f  "
	 * + " on f.process_id = e.process_id where f.origin in ('" +
	 * MurphyConstant.INVESTIGATON + "')  and f.status <> '" +
	 * MurphyConstant.COMPLETE +
	 * "' and e.loc_code = d.location_code ) as invtTaskId ," +
	 * "  (select f.task_id from tm_proc_evnts as e left outer join tm_task_evnts as f "
	 * + " on f.process_id = e.process_id where f.origin in ('" +
	 * MurphyConstant.VARIANCE + "')  and f.status <> '" +
	 * MurphyConstant.COMPLETE + "' and e.loc_code = d.location_code and  " +
	 * "  f.created_at = (select max(f1.created_at) from tm_proc_evnts as e1 left outer join tm_task_evnts as f1 "
	 * + " on f1.process_id = e1.process_id where f1.origin in ('" +
	 * MurphyConstant.VARIANCE + "')  and f1.status <> '" +
	 * MurphyConstant.COMPLETE + "' and e1.loc_code = d.location_code) " +
	 * " ) as varianceTask , " +
	 * "(select max(st.data_value) from  TM_PRODUCTION_VARIANCE_STAGING st where st.variance_type = '"
	 * + varianceType + "' " + " and st.source = '" +
	 * MurphyConstant.DOP_FORECAST +
	 * "' and st.muwi_id = ins.muwi_id and month(created_at) = month('" +
	 * ServicesUtil.convertFromZoneToZoneString(canaryEnd, "", "", "",
	 * MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_IOS_FORMAT) +
	 * "')) as planned_value,  (SELECT f.task_id FROM tm_proc_evnts AS e	LEFT OUTER JOIN "
	 * +
	 * "tm_task_evnts AS f ON f.process_id = e.process_id WHERE (f.origin IN ('Investigation')) AND f.status <> 'COMPLETED' AND e.loc_code = d.location_code	) AS invtigationTaskId  "
	 * +
	 * " from TM_PRODUCTION_VARIANCE_STAGING ins left outer join well_muwi as c on ins.muwi_id = c.muwi  "
	 * +
	 * " left outer join well_tier as d  on d.location_code = c.location_code where ins.variance_type = '"
	 * + varianceType + "' and  source = '" + MurphyConstant.DOP_PROJECTED +
	 * "' " + "";
	 * 
	 * if (ServicesUtil.isEmpty(muwiIdList) &&
	 * MurphyConstant.WELLPAD.equals(uiRequestDto.getLocationType())) return
	 * null; else if (!ServicesUtil.isEmpty(muwiIdList)) { queryString =
	 * queryString + " and ins.muwi_id in (" +
	 * ServicesUtil.getStringFromList(muwiIdList) + ")"; }
	 * 
	 * Query q = this.getSession().createSQLQuery(queryString); logger.error(
	 * "[Murphy][ProductionVarianceDao][fetchVarianceDataForWells][queryString]"
	 * + queryString); // logger.error(
	 * "[Murphy][ProductionVarianceDao][fetchVarianceDataForWells][queryString]"
	 * + q.list().size()); List<Object[]> objList = (List<Object[]>) q.list();
	 * if (!ServicesUtil.isEmpty(objList)) { List<ThresholdDto> thresholdList =
	 * getThreshold(); for (Object[] obj : objList) { DOPVarianceDto dto = new
	 * DOPVarianceDto(); dto.setMuwi(ServicesUtil.isEmpty(obj[0]) ? null :
	 * (String) obj[0]); dto.setActualBoed(ServicesUtil.isEmpty(obj[1]) ? 0.00 :
	 * Double.valueOf(df.format((Double) obj[1])));
	 * dto.setLocation(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
	 * dto.setTier(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
	 * dto.setLocationCode(ServicesUtil.isEmpty(obj[4]) ? null : (String)
	 * obj[4]); dto.setForecastBoed( ServicesUtil.isEmpty(obj[5]) ? 0.00 :
	 * Double.valueOf(df.format((Double) obj[5]))); dto.setPlannedBoed(
	 * ServicesUtil.isEmpty(obj[8]) ? 0.00 : Double.valueOf(df.format((Double)
	 * obj[8]))); dto.setLocationType(MurphyConstant.WELL); // double forecast =
	 * 0; // if(uiRequestDto.getDuration() == 1){ // forecast = getForecastBoed(
	 * 7 , 0 , // calEnd.get(Calendar.HOUR_OF_DAY) , // dto.getActualBoed(),1);
	 * // }else{ //
	 * dto.setPlannedBoed(Double.valueOf(df.format(dto.getForecastBoed()*7)));
	 * // // forecast = getForecastBoed( 2 , 0 , //
	 * calEnd.get(Calendar.DAY_OF_WEEK) , // dto.getActualBoed(),7); // }
	 * 
	 * // dto.setForecastBoed(forecast); //
	 * dto.setForecastBoed(Double.valueOf(df.format(dto.getProjectedBoed())));
	 * if (!ServicesUtil.isEmpty(dto.getProjectedBoed()) &&
	 * !ServicesUtil.isEmpty(dto.getForecastBoed())) {
	 * dto.setVariance(dto.getProjectedBoed() - dto.getForecastBoed()); //
	 * dto.setVariance( dto.getForecastBoed() - // dto.getProjectedBoed() ); if
	 * (dto.getProjectedBoed() != 0 && dto.getForecastBoed() != 0) { //
	 * dto.setVariancePercent(dto.getVariance()/dto.getProjectedBoed()*100);
	 * dto.setVariancePercent(dto.getVariance() / dto.getForecastBoed() * 100);
	 * dto.setVariancePercent(Double.valueOf(df.format(dto.getVariancePercent())
	 * )); } dto.setVariance(Double.valueOf(df.format(dto.getVariance()))); }
	 * dto.setInvestigationTaskId(ServicesUtil.isEmpty(obj[9]) ? null : (String)
	 * obj[9]); dto.setInvestigationProcessId(ServicesUtil.isEmpty(obj[6]) ?
	 * null : (String) obj[6]);
	 * dto.setHasInvestigation(!ServicesUtil.isEmpty(dto.
	 * getInvestigationProcessId()) ? true : false);
	 * dto.setDispatchTaskId(ServicesUtil.isEmpty(obj[7]) ? null : (String)
	 * obj[7]); dto.setDispatch(!ServicesUtil.isEmpty(dto.getDispatchTaskId()) ?
	 * true : false); // dto.setPlannedBoed(ServicesUtil.isEmpty(obj[8]) ? 0.00:
	 * // Double.valueOf(df.format((Double) obj[8])) );
	 * dto.setAcknowledge(false); if (!ServicesUtil.isEmpty(thresholdList)) {
	 * for (ThresholdDto tres : thresholdList) { if (dto.getVariancePercent() >=
	 * tres.getMinimumValue() && dto.getVariancePercent() <=
	 * tres.getMaximumValue() && tres.getTier().equals(dto.getTier())) {
	 * dto.setAlarmSeverity(tres.getCondition()); break; } } }
	 * 
	 * result.add(dto); } } // logger.error(
	 * "[Murphy][ProductionVarianceDao][fetchVarianceDataForWells][queryString]"
	 * // + queryString +"\nresult"+result); } catch (Exception e) { //
	 * logger.error(
	 * "[Murphy][ProductionVarianceDao][fetchVarianceDataForWells][queryString]"
	 * // + queryString +"[error]" + e.getMessage()); logger.error(
	 * "[Murphy][ProductionVarianceDao][fetchVarianceDataForWells][error]" +
	 * e.getMessage()); } return result;
	 * 
	 * }
	 */

	@SuppressWarnings("unchecked")
	public List<String> getMuwiByLocationTypeAndCode(String locationType, List<String> locationCodeList)
			throws Exception {
		StringBuilder stringBuilder = null;
		List<String> muwiIdList = null;
		try {
			stringBuilder = new StringBuilder("select wm.muwi as well from production_location p1 ");

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

			stringBuilder.append(" in (" + ServicesUtil.getStringFromList(locationCodeList) + ")");

			Query q = this.getSession().createSQLQuery(stringBuilder.toString());
			muwiIdList = (List<String>) q.list();
			// logger.error("[Murphy][ProductionVarianceDao][getMuwiByLocationTypeAndCode][query]"+stringBuilder
			// +"\n"+muwiIdList.size());

		} catch (Exception e) {
			logger.error("[Murphy][ProductionVarianceDao][getMuwiByLocationTypeAndCode]" + e.getMessage() + "[query]"
					+ stringBuilder);
		}
		return muwiIdList;
	}

	public String getQueryMuwiByLocTypeAndCode(String locationType, List<String> locationCodeList) {
		StringBuilder stringBuilder = null;
		try {
			stringBuilder = new StringBuilder("select wm.muwi as well from production_location p1 ");

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

			// stringBuilder.append(" in (" +
			// ServicesUtil.getStringFromList(locationCodeList) + ")");

			// logger.error("[Murphy][ProductionVarianceDao][getMuwiByLocationTypeAndCode][query]"+stringBuilder
			// +"\n"+muwiIdList.size());

		} catch (Exception e) {
			logger.error("[Murphy][ProductionVarianceDao][getMuwiByLocationTypeAndCode]" + e.getMessage() + "[query]"
					+ stringBuilder);
		}
		return stringBuilder.toString();
	}

	@SuppressWarnings("unchecked")
	private List<ThresholdDto> getThreshold() {
		List<ThresholdDto> returnList = null;
		String queryString = "";
		try {
			queryString = "select maximum_value ,minimum_value , ALARM_CONDITION ,tier  from TM_THRESHOLD_MAPPING ";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> list = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(list)) {
				returnList = new ArrayList<ThresholdDto>();
				ThresholdDto dto = null;
				for (Object[] obj : list) {
					dto = new ThresholdDto();
					dto.setMaximumValue(ServicesUtil.isEmpty(obj[0]) ? 0 : (Double) obj[0]);
					dto.setMinimumValue(ServicesUtil.isEmpty(obj[1]) ? 0 : (Double) obj[1]);
					dto.setCondition(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					dto.setTier(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					returnList.add(dto);
				}
			}
			// logger.error("[Murphy][ProductionVarianceDao][getThreshold][queryString]"
			// + queryString +"\nresult"+returnList);

		} catch (Exception e) {
			logger.error(
					"[Murphy][ProductionVarianceDao][getThreshold]" + e.getMessage() + "[queryString]" + queryString);
		}
		return returnList;
	}

	/*
	 * private double getForecastBoed(double x1 ,double y1 ,double x2 ,double y2
	 * ,int type){ int x = 1; if(type == 1){ if(x2 < 7){ x2 = x2 + 24; } x = 31;
	 * }else { if(x2 == 0){ x2 = 8; } x = 8; } double num = y2 - y1; double deno
	 * = x2 - x1; double returnVal = 0; if(deno != 0 && num != 0){ returnVal =
	 * (num / deno) * x ; } //
	 * logger.error("[x1]"+x1+"[y1]"+y1+"[x2]"+x2+"[y2]"+y2+"returnVal"+
	 * returnVal); return returnVal;
	 * 
	 * }
	 */

	/*
	 * private List<Date> getStartTimeForCanaryWeekly(){ List<Date> returnList =
	 * null; Date current = ServicesUtil.convertFromZoneToZone(null, null,
	 * MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE, "", "");
	 * 
	 * 
	 * int startDiff = 0; int day = ServicesUtil.getDayOfTheDate(current, "");
	 * int hour = ServicesUtil.getDayOfTheDate(current, ""); if((day == 2 &&
	 * hour >= 7 ) || (day == 3 && hour < 7)){
	 * 
	 * }else{ Calendar cal = Calendar.getInstance(); cal.setTime(current);
	 * cal.set(Calendar.HOUR, 7); cal.set(Calendar.MINUTE, 0);
	 * cal.set(Calendar.SECOND, 0); Date currentDateAt7 = cal.getTime() ; Date
	 * startDate = currentDateAt7; Date endDate = currentDateAt7;
	 * 
	 * returnList = new ArrayList<Date>(); if((day >= 3 && day <= 7)){ startDiff
	 * = day -2 ; }else if(day == 2 || (day == 0)){ startDiff = 6; } if(hour <
	 * 7){ endDate = ServicesUtil.getDateWithInterval(currentDateAt7 , -1,
	 * MurphyConstant.DAYS); }
	 * 
	 * startDiff = -startDiff; if(startDiff != 0){ startDate =
	 * ServicesUtil.getDateWithInterval(currentDateAt7 , startDiff,
	 * MurphyConstant.DAYS); } returnList.add(startDate);
	 * returnList.add(endDate);
	 * logger.error("[Murphy][ProductionVarianceDao][startDate]"+startDate
	 * +"[endDate]"+endDate); }
	 * 
	 * return null; }
	 */

	public Calendar getMeMonday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c = scaleDownTimeToSeventhHour(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int numberOfDays = (dayOfWeek - 1) % 7; // Shifting Sun - Sat to Mon -
		// Sun
		numberOfDays += numberOfDays == 0 ? 7 : 0;// Correcting for %7; values
		// should be 1 to 7
		numberOfDays = 1 - numberOfDays; // Number of days to be subtracted
		c.add(Calendar.DATE, numberOfDays);
		return c;
	}

	public Calendar getMeNextMonday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c = scaleDownTimeToSeventhHour(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int numberOfDays = 9 - dayOfWeek;
		c.add(Calendar.DATE, numberOfDays);
		return c;
	}

	public Calendar scaleDownTimeToSeventhHour(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (c.get(Calendar.HOUR_OF_DAY) < 7) {
			c.add(Calendar.DATE, -1);
		}
		c.set(Calendar.HOUR_OF_DAY, 07);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	public Calendar scaleDownTimeToSeventhHourOfDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (c.get(Calendar.HOUR_OF_DAY) < 7) {
			c.add(Calendar.DATE, -1);
		}
		c.set(Calendar.HOUR_OF_DAY, 07);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	public int updatePlannedVariance(int daysInPrevsMonth, int dayInCurrentMonth, int currentMonth,
			String currentDateInString) {
		int result = 0;
		String queryString = "";
		try {
			queryString = "Update TM_PRODUCTION_VARIANCE_STAGING as st  set st.data_value = "
					+ "((	( Select avg(FORECAST_BOED) from  PRODUCTION_VARIANCE ins  where "
					+ "month(PRODUCTION_DATE)= (" + currentMonth + " -1 ) and ins.muwi_id = st.muwi_id) * "
					+ daysInPrevsMonth + " ) + ( Select avg(FORECAST_BOED) from  PRODUCTION_VARIANCE ins  where "
					+ " month(PRODUCTION_DATE)= " + currentMonth + " and ins.muwi_id = st.muwi_id) * "
					+ dayInCurrentMonth + " ) " + "where source = '" + MurphyConstant.DOP_FORECAST
					+ "' and created_at = to_timestamp('" + currentDateInString + "','yyyy-MM-dd HH24:mi:ss')";

			Query q = this.getSession().createSQLQuery(queryString);
			// logger.error("[Murphy][ProductionVarianceDao][updateDailyVariance][queryString]"
			// + queryString);
			result = (Integer) q.executeUpdate();
		} catch (Exception e) {
			logger.error("[Murphy][ProductionVarianceDao][updateDailyVariance][queryString]" + queryString + "[error]"
					+ e.getMessage());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<DOPVarianceDto> fetchVarianceDataForWells(UIRequestDto uiRequestDto) {
		// logger.error("[PVD]uiRequestDto : "+uiRequestDto);
		List<DOPVarianceDto> result = new ArrayList<DOPVarianceDto>();
		String queryString = "";
		try {
			DecimalFormat df = new DecimalFormat("#.##");
			queryString = dopQuery(uiRequestDto.getLocationCodeList(), uiRequestDto.getLocationType(),
					uiRequestDto.getDuration(), uiRequestDto.getUserType(), uiRequestDto.isRolledUp());
			Query q = this.getSession().createSQLQuery(queryString);
			logger.error("[Murphy][ProductionVarianceDao][fetchVarianceDataForWells][uiRequestDto.isRolledUp()]"
					+ uiRequestDto.isRolledUp() + "\n[queryString]" + queryString);
			List<Object[]> objList = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(objList)) {
				List<ThresholdDto> thresholdList = getThreshold();
				for (Object[] obj : objList) {
					DOPVarianceDto dto = new DOPVarianceDto();
					dto.setActualBoed(ServicesUtil.isEmpty(obj[0]) ? 0.00 : Double.valueOf(df.format((Double) obj[0])));
					dto.setForecastBoed(
							ServicesUtil.isEmpty(obj[1]) ? 0.00 : Double.valueOf(df.format((Double) obj[1])));
					dto.setProjectedBoed(
							ServicesUtil.isEmpty(obj[2]) ? 0.00 : Double.valueOf(df.format((Double) obj[2])));
					dto.setLocation(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					dto.setLocationCode(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					dto.setLocationType(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
					dto.setTier("N/A");
					dto.setMaxValue(1000);
					if (!ServicesUtil.isEmpty(dto.getProjectedBoed()) && !ServicesUtil.isEmpty(dto.getForecastBoed())) {
						dto.setVariance(dto.getProjectedBoed() - dto.getForecastBoed());
						if (dto.getProjectedBoed() != 0 && dto.getForecastBoed() != 0) {
							dto.setVariancePercent(dto.getVariance() / dto.getForecastBoed() * 100);
							dto.setVariancePercent(Double.valueOf(df.format(dto.getVariancePercent())));
						}
						dto.setVariance(Double.valueOf(df.format(dto.getVariance())));
					}
					dto.setProActive(ServicesUtil.isEmpty(obj[6]) ? false : ((Byte) obj[6] == 0 ? false : true));
					// dto.setProActive(ServicesUtil.isEmpty(obj[6])? false :
					// ("1".equals((String) obj[6]) ? true : false));
					if (MurphyConstant.WELL.equalsIgnoreCase(uiRequestDto.getLocationType())
							|| !uiRequestDto.isRolledUp()) {
						dto.setMuwi(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
						dto.setTier(ServicesUtil.isEmpty(obj[8]) ? null : (String) obj[8]);
						dto.setInvestigationProcessId(ServicesUtil.isEmpty(obj[9]) ? null : (String) obj[9]);
						dto.setInvestigationTaskId(ServicesUtil.isEmpty(obj[10]) ? null : (String) obj[10]);
						dto.setHasInvestigation(!ServicesUtil.isEmpty(dto.getInvestigationProcessId()) ? true : false);
						dto.setDispatchTaskId(ServicesUtil.isEmpty(obj[11]) ? null : (String) obj[11]);
						dto.setDispatch(!ServicesUtil.isEmpty(dto.getDispatchTaskId()) ? true : false);
						dto.setInquiryTaskId(ServicesUtil.isEmpty(obj[12]) ? null : (String) obj[12]);
						dto.setHasInquiry(!ServicesUtil.isEmpty(dto.getInquiryTaskId()) ? true : false);
						if (ServicesUtil.isEmpty(dto.getInvestigationTaskId())) {
							dto.setInvestigationProcessId(ServicesUtil.isEmpty(obj[13]) ? null : (String) obj[13]);
							dto.setInvestigationTaskId(ServicesUtil.isEmpty(obj[14]) ? null : (String) obj[14]);
						}
						dto.setAcknowledge(false);
						if (!ServicesUtil.isEmpty(thresholdList)) {
							for (ThresholdDto tres : thresholdList) {
								if (dto.getVariancePercent() >= tres.getMinimumValue()
										&& dto.getVariancePercent() <= tres.getMaximumValue()
										&& tres.getTier().equals(dto.getTier())) {
									dto.setAlarmSeverity(tres.getCondition());
									break;
								}
							}
						}
					}
					result.add(dto);
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][ProductionVarianceDao][fetchVarianceDataForWells][error]" + e.getMessage()
					+ "\n[query] " + queryString);
		}
		return result;
	}

	public String getLocationQuery(String locationCode, String locationType) {
		String locQuery = "";
		return locQuery;
	}

	public String dopQuery(List<String> locCodes, String locType, int duration, String userType, boolean isRolledUp) {
		// String varianceType = "";
		Date endDateTime;
		// Date currentDateTime = ServicesUtil.convertFromZoneToZone(null, "",
		// MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE, "",
		// MurphyConstant.DATE_DB_FORMATE_SD) ;
		Date currentDateTime = ServicesUtil.getDateWithInterval(new Date(), -300, MurphyConstant.MINUTES);
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(currentDateTime);
		calStart.set(Calendar.SECOND, 00);
		calStart.set(Calendar.MINUTE, 00);
		currentDateTime = calStart.getTime();

		logger.error("[Murphy][ProductionVarianceDao][currentDateTime][  ]" + currentDateTime);

		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(currentDateTime);
		if (calEnd.get(Calendar.HOUR_OF_DAY) >= 6) {
			calEnd.add(Calendar.DAY_OF_MONTH, 01);
		}
		calEnd.set(Calendar.HOUR_OF_DAY, 07);
		calEnd.set(Calendar.SECOND, 00);
		calEnd.set(Calendar.MINUTE, 00);
		endDateTime = calEnd.getTime();

		logger.error("[Murphy][ProductionVarianceDao][endDateTime][  ]" + endDateTime);

		String currentDateTimeString = ServicesUtil.convertFromZoneToZoneString(currentDateTime, "", "", "",
				MurphyConstant.DATE_DB_FORMATE_SD, MurphyConstant.DATE_DB_FORMATE_SD);
		logger.error("[Murphy][ProductionVarianceDao][currentDateTimeString][  ]" + currentDateTimeString);

		String endDateTimeString = ServicesUtil.convertFromZoneToZoneString(endDateTime, "", "", "",
				MurphyConstant.DATE_DB_FORMATE_SD, MurphyConstant.DATE_DB_FORMATE_SD);

		logger.error("[Murphy][ProductionVarianceDao][endDateTimeString][  ]" + endDateTimeString);

		// if (duration == 1) {
		// varianceType = MurphyConstant.DAILY;
		// } else {
		// // endDateTime = getMeNextMonday();
		// varianceType = MurphyConstant.WEEKLY;
		// }

		String locQuery = getQueryMuwiByLocTypeAndCode(locType, locCodes) + " = loc.location_code ";
		String commonQueryForValues = "";

		if (isRolledUp) {
			commonQueryForValues = "select sum(st.data_value) from  TM_PRODUCTION_VARIANCE_STAGING st where "
					// + "st.variance_type = '"
					// + varianceType + "' " + " and"
					+ "st.muwi_id in (" + locQuery + ") and st.source = '";
		} else {
			commonQueryForValues = " select max(st.data_value) from TM_PRODUCTION_VARIANCE_STAGING st where "
					// + "st.variance_type = 'Daily' and "
					+ "st.muwi_id = ins.muwi and st.source = '";

			if (!MurphyConstant.WELL.equalsIgnoreCase(locType)) {
				locCodes = locDao.getLocCodeByLocationTypeAndCode(locType, locCodes);
			}
		}
		String forecastQuery = commonQueryForValues + MurphyConstant.DOP_FORECAST
				+ "' and st.created_at = to_timestamp('" + endDateTimeString + "','yyyy-mm-dd hh24:mi:ss')";
		logger.error("[Murphy][ProductionVarianceDao][DOP QUERY][ forecastQuery ]" + forecastQuery);

		String projectedQuery = commonQueryForValues + MurphyConstant.DOP_PROJECTED
				+ "' and st.created_at = to_timestamp('" + endDateTimeString + "','yyyy-mm-dd hh24:mi:ss')";
		logger.error("[Murphy][ProductionVarianceDao][DOP QUERY][  projectedQuery]" + projectedQuery);
		String actualQuery = commonQueryForValues + MurphyConstant.DOP_CANARY + "' and st.created_at = to_timestamp('"
				+ currentDateTimeString + "','yyyy-mm-dd hh24:mi:ss')";
		String isProActive = "(select distinct ins.is_proactive from  tm_pwhopper_well_insts ins where ins.location_code  = loc.location_code )";
		String queryStringStart = "Select distinct (" + actualQuery + ") as actual_value , (" + forecastQuery
				+ ") as forecast_value , (" + projectedQuery + ") as projected_value ,"
				+ "loc.location_text as location , loc.location_code as loc_code , loc.LOCATION_TYPE as loc_type , "
				+ isProActive + " as isProActive ";
		String queryStringMid = " from production_location loc ";
		String queryStringEnd = " where loc.location_code in (" + ServicesUtil.getStringFromList(locCodes) + ")";

		String queryString = "";
		if (MurphyConstant.WELL.equalsIgnoreCase(locType) || !isRolledUp) {

			String investigationProcessId = "select max(f.process_id) from  tm_proc_evnts as e left outer join  tm_task_evnts as f on f.process_id = e.process_id "
					+ "where f.origin in ('" + MurphyConstant.INVESTIGATON + "') and f.status <> '"
					+ MurphyConstant.COMPLETE + "' and e.loc_code = loc.location_code";

			String investigationTaskId = "select max(f.task_id) from  tm_proc_evnts as e left outer join  tm_task_evnts as f on f.process_id = e.process_id "
					+ "where f.origin in ('" + MurphyConstant.INVESTIGATON + "') and f.status <> '"
					+ MurphyConstant.COMPLETE + "' and e.loc_code = loc.location_code";
			String getLatestCreatedAtVariance = "(select max(f1.created_at) from  tm_proc_evnts as e1 left outer join  tm_task_evnts as f1 on "
					+ "f1.process_id = e1.process_id where f1.parent_origin in ('" + MurphyConstant.VARIANCE + "') "
					+ "and f1.status <> '" + MurphyConstant.COMPLETE + "' and e1.loc_code = loc.location_code)";
			String varianceTaskId = "select max(f.task_id) from  tm_proc_evnts as e left outer join  tm_task_evnts as f on f.process_id = e.process_id where "
					+ "f.parent_origin in ('" + MurphyConstant.VARIANCE + "') and f.status <> '"
					+ MurphyConstant.COMPLETE + "' and e.loc_code = loc.location_code  " + "and f.created_at = "
					+ getLatestCreatedAtVariance;
			String inquiryTaskId = "select max(f.task_id) from  tm_proc_evnts as e left outer join  tm_task_evnts as f on f.process_id = e.process_id "
					+ "where f.origin in ('" + MurphyConstant.INQUIRY + "') and f.status <> '" + MurphyConstant.COMPLETE
					+ "' and e.loc_code = loc.location_code and e.USER_GROUP like '%" + userType + "%'";
			String processIdProActive = "select max(ins.INVESTIGATION_ID) from  tm_pwhopper_well_insts ins where ins.location_code  = loc.location_code ";
			String taskIdProActive = "select max(te.task_id) from  tm_task_evnts te where te.process_id = ("
					+ processIdProActive + " )"
					+ " and te.created_at = (select max(tr.created_At) from  tm_task_evnts tr where tr.process_id = ("
					+ processIdProActive + "))";

			queryStringStart = queryStringStart + " ,  ins.muwi as muwi , wt.tier as tier  , (" + investigationProcessId
					+ ") as invest_process_id ," + " (" + investigationTaskId + " ) as invest_task_id  , ("
					+ varianceTaskId + ") as variance_task_id ," + " (" + inquiryTaskId + " ) as inquiry_task_id  , ("
					+ processIdProActive + ")  as processIdProActive " + ", (" + taskIdProActive
					+ ") as taskIdProActive" + queryStringMid
					+ "  left outer join well_muwi ins on ins.location_Code = loc.location_code "
					+ " left outer join well_tier wt on loc.location_code = wt.location_code ";
		} else {
			queryStringStart = queryStringStart + queryStringMid;
		}

		queryString = queryStringStart + queryStringEnd;

		logger.error("[Murphy][ProductionVarianceDao][DOP QUERY][  ]" + queryString);
		return queryString;
	}

	@SuppressWarnings({ "unchecked" })
	public List<DopDummyDto> fetchVarianceData() {

		DopDummyDto responseDto = null;
		UIRequestDto uiRequestDto = new UIRequestDto();
		List<DopDummyDto> responseList = new ArrayList<DopDummyDto>();
		List<String> locCodes = new ArrayList<String>();
		List<DOPVarianceDto> dopVarianceDto = new ArrayList<DOPVarianceDto>();

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		try {

			String startQueryString = "Select  wm.muwi , p1.location_code , wt.tier , st.data_value , st.created_at , st.param_type "
					+ "from production_location p1 join well_muwi wm on wm.location_code=p1.location_code join well_tier wt "
					+ "on wm.location_code = wt.location_code join tm_canary_staging_table st "
					+ " on wm.muwi = st.muwi_id where st.data_value is not null and  st.param_type ='QTOILD' and "
					+ "((wt.tier='Tier A' and st.data_value >='10.00')"
					+ "or (wt.tier = 'Tier B' and st.data_value  >='2.00')"
					+ "or (wt.tier = 'Tier C' and st.data_value  >='1.00')) and st.created_at >=  TO_TIMESTAMP('2019-08-13 07:00:00','yyyy-MM-dd HH24:mi:ss')"
					+ " and  st.created_at <= ADD_SECONDS(TO_TIMESTAMP ('2019-08-13 07:00:00','yyyy-MM-dd HH24:mi:ss'), 60*60) order by st.created_at desc";

			Query q = this.getSession().createSQLQuery(startQueryString);
			logger.error("Fetch Query is: " + q);
			List<Object[]> objList = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(objList)) {
				for (Object[] obj : objList) {
					responseDto = new DopDummyDto();
					responseDto.setMuwi(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					responseDto.setLocationCode(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					responseDto.setTier(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					responseDto.setDataValue(ServicesUtil.isEmpty(obj[3]) ? null : (Double) obj[3]);
					responseDto.setCreatedAt(ServicesUtil.isEmpty(obj[4]) ? null : (Date) obj[4]);
					responseDto.setParamType(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
					responseList.add(responseDto);
					locCodes.add(responseDto.getLocationCode());

				}
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseDto.setResponseMessage(responseMessage);

			// String queryString = "";

			uiRequestDto.setLocationCodeList(locCodes);
			uiRequestDto.setDuration(7);
			uiRequestDto.setLocationType("Well");
			uiRequestDto.setRolledUp(true);
			uiRequestDto.setUserType("ROC");
			dopVarianceDto = fetchVarianceDataForWells(uiRequestDto);

			for (DOPVarianceDto dto : dopVarianceDto) {
				String muwi = dto.getMuwi();
				double varPercent = dto.getVariancePercent();
				String locCode = dto.getLocationCode();
				logger.error("[Murphy][VarianceFacade][fetchVarianceData][error]" + locCode + "for the well is:  "
						+ muwi + "and variance percent is:  " + varPercent);

			}

		} catch (Exception e) {
			logger.error("[Murphy][VarianceFacade][fetchVarianceData][error]" + e.getMessage());
		}
		return responseList;

	}

	// ITA-DOP Task creation
	@javax.transaction.Transactional(value = TxType.REQUIRES_NEW)
	@SuppressWarnings({ "unchecked" })
	public ResponseMessage createItaTaskDop(String runtime, String Tier, double barrels, String classification,
			String subClassification, String STSSV_Rules, double deviation, String taskType,
			HashMap<String, String> muwiSTSSVMap) {

		List<String> locCodes = new ArrayList<String>();
		List<DOPVarianceDto> dopVarianceDto = new ArrayList<DOPVarianceDto>();
		boolean create = false;

		ResponseMessage responseMessage = new ResponseMessage();
		try {
			String QueryStringTier = "Select  distinct st.muwi_id , p1.location_code , wt.tier , st.data_value , st.created_at"
					+ " from TM_PRODUCTION_VARIANCE_STAGING st join well_muwi wm on wm.muwi = st.muwi_id join"
					+ " production_location p1 on wm.location_code=p1.location_code join well_tier wt"
					+ " on wm.location_code = wt.location_code where st.param_type ='QTOILD' and" + " wt.tier= '" + Tier
					+ "' and st.data_value >= '" + barrels + "'" + " and st.source = 'Actual (Canary)'"
					+ " and st.created_at = TO_TIMESTAMP ('" + runtime
					+ "','yyyy-MM-dd HH24:mi:ss') order by st.created_at";

			Query q = this.getSession().createSQLQuery(QueryStringTier);
			logger.error("Fetch Query is: " + q);
			List<Object[]> objList = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(objList)) {
				for (Object[] obj : objList) {
					locCodes.add(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
				}
			}

			if (!ServicesUtil.isEmpty(locCodes)) {
				logger.error("[ProductionVarianceDao][location code not empty]");
				dopVarianceDto = calculateVariance(runtime, locCodes);
			}

			if (!ServicesUtil.isEmpty(dopVarianceDto)) {
				for (DOPVarianceDto dto : dopVarianceDto) {
					double varPercent = dto.getVariancePercent();
					String locCode = dto.getLocationCode();
					String muwi = dto.getMuwi();
					logger.error("STSSV_Rules : " + STSSV_Rules);
					/*
					 * if (muwi.equalsIgnoreCase("9264231100000000BAR0030H1"))
					 * varPercent = -10.00;
					 */
					if (!ServicesUtil.isEmpty(STSSV_Rules) && !ServicesUtil.isEmpty(muwiSTSSVMap)) {
						String STSSV_value_map = muwiSTSSVMap.get(muwi);
						// logger.error("STSSV_value_map : "+ STSSV_value_map);
						// If value from rules matches with Map, create
						if (STSSV_value_map.equalsIgnoreCase(STSSV_Rules)) {
							create = true;
						} else {
							if (STSSV_value_map.equalsIgnoreCase("Applicable")) {
								create = true;
							} else
								create = false;
						}
					} else {
						create = true;
					}
					
					//CHG0037926 - ITA well intervention do not create new task if a task exists
					if(itaExists(locCode, classification, subClassification)){
						create = false;
					}
					
					logger.error("[Murphy][ProductionVarianceDao][fetchVarianceData][create] : " + create);
					logger.error("[Murphy][ProductionVarianceDao][fetchVarianceData][varPercent] :" + varPercent);
					logger.error("[Murphy][ProductionVarianceDao][fetchVarianceData][deviation] : " + deviation);
					if (create) {
						if (varPercent <= deviation) {
							logger.error("[Murphy][ProductionVarianceDao][fetchVarianceData][create task]");
							responseMessage = itaDopDao.createTask(locCode, classification, subClassification,
									taskType);
							logger.error("[Murphy][ProductionVarianceDao][fetchVarianceData][locCode] " + locCode);
						}
					} else {
						responseMessage.setStatus(MurphyConstant.SUCCESS);
						responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
						responseMessage.setMessage("No record eligible for DOP task creation");
					}
				}
			} else {
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				responseMessage.setMessage("Location code is empty");
				logger.error("[ProductionVarianceDao][location code is empty]");
			}

		} catch (Exception e) {
			logger.error("[Murphy][ProductionVarianceDao][createItaTaskDop][error]" + e.getMessage());
			responseMessage.setMessage(MurphyConstant.READ_FAILURE);
			responseMessage.setStatus(MurphyConstant.FAILURE);
			responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		}
		return responseMessage;

	}
	
	//This method checks if an ITA-DOP exists: CHG0037926 - ITA well intervention do not create new task if a task exists
	private boolean itaExists(String locCode, String classification, String subClassification) {
		int durationInDays = Integer.parseInt(configdao.getConfigurationByRef("ITA_DOP_BACKLOG_DURATION_DAYS"));
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, - durationInDays);

		String classificationQuery = "select TASK_ID from TM_ATTR_INSTS where INS_VALUE = '" + subClassification
				+ "' and ATTR_TEMP_ID = '123456'" + " and TASK_ID in ("
				+ " select i.task_id from TM_ATTR_INSTS i inner join tm_task_evnts t on i.task_id = t.task_id"
				+ " where INS_VALUE = '" + classification + "' and i.ATTR_TEMP_ID = '12345')";

		// Query to check if an active ITA created on the same day exists on the location or not that is not COMPLETED or REJECTED
		String queryNotCompleted = "select top 1 te.task_id from TM_TASK_EVNTS te,TM_PROC_EVNTS pe"
				+ " where pe.PROCESS_ID = te.PROCESS_ID and pe.loc_code = '" + locCode + "' and te.parent_origin = '"
				+ MurphyConstant.P_ITA_DOP + "'" + " and te.status not in ('" + MurphyConstant.COMPLETE + "','"
				+ MurphyConstant.REJECTED + "')" + " and te.TASK_ID in (" + classificationQuery + ")"
				+ " and to_date(te.CREATED_AT) >= '" + sdf.format(cal.getTime()) +"'";
		
		 logger.error("[Murphy][ProductionVarianceDao][itaExists][queryNotCompleted]" + queryNotCompleted);
		  Object obj1 = this.getSession().createSQLQuery(queryNotCompleted).uniqueResult();
		if (!ServicesUtil.isEmpty(obj1)) {

			logger.error("[Murphy][ProductionVarianceDao][itaExists][task_id_NotCompleted]  " + obj1.toString());
			if (!ServicesUtil.isEmpty(obj1.toString())) {
				return true;
			}
		}

		return false;
	}

	// ITA-DOP calculate variance percentage
	public List<DOPVarianceDto> calculateVariance(String runtime, List<String> locCodes) {
		List<DOPVarianceDto> result = new ArrayList<DOPVarianceDto>();
		String queryString = null;
		try {
			DecimalFormat df = new DecimalFormat("#.##");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 1);
			String nextDay = sdf.format(cal.getTime());
			String nextDayTime = nextDay + " 07:00:00";
			logger.error("nextDayTime : " + nextDayTime);
			// nextDayTime = "2019-11-19 07:00:00";

			queryString = "Select distinct (select max(st.data_value) from TM_PRODUCTION_VARIANCE_STAGING st where"
					+ " st.muwi_id = ins.muwi and st.source = 'Actual (Canary)' and st.param_type ='QTOILD'and"
					+ " st.created_at = TO_TIMESTAMP ('" + runtime + "','yyyy-MM-dd HH24:mi:ss')) as ACTUAL_VALUE,"
					+ " (select max(st.data_value) from TM_PRODUCTION_VARIANCE_STAGING st where st.muwi_id = ins.muwi"
					+ " and st.source = 'Forecast (Enersight)' and st.param_type ='QTOILD' and"
					+ " st.created_at = TO_TIMESTAMP ('" + nextDayTime
					+ "','yyyy-MM-dd HH24:mi:ss')) as FORECAST_VALUE ,"
					+ " (select max(st.data_value) from TM_PRODUCTION_VARIANCE_STAGING st where st.muwi_id = ins.muwi"
					+ " and st.source = 'Projected (HANA)'  and st.param_type ='QTOILD' and"
					+ " st.created_at = TO_TIMESTAMP ('" + nextDayTime
					+ "','yyyy-MM-dd HH24:mi:ss')) as PROJECTED_VALUE,"
					+ " loc.location_text as location , loc.location_code as loc_code , loc.LOCATION_TYPE as loc_type,"
					+ " ins.muwi as muwi , wt.tier as tier"
					+ " from production_location loc left outer join well_muwi ins on ins.location_Code = loc.location_code"
					+ " left outer join well_tier wt on loc.location_code = wt.location_code  where"
					+ " loc.location_code in (" + ServicesUtil.getStringFromList(locCodes) + ")";

			logger.error("calculateVariance Query is: " + queryString);
			Query q = this.getSession().createSQLQuery(queryString);

			List<Object[]> objList = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(objList)) {
				for (Object[] obj : objList) {
					DOPVarianceDto dto = new DOPVarianceDto();
					dto.setActualBoed(ServicesUtil.isEmpty(obj[0]) ? 0.00 : Double.valueOf(df.format((Double) obj[0])));
					dto.setForecastBoed(
							ServicesUtil.isEmpty(obj[1]) ? 0.00 : Double.valueOf(df.format((Double) obj[1])));
					dto.setProjectedBoed(
							ServicesUtil.isEmpty(obj[2]) ? 0.00 : Double.valueOf(df.format((Double) obj[2])));
					dto.setLocation(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					dto.setLocationCode(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					dto.setLocationType(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
					dto.setMuwi(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
					dto.setTier(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
					if (!ServicesUtil.isEmpty(dto.getProjectedBoed()) && !ServicesUtil.isEmpty(dto.getForecastBoed())) {
						dto.setVariance(dto.getProjectedBoed() - dto.getForecastBoed());
						if (dto.getProjectedBoed() != 0 && dto.getForecastBoed() != 0) {
							dto.setVariancePercent(dto.getVariance() / dto.getForecastBoed() * 100);
							dto.setVariancePercent(Double.valueOf(df.format(dto.getVariancePercent())));
						}
						dto.setVariance(Double.valueOf(df.format(dto.getVariance())));
					}
					result.add(dto);
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][ProductionVarianceDao][calculateVariance][error] " + e.getMessage());
		}
		return result;
	}

//	@SuppressWarnings("unchecked")
//	public List<ProductionVarianceDto> fetchTrendsDataForDOP(String muwi) throws Exception {
//		List<ProductionVarianceDto> productionVarienceDtoList = null;
//		String queryString = null;
//		try {
//			queryString = "SELECT * FROM TM_PRODUCTION_VARIANCE_STAGING WHERE MUWI_ID='" + muwi + "'";
//			Query q = this.getSession().createSQLQuery(queryString);
//			List<Object[]> objList = (List<Object[]>) q.list();
//			if (!ServicesUtil.isEmpty(objList)) {
//				productionVarienceDtoList = new ArrayList<>();
//				for (Object[] obj : objList) {
//					ProductionVarianceDto dto = new ProductionVarianceDto();
//					dto.setCreatedAt(ServicesUtil.isEmpty(obj[0]) ? null : (Date) obj[0]);
//					dto.setMuwi(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
//					dto.setParamType(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
//					dto.setSource(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
//					dto.setDataValue(ServicesUtil.isEmpty(obj[4]) ? 0.0 : (double) obj[4]);
//					productionVarienceDtoList.add(dto);
//				}
//				return productionVarienceDtoList;
//			}
//			
//		} catch (Exception e) {
//			logger.error("[Murphy][ProductionVarianceDao][fetchTrendsDataForDOP][error] " + e.getMessage());
//			e.printStackTrace();
//			throw e;
//		}
//
//		return productionVarienceDtoList;
//
//	}
	
	
	//For setting DGP_DOP query
	public String dgpQueryForOtherDetailsOriginal(List<String> locCodes, String locType, int duration, String userType, boolean isRolledUp) {
		String isProActive = "(select  ins.is_proactive from  tm_pwhopper_well_insts ins where ins.location_code  = loc.location_code )";
		String queryStringStart = "Select distinct loc.location_text as location , loc.location_code as loc_code , loc.LOCATION_TYPE as loc_type , "
				+ isProActive + " as isProActive ";
		String queryStringMid = " from production_location loc ";
		
		if (!MurphyConstant.WELL.equalsIgnoreCase(locType)) {
			locCodes = locDao.getLocCodeByLocationTypeAndCode(locType, locCodes);
		}
		
		String queryStringEnd = " where loc.location_code in (" + ServicesUtil.getStringFromList(locCodes) + ")";

		String queryString = "";
		if (!isRolledUp) {

			String investigationProcessId = "select max(f.process_id) from  task_proc_join as f "
					+ "where f.origin in ('" + MurphyConstant.INVESTIGATON + "') and f.status <> '"
					+ MurphyConstant.COMPLETE + "' and f.loc_code = loc.location_code";

			String investigationTaskId = "select max(f.task_id) from  task_proc_join as f "
					+ "where f.origin in ('" + MurphyConstant.INVESTIGATON + "') and f.status <> '"
					+ MurphyConstant.COMPLETE + "' and f.loc_code = loc.location_code";
			String getLatestCreatedAtVariance = "(select max(f1.created_at) from  task_proc_join as f1 "
					+ "where f1.parent_origin in ('" + MurphyConstant.VARIANCE + "') "
					+ "and f1.status <> '" + MurphyConstant.COMPLETE + "' and f1.loc_code = loc.location_code)";
			String varianceTaskId = "select max(f.task_id) from  task_proc_join as f where "
					+ "f.parent_origin in ('" + MurphyConstant.VARIANCE + "') and f.status <> '"
					+ MurphyConstant.COMPLETE + "' and f.loc_code = loc.location_code  " + "and f.created_at = "
					+ getLatestCreatedAtVariance;
			String inquiryTaskId = "select max(f.task_id) from  task_proc_join as f "
					+ "where f.origin in ('" + MurphyConstant.INQUIRY + "') and f.status <> '" + MurphyConstant.COMPLETE
					+ "' and f.loc_code = loc.location_code and f.USER_GROUP like '%" + userType + "%'";
			String processIdProActive = "select max(ins.INVESTIGATION_ID) from  tm_pwhopper_well_insts ins where ins.location_code  = loc.location_code ";
			String taskIdProActive = "select max(te.task_id) from  tm_task_evnts te where te.process_id = ("
					+ processIdProActive + " )"
					+ " and te.created_at = (select max(tr.created_At) from  tm_task_evnts tr where tr.process_id = ("
					+ processIdProActive + "))";

			queryStringStart = queryStringStart + " ,  ins.muwi as muwi , wt.tier as tier  , (" + investigationProcessId
					+ ") as invest_process_id ," + " (" + investigationTaskId + " ) as invest_task_id  , ("
					+ varianceTaskId + ") as variance_task_id ," + " (" + inquiryTaskId + " ) as inquiry_task_id  , ("
					+ processIdProActive + ")  as processIdProActive " + ", (" + taskIdProActive
					+ ") as taskIdProActive" + queryStringMid
					+ "  left outer join well_muwi ins on ins.location_Code = loc.location_code "
					+ " left outer join well_tier wt on loc.location_code = wt.location_code ";
		} else{
			queryStringStart = queryStringStart + queryStringMid;
		}
		queryString = queryStringStart + queryStringEnd;

		//logger.error("[Murphy][ProductionVarianceDao][DGP QUERY][  ]" + queryString);
		return queryString;
	}

	@SuppressWarnings("unchecked")
	public List<DOPVarianceDto> setWellDataForDGPDOPoriginal(String dgpQuery,List<DOPVarianceDto> dgpDataList, UIRequestDto uiRequestDto,
			Map<String,DOPVarianceDto> mapDgpDataList){
		try{
			Query q = this.getSession().createSQLQuery(dgpQuery);
			List<Object[]> objList = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(objList)) {
				List<ThresholdDto> thresholdList = getThreshold();
				DOPVarianceDto dto =null;
				for (Object[] obj : objList) {
					String muwi = ServicesUtil.isEmpty(obj[4])? null : (String)obj[4];
					dto = mapDgpDataList.get(muwi);
					if(!ServicesUtil.isEmpty(dto) && dto.getMuwi().equalsIgnoreCase(muwi)){
						dto.setAcknowledge(false);
						dto.setLocation(ServicesUtil.isEmpty(obj[0]) ? null : (String)obj[0]);
						dto.setLocationCode(ServicesUtil.isEmpty(obj[1]) ? null : (String)obj[1]);
						dto.setLocationType(ServicesUtil.isEmpty(obj[2]) ? null : (String)obj[2]);
						dto.setProActive(ServicesUtil.isEmpty(obj[3]) ? false : ((Byte) obj[3] == 0 ? false : true));
						dto.setTier(ServicesUtil.isEmpty(obj[5]) ? "N/A" : (String) obj[5]);
						dto.setMaxValue(1000);
						if(!uiRequestDto.isRolledUp()){
							dto.setInvestigationProcessId(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
							dto.setInvestigationTaskId(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
							dto.setHasInvestigation(!ServicesUtil.isEmpty(dto.getInvestigationProcessId()) ? true : false);
							dto.setDispatchTaskId(ServicesUtil.isEmpty(obj[8]) ? null : (String) obj[8]);
							dto.setDispatch(!ServicesUtil.isEmpty(dto.getDispatchTaskId()) ? true : false);
							if(uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE)){
								dto.setInquiryTaskId(ServicesUtil.isEmpty(obj[9]) ? null : (String) obj[9]);
								dto.setHasInquiry(!ServicesUtil.isEmpty(dto.getInquiryTaskId()) ? true : false);
							}
							if (ServicesUtil.isEmpty(dto.getInvestigationTaskId())) {
								dto.setInvestigationProcessId(ServicesUtil.isEmpty(obj[10]) ? null : (String) obj[10]);
								dto.setInvestigationTaskId(ServicesUtil.isEmpty(obj[11]) ? null : (String) obj[11]);
							}
							if (!ServicesUtil.isEmpty(thresholdList)) {
								for (ThresholdDto tres : thresholdList) {
									if (dto.getVariancePercent() >= tres.getMinimumValue()
										&& dto.getVariancePercent() <= tres.getMaximumValue()
										&& tres.getTier().equals(dto.getTier())) {
											dto.setAlarmSeverity(tres.getCondition());
											break;
									}
								}
							}
						}
						dgpDataList.add(dto);	
					}
				}
			}
		}catch(Exception e){
			logger.error("[Murphy][ProductionVarianceDao][fetchTrendsDataForDOP][error] " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return dgpDataList;
	}
	
	//added to resolve query performance issue 
	public String dgpQueryForOtherDetails(List<String> locCodes, String locType, int duration, String userType, boolean isRolledUp) {
		String isProActive = "(select  ins.is_proactive from  tm_pwhopper_well_insts ins where ins.location_code  = loc.location_code )";
		String queryStringStart = "Select distinct loc.location_text as location , loc.location_code as loc_code , loc.LOCATION_TYPE as loc_type , "
				+ isProActive + " as isProActive ";
		String queryStringMid = " from production_location loc ";
		
		if (!MurphyConstant.WELL.equalsIgnoreCase(locType)) {
			locCodes = locDao.getLocCodeByLocationTypeAndCode(locType, locCodes);
		}
		
		String queryStringEnd = " where loc.location_code in (" + ServicesUtil.getStringFromList(locCodes) + ")";

		String queryString = "";
		if (!isRolledUp) {

			String investigationProcessId = "select max(f.process_id) from  task_proc_join as f "
					+ "where f.origin in ('" + MurphyConstant.INVESTIGATON + "')"
					//+ " and f.status <> '"+ MurphyConstant.COMPLETE + "' "
					+ " and f.loc_code = loc.location_code";

			String investigationTaskId = "select max(f.task_id) from  task_proc_join as f "
					+ "where f.origin in ('" + MurphyConstant.INVESTIGATON + "') "
					//+ "and f.status <> '"+ MurphyConstant.COMPLETE + "' "
					+ " and f.loc_code = loc.location_code";
			String getLatestCreatedAtVariance = "(select max(f1.created_at) from  task_proc_join as f1 "
					+ "where f1.parent_origin in ('" + MurphyConstant.VARIANCE + "') "
					//+ "and f1.status <> '" + MurphyConstant.COMPLETE + "' "
					+ " and f1.loc_code = loc.location_code)";
			String varianceTaskId = "select max(f.task_id) from  task_proc_join as f where "
					+ "f.parent_origin in ('" + MurphyConstant.VARIANCE + "') "
					//+ "and f.status <> '"+ MurphyConstant.COMPLETE + "' "
					+ " and f.loc_code = loc.location_code  " + "and f.created_at = "
					+ getLatestCreatedAtVariance;
			String inquiryTaskId = "select max(f.task_id) from  task_proc_join as f "
					+ "where f.origin in ('" + MurphyConstant.INQUIRY + "') "
					//+ "and f.status <> '" + MurphyConstant.COMPLETE+ "' "
					+ " and f.loc_code = loc.location_code and f.USER_GROUP like '%" + userType + "%'";
			String processIdProActive = "select max(ins.INVESTIGATION_ID) from  tm_pwhopper_well_insts ins where ins.location_code  = loc.location_code ";
			String taskIdProActive = "select max(te.task_id) from  tm_task_evnts te where te.process_id = ("
					+ processIdProActive + " )"
					+ " and te.created_at = (select max(tr.created_At) from  tm_task_evnts tr where tr.process_id = ("
					+ processIdProActive + "))";

			queryStringStart = queryStringStart + " ,  ins.muwi as muwi , wt.tier as tier  , (" + investigationProcessId
					+ ") as invest_process_id ," + " (" + investigationTaskId + " ) as invest_task_id  , ("
					+ varianceTaskId + ") as variance_task_id ," + " (" + inquiryTaskId + " ) as inquiry_task_id  , ("
					+ processIdProActive + ")  as processIdProActive " + //", (" + taskIdProActive
					//+ ") as taskIdProActive" +
					queryStringMid
					+ "  left outer join well_muwi ins on ins.location_Code = loc.location_code "
					+ " left outer join well_tier wt on loc.location_code = wt.location_code ";
		} else{
			queryStringStart = queryStringStart + queryStringMid;
		}
		queryString = queryStringStart + queryStringEnd;

		logger.error("[Murphy][ProductionVarianceDao][DGP QUERY][  ]" + queryString);
		return queryString;
	}

	@SuppressWarnings("unchecked")
	public List<DOPVarianceDto> setWellDataForDGPDOP(String dgpQuery,List<DOPVarianceDto> dgpDataList, UIRequestDto uiRequestDto,
			Map<String,DOPVarianceDto> mapDgpDataList){
		try{
			Timestamp dopqueryexecutionstartoff= new Timestamp(System.currentTimeMillis());
			 logger.error("dopqueryexecutionstartoff : " +dopqueryexecutionstartoff);
			Query q = this.getSession().createSQLQuery(dgpQuery);
			List<Object[]> objList = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(objList)) {
				List<ThresholdDto> thresholdList = getThreshold();
				DOPVarianceDto dto =null;
				for (Object[] obj : objList) {
					String muwi = ServicesUtil.isEmpty(obj[4])? null : (String)obj[4];
					dto = mapDgpDataList.get(muwi);
					if(!ServicesUtil.isEmpty(dto) && dto.getMuwi().equalsIgnoreCase(muwi)){
						dto.setAcknowledge(false);
						dto.setLocation(ServicesUtil.isEmpty(obj[0]) ? null : (String)obj[0]);
						dto.setLocationCode(ServicesUtil.isEmpty(obj[1]) ? null : (String)obj[1]);
						dto.setLocationType(ServicesUtil.isEmpty(obj[2]) ? null : (String)obj[2]);
						dto.setProActive(ServicesUtil.isEmpty(obj[3]) ? false : ((Byte) obj[3] == 0 ? false : true));
						dto.setTier(ServicesUtil.isEmpty(obj[5]) ? "N/A" : (String) obj[5]);
						dto.setMaxValue(1000);
						if(!uiRequestDto.isRolledUp()){
							dto.setInvestigationProcessId(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
							dto.setInvestigationTaskId(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
						
							dto.setDispatchTaskId(ServicesUtil.isEmpty(obj[8]) ? null : (String) obj[8]);
							dto.setDispatch(!ServicesUtil.isEmpty(dto.getDispatchTaskId()) ? true : false);
							if(uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE)){
								dto.setInquiryTaskId(ServicesUtil.isEmpty(obj[9]) ? null : (String) obj[9]);
								dto.setHasInquiry(!ServicesUtil.isEmpty(dto.getInquiryTaskId()) ? true : false);
							}
							if (ServicesUtil.isEmpty(dto.getInvestigationTaskId())) {
								dto.setInvestigationProcessId(ServicesUtil.isEmpty(obj[10]) ? null : (String) obj[10]);
								//dto.setInvestigationTaskId(ServicesUtil.isEmpty(obj[11]) ? null : (String) obj[11]);
							}
							
							dto.setHasInvestigation(!ServicesUtil.isEmpty(dto.getInvestigationProcessId()) ? true : false);
							if (!ServicesUtil.isEmpty(thresholdList)) {
								for (ThresholdDto tres : thresholdList) {
									if (dto.getVariancePercent() >= tres.getMinimumValue()
										&& dto.getVariancePercent() <= tres.getMaximumValue()
										&& tres.getTier().equals(dto.getTier())) {
											dto.setAlarmSeverity(tres.getCondition());
											break;
									}
								}
							}
						}
						dgpDataList.add(dto);	
					}
				}
			}
			Timestamp dopqueryexecutionend= new Timestamp(System.currentTimeMillis());
			 logger.error("dopqueryexecutionend : " +dopqueryexecutionend);
		}catch(Exception e){
			logger.error("[Murphy][ProductionVarianceDao][fetchTrendsDataForDOP][error] " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return dgpDataList;
	}
	
	//added newly
	@SuppressWarnings("unchecked")
	public HashMap<String, Double> fetchActualDataFromHana(List<String> muwiList, String locCode,
			UIRequestDto uiRequestDto,String currentDate) {
		HashMap<String, Double> muwiActualsDataMap = new HashMap<String, Double>();
		String queryString = "";
		String tagName = "";
		String muwi="";
		Double dataValue = 0.0;
		
		try {
			String muwis= ServicesUtil.getStringFromList(muwiList);
			String muwiInQuery =ServicesUtil.getStringForInQuery(muwis);
			// EFS
			if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE)) {
				// EFS DOP
				if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID))
					tagName += MurphyConstant.CANARY_PARAM_PV[0];
				// EFS DGP
				else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID))
					tagName += MurphyConstant.CANARY_PARAM[3];
				
			}
			// Canada
			else if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE)) {
				// Kaybob DOP
				if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID))
					tagName += MurphyConstant.CANARY_CANADA_PARAM[0];
				
				else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID))
				{
				//Canada Gas
				String muwiId = "";
				if (!ServicesUtil.isEmpty(muwiList.get(0)))
					muwiId = muwiList.get(0);
				else if (muwiList.size() > 1)
					muwiId = muwiList.get(1);
				String facilityText = locDao.getFacilityDetailsforMuwi(muwiId).getFacility();
				if (facilityText.toLowerCase().contains(MurphyConstant.KAYBOB_LOC.toLowerCase()))
					tagName += MurphyConstant.CANARY_CANADA_PARAM[1];
				else if (facilityText.toLowerCase().contains(MurphyConstant.TUPPER_MAIN_LOC.toLowerCase()))
					tagName += MurphyConstant.CANARY_CANADA_PARAM[2];
				else if (facilityText.toLowerCase().contains(MurphyConstant.TUPPER_WEST_LOC.toLowerCase()))
					tagName += MurphyConstant.CANARY_CANADA_PARAM[3];
				}
			}
			logger.error("current date in test method for dop/dgp : " +currentDate);
			
			queryString = "SELECT MUWI_ID,DATA_VALUE from TM_PRODUCTION_VARIANCE_STAGING where SOURCE = '"+MurphyConstant.DOP_CANARY+"' "
					     + "and PARAM_TYPE= '"+tagName+"' and CREATED_AT = TO_TIMESTAMP('"+currentDate+"','yyyy-MM-dd HH:mi:ss') "
					     		+ "and MUWI_ID IN("+muwis+")";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> objList = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(objList)) {
				for(Object[] obj : objList)
				{
					if(!ServicesUtil.isEmpty(obj[0]))
					{
						muwi= (String) obj[0];
					}
					if(!ServicesUtil.isEmpty(obj[1]))
					{
						dataValue= (Double) obj[1];
					}
					
					muwiActualsDataMap.put(muwi, dataValue);
				}
			
			}
					
			
		} catch (Exception e) {
			
			logger.error("[ProductionVarianceDao][fetchActualDataFromHana][error] :: " +e.getMessage());

		}
		return muwiActualsDataMap;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Double> fetchForecastDataFromHana(List<String> muwiList, String locCode,
			UIRequestDto uiRequestDto,String currentDate) {
		HashMap<String, Double> muwiActualsDataMap = new HashMap<String, Double>();
		String queryString = "";
		String tagName = "";
		String muwi="";
		Double dataValue = 0.0;
		
		try {
			String muwis= ServicesUtil.getStringFromList(muwiList);
			String muwiInQuery =ServicesUtil.getStringForInQuery(muwis);
			// EFS
			if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE)) {
				// EFS DOP
				if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID))
					tagName += MurphyConstant.CANARY_PARAM_PV[0];
				// EFS DGP
				else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID))
					tagName += MurphyConstant.CANARY_PARAM[3];
				
			}
			// Canada
			else if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE)) {
				// Kaybob DOP
				if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID))
					tagName += MurphyConstant.CANARY_CANADA_PARAM[0];
				
				//Canada Gas
				else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID))
				{
				String muwiId = "";
				if (!ServicesUtil.isEmpty(muwiList.get(0)))
					muwiId = muwiList.get(0);
				else if (muwiList.size() > 1)
					muwiId = muwiList.get(1);
				String facilityText = locDao.getFacilityDetailsforMuwi(muwiId).getFacility();
				if (facilityText.toLowerCase().contains(MurphyConstant.KAYBOB_LOC.toLowerCase()))
					tagName += MurphyConstant.CANARY_CANADA_PARAM[1];
				else if (facilityText.toLowerCase().contains(MurphyConstant.TUPPER_MAIN_LOC.toLowerCase()))
					tagName += MurphyConstant.CANARY_CANADA_PARAM[2];
				else if (facilityText.toLowerCase().contains(MurphyConstant.TUPPER_WEST_LOC.toLowerCase()))
					tagName += MurphyConstant.CANARY_CANADA_PARAM[3];
				}
				
			}
			
			queryString = "SELECT MUWI_ID,DATA_VALUE from TM_PRODUCTION_VARIANCE_STAGING where SOURCE = '"+MurphyConstant.DOP_FORECAST+"' "
					     + "and PARAM_TYPE= '"+tagName+"' and CREATED_AT =TO_TIMESTAMP('"+currentDate+"','yyyy-MM-dd HH:mi:ss') "
					     		+ "and MUWI_ID IN("+muwis+")";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> objList = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(objList)) {
				for(Object[] obj : objList)
				{
					if(!ServicesUtil.isEmpty(obj[0]))
					{
						muwi= (String) obj[0];
					}
					if(!ServicesUtil.isEmpty(obj[1]))
					{
						dataValue= (Double) obj[1];
					}
					
					muwiActualsDataMap.put(muwi, dataValue);
				}
			
			}
					
			
		} catch (Exception e) {
			
			logger.error("[ProductionVarianceDao][fetchActualDataFromHana][error] :: " +e.getMessage());

		}
		return muwiActualsDataMap;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Double> fetchprojectedDataFromHana(List<String> muwiList, String locCode,
			UIRequestDto uiRequestDto,String currentDate) {
		HashMap<String, Double> muwiActualsDataMap = new HashMap<String, Double>();
		String queryString = "";
		String tagName = "";
		String muwi="";
		Double dataValue = 0.0;
		
		try {
			String muwis= ServicesUtil.getStringFromList(muwiList);
			String muwiInQuery =ServicesUtil.getStringForInQuery(muwis);
			// EFS
			if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.EFS_CODE)) {
				// EFS DOP
				if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID))
					tagName += MurphyConstant.CANARY_PARAM_PV[0];
				// EFS DGP
				else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID))
					tagName += MurphyConstant.CANARY_PARAM[3];
				
			}
			// Canada
			else if (uiRequestDto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE)) {
				// Kaybob DOP
				if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DOP_REPORT_ID))
					tagName += MurphyConstant.CANARY_CANADA_PARAM[0];
				
				//Canada Gas
				else if (uiRequestDto.getReportId().equalsIgnoreCase(MurphyConstant.DGP_REPORT_ID))
				{
				String muwiId = "";
				if (!ServicesUtil.isEmpty(muwiList.get(0)))
					muwiId = muwiList.get(0);
				else if (muwiList.size() > 1)
					muwiId = muwiList.get(1);
				String facilityText = locDao.getFacilityDetailsforMuwi(muwiId).getFacility();
				if (facilityText.toLowerCase().contains(MurphyConstant.KAYBOB_LOC.toLowerCase()))
					tagName += MurphyConstant.CANARY_CANADA_PARAM[1];
				else if (facilityText.toLowerCase().contains(MurphyConstant.TUPPER_MAIN_LOC.toLowerCase()))
					tagName += MurphyConstant.CANARY_CANADA_PARAM[2];
				else if (facilityText.toLowerCase().contains(MurphyConstant.TUPPER_WEST_LOC.toLowerCase()))
					tagName += MurphyConstant.CANARY_CANADA_PARAM[3];
				}
			}
			
			queryString = "SELECT MUWI_ID,DATA_VALUE from TM_PRODUCTION_VARIANCE_STAGING where SOURCE = '"+MurphyConstant.DOP_PROJECTED+"' "
					     + "and PARAM_TYPE= '"+tagName+"' and CREATED_AT = TO_TIMESTAMP('"+currentDate+"','yyyy-MM-dd HH:mi:ss') "
					     + "and MUWI_ID IN("+muwis+")";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> objList = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(objList)) {
				for(Object[] obj : objList)
				{
					if(!ServicesUtil.isEmpty(obj[0]))
					{
						muwi= (String) obj[0];
					}
					if(!ServicesUtil.isEmpty(obj[1]))
					{
						dataValue= (Double) obj[1];
					}
					
					muwiActualsDataMap.put(muwi, dataValue);
				}
			
			}
					
			
		} catch (Exception e) {
			
			logger.error("[ProductionVarianceDao][fetchActualDataFromHana][error] :: " +e.getMessage());

		}
		return muwiActualsDataMap;
	}
	
	//added as incident
	@SuppressWarnings("unchecked")
	public List<ProductionVarianceDto> fetchTrendsData(String muwi, String tagName) throws Exception {
		List<ProductionVarianceDto> productionVarienceDtoList = null;
		String queryString = null;
		try {
			queryString = "SELECT * FROM TM_PRODUCTION_VARIANCE_STAGING WHERE MUWI_ID='" + muwi + "'"
					+ "and PARAM_TYPE='"+tagName+"' ORDER BY CREATED_AT ASC";
			logger.error("Query " + queryString);
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> objList = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(objList)) {
				productionVarienceDtoList = new ArrayList<>();
				for (Object[] obj : objList) {
					ProductionVarianceDto dto = new ProductionVarianceDto();
					dto.setCreatedAt(ServicesUtil.isEmpty(obj[0]) ? null : (Date) obj[0]);
					dto.setMuwi(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					dto.setParamType(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					dto.setSource(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					dto.setDataValue(ServicesUtil.isEmpty(obj[4]) ? 0.0 : (double) obj[4]);
					productionVarienceDtoList.add(dto);
				}
				return productionVarienceDtoList;
			}
			
		} catch (Exception e) {
			logger.error("[Murphy][ProductionVarianceDao][fetchTrendsData][error] " + e.getMessage());
			e.printStackTrace();
			throw e;
		}

		return productionVarienceDtoList;

	}
	
	public DOPResponseDto dgpQueryForOtherDetails(String locationCodesList)
	{
		String queryString ="";
		String locationCodesString =locationCodesList;
		String taskIdValue="";
		ResponseMessage responseMessage= new ResponseMessage();
		DOPResponseDto response = null;
		try{
			queryString="Select (select max(te.task_id) from  TM_TASK_EVNTS te where te.process_id = (select max(ins.INVESTIGATION_ID) "
					+ "from  TM_PWHOPPER_WELL_INSTS ins where ins.location_code  = loc.location_code  ) "
					+ "and te.created_at = (select max(tr.created_At) from  TM_TASK_EVNTS tr "
					+ "where tr.process_id = (select max(ins.INVESTIGATION_ID) from  TM_PWHOPPER_WELL_INSTS ins "
					+ "where ins.location_code  = loc.location_code ))) as taskIdProActive from PRODUCTION_LOCATION loc "
					+ " left outer join WELL_MUWI ins on ins.location_Code = loc.location_code  "
					+ "left outer join WELL_TIER wt on loc.location_code = wt.location_code "
					+ " where loc.location_code in ('"+locationCodesString+"')";
			Query q = this.getSession().createSQLQuery(queryString);
			String taskId = (String) q.uniqueResult();
			if (!ServicesUtil.isEmpty(taskId)) {
				taskIdValue= taskId;
			}
		
			
			responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			 response = new DOPResponseDto();
			response.setTaskId(taskIdValue);
			response.setResponseMessage(responseMessage);
		}
		catch(Exception e)
		{
			logger.error("[Murphy][ProductionVarianceDao][dgpQueryForOtherDetails][error] " + e.getMessage());
			e.printStackTrace();
			responseMessage.setMessage(MurphyConstant.READ_FAILURE);
			responseMessage.setStatus(MurphyConstant.FAILURE);
			responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
			 response = new DOPResponseDto();
			 response.setTaskId(taskIdValue);
				response.setResponseMessage(responseMessage);
			throw e;
		}
		return response;
	}
}
