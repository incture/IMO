package com.incture.ptw.dao;

import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.incture.ptw.dto.JsaHazardsDroppedDto;

@Repository
public class JsaHazardsDroppedDao extends BaseDao {
	public void insertJsaHazardsDropped(String permitNumber, JsaHazardsDroppedDto jsaHazardsDroppedDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSDROPPED\" VALUES (?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsDroppedDto.getDroppedObjects());
			query.setParameter(3, jsaHazardsDroppedDto.getMarkRestrictEntry());
			query.setParameter(4, jsaHazardsDroppedDto.getUseLiftingEquipmentToRaise());
			query.setParameter(5, jsaHazardsDroppedDto.getSecureTools());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
