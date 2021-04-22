package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsSubstancesDto;

@Repository
public class JsaHazardsSubstancesDao extends BaseDao {
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;

	public void insertJsaHazardsSubstances(JsaHazardsSubstancesDto jsaHazardsSubstancesDto) {
		try {
			String sql = "INSERT INTO IOP.JSAHAZARDSSUBSTANCES VALUES (?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			String str = keyGeneratorDao.getPermitNumber();
			query.setParameter(1, str);
			query.setParameter(2, jsaHazardsSubstancesDto.getHazardousSubstances());
			query.setParameter(3, jsaHazardsSubstancesDto.getDrainEquipment());
			query.setParameter(4, jsaHazardsSubstancesDto.getFollowsDSControls());
			query.setParameter(5, jsaHazardsSubstancesDto.getImplementHealthHazardControls());
			query.setParameter(6, jsaHazardsSubstancesDto.getTestMaterial());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}
