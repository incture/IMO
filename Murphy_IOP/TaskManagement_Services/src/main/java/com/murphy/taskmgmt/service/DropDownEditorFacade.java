package com.murphy.taskmgmt.service;


import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.murphy.taskmgmt.dao.DropDownEditorDao;
import com.murphy.taskmgmt.dto.DropDownEditorRequestDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.DropDownEditorFacadeLocal;

@Service("DropDownEditorFacade")

public class DropDownEditorFacade implements DropDownEditorFacadeLocal{

	@Autowired
	private DropDownEditorDao dropDownEditorDao;
	
	ResponseMessage message = null;
	
	/*@Override
	public ResponseMessage dropDownManipulate(DropDownEditorRequestDto base64) {
		return dropDownEditorDao.dropDownManipulate(base64);
	}
*/
	
	public ResponseMessage manipulateAllData(DropDownEditorRequestDto base64) {
		return  dropDownEditorDao.manipulateAllData(base64);
	}
	public ResponseMessage dropDownClassificationManipulate(DropDownEditorRequestDto base64)
	{
		return dropDownEditorDao.dropDownClassificationManipulate(base64);
	}
	
}
