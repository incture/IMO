package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.FracWellStatusDto;
import com.murphy.taskmgmt.entity.FracWellStatusDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("FracWellStatusDao")
@Transactional
public class FracWellStatusDao extends BaseDao<FracWellStatusDo, FracWellStatusDto> {

	@Override
	protected FracWellStatusDo importDto(FracWellStatusDto fracWellStatusDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		FracWellStatusDo fracWellStatusDo = new FracWellStatusDo();
		fracWellStatusDo.setWellStatus(fracWellStatusDto.getWellStatus());
		fracWellStatusDo.setDependentValue(fracWellStatusDto.getDependentValue());
		fracWellStatusDo.setActiveFlag(fracWellStatusDto.getActiveFlag());
		return fracWellStatusDo;
	}

	@Override
	protected FracWellStatusDto exportDto(FracWellStatusDo fracWellStatusDo) {
		FracWellStatusDto fracWellStatusDto = new FracWellStatusDto();
		fracWellStatusDto.setWellStatus(fracWellStatusDo.getWellStatus());
		fracWellStatusDto.setDependentValue(fracWellStatusDo.getDependentValue());
		fracWellStatusDto.setActiveFlag(fracWellStatusDo.getActiveFlag());
		return fracWellStatusDto;
	}

	@SuppressWarnings("unchecked")
	public List<FracWellStatusDto> getActiveFracWellStatus(String activeFlag) {
		List<FracWellStatusDto> fracWellStatusDTOList = new ArrayList<FracWellStatusDto>();

		String query = "select * from  FRAC_WELL_STATUS p where p.ACTIVE_FLAG='" + activeFlag + "'";
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			for (Object[] object : resultList) {
				FracWellStatusDto fracWellStatusDto = new FracWellStatusDto();
				fracWellStatusDto.setWellStatus(ServicesUtil.isEmpty(object[0]) ? null : (String) object[0]);
				fracWellStatusDto.setDependentValue(ServicesUtil.isEmpty(object[1]) ? null : (String) object[1]);
				fracWellStatusDto.setActiveFlag(ServicesUtil.isEmpty(object[2]) ? null : (String) object[2]);

				fracWellStatusDTOList.add(fracWellStatusDto);
			}
		}

		return fracWellStatusDTOList;
	}
}
