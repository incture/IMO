package com.murphy.taskmgmt.util;

import com.murphy.taskmgmt.dao.AutoSignInDao;
import com.murphy.taskmgmt.dao.BlackLineDao;
import com.murphy.taskmgmt.dao.ConfigDao;
import com.murphy.taskmgmt.dao.CygnetDowntimeCounterDao;
import com.murphy.taskmgmt.dao.CygnetRecomDowntimeDao;
import com.murphy.taskmgmt.dao.EmpDetailsDao;
import com.murphy.taskmgmt.dao.GeoTabDao;
import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dao.UserIDPMappingDao;

public interface SpringContextBridgeService {

	public GeoTabDao getGeoTabDao();
	
	public CygnetDowntimeCounterDao getCygnetDowntimeCounterDao();
	
	public CygnetRecomDowntimeDao getCygnetRecomDowntimeDao() ;

//	public LocationHierarchyDao getLocationHierarchyDao();

	ConfigDao getConfigDao();

	public UserIDPMappingDao getUserIDPMappingDao();

	AutoSignInDao getAutoSignInDao();

	public BlackLineDao getBlackLineDao();

	public HierarchyDao gethierarchyDao();

	public EmpDetailsDao getEmpDetailsDao();
}
