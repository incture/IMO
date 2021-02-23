package com.murphy.taskmgmt.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("WellMuwiDao")
@Transactional
public class WellMuwiDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][BaseDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}

	}
	
	private static final Logger logger = LoggerFactory.getLogger(WellMuwiDao.class);
	
	public Set<String> getMuwi()
	{
		Query query = null;
		Set<String> muwiID = null;
		try{
			String queryString="Select DISTINCT MUWI from WELL_MUWI WHERE LOCATION_CODE LIKE 'MUR-US-EFS%'";
			query = this.getSession().createSQLQuery(queryString);
			
			muwiID= new HashSet<String>();
			List<Object> response = (List<Object>) query.list();
			if (!ServicesUtil.isEmpty(response)) {
				for(Object obj : response){
					if(!ServicesUtil.isEmpty(obj))
						 muwiID.add((String) obj); 
				}
				/*muwiID.add("9264225500000000SWK0141H1");
				muwiID.add("9264212700000000STB0041H1");
				muwiID.add("9264231100000000TYR0049H1");*/
			}
			
		}
		catch(Exception e)
		{
			logger.error("[Murphy][WellMuwiDao][getMuwi][Exception]" + e.getMessage());
		}
		return muwiID;
	}
	
	
	public String getLocationCodeByMUWI(String muwi) throws InvalidInputFault {
		ServicesUtil.enforceMandatory("muwi", muwi);
		StringBuilder sql = new StringBuilder("SELECT LOCATION_CODE FROM WELL_MUWI WHERE MUWI='" + muwi + "'");
		Query query = this.getSession().createSQLQuery(sql.toString());
		String locatioCode = (String) query.uniqueResult();
		return locatioCode;

	}
	
}
