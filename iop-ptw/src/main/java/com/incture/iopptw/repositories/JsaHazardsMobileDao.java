package com.incture.iopptw.repositories;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsMobileDto;

@Repository
public class JsaHazardsMobileDao extends BaseDao {

	public void insertJsaHazardsMobile(String permitNumber, JsaHazardsMobileDto jsaHazardsMobileDto) {
		try {
			logger.info("JsaHazardsMobileDto: " + jsaHazardsMobileDto);
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSMOBILE\" VALUES (?,?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsMobileDto.getMobileEquipment());
			query.setParameter(3, jsaHazardsMobileDto.getAssessEquipmentCondition());
			query.setParameter(4, jsaHazardsMobileDto.getControlAccess());
			query.setParameter(5, jsaHazardsMobileDto.getMonitorProximity());
			query.setParameter(6, jsaHazardsMobileDto.getManageOverheadHazards());
			query.setParameter(7, jsaHazardsMobileDto.getAdhereToRules());
			logger.info("sql " + sql);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}

	public void updateJsaHazardsMobile(JsaHazardsMobileDto jsaHazardsMobileDto) {
		try {
			String sql = "UPDATE IOP.JSAHAZARDSMOBILE SET  MOBILEEQUIPMENT=?,ASSESSEQUIPMENTCONDITION=?,CONTROLACCESS=?,"
					+ " MONITORPROXIMITY=?,MANAGEOVERHEADHAZARDS=?,ADHERETORULES=? WHERE PERMITNUMBER=?";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, jsaHazardsMobileDto.getMobileEquipment());
			query.setParameter(2, jsaHazardsMobileDto.getAssessEquipmentCondition());
			query.setParameter(3, jsaHazardsMobileDto.getControlAccess());
			query.setParameter(4, jsaHazardsMobileDto.getMonitorProximity());
			query.setParameter(5, jsaHazardsMobileDto.getManageOverheadHazards());
			query.setParameter(6, jsaHazardsMobileDto.getAdhereToRules());
			query.setParameter(7, jsaHazardsMobileDto.getPermitNumber());
			logger.info("sql " + sql);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}
	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsMobileDto getJsaHazardsMobileDto(String permitNum){
		List<Object[]> obj;
		JsaHazardsMobileDto jsaHazardsMobileDto = new JsaHazardsMobileDto();
		try{
			
			String sql = "select distinct PERMITNUMBER, MOBILEEQUIPMENT,ASSESSEQUIPMENTCONDITION,CONTROLACCESS, "
					+ " MONITORPROXIMITY,MANAGEOVERHEADHAZARDS,ADHERETORULES from IOP.JSAHAZARDSMOBILE "
					+ " where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			
			for (Object[] a : obj) {
				jsaHazardsMobileDto.setPermitNumber((Integer) a[0]);
				jsaHazardsMobileDto.setMobileEquipment(Integer.parseInt(a[1].toString()));
				jsaHazardsMobileDto.setAssessEquipmentCondition(Integer.parseInt(a[2].toString()));
				jsaHazardsMobileDto.setControlAccess(Integer.parseInt(a[3].toString()));
				jsaHazardsMobileDto.setMonitorProximity(Integer.parseInt(a[4].toString()));
				jsaHazardsMobileDto.setManageOverheadHazards(Integer.parseInt(a[5].toString()));
				jsaHazardsMobileDto.setAdhereToRules(Integer.parseInt(a[6].toString()));
			}
			return jsaHazardsMobileDto;
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
