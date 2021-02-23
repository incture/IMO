package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.FracScenarioLookUpDto;
import com.murphy.taskmgmt.entity.FracScenarioLookUpDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("FracScenarioLookUpDao")
@Transactional
public class FracScenarioLookUpDao extends BaseDao<FracScenarioLookUpDo, FracScenarioLookUpDto> {

	@Override
	protected FracScenarioLookUpDo importDto(FracScenarioLookUpDto fracScenarioLookUpDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		FracScenarioLookUpDo fracScenarioLookUpDo = new FracScenarioLookUpDo();
		fracScenarioLookUpDo.setScenario(fracScenarioLookUpDto.getScenario());
		fracScenarioLookUpDo.setDependentValue(fracScenarioLookUpDto.getDependentValue());
		fracScenarioLookUpDo.setActiveFlag(fracScenarioLookUpDto.getActiveFlag());
		return fracScenarioLookUpDo;

	}

	@Override
	protected FracScenarioLookUpDto exportDto(FracScenarioLookUpDo fracScenarioLookUpDo) {
		FracScenarioLookUpDto fracScenarioLookUpDto = new FracScenarioLookUpDto();
		fracScenarioLookUpDto.setScenario(fracScenarioLookUpDo.getScenario());
		fracScenarioLookUpDto.setDependentValue(fracScenarioLookUpDo.getDependentValue());
		fracScenarioLookUpDto.setActiveFlag(fracScenarioLookUpDo.getActiveFlag());
		return fracScenarioLookUpDto;

	}

	@SuppressWarnings("unchecked")
	public List<FracScenarioLookUpDto> getActiveFracScenarios(String activeFlag) {
		List<FracScenarioLookUpDto> fracScenarioDTOList = new ArrayList<FracScenarioLookUpDto>();

		String query = "select * from  FRAC_SCENARIO_LOOK_UP p where p.ACTIVE_FLAG='" + activeFlag + "'";
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			for (Object[] object : resultList) {
				FracScenarioLookUpDto fracScenarioLookUpDto = new FracScenarioLookUpDto();
				fracScenarioLookUpDto.setScenario(ServicesUtil.isEmpty(object[0]) ? null : (String) object[0]);
				fracScenarioLookUpDto.setDependentValue(ServicesUtil.isEmpty(object[1]) ? null : (String) object[1]);
				fracScenarioLookUpDto.setActiveFlag(ServicesUtil.isEmpty(object[2]) ? null : (String) object[2]);

				fracScenarioDTOList.add(fracScenarioLookUpDto);
			}
		}

		return fracScenarioDTOList;
	}

}
