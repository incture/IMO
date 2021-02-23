package com.murphy.taskmgmt.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.UpliftConfigMasterDto;
import com.murphy.taskmgmt.entity.UpliftConfigMasterDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.weatherServices.util.ServicesUtil;

@Repository("UpliftConfigMasterDao")
@Transactional
public class UpliftConfigMasterDao extends BaseDao<UpliftConfigMasterDo, UpliftConfigMasterDto> {
	
	@Override
	protected UpliftConfigMasterDo importDto(UpliftConfigMasterDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		UpliftConfigMasterDo entity = new UpliftConfigMasterDo();
		entity.setWeatherConditionCode(fromDto.getWeatherConditionCode());
		entity.setDescription(fromDto.getDescription());
		entity.setUpliftPercentage(fromDto.getUpliftPercentage());
		return entity;
	}

	@Override
	protected UpliftConfigMasterDto exportDto(UpliftConfigMasterDo entity) {
		UpliftConfigMasterDto dto = new UpliftConfigMasterDto();
		dto.setWeatherConditionCode(entity.getWeatherConditionCode());
		dto.setDescription(entity.getDescription());
		dto.setUpliftPercentage(entity.getUpliftPercentage());
		return dto;
	}
	
	@Override
	public UpliftConfigMasterDto getByKeys(UpliftConfigMasterDto dto) throws NoResultFault {
		UpliftConfigMasterDo entity = (UpliftConfigMasterDo) getSession().get(UpliftConfigMasterDo.class,
				dto.getWeatherConditionCode());
		if (!ServicesUtil.isEmpty(entity)) {
			dto = exportDto(entity);
		} else {
			throw new NoResultFault("UpliftConfigMasterDao.getByKeys()::No Records found for the key id::"
					+ dto.getWeatherConditionCode());
		}
		return dto;

	}
	

}
