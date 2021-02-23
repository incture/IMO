package com.murphy.taskmgmt.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.SequenceNumberDto;
import com.murphy.taskmgmt.entity.SequenceNumberDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;

@Repository("SequenceNumberDao")
@Transactional
public class SequenceNumberDao extends BaseDao<SequenceNumberDo, SequenceNumberDto> {

	@Override
	protected SequenceNumberDo importDto(SequenceNumberDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		SequenceNumberDo entity = new SequenceNumberDo();
		entity.setSeqCategory(fromDto.getSeqCategory());
		entity.setNextAvailableNum(fromDto.getNextAvailableNum());
		return entity;
	}

	@Override
	protected SequenceNumberDto exportDto(SequenceNumberDo entity) {
		SequenceNumberDto toDto = new SequenceNumberDto();
		toDto.setSeqCategory(entity.getSeqCategory());
		toDto.setNextAvailableNum(entity.getNextAvailableNum());
		return toDto;
	}
	
}
