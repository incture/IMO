package com.murphy.taskmgmt.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.integration.dto.EnersightProveDailyDto;
import com.murphy.integration.util.ServicesUtil;

@Repository("EnersightProveMonthlyDao")
public class EnersightProveMonthlyDao {

	private static final Logger logger = LoggerFactory.getLogger(EnersightProveMonthlyDao.class);

	public EnersightProveMonthlyDao() {
		super();
	}

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		/*
		 * try { ServicesUtil.unSetupSOCKS(); logger.error("basedao"+System.getProperty("socksProxyHost"));
		 * logger.error("basedao"+System.getProperty("socksProxyPort")); logger.error("basedao"+System.getProperty("java.net.socks.username"));
		 * while(System.getProperty(MurphyConstant.SOCKS_PORT_NAME).equals(MurphyConstant.SOCKS_PORT)) { } } catch (Exception e) {
		 * logger.error("[Murphy][BaseDao][getSession][Socks Exception] "+e.getMessage()); }
		 */
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][BaseDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}

	}

	@SuppressWarnings("unchecked")
	public List<EnersightProveDailyDto> fetchProveDailyData() throws Exception {
		List<EnersightProveDailyDto> enersightProveDailyDtoList = null;
		EnersightProveDailyDto enersightProveDailyDto = null;
		SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String versionName = getVersionNameOfMonthFromconfig();

			String query = "SELECT UWI, WELL_NAME, PRODUCTION_DATE, VERSION_NAME, ENERSIGHT_OIL FROM ENERSIGHT_PROVE_DAILY WHERE PRODUCTION_DATE ='" + SimpleDateFormat.format(new Date()) +"' and  VERSION_NAME ='"+versionName+"'" ;
			logger.error("[fetchProveDailyData] Info - ProductionDate " + SimpleDateFormat.format(new Date()) + "versionName " + versionName);
			logger.error("[fetchProveDailyData] Info - Query " + query);
			
			
			Query q = this.getSession().createSQLQuery(query);
			
			logger.error("[fetchProveDailyData] Info - Query " + q.toString());

			List<Object[]> resultList = q.list();

			if (!ServicesUtil.isEmpty(resultList)) {
				enersightProveDailyDtoList = new ArrayList<EnersightProveDailyDto>();
				for (Object[] obj : resultList) {
					enersightProveDailyDto = new EnersightProveDailyDto();
					if (!ServicesUtil.isEmpty(obj[0]))
						enersightProveDailyDto.setMuwiId((String) obj[0]);
					if (!ServicesUtil.isEmpty(obj[1]))
						enersightProveDailyDto.setWell((String) obj[1]);
					if (!ServicesUtil.isEmpty(obj[2]))
						enersightProveDailyDto.setLastProdDate(obj[2].toString());
//					if (!ServicesUtil.isEmpty(obj[3]))
//						enersightProveDailyDto.setVersionName((String) obj[3]);
					if (!ServicesUtil.isEmpty(obj[4]))
						enersightProveDailyDto.setForecastBoed((Double) obj[4]);
					enersightProveDailyDtoList.add(enersightProveDailyDto);
				}
				logger.error("[fetchProveDailyData] : INFO - enersightProveMonthlyDtoList Size" + enersightProveDailyDtoList.size());
			}
		} catch (Exception e) {
			logger.error("[fetchProveDailyData] : ERROR- Exception while fetching data from database " + e);
			throw e;
		}
		return enersightProveDailyDtoList;
	}

	public String getVersionNameOfMonthFromconfig() {

		String VersionNameofMonth = null;
		String configId = "DOP_Version";

		String qryString = "SELECT TCV.CONFIG_DESC_VALUE FROM TM_CONFIG_VALUES TCV WHERE TCV.CONFIG_ID = '" + configId + "'";

		try {
			Query q = this.getSession().createSQLQuery(qryString);
			Object result = q.uniqueResult();

			if (!ServicesUtil.isEmpty(result)) {
				VersionNameofMonth = result.toString();
			}
			logger.error("VersionNameofMonth:" + VersionNameofMonth);
		} catch (Exception e) {
			logger.error("[getVersionNameOfMonthFromconfig] : ERROR- Date Format Exception" + VersionNameofMonth);
			throw e;
		}
		return VersionNameofMonth;
	}
}
