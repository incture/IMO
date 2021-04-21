package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsSimultaneousDto;

@Repository
public class JsaHazardsSimultaneousDao extends BaseDao {
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;
	
	public void insertJsaHazardsSimultaneousDao(JsaHazardsSimultaneousDto jsaHazardsSimultaneousDto){
		try{
			String sql = "INSERT INTO IOP.JSAHAZARDSSIMULTANEOUS VALUES (?,?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			String str = keyGeneratorDao.getPermitNumber();
			query.setParameter(1, str);
			query.setParameter(2, jsaHazardsSimultaneousDto.getSimultaneousOperations());
			query.setParameter(3, jsaHazardsSimultaneousDto.getFollowsImopsMatrix());
			query.setParameter(4, jsaHazardsSimultaneousDto.getMocRequiredFor());
			query.setParameter(5, jsaHazardsSimultaneousDto.getInterfaceBetweenGroups());
			query.setParameter(6, jsaHazardsSimultaneousDto.getUseBarriersAnd());
			query.setParameter(7, jsaHazardsSimultaneousDto.getHavePermitSigned());
			query.executeUpdate();
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
	}
	
}
