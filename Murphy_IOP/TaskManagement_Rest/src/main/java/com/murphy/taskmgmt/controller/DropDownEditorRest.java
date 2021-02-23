package com.murphy.taskmgmt.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.DropDownEditorRequestDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.DropDownEditorFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/dropDown", produces = "application/json")
public class DropDownEditorRest {
	
	@Autowired
	DropDownEditorFacadeLocal dropDownLocal;
	
	@RequestMapping(value = "/dropDownDataEdit", method = RequestMethod.POST)
	public ResponseMessage manipulateAllData(@RequestBody DropDownEditorRequestDto base64) {
		return  dropDownLocal.manipulateAllData(base64);
	}
	
	@RequestMapping(value = "/dropDownClassificationsEdit", method = RequestMethod.POST)
	public ResponseMessage dropDownClassificationManipulate(@RequestBody DropDownEditorRequestDto base64)
	{
		return dropDownLocal.dropDownClassificationManipulate(base64);
	}
	
	
	

}
