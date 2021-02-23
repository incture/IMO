package com.murphy.taskmgmt.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.LocationTimeZoneMasterDto;
import com.murphy.taskmgmt.entity.LocationTimeZoneMasterDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("LocationTimeZoneMasterDao")
public class LocationTimeZoneMasterDao extends BaseDao<LocationTimeZoneMasterDo,LocationTimeZoneMasterDto>  {

	private static final Logger logger = LoggerFactory.getLogger(LocationTimeZoneMasterDao.class);

	
	@Override
	protected LocationTimeZoneMasterDo importDto(LocationTimeZoneMasterDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		LocationTimeZoneMasterDo entity=new LocationTimeZoneMasterDo();
		entity.setLocationCode(fromDto.getLocationCode());
		entity.setLocationText(fromDto.getLocationText());
		entity.setTimeZone(fromDto.getTimeZone());
		return entity;
	}

	@Override
	protected LocationTimeZoneMasterDto exportDto(LocationTimeZoneMasterDo entity) {
		LocationTimeZoneMasterDto fromDto=new LocationTimeZoneMasterDto();
		fromDto.setLocationCode(entity.getLocationCode());
		fromDto.setLocationText(entity.getLocationText());
		fromDto.setTimeZone(entity.getTimeZone());
		return fromDto;
	}
	
	    //Fetch Location TimeZone
		public Map<String,LocationTimeZoneMasterDto> fetchTimeZoneByLocation(){
			LocationTimeZoneMasterDto masterTimeZone=null;
			Map<String, LocationTimeZoneMasterDto> locationMasterTimeZoneDto=new HashMap<String,LocationTimeZoneMasterDto>();
			try{
				String fetchQuery="SELECT * FROM LOC_TIMEZONE_MASTER";
				Query query = this.getSession().createSQLQuery(fetchQuery);
				logger.error("[Murphy][LocationTimeZoneMasterDao][fetchLocTimeZone]" + fetchQuery);
				List<Object[]> resultList = query.list();
				if(!ServicesUtil.isEmpty(resultList)){
					for (Object[] obj : resultList) {
						masterTimeZone=new LocationTimeZoneMasterDto();
						masterTimeZone.setLocationCode(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
						masterTimeZone.setLocationText(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
						masterTimeZone.setTimeZone(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
						locationMasterTimeZoneDto.put(masterTimeZone.getLocationCode(),masterTimeZone);
					}
					return locationMasterTimeZoneDto;
				}
				
			}catch(Exception e){
				logger.error("[Murphy][IOPTagDefaultDao][fetchYminYmaxByunit][Exception] " + e.getMessage());
	            e.printStackTrace();
			}
			return locationMasterTimeZoneDto;
		}

}
