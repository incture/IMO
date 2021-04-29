package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import com.incture.ptw.dto.JsaHazardsMovingDto;

@Repository
public class JsaHazardsMovingDao extends BaseDao {
	public void insertJsaHazardsMoving(String permitNumber, JsaHazardsMovingDto jsaHazardsMovingDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSMOVING\" VALUES (?,?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsMovingDto.getMovingEquipment());
			query.setParameter(3, jsaHazardsMovingDto.getConfirmMachineryIntegrity());
			query.setParameter(4, jsaHazardsMovingDto.getProvideProtectiveBarriers());
			query.setParameter(5, jsaHazardsMovingDto.getObserverToMonitorProximityPeopleAndEquipment());
			query.setParameter(6, jsaHazardsMovingDto.getLockOutEquipment());
			query.setParameter(7, jsaHazardsMovingDto.getDoNotWorkInLineOfFire());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsMovingDto getJsaHazardsMovingDto(String permitNum){
		List<Object[]> obj;
		JsaHazardsMovingDto jsaHazardsMovingDto = new JsaHazardsMovingDto();
		try{
			String sql = "select distinct PERMITNUMBER, MOVINGEQUIPMENT,CONFIRMMACHINERYINTEGRITY, "
					+ " PROVIDEPROTECTIVEBARRIERS,OBSERVERTOMONITORPROXIMITYPEOPLEANDEQUIPMENT,LOCKOUTEQUIPMENT, "
					+ " DONOTWORKINLINEOFFIRE from IOP.JSAHAZARDSMOVING where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			
			for (Object[] a : obj) {
				jsaHazardsMovingDto.setPermitNumber((Integer) a[0]);
				jsaHazardsMovingDto.setMovingEquipment(Integer.parseInt(a[1].toString()));
				jsaHazardsMovingDto.setConfirmMachineryIntegrity(Integer.parseInt(a[2].toString()));
				jsaHazardsMovingDto.setProvideProtectiveBarriers(Integer.parseInt(a[3].toString()));
				jsaHazardsMovingDto.setObserverToMonitorProximityPeopleAndEquipment(Integer.parseInt(a[4].toString()));
				jsaHazardsMovingDto.setLockOutEquipment(Integer.parseInt(a[5].toString()));
				jsaHazardsMovingDto.setDoNotWorkInLineOfFire(Integer.parseInt(a[6].toString()));
			}
			return jsaHazardsMovingDto;
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void updateJsaHazardsMoving(JsaHazardsMovingDto jsaHazardsMovingDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSMOVING\" SET  \"MOVINGEQUIPMENT\"=?,\"CONFIRMMACHINERYINTEGRITY\"=?,\"PROVIDEPROTECTIVEBARRIERS\"=?," +
        "\"OBSERVERTOMONITORPROXIMITYPEOPLEANDEQUIPMENT\"=?,\"LOCKOUTEQUIPMENT\"=?,\"DONOTWORKINLINEOFFIRE\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsaHazardsMoving sql" + sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, jsaHazardsMovingDto.getMovingEquipment());
			query.setParameter(2, jsaHazardsMovingDto.getConfirmMachineryIntegrity());
			query.setParameter(3, jsaHazardsMovingDto.getProvideProtectiveBarriers());
			query.setParameter(4, jsaHazardsMovingDto.getObserverToMonitorProximityPeopleAndEquipment());
			query.setParameter(5, jsaHazardsMovingDto.getLockOutEquipment());
			query.setParameter(6, jsaHazardsMovingDto.getDoNotWorkInLineOfFire());
			query.setParameter(7, jsaHazardsMovingDto.getPermitNumber());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
