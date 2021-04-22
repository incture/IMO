package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsIgnitionDto;

@Repository
public class JsaHazardsIgnitionDao extends BaseDao {
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;

	public void insertJsaHazardsIgnition(JsaHazardsIgnitionDto jsaHazardsIgnitionDto) {
		try {
			String sql = "INSERT INTO IOP.JSAHAZARDSIGNITION VALUES (?,?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			String str = keyGeneratorDao.getPermitNumber();
			query.setParameter(1, str);
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

}
