package com.incture.iopptw.template.repositories;

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

}
