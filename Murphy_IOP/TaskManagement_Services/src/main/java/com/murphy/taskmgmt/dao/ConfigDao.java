package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.BaseDto;
import com.murphy.taskmgmt.dto.ConfigDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.BaseDo;
import com.murphy.taskmgmt.entity.ConfigValuesDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("ConfigDao")
public class ConfigDao extends BaseDao<BaseDo, BaseDto> {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigDao.class);

	@Override
	protected BaseDo importDto(BaseDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		return null;
	}

	@Override
	protected BaseDto exportDto(BaseDo entity) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ConfigDto> getConfigurations(String configType) {

		List<ConfigDto> configDtos = null;
		ConfigDto configDto = null;

		String qryString = "SELECT TC.CONFIG_ID, TC.CONFIG_TYPE, TC.CONFIG_DESC, TCV.CONFIG_DESC_VALUE FROM TM_CONFIG TC JOIN TM_CONFIG_VALUES TCV ON TC.CONFIG_ID = TCV.CONFIG_ID WHERE TC.CONFIG_TYPE = '"
				+ configType + "'";
		Query query = this.getSession().createSQLQuery(qryString);
		List<Object[]> resultList = query.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			configDtos = new ArrayList<ConfigDto>();
			for (Object[] result : resultList) {
				configDto = new ConfigDto();
				configDto.setConfigId(ServicesUtil.isEmpty(result[0]) ? null : (String) result[0]);
//				configDto.setConfigType(ServicesUtil.isEmpty(result[1]) ? null : (String) result[1]);
//				configDto.setConfigDesc(ServicesUtil.isEmpty(result[2]) ? null : (String) result[2]);
				configDto.setConfigValue(ServicesUtil.isEmpty(result[3]) ? null : (String) result[3]);
				configDtos.add(configDto);
			}
		}
		return configDtos;
	}

	@SuppressWarnings("unchecked")
	public String getConfigurationByRef(String configRef) {
		String qryString = "SELECT TCV.CONFIG_ID, TCV.CONFIG_DESC_VALUE FROM TM_CONFIG_VALUES TCV WHERE TCV.CONFIG_ID IN ('"
				+ configRef + "')";
		Query query = this.getSession().createSQLQuery(qryString);
		List<Object[]> resultList = query.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			Object[] result = resultList.get(0);
			return ServicesUtil.isEmpty(result[1]) ? null : (String) result[1];
		}
		return null;
	}
	
	public ResponseMessage saveOrUpdateConfigByRef(String configRef, String configNewValue) {
		ConfigValuesDo configValuesDo = null;
		Session session = null;
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseMessage.setMessage(MurphyConstant.MAND_MISS);
		if(!ServicesUtil.isEmpty(configRef) && !ServicesUtil.isEmpty(configNewValue)) {
			session = this.getSession();
			configValuesDo = new ConfigValuesDo();
			configValuesDo.setConfigId(configRef);
			configValuesDo.setConfigValue(configNewValue);
			try {
				session.saveOrUpdate(configValuesDo);
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				responseMessage.setMessage("Save or update successful");
			} catch (Exception ex) {
				responseMessage.setMessage("Exception while save or update : " + ex.getMessage());
				logger.error("[ConfigDao][updateConfigurationByRef][Exception] : "+ex);
			}
		}
		return responseMessage;
	}
	
	public ResponseMessage deleteConfigurationByRef(String configRef) {
		ConfigValuesDo configValuesDo = null;
		Session session = null;
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseMessage.setMessage(MurphyConstant.MAND_MISS);
		
		if(!ServicesUtil.isEmpty(configRef)) {
			session = this.getSession();
			configValuesDo = new ConfigValuesDo();
			configValuesDo.setConfigId(configRef);
			try {
				session.delete(configValuesDo);
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				responseMessage.setMessage("Delete successful");
			} catch (Exception ex) {
				responseMessage.setMessage("Exception while delete : " + ex.getMessage());
				logger.error("[ConfigDao][deleteConfigurationByRef][Exception] : "+ex);
			}
		}
		return responseMessage;
	}
	
	
}