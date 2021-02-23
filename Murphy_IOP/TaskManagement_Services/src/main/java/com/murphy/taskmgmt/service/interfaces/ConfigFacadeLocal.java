package com.murphy.taskmgmt.service.interfaces;

import java.util.Map;

import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.ConfigValuesDo;

public interface ConfigFacadeLocal {

	String getConfigurationByRef(String configRef);

	ResponseMessage saveOrUpdateConfigByRef(ConfigValuesDo configValuesDo);

	ResponseMessage deleteConfigurationByRef(ConfigValuesDo configValuesDo);

	Object getPropertyConfig(String propertyName);

	ResponseMessage setPropertyConfig(String propertyName, String propertyValue);

	Map<String, Object> getAllPropertyConfig();

}
