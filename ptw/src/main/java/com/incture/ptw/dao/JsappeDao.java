package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsappeDto;

@Repository
public class JsappeDao extends BaseDao {
	public void insertJsappe(String permitNumber, JsappeDto jsappeDto) {
		logger.info("jsappeDto"+jsappeDto);
		try {
			String sql ="INSERT INTO \"IOP\".\"JSA_PPE\" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsappeDto.getHardHat());
			query.setParameter(3, jsappeDto.getSafetyBoot());
			query.setParameter(4, jsappeDto.getGoggles());
			query.setParameter(5, jsappeDto.getFaceShield());
			query.setParameter(6, jsappeDto.getSafetyGlasses());
			query.setParameter(7, jsappeDto.getSingleEar());
			query.setParameter(8, jsappeDto.getDoubleEars());
			query.setParameter(9, jsappeDto.getRespiratorTypeDescription());
			query.setParameter(10, jsappeDto.getNeedSCBA());
			query.setParameter(11, jsappeDto.getNeedDustMask());
			query.setParameter(12, jsappeDto.getCottonGlove());
			query.setParameter(13, jsappeDto.getLeatherGlove());
			query.setParameter(14, jsappeDto.getImpactProtection());
			query.setParameter(15, jsappeDto.getGloveDescription());
			query.setParameter(16, jsappeDto.getChemicalGloveDescription());
			query.setParameter(17, jsappeDto.getFallProtection());
			query.setParameter(18, jsappeDto.getFallRestraint());
			query.setParameter(19, jsappeDto.getChemicalSuit());
			query.setParameter(20, jsappeDto.getApron());
			query.setParameter(21, jsappeDto.getFlameResistantClothing());
			query.setParameter(22, jsappeDto.getOtherPPEDescription());
			query.setParameter(23, jsappeDto.getNeedFoulWeatherGear());
			query.setParameter(24, jsappeDto.getHaveConsentOfTaskLeader());
			query.setParameter(25, jsappeDto.getCompanyOfTaskLeader());
			query.executeUpdate();
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
