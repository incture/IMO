package com.incture.iopptw.repositories;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsElectricalDto;

@Repository
public class JsaHazardsElectricalDao extends BaseDao {
	public void insertJsaHazardsElectrical(String permitNumber, JsaHazardsElectricalDto jsaHazardsElectricalDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSELECTRICAL\" VALUES (?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsElectricalDto.getPortableElectricalEquipment());
			query.setParameter(3, jsaHazardsElectricalDto.getInspectToolsForCondition());
			query.setParameter(4, jsaHazardsElectricalDto.getImplementGasTesting());
			query.setParameter(5, jsaHazardsElectricalDto.getProtectElectricalLeads());
			query.setParameter(6, jsaHazardsElectricalDto.getIdentifyEquipClassification());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsElectricalDto getJsaHazardsElectricalDto(String permitNum){
		JsaHazardsElectricalDto jsaHazardsElectricalDto = new JsaHazardsElectricalDto();
		List<Object[]> obj;
		try{
			String sql = "select distinct PERMITNUMBER, PORTABLEELECTRICALEQUIPMENT,INSPECTTOOLSFORCONDITION, "
					+ " IMPLEMENTGASTESTING,PROTECTELECTRICALLEADS,IDENTIFYEQUIPCLASSIFICATION "
					+ " from IOP.JSAHAZARDSELECTRICAL where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardsElectricalDto.setPermitNumber((Integer) a[0]);
				jsaHazardsElectricalDto.setPortableElectricalEquipment(Integer.parseInt(a[1].toString()));
				jsaHazardsElectricalDto.setInspectToolsForCondition(Integer.parseInt(a[2].toString()));
				jsaHazardsElectricalDto.setImplementGasTesting(Integer.parseInt(a[3].toString()));
				jsaHazardsElectricalDto.setProtectElectricalLeads(Integer.parseInt(a[4].toString()));
				jsaHazardsElectricalDto.setIdentifyEquipClassification(Integer.parseInt(a[5].toString()));
			}
			return jsaHazardsElectricalDto;
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void updateJsaHazardsElectrical(JsaHazardsElectricalDto jsaHazardsElectricalDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSELECTRICAL\" SET  \"PORTABLEELECTRICALEQUIPMENT\"=?,\"INSPECTTOOLSFORCONDITION\"=?,\"IMPLEMENTGASTESTING\"=?," +
        "\"PROTECTELECTRICALLEADS\"=?,\"IDENTIFYEQUIPCLASSIFICATION\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsaHazardsElectrical sql" + sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, jsaHazardsElectricalDto.getPortableElectricalEquipment());
			query.setParameter(2, jsaHazardsElectricalDto.getInspectToolsForCondition());
			query.setParameter(3, jsaHazardsElectricalDto.getImplementGasTesting());
			query.setParameter(4, jsaHazardsElectricalDto.getProtectElectricalLeads());
			query.setParameter(5, jsaHazardsElectricalDto.getIdentifyEquipClassification());
			query.setParameter(6, jsaHazardsElectricalDto.getPermitNumber());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
