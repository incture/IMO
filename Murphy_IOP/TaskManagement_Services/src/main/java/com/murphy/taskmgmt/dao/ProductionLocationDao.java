package com.murphy.taskmgmt.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.constants.EnLocationType;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository
@Transactional
public class ProductionLocationDao {

	private static final Logger logger = LoggerFactory.getLogger(ProductionLocationDao.class);

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][WorkBenchDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}
	}

	public String getCFLocationCode(String wellLocationCode) throws NoResultFault, InvalidInputFault {

		ServicesUtil.enforceMandatory("wellLocationCode", wellLocationCode);

		StringBuilder sql = new StringBuilder("SELECT LOCATION_CODE FROM PRODUCTION_LOCATION WHERE LOCATION_CODE="
				+ "(SELECT PARENT_CODE FROM PRODUCTION_LOCATION WHERE LOCATION_CODE="
				+ "(SELECT PARENT_CODE FROM PRODUCTION_LOCATION WHERE LOCATION_CODE= :wellLocationCode))");

		System.err.println(sql.toString());

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("wellLocationCode", wellLocationCode);

		String cfLocationtext = (String) query.uniqueResult();

		if (!ServicesUtil.isEmpty(cfLocationtext)) {
			return cfLocationtext;
		}

		throw new NoResultFault("ProductionLocationDao.getCFText() no records found for query");
	}

	@SuppressWarnings("unchecked")
	public String getLocationCodeByLocationtext(String locationText) throws NoResultFault, InvalidInputFault {
		ServicesUtil.enforceMandatory("locationText", locationText);
		StringBuilder sql = new StringBuilder(
				"SELECT LOCATION_CODE FROM PRODUCTION_LOCATION WHERE TRIM(LOCATION_TEXT) =:locationText");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("locationText", locationText);

		List<String> locationCode = query.list();

		if (!ServicesUtil.isEmpty(locationCode)) {
			return locationCode.get(0);

		}
		throw new NoResultFault("ProductionLocationDao.getLocationCodeByLocationtext()");

	}

	public String getLocationType(String locationCode) throws InvalidInputFault, NoResultFault {

		ServicesUtil.enforceMandatory("locationCode", locationCode);
		StringBuilder sql = new StringBuilder(
				"SELECT LOCATION_TYPE FROM PRODUCTION_LOCATION WHERE LOCATION_CODE = :locationCode");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("locationCode", locationCode);
		String locationType = (String) query.uniqueResult();

		if (!ServicesUtil.isEmpty(locationType)) {
			return locationType;
		}

		throw new NoResultFault("ProductionLocationDao.getLocationType() no records found for query");

	}

	public String getFieldLocationCode(String locationCode) throws InvalidInputFault, NoResultFault {
		// Step1 : get LocationType
		String locationType = getLocationType(locationCode);

		StringBuilder sql = new StringBuilder();

		switch (EnLocationType.get(locationType)) {

		case FIELD:
			return locationCode;

		case CENTRAL_FACILITY:
			sql = new StringBuilder("SELECT PARENT_CODE FROM PRODUCTION_LOCATION WHERE LOCATION_CODE= :locationCode");
			break;

		case FACILITY:
			sql = new StringBuilder("SELECT PARENT_CODE FROM PRODUCTION_LOCATION WHERE LOCATION_CODE= :locationCode");

			break;

		case WELL_PAD:
			sql = new StringBuilder("SELECT PARENT_CODE FROM PRODUCTION_LOCATION WHERE LOCATION_CODE="
					+ "(SELECT PARENT_CODE FROM PRODUCTION_LOCATION WHERE LOCATION_CODE= :locationCode)");

			break;
		case WELL:
			sql = new StringBuilder("SELECT PARENT_CODE FROM PRODUCTION_LOCATION WHERE LOCATION_CODE="
					+ "(SELECT PARENT_CODE FROM PRODUCTION_LOCATION WHERE LOCATION_CODE="
					+ "(SELECT PARENT_CODE FROM PRODUCTION_LOCATION WHERE LOCATION_CODE= :locationCode))");
			break;

		case BASE:
		default:
			throw new InvalidInputFault(
					"ProductionLocationDao.getFieldLocationCode() field can get for the locationtype,locationsCode "
							+ locationType + "," + locationCode);
		}

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("locationCode", locationCode);
		String fieldLocationCode = (String) query.uniqueResult();

		if (!ServicesUtil.isEmpty(fieldLocationCode)) {
			return fieldLocationCode;
		}

		throw new NoResultFault("ProductionLocationDao.getFieldLocationCode() no records found for query " + query);

	}

}
