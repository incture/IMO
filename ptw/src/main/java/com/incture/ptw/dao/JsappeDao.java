package com.incture.ptw.dao;

import javax.persistence.Query;
import com.incture.ptw.dto.JsappeDto;

public class JsappeDao extends BaseDao {
	private void insertJsappe(JsappeDto jsappeDto){
		Query query = getSession().createNativeQuery("INSERT INTO \"IOP\".\"JSA_PPE\" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		query.setParameter(1, jsappeDto.getPermitNumber());
		query.setParameter(2, jsappeDto.getHardHat());
		query.setParameter(3, jsappeDto.getSafetyBoot());
		query.setParameter(4, jsappeDto.getGoggles());
		query.setParameter(5, jsappeDto.getFaceShield());
		query.setParameter(6, jsappeDto.getSafetyGlasses());
		query.setParameter(7, jsappeDto.getSingleEar());
		query.setParameter(8, jsappeDto.getDoubleEars());
		query.setParameter(9, jsappeDto.getRespiratorTypeDescription());
		query.setParameter(10, jsappeDto.getNeedScba());
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
		query.setParameter(22, jsappeDto.getOtherppeDescription());
		query.setParameter(23, jsappeDto.getNeedFoulWeatherGear());
		query.setParameter(24, jsappeDto.getHaveConsentOfTaskLeader());
		query.setParameter(25, jsappeDto.getCompanyOfTaskLeader());
		query.executeUpdate();		
	} 


}
