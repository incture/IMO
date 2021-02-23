package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.integration.dto.EnersightProveDailyDto;
import com.murphy.taskmgmt.dto.CanaryStagingDto;
import com.murphy.taskmgmt.dto.ProductionVarianceDto;
import com.murphy.taskmgmt.entity.ProductionVarianceDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("dopStagingDao")
public class DOPStagingDao extends BaseDao<ProductionVarianceDo, ProductionVarianceDto> {

	private static final Logger logger = LoggerFactory.getLogger(DOPStagingDao.class);

	@Override
	protected ProductionVarianceDo importDto(ProductionVarianceDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		return null;
	}

	@Override
	protected ProductionVarianceDto exportDto(ProductionVarianceDo entity) {
		return null;
	}

	public int insertToCanaryStaging(List<CanaryStagingDto> loadedData) throws Exception {
		Session session = this.getSession();
		int insertCount = 0;
		for (CanaryStagingDto canaryStagingDto : loadedData) {
			insertCount += insertToCanaryStaging(canaryStagingDto, session);
		}
		return insertCount;
	}

	private int insertToCanaryStaging(CanaryStagingDto dto, Session session) throws Exception {
		try {
			String queryString = "INSERT INTO TM_CANARY_STAGING_TABLE(CREATED_AT, DATA_VALUE, MUWI_ID, PARAM_TYPE) VALUES(TO_TIMESTAMP('"
					+ dto.getCreatedAtInString() + "', 'yyyy-MM-dd HH24:mi:ss'), " + dto.getDataValue() + ", '"
					+ dto.getMuwiId() + "', '" + dto.getParameterType() + "')";
			Query q = session.createSQLQuery(queryString);
			logger.debug("DOPStagingDao.insertToCanaryStaging();" + queryString);
			return q.executeUpdate();
		} catch (Exception e) {
			logger.error("DOPStagingDao.insertToCanaryStaging(" + dto + ")[error]" + e.getMessage());
			throw e;
		}
	}

	public int deleteAllStagedBeforeDate(String deletTimeInDBFormat) throws Exception {
		logger.debug("DOPStagingDao.deleteAllStagedBeforeDate(" + deletTimeInDBFormat + ")");
		try {
			String sql = "DELETE FROM TM_CANARY_STAGING_TABLE WHERE created_at < TO_TIMESTAMP('" + deletTimeInDBFormat
					+ "', 'yyyy-MM-dd HH24:mi:ss')";
			logger.debug("DOPStagingDao.deleteAllStagedBeforeDate(" + deletTimeInDBFormat + ")" + sql);
			Query q = this.getSession().createSQLQuery(sql);
			return q.executeUpdate();
		} catch (Exception e) {
			logger.error("DOPStagingDao.deleteAllDataBeforeDate(" + deletTimeInDBFormat + ")[error]" + e.getMessage());
			throw e;
		}
	}

	public int clearStaging(String endTimeInDBFormat) {
		logger.debug("DOPStagingDao.clearStaging(" + endTimeInDBFormat + ")");
		try {
			String sql = "DELETE FROM TM_CANARY_STAGING_TABLE WHERE created_at = TO_TIMESTAMP('" + endTimeInDBFormat
					+ "', 'yyyy-MM-dd HH24:mi:ss')";
			Query q = this.getSession().createSQLQuery(sql);
			return q.executeUpdate();
		} catch (Exception e) {
			logger.error("DOPStagingDao.clearStaging()[error]" + e.getMessage());
		}
		return -1;
	}

	public int insertToEnersightStaging(String endDayDBFormat, List<EnersightProveDailyDto> list) throws Exception {
		Session session = this.getSession();
		int insertCount = 0;
		for (EnersightProveDailyDto enersightProveDailyDto : list) {
			insertCount += insertToEnersightStaging(endDayDBFormat, enersightProveDailyDto, session);
		}
		return insertCount;
	}

	public int insertToDOPStaging(List<CanaryStagingDto> loadedData) throws Exception {
		Session session = this.getSession();
		int insertCount = 0;
		logger.error("insertToDOPStaging" + loadedData.size());
		for (CanaryStagingDto canaryStagingDto : loadedData) {
			insertCount += insertToDOPStaging(canaryStagingDto, session);
		}
		return insertCount;
	}

	/**
	 * <ul>
	 * <li>Always inserts as Daily record</li>
	 * <li>Week records concept is deprecated now</li>
	 * </ul>
	 */
	public int insertToDOPStaging(CanaryStagingDto dto, Session session) throws Exception {
		try {
			String sql = "INSERT INTO TM_PRODUCTION_VARIANCE_STAGING(CREATED_AT, DATA_VALUE, MUWI_ID, PARAM_TYPE, SOURCE) VALUES(TO_TIMESTAMP('"
					+ dto.getCreatedAtInString() + "', 'yyyy-MM-dd HH24:mi:ss'), " + dto.getDataValue() + ", '"
					+ dto.getMuwiId() + "','" + dto.getParameterType() + "', '" + MurphyConstant.DOP_CANARY + "')";
			Query q = session.createSQLQuery(sql);
			logger.error("DOPStagingDao.insertToDOPStaging():" + sql);
			return q.executeUpdate();
		} catch (Exception e) {
			logger.error("DOPStagingDao.insertToDOPStaging(" + dto + ")" + e.getMessage());
			throw e;
		}
	}

	/**
	 * <ul>
	 * <li>Always inserts as Daily record</li>
	 * <li>Week records concept is deprecated now</li>
	 * </ul>
	 */
	public int insertToEnersightStaging(String dateInDBFormat, EnersightProveDailyDto dto, Session session)
			throws Exception {
		try {
			String sql = "INSERT INTO TM_PRODUCTION_VARIANCE_STAGING(CREATED_AT, DATA_VALUE, MUWI_ID, PARAM_TYPE, SOURCE) VALUES(TO_TIMESTAMP('"
					+ dateInDBFormat + "', 'yyyy-MM-dd HH24:mi:ss'), " + dto.getForecastBoed() + ", '" + dto.getMuwiId()
					+ "', 'QTOILD', '" + MurphyConstant.DOP_FORECAST + "')";
			Query q = session.createSQLQuery(sql);
			logger.error("DOPStagingDao.insertToEnersightStaging():" + sql);
			return q.executeUpdate();
		} catch (Exception e) {
			logger.error("DOPStagingDao.insertToEnersightStaging(" + dto + ")" + e.getMessage());
			throw e;
		}
	}

	public int insertDefaultDOPRecords(String createdAt, String paramType, String source) throws Exception {
		// NOTE: createdAt should be in 'yyyy-MM-dd HH24:mi:ss' format
		try {
			String sql = "INSERT INTO TM_PRODUCTION_VARIANCE_STAGING (MUWI_ID, CREATED_AT, PARAM_TYPE, SOURCE, DATA_VALUE) "
					+ "SELECT distinct MUWI, TO_TIMESTAMP('" + createdAt
					+ "', 'yyyy-MM-dd HH24:mi:ss') as \"CREATED_AT\", '" + paramType + "' as \"PARAM_TYPE\", '" + source
					+ "' as \"SOURCE\", 0 as \"DATA_VALUE\" "
					+ "FROM WELL_MUWI WHERE MUWI is not null and trim(MUWI) != '';";
			//to be used here
			//INSERT INTO TM_PRODUCTION_VARIANCE_STAGING (MUWI_ID, CREATED_AT, SOURCE, DATA_VALUE, PARAM_TYPE) 
//			SELECT distinct MUWI,TO_TIMESTAMP('2021-01-28 07:00:00','yyyy-MM-dd HH24:mi:ss')
//			as CREATED_AT,'Actual (Canary)' as SOURCE, 0 as DATA_VALUE,
//			CASE
//			 WHEN  MUWI like '%CAN%'
//			 THEN 'SEP_CNDTOTTDY'
//			 ELSE 'QTOILD'
//			 END
//			FROM WELL_MUWI WHERE MUWI is not null and trim(MUWI) != '';

			
			logger.error("DOPStagingDao.insertDefaultDOPRecords();" + sql);
			Query q = this.getSession().createSQLQuery(sql);
			return q.executeUpdate();
		} catch (Exception e) {
			logger.error("DOPStagingDao.insertDefaultDOPRecords()[error]" + e.getMessage());
			throw e;
		}
	}

	public int updateDOPProjected(int currentHour, String nextDayTime) throws Exception {
		logger.error("DOPStagingDao.updateProjected(" + currentHour + ", " + nextDayTime + ")");
		if (currentHour < 7) {
			currentHour = currentHour + 24;
		}
		try {
			String sql = "UPDATE TM_PRODUCTION_VARIANCE_STAGING as st "
					+ "SET st.data_value = (((SELECT max(data_value) FROM TM_PRODUCTION_VARIANCE_STAGING ins "
					+ "WHERE ins.muwi_id = st.muwi_id AND ins.source = '" + MurphyConstant.DOP_CANARY + "')/"
					+ (currentHour - 6) + ")*24)" + " WHERE st.source = '" + MurphyConstant.DOP_PROJECTED
					+ "' AND st.created_at = '" + nextDayTime + "'";
			Query q = this.getSession().createSQLQuery(sql);
			logger.error("DOPStagingDao.updateDOPProjected();" + sql);
			return q.executeUpdate();
		} catch (Exception e) {
			logger.error("DOPStagingDao.updateDOPProjected()[error]" + e.getMessage());
			throw e;
		}
	}

	public int deleteAllDOPStaged() throws Exception {
		logger.debug("DOPStagingDao.deleteAllDOPStaged()");
		try {
			String sql = "DELETE FROM TM_PRODUCTION_VARIANCE_STAGING";
			logger.debug("DOPStagingDao.deleteAllDOPStaged()" + sql);
			Query q = this.getSession().createSQLQuery(sql);
			return q.executeUpdate();
		} catch (Exception e) {
			logger.error("DOPStagingDao.deleteAllDOP()[error]" + e.getMessage());
			throw e;
		}
	}

	public int clearDOP(String endTimeDBFormat) throws Exception {
		logger.debug("DOPStagingDao.clearDOP(" + endTimeDBFormat + ")");
		try {
			String sql = "DELETE FROM TM_PRODUCTION_VARIANCE_STAGING WHERE created_at = TO_TIMESTAMP('"
					+ endTimeDBFormat + "', 'yyyy-MM-dd HH24:mi:ss')";
			Query q = this.getSession().createSQLQuery(sql);
			return q.executeUpdate();
		} catch (Exception e) {
			logger.error("DOPStagingDao.clearDOP()[error]" + e.getMessage());
			throw e;
		}
	}

	public String getVersionNameOfMonthFromconfig() {
		try {
			String versionNameofMonth = null;
			String configId = "DOP_Version";
			String sql = "SELECT TCV.CONFIG_DESC_VALUE FROM TM_CONFIG_VALUES TCV WHERE TCV.CONFIG_ID = '" + configId
					+ "'";
			Query q = this.getSession().createSQLQuery(sql);
			Object result = q.uniqueResult();

			if (!ServicesUtil.isEmpty(result)) {
				return result.toString();
			}
			logger.error("DOPStagingDao.getVersionNameOfMonthFromconfig(): " + versionNameofMonth);
		} catch (Exception e) {
			logger.error("DOPStagingDao.getVersionNameOfMonthFromconfig()[error]" + e.getMessage());
			throw e;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<EnersightProveDailyDto> fetchProveDailyData(String startTimeInString, String versionName)
			throws Exception {
		logger.debug("DOPStagingDao.fetchProveDailyData(" + startTimeInString + ", " + versionName + ")");
		// STEP1: Fetch records
		List<Object[]> resultList = null;
		try {
			String sql = "SELECT UWI, AVG(ENERSIGHT_OIL) FROM ENERSIGHT_PROVE_DAILY WHERE PRODUCTION_DATE = TO_DATE(TO_TIMESTAMP('"
					+ startTimeInString + "', 'yyyy-MM-dd HH24:mi:ss')) and VERSION_NAME ='" + versionName
					+ "' GROUP BY UWI";
			Query q = this.getSession().createSQLQuery(sql);
			logger.error("DOPStagingDao.fetchProveDailyData() " + sql);
			resultList = (List<Object[]>) q.list();
		} catch (Exception e) {
			logger.error("DOPStagingDao.fetchProveDailyData()[error]" + e.getMessage());
			throw e;
		}
		// STEP 2: Export to resultList to returnList
		if (!ServicesUtil.isEmpty(resultList)) {
			List<EnersightProveDailyDto> returnList = new ArrayList<EnersightProveDailyDto>();
			for (Object[] obj : resultList) {
				EnersightProveDailyDto enersightProveDailyDto = new EnersightProveDailyDto();
				if (!ServicesUtil.isEmpty(obj[0]))
					enersightProveDailyDto.setMuwiId((String) obj[0]);
				if (!ServicesUtil.isEmpty(obj[1]))
					enersightProveDailyDto.setForecastBoed((Double) obj[1]);
				returnList.add(enersightProveDailyDto);
			}
			return returnList;
		}
		return null;
	}
	
	
	
	
	//added by me
	public int clearDOPAtRestTime(String endTimeDBFormat) throws Exception {
		logger.debug("DOPStagingDao.clearDOP(" + endTimeDBFormat + ")");
		try {
			String sql = "DELETE FROM TM_PRODUCTION_VARIANCE_STAGING WHERE created_at = TO_TIMESTAMP('"
					+ endTimeDBFormat + "', 'yyyy-MM-dd HH24:mi:ss')";
			Query q = this.getSession().createSQLQuery(sql);
			return q.executeUpdate();
		} catch (Exception e) {
			logger.error("DOPStagingDao.clearDOP()[error]" + e.getMessage());
			throw e;
		}
	}
	
	
	public int deleteAllDOPStagedDataForTags(String tag) throws Exception {
		logger.debug("DOPStagingDao.deleteAllDOPStagedDataForTags(tag)");
		try {
			String sql = "DELETE FROM TM_PRODUCTION_VARIANCE_STAGING where PARAM_TYPE = '"+tag+"'";
			logger.debug("DOPStagingDao.deleteAllDOPStagedDataForTags(tag)" + sql);
			Query q = this.getSession().createSQLQuery(sql);
			return q.executeUpdate();
		} catch (Exception e) {
			logger.error("DOPStagingDao.deleteAllDOPStagedDataForTags(tag)[error]" + e.getMessage());
			throw e;
		}
	}
	
	public int insertDefaultDOPRecordsForEFS(String createdAt, String paramType, String source,List<String> muwiList) throws Exception {
		// NOTE: createdAt should be in 'yyyy-MM-dd HH24:mi:ss' format
		try {
			String muwis= ServicesUtil.getStringFromList(muwiList);
			//String inQuery= ServicesUtil.getStringForInQuery(muwis);
			String sql = "INSERT INTO TM_PRODUCTION_VARIANCE_STAGING (MUWI_ID, CREATED_AT, PARAM_TYPE, SOURCE, DATA_VALUE) "
					+ "SELECT distinct MUWI, TO_TIMESTAMP('" + createdAt
					+ "', 'yyyy-MM-dd HH24:mi:ss') as \"CREATED_AT\", '" + paramType + "' as \"PARAM_TYPE\", '" + source
					+ "' as \"SOURCE\", 0 as \"DATA_VALUE\" "
					+ "FROM WELL_MUWI WHERE MUWI is not null and trim(MUWI) != '' and LOCATION_CODE like 'MUR-US-EFS%' "
					+ " and MUWI in ("+muwis+") ";
			
			logger.error("DOPStagingDao.insertDefaultDOPRecords();" + sql);
			Query q = this.getSession().createSQLQuery(sql);
			return q.executeUpdate();
		} catch (Exception e) {
			logger.error("DOPStagingDao.insertDefaultDOPRecords()[error]" + e.getMessage());
			throw e;
		}
	}

	public int insertDefaultDOPRecordsForCanada(String createdAt, String paramType, String source,List<String> muwiList) {
		// NOTE: createdAt should be in 'yyyy-MM-dd HH24:mi:ss' format
				try {
					String muwis= ServicesUtil.getStringFromList(muwiList);
					String inQuery= ServicesUtil.getStringForInQuery(muwis);
					String sql = "INSERT INTO TM_PRODUCTION_VARIANCE_STAGING (MUWI_ID, CREATED_AT, PARAM_TYPE, SOURCE, DATA_VALUE) "
							+ "SELECT distinct MUWI, TO_TIMESTAMP('" + createdAt
							+ "', 'yyyy-MM-dd HH24:mi:ss') as \"CREATED_AT\", '" + paramType + "' as \"PARAM_TYPE\", '" + source
							+ "' as \"SOURCE\", 0 as \"DATA_VALUE\" "
							+ "FROM WELL_MUWI WHERE MUWI is not null and trim(MUWI) != '' and LOCATION_CODE like 'MUR-CA%' "
							+ " and MUWI in ("+muwis+") ";
					
					logger.error("DOPStagingDao.insertDefaultDOPRecords();" + sql);
					Query q = this.getSession().createSQLQuery(sql);
					return q.executeUpdate();
				} catch (Exception e) {
					logger.error("DOPStagingDao.insertDefaultDOPRecords()[error]" + e.getMessage());
					throw e;
				}
	}
	
	public int updateDOPProjectedValues(int currentHour, String nextDayTime , String tag, boolean isEFSOil, boolean isEFSGas,
			boolean isCAOil,boolean isCAKaybobGas,boolean isCAMontneyOneGas,boolean isCAMontneytwoGas,int resetHour) throws Exception {
		logger.error("DOPStagingDao.updateProjected(" + currentHour + ", " + nextDayTime + ")");
		if(isEFSOil)
		{
		if (currentHour < resetHour) {
			currentHour = currentHour + 24;
		}
		}
		if(isEFSGas)
		{
		if (currentHour < resetHour) {
			currentHour = currentHour + 24;
		}
		}
		
		//canada
		if(isCAOil)
		{
		if (currentHour < resetHour) {
			currentHour = currentHour + 24;
		}
		}
		
		if(isCAKaybobGas)
		{
		if (currentHour < resetHour) {
			currentHour = currentHour + 24;
		}
		}
		if(isCAMontneyOneGas)
		{
		if (currentHour < resetHour) {
			currentHour = currentHour + 24;
		}
		}
		if(isCAMontneytwoGas)
		{
		if (currentHour < resetHour) {
			currentHour = currentHour + 24;
		}
		}
		try {
			logger.error("current hour while updating projected values : "+currentHour);
			logger.error("reset hour while updating projected values : "+resetHour);
			String sql = "UPDATE TM_PRODUCTION_VARIANCE_STAGING as st "
					+ "SET st.data_value = (((SELECT max(data_value) FROM TM_PRODUCTION_VARIANCE_STAGING ins "
					+ "WHERE ins.muwi_id = st.muwi_id and ins.PARAM_TYPE = '"+tag+"' "
							+ " AND ins.source = '" + MurphyConstant.DOP_CANARY + "')/"
					+ (currentHour - resetHour) + ")*24)" + " WHERE st.source = '" + MurphyConstant.DOP_PROJECTED
					+ "' AND st.created_at = '" + nextDayTime + "' and st.PARAM_TYPE = '"+tag+"'";
			Query q = this.getSession().createSQLQuery(sql);
			logger.error("DOPStagingDao.updateDOPProjected.query :: " + sql);
			return q.executeUpdate();
		} catch (Exception e) {
			logger.error("DOPStagingDao.updateDOPProjected()[error]" + e.getMessage());
			throw e;
		}
	}
	
	public int insertToEnersightStaging(String endDayDBFormat, List<EnersightProveDailyDto> list,String tag) throws Exception {
		Session session = this.getSession();
		int insertCount = 0;
		logger.error("sending list for  insert enersight to hana : ");
		for (EnersightProveDailyDto enersightProveDailyDto : list) {
			insertCount += insertToEnersightStaging(endDayDBFormat, enersightProveDailyDto, session,tag);
		}
		logger.error("finsihed insert enersight to hana : "+insertCount);
		return insertCount;
	}
	public int insertToEnersightStaging(String dateInDBFormat, EnersightProveDailyDto dto, Session session,String tag)
			throws Exception {
		try {
			logger.error("begin insert enersight data to hana : " +dto);
			logger.error("next day date : "+dateInDBFormat);
			String sql = "INSERT INTO TM_PRODUCTION_VARIANCE_STAGING(CREATED_AT, DATA_VALUE, MUWI_ID, PARAM_TYPE, SOURCE) VALUES(TO_TIMESTAMP('"
					+ dateInDBFormat + "', 'yyyy-MM-dd HH24:mi:ss'), " + dto.getForecastBoed() + ", '" + dto.getMuwiId()
					+ "', '"+tag+"', '" + MurphyConstant.DOP_FORECAST + "')";
			Query q = session.createSQLQuery(sql);
			logger.error("DOPStagingDao.insertToEnersightStaging(): query " + sql);
			logger.error(" insert enersight data to hana completed auccessfully : ");
			return q.executeUpdate();
		} catch (Exception e) {
			logger.error("DOPStagingDao.insertToEnersightStaging(" + dto + ")" + e.getMessage());
			throw e;
		}
	}
}