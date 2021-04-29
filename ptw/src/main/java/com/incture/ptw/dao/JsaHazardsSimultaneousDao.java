package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsSimultaneousDto;

@Repository
public class JsaHazardsSimultaneousDao extends BaseDao {

	public void insertJsaHazardsSimultaneous(String permitNumber, JsaHazardsSimultaneousDto jsaHazardsSimultaneousDto) {
		try {
			String sql = "INSERT INTO IOP.JSAHAZARDSSIMULTANEOUS VALUES (?,?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsSimultaneousDto.getSimultaneousOperations());
			query.setParameter(3, jsaHazardsSimultaneousDto.getFollowSimopsMatrix());
			query.setParameter(4, jsaHazardsSimultaneousDto.getMocRequiredFor());
			query.setParameter(5, jsaHazardsSimultaneousDto.getInterfaceBetweenGroups());
			query.setParameter(6, jsaHazardsSimultaneousDto.getUseBarriersAnd());
			query.setParameter(7, jsaHazardsSimultaneousDto.getHavePermitSigned());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsSimultaneousDto getJsaHazardsSimultan(String permitNum){
		List<Object[]> obj;
		JsaHazardsSimultaneousDto jsaHazardsSimultaneousDto = new JsaHazardsSimultaneousDto();
		try{
			String sql = "select distinct PERMITNUMBER, SIMULTANEOUSOPERATIONS,FOLLOWSIMOPSMATRIX, "
					+ " MOCREQUIREDFOR,INTERFACEBETWEENGROUPS,USEBARRIERSAND,HAVEPERMITSIGNED "
					+ " from IOP.JSAHAZARDSSIMULTANEOUS where PERMITNUMBER = :permitNum";
			logger.info("JSAHAZARDSSIMULTANEOUS sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardsSimultaneousDto.setPermitNumber((Integer) a[0]);
				jsaHazardsSimultaneousDto.setSimultaneousOperations(Integer.parseInt(a[1].toString()));
				jsaHazardsSimultaneousDto.setFollowSimopsMatrix(Integer.parseInt(a[2].toString()));
				jsaHazardsSimultaneousDto.setMocRequiredFor(Integer.parseInt(a[3].toString()));
				jsaHazardsSimultaneousDto.setInterfaceBetweenGroups(Integer.parseInt(a[4].toString()));
				jsaHazardsSimultaneousDto.setUseBarriersAnd(Integer.parseInt(a[5].toString()));
				jsaHazardsSimultaneousDto.setHavePermitSigned(Integer.parseInt(a[6].toString()));
			}
			return jsaHazardsSimultaneousDto;
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void updateJsaHazardsSimultaneous(JsaHazardsSimultaneousDto jsaHazardsSimultaneousDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSSIMULTANEOUS\" SET  \"SIMULTANEOUSOPERATIONS\"=?,\"FOLLOWSIMOPSMATRIX\"=?,\"MOCREQUIREDFOR\"=?," +
        "\"INTERFACEBETWEENGROUPS\"=?,\"USEBARRIERSAND\"=?,\"HAVEPERMITSIGNED\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsaHazardsSimultaneous sql" + sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, jsaHazardsSimultaneousDto.getSimultaneousOperations());
			query.setParameter(2, jsaHazardsSimultaneousDto.getFollowSimopsMatrix());
			query.setParameter(3, jsaHazardsSimultaneousDto.getMocRequiredFor());
			query.setParameter(4, jsaHazardsSimultaneousDto.getInterfaceBetweenGroups());
			query.setParameter(5, jsaHazardsSimultaneousDto.getUseBarriersAnd());
			query.setParameter(6, jsaHazardsSimultaneousDto.getHavePermitSigned());
			query.setParameter(7, jsaHazardsSimultaneousDto.getPermitNumber());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
