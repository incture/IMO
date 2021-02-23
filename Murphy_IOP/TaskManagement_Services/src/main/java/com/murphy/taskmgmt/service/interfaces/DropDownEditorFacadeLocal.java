package com.murphy.taskmgmt.service.interfaces;


import com.murphy.taskmgmt.dto.DropDownEditorRequestDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface DropDownEditorFacadeLocal {

	
	ResponseMessage manipulateAllData(DropDownEditorRequestDto base64);
	ResponseMessage dropDownClassificationManipulate(DropDownEditorRequestDto base64);
}
