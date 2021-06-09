package com.incture.iopptw.template.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsSimultaneousDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsSimultaneousTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsSimultaneousTemplate(String id, JsaHazardsSimultaneousDto jsaHazardsSimultaneousDto) {
		try {
			String sql = "INSERT INTO IOP.TMPJSAHAZARDSSIMULTANEOUS VALUES (?,?,?,?,?,?,?,?)";
			logger.info(sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, null);
			query.setParameter(2, jsaHazardsSimultaneousDto.getSimultaneousOperations());
			query.setParameter(3, jsaHazardsSimultaneousDto.getFollowSimopsMatrix());
			query.setParameter(4, jsaHazardsSimultaneousDto.getMocRequiredFor());
			query.setParameter(5, jsaHazardsSimultaneousDto.getInterfaceBetweenGroups());
			query.setParameter(6, jsaHazardsSimultaneousDto.getUseBarriersAnd());
			query.setParameter(7, jsaHazardsSimultaneousDto.getHavePermitSigned());
			query.setParameter(8, id);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsSimultaneousDto getJsaHazardsSimultan(Integer id) {
		List<Object[]> obj;
		JsaHazardsSimultaneousDto jsaHazardsSimultaneousDto = new JsaHazardsSimultaneousDto();
		try {
			String sql = "select distinct PERMITNUMBER, SIMULTANEOUSOPERATIONS,FOLLOWSIMOPSMATRIX, "
					+ " MOCREQUIREDFOR,INTERFACEBETWEENGROUPS,USEBARRIERSAND,HAVEPERMITSIGNED "
					+ " from IOP.TMPJSAHAZARDSSIMULTANEOUS where TMPID = :id";
			logger.info("JSAHAZARDSSIMULTANEOUS sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("id", id);
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
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
