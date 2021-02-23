package com.murphy.taskmgmt.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.EIReasonDto;
import com.murphy.taskmgmt.entity.EIReasonDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;

@Repository("EIReasonDao")
@Transactional
public class EIReasonDao extends BaseDao<EIReasonDo, EIReasonDto> {

	@Override
	protected EIReasonDo importDto(EIReasonDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		EIReasonDo entity = new EIReasonDo();
		entity.setId(fromDto.getId());
		entity.setReason(fromDto.getReason());
		entity.setActiveFlag(fromDto.getActiveFlag());
		return null;
	}

	@Override
	protected EIReasonDto exportDto(EIReasonDo entity) {
		EIReasonDto toDto = new EIReasonDto();
		toDto.setId(entity.getId());
		toDto.setReason(entity.getReason());
		toDto.setActiveFlag(entity.getActiveFlag());
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAllReasons() {
		String activeFlag=MurphyConstant.ACTIVE;
		String query = "select REASON from EI_REASON p where p.ACTIVE_FLAG='" + activeFlag + "'";
		Query q = this.getSession().createSQLQuery(query);
		List<String> reasonList = q.list();
		return reasonList;
	}
}
