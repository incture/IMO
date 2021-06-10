package com.incture.iopptw.template.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsExcavationdDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsExcavationTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsExcavation(String tmpId, JsaHazardsExcavationdDto jsaHazardsExcavationdDto) {
		try {
			logger.info("JsaHazardsExcavationdDto: " + jsaHazardsExcavationdDto);
			String sql = "INSERT INTO \"IOP\".\"TMPJSAHAZARDSEXCAVATION\" VALUES (?,?,?,?,?,?,?)";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, null);
			query.setParameter(2, jsaHazardsExcavationdDto.getExcavations());
			query.setParameter(3, jsaHazardsExcavationdDto.getHaveExcavationPlan());
			query.setParameter(4, jsaHazardsExcavationdDto.getLocatePipesByHandDigging());
			query.setParameter(5, jsaHazardsExcavationdDto.getDeEnergizeUnderground());
			query.setParameter(6, jsaHazardsExcavationdDto.getCseControls());
			query.setParameter(7, tmpId);
			logger.info("sql " + sql);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	public JsaHazardsExcavationdDto getJsaHazardsExcavationdDto(Integer id) {
		List<Object[]> obj;
		JsaHazardsExcavationdDto jsaHazardsExcavationdDto = new JsaHazardsExcavationdDto();
		try {
			String sql = "select distinct PERMITNUMBER, EXCAVATIONS,HAVEEXCAVATIONPLAN,LOCATEPIPESBYHANDDIGGING, "
					+ " DEENERGIZEUNDERGROUND,CSECONTROLS from IOP.TMPJSAHAZARDSEXCAVATION "
					+ " where TMPID = :id";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("id", id);
			obj = q.getResultList();

			for (Object[] a : obj) {
				jsaHazardsExcavationdDto.setPermitNumber((Integer) a[0]);
				jsaHazardsExcavationdDto.setExcavations(Integer.parseInt(a[1].toString()));
				jsaHazardsExcavationdDto.setHaveExcavationPlan(Integer.parseInt(a[2].toString()));
				jsaHazardsExcavationdDto.setLocatePipesByHandDigging(Integer.parseInt(a[3].toString()));
				jsaHazardsExcavationdDto.setDeEnergizeUnderground(Integer.parseInt(a[4].toString()));
				jsaHazardsExcavationdDto.setCseControls(Integer.parseInt(a[5].toString()));
			}
			return jsaHazardsExcavationdDto;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
