package com.incture.ptw.dao;

import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.incture.ptw.dto.JsaHazardsLiftingDto;

@Repository
public class JsaHazardsLiftingDao extends BaseDao {
	public void insertJsaHazardsLifting(String permitNumber, JsaHazardsLiftingDto jsaHazardsLiftingDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSLIFTING\" VALUES (?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsLiftingDto.getLiftingEquipment());
			query.setParameter(3, jsaHazardsLiftingDto.getConfirmEquipmentCondition());
			query.setParameter(4, jsaHazardsLiftingDto.getObtainApprovalForLifts());
			query.setParameter(5, jsaHazardsLiftingDto.getHaveDocumentedLiftPlan());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
