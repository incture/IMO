package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsExcavationdDto;

@Repository
public class JsaHazardsExcavationdDao extends BaseDao {

	public void insertJsaHazardsExcavation(String permitNumber, JsaHazardsExcavationdDto jsaHazardsExcavationdDto) {
		try {
			logger.info("JsaHazardsExcavationdDto: " + jsaHazardsExcavationdDto);
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSEXCAVATION\" VALUES (?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsExcavationdDto.getExcavations());
			query.setParameter(3, jsaHazardsExcavationdDto.getHaveExcavationPlan());
			query.setParameter(4, jsaHazardsExcavationdDto.getLocatePipesByHandDigging());
			query.setParameter(5, jsaHazardsExcavationdDto.getDeEnergizeUnderground());
			query.setParameter(6, jsaHazardsExcavationdDto.getCseControls());
			logger.info("sql " + sql);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}

	public void updateJsaHazardsExcavation(JsaHazardsExcavationdDto jsaHazardsExcavationdDto) {
		try {
			logger.info("JsaHazardsExcavationdDto: " + jsaHazardsExcavationdDto);
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSEXCAVATION\" SET  \"EXCAVATIONS\"=?,\"HAVEEXCAVATIONPLAN\"=?,\"LOCATEPIPESBYHANDDIGGING\"=?,"
					+ "\"DEENERGIZEUNDERGROUND\"=?,\"CSECONTROLS\"=? WHERE \"PERMITNUMBER\"=?";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, jsaHazardsExcavationdDto.getExcavations());
			query.setParameter(2, jsaHazardsExcavationdDto.getHaveExcavationPlan());
			query.setParameter(3, jsaHazardsExcavationdDto.getLocatePipesByHandDigging());
			query.setParameter(4, jsaHazardsExcavationdDto.getDeEnergizeUnderground());
			query.setParameter(5, jsaHazardsExcavationdDto.getCseControls());
			query.setParameter(6, jsaHazardsExcavationdDto.getPermitNumber());
			logger.info("updateJsaHazardsExcavation sql " + sql);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();

		}

	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsExcavationdDto getJsaHazardsExcavationdDto(String permitNum){
		List<Object[]> obj;
		JsaHazardsExcavationdDto jsaHazardsExcavationdDto = new JsaHazardsExcavationdDto();
		try{
			String sql = "select distinct PERMITNUMBER, EXCAVATIONS,HAVEEXCAVATIONPLAN,LOCATEPIPESBYHANDDIGGING, "
					+ " DEENERGIZEUNDERGROUND,CSECONTROLS from IOP.JSAHAZARDSEXCAVATION "
					+ " where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			
			for (Object[] a : obj) {
				jsaHazardsExcavationdDto.setPermitNumber((Integer) a[0]);
				jsaHazardsExcavationdDto.setExcavations(Integer.parseInt(a[1].toString()));
				jsaHazardsExcavationdDto.setHaveExcavationPlan(Integer.parseInt(a[2].toString()));
				jsaHazardsExcavationdDto.setLocatePipesByHandDigging(Integer.parseInt(a[3].toString()));
				jsaHazardsExcavationdDto.setDeEnergizeUnderground(Integer.parseInt(a[4].toString()));
				jsaHazardsExcavationdDto.setCseControls(Integer.parseInt(a[5].toString()));
			}
			return jsaHazardsExcavationdDto;
			
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
