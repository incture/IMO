package com.murphy.taskmgmt.service;

import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.ConfigDao;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.ConfigValuesDo;
import com.murphy.taskmgmt.service.interfaces.ConfigFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("ConfigFacade")
public class ConfigFacade implements ConfigFacadeLocal {
	
	@Autowired
	ConfigDao configDao;
	
	@Override
	public String getConfigurationByRef(String configRef) {
		return configDao.getConfigurationByRef(configRef);
	}
	
	@Override
	public ResponseMessage saveOrUpdateConfigByRef(ConfigValuesDo configValuesDo) {
		if(!ServicesUtil.isEmpty(configValuesDo))
			return configDao.saveOrUpdateConfigByRef(configValuesDo.getConfigId(), configValuesDo.getConfigValue());
		return null;
	}
	
	@Override
	public ResponseMessage deleteConfigurationByRef(ConfigValuesDo configValuesDo) {
		if(!ServicesUtil.isEmpty(configValuesDo))
			return configDao.deleteConfigurationByRef(configValuesDo.getConfigId());
		return null;
	}
	
	@Override
	public Object getPropertyConfig(String propertyName) {
		Object obj = null;
		try {
			obj = ServicesUtil.getProperty(propertyName, MurphyConstant.IOP_PROPERTY_FILE_LOCATION);
		} catch (ConfigurationException e) {
			System.err.print("Error getting property config : "+e.getLocalizedMessage());
		}
		if(!ServicesUtil.isEmpty(obj))
			return (String) obj;
		return null;
	}
	
	@Override
	public ResponseMessage setPropertyConfig(String propertyName, String propertyValue) {
		ResponseMessage message = new ResponseMessage();
		message.setMessage("Setting configuration failed");
		message.setStatus(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			ServicesUtil.setProperty(propertyName, propertyValue, MurphyConstant.IOP_PROPERTY_FILE_LOCATION);
			message.setMessage("Setting configuration success");
			message.setStatus(MurphyConstant.SUCCESS);
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (ConfigurationException e) {
			System.err.print("Error getting property config : "+e.getLocalizedMessage());
		}
		return message;
	}
	
	@Override
	public Map<String, Object> getAllPropertyConfig() {
		Map<String, Object> allProperties = null;
		try {
			allProperties = ServicesUtil.getAllProperties(MurphyConstant.IOP_PROPERTY_FILE_LOCATION);
		} catch (ConfigurationException e) {
			System.err.print("Error getting all property config : "+e.getLocalizedMessage());
		}
		return allProperties;
	}

}
