package com.incture.iopptw.template.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsIgnitionDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsIgnitionTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsIgnitionTemplate(String id, JsaHazardsIgnitionDto jsaHazardsIgnitionDto) {
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			String sql = "INSERT INTO IOP.TMPJSAHAZARDSIGNITION VALUES (?,?,?,?,?,?,?,?)";
			logger.info(sql);
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, null);
			query.setParameter(2, jsaHazardsIgnitionDto.getIgnitionSources());
			query.setParameter(3, jsaHazardsIgnitionDto.getRemoveCombustibleMaterials());
			query.setParameter(4, jsaHazardsIgnitionDto.getProvideFireWatch());
			query.setParameter(5, jsaHazardsIgnitionDto.getImplementAbrasiveBlastingControls());
			query.setParameter(6, jsaHazardsIgnitionDto.getConductContinuousGasTesting());
			query.setParameter(7, jsaHazardsIgnitionDto.getEarthForStaticElectricity());
			query.setParameter(8, id);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsIgnitionDto getJsaHazardsIgnition(Integer id) {
		List<Object[]> obj;
		JsaHazardsIgnitionDto jsaHazardsIgnitionDto = new JsaHazardsIgnitionDto();
		try {
			String sql = "select distinct PERMITNUMBER,IGNITIONSOURCES,REMOVECOMBUSTIBLEMATERIALS,PROVIDEFIREWATCH, "
					+ " IMPLEMENTABRASIVEBLASTINGCONTROLS,CONDUCTCONTINUOUSGASTESTING,EARTHFORSTATICELECTRICITY "
					+ " from IOP.TMPJSAHAZARDSIGNITION where TMPID = :id";
			logger.info("JSAHAZARDSIGNITION sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("id", id);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardsIgnitionDto.setPermitNumber((Integer) a[0]);
				jsaHazardsIgnitionDto.setIgnitionSources(Integer.parseInt(a[1].toString()));
				jsaHazardsIgnitionDto.setRemoveCombustibleMaterials(Integer.parseInt(a[2].toString()));
				jsaHazardsIgnitionDto.setProvideFireWatch(Integer.parseInt(a[3].toString()));
				jsaHazardsIgnitionDto.setImplementAbrasiveBlastingControls(Integer.parseInt(a[4].toString()));
				jsaHazardsIgnitionDto.setConductContinuousGasTesting(Integer.parseInt(a[5].toString()));
				jsaHazardsIgnitionDto.setEarthForStaticElectricity(Integer.parseInt(a[6].toString()));
			}
			return jsaHazardsIgnitionDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
