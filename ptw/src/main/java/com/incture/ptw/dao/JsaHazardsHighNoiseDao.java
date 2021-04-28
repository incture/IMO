package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsHighNoiseDto;

@Repository
public class JsaHazardsHighNoiseDao extends BaseDao{
	public void insertJsaHazardsHighNoise(String permitNumber, JsaHazardsHighNoiseDto jsaHazardsHighNoiseDto){
		try{
			String sql = "INSERT INTO IOP.JSAHAZARDSHIGHNOISE VALUES (?,?,?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsHighNoiseDto.getHighNoise());
			query.setParameter(3, jsaHazardsHighNoiseDto.getWearCorrectHearing());
			query.setParameter(4, jsaHazardsHighNoiseDto.getManageExposureTimes());
			query.setParameter(5, jsaHazardsHighNoiseDto.getShutDownEquipment());
			query.setParameter(6, jsaHazardsHighNoiseDto.getUseQuietTools());
			query.setParameter(7, jsaHazardsHighNoiseDto.getSoundBarriers());
			query.setParameter(8, jsaHazardsHighNoiseDto.getProvideSuitableComms());
			query.executeUpdate();
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsHighNoiseDto getJsaHazardsHighNoiseDto(String permitNum){
		List<Object[]> obj;
		JsaHazardsHighNoiseDto jsaHazardsHighNoiseDto = new JsaHazardsHighNoiseDto();
		try{
			String sql = "select distinct PERMITNUMBER, HIGHNOISE,WEARCORRECTHEARING,MANAGEEXPOSURETIMES, "
					+ " SHUTDOWNEQUIPMENT,USEQUIETTOOLS,SOUNDBARRIERS,PROVIDESUITABLECOMMS "
					+ " from IOP.JSAHAZARDSHIGHNOISE where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardsHighNoiseDto.setPermitNumber((Integer) a[0]);
				jsaHazardsHighNoiseDto.setHighNoise(Integer.parseInt(a[1].toString()));
				jsaHazardsHighNoiseDto.setWearCorrectHearing(Integer.parseInt(a[2].toString()));
				jsaHazardsHighNoiseDto.setManageExposureTimes(Integer.parseInt(a[3].toString()));
				jsaHazardsHighNoiseDto.setShutDownEquipment(Integer.parseInt(a[4].toString()));
				jsaHazardsHighNoiseDto.setUseQuietTools(Integer.parseInt(a[5].toString()));
				jsaHazardsHighNoiseDto.setSoundBarriers(Integer.parseInt(a[6].toString()));
				jsaHazardsHighNoiseDto.setProvideSuitableComms(Integer.parseInt(a[7].toString()));
			}
			return jsaHazardsHighNoiseDto;
			
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
