package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.DowntimeWellChildCodeDto;
import com.murphy.taskmgmt.dto.DowntimeWellParentCodeDto;
import com.murphy.taskmgmt.entity.DowntimeWellChildCodeDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("DowntimeWellChildCodeDao")
@Transactional
public class DowntimeWellChildCodeDao extends BaseDao<DowntimeWellChildCodeDo, DowntimeWellChildCodeDto> {

	@Override
	protected DowntimeWellChildCodeDo importDto(DowntimeWellChildCodeDto downtimeWellChildCodeDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		DowntimeWellChildCodeDo downtimeWellChildCodeDo = new DowntimeWellChildCodeDo();
		downtimeWellChildCodeDo.setChildCode(downtimeWellChildCodeDto.getChildCode());
		downtimeWellChildCodeDo.setChildCodeDescription(downtimeWellChildCodeDto.getChildCodeDescription());
		downtimeWellChildCodeDo.setParentCode(downtimeWellChildCodeDto.getParentCode());
		downtimeWellChildCodeDo.setActiveFlag(downtimeWellChildCodeDto.getActiveFlag());
		return downtimeWellChildCodeDo;
	}

	@Override
	protected DowntimeWellChildCodeDto exportDto(DowntimeWellChildCodeDo downtimeWellChildCodeDo) {
		DowntimeWellChildCodeDto downtimeWellChildCodeDto = new DowntimeWellChildCodeDto();
		downtimeWellChildCodeDto.setChildCode(downtimeWellChildCodeDo.getChildCode());
		downtimeWellChildCodeDto.setChildCodeDescription(downtimeWellChildCodeDo.getChildCodeDescription());
		downtimeWellChildCodeDto.setParentCode(downtimeWellChildCodeDo.getParentCode());
		downtimeWellChildCodeDto.setActiveFlag(downtimeWellChildCodeDo.getActiveFlag());
		return downtimeWellChildCodeDto;
	}

	@SuppressWarnings("unchecked")
	public List<DowntimeWellChildCodeDto> getActiveChildCodeForWellDowntime(String activeFlag) {
		List<DowntimeWellChildCodeDto> downtimeWellChildCodeDTOList = new ArrayList<DowntimeWellChildCodeDto>();
		

			String query = "select * from  DT_WELL_CHILD_CODE p where p.ACTIVE_FLAG='" + activeFlag + "'";
			Query q = this.getSession().createSQLQuery(query);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] object : resultList) {
					DowntimeWellChildCodeDto downtimeWellChildCodeDto = new DowntimeWellChildCodeDto();
					downtimeWellChildCodeDto.setChildCode(ServicesUtil.isEmpty(object[0]) ? null : (String) object[0]);
					downtimeWellChildCodeDto
							.setChildCodeDescription(ServicesUtil.isEmpty(object[1]) ? null : (String) object[1]);
					downtimeWellChildCodeDto.setParentCode(ServicesUtil.isEmpty(object[2]) ? null : (String) object[2]);
					downtimeWellChildCodeDto.setActiveFlag(ServicesUtil.isEmpty(object[3]) ? null : (String) object[3]);
					downtimeWellChildCodeDTOList.add(downtimeWellChildCodeDto);
				}
			}
		
		return downtimeWellChildCodeDTOList;
	}

	@SuppressWarnings("unchecked")
	public List<DowntimeWellChildCodeDto> getActiveChildCodeForWellDowntimeByParentCode(String activeFlag,
			String parentCode) {
		List<DowntimeWellChildCodeDto> downtimeWellChildCodeDTOList = new ArrayList<DowntimeWellChildCodeDto>();
			String query = "select parent_code,child_code,child_code_description,active_flag from  DT_WELL_CHILD_CODE p where p.PARENT_CODE='" + parentCode + "'"
					+ " and p.ACTIVE_FLAG='" + activeFlag + "'";
			Query q = this.getSession().createSQLQuery(query);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] object : resultList) {
					DowntimeWellChildCodeDto downtimeWellChildCodeDto = new DowntimeWellChildCodeDto();
					downtimeWellChildCodeDto.setChildCode(ServicesUtil.isEmpty(object[1]) ? null : (String) object[1]);
					downtimeWellChildCodeDto
							.setChildCodeDescription(ServicesUtil.isEmpty(object[2]) ? null : (String) object[2]);
					downtimeWellChildCodeDto.setParentCode(ServicesUtil.isEmpty(object[0]) ? null : (String) object[0]);
					downtimeWellChildCodeDto.setActiveFlag(ServicesUtil.isEmpty(object[3]) ? null : (String) object[3]);
					downtimeWellChildCodeDTOList.add(downtimeWellChildCodeDto);
				}
			}
		
		return downtimeWellChildCodeDTOList;
	}

	

}
