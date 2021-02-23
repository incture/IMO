package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.DowntimeWellParentCodeDto;
import com.murphy.taskmgmt.entity.DowntimeWellParentCodeDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("DowntimeWellParentCodeDao")
@Transactional
public class DowntimeWellParentCodeDao extends BaseDao<DowntimeWellParentCodeDo, DowntimeWellParentCodeDto> {

	@Override
	protected DowntimeWellParentCodeDo importDto(DowntimeWellParentCodeDto downtimeWellParentCodeDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		DowntimeWellParentCodeDo downtimeWellParentCodeDo = new DowntimeWellParentCodeDo();
		downtimeWellParentCodeDo.setParentCode(downtimeWellParentCodeDto.getParentCode());
		downtimeWellParentCodeDo.setParentCodeDescription(downtimeWellParentCodeDto.getParentCodeDescription());
		downtimeWellParentCodeDo.setDependentValue(downtimeWellParentCodeDto.getDependentValue());
		downtimeWellParentCodeDo.setActiveFlag(downtimeWellParentCodeDto.getActiveFlag());
		return downtimeWellParentCodeDo;
	}

	@Override
	protected DowntimeWellParentCodeDto exportDto(DowntimeWellParentCodeDo downtimeWellParentCodeDo) {
		DowntimeWellParentCodeDto downtimeWellParentCodeDto = new DowntimeWellParentCodeDto();
		downtimeWellParentCodeDto.setParentCode(downtimeWellParentCodeDo.getParentCode());
		downtimeWellParentCodeDto.setParentCodeDescription(downtimeWellParentCodeDo.getParentCodeDescription());
		downtimeWellParentCodeDto.setDependentValue(downtimeWellParentCodeDo.getDependentValue());
		downtimeWellParentCodeDto.setActiveFlag(downtimeWellParentCodeDo.getActiveFlag());
		return downtimeWellParentCodeDto;
	}

	@SuppressWarnings("unchecked")
	public List<DowntimeWellParentCodeDto> getActiveParenCodeForWellDowntime(String countryCode) {
		List<DowntimeWellParentCodeDto> downtimeWellParentCodeDTOList = new ArrayList<DowntimeWellParentCodeDto>();
		String query = "select * from  DT_WELL_PARENT_CODE p where p.ACTIVE_FLAG='" + MurphyConstant.ACTIVE + "'"
				+ " AND COUNTRY_CODE='" + countryCode + "'";
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			for (Object[] object : resultList) {
				DowntimeWellParentCodeDto downtimeWellParentCodeDto = new DowntimeWellParentCodeDto();
				downtimeWellParentCodeDto.setParentCode(ServicesUtil.isEmpty(object[0]) ? null : (String) object[0]);
				downtimeWellParentCodeDto
						.setParentCodeDescription(ServicesUtil.isEmpty(object[1]) ? null : (String) object[1]);
				downtimeWellParentCodeDto
						.setDependentValue(ServicesUtil.isEmpty(object[2]) ? null : (String) object[2]);
				downtimeWellParentCodeDto.setActiveFlag(ServicesUtil.isEmpty(object[3]) ? null : (String) object[3]);
				downtimeWellParentCodeDto.setCountryCode(ServicesUtil.isEmpty(object[4]) ? null : (String) object[4]);
				downtimeWellParentCodeDTOList.add(downtimeWellParentCodeDto);
			}
		}

		return downtimeWellParentCodeDTOList;

	}
	
	@SuppressWarnings("unchecked")
	public List<DowntimeWellParentCodeDto> getActiveParentCodeForCaWellDowntime() throws Exception{
		List<DowntimeWellParentCodeDto> downtimeWellParentCodeDTOList = new ArrayList<DowntimeWellParentCodeDto>();
		String query = "select * from  DT_WELL_PARENT_CODE p where p.ACTIVE_FLAG='" + MurphyConstant.ACTIVE+ "'"
				+ " AND COUNTRY_CODE='" + MurphyConstant.CA_CODE + "'";
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			for (Object[] object : resultList) {
				DowntimeWellParentCodeDto downtimeWellParentCodeDto = new DowntimeWellParentCodeDto();
				downtimeWellParentCodeDto.setParentCode(ServicesUtil.isEmpty(object[0]) ? null : (String) object[0]);
				downtimeWellParentCodeDto
						.setParentCodeDescription(ServicesUtil.isEmpty(object[1]) ? null : (String) object[1]);
				downtimeWellParentCodeDto
						.setDependentValue(ServicesUtil.isEmpty(object[2]) ? null : (String) object[2]);
				downtimeWellParentCodeDto.setActiveFlag(ServicesUtil.isEmpty(object[3]) ? null : (String) object[3]);
				downtimeWellParentCodeDto.setCountryCode(ServicesUtil.isEmpty(object[4]) ? null : (String) object[4]);
				downtimeWellParentCodeDTOList.add(downtimeWellParentCodeDto);
			}
		}

		return downtimeWellParentCodeDTOList;

	}
	
	

}
