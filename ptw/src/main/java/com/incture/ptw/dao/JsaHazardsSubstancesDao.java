package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsSubstancesDto;

@Repository
public class JsaHazardsSubstancesDao extends BaseDao {

	public void insertJsaHazardsSubstances(String permitNumber, JsaHazardsSubstancesDto jsaHazardsSubstancesDto) {
		try {
			String sql = "INSERT INTO IOP.JSAHAZARDSSUBSTANCES VALUES (?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsSubstancesDto.getHazardousSubstances());
			query.setParameter(3, jsaHazardsSubstancesDto.getDrainEquipment());
			query.setParameter(4, jsaHazardsSubstancesDto.getFollowSdsControls());
			query.setParameter(5, jsaHazardsSubstancesDto.getImplementHealthHazardControls());
			query.setParameter(6, jsaHazardsSubstancesDto.getTestMaterial());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsSubstancesDto getJsaHazardsSubstances(String permitNum){
		List<Object[]> obj;
		JsaHazardsSubstancesDto jsaHazardsSubstancesDto = new JsaHazardsSubstancesDto();
		try{
			String sql = "select distinct PERMITNUMBER, HAZARDOUSSUBSTANCES,DRAINEQUIPMENT,FOLLOWSDSCONTROLS, "
					+ " IMPLEMENTHEALTHHAZARDCONTROLS,TESTMATERIAL from IOP.JSAHAZARDSSUBSTANCES "
					+ " where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			
			for (Object[] a : obj) {
				jsaHazardsSubstancesDto.setPermitNumber((Integer) a[0]);
				jsaHazardsSubstancesDto.setHazardousSubstances(Integer.parseInt(a[1].toString()));
				jsaHazardsSubstancesDto.setDrainEquipment(Integer.parseInt(a[2].toString()));
				jsaHazardsSubstancesDto.setFollowSdsControls(Integer.parseInt(a[3].toString()));
				jsaHazardsSubstancesDto.setImplementHealthHazardControls(Integer.parseInt(a[4].toString()));
				jsaHazardsSubstancesDto.setTestMaterial(Integer.parseInt(a[5].toString()));
			}
			return jsaHazardsSubstancesDto;
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void updateJsaHazardsSubstances(JsaHazardsSubstancesDto jsaHazardsSubstancesDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSSUBSTANCES\" SET  \"HAZARDOUSSUBSTANCES\"=?,\"DRAINEQUIPMENT\"=?,\"FOLLOWSDSCONTROLS\"=?," +
        "\"IMPLEMENTHEALTHHAZARDCONTROLS\"=?,\"TESTMATERIAL\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsaHazardsSubstances sql" + sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, jsaHazardsSubstancesDto.getHazardousSubstances());
			query.setParameter(2, jsaHazardsSubstancesDto.getDrainEquipment());
			query.setParameter(3, jsaHazardsSubstancesDto.getFollowSdsControls());
			query.setParameter(4, jsaHazardsSubstancesDto.getImplementHealthHazardControls());
			query.setParameter(5, jsaHazardsSubstancesDto.getTestMaterial());
			query.setParameter(6, jsaHazardsSubstancesDto.getPermitNumber());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
