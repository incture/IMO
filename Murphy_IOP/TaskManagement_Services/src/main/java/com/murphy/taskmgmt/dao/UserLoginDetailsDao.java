package com.murphy.taskmgmt.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.UserLoginDetailsDto;
import com.murphy.taskmgmt.entity.UpliftConfigMasterDo;
import com.murphy.taskmgmt.entity.UserLoginDetailsDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.service.GreetingFacade;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.weatherServices.util.ServicesUtil;

@Repository("UserLoginDetailsDao")
@Transactional
public class UserLoginDetailsDao extends BaseDao<UserLoginDetailsDo, UserLoginDetailsDto> {
	
	private static final Logger logger = LoggerFactory.getLogger(UserLoginDetailsDao.class);

	@Override
	protected UserLoginDetailsDo importDto(UserLoginDetailsDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		UserLoginDetailsDo entity = new UserLoginDetailsDo();
		entity.setUserEmail(fromDto.getUserEmail());
		entity.setLastLoginTime(fromDto.getLastLoginTime());
		return entity;
	}

	@Override
	protected UserLoginDetailsDto exportDto(UserLoginDetailsDo entity) {
		UserLoginDetailsDto dto = new UserLoginDetailsDto();
		dto.setUserEmail(entity.getUserEmail());
		dto.setLastLoginTime(entity.getLastLoginTime());
		return dto;
	}
	
	@Override
	public UserLoginDetailsDto getByKeys(UserLoginDetailsDto dto) throws NoResultFault{
		UserLoginDetailsDo entity = (UserLoginDetailsDo) getSession().get(UserLoginDetailsDo.class, dto.getUserEmail());
		if (!ServicesUtil.isEmpty(entity)) {
			dto = exportDto(entity);
		} else {
			throw new NoResultFault("UserLoginDetailsDao.getByKeys()::No Records found for the key id::"
					+ dto.getUserEmail());
		}
		return dto;
	}
	
	public String updateLastLogin(UserLoginDetailsDto dto){
		String response = MurphyConstant.FAILURE;
		try{
			saveOrUpdate(importDto(dto));
			response = MurphyConstant.SUCCESS;
		}catch(Exception e){
			logger.error("[Murphy][UserLoginDetailsDao][updateLastLogin] error : "+e.getMessage());
		}
		return response;
	}
	
}
