package com.incture.iopptw.template.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsDroppedDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsDroppedTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsDroppedTemplate(String id, JsaHazardsDroppedDto jsaHazardsDroppedDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"TMPJSAHAZARDSDROPPED\" VALUES (?,?,?,?,?,?)";
			logger.info(sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, id);
			query.setParameter(2, jsaHazardsDroppedDto.getDroppedObjects());
			query.setParameter(3, jsaHazardsDroppedDto.getMarkRestrictEntry());
			query.setParameter(4, jsaHazardsDroppedDto.getUseLiftingEquipmentToRaise());
			query.setParameter(5, jsaHazardsDroppedDto.getSecureTools());
			query.setParameter(6, id);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public JsaHazardsDroppedDto getJsaHazardsDroppedDto(Integer id) {
		List<Object[]> obj;
		JsaHazardsDroppedDto jsaHazardsDroppedDto = new JsaHazardsDroppedDto();
		try {
			String sql = "select distinct PERMITNUMBER, DROPPEDOBJECTS,MARKRESTRICTENTRY,USELIFTINGEQUIPMENTTORAISE, "
					+ " SECURETOOLS from IOP.TMPJSAHAZARDSDROPPED where TMPID = :id";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("id", id);
			obj = q.getResultList();

			for (Object[] a : obj) {
				jsaHazardsDroppedDto.setPermitNumber((Integer) a[0]);
				jsaHazardsDroppedDto.setDroppedObjects(Integer.parseInt(a[1].toString()));
				jsaHazardsDroppedDto.setMarkRestrictEntry(Integer.parseInt(a[2].toString()));
				jsaHazardsDroppedDto.setUseLiftingEquipmentToRaise(Integer.parseInt(a[3].toString()));
				jsaHazardsDroppedDto.setSecureTools(Integer.parseInt(a[4].toString()));
			}
			return jsaHazardsDroppedDto;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	
}
