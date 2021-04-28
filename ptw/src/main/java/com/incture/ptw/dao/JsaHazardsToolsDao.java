package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsToolsDto;

@Repository
public class JsaHazardsToolsDao extends BaseDao {
	public void insertJsaHazardsTools(String permitNumber, JsaHazardsToolsDto jsaHazardsToolsDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSTOOLS\" VALUES (?,?,?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsToolsDto.getEquipmentAndTools());
			query.setParameter(3, jsaHazardsToolsDto.getInspectEquipmentTool());
			query.setParameter(4, jsaHazardsToolsDto.getBrassToolsNecessary());
			query.setParameter(5, jsaHazardsToolsDto.getUseProtectiveGuards());
			query.setParameter(6, jsaHazardsToolsDto.getUseCorrectTools());
			query.setParameter(7, jsaHazardsToolsDto.getCheckForSharpEdges());
			query.setParameter(8, jsaHazardsToolsDto.getApplyHandSafetyPrinciple());
			query.executeUpdate();
			logger.info("jsaHazardsToolsDto"+jsaHazardsToolsDto);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsToolsDto getJsaHazardsToolsDto(String permitNum){
		JsaHazardsToolsDto jsaHazardsToolsDto = new JsaHazardsToolsDto();
		List<Object[]> obj;
		try{
			String sql = "select distinct PERMITNUMBER, EQUIPMENTANDTOOLS,INSPECTEQUIPMENTTOOL, "
					+ " BRASSTOOLSNECESSARY,USEPROTECTIVEGUARDS,USECORRECTTOOLS,CHECKFORSHARPEDGES, "
					+ " APPLYHANDSAFETYPRINCIPLE from IOP.JSAHAZARDSTOOLS where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardsToolsDto.setPermitNumber((Integer) a[0]);
				jsaHazardsToolsDto.setEquipmentAndTools(Integer.parseInt(a[1].toString()));
				jsaHazardsToolsDto.setInspectEquipmentTool(Integer.parseInt(a[2].toString()));
				jsaHazardsToolsDto.setBrassToolsNecessary(Integer.parseInt(a[3].toString()));
				jsaHazardsToolsDto.setUseProtectiveGuards(Integer.parseInt(a[4].toString()));
				jsaHazardsToolsDto.setUseCorrectTools(Integer.parseInt(a[5].toString()));
				jsaHazardsToolsDto.setCheckForSharpEdges(Integer.parseInt(a[6].toString()));
				jsaHazardsToolsDto.setApplyHandSafetyPrinciple(Integer.parseInt(a[7].toString()));
			}
			return jsaHazardsToolsDto;
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
