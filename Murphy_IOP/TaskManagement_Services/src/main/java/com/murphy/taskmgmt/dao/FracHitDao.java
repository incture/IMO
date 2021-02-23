package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.AlsInvestigationDto;
import com.murphy.taskmgmt.dto.FracHitDto;
import com.murphy.taskmgmt.dto.FracOrientationDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.entity.FracHitDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.service.PlotlyFacade;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("FracHitDao")
@Transactional
public class FracHitDao extends BaseDao<FracHitDo, FracHitDto> {

	private static final Logger logger = LoggerFactory.getLogger(FracHitDao.class);

	@Override
	protected FracHitDo importDto(FracHitDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		FracHitDo entity = new FracHitDo();
		entity.setFracId(fromDto.getFracId());
		entity.setMuwi(fromDto.getMuwi());
		entity.setFracHitTime(fromDto.getFracHitTime());
		return entity;
	}

	@Override
	protected FracHitDto exportDto(FracHitDo entity) {
		FracHitDto dto = new FracHitDto();
		dto.setFracId(entity.getFracId());
		dto.setMuwi(entity.getMuwi());
		dto.setFracHitTime(entity.getFracHitTime());
		return dto;
	}

	@SuppressWarnings("unchecked")
	public List<Date> getFracHitTimeByMuwi(String muwi) throws Exception {
		List<Date> fracDateList = null;
		String query = "SELECT FRAC_HIT_TIME FROM FRAC_HIT_TABLE WHERE MUWI='" + muwi
				+ "' ORDER BY FRAC_HIT_TIME DESC ";
		Query q = this.getSession().createSQLQuery(query);
		List<Object> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			fracDateList=new ArrayList<>();
			for (Object obj : resultList) {
		      Date fracDate = (Date) obj;
		      fracDateList.add(fracDate);
			}
			return fracDateList;

		}

		return fracDateList;
	}

	@SuppressWarnings("unchecked")
	public List<AlsInvestigationDto> fetchHistInvestigationDetails(String muwi) throws Exception {
		List<AlsInvestigationDto> alsDtoList = null;

//		String queryString = "SELECT * FROM (SELECT MUWI, DATE, REASON, SOURCE FROM ALS_INVESTIGATION " + "UNION ALL "
//				+ "SELECT MUWI, FRAC_HIT_TIME as DATE, 'Frac Hit' as REASON, 'IOP' as SOURCE "
//				+ "FROM FRAC_HIT_TABLE ) " + "WHERE MUWI='" + muwi + "'";
		
		String queryString= "SELECT DISTINCT DATE,MUWI,REASON,SOURCE FROM ALS_INVESTIGATION WHERE MUWI='"+muwi+"'";

		Query q = this.getSession().createSQLQuery(queryString);
		List<Object[]> resulList = (List<Object[]>) q.list();
		if (!ServicesUtil.isEmpty(resulList)) {
			alsDtoList = new ArrayList<AlsInvestigationDto>();
			for (Object[] obj : resulList) {
				AlsInvestigationDto dto = new AlsInvestigationDto();
				dto.setMuwId(ServicesUtil.isEmpty(obj[1]) ? "" : (String) obj[1]);
				dto.setDate(ServicesUtil.isEmpty(obj[0]) ? null : (Date) obj[0]);
				dto.setReason(ServicesUtil.isEmpty(obj[2]) ? "" : (String) obj[2]);
				dto.setSource(ServicesUtil.isEmpty(obj[3]) ? "" : (String) obj[3]);
				alsDtoList.add(dto);
			}
          return alsDtoList;
		}

		return alsDtoList;

	}

}
