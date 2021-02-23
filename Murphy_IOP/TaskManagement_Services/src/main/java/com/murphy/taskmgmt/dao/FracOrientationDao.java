package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.FracOrientationDto;
import com.murphy.taskmgmt.entity.FracOrientationDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("FracOrientationDao")
@Transactional
public class FracOrientationDao extends BaseDao<FracOrientationDo, FracOrientationDto> {

	@Override
	protected FracOrientationDo importDto(FracOrientationDto fracOrientationDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		FracOrientationDo fracOrientationDo = new FracOrientationDo();
		fracOrientationDo.setOrientation(fracOrientationDto.getOrientation());
		fracOrientationDo.setDependentValue(fracOrientationDto.getDependentValue());
		fracOrientationDo.setActiveFlag(fracOrientationDto.getActiveFlag());
		return fracOrientationDo;
	}

	@Override
	protected FracOrientationDto exportDto(FracOrientationDo fracOrientationDo) {
		FracOrientationDto fracOrientationDto = new FracOrientationDto();
		fracOrientationDto.setOrientation(fracOrientationDo.getOrientation());
		fracOrientationDto.setDependentValue(fracOrientationDo.getDependentValue());
		fracOrientationDto.setActiveFlag(fracOrientationDo.getActiveFlag());
		return fracOrientationDto;
	}

	@SuppressWarnings("unchecked")
	public List<FracOrientationDto> getActiveFracOrientation(String activeFlag) {
		List<FracOrientationDto> fracOrientationDTOList = new ArrayList<FracOrientationDto>();
		String query = "select * from  FRAC_ORIENTATION p where p.ACTIVE_FLAG='" + activeFlag + "'";
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			for (Object[] object : resultList) {
				FracOrientationDto fracOrientationDto = new FracOrientationDto();
				fracOrientationDto.setOrientation(ServicesUtil.isEmpty(object[0]) ? null : (String) object[0]);
				fracOrientationDto.setDependentValue(ServicesUtil.isEmpty(object[1]) ? null : (String) object[1]);
				fracOrientationDto.setActiveFlag(ServicesUtil.isEmpty(object[2]) ? null : (String) object[2]);

				fracOrientationDTOList.add(fracOrientationDto);
			}
		}

		return fracOrientationDTOList;
	}

}
