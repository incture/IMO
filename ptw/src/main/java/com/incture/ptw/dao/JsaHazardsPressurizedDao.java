package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsPressurizedDto;

@Repository
public class JsaHazardsPressurizedDao extends BaseDao {
	public void insertJsaHazardsPressurized(String permitNumber, JsaHazardsPressurizedDto jsaHazardsPressurizedDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSPRESSURIZED\" VALUES (?,?,?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsPressurizedDto.getPresurizedEquipment());
			query.setParameter(3, jsaHazardsPressurizedDto.getPerformIsolation());
			query.setParameter(4, jsaHazardsPressurizedDto.getDepressurizeDrain());
			query.setParameter(5, jsaHazardsPressurizedDto.getRelieveTrappedPressure());
			query.setParameter(6, jsaHazardsPressurizedDto.getDoNotWorkInLineOfFire());
			query.setParameter(7, jsaHazardsPressurizedDto.getAnticipateResidual());
			query.setParameter(8, jsaHazardsPressurizedDto.getSecureAllHoses());

			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void updateJsaHazardsPressurized(JsaHazardsPressurizedDto jsaHazardsPressurizedDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSPRESSURIZED\" SET  \"PRESURIZEDEQUIPMENT\"=?,\"PERFORMISOLATION\"=?,\"DEPRESSURIZEDRAIN\"=?," +
        "\"RELIEVETRAPPEDPRESSURE\"=?,\"DONOTWORKINLINEOFFIRE\"=?,\"ANTICIPATERESIDUAL\"=?,\"SECUREALLHOSES\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsaHazardsPressurized sql" + sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, jsaHazardsPressurizedDto.getPresurizedEquipment());
			query.setParameter(2, jsaHazardsPressurizedDto.getPerformIsolation());
			query.setParameter(3, jsaHazardsPressurizedDto.getDepressurizeDrain());
			query.setParameter(4, jsaHazardsPressurizedDto.getRelieveTrappedPressure());
			query.setParameter(5, jsaHazardsPressurizedDto.getDoNotWorkInLineOfFire());
			query.setParameter(6, jsaHazardsPressurizedDto.getAnticipateResidual());
			query.setParameter(7, jsaHazardsPressurizedDto.getSecureAllHoses());
			query.setParameter(8, jsaHazardsPressurizedDto.getPermitNumber());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
