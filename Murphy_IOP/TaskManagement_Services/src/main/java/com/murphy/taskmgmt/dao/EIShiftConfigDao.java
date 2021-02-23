package com.murphy.taskmgmt.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.EIShiftConfigDto;
import com.murphy.taskmgmt.entity.EIShiftConfigDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;

@Repository("EIShiftConfigDao")
@Transactional
public class EIShiftConfigDao extends BaseDao<EIShiftConfigDo, EIShiftConfigDto>  {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private static final Logger logger = LoggerFactory.getLogger(EIShiftConfigDao.class);

	@Override
	protected EIShiftConfigDo importDto(EIShiftConfigDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		EIShiftConfigDo entity = new EIShiftConfigDo();
		entity.setId(fromDto.getId());
		entity.setShiftType(fromDto.getShiftType());
		return entity;
	}

	@Override
	protected EIShiftConfigDto exportDto(EIShiftConfigDo entity) {
		EIShiftConfigDto toDto = new EIShiftConfigDto();
		toDto.setId(entity.getId());
		toDto.setShiftType(entity.getShiftType());
		return toDto;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAllShifts() {
		String query = "select SHIFT_TYPE from EI_SHIFT_CONFIG";
		Query q = this.getSession().createSQLQuery(query);
		List<String> shiftList = q.list();
		return shiftList;
	}

}
