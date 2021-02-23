package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.FracZoneDto;
import com.murphy.taskmgmt.entity.FracZoneDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("FracZoneDao")
@Transactional
public class FracZoneDao extends BaseDao<FracZoneDo, FracZoneDto> {

	@Override
	protected FracZoneDo importDto(FracZoneDto fracZoneDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		FracZoneDo fracZoneDo = new FracZoneDo();
		fracZoneDo.setZone(fracZoneDto.getZone());
		fracZoneDo.setDependentValue(fracZoneDto.getDependentValue());
		fracZoneDo.setActiveFlag(fracZoneDto.getActiveFlag());
		return fracZoneDo;
	}

	@Override
	protected FracZoneDto exportDto(FracZoneDo fracZoneDo) {
		FracZoneDto fracZoneDto = new FracZoneDto();
		fracZoneDto.setZone(fracZoneDo.getZone());
		fracZoneDto.setDependentValue(fracZoneDo.getDependentValue());
		fracZoneDto.setActiveFlag(fracZoneDo.getActiveFlag());
		return fracZoneDto;
	}

	@SuppressWarnings("unchecked")
	public List<FracZoneDto> getActiveFracZone(String activeFlag) {
		List<FracZoneDto> fracZoneDTOList = new ArrayList<FracZoneDto>();
		String query = "select * from  FRAC_ZONE p where p.ACTIVE_FLAG='" + activeFlag + "'";
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			for (Object[] object : resultList) {
				FracZoneDto fracZoneDto = new FracZoneDto();
				fracZoneDto.setZone(ServicesUtil.isEmpty(object[0]) ? null : (String) object[0]);
				fracZoneDto.setDependentValue(ServicesUtil.isEmpty(object[1]) ? null : (String) object[1]);
				fracZoneDto.setActiveFlag(ServicesUtil.isEmpty(object[2]) ? null : (String) object[2]);

				fracZoneDTOList.add(fracZoneDto);
			}
		}

		return fracZoneDTOList;
	}

}
