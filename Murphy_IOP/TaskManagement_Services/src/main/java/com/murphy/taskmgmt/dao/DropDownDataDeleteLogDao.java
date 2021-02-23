package com.murphy.taskmgmt.dao;

import com.murphy.taskmgmt.dto.DropDownDataDeleteLogDto;
import com.murphy.taskmgmt.entity.DropDownDataDeleteLogDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;

public class DropDownDataDeleteLogDao  extends BaseDao<DropDownDataDeleteLogDo, DropDownDataDeleteLogDto>{

	@Override
	protected DropDownDataDeleteLogDo importDto(DropDownDataDeleteLogDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		DropDownDataDeleteLogDo dropDownDataDeleteLogDo=new DropDownDataDeleteLogDo();
		dropDownDataDeleteLogDo.setClassification(fromDto.getClassification());
		dropDownDataDeleteLogDo.setSubClassification(fromDto.getSubclassification());
		dropDownDataDeleteLogDo.setDeletedAt(fromDto.getDeletedAt());
		dropDownDataDeleteLogDo.setDeleteId(fromDto.getDeleteId());
		return dropDownDataDeleteLogDo;
	}

	@Override
	protected DropDownDataDeleteLogDto exportDto(DropDownDataDeleteLogDo entity) {
		DropDownDataDeleteLogDto dropDownDataDeleteLogDto=new DropDownDataDeleteLogDto();
		dropDownDataDeleteLogDto.setClassification(entity.getClassification());
		dropDownDataDeleteLogDto.setSubclassification(entity.getSubClassification());
		dropDownDataDeleteLogDto.setDeletedAt(entity.getDeletedAt());
		dropDownDataDeleteLogDto.setDeleteId(entity.getDeleteId());
		return dropDownDataDeleteLogDto;
	}

}
