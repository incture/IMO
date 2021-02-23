package com.murphy.taskmgmt.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.ConfigValuesDo;
import com.murphy.taskmgmt.service.interfaces.ConfigFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/config", produces = "application/json")
public class ConfigRest {
	
	@Autowired
	ConfigFacadeLocal configFacadeLocal;
	
	@RequestMapping(value = "/getConfig", method = RequestMethod.GET)
	public String getConfig(@RequestParam("configRef") String configRef) {
		return configFacadeLocal.getConfigurationByRef(configRef);
	}
	
	@RequestMapping(value = "/saveOrUpdateConfig", method = RequestMethod.POST)
	public ResponseMessage saveOrUpdateConfig(@RequestBody ConfigValuesDo configValuesDo) {
		return configFacadeLocal.saveOrUpdateConfigByRef(configValuesDo);
	}
	
	@RequestMapping(value = "/deleteConfig", method = RequestMethod.POST)
	public ResponseMessage deleteConfig(@RequestBody ConfigValuesDo configValuesDo) {
		return configFacadeLocal.deleteConfigurationByRef(configValuesDo);
	}
	
	@RequestMapping(value = "/getPropertyConfig", method = RequestMethod.GET)
	public Object getPropertyConfig(@RequestParam("propertyName") String propertyName) {
		return configFacadeLocal.getPropertyConfig(propertyName);
	}
	
	@RequestMapping(value = "/setPropertyConfig", method = RequestMethod.GET)
	public ResponseMessage setPropertyConfig(@RequestParam("propertyName") String propertyName, @RequestParam("propertyValue") String propertyValue) {
		return configFacadeLocal.setPropertyConfig(propertyName, propertyValue);
	}
	
	@RequestMapping(value = "/getAllPropertyConfig", method = RequestMethod.GET)
	public Map<String, Object> getAllPropertyConfig() {
		return configFacadeLocal.getAllPropertyConfig();
	}

}
