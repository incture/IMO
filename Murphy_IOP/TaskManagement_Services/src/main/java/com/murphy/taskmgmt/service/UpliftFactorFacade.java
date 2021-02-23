package com.murphy.taskmgmt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.geotab.Coordinates;
import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dao.UpliftConfigMasterDao;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.UpliftConfigMasterDto;
import com.murphy.taskmgmt.dto.UpliftFactorDto;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.service.interfaces.UpliftFactorFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;
import com.murphy.weatherServices.dto.WeatherDto;
import com.murphy.weatherServices.service.interfaces.WeatherFacadeLocal;

@Service("upliftFactorFacade")
public class UpliftFactorFacade implements UpliftFactorFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(UpliftFactorFacade.class);

	@Autowired
	private HierarchyDao hierarchyDao;

	@Autowired
	private WeatherFacadeLocal weatherFacade;

	@Autowired
	private UpliftConfigMasterDao upliftConfigMasterDao;

	@Override
	public UpliftFactorDto getUpliftFactor(String locationCode, Long startDate) {
		// Get location coordinates to fetch the weather information
		logger.error("[Murphy][UpliftFactorFacade][getUpliftFactor] locationCode : " + locationCode);
		Coordinates coords = hierarchyDao.getCoordByCode(locationCode);
		UpliftFactorDto upliftFactor = new UpliftFactorDto();
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage("Failed to fetch the uplift factor");
		resp.setStatus(MurphyConstant.FAILURE);
		resp.setStatusCode(MurphyConstant.CODE_FAILURE);
		upliftFactor.setValue(0.0);
		try {
			// If the start date is empty, get the current weather
			if (ServicesUtil.isEmpty(startDate)) {
				WeatherDto weatherDto = weatherFacade.getCurrentWeather(coords.getLatitude(), coords.getLongitude());
				if (!ServicesUtil.isEmpty(weatherDto)) {
					UpliftConfigMasterDto upliftDto = new UpliftConfigMasterDto();
					upliftDto.setWeatherConditionCode(weatherDto.getId());
					try {
						upliftDto = upliftConfigMasterDao.getByKeys(upliftDto);
						upliftFactor.setValue(upliftDto.getUpliftPercentage());
						
					} catch (NoResultFault e) {
						upliftFactor.setValue(0.0);
					}
					upliftFactor.setIcon(weatherDto.getIcon());
					logger.error("[Murphy][UpliftFactorFacade][getUpliftFactor] upliftDto : " + upliftDto.toString());
				}
			}else{
				Long currentTime = System.currentTimeMillis()/1000;
				if (Math.abs(startDate - currentTime) >= 3600 * 48) {
					List<WeatherDto> weatherDtos = weatherFacade.getDailyWeather(coords.getLatitude(), coords.getLongitude());
					for (WeatherDto dto : weatherDtos) {
						if (Math.abs(startDate - dto.getTime()) < 3600 * 24) {
							UpliftConfigMasterDto upliftDto = new UpliftConfigMasterDto();
							upliftDto.setWeatherConditionCode(dto.getId());
							try {
								upliftDto = upliftConfigMasterDao.getByKeys(upliftDto);
								upliftFactor.setValue(upliftDto.getUpliftPercentage());
							} catch (NoResultFault e) {
								upliftFactor.setValue(0.0);
							}
							upliftFactor.setIcon(dto.getIcon());
							logger.error("[Murphy][UpliftFactorFacade][getUpliftFactor] upliftDto : "
									+ upliftDto.toString());
							break;
						}
					}
				}else{
					List<WeatherDto> weatherDtos = weatherFacade.getHourlyWeather(coords.getLatitude(), coords.getLongitude());
					for(WeatherDto dto : weatherDtos){
						if (Math.abs(startDate - dto.getTime()) < 3600) {
							UpliftConfigMasterDto upliftDto = new UpliftConfigMasterDto();
							upliftDto.setWeatherConditionCode(dto.getId());
							try {
								upliftDto = upliftConfigMasterDao.getByKeys(upliftDto);
								upliftFactor.setValue(upliftDto.getUpliftPercentage());
							} catch (NoResultFault e) {
								upliftFactor.setValue(0.0);
							}
							upliftFactor.setIcon(dto.getIcon());
							logger.error("[Murphy][UpliftFactorFacade][getUpliftFactor] upliftDto : "
									+ upliftDto.toString());
							break;
						}
					}
				}
				
			}
			resp.setMessage("Uplift Factor fetched successfully");
			resp.setStatus(MurphyConstant.SUCCESS);
			resp.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][UpliftFactorFacade][getUpliftFactor] error : " + e.getMessage());
		}
		upliftFactor.setMessage(resp);
		return upliftFactor;
	}

}
