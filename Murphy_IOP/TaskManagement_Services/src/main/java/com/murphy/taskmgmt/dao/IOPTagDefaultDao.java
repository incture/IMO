package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.IOPTagDefaultDto;
import com.murphy.taskmgmt.dto.PlotlyRequestDto;
import com.murphy.taskmgmt.entity.IOPTagDefaultDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("IOPTagDefaultDao")
public class IOPTagDefaultDao extends BaseDao<IOPTagDefaultDo,IOPTagDefaultDto> {

	public static final Logger logger = LoggerFactory.getLogger(IOPTagDefaultDao.class);

	@Autowired
	SessionFactory sessionfactory;
	
	@Override
	protected IOPTagDefaultDo importDto(IOPTagDefaultDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		
		IOPTagDefaultDo entity=new IOPTagDefaultDo();
		
		if (!ServicesUtil.isEmpty(fromDto.getTagId())) {
			entity.setTagId(fromDto.getTagId());
		}
		if (!ServicesUtil.isEmpty(fromDto.getTagName())) {
			entity.setTagName(fromDto.getTagName());
		}
		if (!ServicesUtil.isEmpty(fromDto.getDisplayName())) {
			entity.setDisplayName(fromDto.getDisplayName());
		}
		if (!ServicesUtil.isEmpty(fromDto.getAggregationName())) {
			entity.setAggregation(fromDto.getAggregationName());
		}
		if (!ServicesUtil.isEmpty(fromDto.getUnit())) {
			entity.setUnit(fromDto.getUnit());
		}
		
		return entity;
	}

	@Override
	protected IOPTagDefaultDto exportDto(IOPTagDefaultDo entity) {
		
		IOPTagDefaultDto dto=new IOPTagDefaultDto();
		
		if (!ServicesUtil.isEmpty(entity.getTagId())) {
			dto.setTagId(entity.getTagId());
		}
		if (!ServicesUtil.isEmpty(entity.getTagName())) {
			dto.setTagName(entity.getTagName());
		}
		if (!ServicesUtil.isEmpty(entity.getDisplayName())) {
			dto.setDisplayName(entity.getDisplayName());
		}
		if (!ServicesUtil.isEmpty(entity.getAggregation())) {
			dto.setAggregationName(entity.getAggregation());
		}
		if (!ServicesUtil.isEmpty(entity.getUnit())) {
			dto.setUnit(entity.getUnit());
		}
		
		return dto;
	}

	@SuppressWarnings("unchecked")
	public List<IOPTagDefaultDto> fetchTagNamesbyReportID(PlotlyRequestDto plotDto) throws Exception  {
		List<IOPTagDefaultDto> tagDefaultDtoList=null;
		IOPTagDefaultDto tagDefaultDto=null;
		String fetchQuery="select t2.TAG_NAME,t2.DISPLAY_NAME,t2.UNIT,t1.TAG_ID,t1.Y_AXIS,t1.REPORT_NAME from IOP_REPORT_TAG_MAPPING as t1 " + "left join "
				+ "IOP_TAG_DEFAULT as t2 on t1.TAG_ID=t2.TAG_ID  where t1.REPORT_ID='"
				+ plotDto.getReportId() + "' AND LOCATION_CODE='"+plotDto.getLocation()+"'";
		
		Query query = this.getSession().createSQLQuery(fetchQuery);
		List<Object[]> resultList = query.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			tagDefaultDtoList=new ArrayList<IOPTagDefaultDto>();
			for (Object[] obj : resultList) {
				tagDefaultDto = new IOPTagDefaultDto();
				tagDefaultDto.setTagName(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
				tagDefaultDto.setDisplayName(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
				tagDefaultDto.setUnit(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
				tagDefaultDto.setTagId(ServicesUtil.isEmpty(obj[3]) ? null : (Integer) obj[3]);
				tagDefaultDto.setyAxis(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
				tagDefaultDto.setReportName(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
				tagDefaultDto.setMuwi(plotDto.getMuwi());
				tagDefaultDto.setWellName(plotDto.getWellName());
				tagDefaultDto.setReportId(plotDto.getReportId());
				tagDefaultDtoList.add(tagDefaultDto);

			}
			logger.error("[Murphy][IOPTagDefaultDao][fetchTagNamesbyReportID][tagIdList]" + tagDefaultDtoList.toString());
		}

		return tagDefaultDtoList;
		
	}
	
	
	//Fetch Ymin and Ymax range
	public Map<String,String> fetchYminYmaxByunit() throws Exception{
		Map<String, String> tagMappingUnits=new HashMap<String,String>();
		try{
			String fetchUnits="SELECT UNIT,PLOT_MIN,PLOT_MAX FROM TAG_UNIT_RANGE";
			Query query = this.getSession().createSQLQuery(fetchUnits);
			logger.error("[Murphy][CRT][CRTDefaultTagDao][fetchYminYmaxByunit][queryString]" + fetchUnits);
			List<Object[]> resultList = query.list();
			if(!ServicesUtil.isEmpty(resultList)){
				for (Object[] obj : resultList) {
					tagMappingUnits.put(ServicesUtil.isEmpty(obj[0]) ? "" : (String) obj[0], (String)obj[1]+"#&#"+(String)obj[2]);
				}
				return tagMappingUnits;
			}
			
		}catch(Exception e){
			logger.error("[Murphy][IOPTagDefaultDao][fetchYminYmaxByunit][Exception] " + e.getMessage());
            e.printStackTrace();
			throw e;
		}
		return tagMappingUnits;
	}

	
}
