package com.incture.ptw.dao;

import javax.persistence.Query;

import com.incture.ptw.dto.JsaHazardsPressurizedDto;

public class JsaHazardsPressurizedDao extends BaseDao {
	public void insertJsaHazardsPressurized(JsaHazardsPressurizedDto jsaHazardsPressurizedDto) {
		Query query = getSession()
				.createNativeQuery("INSERT INTO \"IOP\".\"JSAHAZARDSPRESSURIZED\" VALUES (?,?,?,?,?,?,?,?)");
		query.setParameter(1, jsaHazardsPressurizedDto.getPermitNumber());
		query.setParameter(2, jsaHazardsPressurizedDto.getPresurizedEquipment());
		query.setParameter(3, jsaHazardsPressurizedDto.getPerformIsolation());
		query.setParameter(4, jsaHazardsPressurizedDto.getDepressurizeDrain());
		query.setParameter(5, jsaHazardsPressurizedDto.getRelieveTrappedPressure());
		query.setParameter(6, jsaHazardsPressurizedDto.getDoNotWorkInlineOfFire());
		query.setParameter(7, jsaHazardsPressurizedDto.getAnticipateResidual());
		query.setParameter(8, jsaHazardsPressurizedDto.getSecureAllHoses());

		query.executeUpdate();
	}

}
