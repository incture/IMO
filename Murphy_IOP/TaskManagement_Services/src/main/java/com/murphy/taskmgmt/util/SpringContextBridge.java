package com.murphy.taskmgmt.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.murphy.taskmgmt.dao.AutoSignInDao;
import com.murphy.taskmgmt.dao.BlackLineDao;
import com.murphy.taskmgmt.dao.ConfigDao;
import com.murphy.taskmgmt.dao.CygnetDowntimeCounterDao;
import com.murphy.taskmgmt.dao.CygnetRecomDowntimeDao;
import com.murphy.taskmgmt.dao.EmpDetailsDao;
import com.murphy.taskmgmt.dao.GeoTabDao;
import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dao.UserIDPMappingDao;

@Component
public class SpringContextBridge implements SpringContextBridgeService, ApplicationContextAware {
	
	@Autowired
	private GeoTabDao geoTabDao;
	
//	@Autowired
//	private LocationHierarchyDao hierarchyDao;
	
	@Autowired
	private BlackLineDao blackLineDao;
	
	@Autowired
	private CygnetDowntimeCounterDao cygnetDowntimeCounterDao;
	
	@Autowired
	private CygnetRecomDowntimeDao cygnetRecomDowntimeDao;
	
	@Autowired
	private ConfigDao configDao;
	
	@Autowired
	private AutoSignInDao autoSignInDao;
	
	@Autowired
	private UserIDPMappingDao userIdpMappingDao;
	
	@Autowired
	private HierarchyDao hierarchyDao;
	
	@Autowired
	private EmpDetailsDao empDetailsDao;

	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		 SpringContextBridge.applicationContext = applicationContext;
	}

	public static SpringContextBridgeService services() {
        return applicationContext.getBean(SpringContextBridgeService.class);
    }
	
	@Override
	public GeoTabDao getGeoTabDao() {
		return geoTabDao;
	}
	
	
	public CygnetDowntimeCounterDao getCygnetDowntimeCounterDao() {
		return cygnetDowntimeCounterDao;
	}

	public CygnetRecomDowntimeDao getCygnetRecomDowntimeDao() {
		return cygnetRecomDowntimeDao;
	}
	
	@Override
	public ConfigDao getConfigDao() {
		return configDao;
	}

//	@Override
//	public LocationHierarchyDao getLocationHierarchyDao() {
//		return hierarchyDao;
//	}

	@Override
	public UserIDPMappingDao getUserIDPMappingDao() {
		return userIdpMappingDao;
	}

	@Override
	public AutoSignInDao getAutoSignInDao() {
		return autoSignInDao;
	}
	
	@Override
	public BlackLineDao getBlackLineDao(){
		return blackLineDao;
	}
	
	@Override
	public HierarchyDao gethierarchyDao(){
		return hierarchyDao;
	}
	
	@Override
	public EmpDetailsDao getEmpDetailsDao(){
		return empDetailsDao;
	}
}
