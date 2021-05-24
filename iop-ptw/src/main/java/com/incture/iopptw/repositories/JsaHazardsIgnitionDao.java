package com.incture.iopptw.repositories;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsIgnitionDto;

@Repository
public class JsaHazardsIgnitionDao extends BaseDao {

	public void insertJsaHazardsIgnition(String permitNumber,JsaHazardsIgnitionDto jsaHazardsIgnitionDto) {
		try {
			String sql = "INSERT INTO IOP.JSAHAZARDSIGNITION VALUES (?,?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsIgnitionDto.getIgnitionSources());
			query.setParameter(3, jsaHazardsIgnitionDto.getRemoveCombustibleMaterials());
			query.setParameter(4, jsaHazardsIgnitionDto.getProvideFireWatch());
			query.setParameter(5, jsaHazardsIgnitionDto.getImplementAbrasiveBlastingControls());
			query.setParameter(6, jsaHazardsIgnitionDto.getConductContinuousGasTesting());
			query.setParameter(7, jsaHazardsIgnitionDto.getEarthForStaticElectricity());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsIgnitionDto getJsaHazardsIgnition(String permitNum){
		List<Object[]> obj;
		JsaHazardsIgnitionDto jsaHazardsIgnitionDto = new JsaHazardsIgnitionDto();
		try{
			String sql = "select distinct PERMITNUMBER,IGNITIONSOURCES,REMOVECOMBUSTIBLEMATERIALS,PROVIDEFIREWATCH, "
					+ " IMPLEMENTABRASIVEBLASTINGCONTROLS,CONDUCTCONTINUOUSGASTESTING,EARTHFORSTATICELECTRICITY "
					+ " from IOP.JSAHAZARDSIGNITION where PERMITNUMBER = :permitNum";
			logger.info("JSAHAZARDSIGNITION sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardsIgnitionDto.setPermitNumber((Integer) a[0]);
				jsaHazardsIgnitionDto.setIgnitionSources(Integer.parseInt(a[1].toString()));
				jsaHazardsIgnitionDto.setRemoveCombustibleMaterials(Integer.parseInt(a[2].toString()));
				jsaHazardsIgnitionDto.setProvideFireWatch(Integer.parseInt(a[3].toString()));
				jsaHazardsIgnitionDto.setImplementAbrasiveBlastingControls(Integer.parseInt(a[4].toString()));
				jsaHazardsIgnitionDto.setConductContinuousGasTesting(Integer.parseInt(a[5].toString()));
				jsaHazardsIgnitionDto.setEarthForStaticElectricity(Integer.parseInt(a[6].toString()));
			}
			return jsaHazardsIgnitionDto;
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void updateJsaHazardsIgnition(JsaHazardsIgnitionDto jsaHazardsIgnitionDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSIGNITION\" SET  \"IGNITIONSOURCES\"=?,\"REMOVECOMBUSTIBLEMATERIALS\"=?,\"PROVIDEFIREWATCH\"=?," +
        "\"IMPLEMENTABRASIVEBLASTINGCONTROLS\"=?,\"CONDUCTCONTINUOUSGASTESTING\"=?,\"EARTHFORSTATICELECTRICITY\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsaHazardsIgnition sql" + sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, jsaHazardsIgnitionDto.getIgnitionSources());
			query.setParameter(2, jsaHazardsIgnitionDto.getRemoveCombustibleMaterials());
			query.setParameter(3, jsaHazardsIgnitionDto.getProvideFireWatch());
			query.setParameter(4, jsaHazardsIgnitionDto.getImplementAbrasiveBlastingControls());
			query.setParameter(5, jsaHazardsIgnitionDto.getConductContinuousGasTesting());
			query.setParameter(6, jsaHazardsIgnitionDto.getEarthForStaticElectricity());
			query.setParameter(7, jsaHazardsIgnitionDto.getPermitNumber());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
