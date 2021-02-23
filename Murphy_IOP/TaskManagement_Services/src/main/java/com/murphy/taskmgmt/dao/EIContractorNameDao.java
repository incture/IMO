package com.murphy.taskmgmt.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.EIContractorNameDto;
import com.murphy.taskmgmt.entity.EIContractorNameDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;

@Repository("EIContractorNameDao")
@Transactional
public class EIContractorNameDao extends BaseDao<EIContractorNameDo, EIContractorNameDto> {

	@Override
	protected EIContractorNameDo importDto(EIContractorNameDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		EIContractorNameDo entity = new EIContractorNameDo();
		entity.setId(fromDto.getId());
		entity.setContractorName(fromDto.getContractorName());
		return null;
	}

	@Override
	protected EIContractorNameDto exportDto(EIContractorNameDo entity) {
		EIContractorNameDto toDto = new EIContractorNameDto();
		toDto.setId(entity.getId());
		toDto.setContractorName(entity.getContractorName());
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAllContractorNames() {
		String query = "select CONTRACTOR_NAME from EI_CONTRACTOR_LIST";
		Query q = this.getSession().createSQLQuery(query);
		List<String> contractorList = q.list();
		return contractorList;
	}

}
