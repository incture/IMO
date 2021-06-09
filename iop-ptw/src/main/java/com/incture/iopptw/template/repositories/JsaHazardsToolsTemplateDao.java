package com.incture.iopptw.template.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsToolsDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsToolsTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsTools(String id, JsaHazardsToolsDto jsaHazardsToolsDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"TMPJSAHAZARDSTOOLS\" VALUES (?,?,?,?,?,?,?,?,?)";
			logger.info(sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, null);
			query.setParameter(2, jsaHazardsToolsDto.getEquipmentAndTools());
			query.setParameter(3, jsaHazardsToolsDto.getInspectEquipmentTool());
			query.setParameter(4, jsaHazardsToolsDto.getBrassToolsNecessary());
			query.setParameter(5, jsaHazardsToolsDto.getUseProtectiveGuards());
			query.setParameter(6, jsaHazardsToolsDto.getUseCorrectTools());
			query.setParameter(7, jsaHazardsToolsDto.getCheckForSharpEdges());
			query.setParameter(8, jsaHazardsToolsDto.getApplyHandSafetyPrinciple());
			query.setParameter(9, id);
			query.executeUpdate();
			tx.commit();
			session.close();
			logger.info("jsaHazardsToolsDto" + jsaHazardsToolsDto);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public JsaHazardsToolsDto getJsaHazardsToolsDto(Integer id) {
		JsaHazardsToolsDto jsaHazardsToolsDto = new JsaHazardsToolsDto();
		List<Object[]> obj;
		try {
			String sql = "select distinct PERMITNUMBER, EQUIPMENTANDTOOLS,INSPECTEQUIPMENTTOOL, "
					+ " BRASSTOOLSNECESSARY,USEPROTECTIVEGUARDS,USECORRECTTOOLS,CHECKFORSHARPEDGES, "
					+ " APPLYHANDSAFETYPRINCIPLE from IOP.TMPJSAHAZARDSTOOLS where TMPID = :id";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("id", id);
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
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
